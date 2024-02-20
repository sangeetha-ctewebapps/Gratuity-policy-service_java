package com.lic.epgs.gratuity.policyservices.premiumcollection.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policyservices.premiumcollection.entity.LCPremiumCollectionDuesEntity;

public interface PremiumCollectionDuesRepository extends JpaRepository<LCPremiumCollectionDuesEntity, Long>{

	List<LCPremiumCollectionDuesEntity> findBylcPremCollPropsId(Long premiumpropsId);

	List<LCPremiumCollectionDuesEntity> deleteBylcPremCollPropsId(Long id);

}
