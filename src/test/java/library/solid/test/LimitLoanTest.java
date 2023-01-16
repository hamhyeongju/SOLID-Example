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
    private static LoanService loanService;

    @Test
    @DisplayName("등급에 따른 대출 한도")
    public void limitAccordingToGrade() {
        /****** given - 회원, 책 생성 *************/
        loanService = init.loanService(); // LimitLoanService 주입

        Long basicMemberId = memberRepository.save(
                new Member(Sequence.getSequence(), "basicMember", Grade.BASIC, new HashMap<>()));
        Long vipMemberId = memberRepository.save(
                new Member(Sequence.getSequence(), "vipMember", Grade.VIP, new HashMap<>()));

        Long bookId = bookRepository.save(
                new Book(Sequence.getSequence(), "book", "author", 12000, 10));
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

    @Test @DisplayName("도서 재고 예외 및 공통 정책")
    public void exception() {
        /****** given - 회원, 책 생성 *************/
        loanService = init.loanService(); // LimitLoanService 주입

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
        Loan loan2 = loanService.loan(vipMemberId, book1Id);
        /****** when v1 - 대출 실행 *************/

        /****** then v1 - 대출 결과 *************/
        // BASIC 멤버
        Loan member1Loan = memberRepository.findById(basicMemberId).getLoans().get(loan1.getId());
        // VIP 멤버
        Loan member2Loan = memberRepository.findById(vipMemberId).getLoans().get(loan2.getId());

        // 대출 결과가 회원에게 적용 되었는지 확인
        assertThat(member1Loan).isEqualTo(loan1);
        assertThat(member2Loan).isEqualTo(loan2);

        // book 의 재고 10에서 - 2
        assertThat(bookRepository.findById(book1Id).getStockQuantity()).isEqualTo(8);

        // BASIC 멤버 - 12000 * 0.1 = 1200
        assertThat(member1Loan.getLoanPrice()).isEqualTo(1200);

        // VIP 멤버 - 12000 * 0.1 = 1200
        assertThat(member2Loan.getLoanPrice()).isEqualTo(1200);
        /****** then v1 - 대출 결과 *************/


        /****** when v2 - 대출 반납 실행 *************/
        loanService.returnBook(member1Loan.getId(), basicMemberId, book1Id);
        loanService.returnBook(member2Loan.getId(), vipMemberId, book1Id);
        /****** when v2 - 대출 반납 실행 *************/

        /****** then v2 - 대출 반납 결과 *************/
        // book 재고가 다시 + 2
        assertThat(bookRepository.findById(book1Id).getStockQuantity()).isEqualTo(10);

        Loan member1LoanAfterReturn = memberRepository.findById(basicMemberId).getLoans().get(loan1.getId());
        Loan member2LoanAfterReturn = memberRepository.findById(vipMemberId).getLoans().get(loan2.getId());

        // 대출 내역 삭제
        assertThat(member1LoanAfterReturn).isNull();
        assertThat(member2LoanAfterReturn).isNull();
        /****** then v2 - 대출 반납 결과 *************/

        /****** when v3 - 재고 부족 도서 대출 실행 *************/
        assertThatExceptionOfType(OutOfStockException.class)
                .isThrownBy(()-> loanService.loan(vipMemberId, book2Id));
        /****** when v3 - 재고 부족 도서 대출 실행 *************/
    }
}
