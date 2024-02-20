package com.lic.epgs.gratuity.quotation.premium.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;
import com.lic.epgs.gratuity.quotation.premium.entity.GratuityCalculationEntity;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Repository
public class GratuityCalculationRepositoryImpl {

	@PersistenceContext
	private EntityManager entityManager;
	
	public GratuityCalculationEntity getGratCalculatedValues(List<RenewalPolicyTMPMemberEntity> renewalPolicyTMPMemberEntityList) {

		log.info("Entering into GratuityCalculationRepositoryImpl : getGratCalculatedValues");

		GratuityCalculationEntity result = new GratuityCalculationEntity();		
		List<Long> memberIdList = new ArrayList<>();
		
		
		if (!ObjectUtils.isEmpty(renewalPolicyTMPMemberEntityList)) {
			for (RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity : renewalPolicyTMPMemberEntityList) {
				memberIdList.add(renewalPolicyTMPMemberEntity.getPmstMemebrId());
			}
		}
							
		StringBuilder sqlQuery = new StringBuilder("SELECT sum(ACCRUED_GRAT), sum(LC_PREMIUM) FROM GRATCALCULATION WHERE IS_ACTIVE = 1 ");
		
		if (!ObjectUtils.isEmpty(memberIdList)) {
			sqlQuery.append("and (MEMBER_ID) IN (:memberId) ");
			
			log.info("Query for getGratCalculatedValues : "+sqlQuery);
						
			Query query = entityManager.createNativeQuery(sqlQuery.toString());		
			
			if (!ObjectUtils.isEmpty(memberIdList)) {
				query.setParameter("memberId", memberIdList);			
			}

			try {
				List<Object[]> obj = query.getResultList();

				if (!obj.isEmpty()) {
					for (Object[] record : obj) {	
						
						result.setAccruedGrat(String.valueOf(record[0]).equals("null") ? 0L : Double.parseDouble(String.valueOf(record[0])));
						result.setLcPremium(String.valueOf(record[1]).equals("null") ? 0L : Double.parseDouble(String.valueOf(record[1])));																
					}
				}
			} catch (Exception e) {
				log.error("Exception occurred while getGratCalculatedValues due to : "+e.getMessage());
			}			
			}
		return result;
	}
}
		


