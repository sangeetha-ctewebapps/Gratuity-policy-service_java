package com.lic.epgs.gratuity.policyservices.merger.dto;

import java.io.Serializable;
import java.util.List;

import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policyservices.common.dto.CommonDocsDto;
import com.lic.epgs.gratuity.policyservices.common.dto.CommonNotesDto;
import com.lic.epgs.gratuity.policyservices.common.entity.PolicyServiceDocumentDto;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.PolicyServiceNotesDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class PolicyLevelMergerApiResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<PolicyLevelMergerDto> responseDataList;
	private Object responseData;
	private List<CommonNotesDto> commonNotesDto;
	private List<PolicyLevelMergerSearchDto> policySearchResponseDtoList;
	private List<PolicyServiceDocumentDto>policyServiceDocumentDtoList;
	private PolicyServiceDocumentDto policyServiceDocumentDto;
	private Object responseObject;
	private PolicyLevelMergerDto quotationDto;
	private CommonDocsDto docsDto;
	private List<CommonDocsDto> docsDtoList;
	private List<PolicyServiceNotesDto> mergerNotesDtoList;
	private PolicyServiceNotesDto mergerNote;
	private Long mergeId;
	private String transactionStatus;
	private String transactionMessage;
	
private RenewalPolicyTMPDto sourcePolicyMerge;
	
	private RenewalPolicyTMPDto destPolicyMerge;
	
	private SearchSourcePolicyDto searchSourcePolicyDto;
	
	

}
