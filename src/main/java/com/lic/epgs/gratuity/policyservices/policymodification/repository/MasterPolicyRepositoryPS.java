package com.lic.epgs.gratuity.policyservices.policymodification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;

public interface MasterPolicyRepositoryPS extends JpaRepository<MasterPolicyEntity, Long> {

	@Query(value = "SELECT POLICY_STATUS_ID FROM PMST_POLICY WHERE POLICY_NUMBER =?1", nativeQuery = true)
	Long getStatusByPolicy(String policyNumber);

}
