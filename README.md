
# [스프링 핵심 원리 - 기본편 (김영한)]

#### 강의 링크
- [강의 바로가기](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8/dashboard)

## 해당 글은 강의를 들으면서 적었던 메모들을 남겨보겠다.

#### HashMap
- HashMap 은 동시성 이슈가 발생할 수 있다.
- 따라서 Multi-Thread 환경에서도 사용이 가능한 ConcurrentHashMap 을 사용하자.

#### SOLID
- 모든 설계는 구현객체가 아닌 인터페이스에 의존하게 설계해야한다 (DIP 원칙)
	- 그래야 갈아끼울때 구현코드는 건드리지 않고 간편하게 갈아끼울수 있다.

#### AppConfig - 관심사의 분리
(SRP 원칙 - 단일책임, DIP 원칙 - 의존 역전, OCP 원칙 - 개방폐쇄)

- SRP
	- AppConfig를 통하여 구체클래스를 생성하고 연결(주입)하는 기획자 같은 역할을 담당하여 확실하게 분리했다
- DIP
	- 새로운 할인 정책을 개발하고, 적용할 때 클라이언트 코드를 변경하지 않고 AppConfig 내에서 새로운 할인정책 객체 인스턴스를 생성하여 주입함
- OCP
	- AppConfig 내에서 구체클래스를 선택하여 주입함으로 클라이언트 코드의 수정은 일어나지 않음

#### IoC
- 프로그램에 대한 제어 흐름에 대한 권한은 모두 AppConfig가 가지고 있다
- 따라서 클라이언트 코드(구체클래스)는 묵묵히 자신의 로직을 실행 한다.
- 이처럼 프로그램의 제어 흐름을 개발자가 직접 제어하지 않고, 외부에서 관리하는 것이 IoC 즉 제어의 역전이다’

#### 프레임워크 vs 라이브러리
- 프레임워크
	- JUnit 같이 프레임워크가 내가 작성한 코드를 제어하고 대신 실행한다.
- 라이브러리
	- 내가 작성한 코드가 직접 제어의 흐름을 담당한다.

#### 스프링 컨테이너
- ApplicationContext 은 스프링 컨테이너로써 모든 객체를 관리해준다. (시작점)
	- ex) @Bean
- XML 기반으로도 만들 수 있고, 어노테이션 기반의 자바 설정 클래스로 만들 수도 있다.

아래처럼 하면 `AppConfig Class`에 있는 모든 메소드들을 스프링에서 관리해줌
- `ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);`

- 최상위에 BeanFactory가 있고 그 밑에 ApplicationContext가 존재한다.
- 하지만 BeanFactory를 직접 사용하는 경우는 거의 없고 일반적으로 부가기능이 포함된 AppicationContext를 사용한다
- 따라서 보통 ApplicationContext를 스프링 컨테이너라고 부른다

#### ApplicationContext 가 제공하는 부가기능
- MessageSource
- EnvironmentCapable
- **ApplicationEventPublisher**
- ResourceLoader

#### 빈 이름
- 빈 이름은 보통 메서드 이름을 사용한다
- 또한 직접 부여하는 방법도 있다
	- ex) @Bean(name="memberService2")

`❗빈 이름은 항상 다른 이름을 부여해야 한다. 만약 같은 이름이 부여되면, 다른 빈이 무시되거나, 기존 빈을 덮어버리거나 설정에 따라 오류가 발생 할 수 있다.`

#### 스프링에 등록된 모든 빈 이름 조회
- AnnotationConfigApplicationContext.getBeanDefinitionNames()

#### 빈 이름으로 빈 객체(인스턴스 조회)
- AnnotationConfigApplicationContext.getBean()

#### Assertions.assertThat (AssertJ)
- isSameAs : 주소값 비교 (객체 인스턴스, 참조값)
- isEqualTo : 문자비교

### `스프링은 싱글톤 패턴이기 때문에 스프링 빈은 항상 무상태로 설계해야된다‼️ - 공유필드는 진짜 조심해야됨❗`

#### @Configuration
- 바이트코드를 조작하는 CGLIB 기술을 사용해서 싱글톤을 보장
    - CGLIB 기술 예상코드
        ```java
        if (memoryMemberRepository가 이미 스프링 컨테이너에 등록되어 있으면?) {
        	return 스프링 컨테이너에서 찾아서 반환;
        } else { //스프링 컨테이너에 없으면
        기존 로직을 호출해서 MemoryMemberRepository를 생성하고 스프링 컨테이너에 등록
        	return 반환
        }
        ```    
- 이걸 빼면 @Bean은 다 스프링 컨테이너에 등록은 되지만 싱글톤이 깨짐 (new 일 때마다 계속 등록됨)

#### @ComponentScan
```java
@ComponentScan(
				basePackages = "hello.core.member",
        excludeFilters =@ComponentScan.Filter(type = FilterType.ANNOTATION, classes =Configuration.class)
)
```
- excludeFilters 는 컴포넌트 스캔에 제외 될 대상들
- basePackges 는 해당 경로 밑에있는 컴포넌트들만 스캔이 된다(경로 밑에있는 애들만 빈 생성)
	- 디폴트는 해당 파일이 속해있는 패키지

