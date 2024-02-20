package com.lic.epgs.gratuity.quotation.member.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.yarn.webapp.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lic.epgs.gratuity.common.constants.HttpConstants;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberEntity;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberRepository;
import com.lic.epgs.gratuity.quotation.entity.MasterQuotationEntity;
import com.lic.epgs.gratuity.quotation.entity.QuotationEntity;
import com.lic.epgs.gratuity.quotation.member.dto.MemberAddressDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberAppointeeDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBankAccountDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBatchDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBulkResponseDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberNomineeDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberSearchDto;
import com.lic.epgs.gratuity.quotation.member.entity.MasterMemberAppointeeEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MasterMemberEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberAddressEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberAppointeeEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberBankAccount;
import com.lic.epgs.gratuity.quotation.member.entity.MemberBatchEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberBulkErrorEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberNomineeEntity;
import com.lic.epgs.gratuity.quotation.member.helper.BulkMemberUploadHelper;
import com.lic.epgs.gratuity.quotation.member.helper.MemberErrorWorkbookHelper;
import com.lic.epgs.gratuity.quotation.member.helper.MemberHelper;
import com.lic.epgs.gratuity.quotation.member.repository.MasterMemberRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberAddressRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberAppointeeRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberBankAccountRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberBatchFileRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberBatchRepository;
import com.lic.epgs.gratuity.quotation.member.service.MemberService;
import com.lic.epgs.gratuity.quotation.premium.dto.GratuityCalculationDto;
import com.lic.epgs.gratuity.quotation.premium.repository.GratuityCalculationRepository;
import com.lic.epgs.gratuity.quotation.repository.MasterQuotationRepository;
import com.lic.epgs.gratuity.quotation.repository.QuotationRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberBulkStgRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberErrorRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberNomineeRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberRepository;

/**
 * @author Gopi
 *
 */

