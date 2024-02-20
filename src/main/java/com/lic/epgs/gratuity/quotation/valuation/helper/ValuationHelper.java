package com.lic.epgs.gratuity.quotation.valuation.helper;


import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.google.common.base.CharMatcher;
import com.lic.epgs.gratuity.common.entity.MemberWithdrawalEntity;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.quotation.entity.QuotationEntity;
import com.lic.epgs.gratuity.quotation.valuation.dto.ValuationDto;
import com.lic.epgs.gratuity.quotation.valuation.entity.MasterValuationEntity;
import com.lic.epgs.gratuity.quotation.valuation.entity.ValuationEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationWithdrawalRateEntity;

public class ValuationHelper {
	public static MasterValuationEntity entityToMasterEntity(ValuationEntity entity) {
		return new ModelMapper().map(entity, MasterValuationEntity.class);
	}

	public static ValuationDto entityToDto(ValuationEntity entity) {
		return new ModelMapper().map(entity, ValuationDto.class);
	}

	public static ValuationDto entityToDto(MasterValuationEntity entity) {
		return new ModelMapper().map(entity, ValuationDto.class);
	}

	public static ValuationEntity dtoToEntity(ValuationDto dto) {
		return new ModelMapper().map(dto, ValuationEntity.class);
	}





	public static QuotationEntity checkStdValuationDeviated(ValuationEntity getValuationEntity,
			StandardCodeEntity standardCodeEntity, QuotationEntity quotationEntity,
			List<MemberWithdrawalEntity> memberWithdrawalEntities,
			List<ValuationWithdrawalRateEntity> valuationWithdrawalRateEntity) {
		String[] get = standardCodeEntity.getValue().split("-");
		String minSalary = get[0];
		String maxSalary = get[1];
		if (Float.parseFloat(minSalary) <= getValuationEntity.getSalaryEscalation() * 100
				&& Float.parseFloat(maxSalary) >= getValuationEntity.getSalaryEscalation() * 100) {

			quotationEntity.setIsStdValuesDeviated(false);
			
			

		} else {
			if(quotationEntity.getZoEscalationReason() ==null) {
				quotationEntity.setZoEscalationReason("ESCLATION");	
			}else {
			if(quotationEntity.getZoEscalationReason().contains("ESCLATION") != true) {
				
			quotationEntity.setZoEscalationReason(quotationEntity.getZoEscalationReason()+","+"ESCLATION");
			}
			}
			quotationEntity.setIsStdValuesDeviated(true);
		}
		if(quotationEntity.getZoEscalationReason() != null && quotationEntity.getZoEscalationReason().contains("ESCLATION") == true) {
			quotationEntity.setZoEscalationReason(quotationEntity.getZoEscalationReason().replace("ESCLATION,", ""));;
			}
		
		

			int count = 0;
			if (valuationWithdrawalRateEntity.size() != 0) {
				if (memberWithdrawalEntities.size() == valuationWithdrawalRateEntity.size()) {
					for (ValuationWithdrawalRateEntity getValuationWithdrawalRate : valuationWithdrawalRateEntity) {

						for (MemberWithdrawalEntity getEntity : memberWithdrawalEntities) {
							Float getMemberRate = getEntity.getRate();
							Float getValuationRate = getValuationWithdrawalRate.getRate();
							if (getValuationWithdrawalRate.getFromAgeBand() == getEntity.getAgeFrom()) {
								if (getEntity.getAgeTo() == getValuationWithdrawalRate.getToAgeBand()) {
									if (getMemberRate.equals(getValuationRate)) {
										count = count + 1;
									}
								}
								
							}
						}
					}
					System.out.println("count==" + count);
					if (count == memberWithdrawalEntities.size()) {
						if (quotationEntity.getIsStdValuesDeviated() != true) {
						quotationEntity.setIsStdValuesDeviated(false);
						}else {
							if(quotationEntity.getZoEscalationReason() != null && quotationEntity.getZoEscalationReason().contains("WITHDRAWAL") == true) {
							quotationEntity.setZoEscalationReason(quotationEntity.getZoEscalationReason().replace(",WITHDRAWAL", ""));
							}
						}
						
					} else {
						if(quotationEntity.getZoEscalationReason().contains("WITHDRAWAL") != true) {
							quotationEntity.setZoEscalationReason(quotationEntity.getZoEscalationReason()+","+"WITHDRAWAL");
							}
						quotationEntity.setIsStdValuesDeviated(true);
					}

				}
			}
		
		return quotationEntity;
	}

	public static ValuationEntity clonestaging(ValuationEntity entity, String username, Long quotationId) {
		ValuationDto valuationDto = new ModelMapper().map(entity, ValuationDto.class);
		valuationDto.setId(null);
		ValuationEntity valuationEntity = new ModelMapper().map(valuationDto, ValuationEntity.class);
		valuationEntity.setQuotationId(quotationId);
		valuationEntity.setCreatedBy(username);
		valuationEntity.setCreatedDate(new Date());
		valuationEntity.setIsActive(true);
		return valuationEntity;
	}

	public static MasterValuationEntity clone(MasterValuationEntity entity, String username, Long quotationId) {

		ValuationDto valuationDto = new ModelMapper().map(entity, ValuationDto.class);
		valuationDto.setId(null);
		MasterValuationEntity masterValuationEntity = new ModelMapper().map(valuationDto, MasterValuationEntity.class);
		masterValuationEntity.setQuotationId(quotationId);
		masterValuationEntity.setCreatedBy(username);
		masterValuationEntity.setCreatedDate(new Date());
		masterValuationEntity.setIsActive(true);
		return masterValuationEntity;
	}
}
