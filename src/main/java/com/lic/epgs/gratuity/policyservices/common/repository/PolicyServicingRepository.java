package com.lic.epgs.gratuity.policyservices.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policyservices.common.entity.PolicyServiceMasterEntity;

public interface PolicyServicingRepository extends JpaRepository<PolicyServiceMasterEntity, Long>{
	
	@Query(value="SELECT SERVICE_NUMBER FROM POLICY_SERVICE WHERE POLICY_ID =?1 ",nativeQuery = true)
	String findByPolicyId(Long id);

	List<PolicyServiceMasterEntity> findByPolicyNumberAndServiceStatusIgnoreCase(String policyNumber, String string);

}
