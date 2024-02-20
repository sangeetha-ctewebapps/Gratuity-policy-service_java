package com.lic.epgs.gratuity.mph.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;

public interface TempMPHRepository extends JpaRepository<TempMPHEntity, Long>{

	@Query(value="SELECT * FROM PMST_TMP_MPH WHERE TMP_POLICY_ID =?1 ",nativeQuery = true)
	Optional<TempMPHEntity> findBytmpPolicyId(Long temppolicyId);

@Query(value="SELECT * FROM PMST_TMP_MPH WHERE TMP_POLICY_ID =?1 ",nativeQuery = true)
	TempMPHEntity findByTempPolicyId(Long tempPolicyId);


	}
