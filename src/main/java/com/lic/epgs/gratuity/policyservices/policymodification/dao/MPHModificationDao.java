package com.lic.epgs.gratuity.policyservices.policymodification.dao;

import java.util.List;

import com.lic.epgs.gratuity.policyservices.policymodification.dto.TempPolicyModificationDataTableRequestDto;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.TempPolicyModificationDataTableResponseDto;

public interface MPHModificationDao {

	List<TempPolicyModificationDataTableResponseDto> tempPolicyModificationDataTableResponseDtoList(
			TempPolicyModificationDataTableRequestDto tempPolicyModificationDataTableRequestDto, Long statusId);

	List<TempPolicyModificationDataTableResponseDto> getPolicyModifcationExisting(
			TempPolicyModificationDataTableRequestDto tempPolicyModificationDataTableRequestDto, Long draft,
			Long pendingForApproval);

	List<TempPolicyModificationDataTableResponseDto> getPolicyModifcationMakerInprogress(
			TempPolicyModificationDataTableRequestDto tempPolicyModificationDataTableRequestDto, Long draft,
			Long pendingForApproval, Long sendToMaker);

}
