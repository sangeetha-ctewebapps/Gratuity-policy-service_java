package com.lic.epgs.gratuity.policyservices.policymodification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.mph.entity.TempMPHAddressEntity;

public interface TempMPHAddressRepositoryPS  extends JpaRepository<TempMPHAddressEntity, Long> {

	List<TempMPHAddressEntity> findBymasterMphId(Long id);

}
