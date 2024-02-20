package com.lic.epgs.gratuity.policyservices.policymodification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;

public interface MPHAddressRepositoryPS  extends JpaRepository<MPHAddressEntity, Long>{

	List<MPHAddressEntity> findByMasterMphId(Long id);

}
