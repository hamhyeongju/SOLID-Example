package library.solid.repository;

import library.solid.domain.Grade;
import library.solid.domain.Member;

import java.util.HashMap;
import java.util.Map;

public class MemberRepositoryImpl implements MemberRepository{

    private static Map<Long, Member> store = new HashMap<>();
    private static Long sequence = 0L;

    @Override
    public Member save(String name, Grade grade) {
        return store.put(++sequence, new Member(sequence, name, grade));
    }

    @Override
    public Member findById(Long id) {
        return store.get(id);
    }

}