@Service
public class MemberServiceImpl implements MemberService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private MemberBulkStgRepository memberBulkStgRepository;

	@Autowired
	private MemberBatchRepository memberBatchRepository;

	@Autowired
	private MemberBatchFileRepository memberBatchFileRepository;

	@Autowired
	private BulkMemberUploadHelper memberHelper;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private QuotationRepository quotationRepository;

	@Autowired
	private MasterQuotationRepository masterQuotationRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private GratuityCalculationRepository gratuityCalculationRepository;

	@Autowired
	private MemberErrorRepository memberErrorRepository;

	@Autowired
	private MemberAddressRepository memberAddressRepository;

	@Autowired
	private MemberAppointeeRepository memberAppointeeRepository;

	@Autowired
	private MemberBankAccountRepository memberBankAccountRepository;

	@Autowired
	private MemberNomineeRepository memberNomineeRepository;

	@Autowired
	private MasterMemberRepository masterMemberRepository;
	
	@Autowired
	private PolicyMemberRepository policyMemberRepository;
	
	@Autowired
	private TempMemberRepository tempMemberRepository;
	
	@Autowired
	private StandardCodeRepository standardCodeRepository;

	@Override
	public ApiResponseDto<MemberBulkResponseDto> getUploadedMemberData(final Long quotationId, String type,Long tmpPolicyId) {
		MemberBulkResponseDto response = new MemberBulkResponseDto();
		MasterQuotationEntity masterQuotationEntity=null;
		String quotationnumber = null;
		if (type.equals("INPROGRESS")) {
			if(quotationId != 0) {
			QuotationEntity quotationEntity = quotationRepository.findById(quotationId)
					.orElseThrow(() -> new NotFoundException("Quotation Id not exists."));
			quotationnumber = quotationEntity.getNumber();
			}
		} else {
			if(quotationId != 0) {
			 masterQuotationEntity = masterQuotationRepository.findById(quotationId)
					.orElseThrow(() -> new NotFoundException("Quotation Id not exists."));
			quotationnumber = masterQuotationEntity.getNumber();
			}
		}
		if(quotationId != 0) {
			List<MemberBatchEntity> tt;
			if (type.equals("INPROGRESS")) {
					tt	= memberBatchRepository.findByQuotationId(quotationId);
			}else {
				tt	= memberBatchRepository.findByQuotationId(masterQuotationEntity.getTempQuotationId());
			}
			

		if (tt.size() > 0) {
			response.setQuotationId(quotationId);
			response.setTransactionStatus(HttpConstants.STATUS);
			response.setTransactionMessage(HttpConstants.FETCH);
			response.setErrors(null);
			if (type.equals("INPROGRESS")) {
			response.setMemberImportCount(memberBatchRepository.memberImportCount(quotationId).toString());
			}else {
				response.setMemberImportCount(memberBatchRepository.memberImportCountmasterquotation(quotationId).toString());
			}
			response.setBatchId(tt.get(0).getBatchId());
			response.setQuotationNo(quotationnumber);
			response.setSuccessCount(tt.get(0).getSuccessCount());
			response.setFailedCount(tt.get(0).getFailedCount());
			response.setTotalCount(tt.get(0).getTotalCount());
			response.setFileName(tt.get(0).getFileName());
			response.setCreatedBy(tt.get(0).getCreatedBy());
			response.setCreatedOn(tt.get(0).getCreatedDate());

			return ApiResponseDto.success(response);
		}
		}else {
			List<MemberBatchEntity> tt = memberBatchRepository.findByTmpPolicyId(tmpPolicyId);

			if (tt.size() > 0) {
				response.setTmpPolicyId(tmpPolicyId);
				response.setTransactionStatus(HttpConstants.STATUS);
				response.setTransactionMessage(HttpConstants.FETCH);
				response.setErrors(null);
				response.setMemberImportCount(memberBatchRepository.renewalMemberImportCount(tmpPolicyId).toString());
				response.setBatchId(tt.get(0).getBatchId());
				response.setQuotationNo(quotationnumber);
				response.setSuccessCount(tt.get(0).getSuccessCount());
				response.setFailedCount(tt.get(0).getFailedCount());
				response.setTotalCount(tt.get(0).getTotalCount());
				response.setFileName(tt.get(0).getFileName());
				response.setCreatedBy(tt.get(0).getCreatedBy());
				response.setCreatedOn(tt.get(0).getCreatedDate());

				return ApiResponseDto.success(response);
			}
		}

		return ApiResponseDto.notFound(null);
	}


	

	@Transactional
	@Override
	public ApiResponseDto<MemberBulkResponseDto> importMemberData(Long quotationId, Long batchId, String username) {
		logger.info("IMPORTING");
		
		memberHelper.saveMembersFromStg(quotationId, batchId, username);
		return ApiResponseDto.success(new MemberBulkResponseDto());
	}

	@Override
	public ApiResponseDto<List<MemberDto>> findAll(Long quotationId) {
		List<MemberEntity> entities = memberRepository.findbyquotation(quotationId);
		
		return ApiResponseDto.success(entities.stream().map(this::entityToDto).collect(Collectors.toList()));
	}

	public MemberDto entityToDto(MemberEntity entity) {
		return new ModelMapper().map(entity, MemberDto.class);
	}

	@Override
	public ApiResponseDto<List<MemberDto>> filter(MemberSearchDto memberSearchDto) {
		if (memberSearchDto.getTaggedStatusId().equals(78L))
			return this.inprogressFilter(memberSearchDto);
		else
			return this.existingFilter(memberSearchDto);

	}

	private ApiResponseDto<List<MemberDto>> existingFilter(MemberSearchDto memberSearchDto) {
		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MasterMemberEntity> createQuery = criteriaBuilder.createQuery(MasterMemberEntity.class);
		Root<MasterMemberEntity> root = createQuery.from(MasterMemberEntity.class);

		predicates.add(criteriaBuilder.equal(root.get("quotationId"), memberSearchDto.getQuotationId()));
//		if (StringUtils.isNotBlank(memberSearchDto.getLicId())) {
//			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("licId")),
//					memberSearchDto.getLicId().toLowerCase()));
//		}
		if (StringUtils.isNotBlank(memberSearchDto.getEmployeeCode())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("employeeCode")),
					memberSearchDto.getEmployeeCode().toLowerCase()));
		}
		if (StringUtils.isNotBlank(memberSearchDto.getName())) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
					"%" + memberSearchDto.getName().toLowerCase() + "%"));
		}
		/*
		 * if (StringUtils.isNotBlank(memberSearchDto.getProposalNumber())) {
		 * predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("code")),
		 * memberSearchDto.getProposalNumber().toLowerCase())); }
		 */
		if (StringUtils.isNotBlank(memberSearchDto.getLoanNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("loanNumber")),
					memberSearchDto.getLoanNumber().toLowerCase()));
		}
		if (StringUtils.isNotBlank(memberSearchDto.getPanNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("panNumber")),
					memberSearchDto.getPanNumber().toLowerCase()));
		}
		if (StringUtils.isNotBlank(memberSearchDto.getAadharNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("aadharNumber")),
					memberSearchDto.getAadharNumber().toLowerCase()));
		}
		/*
		 * if (StringUtils.isNotBlank(memberSearchDto.getStatus())) {
		 * predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")
		 * ), memberSearchDto.getStatus().toLowerCase())); }
		 */

		createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
		List<MasterMemberEntity> entities = new ArrayList<MasterMemberEntity>();
		entities = entityManager.createQuery(createQuery).getResultList();

		GratuityCalculationDto gratuityCalculationDto = new GratuityCalculationDto();
