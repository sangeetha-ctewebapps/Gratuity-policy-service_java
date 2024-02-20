package com.lic.epgs.gratuity.policy.helper;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.gratuity.common.entity.CommonMasterUnitEntity;
import com.lic.epgs.gratuity.policy.dto.AgeValuationDto;
import com.lic.epgs.gratuity.policy.dto.AgeValuationReportDto;
import com.lic.epgs.gratuity.policy.dto.DocumentStoragedto;
import com.lic.epgs.gratuity.policy.dto.GenerateCBSheetPdfDto;
import com.lic.epgs.gratuity.policy.dto.PmstTmpMergerPropsDto;
import com.lic.epgs.gratuity.policy.dto.PolicyBondDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.PremiumReceiptDto;
import com.lic.epgs.gratuity.policy.entity.DocumentStorageEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicySearchEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyContributionDetailEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyHistoryEntity;
import com.lic.epgs.gratuity.policy.entity.StagingPolicyEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.dto.PolicyContrySummarydto;
import com.lic.epgs.gratuity.policy.premiumadjustment.dto.PolicyDepositdto;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyContributionEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyContrySummaryEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyDepositEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyContributionEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyContrySummaryEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyDepositEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.PolicyTmpSearchDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyTmpSearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policyservices.aom.dto.AOMSearchDto;
import com.lic.epgs.gratuity.policyservices.aom.entity.AOMSearchEntity;
import com.lic.epgs.gratuity.simulation.dto.PolicyDepositDto;

@Component
public class PolicyHelper {

	public static final Logger logger = LogManager.getLogger(PolicyHelper.class);

	private static String urlLocal = "http://localhost:4202";

	public static PolicyDto entityToDto(StagingPolicyEntity stagingPolicyEntity) {
		return new ModelMapper().map(stagingPolicyEntity, PolicyDto.class);
	}

	public static PolicyDepositDto entityToDto(PolicyDepositEntity policyDepositEntity) {
		return new ModelMapper().map(policyDepositEntity, PolicyDepositDto.class);
	}

	public static PolicyDto entityToDto(MasterPolicySearchEntity masterPolicyEntities) {
		return new ModelMapper().map(masterPolicyEntities, PolicyDto.class);
	}


	public static PolicyTmpSearchDto entityToDto(PolicyTmpSearchEntity policyTmpSearchEntity) {
		PolicyTmpSearchDto policyTmpSearchDto = new ModelMapper().map(policyTmpSearchEntity, PolicyTmpSearchDto.class);
		policyTmpSearchDto.setTmpPolicyId(policyTmpSearchEntity.getId());
		return policyTmpSearchDto;
	}

	public static PolicyEntity dtoToEntity(PolicyDto dto) {
		return new ModelMapper().map(dto, PolicyEntity.class);
	}

	public static Long nextPolicyNumber(Long lastPolicyNumber) {
		lastPolicyNumber = lastPolicyNumber == null ? 1 : lastPolicyNumber + 1;
		return lastPolicyNumber;
	}

	public static PolicyDto entityToDto(MasterPolicyEntity masterPolicyEntity) {
		return new ModelMapper().map(masterPolicyEntity, PolicyDto.class);
	}

	public static PolicyContrySummarydto EntityTosummarydto(MasterPolicyContrySummaryEntity entity) {
		return new ModelMapper().map(entity, PolicyContrySummarydto.class);
	}

	public static MasterPolicyEntity stagingPolicyEntityToMasterPolicyEntity(StagingPolicyEntity entity, Long PolicyId,
			String username, Long mphId, String policyNumber) {
		MasterPolicyEntity masterPolicyEntity = new ModelMapper().map(entity, MasterPolicyEntity.class);
		masterPolicyEntity.setId(null);
		masterPolicyEntity.setCreatedBy(username);
		masterPolicyEntity.setPolicyNumber(policyNumber);
		masterPolicyEntity.setCreatedDate(new Date());
		masterPolicyEntity.setModifiedBy(null);
		masterPolicyEntity.setModifiedDate(null);
		masterPolicyEntity.setMasterpolicyHolder(mphId);
		return masterPolicyEntity;

	}

