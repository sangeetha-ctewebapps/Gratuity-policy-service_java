package com.lic.epgs.gratuity.policyservices.conversion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.entity.PmstTmpMergerPropsEntity;
@Repository
public interface PsPmstTmpMergerPropsRepository extends JpaRepository<PmstTmpMergerPropsEntity,Long> {
	
	PmstTmpMergerPropsEntity findByIdAndIsActiveTrue(Long mergeId);

}
