package com.lic.epgs.gratuity.quotation.valuationmatrix.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationMatrixEntity;

public interface MasterValuationMatrixRepository extends JpaRepository<MasterValuationMatrixEntity, Long> {
	Optional<MasterValuationMatrixEntity> findByQuotationId(Long quotaitonId);
}
