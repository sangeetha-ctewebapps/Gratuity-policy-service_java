package com.lic.epgs.gratuity.policy.surrender.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderDocUpdateDto;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderDocumentDto;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderPaymentRequest;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderPaymentResponse;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderWorkflowRequest;
import com.lic.epgs.gratuity.policy.surrender.dto.UploadSurrenderDocReq;
import com.lic.epgs.gratuity.policy.surrender.entity.PolicySurrender;
import com.lic.epgs.gratuity.policy.surrender.entity.SurrenderDocumentDetailEntity;
import com.lic.epgs.gratuity.policy.surrender.dto.GetSurrenderNotesRequest;
import com.lic.epgs.gratuity.policy.surrender.dto.PolicySurrenderDto;
import com.lic.epgs.gratuity.policy.surrender.dto.PolicySurrenderInProgressSearchDto;
import com.lic.epgs.gratuity.policy.surrender.dto.SaveSurrenderNotesRequest;
import com.lic.epgs.gratuity.policy.surrender.dto.SaveSurrenderPayoutDtlsReq;
import com.lic.epgs.gratuity.policy.surrender.dto.SaveSurrenderRequest;
import com.lic.epgs.gratuity.policy.surrender.dto.SaveSurrenderResponse;
import com.lic.epgs.gratuity.policy.surrender.service.SurrenderService;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.RemoveDocReq;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferDocUpdateDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferDocumentDto;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/surrenderService" })
public class SurrenderController {

	private static final Logger logger = LogManager.getLogger(SurrenderController.class);
	
	@Autowired
	SurrenderService surrenderService;

	@PostMapping(value = "/masterPolicySearch")
	public ApiResponseDto<List<PolicySurrenderDto>> masterPolicySearch(@RequestBody PolicySearchDto policySearchDto) {
		
		logger.info("masterPolicySearch  method starts ....... ");
		return surrenderService.surrenderPolicySearch(policySearchDto);
	}
	
	@PostMapping(value="/saveSurrenderDetails")
	public SaveSurrenderResponse saveSurrenderDetails(@RequestBody SaveSurrenderRequest saveSurrenderRequest) {
		
		logger.info("saveSurrenderDetails method starts ....... ");
		return surrenderService.saveSurrenderDetails(saveSurrenderRequest);
	}
	
	@PostMapping(value="/updateSurrenderDetails")
	public SaveSurrenderResponse updateSurrenderDetails(@RequestBody SaveSurrenderRequest saveSurrenderRequest) {
		
		logger.info("updateSurrenderDetails method starts ....... ");
		return surrenderService.updateSurrenderDetails(saveSurrenderRequest);
	}
	
	@PostMapping("/saveDocument")
	public ResponseEntity<Map<String, Object>> create(@RequestBody SurrenderDocumentDto surrenderDocumentDto) {
		logger.info("saveDocument method starts ....... ");
		return surrenderService.create(surrenderDocumentDto);
	}
	
	@PostMapping("/updateDocument")
	public ResponseEntity<Map<String, Object>> edit(@RequestBody SurrenderDocUpdateDto surrenderDocUpdateDto) {
		logger.info("updateDocument method starts ....... ");
		return surrenderService.edit(surrenderDocUpdateDto);
	}
	
	@PostMapping(value = "/uploadDocs")
	public Map<String, Object> uploadDocs(@RequestBody UploadSurrenderDocReq uploadDocReq) throws JsonProcessingException {
		logger.info("uploadDocs method starts ....... ");
		return surrenderService.uploadDocs(uploadDocReq);

	}

	@PostMapping("/removeUploadedDocs")
	public ResponseEntity<Map<String, Object>> removeUploadedDocs(@RequestBody RemoveDocReq removeDocReq)
			throws JsonProcessingException {
		logger.info("removeUploadedDocs method starts ....... ");
		return surrenderService.removeUploadedDocs(removeDocReq);
	}

	@PostMapping(value = "/getUploadDocs")
	public ResponseEntity<String> getUploadSurrenderDocs(@RequestBody RemoveDocReq removeDocReq)
			throws JsonProcessingException {
		logger.info("getUploadDocs method starts ....... ");
		return surrenderService.getUploadDocs(removeDocReq);
	}
	
