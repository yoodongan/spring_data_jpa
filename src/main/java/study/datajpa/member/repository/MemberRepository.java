package study.datajpa.member.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.datajpa.member.Member;
import study.datajpa.member.MemberDto;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    public List<Member> findByNameAndAgeGreaterThan(String name, int age);

    public boolean existsMemberByName(String name);

    @Query("select m from Member m " +
            "where m.name = :name and m.age = :age")
    public Member findNameAndAgeIsSame(@Param("name") String name, @Param("age") int age);

    @Query("select m.name from Member m")
    public List<String> findNameList();

    @Query("select new study.datajpa.member.MemberDto(m.id, m.name, t.name) "
            +"from Member m join m.team t")
    public List<MemberDto> findMemberDto();

    @Query("select m from Member m "
            +"where m.name in :names")
    public List<Member> findByNames(List<String> names);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 " +
    "where m.age >= :age")
    public int bulkAgePlus(@Param("age") int age);


    @Query("select m from Member m left join fetch m.team")
    public List<Member> findMemberFetchJoin();
}