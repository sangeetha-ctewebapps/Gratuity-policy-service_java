package com.lic.epgs.gratuity.quotation.valuationmatrix.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationBasicEntity;

public interface MasterValuationBasicRepository extends JpaRepository<MasterValuationBasicEntity, Long> {
	
	Optional<MasterValuationBasicEntity> findByQuotationId(Long quotaitonId);
	
	@Query(value="SELECT MAX(TO_NUMBER(REFERENCE_NUMBER)) AS MAXREFERENCENUMBER FROM QMST_VALUATIONBASIC", nativeQuery =true)
	Long maxReferenceNumber();
}
