package library.solid.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Member {

    private Long id;
    private String name;
    private Grade grade;

}
