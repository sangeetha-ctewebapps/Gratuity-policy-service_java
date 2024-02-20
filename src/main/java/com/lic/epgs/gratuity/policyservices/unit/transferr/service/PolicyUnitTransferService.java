package com.lic.epgs.gratuity.policyservices.unit.transferr.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.AdjustmentPropsDto;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.DocumentDetailsResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.ExactMatchTransferSearchRequest;
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
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.model.TransferGratuityCalculationDTO;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.model.TransferInCalculateDTO;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.model.TransferRefundCalculationDTO;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.SavePolicyUnitTransferDetailRequest;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.UnitDocumentDetailsResponse;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.UnitTransferDocUpdateDto;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.UnitTransferNotesRequest;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.UnitUploadDocReq;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBulkResponseDto;

public interface PolicyUnitTransferService {

	ResponseEntity<Map<String, Object>> savePolicyUnitTransfer(
			SavePolicyUnitTransferDetailRequest savePolicyUnitTransferDetailRequest);

	public Map<String, Object> saveTransferNotes(UnitTransferNotesRequest unitTransferNotesRequest);

	ResponseEntity<Map<String, Object>> edit(UnitTransferDocUpdateDto transferDocUpdateDto);

	public Map<String, Object> uploadDocs(UnitUploadDocReq uploadDocReq) throws JsonProcessingException;

	public ResponseEntity<Map<String, Object>> removeUploadedDocs(RemoveDocReq removeDocReq)
			throws JsonMappingException, JsonProcessingException;

	public ResponseEntity<String> getUploadDocs(RemoveDocReq removeDocReq);

	public List<UnitDocumentDetailsResponse> getDocumentDetails(Long unitTransferRequisitionId);

}