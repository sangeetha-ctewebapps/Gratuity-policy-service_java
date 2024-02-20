package com.lic.epgs.gratuity.policyservices.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policyservices.common.entity.TempPolicyServiceNotes;

public interface TempPolicyServiceNotesRepository  extends JpaRepository<TempPolicyServiceNotes, Long>{

//	Optional<TempPolicyServiceNotes> findByReferenceId(Long id);

	List<TempPolicyServiceNotes> findByReferenceIdAndPolicyNumberAndReferenceServiceTypeAndIsActiveOrderByCreatedDateDesc(
			Long referenceId, String policyNumber, String referenceServiceType, boolean b);

	
	List<TempPolicyServiceNotes> findByServiceNumberAndIsActive(String serviceNumber, boolean b);

	

}
