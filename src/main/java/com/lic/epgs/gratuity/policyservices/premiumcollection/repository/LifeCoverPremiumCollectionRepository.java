package com.lic.epgs.gratuity.policyservices.premiumcollection.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policyservices.premiumcollection.entity.LifeCoverPremiumCollectionPropsEntity;

public interface LifeCoverPremiumCollectionRepository extends JpaRepository<LifeCoverPremiumCollectionPropsEntity, Long>{

	LifeCoverPremiumCollectionPropsEntity findBytmpPolicyId(Long masterTmpPolicyId);
	
}