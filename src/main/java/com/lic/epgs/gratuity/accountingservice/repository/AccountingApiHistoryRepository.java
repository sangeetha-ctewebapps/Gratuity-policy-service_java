package com.lic.epgs.gratuity.accountingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.entity.AccountingApiHistoryEntity;

@Repository
public interface AccountingApiHistoryRepository extends JpaRepository<AccountingApiHistoryEntity, Long> {

	
	
}
