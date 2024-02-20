package com.lic.epgs.gratuity.policy.lifecover.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policy.lifecover.entity.HistoryLifeCoverEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateHistoryEntity;

public interface HistoryLifeCoverRepository extends JpaRepository<HistoryLifeCoverEntity, Long>{
	List<HistoryLifeCoverEntity> findBypolicyId(Long id);

}
