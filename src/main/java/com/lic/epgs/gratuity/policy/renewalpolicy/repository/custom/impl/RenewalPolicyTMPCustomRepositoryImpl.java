package com.lic.epgs.gratuity.policy.renewalpolicy.repository.custom.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.lic.epgs.gratuity.common.entity.CommonMasterProductEntity;
import com.lic.epgs.gratuity.common.entity.CommonMasterUnitEntity;
import com.lic.epgs.gratuity.common.entity.CommonMasterVariantEntity;
import com.lic.epgs.gratuity.common.repository.CommonMasterProductRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterVariantRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.RenewalPolicyTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.custom.RenewalPolicyTMPCustomRepository;

@Component
public class RenewalPolicyTMPCustomRepositoryImpl implements RenewalPolicyTMPCustomRepository {

	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;
	
	@Autowired
	private RenewalPolicyTMPRepository renewalPolicyTMPRepository;

	@Autowired
	private CommonMasterUnitRepository commonMasterUnitRepository;

	@Autowired
	private CommonMasterProductRepository commonMasterProductRepository;

	@Autowired
	private CommonMasterVariantRepository commonMasterVariantRepository;
	
	@Override
	public List<RenewalPolicyTMPEntity> findByquotationTaggedStatusId(Long quotationTaggedStatusId, String policyNumber) {
		List<RenewalPolicyTMPEntity> renewalPolicyTMPEntities = renewalPolicyTMPRepository.findByquotationTaggedStatusIdJpaRepo(quotationTaggedStatusId, policyNumber);

		List<RenewalPolicyTMPEntity> renewalPolicyTMPEntitiess = new ArrayList<>();
		for (RenewalPolicyTMPEntity renewalPolicyTMPEntity : renewalPolicyTMPEntities) {
			renewalPolicyTMPEntity = setUnitProps(renewalPolicyTMPEntity);
			renewalPolicyTMPEntity = setProductProps(renewalPolicyTMPEntity);
			renewalPolicyTMPEntitiess.add(setVariantProps(renewalPolicyTMPEntity));
		}

		return renewalPolicyTMPEntitiess;
	}
	
	@Override
	public List<RenewalPolicyTMPEntity> findBypolicytaggedStatusId(Long policytaggedStatusId) {
		List<RenewalPolicyTMPEntity> renewalPolicyTMPEntities = renewalPolicyTMPRepository.findBypolicytaggedStatusIdJpaRepo(policytaggedStatusId);

		List<RenewalPolicyTMPEntity> renewalPolicyTMPEntitiess = new ArrayList<>();
		for (RenewalPolicyTMPEntity renewalPolicyTMPEntity : renewalPolicyTMPEntities) {
			renewalPolicyTMPEntity = setUnitProps(renewalPolicyTMPEntity);
			renewalPolicyTMPEntity = setProductProps(renewalPolicyTMPEntity);
			renewalPolicyTMPEntitiess.add(setVariantProps(renewalPolicyTMPEntity));
		}

		return renewalPolicyTMPEntitiess;
	}
	
	@Override
	public RenewalPolicyTMPEntity findBytempidandmasterPolicyId(Long id, Long masterPolicyId) {
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findBytempidandmasterPolicyIdJpaRepo(id, masterPolicyId);
		renewalPolicyTMPEntity = setUnitProps(renewalPolicyTMPEntity);
		renewalPolicyTMPEntity = setProductProps(renewalPolicyTMPEntity);
		return setVariantProps(renewalPolicyTMPEntity);
	}
	
	@Override
	public List<RenewalPolicyTMPEntity> findByMasterPolicyId(Long masterPolicyId) {
		List<RenewalPolicyTMPEntity> renewalPolicyTMPEntities = renewalPolicyTMPRepository.findByMasterPolicyIdJpaRepo(masterPolicyId);

		List<RenewalPolicyTMPEntity> renewalPolicyTMPEntitiess = new ArrayList<>();
		for (RenewalPolicyTMPEntity renewalPolicyTMPEntity : renewalPolicyTMPEntities) {
			renewalPolicyTMPEntity = setUnitProps(renewalPolicyTMPEntity);
			renewalPolicyTMPEntity = setProductProps(renewalPolicyTMPEntity);
			renewalPolicyTMPEntitiess.add(setVariantProps(renewalPolicyTMPEntity));
		}

		return renewalPolicyTMPEntitiess;
	}
	
