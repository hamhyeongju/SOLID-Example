package library.solid.repository;

import library.solid.domain.Grade;
import library.solid.domain.Member;

import java.util.HashMap;
import java.util.Map;

public class MemberRepositoryImpl implements MemberRepository{

    private static Map<Long, Member> store = new HashMap<>();

    @Override
    public Member save(String name, Grade grade) {
        Member member = new Member(Sequence.getSequence(), name, grade);
        return store.put(member.getId(), member);
    }

    @Override
    public Member findById(Long id) {
        return store.get(id);
    }

}
