package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.controller;

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
import com.lic.epgs.gratuity.quotation.member.dto.MemberBulkResponseDto;
import com.lowagie.text.DocumentException;

import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/memberTransfer")
@Log4j2
public class MemberTransferInOutController {

	@Autowired
	TransferRequisitionRepo transferRequisitionRepo;

	@Autowired
	MemberTransferInOutService memberTransferInOutService;

	@Autowired
	TransferNotesRepo transferNotesRepo;

	@Autowired
	MemberTransferDocumentService memberTransferDocumentService;
	
	@Autowired
	TaskProcessRepository taskProcessRepository;
	
	@Autowired
	TaskAllocationRepository taskAllocationRepository;
	
	@Value("${app.transfer.sendToCheckerTransferOutStatusId}")
	private String transferOutSendToCheckerId;
	
	@Value("${app.transfer.approvedTransferOutStatusId}")
	private String transferOutApproved;
	
	@Value("${app.transfer.sendBackToMakerStatusId}")
	private String sendBackToMakerId;
	
	@Autowired
	TransferPolicyDetailRepo transferPolicyDetailRepo;
	
	
	@PostMapping(value = "/saveNotes")
	public Map<String, Object> saveTransferNotes(@RequestBody TransferNotesRequest transferNotesRequest) {
		log.info("Save Transfer Notes controller started");
		return memberTransferInOutService.saveTransferNotes(transferNotesRequest);
	}

