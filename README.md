# 키워드 주소 검색 WEB

## 사용 기술
- JAVA 11
- SpringBoot 2.1.1.RELEASE
- Lombok
- JPA
- jQuery 3.3.1
- BootStrap 3.3.7
- Thymeleaf
- PostgreSQL
- H2
- SpringSecurity 2.1.1.RELEASE

## 환경 구성
1. JAVA 11 설치
2. maven 설치 

## 셋팅 방법
### application.properties 에 키 셋팅
> application.properties 에 API KEY 설정

```properties
## 카카오 REST API HEADER
kakao.rest-api-header-name=Authorization

## 카카오 REST API KEY
kakao.rest-api-key=

## 카카오 KEYWORD API URL
kakao.rest-api-keyword-url=https://dapi.kakao.com/v2/local/search/keyword.json

## 카카오 Javascript Key
kakao.javascript-key=
```

## 실행방법
1. Maven Package 빌드
```bash
mvn clean package
``` 

2. `target` 디렉토리에 생성된 `loc-0.0.1-SNAPSHOT.jar` 파일을 `java -jar` 로 실행
```bash
java -jar target/loc-0.0.1-SNAPSHOT.jar
```

3. localhost:8080 에 접속하여 로그인
> 아래 두개의 유저중 하나로 로그인
- user@email.com / user
- admin@email.com / admin

4. H2 Console 접속
> 현재 DB 설정은 스프링부트가 기본 제공하는 H2로 동작하도록 구성되어있음
> 아래의 정보로 H2 Console로 메모리DB에 접근하여 데이터 확인가능
> 메모리DB라 서버 다운시 데이터가 삭제됨
>> 1. `http://localhost:8080/h2-console` 로 H2 Console 접속
>> 2. JDBC URL: `jdbc:h2:mem:testdb`
>>  3. User Name: sa
>>  4. Connect

## 기능 설명
### 키워드 주소 검색
1. 키워드 검색창에 검색요청할 키워드 입력 후 엔터 혹은 검색 버튼 클릭
2. 주소 리스트가 페이징되어 나옴
3. 페이징 사이즈 기본값은 5

### 키워드 검색 순위 TOP 10
1. 최초 로딩시 저장된 키워드 검색 데이터로 순위 데이터 표시
2. 키워드 검색 시 마다 카운트 업데이트하고 재조회하여 적용
3. 키워드 검색 순위 TOP 10 키워드 클릭시 해당 키워드로 즉시 검색 가능

### 키워드 주소를 지도에 표시하기
1. 검색된 키워드 주소 테이블의 데이터중 지도에서 보기 원하는 데이터를 클릭하면 지도에 표시
2. 커스텀 오버레이 창에 장소명, 도로명, 지번 전화번호, 장소 URL 표출

### API 설정
> 현재는 무조건 KAKAO로 파라메터를 넘겨 KAKAO로 처리하도록 되어있지만 
> DB에 ApiType 으로 API 타입을 관리하지 않고 있지만 API를 추가하고 API타입으로 동작하도록 하면
> 변경된 타입에 따라 주소 변경 어댑터로 조회된 API 주소를 정제하여 나타나도록 하여 
> 유동적으로 주소 검색 API 변경이 가능 합니다