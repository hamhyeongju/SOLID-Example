package library.solid.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class Member {

    private Long id;
    private String name;
    private Grade grade;
    private Map<Long, Loan> loans;

}
