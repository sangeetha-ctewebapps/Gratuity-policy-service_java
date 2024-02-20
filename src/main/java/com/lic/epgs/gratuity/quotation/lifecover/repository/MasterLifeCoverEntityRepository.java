package com.lic.epgs.gratuity.quotation.lifecover.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.lifecover.entity.MasterLifeCoverEntity;

@Repository
public interface MasterLifeCoverEntityRepository extends JpaRepository<MasterLifeCoverEntity, Long> {

	//MasterLifeCoverEntity findByQuotationId(Long id);
	@Query(value = "SELECT * FROM QMST_LIFE_COVER  WHERE QUOTATION_ID =?1 ORDER  BY LIFE_COVER_ID",nativeQuery = true)
	List<MasterLifeCoverEntity> findByQuotationId(Long quotationId);	

	
}
