package com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.dto.AOCMCredibilityFactorDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.dto.AOCMDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.dto.AOCMExpenseRateDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.entity.AOCMCredibilityFactorEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.entity.AOCMExpenseRateEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberBulkErrorEntity;

public class AOCMHelper {
	
	public static AOCMCredibilityFactorDto entitytodto(AOCMCredibilityFactorEntity aOCMCredibilityFactorEntity) {
		
		return new ModelMapper().map(aOCMCredibilityFactorEntity, AOCMCredibilityFactorDto.class);
	}
	
	public static AOCMExpenseRateDto entitytoaocmdto(AOCMExpenseRateEntity aOCMExpenseRateEntity) {
		
		return new ModelMapper().map(aOCMExpenseRateEntity, AOCMExpenseRateDto.class);
	}

	public static List<AOCMDto> getObjectToDto(List<Object[]> valuationBasicHistoryEntity) {
		List<AOCMDto> getAOCMDto=new ArrayList<AOCMDto>();
		for (Object[] obj : valuationBasicHistoryEntity) {
			
//			for (int i = 0; i < obj.length; i++) {
				
				AOCMDto newAOCMDto=new AOCMDto();
				System.out.println(obj[0]);
				System.out.println(obj[1]);
				
				
				newAOCMDto.setNoOFLives((obj[0] == null ? 0: Long.parseLong(obj[0].toString())));
                newAOCMDto.setTotalPremium(obj[1] == null ? 0 : Double.valueOf(obj[1].toString()).longValue());
                newAOCMDto.setTotalSumAssured(obj[2] == null ? 0: Double.valueOf( obj[2].toString()).longValue());
                newAOCMDto.setAnnualRenewalDate(obj[3] == null ? new Date() : (Date) obj[3]);
                newAOCMDto.setAmountOfClaim(obj[4] == null ? 0: Double.valueOf(obj[4].toString()).longValue());
                newAOCMDto.setNoOfClaim(obj[5] == null ?0:Long.parseLong( obj[5].toString()));
                newAOCMDto.setAmountOfDCPOurEnd(obj[6] == null ? 0 : Double.valueOf(obj[6].toString()).longValue());
                newAOCMDto.setFlag(obj[7]==null ? "p" : obj[7].toString());

				newAOCMDto.setYear(newAOCMDto.getAnnualRenewalDate().getYear() +1900);
				
				getAOCMDto.add(newAOCMDto);
//			}
							
		
		}
		return getAOCMDto;
	}

	
}
