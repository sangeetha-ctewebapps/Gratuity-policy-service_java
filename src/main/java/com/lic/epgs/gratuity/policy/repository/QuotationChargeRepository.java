package com.lic.epgs.gratuity.policy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.entity.QuotationChargeEntity;

/**
 * @author Gopi
 *
 */

@Repository
public interface QuotationChargeRepository extends JpaRepository<QuotationChargeEntity, Long>{
	
	List<QuotationChargeEntity> findAllByMasterQuotationId(Long id);

	@Query(value = "SELECT CEIL(sum(AMOUNT_CHARGED)) AS SUMAMOUNT FROM QUOTATION_CHARGE WHERE MASTER_QUOTATION_ID =?1", nativeQuery=true)
	List<Double> findSumAmountCharged(Long id);
	
	@Query(value = "SELECT CEIL(AMOUNT_CHARGED) AS SUMAMOUNT FROM QUOTATION_CHARGE WHERE QUOTATION_CHARGE_ID  =?1", nativeQuery=true)
	Long SumAmountCharged(Long id);

	@Query(value = "SELECT CEIL(sum(AMOUNT_CHARGED)) AS SUMAMOUNT FROM QUOTATION_CHARGE WHERE TMP_POLICY_ID =?1", nativeQuery=true)
	List<Double> findSumAmountChargedBasedTmpPolicy(Long tmpPolicyId);
	
	@Query(value = "SELECT * FROM QUOTATION_CHARGE WHERE TMP_POLICY_ID =?1",nativeQuery = true)
	List<QuotationChargeEntity> findAllByTmpPolicyId(Long tmpPolicyId);
	
}
