package com.lic.epgs.gratuity.policy.claim.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.lic.epgs.gratuity.common.entity.CommonMasterUnitEntity;
import com.lic.epgs.gratuity.policy.claim.dto.ClaimPayoutDto;
import com.lic.epgs.gratuity.policy.claim.dto.ClaimPayoutItemDto;
import com.lic.epgs.gratuity.policy.claim.dto.ClaimPropSearchDetailDto;
import com.lic.epgs.gratuity.policy.claim.dto.PdfClaimForwardLetter;
import com.lic.epgs.gratuity.policy.claim.dto.PdfGeneratorDto;
import com.lic.epgs.gratuity.policy.claim.dto.PdfGeneratorForTableDto;
import com.lic.epgs.gratuity.policy.claim.dto.TempPolicyClaimBeneficiaryDto;
import com.lic.epgs.gratuity.policy.claim.dto.TempPolicyClaimPropsDto;
import com.lic.epgs.gratuity.policy.claim.entity.PolicyTempSearchEntity;
import com.lic.epgs.gratuity.policy.claim.entity.TempMemberSearchEntity;
import com.lic.epgs.gratuity.policy.claim.entity.TempPolicyClaimBeneficiaryEntity;
import com.lic.epgs.gratuity.policy.claim.entity.TempPolicyClaimPropsEntity;
import com.lic.epgs.gratuity.policy.claim.entity.TempPolicyClaimPropsSearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.dto.AOCMDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;

public class PolicyClaimHelper {

	public static TempPolicyClaimPropsDto claimpropstoDto(TempPolicyClaimPropsDto tempPolicyClaimPropsDto) {
		
		return new ModelMapper().map(tempPolicyClaimPropsDto, TempPolicyClaimPropsDto.class);
	}
	