	@PostMapping(value="/saveSurrenderNotes")
	public Map<String,Object> saveSurrenderNotes(@RequestBody SaveSurrenderNotesRequest saveSurrenderNotesRequest) {
		
		logger.info("saveSurrenderNotes method starts ....... ");
		return surrenderService.saveSurrenderNotes(saveSurrenderNotesRequest);
	}
	
	@PostMapping(value="/getSurrenderNotes")
	public List<Map<String,Object>> getSurrenderNotes(@RequestBody GetSurrenderNotesRequest getSurrenderNotesRequest) {
		
		logger.info("getSurrenderNotes method starts ....... ");
		return surrenderService.getSurrenderNotes(getSurrenderNotesRequest.getSurrenderId());
	}
	
	//workflow API
	@PostMapping(value="/surrenderWorkflowAction")
	public Map<String,Object> surrenderWorkflowAction(@RequestBody SurrenderWorkflowRequest surrenderWorkflowRequest) {
		
		logger.info("surrenderWorkflowAction method starts ....... ");
		return surrenderService.surrenderWorkflowAction(surrenderWorkflowRequest);
	}
	
	@PostMapping(value = "/surrenderSearch")
	public ApiResponseDto<List<PolicySurrenderDto>> surrenderSearch(@RequestBody PolicySurrenderInProgressSearchDto policySurrenderInProgressSearchDto) {
		
		logger.info("surrenderSearch  method starts ....... ");
		return surrenderService.surrenderSearch(policySurrenderInProgressSearchDto);
	}
	
	@GetMapping(value = "/getSurrenderDetails/{surrenderId}")
	public ApiResponseDto<PolicySurrender> getSurrenderDetails(@PathVariable("surrenderId") Long surrenderId) {
		
		logger.info("getSurrenderDetails  method starts ....... ");
		return surrenderService.getSurrenderDetails(surrenderId);
	}
	
	@GetMapping(value = "/getSurrenderDocumentDetails/{surrenderId}")
	public ApiResponseDto<List<SurrenderDocumentDetailEntity>> getSurrenderDocumentDetails(@PathVariable("surrenderId") Long surrenderId) {
		
		logger.info("getSurrenderDocumentDetails  method starts ....... ");
		return surrenderService.getSurrenderDocumentDetails(surrenderId);
	}
	
	@PostMapping(value = "/getSurrenderPaymentDetails")
	public ApiResponseDto<SurrenderPaymentResponse> getSurrenderPaymentDetails(@RequestBody SurrenderPaymentRequest surrenderPaymentRequest) {
		
		logger.info("getSurrenderPaymentDetails  method starts ....... ");
		return surrenderService.getSurrenderPaymentDetails(surrenderPaymentRequest);
	}
	
	@PostMapping(value="/saveSurrenderPaymentDetails")
	public Map<String,Object> saveSurrenderPaymentDetails(@RequestBody SaveSurrenderPayoutDtlsReq saveSurrenderPayoutDtlsRequest){
		
		logger.info("saveSurrenderPaymentDetails method starts ....... ");
		return surrenderService.saveSurrenderPaymentDetails(saveSurrenderPayoutDtlsRequest);
	}
	
	@GetMapping(value="/callForAccountingAPI/{surrenderId}/{username}/{policyVersion}")
	public Map<String,Object> callForAccountingAPI(@PathVariable("surrenderId") Long surrenderId, @PathVariable("username") String username, @PathVariable("policyVersion") String policyVersion){
		
		logger.info("callForAccountingAPI method starts ....... ");
		return surrenderService.callForAccountingAPI(surrenderId,username,policyVersion);
	}
	
	@PostMapping(value = "/masterPolicySearchPartialSurrender")
	public ApiResponseDto<List<PolicySurrenderDto>> masterPolicySearchPartialSurrender(@RequestBody PolicySearchDto policySearchDto) {
		
		logger.info("masterPolicySearchPartialSurrender  method starts ....... ");
		return surrenderService.masterPolicySearchPartialSurrender(policySearchDto);
	}
}