#### ConflictingBeanDefinitionException
- 컴포넌트 스캔에 의해 자동으로 스프링 빈 등록 중 이름이 같은게 있을 경우 오류
- 자동 빈 등록과 수동 빈 등록의 빈 이름이 겹칠 경우, 수동 빈 등록이 우선권을 가진다

#### 스프링 컨테이너 생성 과정
1. 스프링 컨테이너 생성
2. 스프링 빈 등록
3. 스프링 빈 의존관계 설정
4. 의존관계 주입

#### DI(의존관계 주입)
- **생성자 주입**
	- 자바가 OrderServiceImpl 을 생성할때 어쩔수없이 생성자를 불러야 하기때문에 빈을 등록하면서 의존관계 주입도 같이 일어남
	- 변수에 final 을 붙일 수 있음
	- 어차피 생성자 주입은 어플리케이션이 뜰때 한번 DI 되고 바뀔일이 절대 없기 때문에 (수정 불가) final 을 붙여도 된다.

- **세터 주입**
	- 의존관계를 설정하는 단계와 주입하는 단계가 나눠져있음
	- 따라서 생성자 주입과는 다르게 빈 등록 후 주입이 일어남
- **필드 주입**
	- 외부에서 변경이 불가능해서 테스트하기가 어렵다.
	- DI 프레임워크가 없으면 아무것도 할 수 없기때문에 웬만하면 필드주입은 지양

- 테스트코드에서 new로 해버리면 빈에서 주입받는게 아닌 순수 자바영역이기때문에 결국 세터를 열어서 주입해줘야 함
- Lombok은 자바의 어노테이션 프로세서라는 기능을 이용하여 컴파일 시점에 생성자 코드를 자동으로 생성해줌
	- Lombok 관련내용 링크 : [https://beomdrive.tistory.com/5](https://beomdrive.tistory.com/5)


- `어플리케이션이 뜨는 시점에서 빈을 타입으로 조회할때 빈이 2개 이상일때 문제가 발생한다`
	- (NoUniqueBeanDefinitionException: No qualifying bean of type)

#### @Autowried
- 특별한 기능
	- Autowired는 처음엔 타입 매칭을 시도한다.
	- 여러 빈이 조회되면 필드 이름 혹은 파라미터 이름으로 빈 이름을 추가 매칭한다.
- 자동 주입 대상을 옵션으로 처리하는 방법
	1. @Autowired(required=false) : 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안됨
	2. org.springframework.lang.@Nullable : 자동 주입할 대상이 없으면 null이 입력된다.
	3. Optional<> : 자동 주입할 대상이 없으면 Optional.empty 가 입력된다.

#### @Qualifier
1. @Qualifier끼리 매칭
2. 빈 이름 매칭
3. NoSuchBeanDefinitionException 예외 발생

#### @Provider
- DL (Dependency Lookup)
	- 직접 찾아서 주입
	- 순환참조 발생할때 사용 가능

#### 애노테이션
- 애노테이션에는 상속이라는 개념이 없다.
- 여러 애노테이션을 모아서 사용하는 기능은 스프링이 지원하는 기능

#### 빈 생명주기
- 스프링 빈의 이벤트 라이프사이클
    - 스프링 컨테이너 생성 → 스프링 빈 생성 → 의존관계 주입 → 초기화 콜백 → 사용 → 소멸전 콜백 → 스프링 종료
- 초기화 콜백 : 빈이 생성되고, 빈의 의존관계 주입이 완료된 후 호출
    - 보통 사용법 : @PostConstruct
    - 외부 라이브러리 적용 시 : @Bean(initMethod = "init”)
- 소멸전 콜백 : 빈이 소멸되기 직전에 호출
    - 보통 사용법 : @PreDestroy
    - 외부 라이브러리 적용 시 : @Bean(destroyMethod = "close")
    

#### 빈 스코프
- 싱글톤
    - 기본 스코프, 스프링 컨테이너의 시작과 종료까지 유지되는
    가장 넓은 범위의 스코프
    - ex) @Scope("singleton") or default
- 프로토타입
    - 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입까지만 관여하고 더는 관리하지 않는 매우 짧은 범위의 스코프
    - PreDestroy 메서드 호출 안됨
    - ex) @Scope("prototype")
- 웹 관련 스코프
    - request : 웹 요청이 들어오고 나갈때 까지 유지되는 스코프
        - ex) @Scope(value = "request")
    - session: 웹 세션이 생성되고 종료될 때 까지 유지되는 스코프
    - application: 웹의 서블릿 컨텍스트와 같은 범위로 유지되는 스코프
    

#### 프록시
- proxyMode = ScopedProxyMode.TARGET_CLASS
- 가짜 프록시 클래스가 주입되고, 프록시 내부에서는 실제 빈을 요청하는 위임 로직이 들어있음
