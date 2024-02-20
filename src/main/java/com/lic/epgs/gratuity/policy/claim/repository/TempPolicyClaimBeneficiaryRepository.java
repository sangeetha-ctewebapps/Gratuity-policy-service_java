package com.lic.epgs.gratuity.policy.claim.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lic.epgs.gratuity.policy.claim.entity.TempPolicyClaimBeneficiaryEntity;

public interface TempPolicyClaimBeneficiaryRepository extends JpaRepository<TempPolicyClaimBeneficiaryEntity,Long>{

	boolean existsByclaimPropsId(Long claimPropsId);

	@Modifying
	@Query(value = "Delete from PMST_TMP_CLAIM_BENEFICIARY where CLAIM_PROPS_ID=?1",nativeQuery = true)
	void deleteClaimProsID(Long claimPropsId);

	List<TempPolicyClaimBeneficiaryEntity> findByclaimPropsId(Long id);
	
	@Modifying
	@Query(value = "Delete from PMST_TMP_CLAIM_BENEFICIARY where CLAIM_PROPS_ID = :propsId and MEMBER_TMP_BANK_ID = :memberBankId",nativeQuery = true)
	void deleteByMemberBankId(@Param("propsId") Long propsId,@Param("memberBankId") Long memberBankId);

	@Modifying
	@Query(value = "Delete from PMST_TMP_CLAIM_BENEFICIARY where CLAIM_PROPS_ID = :propsId and APPOINTEE_TMP_BANK_ID= :appointeeId",nativeQuery = true)
	void deleteByAppointeeId(@Param("propsId") Long propsId,@Param("appointeeId") Long appointeeId);

	@Modifying
	@Query(value = "Delete from PMST_TMP_CLAIM_BENEFICIARY where CLAIM_PROPS_ID = :propsId and (NOMINEE_TMP_BANK_ID = :nomineeId OR PARENT_ID=:nomineeId)",nativeQuery = true)
	void deleteByNomineeId(@Param("propsId") Long propsId, @Param("nomineeId") Long nomineeId);


}
