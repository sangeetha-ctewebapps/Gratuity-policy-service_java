package com.lic.epgs.gratuity.policyservices.aom.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lic.epgs.gratuity.accountingservice.service.AccountingService;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.service.IntegrationService;
import com.lic.epgs.gratuity.fund.service.FundService;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyTmpSearchEntity;
import com.lic.epgs.gratuity.policyservices.aom.dto.AOMSearchDto;
import com.lic.epgs.gratuity.policyservices.aom.dto.DocumentUploadDto;
import com.lic.epgs.gratuity.policyservices.aom.dto.GetOverviewDto;
import com.lic.epgs.gratuity.policyservices.aom.dto.MemberTmpDto;
import com.lic.epgs.gratuity.policyservices.aom.service.AOMService;
import com.lic.epgs.gratuity.policyservices.common.dto.PolicyServiceDto;
import com.lic.epgs.gratuity.policyservices.common.service.PolicyServicingCommonService;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBulkResponseDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationMatrixDto;
import com.lic.epgs.gratuity.simulation.dto.DepositDto;
import com.lic.epgs.gratuity.simulation.dto.PolicyDepositDto;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/aomservice" })
public class AOMController {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private AccountingService accountingService;

	@Autowired
	AOMService endorsementService;

	@Autowired
	private FundService fundService;

	@Autowired
	private IntegrationService integrationService;

	@Value("${temp.pdf.location}")
	private String fileLocation;

//AOM	
	@PostMapping("uploadAOMMember")
	public ApiResponseDto<MemberBulkResponseDto> uploadMemberDataAOMService(@RequestParam Long pmstpolicyId,
			@RequestParam String username, @RequestBody MultipartFile file) throws IllegalStateException, IOException {
		return integrationService.uploadMemberDataAOMService(pmstpolicyId, username, file);
	}

	@PostMapping("import/{pmstpolicyId}/{batchId}/{username}")
	public ApiResponseDto<List<MemberTmpDto>> importMemberData(@PathVariable("pmstpolicyId") Long pmstpolicyId,
			@PathVariable("batchId") Long batchId, @PathVariable("username") String username) {
		return endorsementService.importMemberData(pmstpolicyId, batchId, username);
	}

	@Autowired
	private PolicyServicingCommonService policyServicingCommonService;

	@PostMapping(value = "/masterPolicySearch")
	public ApiResponseDto<List<PolicyDto>> masterPolicySearch(@RequestBody PolicySearchDto policySearchDto) {
		return endorsementService.masterPolicySearch(policySearchDto);
	}

	@PostMapping(value = "/saveMemberPersonalInfo")
	public ApiResponseDto<?> saveTempMember(@RequestBody MemberTmpDto memberTmpDto, Long policyId, Long tempPolicyId) {
		return endorsementService.saveTempMember(memberTmpDto, policyId, tempPolicyId);
	}

	@GetMapping(value = "/search/quotation/{policyNumber}/{type}")
	public ApiResponseDto<List<PolicyTmpSearchEntity>> quotationSerachPolicy(
			@PathVariable("policyNumber") String policyNumber, @PathVariable("type") String type) {
		return endorsementService.quotationSearchPolicy(policyNumber, type);
	}

	@GetMapping(value = "/search/policy/{policyNumber}/{type}")
	public ApiResponseDto<List<PolicyTmpSearchEntity>> ServiceSerachPolicy(
			@PathVariable("policyNumber") String policyNumber, @PathVariable("type") String type) {
		return endorsementService.ServiceSerachPolicy(policyNumber, type);
	}

	@PostMapping("/quotationsentForApproval/{id}/{username}")
	public ApiResponseDto<RenewalPolicyTMPDto> sentForApproval(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return endorsementService.sentForApproval(id, username);
	}

	@PostMapping("/quotationforReject/{id}/{username}")
	public ApiResponseDto<RenewalPolicyTMPDto> forReject(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return endorsementService.forReject(id, username);
	}

	@PostMapping("/quotationforApprove/{id}/{username}")
	public ApiResponseDto<RenewalPolicyTMPDto> forApprove(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return endorsementService.forApprove(id, username);

	}

	@PostMapping("/quotationsendBacktoMaker/{id}/{username}")
	public ApiResponseDto<RenewalPolicyTMPDto> sendBacktoMaker(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return endorsementService.sendBacktoMaker(id, username);

	}

	@PostMapping("/aomprocessingsentForApproval/{id}/{username}") // //quotationsentForApproval
	public ApiResponseDto<RenewalPolicyTMPDto> sentPolicyForApproval(@PathVariable("id") Long id,
			@PathVariable("username") String username) {

		return endorsementService.sentPolicyForApproval(id, username);
	}

	@PostMapping("/aomprocessingsendBacktoMaker/{id}/{username}") // quotationsendBacktoMaker
	public ApiResponseDto<RenewalPolicyTMPDto> sentPolicyBacktoMaker(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return endorsementService.sentPolicyBacktoMaker(id, username);

	}

