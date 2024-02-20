package com.lic.epgs.gratuity.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.common.entity.MemberCategoryModuleEntity;


public interface MemberCategoryModuleRepository extends JpaRepository<MemberCategoryModuleEntity, Long> {

	@Query(value ="DELETE FROM MEMBER_CATEGORY_MODULE  WHERE  MEMBER_CATEGORY_ID =?1 AND TMP_POLICY_ID =?2 ",nativeQuery = true)
	 void deleteByPolicyIdandMemberCategoryId(Long categoryId, Long policyId);
	
	@Query(value ="SELECT MEMBER_CATEGORY_MODULE_ID FROM MEMBER_CATEGORY_MODULE  WHERE  MEMBER_CATEGORY_ID =?1 AND TMP_POLICY_ID =?2 ",nativeQuery = true)
	Long findByTempIdandMemberCategoryId(Long categoryId, Long policyId);
		

}
