package study.datajpa.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.datajpa.member.Member;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByNameAndAgeGreaterThan(String name, int age);

    @Query("select m from Member m " +
            "where m.name = :name " +
            "and m.age = :age"
    )
    Member findNameAndAgeIsSame(@Param("name") String name, @Param("age") int age);
}
