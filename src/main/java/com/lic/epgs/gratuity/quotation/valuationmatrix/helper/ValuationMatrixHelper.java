package com.lic.epgs.gratuity.quotation.valuationmatrix.helper;

import java.util.Date;
import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationBasicDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationMatrixDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationWithdrawalRateDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationBasicEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationMatrixEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationWithdrawalRateEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationBasicEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationMatrixEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationWithdrawalRateEntity;

public class ValuationMatrixHelper {
	public static Long nextReferenceNumber(Long lastReferenceNumber) {
		lastReferenceNumber = lastReferenceNumber == null ? 1 : lastReferenceNumber + 1;
		return lastReferenceNumber;
	}

	public static MasterValuationBasicEntity entityToMasterEntity(ValuationBasicEntity entity) {
		return new ModelMapper().map(entity, MasterValuationBasicEntity.class);
	}

	public static MasterValuationMatrixEntity entityToMasterEntity(ValuationMatrixEntity entity) {
		return new ModelMapper().map(entity, MasterValuationMatrixEntity.class);
	}

	public static MasterValuationWithdrawalRateEntity entityToMasterEntity(ValuationWithdrawalRateEntity entity) {
		return new ModelMapper().map(entity, MasterValuationWithdrawalRateEntity.class);
	}

	public static ValuationMatrixEntity clone(ValuationMatrixEntity valuationMatrixEntity, String username,
			Long quotationId) {
		ValuationMatrixDto valuationMatrixDto = new ModelMapper().map(valuationMatrixEntity, ValuationMatrixDto.class);
		valuationMatrixDto.setId(null);
		ValuationMatrixEntity newValuationMatrixEntity = new ModelMapper().map(valuationMatrixDto,
				ValuationMatrixEntity.class);
		newValuationMatrixEntity.setQuotationId(quotationId);
		newValuationMatrixEntity.setCreatedBy(username);
		newValuationMatrixEntity.setCreatedDate(new Date());
		newValuationMatrixEntity.setIsActive(true);
		return newValuationMatrixEntity;

	}

	public static ValuationBasicEntity clonebasic(ValuationBasicEntity valuationBasicEntity, String username,
			Long quotationId) {
		ValuationBasicDto valuationBasicDto = new ModelMapper().map(valuationBasicEntity, ValuationBasicDto.class);
		valuationBasicDto.setId(null);
		ValuationBasicEntity newValuationBasicEntity = new ModelMapper().map(valuationBasicDto,
				ValuationBasicEntity.class);
		newValuationBasicEntity.setQuotationId(quotationId);
		newValuationBasicEntity.setCreatedBy(username);
		newValuationBasicEntity.setCreatedDate(new Date());
		newValuationBasicEntity.setIsActive(true);
		return newValuationBasicEntity;
	}

	public static ValuationWithdrawalRateEntity clone(ValuationWithdrawalRateEntity getValuationWithdrawalRateEntity,
			String username, Long quotationId) {
		ValuationWithdrawalRateDto valuationWithdrawalRateDto = new ModelMapper().map(getValuationWithdrawalRateEntity,
				ValuationWithdrawalRateDto.class);
		valuationWithdrawalRateDto.setId(null);

		ValuationWithdrawalRateEntity newValuationWithdrawalRateEntity = new ModelMapper()
				.map(valuationWithdrawalRateDto, ValuationWithdrawalRateEntity.class);

		newValuationWithdrawalRateEntity.setQuotationId(quotationId);
		newValuationWithdrawalRateEntity.setCreatedBy(username);
		newValuationWithdrawalRateEntity.setCreatedDate(new Date());
		newValuationWithdrawalRateEntity.setIsActive(true);

		return newValuationWithdrawalRateEntity;
	}

