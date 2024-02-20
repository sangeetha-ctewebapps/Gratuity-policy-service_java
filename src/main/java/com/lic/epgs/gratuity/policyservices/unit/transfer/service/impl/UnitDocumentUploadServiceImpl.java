package com.lic.epgs.gratuity.policyservices.unit.transfer.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.lic.epgs.gratuity.common.entity.PickListItemEntity;
import com.lic.epgs.gratuity.common.repository.PickListItemRepository;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.OmniDocsRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.UploadDocReq;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferPolicyDetailEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.TransferPolicyDetailRepo;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.UnitUploadDocReq;
import com.lic.epgs.gratuity.policyservices.unit.transfer.entity.UnitTransferPolicyDetailEntity;
import com.lic.epgs.gratuity.policyservices.unit.transferr.repository.UnitTransferPolicyDetailRepo;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UnitDocumentUploadServiceImpl {

	// RestTemplate restTemplate2 = new RestTemplate(getClientHttpRequestFactory());
	@Autowired
	PickListItemRepository pickListItemRepository;

	@Autowired
	UnitTransferPolicyDetailRepo unitTransferPolicyDetailRepo;

	@Autowired
	RestTemplate restTemplate;

	public ResponseEntity<String> uploadClaimDocs(UnitUploadDocReq uploadDocReq) throws JsonProcessingException {

		// String urlBalance =
		// "https://D1UTVRRPGCA01.licindia.com:8443/omnidocservice/LIC_ePGS/uploadDocwithBase64";
		String urlBalance = "https://uat-epgs.licindia.com/omnidocservice/LIC_ePGS/uploadDocwithBase64";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add("infra", "epgs");

		ResponseEntity<String> uploadStatus = null;

		OmniDocsRequest omniDocRequest = new OmniDocsRequest();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

		Optional<PickListItemEntity> pickListItem = pickListItemRepository
				.findById(Long.valueOf(uploadDocReq.getPicklistItemId()));
		omniDocRequest.setDocumentType(pickListItem.get().getName());
		omniDocRequest.setIdentifier("Gratuity Transfer");
		omniDocRequest.setOther("");
		omniDocRequest.setUserName(uploadDocReq.getCreatedBy());
		omniDocRequest.setClaimOnBoardingNo("");
		UnitTransferPolicyDetailEntity transferPolicyNo = unitTransferPolicyDetailRepo
				.findByUnitTransferRequisitionId(uploadDocReq.getUnitTransferRequisitionId());
		omniDocRequest.setMasterPolicyNo(transferPolicyNo.getPolicyNumber().toString());

		String jsonBalance = ow.writeValueAsString(omniDocRequest);

		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("omniDocRequest", jsonBalance);
		map.add("file", uploadDocReq.getFileBase64());

		log.info("Json Request" + jsonBalance);

		HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				map, headers);

		uploadStatus = restTemplate.exchange(urlBalance, HttpMethod.POST, entity, String.class);

		log.info("Searching Member  " + uploadStatus.getBody().toString());

		return uploadStatus;

	}

	public ResponseEntity<String> removeUploadedDoc(String folderNo, String documentIndexNo) {

		String urlBalance = "https://D1STVRRPGCA01.licindia.com:8443/omnidocservice/LIC_ePGS/removeDocument/" + folderNo
				+ "/" + documentIndexNo;

		HttpHeaders headers = new HttpHeaders();
		headers.add("infra", "epgs");

		ResponseEntity<String> uploadStatus = null;

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		uploadStatus = restTemplate.exchange(urlBalance, HttpMethod.GET, entity, String.class);

		log.info("Response From OmniDocs Service  " + uploadStatus.getBody().toString());

		return uploadStatus;

	}

	public ResponseEntity<String> getUploadedDocs(String documentIndexNo) {

		String urlBalance = "https://D1STVRRPGCA01.licindia.com:8443/omnidocservice/LIC_ePGS/viewDocument/"
				+ documentIndexNo;

		HttpHeaders headers = new HttpHeaders();
		headers.add("infra", "epgs");

		ResponseEntity<String> uploadStatus = null;

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		uploadStatus = restTemplate.exchange(urlBalance, HttpMethod.GET, entity, String.class);

		return uploadStatus;

	}
}
