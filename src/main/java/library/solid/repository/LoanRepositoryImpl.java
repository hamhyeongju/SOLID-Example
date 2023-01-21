package library.solid.repository;

import library.solid.domain.Loan;

import java.util.HashMap;
import java.util.Map;

public class LoanRepositoryImpl implements LoanRepository{

    public static Map<Long, Loan> store = new HashMap<>();

    @Override
    public Long save(Loan loan) {
        store.put(loan.getId(), loan);
        return loan.getId();
    }

    @Override
    public Loan findById(Long id) {
        return store.get(id);
    }

    @Override
    public void delete(Long id) {
        Loan findLoan = findById(id);
        findLoan.getBook().plusStockQuantity();
        findLoan.getMember().getLoans().remove(id);
        store.remove(id);
    }
}
