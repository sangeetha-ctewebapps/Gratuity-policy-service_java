package com.lic.epgs.gratuity.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;

@Repository
public interface MemberCategoryRepository extends JpaRepository<MemberCategoryEntity, Long>{

	
	@Query(value = "SELECT * FROM MEMBER_CATEGORY WHERE QSTG_QUOTATION_ID =?1 ORDER BY MEMBER_CATEGORY_ID",nativeQuery = true)
	List<MemberCategoryEntity> findByqstgQuoationId(Long id);

	@Query(value = "SELECT * FROM MEMBER_CATEGORY WHERE QMST_QUOTATION_ID =?1 ORDER BY MEMBER_CATEGORY_ID",nativeQuery = true)
	List<MemberCategoryEntity> findByqmstQuotationId(Long id);

	@Query(value = "SELECT * FROM MEMBER_CATEGORY WHERE PSTG_POLICY_ID =?1 ORDER BY MEMBER_CATEGORY_ID",nativeQuery = true)
	List<MemberCategoryEntity> findBypstgPolicyId(Long id);

	@Query(value = "SELECT * FROM MEMBER_CATEGORY WHERE PMST_POLICY_ID =?1 ORDER BY MEMBER_CATEGORY_ID",nativeQuery = true)
	List<MemberCategoryEntity> findBypmstPolicyId(Long id);

//
//	@Query(value = "SELECT * FROM MEMBER_CATEGORY WHERE PMST_POLICY_ID =?1 ORDER BY MEMBER_CATEGORY_ID",nativeQuery = true)
//	List<MemberCategoryEntity> findBypmsttmpPolicyId(Long id);

	@Query(value="SELECT * FROM MEMBER_CATEGORY mc WHERE MEMBER_CATEGORY_ID in(SELECT MEMBER_CATEGORY_ID  FROM MEMBER_CATEGORY_MODULE mcm  WHERE mcm.TMP_POLICY_ID =?1)",nativeQuery = true)
	List<MemberCategoryEntity> findBypmstTmpPolicy(Long policyId);
	
	@Query(value = "SELECT * FROM MEMBER_CATEGORY WHERE PMST_POLICY_ID =?1 ORDER BY MEMBER_CATEGORY_ID",nativeQuery = true)
	List<MemberCategoryEntity> findBypmstPolicy(Long policyId);
	
	@Query(value = "SELECT * FROM MEMBER_CATEGORY WHERE PMST_HIS_POLICY_ID =?1 ORDER BY MEMBER_CATEGORY_ID",nativeQuery = true)
	List<MemberCategoryEntity> findBypmstHisPolicyId(Long id);
	
	@Modifying
	@Query(value = "DELETE FROM MEMBER_CATEGORY WHERE QSTG_QUOTATION_ID IN (SELECT QUOTATION_ID FROM QSTG_QUOTATION qq WHERE PROPOSAL_NUMBER=?1) AND QMST_QUOTATION_ID IS NULL", nativeQuery = true)
	void deleteByProposalNumber(String proposalNumber);
	
	@Modifying
	@Query(value = "UPDATE MEMBER_CATEGORY SET QSTG_QUOTATION_ID = NULL WHERE QSTG_QUOTATION_ID IN (SELECT QUOTATION_ID FROM QSTG_QUOTATION qq WHERE PROPOSAL_NUMBER=?1)", nativeQuery = true)
	void unsetQuotationStaing(String proposalNumber);
	
	@Query(value="SELECT * FROM MEMBER_CATEGORY mc WHERE MEMBER_CATEGORY_ID in(SELECT MEMBER_CATEGORY_ID  FROM MEMBER_CATEGORY_MODULE mcm  WHERE mcm.TMP_POLICY_ID =?1)",nativeQuery = true)
	List<MemberCategoryEntity> findByTmpPolicyId(Long tmpPolicyId);

	@Modifying
	@Query(value = "UPDATE MEMBER_CATEGORY SET PMST_HIS_POLICY_ID = ?2 WHERE PMST_POLICY_ID=?1", nativeQuery = true)
	void UpdateHistoryfromMasterPolicy(Long masterPolicyId, Long historyPolicyId);

	@Modifying
	@Query(value = "UPDATE MEMBER_CATEGORY SET PMST_POLICY_ID = ?2 WHERE MEMBER_CATEGORY_ID in(SELECT MEMBER_CATEGORY_ID  FROM MEMBER_CATEGORY_MODULE mcm  WHERE mcm.TMP_POLICY_ID =?1)", nativeQuery = true)
	void updateTmpCategorytoMasterPolicy(Long tmpPolicyID, Long MasterPolicyId);

	@Query(value="Select * from MEMBER_CATEGORY WHERE MEMBER_CATEGORY_ID in(SELECT MEMBER_CATEGORY_ID  FROM MEMBER_CATEGORY_MODULE mcm  WHERE mcm.TMP_POLICY_ID =?1)",nativeQuery = true)
	List<MemberCategoryEntity> getTempMembertoCreateMaster(Long tempPolicyId);
}
