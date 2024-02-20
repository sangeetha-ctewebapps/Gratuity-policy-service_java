package com.lic.epgs.gratuity.policy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.dto.PmstTmpConversionPropsDto;
import com.lic.epgs.gratuity.policy.entity.PmstTmpConversionPropsEntity;
import com.lic.epgs.gratuity.policy.entity.PmstTmpMergerPropsEntity;

@Repository
public interface PmstTmpConversionPropsRepository extends JpaRepository<PmstTmpConversionPropsEntity,Long> {

	
	@Query(value="SELECT MAX(CONVERSION_REQUEST_NUMBER) AS MAXCONVERSIONREQNO FROM PMST_TMP_CONVERSION_PROPS", nativeQuery =true)
	Long maxConversionRequestNo();

	@Query(value="SELECT * FROM PMST_TMP_CONVERSION_PROPS ptcp   WHERE ptcp.PMST_TMP_POLICY_ID  in (SELECT ptp.POLICY_ID  FROM PMST_TMP_POLICY ptp  WHERE ptp.POLICY_NUMBER =?1 AND ptp.IS_ACTIVE =1)", nativeQuery =true)
	List<PmstTmpConversionPropsEntity> findBysourceTmpPolicyIDinprogress(Long policyNumber);

	
	@Query(value="SELECT * FROM PMST_TMP_CONVERSION_PROPS ptcp   WHERE ptcp.PMST_TMP_POLICY_ID  in (SELECT ptp.POLICY_ID  FROM PMST_TMP_POLICY ptp  WHERE ptp.POLICY_NUMBER =?1 AND ptp.IS_ACTIVE =0)", nativeQuery =true)
	List<PmstTmpConversionPropsEntity> findBysourceTmpPolicyIDExisting(Long policyNumber);


}
