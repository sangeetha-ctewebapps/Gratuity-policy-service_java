package com.lic.epgs.gratuity.policy.lifecover.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.lifecover.entity.StagingPolicyLifeCoverEntity;

/**
 * @author Ismail Khan A
 *
 */

@Repository
public interface StagingPolicyLifeCoverRepository extends JpaRepository<StagingPolicyLifeCoverEntity, Long> {

	@Query(value = "SELECT * FROM PSTG_LIFE_COVER WHERE POLICY_ID=?1 and IS_ACTIVE=1 ORDER BY LIFE_COVER_ID",nativeQuery = true)
	List<StagingPolicyLifeCoverEntity> findByPolicyId(Long policyId);	
}
