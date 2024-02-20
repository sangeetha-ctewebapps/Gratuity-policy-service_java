package com.lic.epgs.gratuity.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.common.entity.GratutityIcodesEntity;

@Repository
public interface GratutityIcodesRepository extends JpaRepository<GratutityIcodesEntity, Long>  {
	
	@Query(value = "SELECT g FROM GratutityIcodesEntity g WHERE g.productId = ?1 AND g.variantId = ?2")
	GratutityIcodesEntity findByProductIdVariantId(Long productId, Long variantId);

}
