package com.lic.epgs.gratuity.policyservices.dom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policyservices.dom.entity.PmstMidleaverPropsEntity;


@Repository
public interface MidleaverPropsRepository extends JpaRepository<PmstMidleaverPropsEntity, Long> {

	@Query(value="select To_number(MAX(Service_number)) from PMST_MIDLEAVER_PROPS", nativeQuery = true)
	Long getMaxService();
	
	PmstMidleaverPropsEntity findByTmpPolicyId(Long tmpPolicyId);
	
	
	
}