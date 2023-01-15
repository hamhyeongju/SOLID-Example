package library.solid.test;

import library.solid.ApplicationInit;
import library.solid.domain.Grade;
import library.solid.domain.Member;
import library.solid.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemberTest {

    ApplicationInit init = new ApplicationInit();

    private final MemberRepository memberRepository = init.memberRepository();

    @Test
    public void createMember() throws Exception {
        //given
        Member basic_member = memberRepository.save("Basic member", Grade.BASIC);
        Member vip_member = memberRepository.save("VIP member", Grade.VIP);

        //when
        Member find1 = memberRepository.findById(1L);
        Member find2 = memberRepository.findById(2L);

        //then
        Assertions.assertThat(find1.getName()).isEqualTo("Basic member");
        Assertions.assertThat(find1.getGrade()).isEqualTo(Grade.BASIC);

        Assertions.assertThat(find2.getName()).isEqualTo("VIP member");
        Assertions.assertThat(find2.getGrade()).isEqualTo(Grade.VIP);

    }
}
