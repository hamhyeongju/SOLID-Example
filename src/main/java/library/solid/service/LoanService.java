package library.solid.service;

import library.solid.domain.Loan;

public interface LoanService {

    Loan loan(Long memberId, Long bookId);

    void returnBook(Loan loan);

}
