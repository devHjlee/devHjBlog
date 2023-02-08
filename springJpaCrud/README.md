# 목적
1. 신규 프로젝트에서 사용 되었던 JPA, QueryDsl 을 정리
2. Spring Data Jpa 로 간단한 게시판 구현을 통해 정리
3. User, Post Entity 에서 Fetch LAZY, EAGER 변경해가면서 이해
4. QueryDsl 로 동적쿼리 작성
  
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

### 2. Spring Data JPA 로 Entity, Service, Repository 구현

#### 1) Post, User Entity
  * @NoArgsConstructor(access = AccessLevel.PROTECTED)
    * 기본생성자의 접근 제어를 PROTECTED 설정함으로써 무분별한 객체 생성을 막음 (ex : User user = new User)
  * @Setter 지양(절대 사용금지는 아니다)
    * Setter 는 그 의도 파악과 객체를 변경 할 수 있는 상태가 되어 안전성을 보장받기 힘들다.
    * JPA 에서 Setter는 곧 Update 쿼리를 의미하기에 변경이 필요하면 의미있는 메소드를 생성해서 변경하는것이 좋다.
  * (User) 1:N (Post) 양방향으로 Fetch LAZY, EAGER 차이점
    * 사용자 한명이 여러 글을 작성 할 수 있다는 가정하에 User Entity : OneToMany LAZY, Post Entity : ManyToOne EAGER 로 설정
  * 테스트를 위한 User, Post Table 의 데이터

![image4.png](image4.png)
![image5.png](image5.png)

  * 테스트를 위한 ManyToOne 설정
 ```java
  @Entity
  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public class User {
    //... 생략
    @OneToMany(mappedBy = "user") //default : LAZY
    private List<Post> posts = new ArrayList<>();

  }

  @Entity
  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public class Post {
    //... 생략
    @ManyToOne(fetch = FetchType.EAGER) //default : EAGER
    @JoinColumn(name = "user_no")
    private User user;
    //... 생략
    /* 비지니스로직 */
    /* 게시글 수정 */
    public void updatePost(String title, String content) {
      this.title = title;
      this.content = content;
    }

  }
 ```
* 테스트 코드

 ``` java
    @Test
    void find(){
        System.out.println("Post Find STATR");
        Post post = em.find(Post.class,7L); //1. 설명
        System.out.println("Post Find END");

        System.out.println("===================================");

        System.out.println("USER Find START");
        System.out.println(post.getUser().getUserName()); //2. 설명
        System.out.println("USER Find END");

        System.out.println("===================================");
        System.out.println("USER.POSTS Find START");
        System.out.println(post.getUser().getPosts().get(0).getTitle()); //3. 설명
        System.out.println("USER.POSTS Find END");

    }
 
  ```

* 테스트 코드 실행 로그 설명

 ``` sql
HeFormatSql(P6Spy sql,Hibernate format): //1. 설명
select
post0_.post_id as post_id1_0_0_,
post0_.content as content2_0_0_,
post0_.delete_yn as delete_y3_0_0_,
post0_.title as title4_0_0_,
post0_.user_no as user_no5_0_0_,
user1_.user_no as user_no1_2_1_,
user1_.email as email2_2_1_,
user1_.password as password3_2_1_,
user1_.user_name as user_nam4_2_1_
from
post post0_
left outer join
user user1_
on post0_.user_no=user1_.user_no
where
post0_.post_id=7
Post Find END
===================================
USER Find START
Test1 //2. 설명
USER Find END
===================================
USER.POSTS Find START

HeFormatSql(P6Spy sql,Hibernate format): //3. 설명
select
posts0_.user_no as user_no5_0_0_,
posts0_.post_id as post_id1_0_0_,
posts0_.post_id as post_id1_0_1_,
posts0_.content as content2_0_1_,
posts0_.delete_yn as delete_y3_0_1_,
posts0_.title as title4_0_1_,
posts0_.user_no as user_no5_0_1_
from
post posts0_
where
posts0_.user_no=6
TEST
USER.POSTS Find END
 ```