	@PostMapping("/aomprocessingforApprove/{tempid}/{username}")
	public ApiResponseDto<RenewalPolicyTMPDto> sentPolicyforApprove(@PathVariable("tempid") Long tempid,
			@PathVariable("username") String username) throws Exception {

		ApiResponseDto<RenewalPolicyTMPDto> policyforApprove = endorsementService.sentPolicyforApprove(tempid,
				username);
		fundService.setCreditEntries(policyforApprove.getData().getMasterPolicyId(), new Date());

		return policyforApprove;

	}

	@PostMapping("/aomprocessingforReject/{id}/{username}")
	public ApiResponseDto<RenewalPolicyTMPDto> sentPolicyforReject(@PathVariable("id") Long id,
			@RequestBody RenewalPolicyTMPDto renewalPolicyTMPDto, @PathVariable("username") String username) {
		return endorsementService.sentPolicyforReject(id, username, renewalPolicyTMPDto);

	}

	@GetMapping(value = "/showDeposit/{proposalNo}/{policyNo}/{username}")
	public ApiResponseDto<List<DepositDto>> showDeposit(@PathVariable("proposalNo") String proposalNo,
			@PathVariable("policyNo") String policyNo, @PathVariable("username") String username) {
		ApiResponseDto<List<DepositDto>> deposits = accountingService.getDeposits(proposalNo, policyNo, username);
		return deposits;
	}

	/*
	 * @PostMapping(value = "/calculate/gratuity/{quotationId}/{username}") public
	 * ApiResponseDto<ValuationDetailDto>
	 * calculateGratuity(@PathVariable("tempPolicyId") Long tempPolicyId,
	 * 
	 * @PathVariable("username") String username) { return
	 * endorsementService.calculatePremeium(tempPolicyId, username); }
	 */

	@PostMapping(value = "/calculatepremium/{policyId}/{userName}")
	public ApiResponseDto<ValuationMatrixDto> calculatepremium(@PathVariable(value = "policyId") Long policyId,
			@PathVariable(value = "userName") String userName) {
		return endorsementService.calculatePremeium(policyId, userName);
	}

	@PostMapping(value = "/quotation/otherSearchCriteria/{type}")
	public ApiResponseDto<List<AOMSearchDto>> otherSearchCriteria(@RequestBody AOMSearchDto aOMSearchDto,
			@PathVariable("type") String type) {
		return endorsementService.otherCriteiraQuotationSearch(aOMSearchDto, type);
	}

	@PostMapping(value = "/policy/otherSearchCriteria/{type}")
	public ApiResponseDto<List<AOMSearchDto>> otherSearchCriteriaService(@RequestBody AOMSearchDto aOMSearchDto,
			@PathVariable("type") String type) {
		return endorsementService.otherCriteiraPolicySearch(aOMSearchDto, type);
	}

	@PostMapping(value = "/makepaymentadjustmentforAom")
	public ApiResponseDto<RenewalPolicyTMPDto> makePaymentAdjustmentforRenewal(
			@RequestBody PolicyContributionDetailDto policyContributionDetailDto) {

		return endorsementService.makePaymentAdjustmentforAOM(policyContributionDetailDto);
	}

	@PostMapping(value = "/updatePaymentAdjustmentforAom")
	public ApiResponseDto<RenewalPolicyTMPDto> updatePaymentAdjustmentforAom(
			@RequestBody PolicyContributionDetailDto policyContributionDetailDto) {

		return endorsementService.updatePaymentAdjustmentforAom(policyContributionDetailDto);
	}

	@PostMapping(value = "/sendMPHLetterInEmail/{tmpPolicyId}")
	public ApiResponseDto<String> sendMPHLetterInEmail(@PathVariable(value = "tmpPolicyId") Long tmpPolicyId) {
		return endorsementService.sendMPHLetterInEmail(tmpPolicyId);
	}

	@GetMapping(value = "/getOverView/{tmpPolicyId}")
	public ApiResponseDto<GetOverviewDto> getOverView(@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		return endorsementService.getOverView(tmpPolicyId);

	}

	@GetMapping(value = "/getActiveSerices/{masterPolicyId}/{tmpPolicyId}")
	public ApiResponseDto<List<PolicyServiceDto>> getActiveServices(@PathVariable("masterPolicyId") Long masterPolicyId,
			@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		return policyServicingCommonService.getActiveServices(masterPolicyId, tmpPolicyId);
	}

	@GetMapping(value = "/getFromMakerBucket/{masterPolicyId}/{tmpPolicyId}")
	public ApiResponseDto<RenewalPolicyTMPDto> getInMakerBucket(@PathVariable("masterPolicyId") Long masterPolicyId,
			@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		return endorsementService.getInMakerBucket(masterPolicyId, tmpPolicyId);
	}

	@GetMapping(value = "/getAdjustedDeposit/{tmpPolicyId}")
	public ApiResponseDto<List<PolicyDepositDto>> getAdjustedDeposit(@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		return endorsementService.getAdjustedDeposit(tmpPolicyId);
	}

