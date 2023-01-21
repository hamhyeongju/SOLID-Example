package library.solid.test;

import library.solid.ApplicationInit;
import library.solid.domain.*;
import library.solid.exception.OutOfLoanLimitException;
import library.solid.exception.OutOfStockException;
import library.solid.repository.BookRepository;
import library.solid.repository.MemberRepository;
import library.solid.service.LoanService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class LimitLoanTest {

    static ApplicationInit init = new ApplicationInit();

    private static final MemberRepository memberRepository = init.memberRepository();
    private static final BookRepository bookRepository = init.bookRepository();
    private static LoanService loanService = init.loanService(); // LimitLoanService 주입

    @Test
    @DisplayName("등급에 따른 대출 한도")
    public void limitAccordingToGrade() {
        /****** given - 회원, 책 생성 *************/
        Long basicMemberId = memberRepository.save(
                Member.createMember(Sequence.getSequence(), "basicMember", Grade.BASIC));
        Long vipMemberId = memberRepository.save(
                Member.createMember(Sequence.getSequence(), "vipMember", Grade.VIP));

        Long bookId = bookRepository.save(
                Book.createBook(Sequence.getSequence(), "book", "author", 12000, 10));
        /****** given - 회원, 책 생성 *************/

        /****** when v1 - BASIC 회원 대출 실행 *************/
        loanService.loan(basicMemberId, bookId);
        assertThatExceptionOfType(OutOfLoanLimitException.class)
                .isThrownBy(()-> loanService.loan(basicMemberId, bookId));
        /****** when v1 - BASIC 회원 대출 실행 *************/

        /****** when v2 - VIP 회원 대출 실행 *************/
        loanService.loan(vipMemberId, bookId);
        loanService.loan(vipMemberId, bookId);
        loanService.loan(vipMemberId, bookId);
        assertThatExceptionOfType(OutOfLoanLimitException.class)
                .isThrownBy(()-> loanService.loan(vipMemberId, bookId));
        /****** when v2 - VIP 회원 대출 실행 *************/

    }

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
        Loan loan1 = loanService.loan(basicMemberId, book1Id);
        Loan loan2 = loanService.loan(vipMemberId, book1Id);
        /****** when v1 - 대출 실행 *************/

        /****** then v1 - 대출 결과 *************/
        // BASIC 멤버의 Loan
        Loan basicLoan = memberRepository.findById(basicMemberId).getLoans().get(loan1.getId());
        // VIP 멤버의 Loan
        Loan vipLoan = memberRepository.findById(vipMemberId).getLoans().get(loan2.getId());

        // BASIC 멤버 - 12000 * 0.1 = 1200
        assertThat(basicLoan.getLoanPrice()).isEqualTo(1200);

        // VIP 멤버 - 12000 * 0.1 = 1200
        assertThat(vipLoan.getLoanPrice()).isEqualTo(1200);
        /****** then v1 - 대출 결과 *************/
    }
}