	@GetMapping(value = "/getNotes/{transferRequisitionId}")
	public ResponseEntity<?> getnotes(@PathVariable("transferRequisitionId") Long transferRequisitionId) {

		Optional<List<TransferNotesEntity>> transferNotesEntity = transferNotesRepo
				.findByTransferRequisitionId(transferRequisitionId);
		if (transferNotesEntity.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new NotesResponse("Notes Fetched Successfully", "Success", transferNotesEntity.get()));
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(new NotesResponse("No Notes Available", "Failed", null));
		}
	}

	@PostMapping(value = "/save")
	public ResponseEntity<Map<String, Object>> saveMemberTransfer(
			@RequestBody SaveMemberTransferDetailRequest saveMemberTransferDetailRequest) {
		return memberTransferInOutService.saveMemberTransfer(saveMemberTransferDetailRequest);
	}

	@PutMapping("/update/{transferRequisitionId}")
	public ResponseEntity<Map<String, Object>> updateMemberTransfer(
			@PathVariable(value = "transferRequisitionId") Long transferRequisitionId,
			@RequestBody UpdateTransferMemberModel updateTransferMemberModel) {

		return memberTransferInOutService.updateMemberTransfer(transferRequisitionId, updateTransferMemberModel);
	}

	@PostMapping("calculategratuity-v2")
	public ResponseEntity<?> calculategratuity(
			@RequestBody TransferGratuityCalculationDTO transferGratuityCalculationDTO) {
		return memberTransferInOutService.calculategratuity(transferGratuityCalculationDTO);
	}

	@PostMapping("refundcalculation-v2")
	public ResponseEntity<?> RefundCalculation(@RequestBody TransferRefundCalculationDTO transferRefundCalculationDTO) {
		return memberTransferInOutService.RefundCalculation(transferRefundCalculationDTO);
	}

	@PostMapping(value = "/sendToChecker")
	public ResponseEntity<Map<String, Object>> sendToChecker(
			@RequestBody MemberTransferApprovalRequest memberTransferApproval) {
		String msg = "";
		Long taskProcessId;
		log.info("Entering into MemberTransferInOutController : sendToChecker : "
				+ memberTransferApproval.getTransferRequisitionId());
		Map<String, Object> response = new HashMap<>();
		boolean check;

		/** validate product variant **/
		PreValidationRequest preValidationRequest = new PreValidationRequest();
		preValidationRequest.setEventName("preSendToChecker");
		preValidationRequest.setTransferRequisitionId(memberTransferApproval.getTransferRequisitionId());
		check = memberTransferInOutService.validatePolicyProductVariant(preValidationRequest);
		if (check == false) {
			response.put("responseMessage", "Policy doesn't exist in same Product variant");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		/** validate source unit **/
		PreValidationRequest preValidationRequest1 = new PreValidationRequest();
		preValidationRequest1.setEventName("preSendToChecker");
		preValidationRequest1.setTransferRequisitionId(memberTransferApproval.getTransferRequisitionId());
		check = memberTransferInOutService.validatePolicyUnit(preValidationRequest1);
		if (check == false) {
			response.put("responseMessage", "Policy doesn't exist in same Unit");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		Optional<TransferRequisitionEntity> memberTransferOpt = transferRequisitionRepo
				.findById(memberTransferApproval.getTransferRequisitionId());
		
		TransferPolicyDetailEntity transferPolicyDetailEntity = transferPolicyDetailRepo.findByTransferRequisitionId(memberTransferApproval.getTransferRequisitionId());
		
		if (memberTransferOpt.isPresent()) {
			if (memberTransferOpt.get().getTransferStatus().equalsIgnoreCase("Active")
					|| memberTransferOpt.get().getTransferStatus().equalsIgnoreCase("In-Progress")) {
				try {
					if ("UO".equalsIgnoreCase(memberTransferApproval.getLocationType())
							&& "Maker".equalsIgnoreCase(memberTransferApproval.getRole())
							&& "OUT".equalsIgnoreCase(memberTransferApproval.getTransferType())) {
						transferRequisitionRepo.updateMemberTransferRequisitionApproval(
								memberTransferApproval.getTransferRequisitionId(), "In-Progress",
								"Sent To Checker-Transfer Out", Arrays.asList("ACTIVE", "IN-PROGRESS"), "Checker", "ZO",
								memberTransferApproval.getRole(), memberTransferApproval.getLocationType(),
								memberTransferApproval.getUserName(), null, null);
						taskProcessId =taskProcessRepository.getTaskIdByProcessName("TRANSFER-OUT");
						
						TaskAllocationEntity taskAllocationEntity = taskAllocationRepository.getByRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
					
						if (taskAllocationEntity == null) {
					
						TaskAllocationEntity taskAllocationEntity1 = new TaskAllocationEntity();
						
						taskAllocationEntity1.setTaskStatus(transferOutSendToCheckerId);
						taskAllocationEntity1.setRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
						taskAllocationEntity1.setTaskProcessTaskPrId(taskProcessId);
						taskAllocationEntity1.setLocationType(transferPolicyDetailEntity.getUnitOut());
						taskAllocationEntity1.setCreatedBy(memberTransferApproval.getUserName());
						taskAllocationEntity1.setIsActive(true);
						taskAllocationEntity1.setModulePrimaryId(memberTransferOpt.get().getTransferRequisitionId());
						taskAllocationEntity1.setCreatedDate(new Date());
						
						taskAllocationRepository.save(taskAllocationEntity1);
						log.info(taskAllocationEntity1);
						}
						else
						{
							TaskAllocationEntity taskAllocationEntityList = taskAllocationRepository.getByRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
							taskAllocationEntityList.setTaskStatus(transferOutSendToCheckerId);
							taskAllocationEntityList.setRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
							taskAllocationEntityList.setTaskProcessTaskPrId(taskProcessId);
							taskAllocationEntityList.setLocationType(transferPolicyDetailEntity.getUnitOut());
							taskAllocationEntityList.setCreatedBy(memberTransferApproval.getUserName());
							taskAllocationEntityList.setIsActive(true);
							taskAllocationEntityList.setModulePrimaryId(memberTransferOpt.get().getTransferRequisitionId());
							taskAllocationEntityList.setCreatedDate(new Date());
							taskAllocationRepository.save(taskAllocationEntityList);
							log.info(taskAllocationEntityList);
						}
						
						msg = "Transfer Request No : " + memberTransferOpt.get().getTransferRequestNumber()
								+ " has been sent to ZO checker for approval";
						// Add history later
					} else if ("UO".equalsIgnoreCase(memberTransferApproval.getLocationType())
							&& "Maker".equalsIgnoreCase(memberTransferApproval.getRole())
							&& "IN".equalsIgnoreCase(memberTransferApproval.getTransferType())) {
						transferRequisitionRepo.updateMemberTransferRequisitionApproval(
								memberTransferApproval.getTransferRequisitionId(), "In-Progress",
								"Sent To Checker-Transfer In", Arrays.asList("ACTIVE", "IN-PROGRESS"), "Checker", "ZO",
								memberTransferApproval.getRole(), memberTransferApproval.getLocationType(),
								memberTransferApproval.getUserName(), null, null);
						
						taskProcessId =taskProcessRepository.getTaskIdByProcessName("TRANSFER-IN");
						TaskAllocationEntity taskAllocationEntity = taskAllocationRepository.getByRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
						
						if (taskAllocationEntity == null) {
						
						
						TaskAllocationEntity taskAllocationEntity1 = new TaskAllocationEntity();
						
						
						taskAllocationEntity1.setTaskStatus(transferOutSendToCheckerId);
						taskAllocationEntity1.setRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
						taskAllocationEntity1.setTaskProcessTaskPrId(taskProcessId);
						taskAllocationEntity1.setLocationType(transferPolicyDetailEntity.getUnitIn());
						taskAllocationEntity1.setCreatedBy(memberTransferApproval.getUserName());
						taskAllocationEntity1.setIsActive(true);
						taskAllocationEntity1.setModulePrimaryId(memberTransferOpt.get().getTransferRequisitionId());
						taskAllocationEntity1.setCreatedDate(new Date());
						
						taskAllocationRepository.save(taskAllocationEntity1);
						}
						else
						{
							TaskAllocationEntity taskAllocationEntityList = taskAllocationRepository.getByRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
							taskAllocationEntityList.setTaskStatus(transferOutSendToCheckerId);
							taskAllocationEntityList.setRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
							taskAllocationEntityList.setTaskProcessTaskPrId(taskProcessId);
							taskAllocationEntityList.setLocationType(transferPolicyDetailEntity.getUnitIn());
							taskAllocationEntityList.setCreatedBy(memberTransferApproval.getUserName());
							taskAllocationEntityList.setIsActive(true);
							taskAllocationEntityList.setModulePrimaryId(memberTransferOpt.get().getTransferRequisitionId());
							taskAllocationEntityList.setCreatedDate(new Date());
							taskAllocationRepository.save(taskAllocationEntityList);
						}
						
						
						msg = "Transfer Request No : " + memberTransferOpt.get().getTransferRequestNumber()
								+ " has been sent to ZO checker for approval";
						// Add history later
					}
					if (StringUtils.isNotBlank(msg)) {
						response.put("responseCode", "success");
						response.put("transferStatus", "Sent To Checker");
						response.put("responseMessage", msg);
					} else {
						response.put("responseCode", "failed");
						response.put("responseMessage", "No condition satisfied for approval on  "
								+ memberTransferApproval.getRole() + " : " + memberTransferApproval.getLocationType());
					}
				} catch (Exception e) {
					log.error("Entering into MemberTransferInOutController : sendToChecker : " + e.getMessage());
					response.put("Status", "Failed");
					response.put("responseMessage", "Send To Checker for approval has Failed due to " + e.getMessage());
					return ResponseEntity.accepted().body(response);
				}
			} else {
				response.put("responseCode", "failed");
				response.put("responseMessage", "Transfer Request is not in Active or In Progress state");
			}
		} else {
			response.put("responseCode", "failed");
			response.put("responseMessage", "Transfer Request does not exist for Approval");
		}
		return ResponseEntity.accepted().body(response);
	}

	@PostMapping(value = "/approval")
	public ResponseEntity<Map<String, Object>> approval(
			@RequestBody MemberTransferApprovalRequest memberTransferApproval) {
		String msg = "";
		Long taskProcessId;
		log.info("Entering into MemberTransferInOutController : approval : "
				+ memberTransferApproval.getTransferRequisitionId());
		Map<String, Object> response = new HashMap<>();
		boolean check;

		/** validate product variant **/
		PreValidationRequest preValidationRequest = new PreValidationRequest();
		preValidationRequest.setEventName("preSendToChecker");
		preValidationRequest.setTransferRequisitionId(memberTransferApproval.getTransferRequisitionId());
		check = memberTransferInOutService.validatePolicyProductVariant(preValidationRequest);
		if (check == false) {
			response.put("responseMessage", "Policy doesn't exist in same Product variant");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		/** validate source unit **/
		PreValidationRequest preValidationRequest1 = new PreValidationRequest();
		preValidationRequest1.setEventName("preSendToChecker");
		preValidationRequest1.setTransferRequisitionId(memberTransferApproval.getTransferRequisitionId());
		check = memberTransferInOutService.validatePolicyUnit(preValidationRequest1);
		if (check == false) {
			response.put("responseMessage", "Policy doesn't exist in same Unit");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		Optional<TransferRequisitionEntity> memberTransferOpt = transferRequisitionRepo
				.findById(memberTransferApproval.getTransferRequisitionId());
		
		TransferPolicyDetailEntity transferPolicyDetailEntity = transferPolicyDetailRepo.findByTransferRequisitionId(memberTransferApproval.getTransferRequisitionId());

		if (memberTransferOpt.isPresent()) {
			if (memberTransferOpt.get().getTransferStatus().equals("Active")
					|| memberTransferOpt.get().getTransferStatus().equals("In-Progress")) {
				try {
					if ("ZO".equalsIgnoreCase(memberTransferApproval.getLocationType())
							&& "Checker".equalsIgnoreCase(memberTransferApproval.getRole())
							&& "OUT".equalsIgnoreCase(memberTransferApproval.getTransferType())) {
						transferRequisitionRepo.updateMemberTransferRequisitionApproval(
								memberTransferApproval.getTransferRequisitionId(), "In-Progress",
								"Approved-Transfer Out", Arrays.asList("IN-PROGRESS"), "Maker", "UO",
								memberTransferApproval.getRole(), memberTransferApproval.getLocationType(),
								memberTransferApproval.getUserName(), memberTransferApproval.getUserName(), new Date());
						
						taskProcessId =taskProcessRepository.getTaskIdByProcessName("TRANSFER-OUT");
						
                        TaskAllocationEntity taskAllocationEntity = taskAllocationRepository.getByRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
						
						if (taskAllocationEntity == null) {
						TaskAllocationEntity taskAllocationEntity1 = new TaskAllocationEntity();
						
						
						taskAllocationEntity1.setTaskStatus(transferOutApproved);
						taskAllocationEntity1.setRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
						taskAllocationEntity1.setTaskProcessTaskPrId(taskProcessId);
						taskAllocationEntity1.setLocationType(transferPolicyDetailEntity.getUnitOut());
						taskAllocationEntity1.setCreatedBy(memberTransferApproval.getUserName());
						taskAllocationEntity1.setIsActive(true);
						taskAllocationEntity1.setModulePrimaryId(memberTransferOpt.get().getTransferRequisitionId());
						taskAllocationEntity1.setCreatedDate(new Date());
						
						taskAllocationRepository.save(taskAllocationEntity1);
						}
						else 
						{
	                        TaskAllocationEntity taskAllocationEntityList = taskAllocationRepository.getByRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));

	                        taskAllocationEntityList.setTaskStatus(transferOutApproved);
	                        taskAllocationEntityList.setRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
	                        taskAllocationEntityList.setTaskProcessTaskPrId(taskProcessId);
	                        taskAllocationEntityList.setLocationType(transferPolicyDetailEntity.getUnitOut());
	                        taskAllocationEntityList.setCreatedBy(memberTransferApproval.getUserName());
	                        taskAllocationEntityList.setIsActive(true);
							taskAllocationEntityList.setModulePrimaryId(memberTransferOpt.get().getTransferRequisitionId());
							taskAllocationEntityList.setCreatedDate(new Date());
							taskAllocationRepository.save(taskAllocationEntityList);
						}
						
						msg = "Transfer Request No : " + memberTransferOpt.get().getTransferRequestNumber()
								+ " has been approved";
						// Add history later
					} else if ("ZO".equalsIgnoreCase(memberTransferApproval.getLocationType())
							&& "Checker".equalsIgnoreCase(memberTransferApproval.getRole())
							&& "IN".equalsIgnoreCase(memberTransferApproval.getTransferType())) {

						/**Approval**/
						memberTransferInOutService.transferInApprove(memberTransferApproval.getTransferRequisitionId(), memberTransferApproval.getUserName());
						
						transferRequisitionRepo.updateMemberTransferRequisitionApproval(
								memberTransferApproval.getTransferRequisitionId(), "In-Progress",
								"Approved-Transfer In", Arrays.asList("IN-PROGRESS"), "Maker", "UO",
								memberTransferApproval.getRole(), memberTransferApproval.getLocationType(),
								memberTransferApproval.getUserName(), memberTransferApproval.getUserName(), new Date());
						
						
						taskProcessId =taskProcessRepository.getTaskIdByProcessName("TRANSFER-IN");
						
						 TaskAllocationEntity taskAllocationEntity = taskAllocationRepository.getByRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
							
							if (taskAllocationEntity == null) {
						TaskAllocationEntity taskAllocationEntity1 = new TaskAllocationEntity();
						
						
						taskAllocationEntity1.setTaskStatus(transferOutApproved);
						taskAllocationEntity1.setRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
						taskAllocationEntity1.setTaskProcessTaskPrId(taskProcessId);
						taskAllocationEntity1.setLocationType(transferPolicyDetailEntity.getUnitIn());
						taskAllocationEntity1.setCreatedBy(memberTransferApproval.getUserName());
						taskAllocationEntity1.setIsActive(true);
						taskAllocationEntity1.setModulePrimaryId(memberTransferOpt.get().getTransferRequisitionId());
						taskAllocationEntity1.setCreatedDate(new Date());
						
						taskAllocationRepository.save(taskAllocationEntity1);
						
							}
							else {
								 TaskAllocationEntity taskAllocationEntityList = taskAllocationRepository.getByRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));

								 taskAllocationEntityList.setTaskStatus(transferOutApproved);
								 taskAllocationEntityList.setRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
								 taskAllocationEntityList.setTaskProcessTaskPrId(taskProcessId);
								 taskAllocationEntityList.setLocationType(transferPolicyDetailEntity.getUnitIn());
								 taskAllocationEntityList.setCreatedBy(memberTransferApproval.getUserName());
								 taskAllocationEntityList.setIsActive(true);
								 taskAllocationEntityList.setModulePrimaryId(memberTransferOpt.get().getTransferRequisitionId());
								 taskAllocationEntityList.setCreatedDate(new Date());
								 taskAllocationRepository.save(taskAllocationEntityList);
							}
						
						
						
						
						msg = "Transfer Request No : " + memberTransferOpt.get().getTransferRequestNumber()
								+ " has been approved";
						// Add history later
					}

					if (StringUtils.isNotBlank(msg)) {
						response.put("responseCode", "success");
						response.put("transferStatus", "Approved");
						response.put("responseMessage", msg);
					} else {
						response.put("responseCode", "failed");
						response.put("responseMessage", "No condition satisfied for approval on  "
								+ memberTransferApproval.getRole() + " : " + memberTransferApproval.getLocationType());
					}
				} catch (Exception e) {
					log.error("Entering into MemberTransferInOutController : approval : " + e.getMessage());
					response.put("Status", "Failed");
					response.put("responseMessage", "Approval has Failed due to " + e.getMessage());
					return ResponseEntity.accepted().body(response);
				}
			} else {
				response.put("responseCode", "failed");
				response.put("responseMessage", "Transfer Request is not in Active or In Progress state");
			}
		} else {
			response.put("responseCode", "failed");
			response.put("responseMessage", "Transfer Request does not exist for Approval");
		}
		return ResponseEntity.accepted().body(response);
	}

	@PostMapping(value = "/sendBackToMaker")
	public ResponseEntity<Map<String, Object>> sendBackToMaker(
			@RequestBody MemberTransferApprovalRequest memberTransferApproval) {
		log.info("Entering into MemberTransferInOutController : sendBackToMaker : "
				+ memberTransferApproval.getTransferRequisitionId());
		Map<String, Object> response = new HashMap<String, Object>();
		boolean check;
		Long taskProcessId;

		/** validate product variant **/
		PreValidationRequest preValidationRequest = new PreValidationRequest();
		preValidationRequest.setEventName("preSendBackToMaker");
		preValidationRequest.setTransferRequisitionId(memberTransferApproval.getTransferRequisitionId());
		check = memberTransferInOutService.validatePolicyProductVariant(preValidationRequest);
		if (check == false) {
			response.put("responseMessage", "Policy doesn't exist in same Product variant");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		/** validate source unit **/
		PreValidationRequest preValidationRequest1 = new PreValidationRequest();
		preValidationRequest1.setEventName("preSendToChecker");
		preValidationRequest1.setTransferRequisitionId(memberTransferApproval.getTransferRequisitionId());
		check = memberTransferInOutService.validatePolicyUnit(preValidationRequest1);
		if (check == false) {
			response.put("responseMessage", "Policy doesn't exist in same Unit");
			response.put("responseCode", "failed");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		String msg = "";
		Optional<TransferRequisitionEntity> memberTransferOpt = transferRequisitionRepo
				.findById(memberTransferApproval.getTransferRequisitionId());
		
		TransferPolicyDetailEntity transferPolicyDetailEntity = transferPolicyDetailRepo.findByTransferRequisitionId(memberTransferApproval.getTransferRequisitionId());

		if (memberTransferOpt.isPresent()) {
			if (memberTransferOpt.get().getTransferStatus().equals("In-Progress")) {
				try {

					if ("ZO".equalsIgnoreCase(memberTransferApproval.getLocationType())
							&& "Checker".equalsIgnoreCase(memberTransferApproval.getRole())
							&& "OUT".equalsIgnoreCase(memberTransferApproval.getTransferType())) {
						transferRequisitionRepo.updateMemberTransferRequisitionApproval(
								memberTransferApproval.getTransferRequisitionId(), "In-Progress",
								"Sent Back To Maker-Transfer Out", Arrays.asList("IN-PROGRESS"), "Maker", "UO",
								memberTransferApproval.getRole(), memberTransferApproval.getLocationType(),
								memberTransferApproval.getUserName(), null, null);
						
						taskProcessId =taskProcessRepository.getTaskIdByProcessName("TRANSFER-OUT");
						
						TaskAllocationEntity taskAllocationEntity = taskAllocationRepository.getByRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
							
							if (taskAllocationEntity == null) {
						TaskAllocationEntity taskAllocationEntity1 = new TaskAllocationEntity();
						
						
						taskAllocationEntity1.setTaskStatus(sendBackToMakerId);
						taskAllocationEntity1.setRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
						taskAllocationEntity1.setTaskProcessTaskPrId(taskProcessId);
						taskAllocationEntity1.setLocationType(transferPolicyDetailEntity.getUnitOut());
						taskAllocationEntity1.setCreatedBy(memberTransferApproval.getUserName());
						taskAllocationEntity1.setIsActive(true);
						taskAllocationEntity1.setModulePrimaryId(memberTransferOpt.get().getTransferRequisitionId());
						taskAllocationEntity1.setCreatedDate(new Date());
						
						taskAllocationRepository.save(taskAllocationEntity1);
							}
							else
							{
								 TaskAllocationEntity taskAllocationEntityList = taskAllocationRepository.getByRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
								
								 taskAllocationEntityList.setTaskStatus(sendBackToMakerId);
								 taskAllocationEntityList.setRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
								 taskAllocationEntityList.setTaskProcessTaskPrId(taskProcessId);
								 taskAllocationEntityList.setLocationType(transferPolicyDetailEntity.getUnitOut());
								 taskAllocationEntityList.setCreatedBy(memberTransferApproval.getUserName());
								 taskAllocationEntityList.setIsActive(true);
								 taskAllocationEntityList.setModulePrimaryId(memberTransferOpt.get().getTransferRequisitionId());
								 taskAllocationEntityList.setCreatedDate(new Date());
								 
								 taskAllocationRepository.save(taskAllocationEntityList);
							}
						
						msg = "Transfer Request No : " + memberTransferOpt.get().getTransferRequestNumber()
								+ " has been sent back to UO maker";
						// Add history later
					} else if ("ZO".equalsIgnoreCase(memberTransferApproval.getLocationType())
							&& "Checker".equalsIgnoreCase(memberTransferApproval.getRole())
							&& "IN".equalsIgnoreCase(memberTransferApproval.getTransferType())) {
						transferRequisitionRepo.updateMemberTransferRequisitionApproval(
								memberTransferApproval.getTransferRequisitionId(), "In-Progress",
								"Sent Back To Maker-Transfer In", Arrays.asList("IN-PROGRESS"), "Maker", "UO",
								memberTransferApproval.getRole(), memberTransferApproval.getLocationType(),
								memberTransferApproval.getUserName(), null, null);
						
						taskProcessId =taskProcessRepository.getTaskIdByProcessName("TRANSFER-IN");
						
						 TaskAllocationEntity taskAllocationEntity = taskAllocationRepository.getByRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
							
							if (taskAllocationEntity == null) {
						TaskAllocationEntity taskAllocationEntity1 = new TaskAllocationEntity();
						
						
						taskAllocationEntity1.setTaskStatus(sendBackToMakerId);
						taskAllocationEntity1.setRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
						taskAllocationEntity1.setTaskProcessTaskPrId(taskProcessId);
						taskAllocationEntity1.setLocationType(transferPolicyDetailEntity.getUnitIn());
						taskAllocationEntity1.setCreatedBy(memberTransferApproval.getUserName());
						taskAllocationEntity1.setIsActive(true);
						taskAllocationEntity1.setModulePrimaryId(memberTransferOpt.get().getTransferRequisitionId());
						taskAllocationEntity1.setCreatedDate(new Date());
						
						taskAllocationRepository.save(taskAllocationEntity1);
					} else {
						TaskAllocationEntity taskAllocationEntityList = taskAllocationRepository
								.getByRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
						taskAllocationEntityList.setTaskStatus(sendBackToMakerId);
						taskAllocationEntityList
								.setRequestId(String.valueOf(memberTransferOpt.get().getTransferRequestNumber()));
						taskAllocationEntityList.setTaskProcessTaskPrId(taskProcessId);
						taskAllocationEntityList.setLocationType(transferPolicyDetailEntity.getUnitIn());
						taskAllocationEntityList.setCreatedBy(memberTransferApproval.getUserName());
						taskAllocationEntityList.setIsActive(true);
						taskAllocationEntityList.setModulePrimaryId(memberTransferOpt.get().getTransferRequisitionId());
						taskAllocationEntityList.setCreatedDate(new Date());

						taskAllocationRepository.save(taskAllocationEntityList);
					}						
						
						msg = "Transfer Request No : " + memberTransferOpt.get().getTransferRequestNumber()
								+ " has been sent back to UO maker";
						// Add history later
					}

					if (StringUtils.isNotBlank(msg)) {
						response.put("responseCode", "success");
						response.put("transferStatus", "Sent Back To Maker");
						response.put("responseMessage", msg);
						return ResponseEntity.accepted().body(response);

					} else {
						response.put("responseCode", "failed");
						response.put("responseMessage", "No Condition satisfied for Send Back on  "
								+ memberTransferApproval.getRole() + " :" + memberTransferApproval.getLocationType());
						return ResponseEntity.accepted().body(response);
					}
				} catch (Exception e) {
					log.error(
							"Exception occured in MemberTransferInOutController : sendBackToMaker :" + e.getMessage());
					response.put("responseCode", "failed");
					response.put("responseMessage", "Sending back to UO maker failed due to " + e.getMessage());
					return ResponseEntity.accepted().body(response);
				}
			} else {
				response.put("responseCode", "failed");
				response.put("responseMessage", "Transfer Request is not in In progress state to send back to maker");
				return ResponseEntity.accepted().body(response);
			}
		} else {
			response.put("responseCode", "failed");
			response.put("responseMessage", "Transfer Request does not exist for sending back to maker");
			return ResponseEntity.accepted().body(response);
		}
	}

	@PostMapping(value = "/reject")
	public ResponseEntity<?> rejectMemberTransfer(
			@RequestBody RejectMemberTransferRequest rejectMemberTransferRequest) {
		return memberTransferInOutService.rejectMemberTransfer(rejectMemberTransferRequest);
	}

	@GetMapping(value = "/getOverviewDetails/{transferRequisitionId}")
	public ResponseEntity<Map<String, Object>> getOverviewDetails(
			@PathVariable("transferRequisitionId") Long transferRequisitionId) {
		log.info("Entering into MemberTransferInOutController : getOverviewDetails");
		Map<String, Object> response = new HashMap<>();
		try {
			List<TransferSearchWithServiceResponse> transferSearchWithServiceResponse = memberTransferInOutService
					.getOverviewDetails(transferRequisitionId);
			if (transferSearchWithServiceResponse.size() == 0 || transferSearchWithServiceResponse.isEmpty()) {
				response.put("responseCode", "failed");
				response.put("responseMessage", "Data Not Found");
			} else {
				response.put("responseCode", "success");
				response.put("responseMessage", "Data Found");
				response.put("overviewDetail", transferSearchWithServiceResponse.get(0));
			}
		} catch (InputMismatchException e) {
			log.info("InputMismatchException : MemberTransferInOutController ");
		} catch (Exception e) {
			log.error("Exception in MemberTransferInOutController. {%s}");
		}
		return ResponseEntity.accepted().body(response);
	}

	@GetMapping(value = "/getSeviceDetails/{transferRequisitionId}")
	public ResponseEntity<Map<String, Object>> getSeviceDetails(
			@PathVariable("transferRequisitionId") Long transferRequisitionId) {
		log.info("Entering into MemberTransferInOutController : getSeviceDetails");
		Map<String, Object> response = new HashMap<>();
		try {
			List<ServiceMemberTransferDetailsResponse> serviceMemberTransferDetailsResponse = memberTransferInOutService
					.getSeviceDetails(transferRequisitionId);
			if (serviceMemberTransferDetailsResponse.size() == 0 || serviceMemberTransferDetailsResponse.isEmpty()) {
				response.put("responseCode", "failed");
				response.put("responseMessage", "Data Not Found");
			} else {
				response.put("responseCode", "success");
				response.put("responseMessage", "Data Found");
				response.put("overviewDetail", serviceMemberTransferDetailsResponse.get(0));
			}
		} catch (InputMismatchException e) {
			log.info("InputMismatchException : MemberTransferInOutController ");
		} catch (Exception e) {
			log.error("Exception in MemberTransferInOutController. {%s}");
		}
		return ResponseEntity.accepted().body(response);
	}

	@GetMapping(value = "{entrytype}/{type}/{id}")
	public ResponseEntity<Map<String, Object>> getAllMemberCategoryForTransfer(
			@PathVariable("entrytype") String entrytype, @PathVariable("type") String type,
			@PathVariable("id") Long id) {
		return memberTransferInOutService.getAllMemberCategoryForTransfer(entrytype, type, id);
	}

	@PostMapping(value = "/searchWithServiceNumber")
	public ResponseEntity<Map<String, Object>> searchWithServiceNumber(
			@RequestBody ExactMatchTransferSearchRequest exactMatchTransferSearchRequest) {
		log.info("Entering into MemberTransferInOutController : searchWithServiceNumber");
		Map<String, Object> response = new HashMap<>();

		try {
			List<TransferSearchWithServiceResponse> transferSearchWithServiceResponse = memberTransferInOutService
					.searchWithServiceNumber(exactMatchTransferSearchRequest);
			if (transferSearchWithServiceResponse.size() == 0 || transferSearchWithServiceResponse.isEmpty()) {
				response.put("responseCode", "failed");
				response.put("responseMessage", "Data Not Found");
				response.put("memberDetail", transferSearchWithServiceResponse);
			} else {
				response.put("responseCode", "success");
				response.put("responseMessage", "Data Found");
				response.put("memberDetail", transferSearchWithServiceResponse);
			}
		} catch (InputMismatchException e) {
			log.info("InputMismatchException : MemberTransferInOutController ");
		} catch (Exception e) {
			log.error("Exception in MemberTransferInOutController. {%s}");
		}
		return ResponseEntity.accepted().body(response);
	}

	@PostMapping(value = "/searchWithFilter")
	public ResponseEntity<Map<String, Object>> getTransferMemberDetailsWithFilter(
			@RequestBody TransferMemberSearchWithFilterRequest TransferMemberSearchWithFilterRequest) {
		log.info("Entering into MemberTransferInOutController : searchWithFilter");
		Map<String, Object> response = new HashMap<>();
		try {
			List<TransferSearchWithServiceResponse> transferSearchWithServiceResponse = memberTransferInOutService
					.getTransferMemberDetailsWithFilter(TransferMemberSearchWithFilterRequest);
			if (transferSearchWithServiceResponse.size() == 0 || transferSearchWithServiceResponse.isEmpty()) {
				response.put("responseCode", "failed");
				response.put("responseMessage", "Data Not Found");
				response.put("memberDetail", transferSearchWithServiceResponse);
			} else {
				response.put("responseCode", "success");
				response.put("responseMessage", "Data Found");
				response.put("memberDetail", transferSearchWithServiceResponse);
			}
		} catch (InputMismatchException e) {
			log.info("InputMismatchException : searchWithFilter ");
		} catch (Exception e) {
			log.error("Exception in MemberTransferInOutController. {%s}");
		}
		return ResponseEntity.accepted().body(response);
	}

	@PostMapping(value = "/searchSourcePolicy")
	public ApiResponseDto<List<PolicyDto>> searchSourcePolicy(@RequestBody PolicySearchDto policySearchDto) {
		return memberTransferInOutService.searchSourcePolicy(policySearchDto);
	}

	@PostMapping(value = "/searchDestinationPolicy")
	public ApiResponseDto<List<PolicyDto>> searchDestinationPolicy(@RequestBody PolicySearchDto policySearchDto) {
		return memberTransferInOutService.searchDestinationPolicy(policySearchDto);
	}

	@PostMapping("TransferInCalculate")
	public ResponseEntity<?> TransferInCalculate(@RequestBody TransferInCalculateDTO transferInCalculateDTO) {
		return memberTransferInOutService.TransferInCalculate(transferInCalculateDTO);
	}

	@PostMapping("saveDocument")
	public ResponseEntity<Map<String, Object>> create(@RequestBody TransferDocumentDto transferDocumentDto) {
		return memberTransferInOutService.create(transferDocumentDto);
	}

	
	@PutMapping("updateDocument")
	public ResponseEntity<Map<String, Object>> edit(@RequestBody TransferDocUpdateDto transferDocUpdateDto) {
		return memberTransferInOutService.edit(transferDocUpdateDto);
	}

	@GetMapping("getMphDetails/{policyNumber}")
	public ResponseEntity<Map<String, Object>> getMphDetailsById(@PathVariable("policyNumber") String policyNumber) {
		return memberTransferInOutService.getMphDetails(policyNumber);
	}

	@PostMapping(value = "/uploadDocs")
	public Map<String, Object> uploadDocs(@RequestBody UploadDocReq uploadDocReq) throws JsonProcessingException {

		return memberTransferInOutService.uploadDocs(uploadDocReq);

	}

	@PostMapping("/removeUploadedDocs")
	public ResponseEntity<Map<String, Object>> removeUploadedDocs(@RequestBody RemoveDocReq removeDocReq)
			throws JsonProcessingException {

		return memberTransferInOutService.removeUploadedDocs(removeDocReq);
	}

	@PostMapping(value = "/getUploadDocs")
	public ResponseEntity<String> getUploadClaimDocs(@RequestBody RemoveDocReq removeDocReq)
			throws JsonProcessingException {
		return memberTransferInOutService.getUploadDocs(removeDocReq);
	}

	@GetMapping(value = "/getDocumentDetails/{transferRequisitionId}")
	public ResponseEntity<Map<String, Object>> getDocumentDetails(
			@PathVariable("transferRequisitionId") Long transferRequisitionId) {
		log.info("Entering into MemberTransferInOutController : getDocumentDetails");
		Map<String, Object> response = new HashMap<>();
		try {
			List<DocumentDetailsResponse> documentDetailsResponse = memberTransferInOutService
					.getDocumentDetails(transferRequisitionId);
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

	@PostMapping("transferIn/save/{masterPolicyId}")
	public ApiResponseDto<AdjustmentPropsDto> transferInSave(@RequestBody PolicyContriDetailDto policyContriDetailDto,
			@PathVariable(value = "masterPolicyId") Long masterPolicyId) {
		return memberTransferInOutService.transferInSave(policyContriDetailDto, masterPolicyId);
	}
	
	@PutMapping("updateTransferIn/{tmpPolicyId}")
	public ApiResponseDto<AdjustmentPropsDto> transferInUpdate(
		 	@RequestBody PolicyContriDetailDto policyContriDetailDto,@PathVariable (value="tmpPolicyId")Long tmpPolicyId) {
		return memberTransferInOutService.transferInUpdate(policyContriDetailDto,tmpPolicyId);
	}	
	
	@PostMapping("transferIn/approve/{transferRequisitionId}/{userName}")
	public ResponseEntity<Map<String, Object>> transferInApprove(@PathVariable("transferRequisitionId") Long transferRequisitionId,
			@PathVariable("userName") String userName) throws JsonProcessingException{
	
		return memberTransferInOutService.transferInApprove(transferRequisitionId, userName);
	}

	@PostMapping(value = "/downloadPdf/{transferRequisitionId}")
	@ResponseBody
	public Map<String, Object> downloadPdf(@PathVariable(value = "transferRequisitionId") Long transferRequisitionId)
			throws IOException, DocumentException {

		return memberTransferDocumentService.downloadPdf(transferRequisitionId);
	}
	
//	@PostMapping(value = "/sendEmail/{transferRequisitionId}")
//	@ResponseBody
//	public Map<String, Object> sendEmail(@PathVariable(value = "transferRequisitionId") Long transferRequisitionId)
//			throws IOException, DocumentException {
//		return memberTransferInOutService.sendEmail(transferRequisitionId);
//	}
	

	
	@PostMapping("setMemberLicIdRetention")
	public ResponseEntity<?> setMemberLicIdRetention(@RequestBody RetainMemberLicIdRequest retainMemberLicIdRequest) {
		return memberTransferInOutService.setMemberLicIdRetention(retainMemberLicIdRequest);
	}
	
	@GetMapping("getCollectionDetails/{transferRequisitionId}")
	public ResponseEntity<?> getCollectionDetails(@PathVariable("transferRequisitionId") Long transferRequisitionId) {
		return memberTransferInOutService.getCollectionDetails(transferRequisitionId);
	}
	
	@PostMapping("bulk/generateTransferRequisition")
	public ResponseEntity<?> generateTransferRequisition(@RequestBody TransferRequisitionModel transferRequisitionRequest) {
		return memberTransferInOutService.generateTransferRequisition(transferRequisitionRequest);	 	
	}
		
	@PostMapping("bulk/upload")
	public ApiResponseDto<MemberBulkResponseDto> uploadMemberData(@RequestParam String username,
			@RequestParam Long transferId, @RequestParam Long masterPolicyId, @RequestBody MultipartFile file)
					throws IllegalStateException, IOException {
		return memberTransferInOutService.uploadTransferMemberData(username, transferId, masterPolicyId, file);
	}
		
	@GetMapping("bulk/download/sample")
	public ResponseEntity<byte[]> downloadSample() {
		return memberTransferInOutService.downloadSampleFile();		
	}
		
	@DeleteMapping("delete/{batchId}")
	public ResponseEntity<MemberBulkResponseDto> deleteBatch(@PathVariable("batchId") Long batchId) {
		return memberTransferInOutService.deleteBatch(batchId);
	}
		
	@GetMapping("bulk/download/errorlog/{transferRequisitionId}")
	public ResponseEntity<byte[]> downloadErrorLog(@PathVariable("transferRequisitionId") Long transferRequisitionId) {
		return memberTransferInOutService.downloadErrorLog(transferRequisitionId);		
	}
	
	@GetMapping("bulk/getUploadSummary/{transferRequisitionId}")
	public ResponseEntity<?> getUploadSummary(@PathVariable("transferRequisitionId") Long transferRequisitionId) {
		return memberTransferInOutService.getUploadSummary(transferRequisitionId);
	}
		
	@GetMapping("bulk/transferInCalculate/{transferRequisitionId}")
	public ResponseEntity<?> transferInCalculateForBulk(@PathVariable("transferRequisitionId") Long transferRequisitionId) {
		return memberTransferInOutService.transferInCalculateForBulk(transferRequisitionId);
	}

	@PostMapping("bulk/refundcalculation-v2/{transferRequisitionId}")
	public ResponseEntity<?> RefundCalculation(@PathVariable("transferRequisitionId") Long transferRequisitionId,@RequestBody TransferRefundCalculationDTO transferRefundCalculationDTO) {
		return memberTransferInOutService.RefundCalculationBulk(transferRequisitionId,transferRefundCalculationDTO);
	}

	@PostMapping("bulk/calculategratuity-v2/{transferRequisitionId}")
	public ResponseEntity<?> calculategratuity(@PathVariable("transferRequisitionId") Long transferRequisitionId,@RequestBody TransferGratuityCalculationDTO transferGratuityCalculationDTO) {
		return memberTransferInOutService.calculategratuityBulk(transferRequisitionId,transferGratuityCalculationDTO);
	}
	
}