	@GetMapping(value = "/getPremiumRate/{tmpPolicyId}")
	public ApiResponseDto<Map<String, String>> getPremiumRate(@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		return endorsementService.getPremiumRate(tmpPolicyId);

	}

	@GetMapping(value = "/download/{tmpPolicyId}")
	public void download(@PathVariable(value = "tmpPolicyId") Long tmpPolicyId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String fileName = "/MemberData";
		String filePath = fileLocation + fileName + "_" + tmpPolicyId + ".pdf";
		File file = new File(filePath);
		Path path = null;
		if (file.exists()) {
			path = Paths.get(file.getAbsolutePath());
		} else {
			endorsementService.generateMemberPDF(tmpPolicyId);
			path = Paths.get(file.getAbsolutePath());
		}

		ServletContext context = request.getServletContext();
		// construct the complete absolute path of the file
		File downloadFile = new File(file.getAbsolutePath());
		FileInputStream inputStream = new FileInputStream(downloadFile);
		// get MIME type of the file
		String mimeType = context.getMimeType(file.getAbsolutePath());
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", tmpPolicyId + ".pdf");
		response.setHeader(headerKey, headerValue);
		// get output stream of the response
		OutputStream outStream = response.getOutputStream();
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
		inputStream.close();
		outStream.close();
		if (new File(file.getAbsolutePath()).exists()) {
			// new File(pdfDocumentValuationReport).delete();
		}
	}

	@PostMapping(value = "/uploadDocs")
	public Map<String, Object> uploadDocs(@RequestBody DocumentUploadDto documentUploadDto)
			throws JsonProcessingException {
		return endorsementService.uploadDocs(documentUploadDto);
	}

	@GetMapping(value = "/getDocumentDetails/{tmpPolicyId}")
	public ResponseEntity<Map<String, Object>> getDocumentDetails(@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		logger.info("Entering into MemberTransferInOutController : getDocumentDetails");
		Map<String, Object> response = new HashMap<>();
		try {
			List<DocumentUploadDto> documentDetailsResponse = endorsementService.getDocumentDetails(tmpPolicyId);
			if (documentDetailsResponse.size() == 0 || documentDetailsResponse.isEmpty()) {
				response.put("responseCode", "failed");
				response.put("responseMessage", "Data Not Found");
			} else {
				response.put("responseCode", "success");
				response.put("responseMessage", "Data Found");
				response.put("documentDetail", documentDetailsResponse);
			}
		} catch (InputMismatchException e) {
			logger.info("InputMismatchException : MemberTransferInOutController ");
		} catch (Exception e) {
			logger.error("Exception in MemberTransferInOutController. {%s}");
		}
		return ResponseEntity.accepted().body(response);
	}

	@PostMapping("/removeUploadedDocs/{documentId}/{username}")
	public ResponseEntity<Map<String, Object>> removeUploadedDocs(@PathVariable(value = "documentId") Long documentId,
			@PathVariable(value = "username") String username) throws JsonProcessingException {
		return endorsementService.removeUploadedDocs(documentId, username);
	}

	@PostMapping(value = "/deleteMembers/{memId}")
	public ApiResponseDto<String> inActive(@PathVariable("memId") List<Long> memId) {
		return endorsementService.inActiveMemberInBulk(memId);
	}

////	@PostMapping("/saveMasterToTemp/{masterPolicyId}/{createdBy}")
//	public ApiResponseDto<RenewalPolicyTMPDto> createforQuotation(@PathVariable Long masterPolicyId,
//			@PathVariable String createdBy) {
//		return endorsementService.masterToTemp(masterPolicyId, createdBy);
//
//	}
//
//	@PostMapping(value = "/saveMemberBankAccount")
//	public RenewalPolicyTMPMemberBankAccountEntity saveTempMemberBankAccount(@RequestBody RenewalPolicyTMPMemberBankAccountEntity request,
//			Long memberId) {
//		return endorsementService.saveTempMemberBankAccount(request, memberId);
//	}
//
//	@PostMapping(value = "/saveMemberAddress")
//	public RenewalPolicyTMPMemberAddressEntity saveTempMemberAddress(@RequestBody RenewalPolicyTMPMemberAddressEntity request, Long memberId) {
//		return endorsementService.saveTempMemberAddress(request, memberId);
//	}
//
//	@PostMapping(value = "/saveMemberNominee")
//	public RenewalPolicyTMPMemberNomineeEntity saveTempMemberNominee(@RequestBody RenewalPolicyTMPMemberNomineeEntity request, Long memberId) {
//		return endorsementService.saveTempMemberNominee(request, memberId);
//	}
//	
//	@PostMapping(value = "/saveMemberAppointee")
//	public RenewalPolicyTMPMemberAppointeeEntity saveTempMemberAppointee(@RequestBody RenewalPolicyTMPMemberAppointeeEntity request,
//			Long memberId) {
//		return endorsementService.saveTempMemberAppointee(request, memberId);
//	}
}
