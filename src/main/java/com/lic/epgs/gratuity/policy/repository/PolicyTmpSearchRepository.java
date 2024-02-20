package com.lic.epgs.gratuity.policy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyTmpSearchEntity;

public interface PolicyTmpSearchRepository extends JpaRepository<PolicyTmpSearchEntity, Long> {

	/*
	 * @Query(value = "\r\n" + "select * from pmst_tmp_policy a \r\n" +
	 * "join pmst_policy_service b on a.policy_service_id=b.policy_service_id\r\n" +
	 * "join pmst_policy c on c.policy_id=b.policy_id\r\n" +
	 * "where b.is_active=1  and b.service_type='MEMBER_ADDITION' and a.QUOTATION_TAGGED_STATUS_ID=:taggedStatusId and\r\n"
	 * + "c.policy_number=:policyNumber", nativeQuery = true)
	 */

	@Query(value = "SELECT * FROM PMST_TMP_POLICY A\r\n" + "JOIN PMST_POLICY B ON B.POLICY_ID=A.MASTER_POLICY_ID\r\n"
			+ "JOIN PMST_TMP_AOM_PROPS C ON C.TMP_POLICY_ID=A.POLICY_ID\r\n"
			+ "WHERE B.POLICY_NUMBER=:policyNumber AND A.QUOTATION_TAGGED_STATUS_ID=:taggedStatusId", nativeQuery = true)
	List<PolicyTmpSearchEntity> findQuotationByPolicyId(int taggedStatusId, String policyNumber);

	/*
	 * @Query(value = "\r\n" + "select * from pmst_tmp_policy a \r\n" +
	 * "join pmst_policy_service b on a.policy_service_id=b.policy_service_id\r\n" +
	 * "join pmst_policy c on c.policy_id=b.policy_id\r\n" +
	 * "where b.is_active=1  and b.service_type='MEMBER_ADDITION' and a.POLICY_TAGGED_STATUS_ID=:taggedStatusId and\r\n"
	 * + "c.policy_number=:policyNumber", nativeQuery = true)
	 */

	@Query(value = "SELECT * FROM PMST_TMP_POLICY A\r\n" + "JOIN PMST_POLICY B ON B.POLICY_ID=A.MASTER_POLICY_ID\r\n"
			+ "JOIN PMST_MIDLEAVER_PROPS C ON C.TMP_POLICY_ID=A.POLICY_ID\r\n"
			+ "WHERE B.POLICY_NUMBER=:policyNumber AND C.STATUS_ID IN :statusIds", nativeQuery = true)
	List<PolicyTmpSearchEntity> getDOMbyPolicyNumber(List<Long> statusIds, String policyNumber);

	@Query(value = "SELECT * FROM PMST_TMP_POLICY A\r\n" + "JOIN PMST_POLICY B ON B.POLICY_ID=A.MASTER_POLICY_ID\r\n"
			+ "JOIN PMST_MIDLEAVER_PROPS C ON C.TMP_POLICY_ID=A.POLICY_ID\r\n"
			+ "WHERE C.IS_PREMIUM_REFUND=1 AND B.POLICY_NUMBER=:policyNumber AND C.STATUS_ID IN :statusIds", nativeQuery = true)
	List<PolicyTmpSearchEntity> getDOMbyPolicyNumber1(List<Long> statusIds, String policyNumber);

	@Query(value = "SELECT * FROM PMST_TMP_POLICY A\r\n" + "JOIN PMST_POLICY B ON B.POLICY_ID=A.MASTER_POLICY_ID\r\n"
			+ "JOIN PMST_TMP_AOM_PROPS C ON C.TMP_POLICY_ID=A.POLICY_ID\r\n"
			+ "WHERE B.POLICY_NUMBER=:policyNumber AND A.POLICY_TAGGED_STATUS_ID=:taggedStatusId AND A.POLICY_STATUS_ID IN :statusIds", nativeQuery = true)
	List<PolicyTmpSearchEntity> findServiceByPolicyId(int taggedStatusId, String policyNumber, List<Integer> statusIds);

	@Query(value = "SELECT * FROM PMST_TMP_POLICY A\r\n" + "JOIN PMST_POLICY B ON B.POLICY_ID=A.MASTER_POLICY_ID\r\n"
			+ "JOIN PMST_TMP_AOM_PROPS C ON C.TMP_POLICY_ID=A.POLICY_ID\r\n"
			+ "WHERE A.POLICY_STATUS_ID is null AND B.POLICY_NUMBER=:policyNumber AND A.POLICY_TAGGED_STATUS_ID=:taggedStatusId ", nativeQuery = true)
	List<PolicyTmpSearchEntity> findNewServiceByPolicyId(int taggedStatusId, String policyNumber);
}
