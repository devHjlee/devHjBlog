# 목적
* 신규 프로젝트에서 사용 되었던 JPA, QueryDsl 을 정리
* Entity 연관관계와 Fetch 에 대해 간단한 게시판 구현을 통해 정리
* 
  
## 목차
  
  [0.Quartz란?](#Quartz란?)

## JPA 란?
Job Scheduling 라이브러리 이며 자바로 개발되어 모든 자바 프로그램에서 사용 가능하고
간단한 interval형식이나 Cron 표현식 스케줄링 지원
* 장점
  * DB 기반의 클러스터 기능 제공
  * 시스템 Fail-over / Random 방식의 로드 분산처리 지원
  * In-memory Job scheduler 제공
  * 여러 기본 플러그인 제공
    * ShutdownHookPlugin – JVM 종료 이벤트 캐치
    * LoggingJobHistoryPlugin – Job 실행 로그 남기기

* 단점
  * Random 방식 클러스터링 기능이라 완벽한 로드 분산 안됨
  * 스케줄링 실행에 대한 히스토리 보관에 대한 개발 필요

* Quartz 흐름
  ![img_1.png](img_1.png)
  출처 : https://www.javarticles.com/2016/03/quartz-scheduler-model.html#prettyPhoto

## QueryDsl 이란?




## 개발환경
* IDE : IntelliJ
* Jdk : OpenJdk 1.8
* DB : Mysql 8.0
* gradle
* spring boot : 2.7.8
  - spring-boot-starter-data-jpa
  - com.querydsl jpa, apt
  - mysql-connector-j


## 프로젝트 구조

이미지 넣어라


### 1. JPA, QueryDsl 설정
#### 1) build.gradle
* JPA 의존성 추가
* querydsl 플러그인 및 의존성 추가

```groovy
buildscript {
  ext {
    queryDslVersion = "5.0.0"
  }
}

plugins {
  id 'java'
  id 'org.springframework.boot' version '2.7.8'
  id 'io.spring.dependency-management' version '1.0.15.RELEASE'
  id 'com.ewerk.gradle.plugins.querydsl' version '1.0.10'
}

group = 'com.springDataJpaCrud'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '1.8'

configurations {
  compileOnly {
    extendsFrom annotationProcessor
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
  implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
  implementation 'org.springframework.boot:spring-boot-starter-web'

  implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.1'

  //querydsl 추가
  implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"
  implementation "com.querydsl:querydsl-apt:${queryDslVersion}"

  runtimeOnly 'com.mysql:mysql-connector-j'
  compileOnly 'org.projectlombok:lombok'
  developmentOnly 'org.springframework.boot:spring-boot-devtools'
  annotationProcessor 'org.projectlombok:lombok'
  testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

test {
  useJUnitPlatform()
}

//querydsl 추가 시작
def querydslDir = "$buildDir/generated/querydsl"

querydsl {
  jpa = true
  querydslSourcesDir = querydslDir
}
sourceSets {
  main.java.srcDir querydslDir
}
compileQuerydsl{
  options.annotationProcessorPath = configurations.querydsl
}

configurations {
  compileOnly {
    extendsFrom annotationProcessor
  }
  querydsl.extendsFrom compileClasspath
}
```

* Unable to load class 'com.querydsl.apt.jpa.JPAAnnotationProcessor'
  * http://honeymon.io/tech/2020/07/09/gradle-annotation-processor-with-querydsl.html
```groovy
    configurations {
      compileOnly {
        extendsFrom annotationProcessor
      }
      querydsl.extendsFrom compileClasspath
    }
```
![image1_error.png](image1_error.png)
* Unable to load class 'com.mysema.codegen.model.Type'
  * 최초 설정 시 버전을 명시 하지 않았을때 5.0.0 버전으로 되는걸 확인 했으나 위에 오류가 발생하여 버전을 명시하여 수정
```groovy
    // 변경전
    implementation 'com.querydsl:querydsl-jpa'
    implementation 'com.querydsl:querydsl-apt'
    // 변경 후
    // 버전 명시 추가
    buildscript {
        ext {
        queryDslVersion = "5.0.0"
        }
    }
    //dependency 버전 명시
    implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"
    implementation "com.querydsl:querydsl-apt:${queryDslVersion}"
 ```
![image2_error.png](image2_error.png)

#### 2) Q타입 확인용 Test Entity, Test Code 생성
* Gradle console : ./gradlew clean comlieQuerydsl

 ``` java
  @Entity
  @Getter
  @Setter
  public class QTypeTestEntity {
      @Id @GeneratedValue
      private Long id;
  
  }
 ```
![image3.png](image3.png)

 ``` java
    @Test
    public void testQueryDsl() {
        QTypeTestEntity qType = new QTypeTestEntity();
        em.persist(qType);

        JPAQueryFactory query = new JPAQueryFactory(em);
        QQTypeTestEntity qTyeTest = QQTypeTestEntity.qTypeTestEntity;

        QTypeTestEntity result = query.selectFrom(qTyeTest)
                .fetchOne();

        assertThat(result).isEqualTo(qType);
        assertThat(result.getId()).isEqualTo(qType.getId());
    }
 ```

### 2. Spring Data JPA 로 구현
  * QueryDsl 을 적용하기 전에 Spring Data JPA 를 이용하여 연관관계, Fetch 에 대한 설명
#### 1) Post, User Entity 
  * @NoArgsConstructor(access = AccessLevel.PROTECTED)
    * 기본생성자의 접근 제어를 PROTECTED 설정함으로써 무분별한 객체 생성을 막음 (ex : User user = new User)
  * @Setter 지양(절대 사용금지는 아니다)
    * Setter 는 그 의도 파악과 객체를 변경 할 수 있는 상태가 되어 안전성을 보장받기 힘들다.
    * JPA 에서 Setter는 곧 Update 쿼리를 의미하기에 변경이 필요하면 의미있는 메소드를 생성해서 변경하는것이 좋다.

 ```java
  @Entity
  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public class User {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Long id;
  
    private String email;
    private String userName;
    private String password;
  
    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();
  
    @Builder
    public User(String email, String userName, String password,List<Post> posts){
      this.email = email;
      this.userName = userName;
      this.password = password;
      this.posts = posts;
    }
  }
 ```

 ```java
  @Entity
  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public class Post {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
  
    @Column(nullable = false)
    private String title;
  
    @Column(nullable = false,columnDefinition = "TEXT")
    private String content;
  
    @Column(length = 1)
    private String deleteYn = "N";
  
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_no")
    private User user;
  
    @Builder
    public Post(String title, String content, User user){
      this.title = title;
      this.content = content;
      this.user = user;
    }
  
    /* 비지니스로직 */
    /* 게시글 수정 */
    public void updatePost(String title, String content) {
      this.title = title;
      this.content = content;
    }
    /* 게시글 삭제 */
    public void deletePost() {
      this.deleteYn = "Y";
    }
  }
 ```

#### 2) Post, User Entity(변경후)
 ```java
  @Entity
  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public class User {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Long id;
  
    @Nullable
    private String email;
  
    private String userName;
  
    @Nullable
    @JsonIgnore
    private String password;
  
    @Builder
    public User(String email, String userName, String password){
      this.email = email;
      this.userName = userName;
      this.password = password;
    }
  }
 ```

 ```java
  @Entity
  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public class Post {
  
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Column(name = "post_id")
      private Long id;
  
      @Column(nullable = false)
      private String title;
  
      @Column(nullable = false,columnDefinition = "TEXT")
      private String content;
  
      @Column(length = 1)
      private String deleteYn = "N";
  
      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "user_no")
      private User user;
  
      @Builder
      public Post(String title, String content, User user){
          this.title = title;
          this.content = content;
          this.user = user;
      }
  
      /* 비지니스로직 */
      /* 게시글 수정 */
      public void updatePost(String title, String content) {
          this.title = title;
          this.content = content;
      }
      /* 게시글 삭제 */
      public void deletePost() {
          this.deleteYn = "Y";
      }
  }
 ```