package com.lic.epgs.gratuity.policyservices.conversion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberAddressEntity;

@Repository
public interface PsPolicyMemberAddressRepository extends JpaRepository<PolicyMemberAddressEntity, Long> {
	@Query(value = "SELECT d.* FROM PMST_MEMBER_ADDRESS d WHERE MEMBER_ID =:memberId", nativeQuery = true)
	List<PolicyMemberAddressEntity> findAllByMemberId(@Param("memberId") Long memberId);
}
