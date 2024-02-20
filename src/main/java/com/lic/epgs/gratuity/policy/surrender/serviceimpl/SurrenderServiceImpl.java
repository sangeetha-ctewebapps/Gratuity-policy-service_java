package com.lic.epgs.gratuity.policy.surrender.serviceimpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.gratuity.accountingservice.dto.GstDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.HSNCodeDto;
import com.lic.epgs.gratuity.accountingservice.dto.JournalVoucherDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.NewBusinessContributionAndLifeCoverAdjustmentDto;
import com.lic.epgs.gratuity.accountingservice.service.AccountingService;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.common.repository.CommonMasterStateRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.common.service.CommonModuleService;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyContributionDetailEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyEntity;
import com.lic.epgs.gratuity.policy.integration.dto.ResponseDto;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderDocUpdateDto;
import com.lic.epgs.gratuity.policy.surrender.dto.BeneficiaryPaymentDetailModel;
import com.lic.epgs.gratuity.policy.surrender.dto.DebitCreditPolicySurrenderModel;
import com.lic.epgs.gratuity.policy.surrender.dto.PolicySurrenderDto;
import com.lic.epgs.gratuity.policy.surrender.dto.PolicySurrenderInProgressSearchDto;
import com.lic.epgs.gratuity.policy.surrender.dto.SaveSurrenderNotesRequest;
import com.lic.epgs.gratuity.policy.surrender.dto.SaveSurrenderPayoutDtlsReq;
import com.lic.epgs.gratuity.policy.surrender.dto.SaveSurrenderRequest;
import com.lic.epgs.gratuity.policy.surrender.dto.SaveSurrenderResponse;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderAccountingIntegrationRequestModel;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderDocumentDto;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderPaymentRequest;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderPaymentResponse;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderWorkflowRequest;
import com.lic.epgs.gratuity.policy.surrender.dto.UploadSurrenderDocReq;
import com.lic.epgs.gratuity.policy.surrender.entity.PolicySurrender;
import com.lic.epgs.gratuity.policy.surrender.entity.SurrConfigEntity;
import com.lic.epgs.gratuity.policy.surrender.entity.SurrenderDocumentDetailEntity;
import com.lic.epgs.gratuity.policy.surrender.entity.SurrenderExitLoadEntity;
import com.lic.epgs.gratuity.policy.surrender.entity.SurrenderNotes;
import com.lic.epgs.gratuity.policy.surrender.repository.SurrConfigRepository;
import com.lic.epgs.gratuity.policy.surrender.repository.SurrenderDocumentRepository;
import com.lic.epgs.gratuity.policy.surrender.repository.SurrenderExitLoadRepository;
import com.lic.epgs.gratuity.policy.surrender.repository.SurrenderNotesRepository;
import com.lic.epgs.gratuity.policy.surrender.repository.SurrenderRepository;
import com.lic.epgs.gratuity.policy.surrender.service.SurrenderService;
import com.lic.epgs.gratuity.policy.surrender.util.CodeUtil;
import com.lic.epgs.gratuity.policyservices.conversion.dto.TrnRegisModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.DocumentDetailsResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.DocumentRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.OmniDocumentResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.RemoveDocReq;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferDocUpdateDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferDocumentDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferDocumentDetailEntity;

@Service
public class SurrenderServiceImpl implements SurrenderService{

	private static final Logger logger = LogManager.getLogger(SurrenderServiceImpl.class);
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	SurrenderRepository surrenderRepository;
	
	@Autowired
	SurrenderDocumentRepository surrenderDocumentRepository;
	
    @Autowired
    private DocumentUploadSurrenderServiceImpl documentUploadSurrenderServiceImpl;
    
    @Autowired
	SurrenderNotesRepository surrenderNotesRepository;
    
    @Autowired
    MasterPolicyRepository masterPolicyRepository;
    
    @Autowired
    SurrenderExitLoadRepository surrenderExitLoadRepository;
    
    @Autowired
    SurrConfigRepository surrConfigRepository;
	
    @Autowired
    StandardCodeRepository standardCodeRepository;
    
	@Autowired
	private CommonMasterUnitRepository commonMasterUnitRepository;
	
	@Autowired
	private CommonMasterStateRepository commonMasterStateRepository;
	
	@Autowired
	private CommonModuleService commonModuleService;
	
	@Autowired
	private MPHRepository mPHRepository;
	
	@Autowired
	private MasterPolicyContributionDetailRepository masterPolicyContributionDetailRepository; 
	
	@Autowired
	private AccountingService accountingService;
	
	@Autowired
	@Qualifier(value = "restTemplateService")
	private RestTemplate restTemplateService;
	
	@Autowired
	private Environment environment;
    
	@Override
	public ApiResponseDto<List<PolicySurrenderDto>> surrenderPolicySearch(PolicySearchDto policySearchDto) {
		
		List<PolicySurrenderDto> policySurrenderDtos=null;
		PolicySurrenderDto policySurrenderDto=null;
		StringBuilder sqlQuery = new StringBuilder(
				"SELECT pmp.POLICY_ID,pmp.POLICY_NUMBER,pmp.PRODUCT_ID,pmp.PRODUCT_VARIANT_ID,pmp.POLICY_STATUS_ID,pmp.MPH_ID,pmph.MPH_CODE,pmph.MPH_NAME,pmp.POLICY_START_DATE,pmp.POLICY_END_DATE,pmp.DATE_OF_COMMENCEMENT,pmp.ANNUAL_RENEWAL_DATE,pmp.TOTAL_MEMBER,pmph.PAN,pmp.UNIT_CODE,pvd.VARIANT_NAME,pvd.VARIANT_VERSION,sc.NOTICE_PERIOD_IN_MONTHS,sc.WAITING_PERIOD_IN_MONTHS \r\n"
						+ "from PMST_POLICY pmp inner join PMST_MPH pmph on pmp.MPH_ID = pmph.MPH_ID inner join liccustomercommon.product_details pd on pd.LEAD_PRODUCT_ID=pmp.PRODUCT_ID inner join liccustomercommon.product_variant_details pvd on pvd.LEAD_VARIANT_ID=pmp.PRODUCT_VARIANT_ID inner join SURR_CONFIG sc on pvd.VARIANT_VERSION=sc.VARIANT_VERSION \r\n"
						+ "where 1=1");

		if (!ObjectUtils.isEmpty(policySearchDto.getPolicyNumber())) {
			sqlQuery.append(" and pmp.POLICY_NUMBER=:policyNumber");
		}
		if (!ObjectUtils.isEmpty(policySearchDto.getCustomerName())) {
			sqlQuery.append(" and pmp.CUSTOMER_NAME=:customerName");
		}
		if (!ObjectUtils.isEmpty(policySearchDto.getIntermediaryName())) {
			sqlQuery.append(" and pmp.INTERMEDIARY_NAME=:intermediaryName");
		}
		if (!ObjectUtils.isEmpty(policySearchDto.getLineOfBusiness())) {
			sqlQuery.append(" and pmp.LINE_OF_BUSINESS=:lineOfBusiness");
		}
		if (!ObjectUtils.isEmpty(policySearchDto.getUnitCode())) {
			sqlQuery.append(" and pmp.UNIT_CODE=:unitCode");
		}
		if (!ObjectUtils.isEmpty(policySearchDto.getMphPAN())) {
			sqlQuery.append(" and pmph.PAN=:mphPAN");
		}
		Query query = entityManager.createNativeQuery(sqlQuery.toString());
		
		if (!ObjectUtils.isEmpty(policySearchDto.getPolicyNumber())) {
			query.setParameter("policyNumber", policySearchDto.getPolicyNumber());
		}
		if (!ObjectUtils.isEmpty(policySearchDto.getCustomerName())) {
			query.setParameter("customerName", policySearchDto.getCustomerName());
		}
		if (!ObjectUtils.isEmpty(policySearchDto.getIntermediaryName())) {
			query.setParameter("intermediaryName", policySearchDto.getIntermediaryName());
		}
		if (!ObjectUtils.isEmpty(policySearchDto.getLineOfBusiness())) {
			query.setParameter("lineOfBusiness", policySearchDto.getLineOfBusiness());
		}
		if (!ObjectUtils.isEmpty(policySearchDto.getUnitCode())) {
			query.setParameter("unitCode", policySearchDto.getUnitCode());
		}
		if (!ObjectUtils.isEmpty(policySearchDto.getMphPAN())) {
			query.setParameter("mphPAN", policySearchDto.getMphPAN());
		}
		try {
			policySurrenderDtos = new ArrayList<PolicySurrenderDto>();
			@SuppressWarnings("unchecked")
			List<Object[]> obj1 = query.getResultList();
			if (!obj1.isEmpty()) {
				for (Object[] record : obj1) {	
					policySurrenderDto = new PolicySurrenderDto();
					if(record[0]!=null)
					policySurrenderDto.setPolicyId(Long.parseLong(CodeUtil.getStringValue(record[0])));
					policySurrenderDto.setPolicyNumber(CodeUtil.getStringValue(record[1]));
					if(record[2]!=null)
					policySurrenderDto.setProductId(Long.parseLong(CodeUtil.getStringValue(record[2])));
					if(record[3]!=null)
					policySurrenderDto.setVariantId(Long.parseLong(CodeUtil.getStringValue(record[3])));
					if(record[4]!=null)
					policySurrenderDto.setPolicyStatusId(Long.parseLong(CodeUtil.getStringValue(record[4])));
					policySurrenderDto.setPolicyStatus("");
					if(record[5]!=null)
					policySurrenderDto.setMphId(Long.parseLong(CodeUtil.getStringValue(record[5])));
					policySurrenderDto.setMphCode(CodeUtil.getStringValue(record[6]));
					policySurrenderDto.setMphName(CodeUtil.getStringValue(record[7]));
					if(record[8]!=null)
					policySurrenderDto.setPolicyStartDate((Date)record[8]);
					if(record[9]!=null)
					policySurrenderDto.setPolicyEndDate((Date)record[9]);
					if(record[10]!=null)
					policySurrenderDto.setDateOfCommencementOfPolicy((Date)record[10]);
					if(record[11]!=null)
					policySurrenderDto.setAnnualRenewalDate((Date)record[11]);
					if(record[12]!=null)
					policySurrenderDto.setTotalNumberOfLives(Long.parseLong(CodeUtil.getStringValue(record[12])));
					policySurrenderDto.setMphPAN(CodeUtil.getStringValue(record[13]));
					policySurrenderDto.setUnitOffice(CodeUtil.getStringValue(record[14]));
					policySurrenderDto.setProductName(CodeUtil.getStringValue(record[15]));
					policySurrenderDto.setVariantName(CodeUtil.getStringValue(record[16]));
					if(record[17]!=null)
					policySurrenderDto.setNoticePeriodInMonths(Long.parseLong(CodeUtil.getStringValue(record[17])));
					if(record[18]!=null)
					policySurrenderDto.setWaitingPeriodInMonths(Long.parseLong(CodeUtil.getStringValue(record[18])));
							

				policySurrenderDtos.add(policySurrenderDto);
				}
			}
			if(policySurrenderDtos.size() > 0)
			{
				return ApiResponseDto.success(policySurrenderDtos);
			}
			else
			{
				return ApiResponseDto.notFound(policySurrenderDtos);
			}
		} catch (Exception e) {
		}
		return ApiResponseDto.errorMessage(null,"", "Error");
	}

