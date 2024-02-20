package com.lic.epgs.gratuity.policyservices.policymodification.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.TempMPHModificationResponseDto;

public interface TempMPHRepositoryPS extends JpaRepository<TempMPHEntity, Long>{

	
@Query(value = "select new com.lic.epgs.gratuity.policyservices.policymodification.dto.TempMPHModificationResponseDto(policy, mph ) from"
+ "   RenewalPolicyTMPEntity policy join TempMPHEntity mph "
+ "on policy.masterpolicyHolder = mph.id  And policy.policyNumber =:policyNumber And policy.policyStatusId =:stat ")
TempMPHModificationResponseDto getMPHByPolicyNumber(@Param("policyNumber")String policyNumber, @Param("stat")Long status);

@Query(value = "select new com.lic.epgs.gratuity.policyservices.policymodification.dto.TempMPHModificationResponseDto(policy, mph ) from"
		+ "   RenewalPolicyTMPEntity policy join TempMPHEntity mph "
		+ "on policy.masterpolicyHolder = mph.id  And policy.id =:policy  ")
TempMPHModificationResponseDto getByPolicyId(@Param("policy")Long policyId);


	}
