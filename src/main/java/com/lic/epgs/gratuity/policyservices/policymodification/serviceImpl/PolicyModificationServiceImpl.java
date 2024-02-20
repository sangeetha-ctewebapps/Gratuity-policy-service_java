package com.lic.epgs.gratuity.policyservices.policymodification.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.ApiValidationResponse;
import com.lic.epgs.gratuity.common.entity.CommonMasterProductEntity;
import com.lic.epgs.gratuity.common.entity.CommonMasterVariantEntity;
import com.lic.epgs.gratuity.common.repository.CommonMasterProductRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterVariantRepository;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.StagingPolicyEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.repository.custom.StagingPolicyCustomRepository;
import com.lic.epgs.gratuity.policyservices.common.dto.PolicyServiceDto;
import com.lic.epgs.gratuity.policyservices.common.entity.TempPolicyServiceNotes;
import com.lic.epgs.gratuity.policyservices.common.enums.PolicyServiceName;
import com.lic.epgs.gratuity.policyservices.common.service.impl.NotesService;
import com.lic.epgs.gratuity.policyservices.common.service.impl.PolicyServicingCommonServiceImpl;
import com.lic.epgs.gratuity.policyservices.policymodification.dao.MPHModificationDao;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.DataTable;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.MPHModificationResponse;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.StatusDto;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.StatusMaster;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.TempMPHModificationResponseDto;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.TempPolicyModificationDataTableRequestDto;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.TempPolicyModificationDataTableResponseDto;
import com.lic.epgs.gratuity.policyservices.policymodification.repository.MPHAddressRepositoryPS;
import com.lic.epgs.gratuity.policyservices.policymodification.repository.MPHBankRepositoryPS;
import com.lic.epgs.gratuity.policyservices.policymodification.repository.MPHRepositoryPS;
import com.lic.epgs.gratuity.policyservices.policymodification.repository.MasterPolicyRepositoryPS;
import com.lic.epgs.gratuity.policyservices.policymodification.repository.RenewalPolicyTMPRepositoryPS;
import com.lic.epgs.gratuity.policyservices.policymodification.repository.TempMPHAddressRepositoryPS;
import com.lic.epgs.gratuity.policyservices.policymodification.repository.TempMPHBankRepositoryPS;
import com.lic.epgs.gratuity.policyservices.policymodification.repository.TempMPHRepositoryPS;
import com.lic.epgs.gratuity.policyservices.policymodification.service.MPHModificationService;
import com.lic.epgs.gratuity.policyservices.policymodification.util.Mapper;

@Service
public class PolicyModificationServiceImpl implements MPHModificationService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;

	@Autowired
	MPHRepositoryPS mphRepository;
	@Autowired
	MPHBankRepositoryPS mphBankRepository;
	@Autowired
	MPHAddressRepositoryPS mphAddressRepository;
	@Autowired
	TempMPHRepositoryPS tempMPHRepository;
	@Autowired
	TempMPHBankRepositoryPS tempMPHBankRepository;
	@Autowired
	TempMPHAddressRepositoryPS tempMPHAddressRepository;
	@Autowired
	RenewalPolicyTMPRepositoryPS renewalPolicyTMPRepository;
	@Autowired
	MasterPolicyRepositoryPS masterPolicyRepository;
	@Autowired
	MPHModificationDao mPHModificationDao;