//		List<MemberDto> members = MemberHelper.fromMasterEntities(entities);
//		for (MemberDto mdto : members) {
//			mdto.setGratuityCalculationDto(
//					gratuityCalculationDto.entityToDto(gratuityCalculationRepository.findById(mdto.getGratCalculationId()).get()));
//		}

		return ApiResponseDto.success(MemberHelper.fromMasterEntities(entities));
	}

	private ApiResponseDto<List<MemberDto>> inprogressFilter(MemberSearchDto memberSearchDto) {
		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MemberEntity> createQuery = criteriaBuilder.createQuery(MemberEntity.class);
		Root<MemberEntity> root = createQuery.from(MemberEntity.class);

		predicates.add(criteriaBuilder.equal(root.get("quotationId"), memberSearchDto.getQuotationId()));
//		if (StringUtils.isNotBlank(memberSearchDto.getLicId())) {
//			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("licId")),
//					memberSearchDto.getLicId().toLowerCase()));
//		}
		if (StringUtils.isNotBlank(memberSearchDto.getEmployeeCode())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("employeeCode")),
					memberSearchDto.getEmployeeCode().toLowerCase()));
		}
		if (StringUtils.isNotBlank(memberSearchDto.getName())) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
					"%" + memberSearchDto.getName().toLowerCase() + "%"));
		}
		/*
		 * if (StringUtils.isNotBlank(memberSearchDto.getProposalNumber())) {
		 * predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("code")),
		 * memberSearchDto.getProposalNumber().toLowerCase())); }
		 */
		if (StringUtils.isNotBlank(memberSearchDto.getLoanNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("loanNumber")),
					memberSearchDto.getLoanNumber().toLowerCase()));
		}
		if (StringUtils.isNotBlank(memberSearchDto.getPanNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("panNumber")),
					memberSearchDto.getPanNumber().toLowerCase()));
		}
		if (StringUtils.isNotBlank(memberSearchDto.getAadharNumber())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("aadharNumber")),
					memberSearchDto.getAadharNumber().toLowerCase()));
		}
		if (memberSearchDto.getStatusId() != null) {
			predicates.add(criteriaBuilder.equal((root.get("statusId")),
					memberSearchDto.getStatusId()));
		}
		
		/*
		 * if (StringUtils.isNotBlank(memberSearchDto.getStatus())) {
		 * predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")
		 * ), memberSearchDto.getStatus().toLowerCase())); }
		 */

		createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
		List<MemberEntity> entities = new ArrayList<MemberEntity>();
		entities = entityManager.createQuery(createQuery).getResultList();

		GratuityCalculationDto gratuityCalculationDto = new GratuityCalculationDto();
		List<MemberDto> members = MemberHelper.fromEntities(entities);
