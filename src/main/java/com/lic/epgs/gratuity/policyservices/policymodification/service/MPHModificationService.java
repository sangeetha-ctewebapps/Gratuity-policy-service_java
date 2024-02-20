package com.lic.epgs.gratuity.policyservices.policymodification.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.ApiValidationResponse;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policyservices.common.entity.TempPolicyServiceNotes;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.DataTable;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.MPHModificationResponse;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.StatusDto;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.TempPolicyModificationDataTableRequestDto;

public interface MPHModificationService {

	Object findById(String policyNumber, Long mphId);

	ApiValidationResponse saveTempMphDetails(MPHModificationResponse mPHModificationResponse);

	StatusDto updateTempMphDetails(String policyNumber, String status);

	DataTable getPolicyModifcationDetailsDataTable(
			TempPolicyModificationDataTableRequestDto TempPolicyModificationDataTableRequestDto, Long pendingForApproval);

//	MPHModificationResponseDto getMPHForChecker(String policyNumber, Long mphId);

	Object getPolicyModifcationMakerInprogress(
			TempPolicyModificationDataTableRequestDto tempPolicyModificationDataTableRequestDto);

	Object getPolicyModifcationExisting(
			TempPolicyModificationDataTableRequestDto tempPolicyModificationDataTableRequestDto);

	MPHModificationResponse getByPolicyId(Long policyId);

	Object saveNotes(TempPolicyServiceNotes tempPolicyServiceNotes);

	Object getListOfNotes(Long tempPolicyId, String policyNumber);

	ApiResponseDto<List<PolicyDto>> filter(PolicySearchDto policySearchDto);

}
