package com.lic.epgs.gratuity.policy.gratuitybenefit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.HistoryGratuityBenefitEntity;

public interface HistoryGratutiyBenefitRepository extends JpaRepository<HistoryGratuityBenefitEntity, Long> {
	List<HistoryGratuityBenefitEntity> findBypolicyId(Long id);

}