	public static PolicyDto getDetailFromProposalAPI(String proposalNumber, String userName, String endPoint) {

		PolicyDto policyDto = new PolicyDto();
		try {
			URL url = new URL(
					endPoint + "/api/proposal/getProposalDetailsByProposalNumber?proposalNumber=" + proposalNumber);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);
			System.out.println(actualObj);

			JsonNode customerDetail = actualObj.path("responseData").path("customerDetails")
					.path("customerBasicDetails");
			JsonNode mphBasic = actualObj.path("responseData").path("mphDetails").path("mphBasicDetails");
			JsonNode mphProduct = actualObj.path("responseData").path("mphDetails").path("productDetails");
			JsonNode mphVariant = actualObj.path("responseData").path("mphDetails").path("productVariantDetails");
			JsonNode mphChannel = actualObj.path("responseData").path("mphDetails").path("mphChannelDetails");

			policyDto.setCustomerName(customerDetail.path("customerName").textValue());
			policyDto.setLineOfBusiness(mphVariant.path("mkgtLob").textValue());
			policyDto.setProductName(mphProduct.path("productName").textValue());
			policyDto.setProductVariant(mphVariant.path("variantName").textValue());
			policyDto.setUnitCode(mphBasic.path("unitCode").textValue());
			policyDto.setUnitId(mphChannel.path("unitOfficeId").textValue());
			policyDto.setMarketingOfficerCode(mphChannel.path("marketingOfficerCode").textValue());
			policyDto.setMarketingOfficerName(mphChannel.path("marketingOfficerName").textValue());
			policyDto.setIntermediaryCode(mphChannel.path("intermediaryCode").textValue());
			policyDto.setIntermediaryName(mphChannel.path("intermediaryName").textValue());
			policyDto.setIndustryType(customerDetail.path("industryTypeName").textValue());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return policyDto;

	}

	public static PolicyContributionDetailDto contributentitytoDto(PolicyContributionDetailEntity entity) {
		return new ModelMapper().map(entity, PolicyContributionDetailDto.class);
	}

	public static PolicyContributionDetailDto EntityTodo(PolicyContributionDetailEntity entity) {
		return new ModelMapper().map(entity, PolicyContributionDetailDto.class);
	}

	public static PolicyDepositdto EntityTodepositdto(PolicyDepositEntity entity) {
		return new ModelMapper().map(entity, PolicyDepositdto.class);
	}

	public static PolicyContrySummarydto EntityTosummarydto(PolicyContrySummaryEntity entity) {
		return new ModelMapper().map(entity, PolicyContrySummarydto.class);
	}

	public static List<MasterPolicyDepositEntity> entityToPolicyMater(List<PolicyDepositEntity> entities, Long id,
			MasterPolicyContributionDetails masterPolicyContributionDetails, String userName) {
		List<MasterPolicyDepositEntity> addMasterPolicyDepositEntity = new ArrayList<MasterPolicyDepositEntity>();
		for (PolicyDepositEntity policyDepositEntity : entities) {
			MasterPolicyDepositEntity masterPolicyDepositEntity = new ModelMapper().map(policyDepositEntity,
					MasterPolicyDepositEntity.class);
			masterPolicyDepositEntity.setId(null);
			masterPolicyDepositEntity.setMasterPolicyId(id);
			masterPolicyDepositEntity.setActive(true);
			masterPolicyDepositEntity.setContributionDetailId(masterPolicyContributionDetails.getId());
			addMasterPolicyDepositEntity.add(masterPolicyDepositEntity);
			;
		}

		return addMasterPolicyDepositEntity;
	}

	public static List<MasterPolicyContributionEntity> entityToPolicyConMaster(List<PolicyContributionEntity> entities,
			Long id, MasterPolicyContributionDetails masterPolicyContributionDetails, String username) {
		List<MasterPolicyContributionEntity> addMasterPolicyContributionEntity = new ArrayList<MasterPolicyContributionEntity>();
		for (PolicyContributionEntity policyContributionEntity : entities) {
			MasterPolicyContributionEntity masterPolicyContributionEntity = new ModelMapper()
					.map(policyContributionEntity, MasterPolicyContributionEntity.class);
			masterPolicyContributionEntity.setId(null);			
			masterPolicyContributionEntity.setMasterPolicyId(id);
			masterPolicyContributionEntity.setActive(true);
			masterPolicyContributionEntity.setCreatedBy(username);
			masterPolicyContributionEntity.setContributionDetailId(masterPolicyContributionDetails.getId());
			masterPolicyContributionEntity.setContributionDate(new Date());
			addMasterPolicyContributionEntity.add(masterPolicyContributionEntity);
		}

		return addMasterPolicyContributionEntity;
	}
	
