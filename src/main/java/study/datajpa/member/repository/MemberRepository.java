package study.datajpa.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.datajpa.member.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
