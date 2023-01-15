package library.solid.repository;

import library.solid.domain.Grade;
import library.solid.domain.Member;

public interface MemberRepository {

    public Member save(String name, Grade grade);

    Member findById(Long id);
}
