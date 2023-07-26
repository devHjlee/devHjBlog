import requests
import json
import feedparser
import telegram
import urllib.request

from telegram import Update
from telegram.ext import Application, ContextTypes, MessageHandler, filters

naver_id = "네이버API ID"
naver_secret = "네이버API secret"

telegram_token = "텔레그램 봇 토큰"
bot = telegram.Bot(token=telegram_token)


# Naver
def navernews(message):
    url = "https://openapi.naver.com/v1/search/news.json?query=%s&start=1&display=100" % urllib.parse.quote(message)
    req = urllib.request.Request(url)
    req.add_header("X-Naver-Client-Id", naver_id)
    req.add_header("X-Naver-Client-Secret", naver_secret)

    try:
        response = urllib.request.urlopen(req)
        if response.getcode() == 200:
            return json.loads(response.read().decode('utf-8'))
    except Exception as e:
        print(e)
        return


# Google
def googlenews(message):
    URL = 'https://news.google.com/rss/search?q={}+when:1d&hl=ko&gl=KR&ceid=KR:ko'.format(message)
    try:
        res = requests.get(URL)
        if res.status_code == 200:
            return feedparser.parse(res.text).entries
        else:
            print('Google 검색 에러')
    except requests.exceptions.RequestException as err:
        print('Error Requests: {}'.format(err))


async def echo(update: Update, context: ContextTypes.DEFAULT_TYPE) -> None:
    googleRs = googlenews(update.message.text)

    for i in range(5):
        data = googleRs[i]
        msg = data['title'] + ' / ' + data['links'][0]['href']
        bot = telegram.Bot(telegram_token)
        await bot.sendMessage(chat_id="6171495764", text=msg)

    naverRs = navernews(update.message.text)

    for i in range(5):
        data = naverRs['items'][i]
        msg = data['title'] + ' / ' + data['link']
        bot = telegram.Bot(telegram_token)
        await bot.sendMessage(chat_id="6171495764", text=msg)

    await update.message.reply_text("완료")


def main() -> None:
    application = Application.builder().token(telegram_token).build()
    application.add_handler(MessageHandler(filters.TEXT & ~filters.COMMAND, echo))
    application.run_polling(allowed_updates=Update.ALL_TYPES)


if __name__ == "__main__":
    main()