	public static MasterValuationMatrixEntity masterClone(MasterValuationMatrixEntity masterValuationMatrixEntity,
			String username, Long quotationId) {
		ValuationMatrixDto valuationMatrixDto = new ModelMapper().map(masterValuationMatrixEntity,
				ValuationMatrixDto.class);
		valuationMatrixDto.setId(null);

		MasterValuationMatrixEntity newMasterValuationMatrixEntity = new ModelMapper().map(valuationMatrixDto,
				MasterValuationMatrixEntity.class);

		newMasterValuationMatrixEntity.setQuotationId(quotationId);
		newMasterValuationMatrixEntity.setCreatedBy(username);
		newMasterValuationMatrixEntity.setCreatedDate(new Date());
		newMasterValuationMatrixEntity.setIsActive(true);
		return newMasterValuationMatrixEntity;
	}

	public static MasterValuationBasicEntity masterClone(MasterValuationBasicEntity masterValuationBasicEntity,
			String username, Long quotationId) {
		ValuationBasicDto valuationBasicDto = new ModelMapper().map(masterValuationBasicEntity,
				ValuationBasicDto.class);
		valuationBasicDto.setId(null);

		MasterValuationBasicEntity newMasterValuationBasicEntity = new ModelMapper().map(valuationBasicDto,
				MasterValuationBasicEntity.class);
		newMasterValuationBasicEntity.setQuotationId(quotationId);
		newMasterValuationBasicEntity.setCreatedBy(username);
		newMasterValuationBasicEntity.setCreatedDate(new Date());
		newMasterValuationBasicEntity.setIsActive(true);

		return newMasterValuationBasicEntity;
	}

	public static MasterValuationWithdrawalRateEntity masterClone(
			MasterValuationWithdrawalRateEntity getMasterValuationWithdrawalRateEntity, String username,
			Long quotationId) {

		ValuationWithdrawalRateDto valuationWithdrawalRateDto = new ModelMapper()
				.map(getMasterValuationWithdrawalRateEntity, ValuationWithdrawalRateDto.class);
		valuationWithdrawalRateDto.setId(null);

		MasterValuationWithdrawalRateEntity newMasterValuationWithdrawalRateEntity = new ModelMapper()
				.map(valuationWithdrawalRateDto, MasterValuationWithdrawalRateEntity.class);

		newMasterValuationWithdrawalRateEntity.setQuotationId(quotationId);
		newMasterValuationWithdrawalRateEntity.setCreatedBy(username);
		newMasterValuationWithdrawalRateEntity.setCreatedDate(new Date());
		newMasterValuationWithdrawalRateEntity.setIsActive(true);
		return newMasterValuationWithdrawalRateEntity;
	}
	
	public static ValuationBasicDto valuationBasicEntityToDto(ValuationBasicEntity valBasicEntity) {
		ValuationBasicDto get= new ModelMapper().map(valBasicEntity, ValuationBasicDto.class);
		 get.setGratuityAmountCelling((float)Math.round(valBasicEntity.getGratuityAmountCelling()==null?0.0:valBasicEntity.getGratuityAmountCelling()));
		 get.setMaxLifeCoverSumAssured((float)Math.round(valBasicEntity.getMaxLifeCoverSumAssured()==null?0.0:valBasicEntity.getMaxLifeCoverSumAssured()));
		 get.setMaxSalary((float)Math.round(valBasicEntity.getMaxSalary()==null?0.0:valBasicEntity.getMaxSalary()));
		 get.setPastServiceDeath((float)Math.round(valBasicEntity.getPastServiceDeath()==null?0.0:valBasicEntity.getPastServiceDeath()));
		 get.setPastServiceWithdrawal((float)Math.round(valBasicEntity.getPastServiceWithdrawal()==null?0.0:valBasicEntity.getPastServiceWithdrawal()));
		 get.setPastServiceRetirement((float)Math.round(valBasicEntity.getPastServiceRetirement()==null?0.0:valBasicEntity.getPastServiceRetirement()));
		 get.setCurrentServiceDeath((float)Math.round(valBasicEntity.getCurrentServiceDeath()==null?0.0:valBasicEntity.getCurrentServiceDeath()));
		 get.setCurrentServiceWithdrawal((float)Math.round(valBasicEntity.getCurrentServiceWithdrawal()==null?0.0:valBasicEntity.getCurrentServiceWithdrawal()));
		 get.setCurrentServiceRetirement((float)Math.round(valBasicEntity.getCurrentServiceRetirement()==null?0.0:valBasicEntity.getCurrentServiceRetirement()));
		 get.setAccruedGratuity((float)Math.round(valBasicEntity.getAccruedGratuity()==null?0.0:valBasicEntity.getAccruedGratuity()));
		 get.setTotalGratuity((float)Math.round(valBasicEntity.getTotalGratuity()==null?0.0:valBasicEntity.getTotalGratuity()));
		 get.setMaximumSalary((float)Math.round(valBasicEntity.getMaximumSalary()==null?0.0:valBasicEntity.getMaximumSalary()));
		 get.setMinimumSalary((float)Math.round(valBasicEntity.getMinimumSalary()==null?0.0:valBasicEntity.getMinimumSalary()));
		
		return get;
		
		
	}
	
