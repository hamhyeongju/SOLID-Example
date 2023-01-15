package library.solid.repository;

import library.solid.domain.Member;

import java.util.Map;

public class MemberRepositoryImpl implements MemberRepository{

    public static Map<Long, Member> store;

    public void save(Member member) {
    }

    @Override
    public Member findById(Long id) {
        return null;
    }

}
