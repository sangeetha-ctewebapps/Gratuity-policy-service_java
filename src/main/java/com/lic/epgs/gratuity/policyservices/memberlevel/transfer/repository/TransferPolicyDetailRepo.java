package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferPolicyDetailEntity;

public interface TransferPolicyDetailRepo extends JpaRepository<TransferPolicyDetailEntity, Long>{

	TransferPolicyDetailEntity findByTransferRequisitionId(Long transferRequisitionId);

}
