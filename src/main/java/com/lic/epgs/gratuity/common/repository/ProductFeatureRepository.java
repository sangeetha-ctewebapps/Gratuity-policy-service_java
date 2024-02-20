package com.lic.epgs.gratuity.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.common.entity.ProductFeatureEntity;

@Repository
public interface ProductFeatureRepository extends JpaRepository<ProductFeatureEntity, Long>{

	@Query(value = "SELECT * FROM PRODUCT_LEVEL_MASTER WHERE PRODUCT_ID=?1 AND VARIANT_ID=?2 AND IS_ACTIVE=1", nativeQuery=true)
	ProductFeatureEntity findByProductVariantId(Long productId, Long variantId);
}
