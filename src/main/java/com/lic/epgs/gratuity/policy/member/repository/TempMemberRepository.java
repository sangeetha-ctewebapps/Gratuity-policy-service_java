package com.lic.epgs.gratuity.policy.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.member.entity.TempMemberEntity;

@Repository
public interface TempMemberRepository extends JpaRepository<TempMemberEntity, Long> {



	List<TempMemberEntity> findByendorsementId(Long id);

	List<TempMemberEntity> findByTmpPolicyId(Long id);
	
	@Query(value="SELECT * FROM PMST_TMP_MEMBER WHERE TMP_POLICY_ID=?1 AND PMST_MEMBER_ID=?2", nativeQuery=true)
	TempMemberEntity findByTmpPolicyIdAndMasterMemberId(Long tmpPolicyId, Long masterMemberId);

	@Modifying
	@Query(value="DELETE FROM PMST_TMP_MEMBER_NOMINEE WHERE MEMBER_NOMINEE_ID = :nomineeId",nativeQuery=true)
	void deleteByNominee(@Param("nomineeId") Long nomineeId);

	@Query(value="SELECT * From PMST_TMP_MEMBER where TMP_POLICY_ID=?1 AND MEMBER_ID NOT IN (?2) AND EMPLOYEE_CODE=?3 and IS_ACTIVE=1", nativeQuery=true)
	List<TempMemberEntity> findByEmployeeCode(Long tmpPolicyId, Long tmpMemberId, String employeeCode);
	
	@Query(value="SELECT * From PMST_TMP_MEMBER where TMP_POLICY_ID=?1 AND MEMBER_ID NOT IN (?2) AND PAN_NUMBER=?3 and IS_ACTIVE=1", nativeQuery=true)
	List<TempMemberEntity> findByEmployeePan(Long tmpPolicyId, Long tmpMemberId, String employeePan);
	
	@Query(value="SELECT * From PMST_TMP_MEMBER where TMP_POLICY_ID=?1 AND MEMBER_ID NOT IN (?2) AND AADHAR_NUMBER=?3 and IS_ACTIVE=1", nativeQuery=true)
	List<TempMemberEntity> findByEmployeeAadhar(Long tmpPolicyId, Long tmpMemberId, String employeeAadhar);

	@Query(value = "SELECT B.EMPLOYEE_CODE, B.LIC_ID, C.PICK_LIST_ITEM_NAME, B.DATE_OF_APPOINTMENT, A.LEAVING_DATE, " + 
			"A.LAST_PREM_COLLECTED_DATE, A.LAST_PREM_COLLECTED_AMT, (A.LAST_PREM_COLLECTED_AMT * ?2) AS GST_COLLECTED, A.REFUND_PREM_AMOUNT, " +
			"A.REFUND_GST_AMOUNT " + 
			"FROM PMST_MIDLEAVER_MEMBER_PROPS A " + 
			"JOIN PMST_TMP_MEMBER B ON A.TMP_MEMBER_ID=B.MEMBER_ID " + 
			"JOIN PICK_LIST_ITEM C ON C.PICK_LIST_ITEM_ID=B.STATUS_ID " +
			"WHERE B.TMP_POLICY_ID=?1" , nativeQuery = true)
	List<Object[]> findAllMidLeaverByTmpPolicyId(Long id, Double gstRate);
}
