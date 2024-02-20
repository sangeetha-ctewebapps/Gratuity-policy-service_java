package com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.entity.RenewalLifeCoverTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.dto.RenewalLifeCoverAndGratuityDto;
import com.lic.epgs.gratuity.quotation.lifecover.entity.LifeCoverEntity;

public interface RenewalLifeCoverTMPRepository extends JpaRepository<RenewalLifeCoverTMPEntity, Long> {

//	@Query(value = "SELECT * FROM PMST_TMP_LIFE_COVER  WHERE TMP_POLICY_ID =?1 ORDER BY LIFE_COVER_ID", nativeQuery = true)
//	List<RenewalLifeCoverTMPEntity> findByPolicyId(Long policyId);
	
	@Query(value = "SELECT * FROM PMST_TMP_LIFE_COVER  WHERE TMP_POLICY_ID =?1 and IS_ACTIVE=1 ORDER BY LIFE_COVER_ID", nativeQuery = true)
	List<RenewalLifeCoverTMPEntity> findBytmpPolicyId(Long policyId);

	@Modifying
	@Query(value ="UPDATE PMST_TMP_LIFE_COVER SET IS_ACTIVE=0 WHERE TMP_POLICY_ID=?1",nativeQuery = true)
	void updateisActivefalse(Long id);

	@Modifying
	@Query(value ="DELETE from PMST_TMP_LIFE_COVER  WHERE TMP_POLICY_ID=?1",nativeQuery = true)
	void deleteBytmpPolicyId(Long id);
	
}
