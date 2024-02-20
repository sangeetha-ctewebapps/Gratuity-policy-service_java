package com.lic.epgs.gratuity.policyservices.merger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policyservices.merger.entity.PolicyLevelMergerTempEntity;

@Repository
public interface PolicyLevelMergerTempRepository  extends JpaRepository<PolicyLevelMergerTempEntity, Long>{

	PolicyLevelMergerTempEntity findByMergingPolicyAndMergeStatusInAndIsActiveTrue(String policyNumber,
			List<Integer> validationMergerinprogressMaker);

	PolicyLevelMergerTempEntity findByDestinationPolicyAndMergeStatusInAndIsActiveTrue(String policyNumber,
			List<Integer> validationMergerinprogressMaker);

	PolicyLevelMergerTempEntity findByMergeIdAndIsActiveTrue(Long mergeId);

	PolicyLevelMergerTempEntity findByMergeIdAndMergeStatusInAndIsActiveTrue(Long id,
			List<Integer> stringToIntegerList);
		
}
