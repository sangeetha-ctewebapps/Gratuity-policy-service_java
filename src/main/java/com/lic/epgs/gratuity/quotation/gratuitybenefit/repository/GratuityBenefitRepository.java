package com.lic.epgs.gratuity.quotation.gratuitybenefit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitEntity;

/**
 * @author Ismail Khan A
 *
 */

@Repository
public interface GratuityBenefitRepository extends JpaRepository<GratuityBenefitEntity, Long> {

	@Query(value="SELECT * FROM QSTG_GRATUITY_BENEFIT  WHERE QUOTATION_ID =?1 ORDER BY GRATUITY_BENEFIT_ID",nativeQuery = true)
	List<GratuityBenefitEntity> findByQuotationId(Long quotaitonId);
	void deleteAllByQuotationId(Long quotationId);
	
	@Modifying
	@Query(value = "DELETE FROM QSTG_GRATUITY_BENEFIT WHERE QUOTATION_ID IN (SELECT QUOTATION_ID FROM QSTG_QUOTATION WHERE PROPOSAL_NUMBER=?1)", nativeQuery = true)
	void deleteByProposalNumber(String proposalNumber);
	
	@Modifying
	@Query(value = "UPDATE  QSTG_GRATUITY_BENEFIT SET IS_ACTIVE =1 WHERE QUOTATION_ID =?1", nativeQuery = true)
	void updateisactive(Long id);

}
