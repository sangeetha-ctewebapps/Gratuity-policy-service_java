package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferMemberPolicyDetailEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferPolicyDetailEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferRequisitionEntity;

public interface TransferMemberPolicyDetailRepo extends JpaRepository<TransferMemberPolicyDetailEntity, Long>{

	TransferMemberPolicyDetailEntity findByTransferRequisitionId(Long transferRequisitionId);

	TransferMemberPolicyDetailEntity findPolicyByTransferRequisitionId(Long transferRequisitionId);

	//TransferMemberPolicyDetailEntity findByIdPolicyTempId(Long tmpPolicyId);

}
