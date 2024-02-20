package com.lic.epgs.gratuity.policy.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberEntity;

@Repository
public interface HistoryMemberRepository extends JpaRepository<HistoryMemberEntity, Long> {


	@Query(value="SELECT * From PMST_HIS_MEMBER where POLICY_ID=?1 and PMST_MEMBER_ID=?2", nativeQuery =true)
	HistoryMemberEntity findBypolicyIdandpmstMemberId(Long id, Long pmstMemberId);



}
