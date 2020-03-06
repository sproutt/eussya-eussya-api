package com.sproutt.eussyaeussyaapi.domain;

import com.sproutt.eussyaeussyaapi.infrastructure.CustomMemberRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {

    Optional<Member> findByMemberId(String memberId);

    Optional<Member> findByName(String name);

    Optional<Member> findByNickName(String email);

    Optional<Member> findByEmail(String email);

    List<Member> findAll();
}
