package com.lic.epgs.gratuity.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.common.entity.CommonMasterStateEntity;

public interface CommonMasterStateRepository extends JpaRepository<CommonMasterStateEntity, Long> {

	@Query(value = "SELECT c.type FROM CommonMasterStateEntity c WHERE c.description=?1 AND c.isActive='Y' AND c.isDeleted='N'")
	String getStateType(String getStateName);
	
	@Query(value = "SELECT c.code FROM CommonMasterStateEntity c WHERE c.description=?1 AND c.isActive='Y' AND c.isDeleted='N'")
	String getStateCode(String getStateName);
	
	@Query(value = "SELECT c.gstin FROM CommonMasterStateEntity c WHERE c.description=?1 AND c.isActive='Y' AND c.isDeleted='N'")
	String getGSTIn(String getStateName);
	
	
	@Query(value = "SELECT c.gstStateCode FROM CommonMasterStateEntity c WHERE c.description=?1 AND c.isActive='Y' AND c.isDeleted='N'")
	String getGSTStateCode(String getStateName);

	@Query(value = "SELECT c.gstStateCode FROM CommonMasterStateEntity c WHERE c.id=?1 AND c.isActive='Y' AND c.isDeleted='N'")
	String getGSTStatecodebyid(Long getStateName);
	
	
}
