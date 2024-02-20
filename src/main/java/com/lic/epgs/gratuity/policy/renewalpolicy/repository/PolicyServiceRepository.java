package com.lic.epgs.gratuity.policy.renewalpolicy.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceEntitiy;

public interface PolicyServiceRepository extends JpaRepository<PolicyServiceEntitiy, Long> {

	@Query(value = "Select * from PMST_POLICY_SERVICE where POLICY_ID=?1 and SERVICE_TYPE=?2 and IS_ACTIVE=1 ", nativeQuery = true)
	List<PolicyServiceEntitiy> findByPolicyandType(Long policyId, String serviceType);

	@Query(value = "SELECT * FROM PMST_POLICY_SERVICE pp2 WHERE pp2.POLICY_ID in(SELECT pps.POLICY_ID FROM PMST_POLICY_SERVICE pps WHERE pps.IS_ACTIVE =0  AND pps.POLICY_ID in(SELECT pp.POLICY_ID  FROM PMST_POLICY pp WHERE pp.POLICY_NUMBER =?1))", nativeQuery = true)
	PolicyServiceEntitiy getData(Long id);

	@Modifying
	@Query(value = "UPDATE PMST_POLICY_SERVICE SET IS_ACTIVE=0 WHERE POLICY_ID=?1 and SERVICE_TYPE='merger'", nativeQuery = true)
	void updateisActivefalse(Long masterPolicyId);

//	@Query(value ="UPDATE PMST_POLICY_SERVICE SET IS_ACTIVE=0 WHERE TMP_MERGER_PROPS_ID=?1 ",nativeQuery = true)
//	PolicyServiceEntitiy inactiveservice(Long policyServiceId, Long policyServiceId2);

	@Modifying
	@Query(value = "UPDATE PMST_POLICY_SERVICE SET IS_ACTIVE=0 WHERE POLICY_SERVICE_ID IN (?1)", nativeQuery = true)
	void inactiveservice(List<Long> policyServiceIDs);

	@Query(value = "SELECT * FROM PMST_POLICY_SERVICE pps WHERE  IS_ACTIVE=1  AND POLICY_ID IN (SELECT ptp.MASTER_POLICY_ID  FROM PMST_TMP_POLICY ptp  WHERE ptp.POLICY_NUMBER =?1 AND ptp.IS_ACTIVE =1)", nativeQuery = true)
	PolicyServiceEntitiy findcheckserviceIsActive(Long policyNumber, String serviceType);

	@Query(value = "SELECT * FROM PMST_POLICY_SERVICE pps WHERE  IS_ACTIVE=0  AND POLICY_ID IN (SELECT ptp.MASTER_POLICY_ID  FROM PMST_TMP_POLICY ptp  WHERE ptp.POLICY_NUMBER =?1 AND ptp.IS_ACTIVE =1)", nativeQuery = true)
	PolicyServiceEntitiy findcheckserviceInActive(Long policyNumber);

	@Query(value = "SELECT * FROM PMST_POLICY_SERVICE WHERE POLICY_ID =:policyId And SERVICE_TYPE='renewals'", nativeQuery = true)
	List<PolicyServiceEntitiy> findByPolicyId(Long policyId);

	@Query(value = "SELECT * FROM PMST_POLICY_SERVICE WHERE POLICY_SERVICE_ID =:policyServiceId", nativeQuery = true)
	PolicyServiceEntitiy findByPolicyServiceId(@Param("policyServiceId") Long policyServiceId);

	@Modifying
	@Query(value = "UPDATE PMST_POLICY_SERVICE SET IS_ACTIVE=0 WHERE POLICY_ID=?1", nativeQuery = true)
	void deactivateservicetype(Long masterPolicyId);

	@Query(value = "Select * from PMST_POLICY_SERVICE where POLICY_ID=?1 and SERVICE_TYPE=?2 and IS_ACTIVE=1 ", nativeQuery = true)
	PolicyServiceEntitiy findByPolicyandTypeandActive(Long policyId, String serviceType);

	@Query(value = "Select * from PMST_POLICY_SERVICE where POLICY_ID=?1 and SERVICE_TYPE=?2 and IS_ACTIVE=1 ", nativeQuery = true)
	PolicyServiceEntitiy GetPolicyandTypeandActive(Long policyId, String serviceType);

