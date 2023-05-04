# 목적
Spring ControllerAdvice 사용방법

## 예제소스
### https://github.com/devHjlee/devHjBlog/tree/main/springControllerAdivce  

## ControllerAdvice  
* 스프링프레임워크에서 전체 컨트롤러에 대한 전역적인 예외처리를 정의 할 수 있다.   
* 컨트롤러 메소드에서 예외가 발생하면 ControllerAdvice가 이를 가로채고 정의된 예외 처리 로직을 적용한다.     

## RestControllerAdvice  
* ControllerAdvice 와 비슷한 기능을 제공하지만 반환되는 값의 형식이 다르다.
* ControllerAdvice 는 ModelAndView 객체를 반환하여 뷰를 랜더링 할 수 있지만, RestControllerAdvice는 JSON 형식의 응답을 반환 하기 위해 ReponseEntity나 ReponseBody 를 사용한다.

## 개발환경
* IDE : IntelliJ
* Jdk : OpenJdk 11
* gradle
* spring boot : 2.7.9   

## 프로젝트 구조   
![img1.png](img1.png)

### 예제 소스
프로젝트를 진행하다보면 ControllerAdvice 보다 RestControllerAdvice 를 주로 사용하여 RestControllerAdvice 로 예시를 작성.   

#### CustomExceptionHandler   
``` java    
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.ERROR_001, HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MyCustomException.class)
    public ResponseEntity<ErrorResponse> handleMyCustomException(MyCustomException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.ERROR_002, HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
```   

#### MyCustomException   

``` java    
public class MyCustomException extends Exception {
    
    public MyCustomException(String message) {
        super(message);
    }
}
```   

#### AlarmEventListener
* Spring 4.2 이전까지는 ApplicationListener를 상속받아야 했지만 Spring 4.2부터는 @EventListener 애노테이션 기반으로 이벤트를 처리할 수 있다.   

``` java    
@Slf4j
@Component
public class AlarmEventListener {
    @EventListener
    public void sendTelegram(AlarmEvent event) {
        log.info(String.format("텔레그램 발송[수신자 : %s][내용 : %s]", event.getUsrId(), event.getMsg()));
    }

    @EventListener
    public void sendMail(AlarmEvent event) {
        log.info(String.format("EMAIL 발송[수신자 : %s][내용 : %s]", event.getUsrId(), event.getMsg()));
    }
}
```   
* 테스트 결과
  ![img0.png](img0.png)

``` java   
@Slf4j
@Component
public class AlarmEventListener {
    @EventListener
    @Order(2)
    public void sendTelegram(AlarmEvent event) {
        log.info(String.format("텔레그램 발송[수신자 : %s][내용 : %s]", event.getUsrId(), event.getMsg()));
    }

    @EventListener
    @Order(1)
    public void sendMail(AlarmEvent event) {
        log.info(String.format("EMAIL 발송[수신자 : %s][내용 : %s]", event.getUsrId(), event.getMsg()));
    }
}
```   
* Order 테스트 결과   

![img2.png](img2.png)   


#### Test Code
```java   
@SpringBootTest
class CoinTradeServiceTest {
  @Autowired
  CoinTradeService coinTradeService;
  @Test
  void coinTrade() {
    coinTradeService.coinTrade();
  }
}
```   
### 비동기를 위한 @EnableAsync / @Async   
#### SpringEventPublisherApplication   
```java    
@SpringBootApplication
@EnableAsync // 비동기 Event를 위한 설정
public class SpringEventPublisherApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringEventPublisherApplication.class, args);
    }

}
```   
#### AlarmEventListener 수정
```java    
@Slf4j
@Component
public class AlarmEventListener {
    @EventListener
    @Async
    @Order(1)
    public void sendTelegram(AlarmEvent event) {
        log.info(String.format("텔레그램 발송[수신자 : %s][내용 : %s]", event.getUsrId(), event.getMsg()));
    }

    @EventListener
    @Async
    @Order(2)
    public void sendMail(AlarmEvent event) {
        log.info(String.format("EMAIL 발송[수신자 : %s][내용 : %s]", event.getUsrId(), event.getMsg()));
    }
}
```    
* 예를들어 이벤트인 메시지 발송이 오래 걸리는 상황에서 비동기 처리시 메인 로직은 이벤트의 응답을 기다리지 않아도 된다.   

![img3.png](img3.png)

### @TransactionalEventListener    
* 이벤트 발행자의 트랜잭션을 기준으로 이벤트 실행시점을 조절할때 사용   
  * BEFORE_COMMIT : 발행자의 트랜잭션이 커밋되기 직전에 이벤트를 발생   
  * AFTER_COMMIT : 발행자의 트랜잭션이 커밋된 후 이벤트 발생(Default)   
  * AFTER_ROLLBACK : 발행자의 트랜잭션이 롤백된 후 이벤트 발생   
  * AFTER_COMPLETION : 발행자의 트랜잭션 성공여부와 상관없이 끝나면 발생   


