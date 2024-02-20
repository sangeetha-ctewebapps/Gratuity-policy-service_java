package com.lic.epgs.gratuity.policy.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberAppointeeEntity;

/**
 * @author Ismail Khan A
 *
 */

@Repository
public interface PolicyMemberAppointeeRepository extends JpaRepository<PolicyMemberAppointeeEntity, Long> {
	@Query(value = "UPDATE PMST_MEMBER_APPOINTEE m SET m.IS_ACTIVE=0 WHERE m.MEMBER_ID IN "
			+ "(SELECT MEMBER_ID FROM PMST_MEMBER WHERE POLICY_ID=?1)", nativeQuery=true)
	void inactiveByPolicyId(Long policyId);
	
	@Modifying
	@Query(value = "DELETE FROM PMST_MEMBER_APPOINTEE ma WHERE MEMBER_ID IN "
		+ "(SELECT MEMBER_ID FROM PMST_MEMBER WHERE MEMBER_BATCH_ID=?1)", nativeQuery=true)
	void deleteByBatchId(Long batchId);
}