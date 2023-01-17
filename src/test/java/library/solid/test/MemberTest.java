package library.solid.test;

import library.solid.ApplicationInit;
import library.solid.domain.Grade;
import library.solid.domain.Member;
import library.solid.repository.MemberRepository;
import library.solid.domain.Sequence;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.*;

public class MemberTest {

    ApplicationInit init = new ApplicationInit();

    private final MemberRepository memberRepository = init.memberRepository();

    @Test
    public void createMember() {
        /****** given - 회원 생성 *************/
        Member basic_member = Member.createMember(Sequence.getSequence(), "Basic member", Grade.BASIC, new HashMap<>());
        Member vip_member = Member.createMember(Sequence.getSequence(), "VIP member", Grade.VIP, new HashMap<>());
        Long long1 = memberRepository.save(basic_member);
        Long long2 = memberRepository.save(vip_member);
        /****** given - 회원 생성 *************/

        /****** when - 회원 조회 *************/
        // BASIC Member
        Member find1 = memberRepository.findById(long1);
        // VIP Member
        Member find2 = memberRepository.findById(long2);
        /****** when - 회원 조회 *************/

        /****** then - 회원 비교 *************/
        // 리포지토리에 저장이 잘 됐는지, 등급은 맞는 지 비교
        assertThat(basic_member).isEqualTo(find1);
        assertThat(find1.getGrade()).isEqualTo(Grade.BASIC);

        assertThat(vip_member).isEqualTo(find2);
        assertThat(find2.getGrade()).isEqualTo(Grade.VIP);
        /****** then - 회원 비교 *************/

    }
}
