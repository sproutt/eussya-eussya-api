package com.sproutt.eussyaeussyaapi.domain.mission;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
    List<Mission> findAllByWriter(Member writer);
}
