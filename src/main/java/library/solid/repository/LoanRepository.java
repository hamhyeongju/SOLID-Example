package library.solid.repository;

import library.solid.domain.Loan;

public interface LoanRepository {

    Long save(Loan loan);

    Loan findById(Long id);

    void delete(Long id);
}