1. Post Entity에서 User를 ManyToOne Fetch EAGER(즉시로딩) 설정시에는 User 와 조인(user_no)을 걸어 쿼리를 실행하여 User Entity도 불러온다.
2. 1에서 User값도 이미 불러온 상태이므로 User에 대한 쿼리를 실행하지 않아도 값을 불러올 수 있다.
3. User Entity 에서는 Post를 OneToMany Fetch LAZY(지연로딩) 로 설정하였기에 posts 는 프록시 객체로 가져온 상태였지만 post.getUser().getPosts() 로 접근시 해당 쿼리를 호출하게 된다.

위에 코드에서는 find primaryKey를 통해 JPA에 의해 Join 되면서 게시글 하나의 값만 가져오게 해놨는데 게시글이 9천건 이라고 가정하고  
JPQL 을 통해 select p from POST p 쿼리를 진행하면 Post 안에 User가 비어있는것을 채우기 위해 User에 관한 쿼리가 9천번 실행될 것이다. 
이러한 N+1 문제는 LAZY 로 변경 후 JPQL fetch join 으로 해결 할 수도 있다.

다시 원래 소스로 돌아가서 게시글 테이블은 회원 테이블과 연관관계(ManyToOne)를 갖고 회원테이블은 연관관계없이 단방향으로 하였다.

* Entity
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
    
        @Builder
        public User(String email, String userName, String password){
            this.email = email;
            this.userName = userName;
            this.password = password;
        }
    }

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
    }
 ```
* Repository
 ```java
 public interface UserRepository extends JpaRepository<User,Long>{

    User findByEmail(String email);

    boolean existsByEmail(String email);

}

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findPostByTitleOrContent(String content, String title);
}
  ```

* Service
```java
@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /* Spring Data Jpa 를 통한 기능 */
    /**
     * 전체 사용자 조회
     * @return List<User>
     */
    public List<UserDTO> findAll(){

        return userRepository.findAll().stream()
                .map(m->UserDTO.builder()
                        .userName(m.getUserName())
                        .email(m.getEmail())
                        .password(m.getPassword()) //@jsonignore Test
                        .build())
                .collect(Collectors.toList());
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }
    /**
     * 회원가입
     * @param userDTO
     * @return boolean
     */
    public boolean save(UserDTO userDTO){
        if(!userRepository.existsByEmail(userDTO.getEmail())){
            User user = userDTO.toEntity();
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
```

```java
@RequiredArgsConstructor
@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /* Spring Data Jpa 를 통한 기능 */

    /**
     * 게시글 조회
     * @param title
     * @param content
     * @return List<PostDTO>
     */
    public List<PostDTO> findByTitleOrContent(String title, String content) {
        return postRepository.findPostByTitleOrContent(title, content).stream()
                .map(m-> PostDTO.builder()
                        .title(m.getTitle())
                        .content(m.getContent())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 게시글 상세
     * @param id
     * @return PostDTO
     */
    public PostDTO findPostById (Long id) {
        Post post = postRepository.findPostById(id);
        return PostDTO.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    /**
     * 전체 게시글 조회
     * @return List<PostDTO>
     */
    public List<PostDTO> findAll() {
        return postRepository.findAll().stream()
                .map(m-> PostDTO.builder()
                        .title(m.getTitle())
                        .content(m.getContent())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 게시글 저장
     * @param email
     * @param postDTO
     * @return
     */
    public boolean save(String email, PostDTO postDTO) {
        User user = userRepository.findByEmail(email);
        if(user.getId() != null){
            postDTO.setUser(user);
            postRepository.save(postDTO.toEntity());
            return true;
        }
        return false;
    }

    /**
     * 게시글 수정
     * @param postDTO
     * @return boolean
     */
    public boolean updatePost(PostDTO postDTO) {
        Post post = postRepository.findPostById(postDTO.getId());
        post.updatePost(postDTO.getTitle(),postDTO.getContent());
        return true;
    }
}
```

