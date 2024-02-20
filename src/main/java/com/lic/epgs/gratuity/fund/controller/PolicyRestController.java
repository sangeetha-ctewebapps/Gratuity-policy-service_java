package com.lic.epgs.gratuity.fund.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.fund.dto.DocumentDto;
import com.lic.epgs.gratuity.fund.dto.JsonResponse;
import com.lic.epgs.gratuity.fund.dto.PolicyFundStmtRequestDto;
import com.lic.epgs.gratuity.fund.dto.PolicyFundStmtResponseDto;
import com.lic.epgs.gratuity.fund.dto.PolicyStmtGenReqDto;
import com.lic.epgs.gratuity.fund.dto.PolicyStmtGenRespDto;
import com.lic.epgs.gratuity.fund.service.FundService;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/policy")
public class PolicyRestController {

	@Autowired
	private FundService fundService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("/generateFundStatement")
	public PolicyStmtGenRespDto generateFundStatement(@RequestBody PolicyStmtGenReqDto requestDto) {
		PolicyStmtGenRespDto responseDto = new PolicyStmtGenRespDto();
		try {
			logger.info("PolicyMemberController -- generateFundStatement --started");
			responseDto = fundService.generateFundStatement(requestDto);
			logger.info("PolicyMemberController -- generateFundStatement --ended");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseDto;
	}
	
	@PostMapping("/fetchBatchStatus")
	public JsonResponse fetchBatchStatus(@RequestParam String batchNo) {
		JsonResponse responseDto = new JsonResponse();
		try { 
			logger.info("PolicyMemberController -- fetchBatchStatus --started");
			responseDto = fundService.fetchBatchStatus(batchNo);
			responseDto.setSuccess(true);
			logger.info("PolicyMemberController -- fetchBatchStatus --ended");
		} catch (Exception e) {		
			responseDto.setFailureData(e);
			e.printStackTrace();
		}
		return responseDto;
	}
	
	@PostMapping("/fetchDetailsFrPolicyFundStatement")
	public PolicyFundStmtResponseDto fetchDetailsFrPolicyFundStatement(@RequestBody PolicyFundStmtRequestDto requestDto) {
		PolicyFundStmtResponseDto responseDto = null;
		try { 
			logger.info("PolicyMemberController -- fetchDetailsFrPolicyFundStatement --started");
			responseDto = fundService.fetchDetailsFrPolicyFundStatement(requestDto);
			logger.info("PolicyMemberController -- fetchDetailsFrPolicyFundStatement --ended");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseDto;
	}
	
	@GetMapping("/downloadPolicyFundStatementForPolicyId")
	@Operation(summary = "Download the Member Account Statement as CSV format", description = "{\"policyId\":\"141\",\"memberId\":\"1\"}  = eyJwb2xpY3lJZCI6IjE0MSIsIm1lbWJlcklkIjoiMSJ9")
	public ResponseEntity<byte[]> downloadPolicyFundStatementForPolicyId(@RequestParam String policyId,
			HttpServletResponse response) throws IllegalStateException, IOException {
		logger.info("downloadMemberFundStatement: Start");
		DocumentDto documentDto = fundService.downloadPolicyFundStatementForPolicyId(policyId);
		logger.info("downloadMemberFundStatement:End:{}:{}", policyId);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + documentDto.getFileName())
				.header("fileName", documentDto.getFileName()).contentType(MediaType.TEXT_PLAIN)
				.body(documentDto.getBytes());
	}
}
