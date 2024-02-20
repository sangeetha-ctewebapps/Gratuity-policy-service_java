package com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.entity.AOCMCredibilityFactorEntity;

public interface AOCMCredibilityFactorRepository extends JpaRepository<AOCMCredibilityFactorEntity, Long>{

	@Query(value="SELECT * FROM AOCM_CREDIBILITY_FACTOR WHERE VARIANT=?1 and IS_ACTIVE=1",nativeQuery = true)
	List<AOCMCredibilityFactorEntity> findByvariant(String variant);

}
