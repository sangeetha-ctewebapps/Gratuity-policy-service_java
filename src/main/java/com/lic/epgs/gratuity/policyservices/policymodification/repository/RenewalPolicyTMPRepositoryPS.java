package com.lic.epgs.gratuity.policyservices.policymodification.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;

public interface RenewalPolicyTMPRepositoryPS extends JpaRepository<RenewalPolicyTMPEntity, Long> {

	
	
	
	@Query(value="SELECT * FROM PMST_TMP_POLICY ptp WHERE POLICY_NUMBER =?1 and POLICY_STATUS_ID =?2 ",nativeQuery = true)
	RenewalPolicyTMPEntity getByPolicyNubmber(String policyNumber, Long statusId);


	Optional<RenewalPolicyTMPEntity> findByPolicyNumberAndPolicyStatusId(String policyNumber, Long pendingForApproval);

	@Query(value="SELECT * FROM PMST_TMP_POLICY ptp WHERE POLICY_NUMBER =?1 and ( POLICY_STATUS_ID =?2 or POLICY_STATUS_ID =?3) ",nativeQuery = true)
	RenewalPolicyTMPEntity getByPolicyNubmberForSave(String policyNumber, Long draft, Long sendToMaker);

	
}