	@Override
	public SaveSurrenderResponse saveSurrenderDetails(SaveSurrenderRequest saveSurrenderRequest) {
		logger.info("surrender saveSurrenderDetails serviceImpl starts ....... ");
		SaveSurrenderResponse saveSurrenderResponse = new SaveSurrenderResponse();
		
		try {
			PolicySurrender policySurrenderEntity = new PolicySurrender();
			Timestamp waitingPeriodStartDateAsTimestamp = null;
			Timestamp waitingPeriodEndDateAsTimestamp = null;
			
			if(saveSurrenderRequest.getSurrenderType().equalsIgnoreCase("Partial"))
			{
				String effDateAsString = saveSurrenderRequest.getPartialSurrenderEffectiveDate();
				Date effDateAsDate = new SimpleDateFormat("dd-MM-yyyy").parse(effDateAsString);  
				Timestamp effDateAsTimestamp = new Timestamp(effDateAsDate.getTime());
				policySurrenderEntity.setPartialSurrenderEffectiveDate(effDateAsTimestamp);
				policySurrenderEntity.setSurrenderPartialAmount(saveSurrenderRequest.getPartialSurrenderAmount());
			}
			else
			{
				String reqDateAsString = saveSurrenderRequest.getSurrenderRequestedDate();
				Date reqDateAsDate = new SimpleDateFormat("dd-MM-yyyy").parse(reqDateAsString);  
				Timestamp reqDateAsTimestamp = new Timestamp(reqDateAsDate.getTime());
				String noticePeriodStartDateAsString = saveSurrenderRequest.getNoticePeriodStartDate();
				Date noticePeriodStartDateAsDate = new SimpleDateFormat("dd-MM-yyyy").parse(noticePeriodStartDateAsString);  
				Timestamp noticePeriodStartDateAsTimestamp = new Timestamp(noticePeriodStartDateAsDate.getTime());
				String noticePeriodEndDateAsString = saveSurrenderRequest.getNoticePeriodEndDate();
				Date noticePeriodEndDateAsDate = new SimpleDateFormat("dd-MM-yyyy").parse(noticePeriodEndDateAsString);  
				Timestamp noticePeriodEndDateAsTimestamp = new Timestamp(noticePeriodEndDateAsDate.getTime());
				
				if(!saveSurrenderRequest.getWaitingPeriodStartDate().isEmpty())
				{
					String waitingPeriodStartDateAsString = saveSurrenderRequest.getWaitingPeriodStartDate();
					Date waitingPeriodStartDateAsDate = new SimpleDateFormat("dd-MM-yyyy").parse(waitingPeriodStartDateAsString);  
					waitingPeriodStartDateAsTimestamp = new Timestamp(waitingPeriodStartDateAsDate.getTime());
				}
				if(!saveSurrenderRequest.getWaitingPeriodEndDate().isEmpty())
				{
					String waitingPeriodEndDateAsString = saveSurrenderRequest.getWaitingPeriodEndDate();
					Date waitingPeriodEndDateAsDate = new SimpleDateFormat("dd-MM-yyyy").parse(waitingPeriodEndDateAsString);  
					waitingPeriodEndDateAsTimestamp = new Timestamp(waitingPeriodEndDateAsDate.getTime());
				}
				
				policySurrenderEntity.setNoticePeriodStartDate(noticePeriodStartDateAsTimestamp);
				policySurrenderEntity.setNoticePeriodEndDate(noticePeriodEndDateAsTimestamp);
				policySurrenderEntity.setWaitingPeriodStartDate(waitingPeriodStartDateAsTimestamp);
				policySurrenderEntity.setWaitingPeriodEndDate(waitingPeriodEndDateAsTimestamp);
				policySurrenderEntity.setSurrenderReason(saveSurrenderRequest.getSurrenderReason());
				policySurrenderEntity.setRequestedBy(saveSurrenderRequest.getSurrenderRequestedBy());
				policySurrenderEntity.setSurrenderRequestedDate(reqDateAsTimestamp);
				policySurrenderEntity.setSurrenderValuePayableTo(saveSurrenderRequest.getSurrenderPayableTo());
				policySurrenderEntity.setWaiveWaitingPeriod(saveSurrenderRequest.getWaiveWaitingPeriod());
			}
			
			policySurrenderEntity.setSurrenderType(saveSurrenderRequest.getSurrenderType());
			policySurrenderEntity.setPolicyId(saveSurrenderRequest.getPolicyId());
			policySurrenderEntity.setModifiedBy(saveSurrenderRequest.getModifiedBy());
			policySurrenderEntity.setModifiedOn(new Date());
			policySurrenderEntity.setUserId(saveSurrenderRequest.getUserId());
			policySurrenderEntity.setUnitCode(saveSurrenderRequest.getUnitCode());
			policySurrenderEntity.setSurrenderStatusId(1);
			policySurrenderEntity.setSurrenderStatus("Draft");
			policySurrenderEntity.setSurrenderNumber(surrenderRepository.getSurrenderNumberSequence());
			PolicySurrender policySurrenderEntity1 = surrenderRepository.save(policySurrenderEntity);
			
			Long surrenderNumber = surrenderRepository.getSurrenderNumber(policySurrenderEntity1.getSurrenderId());
			
			saveSurrenderResponse.setSurrenderId(policySurrenderEntity1.getSurrenderId());
			saveSurrenderResponse.setSurrenderNumber(surrenderNumber);
			saveSurrenderResponse.setSurrenderDate(policySurrenderEntity1.getModifiedOn());
			saveSurrenderResponse.setSurrenderStatus(policySurrenderEntity1.getSurrenderStatus());
			saveSurrenderResponse.setResponseMessage("Surrender Details Saved Successfully.");
			saveSurrenderResponse.setResponseCode(0);
			
//			String status = "Y";
//			surrenderRepository.updateSurrenderApplicableStatus(saveSurrenderRequest.getPolicyNumber(), status);
				
			logger.info("surrender saveSurrenderDetails serviceImpl success ....... ");
			return saveSurrenderResponse;
		} catch (Exception e) {
			System.out.println("Exception e is ..."+e);
			saveSurrenderResponse.setResponseMessage("Surrender Details Failed to Save.");
			saveSurrenderResponse.setResponseCode(1);
			logger.info("surrender saveSurrenderDetails serviceImpl failed ....... ");
		}
		logger.info("surrender saveSurrenderDetails serviceImpl ends ....... ");
		return saveSurrenderResponse;
	}
	
	@Override
	public SaveSurrenderResponse updateSurrenderDetails(SaveSurrenderRequest saveSurrenderRequest) {
		logger.info("surrender updateSurrenderDetails serviceImpl starts ....... ");
		SaveSurrenderResponse saveSurrenderResponse = new SaveSurrenderResponse();
	
		try {
			Long surrenderId = saveSurrenderRequest.getSurrenderId();
			String modifiedBy = saveSurrenderRequest.getModifiedBy();
			Date modifiedOn = new Date();
			Long userId = saveSurrenderRequest.getUserId();
			String unitCode = saveSurrenderRequest.getUnitCode();
			int count = 0;
			if(saveSurrenderRequest.getSurrenderType().equalsIgnoreCase("Partial"))
			{
				String effDateAsString = saveSurrenderRequest.getPartialSurrenderEffectiveDate();
				Date effDateAsDate = new SimpleDateFormat("dd-MM-yyyy").parse(effDateAsString);  
				Timestamp effDateAsTimestamp = new Timestamp(effDateAsDate.getTime());
				Long partialSurrenderAmount = saveSurrenderRequest.getPartialSurrenderAmount();
				count = surrenderRepository.updatePartialSurrenderDetails(surrenderId, partialSurrenderAmount, effDateAsTimestamp, modifiedBy, modifiedOn, unitCode);
			}
			else
			{
				String surrenderReason = saveSurrenderRequest.getSurrenderReason();
				String surrenderRequestedBy = saveSurrenderRequest.getSurrenderRequestedBy();
				String reqDateAsString = saveSurrenderRequest.getSurrenderRequestedDate();
				Date reqDateAsDate = new SimpleDateFormat("dd-MM-yyyy").parse(reqDateAsString);  
				Timestamp reqDateAsTimestamp = new Timestamp(reqDateAsDate.getTime());
				String effDateAsString = saveSurrenderRequest.getSurrenderEffectiveDate();
				Date effDateAsDate = new SimpleDateFormat("dd-MM-yyyy").parse(effDateAsString);  
				Timestamp effDateAsTimestamp = new Timestamp(effDateAsDate.getTime());
				
				count = surrenderRepository.updateSurrenderDetails(surrenderId, surrenderReason, surrenderRequestedBy,
						reqDateAsTimestamp, effDateAsTimestamp, modifiedBy, modifiedOn, userId, unitCode);
			}
			if(count > 0) {
				saveSurrenderResponse.setResponseMessage("Surrender Details Updated Successfully.");
				saveSurrenderResponse.setResponseCode(0);
				logger.info("surrender updateSurrenderDetails serviceImpl success ....... ");
				return saveSurrenderResponse;
			} else {
				saveSurrenderResponse.setResponseMessage("Surrender Details Failed To Update.");
				saveSurrenderResponse.setResponseCode(1);
				logger.info("surrender updateSurrenderDetails serviceImpl failed ....... ");
			}
		} catch (Exception e) {
			System.out.println("Exception e is ..."+e);
			e.printStackTrace();
			saveSurrenderResponse.setResponseMessage("Surrender Details Failed To Update.");
			saveSurrenderResponse.setResponseCode(1);
			logger.info("surrender updateSurrenderDetails serviceImpl failed ....... ");
		}
		
		logger.info("surrender updateSurrenderDetails serviceImpl ends ....... ");
		return saveSurrenderResponse;
	}
	
	 @Override
	    public ResponseEntity<Map<String, Object>> create(SurrenderDocumentDto surrenderDocumentDto) {
		 logger.info("Entering into create method.. ");
	        Map<String, Object> response = new HashMap<String, Object>();
	        try {
	            SurrenderDocumentDetailEntity surrenderDocumentDetailEntity = new SurrenderDocumentDetailEntity();
	            for (DocumentRequest deposit : surrenderDocumentDto.getDocumentRequest()) {
	            	surrenderDocumentDetailEntity.setSurrenderId(surrenderDocumentDto.getSurrenderId());
	            	surrenderDocumentDetailEntity.setPicklistItemId(deposit.getPicklistItemId());
	            	surrenderDocumentDetailEntity.setIsReceived(deposit.getIsReceived());
	            	surrenderDocumentDetailEntity.setIsUploaded(deposit.getIsUploaded());
	            	surrenderDocumentDetailEntity.setRemarks(surrenderDocumentDto.getRemarks());
	            	surrenderDocumentDetailEntity.setDocumentIndex(deposit.getDocumentIndex());
	            	surrenderDocumentDetailEntity.setCreatedBy(surrenderDocumentDto.getCreatedBy());
	                //surrenderDocumentDetailEntity.setModifiedBy(deposit.getModifiedBy());
	            	surrenderDocumentDetailEntity.setFolderIndexNo(deposit.getFolderIndexNo());               
	            	surrenderDocumentDetailEntity.setModifiedOn(new Date());
	            	surrenderDocumentDetailEntity.setCreatedOn(new Date());
	            	surrenderDocumentRepository.saveAndFlush(surrenderDocumentDetailEntity);
	                surrenderDocumentDetailEntity = new SurrenderDocumentDetailEntity();
	            }

	            response.put("surrenderDocumentsDetails", surrenderDocumentDto);
	            response.put("responseMessage", "Surrender Document details saved successfully.");
	            response.put("responseCode", "success");
	            return ResponseEntity.status(HttpStatus.OK).body(response);
	        } catch (Exception e) {
	        	logger.error("Save Surrender Document Deatils failed due to : " + e.getMessage());
	            response.put("responseMessage", "Surrender Document details Failed to Save.");
	            response.put("responseCode", "failed");
	            return ResponseEntity.status(HttpStatus.OK).body(response);
	        }
	    }

