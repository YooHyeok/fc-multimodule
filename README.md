# *멀티모듈 이란?*
필요한 기능들로 모듈 생성 후 레고를 조립하듯 필요한 모듈을 조립한다.  
N개의 모듈이 조립되어있는 프로젝트를 Multi Module 프로젝트라 부른다.  
일반적 예로 로그인을 담당하는 로그인 모듈 혹은 인증 모듈 혹은 DB엔티티만을 소유한 모듈 등등  
개발자가 정의하기 나름이다.
## *멀티모듈 프로젝트를 사용하는 이유?*
하나의 프로젝트에 모든 코드를 다 넣어도 되겠으나, 만약 API 서버를 만들어야 하는데  
DB Entity가 필요하고, Batch서버에서도 동일한 DB Entity가 필요하다면  
중복된 Entity를 Module화 시켜 사용하기 위해 Multi Module 프로젝트를 사용한다.  
만약 독립적으로 관리한다면 즉, API서버와 Batch서버에 동일한 DB Entity를 각각 따로 넣게 되면  
관리를 해야 하는 포인트가 늘어나기 때문에 이경우 혹시라도 한 곳에서 누락이 발생하면 리스크가 늘어날 수 밖에 없다.


# *멀티 모듈 구성*
1. Root 프로젝트 생성 후 HELP.md 파일과 src 디렉토리를 제거한다.
2. api로 사용할 모듈 추가 및 HELP.md 파일을 제거한다.
3. common 공통 모듈 추가 및 HELP.md 파일을 제거한다.  
2~3번 과정에서는 java jdk 버전을 모두 맞춰주고 각 모듈들에서 git을 제거해준다
4. Root 프로젝트의 settings.gradle에 include키워드로 모듈 이름을 등록해준다
    ```text
    include 'module-api'
    include 'module-common'
    ```
5. api 모듈에 common 모듈에 대한 의존성을 설정한다.
    ```text
    dependencies {
        /* 생략 */
        implementation project(':module-common') // root project -> settings.gradle에 선언한 값과 같아야함.
    }
    ```
6. Root모듈에서 하나의 settings.gradle 파일로 관리를 하므로 각 모듈의 해당 파일을 제거한다.  
    이때 각 모듈의 build.gradle에 `tasks.register('prepareKotlinBuildScriptModel') {}` 코드를 추가해야한다.



## *서로 다른 모듈 간 Componant Scan*
```java
@SpringBootApplication(
        /* 모든 모듈을 다 스캔하는 것은 시간도 오래걸리고 굉장히 비효율적이기 때문에, 빈으로 등록해야 되는 필요한 것들만 명시한다. */
        scanBasePackages = {"dev.be.moduleapi", "dev.be.modulecommon"}
)
public class ModuleApiApplication {/* 생략 */}
```
위와 같이 Appication 메인 클래스에 `@SpringBootApplication` 애노테이션을 활용하여 scanBasePackages 옵션에 
스캔할 패키지명을 등록해준다.

# *DB연동* 
### common모듈 build.gradle에 공통 dependency jpa 추가 및 api 설정
 - build.gradle
    ```json
    plugins {
        /* 생략 */
        id 'java-library' // dependency 추가시 api라는 함수 키워드를 사용하기위한 설정
    }
    dependencies {
      /* 공통 dependency 추가 */
      api 'org.springframework.boot:spring-boot-starter-data-jpa'
    }
    ```
### 서로 다른 모듈간 Repository, Entity에 대한 도메인 패키지 공유 설정
- #### 서버 구동시 에러 발생 1.
    ```text
    Parameter 1 of constructor in dev.be.moduleapi.service.DemoService required a bean of type 'dev.be.modulecommon.repository.MemberRepository' that could not be found.
    ```
    `@EnableJpaRepositories` 애노테이션으로 오류 해결
    ```java
    @EnableJpaRepositories(basePackages = "dev.be.modulecommon.repository")
    public class ModuleApiApplication {/* 생략 */}
    ```
    참조할 repository의 패키지 경로를 명시적으로 지정한다.  
- #### 서버 구동시 에러 발생 2.
    ```text
    Not a managed type: class dev.be.modulecommon.domain.Member
    ```
    `@EntityScan 오류 해결`
    ```java
    @EntityScan("dev.be.modulecommon.domain")
    public class ModuleApiApplication {/* 생략 */}
    ```
    참조할 엔티티의 패키지 경로를 명시적으로 지정한다.