* 위에 사용하던 소스에서 CoinTradeService 와 AlarmEventListener 단순한 테스트를 위해 Save 로직추가    

```java    
@Service
@RequiredArgsConstructor
@Transactional
public class CoinTradeService {
    final private TradeHistoryRepository tradeHistoryRepository;
    final private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void coinTrade(){
        log.info("Coin 자동구매 로직 실행");
        TradeHistory th = new TradeHistory();
        th.setCoin("BTC");
        th.setPrice(10000L);
        tradeHistoryRepository.save(th);
        log.info("Coin 자동구매 로직 종료");
        //Event 발생
        applicationEventPublisher.publishEvent(new AlarmEvent("USER1","BTC구매"));
        log.info("Coin 자동구매 종료");

        //throw new RuntimeException();
    }
}
```    

* 기존 사용하던 @EventListener 에서 @TransactionEventListener 로 변경하고 이벤트(카카오,슬랙) 추가   

```java    
@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmEventListener {
    private final AlarmHistoryRepository alarmHistoryRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void sendSlack(AlarmEvent event) {
        AlarmHistory ah = new AlarmHistory();
        ah.setSendType("Slack");
        alarmHistoryRepository.save(ah);
        log.info(String.format("슬랙 발송[수신자 : %s][내용 : %s]", event.getUsrId(), event.getMsg()));
        //throw new RuntimeException();
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void sendTelegram(AlarmEvent event) {
        AlarmHistory ah = new AlarmHistory();
        ah.setSendType("Telegram");
        alarmHistoryRepository.save(ah);
        log.info(String.format("Telegram 발송[수신자 : %s][내용 : %s]", event.getUsrId(), event.getMsg()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void sendKakao(AlarmEvent event) {
        AlarmHistory ah = new AlarmHistory();
        ah.setSendType("Kakao");
        alarmHistoryRepository.save(ah);
        log.info(String.format("Kakao 발송[수신자 : %s][내용 : %s]", event.getUsrId(), event.getMsg()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void sendMail(AlarmEvent event) {
        AlarmHistory ah = new AlarmHistory();
        ah.setSendType("Email");
        alarmHistoryRepository.save(ah);
        log.info(String.format("EMAIL 발송[수신자 : %s][내용 : %s]", event.getUsrId(), event.getMsg()));
    }
}
```   

예시 : 정상적인 흐름일때에는 아래와 같다.   
* 위에 소스에서 텔레그램은 BEFORE_COMMIT 으로 선언되어 있어서 발행자와 같은 트랜잭션으로 묶인다.   
* 슬랙은 AFTER_COMMIT 으로 선언되어 있고 발행자의 트랜잭션이 커밋된 후 REQUIRES_NEW 를 통해 새 트랜잭션을 생성하고 Insert를 진행한다.   
* 이메일은 AFTER_COMPLETION이므로 발행자의 트랜잭션 커밋 후 성공여부와 상관없이 REQUIRES_NEW 를 통해 새 트랜잭션을 생성하고 Insert를 진행한다.   
* 카카오는 AFTER_ROLLBACK 으로 발행자의 트랜잭션이 롤백될 때에만 실행되게 설정했다.   
![img4.png](img4.png)     
      
예시 : 발행자쪽 RuntimeException 발생시는 아래와 같다.
* 텔레그램은 발행자와 같은 트랜잭션에 있으므로 롤백
* 슬랙은 발행자의 롤백으로 인해 실행이 되지 않는다.
* 카카오는 발행자의 롤백으로 인해 실행된다.
* 이메일은 발행자의 트랜잭션 커밋,롤백 후 진행되므로 실행된다.
  ![img6.png](img6.png)   
### @Transactional(Transactional.TxType.REQUIRES_NEW)   
예시 : 정상적인 흐름이나 슬랙이벤트에 REQUIRES_NEW 주석처리   
![img5.png](img5.png)   

## Spring Event를 통해 위와 같이 비동기 처리를하거나 메인로직과 이벤트의 트랜잭션을 묶어 처리하거나 분리하여 처리할 수 있다.

### 참고자료
https://www.baeldung.com/spring-events    
https://velog.io/@znftm97/%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EA%B8%B0%EB%B0%98-%EC%84%9C%EB%B9%84%EC%8A%A4%EA%B0%84-%EA%B0%95%EA%B2%B0%ED%95%A9-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0%ED%95%98%EA%B8%B0-ApplicationEventPublisher#span-stylecolor0b6e994-transactionaleventlistener-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0span