	public static List<MasterPolicyContributionEntity> entityToPolicyContriMaster(Long versionNumber, List<PolicyContributionEntity> entities,
			Long id, MasterPolicyContributionDetails masterPolicyContributionDetails, String username, Long tempPolicyId) {
		List<MasterPolicyContributionEntity> addMasterPolicyContributionEntity = new ArrayList<MasterPolicyContributionEntity>();
		for (PolicyContributionEntity policyContributionEntity : entities) {
			MasterPolicyContributionEntity masterPolicyContributionEntity = new ModelMapper()
					.map(policyContributionEntity, MasterPolicyContributionEntity.class);
			masterPolicyContributionEntity.setId(null);
		//	masterPolicyContributionEntity.setPolicyId(tempPolicyId);
			masterPolicyContributionEntity.setMasterPolicyId(id);
			masterPolicyContributionEntity.setActive(true);
			masterPolicyContributionEntity.setCreatedBy(username);
			masterPolicyContributionEntity.setContributionDetailId(masterPolicyContributionDetails.getId());
			masterPolicyContributionEntity.setContributionDate(new Date());
			masterPolicyContributionEntity.setVersionNo(versionNumber);	
			addMasterPolicyContributionEntity.add(masterPolicyContributionEntity);
		}

		return addMasterPolicyContributionEntity;
	}

	public static List<MasterPolicyContrySummaryEntity> entityToPolicySummaryMaster(
			List<PolicyContrySummaryEntity> entities, Long id,
			MasterPolicyContributionDetails masterPolicyContributionDetails, String username) {
		List<MasterPolicyContrySummaryEntity> addMasterPolicyContrySummaryEntity = new ArrayList<MasterPolicyContrySummaryEntity>();
		for (PolicyContrySummaryEntity policyContrySummaryEntity : entities) {
			MasterPolicyContrySummaryEntity masterPolicyContrySummaryEntity = new ModelMapper()
					.map(policyContrySummaryEntity, MasterPolicyContrySummaryEntity.class);
			masterPolicyContrySummaryEntity.setId(null);
			masterPolicyContrySummaryEntity.setContributionDetailId(masterPolicyContributionDetails.getId());
			masterPolicyContrySummaryEntity.setPolicyId(id);
			masterPolicyContrySummaryEntity.setActive(true);
			masterPolicyContrySummaryEntity.setCreatedBy(username);
			addMasterPolicyContrySummaryEntity.add(masterPolicyContrySummaryEntity);
		}

		return addMasterPolicyContrySummaryEntity;

	}

	public static PolicyDepositdto masterEntityTodepositdto(MasterPolicyDepositEntity masterPolicyEntity) {
		return new ModelMapper().map(masterPolicyEntity, PolicyDepositdto.class);
	}

	public static byte[] GetpolicyDocument(PolicyBondDetailDto policyBondDetailDto) {

		byte[] bytes = null;
		System.out.println("before service now helper end Blog" + bytes);
		return bytes;
	}

