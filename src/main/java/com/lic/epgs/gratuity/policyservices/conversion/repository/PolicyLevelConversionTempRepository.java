package com.lic.epgs.gratuity.policyservices.conversion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policyservices.conversion.entity.PolicyLevelConversionTempEntity;

@Repository
public interface PolicyLevelConversionTempRepository extends JpaRepository<PolicyLevelConversionTempEntity, Long> {

	List<PolicyLevelConversionTempEntity> findAllByConversionStatusInAndUnitCodeAndIsActiveTrueOrderByModifiedOnDesc(
			List<Integer> inProgressPolicyConversionMaker, String unitCode);

	PolicyLevelConversionTempEntity findByConversionIdAndIsActiveTrue(Long convertStringToLong);





	
}