//	@Autowired
//	TempPolicyServiceNotesRepository tempPolicyServiceNotesRepository;
	@Autowired
	PolicyServicingCommonServiceImpl policyServicingCommonServiceImpl;
	@Autowired
	Mapper mappers;
	@Autowired
	NotesService notesService;

	@Autowired
	private CommonMasterVariantRepository commonMasterVariantRepository;
	
	@Autowired
	private CommonMasterProductRepository commonMasterProductRepository;

	@Override
	public Object findById(String policyNumber, Long mphId) {
		logger.info("MPHModificationServiceImpl - findById - start");

		MPHModificationResponse mphModificationResponseDto = null;

		Optional<RenewalPolicyTMPEntity> checkPolicySendToMaker = renewalPolicyTMPRepository
				.findByPolicyNumberAndPolicyStatusId(policyNumber, StatusMaster.SEND_TO_MAKER);

		if (checkPolicySendToMaker.isPresent()) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

			TempMPHModificationResponseDto mphByPolicyNumber = tempMPHRepository.getMPHByPolicyNumber(policyNumber,
					StatusMaster.SEND_TO_MAKER);

			MPHModificationResponse convertValue = mapper.convertValue(mphByPolicyNumber,
					MPHModificationResponse.class);
			mphModificationResponseDto = convertValue;

			List<MPHBankEntity> bankList = new ArrayList();
			List<TempMPHBankEntity> tempMPHBankEntity = tempMPHBankRepository
					.findBymasterMphId(mphByPolicyNumber.getMphMaster().getId());

			for (TempMPHBankEntity tempMPHBankEntity2 : tempMPHBankEntity) {
				MPHBankEntity bank = mappers.convertTempMPHBankEntityToMPHBankEntity(tempMPHBankEntity2);
				bankList.add(bank);
			}
			mphModificationResponseDto.setMphBank(bankList);
			List<MPHAddressEntity> addressList = new ArrayList<>();
			List<TempMPHAddressEntity> tempMPHAddressEntity = tempMPHAddressRepository
					.findBymasterMphId(mphByPolicyNumber.getMphMaster().getId());
			for (TempMPHAddressEntity tempMPHAddressEntity2 : tempMPHAddressEntity) {
				MPHAddressEntity address = mappers.convertTempMPHAddressEntityToMPHAddressEntity(tempMPHAddressEntity2);
				addressList.add(address);
			}
			mphModificationResponseDto.setMphAddress(addressList);

//			Optional<TempPolicyServiceNotes> findByPmstTmpPolicyId = tempPolicyServiceNotesRepository
//					.findByPmstTmpPolicyId(mphByPolicyNumber.getPolicyDetails().getId());
//			mphModificationResponseDto.setNotes(findByPmstTmpPolicyId.get());

			mphModificationResponseDto.setExistingpolicyId(masterPolicyRepository.getStatusByPolicy(policyNumber));
			try {
				mphModificationResponseDto.setServiceNumber(policyServicingCommonServiceImpl
						.getServiceNumberByPolicyId(mphModificationResponseDto.getPolicyDetails().getId()));

			} catch (Exception e) {

			}

//			mphModificationResponseDto.setPolicyDetails(null);

			MasterPolicyEntity setProductProps = setProductProps(mphModificationResponseDto.getPolicyDetails());
			mphModificationResponseDto.setPolicyDetails(setProductProps);
			return mphModificationResponseDto;
		}

		Optional<RenewalPolicyTMPEntity> getPolicy = renewalPolicyTMPRepository
				.findByPolicyNumberAndPolicyStatusId(policyNumber, StatusMaster.PENDING_FOR_APPROVAL);

		if (getPolicy.isPresent()) {
			return new StatusDto("Policy Already Pending For Approval");
		}

		RenewalPolicyTMPEntity policyTempObj = renewalPolicyTMPRepository.getByPolicyNubmber(policyNumber,
				StatusMaster.DRAFT);

		if (policyTempObj == null) {
			MPHModificationResponse mPHModificationResponse = mphRepository.getPolicyAndMPHDetails(policyNumber);
			mPHModificationResponse
					.setMphBank(mphBankRepository.findByMasterMphId(mPHModificationResponse.getMphMaster().getId()));
			mPHModificationResponse.setMphAddress(
					mphAddressRepository.findByMasterMphId(mPHModificationResponse.getMphMaster().getId()));
//			mPHModificationResponse.setNotes(new TempPolicyServiceNotes());
			mPHModificationResponse.setExistingpolicyId(masterPolicyRepository.getStatusByPolicy(policyNumber));
			MasterPolicyEntity setProductProps = setProductProps(mPHModificationResponse.getPolicyDetails());
			mPHModificationResponse.setPolicyDetails(setProductProps);
			return mPHModificationResponse;
		} else {

			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

			TempMPHModificationResponseDto mphByPolicyNumber = tempMPHRepository.getMPHByPolicyNumber(policyNumber,
					StatusMaster.DRAFT);

			MPHModificationResponse convertValue = mapper.convertValue(mphByPolicyNumber,
					MPHModificationResponse.class);
			mphModificationResponseDto = convertValue;

			List<MPHBankEntity> bankList = new ArrayList();
			List<TempMPHBankEntity> tempMPHBankEntity = tempMPHBankRepository
					.findBymasterMphId(mphByPolicyNumber.getMphMaster().getId());

			for (TempMPHBankEntity tempMPHBankEntity2 : tempMPHBankEntity) {
				MPHBankEntity bank = mappers.convertTempMPHBankEntityToMPHBankEntity(tempMPHBankEntity2);
				bankList.add(bank);
			}
			mphModificationResponseDto.setMphBank(bankList);
			List<MPHAddressEntity> addressList = new ArrayList<>();
			List<TempMPHAddressEntity> tempMPHAddressEntity = tempMPHAddressRepository
					.findBymasterMphId(mphByPolicyNumber.getMphMaster().getId());
			for (TempMPHAddressEntity tempMPHAddressEntity2 : tempMPHAddressEntity) {
				MPHAddressEntity address = mappers.convertTempMPHAddressEntityToMPHAddressEntity(tempMPHAddressEntity2);
				addressList.add(address);
			}
			mphModificationResponseDto.setMphAddress(addressList);

//			Optional<TempPolicyServiceNotes> findByPmstTmpPolicyId = tempPolicyServiceNotesRepository
//					.findByPmstTmpPolicyId(mphByPolicyNumber.getPolicyDetails().getId());
//			mphModificationResponseDto.setNotes(findByPmstTmpPolicyId.get());
			mphModificationResponseDto.setExistingpolicyId(masterPolicyRepository.getStatusByPolicy(policyNumber));

			try {
				mphModificationResponseDto.setServiceNumber(policyServicingCommonServiceImpl
						.getServiceNumberByPolicyId(mphModificationResponseDto.getPolicyDetails().getId()));

			} catch (Exception e) {

			}
		}

