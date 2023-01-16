package library.solid.repository;

import library.solid.domain.Grade;
import library.solid.domain.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemberRepositoryImpl implements MemberRepository{

    private static Map<Long, Member> store = new HashMap<>();

    @Override
    public Long save(Member member) {
        store.put(member.getId(), member);
        return member.getId();
    }

    @Override
    public Member findById(Long id) {
        return store.get(id);
    }

}
