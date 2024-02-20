package com.lic.epgs.gratuity.policy.renewalpolicy.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberBankAccountEntity;

public interface RenewalPolicyTMPMemberBankRepository extends JpaRepository<RenewalPolicyTMPMemberBankAccountEntity, Long>{

	@Modifying
	@Query(value = "DELETE FROM PMST_TMP_MEMBER_BANK_ACCOUNT WHERE MEMBER_BANK_ACCOUNT_ID=:id", nativeQuery = true)
	void deleteByBankAccount(@Param("id") Long id);

}
