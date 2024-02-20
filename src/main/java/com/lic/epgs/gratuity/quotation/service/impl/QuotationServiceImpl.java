package com.lic.epgs.gratuity.quotation.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lic.epgs.gratuity.common.constants.HttpConstants;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.common.entity.TaskAllocationEntity;
import com.lic.epgs.gratuity.common.entity.TaskProcessEntity;
import com.lic.epgs.gratuity.common.helper.MemberCategoryHelper;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.common.repository.TaskAllocationRepository;
import com.lic.epgs.gratuity.common.repository.TaskProcessRepository;
import com.lic.epgs.gratuity.common.search.SearchRequest;
import com.lic.epgs.gratuity.common.search.SearchSpecification;
import com.lic.epgs.gratuity.common.service.CommonModuleService;
import com.lic.epgs.gratuity.policy.dto.GenerateCBSheetPdfDto;
import com.lic.epgs.gratuity.policy.entity.QuotationChargeEntity;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.QuotationChargeRepository;
import com.lic.epgs.gratuity.quotation.document.entity.DocumentEntity;
import com.lic.epgs.gratuity.quotation.document.entity.MasterDocumentEntity;
import com.lic.epgs.gratuity.quotation.document.helper.DocumentHelper;
import com.lic.epgs.gratuity.quotation.document.repository.DocumentRepository;
import com.lic.epgs.gratuity.quotation.document.repository.MasterDocumentRepository;
import com.lic.epgs.gratuity.quotation.dto.NewQuotationDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationBasicDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationSearchDto;
import com.lic.epgs.gratuity.quotation.entity.MasterQuotationEntity;
import com.lic.epgs.gratuity.quotation.entity.QuotationEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.helper.GratuityBenefitHelper;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.repository.GratuityBenefitPropsRepository;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.repository.GratuityBenefitRepository;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.repository.MasterGratuityBenefitRepository;
import com.lic.epgs.gratuity.quotation.helper.QuotationHelper;
import com.lic.epgs.gratuity.quotation.lifecover.entity.LifeCoverEntity;
import com.lic.epgs.gratuity.quotation.lifecover.entity.MasterLifeCoverEntity;
import com.lic.epgs.gratuity.quotation.lifecover.helper.LifeCoverFlatHelper;
import com.lic.epgs.gratuity.quotation.lifecover.helper.LifeCoverHelper;
import com.lic.epgs.gratuity.quotation.lifecover.repository.LifeCoverEntityRepository;
import com.lic.epgs.gratuity.quotation.lifecover.repository.MasterLifeCoverEntityRepository;
import com.lic.epgs.gratuity.quotation.member.entity.MasterMemberEntity;
import com.lic.epgs.gratuity.quotation.member.helper.BulkMemberUploadHelper;
import com.lic.epgs.gratuity.quotation.member.helper.MemberHelper;
import com.lic.epgs.gratuity.quotation.member.repository.MasterMemberRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberAddressRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberAppointeeRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberBankAccountRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberBatchFileRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberBatchRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberBulkStgRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberErrorRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberNomineeRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberRepository;
import com.lic.epgs.gratuity.quotation.notes.entity.MasterNotesEntity;
import com.lic.epgs.gratuity.quotation.notes.entity.NotesEntity;
import com.lic.epgs.gratuity.quotation.notes.helper.NotesHelper;
import com.lic.epgs.gratuity.quotation.notes.repository.MasterNotesRepository;
import com.lic.epgs.gratuity.quotation.notes.repository.NotesRepository;
import com.lic.epgs.gratuity.quotation.premium.entity.GratuityCalculationEntity;
import com.lic.epgs.gratuity.quotation.premium.entity.MasterPremiumEntity;
import com.lic.epgs.gratuity.quotation.premium.entity.PremiumEntity;
import com.lic.epgs.gratuity.quotation.premium.helper.PremiumHelper;
import com.lic.epgs.gratuity.quotation.premium.repository.GratuityCalculationRepository;
import com.lic.epgs.gratuity.quotation.premium.repository.MasterPremiumRepository;
import com.lic.epgs.gratuity.quotation.premium.repository.PremiumRepository;
import com.lic.epgs.gratuity.quotation.repository.MasterQuotationRepository;
import com.lic.epgs.gratuity.quotation.repository.QuotationRepository;
import com.lic.epgs.gratuity.quotation.schemerule.entity.MasterSchemeRuleEntity;
import com.lic.epgs.gratuity.quotation.schemerule.entity.SchemeRuleEntity;
import com.lic.epgs.gratuity.quotation.schemerule.helper.SchemeRuleHelper;
import com.lic.epgs.gratuity.quotation.schemerule.repository.MasterSchemeRuleRepository;
import com.lic.epgs.gratuity.quotation.schemerule.repository.SchemeRuleRepository;
import com.lic.epgs.gratuity.quotation.service.QuotationService;
import com.lic.epgs.gratuity.quotation.valuation.entity.MasterValuationEntity;
import com.lic.epgs.gratuity.quotation.valuation.entity.ValuationEntity;
import com.lic.epgs.gratuity.quotation.valuation.helper.ValuationHelper;
import com.lic.epgs.gratuity.quotation.valuation.repository.MasterValuationRepository;
import com.lic.epgs.gratuity.quotation.valuation.repository.ValuationRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationBasicEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationMatrixEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationBasicEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationMatrixEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.helper.ValuationMatrixHelper;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.MasterValuationBasicRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.MasterValuationMatrixRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.MasterValuationWithdrawalRateRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.ValuationBasicRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.ValuationMatrixRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.ValuationWithdrawalRateRepository;

/**
 * @author Gopi
 *
 */