	public static byte[] decompressImage(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

		byte[] tmp = new byte[4 * 1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(tmp);
				outputStream.write(tmp, 0, count);
			}
			outputStream.close();
		} catch (Exception exception) {
		}
		return outputStream.toByteArray();
	}

	public static DocumentStoragedto entityToDtoDoc(DocumentStorageEntity DocumentStorageEntity) {
		return new ModelMapper().map(DocumentStorageEntity, DocumentStoragedto.class);

	}

	public static AgeValuationDto valuationObjtoDto(List<Object[]> getAgeReport) {

		AgeValuationDto ageValuationDto = new AgeValuationDto();

		List<AgeValuationReportDto> getAgeValuationReportDto = new ArrayList<AgeValuationReportDto>();

		for (Object[] obj : getAgeReport) {

			AgeValuationReportDto newAgeValuationReportDto = new AgeValuationReportDto();

			newAgeValuationReportDto.setCategory((obj[0] == null ? 0 : Integer.parseInt(obj[0].toString())));
			newAgeValuationReportDto.setAge(obj[1] == null ? 0 : Integer.parseInt(obj[1].toString()));
			newAgeValuationReportDto.setCount(obj[2] == null ? 0 : Integer.parseInt(obj[2].toString()));
			newAgeValuationReportDto.setPastService(obj[3] == null ? 0 : Integer.parseInt(obj[3].toString()));
			newAgeValuationReportDto.setSalary(obj[4] == null ? 0 : Double.parseDouble(obj[4].toString()));
			newAgeValuationReportDto.setAccruedGratuity(obj[5] == null ? 0 : Double.parseDouble(obj[5].toString()));
			newAgeValuationReportDto
					.setTotalServiceGratuity(obj[6] == null ? 0 : Double.parseDouble(obj[6].toString()));
			newAgeValuationReportDto.setLifeCover(obj[7] == null ? 0 : Double.parseDouble(obj[7].toString()));
			newAgeValuationReportDto.setLifeCoverPremium(obj[8] == null ? 0 : Double.parseDouble(obj[8].toString()));

			getAgeValuationReportDto.add(newAgeValuationReportDto);
//			}
		}
		ageValuationDto.setAgeValuationReportDto(getAgeValuationReportDto);
		return ageValuationDto;
	}

	// karthi --start

	public static List<GenerateCBSheetPdfDto> getcbObjtoDto(List<Object[]> getcbsheet) {
		List<GenerateCBSheetPdfDto> getAgeValuationReportDto = new ArrayList<GenerateCBSheetPdfDto>();

		for (Object[] obj : getcbsheet) {
			GenerateCBSheetPdfDto generateCBSheetPdfDto = new GenerateCBSheetPdfDto();
			if (obj[0] != null) {
				generateCBSheetPdfDto.setLicId(obj[0].toString());
			} else {
				generateCBSheetPdfDto.setLicId("");
			}

			generateCBSheetPdfDto.setName(obj[1].toString());
			generateCBSheetPdfDto.setEmployeeNo(obj[2].toString());
			generateCBSheetPdfDto.setCustomerCode(obj[3].toString());
			generateCBSheetPdfDto.setCustomerName(obj[4].toString());

			if (obj[5] != null) {
				generateCBSheetPdfDto.setPolicyNumber(obj[5].toString());
			} else {
				generateCBSheetPdfDto.setPolicyNumber("");
			}
//				generateCBSheetPdfDto.setPolicyNumber(obj[5].toString());
			generateCBSheetPdfDto.setDob(obj[6].toString());
			generateCBSheetPdfDto.setDoa(obj[7].toString());

			generateCBSheetPdfDto.setSalary(obj[9] == null ? 0 : Double.parseDouble(obj[9].toString()));
			generateCBSheetPdfDto.setPastService(obj[10] == null ? 0 : Long.parseLong(obj[10].toString()));
			generateCBSheetPdfDto.setTotalService(obj[11] == null ? 0 : Long.parseLong(obj[11].toString()));
			generateCBSheetPdfDto.setTotalServiceGratuity(obj[12] == null ? 0 : Double.parseDouble(obj[12].toString()));
			generateCBSheetPdfDto.setPastServiceGratuity(obj[13] == null ? 0 : Double.parseDouble(obj[13].toString()));
			generateCBSheetPdfDto.setLifeCover(obj[14] == null ? 0 : Double.parseDouble(obj[14].toString()));
			generateCBSheetPdfDto.setAnnualRenewlDate(obj[15].toString());
			generateCBSheetPdfDto.setAge(obj[16] == null ? 0 : Long.parseLong(obj[16].toString()));
			generateCBSheetPdfDto.setLifeCoverPremium(obj[17] == null ? 0 : Double.parseDouble(obj[17].toString()));
			generateCBSheetPdfDto.setCategory(obj[18].toString());
			generateCBSheetPdfDto.setPastServiceBenefit(obj[19] == null ? 0 : Double.parseDouble(obj[19].toString()));
			generateCBSheetPdfDto
					.setCurrentServiceBenefit(obj[20] == null ? 0 : Double.parseDouble(obj[20].toString()));
			getAgeValuationReportDto.add(generateCBSheetPdfDto);

//			}
		}
		return getAgeValuationReportDto;
	}

	// karthi -- end

	public static PolicyHistoryEntity policymasterToHisTransfer(MasterPolicyEntity masterPolicyEntity,
			RenewalPolicyTMPEntity renewalPolicyTMPEntity, String username) {
		PolicyHistoryEntity policyHistoryEntity = new ModelMapper().map(masterPolicyEntity, PolicyHistoryEntity.class);
		policyHistoryEntity.setId(null);
		if (policyHistoryEntity.getModifiedDate() == null) {
			policyHistoryEntity.setMasterPolicyId(masterPolicyEntity.getId());
			policyHistoryEntity.setTmpPolicyId(renewalPolicyTMPEntity.getId());
			// policyHistoryEntity.setEffectiveFromDate(masterPolicyEntity.getCreatedDate());
//			policyHistoryEntity.setEffectiveToDate(new Date());

			policyHistoryEntity.setCreatedBy(username);
			policyHistoryEntity.setCreatedDate(new Date());
			policyHistoryEntity.setModifiedBy(null);
			policyHistoryEntity.setModifiedDate(new Date());
		} else {
			Timestamp effictiveDate = new Timestamp(policyHistoryEntity.getModifiedDate().getTime() + (60 * 1000L));
			policyHistoryEntity.setMasterPolicyId(masterPolicyEntity.getId());
//			policyHistoryEntity.setEffectiveFromDate(effictiveDate);
//			policyHistoryEntity.setEffectiveToDate(new Date());
			policyHistoryEntity.setCreatedBy(username);
			policyHistoryEntity.setCreatedDate(new Date());
			policyHistoryEntity.setModifiedBy(null);
			policyHistoryEntity.setModifiedDate(new Date());
		}
		return policyHistoryEntity;
	}

	public static MasterPolicyEntity updateTemptoPolicyMaster(RenewalPolicyTMPEntity renewalPolicyTMPEntity,
			String username) {
		MasterPolicyEntity masterPolicyEntity = new ModelMapper().map(renewalPolicyTMPEntity, MasterPolicyEntity.class);
		masterPolicyEntity.setId(renewalPolicyTMPEntity.getMasterPolicyId());
		masterPolicyEntity.setModifiedBy(username);
		masterPolicyEntity.setModifiedDate(new Date());
		masterPolicyEntity.setIsActive(true);
		return masterPolicyEntity;
	}

	
	public static MasterPolicyEntity createTemptoPolicyMaster(RenewalPolicyTMPEntity renewalPolicyTMPEntity,
			String username) {
		MasterPolicyEntity masterPolicyEntity = new ModelMapper().map(renewalPolicyTMPEntity, MasterPolicyEntity.class);
		masterPolicyEntity.setId(null);
		masterPolicyEntity.setModifiedBy(username);
		masterPolicyEntity.setModifiedDate(new Date());
		masterPolicyEntity.setIsActive(true);
		return masterPolicyEntity;
	}
	public static PolicyBondDetailDto getPolicyBondDtoDetail(String[] get, JsonNode mphBasic, JsonNode mphAdds,
			JsonNode mphcont, CommonMasterUnitEntity pdfClaimForwardLetter, JsonNode mphRep, String maxRetirementAge,
			Long getLCSumAssure) {
		PolicyBondDetailDto policyBondDetailDto = new PolicyBondDetailDto();

		policyBondDetailDto.setPolicyNumber(get[0]);
		Date date = null;
		Date datevaluation = null;
		Double totalSumAssured = 0.0;
		policyBondDetailDto.setAnnualRenewalDate(get[1]);
		policyBondDetailDto.setTotalMember(Long.parseLong(get[2]));
		policyBondDetailDto.setProposalNumber(get[3]);
		policyBondDetailDto.setValuationEffectiveDate(get[4]);
		policyBondDetailDto.setPolicyIssuanceDate(get[5]);
		policyBondDetailDto.setUnitCode(mphBasic.path("unitCode").textValue());
		policyBondDetailDto.setProposalDate(mphBasic.path("proposalDate").textValue());
		policyBondDetailDto.setIntermediaryCode(mphRep.path("intermediaryCode").asLong());
		policyBondDetailDto.setIntermediaryContactNo(mphRep.path("intermediaryName").asLong());
		policyBondDetailDto.setIntermediaryName(mphRep.path("intermediaryName").textValue());
		policyBondDetailDto.setAdjustmentAmount(get[6]);
		policyBondDetailDto.setTotalGratuity(get[7]);
		policyBondDetailDto.setTotalPremium(get[8]);
		policyBondDetailDto.setTotalSumAssuredAmount(get[9]);
		policyBondDetailDto.setUnitCode(get[10]);
		policyBondDetailDto.setTotalSumAssured(getLCSumAssure);
		policyBondDetailDto.setProductCode(mphcont.path("productCode").textValue());
		policyBondDetailDto.setRetirementAge(maxRetirementAge);
		policyBondDetailDto.setPaymentReceived(null);
		policyBondDetailDto.setMphName(mphBasic.path("mphName").textValue());
		policyBondDetailDto.setMphEmail(null);
		policyBondDetailDto.setUnitName(pdfClaimForwardLetter.getDescription());
		policyBondDetailDto.setAddress1(pdfClaimForwardLetter.getAddress1());
		policyBondDetailDto.setAddress2(pdfClaimForwardLetter.getAddress2());
		policyBondDetailDto.setAddress3(pdfClaimForwardLetter.getAddress3());
		policyBondDetailDto.setMphAddress1(mphAdds.get(0).path("address1").textValue());
		policyBondDetailDto.setMphAddress2(mphAdds.get(0).path("address2").textValue());
		policyBondDetailDto.setMphAddress3(mphAdds.get(0).path("address3").textValue());
		policyBondDetailDto.setMphAddress3(mphAdds.get(0).path("district").textValue());
		policyBondDetailDto.setMphAddress3(mphAdds.get(0).path("state").textValue());

		return policyBondDetailDto;

	}

	public static String getFinancialQuarterIdentifier(Date date) {
		Calendar today = Calendar.getInstance();
		today.setTime(date);

		if (today.get(Calendar.MONTH) <= 2) { // jan-mar
			return "Q4";
		} else if (today.get(Calendar.MONTH) <= 5) { // apr-jun
			return "Q1";
		} else if (today.get(Calendar.MONTH) <= 8) { // jul-sep
			return "Q2";
		} else { // oct-dec
			return "Q3";
		}
	}

	public static PmstTmpMergerPropsDto entityToDtoMerger(MasterPolicyEntity getMasterPolicyEntity) {
		return new ModelMapper().map(getMasterPolicyEntity, PmstTmpMergerPropsDto.class);
	}

	public static List<PremiumReceiptDto> getPRobjecttoDto(List<Object[]> premiumReceiptData) {

		List<PremiumReceiptDto> premiumReceiptDto = new ArrayList<PremiumReceiptDto>();

		for (Object[] obj : premiumReceiptData) {

			PremiumReceiptDto premiumReceiptDtos = new PremiumReceiptDto();
			if (obj[0] != null) {
				premiumReceiptDtos.setPolicyId(obj[0].toString());
			} else {
				premiumReceiptDtos.setPolicyId("");
			}

			if (obj[1] != null) {
				premiumReceiptDtos.setPolicyNumber(obj[1].toString());
			} else {
				premiumReceiptDtos.setPolicyNumber("");
			}

			if (obj[2] != null) {
				premiumReceiptDtos.setUnitCode(obj[2].toString());
			} else {
				premiumReceiptDtos.setUnitCode("");
			}

			if (obj[3] != null) {
				premiumReceiptDtos.setProposalNumber(obj[3].toString());
			} else {
				premiumReceiptDtos.setProposalNumber("");
			}

			premiumReceiptDtos.setCreatedDate((Date) obj[4]);

			if (obj[5] != null) {
				premiumReceiptDtos.setChallanNo(obj[5].toString());
			} else {
				premiumReceiptDtos.setChallanNo("");
			}

			premiumReceiptDtos.setAnnualRenewalDate((Date) obj[6]);

			premiumReceiptDtos.setPremiumAdjustment((Date) obj[7]);

			premiumReceiptDtos.setNextArd((Date) obj[8]);

			if (obj[9] != null) {
				premiumReceiptDtos.setContributionId(obj[9].toString());
			} else {
				premiumReceiptDtos.setContributionId("");
			}

			if (obj[10] != null) {
				premiumReceiptDtos.setPremiumMode(obj[10].toString());
			} else {
				premiumReceiptDtos.setPremiumMode("");
			}

			if (obj[11] != null) {
				premiumReceiptDtos.setMphname(obj[11].toString());
			} else {
				premiumReceiptDtos.setMphname("");
			}

			if (obj[12] != null) {
				premiumReceiptDtos.setAddressLine1(obj[12].toString());
			} else {
				premiumReceiptDtos.setAddressLine1("");
			}

			if (obj[13] != null) {
				premiumReceiptDtos.setAddressLine2(obj[13].toString());
			} else {
				premiumReceiptDtos.setAddressLine2("");
			}

			if (obj[14] != null) {
				premiumReceiptDtos.setAddressLine3(obj[14].toString());
			} else {
				premiumReceiptDtos.setAddressLine3("");
			}

			if (obj[15] != null) {
				premiumReceiptDtos.setCollectionNumber(obj[15].toString());
			} else {
				premiumReceiptDtos.setCollectionNumber("");
			}

			premiumReceiptDtos.setCollectionDate((Date) obj[16]);

			if (obj[17] != null) {
				premiumReceiptDtos.setAvailableAmount(obj[17].toString());
			} else {
				premiumReceiptDtos.setAvailableAmount("");
			}

			if (obj[18] != null) {
				premiumReceiptDtos.setLifePremium(obj[18] == null ? 0 : Long.parseLong(obj[18].toString()));
			} else {
				premiumReceiptDtos.setLifePremium(0l);
			}

			if (obj[19] != null) {
				premiumReceiptDtos.setGst(obj[19] == null ? 0 : Long.parseLong(obj[19].toString()));
			} else {
				premiumReceiptDtos.setGst(0l);
			}

			if (obj[20] != null) {
				premiumReceiptDtos.setProductId(obj[20] == null ? 0 : Long.parseLong(obj[20].toString()));
			} else {
				premiumReceiptDtos.setProductId(0l);
			}

			if (obj[21] != null) {
				premiumReceiptDtos.setCurrentService(obj[21] == null ? 0 : Long.parseLong(obj[21].toString()));
			} else {
				premiumReceiptDtos.setCurrentService(0l);
			}

			if (obj[22] != null) {
				premiumReceiptDtos.setPastService(obj[22] == null ? 0 : Long.parseLong(obj[22].toString()));
			} else {
				premiumReceiptDtos.setPastService(0l);
			}

			premiumReceiptDto.add(premiumReceiptDtos);
		}
		return premiumReceiptDto;
	}

	public static MasterPolicyContributionDetails entityToMaster(PolicyContributionDetailEntity entities,
			Long tempPolicyId, Long masterPolicyId) {
		MasterPolicyContributionDetails masterPolicyDepositEntity = new ModelMapper().map(entities,
				MasterPolicyContributionDetails.class);
		//masterPolicyDepositEntity.setPolicyId(tempPolicyId);
		masterPolicyDepositEntity.setMasterPolicyId(masterPolicyId);

		return masterPolicyDepositEntity;
	}

	public static PremiumReceiptDto getobjmph(JsonNode mphBasic) {

		PremiumReceiptDto premiumReceiptDto = new PremiumReceiptDto();
		premiumReceiptDto.setUin(mphBasic.path("uin").textValue());

		return premiumReceiptDto;
	}

	public static PolicyContributionDetailDto contridetentityTocontridetailDto(
			PolicyContributionDetailEntity policyContributionDetailEntity) {
		return new ModelMapper().map(policyContributionDetailEntity, PolicyContributionDetailDto.class);
	}

	public static MasterPolicyContributionDetails entityToMaster(List<PolicyContributionDetailEntity> findBytmpPolicyId,
			Long masterPolicyId, String userName) {

		MasterPolicyContributionDetails masterPolicyDepositEntity = new ModelMapper().map(findBytmpPolicyId,
				MasterPolicyContributionDetails.class);
		masterPolicyDepositEntity.setCreatedBy(userName);
		masterPolicyDepositEntity.setMasterPolicyId(masterPolicyId);
		masterPolicyDepositEntity.setActive(true);
		return masterPolicyDepositEntity;
	}

	public static MasterPolicyContributionDetails createtmpentityToMaster(
			PolicyContributionDetailEntity findBymasterTmpPolicyId, Long masterPolicyId, String username) {
		MasterPolicyContributionDetails masterPolicyDepositEntity = new ModelMapper().map(findBymasterTmpPolicyId,
				MasterPolicyContributionDetails.class);
		masterPolicyDepositEntity.setCreatedBy(username);
		masterPolicyDepositEntity.setMasterPolicyId(masterPolicyId);
		masterPolicyDepositEntity.setAdjustmentForDate(new Date());
		masterPolicyDepositEntity.setActive(true);
		return masterPolicyDepositEntity;
	}

}
