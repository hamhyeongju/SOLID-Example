package library.solid.domain;

import lombok.Getter;

import java.util.Map;

@Getter
public class Member {

    private Long id;
    private String name;
    private Grade grade;
    private Map<Long, Loan> loans;

    public static Member createMember(Long id, String name, Grade grade, Map loans) {
        Member member = new Member();
        member.id = id;
        member.name = name;
        member.grade = grade;
        member.loans = loans;

        return member;
    }

}
