package com.lic.epgs.gratuity.policyservices.policymodification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.mph.entity.TempMPHBankEntity;

public interface TempMPHBankRepositoryPS extends JpaRepository<TempMPHBankEntity, Long>{
	
	@Query(value = "DELETE FROM PMST_TMP_MPH_BANK WHERE MPH_BANK_ID=?1", nativeQuery=true)
	void deleteMphBankId(Long mphbankId);

	List<TempMPHBankEntity> findBymasterMphId(Long id);

}