@Service
public class QuotationServiceImpl implements QuotationService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Value("${app.commonModuleServiceEndpoint}")
	private String endPoint;

	@Value("${app.quotation.defaultStatudId}")
	private String defaultStatusId;

	@Value("${app.quotation.submittedForApprovalStatudId}")
	private String submittedForApprovalStatudId;

	@Value("${app.quotation.rejectedStatudId}")
	private String rejectedStatudId;

	@Value("${app.quotation.expiredStatudId}")
	private String expiredStatudId;

	@Value("${app.quotation.approvedStatudId}")
	private String approvedStatudId;

	@Value("${app.quotation.defaultSubStatudId}")
	private String defaultSubStatusId;

	@Value("${app.quotation.sentBackToMakerSubStatudId}")
	private String sentBackToMakerSubStatudId;

	@Value("${app.quotation.rejectedSubStatudId}")
	private String rejectedSubStatudId;

	@Value("${app.quotation.approvedSubStatudId}")
	private String approvedSubStatudId;

	@Value("${app.quotation.expiredSubStatudId}")
	private String expiredSubStatudId;

	@Value("${app.quotation.defaultTaggedStatusId}")
	private String defaultTaggedStatusId;

	@Value("${app.quotation.existingaggedStatusId}")
	private String existingaggedStatusId;

	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;

	@Value("${app.SITurl}")
	private String SITurl;

	@Autowired
	private QuotationRepository quotationRepository;

	@Autowired
	private MasterQuotationRepository masterQuotationRepository;

	@Autowired
	private SchemeRuleRepository schemeRuleRepository;

	@Autowired
	private MasterSchemeRuleRepository masterSchemeRuleRepository;

	@Autowired
	private LifeCoverEntityRepository lifeCoverEntityRepository;

	@Autowired
	private MasterLifeCoverEntityRepository masterLifeCoverEntityRepository;

	@Autowired
	private GratuityBenefitRepository gratuityBenefitRepository;

	@Autowired
	private GratuityBenefitPropsRepository gratuityBenefitPropsRepository;

	@Autowired
	private MasterGratuityBenefitRepository masterGratuityBenefitRepository;

	@Autowired
	private PolicyLifeCoverRepository policyLifeCoverRepository;

	@Autowired
	private MasterPolicyRepository masterPolicyRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberAddressRepository memberAddressRepository;

	@Autowired
	private MemberBankAccountRepository memberBankAccountRepository;

	@Autowired
	private MemberNomineeRepository memberNomineeRepository;

	@Autowired
	private MemberAppointeeRepository memberAppointeeRepository;

	@Autowired
	private MemberBatchRepository memberBatchRepository;

	@Autowired
	private MemberErrorRepository memberErrorRepository;

	@Autowired
	private MemberBatchFileRepository memberBatchFileRepository;

	@Autowired
	private MemberBulkStgRepository memberBulkStgRepository;

	@Autowired
	private MasterMemberRepository masterMemberRepository;

	@Autowired
	private NotesRepository notesRepository;

	@Autowired
	private MasterNotesRepository masterNotesRepository;

	@Autowired
	private PremiumRepository premiumRepository;

	@Autowired
	private MasterPremiumRepository masterPremiumRepository;

	@Autowired
	private ValuationRepository valuationRepository;

	@Autowired
	private MasterValuationRepository masterValuationRepository;

	@Autowired
	private ValuationBasicRepository valuationBasicRepository;

	@Autowired
	private MasterValuationBasicRepository masterValuationBasicRepository;

	@Autowired
	private ValuationMatrixRepository valuationMatrixRepository;

	@Autowired
	private MasterValuationMatrixRepository masterValuationMatrixRepository;

	@Autowired
	private ValuationWithdrawalRateRepository valuationWithdrawalRateRepository;

	@Autowired
	private MasterValuationWithdrawalRateRepository masterValuationWithdrawalRateRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private MasterDocumentRepository masterDocumentRepository;

	@Autowired
	private StandardCodeRepository standardCodeRepository;

	@Autowired
	private GratuityCalculationRepository gratuityCalculationRepository;

	@Autowired
	private QuotationChargeRepository quotationChargeRepository;

	@Autowired
	private MemberCategoryRepository memberCategoryRepository;

	@Autowired
	private CommonMasterUnitRepository commonMasterUnitRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private CommonModuleService commonModuleService;

	@Autowired
	private BulkMemberUploadHelper memberHelper;
	
	@Autowired
	private TaskProcessRepository taskProcessRepository;
	
	@Autowired
	private TaskAllocationRepository taskAllocationRepository;

	@Override
	public ApiResponseDto<QuotationDto> findById(Long id, String type) {
		if (type.equals("INPROGRESS")) {
			Optional<QuotationEntity> entity = quotationRepository.findById(id);
			if (entity.isPresent()) {
				return ApiResponseDto.success(QuotationHelper.entityToDto(entity.get()));
			}
		} else {
			Optional<MasterQuotationEntity> entity = masterQuotationRepository.findById(id);
			if (entity.isPresent()) {
				return ApiResponseDto.success(QuotationHelper.entityToDto(entity.get()));
			}
		}
		return ApiResponseDto.notFound(new QuotationDto());
	}

	@Override
	public ApiResponseDto<QuotationDto> findByProposalNumber(String proposalNumber) {
		Optional<QuotationEntity> entity = quotationRepository.findByProposalNumber(proposalNumber);
		if (entity.isPresent()) {
			return ApiResponseDto.success(QuotationHelper.entityToDto(entity.get()));
		} else {
			return ApiResponseDto.notFound(new QuotationDto());
		}
	}

	@Transactional
	@Override
	public ApiResponseDto<QuotationDto> create(String proposalNumber, NewQuotationDto newQuotationDto) {
		QuotationEntity newQuotationEntity = null;
		TaskProcessEntity taskProcessEntity =taskProcessRepository.findByProcessName("QUOTATION");
		TaskAllocationEntity taskAllocationEntity = new TaskAllocationEntity();
	
		List<String> getListNumer = quotationRepository.addQuotationNumber(proposalNumber);
		String getQuotationNumber = null;
		if (getListNumer.size() > 0) {
			getQuotationNumber = QuotationHelper.addQuotationNumber(proposalNumber, getListNumer.get(0));
		} else {
			getQuotationNumber = proposalNumber + "01";
		}
		if (getQuotationNumber != null) {
			QuotationEntity quotationEntity = new QuotationEntity();
			quotationEntity.setCustomerCode(newQuotationDto.getCustomerCode());
			quotationEntity.setUnitOfficeId(newQuotationDto.getUnitOfficeId());
			quotationEntity.setProductId(newQuotationDto.getProductId());
			quotationEntity.setProductVariantId(newQuotationDto.getProductVariantId());
			quotationEntity.setBusinessTypeId(newQuotationDto.getBusinessTypeId());
			quotationEntity.setRiskGroupId(newQuotationDto.getRiskGroupId());
			quotationEntity.setGroupSizeId(newQuotationDto.getGroupSizeId());
			quotationEntity.setGroupSumAssuredId(newQuotationDto.getGroupSumAssuredId());
			quotationEntity.setFrequencyId(newQuotationDto.getFrequencyId());

			quotationEntity.setDateOfCommencement(newQuotationDto.getDateOfCommencement());
			quotationEntity.setGstApplicableId(newQuotationDto.getGstApplicableId());
			quotationEntity.setCategoryForGstNonApplicableId(newQuotationDto.getCategoryForGstNonApplicableId());
			quotationEntity.setNumber(getQuotationNumber);
			quotationEntity.setProposalNumber(proposalNumber);
			quotationEntity.setDate(new Date());
			quotationEntity.setStatusId(Long.parseLong(defaultStatusId));
			quotationEntity.setSubStatusId(Long.parseLong(defaultSubStatusId));
			quotationEntity.setTaggedStatusID(Long.parseLong(defaultTaggedStatusId));
			quotationEntity.setPayto(newQuotationDto.getPayto());
			quotationEntity.setUnitCode(newQuotationDto.getUnitCode());
			quotationEntity.setIsActive(true);
			quotationEntity.setCreatedBy(newQuotationDto.getCreatedBy());
			quotationEntity.setCreatedDate(new Date());
			quotationEntity.setIndustryType(newQuotationDto.getIndustryType());

			newQuotationEntity = quotationRepository.save(quotationEntity);
			
			taskAllocationEntity.setTaskStatus(defaultStatusId);
			taskAllocationEntity.setRequestId(getQuotationNumber);
			taskAllocationEntity.setTaskProcessTaskPrId(taskProcessEntity.getId());
			taskAllocationEntity.setLocationType(newQuotationDto.getUnitCode());
			taskAllocationEntity.setCreatedBy(newQuotationDto.getCreatedBy());
			taskAllocationEntity.setCreatedDate(new Date());
			taskAllocationEntity.setModulePrimaryId(newQuotationEntity.getId());
			taskAllocationEntity.setIsActive(true);
			taskAllocationRepository.save(taskAllocationEntity);
			
			

			return ApiResponseDto.created(QuotationHelper.entityToDto(newQuotationEntity));
		} else {
			System.out.println("start error");
			return ApiResponseDto.errorMessage(null, HttpConstants.SERVER_EXCEPTION,
					"Cannot Create Quotation more than 99 for one Proposal");
		}

	}

	@Override
	public ApiResponseDto<QuotationDto> update(Long id, QuotationBasicDto quotationBasicDto) {
		QuotationEntity quotationEntity = quotationRepository.findById(id).get();

		quotationEntity.setDateOfCommencement(quotationBasicDto.getDateOfCommencement());
		quotationEntity.setGstApplicableId(quotationBasicDto.getGstApplicableId());
		quotationEntity.setCategoryForGstNonApplicableId(quotationBasicDto.getCategoryForGstNonApplicableId());
		quotationEntity.setModifiedBy(quotationBasicDto.getModifiedBy());
		quotationEntity.setModifiedDate(new Date());

		return ApiResponseDto.success(QuotationHelper.entityToDto(quotationRepository.save(quotationEntity)));
	}

	@Override
	public ApiResponseDto<QuotationDto> associateBankAccount(Long id, Long bankAccountId, String modifiedBy) {
		QuotationEntity quotationEntity = quotationRepository.findById(id).get();

		quotationEntity.setBankAccountId(bankAccountId);
		quotationEntity.setModifiedBy(modifiedBy);
		quotationEntity.setModifiedDate(new Date());

		return ApiResponseDto.success(QuotationHelper.entityToDto(quotationRepository.save(quotationEntity)));
	}

	@Override
	public ApiResponseDto<QuotationDto> associateContactPerson(Long id, Long contactPersonId, String modifiedBy) {
		QuotationEntity quotationEntity = quotationRepository.findById(id).get();

		quotationEntity.setContactPersonId(contactPersonId);
		quotationEntity.setModifiedBy(modifiedBy);
		quotationEntity.setModifiedDate(new Date());

		return ApiResponseDto.success(QuotationHelper.entityToDto(quotationRepository.save(quotationEntity)));
	}

	public Page<QuotationDto> search(SearchRequest request) {
		SearchSpecification<QuotationEntity> specification = new SearchSpecification<>(request);
		Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
		Page<QuotationEntity> t = quotationRepository.findAll(specification, pageable);
		List<QuotationDto> tt = t.stream().map(QuotationHelper::entityToDto).collect(Collectors.toList());

		return new PageImpl<QuotationDto>(tt, pageable, tt.size());
	}

	private ApiResponseDto<List<QuotationDto>> inprogressFilter(QuotationSearchDto quotationSearchDto) {
		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<QuotationEntity> createQuery = criteriaBuilder.createQuery(QuotationEntity.class);

		Root<QuotationEntity> root = createQuery.from(QuotationEntity.class);

		if (quotationSearchDto.getTaggedStatusId() > 0) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("taggedStatusID")),
					quotationSearchDto.getTaggedStatusId()));
		}
		if (StringUtils.isNotBlank(quotationSearchDto.getNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("number")),
					quotationSearchDto.getNumber().toLowerCase().trim()));
		}
		if (StringUtils.isNotBlank(quotationSearchDto.getProposalNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("proposalNumber")),
					quotationSearchDto.getProposalNumber().toLowerCase()));
		}
		if (quotationSearchDto.getUnitOfficeId() != null && quotationSearchDto.getUnitOfficeId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("unitOfficeId"), quotationSearchDto.getUnitOfficeId()));
		}
		if (quotationSearchDto.getProductId() != null && quotationSearchDto.getProductId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("productId"), quotationSearchDto.getProductId()));
		}
		if (quotationSearchDto.getProductVariantId() != null && quotationSearchDto.getProductVariantId() > 0) {
			predicates
					.add(criteriaBuilder.equal(root.get("productVariantId"), quotationSearchDto.getProductVariantId()));
		}
		if (quotationSearchDto.getCustomerCode() != null
				&& StringUtils.isNotBlank(quotationSearchDto.getCustomerCode())) {
			predicates.add(criteriaBuilder.equal(root.get("customerCode"), quotationSearchDto.getCustomerCode()));
		}
		if (quotationSearchDto.getBusinessTypeId() != null && quotationSearchDto.getBusinessTypeId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("businessTypeId"), quotationSearchDto.getBusinessTypeId()));
		}
		if (quotationSearchDto.getStatusId() != null && quotationSearchDto.getStatusId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("statusId"), quotationSearchDto.getStatusId()));
		}
		
		if( quotationSearchDto.getSubStatusId()!= null && quotationSearchDto.getSubStatusId() >0) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("subStatusId")),
					quotationSearchDto.getSubStatusId() ));
		}
		/*
		 * if (StringUtils.isNotBlank(quotationSearchDto.getPolicyNumber())) {
		 * predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get(
		 * "policyNumber")), quotationSearchDto.getPolicyNumber().toLowerCase())); }
		 */
