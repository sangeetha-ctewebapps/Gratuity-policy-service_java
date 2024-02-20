/**
 * 
 */
package com.lic.epgs.gratuity.common.service.impl;

import java.io.IOException;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.service.IntegrationService;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBulkResponseDto;

/**
 * @author Uthayakumar Chelliah
 *
 */

@Service
@Log4j2
public class IntegrationServiceImpl  implements IntegrationService {
	

	
	@Autowired
	@Qualifier("restTemplateService")
	private RestTemplate restTemplateService;
	
	
	@Value("${app.bulkServiceEndPoint}")
	private String bulkServiceEndPoint;

//Aom
	@Override
	public ApiResponseDto<MemberBulkResponseDto> uploadMemberDataAOMService(Long pmstpolicyId, String username,
			MultipartFile file) {
		ApiResponseDto<MemberBulkResponseDto> response = new ApiResponseDto<MemberBulkResponseDto>();
		try {
			String sparkUrl = bulkServiceEndPoint+"/api/member/uploadaom";
			response.setMessage("spark error");
			response.setStatus("fail");
			log.info("IntegrationServiceImpl-bulkMemberUpload-Start");
 
			byte[] fileContent = null;
			
			try {
				fileContent = IOUtils.toByteArray(file.getInputStream());
			} catch (IOException e) {
				log.info("IntegrationServiceImpl-bulkMemberUpload-Error:", e);
				response.setMessage("Multipart file is empty!");
				response.setStatus("fail");
				return response;
			}
 
			
			String filename = file.getOriginalFilename();
 
			MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
			ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
					.filename(filename).build();
 
			fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
			HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileContent, fileMap);
 
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", fileEntity);
			body.add("masterPolicyId", pmstpolicyId);
			body.add("username", username);
 
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
 
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
 
			ResponseEntity<ApiResponseDto> responseEntity = restTemplateService.postForEntity(sparkUrl,
					requestEntity, ApiResponseDto.class);
 
			if (responseEntity.getBody() != null) {
				return responseEntity.getBody();
			}
 
		} catch (IllegalArgumentException e) {
			log.info("IntegrationServiceImpl-bulkMemberUpload-Error:", e);
		}
		return response;
	}

	
	
	@Override
	public ApiResponseDto<MemberBulkResponseDto> uploadMemberData(Long quotationId, String username,Long tmpPolicyId,
			MultipartFile file) {
		ApiResponseDto<MemberBulkResponseDto> response = new ApiResponseDto<MemberBulkResponseDto>();
		try {
			String sparkUrl = bulkServiceEndPoint+"/api/member/upload";
			response.setMessage("spark error");
			response.setStatus("fail");
			log.info("IntegrationServiceImpl-bulkMemberUpload-Start");

			byte[] fileContent = null;
			
			try {
				fileContent = IOUtils.toByteArray(file.getInputStream());
			} catch (IOException e) {
				log.info("IntegrationServiceImpl-bulkMemberUpload-Error:", e);
				response.setMessage("Multipart file is empty!");
				response.setStatus("fail");
				return response;
			}

			
			String filename = file.getOriginalFilename();

			MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
			ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
					.filename(filename).build();

			fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
			HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileContent, fileMap);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", fileEntity);
			body.add("quotationId", quotationId);
			body.add("username", username);
			body.add("tmpPolicyId", tmpPolicyId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			ResponseEntity<ApiResponseDto> responseEntity = restTemplateService.postForEntity(sparkUrl,
					requestEntity, ApiResponseDto.class);

			if (responseEntity.getBody() != null) {
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			log.info("IntegrationServiceImpl-bulkMemberUpload-Error:", e);
		}
		return response;
	}


	@Override
	public ApiResponseDto<MemberBulkResponseDto> uploadMemberDataClaim(Long pmstpolicyId, String username,
			MultipartFile file) {
		ApiResponseDto<MemberBulkResponseDto> response = new ApiResponseDto<MemberBulkResponseDto>();
		try {
			String sparkUrl = bulkServiceEndPoint+"/api/member/uploadClaim";
			response.setMessage("spark error");
			response.setStatus("fail");
			log.info("IntegrationServiceImpl-bulkMemberUpload-Start");

			byte[] fileContent = null;
			
			try {
				fileContent = IOUtils.toByteArray(file.getInputStream());
			} catch (IOException e) {
				log.info("IntegrationServiceImpl-bulkMemberUpload-Error:", e);
				response.setMessage("Multipart file is empty!");
				response.setStatus("fail");
				return response;
			}

			
			String filename = file.getOriginalFilename();

			MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
			ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
					.filename(filename).build();

			fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
			HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileContent, fileMap);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", fileEntity);
			body.add("pmstpolicyId", pmstpolicyId);
			body.add("username", username);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			ResponseEntity<ApiResponseDto> responseEntity = restTemplateService.postForEntity(sparkUrl,
					requestEntity, ApiResponseDto.class);

			if (responseEntity.getBody() != null) {
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			log.info("IntegrationServiceImpl-bulkMemberUpload-Error:", e);
		}
		return response;
	}


//Dom
	@Override
	public ApiResponseDto<MemberBulkResponseDto> uploadMemberDataDOMService(Long pmstpolicyId, String username,
			MultipartFile file) {
		
		ApiResponseDto<MemberBulkResponseDto> response = new ApiResponseDto<MemberBulkResponseDto>();
		try {
			String sparkUrl = bulkServiceEndPoint+"/api/member/uploaddom";
			response.setMessage("spark error");
			response.setStatus("fail");
			log.info("IntegrationServiceImpl-bulkMemberUpload-Start");
 
			byte[] fileContent = null;
			
			try {
				fileContent = IOUtils.toByteArray(file.getInputStream());
			} catch (IOException e) {
				log.info("IntegrationServiceImpl-bulkMemberUpload-Error:", e);
				response.setMessage("Multipart file is empty!");
				response.setStatus("fail");
				return response;
			}
 
			
			String filename = file.getOriginalFilename();
 
			MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
			ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
					.filename(filename).build();
 
			fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
			HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileContent, fileMap);
 
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", fileEntity);
			body.add("masterPolicyId", pmstpolicyId);
			body.add("username", username);
 
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
 
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
 
			ResponseEntity<ApiResponseDto> responseEntity = restTemplateService.postForEntity(sparkUrl,
					requestEntity, ApiResponseDto.class);
 
			if (responseEntity.getBody() != null) {
				return responseEntity.getBody();
			}
 
		} catch (IllegalArgumentException e) {
			log.error("IntegrationServiceImpl-bulkMemberUpload-Error:", e);
		}
		return response;
	}

}
