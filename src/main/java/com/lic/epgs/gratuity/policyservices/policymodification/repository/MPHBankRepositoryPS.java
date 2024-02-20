package com.lic.epgs.gratuity.policyservices.policymodification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.mph.entity.MPHBankEntity;

public interface MPHBankRepositoryPS extends JpaRepository<MPHBankEntity, Long>{

	List<MPHBankEntity> findByMasterMphId(Long id);
	
	MPHBankEntity findByMasterMph(Long mphId);

}