	public static TempPolicyClaimPropsDto entityToDto(TempPolicyClaimPropsEntity tempPolicyClaimPropsEntity) {
		return new ModelMapper().map(tempPolicyClaimPropsEntity, TempPolicyClaimPropsDto.class);
	}
	

	

public static TempPolicyClaimBeneficiaryEntity dtotoentity(TempPolicyClaimBeneficiaryDto tempPolicyClaimBeneficiaryDto) {
	
	return new ModelMapper().map(tempPolicyClaimBeneficiaryDto, TempPolicyClaimBeneficiaryEntity.class);
}

public static TempPolicyClaimBeneficiaryDto beneficiaryEntityToDto(TempPolicyClaimBeneficiaryEntity tempPolicyClaimBeneficiaryEntity) {
	
	
	return new ModelMapper().map(tempPolicyClaimBeneficiaryEntity, TempPolicyClaimBeneficiaryDto.class);
}

public static List<ClaimPropSearchDetailDto> getObjectToDto(List<Object[]> getTempPolicySearchList) {
	
	List<ClaimPropSearchDetailDto> getClaimPropSearchDetail=new ArrayList<ClaimPropSearchDetailDto>();
	for (Object[] obj : getTempPolicySearchList) {
		
//		for (int i = 0; i < obj.length; i++) {
			
		ClaimPropSearchDetailDto newClaimPropSearchDetail=new ClaimPropSearchDetailDto();
			System.out.println(obj[8]);
			System.out.println(obj[9]);
			
			newClaimPropSearchDetail.setOnboardNumber(obj[0] == null ?null:obj[0].toString());
			newClaimPropSearchDetail.setPolicyNumber(obj[1] == null ? null :obj[1].toString());
			newClaimPropSearchDetail.setMphCode(obj[2] == null ? null:  obj[2].toString());
			newClaimPropSearchDetail.setLicId(obj[3] == null ? null:obj[3].toString());
			newClaimPropSearchDetail.setEmployeeCode(obj[4] == null ? null: obj[4].toString());
			newClaimPropSearchDetail.setClaimType(obj[5] == null ? null: obj[5].toString());
			newClaimPropSearchDetail.setModeOfExitName(obj[6]==null ? null : obj[6].toString());

			newClaimPropSearchDetail.setClaimStatusName(obj[7]==null ? null : obj[7].toString());
			newClaimPropSearchDetail.setInitimationNumber(obj[8] == null ?null:obj[8].toString());
			newClaimPropSearchDetail.setPayoutNo(obj[9] == null ?null:obj[9].toString());
			getClaimPropSearchDetail.add(newClaimPropSearchDetail);
//		}
						
	
	}
	return getClaimPropSearchDetail;
	
	
	
}

public static ClaimPayoutDto valuationObjtoDto(List<Object[]> claimreport) {

	ClaimPayoutDto claimPayoutDto = new ClaimPayoutDto();
	List<ClaimPayoutItemDto> claimPayoutItemDto = new ArrayList<ClaimPayoutItemDto>();
	
	for (Object[] obj : claimreport) {
		
		ClaimPayoutItemDto newClaimPayoutItemDto = new ClaimPayoutItemDto();
		
		claimPayoutDto.setClaimPropsId(obj[0] ==null?0l:Long.parseLong(obj[0].toString()));
		claimPayoutDto.setPolicyNumber(obj[1] ==null?"":obj[1].toString());
		claimPayoutDto.setUnitCode(obj[2] ==null?"":obj[2].toString());
		claimPayoutDto.setProductVariatId(obj[3] ==null?0: Long.parseLong(obj[3].toString()));
		claimPayoutDto.setMphName(obj[4] ==null?"":obj[4].toString());
//		newClaimPayoutItemDto.setPickListItemDescription(obj[5].toString());
//		newClaimPayoutItemDto.setAmountOfClaims((obj[6] == null ? 0: Double.parseDouble(obj[6].toString())));
		claimPayoutDto.setPayoutDate((Date) obj[7]);
		claimPayoutDto.setProductId(obj[8] ==null?0: Long.parseLong(obj[8].toString()));

		claimPayoutItemDto.add(newClaimPayoutItemDto);
		
}
		claimPayoutDto.setClaimPayoutItemDto(claimPayoutItemDto);
		return claimPayoutDto;
}

public static PdfGeneratorDto claimpdfDto(List<Object[]> claimGenerationPdf) {
	
	PdfGeneratorDto pdfGeneratorDto = new PdfGeneratorDto();
	List<PdfGeneratorForTableDto> pdfGeneratorForTableDto = new ArrayList<PdfGeneratorForTableDto>();
	
	for (Object[] obj : claimGenerationPdf) {
		
		PdfGeneratorForTableDto pdfGeneratorForTableDtos = new PdfGeneratorForTableDto();
		
		
		pdfGeneratorDto.setPolicyNumber(obj[0] ==null?"":obj[0].toString());
		pdfGeneratorDto.setPayoutNumber(obj[1] ==null?"":obj[1].toString());
		pdfGeneratorDto.setUnitCode(obj[2] ==null?"":obj[2].toString());
		pdfGeneratorDto.setPayoutDate((Date) obj[3]);
		pdfGeneratorDto.setMphName(obj[4] ==null?"":obj[4].toString());
		pdfGeneratorDto.setMphAddress1(obj[5] ==null?"":obj[5].toString());
		pdfGeneratorDto.setMphAddress2(obj[6] ==null?"":obj[6].toString());
		pdfGeneratorDto.setMphAddress3(obj[7] ==null?"":obj[7].toString());
		pdfGeneratorDto.setCity(obj[8] ==null?"":obj[8].toString());
		pdfGeneratorDto.setState(obj[9] ==null?"":obj[9].toString());
		pdfGeneratorDto.setPincode(obj[10] ==null?"":obj[10].toString());
		pdfGeneratorDto.setBankName(obj[11] ==null?"":obj[11].toString());
		pdfGeneratorDto.setAccNo(obj[12] ==null?"":obj[12].toString());
		pdfGeneratorDto.setIfcsCode(obj[13] ==null?"":obj[13].toString());

		pdfGeneratorForTableDtos.setLicId(obj[14] ==null?"":obj[14].toString());
		pdfGeneratorForTableDtos.setEmpCode(obj[15] ==null?"":obj[15].toString());
		pdfGeneratorForTableDtos.setEmpName(obj[16] ==null?"":obj[16].toString());
		pdfGeneratorForTableDtos.setLcSumAssured(obj[17] ==null?0.0:Double.parseDouble(obj[17].toString()));
		pdfGeneratorForTableDtos.setSvMatWthd(obj[18] ==null?0.0:Double.parseDouble(obj[18].toString()));
		pdfGeneratorForTableDtos.setRefund(obj[19] ==null?0.0:Double.parseDouble(obj[19].toString()));
//		pdfGeneratorForTableDtos.setOthAmt(obj[20] ==null?"":obj[20].toString());

		pdfGeneratorForTableDto.add(pdfGeneratorForTableDtos);
	}
	pdfGeneratorDto.setPdfGeneratorForTable(pdfGeneratorForTableDto);
	return pdfGeneratorDto;
}

public static CommonMasterUnitEntity claimforward(CommonMasterUnitEntity pdfClaimForward) {
	
	CommonMasterUnitEntity pdfClaimForwardLetter = new CommonMasterUnitEntity();
	
	pdfClaimForwardLetter.setDescription(pdfClaimForward.getDescription());
	pdfClaimForwardLetter.setAddress1(pdfClaimForward.getAddress1());
	pdfClaimForwardLetter.setAddress2(pdfClaimForward.getAddress2());
	pdfClaimForwardLetter.setAddress3(pdfClaimForward.getAddress3());
	pdfClaimForwardLetter.setCityName(pdfClaimForward.getCityName());
	pdfClaimForwardLetter.setStateName(pdfClaimForward.getStateName());
	pdfClaimForwardLetter.setPincode(pdfClaimForward.getPincode());
	pdfClaimForwardLetter.setGstin(pdfClaimForward.getGstin());

	return pdfClaimForwardLetter;
}




public static ClaimPropSearchDetailDto entityToDtoFilter(TempPolicyClaimPropsSearchEntity singleTempPolicyClaimPropsEntity) {
return new ModelMapper().map(singleTempPolicyClaimPropsEntity, ClaimPropSearchDetailDto.class);
}

public static 	List<ClaimPayoutItemDto> claimvalObjtoDto(List<Object[]> claimPayoutItem) {

	
	
	List<ClaimPayoutItemDto> claimPayoutItemDto = new ArrayList<ClaimPayoutItemDto>();
	
	for (Object[] obj : claimPayoutItem) {
		
		ClaimPayoutItemDto claimPayoutItemDtos = new ClaimPayoutItemDto();
		claimPayoutItemDtos.setDebitcode(obj[0] ==null?"":obj[0].toString());
		claimPayoutItemDtos.setDebitCodeDescribtion(obj[1] ==null?"":obj[1].toString());
		claimPayoutItemDtos.setDebitAmount(obj[2] ==null?0.0:Double.parseDouble(obj[2].toString()));
		claimPayoutItemDtos.setCreditcode(obj[3] ==null?"":obj[3].toString());
		claimPayoutItemDtos.setCreditCodeDescribtion(obj[4] ==null?"":obj[4].toString());
		claimPayoutItemDtos.setCreditAmount(obj[5] ==null?0.0:Double.parseDouble(obj[5].toString()));
		
		claimPayoutItemDto.add(claimPayoutItemDtos);
	}

	return claimPayoutItemDto;
}

public static ClaimPayoutDto voucherObjtoDto(List<Object[]> claimreport) {

	ClaimPayoutDto claimPayoutDto = new ClaimPayoutDto();
	List<ClaimPayoutItemDto> claimPayoutItemDto = new ArrayList<ClaimPayoutItemDto>();
	
	for (Object[] obj : claimreport) {
		
		ClaimPayoutItemDto newClaimPayoutItemDto = new ClaimPayoutItemDto();
		
		claimPayoutDto.setPolicyNumber(obj[0] ==null?"":obj[0].toString());
		claimPayoutDto.setUnitCode(obj[1] ==null?"":obj[1].toString());
		claimPayoutDto.setProductId(obj[2] ==null?0: Long.parseLong(obj[2].toString()));
		claimPayoutDto.setMphName(obj[3] ==null?"":obj[3].toString());
		claimPayoutDto.setContributionDetailId(obj[4] ==null?0: Long.parseLong(obj[4].toString()));
		claimPayoutDto.setChallanNo(obj[5] ==null?0: Long.parseLong(obj[5].toString()));
		claimPayoutDto.setChallanDate((Date) obj[6]);
		
		claimPayoutItemDto.add(newClaimPayoutItemDto);
		
}
		claimPayoutDto.setClaimPayoutItemDto(claimPayoutItemDto);
		return claimPayoutDto;
	
}

public static TempPolicyClaimPropsDto entityToDtoFilter(PolicyTempSearchEntity singleTempPolicyClaimPropsEntity) {
	return new ModelMapper().map(singleTempPolicyClaimPropsEntity, TempPolicyClaimPropsDto.class);
}

public static ClaimPropSearchDetailDto entityToDtoFilter(TempMemberSearchEntity tempMphSearchEntity) {
	return new ModelMapper().map(tempMphSearchEntity, ClaimPropSearchDetailDto.class);
}

public static TempPolicyClaimPropsDto memberentityToDtoFilter(TempMemberSearchEntity singleTempPolicyClaimPropsEntity) {
	
	return new ModelMapper().map(singleTempPolicyClaimPropsEntity, TempPolicyClaimPropsDto.class);
}

public static TempPolicyClaimPropsDto claimpropstoDto(TempPolicyClaimPropsEntity tempPolicyClaimPropsEntity) {

	return new ModelMapper().map(tempPolicyClaimPropsEntity, TempPolicyClaimPropsDto.class);
}

}
