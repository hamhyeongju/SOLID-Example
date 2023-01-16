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

import static org.assertj.core.api.Assertions.*;

public class DiscountLoanTest {

    static ApplicationInit init = new ApplicationInit();

    private static final MemberRepository memberRepository = init.memberRepository();
    private static final BookRepository bookRepository = init.bookRepository();
    private static LoanService loanService;

    @Test @DisplayName("등급에 따른 요금")
    public void createAndReturnLoan() {
        /****** given - 회원, 책 생성 *************/
        loanService = init.loanService(); // DiscountLoanService 주입

        Long basicMemberId = memberRepository.save(
                new Member(Sequence.getSequence(), "basicMember", Grade.BASIC, new HashMap<>()));
        Long vipMemberId = memberRepository.save(
                new Member(Sequence.getSequence(), "vipMember", Grade.VIP, new HashMap<>()));

        Long bookId = bookRepository.save(
                new Book(Sequence.getSequence(), "book", "author", 12000, 10));
        /****** given - 회원, 책 생성 *************/

        /****** when v1 - 대출 실행 *************/
        Loan loan1 = loanService.loan(basicMemberId, bookId);
        Loan loan2 = loanService.loan(vipMemberId, bookId);
        /****** when v1 - 대출 실행 *************/

        /****** then v1 - 대출 실행 결과 *************/
        // BASIC 멤버
        Loan member1Loan = memberRepository.findById(basicMemberId).getLoans().get(loan1.getId());
        // VIP 멤버
        Loan member2Loan = memberRepository.findById(vipMemberId).getLoans().get(loan2.getId());

        // 대출 결과가 회원에게 적용 되었는지 확인
        assertThat(member1Loan).isEqualTo(loan1);
        assertThat(member2Loan).isEqualTo(loan2);

        // book 의 재고 10에서 - 2
        assertThat(bookRepository.findById(bookId).getStockQuantity()).isEqualTo(8);

        // BASIC 멤버 - 12000 * 0.2 = 2400
        assertThat(member1Loan.getLoanPrice()).isEqualTo(2400);

        // VIP 멤버 - 12000 * 0.1 = 1200
        assertThat(member2Loan.getLoanPrice()).isEqualTo(1200);
        /****** then v1 - 대출 실행 결과 *************/

        /****** when v2 - 대출 반납 실행 *************/
        loanService.returnBook(member1Loan.getId(), basicMemberId, bookId);
        loanService.returnBook(member2Loan.getId(), vipMemberId, bookId);
        /****** when v2 - 대출 반납 실행 *************/

        /****** then v2 - 대출 반납 결과 *************/
        // book 재고가 다시 + 2
        assertThat(bookRepository.findById(bookId).getStockQuantity()).isEqualTo(10);

        Loan member1LoanAfterReturn = memberRepository.findById(basicMemberId).getLoans().get(loan1.getId());
        Loan member2LoanAfterReturn = memberRepository.findById(vipMemberId).getLoans().get(loan2.getId());

        // 대출 내역 삭제
        assertThat(member1LoanAfterReturn).isNull();
        assertThat(member2LoanAfterReturn).isNull();
        /****** then v2 - 대출 반납 결과 *************/
    }

    @Test @DisplayName("대출 한도 및 도서 재고 예외")
    public void exception() {
        /****** given - 회원, 책 생성 *************/
        loanService = init.loanService(); // DiscountLoanService 주입

        Long basicMemberId = memberRepository.save(
                new Member(Sequence.getSequence(), "basicMember", Grade.BASIC, new HashMap<>()));
        Long vipMemberId = memberRepository.save(
                new Member(Sequence.getSequence(), "vipMember", Grade.VIP, new HashMap<>()));

        Long book1Id = bookRepository.save(
                new Book(Sequence.getSequence(), "book1", "author1", 12000, 10));
        Long book2Id = bookRepository.save(
                new Book(Sequence.getSequence(), "book2", "author2", 12000, 0));
        /****** given - 회원, 책 생성 *************/

        /****** when v1 - 대출 실행 *************/
        Loan loan1 = loanService.loan(basicMemberId, book1Id);
        Loan loan2 = loanService.loan(basicMemberId, book1Id);
        /****** when v1 - 대출 실행 *************/

        /****** then v1 - 세번째 대출 결과 *************/
        // 대출 한도 초과 예외 발생
        assertThatExceptionOfType(OutOfLoanLimitException.class)
                .isThrownBy(()-> loanService.loan(basicMemberId, book1Id));
        /****** then v1 - 세번째 대출 결과 *************/

        /****** when v2 - 재고 부족 도서 대출 실행 *************/
        assertThatExceptionOfType(OutOfStockException.class)
                .isThrownBy(()-> loanService.loan(vipMemberId, book2Id));
        /****** when v2 - 재고 부족 도서 대출 실행 *************/
    }
}
