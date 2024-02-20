package com.lic.epgs.gratuity.policy.renewal.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.renewal.entity.EmailMessagesEntity;
import com.lic.epgs.gratuity.policy.renewal.entity.PolicyRenewalRemainderEntity;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeEntity;
import com.lic.epgs.gratuity.quotation.document.entity.MasterDocumentEntity;

@Repository
public interface PolicyRenewalRemainderRepository extends JpaRepository<PolicyRenewalRemainderEntity, Long> {

	@Query(value = "SELECT * FROM PMST_POLICY WHERE TRUNC(ANNUAL_RENEWAL_DATE) - TRUNC(CURRENT_DATE) = ?1", nativeQuery = true)
	List<MasterPolicyEntity> findNotInPolicyRenewal(String renewalday);


		@Query(value = "SELECT * FROM PMST_POLICY WHERE TRUNC(ANNUAL_RENEWAL_DATE) - TRUNC(CURRENT_DATE) = ?1", nativeQuery = true)
		List<MasterPolicyEntity> findNotInPolicyRenewalRemainder(String remainderday);


		PolicyRenewalRemainderEntity findBypolicyId(Long policyId);

		@Query(value = "SELECT * FROM PMST_RENEWAL_REMINDERS WHERE POLICY_ID=?1 and EMAIL_SUBJECT=?2 and(ANNUAL_RENEWAL_DATE =?3 OR IS_ACTIVE=1)", nativeQuery = true)
		List<PolicyRenewalRemainderEntity> findByPolicyandType(Long id, String subject, Date date);

	

}