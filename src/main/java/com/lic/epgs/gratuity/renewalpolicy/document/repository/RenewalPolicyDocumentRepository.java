package com.lic.epgs.gratuity.renewalpolicy.document.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.renewalpolicy.document.dto.RenewalPolicyDocumentDto;
import com.lic.epgs.gratuity.renewalpolicy.document.entity.RenewalPolicyDocumentEntity;



public interface RenewalPolicyDocumentRepository extends JpaRepository<RenewalPolicyDocumentEntity, Long>{

	
	List<RenewalPolicyDocumentEntity> findBytmpPolicyId(Long tmpPolicyId);

	@Query(value = "SELECT d FROM RenewalPolicyDocumentEntity d WHERE isDeleted=false AND tmpPolicyId=:tmpPolicyId and moduleType=:moduleType")
	List<RenewalPolicyDocumentEntity> findBytmpPolicyIdandmoduleType(Long tmpPolicyId, String moduleType);
	

}