//		for (MemberDto mdto : members) {
//			mdto.setGratuityCalculationDto(
//					gratuityCalculationDto.entityToDto(gratuityCalculationRepository.findByMemberId(mdto.getId())));
//		}

		return ApiResponseDto.success(MemberHelper.fromEntities(entities));
	}

	@Override
	public ApiResponseDto<MemberDto> findById(Long memberId, Long taggedStatusId) {
		if (taggedStatusId.equals(78L))
			return this.inprogressMember(memberId);
		else
			return this.existingMember(memberId);
	}

	private ApiResponseDto<MemberDto> existingMember(Long memberId) {
		GratuityCalculationDto gratuityCalculationDto = new GratuityCalculationDto();

		MasterMemberEntity entity = masterMemberRepository.findById(memberId).get();
		MemberDto mdto = entityMasterToDto(entity);

		if (entity.getAddresses() != null && !entity.getAddresses().isEmpty()) {
			mdto.setAddresses(entity.getAddresses().stream().map(MemberHelper::masterAddressEntityToDto)
					.collect(Collectors.toList()));
		}
		if (entity.getBankAccounts() != null && !entity.getBankAccounts().isEmpty()) {
			mdto.setBankAccounts(entity.getBankAccounts().stream().map(MemberHelper::masterBankAccountEntityToDto)
					.collect(Collectors.toList()));
		}
		if (entity.getNominees() != null && !entity.getNominees().isEmpty()) {
			mdto.setNominees(entity.getNominees().stream().map(MemberHelper::masterNomineeEntityToDto)
					.collect(Collectors.toList()));
		}
		if (entity.getAppointees() != null && !entity.getAppointees().isEmpty()) {
			List<MemberAppointeeDto> memberApponteeDtos = new ArrayList<MemberAppointeeDto>();
			for (MasterMemberAppointeeEntity mae : entity.getAppointees()) {
				MemberAppointeeDto mad = MemberHelper.masterAppointeeEntityToDto(mae);
				mad.setNominee(MemberHelper.masterNomineeEntityToDto(mae.getNominee()));
				memberApponteeDtos.add(mad);
			}
			mdto.setAppointees(memberApponteeDtos);
		}

		mdto.setGratuityCalculationDto(
				gratuityCalculationDto.entityToDto(gratuityCalculationRepository.findByMemberId(mdto.getId())));

		return ApiResponseDto.success(mdto);
	}

	private MemberDto entityMasterToDto(MasterMemberEntity entity) {
		return new ModelMapper().map(entity, MemberDto.class);
	}

	private ApiResponseDto<MemberDto> inprogressMember(Long memberId) {
		GratuityCalculationDto gratuityCalculationDto = new GratuityCalculationDto();

		MemberEntity entity = memberRepository.findById(memberId).get();
		MemberDto mdto = entityToDto(entity);

		if (entity.getAddresses() != null && !entity.getAddresses().isEmpty()) {
			mdto.setAddresses(
					entity.getAddresses().stream().map(MemberHelper::addressEntityToDto).collect(Collectors.toList()));
		}
		if (entity.getBankAccounts() != null && !entity.getBankAccounts().isEmpty()) {
			mdto.setBankAccounts(entity.getBankAccounts().stream().map(MemberHelper::bankAccountEntityToDto)
					.collect(Collectors.toList()));
		}
		if (entity.getNominees() != null && !entity.getNominees().isEmpty()) {
			mdto.setNominees(
					entity.getNominees().stream().map(MemberHelper::nomineeEntityToDto).collect(Collectors.toList()));
		}
		if (entity.getAppointees() != null && !entity.getAppointees().isEmpty()) {
			List<MemberAppointeeDto> memberApponteeDtos = new ArrayList<MemberAppointeeDto>();
			for (MemberAppointeeEntity mae : entity.getAppointees()) {
				MemberAppointeeDto mad = MemberHelper.appointeeEntityToDto(mae);
				mad.setNominee(MemberHelper.nomineeEntityToDto(mae.getNominee()));
				memberApponteeDtos.add(mad);
			}
			mdto.setAppointees(memberApponteeDtos);
		}

		mdto.setGratuityCalculationDto(
				gratuityCalculationDto.entityToDto(gratuityCalculationRepository.findByMemberId(mdto.getId())));

		return ApiResponseDto.success(mdto);
	}

	@Override
	public byte[] findAllErrors(Long batchId) {
		List<MemberBulkErrorEntity> errornousInfo = memberErrorRepository.findAllByBatchId(batchId);
		List<Object[]> errornousData = memberHelper.getErrorForExcelData(batchId);

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sheet1");
		MemberErrorWorkbookHelper memberErrorWorkbookHelper = new MemberErrorWorkbookHelper();
		memberErrorWorkbookHelper.createHeader(workbook, sheet, false);
		memberErrorWorkbookHelper.createDetailRow(workbook, sheet, errornousInfo, errornousData);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			workbook.write(out);
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}
	
	@Override
	public byte[] findAlldomErrors(Long batchId) {
		List<MemberBulkErrorEntity> errornousInfo = memberErrorRepository.findAllByBatchId(batchId);
		List<Object[]> errornousData = memberHelper.getdomErrorForExcelData(batchId);

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sheet1");
		MemberErrorWorkbookHelper memberErrorWorkbookHelper = new MemberErrorWorkbookHelper();
		memberErrorWorkbookHelper.createHeaderdom(workbook, sheet, false);
		memberErrorWorkbookHelper.createDetailRowForDom(workbook, sheet, errornousInfo, errornousData);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			workbook.write(out);
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public byte[] downloadSample() {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sheet1");
		MemberErrorWorkbookHelper memberErrorWorkbookHelper = new MemberErrorWorkbookHelper();
		memberErrorWorkbookHelper.createHeader(workbook, sheet, true);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			workbook.write(out);
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}
	
	@Override
	public byte[] downloadMidLeaverSample() {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sheet1");
		MemberErrorWorkbookHelper memberErrorWorkbookHelper = new MemberErrorWorkbookHelper();
		memberErrorWorkbookHelper.createMidLeaverHeader(workbook, sheet, true);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			workbook.write(out);
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}
	
	@Override
	public byte[] downloadMidLeaverMemberDetail(Long tmpPolicyId) {
		
		StandardCodeEntity standardCodeEntity = standardCodeRepository.findById(5L).get();
		List<Object[]> midLeaversDetail = tempMemberRepository.findAllMidLeaverByTmpPolicyId(tmpPolicyId, (Double.valueOf(standardCodeEntity.getValue()) / 100));
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sheet1");
		MemberErrorWorkbookHelper memberErrorWorkbookHelper = new MemberErrorWorkbookHelper();
		memberErrorWorkbookHelper.createMidLeaverDetailHeader(workbook, sheet, true);
		memberErrorWorkbookHelper.createMidLeaverDetailRow(workbook, sheet, midLeaversDetail);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			workbook.write(out);
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}

	@Transactional
	@Override
	public ApiResponseDto<MemberBulkResponseDto> deleteBatch(Long batchId) {
		memberBatchRepository.deleteById(batchId);
		memberBatchFileRepository.deleteByBatchFileId(batchId);
		memberErrorRepository.deleteByBatchId(batchId);
		memberBulkStgRepository.deleteByBatchId(batchId);

		memberAddressRepository.deleteByBatchId(batchId);
		memberAppointeeRepository.deleteByBatchId(batchId);
		memberBankAccountRepository.deleteByBatchId(batchId);
		memberNomineeRepository.deleteByBatchId(batchId);
		memberRepository.deleteByBatchId(batchId);
		return ApiResponseDto.success(null);
	}

	// add single member
	@Override
	public ApiResponseDto<MemberDto> uploadMember(MemberDto memberDto) {
		Long memberStatusActive = 146l;
		MemberEntity memberEntity = new ModelMapper().map(memberDto, MemberEntity.class);
//		Long getMaxLicNumber=memberRepository.MaxLicNumber(memberDto.getQuotationId()); 
//		getMaxLicNumber = getMaxLicNumber == null ? 1 : getMaxLicNumber + 1;
//		
//		memberEntity.setLicId(getMaxLicNumber.toString());
		memberEntity.setStatusId(memberStatusActive);
		memberEntity.setCreatedDate(new Date());
		memberEntity.setCreatedBy(memberDto.getCreatedBy());
		memberEntity.setIsActive(true);
		return ApiResponseDto.created(MemberHelper.entitytomemberdto(memberRepository.save(memberEntity)));
	}
	
	@Override
	public ApiResponseDto<MemberDto> updateMember(Long id, MemberDto memberDto) {
		
		MemberEntity memberEntity = memberRepository.findById(id).get();
		MemberEntity newMemberEntity=new ModelMapper().map(memberDto, MemberEntity.class);
		newMemberEntity.setCreatedDate(memberEntity.getCreatedDate());
		newMemberEntity.setCreatedBy(memberEntity.getCreatedBy());
		newMemberEntity.setIsActive(true);
		newMemberEntity.setModifiedDate(memberDto.getModifiedDate());
		newMemberEntity.setModifiedBy(memberDto.getModifiedBy());
		memberEntity.setModifiedDate(new Date());
		return ApiResponseDto.success(MemberHelper.entitytomemberdto(memberRepository.save(newMemberEntity)));
	}

	@Override
	public ApiResponseDto<List<MemberAddressDto>> uploadMemberAddress(MemberAddressDto memberAddressDto, Long memberId) {
	
		MemberEntity memberEntity = memberRepository.findById(memberId).get();
		List<MemberAddressEntity> getMemberAddress=new ArrayList<MemberAddressEntity>();
		
		MemberAddressEntity memberAddressEntity = MemberHelper.addressDtotoEntity(memberAddressDto);
		memberAddressEntity.setCreatedDate(new Date());
		memberAddressEntity.setCreatedBy(memberAddressDto.getCreatedBy());
		memberAddressEntity.setIsActive(true);
		memberAddressEntity.setMember(memberEntity);
		getMemberAddress.add(memberAddressEntity);
		getMemberAddress.addAll(memberEntity.getAddresses());
		memberAddressRepository.saveAll(getMemberAddress);
		
		
		
		return ApiResponseDto.created(getMemberAddress.stream().map(MemberHelper::addressEntityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<List<MemberAddressDto>> updateMemberAddress(Long id, MemberAddressDto memberAddressDto) {
		MemberAddressEntity memberAddressEntity = memberAddressRepository.findById(id).get();
		MemberAddressEntity newMemberAddressEntity = new ModelMapper().map(memberAddressDto, MemberAddressEntity.class);
		MemberEntity memberEntity = memberRepository.findById(memberAddressEntity.getMember().getId()).get();
		newMemberAddressEntity.setId(id);
		newMemberAddressEntity.setMember(memberAddressEntity.getMember());
		newMemberAddressEntity.setModifiedBy(memberAddressEntity.getModifiedBy());
		newMemberAddressEntity.setModifiedDate(new Date());
		newMemberAddressEntity.setCreatedBy(memberAddressEntity.getCreatedBy());
		newMemberAddressEntity.setCreatedDate(memberAddressEntity.getCreatedDate());
		newMemberAddressEntity.setIsActive(true);
		memberAddressRepository.save(newMemberAddressEntity);
		return ApiResponseDto.created(memberEntity.getAddresses().stream().map(MemberHelper::addressEntityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<List<MemberBankAccountDto>> uploadMemberBank(MemberBankAccountDto memberBankAccountDto,
			Long memberId) {
		MemberEntity memberEntity = memberRepository.findById(memberId).get();
		MemberBankAccount memberBankAccount = MemberHelper.bankDtoToEntity(memberBankAccountDto);
		List<MemberBankAccount> getMemberBankAccount=new ArrayList<MemberBankAccount>();
		
		memberBankAccount.setCreatedDate(new Date());
		memberBankAccount.setCreatedBy(memberBankAccountDto.getCreatedBy());
		memberBankAccount.setIsActive(true);
		memberBankAccount.setMember(memberEntity);
		getMemberBankAccount.add(memberBankAccount);
		getMemberBankAccount.addAll(memberEntity.getBankAccounts());
		getMemberBankAccount=memberBankAccountRepository.saveAll(getMemberBankAccount);
		
		return ApiResponseDto.created(getMemberBankAccount.stream().map(MemberHelper::bankAccountEntityToDto).collect(Collectors.toList()));
	}


	@Override
	public ApiResponseDto<List<MemberBankAccountDto>> updateMemberBank(Long id, MemberBankAccountDto memberBankAccountDto) {
		MemberBankAccount memberBankAccount = memberBankAccountRepository.findById(id).get();
		MemberBankAccount newMemberBankAccount = MemberHelper.bankDtoToEntity(memberBankAccountDto);
		MemberEntity memberEntity = memberRepository.findById(memberBankAccount.getMember().getId()).get();
		newMemberBankAccount.setId(id);
		newMemberBankAccount.setMember(memberBankAccount.getMember());
		newMemberBankAccount.setModifiedBy(memberBankAccountDto.getModifiedBy());
		newMemberBankAccount.setModifiedDate(new Date());
		newMemberBankAccount.setCreatedBy(memberBankAccount.getCreatedBy());
		newMemberBankAccount.setCreatedDate(memberBankAccount.getCreatedDate());
		newMemberBankAccount.setIsActive(true);
		memberBankAccountRepository.save(newMemberBankAccount);
		return ApiResponseDto.created(memberEntity.getBankAccounts().stream().map(MemberHelper::bankAccountEntityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<List<MemberNomineeDto>> uploadMemberNominee(MemberNomineeDto memberNomineeDto, Long memberId) {
		MemberEntity memberEntity = memberRepository.findById(memberId).get();
		MemberNomineeEntity memberNomineeEntity = MemberHelper.nomineeDtotoEntity(memberNomineeDto);
		List<MemberNomineeEntity> getMemberNomineeEntity=new ArrayList<MemberNomineeEntity>();
		
		memberNomineeEntity.setCreatedDate(new Date());
		memberNomineeEntity.setCreatedBy(memberNomineeDto.getCreatedBy());
		memberNomineeEntity.setIsActive(true);
		memberNomineeEntity.setMember(memberEntity);
		getMemberNomineeEntity.add(memberNomineeEntity);
		getMemberNomineeEntity.addAll(memberEntity.getNominees());
		getMemberNomineeEntity=memberNomineeRepository.saveAll(getMemberNomineeEntity);
		
		return ApiResponseDto.created(getMemberNomineeEntity.stream().map(MemberHelper::nomineeEntityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<List<MemberNomineeDto>> updateMemberNominee(MemberNomineeDto memberNomineeDto, Long id) {
		MemberNomineeEntity memberNomineeEntity = memberNomineeRepository.findById(id).get();
		MemberNomineeEntity newMemberNomineeEntity = MemberHelper.nomineeDtotoEntity(memberNomineeDto);
		MemberEntity memberEntity = memberRepository.findById(memberNomineeEntity.getMember().getId()).get();
		newMemberNomineeEntity.setId(id);
		newMemberNomineeEntity.setMember(memberNomineeEntity.getMember());
		newMemberNomineeEntity.setModifiedBy(memberNomineeDto.getModifiedBy());
		newMemberNomineeEntity.setModifiedDate(new Date());
		newMemberNomineeEntity.setCreatedBy(memberNomineeEntity.getCreatedBy());
		newMemberNomineeEntity.setCreatedDate(memberNomineeEntity.getCreatedDate());
		newMemberNomineeEntity.setIsActive(true);
		memberNomineeRepository.save(newMemberNomineeEntity);	

		return ApiResponseDto.created(memberEntity.getNominees().stream().map(MemberHelper::nomineeEntityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<List<MemberAppointeeDto>> uploadMemberAppointee(MemberAppointeeDto memberAppointeeDto,
			Long nomineeId, Long memberId) {
		MemberEntity memberEntity = memberRepository.findById(memberId).get();
		MemberNomineeEntity memberNomineeEntity = memberNomineeRepository.findById(nomineeId).get();
		List<MemberAppointeeEntity> getMemberAppointeeEntity=new ArrayList<MemberAppointeeEntity>();
		MemberAppointeeEntity memberAppointeeEntity = MemberHelper.appointeeDtotoEntity(memberAppointeeDto);

		memberAppointeeEntity.setMember(memberEntity);
		memberAppointeeEntity.setNominee(memberNomineeEntity);
		memberAppointeeEntity.setCreatedDate(new Date());
		memberAppointeeEntity.setCreatedBy(memberAppointeeDto.getCreatedBy());
		memberAppointeeEntity.setIsActive(true);
		getMemberAppointeeEntity.add(memberAppointeeEntity);
		getMemberAppointeeEntity.addAll(memberEntity.getAppointees());
		memberAppointeeRepository.saveAll(getMemberAppointeeEntity);
		return ApiResponseDto.created(getMemberAppointeeEntity.stream().map(MemberHelper::appointeeEntityToDto).collect(Collectors.toList()));
	}
	
	@Override
	public ApiResponseDto<List<MemberAppointeeDto>> updateMemberAppointee(MemberAppointeeDto memberAppointeeDto, Long id) {
		MemberAppointeeEntity memberAppointeeEntity = memberAppointeeRepository.findById(id).get();
		MemberAppointeeEntity newMemberAppointeeEntity = MemberHelper.appointeeDtotoEntity(memberAppointeeDto);
		MemberEntity memberEntity = memberRepository.findById(memberAppointeeEntity.getMember().getId()).get();
		newMemberAppointeeEntity.setId(id);
		newMemberAppointeeEntity.setMember(memberAppointeeEntity.getMember());
		newMemberAppointeeEntity.setModifiedBy(memberAppointeeDto.getModifiedBy());
		newMemberAppointeeEntity.setModifiedDate(new Date());
		newMemberAppointeeEntity.setNominee(memberAppointeeEntity.getNominee());
		newMemberAppointeeEntity.setCreatedBy(memberAppointeeEntity.getCreatedBy());
		newMemberAppointeeEntity.setCreatedDate(memberAppointeeEntity.getCreatedDate());
		newMemberAppointeeEntity.setIsActive(true);
		memberAppointeeRepository.save(newMemberAppointeeEntity);
		return ApiResponseDto.created(memberEntity.getAppointees().stream().map(MemberHelper::appointeeEntityToDto).collect(Collectors.toList()));
	}

	@Override
	public ApiResponseDto<MemberDto> memberCodeStatus(Long quotationId, String code, Long memberId) {
		MemberDto memberDto = new MemberDto();
		
		if(memberId == 0) {
			
			memberDto.setIsMemberCodeUnique(memberRepository.codeStatus(quotationId,code).isPresent());
					}else {
						memberDto.setIsMemberCodeUnique(memberRepository.codeStatuswithMember(quotationId,code,memberId).isPresent());
					}
		return ApiResponseDto.success(memberDto);
	}
	//delete Member Detais Start ...
		@Transactional
		@Override
		public ApiResponseDto<List<MemberBankAccountDto>> deleteMemberBank(Long id) {
			memberBankAccountRepository.deleteMemberBankId(id);
			return ApiResponseDto.success(null);
		}

		@Transactional
		@Override
		public ApiResponseDto<List<MemberAddressDto>> deleteMemberAddress(Long id) {

			memberAddressRepository.deleteMemberAddressId(id);
			return ApiResponseDto.success(null);
		}

		@Transactional
		@Override
		public ApiResponseDto<List<MemberNomineeDto>> deleteMemberNominee(Long id) {
			memberNomineeRepository.deleteMemberNomineeId(id);
			return ApiResponseDto.success(null);
		}

		@Transactional
		@Override
		public ApiResponseDto<List<MemberAppointeeDto>> deleteMemberAppointee(Long id) {
			
			memberAppointeeRepository.deleteMemberAppointeeId(id);
			return ApiResponseDto.success(null);
		}

		@Override
		public ApiResponseDto<MemberBatchDto> memberTotalCount(Long quotationId) {
			MemberBatchDto memberBatchDto=new MemberBatchDto();
			memberBatchDto.setMemberImportCount(Integer.toString(memberRepository.numberMemberCount(quotationId)));
			return ApiResponseDto.success(memberBatchDto);
		}

		@Override
		public byte[] downloadSampleforCalim() {
		
				Workbook workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				MemberErrorWorkbookHelper memberErrorWorkbookHelper = new MemberErrorWorkbookHelper();
				memberErrorWorkbookHelper.createHeaderforClaim(workbook, sheet, true);

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				try {
					workbook.write(out);
					out.close();
					return out.toByteArray();
				} catch (IOException e) {
					return null;
				}
			
		}
		


		@Override
		public ApiResponseDto<List<MemberDto>> delete(Long id) {
			MemberDto memberDto=new MemberDto();
	     memberRepository.deleteById(id);
		return ApiResponseDto.success(null);
		
		}

		@Override
		public byte[] findforclaimsErrors(Long batchId) {
			List<MemberBulkErrorEntity> errornousInfo = memberErrorRepository.findAllByBatchId(batchId);
			List<Object[]> errornousData = memberHelper.getErrorForClaimsExcelData(batchId);

			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Sheet1");
			MemberErrorWorkbookHelper memberErrorWorkbookHelper = new MemberErrorWorkbookHelper();
			memberErrorWorkbookHelper.createclaimsHeader(workbook, sheet, false);
			memberErrorWorkbookHelper.createHeaderforClaims(workbook, sheet, errornousInfo, errornousData);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				workbook.write(out);
				out.close();
				return out.toByteArray();
			} catch (IOException e) {
				return null;
			}
		}
		
		@Override
		public ApiResponseDto<Boolean> isEmployeeCodeExist(Long masterPolicyId, Long tmpPolicyId, Long tmpMemberId, String employeeCode) {			
			if (tmpMemberId == 0) {
				List<PolicyMemberEntity> policyMemberEntities = policyMemberRepository.findByEmployeeCode(masterPolicyId, employeeCode);
				if (policyMemberEntities.size() == 0)
					return ApiResponseDto.success(false);
				else
					return ApiResponseDto.success(true);
			} else {
				List<PolicyMemberEntity> policyMemberEntities = policyMemberRepository.findByEmployeeCode(masterPolicyId, employeeCode);
				if (policyMemberEntities.size() == 0) {
					List<TempMemberEntity> tempMemberEntities = tempMemberRepository.findByEmployeeCode(tmpPolicyId, tmpMemberId, employeeCode);
					if (tempMemberEntities.size() == 0)
						return ApiResponseDto.success(false);
					else
						return ApiResponseDto.success(true);
				} else
					return ApiResponseDto.success(true);
			}
		}

		@Override
		public ApiResponseDto<Boolean> isEmployeePanExist(Long masterPolicyId, Long tmpPolicyId, Long tmpMemberId, String employeePan) {
			if (tmpMemberId == 0) {
				List<PolicyMemberEntity> policyMemberEntities = policyMemberRepository.findByEmployeePan(masterPolicyId, employeePan);
				if (policyMemberEntities.size() == 0)
					return ApiResponseDto.success(false);
				else
					return ApiResponseDto.success(true);
			} else {
				List<PolicyMemberEntity> policyMemberEntities = policyMemberRepository.findByEmployeePan(masterPolicyId, employeePan);
				if (policyMemberEntities.size() == 0) {
					List<TempMemberEntity> tempMemberEntities = tempMemberRepository.findByEmployeePan(tmpPolicyId, tmpMemberId, employeePan);
					if (tempMemberEntities.size() == 0)
						return ApiResponseDto.success(false);
					else
						return ApiResponseDto.success(true);
				} else
					return ApiResponseDto.success(true);
			}
		}
		
		@Override
		public ApiResponseDto<Boolean> isEmployeeAadharExist(Long masterPolicyId, Long tmpPolicyId, Long tmpMemberId, String employeeAadhar) {
			if (tmpMemberId == 0) {
				List<PolicyMemberEntity> policyMemberEntities = policyMemberRepository.findByEmployeeAadhar(masterPolicyId, employeeAadhar);
				if (policyMemberEntities.size() == 0)
					return ApiResponseDto.success(false);
				else
					return ApiResponseDto.success(true);
			} else {
				List<PolicyMemberEntity> policyMemberEntities = policyMemberRepository.findByEmployeeAadhar(masterPolicyId, employeeAadhar);
				if (policyMemberEntities.size() == 0) {
					List<TempMemberEntity> tempMemberEntities = tempMemberRepository.findByEmployeeAadhar(tmpPolicyId, tmpMemberId, employeeAadhar);
					if (tempMemberEntities.size() == 0)
						return ApiResponseDto.success(false);
					else
						return ApiResponseDto.success(true);
				} else
					return ApiResponseDto.success(true);
			}
		}
		
}

