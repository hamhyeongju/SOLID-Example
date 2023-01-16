package library.solid.service;

import library.solid.domain.Book;
import library.solid.domain.Grade;
import library.solid.domain.Loan;
import library.solid.domain.Member;
import library.solid.exception.OutOfLoanLimitException;
import library.solid.exception.OutOfStockException;
import library.solid.repository.BookRepository;
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

    public DiscountLoanService(MemberRepository memberRepository, BookRepository bookRepository) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Loan loan(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId);
        Book book = bookRepository.findById(bookId);

        if (member.getLoans().size() >= 2) throw new OutOfLoanLimitException();
        if (book.getStockQuantity() == 0) throw new OutOfStockException();

        int loanPrice = Grade.BASIC.equals(member.getGrade()) ?
                book.getPrice() / 20 : book.getPrice() / 10;

        return Loan.createLoan(Sequence.getSequence(), bookId, loanPrice, member, book);
    }

    @Override
    public void returnBook(Long loanId, Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId);
        Book book = bookRepository.findById(bookId);
        Loan.returnLoan(loanId, member, book);
    }
}
