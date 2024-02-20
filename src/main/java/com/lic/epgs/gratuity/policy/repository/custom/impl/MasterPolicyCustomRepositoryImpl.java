package com.lic.epgs.gratuity.policy.repository.custom.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.CommonMasterProductEntity;
import com.lic.epgs.gratuity.common.entity.CommonMasterUnitEntity;
import com.lic.epgs.gratuity.common.entity.CommonMasterVariantEntity;
import com.lic.epgs.gratuity.common.repository.CommonMasterProductRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterVariantRepository;
import com.lic.epgs.gratuity.policy.claim.entity.PolicyTempSearchEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicySearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyTmpSearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicySearchEntity;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;

@Component
public class MasterPolicyCustomRepositoryImpl implements MasterPolicyCustomRepository {

	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;

	@Autowired
	private MasterPolicyRepository masterPolicyRepository;

	@Autowired
	private CommonMasterUnitRepository commonMasterUnitRepository;

	@Autowired
	private CommonMasterProductRepository commonMasterProductRepository;

	@Autowired
	private CommonMasterVariantRepository commonMasterVariantRepository;

	@Override
	public MasterPolicyEntity findById(Long policyId) {
		MasterPolicyEntity masterPolicyEntity = setUnitProps(masterPolicyRepository.findById(policyId).get());
		masterPolicyEntity = setProductProps(masterPolicyEntity);
		return setProductProps(masterPolicyEntity);
	}

	@Override
	public List<MasterPolicyEntity> findNotInPolicyRenewal(String value) {
		List<MasterPolicyEntity> masterPolicyEntities = masterPolicyRepository.findNotInPolicyRenewalJpaRepo(value);

		List<MasterPolicyEntity> masterPolicyEntitiess = new ArrayList<>();
		for (MasterPolicyEntity masterPolicyEntity : masterPolicyEntities) {
			masterPolicyEntity = setUnitProps(masterPolicyEntity);
			masterPolicyEntity = setProductProps(masterPolicyEntity);
			masterPolicyEntitiess.add(setProductProps(masterPolicyEntity));
		}

		return masterPolicyEntities;
	}

	@Override
	public List<MasterPolicyEntity> findNotInPolicyRenewalRemainder(String value) {
		List<MasterPolicyEntity> masterPolicyEntities = masterPolicyRepository
				.findNotInPolicyRenewalRemainderJpaRepo(value);

		List<MasterPolicyEntity> masterPolicyEntitiess = new ArrayList<>();
		for (MasterPolicyEntity masterPolicyEntity : masterPolicyEntities) {
			masterPolicyEntity = setUnitProps(masterPolicyEntity);
			masterPolicyEntity = setProductProps(masterPolicyEntity);
			masterPolicyEntitiess.add(setProductProps(masterPolicyEntity));
		}

		return masterPolicyEntities;
	}

	@Override
	public List<MasterPolicyEntity> fetchpolicyDetails() {
		List<MasterPolicyEntity> masterPolicyEntities = masterPolicyRepository.fetchpolicyDetailsJpaRepo();

		List<MasterPolicyEntity> masterPolicyEntitiess = new ArrayList<>();
		for (MasterPolicyEntity masterPolicyEntity : masterPolicyEntities) {
			masterPolicyEntity = setUnitProps(masterPolicyEntity);
			masterPolicyEntity = setProductProps(masterPolicyEntity);
			masterPolicyEntitiess.add(setProductProps(masterPolicyEntity));
		}

		return masterPolicyEntities;
	}

	@Override
	public List<MasterPolicyEntity> findByPolicyNumber(String policyNumber) {
		List<MasterPolicyEntity> masterPolicyEntities = masterPolicyRepository.findByPolicyNumberJpaRepo(policyNumber);

		List<MasterPolicyEntity> masterPolicyEntitiess = new ArrayList<>();
		for (MasterPolicyEntity masterPolicyEntity : masterPolicyEntities) {
			masterPolicyEntity = setUnitProps(masterPolicyEntity);
			masterPolicyEntity = setProductProps(masterPolicyEntity);
			masterPolicyEntitiess.add(setProductProps(masterPolicyEntity));
		}

		return masterPolicyEntities;
	}
	