	@Override
	public List<RenewalPolicyTMPEntity> findByquotationTaggedStatusIdwithUnit(Long quotationTaggedStatusId,
			String policyNumber, String unitCode) {
		List<RenewalPolicyTMPEntity> renewalPolicyTMPEntities = renewalPolicyTMPRepository.findByquotationTaggedStatusIdwithUnitJpaRepo(quotationTaggedStatusId, policyNumber, unitCode);

		List<RenewalPolicyTMPEntity> renewalPolicyTMPEntitiess = new ArrayList<>();
		for (RenewalPolicyTMPEntity renewalPolicyTMPEntity : renewalPolicyTMPEntities) {
			renewalPolicyTMPEntity = setUnitProps(renewalPolicyTMPEntity);
			renewalPolicyTMPEntity = setProductProps(renewalPolicyTMPEntity);
			renewalPolicyTMPEntitiess.add(setVariantProps(renewalPolicyTMPEntity));
		}

		return renewalPolicyTMPEntitiess;
	}
	
	@Override
	public List<RenewalPolicyTMPEntity> findByquotationTaggedStatuswithgetUnitCode(Long quotationTaggedStatusId,
			String policyNumber,@Param("getUnitCode") String getUnitCode) {
		List<RenewalPolicyTMPEntity> renewalPolicyTMPEntities = renewalPolicyTMPRepository.findByquotationTaggedStatuswithgetUnitCodeJpaRepo(quotationTaggedStatusId, policyNumber, getUnitCode);

		List<RenewalPolicyTMPEntity> renewalPolicyTMPEntitiess = new ArrayList<>();
		for (RenewalPolicyTMPEntity renewalPolicyTMPEntity : renewalPolicyTMPEntities) {
			renewalPolicyTMPEntity = setUnitProps(renewalPolicyTMPEntity);
			renewalPolicyTMPEntity = setProductProps(renewalPolicyTMPEntity);
			renewalPolicyTMPEntitiess.add(setVariantProps(renewalPolicyTMPEntity));
		}

		return renewalPolicyTMPEntitiess;
	}
	
	@Override
	public RenewalPolicyTMPEntity findBypolicyNumber(String policyNumber) {
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findBypolicyNumberJpaRepo(policyNumber);
		renewalPolicyTMPEntity = setUnitProps(renewalPolicyTMPEntity);
		renewalPolicyTMPEntity = setProductProps(renewalPolicyTMPEntity);
		return setVariantProps(renewalPolicyTMPEntity);
	}
	
	@Override
	public List<RenewalPolicyTMPEntity> findBypolicytaggedStatusIdwithUnit(Long policytaggedStatusId, String policyNumber,
			String unitCode) {
		List<RenewalPolicyTMPEntity> renewalPolicyTMPEntities = renewalPolicyTMPRepository.findBypolicytaggedStatusIdwithUnitJpaRepo(policytaggedStatusId, policyNumber, unitCode);

		List<RenewalPolicyTMPEntity> renewalPolicyTMPEntitiess = new ArrayList<>();
		for (RenewalPolicyTMPEntity renewalPolicyTMPEntity : renewalPolicyTMPEntities) {
			renewalPolicyTMPEntity = setUnitProps(renewalPolicyTMPEntity);
			renewalPolicyTMPEntity = setProductProps(renewalPolicyTMPEntity);
			renewalPolicyTMPEntitiess.add(setVariantProps(renewalPolicyTMPEntity));
		}

		return renewalPolicyTMPEntitiess;
	}
	
	@Override
	public List<RenewalPolicyTMPEntity> findBypolicytaggedStatusIdwithgetUnitCode(Long policytaggedStatusId,
			String policyNumber, String getUnitCode) {
		List<RenewalPolicyTMPEntity> renewalPolicyTMPEntities = renewalPolicyTMPRepository.findBypolicytaggedStatusIdwithgetUnitCodeJpaRepo(policytaggedStatusId, policyNumber, getUnitCode);

		List<RenewalPolicyTMPEntity> renewalPolicyTMPEntitiess = new ArrayList<>();
		for (RenewalPolicyTMPEntity renewalPolicyTMPEntity : renewalPolicyTMPEntities) {
			renewalPolicyTMPEntity = setUnitProps(renewalPolicyTMPEntity);
			renewalPolicyTMPEntity = setProductProps(renewalPolicyTMPEntity);
			renewalPolicyTMPEntitiess.add(setVariantProps(renewalPolicyTMPEntity));
		}

		return renewalPolicyTMPEntitiess;
	}
	
