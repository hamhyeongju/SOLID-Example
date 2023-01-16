package library.solid.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter @EqualsAndHashCode
public class Loan {

    private Long id;
    private Long bookId;
    private int loanPrice;

    public static Loan createLoan(Long id, Long bookId, int loanPrice, Member member, Book book) {
        Loan loan = new Loan();
        loan.id = id;
        loan.bookId = bookId;
        loan.loanPrice = loanPrice;

        member.getLoans().put(loan.getId(), loan);
        book.minusStockQuantity();

        return loan;
    }

    public static void returnLoan(Long id, Member member, Book book) {
        member.getLoans().remove(id);
        book.plusStockQuantity();
    }
}