	@Override
	public List<MasterPolicyEntity> findByPolicyNumberValidation(String policyNumber) {
		  List<MasterPolicyEntity> masterPolicyEntityy = masterPolicyRepository.findByPolicyNumberJpa(policyNumber);
		  List<MasterPolicyEntity> masterPolicyEntitiess = new ArrayList<>();
			for (MasterPolicyEntity masterPolicyEntity : masterPolicyEntityy) {
				masterPolicyEntity = setUnitProps(masterPolicyEntity);
				masterPolicyEntity = setProductProps(masterPolicyEntity);
				masterPolicyEntitiess.add(setProductProps(masterPolicyEntity));
			}
		return masterPolicyEntitiess;
	}

	@Override
	public MasterPolicyEntity findPolicyDetail(String policyNumber) {
		MasterPolicyEntity masterPolicyEntity = setUnitProps(
				masterPolicyRepository.findPolicyDetailJpaRepo(policyNumber));
		masterPolicyEntity = setProductProps(masterPolicyEntity);
		return setProductProps(masterPolicyEntity);
	}

	@Override
	public MasterPolicyEntity findBypolicyNumber(String policyNumber) {
		MasterPolicyEntity masterPolicyEntity = setUnitProps(
				masterPolicyRepository.findBypolicyNumberJpaRepo(policyNumber));
		masterPolicyEntity = setProductProps(masterPolicyEntity);
		return setProductProps(masterPolicyEntity);
	}

	@Override
	public List<MasterPolicyEntity> findPolicyDetailSeerch(String policyNumber) {
		List<MasterPolicyEntity> masterPolicyEntities = masterPolicyRepository
				.findPolicyDetailSeerchJpaRepo(policyNumber);

		List<MasterPolicyEntity> masterPolicyEntitiess = new ArrayList<>();
		for (MasterPolicyEntity masterPolicyEntity : masterPolicyEntities) {
			masterPolicyEntity = setUnitProps(masterPolicyEntity);
			masterPolicyEntity = setProductProps(masterPolicyEntity);
			masterPolicyEntitiess.add(setProductProps(masterPolicyEntity));
		}

		return masterPolicyEntities;
	}

	@Override
	public MasterPolicyEntity findByPolicyNumberisactive(String policyNumber) {
		MasterPolicyEntity masterPolicyEntity = setUnitProps(
				masterPolicyRepository.findByPolicyNumberisactiveJpaRepo(policyNumber));
		masterPolicyEntity = setProductProps(masterPolicyEntity);
		return setProductProps(masterPolicyEntity);
	}

	@Override
	public List<MasterPolicyEntity> findByPolicyNumberwithUnitcode(String policyNumber, String unitcode) {
		List<MasterPolicyEntity> masterPolicyEntities = masterPolicyRepository
				.findByPolicyNumberwithUnitcodeJpaRepo(policyNumber, unitcode);

		List<MasterPolicyEntity> masterPolicyEntitiess = new ArrayList<>();
		for (MasterPolicyEntity masterPolicyEntity : masterPolicyEntities) {
			masterPolicyEntity = setUnitProps(masterPolicyEntity);
			masterPolicyEntity = setProductProps(masterPolicyEntity);
			masterPolicyEntitiess.add(setProductProps(masterPolicyEntity));
		}

		return masterPolicyEntities;
	}

	@Override
	public List<MasterPolicyEntity> findBypolicyNumberandActive(String policyNumber) {
		List<MasterPolicyEntity> masterPolicyEntities = masterPolicyRepository
				.findBypolicyNumberandActiveJpaRepo(policyNumber);

		List<MasterPolicyEntity> masterPolicyEntitiess = new ArrayList<>();
		for (MasterPolicyEntity masterPolicyEntity : masterPolicyEntities) {
			masterPolicyEntity = setUnitProps(masterPolicyEntity);
			masterPolicyEntity = setProductProps(masterPolicyEntity);
			masterPolicyEntitiess.add(setProductProps(masterPolicyEntity));
		}

		return masterPolicyEntities;
	}

	@Override
	public MasterPolicyEntity findBymasterPolicyId(Long masterPolicyId, String dateOfExit) {
		MasterPolicyEntity masterPolicyEntity = setUnitProps(
				masterPolicyRepository.findBymasterPolicyIdJpaRepo(masterPolicyId, dateOfExit));
		masterPolicyEntity = setProductProps(masterPolicyEntity);
		return setProductProps(masterPolicyEntity);
	}

