package com.lic.epgs.gratuity.policy.surrender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.surrender.entity.SurrConfigEntity;

@Repository
public interface SurrConfigRepository extends JpaRepository<SurrConfigEntity, Long>{

	@Query(value="SELECT * FROM SURR_CONFIG WHERE VARIANT_VERSION =:variantVersion",nativeQuery=true)
	public SurrConfigEntity getSurrenderConfigDetailsUsingVersion(String variantVersion);
}
