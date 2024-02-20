package com.lic.epgs.gratuity.policy.member.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.endorsement.dto.EndorsementDto;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberAppointeeDto;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberDto;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberSearchDto;
import com.lic.epgs.gratuity.policy.member.dto.TempMemberDto;
import com.lic.epgs.gratuity.policy.member.entity.StagingPolicyMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.member.entity.StagingPolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberAddressEntity;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberAddressEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.helper.PolicyMemberHelper;
import com.lic.epgs.gratuity.policy.member.repository.StagingPolicyMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.TempMemberRepository;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.member.service.PolicyMemberService;
import com.lic.epgs.gratuity.quotation.member.entity.MemberAddressEntity;


/**
 * @author Gopi
 *
 */

@Service
public class PolicyMemberServiceImpl implements PolicyMemberService {
 
	protected final Logger logger = LogManager.getLogger(getClass());
	
	//@Value("${spring.datasource.url}")
	private String dbUrl;

	//@Value("${spring.datasource.driver-class-name}")
	private String dbDriver;

	//@Value("${spring.datasource.username}")
	private String dbUsername;

	//@Value("${spring.datasource.password}")
	private String dbPassword;
	
	@Autowired
	private StagingPolicyMemberRepository stagingPolicyMemberRepository;
	
	@Autowired
	private PolicyMemberRepository policyMemberRepository;

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private TempMemberRepository tempMemberRepository;
	
	//@Autowired
	//private GratuityCalculationRepository gratuityCalculationRepository;

