package library.solid.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class BookLoan {

    private Long id;
    private int count;
    private int loanPrice;
}
