package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lic.epgs.gratuity.common.entity.CommonMasterProductEntity;
import com.lic.epgs.gratuity.common.entity.CommonMasterUnitEntity;
import com.lic.epgs.gratuity.common.entity.CommonMasterVariantEntity;
import com.lic.epgs.gratuity.common.repository.CommonMasterProductRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterVariantRepository;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.MasterPolicyDestinationVersionEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.MasterPolicyCustomDestinationVersionRepository;

@Component
public class MasterPolicyCustomDestinationVerRepositoryImpl implements MasterPolicyCustomDestinationVersionRepository {

	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;

	@Autowired
	private CommonMasterUnitRepository commonMasterUnitRepository;

	@Autowired
	private CommonMasterProductRepository commonMasterProductRepository;

	@Autowired
	private CommonMasterVariantRepository commonMasterVariantRepository;

//	--------------------------------------
	public MasterPolicyEntity setTransientValues(
			MasterPolicyEntity masterPolicyEntity) {
		masterPolicyEntity = setUnitProps(masterPolicyEntity);
		masterPolicyEntity = setProductProps(masterPolicyEntity);
		return setProductProps(masterPolicyEntity);
	}

	private MasterPolicyEntity setUnitProps(
			MasterPolicyEntity masterPolicyEntity) {
		if (isDevEnvironment) {
			if (masterPolicyEntity != null) {
				masterPolicyEntity.setUnitId("1");
				masterPolicyEntity.setUnitOffice("Mumbai");
			}
		} else {
			if (masterPolicyEntity != null) {
				CommonMasterUnitEntity commonMasterUnitEntity = commonMasterUnitRepository
						.findByUnitCode(masterPolicyEntity.getUnitCode());
				masterPolicyEntity.setUnitId(commonMasterUnitEntity.getId().toString());
				masterPolicyEntity.setUnitOffice(commonMasterUnitEntity.getDescription());
			}
		}

		return masterPolicyEntity;
	}

	private MasterPolicyEntity setProductProps(
			MasterPolicyEntity masterPolicyEntity) {
		if (isDevEnvironment) {
			if (masterPolicyEntity != null) {
				masterPolicyEntity.setProductName("LIC’s New Group Gratuity Cash Accumulation Plan");
				masterPolicyEntity.setProductVariant("GGS");
			}
		} else {
			if (masterPolicyEntity != null) {
				CommonMasterProductEntity commonMasterProductEntity = commonMasterProductRepository
						.findById(masterPolicyEntity.getProductId()).get();				
				CommonMasterVariantEntity commonMasterVariantEntity = commonMasterVariantRepository
						.findByProductId(masterPolicyEntity.getProductId()); 
				masterPolicyEntity.setProductName(commonMasterProductEntity.getProductName());
				masterPolicyEntity.setProductVariant(commonMasterVariantEntity.getVariantVersion());
			}
		}
		return masterPolicyEntity;
	}

}