//		if (quotationSearchDto.getFromDate() != null && quotationSearchDto.getToDate() != null) {
//			predicates.add(criteriaBuilder.between(root.get("createdDate"), quotationSearchDto.getFromDate(),
//					quotationSearchDto.getToDate()));
//		}
		if (quotationSearchDto.getFromDate() != null && quotationSearchDto.getToDate() != null) {
			Date fromDate = quotationSearchDto.getFromDate();
			Date toDate = quotationSearchDto.getToDate();
			
			fromDate= constructeStartDateTime(fromDate);
			toDate = constructeEndDateTime(toDate);
			Predicate onStart = criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), fromDate);
			Predicate onEnd = criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), toDate);
			predicates.add(onStart);
			predicates.add(onEnd);
		}
//		if (StringUtils.isNotBlank(quotationSearchDto.getFromDate())
//				&& StringUtils.isNotBlank(quotationSearchDto.getToDate())) {
//			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//			try {
//				predicates.add(
//						criteriaBuilder.between(root.get("date"), dateFormat.parse(quotationSearchDto.getFromDate()),
//								dateFormat.parse(quotationSearchDto.getFromDate())));
//			} catch (ParseException e) {
//
//			}
//		}
		if (StringUtils.isNotBlank(quotationSearchDto.getSortOrder())
				&& StringUtils.isNotBlank(quotationSearchDto.getSortBy())) {
			if (quotationSearchDto.getSortOrder().toLowerCase().equals("asc")) {
				createQuery.orderBy(criteriaBuilder.asc(root.get(quotationSearchDto.getSortBy())));
			} else {
				createQuery.orderBy(criteriaBuilder.desc(root.get(quotationSearchDto.getSortBy())));
			}
		} else {
			createQuery.orderBy(criteriaBuilder.asc(root.get("date")));
		}

		if (quotationSearchDto.getUserType().equals("UO")) {
			if (quotationSearchDto.getUnitCode() != null) {
				predicates.add(criteriaBuilder.equal(root.get("unitCode"), quotationSearchDto.getUnitCode()));
			}
		}
		if (quotationSearchDto.getUserType().equals("ZO")) {

			String get = quotationSearchDto.getUnitCode().substring(0, 2);
			predicates.add(
					criteriaBuilder.like(criteriaBuilder.lower(root.get("unitCode")), "%" + get.toLowerCase() + "%"));
		}

		createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
		List<QuotationEntity> entities = new ArrayList<QuotationEntity>();
		entities = entityManager.createQuery(createQuery).getResultList();

		return ApiResponseDto.success(entities.stream().map(QuotationHelper::entityToDto).collect(Collectors.toList()));
	}

	private ApiResponseDto<List<QuotationDto>> existingFilter(QuotationSearchDto quotationSearchDto) {
		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MasterQuotationEntity> createQuery = criteriaBuilder.createQuery(MasterQuotationEntity.class);

		Root<MasterQuotationEntity> root = createQuery.from(MasterQuotationEntity.class);

		if (quotationSearchDto.getTaggedStatusId() > 0) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("taggedStatusID")),
					quotationSearchDto.getTaggedStatusId()));
		}
		if (StringUtils.isNotBlank(quotationSearchDto.getNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("number")),
					quotationSearchDto.getNumber().toLowerCase().trim()));
		}
		if(quotationSearchDto.getModuleType()=="NEWPOLICY") {
			if (StringUtils.isNotBlank(quotationSearchDto.getProposalNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("proposalNumber")),
						quotationSearchDto.getProposalNumber().toLowerCase()));
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("isActive")), 1l));
			}
		}else {
		if (StringUtils.isNotBlank(quotationSearchDto.getProposalNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("proposalNumber")),
					quotationSearchDto.getProposalNumber().toLowerCase()));
//			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("isActive")), 1l));
		}
		}
		if (quotationSearchDto.getUnitOfficeId() != null && quotationSearchDto.getUnitOfficeId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("unitOfficeId"), quotationSearchDto.getUnitOfficeId()));
		}
		if (quotationSearchDto.getProductId() != null && quotationSearchDto.getProductId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("productId"), quotationSearchDto.getProductId()));
		}
		if (quotationSearchDto.getProductVariantId() != null && quotationSearchDto.getProductVariantId() > 0) {
			predicates
					.add(criteriaBuilder.equal(root.get("productVariantId"), quotationSearchDto.getProductVariantId()));
		}
		if (quotationSearchDto.getBusinessTypeId() != null && quotationSearchDto.getBusinessTypeId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("businessTypeId"), quotationSearchDto.getBusinessTypeId()));
		}
		if (quotationSearchDto.getCustomerCode() != null
				&& StringUtils.isNotBlank(quotationSearchDto.getCustomerCode())) {
			predicates.add(criteriaBuilder.equal(root.get("customerCode"), quotationSearchDto.getCustomerCode()));
		}
		if (quotationSearchDto.getStatusId() != null && quotationSearchDto.getStatusId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("statusId"), quotationSearchDto.getStatusId()));
		}
		/*
		 * if (StringUtils.isNotBlank(quotationSearchDto.getPolicyNumber())) {
		 * predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get(
		 * "policyNumber")), quotationSearchDto.getPolicyNumber().toLowerCase())); }
		 */
//		if (quotationSearchDto.getFromDate() != null && quotationSearchDto.getToDate() != null) {
//			predicates.add(criteriaBuilder.between(root.get("createdDate"), quotationSearchDto.getFromDate(),
//					quotationSearchDto.getToDate()));
//		}

		if (quotationSearchDto.getFromDate() != null && quotationSearchDto.getToDate() != null) {
			Date fromDate = quotationSearchDto.getFromDate();
			Date toDate = quotationSearchDto.getToDate();
			
			fromDate= constructeStartDateTime(fromDate);
			toDate = constructeEndDateTime(toDate);
			Predicate onStart = criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), fromDate);
			Predicate onEnd = criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), toDate);
			predicates.add(onStart);
			predicates.add(onEnd);
		}

