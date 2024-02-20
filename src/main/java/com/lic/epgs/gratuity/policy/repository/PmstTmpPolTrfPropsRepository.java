package com.lic.epgs.gratuity.policy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.entity.PmstTmpPolTrfPropsEntity;


@Repository
public interface PmstTmpPolTrfPropsRepository  extends JpaRepository<PmstTmpPolTrfPropsEntity,Long> {

//	
//	@Query(value="SELECT * FROM PMST_TMP_POL_TRF_PROPS ptmp WHERE ptmp.PMST_TMP_POLICY_ID in (SELECT ptp.POLICY_ID  FROM PMST_TMP_POLICY ptp  WHERE ptp.POLICY_NUMBER =?1 AND ptp.IS_ACTIVE =1)", nativeQuery =true)
//	List<PmstTmpPolTrfPropsEntity> findByTmpPolicyIDinprogress(String policyNumber);
//	
//	@Query(value="SELECT * FROM PMST_TMP_POL_TRF_PROPS ptmp WHERE ptmp.PMST_TMP_POLICY_ID in (SELECT ptp.POLICY_ID  FROM PMST_TMP_POLICY ptp  WHERE ptp.POLICY_NUMBER =?1 AND ptp.IS_ACTIVE =0)", nativeQuery =true)
//	List<PmstTmpPolTrfPropsEntity> findByExistingSearch(String policyNumber);


}
