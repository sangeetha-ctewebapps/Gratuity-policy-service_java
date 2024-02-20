package com.lic.epgs.gratuity.policy.repository.custom.impl;

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

@Component
public class StagingPolicyCustomRepositoryImpl implements StagingPolicyCustomRepository {

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
	public List<StagingPolicyEntity> findinProposalnumberexitpolicyno(String proposalNumber) {
		List<StagingPolicyEntity> stagingPolicyEntities = stagingPolicyRepository.findinProposalnumberexitpolicynoJpaRepo(proposalNumber);

		List<StagingPolicyEntity> stagingPolicyEntitiess = new ArrayList<>();
		for (StagingPolicyEntity stagingPolicyEntity : stagingPolicyEntities) {
			stagingPolicyEntity = setUnitProps(stagingPolicyEntity);
			stagingPolicyEntity = setProductProps(stagingPolicyEntity);
			stagingPolicyEntitiess.add(setProductProps(stagingPolicyEntity));
		}

		return stagingPolicyEntitiess;
	}
	
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

	
//	for Use policy modification --- using on temp table
	@Override
	public RenewalPolicyTMPEntity setTransientValues(RenewalPolicyTMPEntity stagingPolicyEntity) {
		stagingPolicyEntity = setUnitProps(stagingPolicyEntity);
		stagingPolicyEntity = setProductProps(stagingPolicyEntity);
		return setVariantProps(stagingPolicyEntity);
	}
	
	private RenewalPolicyTMPEntity setUnitProps(RenewalPolicyTMPEntity stagingPolicyEntity) {
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

	
	private RenewalPolicyTMPEntity setProductProps(RenewalPolicyTMPEntity stagingPolicyEntity) {
		if (isDevEnvironment) {
			stagingPolicyEntity.setProductName("LIC’s New Group Gratuity Cash Accumulation Plan");
		} else {
			CommonMasterProductEntity commonMasterProductEntity = commonMasterProductRepository
					.findById(stagingPolicyEntity.getProductId()).get();
			stagingPolicyEntity.setProductName(commonMasterProductEntity.getProductName());
		}
		return stagingPolicyEntity;
	}
	
	
	private RenewalPolicyTMPEntity setVariantProps(RenewalPolicyTMPEntity stagingPolicyEntity) {
		if (isDevEnvironment) {
			stagingPolicyEntity.setProductVariant("LIC’s New Group Gratuity Cash Accumulation Plan-V3");
		} else {
			CommonMasterVariantEntity commonMasterVariantEntity = commonMasterVariantRepository
					.findById(stagingPolicyEntity.getProductId()).get();
			stagingPolicyEntity.setProductVariant(commonMasterVariantEntity.getVariantName());
		}
		return stagingPolicyEntity;
	}
	
	
	
//	private StagingPolicyEntity setVariantProps(StagingPolicyEntity stagingPolicyEntity) {
//		if (isDevEnvironment) {
//			stagingPolicyEntity.setProductVariant("GGS");
//		} else {
////			CommonMasterVariantEntity commonMasterVariantEntity = commonMasterVariantRepository
////					.findById(stagingPolicyEntity.getProductVariantId()).get();
//			CommonMasterProductEntity commonMasterProductEntity = commonMasterProductRepository
//					.findById(stagingPolicyEntity.getProductId()).get();
//			stagingPolicyEntity.setProductVariant(commonMasterProductEntity.getProductCode());
//		}
//		return stagingPolicyEntity;
//	}
}
