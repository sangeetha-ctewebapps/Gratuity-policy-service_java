package com.lic.epgs.gratuity.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.common.entity.CommonMasterUnitEntity;

public interface CommonMasterUnitRepository extends JpaRepository<CommonMasterUnitEntity, Long> {
	@Query(value = "SELECT c.stateName FROM CommonMasterUnitEntity c WHERE c.code=?1 AND c.isActive='Y' AND c.isDeleted='N'")
	String getStateNameByUnitCode(String unitCode);

	CommonMasterUnitEntity findByCode(String code);

	@Query(value = "SELECT c FROM CommonMasterUnitEntity c WHERE c.code=?1 AND c.isActive='Y' AND c.isDeleted='N'")
	CommonMasterUnitEntity findByUnitCode(String unitCode);
}
