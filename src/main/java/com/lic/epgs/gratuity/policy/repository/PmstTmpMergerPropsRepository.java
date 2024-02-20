package com.lic.epgs.gratuity.policy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.dto.PmstTmpMergerPropsDto;
import com.lic.epgs.gratuity.policy.entity.PmstTmpMergerPropsEntity;

@Repository
public interface PmstTmpMergerPropsRepository  extends JpaRepository<PmstTmpMergerPropsEntity,Long> {
	@Query(value="SELECT MAX(MERGER_REQUEST_NUMBER) AS MAXMERGERREQNO FROM PMST_TMP_MERGER_PROPS", nativeQuery =true)
	Long maxMergerRequestNo();
	
	
	@Query(value="SELECT * FROM  PMST_TMP_MERGER_PROPS WHERE SOURCE_TMP_POLICY_ID=?1 AND isActive=0", nativeQuery =true)
	PmstTmpMergerPropsEntity findBysourceTmpPolicyID(Long id);


	@Query(value = "SELECT  ptmp.* FROM PMST_TMP_MERGER_PROPS ptmp "
			+ "		    JOIN PMST_TMP_POLICY ptp ON ptp.POLICY_ID =ptmp.SOURCE_TMP_POLICY_ID  \r\n"
			+ "			JOIN PMST_TMP_POLICY ptp1 on ptp1.POLICY_ID =ptmp.DESTINATION_TMP_POLICY_ID \r\n"
			+ "			JOIN PMST_POLICY_SERVICE pps  ON ptp.POLICY_ID =pps.POLICY_ID\r\n"
			+ "			JOIN PMST_POLICY_SERVICE pps  ON ptp1.POLICY_ID =pps.POLICY_ID AND ptmp.IS_ACTIVE =0",nativeQuery = true)

	Object[] findPolicyDetailSeerch();

	@Query(value="SELECT * FROM PMST_TMP_MERGER_PROPS ptmp WHERE ptmp.SOURCE_TMP_POLICY_ID in (SELECT ptp.POLICY_ID  FROM PMST_TMP_POLICY ptp  WHERE ptp.POLICY_NUMBER =?1 AND ptp.IS_ACTIVE =1)", nativeQuery =true)
	List<PmstTmpMergerPropsEntity> findBysourceTmpPolicyIDinprogress(Long policyNumber);

	@Query(value="SELECT * FROM PMST_TMP_MERGER_PROPS ptmp WHERE ptmp.SOURCE_TMP_POLICY_ID in (SELECT ptp.POLICY_ID  FROM PMST_TMP_POLICY ptp  WHERE ptp.POLICY_NUMBER =?1 AND ptp.IS_ACTIVE =0)", nativeQuery =true)
	List<PmstTmpMergerPropsEntity> findBysourceTmpPolicyIDExisting(Long policyNumber);


	List<PmstTmpMergerPropsEntity> findBySourcePolicyID(Long id);
	
	PmstTmpMergerPropsEntity findBySourceTmpPolicyID(Long sourceTmpPolicyID);
}
