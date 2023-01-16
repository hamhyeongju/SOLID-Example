package library.solid.service;

import library.solid.domain.*;
import library.solid.exception.OutOfLoanLimitException;
import library.solid.exception.OutOfStockException;
import library.solid.repository.BookRepository;
import library.solid.repository.MemberRepository;

/**
 * BASIC - 최대 1권 대출 가능
 * VIP - 최대 3권 대출 가능
 *
 * 공통 정책 - 책 가격의 10% 요금
 */
public class LimitLoanService implements LoanService{

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public LimitLoanService(MemberRepository memberRepository, BookRepository bookRepository) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Loan loan(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId);
        Book book = bookRepository.findById(bookId);

        if (book.getStockQuantity() == 0) throw new OutOfStockException();
        if (member.getGrade().equals(Grade.BASIC)) {
            if (member.getLoans().size() >= 1) throw new OutOfLoanLimitException();
        } else {
            if (member.getLoans().size() >= 3) throw new OutOfLoanLimitException();
        }

        return Loan.createLoan(Sequence.getSequence(), bookId, book.getPrice() / 10, member, book);
    }

    @Override
    public void returnBook(Long loanId, Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId);
        Book book = bookRepository.findById(bookId);
        Loan.returnLoan(loanId, member, book);
    }
}
