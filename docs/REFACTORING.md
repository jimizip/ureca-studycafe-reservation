# 스터디카페 예약 시스템 — Spring Boot MVC + MyBatis + JSP 리팩토링 문서

## 1. 개요

기존 스터디카페 예약 시스템은 **순수 JDBC + Java Swing 데스크톱 애플리케이션**이었다.
이를 `SpringBootMVCDBMybatisBoard` 게시판 프로젝트를 레퍼런스로 삼아
**Spring Boot MVC + MyBatis + JSP 웹 애플리케이션**으로 전면 리팩토링했다.

| 구분 | 리팩토링 전 | 리팩토링 후 |
|---|---|---|
| 실행 형태 | Java Swing 데스크톱 GUI | 웹 애플리케이션 (내장 Tomcat) |
| 프레임워크 | 없음 (순수 Java) | Spring Boot 4.1.0 |
| 영속성 | 순수 JDBC (`DriverManager`, `PreparedStatement`) | MyBatis (Mapper 인터페이스 + XML) |
| 의존성 주입 | `StudyCafeFactory` 수동 싱글톤 | Spring DI (생성자 주입) |
| 트랜잭션 | 수동 `setAutoCommit(false)` + `commit/rollback` | `@Transactional` 선언적 트랜잭션 |
| 뷰 | Swing 패널 (`JFrame`, `JPanel`) | JSP + Bootstrap + fetch API |
| 인증 | 객체 참조로 세션 유지 | `HttpSession` + `HandlerInterceptor` |
| 로그인 | 이메일만 | 이메일 + 비밀번호 |
| DB 연결 | `DBUtil` 하드코딩 + 수동 close | `application.properties` + DataSource 풀 |

---

## 2. 레퍼런스(게시판 프로젝트)에서 참고하여 적용한 것

게시판 프로젝트의 핵심 패턴을 스터디카페 도메인에 그대로 이식했다.

### 2-1. 도메인 슬라이스 패키지 구조
레퍼런스의 `board / user / auth / common / config` 구조를 차용해
도메인별 수직 슬라이스로 재편했다.

```
com.ureca/
├── StudycafeApplication.java
├── auth/        controller                    (← 레퍼런스 auth/LoginController)
├── user/        controller·dao·dto·service    (← 레퍼런스 user/)
├── room/        controller·dao·dto·service
├── reservation/ controller·dao·dto·service    (= 게시판 board/ 위치)
├── payment/     controller·dao·dto·service
├── common/      LoginInterceptor, PageController (← 레퍼런스 common/)
└── config/      WebMvcConfig                    (← 레퍼런스 config/WebMvcConfig)
```

### 2-2. Controller 패턴 — `@Controller` + `@ResponseBody`
레퍼런스 `BoardController`처럼 **JSP는 페이지 골격만 forward**하고,
실제 데이터는 컨트롤러가 **JSON으로 응답**하면 프런트가 `fetch`로 받아 렌더링한다.

- `@Controller @ResponseBody @RequestMapping("/...")` 조합
- 생성자 주입으로 Service 의존성 받기
- `HttpSession`에서 `userDto` 꺼내 `userId` 주입 (`@PathVariable`, `@RequestParam` 활용)

### 2-3. 표준 응답 DTO (`*ResultDto`)
레퍼런스 `BoardResultDto`(result/list/dto/count) 패턴을 차용.
모든 응답을 `{ "result": "success"|"fail", "message"?, "list"?, "dto"? }` 형태로 통일.
→ 프런트에서 `data.result === "success"` 단일 분기로 처리.

### 2-4. MyBatis Mapper 인터페이스 + XML
레퍼런스 `BoardDao(@Mapper) + board-mapper.xml` 패턴 그대로:
- `@Mapper` 인터페이스, XML `namespace` = DAO 완전수식명(FQN)
- `mybatis-config.xml`에 `mapUnderscoreToCamelCase=true` → `room_size` ↔ `roomSize` 자동 매핑
- 파라미터 `#{}`, 다중 파라미터는 `@Param`

### 2-5. 세션 기반 인증 + Interceptor
레퍼런스 `LoginInterceptor` + `WebMvcConfig.addInterceptors()` 패턴:
- 로그인 성공 시 `session.setAttribute("userDto", ...)`
- `LoginInterceptor.preHandle()`에서 세션 검사 → 없으면 `/pages/login` 리다이렉트
- `WebMvcConfig`에서 `/**` 적용, 공개 경로(`/pages/login`, `/auth/login`, `/assets/**` 등) 제외

### 2-6. JSP 뷰 리졸버 + 프런트 구성
- `application.properties`: `spring.mvc.view.prefix=/WEB-INF/jsp/`, `suffix=.jsp`
- `PageController`(`/pages/*`)가 JSP 이름만 반환해 forward
- Bootstrap 5 + vanilla `fetch` + 동적 테이블 렌더링 (레퍼런스 `board.jsp` 방식)
- 공통 유틸 `assets/js/util.js` (날짜 포맷 등)

