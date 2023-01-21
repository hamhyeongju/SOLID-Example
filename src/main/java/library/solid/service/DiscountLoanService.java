package library.solid.service;

import library.solid.domain.Book;
import library.solid.domain.Grade;
import library.solid.domain.Loan;
import library.solid.domain.Member;
import library.solid.exception.OutOfLoanLimitException;
import library.solid.exception.OutOfStockException;
import library.solid.repository.BookRepository;
import library.solid.repository.LoanRepository;
import library.solid.repository.MemberRepository;
import library.solid.domain.Sequence;

/**
 * BASIC - 책 가격의 20% 요금 책정
 * VIP - 책 가격의 10% 요금 책정
 *
 * 공통 정책 : 책은 두 권 대출 가능
 */
public class DiscountLoanService implements LoanService{

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    public DiscountLoanService(MemberRepository memberRepository, BookRepository bookRepository, LoanRepository loanRepository) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
    }

    @Override
    public Loan loan(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId);
        Book book = bookRepository.findById(bookId);

        if (member.getLoans().size() >= 2) throw new OutOfLoanLimitException();
        if (book.getStockQuantity() == 0) throw new OutOfStockException();

        int loanPrice = Grade.BASIC.equals(member.getGrade()) ?
                book.getPrice() / 5 : book.getPrice() / 10;

        return Loan.createLoan(Sequence.getSequence(), loanPrice, member, book);
    }

    @Override
    public void returnBook(Loan loan) {
        loanRepository.delete(loan);
    }
}
