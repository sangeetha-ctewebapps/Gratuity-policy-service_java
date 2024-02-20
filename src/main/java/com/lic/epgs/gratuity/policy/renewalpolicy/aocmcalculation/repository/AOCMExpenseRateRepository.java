package com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.entity.AOCMExpenseRateEntity;

public interface AOCMExpenseRateRepository extends JpaRepository<AOCMExpenseRateEntity, Long>{

	List<AOCMExpenseRateEntity> findByvariant(String variant);

}