	@Override
	public ApiResponseDto<List<PolicyMemberDto>> filter(PolicyMemberSearchDto policyMemberSearchDto,
			String type) {
		if (type.equals("INPROGRESS")) {
			List<Predicate> predicates = new ArrayList<>();
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<StagingPolicyMemberEntity> createQuery = criteriaBuilder.createQuery(StagingPolicyMemberEntity.class);
			Root<StagingPolicyMemberEntity> root = createQuery.from(StagingPolicyMemberEntity.class);
			
			predicates.add(criteriaBuilder.equal(root.get("policyId"),
					policyMemberSearchDto.getPolicyId()));
			if (StringUtils.isNotBlank(policyMemberSearchDto.getLicId())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("licId")),
						policyMemberSearchDto.getLicId().toLowerCase()));
			}
			if (StringUtils.isNotBlank(policyMemberSearchDto.getEmployeeCode())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("employeeCode")),
						policyMemberSearchDto.getEmployeeCode().toLowerCase()));
			}
			if (StringUtils.isNotBlank(policyMemberSearchDto.getFirstName())) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName"))
						,"%"+policyMemberSearchDto.getFirstName().toLowerCase()+"%"));
			}
			if (StringUtils.isNotBlank(policyMemberSearchDto.getMiddleName())) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("middleName"))
						,"%"+policyMemberSearchDto.getMiddleName().toLowerCase()+"%"));
			}
			if (StringUtils.isNotBlank(policyMemberSearchDto.getLastName())) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName"))
						,"%"+policyMemberSearchDto.getLastName().toLowerCase()+"%"));
			}
			if (StringUtils.isNotBlank(policyMemberSearchDto.getName())) {
				Predicate predicateForFirstName = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName"))
						,"%"+policyMemberSearchDto.getName().toLowerCase()+"%");
				
				Predicate predicateForMiddleName = criteriaBuilder.like(criteriaBuilder.lower(root.get("middleName"))
						,"%"+policyMemberSearchDto.getName().toLowerCase()+"%");
				
				Predicate predicateForLastName = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName"))
						,"%"+policyMemberSearchDto.getName().toLowerCase()+"%");
				
				predicates.add(criteriaBuilder.or(predicateForFirstName, predicateForMiddleName, 
						predicateForLastName));
			}
			/*if (StringUtils.isNotBlank(memberSearchDto.getProposalNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("code")),
						memberSearchDto.getProposalNumber().toLowerCase()));
			}
			if (StringUtils.isNotBlank(plcTmpMemberSearchDto.getLoanNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("loanNumber")),
						plcTmpMemberSearchDto.getLoanNumber().toLowerCase()));
			}*/
			if (StringUtils.isNotBlank(policyMemberSearchDto.getPanNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("panNumber")),
						policyMemberSearchDto.getPanNumber().toLowerCase()));
			}
			if (StringUtils.isNotBlank(policyMemberSearchDto.getAadharNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("aadharNumber")),
						policyMemberSearchDto.getAadharNumber().toLowerCase()));
			}
			if (policyMemberSearchDto.getMemberStatus() != null && policyMemberSearchDto.getMemberStatus() > 0) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("statusId")), policyMemberSearchDto.getMemberStatus()));
			}
			
			/*if (StringUtils.isNotBlank(memberSearchDto.getStatus())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")),
						memberSearchDto.getStatus().toLowerCase()));
			}*/
			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			List<StagingPolicyMemberEntity> entities = new ArrayList<StagingPolicyMemberEntity>();
			entities = entityManager.createQuery(createQuery).getResultList();
			
	//		GratuityCalculationDto gratuityCalculationDto = new GratuityCalculationDto(); 
	//		List<PlcTmpMemberDto> members = PlcTmpMemberHelper.fromEntities(entities);
	//		for (PlcTmpMemberDto mdto : members) {
	//			mdto.setGratuityCalculationDto(gratuityCalculationDto.entityToDto(gratuityCalculationRepository.findByMemberId(mdto.getId())));
	//		}
			
			return ApiResponseDto.success(PolicyMemberHelper.fromStagingEntities(entities));
		} else {
			List<Predicate> predicates = new ArrayList<>();
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<PolicyMemberEntity> createQuery = criteriaBuilder.createQuery(PolicyMemberEntity.class);
			Root<PolicyMemberEntity> root = createQuery.from(PolicyMemberEntity.class);
			
			predicates.add(criteriaBuilder.equal(root.get("policyId"),
					policyMemberSearchDto.getPolicyId()));
			if (StringUtils.isNotBlank(policyMemberSearchDto.getLicId())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("licId")),
						policyMemberSearchDto.getLicId().toLowerCase()));
			}
			if (StringUtils.isNotBlank(policyMemberSearchDto.getEmployeeCode())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("employeeCode")),
						policyMemberSearchDto.getEmployeeCode().toLowerCase()));
			}
			if (StringUtils.isNotBlank(policyMemberSearchDto.getFirstName())) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName"))
						,"%"+policyMemberSearchDto.getFirstName().toLowerCase()+"%"));
			}
			if (StringUtils.isNotBlank(policyMemberSearchDto.getMiddleName())) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("middleName"))
						,"%"+policyMemberSearchDto.getMiddleName().toLowerCase()+"%"));
			}
			if (StringUtils.isNotBlank(policyMemberSearchDto.getLastName())) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName"))
						,"%"+policyMemberSearchDto.getLastName().toLowerCase()+"%"));
			}
			if (StringUtils.isNotBlank(policyMemberSearchDto.getName())) {
				Predicate predicateForFirstName = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName"))
						,"%"+policyMemberSearchDto.getName().toLowerCase()+"%");
				
				Predicate predicateForMiddleName = criteriaBuilder.like(criteriaBuilder.lower(root.get("middleName"))
						,"%"+policyMemberSearchDto.getName().toLowerCase()+"%");
				
				Predicate predicateForLastName = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName"))
						,"%"+policyMemberSearchDto.getName().toLowerCase()+"%");
				
				predicates.add(criteriaBuilder.or(predicateForFirstName, predicateForMiddleName, 
						predicateForLastName));
			}
			/*if (StringUtils.isNotBlank(memberSearchDto.getProposalNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("code")),
						memberSearchDto.getProposalNumber().toLowerCase()));
			}
			if (StringUtils.isNotBlank(plcTmpMemberSearchDto.getLoanNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("loanNumber")),
						plcTmpMemberSearchDto.getLoanNumber().toLowerCase()));
			}*/
			if (StringUtils.isNotBlank(policyMemberSearchDto.getPanNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("panNumber")),
						policyMemberSearchDto.getPanNumber().toLowerCase()));
			}
			if (StringUtils.isNotBlank(policyMemberSearchDto.getAadharNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("aadharNumber")),
						policyMemberSearchDto.getAadharNumber().toLowerCase()));
			}
			if (policyMemberSearchDto.getMemberStatus() != null && policyMemberSearchDto.getMemberStatus() > 0) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("statusId")), policyMemberSearchDto.getMemberStatus()));
			}
			
			/*if (StringUtils.isNotBlank(memberSearchDto.getStatus())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")),
						memberSearchDto.getStatus().toLowerCase()));
			}*/
			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			List<PolicyMemberEntity> entities = new ArrayList<PolicyMemberEntity>();
			entities = entityManager.createQuery(createQuery).getResultList();
			
	//		GratuityCalculationDto gratuityCalculationDto = new GratuityCalculationDto(); 
	//		List<PlcTmpMemberDto> members = PlcTmpMemberHelper.fromEntities(entities);
	//		for (PlcTmpMemberDto mdto : members) {
	//			mdto.setGratuityCalculationDto(gratuityCalculationDto.entityToDto(gratuityCalculationRepository.findByMemberId(mdto.getId())));
	//		}
			
			return ApiResponseDto.success(PolicyMemberHelper.fromEntities(entities));
		}
	}
	
	@Override
	public ApiResponseDto<PolicyMemberDto> findById(Long memberId, String type) {
		if (type.equals("INPROGRESS")) {
			//GratuityCalculationDto gratuityCalculationDto = new GratuityCalculationDto();
			
			StagingPolicyMemberEntity entity = stagingPolicyMemberRepository.findById(memberId).get();

			PolicyMemberDto mdto = entityToDto(entity);
			
			if (entity.getAddresses() != null && !entity.getAddresses().isEmpty()) {
				mdto.setAddresses(entity.getAddresses().stream().map(PolicyMemberHelper::addressEntityToDto)
				.collect(Collectors.toList()));
			}
			if (entity.getBankAccounts() != null && !entity.getBankAccounts().isEmpty()) {
				mdto.setBankAccounts(entity.getBankAccounts().stream().map(PolicyMemberHelper::bankAccountEntityToDto)
				.collect(Collectors.toList()));
			}
			if (entity.getNominees() != null && !entity.getNominees().isEmpty()) {
				mdto.setNominees(entity.getNominees().stream().map(PolicyMemberHelper::nomineeEntityToDto)
				.collect(Collectors.toList()));
			}
			System.out.println("Appointee SIZE");
			System.out.println(entity.getAppointees().size());
			if (entity.getAppointees() != null && !entity.getAppointees().isEmpty()) {
				List<PolicyMemberAppointeeDto> memberApponteeDtos = new ArrayList<PolicyMemberAppointeeDto>();
				for (StagingPolicyMemberAppointeeEntity mae : entity.getAppointees()) {
					PolicyMemberAppointeeDto mad = PolicyMemberHelper.appointeeEntityToDto(mae);
					mad.setNominee(PolicyMemberHelper.nomineeEntityToDto(mae.getNominee()));
					memberApponteeDtos.add(mad);
				}
				mdto.setAppointees(memberApponteeDtos);
			}
			
			//mdto.setGratuityCalculationDto(gratuityCalculationDto.entityToDto(gratuityCalculationRepository.findByMemberId(mdto.getId())));
			
			return ApiResponseDto.success(mdto);
		} else {
			//GratuityCalculationDto gratuityCalculationDto = new GratuityCalculationDto();
			
			PolicyMemberEntity entity = policyMemberRepository.findById(memberId).get();
			PolicyMemberDto mdto = entityToDto(entity);
			
			if (entity.getAddresses() != null && !entity.getAddresses().isEmpty()) {
				mdto.setAddresses(entity.getAddresses().stream().map(PolicyMemberHelper::addressEntityToDto)
				.collect(Collectors.toList()));
			}
			if (entity.getBankAccounts() != null && !entity.getBankAccounts().isEmpty()) {
				mdto.setBankAccounts(entity.getBankAccounts().stream().map(PolicyMemberHelper::bankAccountEntityToDto)
				.collect(Collectors.toList()));
			}
			if (entity.getNominees() != null && !entity.getNominees().isEmpty()) {
				mdto.setNominees(entity.getNominees().stream().map(PolicyMemberHelper::nomineeEntityToDto)
				.collect(Collectors.toList()));
			}
			System.out.println("Appointee SIZE");
			System.out.println(entity.getAppointees().size());
			if (entity.getAppointees() != null && !entity.getAppointees().isEmpty()) {
				List<PolicyMemberAppointeeDto> memberApponteeDtos = new ArrayList<PolicyMemberAppointeeDto>();
				for (PolicyMemberAppointeeEntity mae : entity.getAppointees()) {
					PolicyMemberAppointeeDto mad = PolicyMemberHelper.appointeeEntityToDto(mae);
					mad.setNominee(PolicyMemberHelper.nomineeEntityToDto(mae.getNominee()));
					memberApponteeDtos.add(mad);
				}
				mdto.setAppointees(memberApponteeDtos);
			}
			
			//mdto.setGratuityCalculationDto(gratuityCalculationDto.entityToDto(gratuityCalculationRepository.findByMemberId(mdto.getId())));
			
			return ApiResponseDto.success(mdto);
		}
	}
	
	public PolicyMemberDto entityToDto(StagingPolicyMemberEntity entity) {
		return new ModelMapper().map(entity, PolicyMemberDto.class);
	}
	
	public PolicyMemberDto entityToDto(PolicyMemberEntity entity) {
		return new ModelMapper().map(entity, PolicyMemberDto.class);
	}
