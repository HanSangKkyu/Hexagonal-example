# Hexagonal Architecture Example

이 프로젝트는 Spring Boot와 Kotlin을 사용하여 헥사고날 아키텍처(Ports and Adapters Pattern)를 구현한 예제입니다.

## 프로젝트 구조

```
src/main/kotlin/com/example/hexagonal/
├── adapter/                    # 외부 어댑터들
│   ├── in/                    # 인바운드 어댑터 (외부에서 도메인으로)
│   │   └── web/               # HTTP API 컨트롤러
│   │       └── MemberController.kt
│   └── out/                   # 아웃바운드 어댑터 (도메인에서 외부로)
│       └── persistence/       # 데이터베이스 영속성
│           ├── MemberJpaEntity.kt
│           ├── MemberJpaRepository.kt
│           └── MemberPersistenceAdapter.kt
├── domain/                    # 도메인 계층 (비즈니스 로직)
│   ├── model/                # 도메인 모델
│   │   └── Member.kt
│   ├── port/                 # 포트 인터페이스
│   │   ├── in/              # 인바운드 포트 (유스케이스)
│   │   │   └── MemberUseCase.kt
│   │   └── out/             # 아웃바운드 포트
│   │       └── MemberPort.kt
│   └── service/             # 도메인 서비스 (비즈니스 로직 구현)
│       └── MemberService.kt
└── HexagonalApplication.kt   # 메인 애플리케이션
```

## 헥사고날 아키텍처란?
비즈니스 로직을 외부 환경으로부터 분리하여 유연하고 테스트하기 쉬운 애플리케이션을 만드는 데 중점을 둔 설계 패턴
이 구조를 통해 비즈니스 로직을 중심으로 한 깔끔하고 유지보수 가능한 애플리케이션을 구축할 수 있습니다.

# 핵심 키워드

## 포트

interface이다. 도메인-외부 사이에 어떤 행동을 할 수 있는지 정의한다.

즉, 실제적인 기능은 없고 어떤 기능을 할 것인지 껍데기만 정의한 것이다.

하위에 포트, 유즈케이스 라는 개념이 더 있다.

### 포트

도메인→ 외부

도메인에서 외부에 요청을 보낼 때 사용한다. 어떤 요청을 보낼 것인지 행동 리스트이다.

### 유스케이스

외부 → 도메인

외부에서 도메인으로 어떤 기능을 수행하라는 명령 목록이다.

## 어댑터

인바운드, 아웃바운드 어댑터가 있다.

### 인바운드 어댑터

외부의 요청을 받아 애플리케이션 내부로 전달하는 역할

ex: controller - servcie를 의존성 주입 받는 필드를 가지고 있어서 서비스의 메서드를 호출한다.

### 아웃바운드 어댑터

**포트를 구현한 구현체**이다.

애플리케이션 내부의 요청을 받아 외부 시스템에 전달하는 역할

ex: MemberPersistenceAdapter

## 모델

비즈니스 로직의 핵심 객체이다.

도메인(내부) 영역에 해당한다.

OOP 에서 말하는 객체에 해당한다.

외부 세계에 해당하는 객체를 컴퓨터 세계로 옮겨 놓은 것이다.

한문장으로 표현할 수 없다. 예제를 보면서 느껴야 한다.

## 서비스

보통 **유즈케이스를 구현하는 구현체**이고 포트를 의존성 주입 받아서 외부에 요청을 보내는 역할을 한다. 요청에 대한 응답을 어댑터에게 넘겨주는 역할도 한다.

핵심 비즈니스 로직을 실행하는 '두뇌'이자 '지휘자' 역할

모델과 함께 도메인(내부) 영역에 해당한다.

여러 모델을 조합해서 기능을 수행해야 할 때 모델을 한곳에서 사용해야 하는데 그 곳이 service이다.

## 실행 방법

```bash
./gradlew bootRun
```

## API 사용 예제

```bash
# 회원 생성
curl -X POST http://localhost:8080/members \
  -H "Content-Type: application/json" \
  -d '{"name": "홍길동", "email": "hong@example.com"}'

# 회원 조회
curl http://localhost:8080/members/1
```

