
---

### Base URL
`http://localhost:8080/api`

### 공통 사항
로그인이 필요한 API의 경우, 클라이언트는 로그인 시 발급받은 `JSESSIONID` 쿠키를 요청 헤더에 포함해야 한다.

---

## 1. 인증 및 유저 (User) API

### 1-1. 회원가입 (Lv 2, Lv 3, Lv 6)

새로운 유저를 등록한다. 비밀번호는 서버 내에서 BCrypt로 암호화되어 저장된다.
* **URL:** `/users`
* **Method:** `POST`
* **Request Body:**

| 필드명 | 타입 | 필수여부 | 제약조건 (Validation) | 설명 |
| --- | --- | --- | --- | --- |
| `username` | String | O | 필수값, 최대 4자 이내 | 유저명 |
| `email` | String | O | 필수값, 올바른 이메일 형식 | 이메일 |
| `password` | String | O | 필수값, 8글자 이상 | 비밀번호 |

```json
// 요청 예시
{
  "username": "홍길동",
  "email": "test@test.com",
  "password": "password123!"
}
```

* **Response (201 Created):**
```json
{
  "id": 1,
  "username": "홍길동",
  "email": "test@test.com",
  "createdAt": "2026-04-21T10:00:00",
  "updatedAt": "2026-04-21T10:00:00"
}
```

---


### 1-2. 유저 전체 조회 (Lv 2)

서비스에 가입된 모든 유저의 목록을 조회한다. 응답 시 비밀번호와 같은 민감한 정보는 제외된다.

- **URL:** `/users`
- **Method:** `GET`
- **Request Header:** `Cookie: JSESSIONID=...` (로그인 세션 쿠키 필요)

- **Response (200 OK)**

```json
[
  {
    "id": 1,
    "username": "홍길동",
    "email": "test1@test.com",
    "createdAt": "2026-04-21T10:00:00",
    "updatedAt": "2026-04-21T10:00:00"
  },
  {
    "id": 2,
    "username": "김철수",
    "email": "test2@test.com",
    "createdAt": "2026-04-21T11:30:00",
    "updatedAt": "2026-04-21T11:30:00"
  }
]
```

- **Response (401 Unauthorized)**
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "로그인이 필요한 서비스입니다."
}
```

---


### 1-3. 유저 단건 조회 (Lv 2)
선택한 특정 유저의 상세 정보를 조회한다.

- **URL:** `/users/{userId}`
- **Method:** `GET`
- **Request Header:** `Cookie: JSESSIONID=...` (로그인 세션 쿠키 필요)
- **Path Variable:**
  - `userId` (Long) : 조회할 유저의 고유 ID

- **Response (200 OK)**

```json
{
  "id": 1,
  "username": "홍길동",
  "email": "test1@test.com",
  "createdAt": "2026-04-21T10:00:00",
  "updatedAt": "2026-04-21T10:00:00"
}
```

- **Response (401 Unauthorized)**
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "로그인이 필요한 서비스입니다."
}
```

- **Response (404 Not Found)**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "해당 유저를 찾을 수 없습니다."
}
```

---


### 1-4. 유저 정보 수정 (Lv 2)
로그인한 유저 본인의 정보(유저명, 이메일 등)를 수정합니다. 세션을 통해 현재 로그인한 사용자와 수정하려는 대상(`userId`)이 일치하는지 권한(인가)을 확인합니다.

- **URL:** `/users/{userId}`
- **Method:** `PATCH`
- **Request Header:** `Cookie: JSESSIONID=...` (로그인 세션 쿠키 필요)
- **Path Variable:**
  - `userId` (Long) : 수정할 유저의 고유 ID

- **Request Body (JSON)**

| 필드명 | 타입 | 필수여부 | 제약조건 (Validation) | 설명 |
| --- | --- | --- | --- | --- |
| `username` | String | O | 필수값, 최대 4자 이내 | 변경할 유저명 |
| `email` | String | X | 올바른 이메일 형식 | 변경할 이메일 (선택) |

```json
// 요청 예시
{
  "username": "고길동",
  "email": "new_email@test.com"
}
```

- **Response (200 OK)**

```json
// 응답 예시 (수정된 데이터 반환, updatedAt 변경됨)
{
  "id": 1,
  "username": "고길동",
  "email": "new_email@test.com",
  "createdAt": "2026-04-21T10:00:00",
  "updatedAt": "2026-04-21T15:30:00"
}
```

- **Response (400 Bad Request)**
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "유저명은 4글자 이내여야 합니다."
}
```

- **Response (401 Unauthorized)**
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "로그인이 필요한 서비스입니다."
}
```

- **Response (404 Not Found)**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "해당 유저를 찾을 수 없습니다."
}
```

---


