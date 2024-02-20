package com.lic.epgs.gratuity.quotation.valuation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.valuation.entity.MasterValuationEntity;

/**
 * @author Ismail Khan
 *
 */

@Repository
public interface MasterValuationRepository extends JpaRepository<MasterValuationEntity, Long> {

	Optional<MasterValuationEntity> findByQuotationId(Long quotaitonId);
}
