package library.solid.test;

import library.solid.ApplicationInit;
import library.solid.domain.*;
import library.solid.exception.OutOfLoanLimitException;
import library.solid.repository.BookRepository;
import library.solid.repository.MemberRepository;
import library.solid.service.LoanService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * DiscountLoanService 정책에 따른 등급별 대출 요금 테스트
 * 공통 정책(회원 등급간 대출 가능 권수 차이 X)
 */
public class DiscountLoanTest {

    static ApplicationInit init = new ApplicationInit();

    private static final MemberRepository memberRepository = init.memberRepository();
    private static final BookRepository bookRepository = init.bookRepository();
    private static LoanService loanService = init.loanService(); // DiscountLoanService 주입

    @Test @DisplayName("등급에 따른 요금")
    public void loanPriceAccordingToGrade() {
        /****** given - 회원, 책 생성 *************/
        Long basicMemberId = memberRepository.save(
                Member.createMember(Sequence.getSequence(), "basicMember", Grade.BASIC));
        Long vipMemberId = memberRepository.save(
                Member.createMember(Sequence.getSequence(), "vipMember", Grade.VIP));

        Long bookId = bookRepository.save(
                Book.createBook(Sequence.getSequence(), "book", "author", 12000, 10));
        /****** given - 회원, 책 생성 *************/

        /****** when v1 - 대출 실행 *************/
        Loan loan1 = loanService.loan(basicMemberId, bookId);
        Loan loan2 = loanService.loan(vipMemberId, bookId);
        /****** when v1 - 대출 실행 *************/

        /****** then v1 - 대출 실행 결과 *************/
        // BASIC 멤버의 Loan
        Loan member1Loan = memberRepository.findById(basicMemberId).getLoans().get(loan1.getId());
        // VIP 멤버의 Loan
        Loan member2Loan = memberRepository.findById(vipMemberId).getLoans().get(loan2.getId());

        // 대출 결과가 회원에게 적용 되었는지 확인
        assertThat(member1Loan).isEqualTo(loan1);
        assertThat(member2Loan).isEqualTo(loan2);

        // BASIC 멤버 - 12000 * 0.2 = 2400
        assertThat(member1Loan.getLoanPrice()).isEqualTo(2400);

        // VIP 멤버 - 12000 * 0.1 = 1200
        assertThat(member2Loan.getLoanPrice()).isEqualTo(1200);
        /****** then v1 - 대출 실행 결과 *************/
    }

    /**
     * 회원 등급간 대출 가능 권수 차이 X
     */
    @Test @DisplayName("공통 정책")
    public void exception() {
        /****** given - 회원, 책 생성 *************/
        Long basicMemberId = memberRepository.save(
                Member.createMember(Sequence.getSequence(), "basicMember", Grade.BASIC));
        Long vipMemberId = memberRepository.save(
                Member.createMember(Sequence.getSequence(), "vipMember", Grade.VIP));

        Long book1Id = bookRepository.save(
                Book.createBook(Sequence.getSequence(), "book1", "author1", 12000, 10));
        /****** given - 회원, 책 생성 *************/

        /****** when v1 - 대출 실행 *************/
        Loan basicLoan1 = loanService.loan(basicMemberId, book1Id);
        Loan basicLoan2 = loanService.loan(basicMemberId, book1Id);
        Loan vipLoan1 = loanService.loan(vipMemberId, book1Id);
        Loan vipLoan2 = loanService.loan(vipMemberId, book1Id);
        /****** when v1 - 대출 실행 *************/

        /****** then v1 - 세번째 대출 결과 *************/
        // 대출 한도 초과 예외 발생
        assertThatExceptionOfType(OutOfLoanLimitException.class)
                .isThrownBy(()-> loanService.loan(basicMemberId, book1Id));
        assertThatExceptionOfType(OutOfLoanLimitException.class)
                .isThrownBy(()-> loanService.loan(vipMemberId, book1Id));
        /****** then v1 - 세번째 대출 결과 *************/
    }
}
