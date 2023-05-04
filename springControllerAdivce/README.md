# 목적
Spring ControllerAdvice 사용방법

## 예제소스
### https://github.com/devHjlee/devHjBlog/tree/main/springControllerAdivce  

## ControllerAdvice  
* 스프링프레임워크에서 예외 처리 코드를 중복해서 작성하지 않고, 전체 컨트롤러에 대한 전역적인 예외처리를 정의 할 수 있다.   
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
![img0.png](img0.png)

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
*Exception 클래스를 상속받아 정의한 사용자 정의 예외 클래스    

``` java    
public class MyCustomException extends Exception {
    
    public MyCustomException(String message) {
        super(message);
    }
}
```   

#### ApiResponse, ErrorResponse
* 요청한 API 대한 성공, 실패에 따라 클라이언트에 응답하기 위한 객체 

``` java    
public class ApiResponse {
    private String status;
    private String message;

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}

public class ErrorResponse {
    private final ErrorCode errorCode;
    private final String errorMessage;

    public ErrorResponse(ErrorCode errorCode, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = status.getReasonPhrase();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
```   

#### ErrorCode
* 에러코드 정의   

``` java    
public enum ErrorCode {
    ERROR_001("An unexpected error occurred."),
    ERROR_002("A custom error occurred."),
    // 추가로 필요한 에러코드를 여기에 선언합니다.
    ;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
```   

* 테스트 결과
``` java   
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hello")
public class HelloController {
    private final HelloService helloService;

    @GetMapping("/v1/board")
    public ResponseEntity<ApiResponse> helloBoard() {
        return new ResponseEntity<>(new ApiResponse("200","Success"), HttpStatus.OK);
    }

    @GetMapping("/v1/calc")
    public ResponseEntity<ApiResponse> helloCalc() {
        helloService.helloCalc();
        return new ResponseEntity<>(new ApiResponse("200","Success"), HttpStatus.OK);
    }

    @GetMapping("/v1/custom")
    public ResponseEntity<ApiResponse> helloCustom() throws MyCustomException{
        throw new MyCustomException("강제 에러발생");
    }
}


@Service
public class HelloService {

    public void helloBoard() {
      String text = "HELLO";
      System.out.println(text);
    }

    public void helloCalc() {
        int a = 0/0;
    }

}
```   

* Serivce 에서 0/0을 통해 ArithmeticException 강제로 발생시켜서 @ExceptionHandler(Exception.class) 으로 전달

  ![img1.png](img1.png)   


* MyCustomException 강제로 발생시켜  @ExceptionHandler(MyCustomException.class) 으로 전달
  ![img2.png](img2.png)   

### 참고자료
* 위에 소스들을 직접 작성하다가 ChatGPT 가 생각나서 ChatGPT를 통해 작성한 소스들이다. 잘만 활용하면 역시 편하고 좋다.