	@Override
	public List<RenewalPolicyTMPEntity> findBypolicytaggedStatusId(Long policytaggedStatusId, String policyNumber) {
		List<RenewalPolicyTMPEntity> renewalPolicyTMPEntities = renewalPolicyTMPRepository.findBypolicytaggedStatusIdJpaRepo(policytaggedStatusId, policyNumber);

		List<RenewalPolicyTMPEntity> renewalPolicyTMPEntitiess = new ArrayList<>();
		for (RenewalPolicyTMPEntity renewalPolicyTMPEntity : renewalPolicyTMPEntities) {
			renewalPolicyTMPEntity = setUnitProps(renewalPolicyTMPEntity);
			renewalPolicyTMPEntity = setProductProps(renewalPolicyTMPEntity);
			renewalPolicyTMPEntitiess.add(setVariantProps(renewalPolicyTMPEntity));
		}

		return renewalPolicyTMPEntitiess;
	}
	
	@Override
	public RenewalPolicyTMPEntity findByMasterPolicyIdAndAnnualRenewalDate(Long masterPolicyId, Date annualRenewalDate) {
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findByMasterPolicyIdAndAnnualRenewalDateJpaRepo(masterPolicyId, annualRenewalDate);
		renewalPolicyTMPEntity = setUnitProps(renewalPolicyTMPEntity);
		renewalPolicyTMPEntity = setProductProps(renewalPolicyTMPEntity);
		return setVariantProps(renewalPolicyTMPEntity);
	}
	
	@Override
	public RenewalPolicyTMPEntity findByTmpPolicyId(@Param("tmpPolicyId") Long tmpPolicyId) {
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository.findByTmpPolicyId(tmpPolicyId);
		renewalPolicyTMPEntity = setUnitProps(renewalPolicyTMPEntity);
		renewalPolicyTMPEntity = setProductProps(renewalPolicyTMPEntity);
		return setVariantProps(renewalPolicyTMPEntity);
	}
	
	@Override
	public RenewalPolicyTMPEntity setTransientValues(RenewalPolicyTMPEntity renewalPolicyTMPEntity) {
		renewalPolicyTMPEntity = setUnitProps(renewalPolicyTMPEntity);
		renewalPolicyTMPEntity = setProductProps(renewalPolicyTMPEntity);
		return setVariantProps(renewalPolicyTMPEntity);
	}
	
	private RenewalPolicyTMPEntity setUnitProps(RenewalPolicyTMPEntity renewalPolicyTMPEntity) {
		if (isDevEnvironment) {
			renewalPolicyTMPEntity.setUnitId("1");
			renewalPolicyTMPEntity.setUnitOffice("Mumbai");
		} else {
			CommonMasterUnitEntity commonMasterUnitEntity = commonMasterUnitRepository
					.findByUnitCode(renewalPolicyTMPEntity.getUnitCode());
			renewalPolicyTMPEntity.setUnitId(commonMasterUnitEntity.getId().toString());
			renewalPolicyTMPEntity.setUnitOffice(commonMasterUnitEntity.getDescription());
		}
		return renewalPolicyTMPEntity;
	}

	private RenewalPolicyTMPEntity setProductProps(RenewalPolicyTMPEntity renewalPolicyTMPEntity) {
		if (isDevEnvironment) {
			renewalPolicyTMPEntity.setProductName("LIC’s New Group Gratuity Cash Accumulation Plan");
		} else {
			CommonMasterProductEntity commonMasterProductEntity = commonMasterProductRepository
					.findById(renewalPolicyTMPEntity.getProductId()).get();
			renewalPolicyTMPEntity.setProductName(commonMasterProductEntity.getProductName());
		}
		return renewalPolicyTMPEntity;
	}

	private RenewalPolicyTMPEntity setVariantProps(RenewalPolicyTMPEntity renewalPolicyTMPEntity) {
		if (isDevEnvironment) {
			renewalPolicyTMPEntity.setProductVariant("GGS");
		} else {
			CommonMasterProductEntity commonMasterProductEntity = commonMasterProductRepository
					.findById(renewalPolicyTMPEntity.getProductId()).get();
			renewalPolicyTMPEntity.setProductVariant(commonMasterProductEntity.getProductCode());
		}
		return renewalPolicyTMPEntity;
	}
}
