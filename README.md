# SOLID-Example
## 소개
객체 지향 개발 5원칙인 SOLID 의 이해를 높이기 위해 진행한 프로젝트입니다.<br>
간단한 도서 대출 시스템을 SOILD 원칙을 준수하도록 설계하여 개발했습니다.<br>
해당 프로젝트의 전체적인 퀄리티보다 SOLID 원칙을 따라 설계하는 것이 목표였기에 최대한 간단한 예제로 구성했습니다.<br>
의존성 주입(Dependency Injection)을 담당하는 클래스를 구현하여 DIP(의존관계 역전 원칙)을 준수하고자 했습니다.

## 개발환경
* IDE : IntelliJ
* OS : Window 10
* Java 11
* Gradle 7.6

# 의존성
* Lombok : 개발 시 반복 작업을 줄여주는 편의성 라이브러리
* Junit5 : 자바 기반 테스트 프레임워크
* Assertj : 자바 기반 테스트 라이브러리

## 예제

> 요구 사항
* 도서 대출에 관한 정책은 2가지, SOLID 원칙에 따라 클라이언트 코드의 수정 없이 설정 클래스 파일의 변경만으로 서비스를 바꿀 수 있어야 한다.
  * DiscountLoanService : 등급에 따라 대출 요금의 차이가 있으며 대출 한도는 동일하다
  * LimitLoanService : 등급에 따라 대출 한도의 차이가 있으며 대출 요금은 동일하다.
* 책의 재고와 회원의 대출 한도가 남아 있을 경우에만 대출이 가능하다.

**[Member]**
* 회원은 Long id, String name, Grade grade, Map<Loan> loans 필드를 갖는다.
* 회원은 BASIC 혹은 VIP 등급을 갖는다
* 회원은 재고가 남아 있는 책을 대출할 수 있다.
* 회원의 대출 한도 및 요금은 `LoanService(Interface)`에 의해 결정된다. (LoanService 의 구현체는 두 개)

**[Book]**
* 책은 Long id, String name, String author, int price, int stockQuantity의 필드를 가진다.

**[Loan]**
* 대출은 Long id, Long bookId, int loanPrice 필드를 갖는다.

**[Repository]**
* Repository는 도메인 객체의 저장과 조회 기능을 담당한다.

**[LoanService]**
* 해당 서비스는 DiscountLoanService, LimitLoanService 구현체를 가진다.
* 대출 실행 시 대출 가능 여부를 판단하고 대출 요금 책정 및 책 재고 수량 변경, Member에 해당 대출에 대한 참조가 추가된다.
* 반납 시 책의 재고 수량을 변경하고 Member에 해당 대출에 대한 참조를 제거한다.

**[ApplicationInit]**
* 해당 클래스에서 Repository, LoanService에 대하여 의존성 주입의 역할을 담당한다.

## 실행 방법 (윈도우 10, 인텔리제이 기준)
1. 프로젝트 다운
2. 인텔리제이 Open build.gradle (Open as Project)<br>
2-1. gradle을 통해 외부 라이브러리의 의존성이 추가되지 않았다면 우측의 gradle 패널을 펼처 직접 reload
3. ApplicationInit 의 `LoanService loanService()` 메서드에서 DiscountLoanService와 LimitLoanService 중에서 반환할 구현체를 선택
4. `src/test/java/library/solid/test` 해당 패키지 우클릭 후 `Run 'Tests in 'test''` 클릭 (기본 단축키 ctrl+shift+F10)
5. 3.에서 선택한 구현체에 따라 `DiscountLoanTest`와 `LimitLoanTest` 중 하나만 정상적으로 테스트 성공