	public static ValuationBasicDto valuationBasicEntityToDto(MasterValuationBasicEntity valBasicEntity) {
		ValuationBasicDto get= new  ModelMapper().map(valBasicEntity, ValuationBasicDto.class);
		 get.setGratuityAmountCelling((float)Math.round(valBasicEntity.getGratuityAmountCelling()==null?0.0:valBasicEntity.getGratuityAmountCelling()));
		 get.setMaxLifeCoverSumAssured((float)Math.round(valBasicEntity.getMaxLifeCoverSumAssured()==null?0.0:valBasicEntity.getMaxLifeCoverSumAssured()));
		 get.setMaxSalary((float)Math.round(valBasicEntity.getMaxSalary()==null?0.0:valBasicEntity.getMaxSalary()));
		 get.setPastServiceDeath((float)Math.round(valBasicEntity.getPastServiceDeath()==null?0.0:valBasicEntity.getPastServiceDeath()));
		 get.setPastServiceWithdrawal((float)Math.round(valBasicEntity.getPastServiceWithdrawal()==null?0.0:valBasicEntity.getPastServiceWithdrawal()));
		 get.setPastServiceRetirement((float)Math.round(valBasicEntity.getPastServiceRetirement()==null?0.0:valBasicEntity.getPastServiceRetirement()));
		 get.setCurrentServiceDeath((float)Math.round(valBasicEntity.getCurrentServiceDeath()==null?0.0:valBasicEntity.getCurrentServiceDeath()));
		 get.setCurrentServiceWithdrawal((float)Math.round(valBasicEntity.getCurrentServiceWithdrawal()==null?0.0:valBasicEntity.getCurrentServiceWithdrawal()));
		 get.setCurrentServiceRetirement((float)Math.round(valBasicEntity.getCurrentServiceRetirement()==null?0.0:valBasicEntity.getCurrentServiceRetirement()));
		 get.setAccruedGratuity((float)Math.round(valBasicEntity.getAccruedGratuity()==null?0.0:valBasicEntity.getAccruedGratuity()));
		 get.setTotalGratuity((float)Math.round(valBasicEntity.getTotalGratuity()==null?0.0:valBasicEntity.getTotalGratuity()));
		 get.setMaximumSalary((float)Math.round(valBasicEntity.getMaximumSalary()==null?0.0:valBasicEntity.getMaximumSalary()));
		 get.setMinimumSalary((float)Math.round(valBasicEntity.getMinimumSalary()==null?0.0:valBasicEntity.getMinimumSalary()));
		
		return get;
		
		
	
	}
	
