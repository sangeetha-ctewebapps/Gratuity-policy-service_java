package com.lic.epgs.gratuity.policyservices.unit.transferr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policyservices.unit.transfer.entity.UnitTransferPolicyDetailEntity;

public interface UnitTransferPolicyDetailRepo extends JpaRepository<UnitTransferPolicyDetailEntity, Long>{

	UnitTransferPolicyDetailEntity findByUnitTransferRequisitionId(Long unitTransferRequisitionId);

}
