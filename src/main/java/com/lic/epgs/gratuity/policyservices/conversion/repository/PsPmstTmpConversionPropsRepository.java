package com.lic.epgs.gratuity.policyservices.conversion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.entity.PmstTmpConversionPropsEntity;
@Repository
public interface PsPmstTmpConversionPropsRepository extends JpaRepository<PmstTmpConversionPropsEntity, Long> {

	PmstTmpConversionPropsEntity findByIdAndIsActiveTrue(Long convertStringToLong);

	Optional<PmstTmpConversionPropsEntity> findByTmpPolicyID(Long id);

}