	public static ValuationMatrixDto valuationMatrixEntityToDto(ValuationMatrixEntity valMatrixentity) {
		ValuationMatrixDto get=	new ModelMapper().map(valMatrixentity, ValuationMatrixDto.class);
		get.setCurrentServiceLiability((float)Math.round(valMatrixentity.getCurrentServiceLiability()==null?0.0:valMatrixentity.getCurrentServiceLiability()));
		get.setTotalSumAssured((double)Math.round(valMatrixentity.getTotalSumAssured()==null?0.0:valMatrixentity.getTotalSumAssured()));
		get.setPastServiceLiability((float)Math.round(valMatrixentity.getPastServiceLiability()==null?0.0:valMatrixentity.getPastServiceLiability()));
		get.setFutureServiceLiability((float)Math.round(valMatrixentity.getFutureServiceLiability()==null?0.0:valMatrixentity.getFutureServiceLiability()));
		get.setPremium((float)Math.round(valMatrixentity.getPremium()==null?0.0:valMatrixentity.getPremium()));
		get.setGst((float)Math.round(valMatrixentity.getGst()==null?0.0:valMatrixentity.getGst()));
		get.setTotalPremium((double)Math.round(valMatrixentity.getTotalPremium()==null?0.0:valMatrixentity.getTotalPremium()));
		get.setAmountReceived((float)Math.round(valMatrixentity.getAmountReceived()==null?0.0:valMatrixentity.getAmountReceived()));
		get.setAmountPayable((float)Math.round(valMatrixentity.getAmountPayable()==null?0.0:valMatrixentity.getAmountPayable()));
		get.setBalanceToBePaid((float)Math.round(valMatrixentity.getBalanceToBePaid()==null?0.0:valMatrixentity.getBalanceToBePaid()));
		
		
		return get;
	}
	
	public static ValuationMatrixDto valuationMatrixEntityToDto(MasterValuationMatrixEntity valMatrixentity) {
		
		ValuationMatrixDto get=	new ModelMapper().map(valMatrixentity, ValuationMatrixDto.class);
		get.setCurrentServiceLiability((float)Math.round(valMatrixentity.getCurrentServiceLiability()==null?0.0:valMatrixentity.getCurrentServiceLiability()));
		get.setTotalSumAssured((double)Math.round(valMatrixentity.getTotalSumAssured()==null?0.0:valMatrixentity.getTotalSumAssured()));
		get.setPastServiceLiability((float)Math.round(valMatrixentity.getPastServiceLiability()==null?0.0:valMatrixentity.getPastServiceLiability()));
		get.setFutureServiceLiability((float)Math.round(valMatrixentity.getFutureServiceLiability()==null?0.0:valMatrixentity.getFutureServiceLiability()));
		get.setPremium((float)Math.round(valMatrixentity.getPremium()==null?0.0:valMatrixentity.getPremium()));
		get.setGst((float)Math.round(valMatrixentity.getGst()==null?0.0:valMatrixentity.getGst()));
		get.setTotalPremium((double)Math.round(valMatrixentity.getTotalPremium()==null?0.0:valMatrixentity.getTotalPremium()));
		get.setAmountReceived((float)Math.round(valMatrixentity.getAmountReceived()==null?0.0:valMatrixentity.getAmountReceived()));
		get.setAmountPayable((float)Math.round(valMatrixentity.getAmountPayable()==null?0.0:valMatrixentity.getAmountPayable()));
		get.setBalanceToBePaid((float)Math.round(valMatrixentity.getBalanceToBePaid()==null?0.0:valMatrixentity.getBalanceToBePaid()));
		
		
		return get;
		
	}
	
	public static ValuationWithdrawalRateDto valuationWithdrawalRateEntityToDto(ValuationWithdrawalRateEntity valWithdrawalRateentity) {
		ValuationWithdrawalRateDto	get=new ModelMapper().map(valWithdrawalRateentity, ValuationWithdrawalRateDto.class);
		get.setRate((float)Math.round(valWithdrawalRateentity.getRate()));	
		
		return get;
	}
	
	public static ValuationWithdrawalRateDto valuationWithdrawalRateEntityToDto(MasterValuationWithdrawalRateEntity valWithdrawalRateentity) {
		
		ValuationWithdrawalRateDto	get=new ModelMapper().map(valWithdrawalRateentity, ValuationWithdrawalRateDto.class);
		get.setRate((float)Math.round(valWithdrawalRateentity.getRate()));	
		
		return get;
		
	}
	
	public static ValuationBasicEntity dtoToEntity1(ValuationBasicDto dto) {
		return new ModelMapper().map(dto, ValuationBasicEntity.class);
	}
	
	public static ValuationMatrixEntity dtoToEntity2(ValuationMatrixDto dto) {
		return new ModelMapper().map(dto, ValuationMatrixEntity.class);
	}

}