//		if (StringUtils.isNotBlank(quotationSearchDto.getFromDate())
//				&& StringUtils.isNotBlank(quotationSearchDto.getToDate())) {
//			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//			try {
//				predicates.add(criteriaBuilder.between(root.get("dateOfCommencement"),
//						dateFormat.parse(quotationSearchDto.getFromDate()),
//						dateFormat.parse(quotationSearchDto.getToDate())));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		}

		if (StringUtils.isNotBlank(quotationSearchDto.getSortOrder())
				&& StringUtils.isNotBlank(quotationSearchDto.getSortBy())) {
			if (quotationSearchDto.getSortOrder().toLowerCase().equals("asc")) {
				createQuery.orderBy(criteriaBuilder.asc(root.get(quotationSearchDto.getSortBy())));
			} else {
				createQuery.orderBy(criteriaBuilder.desc(root.get(quotationSearchDto.getSortBy())));
			}
		} else {
			createQuery.orderBy(criteriaBuilder.asc(root.get("dateOfCommencement")));
		}

		if (quotationSearchDto.getUserType() != null) {
			if (quotationSearchDto.getUserType().equals("UO")) {
				if (quotationSearchDto.getUnitCode() != null) {
					predicates.add(criteriaBuilder.equal(root.get("unitCode"), quotationSearchDto.getUnitCode()));
				}
			}

			if (quotationSearchDto.getUserType().equals("ZO")) {

				String get = quotationSearchDto.getUnitCode().substring(0, 2);
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("unitCode")),
						"%" + get.toLowerCase() + "%"));
			}
		}

		createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
		List<MasterQuotationEntity> entities = new ArrayList<MasterQuotationEntity>();
		entities = entityManager.createQuery(createQuery).getResultList();

		return ApiResponseDto.success(entities.stream().map(QuotationHelper::entityToDto).collect(Collectors.toList()));
	}

	public static Date constructeStartDateTime(Date start) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 00);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		cal.set(Calendar.MILLISECOND, 00);
		return cal.getTime();
	}

	public static Date constructeEndDateTime(Date end) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(end);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 59);
		return cal.getTime();
	}

	public static Date convertStringToDate(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat("dd/MM/yyyy");
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				/***
				 * ParseException
				 */
			}
		}
		return null;
	}

	@Override
	public ApiResponseDto<List<QuotationDto>> filter(QuotationSearchDto quotationSearchDto) {
		if (quotationSearchDto.getTaggedStatusId().equals(78L))
			return this.inprogressFilter(quotationSearchDto);
		else
			return this.existingFilter(quotationSearchDto);
	}

	@Transactional
	@Override
	public ApiResponseDto<QuotationDto> submitForApproval(Long id, String username) {
		QuotationEntity quotationEntity = quotationRepository.findById(id).get();
		quotationEntity.setStatusId(Long.parseLong(submittedForApprovalStatudId));
		quotationEntity.setSubStatusId(Long.parseLong(defaultSubStatusId));
		quotationEntity.setModifiedBy(username);
		quotationEntity.setModifiedDate(new Date());
		TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(quotationEntity.getNumber());
		if(taskallocationentity!=null) {
			taskallocationentity.setTaskStatus(approvedSubStatudId);
			taskAllocationRepository.save(taskallocationentity);
			}
		return ApiResponseDto.success(QuotationHelper.entityToDto(quotationRepository.save(quotationEntity)));
	}

	@Override
	public ApiResponseDto<QuotationDto> sendBackToMaker(Long id, String username) {
		QuotationEntity quotationEntity = quotationRepository.findById(id).get();
		quotationEntity.setSubStatusId(Long.parseLong(sentBackToMakerSubStatudId));
		quotationEntity.setModifiedBy(username);
		quotationEntity.setModifiedDate(new Date());
		TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(quotationEntity.getNumber());
		if(taskallocationentity!=null) {
			taskallocationentity.setTaskStatus(approvedSubStatudId);
			taskAllocationRepository.save(taskallocationentity);
			}
		return ApiResponseDto.success(QuotationHelper.entityToDto(quotationRepository.save(quotationEntity)));
		
	}

	@Transactional
	@Override
	public ApiResponseDto<QuotationDto> approve(Long id, String username) {
		// approve quotation
		QuotationEntity quotationEntity = quotationRepository.findById(id).get();
		quotationEntity.setStatusId(Long.parseLong(approvedStatudId));
		quotationEntity.setSubStatusId(Long.parseLong(approvedSubStatudId));
		quotationEntity.setTaggedStatusID(Long.parseLong(existingaggedStatusId));
		quotationEntity.setModifiedBy(username);
		quotationEntity.setModifiedDate(new Date());
		TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(quotationEntity.getNumber());
		if(taskallocationentity!=null) {
		taskallocationentity.setTaskStatus(approvedSubStatudId);
		taskAllocationRepository.save(taskallocationentity);
		}
		quotationRepository.save(quotationEntity);

		// copy to master tables
		MasterQuotationEntity masterQuotationEntity = copyToMaster(id, username);
		masterQuotationRepository.deactivateOtherQuotations(masterQuotationEntity.getProposalNumber(),
				masterQuotationEntity.getId());

		// create payments to be received against master quotation
		Double lifeCover = 0D;
		Double currentYearLiability = 0D;
		Double pastYearLiabilty = 0D;
		Double futureServiceLiability = 0D;
		List<GratuityCalculationEntity> gratuityCalculationEntities = gratuityCalculationRepository
				.findAllByQuotationId(id);
		for (GratuityCalculationEntity gratuityCalculationEntity : gratuityCalculationEntities) {
			lifeCover = lifeCover
					+ (gratuityCalculationEntity.getLcPremium() != null ? gratuityCalculationEntity.getLcPremium()
							: 0.0D);
			currentYearLiability = currentYearLiability
					+ (gratuityCalculationEntity.getCurrentServiceBenefitDeath() != null
							? gratuityCalculationEntity.getCurrentServiceBenefitDeath()
							: 0.0D)
					+ (gratuityCalculationEntity.getCurrentServiceBenefitRet() != null
							? gratuityCalculationEntity.getCurrentServiceBenefitRet()
							: 0.0D)
					+ (gratuityCalculationEntity.getCurrentServiceBenefitWdl() != null
							? gratuityCalculationEntity.getCurrentServiceBenefitWdl()
							: 0.0D);
			pastYearLiabilty = pastYearLiabilty
					+ +(gratuityCalculationEntity.getPastServiceBenefitDeath() != null
							? gratuityCalculationEntity.getPastServiceBenefitDeath()
							: 0.0D)
					+ (gratuityCalculationEntity.getPastServiceBenefitRet() != null
							? gratuityCalculationEntity.getPastServiceBenefitRet()
							: 0.0D)
					+ (gratuityCalculationEntity.getPastServiceBenefitWdl() != null
							? gratuityCalculationEntity.getPastServiceBenefitWdl()
							: 0.0D);

			futureServiceLiability = futureServiceLiability
					+ (gratuityCalculationEntity.getTerm() != null ? gratuityCalculationEntity.getTerm() : 0.0D);
		}

		Double gst = 0.0D;
		if (quotationEntity.getGstApplicableId().equals(1L)) {
			StandardCodeEntity standardCodeEntity = standardCodeRepository.getById(5L);
			gst = lifeCover * (Float.parseFloat(standardCodeEntity.getValue()) / 100);
		}

		List<QuotationChargeEntity> quotationChargeEntities = new ArrayList<QuotationChargeEntity>();
		quotationChargeEntities.add(QuotationChargeEntity.builder().masterQuotationId(masterQuotationEntity.getId())
				.chargeTypeId(111L).amountCharged(lifeCover).balanceAmount(0).isActive(true).createdBy(username)
				.createdDate(new Date()).build());
		quotationChargeEntities.add(QuotationChargeEntity.builder().masterQuotationId(masterQuotationEntity.getId())
				.chargeTypeId(112L).amountCharged(gst).balanceAmount(0).isActive(true).createdBy(username)
				.createdDate(new Date()).build());
		quotationChargeEntities.add(QuotationChargeEntity.builder().masterQuotationId(masterQuotationEntity.getId())
				.chargeTypeId(113L).amountCharged(pastYearLiabilty).balanceAmount(0).isActive(true).createdBy(username)
				.createdDate(new Date()).build());
		quotationChargeEntities.add(QuotationChargeEntity.builder().masterQuotationId(masterQuotationEntity.getId())
				.chargeTypeId(114L).amountCharged(currentYearLiability).balanceAmount(0).isActive(true)
				.createdBy(username).createdDate(new Date()).build());
		quotationChargeEntities.add(QuotationChargeEntity.builder().masterQuotationId(masterQuotationEntity.getId())
				.chargeTypeId(145L).amountCharged(futureServiceLiability).balanceAmount(0).isActive(true)
				.createdBy(username).createdDate(new Date()).build());
		quotationChargeRepository.saveAll(quotationChargeEntities);

		return ApiResponseDto.success(QuotationHelper.entityToDto(masterQuotationEntity));
	}

	@Transactional
	@Override
	public ApiResponseDto<QuotationDto> reject(Long id, String username, QuotationDto quotationDto) {
		QuotationEntity quotationEntity = quotationRepository.findById(id).get();
		quotationEntity.setStatusId(Long.parseLong(rejectedStatudId));
		quotationEntity.setSubStatusId(Long.parseLong(rejectedSubStatudId));
		quotationEntity.setTaggedStatusID(Long.parseLong(existingaggedStatusId));
		quotationEntity.setRejectedReason(quotationDto.getRejectedReason());
		quotationEntity.setRejectedRemarks(quotationDto.getRejectedRemarks());
		quotationEntity.setModifiedBy(username);
		quotationEntity.setModifiedDate(new Date());
		quotationEntity.setIsActive(false);
		quotationRepository.save(quotationEntity);
		TaskAllocationEntity taskallocationentity=taskAllocationRepository.findByRequestId(quotationEntity.getNumber());
		if(taskallocationentity!=null) {
			taskallocationentity.setTaskStatus(approvedSubStatudId);
			taskAllocationRepository.save(taskallocationentity);
			}
		MasterQuotationEntity masterQuotationEntity = copyToMaster(id, username);
		return ApiResponseDto.success(QuotationHelper.entityToDto(masterQuotationEntity));
	}

	private MasterQuotationEntity copyToMaster(Long id, String username) {
		// Copy to Master Quotation table
		QuotationEntity quotationEntity = quotationRepository.findById(id).get();
		MasterQuotationEntity masterQuotationEntity = masterQuotationRepository
				.save(QuotationHelper.entityToMasterEntity(quotationEntity, id, username));
		masterQuotationRepository.deactivateAllQuotation(masterQuotationEntity.getProposalNumber());
		
		if(masterQuotationEntity.getIsActive()) {
		masterQuotationRepository.activateCurrentQuotationId(masterQuotationEntity.getProposalNumber(),
				masterQuotationEntity.getId());
		}
		// Copy to Master SchmeRule table
		Optional<SchemeRuleEntity> schemeRuleEntity = schemeRuleRepository.findByQuotationId(id);
		if (schemeRuleEntity.isPresent()) {
			MasterSchemeRuleEntity masterSchemeRuleEntity = SchemeRuleHelper
					.entityToMasterEntity(schemeRuleEntity.get());
			masterSchemeRuleEntity.setId(null);
			masterSchemeRuleEntity.setQuotationId(masterQuotationEntity.getId());
			masterSchemeRuleEntity = masterSchemeRuleRepository.save(masterSchemeRuleEntity);
			System.out.println(masterSchemeRuleEntity.getQuotationId());
		}

		// Copy to Master LifeCoverFlat table
		List<LifeCoverEntity> lifeCoverEntities = lifeCoverEntityRepository.findByQuotationId(id);
		if (lifeCoverEntities.size() > 0) {
			List<MasterLifeCoverEntity> masterLifeCoverEntities = new ArrayList<MasterLifeCoverEntity>();
			for (LifeCoverEntity lifeCoverEntity : lifeCoverEntities) {
				MasterLifeCoverEntity masterLifeCoverEntity = LifeCoverHelper.entityToMasterEntity(lifeCoverEntity);
				masterLifeCoverEntity.setId(null);
				masterLifeCoverEntity.setQuotationId(masterQuotationEntity.getId());
				masterLifeCoverEntities.add(masterLifeCoverEntity);
			}
			masterLifeCoverEntityRepository.saveAll(masterLifeCoverEntities);
		}

		// Copy to Master GratuityBenefit table
		List<GratuityBenefitEntity> gratuityBenefitEntities = gratuityBenefitRepository.findByQuotationId(id);
		if (gratuityBenefitEntities.size() > 0) {
			List<MasterGratuityBenefitEntity> masterGratuityBenefitEntities = new ArrayList<MasterGratuityBenefitEntity>();
			for (GratuityBenefitEntity gratuityBenefitEntity : gratuityBenefitEntities) {
				masterGratuityBenefitEntities.add(GratuityBenefitHelper
						.entityToMasterGradtuityBenefitEntity(gratuityBenefitEntity, masterQuotationEntity.getId()));
			}
			masterGratuityBenefitRepository.saveAll(masterGratuityBenefitEntities);
		}

		// Copy to Master Valuation table
		Optional<ValuationEntity> valuationEntity = valuationRepository.findByQuotationId(id);
		if (valuationEntity.isPresent()) {
			MasterValuationEntity masterValuationEntity = ValuationHelper.entityToMasterEntity(valuationEntity.get());
			masterValuationEntity.setId(null);
			masterValuationEntity.setQuotationId(masterQuotationEntity.getId());
			masterValuationRepository.save(masterValuationEntity);
		}

		// Copy to Master Member data
		List<MasterMemberEntity> tt = MemberHelper.entityToMasterEntity(memberRepository.findByQuotationId(id),
				masterQuotationEntity.getId());
		masterMemberRepository.saveAll(tt);
		// MemberCategoryTest
		List<MemberCategoryEntity> addMemberCategoryEntity = new ArrayList<MemberCategoryEntity>();
		List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository.findByqstgQuoationId(id);
		for (MemberCategoryEntity getmemberCategoryEntity : memberCategoryEntity) {
			getmemberCategoryEntity.setQmstQuotationId(masterQuotationEntity.getId());
			addMemberCategoryEntity.add(getmemberCategoryEntity);
		}
		memberCategoryRepository.saveAll(addMemberCategoryEntity);

		// Copy to Master ValuationMatrix table
		Optional<ValuationMatrixEntity> valuationMatrixEntity = valuationMatrixRepository.findByQuotationId(id);
		if (valuationMatrixEntity.isPresent()) {
			MasterValuationMatrixEntity masterValuationMatrixEntity = ValuationMatrixHelper
					.entityToMasterEntity(valuationMatrixEntity.get());
			masterValuationMatrixEntity.setId(null);
			masterValuationMatrixEntity.setQuotationId(masterQuotationEntity.getId());
			masterValuationMatrixRepository.save(masterValuationMatrixEntity);

			// Copy to Master ValuationBasic table
			if (masterValuationMatrixEntity.getValuationTypeId() == 1) {
				Optional<ValuationBasicEntity> valuationBasicEntity = valuationBasicRepository.findByQuotationId(id);
				if (valuationBasicEntity.isPresent()) {
					MasterValuationBasicEntity masterValuationBasicEntity = ValuationMatrixHelper
							.entityToMasterEntity(valuationBasicEntity.get());
					masterValuationBasicEntity.setId(null);
					masterValuationBasicEntity.setQuotationId(masterQuotationEntity.getId());
					masterValuationBasicRepository.save(masterValuationBasicEntity);

					// Copy to Master ValuationWithdrawalRate table
					List<ValuationWithdrawalRateEntity> valuationWithdrawalRateEntities = valuationWithdrawalRateRepository
							.findByQuotationId(id);
					if (valuationWithdrawalRateEntities.size() > 0) {
						List<MasterValuationWithdrawalRateEntity> masterValuationWithdrawalRateEntities = new ArrayList<MasterValuationWithdrawalRateEntity>();
						for (ValuationWithdrawalRateEntity valuationWithdrawalRateEntity : valuationWithdrawalRateEntities) {
							MasterValuationWithdrawalRateEntity masterValuationWithdrawalRateEntity = ValuationMatrixHelper
									.entityToMasterEntity(valuationWithdrawalRateEntity);
							masterValuationWithdrawalRateEntity.setId(null);
							masterValuationWithdrawalRateEntity.setQuotationId(masterQuotationEntity.getId());
							masterValuationWithdrawalRateEntities.add(masterValuationWithdrawalRateEntity);
						}
						masterValuationWithdrawalRateRepository.saveAll(masterValuationWithdrawalRateEntities);
					}
				}
			}
		}

		// TO Master Premium table
		List<PremiumEntity> premiumEntities = premiumRepository.findByQuotationId(id);
		if (premiumEntities.size() > 0) {
			List<MasterPremiumEntity> masterPremiumEntities = new ArrayList<MasterPremiumEntity>();
			for (PremiumEntity premiumEntity : premiumEntities) {
				MasterPremiumEntity masterPremiumEntity = PremiumHelper.entityToMasterEntity(premiumEntity);
				masterPremiumEntity.setId(null);
				masterPremiumEntity.setQuotationId(masterQuotationEntity.getId());
				masterPremiumEntities.add(PremiumHelper.entityToMasterEntity(premiumEntity));
			}
			masterPremiumRepository.saveAll(masterPremiumEntities);
		}

		// Copy to Master Document table
		List<DocumentEntity> documentEntities = documentRepository.findByQuotationId(id);
		if (documentEntities.size() > 0) {
			List<MasterDocumentEntity> masterDocumentEntities = new ArrayList<MasterDocumentEntity>();
			for (DocumentEntity documentEntity : documentEntities) {
				MasterDocumentEntity masterDocumentEntity = DocumentHelper.entityToMasterEntity(documentEntity);
				masterDocumentEntity.setId(null);
				masterDocumentEntity.setQuotationId(masterQuotationEntity.getId());
				masterDocumentEntities.add(masterDocumentEntity);
			}
			masterDocumentRepository.saveAll(masterDocumentEntities);
		}

		// TO Master Notes table
		List<NotesEntity> notesEntities = notesRepository.findByQuotationId(id);
		if (notesEntities.size() > 0) {
			List<MasterNotesEntity> masterNotesEntities = new ArrayList<MasterNotesEntity>();
			for (NotesEntity notesEntity : notesEntities) {
				MasterNotesEntity masterNotesEntity = NotesHelper.entityToMasterEntity(notesEntity);
				masterNotesEntity.setId(null);
				masterNotesEntity.setQuotationId(masterQuotationEntity.getId());
				masterNotesEntities.add(masterNotesEntity);
			}
			masterNotesRepository.saveAll(masterNotesEntities);
		}

		return masterQuotationEntity;
	}

	@Transactional
	@Override
	public ApiResponseDto<QuotationDto> clone(Long id, String type, String username) {
		QuotationEntity newQuotationEntity = null;
		QuotationEntity newMasterQuotationEntity = null;
		
		if (type.equals("EXISTING")) {
			Optional<MasterQuotationEntity> masterQuotationEntity = masterQuotationRepository.findById(id);
			if(masterQuotationEntity.isPresent())
				 id=masterQuotationEntity.get().getTempQuotationId();
			
			if(masterQuotationEntity.get().getStatusId().toString() ==rejectedStatudId) {
				masterQuotationEntity.get().setIsActive(false);
				masterQuotationRepository.save(masterQuotationEntity.get());
			}
			// quotation
//			Long nextQuotationNumber = QuotationHelper.nextQuotationNumber(quotationRepository.maxQuotationNumber());

			Optional<QuotationEntity> quotationEntity = quotationRepository.findById(id);

			List<String> getListNumer = quotationRepository
					.addQuotationNumber(quotationEntity.get().getProposalNumber());
			String getQuotationNumber = null;
			if (getListNumer.size() > 0) {
				getQuotationNumber = QuotationHelper.addQuotationNumber(quotationEntity.get().getProposalNumber(),
						getListNumer.get(0));
			} else {
				getQuotationNumber = quotationEntity.get().getProposalNumber() + "01";
			}
			if (!quotationEntity.isPresent())
				return ApiResponseDto.notFound(null);

			newMasterQuotationEntity = QuotationHelper.clone(quotationEntity.get(), getQuotationNumber, username,
					Long.parseLong(defaultStatusId), Long.parseLong(defaultSubStatusId),
					Long.parseLong(defaultTaggedStatusId));
			newMasterQuotationEntity = quotationRepository.save(newMasterQuotationEntity);

			// scheme rule
			Optional<SchemeRuleEntity> schemeRuleEntity = schemeRuleRepository.findByQuotationId(id);
			if (schemeRuleEntity.isPresent()) {
				SchemeRuleEntity newSchemeRuleEntity = SchemeRuleHelper.clone(schemeRuleEntity.get(), username,
						newMasterQuotationEntity.getId());
				schemeRuleRepository.save(newSchemeRuleEntity);
			}

			// MemberCategory
			List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository.findByqstgQuoationId(id);
			for (MemberCategoryEntity getMemberCategoryEntity : memberCategoryEntity) {
				MemberCategoryEntity newMemberCategory = MemberCategoryHelper.clone(getMemberCategoryEntity, username,
						newMasterQuotationEntity.getId());
				memberCategoryRepository.save(newMemberCategory);
			}

			// life coverage
			List<MemberCategoryEntity> memberCategoryClone = memberCategoryRepository
					.findByqstgQuoationId(newQuotationEntity.getId());
			List<LifeCoverEntity> lifeCoverEntities = lifeCoverEntityRepository.findByQuotationId(id);
			List<LifeCoverEntity> listLifeCoverEntities = new ArrayList<LifeCoverEntity>();
			for (MemberCategoryEntity getNewMemberCategory : memberCategoryClone) {

				for (MemberCategoryEntity getOldMemberCategory : memberCategoryEntity) {

					for (LifeCoverEntity getLifeCoverEntity : lifeCoverEntities) {

						if (getOldMemberCategory.getName().equals(getNewMemberCategory.getName()) && getLifeCoverEntity.getIsActive() !=false) {
							LifeCoverEntity newLifeCoverEntity = LifeCoverFlatHelper.clone(getLifeCoverEntity, username,
									newQuotationEntity.getId());
							newLifeCoverEntity.setCategoryId(getNewMemberCategory.getId());
							getLifeCoverEntity.setIsActive(false);
						
							listLifeCoverEntities.add(newLifeCoverEntity);
							break;
						}

					}

				}

			}
			listLifeCoverEntities=lifeCoverEntityRepository.saveAll(listLifeCoverEntities);			
			lifeCoverEntityRepository.updateisactive(id);

			// gratuity benefit
						List<GratuityBenefitEntity> GratuityBenefitEntitiesClone = new ArrayList<GratuityBenefitEntity>();
						List<GratuityBenefitEntity> gratuityBenefitEntity = gratuityBenefitRepository.findByQuotationId(id);
						for (MemberCategoryEntity getNewMemberCategory : memberCategoryClone) {
							for (MemberCategoryEntity getOldMemberCategory : memberCategoryEntity) {
								for (GratuityBenefitEntity getGratuityBenefitEntity : gratuityBenefitEntity) {

									if (getOldMemberCategory.getName().equals(getNewMemberCategory.getName()) && getGratuityBenefitEntity.getIsActive() !=false) {
										GratuityBenefitEntity newGratuityBenefitEntity = GratuityBenefitHelper
												.clone(getGratuityBenefitEntity, username, newQuotationEntity.getId());
										newGratuityBenefitEntity.setCategoryId(getNewMemberCategory.getId());
										getGratuityBenefitEntity.setIsActive(false);
										GratuityBenefitEntitiesClone.add(newGratuityBenefitEntity);
										break;
									}

								}
							}

						}
						GratuityBenefitEntitiesClone=	gratuityBenefitRepository.saveAll(GratuityBenefitEntitiesClone);
						gratuityBenefitRepository.updateisactive(id);
					
			// valuation 
			Optional<ValuationEntity> valuationEntity = valuationRepository.findByQuotationId(id);
			if (valuationEntity.isPresent()) {
				ValuationEntity newValuationEntity = ValuationHelper.clonestaging(valuationEntity.get(), username,
						newMasterQuotationEntity.getId());
				valuationRepository.save(newValuationEntity);
			}
			
		}
		if (type.equals("INPROGRESS")) {
			// quotation
//			Long nextQuotationNumber = QuotationHelper.nextQuotationNumber(quotationRepository.maxQuotationNumber());

			Optional<QuotationEntity> quotationEntity = quotationRepository.findById(id);

			List<String> getListNumer = quotationRepository
					.addQuotationNumber(quotationEntity.get().getProposalNumber());
			String getQuotationNumber = null;
			if (getListNumer.size() > 0) {
				getQuotationNumber = QuotationHelper.addQuotationNumber(quotationEntity.get().getProposalNumber(),
						getListNumer.get(0));
			} else {
				getQuotationNumber = quotationEntity.get().getProposalNumber() + "01";
			}
			if (!quotationEntity.isPresent())
				return ApiResponseDto.notFound(null);

			newQuotationEntity = QuotationHelper.clone(quotationEntity.get(), getQuotationNumber, username,
					Long.parseLong(defaultStatusId), Long.parseLong(defaultSubStatusId),
					Long.parseLong(defaultTaggedStatusId));
			newQuotationEntity = quotationRepository.save(newQuotationEntity);

			// scheme rule
			Optional<SchemeRuleEntity> schemeRuleEntity = schemeRuleRepository.findByQuotationId(id);
			if (schemeRuleEntity.isPresent()) {
				SchemeRuleEntity newSchemeRuleEntity = SchemeRuleHelper.clone(schemeRuleEntity.get(), username,
						newQuotationEntity.getId());
				schemeRuleRepository.save(newSchemeRuleEntity);
			}

			// MemberCategory
			List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository.findByqstgQuoationId(id);
			for (MemberCategoryEntity getMemberCategoryEntity : memberCategoryEntity) {
				MemberCategoryEntity newMemberCategory = MemberCategoryHelper.clone(getMemberCategoryEntity, username,
						newQuotationEntity.getId());
				memberCategoryRepository.save(newMemberCategory);
			}

			// life coverage
			List<MemberCategoryEntity> memberCategoryClone = memberCategoryRepository
					.findByqstgQuoationId(newQuotationEntity.getId());
			List<LifeCoverEntity> lifeCoverEntities = lifeCoverEntityRepository.findByQuotationId(id);
			List<LifeCoverEntity> listLifeCoverEntities = new ArrayList<LifeCoverEntity>();
			for (MemberCategoryEntity getNewMemberCategory : memberCategoryClone) {

				for (MemberCategoryEntity getOldMemberCategory : memberCategoryEntity) {

					for (LifeCoverEntity getLifeCoverEntity : lifeCoverEntities) {

						if (getOldMemberCategory.getName().equals(getNewMemberCategory.getName()) && getLifeCoverEntity.getIsActive() !=false) {
							LifeCoverEntity newLifeCoverEntity = LifeCoverFlatHelper.clone(getLifeCoverEntity, username,
									newQuotationEntity.getId());
							newLifeCoverEntity.setCategoryId(getNewMemberCategory.getId());
							getLifeCoverEntity.setIsActive(false);
						
							listLifeCoverEntities.add(newLifeCoverEntity);
							break;
						}

					}

				}

			}
			listLifeCoverEntities=lifeCoverEntityRepository.saveAll(listLifeCoverEntities);			
			lifeCoverEntityRepository.updateisactive(id);
			
			// gratuity benefit
			List<GratuityBenefitEntity> GratuityBenefitEntitiesClone = new ArrayList<GratuityBenefitEntity>();
			List<GratuityBenefitEntity> gratuityBenefitEntity = gratuityBenefitRepository.findByQuotationId(id);
			for (MemberCategoryEntity getNewMemberCategory : memberCategoryClone) {
				for (MemberCategoryEntity getOldMemberCategory : memberCategoryEntity) {
					for (GratuityBenefitEntity getGratuityBenefitEntity : gratuityBenefitEntity) {

						if (getOldMemberCategory.getName().equals(getNewMemberCategory.getName()) && getGratuityBenefitEntity.getIsActive() !=false) {
							GratuityBenefitEntity newGratuityBenefitEntity = GratuityBenefitHelper
									.clone(getGratuityBenefitEntity, username, newQuotationEntity.getId());
							newGratuityBenefitEntity.setCategoryId(getNewMemberCategory.getId());
							getGratuityBenefitEntity.setIsActive(false);
							GratuityBenefitEntitiesClone.add(newGratuityBenefitEntity);
							break;
						}

					}
				}

			}
			GratuityBenefitEntitiesClone=	gratuityBenefitRepository.saveAll(GratuityBenefitEntitiesClone);
			gratuityBenefitRepository.updateisactive(id);
		
			// valuation 
			Optional<ValuationEntity> valuationEntity = valuationRepository.findByQuotationId(id);
			if (valuationEntity.isPresent()) {
				ValuationEntity newValuationEntity = ValuationHelper.clonestaging(valuationEntity.get(), username,
						newQuotationEntity.getId());
				valuationRepository.save(newValuationEntity);
			}
//			// valuation matrix
//			Optional<ValuationMatrixEntity> valuationMatrixEntity = valuationMatrixRepository.findByQuotationId(id);
//			if (valuationMatrixEntity.isPresent()) {
//				ValuationMatrixEntity newValuationMatrixEntity = ValuationMatrixHelper
//						.clone(valuationMatrixEntity.get(), username, newQuotationEntity.getId());
//				valuationMatrixRepository.save(newValuationMatrixEntity);
//
//				if (newValuationMatrixEntity.getValuationTypeId() == 1) {
//					Optional<ValuationBasicEntity> valuationBasicEntity = valuationBasicRepository
//							.findByQuotationId(id);
//					if (valuationBasicEntity.isPresent()) {
//						ValuationBasicEntity newValuationBasicEntity = ValuationMatrixHelper
//								.clonebasic(valuationBasicEntity.get(), username, newQuotationEntity.getId());
//						valuationBasicRepository.save(newValuationBasicEntity);
//
//						List<ValuationWithdrawalRateEntity> valuationWithdrawalRateEntity = valuationWithdrawalRateRepository
//								.findByQuotationId(id);
//						for (ValuationWithdrawalRateEntity getValuationWithdrawalRateEntity : valuationWithdrawalRateEntity) {
//							ValuationWithdrawalRateEntity newValuationWithdrawalRateEntity = ValuationMatrixHelper
//									.clone(getValuationWithdrawalRateEntity, username, newQuotationEntity.getId());
//							valuationWithdrawalRateRepository.save(newValuationWithdrawalRateEntity);
//						}
//					}
//				}
//			}
//
//			// member
//			List<MemberEntity> cloneMemberEntitiesAll = new ArrayList<MemberEntity>();
//			List<MemberEntity> memberEntities = memberRepository.findByQuotationId(id);
//			for (MemberEntity getMemberEntity : memberEntities) {
//				MemberEntity cloneMemberEntities = MemberHelper.clone(getMemberEntity, username,
//						newQuotationEntity.getId());
//				cloneMemberEntitiesAll.add(cloneMemberEntities);
//			}
//			memberRepository.saveAll(cloneMemberEntitiesAll);

		}

//		if (type.equals("EXISTING")) {
////			Long nextQuotationNumber = QuotationHelper
////					.nextQuotationNumber(masterQuotationRepository.maxQuotationNumber());
//
//			// MasterQuotation
//			Optional<MasterQuotationEntity> masterQuotationEntity = masterQuotationRepository.findById(id);
//			List<String> getListNumer = masterQuotationRepository.addQuotationNumber(masterQuotationEntity.get().getProposalNumber());
//			String getQuotationNumber = null;
//			if (getListNumer.size() > 0) {
//				getQuotationNumber = QuotationHelper.addQuotationNumber(masterQuotationEntity.get().getProposalNumber(), getListNumer.get(0));
//			} else {
//				getQuotationNumber = masterQuotationEntity.get().getProposalNumber() + "01";
//			}
//			if (!masterQuotationEntity.isPresent())
//				return ApiResponseDto.notFound(null);
//
//			newMasterQuotationEntity = QuotationHelper.cloneMaster(masterQuotationEntity.get(),
//					getQuotationNumber, username, Long.parseLong(defaultStatusId),
//					Long.parseLong(defaultSubStatusId), Long.parseLong(defaultTaggedStatusId));
//			masterQuotationRepository.save(newMasterQuotationEntity);
//
//			// MasterScheme
//			MasterSchemeRuleEntity masterSchemeRuleEntity = masterSchemeRuleRepository.findByQuotationId(id);
//			if (masterSchemeRuleEntity != null) {
//				MasterSchemeRuleEntity newMasterSchemeRuleEntity = SchemeRuleHelper.masterClone(masterSchemeRuleEntity,
//						username, newMasterQuotationEntity.getId());
//				masterSchemeRuleRepository.save(newMasterSchemeRuleEntity);
//			}
//
//			// Master Life Cover rule
//			List<MasterLifeCoverEntity> newMasterLifeCoverEntities = new ArrayList<MasterLifeCoverEntity>();
//			List<MasterLifeCoverEntity> masterLifeCoverEntities = masterLifeCoverEntityRepository.findByQuotationId(id);
//			for (MasterLifeCoverEntity masterLifeCoverEntity : masterLifeCoverEntities) {
//				MasterLifeCoverEntity newMasterLifeCoverEntity = LifeCoverFlatHelper.masterClone(masterLifeCoverEntity,
//						username, newMasterQuotationEntity.getId());
//				newMasterLifeCoverEntities.add(newMasterLifeCoverEntity);
//			}
//			masterLifeCoverEntityRepository.saveAll(newMasterLifeCoverEntities);
//
//			// Master Gratuity
//			List<MasterGratuityBenefitEntity> masterGratuityBenefitEntityClone = new ArrayList<MasterGratuityBenefitEntity>();
//			List<MasterGratuityBenefitEntity> masterGratuityBenefitEntity = masterGratuityBenefitRepository
//					.findByQuotationId(id);
//			for (MasterGratuityBenefitEntity getMasterGratuityBenefitEntity : masterGratuityBenefitEntity) {
//				MasterGratuityBenefitEntity newMasterGratuityBenefitEntity = GratuityBenefitHelper
//						.masterClone(getMasterGratuityBenefitEntity, username, newMasterQuotationEntity.getId());
//				masterGratuityBenefitEntityClone.add(newMasterGratuityBenefitEntity);
//			}
//			masterGratuityBenefitRepository.saveAll(masterGratuityBenefitEntityClone);
//
//			// Master valuation Entity
//
//			Optional<MasterValuationEntity> masterValuationEntity = masterValuationRepository.findByQuotationId(id);
//			if (masterValuationEntity.isPresent()) {
//				MasterValuationEntity newMasterValuationEntity = ValuationHelper.clone(masterValuationEntity.get(),
//						username, newMasterQuotationEntity.getId());
//				masterValuationRepository.save(newMasterValuationEntity);
//			}
////			// Master Valuation Matrix
////			Optional<MasterValuationMatrixEntity> masterValuationMastrixEntity = masterValuationMatrixRepository
////					.findByQuotationId(id);
////			if (masterValuationMastrixEntity.isPresent()) {
////				MasterValuationMatrixEntity newMasterValuationMatrixEntity = ValuationMatrixHelper
////						.masterClone(masterValuationMastrixEntity.get(), username, newMasterQuotationEntity.getId());
////				masterValuationMatrixRepository.save(newMasterValuationMatrixEntity);
////				if (newMasterValuationMatrixEntity.getValuationTypeId() == 1) {
////
////					Optional<MasterValuationBasicEntity> masterValuationBasicEntity = masterValuationBasicRepository
////							.findByQuotationId(id);
////					if (masterValuationBasicEntity.isPresent()) {
////						MasterValuationBasicEntity newMasterValuationBasicEntity = ValuationMatrixHelper.masterClone(
////								masterValuationBasicEntity.get(), username, newMasterQuotationEntity.getId());
////
////						masterValuationBasicRepository.save(newMasterValuationBasicEntity);
////
////						List<MasterValuationWithdrawalRateEntity> masterValuationWithdrawalRateEntity = masterValuationWithdrawalRateRepository
////								.findByQuotationId(id);
////						for (MasterValuationWithdrawalRateEntity getMasterValuationWithdrawalRateEntity : masterValuationWithdrawalRateEntity) {
////							MasterValuationWithdrawalRateEntity newMasterValuationWithdrawalRateEntity = ValuationMatrixHelper
////									.masterClone(getMasterValuationWithdrawalRateEntity, username,
////											newMasterQuotationEntity.getId());
////							masterValuationWithdrawalRateRepository.save(newMasterValuationWithdrawalRateEntity);
////						}
////					}
////				}
////			}
////
////			// Master Member Entity
////			List<MasterMemberEntity> listMasterMemberEntity = new ArrayList<MasterMemberEntity>();
////			List<MasterMemberEntity> masterMemberEntity = masterMemberRepository.findByQuotationId(id);
////			for (MasterMemberEntity getMasterMemberEntity : masterMemberEntity) {
////				MasterMemberEntity newMasterMemberEntity = MemberHelper.masterClone(getMasterMemberEntity, username,
////						newMasterQuotationEntity.getId());
////				listMasterMemberEntity.add(newMasterMemberEntity);
////			}
////			masterMemberRepository.saveAll(listMasterMemberEntity);
//		}

		if (newQuotationEntity == null && newMasterQuotationEntity == null)
			return ApiResponseDto.notFound(null);
		else if (type.equals("INPROGRESS"))
			return ApiResponseDto.success(QuotationHelper.entityToDto(newQuotationEntity));
		else
			return ApiResponseDto.success(QuotationHelper.entityToDto(newMasterQuotationEntity));

	}

	@Override
	public ApiResponseDto<QuotationDto> escalateToCo(Long id, String username) {

		QuotationEntity quotationEntity = quotationRepository.findById(id).get();
		quotationEntity.setIsEscalatedToCO(true);
		quotationEntity.setModifiedBy(username);
		quotationEntity.setModifiedDate(new Date());
		return ApiResponseDto.success(QuotationHelper.entityToDto(quotationRepository.save(quotationEntity)));
	}

	@Override
	public ApiResponseDto<QuotationDto> escalateToZo(Long id, String username) {

		QuotationEntity quotationEntity = quotationRepository.findById(id).get();
		quotationEntity.setIsEscalatedToZO(true);
		quotationEntity.setModifiedBy(username);
		quotationEntity.setModifiedDate(new Date());
		return ApiResponseDto.success(QuotationHelper.entityToDto(quotationRepository.save(quotationEntity)));
	}

	@Override
	public ApiResponseDto<QuotationDto> saveConsent(Long id, QuotationDto quotationDto) {

		MasterQuotationEntity masterQuotationEntity = masterQuotationRepository.findById(id).get();
		masterQuotationEntity.setConsentReceived(quotationDto.getConsentReceived());
		masterQuotationEntity.setConsentReasonId(quotationDto.getConsentReasonId());
		masterQuotationEntity.setConsentRemarks(quotationDto.getConsentRemarks());
		masterQuotationEntity.setModifiedBy(quotationDto.getModifiedBy());
		masterQuotationEntity.setModifiedDate(new Date());
		return ApiResponseDto
				.success(QuotationHelper.entityToDto(masterQuotationRepository.save(masterQuotationEntity)));
	}
	@Transactional
	@Override
	public ApiResponseDto<QuotationDto> activateMasterQuotation(Long id) {
		MasterQuotationEntity masterQuotationEntity = masterQuotationRepository.findById(id).get();

	masterQuotationRepository.deactivateOtherQuotations(masterQuotationEntity.getProposalNumber(),id);
//	masterQuotationRepository.activateCurrentQuotationId(masterQuotationEntity.getProposalNumber(),
//			id);
	masterQuotationEntity.setIsActive(true);
		masterQuotationEntity=masterQuotationRepository.save(masterQuotationEntity);	
		QuotationDto newQuotationDto = new ModelMapper().map(masterQuotationEntity, QuotationDto.class);
		return ApiResponseDto.success(newQuotationDto);
	}

	private String policyBondReport(Long quotationId) {

		String reportBody = "<p>Life Insurance Corporation of India Pension and Group Schemes Department</p>";

		return reportBody.replace("\\", "").replaceAll("\t", "");
	}