### 1-5. 유저 탈퇴 (삭제) (Lv 2)
로그인한 유저 본인의 계정을 삭제(탈퇴)합니다. 세션을 통해 본인 여부를 확인하고, 보안을 위해 비밀번호를 한 번 더 검증합니다. 유저가 삭제될 때 해당 유저가 작성한 일정과 댓글도 함께 처리(Cascade 또는 서비스 계층 삭제)되어야 합니다.

- **URL:** `/users/{userId}`
- **Method:** `DELETE`
- **Request Header:** `Cookie: JSESSIONID=...` (로그인 세션 쿠키 필요)
- **Path Variable:**
  - `userId` (Long) : 삭제할 유저의 고유 ID

- **Request Body (JSON)**

| 필드명 | 타입 | 필수여부 | 제약조건 (Validation) | 설명 |
| --- | --- | --- | --- | --- |
| `password` | String | O | 필수값 | 본인 확인용(재확인) 비밀번호 |

```json
// 요청 예시
{
  "password": "password123!"
}
```

- **Response (204 No Content)**
  - 본문 없이 상태 코드 `204`만 반환 (성공적으로 삭제됨)

- **Response (400 Bad Request)**
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "비밀번호는 필수 입력값입니다."
}
```

- **Response (401 Unauthorized)**
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "로그인이 필요한 서비스이거나 비밀번호가 일치하지 않습니다."
}
```