	    @Override
	    public ResponseEntity<Map<String, Object>> edit(SurrenderDocUpdateDto documentDocUpdateDto) {
	    	logger.info("Entering into MemberTransferInOutServiceImpl : editDocumentDeatilsTransfer");
	        Map<String, Object> response = new HashMap<String, Object>();

	        try {
	            for (DocumentRequest documentReq : documentDocUpdateDto.getDocumentRequest()) {

	                SurrenderDocumentDetailEntity surrenderDocumentDetailEntity = surrenderDocumentRepository.
	                		getDocBySurrenderIdAndPicklistItemId(documentDocUpdateDto.getSurrenderId(), documentReq.getPicklistItemId());

	                if (documentDocUpdateDto.getSurrenderId() != null) {
	                	surrenderDocumentDetailEntity
	                            .setSurrenderId(surrenderDocumentDetailEntity.getSurrenderId());
	                }

	                if (documentReq.getPicklistItemId() != null) {
	                	surrenderDocumentDetailEntity.setPicklistItemId(documentReq.getPicklistItemId());
	                }
	                if (documentReq.getIsReceived() != null) {
	                	surrenderDocumentDetailEntity.setIsReceived(documentReq.getIsReceived());
	                }
	                if (documentReq.getIsUploaded() != null) {
	                	surrenderDocumentDetailEntity.setIsUploaded(documentReq.getIsUploaded());
	                }
	                if (documentDocUpdateDto.getRemarks() != null) {
	                	surrenderDocumentDetailEntity.setRemarks(documentDocUpdateDto.getRemarks());
	                }
	                if (documentReq.getDocumentIndex() != null) {
	                	surrenderDocumentDetailEntity.setDocumentIndex(documentReq.getDocumentIndex());
	                }
	                if (documentReq.getFolderIndexNo() != null) {
	                	surrenderDocumentDetailEntity.setFolderIndexNo(documentReq.getFolderIndexNo());
	                }
	                if (documentDocUpdateDto.getModifiedBy() != null) {
	                	surrenderDocumentDetailEntity.setModifiedBy(documentDocUpdateDto.getModifiedBy());
	                }
	                surrenderDocumentDetailEntity.setModifiedOn(new Date());

	                surrenderDocumentRepository.save(surrenderDocumentDetailEntity);


	                //	return ResponseEntity.status(HttpStatus.OK).body(response);
	            }
	            //	response.put("TransferDocumentDeatilsEntity ", transferDocumentDetailEntity);
	            response.put("responseMessage", "Document Details Updated successfully.");
	            response.put("responseCode", "success");
	        } catch (Exception e) {
	        	logger.error("Update Document detailsa failed due to : " + e.getMessage());
	            response.put("responseMessage", "Document Details Failed to Update.");
	            response.put("responseCode", "failed");
	            return ResponseEntity.status(HttpStatus.OK).body(response);
	        }
	        return ResponseEntity.status(HttpStatus.OK).body(response);

	    }
	    
	    @Override
	 	public Map<String, Object> uploadDocs(UploadSurrenderDocReq uploadDocReq) throws JsonProcessingException {

	 		ResponseEntity<String> uploadStatus = documentUploadSurrenderServiceImpl.uploadClaimDocs(uploadDocReq);

	 		Map<String, Object> respone = new HashMap<>();

	 		ObjectMapper mapper = new ObjectMapper();
	 		OmniDocumentResponse omniDocsResponseObj = mapper.readValue(uploadStatus.getBody().toString(),
	 				OmniDocumentResponse.class);

	 		if (omniDocsResponseObj.getStatus().equals("true")) {
	            
	 	 SurrenderDocumentDetailEntity surrenderDocumentDetailEntity = new SurrenderDocumentDetailEntity();
	 	surrenderDocumentDetailEntity.setSurrenderId(uploadDocReq.getSurrenderId());
//	 	Optional<PickListItemEntity> pickListItem = pickListItemRepository
//	 			.findById(Long.valueOf(uploadDocReq.getPicklistItemId()));
//	     transferDocumentDetailEntity.setPicklistItemId(pickListItem.get().getName());
	 	surrenderDocumentDetailEntity.setPicklistItemId(uploadDocReq.getPicklistItemId());
	 	surrenderDocumentDetailEntity.setDocumentIndex(omniDocsResponseObj.getDocumentIndexNo());
	     if (omniDocsResponseObj.getDocumentIndexNo() != null) {
	    	 surrenderDocumentDetailEntity.setIsUploaded("Y");
	 	}
	     else {
	    	 surrenderDocumentDetailEntity.setIsUploaded("N");
	     }
	     surrenderDocumentDetailEntity.setIsReceived("Y");
	     surrenderDocumentDetailEntity.setRemarks("Document Uploaded");
	     surrenderDocumentDetailEntity.setFolderIndexNo(omniDocsResponseObj.getFolderIndexNo());
	     surrenderDocumentDetailEntity.setCreatedBy(uploadDocReq.getCreatedBy());
	     surrenderDocumentDetailEntity.setCreatedOn(new Date());
	     SurrenderDocumentDetailEntity savedDoc = surrenderDocumentRepository.save(surrenderDocumentDetailEntity);

	 			respone.put("returnCode", 0);
	 			respone.put("responseMessage", "Document Uploaded Succesfully");
	 			respone.put("surrenderDocumentDetail", surrenderDocumentDetailEntity);
	 			return respone;

	 		} else {
	 			respone.put("returnCode", 1);
	 			respone.put("responseMessage", "Document Upload Failed, Since " + omniDocsResponseObj.getMessage());
	 			return respone;
	 		}

	 	}
	     
	     @Override
	 	public ResponseEntity<Map<String, Object>> removeUploadedDocs(RemoveDocReq removeDocReq)
	 			throws JsonMappingException, JsonProcessingException {
	 		Map<String, Object> response = new HashMap<String, Object>();


	 		ObjectMapper mapper1 = new ObjectMapper();
	 		ResponseEntity<String> removeUploadResponse = documentUploadSurrenderServiceImpl.removeUploadedDoc(
	 				removeDocReq.getFolderIndexNo(),removeDocReq.getDocumentIndex());	
	 		OmniDocumentResponse omniDocsResponseObj = mapper1.readValue(removeUploadResponse.getBody().toString(),OmniDocumentResponse.class);

	 		if (omniDocsResponseObj.getStatus().equals("true")) {

	 			surrenderDocumentRepository.removeDocImageUsingDcoumentIndexNo(Long.parseLong(removeDocReq.getDocumentIndex()));
	 			response.put("responseMessage", "Document deleted Successfully");
	 			response.put("responseCode", "success");
	 			return ResponseEntity.status(HttpStatus.OK).body(response);

	 		}

	 		return ResponseEntity.status(HttpStatus.OK).body(response);

	 	}
	     
	     @Override
	 	public ResponseEntity<String> getUploadDocs(RemoveDocReq removeDocReq) {

	 		ResponseEntity<String> uploadStatus = documentUploadSurrenderServiceImpl.getUploadedDocs(removeDocReq.getDocumentIndex());		return uploadStatus;
	 	}
	     
	     @Override
	 	public Map<String, Object> saveSurrenderNotes(SaveSurrenderNotesRequest saveSurrenderNotesRequest) {
	 		logger.info("surrender saveSurrenderNotes serviceImpl starts ....... ");
	 		Map<String, Object> response = new HashMap<String, Object>();
	 		
	 		try {
	 			SurrenderNotes surrenderNotesEntity = new SurrenderNotes();
	 			
	 			surrenderNotesEntity.setSurrenderId(saveSurrenderNotesRequest.getSurrenderId());
	 			surrenderNotesEntity.setNotesText(saveSurrenderNotesRequest.getSurrenderNote());
	 			surrenderNotesEntity.setUserId(saveSurrenderNotesRequest.getUserId());
	 			surrenderNotesEntity.setModifiedBy(saveSurrenderNotesRequest.getModifiedBy());
	 			surrenderNotesEntity.setModifiedOn(new Date());
	 			surrenderNotesRepository.save(surrenderNotesEntity);
	 			
	 			response.put("responseMessage", "Surrender Notes Saved Successfully.");
	 			response.put("responseCode", 0);
	 				
	 			logger.info("surrender saveSurrenderNotes serviceImpl success ....... ");
	 			return response;
	 		} catch (Exception e) {
	 			System.out.println("Exception e is ..."+e);
	 			response.put("responseMessage", "Surrender Notes Failed to Save.");
	 			response.put("responseCode", 1);
	 			logger.info("surrender saveSurrenderNotes serviceImpl failed ....... ");
	 		}
	 		logger.info("surrender saveSurrenderNotes serviceImpl ends ....... ");
	 		return response;
	 	}

	 	@Override
	 	public List<Map<String,Object>> getSurrenderNotes(Long surrenderId) {
	 		logger.info("surrender getSurrenderNotes serviceImpl starts ....... ");
	 		
	 		try {
	 			return surrenderNotesRepository.getSurrenderNotes(surrenderId);
	 		} catch (Exception e) {
	 			System.out.println("Exception e is ..."+e);
	 			logger.info("surrender getSurrenderNotes serviceImpl failed ....... ");
	 		}
	 		logger.info("surrender getSurrenderNotes serviceImpl ends ....... ");
	 		return surrenderNotesRepository.getSurrenderNotes(surrenderId);
	 	}
	 	
