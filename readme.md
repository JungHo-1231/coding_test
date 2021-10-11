# 오아시스 비즈니스 코딩테스트

## 목차

- [개발 환경](#개발-환경)
- [기능 요구사항](#기능-요구사항)
- [구현](#구현)

## 개발 환경

- 기본 환경
    - IDE: IntelliJ IDEA Ultimate
    - OS: Window 10
    - GIT
- Server && Library
      - Java 11
    - Spring Boot 2.5.5
    - JPA
    - H2
    - Gradle
    - Junit5
    - QueryDsl
    - Model Mapper

## 기능 요구사항

[요건]

1. 세부 요건은 자신이 가정해서 푸시면 됩니다. (README에 작성.)
2. DB는 H2 를 이용해서 해주세요.
3. RESTAPI 구현

   a. 회원 등록, 조회

   b. 주문 등록, 조회

   c. 배송 등록, 취소

   d. 상품 조회

4. 테스트 코드 작성
5. (옵션) QueryDSL 구현 가능

# 구현

## API Specifications

1. 회원

| Action | API | Parameter | Body | Success Response | Fail Response |
|--------|-----|-----------|------|------------------|---------------|
| 회원 id 값으로 조회 | GET /api/v1/member/{id}  | id=[Long] | N/A | Status 200 {"name": "Username", "addressCity":"address","addressZipcode":"zipcode"} | {"code":"Bad", "message" : "errorMessage"} |
| 회원 전체 조회 | GET /api/v1/members| N/A | N/A | Status 200  [{"name": "Username", "addressCity":"address","addressZipcode":"zipcode"}, {"name": "Username", "addressCity":"address","addressZipcode":"zipcode"}]  | {"code":"Bad", "message" : "errorMessage"}|
| 회원 등록  | /api/v1/member  | N/A | {"name": "Username", "addressCity":"address","addressZipcode":"zipcode" } | Status 200 CreateResponseDto{"id": 1} |  {"code":"Bad", "message" : "errorMessage"}|

2. 상품

- 상품은 편의상 Book으로 구현

| Action | API | Parameter | Body | Success Response | Fail Response |
|--------|-----|-----------|------|------------------|---------------|
| 상품 id 값으로 조회 | GET /api/v1/item/{id}  | id=[Long] | N/A | Status 200 {"name": "Username", "addressCity":"address","addressZipcode":"zipcode"} | {"code":"Bad", "message" : "errorMessage"} |
| 회원 전체 조회 | GET /api/v1/members| N/A | N/A | Status 200  [{"name": "Username", "addressCity":"address","addressZipcode":"zipcode"}, {"name": "Username", "addressCity":"address","addressZipcode":"zipcode"}]  | {"code":"Bad", "message" : "errorMessage"}|
| 상품 등록  | /api/v1/member  | N/A | { "name": "jungho", "price": 10_000, "stockQuantity": 10, "author": "jung", "isbn": "1234" } | Status 200 CreateResponseDto{"id": 1} |  {"code":"Bad", "message" : "errorMessage"}| 

3. 주문

| Action | API | Parameter | Body | Success Response | Fail Response |
|--------|-----|-----------|------|------------------|---------------|
|  주문 취소 | POST /api/v1/{orderId}/cancel  | id=[Long] | N/A | Status 200 | {"code":"Bad", "message" : "errorMessage"} |
|  주문 검색 | GET /api/v1/orders/search | N/A |{ "memberName": "", "orderStatus": "ORDER" } | Status 200  [{ "id": 0, "orderDate": "2021-10-11 16:44:19", "member": { "id": 0, "name": "", "address": { "city": "", "street": "", "zipcode": "" } }, "orderItems": [ { "id": 0, "orderPrice": 0, "count": 0, "itemName": "" } ], "orderStatus": "ORDER" } | {"code":"Bad", "message" : "errorMessage"}| | 주문 등록  | /api/v1/order | N/A | { "memberId": 1, "itemId": 2, "count": 2, "addressCity": "경기", "addressStreet": "123", "addressZipcode": "456" } | Status 200 CreateResponseDto{"id": 1}] |  {"code":"Bad", "message" : "errorMessage"}| } | Status 200  [{"name": "Username", "addressCity":"address","addressZipcode":"zipcode"}, {"name": "Username", "addressCity":"address","addressZipcode":"zipcode"}]  | {"code":"Bad", "message" : "errorMessage"}|
|  주문 등록  | /api/v1/order | N/A | { "memberId": 1, "itemId": 2, "count": 2, "addressCity": "경기", "addressStreet": "123", "addressZipcode": "456" } | Status 200 CreateResponseDto{"id": 1} |  {"code":"Bad", "message" : "errorMessage"}| 

- 배송 등록은 주문과 배송이 1:1 관계이므로 주문이 생성 될 때 배송이 같이 등록 됨으로 생략한다.
- 배송 취소는 주문과 배송이 1:1 관계이므로 주문이 취소될때 배송도 같이 취소됨으로 생략한다.