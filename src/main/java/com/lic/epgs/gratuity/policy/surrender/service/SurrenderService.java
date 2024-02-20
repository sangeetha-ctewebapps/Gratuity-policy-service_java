package com.lic.epgs.gratuity.policy.surrender.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderDocUpdateDto;
import com.lic.epgs.gratuity.policy.surrender.dto.PolicySurrenderDto;
import com.lic.epgs.gratuity.policy.surrender.dto.PolicySurrenderInProgressSearchDto;
import com.lic.epgs.gratuity.policy.surrender.dto.SaveSurrenderNotesRequest;
import com.lic.epgs.gratuity.policy.surrender.dto.SaveSurrenderPayoutDtlsReq;
import com.lic.epgs.gratuity.policy.surrender.dto.SaveSurrenderRequest;
import com.lic.epgs.gratuity.policy.surrender.dto.SaveSurrenderResponse;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderDocumentDto;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderPaymentRequest;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderPaymentResponse;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderWorkflowRequest;
import com.lic.epgs.gratuity.policy.surrender.dto.UploadSurrenderDocReq;
import com.lic.epgs.gratuity.policy.surrender.entity.PolicySurrender;
import com.lic.epgs.gratuity.policy.surrender.entity.SurrenderDocumentDetailEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.RemoveDocReq;

public interface SurrenderService {

	public ApiResponseDto<List<PolicySurrenderDto>> surrenderPolicySearch(PolicySearchDto policySearchDto);
	public SaveSurrenderResponse saveSurrenderDetails(SaveSurrenderRequest saveSurrenderRequest);
	public SaveSurrenderResponse updateSurrenderDetails(SaveSurrenderRequest saveSurrenderRequest);
	public ResponseEntity<Map<String, Object>> create(SurrenderDocumentDto surrenderDocumentDto);
	public ResponseEntity<Map<String, Object>> edit(SurrenderDocUpdateDto documentDocUpdateDto);
	public Map<String, Object> uploadDocs(UploadSurrenderDocReq uploadDocReq) throws JsonProcessingException;
	public ResponseEntity<Map<String, Object>> removeUploadedDocs(RemoveDocReq removeDocReq) throws JsonMappingException, JsonProcessingException;
	public ResponseEntity<String> getUploadDocs(RemoveDocReq removeDocReq);
	public Map<String,Object> saveSurrenderNotes(SaveSurrenderNotesRequest saveSurrenderNotesRequest);
	public List<Map<String,Object>> getSurrenderNotes(Long surrenderId);
	public Map<String,Object> surrenderWorkflowAction(SurrenderWorkflowRequest surrenderWorkflowRequest);
	public ApiResponseDto<List<PolicySurrenderDto>> surrenderSearch(PolicySurrenderInProgressSearchDto policySurrenderInProgressSearchDto);
	public ApiResponseDto<PolicySurrender> getSurrenderDetails(Long surrenderId);
	public ApiResponseDto<List<SurrenderDocumentDetailEntity>> getSurrenderDocumentDetails(Long surrenderId);
	public ApiResponseDto<SurrenderPaymentResponse> getSurrenderPaymentDetails(SurrenderPaymentRequest surrenderPaymentRequest);
	public Map<String,Object> saveSurrenderPaymentDetails(SaveSurrenderPayoutDtlsReq saveSurrenderPayoutDtlsRequest);
	public Map<String,Object> callForAccountingAPI(Long surrenderId, String username, String policyVersion);
	public ApiResponseDto<List<PolicySurrenderDto>> masterPolicySearchPartialSurrender(PolicySearchDto policySearchDto);
	
}
