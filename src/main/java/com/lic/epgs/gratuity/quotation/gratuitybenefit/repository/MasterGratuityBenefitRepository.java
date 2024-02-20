package com.lic.epgs.gratuity.quotation.gratuitybenefit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitEntity;

/**
 * @author Ismail Khan A
 *
 */

@Repository
public interface MasterGratuityBenefitRepository extends JpaRepository<MasterGratuityBenefitEntity, Long> {
	
	@Query(value = "SELECT * FROM QMST_GRATUITY_BENEFIT WHERE QUOTATION_ID =?1 ORDER  BY GRATUITY_BENEFIT_ID",nativeQuery = true)
	List<MasterGratuityBenefitEntity> findByQuotationId(Long quotaitonId);

}
