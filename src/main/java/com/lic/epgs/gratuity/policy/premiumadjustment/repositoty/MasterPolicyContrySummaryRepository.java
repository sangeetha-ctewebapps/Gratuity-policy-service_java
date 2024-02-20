package com.lic.epgs.gratuity.policy.premiumadjustment.repositoty;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyContributionEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyContrySummaryEntity;

public interface MasterPolicyContrySummaryRepository extends JpaRepository<MasterPolicyContrySummaryEntity,Long> {

	List<MasterPolicyContrySummaryEntity> findBypolicyId(Long id);

	
	@Query(value= "SELECT * FROM PMST_CONTRIBUTION WHERE MASTER_POLICY_ID =:mastrPolicyId AND FINANCIAL_YEAR =:financialYear AND ROWNUM=1",nativeQuery = true)
	MasterPolicyContributionEntity findByPolicyIdAndFinancial(@Param("mastrPolicyId") Long mastrPolicyId,@Param("financialYear") String financialYear);


	@Modifying
	@Query(value="UPDATE PMST_CONTRI_SUMMARY SET IS_ACTIVE = 0 where CONTRIBUTION_DETAIL_ID IN "
			+ "(select CONTRIBUTION_DETAIL_ID from pmst_contribution_detail"
			+ " where master_policy_id = ?1 AND ENTRY_TYPE = 'NB')",nativeQuery = true)
	void isactivefalse(Long id);

}