	@Override
	public MasterPolicyEntity findByGreaterStartDateandExitDate(Long masterPolicyId, String dateOfExit) {
		MasterPolicyEntity masterPolicyEntity = setUnitProps(
				masterPolicyRepository.findByGreaterStartDateandExitDateJpaRepo(masterPolicyId, dateOfExit));
		if(masterPolicyEntity!=null)
		masterPolicyEntity = setProductProps(masterPolicyEntity);
		return setProductProps(masterPolicyEntity);
	}
	
//	--------------------------------------
	@Override
	public MasterPolicyEntity setTransientValues(MasterPolicyEntity masterPolicyEntity) {
		masterPolicyEntity = setUnitProps(masterPolicyEntity);
		masterPolicyEntity = setProductProps(masterPolicyEntity);
		return setProductProps(masterPolicyEntity);
	}
	
	@Override
	public RenewalPolicySearchEntity setTransientValues(RenewalPolicySearchEntity masterPolicyEntity) {
		masterPolicyEntity = setUnitProps(masterPolicyEntity);
		masterPolicyEntity = setProductProps(masterPolicyEntity);
		return setProductProps(masterPolicyEntity);
	}
	
	
	private RenewalPolicySearchEntity setUnitProps(RenewalPolicySearchEntity masterPolicyEntity) {
		if (isDevEnvironment) {
			if(masterPolicyEntity!=null) {
			masterPolicyEntity.setUnitId("1");
			masterPolicyEntity.setUnitOffice("Mumbai");
			}
		} else {
			if(masterPolicyEntity != null) {
			CommonMasterUnitEntity commonMasterUnitEntity = commonMasterUnitRepository
					.findByUnitCode(masterPolicyEntity.getUnitCode());
			masterPolicyEntity.setUnitId(commonMasterUnitEntity.getId().toString());
			masterPolicyEntity.setUnitOffice(commonMasterUnitEntity.getDescription());
			}
		}
		return masterPolicyEntity;
	}
	
	private RenewalPolicySearchEntity setProductProps(RenewalPolicySearchEntity masterPolicyEntity) {
		if (isDevEnvironment) {
			if(masterPolicyEntity!=null) {
			masterPolicyEntity.setProductName("LIC’s New Group Gratuity Cash Accumulation Plan");
			masterPolicyEntity.setProductVariant("GGS");
			}
		} else {
			if(masterPolicyEntity != null) {
			CommonMasterProductEntity commonMasterProductEntity = commonMasterProductRepository
					.findById(masterPolicyEntity.getProductId()).get();
			masterPolicyEntity.setProductName(commonMasterProductEntity.getProductName());
			masterPolicyEntity.setProductVariant(commonMasterProductEntity.getProductCode());
			}
		}
		return masterPolicyEntity;
	}
	
//	private RenewalPolicySearchEntity setVariantProps(RenewalPolicySearchEntity masterPolicyEntity) {
//		if (isDevEnvironment) {
//			if(masterPolicyEntity!=null) {
//			masterPolicyEntity.setProductVariant("LIC’s New Group Gratuity Cash Accumulation Plan-V3");
//			}
//		} else {
//			if(masterPolicyEntity != null) {
//			CommonMasterVariantEntity commonMasterVariantEntity = commonMasterVariantRepository
//					.findById(masterPolicyEntity.getProductVariantId()).get();
//			masterPolicyEntity.setProductVariant(commonMasterVariantEntity.getVariantName());
//			}
//		}
//		return masterPolicyEntity;
//	}

	private MasterPolicyEntity setUnitProps(MasterPolicyEntity masterPolicyEntity) {
		if (isDevEnvironment) {
			if(masterPolicyEntity!=null) {
			masterPolicyEntity.setUnitId("1");
			masterPolicyEntity.setUnitOffice("Mumbai");
			}
		} else {
			if(masterPolicyEntity != null) {
			CommonMasterUnitEntity commonMasterUnitEntity = commonMasterUnitRepository
					.findByUnitCode(masterPolicyEntity.getUnitCode());
			masterPolicyEntity.setUnitId(commonMasterUnitEntity.getId().toString());
			masterPolicyEntity.setUnitOffice(commonMasterUnitEntity.getDescription());
			}
		}
		return masterPolicyEntity;
	}

