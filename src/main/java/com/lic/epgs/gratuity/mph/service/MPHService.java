package com.lic.epgs.gratuity.mph.service;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.mph.dto.MPHEntityDto;
import com.lic.epgs.gratuity.mph.dto.TempMPHAddressDto;
import com.lic.epgs.gratuity.mph.dto.TempMPHBankDto;
import com.lic.epgs.gratuity.mph.dto.TemptMPHDto;

public interface MPHService {

	ApiResponseDto<MPHEntityDto> findById(Long mphId, String type);

	ApiResponseDto<TemptMPHDto> tempMPHSave(Long endoresementId, TemptMPHDto temptMPHDto);

	ApiResponseDto<TempMPHAddressDto> tempMPHAddressSave(Long endoresementId, TempMPHAddressDto tempMPHAddressDto);

	ApiResponseDto<TemptMPHDto> saveMph(Long temppolicyId, TempMPHBankDto tempMphBankdto);

	ApiResponseDto<TemptMPHDto> deleteMphBank(Long mphbankId);

	ApiResponseDto<TemptMPHDto> updateMph(Long mphId, TempMPHBankDto tempMPHBankDto);

	ApiResponseDto<TemptMPHDto> viewMph(Long tempPolicyId);

	

	

}
