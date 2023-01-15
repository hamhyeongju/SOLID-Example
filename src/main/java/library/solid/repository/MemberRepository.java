package library.solid.repository;

import library.solid.domain.Member;

public interface MemberRepository {

    void save(Member member);

    Member findById(Long id);
}
