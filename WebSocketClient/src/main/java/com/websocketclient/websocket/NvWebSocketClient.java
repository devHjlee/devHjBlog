package com.websocketclient.websocket;

import com.google.gson.*;
import com.neovisionaries.ws.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class NvWebSocketClient {
    WebSocket ws = null;

    @PostConstruct
    public void connect() throws WebSocketException, IOException, InterruptedException {

        final String SERVER = "wss://api.upbit.com/websocket/v1";
        final int TIMEOUT = 5000;

        JsonArray root = new JsonArray();
        JsonObject type = new JsonObject();
        JsonArray codesObj = new JsonArray();

        codesObj.add("KRW-BTC");
        root.add(new JsonObject());
        root.get(0).getAsJsonObject().addProperty("ticket", UUID.randomUUID().toString());
        type.addProperty("type", "ticker");
        type.add("codes", codesObj);
        root.add(type);

        ws = new WebSocketFactory()
                .setConnectionTimeout(TIMEOUT)
                .createSocket(SERVER)
                .addListener(new WebSocketAdapter() {

                    public void onBinaryMessage(WebSocket websocket, byte[] binary) {
                        log.info("NvWebSocket:"+new String(binary));

                    }

                    public void onTextMessage(WebSocket websocket, String message) {}

                    public void onDisconnected(WebSocket websocket,
                                               WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                                               boolean closedByServer) {
                    }
                    public void onError(WebSocket websocket, WebSocketException cause) {
                    }
                })
                .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
                .connect();
        ws.sendText(root.toString());
    }
}
