package library.solid.test;

import library.solid.ApplicationInit;
import library.solid.domain.Grade;
import library.solid.domain.Member;
import library.solid.repository.MemberRepository;
import library.solid.domain.Sequence;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class MemberTest {

    ApplicationInit init = new ApplicationInit();

    private final MemberRepository memberRepository = init.memberRepository();

    @Test
    public void createMember() {
        //given
        Long long1 = memberRepository.save(Member.createMember(Sequence.getSequence(), "Basic member", Grade.BASIC, new HashMap<>()));
        Long long2 = memberRepository.save(Member.createMember(Sequence.getSequence(), "VIP member", Grade.VIP, new HashMap<>()));

        //when
        Member find1 = memberRepository.findById(long1);
        Member find2 = memberRepository.findById(long2);

        //then
        Assertions.assertThat(find1.getName()).isEqualTo("Basic member");
        Assertions.assertThat(find1.getGrade()).isEqualTo(Grade.BASIC);

        Assertions.assertThat(find2.getName()).isEqualTo("VIP member");
        Assertions.assertThat(find2.getGrade()).isEqualTo(Grade.VIP);

    }
}
