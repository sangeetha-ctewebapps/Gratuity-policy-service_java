package com.lic.epgs.gratuity.quotation.premium.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.premium.entity.GratuityCalculationEntity;

import javax.transaction.Transactional;

/**
 * @author Gopi
 *
 */

@Repository
public interface GratuityCalculationRepository extends JpaRepository<GratuityCalculationEntity, Long> {

	
	@Modifying
	@Query(value = "call GRATDECREAM(?1)", nativeQuery=true)
	void calculateGratuity1(Long quotaitonId);
	
	@Modifying
	@Query(value = "call GRATVALUATION(?1)", nativeQuery=true)
	void calculateGratuity2(Long quotaitonId);
	
	@Modifying
	@Query(value = "call GRATCALCULATIONNEW(?1)", nativeQuery=true)
	void calculateGratuity3(Long quotaitonId);
	
	@Modifying
	@Query(value = "call GRATUITYTERMCALCULATION(?1)", nativeQuery=true)
	void calculateGratuity4(Long quotaitonId);
	
	@Modifying
	@Query(value = "call GRATCALCULATIONUPDATE(?1)", nativeQuery=true)
	void calculateGratuity5(Long quotaitonId);
	
	@Query(value = "SELECT * FROM GRATCALCULATION WHERE MEMBER_ID IN (SELECT MEMBER_ID FROM QSTG_MEMBER WHERE QUOTATION_ID=?1 AND IS_ACTIVE=1)AND CAL_TYPE='Q' AND IS_ACTIVE=1", nativeQuery=true)
	List<GratuityCalculationEntity> findAllByQuotationId(Long quotationId);
	
	@Query(value = "SELECT * FROM GRATCALCULATION WHERE MEMBER_ID IN (SELECT MEMBER_ID FROM PMST_TMP_MEMBER WHERE TMP_POLICY_ID=?1 AND IS_ACTIVE=1)AND CAL_TYPE='R' AND IS_ACTIVE=1", nativeQuery=true)
	List<GratuityCalculationEntity> findAllByTmpMemberId(Long tmpMemberId);

	@Query(value = "SELECT * FROM GRATCALCULATION WHERE GRATCALCULATION_ID IN (SELECT GRATCALCULATION_ID FROM PMST_TMP_MEMBER WHERE TMP_POLICY_ID=?1 AND IS_ACTIVE=1) AND CAL_TYPE='Q' AND IS_ACTIVE=1", nativeQuery=true)
	List<GratuityCalculationEntity> findAllByTmpPolicyIdForNew(Long tmpPolicyId);
	@Query(value = "SELECT * FROM GRATCALCULATION WHERE GRATCALCULATION_ID IN (SELECT GRATCALCULATION_ID FROM PMST_TMP_MEMBER WHERE TMP_POLICY_ID=?1 AND IS_ACTIVE=1) AND CAL_TYPE='R' AND IS_ACTIVE=1", nativeQuery=true)
	List<GratuityCalculationEntity> findAllByTmpPolicyIdForRwl(Long tmpPolicyId);
	@Query(value="SELECT * FROM GRATCALCULATION WHERE MEMBER_ID=?1 AND IS_ACTIVE=1", nativeQuery=true)
	GratuityCalculationEntity findByMemberId(Long memberId);
			
	@Modifying
	@Query(value = "call GRATDECREAMRWL(?1)", nativeQuery=true)
	void calculateGratuityRenewal1(Long tmpid);
	
	@Modifying
	@Query(value = "call GRATVALUATIONRWL(?1)", nativeQuery=true)
	void calculateGratuityRenewal2(Long tmpid);

	@Modifying
	@Query(value = "call RENEWALCALCULATION(?1,?2)", nativeQuery=true)
	void calculateGratuityRenewal3(Long tmpid, String autoCoverStatus);

	@Query(value = "SELECT COUNT(*) FROM GRATCALCULATION g WHERE  g.IS_ACTIVE =1 AND g.CAL_TYPE ='Q' and g.MEMBER_ID IN (SELECT qm.MEMBER_ID  FROM QSTG_MEMBER qm WHERE qm.QUOTATION_ID=?1)", nativeQuery=true)
	int findByQuotationId(Long quotationId);

	@Modifying
	@Query(value = "UPDATE GRATCALCULATION g SET  g.IS_ACTIVE =0 Where g.CAL_TYPE ='Q' and g.MEMBER_ID IN (SELECT qm.MEMBER_ID  FROM QSTG_MEMBER qm WHERE qm.QUOTATION_ID=?1)", nativeQuery=true)
	void updateDeActiveQuotationId(Long quotationId);

	@Modifying
	@Query(value = "call MERGERCALCULATION(?1)", nativeQuery=true)
	void calculateGratuityRenewaldes3(Long mergertmpid);

	@Query(value = "SELECT * FROM GRATCALCULATION WHERE MEMBER_ID IN (SELECT MEMBER_ID FROM PMST_TMP_MEMBER WHERE TMP_POLICY_ID=?1 AND IS_ACTIVE=1) AND IS_ACTIVE=1", nativeQuery=true)
	List<GratuityCalculationEntity> findAllByMergerTmpMemberId(Long id);

	

	@Modifying
	@Transactional
	@Query(value = "call GRATDECREAM_NEW(?1,?2,?3,?4)", nativeQuery=true)
	void calculateGratuity11(Long quotationId, String type, String fateortabluar, String rateTable);


	@Modifying
	@Transactional
	@Query(value = "call GRATVALUATION_NEW(?1,?2,?3,?4)", nativeQuery=true)
	void calculateGratuity21(Long quotationId,  String type, String fateortabluar, String rateTable);


	@Modifying
	@Transactional
	@Query(value = "call GRATCALCULATIONNEW_NEW(?1,?2,?3,?4)", nativeQuery=true)
	void calculateGratuity31(Long quotationId,  String type, String fateortabluar, String rateTable);


	@Modifying
	@Transactional
	@Query(value = "call GRATUITYTERMCALCULATION_NEW(?1,?2,?3,?4)", nativeQuery=true)
	void calculateGratuity41(Long quotationId, String type, String fateortabluar, String rateTable);

	@Modifying
	@Transactional
	@Query(value = "call GRATCALCULATIONUPDATE_NEW(?1,?2,?3,?4)", nativeQuery=true)
	void calculateGratuity51(Long quotationId, String type, String fateortabluar, String rateTable);

}
