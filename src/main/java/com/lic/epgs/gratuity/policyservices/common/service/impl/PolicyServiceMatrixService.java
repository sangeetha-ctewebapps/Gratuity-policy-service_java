package com.lic.epgs.gratuity.policyservices.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyServiceEntitiy;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.PolicyServiceRepository;
import com.lic.epgs.gratuity.policyservices.common.dao.PolicyServiceMatrixDaoImpl;
import com.lic.epgs.gratuity.policyservices.common.dto.PolicyServiceMatrixDto;
import com.lic.epgs.gratuity.policyservices.merger.repository.MasterPolicyRepositoryMerger;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.StatusDto;

@Service
public class PolicyServiceMatrixService {

	@Autowired
	PolicyServiceMatrixDaoImpl policyServiceMatrixDaoImpl;
	
	@Autowired
	PolicyServiceRepository policyServiceRepository;
	
	@Autowired
	MasterPolicyRepositoryMerger masterPolicyRepository;
	
	
	public Object getMatrix() {
		
		return policyServiceMatrixDaoImpl.getMatrix();
	}


	public PolicyServiceMatrixDto getMatrix(String currentService, String serviceType) {
		List<PolicyServiceMatrixDto> list = policyServiceMatrixDaoImpl.getMatrix(currentService,serviceType);
		return list.get(0);
		
	}


	public Object getActiveServiceDetailsByPolicy(String policyNumber, String currentService) {
		String message = "";
		MasterPolicyEntity masterPolicyEntity = null;
		try {
			masterPolicyEntity = masterPolicyRepository.findByPolicyNumberAndIsActiveTrue(policyNumber);
		} catch (Exception e) {
			
		}
		
		
		List<PolicyServiceMatrixDto> listPolicyServiceMatrixDto = new ArrayList<>();

		List<PolicyServiceEntitiy> findByPolicyNumberAndServiceStatusIgnoreCase = policyServiceRepository
				.getActiveServiceByPolicyId(masterPolicyEntity.getId());
		
		if (findByPolicyNumberAndServiceStatusIgnoreCase.isEmpty()) {
			return new StatusDto("SUCCESS", message);
		}
		findByPolicyNumberAndServiceStatusIgnoreCase.forEach(policyDetails -> {
			try {
				PolicyServiceMatrixDto policyServiceMatrixDto = getMatrix(currentService,
						policyDetails.getServiceType());
				
				listPolicyServiceMatrixDto.add(policyServiceMatrixDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		});
		int count = 0;
		for (PolicyServiceMatrixDto policyServiceMatrixDto : listPolicyServiceMatrixDto) {
			if (policyServiceMatrixDto.getIsAllowed().equalsIgnoreCase("No")) {
				count++;
				message = message+policyServiceMatrixDto.getOngoingService()+",";
			}
		}

		message= message.substring(0, message.length() - 1);
		if (count > 0) {

			return new StatusDto("FAILED", message);
		} else {
			
			return new StatusDto("SUCCESS", message);
		}
	}

}