//		 tempMPHRepository.getMPHByPolicyNumber(policyNumber);

		// MPHModificationResponseDto mphModificationResponseDto =
		// mphRepository.getByPolicyNumber(policyNumber);
		logger.info("MPH name :" + mphModificationResponseDto.getMphMaster().getMphName());
		logger.info("MPHModificationServiceImpl - findById - end");

		MasterPolicyEntity setProductProps = setProductProps(mphModificationResponseDto.getPolicyDetails());
		mphModificationResponseDto.setPolicyDetails(setProductProps);
		return mphModificationResponseDto;
	}

	@Override
	@Transactional
	public ApiValidationResponse saveTempMphDetails(MPHModificationResponse mPHModificationResponseDto) {

		RenewalPolicyTMPEntity policyTempObj = renewalPolicyTMPRepository.getByPolicyNubmberForSave(
				mPHModificationResponseDto.getPolicyDetails().getPolicyNumber(), StatusMaster.DRAFT,
				StatusMaster.SEND_TO_MAKER);

		if (policyTempObj == null) {

			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

			// save PMST_TMP_POLICY with DRAFT STATUS

			RenewalPolicyTMPEntity renewalPolicyTMPEntity = mapper
					.convertValue(mPHModificationResponseDto.getPolicyDetails(), RenewalPolicyTMPEntity.class);
			// set status from PICK-LIST
			renewalPolicyTMPEntity.setMasterPolicyId(renewalPolicyTMPEntity.getId());
			renewalPolicyTMPEntity.setId(null);

			renewalPolicyTMPEntity.setPolicyStatusId(StatusMaster.DRAFT);
			renewalPolicyTMPEntity.setCreatedDate(new Date());
			renewalPolicyTMPEntity.setIsActive(true);
			RenewalPolicyTMPEntity updatedTempPolicy = renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);
			logger.info("Temp Policy Id: " + updatedTempPolicy.getId());

			// Save PMST_TMP_MPH with modified coloumn
			TempMPHEntity tempMPHEntity = mapper.convertValue(mPHModificationResponseDto.getMphMaster(),
					TempMPHEntity.class);
			tempMPHEntity.setTmpPolicyId(updatedTempPolicy.getId());
			tempMPHEntity.setPmstId(tempMPHEntity.getId());
			tempMPHEntity.setId(null);

			// tempMPHEntity.setTmpPolicyId(1112L);
			tempMPHEntity.setCreatedDate(new Date());
			tempMPHEntity.setIsActive(true);
			TempMPHEntity saveTempMPHEntity = tempMPHRepository.save(tempMPHEntity);
			logger.info("Temp MPH Id: " + saveTempMPHEntity.getId());

			updatedTempPolicy.setMasterpolicyHolder(saveTempMPHEntity.getId());
			renewalPolicyTMPRepository.save(updatedTempPolicy);

			// PMST_TMP_MPH_BANK
			List<MPHBankEntity> mphBank = mPHModificationResponseDto.getMphBank();
			for (MPHBankEntity mphBankEntity : mphBank) {
				TempMPHBankEntity convertMPHBankEntityToTempMPHBankEntity = mappers
						.convertMPHBankEntityToTempMPHBankEntity(mphBankEntity);

				convertMPHBankEntityToTempMPHBankEntity.setMasterMph(saveTempMPHEntity);

				convertMPHBankEntityToTempMPHBankEntity.setPmstId(mphBankEntity.getId());
				convertMPHBankEntityToTempMPHBankEntity.setId(null);
				convertMPHBankEntityToTempMPHBankEntity.setCreatedDate(new Date());
				convertMPHBankEntityToTempMPHBankEntity.setIsActive(true);
				TempMPHBankEntity saveTempMPHBankEntity = tempMPHBankRepository
						.save(convertMPHBankEntityToTempMPHBankEntity);
				logger.info("Temp bank Id: " + saveTempMPHBankEntity.getId());
			}

//			TempMPHBankEntity tempMPHBankEntity = mapper.convertValue(mPHModificationResponseDto.getMphBank(),
//					TempMPHBankEntity.class);
//			
//			tempMPHBankEntity.setMasterMph(saveTempMPHEntity);
//			tempMPHBankEntity.setPmstId(tempMPHBankEntity.getId());
//			tempMPHBankEntity.setCreatedDate(new Date());
//			tempMPHBankEntity.setIsActive(true);
//			TempMPHBankEntity saveTempMPHBankEntity = tempMPHBankRepository.save(tempMPHBankEntity);
//			logger.info("Temp bank Id: " + saveTempMPHBankEntity.getId());

			// PMST_TMP_MPH_ADDRESS
			List<MPHAddressEntity> mphAddress = mPHModificationResponseDto.getMphAddress();

			for (MPHAddressEntity mphAddressEntity : mphAddress) {
				TempMPHAddressEntity convertMPHAddressEntityToTempMPHAddressEntity = mappers
						.convertMPHAddressEntityToTempMPHAddressEntity(mphAddressEntity);

				convertMPHAddressEntityToTempMPHAddressEntity.setMasterMph(saveTempMPHEntity);
				convertMPHAddressEntityToTempMPHAddressEntity.setPmstId(mphAddressEntity.getId());
				convertMPHAddressEntityToTempMPHAddressEntity.setId(null);
				convertMPHAddressEntityToTempMPHAddressEntity.setPincode(mphAddressEntity.getPincode());
				convertMPHAddressEntityToTempMPHAddressEntity.setIsActive(true);
				convertMPHAddressEntityToTempMPHAddressEntity.setCreatedDate(new Date());
				TempMPHAddressEntity saveTempMPHAddressEntity = tempMPHAddressRepository
						.save(convertMPHAddressEntityToTempMPHAddressEntity);
				logger.info("Temp address Id: " + saveTempMPHAddressEntity.getId());
			}
//			TempMPHAddressEntity tempMPHAddressEntity = mapper.convertValue(mPHModificationResponseDto.getMphAddress(),
//					TempMPHAddressEntity.class);
//			// tempMPHAddressEntity.setId(null);
//			tempMPHAddressEntity.setMasterMph(saveTempMPHEntity);
//			tempMPHAddressEntity.setPmstId(mPHModificationResponseDto.getMphAddress().getId());
//			tempMPHAddressEntity.setPincode(mPHModificationResponseDto.getMphAddress().getPincode());
//			tempMPHAddressEntity.setIsActive(true);
//			TempMPHAddressEntity saveTempMPHAddressEntity = tempMPHAddressRepository.save(tempMPHAddressEntity);
//			logger.info("Temp address Id: " + saveTempMPHAddressEntity.getId());

//			TempPolicyServiceNotes

//			TempPolicyServiceNotes tempPolicyServiceNotes = mapper.convertValue(mPHModificationResponseDto.getNotes(),
//					TempPolicyServiceNotes.class);
//			tempPolicyServiceNotes.setPmstTmpPolicyId(updatedTempPolicy.getId());
//			tempPolicyServiceNotes.setPolicyNumber(updatedTempPolicy.getPolicyNumber());
//			tempPolicyServiceNotes.setIsActive(true);
//			tempPolicyServiceNotes.setCreatedDate(new Date());
//			tempPolicyServiceNotesRepository.save(tempPolicyServiceNotes);

			PolicyServiceDto policyServiceDto = new PolicyServiceDto();
			policyServiceDto.setCreatedOn(new Date());
			policyServiceDto.setIsActive(true);
			policyServiceDto.setServiceStatus("Active");
			policyServiceDto.setServiceType(PolicyServiceName.POLICY_DETAILS_CHANGE.getName());
			policyServiceDto.setPolicyId(updatedTempPolicy.getId());
			policyServiceDto.setPolicyNumber(updatedTempPolicy.getPolicyNumber());

			policyServicingCommonServiceImpl.savePolicyService(policyServiceDto);

		} else {

			Optional<RenewalPolicyTMPEntity> findById = renewalPolicyTMPRepository
					.findById(mPHModificationResponseDto.getPolicyDetails().getId());
			RenewalPolicyTMPEntity renewalPolicyTMPEntity = findById.get();

			renewalPolicyTMPEntity
					.setPolicyDispatchDate(mPHModificationResponseDto.getPolicyDetails().getPolicyDispatchDate());
			renewalPolicyTMPEntity
					.setPolicyRecievedDate(mPHModificationResponseDto.getPolicyDetails().getPolicyRecievedDate());
			renewalPolicyTMPEntity.setModifiedDate(new Date());
			renewalPolicyTMPEntity.setModifiedBy(mPHModificationResponseDto.getPolicyDetails().getModifiedBy());
			renewalPolicyTMPRepository.save(renewalPolicyTMPEntity);

			Optional<TempMPHEntity> mphTemObj = tempMPHRepository
					.findById(mPHModificationResponseDto.getMphMaster().getId());
			TempMPHEntity tempMPHEntity = mphTemObj.get();

			tempMPHEntity.setMphCode(mPHModificationResponseDto.getMphMaster().getMphCode());
			tempMPHEntity.setMphName(mPHModificationResponseDto.getMphMaster().getMphName());
			tempMPHEntity.setModifiedDate(new Date());
			tempMPHEntity.setModifiedBy(mPHModificationResponseDto.getMphMaster().getModifiedBy());
			TempMPHEntity save = tempMPHRepository.save(tempMPHEntity);

			// PMST_TMP_MPH_BANK
			List<MPHBankEntity> mphBank = mPHModificationResponseDto.getMphBank();
			for (MPHBankEntity mphBankEntity : mphBank) {
				TempMPHBankEntity tempMPHBankEntity = new TempMPHBankEntity();

				if (mphBankEntity.getId() != null) {
					Optional<TempMPHBankEntity> mphTemBankObj = tempMPHBankRepository.findById(mphBankEntity.getId());
					tempMPHBankEntity = mphTemBankObj.get();

					tempMPHBankEntity.setAccountNumber(mphBankEntity.getAccountNumber());
					tempMPHBankEntity.setAccountType(mphBankEntity.getAccountType());
					tempMPHBankEntity.setBankAddress(mphBankEntity.getBankAddress());
					tempMPHBankEntity.setCityId(mphBankEntity.getCityId());
					tempMPHBankEntity.setTownLocality(mphBankEntity.getTownLocality());
					tempMPHBankEntity.setIfscCode(mphBankEntity.getIfscCode());
					tempMPHBankEntity.setLandlineNumber(mphBankEntity.getLandlineNumber());
					tempMPHBankEntity.setEmailId(mphBankEntity.getEmailId());
					tempMPHBankEntity.setCountryCode(mphBankEntity.getCountryCode());
					tempMPHBankEntity.setStdCode(mphBankEntity.getStdCode());
					tempMPHBankEntity.setModifiedDate(new Date());
					tempMPHBankEntity.setModifiedBy(mphBankEntity.getModifiedBy());
					tempMPHBankEntity.setBankBranch(mphBankEntity.getBankBranch());
					tempMPHBankEntity.setBankName(mphBankEntity.getBankName());
					tempMPHBankEntity.setCountryId(mphBankEntity.getCountryId());

					tempMPHBankRepository.save(tempMPHBankEntity);
				} else {
					tempMPHBankEntity.setId(null);
					tempMPHBankEntity.setAccountNumber(mphBankEntity.getAccountNumber());
					tempMPHBankEntity.setAccountType(mphBankEntity.getAccountType());
					tempMPHBankEntity.setBankAddress(mphBankEntity.getBankAddress());
					tempMPHBankEntity.setCityId(mphBankEntity.getCityId());
					tempMPHBankEntity.setTownLocality(mphBankEntity.getTownLocality());
					tempMPHBankEntity.setIfscCode(mphBankEntity.getIfscCode());
					tempMPHBankEntity.setLandlineNumber(mphBankEntity.getLandlineNumber());
					tempMPHBankEntity.setEmailId(mphBankEntity.getEmailId());
					tempMPHBankEntity.setCountryCode(mphBankEntity.getCountryCode());
					tempMPHBankEntity.setStdCode(mphBankEntity.getStdCode());
					tempMPHBankEntity.setBankBranch(mphBankEntity.getBankBranch());
					tempMPHBankEntity.setBankName(mphBankEntity.getBankName());
					tempMPHBankEntity.setCountryId(mphBankEntity.getCountryId());
					tempMPHBankEntity.setMasterMph(save);
					tempMPHBankEntity.setCreatedDate(new Date());
					tempMPHBankEntity.setCreatedBy(mphBankEntity.getCreatedBy());
					tempMPHBankEntity.setIsActive(true);
					tempMPHBankRepository.save(tempMPHBankEntity);
				}

			}

			// PMST_TMP_MPH_ADDRESS
			List<MPHAddressEntity> mphAddress = mPHModificationResponseDto.getMphAddress();

			for (MPHAddressEntity mphAddressEntity : mphAddress) {
				TempMPHAddressEntity tempMPHAddressEntity = new TempMPHAddressEntity();

				if (mphAddressEntity.getId() != null) {
					Optional<TempMPHAddressEntity> mphTemAddressObj = tempMPHAddressRepository
							.findById(mphAddressEntity.getId());
					tempMPHAddressEntity = mphTemAddressObj.get();

					tempMPHAddressEntity.setAddressLine1(mphAddressEntity.getAddressLine1());
					tempMPHAddressEntity.setAddressLine2(mphAddressEntity.getAddressLine2());
					tempMPHAddressEntity.setAddressLine3(mphAddressEntity.getAddressLine3());
					tempMPHAddressEntity.setAddressType(mphAddressEntity.getAddressType());
					tempMPHAddressEntity.setCityId(mphAddressEntity.getCityId());
					tempMPHAddressEntity.setCityLocality(mphAddressEntity.getCityLocality());
					tempMPHAddressEntity.setPincode(mphAddressEntity.getPincode());
					tempMPHAddressEntity.setDistrict(mphAddressEntity.getDistrict());
					tempMPHAddressEntity.setDistrictId(mphAddressEntity.getDistrictId());
					tempMPHAddressEntity.setModifiedBy(mphAddressEntity.getModifiedBy());
					tempMPHAddressEntity.setStateId(mphAddressEntity.getStateId());
					tempMPHAddressEntity.setStateName(mphAddressEntity.getStateName());
					tempMPHAddressEntity.setCountryId(mphAddressEntity.getCountryId());
					tempMPHAddressEntity.setModifiedDate(new Date());
					tempMPHAddressRepository.save(tempMPHAddressEntity);
				} else {
					tempMPHAddressEntity.setAddressLine1(mphAddressEntity.getAddressLine1());
					tempMPHAddressEntity.setAddressLine2(mphAddressEntity.getAddressLine2());
					tempMPHAddressEntity.setAddressLine3(mphAddressEntity.getAddressLine3());
					tempMPHAddressEntity.setAddressType(mphAddressEntity.getAddressType());
					tempMPHAddressEntity.setCityId(mphAddressEntity.getCityId());
					tempMPHAddressEntity.setCityLocality(mphAddressEntity.getCityLocality());
					tempMPHAddressEntity.setPincode(mphAddressEntity.getPincode());
					tempMPHAddressEntity.setDistrict(mphAddressEntity.getDistrict());
					tempMPHAddressEntity.setDistrictId(mphAddressEntity.getDistrictId());
					tempMPHAddressEntity.setModifiedBy(mphAddressEntity.getModifiedBy());
					tempMPHAddressEntity.setStateId(mphAddressEntity.getStateId());
					tempMPHAddressEntity.setStateName(mphAddressEntity.getStateName());
					tempMPHAddressEntity.setCountryId(mphAddressEntity.getCountryId());
					tempMPHAddressEntity.setMasterMph(save);
					tempMPHAddressEntity.setCreatedDate(new Date());
					tempMPHAddressEntity.setCreatedBy(mphAddressEntity.getCreatedBy());
					tempMPHAddressEntity.setIsActive(true);
					tempMPHAddressRepository.save(tempMPHAddressEntity);
				}

			}

//			TempPolicyServiceNotes

//			Optional<TempPolicyServiceNotes> findNotes = tempPolicyServiceNotesRepository
//					.findById(mPHModificationResponseDto.getNotes().getId());
//			TempPolicyServiceNotes tempPolicyServiceNotes = findNotes.get();
//			tempPolicyServiceNotes.setNotes(mPHModificationResponseDto.getNotes().getNotes());
//			tempPolicyServiceNotes.setModifiedDate(new Date());
//			tempPolicyServiceNotes.setModifiedBy(mPHModificationResponseDto.getNotes().getModifiedBy());
//			tempPolicyServiceNotesRepository.save(tempPolicyServiceNotes);

		}
		ApiValidationResponse response = new ApiValidationResponse();
		response.setMessage("Save Successfully");
		response.setStatus("Save");
		return response;

	}

	@Override
	@Transactional
	public StatusDto updateTempMphDetails(String policyNumber, String status) {
		switch (status) {
		case "22": // PENDING_FOR_APPROVAL
			RenewalPolicyTMPEntity policyTempObj = renewalPolicyTMPRepository.getByPolicyNubmberForSave(policyNumber,
					StatusMaster.DRAFT, StatusMaster.SEND_TO_MAKER);
			policyTempObj.setPolicyStatusId(StatusMaster.PENDING_FOR_APPROVAL);
			renewalPolicyTMPRepository.save(policyTempObj);
			return new StatusDto("PENDING FOR APPROVAL");

		case "33": // APPROVED
			// set policy status in temp as approved (33)

			RenewalPolicyTMPEntity policyTempObjForApproved = renewalPolicyTMPRepository
					.getByPolicyNubmber(policyNumber, StatusMaster.PENDING_FOR_APPROVAL);
			policyTempObjForApproved.setPolicyStatusId(StatusMaster.APPROVED);
			renewalPolicyTMPRepository.save(policyTempObjForApproved);

			// update data in master

			TempMPHModificationResponseDto mphByPolicyNumber = tempMPHRepository
					.getByPolicyId(policyTempObjForApproved.getId());

			updateMaster(mphByPolicyNumber);

			return new StatusDto("APPROVED");
		case "44": // REJECTED
			RenewalPolicyTMPEntity policyTempObject = renewalPolicyTMPRepository.getByPolicyNubmber(policyNumber,
					StatusMaster.PENDING_FOR_APPROVAL);
			policyTempObject.setPolicyStatusId(StatusMaster.REJECTED);
			renewalPolicyTMPRepository.save(policyTempObject);
			return new StatusDto("REJECTED");

		case "55": // send to maker

			RenewalPolicyTMPEntity policyTempObjectfor = renewalPolicyTMPRepository.getByPolicyNubmber(policyNumber,
					StatusMaster.PENDING_FOR_APPROVAL);
			policyTempObjectfor.setPolicyStatusId(StatusMaster.SEND_TO_MAKER);
			renewalPolicyTMPRepository.save(policyTempObjectfor);
			return new StatusDto("SEND TO MAKER");

		default:
			return new StatusDto("Failed to update");
		}
	}

	@Transactional
	private void updateMaster(TempMPHModificationResponseDto tempMPHModificationResponseDto) {

//		update MPH master
		Optional<MasterPolicyEntity> findById = masterPolicyRepository
				.findById(tempMPHModificationResponseDto.getPolicyDetails().getMasterPolicyId());
		MasterPolicyEntity masterPolicyEntity = findById.get();
		masterPolicyEntity
				.setPolicyDispatchDate(tempMPHModificationResponseDto.getPolicyDetails().getPolicyDispatchDate());
		masterPolicyEntity
				.setPolicyRecievedDate(tempMPHModificationResponseDto.getPolicyDetails().getPolicyRecievedDate());
		masterPolicyEntity.setModifiedDate(new Date());
		masterPolicyRepository.save(masterPolicyEntity);

//		update MPH master
		Optional<MPHEntity> getMPHMaster = mphRepository
				.findById(tempMPHModificationResponseDto.getMphMaster().getPmstId());
		MPHEntity mphEntity = getMPHMaster.get();

		mphEntity.setMphCode(tempMPHModificationResponseDto.getMphMaster().getMphCode());
		mphEntity.setMphName(tempMPHModificationResponseDto.getMphMaster().getMphName());
		mphEntity.setModifiedDate(new Date());
		MPHEntity saveMPH = mphRepository.save(mphEntity);

//		update MPH Bank
		List<TempMPHBankEntity> tempMPHBankEntity = tempMPHBankRepository
				.findBymasterMphId(tempMPHModificationResponseDto.getMphMaster().getId());

		for (TempMPHBankEntity tempMPHBankEntity2 : tempMPHBankEntity) {

			if (tempMPHBankEntity2.getPmstId() != null) {

				Optional<MPHBankEntity> getMPHBank = mphBankRepository.findById(tempMPHBankEntity2.getPmstId());
				MPHBankEntity mphBankEntity = getMPHBank.get();

				mphBankEntity.setAccountNumber(tempMPHBankEntity2.getAccountNumber());
				mphBankEntity.setAccountType(tempMPHBankEntity2.getAccountType());
				mphBankEntity.setBankAddress(tempMPHBankEntity2.getBankAddress());
				mphBankEntity.setCityId(tempMPHBankEntity2.getCityId());
				mphBankEntity.setTownLocality(tempMPHBankEntity2.getTownLocality());
				mphBankEntity.setIfscCode(tempMPHBankEntity2.getIfscCode());
				mphBankEntity.setLandlineNumber(tempMPHBankEntity2.getLandlineNumber());
				mphBankEntity.setEmailId(tempMPHBankEntity2.getEmailId());
				mphBankEntity.setCountryCode(tempMPHBankEntity2.getCountryCode());
				mphBankEntity.setStdCode(tempMPHBankEntity2.getStdCode());
				mphBankEntity.setBankBranch(tempMPHBankEntity2.getBankBranch());
				mphBankEntity.setBankName(tempMPHBankEntity2.getBankName());
				mphBankEntity.setCountryId(tempMPHBankEntity2.getCountryId());
				mphBankEntity.setModifiedDate(new Date());
				mphBankRepository.save(mphBankEntity);

			} else {

				MPHBankEntity mphBankEntity = new MPHBankEntity();

				mphBankEntity.setAccountNumber(tempMPHBankEntity2.getAccountNumber());
				mphBankEntity.setAccountType(tempMPHBankEntity2.getAccountType());
				mphBankEntity.setBankAddress(tempMPHBankEntity2.getBankAddress());
				mphBankEntity.setCityId(tempMPHBankEntity2.getCityId());
				mphBankEntity.setTownLocality(tempMPHBankEntity2.getTownLocality());
				mphBankEntity.setIfscCode(tempMPHBankEntity2.getIfscCode());
				mphBankEntity.setLandlineNumber(tempMPHBankEntity2.getLandlineNumber());
				mphBankEntity.setEmailId(tempMPHBankEntity2.getEmailId());
				mphBankEntity.setCountryCode(tempMPHBankEntity2.getCountryCode());
				mphBankEntity.setStdCode(tempMPHBankEntity2.getStdCode());
				mphBankEntity.setCreatedDate(new Date());
				mphBankEntity.setCreatedBy(tempMPHBankEntity2.getCreatedBy());
				mphBankEntity.setBankBranch(tempMPHBankEntity2.getBankBranch());
				mphBankEntity.setBankName(tempMPHBankEntity2.getBankName());
				mphBankEntity.setCountryId(tempMPHBankEntity2.getCountryId());
				mphBankEntity.setIsActive(true);
				mphBankEntity.setMasterMph(saveMPH);
				mphBankRepository.save(mphBankEntity);

			}

		}

////	update MPH Address

		List<TempMPHAddressEntity> tempMPHAddressEntity = tempMPHAddressRepository
				.findBymasterMphId(tempMPHModificationResponseDto.getMphMaster().getId());

		for (TempMPHAddressEntity tempMPHAddressEntity2 : tempMPHAddressEntity) {

			if (tempMPHAddressEntity2.getPmstId() != null) {

				Optional<MPHAddressEntity> getMPHAddress = mphAddressRepository
						.findById(tempMPHAddressEntity2.getPmstId());
				MPHAddressEntity mphAddressEntity = getMPHAddress.get();

				mphAddressEntity.setAddressLine1(tempMPHAddressEntity2.getAddressLine1());
				mphAddressEntity.setAddressLine2(tempMPHAddressEntity2.getAddressLine2());
				mphAddressEntity.setAddressLine3(tempMPHAddressEntity2.getAddressLine3());
				mphAddressEntity.setAddressType(tempMPHAddressEntity2.getAddressType());
				mphAddressEntity.setCityId(tempMPHAddressEntity2.getCityId());
				mphAddressEntity.setCityLocality(tempMPHAddressEntity2.getCityLocality());
				mphAddressEntity.setPincode(tempMPHAddressEntity2.getPincode());
				mphAddressEntity.setDistrict(tempMPHAddressEntity2.getDistrict());
				mphAddressEntity.setDistrictId(tempMPHAddressEntity2.getDistrictId());
				mphAddressEntity.setModifiedBy(tempMPHAddressEntity2.getModifiedBy());
				mphAddressEntity.setStateId(tempMPHAddressEntity2.getStateId());
				mphAddressEntity.setStateName(tempMPHAddressEntity2.getStateName());
				mphAddressEntity.setCountryId(tempMPHAddressEntity2.getCountryId());
				mphAddressEntity.setModifiedDate(new Date());
				mphAddressEntity.setModifiedBy(tempMPHAddressEntity2.getModifiedBy());
				mphAddressRepository.save(mphAddressEntity);

			} else {

				MPHAddressEntity mphAddressEntity = new MPHAddressEntity();
				mphAddressEntity.setAddressLine1(tempMPHAddressEntity2.getAddressLine1());
				mphAddressEntity.setAddressLine2(tempMPHAddressEntity2.getAddressLine2());
				mphAddressEntity.setAddressLine3(tempMPHAddressEntity2.getAddressLine3());
				mphAddressEntity.setAddressType(tempMPHAddressEntity2.getAddressType());
				mphAddressEntity.setCityId(tempMPHAddressEntity2.getCityId());
				mphAddressEntity.setCityLocality(tempMPHAddressEntity2.getCityLocality());
				mphAddressEntity.setPincode(tempMPHAddressEntity2.getPincode());
				mphAddressEntity.setDistrict(tempMPHAddressEntity2.getDistrict());
				mphAddressEntity.setDistrictId(tempMPHAddressEntity2.getDistrictId());
				mphAddressEntity.setModifiedBy(tempMPHAddressEntity2.getModifiedBy());
				mphAddressEntity.setStateId(tempMPHAddressEntity2.getStateId());
				mphAddressEntity.setStateName(tempMPHAddressEntity2.getStateName());
				mphAddressEntity.setCountryId(tempMPHAddressEntity2.getCountryId());
				mphAddressEntity.setCreatedDate(new Date());
				mphAddressEntity.setCreatedBy(tempMPHAddressEntity2.getCreatedBy());
				mphAddressEntity.setMasterMph(saveMPH);
				mphAddressEntity.setIsActive(true);
				mphAddressRepository.save(mphAddressEntity);
			}

		}

	}

	@Override
	public DataTable getPolicyModifcationDetailsDataTable(
			TempPolicyModificationDataTableRequestDto tempPolicyModificationDataTableRequestDto,
			Long pendingForApproval) {
		DataTable dataTable = new DataTable();
		try {
			List<TempPolicyModificationDataTableResponseDto> tempPolicyModificationDataTableResponseDtoList = mPHModificationDao
					.tempPolicyModificationDataTableResponseDtoList(tempPolicyModificationDataTableRequestDto,
							pendingForApproval);

			dataTable.setData(tempPolicyModificationDataTableResponseDtoList);

			if (tempPolicyModificationDataTableResponseDtoList.size() > 0) {
				dataTable.setNoOfPages(tempPolicyModificationDataTableResponseDtoList.get(0).getNoOfPages());
				dataTable.setRecordsTotal(tempPolicyModificationDataTableResponseDtoList.get(0).getTotalRecords());
			}

			return dataTable;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataTable;
	}

//	@Override
//	public MPHModificationResponseDto getMPHForChecker(String policyNumber, Long mphId) {
//		RenewalPolicyTMPEntity policyTempObj = renewalPolicyTMPRepository.getByPolicyNubmber(policyNumber,
//				StatusMaster.PENDING_FOR_APPROVAL);
//		MPHModificationResponseDto mphModificationResponseDto = null;
//
//		if (policyTempObj != null) {
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//
//			TempMPHModificationResponseDto mphByPolicyNumber = tempMPHRepository.getMPHByPolicyNumber(policyNumber,
//					StatusMaster.PENDING_FOR_APPROVAL);
//			MPHModificationResponseDto convertValue = mapper.convertValue(mphByPolicyNumber,
//					MPHModificationResponseDto.class);
//			mphModificationResponseDto = convertValue;
//		}
//
//		logger.info("MPH name :" + mphModificationResponseDto.getMphMaster().getMphName());
//		logger.info("MPHModificationServiceImpl - findById - end");
//
//		return mphModificationResponseDto;
//	}

	@Override
	public Object getPolicyModifcationMakerInprogress(
			TempPolicyModificationDataTableRequestDto tempPolicyModificationDataTableRequestDto) {
		DataTable dataTable = new DataTable();
		try {
			List<TempPolicyModificationDataTableResponseDto> tempPolicyModificationDataTableResponseDtoList = mPHModificationDao
					.getPolicyModifcationMakerInprogress(tempPolicyModificationDataTableRequestDto, StatusMaster.DRAFT,
							StatusMaster.PENDING_FOR_APPROVAL, StatusMaster.SEND_TO_MAKER);

			dataTable.setData(tempPolicyModificationDataTableResponseDtoList);

			if (tempPolicyModificationDataTableResponseDtoList.size() > 0) {
				dataTable.setNoOfPages(tempPolicyModificationDataTableResponseDtoList.get(0).getNoOfPages());
				dataTable.setRecordsTotal(tempPolicyModificationDataTableResponseDtoList.get(0).getTotalRecords());
			}

			return dataTable;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataTable;
	}

	@Override
	public Object getPolicyModifcationExisting(
			TempPolicyModificationDataTableRequestDto tempPolicyModificationDataTableRequestDto) {
		DataTable dataTable = new DataTable();
		try {
			List<TempPolicyModificationDataTableResponseDto> tempPolicyModificationDataTableResponseDtoList = mPHModificationDao
					.getPolicyModifcationExisting(tempPolicyModificationDataTableRequestDto, StatusMaster.APPROVED,
							StatusMaster.REJECTED);

			dataTable.setData(tempPolicyModificationDataTableResponseDtoList);

			if (tempPolicyModificationDataTableResponseDtoList.size() > 0) {
				dataTable.setNoOfPages(tempPolicyModificationDataTableResponseDtoList.get(0).getNoOfPages());
				dataTable.setRecordsTotal(tempPolicyModificationDataTableResponseDtoList.get(0).getTotalRecords());
			}

			return dataTable;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataTable;
	}

	@Override
	public MPHModificationResponse getByPolicyId(Long policyId) {

		MPHModificationResponse mphModificationResponseDto = null;
		try {

			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

			TempMPHModificationResponseDto mphByPolicyNumber = tempMPHRepository.getByPolicyId(policyId);
			MPHModificationResponse convertValue = mapper.convertValue(mphByPolicyNumber,
					MPHModificationResponse.class);
			mphModificationResponseDto = convertValue;

//			Optional<TempPolicyServiceNotes> findByPmstTmpPolicyId = tempPolicyServiceNotesRepository
//					.findByPmstTmpPolicyId(mphByPolicyNumber.getPolicyDetails().getId());
//			mphModificationResponseDto.setNotes(findByPmstTmpPolicyId.get());

			mphModificationResponseDto.setExistingpolicyId(
					masterPolicyRepository.getStatusByPolicy(mphByPolicyNumber.getPolicyDetails().getPolicyNumber()));

			List<MPHBankEntity> bankList = new ArrayList();
			List<TempMPHBankEntity> tempMPHBankEntity = tempMPHBankRepository
					.findBymasterMphId(mphByPolicyNumber.getMphMaster().getId());

			for (TempMPHBankEntity tempMPHBankEntity2 : tempMPHBankEntity) {
				MPHBankEntity bank = mappers.convertTempMPHBankEntityToMPHBankEntity(tempMPHBankEntity2);
				bankList.add(bank);
			}
			mphModificationResponseDto.setMphBank(bankList);
			List<MPHAddressEntity> addressList = new ArrayList<>();
			List<TempMPHAddressEntity> tempMPHAddressEntity = tempMPHAddressRepository
					.findBymasterMphId(mphByPolicyNumber.getMphMaster().getId());
			for (TempMPHAddressEntity tempMPHAddressEntity2 : tempMPHAddressEntity) {
				MPHAddressEntity address = mappers.convertTempMPHAddressEntityToMPHAddressEntity(tempMPHAddressEntity2);
				addressList.add(address);
			}
			mphModificationResponseDto.setMphAddress(addressList);

			try {
				mphModificationResponseDto.setServiceNumber(policyServicingCommonServiceImpl
						.getServiceNumberByPolicyId(mphModificationResponseDto.getPolicyDetails().getId()));

			} catch (Exception e) {

			}
		} catch (Exception e) {
			logger.info(e.getMessage());

		}

		MasterPolicyEntity setProductProps = setProductProps(mphModificationResponseDto.getPolicyDetails());
		mphModificationResponseDto.setPolicyDetails(setProductProps);
		return mphModificationResponseDto;
	}

	@Override
	public Object saveNotes(TempPolicyServiceNotes tempPolicyServiceNotes) {
		tempPolicyServiceNotes.setReferenceServiceType(PolicyServiceName.POLICY_DETAILS_CHANGE.getName());
		notesService.save(tempPolicyServiceNotes);
		ApiValidationResponse response = new ApiValidationResponse();
		response.setMessage("Save Successfully");
		response.setStatus("Save");
		return response;
	}

	@Override
	public Object getListOfNotes(Long tempPolicyId, String policyNumber) {

		return notesService.getListNotes(tempPolicyId, policyNumber, PolicyServiceName.POLICY_DETAILS_CHANGE.getName());
	}

	private MasterPolicyEntity setProductProps(MasterPolicyEntity policyDetails) {

		try {

			if (isDevEnvironment) {
				policyDetails.setProductName("LIC’s New Group Gratuity Cash Accumulation Plan");
			} else {
				CommonMasterProductEntity commonMasterProductEntity = commonMasterProductRepository
						.findById(policyDetails.getProductId()).get();

				policyDetails.setProductName(commonMasterProductEntity.getProductName());

			}
			return policyDetails;
		} catch (Exception e) {
			return policyDetails;
		}
	}

	private StagingPolicyEntity setVariantProps(StagingPolicyEntity stagingPolicyEntity) {
		if (isDevEnvironment) {
			stagingPolicyEntity.setProductVariant("LIC’s New Group Gratuity Cash Accumulation Plan-V3");
		} else {
			CommonMasterVariantEntity commonMasterVariantEntity = commonMasterVariantRepository
					.findById(stagingPolicyEntity.getProductId()).get();
			stagingPolicyEntity.setProductVariant(commonMasterVariantEntity.getVariantName());
		}
		return stagingPolicyEntity;
	}

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private StagingPolicyCustomRepository stagingPolicyCustomRepository;

	@Override
	public ApiResponseDto<List<PolicyDto>> filter(PolicySearchDto policySearchDto) {
		return this.inprogressFilter(policySearchDto);
	}

	public ApiResponseDto<List<PolicyDto>> inprogressFilter(PolicySearchDto policySearchDto) {
		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RenewalPolicyTMPEntity> createQuery = criteriaBuilder.createQuery(RenewalPolicyTMPEntity.class);

		Root<RenewalPolicyTMPEntity> root = createQuery.from(RenewalPolicyTMPEntity.class);

//		if (policySearchDto.getTaggedStatusId() > 0) {
//			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("policyTaggedStatusId")),
//					policySearchDto.getTaggedStatusId()));
//		}

		if (StringUtils.isNotBlank(policySearchDto.getPolicyNumber())&&policySearchDto.getPolicyNumber().length()==4) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("policyNumber")),
					"%" + StringUtils.right(policySearchDto.getPolicyNumber(), 4).toLowerCase()));
		} else {

			if (StringUtils.isNotBlank(policySearchDto.getPolicyNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("policyNumber")),
						policySearchDto.getPolicyNumber().toLowerCase()));
			}
		}

		if (StringUtils.isNotBlank(policySearchDto.getProposalNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("proposalNumber")),
					policySearchDto.getProposalNumber().toLowerCase()));
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("isActive")), 1l));
		}
		if (StringUtils.isNotBlank(policySearchDto.getCustomerName())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("customerName")),
					policySearchDto.getCustomerName().toLowerCase()));
		}
		if (StringUtils.isNotBlank(policySearchDto.getIntermediaryName())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("intermediaryName")),
					policySearchDto.getIntermediaryName().toLowerCase()));
		}
		if (policySearchDto.getLineOfBusinessId() != null && policySearchDto.getLineOfBusinessId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("lineOfBusinessId"), policySearchDto.getLineOfBusinessId()));
		}
		if (policySearchDto.getProductId() != null && policySearchDto.getProductId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("productId"), policySearchDto.getProductId()));
		}
		if (policySearchDto.getProductVariant() != null && policySearchDto.getProductVariant() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("productVariantId"), policySearchDto.getProductVariant()));
		}
		if (policySearchDto.getUnitOffice() != null && policySearchDto.getUnitOffice() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("unitOffice"), policySearchDto.getUnitOffice()));
		}
