package com.lic.epgs.gratuity.policyservices.unit.transfer.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.TaskAllocationEntity;
import com.lic.epgs.gratuity.common.repository.TaskAllocationRepository;
import com.lic.epgs.gratuity.common.repository.TaskProcessRepository;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.AdjustmentPropsDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.DocumentDetailsResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.ExactMatchTransferSearchRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.MemberTransferApprovalRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.NotesResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.PolicyContriDetailDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.PreValidationRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.RejectMemberTransferRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.RemoveDocReq;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.RetainMemberLicIdRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.SaveMemberTransferDetailRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.ServiceMemberTransferDetailsResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferDocUpdateDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferDocumentDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferMemberSearchWithFilterRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferNotesRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferRequisitionModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferSearchWithServiceResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.UpdateTransferMemberModel;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.UploadDocReq;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferNotesEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferPolicyDetailEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferRequisitionEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.model.TransferGratuityCalculationDTO;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.model.TransferInCalculateDTO;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.model.TransferRefundCalculationDTO;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.TransferNotesRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.TransferPolicyDetailRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.TransferRequisitionRepo;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.service.MemberTransferDocumentService;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.service.MemberTransferInOutService;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.SavePolicyUnitTransferDetailRequest;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.UnitDocumentDetailsResponse;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.UnitNotesResponse;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.UnitTransferDocUpdateDto;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.UnitTransferDocumentDto;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.UnitTransferNotesRequest;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.UnitUploadDocReq;
import com.lic.epgs.gratuity.policyservices.unit.transfer.entity.UnitTransferNotesEntity;
import com.lic.epgs.gratuity.policyservices.unit.transferr.repository.UnitTransferNotesRepo;
import com.lic.epgs.gratuity.policyservices.unit.transferr.service.PolicyUnitTransferService;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBulkResponseDto;
import com.lowagie.text.DocumentException;

import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/policyUnitTransfer")
@Log4j2
public class PolicyUnitTransferController {

	@Autowired
	PolicyUnitTransferService policyUnitTransferService;

	@Autowired
	UnitTransferNotesRepo unitTransferNotesRepo;
	
	@PostMapping(value = "/save")
	public ResponseEntity<Map<String, Object>> savePolicyUnitTransfer(
			@RequestBody SavePolicyUnitTransferDetailRequest savePolicyUnitTransferDetailRequest) {
		return policyUnitTransferService.savePolicyUnitTransfer(savePolicyUnitTransferDetailRequest);
	}

	@PostMapping(value = "/saveNotes")
	public Map<String, Object> saveTransferNotes(@RequestBody UnitTransferNotesRequest unitTransferNotesRequest) {
		log.info("Save Transfer Notes controller started");
		return policyUnitTransferService.saveTransferNotes(unitTransferNotesRequest);
	}

	@GetMapping(value = "/getNotes/{unitTransferRequisitionId}")
	public ResponseEntity<?> getnotes(@PathVariable("unitTransferRequisitionId") Long unitTransferRequisitionId) {

		Optional<List<UnitTransferNotesEntity>> transferNotesEntity = Optional.ofNullable(unitTransferNotesRepo
                .findByUnitTransferRequisitionId(unitTransferRequisitionId));
		if (transferNotesEntity.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new UnitNotesResponse("Notes Fetched Successfully", "Success", transferNotesEntity.get()));
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(new UnitNotesResponse("No Notes Available", "Failed", null));
		}
	}

	@PutMapping("updateDocument")
	public ResponseEntity<Map<String, Object>> edit(@RequestBody UnitTransferDocUpdateDto transferDocUpdateDto) {
		return policyUnitTransferService.edit(transferDocUpdateDto);
	}

	@PostMapping(value = "/uploadDocs")
	public Map<String, Object> uploadDocs(@RequestBody UnitUploadDocReq uploadDocReq) throws JsonProcessingException {

		return policyUnitTransferService.uploadDocs(uploadDocReq);

	}

	@PostMapping("/removeUploadedDocs")
	public ResponseEntity<Map<String, Object>> removeUploadedDocs(@RequestBody RemoveDocReq removeDocReq)
			throws JsonProcessingException {

		return policyUnitTransferService.removeUploadedDocs(removeDocReq);
	}

	@PostMapping(value = "/getUploadDocs")
	public ResponseEntity<String> getUploadClaimDocs(@RequestBody RemoveDocReq removeDocReq)
			throws JsonProcessingException {
		return policyUnitTransferService.getUploadDocs(removeDocReq);
	}

	@GetMapping(value = "/getDocumentDetails/{unitTransferRequisitionId}")
	public ResponseEntity<Map<String, Object>> getDocumentDetails(
			@PathVariable("unitTransferRequisitionId") Long unitTransferRequisitionId) {
		log.info("Entering into MemberTransferInOutController : getDocumentDetails");
		Map<String, Object> response = new HashMap<>();
		try {
			List<UnitDocumentDetailsResponse> documentDetailsResponse = policyUnitTransferService
					.getDocumentDetails(unitTransferRequisitionId);
			if (documentDetailsResponse.size() == 0 || documentDetailsResponse.isEmpty()) {
				response.put("responseCode", "failed");
				response.put("responseMessage", "Data Not Found");
			} else {
				response.put("responseCode", "success");
				response.put("responseMessage", "Data Found");
				response.put("documentDetail", documentDetailsResponse);
			}
		} catch (InputMismatchException e) {
			log.info("InputMismatchException : MemberTransferInOutController ");
		} catch (Exception e) {
			log.error("Exception in MemberTransferInOutController. {%s}");
		}
		return ResponseEntity.accepted().body(response);
	}
	
	
	

}