# *멀티모듈 배포*
- ### 공통 모듈 gradle 설정
    - build.gradle 
    ```json
    tasks.bootJar { enabled = false }
    tasks.jar { enabled = true }
    ```
  - **tasks.bootJar**  
    기본값 true *.jar파일 생성  
    파일 안에 어플리케이션을 실행하는데 필요한 디펜던시, 클래스, 리소스를 포함하고 있어 java -jar옵션으로 jar파일을 실행시킬 수 있게 된다.  
    그러나 Common module은 다른 모듈에서 참조하는 목적의 모듈이기 때문에 실행 가능한 jar파일을 만들 필요가 없다.    
    그렇기에 초기에 application.class를 삭제했었다.  
    만약 bootjar 옵션을 true로 했을 경우 Main클래스 즉, application.class를 찾게되는데 앞서 말한것처럼 Common에는 없기 때문에 에러가 발생하게 된다. 
    tasks.jar { enabled=true }
  - **tasks.jar**  
    기본값 true *.plane.jar파일 생성  
    해당 기본값은 *.plane.jar 와 같이 plane이라는 네이밍이 붙게 된다.  
    plane은 dependency를 가지고 있지 않다.  
    즉, 클래스와 리소스만 포함하고 있어 java -jar 와 같이 jar를 실행시킬 경우 plane은 server를 실행시킬수 없게 된다.
- ### Build명령
    ```text
    ./gradlew clean :module-api:buildNeeded --stacktrace --info --refresh-dependencies -x test
    ```
  - `stacktrace`: 빌드를 진행하며 발생하는 로깅 혹은 예외를 빠르게 캐치할 수 있도록 로그를 쌓아서 보여달라  
  - `info`: 로깅레벨을 info 이상으로 설정 (`debug` -> `info` -> `warn` -> `error`)  
  - `refresh-dependencies`: dependecy를 한번 더 refresh  
  - `-x test`: build시 test코드 체크는 skip   
      　　　　　예를들어 타인이 작성한 테스트코드는 타인의 브랜치에서 수정해야 정상반영 되는 등의 이유  
- ### jar파일 Server구동
    1. jar파일이 생성된 곳으로 경로 이동  
       `cd module-api/build/libs`
    2. 목록확인  
       `ls`
    3. plain 없는 jar파일을 실행  
       `java -jar module-api-0.0.1-SNAPSHOT.jar`

# *Profile*
#### Profile이 필요한 이유
실제 회사에서 개발을 할 때는 N개의 Profile을 설정한다.  
ex) local, dev, test, prod  
위와같이 Profile을 나누는 이유는 환경별로 설정해야 하는 Property값들이 다르기 때문이다.  
local과 dev는 하나의 환경으로 볼 수 있으며 test와 prod도 또 다른 환경으로 볼 수 있다.  
동일한 코드로 동일한 로직으로 수행이 돌아가겠지만 상황에 따라서  
즉, local과 dev환경일 때는 A - DB를 바라봐야 할것이고  
test와 prod환경일 때는 B - DB를 바라봐야 할 필요성이 있다.  
이렇게 환경별로 내가 사용해야 하는 Property값들이 다를 경우  
또 다른 예 로는 local과 dev에서 logging level을 낮게 설정하지만 test와 prod에서는 logging level을 높게 설정해야만 한다.  
이렇게 property값들에 의해서 설정을 바꿔야 될 때, 프로파일이라는 개념을 사용하게 된다.

Profile 적용 방법  
application-{env}.yml(yaml/properties)  
```text
application-{env}.yml
application-{env}.yaml
application-{env}.properties
```
파일명의 {env} 영역에는 적용할 환경으로 지정한다.

- application-local.yml
    ```yaml
    profile-name: local
    ```
- java파일
    ```java
    @Value("${profile-name}")
    private String local;
    // 메소드에 멤버 변수 local을 출력하기위한 메소드를 구현해 본 뒤 호출해보자
    ```

서버를 실행해본다.

```text
Description:

Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

Reason: Failed to determine a suitable driver class


Action:

Consider the following:
If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
If you have database settings to be loaded from a particular profile you may need to activate it (no profiles are currently active).
```
서버 실행시 위와같은 오류가 발생한다.  

위로 올려서 로그를 조금 더 살펴본다.
```text
2024-04-10 10:42:59.237  INFO 7540 --- [           main] dev.be.moduleapi.ModuleApiApplication    : No active profile set, falling back to 1 default profile: "default"
```
위 로그에서 말하는 default는 application.yml(yaml/properties) 이다.  
즉, 

IntelliJ 우측 상단의 start debug/run configuration - edit - Active Profiles에 {env} 값을 지정

java -jar 명령
1. cd module
2. java -jar "-Dspring.profiles.active=local" module-api-0.0.1-SNAPSHOT.jar
2. java -jar "-Dspring.profiles.active=beta" module-api-0.0.1-SNAPSHOT.jar