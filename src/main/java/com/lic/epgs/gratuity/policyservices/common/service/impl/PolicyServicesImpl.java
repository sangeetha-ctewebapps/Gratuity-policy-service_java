package com.lic.epgs.gratuity.policyservices.common.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.helper.PolicyHelper;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;

@Service
public class PolicyServicesImpl {
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private MasterPolicyCustomRepository masterPolicyCustomRepository;

	public ApiResponseDto<List<PolicyDto>> masterPolicySearch(PolicySearchDto policySearchDto) {

		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MasterPolicyEntity> createQuery = criteriaBuilder.createQuery(MasterPolicyEntity.class);

		Root<MasterPolicyEntity> root = createQuery.from(MasterPolicyEntity.class);

		if (StringUtils.isNotBlank(policySearchDto.getPolicyNumber())&&policySearchDto.getPolicyNumber().length()==4) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("policyNumber")),
					"%" + StringUtils.right(policySearchDto.getPolicyNumber(), 4).toLowerCase()));
		} else {

			if (StringUtils.isNotBlank(policySearchDto.getPolicyNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("policyNumber")),
						policySearchDto.getPolicyNumber().toLowerCase()));
			}
		}
		if (StringUtils.isNotBlank(policySearchDto.getCustomerName())) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("customerName")),
					"%" + policySearchDto.getCustomerName().toLowerCase() + "%"));
		}
		if (StringUtils.isNotBlank(policySearchDto.getIntermediaryName())) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("intermediaryName")),
					"%" + policySearchDto.getIntermediaryName().toLowerCase() + "%"));
		}
		if (policySearchDto.getLineOfBusinessId() != null && policySearchDto.getLineOfBusinessId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("lineOfBusiness"), policySearchDto.getLineOfBusinessId()));
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
		if (policySearchDto.getPolicyStatusId() != null && policySearchDto.getPolicyStatusId() > 0) {
			predicates.add(criteriaBuilder.equal(root.get("policyStatusId"), policySearchDto.getPolicyStatusId()));
		}
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
		List<MasterPolicyEntity> entities = entityManager.createQuery(createQuery).getResultList();
		List<MasterPolicyEntity> masterPolicyEntities = new ArrayList<>();

		for (MasterPolicyEntity masterPolicyEntity : entities) {
			masterPolicyEntities.add(masterPolicyCustomRepository.setTransientValues(masterPolicyEntity));
		}
		return ApiResponseDto.success(masterPolicyEntities.stream().map(PolicyHelper::entityToDto).collect(Collectors.toList()));
	
	}

}
