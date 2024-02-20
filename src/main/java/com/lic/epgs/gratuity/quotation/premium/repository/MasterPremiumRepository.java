package com.lic.epgs.gratuity.quotation.premium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.premium.entity.MasterPremiumEntity;
/**
 * @author Ismail Khan A
 *
 */

@Repository
public interface MasterPremiumRepository extends JpaRepository<MasterPremiumEntity, Long> {
	
	List<MasterPremiumEntity> findByQuotationId(Long quotaitonId);
	
}
