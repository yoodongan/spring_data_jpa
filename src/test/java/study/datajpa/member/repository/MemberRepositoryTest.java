package study.datajpa.member.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.member.Member;
import study.datajpa.member.MemberDto;
import study.datajpa.team.Team;

import java.util.Arrays;
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

    @Test
    @DisplayName("쿼리 메서드 테스트")
    public void t3() {
        //given
        Member member1 = new Member("springA", 30);
        Member member2 = new Member("springA", 14);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> members = memberRepository.findByNameAndAgeGreaterThan("springA", 14);

        //then
        members.stream()
                        .forEach(System.out::println);
        Assertions.assertThat(members.size()).isEqualTo(1);
    }
    @Test
    @DisplayName("@Query 메서드 테스트")
    public void t4() {
        //given
        Member member1 = new Member("springA", 30);
        Member member2 = new Member("springA", 14);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        Member member = memberRepository.findNameAndAgeIsSame("springA", 14);

        //then
        Assertions.assertThat(member.getName()).isEqualTo(member2.getName());
        Assertions.assertThat(member.getAge()).isEqualTo(member2.getAge());
    }
    @Test
    public void t5() {
        //given
        Member member1 = new Member("springA", 30);
        memberRepository.save(member1);

        //when
        boolean flag = memberRepository.existsMemberByName("springA");

        //then
        Assertions.assertThat(flag).isEqualTo(true);
    }
    @Test
    @DisplayName("DTO 테스트")
    public void t6() {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member = new Member("springA", 21);
        member.changeTeam(teamA);
        em.persist(member);

        //when
        List<MemberDto> memberDto = memberRepository.findMemberDto();
        memberDto.forEach(System.out::println);

        //then
    }
    @Test
    public void t7() {
        //given
        Member member1 = new Member("springA", 21);
        Member member2 = new Member("springB", 23);
        Member member3 = new Member("springC", 18);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when
        List<Member> byNames = memberRepository.findByNames(Arrays.asList("springA", "springC"));
        for (Member byName : byNames) {
            System.out.println(byName.getName());
        }
        //then
    }

    

}