package library.solid.repository;

import library.solid.domain.Grade;
import library.solid.domain.Member;

public interface MemberRepository {

    Long save(Member member);

    Member findById(Long id);
}
