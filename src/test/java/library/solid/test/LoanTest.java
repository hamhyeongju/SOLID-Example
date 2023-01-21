package library.solid.test;

import library.solid.ApplicationInit;
import library.solid.domain.*;
import library.solid.exception.OutOfStockException;
import library.solid.repository.BookRepository;
import library.solid.repository.MemberRepository;
import library.solid.service.LoanService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * 대출 생성 및 삭제, 도서 재고 부족 예외 처리
 * LoanService 가 바뀌어도 대출 생성/삭제, 핵심 기능은 변함 없이 동작함
 */
public class LoanTest {

    static ApplicationInit init = new ApplicationInit();

    private final MemberRepository memberRepository = init.memberRepository();
    private final BookRepository bookRepository = init.bookRepository();
    private final LoanService loanService = init.loanService(); // 어떤 LoanSevice 가 주입되어도 상관 없음

    @Test @DisplayName("대출 생성/삭제 및 도서 재고 부족 예외")
    public void createLoanAndException() {
        /****** given - 회원, 책 생성 *************/
        Long basicMemberId = memberRepository.save(
                Member.createMember(Sequence.getSequence(), "basicMember", Grade.BASIC));
        Long vipMemberId = memberRepository.save(
                Member.createMember(Sequence.getSequence(), "vipMember", Grade.VIP));

        Long book1Id = bookRepository.save(
                Book.createBook(Sequence.getSequence(), "book1", "author1", 12000, 10));
        Long book2Id = bookRepository.save(
                Book.createBook(Sequence.getSequence(), "book2", "author2", 12000, 0));
        /****** given - 회원, 책 생성 *************/

        /****** when v1 - 대출 실행 *************/
        Loan loan1 = loanService.loan(basicMemberId, book1Id);
        Loan loan2 = loanService.loan(vipMemberId, book1Id);
        /****** when v1 - 대출 실행 *************/

        /****** then v1 - 대출 결과 *************/
        Member basicMember = memberRepository.findById(basicMemberId);
        Member vipMember = memberRepository.findById(vipMemberId);

        // 회원이 가지고 있는 대출과 생성한 대출 비교
        assertThat(basicMember.getLoans().get(loan1.getId())).isEqualTo(loan1);
        assertThat(vipMember.getLoans().get(loan2.getId())).isEqualTo(loan2);

        // 책의 재고 변화, 두 번 대출했으니 10 -> 8이 되어야 함
        assertThat(bookRepository.findById(book1Id).getStockQuantity()).isEqualTo(8);
        /****** then v1 - 대출 결과 *************/

        /****** when v2 - 대출 반납 실행 *************/
        loanService.returnBook(loan1);
        loanService.returnBook(loan2);
        /****** when v2 - 대출 반납 실행 *************/

        /****** then v2 - 대출 반납 결과 *************/
        // book 재고가 다시 + 2
        assertThat(bookRepository.findById(book1Id).getStockQuantity()).isEqualTo(10);

        // 대출 내역 삭제
        assertThat(basicMember.getLoans().get(loan1.getId())).isNull();
        assertThat(vipMember.getLoans().get(loan2.getId())).isNull();
        /****** then v2 - 대출 반납 결과 *************/

        /****** when v3 - 재고 부족 도서 대출 실행 *************/
        assertThatExceptionOfType(OutOfStockException.class)
                .isThrownBy(()-> loanService.loan(vipMemberId, book2Id));
        /****** when v3 - 재고 부족 도서 대출 실행 *************/
    }
}
