package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lic.epgs.gratuity.common.entity.CommonMasterProductEntity;
import com.lic.epgs.gratuity.common.entity.CommonMasterUnitEntity;
import com.lic.epgs.gratuity.common.entity.CommonMasterVariantEntity;
import com.lic.epgs.gratuity.common.repository.CommonMasterProductRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterVariantRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.entity.StagingPolicyEntity;
import com.lic.epgs.gratuity.policy.repository.StagingPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.custom.StagingPolicyCustomRepository;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.StagingPolicyDestinationVersionEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.StagingPolicyCustomDestinationVersionRepository;

@Component
public class StagingPolicyCustomDestinationVerRepositoryImpl implements StagingPolicyCustomDestinationVersionRepository {

	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;
	
	@Autowired
	private StagingPolicyRepository stagingPolicyRepository;

	@Autowired
	private CommonMasterUnitRepository commonMasterUnitRepository;

	@Autowired
	private CommonMasterProductRepository commonMasterProductRepository;

	@Autowired
	private CommonMasterVariantRepository commonMasterVariantRepository;
	

	
	@Override
	public StagingPolicyEntity setTransientValues(StagingPolicyEntity stagingPolicyEntity) {
		stagingPolicyEntity = setUnitProps(stagingPolicyEntity);
		stagingPolicyEntity = setProductProps(stagingPolicyEntity);
		return stagingPolicyEntity;
	}
	
	private StagingPolicyEntity setUnitProps(StagingPolicyEntity stagingPolicyEntity) {
		if (isDevEnvironment) {
			stagingPolicyEntity.setUnitId("1");
			stagingPolicyEntity.setUnitOffice("Mumbai");
		} else {
			CommonMasterUnitEntity commonMasterUnitEntity = commonMasterUnitRepository
					.findByUnitCode(stagingPolicyEntity.getUnitCode());
			stagingPolicyEntity.setUnitId(commonMasterUnitEntity.getId().toString());
			stagingPolicyEntity.setUnitOffice(commonMasterUnitEntity.getDescription());
		}
		return stagingPolicyEntity;
	}

	private StagingPolicyEntity setProductProps(StagingPolicyEntity stagingPolicyEntity) {
		if (isDevEnvironment) {
			stagingPolicyEntity.setProductName("LIC’s New Group Gratuity Cash Accumulation Plan");
			stagingPolicyEntity.setProductVariant("GGS");
		} else {
			CommonMasterProductEntity commonMasterProductEntity = commonMasterProductRepository
					.findById(stagingPolicyEntity.getProductId()).get();
			stagingPolicyEntity.setProductName(commonMasterProductEntity.getProductName());
			stagingPolicyEntity.setProductVariant(commonMasterProductEntity.getProductCode());
		}
		return stagingPolicyEntity;
	}


}
