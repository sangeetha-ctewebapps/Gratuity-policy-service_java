package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyDepositEntity;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.PolicyDeposit;

@Repository
public interface MasterPolicyTransferVerRepository extends JpaRepository<MasterPolicyEntity, Long> {
	
	
	@Query(value = "SELECT * FROM  PSTG_DEPOSIT WHERE IS_ACTIVE = 1 AND TMP_POLICY_ID = :tmpPolicyId", nativeQuery = true)
	List<PolicyDeposit> findBytmpPolicyId(Long tmpPolicyId);

}
