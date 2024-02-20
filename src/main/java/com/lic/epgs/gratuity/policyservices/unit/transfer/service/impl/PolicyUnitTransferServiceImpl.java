package com.lic.epgs.gratuity.policyservices.unit.transfer.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.lic.epgs.gratuity.quotation.premium.dto.GratuityCalculationDto;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.gratuity.accountingservice.dto.HSNCodeDto;
import com.lic.epgs.gratuity.accountingservice.dto.UnlockDepositDetailDto;
import com.lic.epgs.gratuity.accountingservice.service.AccountingService;
import com.lic.epgs.gratuity.common.constants.HttpConstants;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.CommonMasterVariantEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryModuleEntity;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.common.entity.TaskAllocationEntity;
import com.lic.epgs.gratuity.common.repository.CommonMasterProductRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterStateRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterVariantRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryModuleRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.common.repository.PickListItemRepository;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.common.repository.TaskAllocationRepository;
import com.lic.epgs.gratuity.common.repository.TaskProcessRepository;
import com.lic.epgs.gratuity.common.service.CommonModuleService;
import com.lic.epgs.gratuity.common.service.CommonService;
import com.lic.epgs.gratuity.common.service.impl.CommonModuleServiceImpl;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.fund.service.FundService;
import com.lic.epgs.gratuity.mph.dto.TemptMPHDto;
import com.lic.epgs.gratuity.mph.entity.HistoryMPHEntity;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.mph.helper.MPHHelper;
import com.lic.epgs.gratuity.mph.repository.HistoryMPHRepository;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.mph.repository.TempMPHRepository;
import com.lic.epgs.gratuity.policy.claim.dto.GratuityCalculationsDto;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimCommonHelper;
import com.lic.epgs.gratuity.policy.claim.repository.TempPolicyClaimBeneficiaryRepository;
import com.lic.epgs.gratuity.policy.claim.repository.TempPolicyClaimPropsRepository;
import com.lic.epgs.gratuity.policy.claim.service.impl.PolicyClaimServiceImpl;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policy.dto.ShowDepositLockDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyAdjustmentDetailEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyAdjustmentDetailEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyContributionDetailEntity;
import com.lic.epgs.gratuity.policy.entity.PolicyHistoryEntity;
import com.lic.epgs.gratuity.policy.entity.StagingPolicyEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.HistoryGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.entity.PolicyGratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.HistoryGratutiyBenefitRepository;
import com.lic.epgs.gratuity.policy.gratuitybenefit.repository.PolicyGratuityBenefitRepository;
import com.lic.epgs.gratuity.policy.helper.PolicyHelper;
import com.lic.epgs.gratuity.policy.integration.dto.ResponseDto;
import com.lic.epgs.gratuity.policy.lifecover.entity.HistoryLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.entity.PolicyLifeCoverEntity;
import com.lic.epgs.gratuity.policy.lifecover.repository.HistoryLifeCoverRepository;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.member.entity.HistoryMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.repository.HistoryMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberAppointeeRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyContributionEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.MasterPolicyDepositEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyContributionEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyContrySummaryEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.entity.PolicyDepositEntity;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyContributionRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.MasterPolicyDepositRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyContributionRepository;
import com.lic.epgs.gratuity.policy.premiumadjustment.repositoty.PolicyDepositRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalGratuityBenefitTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.repository.RenewalGratuityBenefitTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.entity.RenewalLifeCoverTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.lifecover.repository.RenewalLifeCoverTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.PolicyServiceRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.RenewalPolicyTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.entity.RenewalSchemeruleTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.schemerule.repository.RenewalSchemeruleTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.entity.RenewalValuationTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuation.repository.RenewalValuationTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationBasicTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationMatrixTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.entity.RenewalValuationWithdrawalTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationBasicTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationMatrixTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.valuationmatrix.repository.RenewalValuationWithdrawalTMPRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyAdjustmentDetailRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyAdjustmentDetailRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyHistoryRepository;
import com.lic.epgs.gratuity.policy.repository.StagingPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;
import com.lic.epgs.gratuity.policy.repository.custom.StagingPolicyCustomRepository;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeEntity;
import com.lic.epgs.gratuity.policy.schemerule.entity.PolicySchemeRuleHistoryEntity;
import com.lic.epgs.gratuity.policy.schemerule.repository.PolicySchemeRuleHistoryRepository;
import com.lic.epgs.gratuity.policy.schemerule.repository.PolicySchemeRuleRepository;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyMasterValuationEntity;
import com.lic.epgs.gratuity.policy.valuation.entity.PolicyValuationHistoryEntity;
import com.lic.epgs.gratuity.policy.valuation.repository.PolicyMasterValuationRepository;
import com.lic.epgs.gratuity.policy.valuation.repository.PolicyValuationHistoryRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationBasicHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationMatrixHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValuationWithdrawalRateHistoryEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.entity.PolicyValutationBasicEntity;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationBasicHistoryRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationBasicRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationMatrixHistoryRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationMatrixRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationWithdrawalRateHistoryRepository;
import com.lic.epgs.gratuity.policy.valuationmatrix.repository.PolicyValuationWithdrawalRateRepository;
import com.lic.epgs.gratuity.policyservices.contributionadjustment.repository.ContributionAdjustmentPropsRepository;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.AdjustmentPropsDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.BeneficiaryPaymentDetailModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.DepositAdjustementRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.DocumentDetailsResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.DocumentRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.ExactMatchTransferSearchRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.MphDetailsResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.OmniDocumentResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.PolicyContriDetailDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.PreValidationRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.RegularGstDetailModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.RejectMemberTransferRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.RemoveDocReq;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.RetainMemberLicIdRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.ReversalGstDetailModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.SaveMemberTransferDetailRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.ServiceMemberTransferDetailsResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferDocUpdateDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferDocumentDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferMemberDrCrReqModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferMemberReqModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferMemberSearchWithFilterRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferNotesRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferRequisitionModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferSearchWithServiceResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TrnRegisModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.UpdateTransferMemberModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.UploadDocReq;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.ContriAdjustmentPropsEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.MemberCategoryEntityVersion;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.PolicyContriDetailEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.PolicyDeposit;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferDocumentDetailEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferMemberPolicyDetailEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferNotesEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferPolicyDetailEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferRequisitionEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.helper.ContriHelper;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.helper.PolicyDestinationVersionHelper;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.model.TransferGratuityCalculationDTO;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.model.TransferInCalculateDTO;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.model.TransferRefundCalculationDTO;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.AdjustmentPropsRepository;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.MasterPolicyCustomDestinationVersionRepository;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.MasterPolicyTransferVerRepository;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.PolicyAdjustmentDetailRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.PolicyContriDetailRepository;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.PolicyContriRepository;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.PolicyContrySummaryRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.PolicyDepositRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.SearchWithServiceNameAndFilterDao;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.StagingPolicyCustomDestinationVersionRepository;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.TransferDocumentRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.TransferMemberPolicyDetailRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.TransferNotesRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.TransferPolicyDetailRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.TransferRequisitionRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.impl.MemberCategoryRepositoryVersionOne;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.service.MemberTransferInOutService;
import com.lic.epgs.gratuity.policyservices.policymodification.repository.MPHBankRepositoryPS;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.SavePolicyUnitTransferDetailRequest;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.UnitDocumentDetailsResponse;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.UnitDocumentRequest;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.UnitTransferDocUpdateDto;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.UnitTransferNotesRequest;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.UnitUploadDocReq;
import com.lic.epgs.gratuity.policyservices.unit.transfer.entity.UnitTransferDocumentDetailEntity;
import com.lic.epgs.gratuity.policyservices.unit.transfer.entity.UnitTransferNotesEntity;
import com.lic.epgs.gratuity.policyservices.unit.transfer.entity.UnitTransferPolicyDetailEntity;
import com.lic.epgs.gratuity.policyservices.unit.transfer.entity.UnitTransferRequisitionEntity;
import com.lic.epgs.gratuity.policyservices.unit.transferr.repository.UnitSearchWithServiceNameAndFilterDao;
import com.lic.epgs.gratuity.policyservices.unit.transferr.repository.UnitTransferDocumentRepo;
import com.lic.epgs.gratuity.policyservices.unit.transferr.repository.UnitTransferNotesRepo;
import com.lic.epgs.gratuity.policyservices.unit.transferr.repository.UnitTransferPolicyDetailRepo;
import com.lic.epgs.gratuity.policyservices.unit.transferr.repository.UnitTransferRequisitionRepo;
import com.lic.epgs.gratuity.policyservices.unit.transferr.service.PolicyUnitTransferService;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBulkResponseDto;
import com.lic.epgs.gratuity.quotation.member.entity.MemberBulkStgEntity;
import com.lic.epgs.gratuity.quotation.member.repository.MemberBatchRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberBulkStgRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberRepository;
import com.lic.epgs.gratuity.quotation.premium.entity.GratuityCalculationEntity;
import com.lic.epgs.gratuity.quotation.premium.repository.GratuityCalculationRepository;
import com.lic.epgs.gratuity.quotation.premium.repository.impl.GratuityCalculationRepositoryImpl;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PolicyUnitTransferServiceImpl implements PolicyUnitTransferService {

	@Autowired
	UnitTransferNotesRepo unitTransferNotesRepo;

	@Autowired
	UnitDocumentUploadServiceImpl unitDocumentUploadServiceImpl;

	@Autowired
	UnitTransferDocumentRepo unitTransferDocumentRepo;

	@Autowired
	UnitSearchWithServiceNameAndFilterDao unitSearchWithServiceNameAndFilterDao;

	@Autowired
	UnitTransferRequisitionRepo unitTransferRequisitionRepo;

	@Autowired
	UnitTransferPolicyDetailRepo unitTransferPolicyDetailRepo;
	
	@Override
	public ResponseEntity<Map<String, Object>> savePolicyUnitTransfer(SavePolicyUnitTransferDetailRequest saveRequest) {
		log.info("Entering into MemberTransferInOutServiceImpl : saveMemberTransfer");
		Map<String, Object> response = new HashMap<String, Object>();
		boolean check;

		try {

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

			UnitTransferRequisitionEntity transferRequisitionEntity = new UnitTransferRequisitionEntity();

			transferRequisitionEntity.setUnitTransferRequestNumber(unitTransferRequisitionRepo.generateSeq());
			transferRequisitionEntity.setUnitTransferRequestDate(new Date());
			transferRequisitionEntity.setLicId(saveRequest.getLicId());
			transferRequisitionEntity.setUnitTransferStatus("Active");
			transferRequisitionEntity.setUnitTransferSubStatus("Active");
			transferRequisitionEntity.setRole(saveRequest.getRole());
			transferRequisitionEntity.setLocationType(saveRequest.getLocationType());
			transferRequisitionEntity.setModifiedOn(new Date());
			transferRequisitionEntity.setCreatedOn(new Date());
			transferRequisitionEntity.setCreatedBy(saveRequest.getUserName());
			UnitTransferRequisitionEntity savedRequistion = unitTransferRequisitionRepo.save(transferRequisitionEntity);

			UnitTransferPolicyDetailEntity unitTransferPolicyDetailEntity = new UnitTransferPolicyDetailEntity();
			unitTransferPolicyDetailEntity.setUnitTransferPolicyDetailId(savedRequistion.getUnitTransferRequisitionId());
			unitTransferPolicyDetailEntity.setPolicyNumber(Long.valueOf(saveRequest.getPolicyNumber()));
			unitTransferPolicyDetailEntity.setMphName(saveRequest.getMphName());
			unitTransferPolicyDetailEntity.setPolicyStatus(saveRequest.getPolicyStatus());
			unitTransferPolicyDetailEntity.setProductName(saveRequest.getProductName());
			unitTransferPolicyDetailEntity.setProductVariant(saveRequest.getProductVariant());
			unitTransferPolicyDetailEntity.setPolicyStartDate(saveRequest.getPolicyStartDate());
			unitTransferPolicyDetailEntity.setPolicyEndDate(saveRequest.getPolicyEndDate());
			unitTransferPolicyDetailEntity.setSourceUnit(saveRequest.getSourceUnit());
			unitTransferPolicyDetailEntity.setDestinationUnit(saveRequest.getDestinationUnit());
			unitTransferPolicyDetailEntity.setPolicyAccountValue(saveRequest.getPolicyAccountValue());
			unitTransferPolicyDetailEntity.setTotalFundValue(saveRequest.getTotalFundValue());
			unitTransferPolicyDetailEntity.setModifiedOn(new Date());
			unitTransferPolicyDetailEntity.setCreatedOn(new Date());
			unitTransferPolicyDetailEntity.setCreatedBy(saveRequest.getUserName());
			UnitTransferPolicyDetailEntity savedPolicyDetail = unitTransferPolicyDetailRepo
					.save(unitTransferPolicyDetailEntity);

			response.put("requisitionDetail", transferRequisitionEntity);
			response.put("policyDetail", unitTransferPolicyDetailEntity);
			response.put("responseMessage", "Transfer Unit Policy details saved successfully.");
			response.put("responseCode", "success");
			return ResponseEntity.status(HttpStatus.OK).body(response);

		} catch (Exception e) {
			log.error("Save member transfer detailsa failed due to : " + e.getMessage());
			response.put("responseMessage", "member transfer Failed to Save.");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}


	@Override
	public Map<String, Object> saveTransferNotes(UnitTransferNotesRequest unitTransferNotesRequest) {
		log.info("Transfer Renewal Notes serviceImpl started ");
		Map<String, Object> response = new HashMap<>();

		try {
			UnitTransferNotesEntity transferNotesEntity = getTransferNotesEntity(unitTransferNotesRequest);
			unitTransferNotesRepo.save(transferNotesEntity);

			response.put("responseMessage", "TRANSFER Notes Saved Successfully.");
			response.put("responseCode", "success");

			log.info("TRANSFER save Notes serviceImpl success");
			return response;
		} catch (Exception e) {
			System.out.println("Exception e is ..." + e);
			response.put("responseMessage", "TRANSFER Notes Failed to Save.");
			response.put("responseCode", "failed");
			log.info("TRANSFER save Notes serviceImpl failed");
		}
		log.info("TRANSFER save Notes serviceImpl ends");
		return response;
	}

	@NotNull
	private static UnitTransferNotesEntity getTransferNotesEntity(UnitTransferNotesRequest transferNotesRequest) {
		UnitTransferNotesEntity transferNotesEntity = new UnitTransferNotesEntity();

		transferNotesEntity.setUnitTransferRequisitionId(transferNotesRequest.getUnitTransferRequisitionId());
		transferNotesEntity.setUnitNote(transferNotesRequest.getUnitTransferNote());
		transferNotesEntity.setModifiedBy(transferNotesRequest.getModifiedBy());
		transferNotesEntity.setModifiedOn(new Date());
		transferNotesEntity.setCreatedOn(new Date());
		transferNotesEntity.setCreatedBy(transferNotesRequest.getCreatedBy());
		return transferNotesEntity;
	}

	@Override
	public ResponseEntity<Map<String, Object>> edit(UnitTransferDocUpdateDto transferDocUpdateDto) {
		log.info("Entering into MemberTransferInOutServiceImpl : editDocumentDeatilsTransfer");
		Map<String, Object> response = new HashMap<String, Object>();
		boolean check;

		try {
			UnitTransferDocumentDetailEntity transferDocumentDetail = new UnitTransferDocumentDetailEntity();
			for (UnitDocumentRequest updateDocumentReq : transferDocUpdateDto.getDocumentRequest()) {

				UnitTransferDocumentDetailEntity transferDocumentDetailEntity = new UnitTransferDocumentDetailEntity();

				transferDocumentDetailEntity = unitTransferDocumentRepo.findByUnitTransferRequisitionIdAndPicklistItemId(
						transferDocUpdateDto.getUnitTransferRequisitionId(), updateDocumentReq.getPicklistItemId());
				log.info(transferDocumentDetailEntity);
				if (transferDocumentDetailEntity == null) {

					transferDocumentDetail
							.setUnitTransferRequisitionId(transferDocUpdateDto.getUnitTransferRequisitionId());
					transferDocumentDetail.setPicklistItemId(updateDocumentReq.getPicklistItemId());
					transferDocumentDetail.setIsReceived(updateDocumentReq.getIsReceived());
					transferDocumentDetail.setIsUploaded(updateDocumentReq.getIsUploaded());
					transferDocumentDetail.setRemarks(transferDocUpdateDto.getRemarks());
					transferDocumentDetail.setDocumentIndex(updateDocumentReq.getDocumentIndex());
					transferDocumentDetail.setCreatedBy(transferDocUpdateDto.getCreatedBy());
					transferDocumentDetail.setFolderIndexNo(updateDocumentReq.getFolderIndexNo());
					transferDocumentDetail.setModifiedOn(new Date());
					transferDocumentDetail.setCreatedOn(new Date());
					unitTransferDocumentRepo.saveAndFlush(transferDocumentDetail);
					transferDocumentDetail = new UnitTransferDocumentDetailEntity();
				} else {
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

					UnitTransferDocumentDetailEntity unitTransferDocumentDetailEntity = new UnitTransferDocumentDetailEntity();

					unitTransferDocumentDetailEntity = unitTransferDocumentRepo
							.findByUnitTransferRequisitionIdAndPicklistItemId(
									transferDocUpdateDto.getUnitTransferRequisitionId(),
									updateDocumentReq.getPicklistItemId());
					log.info(unitTransferDocumentDetailEntity);

					unitTransferDocumentDetailEntity
							.setUnitTransferRequisitionId(transferDocUpdateDto.getUnitTransferRequisitionId());
					unitTransferDocumentDetailEntity.setPicklistItemId(updateDocumentReq.getPicklistItemId());
					unitTransferDocumentDetailEntity.setIsReceived(updateDocumentReq.getIsReceived());
					unitTransferDocumentDetailEntity.setIsUploaded(updateDocumentReq.getIsUploaded());
					unitTransferDocumentDetailEntity.setRemarks(transferDocUpdateDto.getRemarks());
					unitTransferDocumentDetailEntity.setDocumentIndex(updateDocumentReq.getDocumentIndex());
					unitTransferDocumentDetailEntity.setFolderIndexNo(updateDocumentReq.getFolderIndexNo());
					unitTransferDocumentDetailEntity.setModifiedOn(new Date());
					unitTransferDocumentRepo.save(unitTransferDocumentDetailEntity);
				}
			}
			response.put("responseMessage", "Document Details Updated successfully.");
			response.put("responseCode", "success");
		} catch (Exception e) {
			log.error("Update Document transfer detailsa failed due to : " + e.getMessage());
			response.put("responseMessage", "Document Details transfer Failed to Update.");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	@Override
	public Map<String, Object> uploadDocs(UnitUploadDocReq uploadDocReq) throws JsonProcessingException {

		ResponseEntity<String> uploadStatus = unitDocumentUploadServiceImpl.uploadClaimDocs(uploadDocReq);

		Map<String, Object> respone = new HashMap<>();

		ObjectMapper mapper = new ObjectMapper();
		OmniDocumentResponse omniDocsResponseObj = mapper.readValue(uploadStatus.getBody().toString(),
				OmniDocumentResponse.class);

		if (omniDocsResponseObj.getStatus().equals("true")) {
//	 			List<TransferDocumentDetailEntity> transferDocumentDetailEntity = transferDocumentRepo.
//	                    findByTransferRequisitionId(uploadDocReq.getTransferRequisitionId());
			UnitTransferDocumentDetailEntity transferDocumentDetailEntity = unitTransferDocumentRepo
					.findByUnitTransferRequisitionIdAndPicklistItemId(uploadDocReq.getUnitTransferRequisitionId(),
							uploadDocReq.getPicklistItemId());

			log.info(transferDocumentDetailEntity);

			if (transferDocumentDetailEntity == null) {

				UnitTransferDocumentDetailEntity transferDocumentDetailEntity1 = new UnitTransferDocumentDetailEntity();
				transferDocumentDetailEntity1.setUnitTransferRequisitionId(uploadDocReq.getUnitTransferRequisitionId());
				transferDocumentDetailEntity1.setPicklistItemId(uploadDocReq.getPicklistItemId());
				transferDocumentDetailEntity1.setDocumentIndex(omniDocsResponseObj.getDocumentIndexNo());
				if (omniDocsResponseObj.getDocumentIndexNo() != null) {
					transferDocumentDetailEntity1.setIsUploaded("Y");
				} else {
					transferDocumentDetailEntity1.setIsUploaded("N");
				}
				transferDocumentDetailEntity1.setIsReceived("Y");
				transferDocumentDetailEntity1.setRemarks("Document Uploaded");
				transferDocumentDetailEntity1.setFolderIndexNo(omniDocsResponseObj.getFolderIndexNo());
				transferDocumentDetailEntity1.setCreatedBy(uploadDocReq.getCreatedBy());
				transferDocumentDetailEntity1.setCreatedOn(new Date());
				UnitTransferDocumentDetailEntity savedDoc = unitTransferDocumentRepo
						.save(transferDocumentDetailEntity1);

				respone.put("returnCode", 0);
				respone.put("responseMessage", "Document Uploaded Succesfully");
				respone.put("transferDocumentDetail", transferDocumentDetailEntity1);
				return respone;

			} else {
				UnitTransferDocumentDetailEntity transferDocumentDetailEntity2 = unitTransferDocumentRepo
						.findByUnitTransferRequisitionIdAndPicklistItemId(uploadDocReq.getUnitTransferRequisitionId(),
								uploadDocReq.getPicklistItemId());

				transferDocumentDetailEntity2.setUnitTransferRequisitionId(uploadDocReq.getUnitTransferRequisitionId());
				transferDocumentDetailEntity2.setPicklistItemId(uploadDocReq.getPicklistItemId());
				transferDocumentDetailEntity2.setDocumentIndex(omniDocsResponseObj.getDocumentIndexNo());
				if (omniDocsResponseObj.getDocumentIndexNo() != null) {
					transferDocumentDetailEntity2.setIsUploaded("Y");
				} else {
					transferDocumentDetailEntity2.setIsUploaded("N");
				}
				transferDocumentDetailEntity2.setIsReceived("Y");
				transferDocumentDetailEntity2.setRemarks("Document Uploaded");
				transferDocumentDetailEntity2.setFolderIndexNo(omniDocsResponseObj.getFolderIndexNo());
				transferDocumentDetailEntity2.setCreatedBy(uploadDocReq.getCreatedBy());
				transferDocumentDetailEntity2.setCreatedOn(new Date());

				unitTransferDocumentRepo.save(transferDocumentDetailEntity2);

				respone.put("returnCode", 0);
				respone.put("responseMessage", "Document Uploaded Succesfully");
				respone.put("transferDocumentDetail", transferDocumentDetailEntity2);
				return respone;
			}

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
		ResponseEntity<String> removeUploadResponse = unitDocumentUploadServiceImpl
				.removeUploadedDoc(removeDocReq.getFolderIndexNo(), removeDocReq.getDocumentIndex());
		OmniDocumentResponse omniDocsResponseObj = mapper1.readValue(removeUploadResponse.getBody().toString(),
				OmniDocumentResponse.class);

		if (omniDocsResponseObj.getStatus().equals("true")) {

			unitTransferDocumentRepo
					.removeDocImageUsingDcoumentIndexNo(Long.parseLong(removeDocReq.getDocumentIndex()));
			response.put("responseMessage", "Document deleted Successfully");
			response.put("responseCode", "success");
			return ResponseEntity.status(HttpStatus.OK).body(response);

		}

		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	@Override
	public ResponseEntity<String> getUploadDocs(RemoveDocReq removeDocReq) {

		ResponseEntity<String> uploadStatus = unitDocumentUploadServiceImpl
				.getUploadedDocs(removeDocReq.getDocumentIndex());
		return uploadStatus;
	}

	public List<UnitDocumentDetailsResponse> getDocumentDetails(Long unitTransferRequisitionId) {
		List<UnitDocumentDetailsResponse> listResponse = new ArrayList<>();
		try {
			listResponse = unitSearchWithServiceNameAndFilterDao.getDocumentDetails(unitTransferRequisitionId);
		} catch (Exception e) {
		}
		return listResponse;
	}

	
}