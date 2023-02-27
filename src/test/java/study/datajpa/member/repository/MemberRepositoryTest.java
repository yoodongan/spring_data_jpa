package study.datajpa.member.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.member.Member;
import study.datajpa.team.Team;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @PersistenceContext EntityManager em;

    @Test
    public void t1() {
        //given
        Member member = new Member("springA", 20);
        memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById(member.getId()).get();

        //then
        Assertions.assertThat(member).isEqualTo(findMember);
    }
    
    @Test
    public void t2() {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member = new Member("springA", 21);
        member.changeTeam(teamA);
        em.persist(member);
        Member member2 = new Member("springB", 23);
        member2.changeTeam(teamA);
        em.persist(member2);

        em.flush();
        em.clear();

        //when

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member1 : members) {
            System.out.println("member = " + member1);
            System.out.println("== team 초기화 전(지연 로딩) ==");
            System.out.println("member.getTeam() : " + member1.getTeam());
            System.out.println("== team 초기화 후(프록시 객체 초기화, 값 세팅 완료) ==");
        }

    }

}