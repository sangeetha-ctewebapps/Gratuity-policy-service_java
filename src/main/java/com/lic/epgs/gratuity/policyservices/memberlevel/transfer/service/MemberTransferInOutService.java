package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.service;

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
import com.lic.epgs.gratuity.quotation.member.dto.MemberBulkResponseDto;

public interface MemberTransferInOutService {

	public Map<String, Object> saveTransferNotes(TransferNotesRequest transferNotesRequest);

	ResponseEntity<Map<String, Object>> saveMemberTransfer(
			SaveMemberTransferDetailRequest saveMemberTransferDetailRequest);

	ResponseEntity<?> calculategratuity(TransferGratuityCalculationDTO transferGratuityCalculationDTO);

	ResponseEntity<?> RefundCalculation(TransferRefundCalculationDTO transferRefundCalculationDTO);

	ResponseEntity<?> rejectMemberTransfer(RejectMemberTransferRequest rejectMemberTransferRequest);

	public List<TransferSearchWithServiceResponse> searchWithServiceNumber(
			ExactMatchTransferSearchRequest exactMatchTransferSearchRequest);

	public List<TransferSearchWithServiceResponse> getOverviewDetails(Long transferRequisitionId);

	public List<ServiceMemberTransferDetailsResponse> getSeviceDetails(Long transferRequisitionId);

	ResponseEntity<Map<String, Object>> updateMemberTransfer(Long transferRequisitionId,
			UpdateTransferMemberModel updateTransferMemberModel);

	ResponseEntity<Map<String, Object>> getAllMemberCategoryForTransfer(String type, String entryType, Long id);

	public List<TransferSearchWithServiceResponse> getTransferMemberDetailsWithFilter(
			TransferMemberSearchWithFilterRequest TransferMemberSearchWithFilterRequest);

	ResponseEntity<?> TransferInCalculate(TransferInCalculateDTO transferInCalculateDTO);

	ApiResponseDto<List<PolicyDto>> searchSourcePolicy(PolicySearchDto policySearchDto);

	ApiResponseDto<List<PolicyDto>> searchDestinationPolicy(PolicySearchDto policySearchDto);

	boolean validatePolicyProductVariant(PreValidationRequest preValidationRequest);

	boolean validatePolicyUnit(PreValidationRequest preValidationRequest);

	ResponseEntity<Map<String, Object>> create(TransferDocumentDto transferDocumentDto);

	ResponseEntity<Map<String, Object>> edit(TransferDocUpdateDto transferDocUpdateDto);

	ResponseEntity<Map<String, Object>> getMphDetails(String policyNumber);		
	
//	public ResponseEntity<String> uploadDocs1(String uploadDocReq,String base64String) throws JsonProcessingException;

	public Map<String, Object> uploadDocs(UploadDocReq uploadDocReq) throws JsonProcessingException;

	
	public ResponseEntity<Map<String, Object>> removeUploadedDocs(RemoveDocReq removeDocReq) throws JsonMappingException, JsonProcessingException;

	public ResponseEntity<String> getUploadDocs(RemoveDocReq removeDocReq);
	
	public List<DocumentDetailsResponse> getDocumentDetails(Long transferRequisitionId);
		
	ResponseEntity<Map<String, Object>> transferInApprove(Long transferRequisitionId, String userName) throws JsonProcessingException;

//	public Map<String, Object> sendEmail(Long transferRequisitionId) throws IOException;
		
	ResponseEntity<Map<String, Object>> setMemberLicIdRetention(RetainMemberLicIdRequest retainMemberLicIdRequest);

	ResponseEntity<Map<String, Object>> getCollectionDetails(Long transferRequisitionId);


	ApiResponseDto<MemberBulkResponseDto> uploadTransferMemberData(String username, Long transferId,
			Long masterPolicyId, MultipartFile file);

	ResponseEntity<byte[]> downloadSampleFile();

	ResponseEntity<byte[]> downloadErrorLog(Long transferRequisitionId);

	ResponseEntity<?> getUploadSummary(Long transferRequisitionId);

	ResponseEntity<?> transferInCalculateForBulk(Long transferRequisitionId);

	ResponseEntity<?> generateTransferRequisition(TransferRequisitionModel TransferRequisitionRequest);

    ResponseEntity<?> RefundCalculationBulk(Long transferRequisitionId, TransferRefundCalculationDTO transferRefundCalculationDTO);

	ResponseEntity<?> calculategratuityBulk(Long transferRequisitionId, TransferGratuityCalculationDTO transferGratuityCalculationDTO);
	
	ResponseEntity<MemberBulkResponseDto> deleteBatch(Long batchId);

	ApiResponseDto<AdjustmentPropsDto> transferInSave(PolicyContriDetailDto policyContriDetailDto, Long pmstPolicyId);

	ApiResponseDto<AdjustmentPropsDto> transferInUpdate(PolicyContriDetailDto policyContriDetailDto, Long tmpPolicyId);
}
