package com.lic.epgs.gratuity.policyservices.aom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policyservices.aom.entity.PolicyTmpAOMProps;

@Repository
public interface PolicyTmpAOMPropsRepository extends JpaRepository<PolicyTmpAOMProps, Long> {

	/*
	 * @Query(value =
	 * "select * from pmst_tmp_policy where master_policy_id in (select policy_id\r\n"
	 * +
	 * "		 from pmst_policy_service where service_type='renewals' and policy_id=?);"
	 * ) void renewals(Long policyId);
	 */
	@Modifying
	@Query(value = "call GRATDECREAM_NEW(?1,?2,?3,?4)", nativeQuery = true)
	void calculateGratuity11(Long quotationId, String type, String fateortabluar, String rateTable);

	@Modifying
	@Query(value = "call GRATVALUATION_NEW(?1,?2,?3,?4)", nativeQuery = true)
	void calculateGratuity21(Long quotationId, String type, String fateortabluar, String rateTable);

	@Modifying
	@Query(value = "call GRATCALCULATIONNEW_NEW(?1,?2,?3,?4)", nativeQuery = true)
	void calculateGratuity31(Long quotationId, String type, String fateortabluar, String rateTable);

	@Modifying
	@Query(value = "call GRATUITYTERMCALCULATION_NEW(?1,?2,?3,?4)", nativeQuery = true)
	void calculateGratuity41(Long quotationId, String type, String fateortabluar, String rateTable);

	@Modifying
	@Query(value = "call GRATCALCULATIONUPDATE_NEW(?1,?2,?3,?4)", nativeQuery = true)
	void calculateGratuity51(Long quotationId, String type, String fateortabluar, String rateTable);
	
	@Query(value="SELECT * FROM PMST_TMP_AOM_PROPS WHERE TMP_POLICY_ID=:tmpPolicyId", nativeQuery=true)
	PolicyTmpAOMProps findByTmpPolicyId(Long tmpPolicyId);

}
