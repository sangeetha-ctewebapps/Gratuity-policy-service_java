package com.lic.epgs.gratuity.policy.renewalpolicy.helper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.RenewalPolicyTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.dto.RenewalValuationBasicTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationBasicTMPEntity;

@Service
public class RenewalPolicyHelper {
	
	@Autowired
	private RenewalPolicyTMPRepository renewalPolicyTMPRepos;
	
	public static RenewalPolicyTMPDto entityToDto(RenewalPolicyTMPEntity renewalPolicyTMPEntity) {
		return new ModelMapper().map(renewalPolicyTMPEntity, RenewalPolicyTMPDto.class);
     }

	@SuppressWarnings("unchecked")
	public   List<Object[]> getPolicyMasterDataForExcel(Long tmpPolicyId) {
		
		
		return  renewalPolicyTMPRepos.findBytmpPolicyId(tmpPolicyId);
	}
	public static RenewalValuationBasicTMPDto entityToDto(RenewalValuationBasicTMPEntity renewalValuationBasicTMPEntity) {
	
		return new ModelMapper().map(renewalValuationBasicTMPEntity, RenewalValuationBasicTMPDto.class);
	}

}
