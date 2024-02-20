package com.lic.epgs.gratuity.policyservices.conversion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberAppointeeEntity;

@Repository
public interface PsPolicyMemberAppointeeRepository extends JpaRepository<PolicyMemberAppointeeEntity, Long> {
	@Query(value = "SELECT d.* FROM PMST_MEMBER_APPOINTEE d WHERE MEMBER_ID =:memberId", nativeQuery = true)
	List<PolicyMemberAppointeeEntity> findAllByMemberId(@Param("memberId") Long memberId);
}