	@Modifying
	@Query(value = "update PMST_POLICY_SERVICE set IS_ACTIVE=1 where   SERVICE_TYPE=?2 and POLICY_ID in(SELECT POLICY_ID FROM PMST_RENEWAL_REMINDERS where policy_id=?1 and IS_ACTIVE=1)", nativeQuery = true)
	void updateifRenewalRemainderdoneforsamePolicy(Long policyId, String serviceType);

	@Modifying
	@Query(value = "Delete from PMST_POLICY_SERVICE where  POLICY_SERVICE_ID=?2 and POLICY_ID =?1", nativeQuery = true)
	void deleteBypmstPolicyandServiceId(Long masterPolicyId, Long policyServiceId);

	@Query(value = "SELECT * FROM PMST_POLICY_SERVICE WHERE POLICY_ID =:policyId And IS_ACTIVE=1", nativeQuery = true)
	List<PolicyServiceEntitiy> getActiveServiceByPolicyId(Long policyId);

	@Query(value = "select * from pmst_policy_service where policy_id=:id and service_type='MEMBER_ADDITION'", nativeQuery = true)
	PolicyServiceEntitiy findByPolicyid(Long id);

//    @Query(value="SELECT * FROM PMST_POLICY_SERVICE pps WHERE  IS_ACTIVE=1  AND POLICY_ID IN (SELECT ptp.MASTER_POLICY_ID  FROM PMST_TMP_POLICY ptp  WHERE ptp.POLICY_NUMBER =policyNumber AND ptp.IS_ACTIVE =1",nativeQuery = true)
//	PolicyServiceEntitiy findcheckserviceActive(String policyNumber);
//
//    @Query(value="SELECT * FROM PMST_POLICY_SERVICE pps WHERE  IS_ACTIVE=0  AND POLICY_ID IN (SELECT ptp.MASTER_POLICY_ID  FROM PMST_TMP_POLICY ptp  WHERE ptp.POLICY_NUMBER =policyNumber AND ptp.IS_ACTIVE =1",nativeQuery = true)
//	PolicyServiceEntitiy findCheckserviceInActive(String policyNumber);

	@Query(value = "SELECT * FROM PMST_POLICY_SERVICE WHERE POLICY_ID=?1 AND IS_ACTIVE=1", nativeQuery = true)
	List<PolicyServiceEntitiy> findByPolicyIdAndIsActive(Long policyId);

	Optional<PolicyServiceEntitiy> findByPolicyIdAndServiceTypeAndIsActive(Long policyId, String name, boolean b);

	@Query(value="select count(*) from pmst_policy_service pps \r\n"
			+ "join  pmst_tmp_policy ptp on ptp.Policy_service_id = pps.Policy_service_id where pps.is_active=0 and  pps.policy_id=?1 and pps.service_type=?2 and ptp.policy_tagged_status_id=139",nativeQuery = true)
	int checkforpolicyrenewalsuccessstatus(Long policyId, String serviceType);

	@Query(value = "SELECT * FROM PMST_POLICY_SERVICE a " + 
			"JOIN PMST_TMP_POLICY b on b.POLICY_SERVICE_ID=a.POLICY_SERVICE_ID " + 
			"WHERE a.POLICY_ID=?1 AND a.IS_ACTIVE=1 " + 
			"AND ?2>=b.POLICY_START_DATE " + 
			"AND ?2<=b.POLICY_END_DATE " +
			"ORDER BY a.CREATED_DATE ", nativeQuery=true)
	List<PolicyServiceEntitiy> findAllInactiveServicesForPeriod(Long policyId, Date eventDate);
	
	@Modifying
	@Query(value = "update PMST_POLICY_SERVICE set IS_ACTIVE=0 WHERE POLICY_SERVICE_ID=?1", nativeQuery = true)
	void deactivateservicetypeusingServiceId(Long policyServiceId);

	@Modifying
	@Query(value = "UPDATE PMST_POLICY_SERVICE SET IS_ACTIVE=0 WHERE POLICY_ID=?1 AND SERVICE_TYPE=?2", nativeQuery = true)
	void updateIsActiveBasedOnPoliyAndType(Long sourcemasterPolicyId, String serviceType);

}