//		if (policySearchDto.getPolicyStatus() != null && policySearchDto.getPolicyStatus() > 0) {
//			predicates.add(criteriaBuilder.equal(root.get("policyStatus"), policySearchDto.getPolicyStatus()));
//		}
//		if (policySearchDto.getPolicyStatusId() != null && policySearchDto.getPolicyStatusId() > 0) {
//			List<Long> x = new ArrayList<Long>(Arrays.asList(22L,9L));

		if (policySearchDto.getPolicyStatusId() == 0) {
			if (!policySearchDto.getPolicyStatusIdList().isEmpty()) {
				predicates.add(root.get("policyStatusId").in(policySearchDto.getPolicyStatusIdList()));

			}
		} else {
			predicates.add(criteriaBuilder.equal(root.get("policyStatusId"), policySearchDto.getPolicyStatusId()));
		}

//		}
//		if (policySearchDto.getPolicyStatusId() != null && policySearchDto.getPolicyStatusId() > 0) {
//			predicates.add(criteriaBuilder.equal(root.get("policyStatusId"), ));
//		}

		if (policySearchDto.getPolicyStartDate() != null) {
			predicates.add(criteriaBuilder.equal(root.get("policyStartDate"), policySearchDto.getPolicyStartDate()));
		}
		if (policySearchDto.getPolicyStartDate() != null && policySearchDto.getPolicyEndDate() != null) {
			try {
				predicates.add(criteriaBuilder.between(root.get("date"), policySearchDto.getPolicyStartDate(),
						policySearchDto.getPolicyEndDate()));
			} catch (Exception e) {

			}
		}

		if (policySearchDto.getUserType() != null) {
			if (policySearchDto.getUserType().equals("UO")) {
				if (policySearchDto.getUnitCode() != null) {
					predicates.add(criteriaBuilder.equal(root.get("unitCode"), policySearchDto.getUnitCode()));
				}
			}
			if (policySearchDto.getUserType().equals("ZO")) {

				String get = policySearchDto.getUnitCode().substring(0, 2);
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("unitCode")),
						"%" + get.toLowerCase() + "%"));
			}
		}

		createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
		List<RenewalPolicyTMPEntity> entities = entityManager.createQuery(createQuery).getResultList();

		List<PolicyDto> policyDtos = new ArrayList<>();
		for (RenewalPolicyTMPEntity stagingPolicyEntity : entities) {
			policyDtos.add(entityToDto(stagingPolicyCustomRepository.setTransientValues(stagingPolicyEntity)));
		}

		return ApiResponseDto.success(policyDtos);

	}

	public static PolicyDto entityToDto(RenewalPolicyTMPEntity stagingPolicyEntity) {
		return new ModelMapper().map(stagingPolicyEntity, PolicyDto.class);
	}
	}
