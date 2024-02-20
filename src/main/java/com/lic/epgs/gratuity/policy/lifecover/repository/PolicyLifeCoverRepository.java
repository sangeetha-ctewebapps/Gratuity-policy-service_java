package com.lic.epgs.gratuity.policy.lifecover.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;

/**
 * @author Ismail Khan A
 *
 */

@Repository
public interface PolicyLifeCoverRepository extends JpaRepository<PolicyLifeCoverEntity, Long> {
	
	@Query(value = "SELECT * FROM PMST_LIFE_COVER WHERE POLICY_ID=?1 and IS_ACTIVE=1 ORDER BY LIFE_COVER_ID",nativeQuery = true)
	List<PolicyLifeCoverEntity> findByPolicyId(Long policyId);	
	
	
	@Query(value = "SELECT * FROM PMST_LIFE_COVER WHERE POLICY_ID=?1 ORDER BY LIFE_COVER_ID",nativeQuery = true)
	List<PolicyLifeCoverEntity> findByPolicyId1(Long policyId);

	@Query(value = "SELECT  LISTAGG('Cat ' || mc.NAME || ' - ' || RETIREMENT_AGE,', ')  AS RETIREMENT_CAT_AGE\r\n"
			+ "FROM PMST_LIFE_COVER plc \r\n"
			+ "JOIN MEMBER_CATEGORY mc on mc.MEMBER_CATEGORY_ID =plc.CATEGORY_ID \r\n"
			+ "WHERE POLICY_ID =?",nativeQuery = true)
	String maxRetirement(Long policyId);

	@Modifying
	@Query(value="Update PMST_LIFE_COVER set IS_ACTIVE=1 where POLICY_ID=?1",nativeQuery = true)
	void updateISActive(Long pmstPolicyId);	
}