### 2-7. 기타 차용 사항
- `tomcat-embed-jasper` 의존성으로 내장 Tomcat에서 JSP 컴파일
- `spring-boot-starter-parent` 4.1.0, Java 21 (레퍼런스와 동일 버전 라인)
- 예외 → `ResultDto.result="fail"` 변환 (레퍼런스 try/catch 처리 방식)

---

## 3. 스터디카페 도메인에 맞춘 변경/확장

레퍼런스에 없던, 스터디카페 고유 요구로 직접 설계한 부분.

### 3-1. 순수 JDBC DAO → MyBatis 전환
기존 `*DaoImp`의 SQL을 그대로 추출해 XML mapper로 이전.
`Connection`/`PreparedStatement`/`ResultSet` 수동 관리 및 `DBUtil.close()` 전부 제거.

### 3-2. 수동 트랜잭션 → `@Transactional`
기존 `con.setAutoCommit(false)` + try/catch `commit/rollback` 패턴을
서비스 메서드 `@Transactional` + `TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()`로 대체.
적용 지점:
- **예약 등록**: 예약 INSERT → 결제 INSERT (둘 다 성공해야 커밋)
- **예약 수정**: 예약 UPDATE → 결제 금액 재계산 UPDATE
- **예약 취소**: 결제 삭제 → 예약 삭제 (연쇄 삭제)

### 3-3. 예약 도메인 비즈니스 로직 보존
- **시간 겹침 검증** (`checkOverlap`): 같은 룸 + 같은 날짜 예약과 시간 충돌 차단, 본인 예약(`excludeId`)은 제외
- **금액 자동 계산** (`calcPrice`): `룸 시간당 가격 × 예약 시간(h)`
- **24슬롯 예약 현황** (`getBookedHours`): 0~23시 슬롯별 예약 여부 `List<Boolean>` 반환 → 프런트 빨강/초록 표시
- **시간 유효성** (`validateTime`): 종료 > 시작 검증 (`InvalidTimeException`)

### 3-4. 예약 수정 화면 (UX)
- 메인 예약 목록 → **수정** 버튼 → `reservation.jsp` 수정 모드 진입(쿼리 파라미터)
- 수정 모드에서 `booked-hours?excludeId=`로 **본인 예약 슬롯은 초록(재선택 가능)**, 타인 예약은 빨강
- 변경 시간에 맞춰 결제 금액 자동 재계산

### 3-5. 인증 강화
기존 이메일 단독 로그인 → **이메일 + 비밀번호** 검증.
`User` 테이블 `password` 컬럼 추가, `UserService.login()`에서 비밀번호 일치 확인,
세션 저장 시 비밀번호 필드는 `null` 처리(노출 방지).

### 3-6. DB 초기화 스크립트
`schema.sql`(테이블 + FK), `data.sql`(샘플 룸/유저/예약/결제 — 버그 검증 시나리오 포함) 추가.
`spring.sql.init.mode`로 부팅 시 자동 적용, 시드 후 `never`로 끔.

---

## 4. 기술 스택

| 영역 | 기술 |
|---|---|
| 프레임워크 | Spring Boot 4.1.0 (`spring-boot-starter-webmvc`) |
| 언어 / JDK | Java 21 |
| 영속성 | MyBatis (`mybatis-spring-boot-starter` 4.0.1) |
| DB | MySQL 8.x (`mysql-connector-j`) |
| 뷰 | JSP (`tomcat-embed-jasper`) + Bootstrap 5 + fetch API |
| 빌드 | Maven (`spring-boot-maven-plugin`) |

---

## 5. 패키지 레이어 흐름

```
[브라우저 fetch]
   → @Controller(@ResponseBody)   ← HttpSession에서 userId 추출
       → Service(@Service, @Transactional)   ← 비즈니스 로직/검증
           → Mapper(@Mapper) + XML   ← SQL 실행
               → MySQL
   ← ResultDto(JSON) 응답
```

요청 진입 시 `LoginInterceptor`가 세션 인증을 먼저 검사한다.

---

## 6. API 명세

응답은 별도 표기 없으면 `application/json`.
공통 응답 형태: `{ "result": "success" | "fail", "message"?, "list"?, "dto"?, "bookedHours"? }`
인증: ✅ = 로그인 세션 필요 (Interceptor 보호), ⬜ = 공개 경로.

### 6-1. 인증 — `/auth`

| Method | Path | 인증 | 파라미터 (form) | 응답 | 설명 |
|---|---|---|---|---|---|
| POST | `/auth/login` | ⬜ | `email`, `password` | `{result}` | 로그인. 성공 시 세션에 `userDto` 저장 |
| POST | `/auth/logout` | ✅ | — | `{result}` | 세션 무효화 |

**예시**
```
POST /auth/login
Content-Type: application/x-www-form-urlencoded

email=hong@ureca.com&password=1234
→ { "result": "success" }
```

### 6-2. 유저 — `/users`

