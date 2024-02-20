package com.lic.epgs.gratuity.policyservices.policymodification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.MPHModificationResponse;

public interface MPHRepositoryPS extends JpaRepository<MPHEntity, Long> {

	@Query(value = "select new com.lic.epgs.gratuity.policyservices.policymodification.dto.MPHModificationResponse(mpe, mph) from"
			+ "   MasterPolicyEntity mpe join MPHEntity mph on mpe.masterpolicyHolder = mph.id  And mpe.policyNumber =:policyNumber  ")
	MPHModificationResponse getPolicyAndMPHDetails(String policyNumber);

}