package com.lic.epgs.gratuity.policy.document.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.document.entity.PolicyDocumentEntity;

@Repository
public interface PolicyDocumentRepository extends JpaRepository<PolicyDocumentEntity, Long>{

	@Query(value = "SELECT d FROM PolicyDocumentEntity d WHERE isDeleted=false AND policyId=:policyId")
	List<PolicyDocumentEntity> findBypolicyId(Long policyId);

}