	private MasterPolicyEntity setProductProps(MasterPolicyEntity masterPolicyEntity) {
		if (isDevEnvironment) {
			if(masterPolicyEntity!=null) {
			masterPolicyEntity.setProductName("LIC’s New Group Gratuity Cash Accumulation Plan");
			masterPolicyEntity.setProductVariant("GGS");
			}
		} else {
			if(masterPolicyEntity != null) {
			CommonMasterProductEntity commonMasterProductEntity = commonMasterProductRepository
					.findById(masterPolicyEntity.getProductId()).get();
			CommonMasterVariantEntity commonMasterVariantEntity = commonMasterVariantRepository.findById(masterPolicyEntity.getProductVariantId()).get();
			masterPolicyEntity.setProductName(commonMasterProductEntity.getProductName());
			masterPolicyEntity.setProductVariant(commonMasterVariantEntity.getVariantVersion());
			}
		}
		return masterPolicyEntity;
	}

//	private MasterPolicyEntity setVariantProps(MasterPolicyEntity masterPolicyEntity) {
//		if (isDevEnvironment) {
//			if(masterPolicyEntity!=null) {
//			masterPolicyEntity.setProductVariant("GGS");
//			}
//		} else {
//			if(masterPolicyEntity != null) {
//			CommonMasterVariantEntity commonMasterVariantEntity = commonMasterVariantRepository
//					.findById(masterPolicyEntity.getProductVariantId()).get();
//			masterPolicyEntity.setProductVariant(commonMasterVariantEntity.getProductType());
//			}
//		}
//		return masterPolicyEntity;
//	}

	
	@Override
	public MasterPolicySearchEntity setTransientValues(MasterPolicySearchEntity policySearch) {
		policySearch = setUnitProps(policySearch);
		policySearch = setProductProps(policySearch);
		return setProductProps(policySearch);
	}
	
	private MasterPolicySearchEntity setUnitProps(MasterPolicySearchEntity policySearch) {
		if (isDevEnvironment) {
			if(policySearch!=null) {
				policySearch.setUnitId("1");
				policySearch.setUnitOffice("Mumbai");
			}
		} else {
			if(policySearch != null) {
			CommonMasterUnitEntity commonMasterUnitEntity = commonMasterUnitRepository
					.findByUnitCode(policySearch.getUnitCode());
			policySearch.setUnitId(commonMasterUnitEntity.getId().toString());
			policySearch.setUnitOffice(commonMasterUnitEntity.getDescription());
			}
		}
		return policySearch;
	}
	
	
	private MasterPolicySearchEntity setProductProps(MasterPolicySearchEntity policySearch) {
		if (isDevEnvironment) {
			if(policySearch!=null) {
				policySearch.setProductName("LIC’s New Group Gratuity Cash Accumulation Plan");
				policySearch.setProductVariant("GGS");
			}
		} else {
			if(policySearch != null) {
			CommonMasterProductEntity commonMasterProductEntity = commonMasterProductRepository
					.findById(policySearch.getProductId()).get();
			policySearch.setProductName(commonMasterProductEntity.getProductName());
			policySearch.setProductVariant(commonMasterProductEntity.getProductCode());
			}
		}
		return policySearch;
	}
	//end
	
	@Override
	public PolicyTmpSearchEntity setTransientValue(PolicyTmpSearchEntity policySearch) {
		policySearch = setUnitProps(policySearch);
		policySearch = setProductProps(policySearch);
		return setProductProps(policySearch);
	}

	
	private PolicyTmpSearchEntity setUnitProps(PolicyTmpSearchEntity policySearch) {
		if (isDevEnvironment) {
			if(policySearch!=null) {
				policySearch.setUnitId("1");
				policySearch.setUnitOffice("Mumbai");
			}
		} else {
			if(policySearch != null) {
			CommonMasterUnitEntity commonMasterUnitEntity = commonMasterUnitRepository
					.findByUnitCode(policySearch.getUnitCode());
			policySearch.setUnitId(commonMasterUnitEntity.getId().toString());
			policySearch.setUnitOffice(commonMasterUnitEntity.getDescription());
			}
		}
		return policySearch;
	}
	
