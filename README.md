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

헥사고날 아키텍처(Hexagonal Architecture)는 Alistair Cockburn이 제안한 소프트웨어 아키텍처 패턴으로, **포트와 어댑터 패턴(Ports and Adapters Pattern)**이라고도 불립니다.

### 핵심 개념

#### 1. 도메인(Domain) - 중심부
- 애플리케이션의 핵심 비즈니스 로직
- 외부 의존성 없이 순수한 비즈니스 규칙만 포함
- 도메인 모델, 서비스, 포트 인터페이스로 구성

#### 2. 포트(Ports) - 인터페이스
- **인바운드 포트**: 외부에서 도메인으로 들어오는 요청을 정의 (Use Case)
- **아웃바운드 포트**: 도메인에서 외부로 나가는 요청을 정의 (Repository, External API)
- 도메인과 외부 세계 사이의 계약을 정의

#### 3. 어댑터(Adapters) - 구현체
- **인바운드 어댑터**: 외부 요청을 도메인이 이해할 수 있는 형태로 변환
  - REST API 컨트롤러, CLI, GUI 등
- **아웃바운드 어댑터**: 도메인의 요청을 외부 시스템이 이해할 수 있는 형태로 변환
  - 데이터베이스 어댑터, 외부 API 클라이언트 등

### 아키텍처의 장점

1. **의존성 역전**: 외부 계층이 내부 계층에 의존하며, 도메인은 외부에 의존하지 않음
2. **테스트 용이성**: 도메인 로직을 독립적으로 테스트 가능
3. **유연성**: 외부 기술 변경 시 도메인 로직에 영향 없음
4. **명확한 책임 분리**: 각 계층의 역할이 명확히 구분됨

### 의존성 방향

```
외부 세계 → 어댑터 → 포트 → 도메인
```

- 모든 의존성은 도메인을 향함
- 도메인은 외부 세계를 알지 못함
- 포트를 통해서만 소통

## 예제 설명

### 도메인 계층
- `Member`: 회원 도메인 모델
- `MemberUseCase`: 회원 관련 유스케이스 정의 (인바운드 포트)
- `MemberPort`: 회원 영속성 포트 정의 (아웃바운드 포트)
- `MemberService`: 회원 비즈니스 로직 구현

### 어댑터 계층
- `MemberController`: REST API 어댑터 (인바운드)
- `MemberPersistenceAdapter`: JPA 영속성 어댑터 (아웃바운드)
- `MemberJpaEntity`: JPA 엔티티
- `MemberJpaRepository`: Spring Data JPA 리포지토리

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

이 구조를 통해 비즈니스 로직을 중심으로 한 깔끔하고 유지보수 가능한 애플리케이션을 구축할 수 있습니다.