	 	@Override
		public Map<String,Object> surrenderWorkflowAction(SurrenderWorkflowRequest surrenderWorkflowRequest) {
			
			logger.info("surrender surrenderWorkflowAction serviceImpl starts ....... ");
			Map<String,Object> response = new HashMap<>();
			Long surrenderNumber = surrenderWorkflowRequest.getSurrenderNumber();
			String action = surrenderWorkflowRequest.getAction();
			Long userId = surrenderWorkflowRequest.getUserId();
			String modifiedBy = surrenderWorkflowRequest.getModifiedBy();
			String policyNumber = surrenderWorkflowRequest.getPolicyNumber();
			
			try {
				if(action.equalsIgnoreCase("Sent To Checker")) {
					Long surrenderStatusId = surrenderRepository.getSurrenderStatusId(action);
					int count = surrenderRepository.surrenderWorkflowAction(action, surrenderStatusId, modifiedBy, userId, surrenderNumber);
					if (count > 0){
						response.put("surrenderNumber", surrenderNumber);
						response.put("surrenderStatus", action);
						response.put("message", "Surrender Sent To Checker Successfully.");
						response.put("status", 0);
						logger.info("surrender surrenderWorkflowAction serviceImpl success ....... ");
					} else {
						response.put("message", "Surrender Send For Approval Failed.");
						response.put("status", 1);
						logger.info("surrender surrenderWorkflowAction serviceImpl failed ....... ");
					}
				} 
				if(action.equalsIgnoreCase("Cancelled")) {
					Long surrenderStatusId = surrenderRepository.getSurrenderStatusId(action);
					int count = surrenderRepository.surrenderWorkflowAction(action, surrenderStatusId, modifiedBy, userId, surrenderNumber);
					if (count > 0){
//						String surrenderStatus = "N";
//						surrenderRepository.updateSurrenderApplicableStatus(policyNumber, surrenderStatus);
						response.put("surrenderNumber", surrenderNumber);
						response.put("surrenderStatus", action);
						response.put("message", "Surrender Cancelled Successfully.");
						response.put("status", 0);
						logger.info("surrender surrenderWorkflowAction serviceImpl success ....... ");
					} else {
						response.put("message", "Surrender Cancel Failed.");
						response.put("status", 1);
						logger.info("surrender surrenderWorkflowAction serviceImpl failed ....... ");
					}
				} 
				if(action.equalsIgnoreCase("Approved")) {
					Long surrenderStatusId = surrenderRepository.getSurrenderStatusId(action);
					int count = surrenderRepository.surrenderWorkflowAction(action, surrenderStatusId, modifiedBy, userId, surrenderNumber);
					if (count > 0){
						PolicySurrender policySurrender = surrenderRepository.getSurrenderDetailsUsingSurrenderNumber(surrenderNumber);
						Map<String, Object> accountingResponse = callForAccountingAPI(policySurrender.getSurrenderId(), surrenderWorkflowRequest.getModifiedBy(), surrenderWorkflowRequest.getVariantVersion());
						if((int)accountingResponse.get("responseCode") == 1)
						{
							response.put("message", "Surrender Approved Successfully and Accounting API Called Failed.");
						}
						else
						{
							response.put("message", "Surrender Approved Successfully and Accounting API Called Successfully.");
						}
						response.put("surrenderNumber", surrenderNumber);
						response.put("surrenderStatus", action);
						response.put("status", 0);
						logger.info("surrender surrenderWorkflowAction serviceImpl success ....... ");
					} else {
						response.put("message", "Surrender Approve Failed.");
						response.put("status", 1);
						logger.info("surrender surrenderWorkflowAction serviceImpl failed ....... ");
					}
				} 
				if(action.equalsIgnoreCase("Sent Back To Maker")) {
					Long surrenderStatusId = surrenderRepository.getSurrenderStatusId(action);
					int count = surrenderRepository.surrenderWorkflowAction(action, surrenderStatusId, modifiedBy, userId, surrenderNumber);
					if (count > 0){
						response.put("surrenderNumber", surrenderNumber);
						response.put("surrenderStatus", action);
						response.put("message", "Surrender Sent Back To Maker Successfully.");
						response.put("status", 0);
						logger.info("surrender surrenderWorkflowAction serviceImpl success ....... ");
					} else {
						response.put("message", "Surrender Send Back To Maker Failed.");
						response.put("status", 1);
						logger.info("surrender surrenderWorkflowAction serviceImpl failed ....... ");
					}
				}
				if(action.equalsIgnoreCase("Rejected")) {
					Long surrenderStatusId = surrenderRepository.getSurrenderStatusId(action);
					int count = surrenderRepository.surrenderWorkflowAction(action, surrenderStatusId, modifiedBy, userId, surrenderNumber);
					if (count > 0){
//						String surrenderStatus = "N";
//						surrenderRepository.updateSurrenderApplicableStatus(policyNumber, surrenderStatus);
						response.put("surrenderNumber", surrenderNumber);
						response.put("surrenderStatus", action);
						response.put("message", "Surrender Rejected Successfully.");
						response.put("status", 0);
						logger.info("surrender surrenderWorkflowAction serviceImpl success ....... ");
						return response;
					} else {
						response.put("message", "Surrender Reject Failed.");
						response.put("status", 1);
						logger.info("surrender surrenderWorkflowAction serviceImpl failed ....... ");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("surrender surrenderWorkflowAction serviceImpl failed ....... "+e.getMessage());
			}
				
			logger.info("surrender surrenderWorkflowAction serviceImpl ends ....... ");
			return response;
		}

		@Override
		public ApiResponseDto<List<PolicySurrenderDto>> surrenderSearch(PolicySurrenderInProgressSearchDto policySurrenderInProgressSearchDto) {
			logger.info("surrenderSearch method starts ....... ");
			List<PolicySurrenderDto> policySurrenderDtos=null;
			PolicySurrenderDto policySurrenderDto=null;
			StringBuilder sqlQuery = new StringBuilder(
					"SELECT pmp.POLICY_ID,pmp.POLICY_NUMBER,pmp.PRODUCT_ID,pmp.PRODUCT_VARIANT_ID,pmp.POLICY_STATUS_ID,pmp.MPH_ID,pmph.MPH_CODE,pmph.MPH_NAME,pmp.POLICY_START_DATE,pmp.POLICY_END_DATE,pmp.DATE_OF_COMMENCEMENT,pmp.ANNUAL_RENEWAL_DATE,pmp.TOTAL_MEMBER,pmph.PAN,pmp.UNIT_CODE,pvd.VARIANT_NAME,pvd.VARIANT_VERSION,ps.SURRENDER_NUMBER,ps.SURR_ID,pss.STATUS,ps.NOTICE_PERIOD_END_DATE,ps.SURRENDER_TYPE \r\n"
							+ "from PMST_POLICY pmp inner join PMST_MPH pmph on pmp.MPH_ID = pmph.MPH_ID inner join liccustomercommon.product_details pd on pd.LEAD_PRODUCT_ID=pmp.PRODUCT_ID inner join liccustomercommon.product_variant_details pvd on pvd.LEAD_VARIANT_ID=pmp.PRODUCT_VARIANT_ID inner join POLICY_SURRENDER ps on ps.M_POLICY_ID=pmp.POLICY_ID \r\n"
							+"inner join POLICY_SURRENDER_STS pss on ps.SURR_STS_SURR_STS_ID=pss.SURR_STS_ID \r\n"
							+ "where 1=1");

			if (!ObjectUtils.isEmpty(policySurrenderInProgressSearchDto.getPolicyNumber())) {
				sqlQuery.append(" and pmp.POLICY_NUMBER=:policyNumber");
			}
			if (!ObjectUtils.isEmpty(policySurrenderInProgressSearchDto.getSurrenderNumber())) {
				sqlQuery.append(" and ps.SURRENDER_NUMBER=:surrenderNumber");
			}
			if (!ObjectUtils.isEmpty(policySurrenderInProgressSearchDto.getUnitCode())) {
				sqlQuery.append(" and pmp.UNIT_CODE=:unitCode");
			}
			if (!ObjectUtils.isEmpty(policySurrenderInProgressSearchDto.getMphPAN())) {
				sqlQuery.append(" and pmph.PAN=:mphPAN");
			}
			if (!ObjectUtils.isEmpty(policySurrenderInProgressSearchDto.getSurrenderType())) {
				sqlQuery.append(" and ps.SURRENDER_TYPE=:surrenderType");
			}
//			sqlQuery.append(" and TRUNC(sysdate) > TRUNC(ps.NOTICE_PERIOD_END_DATE)");
			if (!ObjectUtils.isEmpty(policySurrenderInProgressSearchDto.getScreenName())) {
				if(policySurrenderInProgressSearchDto.getScreenName().equalsIgnoreCase("inprogress"))
				{
					if(!ObjectUtils.isEmpty(policySurrenderInProgressSearchDto.getRoleType()))
					{
						if(policySurrenderInProgressSearchDto.getRoleType().equalsIgnoreCase("maker"))
						{
							sqlQuery.append(" and pss.STATUS IN ('Draft', 'Sent Back To Maker')");
						}
						else if(policySurrenderInProgressSearchDto.getRoleType().equalsIgnoreCase("checker"))
						{
							sqlQuery.append(" and pss.STATUS IN ('Sent To Checker')");
						}
					}
				}
				else if (policySurrenderInProgressSearchDto.getScreenName().equalsIgnoreCase("Existing")) {
					sqlQuery.append(" and pss.STATUS IN ('Cancelled', 'Approved', 'Rejected')");
				}
				
			}
			if (!ObjectUtils.isEmpty(policySurrenderInProgressSearchDto.getSortBy())) {
				sqlQuery.append(" order BY pmp.POLICY_NUMBER");
			}
			Query query = entityManager.createNativeQuery(sqlQuery.toString());
			
			if (!ObjectUtils.isEmpty(policySurrenderInProgressSearchDto.getPolicyNumber())) {
				query.setParameter("policyNumber", policySurrenderInProgressSearchDto.getPolicyNumber());
			}
			if (!ObjectUtils.isEmpty(policySurrenderInProgressSearchDto.getSurrenderNumber())) {
				query.setParameter("surrenderNumber", policySurrenderInProgressSearchDto.getSurrenderNumber());
			}
			if (!ObjectUtils.isEmpty(policySurrenderInProgressSearchDto.getUnitCode())) {
				query.setParameter("unitCode", policySurrenderInProgressSearchDto.getUnitCode());
			}
			if (!ObjectUtils.isEmpty(policySurrenderInProgressSearchDto.getMphPAN())) {
				query.setParameter("mphPAN", policySurrenderInProgressSearchDto.getMphPAN());
			}
			if (!ObjectUtils.isEmpty(policySurrenderInProgressSearchDto.getSurrenderType())) {
				query.setParameter("surrenderType", policySurrenderInProgressSearchDto.getSurrenderType());
			}
			try {
				policySurrenderDtos = new ArrayList<PolicySurrenderDto>();
				@SuppressWarnings("unchecked")
				List<Object[]> obj1 = query.getResultList();
				if (!obj1.isEmpty()) {
					for (Object[] record : obj1) {	
						policySurrenderDto = new PolicySurrenderDto();
						if(record[0]!=null)
						policySurrenderDto.setPolicyId(Long.parseLong(CodeUtil.getStringValue(record[0])));
						policySurrenderDto.setPolicyNumber(CodeUtil.getStringValue(record[1]));
						if(record[2]!=null)
						policySurrenderDto.setProductId(Long.parseLong(CodeUtil.getStringValue(record[2])));
						if(record[3]!=null)
						policySurrenderDto.setVariantId(Long.parseLong(CodeUtil.getStringValue(record[3])));
						if(record[4]!=null)
						policySurrenderDto.setPolicyStatusId(Long.parseLong(CodeUtil.getStringValue(record[4])));
						policySurrenderDto.setPolicyStatus("");
						if(record[5]!=null)
						policySurrenderDto.setMphId(Long.parseLong(CodeUtil.getStringValue(record[5])));
						policySurrenderDto.setMphCode(CodeUtil.getStringValue(record[6]));
						policySurrenderDto.setMphName(CodeUtil.getStringValue(record[7]));
						if(record[8]!=null)
						policySurrenderDto.setPolicyStartDate((Date)record[8]);
						if(record[9]!=null)
						policySurrenderDto.setPolicyEndDate((Date)record[9]);
						if(record[10]!=null)
						policySurrenderDto.setDateOfCommencementOfPolicy((Date)record[10]);
						if(record[11]!=null)
						policySurrenderDto.setAnnualRenewalDate((Date)record[11]);
						if(record[12]!=null)
						policySurrenderDto.setTotalNumberOfLives(Long.parseLong(CodeUtil.getStringValue(record[12])));
						policySurrenderDto.setMphPAN(CodeUtil.getStringValue(record[13]));
						policySurrenderDto.setUnitOffice(CodeUtil.getStringValue(record[14]));
						policySurrenderDto.setProductName(CodeUtil.getStringValue(record[15]));
						policySurrenderDto.setVariantName(CodeUtil.getStringValue(record[16]));
						if(record[17]!=null)
						policySurrenderDto.setSurrenderNumber(Long.parseLong(CodeUtil.getStringValue(record[17])));
						if(record[18]!=null)
						policySurrenderDto.setSurrenderId(Long.parseLong(CodeUtil.getStringValue(record[18])));
						policySurrenderDto.setSurrenderStatus(CodeUtil.getStringValue(record[19]));
						if(policySurrenderInProgressSearchDto.getScreenName().equalsIgnoreCase("inprogress") && policySurrenderInProgressSearchDto.getRoleType().equalsIgnoreCase("maker"))
						{
							if(record[20]!=null)
							{
								Date noticePeriodEndDate = (Date)record[20];
								if(System.currentTimeMillis() > noticePeriodEndDate.getTime())
								{
									policySurrenderDto.setIsNoticePeriodEnded("Yes");
								}
								else
								{
									policySurrenderDto.setIsNoticePeriodEnded("No");
								}
							}
						}
						policySurrenderDto.setSurrenderType(CodeUtil.getStringValue(record[21]));		

					policySurrenderDtos.add(policySurrenderDto);
					}
				}
				if(policySurrenderDtos.size() > 0)
				{
					return ApiResponseDto.success(policySurrenderDtos);
				}
				else
				{
					return ApiResponseDto.notFound(policySurrenderDtos);
				}
			} catch (Exception e) {
			}
			return ApiResponseDto.errorMessage(null,"", "Error");
		
		}

		@Override
		public ApiResponseDto<PolicySurrender> getSurrenderDetails(Long surrenderId) {
			logger.info("getSurrenderDetails method starts ....... ");
			try
			{
			PolicySurrender policySurrender = surrenderRepository.getSurrenderDetailsUsingId(surrenderId);
			if(policySurrender != null)
			{
				return ApiResponseDto.success(policySurrender);
			}
			else
			{
				return ApiResponseDto.notFound(policySurrender);
			}
			}
			catch(Exception e)
			{
				return ApiResponseDto.errorMessage(null,"", "Error");
			}
		}
		
		@Override
		public ApiResponseDto<List<SurrenderDocumentDetailEntity>> getSurrenderDocumentDetails(Long surrenderId) {
			logger.info("getSurrenderDocumentDetails method starts ....... ");
			try
			{
			List<SurrenderDocumentDetailEntity> surrenderDocumentDetailEntities = surrenderDocumentRepository.getDocumentDetailsBySurrenderId(surrenderId);
			if(surrenderDocumentDetailEntities.size() > 0)
			{
				return ApiResponseDto.success(surrenderDocumentDetailEntities);
			}
			else
			{
				return ApiResponseDto.notFound(surrenderDocumentDetailEntities);
			}
			}
			catch(Exception e)
			{
				return ApiResponseDto.errorMessage(null,"", "Error");
			}
		}

		@Override
		public ApiResponseDto<SurrenderPaymentResponse> getSurrenderPaymentDetails(SurrenderPaymentRequest surrenderPaymentRequest) {
			logger.info("getSurrenderPaymentDetails method starts ....... ");
			SurrenderPaymentResponse surrenderPaymentResponse = new SurrenderPaymentResponse();
			try
			{
				PolicySurrender policySurrender = surrenderRepository.getSurrenderDetailsUsingId(surrenderPaymentRequest.getSurrenderId());
				MasterPolicyEntity masterPolicyEntity = masterPolicyRepository.findPolicyDetailJpaRepo(surrenderPaymentRequest.getPolicyNumber());
				SurrConfigEntity surrConfigEntity = surrConfigRepository.getSurrenderConfigDetailsUsingVersion(surrenderPaymentRequest.getVariant());
				StandardCodeEntity standardCodeEntity = standardCodeRepository.getStandardCodeDetailsUsingName("GST");
				BigDecimal surrenderValue = new BigDecimal(0.0);
				BigDecimal gstOnExitLoad = new BigDecimal(0.0);
				BigDecimal gstOnMVACharges = new BigDecimal(0.0);
				BigDecimal gstOnSurrenderCharges = new BigDecimal(0.0);
				BigDecimal surrenderCharges = new BigDecimal(0.0);
				BigDecimal surrenderChargesDefault = new BigDecimal(0.0005);
				Date modifiedOn = policySurrender.getModifiedOn();
				Date dateOfCommencement = masterPolicyEntity.getDateOfCommencement();
				long difference = modifiedOn.getTime() - dateOfCommencement.getTime();
				if(surrConfigEntity != null && surrConfigEntity.getSurrenderCharges() != null)
				{
					surrenderChargesDefault = BigDecimal.valueOf(surrConfigEntity.getSurrenderCharges());
				}
				if(surrenderPaymentRequest.getVariant().equalsIgnoreCase("V1"))
				{
					//exit load
					BigDecimal exitLoad = new BigDecimal(0.0);
					String range = "";
					String policyDuration = "";
					if((difference / (1000l * 60 * 60 * 24 * 365)) >= 5)
					{
						if( (surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(50000000)) == 0) || (surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(50000000)) == 1 && surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(60000000)) == -1))
						{
							range = "5-6 Crores";
							policyDuration = ">= 5 years";
						}
						if( (surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(60000000)) == 0) || (surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(60000000)) == 1 && surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(70000000)) == -1))
						{
							range = "6-7 Crores";
							policyDuration = ">= 5 years";
						}
						if( (surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(70000000)) == 0) || (surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(70000000)) == 1 && surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(80000000)) == -1))
						{
							range = "7-8 Crores";
							policyDuration = ">= 5 years";
						}
						if( (surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(80000000)) == 0) || (surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(80000000)) == 1 && surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(90000000)) == -1))
						{
							range = "8-9 Crores";
							policyDuration = ">= 5 years";
						}
						if( (surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(90000000)) == 0) || (surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(90000000)) == 1 && surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(100000000)) == -1))
						{
							range = "9-10 Crores";
							policyDuration = ">= 5 years";
						}
						if( (surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(100000000)) == 0) || (surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(100000000)) == 1 && surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(200000000)) == -1))
						{
							range = "10-20 Crores";
							policyDuration = ">= 5 years";
						}
						if( (surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(200000000)) == 0) || (surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(200000000)) == 1 && surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(500000000)) == -1))
						{
							range = "20-50 Crores";
							policyDuration = ">= 5 years";
						}
						if( (surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(500000000)) == 0 || surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(500000000)) == 1))
						{
							range = "> 50 Crores";
							policyDuration = ">= 5 years";
						}
						if(surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(50000000)) == -1)
						{
							range = "< 5 crores";
							policyDuration = ">= 5 years";
						}
					}
					else if((difference / (1000l * 60 * 60 * 24 * 365)) < 1)
					{
						if(surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(500000000)) == -1)
						{
							range = "< 5 crores";
							policyDuration = "< 1 year";
						}
					}
					else if((difference / (1000l * 60 * 60 * 24 * 365)) >= 1 && (difference / (1000l * 60 * 60 * 24 * 365)) < 2)
					{
						if(surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(500000000)) == -1)
						{
						
						range = "< 5 crores";
						policyDuration = ">= 1 year and < 2 years";
						}
					}
					else if((difference / (1000l * 60 * 60 * 24 * 365)) >= 2 && (difference / (1000l * 60 * 60 * 24 * 365)) < 3)
					{
						if(surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(500000000)) == -1)
						{
						
						range = "< 5 crores";
						policyDuration = ">= 2 year and < 3 years";
						}
					}
					else if((difference / (1000l * 60 * 60 * 24 * 365)) >= 3 && (difference / (1000l * 60 * 60 * 24 * 365)) < 4)
					{
						if(surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(500000000)) == -1)
						{
						
						range = "< 5 crores";
						policyDuration = ">= 3 year and < 4 years";
						}
					}
					else if((difference / (1000l * 60 * 60 * 24 * 365)) >= 4 && (difference / (1000l * 60 * 60 * 24 * 365)) < 5)
					{
						if(surrenderPaymentRequest.getPolicyFundValue().compareTo(BigDecimal.valueOf(500000000)) == -1)
						{
						
						range = "< 5 crores";
						policyDuration = ">= 4 year and < 5 years";
						}
					}
					SurrenderExitLoadEntity surrenderExitLoadEntity = surrenderExitLoadRepository.getSurrenderExitLoadDetailsUsingRange(range,policyDuration);
					BigDecimal tempExitLoad = surrenderPaymentRequest.getPolicyFundValue().multiply(BigDecimal.valueOf(surrenderExitLoadEntity.getPercentageApplied()).divide(BigDecimal.valueOf(100)));
					exitLoad = surrenderPaymentRequest.getPolicyFundValue().subtract(tempExitLoad);
					
					//gst
					gstOnExitLoad = BigDecimal.valueOf(Double.parseDouble(standardCodeEntity.getValue())).divide(BigDecimal.valueOf(100)).multiply(exitLoad);
					gstOnExitLoad = gstOnExitLoad.setScale(2, BigDecimal.ROUND_HALF_EVEN);
					
					//surrender value
					surrenderValue = surrenderPaymentRequest.getPolicyFundValue().subtract(exitLoad).subtract(gstOnExitLoad);
					 
					surrenderPaymentResponse.setVariant(surrenderPaymentRequest.getVariant());
					surrenderPaymentResponse.setSurrenderId(surrenderPaymentRequest.getSurrenderId());
					surrenderPaymentResponse.setPolicyNumber(surrenderPaymentRequest.getPolicyNumber());
					surrenderPaymentResponse.setExitLoad(exitLoad.setScale(2, BigDecimal.ROUND_DOWN));
					surrenderPaymentResponse.setGstOnExitLoad(gstOnExitLoad);
					surrenderPaymentResponse.setSurrenderCharges(surrenderCharges.setScale(2, BigDecimal.ROUND_DOWN));
					surrenderPaymentResponse.setGstOnSurrenderCharges(new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_DOWN));
					surrenderPaymentResponse.setMvaApplicable("N");
					surrenderPaymentResponse.setMvaCharges(new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_DOWN));
					surrenderPaymentResponse.setGstOnMVACharges(new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_DOWN));
					surrenderPaymentResponse.setSurrenderAmount(surrenderValue.setScale(2, BigDecimal.ROUND_DOWN));
				}
				else 
				{
					//surrender charges
					BigDecimal tempSurrenderCharges = new BigDecimal(0.0);
					if((difference / (1000l * 60 * 60 * 24 * 365)) < 3)
					{
						tempSurrenderCharges = surrenderPaymentRequest.getPolicyFundValue().multiply(surrenderChargesDefault);
					}
					if(tempSurrenderCharges.compareTo(BigDecimal.valueOf(surrConfigEntity.getMaxSurrenderCharges())) == 1)
					{
						surrenderCharges = BigDecimal.valueOf(surrConfigEntity.getMaxSurrenderCharges());
					}
					else
					{
						surrenderCharges = tempSurrenderCharges;
					}
					
					//gst on surrender charges
					gstOnSurrenderCharges = BigDecimal.valueOf(Double.parseDouble(standardCodeEntity.getValue())).divide(BigDecimal.valueOf(100)).multiply(surrenderCharges);
					gstOnSurrenderCharges = gstOnSurrenderCharges.setScale(2, BigDecimal.ROUND_HALF_EVEN);
					
					//MVA Calculation Start
					
					//exit amount
					BigDecimal exitAmount = surrenderPaymentRequest.getPolicyFundValue();
					
					//percentage withdrawn
					int percentageWithdrawn = (exitAmount.divide(surrenderPaymentRequest.getPolicyFundValue(),2, RoundingMode.HALF_EVEN)).multiply(new BigDecimal(100)).toBigInteger().intValueExact();
					
					//is Bulk Exit
					int bulkExitValue = 25;
					String isBulkExit = "N";
					if(percentageWithdrawn > bulkExitValue)
					{
						isBulkExit = "Y";
					}
					
					//MVF 
					double mvf = surrConfigEntity.getMvaCharges();
					
					//MVA
					BigDecimal mvaCharges = new BigDecimal(0.0);
					if(isBulkExit.equalsIgnoreCase("Y"))
					{
						//validation for V3 - Bulk exit yes, policyfundvalue greater than 10 crores
						if(surrenderPaymentRequest.getVariant().equalsIgnoreCase("V3"))						
						{
							if(surrenderPaymentRequest.getPolicyFundValue().compareTo(new BigDecimal(100000000)) == 1)
							{
								surrenderPaymentResponse.setMvaApplicable("Y");
								mvaCharges = (exitAmount.multiply(new BigDecimal(mvf)));
							}
							else
							{
								surrenderPaymentResponse.setMvaApplicable("N");
							}
						}//validation for V2 - only Bulk exit yes, no validation for policyfundvalue
						else
						{
							surrenderPaymentResponse.setMvaApplicable("Y");
							mvaCharges = (exitAmount.multiply(new BigDecimal(mvf)));
						}
						//gst on MVA Charges
						gstOnMVACharges = BigDecimal.valueOf(Double.parseDouble(standardCodeEntity.getValue())).divide(BigDecimal.valueOf(100)).multiply(mvaCharges);
						gstOnMVACharges = gstOnMVACharges.setScale(2, BigDecimal.ROUND_HALF_EVEN);
					}
					else
					{
						surrenderPaymentResponse.setMvaApplicable("N");
					}
					
					//MVA Calculation End
					
					
					//surrender value
					surrenderValue = surrenderPaymentRequest.getPolicyFundValue().subtract(surrenderCharges).subtract(gstOnSurrenderCharges).subtract(mvaCharges).subtract(gstOnMVACharges);		
					surrenderPaymentResponse.setVariant(surrenderPaymentRequest.getVariant());
					surrenderPaymentResponse.setSurrenderId(surrenderPaymentRequest.getSurrenderId());
					surrenderPaymentResponse.setPolicyNumber(surrenderPaymentRequest.getPolicyNumber());
					surrenderPaymentResponse.setExitLoad(new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_DOWN));
					surrenderPaymentResponse.setGstOnExitLoad(new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_DOWN));
					surrenderPaymentResponse.setSurrenderCharges(surrenderCharges.setScale(2, BigDecimal.ROUND_DOWN));
					surrenderPaymentResponse.setGstOnSurrenderCharges(gstOnSurrenderCharges);
					surrenderPaymentResponse.setExitAmount(exitAmount.setScale(2, BigDecimal.ROUND_DOWN));
					surrenderPaymentResponse.setPercentageWithdrawn(percentageWithdrawn);
					surrenderPaymentResponse.setMvf(mvf);
					surrenderPaymentResponse.setIsBulkExit(isBulkExit);
					surrenderPaymentResponse.setMvaCharges(mvaCharges.setScale(2, BigDecimal.ROUND_DOWN));
					surrenderPaymentResponse.setGstOnMVACharges(gstOnMVACharges.setScale(2, BigDecimal.ROUND_DOWN));
					surrenderPaymentResponse.setSurrenderAmount(surrenderValue.setScale(2, BigDecimal.ROUND_DOWN));
					}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return ApiResponseDto.success(surrenderPaymentResponse);
		}
		
		@Override
		public Map<String, Object> saveSurrenderPaymentDetails(
				SaveSurrenderPayoutDtlsReq saveSurrenderPayoutDtlsRequest) {
			logger.info("surrender saveSurrenderPaymentDetails serviceImpl starts ....... ");	
			Map<String,Object> response =  new HashMap<String,Object>();
		 
			try {
				Long surrenderId = saveSurrenderPayoutDtlsRequest.getSurrenderId();
				Long totalMembers = saveSurrenderPayoutDtlsRequest.getTotalMembers();
				BigDecimal totalAccumulatedFund = saveSurrenderPayoutDtlsRequest.getTotalAccumulatedFund();
				BigDecimal totalInterestAmt = saveSurrenderPayoutDtlsRequest.getTotalInterestAmt();
				BigDecimal surrenderCharges = saveSurrenderPayoutDtlsRequest.getSurrenderCharges();
				BigDecimal totalPayableAmt = saveSurrenderPayoutDtlsRequest.getTotalPayableAmt();
				String modeOfPayment = saveSurrenderPayoutDtlsRequest.getModeOfPayment();
				String paymentFrequency = saveSurrenderPayoutDtlsRequest.getPaymentFrequency();
				int noOfInstallments = saveSurrenderPayoutDtlsRequest.getNoOfInstallments();
				BigDecimal exitLoad = saveSurrenderPayoutDtlsRequest.getExitLoad();
				BigDecimal mvaCharges = saveSurrenderPayoutDtlsRequest.getMvaCharges();
				String isMVAApplicable = saveSurrenderPayoutDtlsRequest.getIsMVAApplicable();
				BigDecimal gstOnSurrenderCharges = saveSurrenderPayoutDtlsRequest.getGstOnSurrenderCharges();
				BigDecimal gstOnMVACharges = saveSurrenderPayoutDtlsRequest.getGstOnMVACharges();
				BigDecimal gstOnExitLoad = saveSurrenderPayoutDtlsRequest.getGstOnExitLoad();
				BigDecimal exitAmount = saveSurrenderPayoutDtlsRequest.getExitAmount();
				Long percentageWithdrawn = saveSurrenderPayoutDtlsRequest.getPercentageWithdrawn();
				String isBulkExit = saveSurrenderPayoutDtlsRequest.getIsBulkExit();
				BigDecimal mvf = saveSurrenderPayoutDtlsRequest.getMvf();
				
				int count = surrenderRepository.saveSurrenderPaymentDetails(surrenderId, totalMembers, totalAccumulatedFund, totalInterestAmt,
						surrenderCharges, gstOnExitLoad, totalPayableAmt, modeOfPayment, paymentFrequency, noOfInstallments, exitLoad, mvaCharges, isMVAApplicable, gstOnSurrenderCharges, gstOnMVACharges, exitAmount, percentageWithdrawn, isBulkExit, mvf);
				
				if(count > 0) {
					response.put("responseMessage", "Surrender Payment Details Saved Successfully.");
					response.put("responseCode", 0);
					logger.info("surrender saveSurrenderPaymentDetails serviceImpl success ....... ");
					return response;
				} else {
					response.put("responseMessage", "Surrender Payment Details Failed to Save.");
					response.put("responseCode", 1);
					logger.info("surrender saveSurrenderPaymentDetails serviceImpl failed ....... ");
				}
			} catch (Exception e) {
				System.out.println("Exception e is ..."+e);
				e.printStackTrace();
				response.put("responseMessage", "Surrender Payment Details Failed to Save.");
				response.put("responseCode", 1);
				logger.info("surrender saveSurrenderPaymentDetails serviceImpl failed ....... ");
			}
			
			logger.info("surrender saveSurrenderPaymentDetails serviceImpl ends ....... ");
			return response;
		}

		@Override
		public Map<String, Object> callForAccountingAPI(Long surrenderId, String username, String policyVersion) {
			logger.info("surrender callForAccountingAPI method starts ....... ");	
			Map<String,Object> response =  new HashMap<String,Object>();
			try
			{
				PolicySurrender policySurrender = surrenderRepository.getSurrenderDetailsUsingId(surrenderId);
				MasterPolicyEntity masterPolicyEntity = masterPolicyRepository.findByIdAndIsActiveTrue(policySurrender.getPolicyId());
				
				String prodAndVarientCodeSame=	commonModuleService.getProductCode(masterPolicyEntity.getProductId());
				String unitStateName = commonMasterUnitRepository.getStateNameByUnitCode(masterPolicyEntity.getUnitCode());
				String unitStateType = commonMasterStateRepository.getStateType(unitStateName);
				String unitStateCode = commonMasterStateRepository.getStateCode(unitStateName);
				
				String mPHStateType = null;
				String mPHAddress = "";
				String mphStateCode = "";
				
				MPHEntity getMPHEntity = mPHRepository.findById(masterPolicyEntity.getMasterpolicyHolder()).get();
				for (MPHAddressEntity getMPHAddressEntity : getMPHEntity.getMphAddresses()) {
					if (getMPHAddressEntity.getStateName() != null || getMPHAddressEntity.getAddressLine1() != null) {
						mPHAddress = getMPHAddressEntity.getAddressLine1();
						if (getMPHAddressEntity.getAddressLine2() != null)
							mPHAddress += ", " + getMPHAddressEntity.getAddressLine2();
						if (getMPHAddressEntity.getCityLocality() != null)
							mPHAddress += ", " + getMPHAddressEntity.getCityLocality();
						if (getMPHAddressEntity.getStateName() != null)
							mPHAddress += "," + getMPHAddressEntity.getStateName();
						mPHStateType = commonMasterStateRepository.getStateType(getMPHAddressEntity.getStateName());
						mphStateCode = commonMasterStateRepository.getStateCode(getMPHAddressEntity.getStateName());
						break;
					}
				}
				
				
				MasterPolicyContributionDetails masterPolicyContributionDetails =masterPolicyContributionDetailRepository.findBymasterPolicyId(masterPolicyEntity.getId());


				HSNCodeDto hSNCodeDto = accountingService.getHSNCode();
				Map<String, Double> gstComponents = accountingService.getGstComponents(unitStateType, mPHStateType, hSNCodeDto, masterPolicyContributionDetails.getLifePremium());

				SurrenderAccountingIntegrationRequestModel surrenderAccountingIntegrationRequestModel = new SurrenderAccountingIntegrationRequestModel();
				surrenderAccountingIntegrationRequestModel.setIsGstApplicable(false);
				if(masterPolicyEntity.getGstApplicableId() == 1l)
					surrenderAccountingIntegrationRequestModel.setIsGstApplicable(true);	
				surrenderAccountingIntegrationRequestModel.setMphCode(getMPHEntity.getMphCode());
				surrenderAccountingIntegrationRequestModel.setUnitCode(masterPolicyEntity.getUnitCode());
				surrenderAccountingIntegrationRequestModel.setUserCode(username);
			
				surrenderAccountingIntegrationRequestModel.setGlTransactionModel(accountingService.getGlTransactionModel(masterPolicyEntity.getProductId(),masterPolicyEntity.getProductVariantId(),masterPolicyEntity.getUnitCode(), "GratuityPolicyApproval"));
				
				
				ResponseDto responseDto = commonmasterserviceUnitByCode(masterPolicyEntity.getUnitCode());
				GstDetailModelDto gstDetailModelDto=new GstDetailModelDto();
				gstDetailModelDto.setAmountWithTax(masterPolicyContributionDetails.getLifePremium() 
						+ masterPolicyContributionDetails.getGst() + masterPolicyContributionDetails.getCurrentServices() 
						+ masterPolicyContributionDetails.getPastService().doubleValue());
				gstDetailModelDto.setAmountWithoutTax(masterPolicyContributionDetails.getLifePremium() 
						+ masterPolicyContributionDetails.getCurrentServices() + masterPolicyContributionDetails.getPastService().doubleValue());
				gstDetailModelDto.setCessAmount(0.0); //from Docu
				gstDetailModelDto.setCgstAmount(gstComponents.get("CGST"));
				gstDetailModelDto.setCgstRate(hSNCodeDto.getCgstRate());
				gstDetailModelDto.setCreatedBy(username);
				gstDetailModelDto.setCreatedDate(new Date());
				gstDetailModelDto.setEffectiveEndDate(""); //form docu
				gstDetailModelDto.setEffectiveStartDate(new Date());			
				gstDetailModelDto.setEntryType("RE");
				gstDetailModelDto.setFromGstn(getMPHEntity.getGstIn() ==null?"":  getMPHEntity.getGstIn());
				gstDetailModelDto.setFromPan(getMPHEntity.getPan()==null?"":getMPHEntity.getPan());
				gstDetailModelDto.setFromStateCode(mphStateCode ==null?"":mphStateCode); //from MPH detail null
				
				if(masterPolicyEntity.getGstApplicableId() == 1l)
					 gstDetailModelDto.setGstApplicableType("Taxable");
				else
					 gstDetailModelDto.setGstApplicableType("Non-Taxable");
				
				gstDetailModelDto.setGstInvoiceNo(""); //From Docu
				gstDetailModelDto.setGstRate((hSNCodeDto.getCgstRate() + hSNCodeDto.getSgstRate() + hSNCodeDto.getUtgstRate() + hSNCodeDto.getIgstRate()));
				gstDetailModelDto.setGstRefNo("2301212");//From Docu
				gstDetailModelDto.setGstRefTransactionNo("9012371");//From Docu
				gstDetailModelDto.setGstTransactionType("K");//From Docu
				gstDetailModelDto.setGstType("K");//From Docu
				gstDetailModelDto.setHsnCode(hSNCodeDto.getHsnCode());
				gstDetailModelDto.setIgstAmount(gstComponents.get("IGST"));
				gstDetailModelDto.setIgstRate(hSNCodeDto.getIgstRate());
				gstDetailModelDto.setModifiedBy(0L); //from docu
				gstDetailModelDto.setModifiedDate(new Date()); //from Docu
				gstDetailModelDto.setMphAddress(mPHAddress);
				gstDetailModelDto.setMphName(getMPHEntity.getMphName());
				gstDetailModelDto.setNatureOfTransaction("Gratuity Surrender");
				gstDetailModelDto.setOldInvoiceDate(new Date()); //From Docu
				gstDetailModelDto.setOldInvoiceNo("IN20123QE"); //From Docu
				gstDetailModelDto.setProductCode(prodAndVarientCodeSame);
				gstDetailModelDto.setRemarks("Gratuity Surrender");
				gstDetailModelDto.setSgstAmount(gstComponents.get("SGST"));
				gstDetailModelDto.setSgstRate(hSNCodeDto.getSgstRate());
				gstDetailModelDto.setToGstIn(responseDto.getGstIn()==null?"8347":responseDto.getGstIn());  //From Docu from get Common Module
				gstDetailModelDto.setToPan(responseDto.getPan());
				gstDetailModelDto.setToStateCode(unitStateCode);
				gstDetailModelDto.setTotalGstAmount(masterPolicyContributionDetails.getGst().doubleValue());
				gstDetailModelDto.setTransactionDate(new Date());
				gstDetailModelDto.setTransactionSubType("A"); //From Docu
				gstDetailModelDto.setTransactionType("C"); //From Docu
//				gstDetailModelDto.setUserCode(username);
				gstDetailModelDto.setUtgstAmount(gstComponents.get("UTGST"));
			
				gstDetailModelDto.setUtgstRate(hSNCodeDto.getUtgstRate());
				gstDetailModelDto.setVariantCode(prodAndVarientCodeSame);
				gstDetailModelDto.setYear(DateUtils.uniqueNoYYYY());
				gstDetailModelDto.setMonth(DateUtils.currentMonthName());
				surrenderAccountingIntegrationRequestModel.setGstModel(gstDetailModelDto);
				
				JournalVoucherDetailModelDto journalVoucherDetailModelDto =new JournalVoucherDetailModelDto();
				journalVoucherDetailModelDto.setLineOfBusiness(masterPolicyEntity.getLineOfBusiness());
				journalVoucherDetailModelDto.setProduct(prodAndVarientCodeSame);
				journalVoucherDetailModelDto.setProductVariant(prodAndVarientCodeSame);
				surrenderAccountingIntegrationRequestModel.setJournalVoucherDetailModel(journalVoucherDetailModelDto);
				
				surrenderAccountingIntegrationRequestModel.setRefNo(String.valueOf(new Random().nextInt(10000)));
				surrenderAccountingIntegrationRequestModel.setIsLegacy("No");
				surrenderAccountingIntegrationRequestModel.setProductCode(prodAndVarientCodeSame);
				surrenderAccountingIntegrationRequestModel.setVariantCode(prodAndVarientCodeSame);
				surrenderAccountingIntegrationRequestModel.setPolicyNo(masterPolicyEntity.getPolicyNumber());
				
				//BeneficiaryPaymentDetailModel preparation
				String bankName = "";
				String ifscCode = "";
				String bankBranch = "";
				String accountType = "";
				String accountNo = "";
				for(MPHBankEntity mphBankEntity : getMPHEntity.getMphBank()) {
					bankName = mphBankEntity.getBankName();
					ifscCode = mphBankEntity.getIfscCode();
					bankBranch = mphBankEntity.getBankBranch();
					accountType = mphBankEntity.getAccountType();
					accountNo = mphBankEntity.getAccountNumber();
				}

				BeneficiaryPaymentDetailModel beneficiaryPaymentDetailModel = new BeneficiaryPaymentDetailModel();

				beneficiaryPaymentDetailModel.setPayoutSourceModule("Gratuity Surrender");
				beneficiaryPaymentDetailModel.setPaymentSourceRefNumber(String.valueOf(policySurrender.getSurrenderNumber()));
				beneficiaryPaymentDetailModel.setBeneficiaryName(getMPHEntity.getMphName());
				beneficiaryPaymentDetailModel.setBeneficiaryBankName(bankName);
				beneficiaryPaymentDetailModel.setBeneficiaryBankIfsc(ifscCode);
				beneficiaryPaymentDetailModel.setBeneficiaryBranchName(bankBranch);
				beneficiaryPaymentDetailModel.setBeneficiaryAccountNumber(accountNo);
				beneficiaryPaymentDetailModel.setBeneficiaryAccountType(accountType);
//				beneficiaryPaymentDetailModel.setPaymentAmount(transferMemberPolicyDetailEntity.getLicPremiumIn() + transferMemberPolicyDetailEntity.getGstOnPremiumIn());
				beneficiaryPaymentDetailModel.setEffectiveDateOfPayment("2023-11-08");
				beneficiaryPaymentDetailModel.setPaymentMode("N"); //N-NEFT/ R-RTGS
				beneficiaryPaymentDetailModel.setUnitCode(masterPolicyEntity.getUnitCode());
				beneficiaryPaymentDetailModel.setPolicyNumber(masterPolicyEntity.getPolicyNumber());
//				beneficiaryPaymentDetailModel.setMemberNumber(transferMemberPolicyDetailEntity.getMemberName());
				beneficiaryPaymentDetailModel.setPaymentCategory("PCM002"); // Need to check hardcoded
				beneficiaryPaymentDetailModel.setPaymentSubCategory("O");
				beneficiaryPaymentDetailModel.setRemarks("Gratuity Policy Surrender");
				beneficiaryPaymentDetailModel.setBeneficiaryPaymentId(String.valueOf(policySurrender.getSurrenderNumber()));
				beneficiaryPaymentDetailModel.setProductCode(prodAndVarientCodeSame);
				beneficiaryPaymentDetailModel.setVariantCode(prodAndVarientCodeSame);
				beneficiaryPaymentDetailModel.setOperatinUnitType("UO");
				beneficiaryPaymentDetailModel.setIban("");
				beneficiaryPaymentDetailModel.setVoucherNo("");
				beneficiaryPaymentDetailModel.setNroAccount("");;
				
				surrenderAccountingIntegrationRequestModel.setBeneficiaryPaymentDetailModel(beneficiaryPaymentDetailModel);
				
				//TrnRegisModel preparation
				TrnRegisModel trnRegisModel = new TrnRegisModel();

				trnRegisModel.setReferenceIdType("CRI");
				trnRegisModel.setVan("LICG"+masterPolicyEntity.getPolicyNumber());//Hardcode
				trnRegisModel.setPolicyNo(masterPolicyEntity.getPolicyNumber());
//				Double amount = transferMemberPolicyDetailEntity.getLicPremiumIn() +transferMemberPolicyDetailEntity.getGstOnPremiumIn();
				trnRegisModel.setAmount("1");
				trnRegisModel.setAmountType("9");
				trnRegisModel.setProductCode(prodAndVarientCodeSame);
				trnRegisModel.setVariantCode(prodAndVarientCodeSame);
				trnRegisModel.setValidityUptoDate("2024-09-07");
				trnRegisModel.setStatus(2L);
				trnRegisModel.setBankUniqueId("");//Hardcode
				trnRegisModel.setReason("Policy Surrender");
				trnRegisModel.setChallanNo(new BigInteger("0"));
				trnRegisModel.setCreatedBy("1");
				trnRegisModel.setPoolAccountCode("");
				trnRegisModel.setProposalNo("");
				trnRegisModel.setReferenceDate("2024-02-01");				
				surrenderAccountingIntegrationRequestModel.setTrnRegisModel(trnRegisModel);
				
				StandardCodeEntity standardCodeEntity = standardCodeRepository.getStandardCodeDetailsUsingName("GST");
				DebitCreditPolicySurrenderModel debitCreditPolicySurrenderModel = new DebitCreditPolicySurrenderModel();
				BigDecimal gstOnExitLoad = new BigDecimal(0.0);
				BigDecimal gstOnMVACharges = new BigDecimal(0.0);
				BigDecimal gstOnSurrenderCharges = new BigDecimal(0.0);
				
				if(policyVersion.equalsIgnoreCase("V1"))
				{
					//gst
					gstOnExitLoad = BigDecimal.valueOf(Double.parseDouble(standardCodeEntity.getValue())).divide(BigDecimal.valueOf(100)).multiply(policySurrender.getExitLoad());
					gstOnExitLoad = gstOnExitLoad.setScale(2, BigDecimal.ROUND_HALF_EVEN);
					debitCreditPolicySurrenderModel.setCreditSurrenderOrExitLoad(policySurrender.getExitLoad());
					debitCreditPolicySurrenderModel.setCreditGstAccount(gstOnExitLoad);
				}
				else
				{
					//gst on surrender charges
					gstOnSurrenderCharges = BigDecimal.valueOf(Double.parseDouble(standardCodeEntity.getValue())).divide(BigDecimal.valueOf(100)).multiply(policySurrender.getSurrenderCharges());
					gstOnSurrenderCharges = gstOnSurrenderCharges.setScale(2, BigDecimal.ROUND_HALF_EVEN);
					debitCreditPolicySurrenderModel.setCreditSurrenderOrExitLoad(policySurrender.getSurrenderCharges());
					debitCreditPolicySurrenderModel.setCreditGstAccount(gstOnSurrenderCharges.add(gstOnMVACharges));
				}
				
				  
				  debitCreditPolicySurrenderModel.setCreditMvaCharge(new BigDecimal(0.00));
				  debitCreditPolicySurrenderModel.setCreditOsPaymentsGenralCPC(policySurrender.getTotalPayableAmount());
				  debitCreditPolicySurrenderModel.setCreditCoPaymentContraAc(policySurrender.getTotalPayableAmount());
				  debitCreditPolicySurrenderModel.setDebitCoPaymentContraAc(policySurrender.getTotalPayableAmount());
				  debitCreditPolicySurrenderModel.setDebitWholeSaleSurrenderScheme(policySurrender.getAccumulatedValue());
				  
				  surrenderAccountingIntegrationRequestModel.setDebitCreditPolicySurrenderModel(debitCreditPolicySurrenderModel);;
				  
				  Boolean resp =  accountingService.policySurrender(surrenderAccountingIntegrationRequestModel);
				if(resp)
				{
					response.put("responseMessage", "Surrender Accounting API Called Successfully.");
					response.put("responseCode", 0);
				}
				else
				{
					response.put("responseMessage", "Surrender Accounting API Called Failed.");
					response.put("responseCode", 1);
				}
			}
			catch(Exception e)
			{
				response.put("responseMessage", "Surrender Accounting API Called Failed.");
				response.put("responseCode", 1);
			}
			logger.info("surrender callForAccountingAPI method ends ....... ");	
			return response;
		}
		
		public ResponseDto commonmasterserviceUnitByCode(String unitCode) {
			ResponseDto responseDto=null;
			logger.info("CommonServiceImpl-commonmasterserviceUnitByCode-Start");

			try {
				String url = environment.getProperty("COMMON_MASTER_UNITBY_CODE");

				if (StringUtils.isNotBlank(url)) {

					responseDto = restTemplateService.exchange(url + unitCode, HttpMethod.GET, null, ResponseDto.class)
							.getBody();
					if (responseDto == null) {
						responseDto = new ResponseDto();
					}
				}
			} catch (HttpStatusCodeException e) {

				logger.info("CommonServiceImpl-commonmasterserviceUnitByCode-Error:");
			}
			logger.info("CommonServiceImpl-commonmasterserviceUnitByCode-End");

			return responseDto;
		}	

		@Override
		public ApiResponseDto<List<PolicySurrenderDto>> masterPolicySearchPartialSurrender(PolicySearchDto policySearchDto) {
			
			List<PolicySurrenderDto> policySurrenderDtos=null;
			PolicySurrenderDto policySurrenderDto=null;
			StringBuilder sqlQuery = new StringBuilder(
					"SELECT pmp.POLICY_ID,pmp.POLICY_NUMBER,pmp.PRODUCT_ID,pmp.PRODUCT_VARIANT_ID,pmp.POLICY_STATUS_ID,pmp.MPH_ID,pmph.MPH_CODE,pmph.MPH_NAME,pmp.POLICY_START_DATE,pmp.POLICY_END_DATE,pmp.DATE_OF_COMMENCEMENT,pmp.ANNUAL_RENEWAL_DATE,pmp.TOTAL_MEMBER,pmph.PAN,pmp.UNIT_CODE,pvd.VARIANT_NAME,pvd.VARIANT_VERSION,sc.NOTICE_PERIOD_IN_MONTHS,sc.WAITING_PERIOD_IN_MONTHS \r\n"
							+ "from PMST_POLICY pmp inner join PMST_MPH pmph on pmp.MPH_ID = pmph.MPH_ID inner join liccustomercommon.product_details pd on pd.LEAD_PRODUCT_ID=pmp.PRODUCT_ID inner join liccustomercommon.product_variant_details pvd on pvd.LEAD_VARIANT_ID=pmp.PRODUCT_VARIANT_ID inner join SURR_CONFIG sc on pvd.VARIANT_VERSION=sc.VARIANT_VERSION \r\n"
							+ "where pvd.VARIANT_VERSION='V1'");

			if (!ObjectUtils.isEmpty(policySearchDto.getPolicyNumber())) {
				sqlQuery.append(" and pmp.POLICY_NUMBER=:policyNumber");
			}
			if (!ObjectUtils.isEmpty(policySearchDto.getCustomerName())) {
				sqlQuery.append(" and pmp.CUSTOMER_NAME=:customerName");
			}
			if (!ObjectUtils.isEmpty(policySearchDto.getIntermediaryName())) {
				sqlQuery.append(" and pmp.INTERMEDIARY_NAME=:intermediaryName");
			}
			if (!ObjectUtils.isEmpty(policySearchDto.getLineOfBusiness())) {
				sqlQuery.append(" and pmp.LINE_OF_BUSINESS=:lineOfBusiness");
			}
			if (!ObjectUtils.isEmpty(policySearchDto.getUnitCode())) {
				sqlQuery.append(" and pmp.UNIT_CODE=:unitCode");
			}
			if (!ObjectUtils.isEmpty(policySearchDto.getMphPAN())) {
				sqlQuery.append(" and pmph.PAN=:mphPAN");
			}
			Query query = entityManager.createNativeQuery(sqlQuery.toString());
			
			if (!ObjectUtils.isEmpty(policySearchDto.getPolicyNumber())) {
				query.setParameter("policyNumber", policySearchDto.getPolicyNumber());
			}
			if (!ObjectUtils.isEmpty(policySearchDto.getCustomerName())) {
				query.setParameter("customerName", policySearchDto.getCustomerName());
			}
			if (!ObjectUtils.isEmpty(policySearchDto.getIntermediaryName())) {
				query.setParameter("intermediaryName", policySearchDto.getIntermediaryName());
			}
			if (!ObjectUtils.isEmpty(policySearchDto.getLineOfBusiness())) {
				query.setParameter("lineOfBusiness", policySearchDto.getLineOfBusiness());
			}
			if (!ObjectUtils.isEmpty(policySearchDto.getUnitCode())) {
				query.setParameter("unitCode", policySearchDto.getUnitCode());
			}
			if (!ObjectUtils.isEmpty(policySearchDto.getMphPAN())) {
				query.setParameter("mphPAN", policySearchDto.getMphPAN());
			}
			try {
				policySurrenderDtos = new ArrayList<PolicySurrenderDto>();
				@SuppressWarnings("unchecked")
				List<Object[]> obj1 = query.getResultList();
				if (!obj1.isEmpty()) {
					for (Object[] record : obj1) {	
						policySurrenderDto = new PolicySurrenderDto();
						if(record[0]!=null)
						policySurrenderDto.setPolicyId(Long.parseLong(CodeUtil.getStringValue(record[0])));
						policySurrenderDto.setPolicyNumber(CodeUtil.getStringValue(record[1]));
						if(record[2]!=null)
						policySurrenderDto.setProductId(Long.parseLong(CodeUtil.getStringValue(record[2])));
						if(record[3]!=null)
						policySurrenderDto.setVariantId(Long.parseLong(CodeUtil.getStringValue(record[3])));
						if(record[4]!=null)
						policySurrenderDto.setPolicyStatusId(Long.parseLong(CodeUtil.getStringValue(record[4])));
						policySurrenderDto.setPolicyStatus("");
						if(record[5]!=null)
						policySurrenderDto.setMphId(Long.parseLong(CodeUtil.getStringValue(record[5])));
						policySurrenderDto.setMphCode(CodeUtil.getStringValue(record[6]));
						policySurrenderDto.setMphName(CodeUtil.getStringValue(record[7]));
						if(record[8]!=null)
						policySurrenderDto.setPolicyStartDate((Date)record[8]);
						if(record[9]!=null)
						policySurrenderDto.setPolicyEndDate((Date)record[9]);
						if(record[10]!=null)
						policySurrenderDto.setDateOfCommencementOfPolicy((Date)record[10]);
						if(record[11]!=null)
						policySurrenderDto.setAnnualRenewalDate((Date)record[11]);
						if(record[12]!=null)
						policySurrenderDto.setTotalNumberOfLives(Long.parseLong(CodeUtil.getStringValue(record[12])));
						policySurrenderDto.setMphPAN(CodeUtil.getStringValue(record[13]));
						policySurrenderDto.setUnitOffice(CodeUtil.getStringValue(record[14]));
						policySurrenderDto.setProductName(CodeUtil.getStringValue(record[15]));
						policySurrenderDto.setVariantName(CodeUtil.getStringValue(record[16]));
						if(record[17]!=null)
						policySurrenderDto.setNoticePeriodInMonths(Long.parseLong(CodeUtil.getStringValue(record[17])));
						if(record[18]!=null)
						policySurrenderDto.setWaitingPeriodInMonths(Long.parseLong(CodeUtil.getStringValue(record[18])));
								

					policySurrenderDtos.add(policySurrenderDto);
					}
				}
				if(policySurrenderDtos.size() > 0)
				{
					return ApiResponseDto.success(policySurrenderDtos);
				}
				else
				{
					return ApiResponseDto.notFound(policySurrenderDtos);
				}
			} catch (Exception e) {
			}
			return ApiResponseDto.errorMessage(null,"", "Error");
		}

}