		private PolicyTmpSearchEntity setProductProps(PolicyTmpSearchEntity policySearch) {
		if (isDevEnvironment) {
			if(policySearch!=null) {
				policySearch.setProductName("LIC’s New Group Gratuity Cash Accumulation Plan");
				policySearch.setProductVariant("GGS");
			}
		} else {
			if(policySearch != null) {
			CommonMasterProductEntity commonMasterProductEntity = commonMasterProductRepository
					.findById(policySearch.getProductId()).get();
			policySearch.setProductName(commonMasterProductEntity.getProductName());
			policySearch.setProductVariant(commonMasterProductEntity.getProductCode());
			}
		}
		return policySearch;
	}

		
		
		
//		-------------------------------------------------------
		@Override
		public PolicyTempSearchEntity setTransientValue(PolicyTempSearchEntity policySearch) {
			policySearch = setUnitProps(policySearch);
			policySearch = setProductProps(policySearch);
			return setProductProps(policySearch);
		}
		
		
		private PolicyTempSearchEntity setProductProps(PolicyTempSearchEntity policySearch) {
			if (isDevEnvironment) {
				if(policySearch!=null) {
					policySearch.setUnitId("1");
					policySearch.setUnitOffice("Mumbai");
				}
			} else {
				if(policySearch != null) {
				CommonMasterUnitEntity commonMasterUnitEntity = commonMasterUnitRepository
						.findByUnitCode(policySearch.getUnitCode());
				policySearch.setUnitId(commonMasterUnitEntity.getId().toString());
				policySearch.setUnitOffice(commonMasterUnitEntity.getDescription());
				}
			}
			return policySearch;
		}

		private PolicyTempSearchEntity setUnitProps(PolicyTempSearchEntity policySearch) {
			if (isDevEnvironment) {
				if(policySearch!=null) {
					policySearch.setProductName("LIC’s New Group Gratuity Cash Accumulation Plan");
					policySearch.setProductVariant("GGS");
				}
			} else {
				if(policySearch != null) {
				CommonMasterProductEntity commonMasterProductEntity = commonMasterProductRepository
						.findById(policySearch.getProductId()).get();
				policySearch.setProductName(commonMasterProductEntity.getProductName());
				policySearch.setProductVariant(commonMasterProductEntity.getProductCode());
				}
			}
			return policySearch;
		}

		//--------------------
		@Override
		public RenewalPolicyTMPDto setTransientValues(RenewalPolicyTMPDto renewalPolicyTMPDto) {
			renewalPolicyTMPDto = setUnitProps(renewalPolicyTMPDto);
			renewalPolicyTMPDto = setProductProps(renewalPolicyTMPDto);
			return setProductProps(renewalPolicyTMPDto);
		}

		private RenewalPolicyTMPDto setProductProps(RenewalPolicyTMPDto renewalPolicyTMPDto) {
			if (isDevEnvironment) {
				if(renewalPolicyTMPDto!=null) {
					renewalPolicyTMPDto.setUnitId("1");
					renewalPolicyTMPDto.setUnitOffice("Mumbai");
				}
			} else {
				if(renewalPolicyTMPDto != null) {
				CommonMasterUnitEntity commonMasterUnitEntity = commonMasterUnitRepository
						.findByUnitCode(renewalPolicyTMPDto.getUnitCode());
				renewalPolicyTMPDto.setUnitId(commonMasterUnitEntity.getId().toString());
				renewalPolicyTMPDto.setUnitOffice(commonMasterUnitEntity.getDescription());
				}
			}
			return renewalPolicyTMPDto;
		}

		private RenewalPolicyTMPDto setUnitProps(RenewalPolicyTMPDto renewalPolicyTMPDto) {
			if (isDevEnvironment) {
				if(renewalPolicyTMPDto!=null) {
					renewalPolicyTMPDto.setProductName("LIC’s New Group Gratuity Cash Accumulation Plan");
					renewalPolicyTMPDto.setProductVariant("GGS");
				}
			} else {
				if(renewalPolicyTMPDto != null) {
				CommonMasterProductEntity commonMasterProductEntity = commonMasterProductRepository
						.findById(renewalPolicyTMPDto.getProductId()).get();
				renewalPolicyTMPDto.setProductName(commonMasterProductEntity.getProductName());
				renewalPolicyTMPDto.setProductVariant(commonMasterProductEntity.getProductCode());
				}
			}
			return renewalPolicyTMPDto;
		}
	
//	private PolicyTmpSearchEntity setVariantProps(PolicyTmpSearchEntity policySearch) {
//		if (isDevEnvironment) {
//			if(policySearch!=null) {
//				policySearch.setProductVariant("LIC’s New Group Gratuity Cash Accumulation Plan-V3");
//			}
//		} else {
//			if(policySearch != null) {
//			CommonMasterVariantEntity commonMasterVariantEntity = commonMasterVariantRepository
//					.findById(policySearch.getProductVariantId()).get();
//			policySearch.setProductVariant(commonMasterVariantEntity.getVariantName());
//			}
//		}
//		return policySearch;
//	}


	
}
