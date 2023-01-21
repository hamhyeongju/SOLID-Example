package library.solid.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter @EqualsAndHashCode
public class Loan {

    private Long id;
    private int loanPrice;
    private Member member;
    private Book book;

    public static Loan createLoan(Long id, int loanPrice, Member member, Book book) {
        Loan loan = new Loan();
        loan.id = id;
        loan.loanPrice = loanPrice;
        loan.member = member;
        loan.book = book;

        member.getLoans().put(loan.getId(), loan);
        book.minusStockQuantity();

        return loan;
    }
}