- **Response (404 Not Found)**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "해당 유저를 찾을 수 없습니다."
}
```

---


### 1-6. 로그인 (Lv 4)
이메일과 비밀번호를 검증하고 세션을 생성한다.
* **URL:** `/users/login`
* **Method:** `POST`
* **Request Body:**

| 필드명 | 타입 | 필수여부 | 제약조건 (Validation) | 설명 |
| --- | --- | --- | --- | --- |
| `email` | String | O | 필수값 | 가입 시 사용한 이메일 |
| `password` | String | O | 필수값 | 원본 비밀번호 |

* **Response (200 OK):**
  성공 시 `JSESSIONID` 쿠키가 발급된다.
    ```json
    {
      "id": 1,
      "email": "test@test.com"
    }
    ```

---

### 1-7. 로그아웃 (Lv 4)
현재 로그인된 유저의 세션을 만료시킨다.
* **URL:** `/users/logout`
* **Method:** `POST`
* **Request:** `JSESSIONID` 쿠키 필요
* **Response (204 No Content):** 본문 없음.

---

### 2. 일정 (Schedule) API

**2-1. 일정 생성 (Lv 1, Lv 2)**
로그인한 유저의 정보(세션)를 바탕으로 일정을 생성한다.
* **URL:** `/schedules`
* **Method:** `POST`
* **Request:** `JSESSIONID` 쿠키 필요
* **Request Body:**

| 필드명 | 타입 | 필수여부 | 제약조건 (Validation) | 설명 |
| --- | --- | --- |-------------------| --- |
| `title` | String | O | 필수값, 최대 30자 이내    | 할일 제목 |
| `content` | String | O | 필수값, 최대 200자 이내   | 할일 내용 |
* **Response (201 Created):**
    ```json
    {
      "id": 1,
      "title": "스프링 과제",
      "content": "페이징 처리 구현하기",
      "userId": 1,
      "createdAt": "2026-04-21T12:00:00",
      "updatedAt": "2026-04-21T12:00:00"
    }
    ```

**2-2. 일정 페이징 조회 (Lv 8)**
등록된 일정을 페이지 단위로 조회한다. 기본 페이지 크기는 10이며, 수정일 기준 내림차순 정렬된다.
* **URL:** `/schedules`
* **Method:** `GET`
* **Query Parameter:**
  * `page` (Integer, 선택): 조회할 페이지 번호 (기본값: 0)
  * `size` (Integer, 선택): 페이지당 항목 수 (기본값: 10)
* **Response (200 OK):**
  Spring Data JPA의 Page 객체 구조를 따른다.
    ```json
    {
      "content": [
        {
          "title": "스프링 과제",
          "content": "페이징 처리 구현하기",
          "commentCount": 2,
          "createdAt": "2026-04-21T12:00:00",
          "updatedAt": "2026-04-21T12:00:00",
          "username": "홍길동"
        }
      ],
      "pageable": {
        "pageNumber": 0,
        "pageSize": 10
      },
      "totalElements": 1,
      "totalPages": 1
    }
    ```

**2-3. 일정 단건 조회 (Lv 1, Lv 7)**
선택한 일정의 상세 정보와 연관된 댓글 목록을 함께 조회한다.
* **URL:** `/schedules/{scheduleId}`
* **Method:** `GET`
* **Response (200 OK):**
    ```json
    {
      "id": 1,
      "title": "스프링 과제",
      "content": "페이징 처리 구현하기",
      "userId": 1,
      "createdAt": "2026-04-21T12:00:00",
      "updatedAt": "2026-04-21T12:00:00",
      "comments": [
        {
          "id": 1,
          "content": "도움이 필요하면 알려주세요.",
          "userId": 2,
          "createdAt": "2026-04-21T13:00:00",
          "updatedAt": "2026-04-21T13:00:00"
        }
      ]
    }
    ```

**2-4. 일정 수정 (Lv 1, Lv 4)**
본인이 작성한 일정만 수정할 수 있다. 세션 검증을 통해 권한을 확인한다.
* **URL:** `/schedules/{scheduleId}`
* **Method:** `PATCH`
* **Request:** `JSESSIONID` 쿠키 필요
* **Request Body:**

| 필드명 | 타입 | 필수여부 | 제약조건 (Validation) | 설명 |
| --- | --- | --- |-------------------| --- |
| `title` | String | O | 필수값, 최대 30자 이내    | 변경할 할일 제목 |
| `content` | String | O | 필수값, 최대 200자 이내   | 변경할 할일 내용 |
* **Response (200 OK):**
  수정된 일정 정보 반환 (updatedAt 변경됨).

**2-5. 일정 삭제 (Lv 1, Lv 4)**
본인이 작성한 일정만 삭제할 수 있다. 세션 검증을 통해 권한을 확인한다.
* **URL:** `/schedules/{scheduleId}`
* **Method:** `DELETE`
* **Request:** `JSESSIONID` 쿠키 필요
* **Response (204 No Content):** 본문 없음.

---

### 3. 댓글 (Comment) API

**3-1. 댓글 생성 (Lv 7)**
특정 일정에 로그인한 유저의 명의로 댓글을 작성한다.
* **URL:** `/schedules/{scheduleId}/comments`
* **Method:** `POST`
* **Request:** `JSESSIONID` 쿠키 필요
* **Request Body:**

| 필드명 | 타입 | 필수여부 | 제약조건 (Validation) | 설명 |
| --- | --- | --- | --- | --- |
| `content` | String | O | 필수값, 최대 100자 이내 | 댓글 내용 |

* **Response (201 Created):**
    ```json
    {
      "id": 1,
      "scheduleId": 1,
      "userId": 2,
      "content": "도움이 필요하면 알려주세요.",
      "createdAt": "2026-04-21T13:00:00",
      "updatedAt": "2026-04-21T13:00:00"
    }
    ```

**3-2. 전체 댓글 조회 (Lv 7)**
특정 일정에 달린 모든 댓글을 조회한다. (일정 단건 조회와 분리하여 별도 호출이 필요한 경우 사용한다.)
* **URL:** `/schedules/{scheduleId}/comments`
* **Method:** `GET`
* **Response (200 OK):**
  댓글 데이터 배열을 반환한다.

새롭게 추가된 요구사항(Lv 1 ~ Lv 8)과 검증(Validation) 규칙을 모두 반영하여, API에서 발생할 수 있는 전체 에러 메시지를 상태 코드별로 정리했습니다.

### 🚨 400 Bad Request (입력값 검증 실패)
클라이언트가 필수 값을 누락했거나, 글자 수 제한 등의 규칙을 어겼을 때 반환되는 메시지입니다.

**[유저 관련]**
* "유저명은 필수 입력값입니다."
* "유저명은 4글자 이내여야 합니다."
* "이메일은 필수 입력값입니다."
* "이메일 형식이 올바르지 않습니다."
* "비밀번호는 필수 입력값입니다."
* "비밀번호는 8글자 이상이어야 합니다."

**[일정 관련]**
* "할일 제목은 필수 입력값입니다."
* "할일 제목은 10글자 이내여야 합니다."
* "할일 내용은 필수 입력값입니다."

**[댓글 관련]**
* "댓글 내용은 필수 입력값입니다."
* "댓글 내용은 100자 이내여야 합니다."
* "댓글은 최대 10개까지만 작성할 수 있습니다."

---

### 🚫 401 Unauthorized (인증 실패)
로그인이 되어있지 않거나, 로그인 정보가 틀렸을 때 반환되는 메시지입니다.

* "로그인이 필요한 서비스입니다."
* "이메일 또는 비밀번호가 일치하지 않습니다."

---

### ⛔ 403 Forbidden (권한 없음)
로그인은 되어있으나, 남의 일정을 수정/삭제하려고 시도할 때 반환되는 메시지입니다.

* "본인이 작성한 일정만 수정 및 삭제할 수 있습니다."
* "본인이 작성한 댓글만 수정 및 삭제할 수 있습니다."

---

### 🔍 404 Not Found (데이터 없음)
요청한 ID에 해당하는 데이터가 데이터베이스에 존재하지 않을 때 반환되는 메시지입니다.

* "해당 일정을 찾을 수 없습니다."
* "해당 유저를 찾을 수 없습니다."
* "해당 댓글을 찾을 수 없습니다."