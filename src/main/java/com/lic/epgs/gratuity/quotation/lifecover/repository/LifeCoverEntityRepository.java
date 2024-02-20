package com.lic.epgs.gratuity.quotation.lifecover.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.lifecover.entity.LifeCoverEntity;

@Repository
public interface LifeCoverEntityRepository extends JpaRepository<LifeCoverEntity, Long> {
	@Query(value="SELECT * FROM QSTG_LIFE_COVER  WHERE QUOTATION_ID =?1 ORDER BY LIFE_COVER_ID",nativeQuery = true)
	List<LifeCoverEntity> findByQuotationId(Long quotationId);
	
	@Modifying
	@Query(value = "DELETE FROM QSTG_LIFE_COVER WHERE QUOTATION_ID IN (SELECT QUOTATION_ID FROM QSTG_QUOTATION WHERE PROPOSAL_NUMBER=?1)", nativeQuery = true)
	void deleteByProposalNumber(String proposalNumber);

	@Modifying
	@Query(value = "UPDATE  QSTG_LIFE_COVER  SET IS_ACTIVE =1 WHERE QUOTATION_ID =?1", nativeQuery = true)
	void updateisactive(Long id);
}
