package com.lic.epgs.gratuity.quotation.gratuitybenefit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitPropsEntity;

public interface GratuityBenefitPropsRepository extends JpaRepository<GratuityBenefitPropsEntity, Long> {
	@Modifying
	@Query(value = "DELETE FROM QSTG_GRATUITY_BENEFIT_PROPS WHERE GRATUITY_BENEFIT_ID IN ("
			+ "SELECT GRATUITY_BENEFIT_ID FROM QSTG_GRATUITY_BENEFIT WHERE QUOTATION_ID IN ("
			+ "SELECT QUOTATION_ID FROM QSTG_QUOTATION WHERE PROPOSAL_NUMBER=?1))", nativeQuery = true)
	void deleteByProposalNumber(String proposalNumber);
}
