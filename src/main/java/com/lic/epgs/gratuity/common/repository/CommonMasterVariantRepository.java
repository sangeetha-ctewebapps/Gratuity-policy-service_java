package com.lic.epgs.gratuity.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.common.entity.CommonMasterVariantEntity;

public interface CommonMasterVariantRepository extends JpaRepository<CommonMasterVariantEntity, Long> {

	CommonMasterVariantEntity findByProductId(Long productId);

}