//Vignesh Author 
	//start
	@Override
	public ApiResponseDto<TempMemberDto> saveTempMember(Long endorsementId, TempMemberDto tempMemberDto) {
		
		PolicyMemberEntity policyMemberEntity=policyMemberRepository.findById(tempMemberDto.getPmstMemberId()).get();
	
		TempMemberEntity tempMemberEntity= new ModelMapper().map(policyMemberEntity, TempMemberEntity.class);
		
		tempMemberEntity.setIsActive(true);
		tempMemberEntity.setCreatedBy(tempMemberDto.getCreatedBy());
		tempMemberEntity.setCreatedDate(new Date());
		tempMemberEntity.setEndorsementId(endorsementId);
		tempMemberEntity.setPanNumber(tempMemberDto.getPanNumber());
		tempMemberEntity.setAadharNumber(tempMemberDto.getAadharNumber());
		tempMemberEntity.setFirstName(tempMemberDto.getFirstName());
		tempMemberEntity.setLastName(tempMemberDto.getLastName());
		tempMemberEntity.setMiddleName(tempMemberDto.getMiddleName());
		tempMemberEntity.setGenderId(tempMemberDto.getGenderId());
		tempMemberEntity.setMobileNo(tempMemberDto.getMobileNo());
		tempMemberEntity.setEmailId(tempMemberDto.getEmailId());
		tempMemberEntity.setPmstMemberId(tempMemberDto.getPmstMemberId());
		tempMemberEntity=tempMemberRepository.save(tempMemberEntity);
		return ApiResponseDto.created(PolicyMemberHelper.tempentitytoDto(tempMemberEntity));
	}

	@Override
	public ApiResponseDto<List<TempMemberDto>> getTempMember(Long endorsementId) {
		List<TempMemberEntity> tempMemberEntity=tempMemberRepository.findByendorsementId(endorsementId);
		List<TempMemberDto> tempMemberDto = new ArrayList<TempMemberDto>();
		if(tempMemberEntity.size() >0) {
			for(TempMemberEntity newTempMemberEntity :  tempMemberEntity) {
			TempMemberDto newTempMemberDto=PolicyMemberHelper.tempentitytoDto(newTempMemberEntity);
			tempMemberDto.add(newTempMemberDto);
			}
		}
		return ApiResponseDto.success(tempMemberDto) ;
	}

	@Override
	public ApiResponseDto<TempMemberDto> updateTempMember(Long id, TempMemberDto tempMemberDto) {
		TempMemberEntity tempMemberEntity=tempMemberRepository.findById(id).get();
		
		tempMemberEntity.setFirstName(tempMemberDto.getFirstName());
		tempMemberEntity.setLastName(tempMemberDto.getLastName());
		tempMemberEntity.setMiddleName(tempMemberDto.getMiddleName());
		tempMemberEntity.setGenderId(tempMemberDto.getGenderId());
		tempMemberEntity.setPanNumber(tempMemberDto.getPanNumber());
		tempMemberEntity.setAadharNumber(tempMemberDto.getAadharNumber());
		tempMemberEntity.setMobileNo(tempMemberDto.getMobileNo());
		tempMemberEntity.setEmailId(tempMemberDto.getEmailId());
	
		return ApiResponseDto.success(PolicyMemberHelper.tempentitytoDto(tempMemberRepository.save(tempMemberEntity)));
	}

}
