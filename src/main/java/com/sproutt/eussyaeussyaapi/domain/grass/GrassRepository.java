package com.sproutt.eussyaeussyaapi.domain.grass;

import com.sproutt.eussyaeussyaapi.domain.activity.Activity;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GrassRepository extends JpaRepository<Grass, Long> {
    List<Grass> findAllByMember(Member member);

    Grass findByActivity(Activity activity);

    @Query(value = "SELECT g.* FROM grass g join member m on g.member_id = m.id WHERE m.id = :memberId AND GENERATED_TIME BETWEEN :start AND :end ", nativeQuery = true)
    List<Grass> findByMemberDuring(@Param("memberId") Long memberId, @Param("start") LocalDate start, @Param("end") LocalDate end);

}
