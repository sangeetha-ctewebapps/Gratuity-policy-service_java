package com.lic.epgs.gratuity.mph.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.mph.entity.TempMPHBankEntity;

public interface TempMPHBankRepository extends JpaRepository<TempMPHBankEntity, Long> {

	@Query(value = "DELETE FROM PMST_TMP_MPH_BANK WHERE MPH_BANK_ID=?1", nativeQuery = true)
	void deleteMphBankId(Long mphbankId);

	@Query(value = "SELECT * FROM PMST_TMP_MPH_BANK WHERE MPH_BANK_ID=?1 AND IS_ACTIVE=1", nativeQuery = true)
	List<TempMPHBankEntity> findByMphId(Long mphId);

	@Query(value = "SELECT * FROM PMST_TMP_MPH_BANK WHERE MPH_ID=?1 AND IS_ACTIVE=1", nativeQuery = true)
	List<TempMPHBankEntity> findByMphId1(Long mphId);

	@Query(value = "SELECT * FROM PMST_TMP_MPH_BANK WHERE BANK_NAME=:bankName AND ACCOUNT_NUMBER=:accountNo AND MPH_ID=:tmpMphId", nativeQuery = true)
	List<TempMPHBankEntity> checkAccountNoAndBankNameAndMphId(String bankName, Long accountNo, Long tmpMphId);

}