| Method | Path | 인증 | 파라미터 | 응답 | 설명 |
|---|---|---|---|---|---|
| POST | `/users/insert` | ⬜ | `name`, `tel`, `email`, `password` | `{result, message?}` | 회원가입. 이메일 중복 시 `fail` |
| GET | `/users/list` | ✅ | — | `{result, list[]}` | 전체 유저 조회 |
| POST | `/users/delete/{id}` | ✅ | path `id` | `{result}` | 유저 삭제 |

### 6-3. 회의실 — `/rooms`

| Method | Path | 인증 | 파라미터 | 응답 | 설명 |
|---|---|---|---|---|---|
| GET | `/rooms/list` | ✅ | — | `{result, list[]}` | 전체 회의실 조회 |
| GET | `/rooms/available` | ✅ | — | `{result, list[]}` | 예약 가능 회의실 (현재 전체 반환) |

`RoomDto`: `{ id, roomSize, price }`

### 6-4. 예약 — `/reservations`

| Method | Path | 인증 | 파라미터 | 응답 | 설명 |
|---|---|---|---|---|---|
| GET | `/reservations/list` | ✅ | — (세션 userId) | `{result, list[]}` | 로그인 유저의 예약 목록 |
| GET | `/reservations/booked-hours` | ✅ | `roomId`, `date`(yyyy-MM-dd), `excludeId`? | `{result, bookedHours[24]}` | 룸/날짜 24시간 슬롯 예약 여부. `excludeId`=수정 중 본인 예약 제외 |
| POST | `/reservations/insert` | ✅ | `roomId`, `startTime`, `endTime`, `userCount` | `{result, dto?, message?}` | 예약 등록 + 결제 생성 (트랜잭션) |
| POST | `/reservations/update` | ✅ | `id`, `roomId`, `startTime`, `endTime`, `userCount` | `{result, dto?, message?}` | 예약 수정 + 결제 금액 재계산 (트랜잭션) |
| POST | `/reservations/cancel/{id}` | ✅ | path `id` | `{result, message?}` | 예약 취소 + 결제 연쇄 삭제 (트랜잭션) |

- `userId`는 세션에서 주입 (요청 파라미터 불필요).
- `startTime`/`endTime` 형식: `yyyy-MM-dd'T'HH:mm:ss` (예: `2026-06-23T10:00:00`).
- 실패 사유: 시간 겹침(`이미 예약된 시간입니다.`), 시간 역전(`종료 시간은 시작 시간보다 늦어야 합니다.`).

`ReservationDto`: `{ id, roomId, userId, startTime, endTime, userCount }`

**예시 — 예약 등록**
```
POST /reservations/insert
roomId=4&startTime=2026-06-23T10:00:00&endTime=2026-06-23T13:00:00&userCount=4
→ { "result": "success", "dto": { "id": 12, "roomId": 4, ... } }
```

**예시 — 슬롯 조회**
```
GET /reservations/booked-hours?roomId=2&date=2026-06-23
→ { "result": "success", "bookedHours": [false,...,true(10),true(11),...] }
```

### 6-5. 결제 — `/payments`

| Method | Path | 인증 | 파라미터 | 응답 | 설명 |
|---|---|---|---|---|---|
| GET | `/payments/list` | ✅ | — (세션 userId) | `{result, list[]}` | 로그인 유저의 결제 내역 |

`PaymentDto`: `{ id, userId, roomId, price, paymentDate, roomHistoryId }`

### 6-6. 페이지 forward — `/pages` (HTML 응답)

| Method | Path | 인증 | 뷰 |
|---|---|---|---|
| GET | `/pages/login` | ⬜ | login.jsp |
| GET | `/pages/register` | ⬜ | register.jsp |
| GET | `/pages/main` | ✅ | main.jsp |
| GET | `/pages/reservation` | ✅ | reservation.jsp |
| GET | `/pages/payment` | ✅ | payment.jsp |
| GET | `/pages/logout` | ✅ | 세션 무효화 후 login.jsp |

---

## 7. 데이터 모델 (테이블)

| 테이블 | 주요 컬럼 |
|---|---|
| `User` | id(PK), name, tel, email(UQ), **password** |
| `Room` | id(PK), room_size, price |
| `Room_history` (예약) | id(PK), room_id(FK), user_id(FK), start_time, end_time, user_count |
| `payment_history` (결제) | id(PK), user_id(FK), room_id(FK), price, payment_date, room_history_id(FK) |

예약 1건 ↔ 결제 1건 (`room_history_id`로 연결). 예약 취소 시 결제도 함께 삭제된다.

---

## 8. 실행 방법

```bash
# 1. DB/유저 준비 (최초 1회)
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS studycafedb DEFAULT CHARACTER SET utf8mb4;"

# 2. 실행 (schema.sql + data.sql 자동 적용)
mvn spring-boot:run

# 3. 접속
#    http://localhost:8080/pages/login
#    샘플 계정: hong@ureca.com / 1234
```

시드 후에는 `application.properties`의 `spring.sql.init.mode=always` → `never`로 변경하면
재기동 시 데이터가 덮어써지지 않는다.