// karthi -- start

	@Override
	public ApiResponseDto<List<GenerateCBSheetPdfDto>> getcbsheetpdf(Long quotationId, Long taggedStatusId) {

		List<GenerateCBSheetPdfDto> generateCBSheetPdfDto = new ArrayList<GenerateCBSheetPdfDto>();
		List<Object[]> getcbsheet = masterMemberRepository.findByQuotationId2(quotationId);

		generateCBSheetPdfDto = QuotationHelper.getcbObjtoDto(getcbsheet);
		System.out.println("" + generateCBSheetPdfDto);

		return ApiResponseDto.success(generateCBSheetPdfDto);

	}

	@Transactional
	@Override
	public void deleteStaingDataByProposalNumber(String proposalNumber) {
		documentRepository.deleteByProposalNumber(proposalNumber);
		gratuityBenefitPropsRepository.deleteByProposalNumber(proposalNumber);
		gratuityBenefitRepository.deleteByProposalNumber(proposalNumber);
		lifeCoverEntityRepository.deleteByProposalNumber(proposalNumber);
		notesRepository.deleteByProposalNumber(proposalNumber);
		premiumRepository.deleteByProposalNumber(proposalNumber);
		schemeRuleRepository.deleteByProposalNumber(proposalNumber);
		valuationRepository.deleteByProposalNumber(proposalNumber);
		valuationBasicRepository.deleteByProposalNumber(proposalNumber);
		valuationMatrixRepository.deleteByProposalNumber(proposalNumber);
		valuationWithdrawalRateRepository.deleteByProposalNumber(proposalNumber);

		memberErrorRepository.deleteByProposalNumber(proposalNumber);
		memberBatchRepository.deleteByProposalNumber(proposalNumber);
		memberBatchFileRepository.deleteByProposalNumber(proposalNumber);
		memberBulkStgRepository.deleteByProposalNumber(proposalNumber);
		memberAddressRepository.deleteByProposalNumber(proposalNumber);
		;
		memberBankAccountRepository.deleteByProposalNumber(proposalNumber);
		;
		memberAppointeeRepository.deleteByProposalNumber(proposalNumber);
		memberNomineeRepository.deleteByProposalNumber(proposalNumber);
		;
		memberRepository.deleteByProposalNumber(proposalNumber);
		memberCategoryRepository.deleteByProposalNumber(proposalNumber);
		memberCategoryRepository.unsetQuotationStaing(proposalNumber);
		quotationRepository.deleteByProposalNumber(proposalNumber);
	}

	@Override
	public Long getQuotationSubStatus(Long subStatusId) {
	
		Long count=quotationRepository.getCountQuotationSubStatus(subStatusId);
		
		return count;
	}
}

// karthi --end
