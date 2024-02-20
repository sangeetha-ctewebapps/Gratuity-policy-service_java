package com.lic.epgs.gratuity.policy.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.member.entity.StagingPolicyMemberEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MasterMemberEntity;

/**
 * @author Ismail Khan A
 *
 */

@Repository
public interface StagingPolicyMemberRepository extends JpaRepository<StagingPolicyMemberEntity, Long> {

	
	@Query(value="SELECT * FROM PSTG_MEMBER pm2 WHERE pm2.POLICY_ID =?1 ORDER BY EMPLOYEE_CODE", nativeQuery =true)
	List<StagingPolicyMemberEntity> findByPolicyId(Long id);
	
	@Query(value="SELECT count( * ) as totalCount  FROM PSTG_MEMBER WHERE IS_ACTIVE =1 AND POLICY_ID =?", nativeQuery =true)
	Long totalCount(Long id);
	
}
