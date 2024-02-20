package com.lic.epgs.gratuity.policy.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lic.epgs.gratuity.accountingservice.service.AccountingService;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.fund.service.FundService;
import com.lic.epgs.gratuity.policy.dto.AgeValuationReportDto;
import com.lic.epgs.gratuity.policy.dto.ContributionRequestDto;
import com.lic.epgs.gratuity.policy.dto.ContributionResponseDto;
import com.lic.epgs.gratuity.policy.dto.DocumentStoragedto;
import com.lic.epgs.gratuity.policy.dto.PolicyBondDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policy.dto.PolicyStatementPeriod;
import com.lic.epgs.gratuity.policy.dto.QuotationChargeDto;
import com.lic.epgs.gratuity.policy.premiumadjustment.dto.PolicyContrySummarydto;
import com.lic.epgs.gratuity.policy.premiumadjustment.dto.PolicyDepositdto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyNewSearchFilterDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.repository.DocumentStorageRepository;
import com.lic.epgs.gratuity.policy.service.PolicyPdfService;
import com.lic.epgs.gratuity.policy.service.PolicyService;
import com.lic.epgs.gratuity.simulation.dto.DepositDto;
import com.lic.epgs.gratuity.simulation.service.DepositService;

/**
 * @author Vigneshwaran
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/policy")
public class PolicyController {

	@Autowired
	DocumentStorageRepository documentStorageRepository;

	@Autowired
	private PolicyService policyService;
	
	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;
	@Autowired
	private DepositService depositService;
	
	@Autowired
	private AccountingService accountingService;

	@Autowired
	private FundService fundService;
	
	@Autowired
	private PolicyPdfService policyPdfService;

	// TODO: as we have implemented find by id/type - this method is no more needed.
	// remove this
	// this may need angular update also
	@GetMapping(value = "/{id}")
	public ApiResponseDto<PolicyDto> findById(@PathVariable("id") Long id) {
		return policyService.findById(id);
	}

	// when PolicyDto and MasterPolicyDto has normalized parameters, let us return
	// only PolicyDto - make this consistent for all methods
	@GetMapping(value = "{id}/{type}")
	public ApiResponseDto<PolicyDto> findById(@PathVariable("id") Long id, @PathVariable("type") String type) {
		return policyService.findById(id, type);
	}

	// TODO: implement this method for PSTG_POLICY also
	@PostMapping(value = "/filter")
	public ApiResponseDto<List<PolicyDto>> filter(@RequestBody PolicySearchDto policySearchDto) {
		return policyService.filter(policySearchDto);
	}

	// TODO: change this method to create new policy (PSTG_POLICY) from
	// QMST_QUOTATION, also copy following entities from quotation master

//	PSTG_SCHEMERULE
//	PSTG_LIFE_COVER
//	PSTG_GRATUITY_BENEFIT
//	PSTG_GRATUITY_BENEFIT_PROPS
//	PSTG_MEMBER
//	PSTG_MEMBER_ADDRESS
//	PSTG_MEMBER_APPOINTEE
//	PSTG_MEMBER_BANK_ACCOUNT
//	PSTG_MEMBER_NOMINEE

//	PSTG_VALUATION
//	@PostMapping(value = "/makepayment")
//	public ApiResponseDto<PolicyDto> makePayment(@RequestBody List<PaymentDto> paymentDtos) {
//		return policyService.makePaynent(paymentDtos);
//	}

	// TODO: change this method to update PSTG_POLICY
	@PostMapping(value = "/submitforapproval/{id}/{username}")
	public ApiResponseDto<PolicyDto> submitForApproval(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return policyService.submitForApproval(id, username);
	}

	// TODO: change this method to update PSTG_POLICY
	@PostMapping(value = "/sendbacktomaker/{id}/{username}")
	public ApiResponseDto<PolicyDto> sendBackToMaker(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return policyService.sendBackToMaker(id, username);
	}

	// TODO: change this method to update PSTG_POLICY
	// when approving copy the following from policy staging to policy master

//  PSTG_POLICY - update master policy id in this table
//	PSTG_GRATUITY_BENEFIT
//	PSTG_GRATUITY_BENEFIT_PROPS
//	PSTG_LIFE_COVER
//	PSTG_MEMBER
//	PSTG_MEMBER_ADDRESS
//	PSTG_MEMBER_APPOINTEE
//	PSTG_MEMBER_BANK_ACCOUNT
//	PSTG_MEMBER_NOMINEE
//	PSTG_SCHEMERULE
//	PSTG_VALUATION
	
	@PostMapping(value = "/approve/{id}/{username}")
	public ApiResponseDto<PolicyDto> approve(@PathVariable("id") Long id, @PathVariable("username") String username) {
		ApiResponseDto<PolicyDto> tt = policyService.approve(id, username);
		fundService.setCreditEntries(tt.getData().getId(), new Date());
		return tt;
	}

	// TODO: change this method to update PSTG_POLICY
	@PostMapping(value = "/reject/{id}/{username}")
	public ApiResponseDto<PolicyDto> reject(@PathVariable("id") Long id, @PathVariable("username") String username,@RequestBody PolicyDto policyDto) {
		return policyService.reject(id, username,policyDto);
	}

	// TODO: change this method to update PSTG_POLICY
	@PostMapping(value = "/escalatetoco/{id}/{username}")
	public ApiResponseDto<PolicyDto> escalateToCo(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return policyService.escalateToCo(id, username);
	}

	// TODO: change this method to update PSTG_POLICY
	@PostMapping(value = "/escalatetozo/{id}/{username}")
	public ApiResponseDto<PolicyDto> escalateToZo(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return policyService.escalateToZo(id, username);
	}

	@GetMapping(value = "/charges/{id}")
	public ApiResponseDto<List<QuotationChargeDto>> findCharges(@PathVariable("id") Long id) {
		return policyService.findCharges(id);
	}

	@GetMapping(value = "/renewalchargedetail/{tmpPolicyId}")
	public ApiResponseDto<List<QuotationChargeDto>> renewalChargeDetail(@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		return policyService.renewalChargeDetail(tmpPolicyId);
	}

	@GetMapping("downloads/candbsheet/{policyId}/{quotationId}/{tmpPolicyId}/{taggedStatusId}")
	public ResponseEntity<byte[]> findByPolicyId(@PathVariable("policyId") Long policyId,
			@PathVariable("quotationId") Long quotationId, @PathVariable("tmpPolicyId") Long tmpPolicyId,
			@PathVariable("taggedStatusId") Long taggedStatusId) {
		byte[] tt = null;
		if (policyId != 0) {
			tt = policyService.findByPolicyId(policyId, taggedStatusId);
		} else if (quotationId != 0) {
			tt = policyService.findByquotationId(quotationId, taggedStatusId);
		} else {
			tt = policyService.findBytmpPolicyId(tmpPolicyId);
		}

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("application/nvd.ms-excel"));
		header.setContentLength(tt.length);
		header.set("Content-Disposition", "attachment; filename=CandBsheet.xlsx");

		return new ResponseEntity<>(tt, header, HttpStatus.OK);
	}

	// r
	@PostMapping(value = "/createcontribution/{masterQuotationId}/{tmpPolicyId}")
	public ApiResponseDto<PolicyContributionDetailDto> createContributionid(
			@PathVariable("masterQuotationId") Long masterQuotationId, @PathVariable("tmpPolicyId") Long tmpPolicyId,
			@RequestBody PolicyContributionDetailDto contributionDto) {
		ApiResponseDto<PolicyContributionDetailDto> policyContributionDetailDto = null;
		if (masterQuotationId != 0) {
			policyContributionDetailDto = policyService.createContributionid(masterQuotationId, contributionDto);
		} else {
			policyContributionDetailDto = policyService.createContributionTmpId(tmpPolicyId, contributionDto);
		}

		return policyContributionDetailDto;
	}

	@GetMapping(value = "getcontribution/{masterquotationid}")
	public ApiResponseDto<List<PolicyContributionDetailDto>> findContributionId(
			@PathVariable("masterquotationid") Long masterquotationid) {

		return policyService.findContributionId(masterquotationid);
	}

	@GetMapping(value = "getcontributionbytmppolicyid/{tempPolicyId}")
	public ApiResponseDto<List<PolicyContributionDetailDto>> findContributionTmpPolicyId(
			@PathVariable("tempPolicyId") Long tempPolicyId) {

		return policyService.findContributionTmpPolicyId(tempPolicyId);
	}

	@PutMapping(value = "updatecontribution/{id}")
	public ApiResponseDto<PolicyContributionDetailDto> updateContributionId(@PathVariable("id") Long id,
			@RequestBody PolicyContributionDetailDto contributionDto) {

		return policyService.updateContributionid(id, contributionDto);
	}

	@PostMapping(value = "makepaymentadjustment")
	public ApiResponseDto<PolicyContributionDetailDto> makePaymentAdjustment(
			@RequestBody PolicyContributionDetailDto policyContributionDetailDto) {

		return policyService.makePayment(policyContributionDetailDto);
	}

	@PostMapping(value = "updatepremiumadjustment")
	public ApiResponseDto<PolicyContributionDetailDto> updatePaymentAdjustment(
			@RequestBody PolicyContributionDetailDto policyContributionDetailDto) {

		return policyService.updatePaymentAdjustment(policyContributionDetailDto);
	}

	// Renwal
	@PostMapping(value = "makepaymentadjustmentforrenewal")
	public ApiResponseDto<RenewalPolicyTMPDto> makePaymentAdjustmentforRenewal(
			@RequestBody PolicyContributionDetailDto policyContributionDetailDto) {

		return policyService.makePaymentAdjustmentforRenewal(policyContributionDetailDto);
	}

	@GetMapping(value = "getdepositusingtmppolicy/{type}/{tmpPolicyId}")
	public ApiResponseDto<List<PolicyDepositdto>> getDepositTmpPolicy(@PathVariable("type") String type,
			@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		return policyService.getDepositTmpPolicy(type, tmpPolicyId);
	}

	// renewal

	// Valuation Report for Age
	@GetMapping(value = "getagereport/{policyId}")
	public ApiResponseDto<List<AgeValuationReportDto>> getAgeReport(@PathVariable("policyId") Long policyId) {

		return policyService.getAgeReport(policyId);

	}

	@GetMapping(value = { "downloadReport/report" })
	public void quotationValuationReport(@RequestParam("policyId") Long policyId,
			@RequestParam("reportType") String reportType, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String pdfDocumentValuationReport = policyPdfService.generateReport(policyId, reportType);

		ServletContext context = request.getServletContext();
		// construct the complete absolute path of the file
		File downloadFile = new File(pdfDocumentValuationReport);
		FileInputStream inputStream = new FileInputStream(downloadFile);
		// get MIME type of the file
		String mimeType = context.getMimeType(pdfDocumentValuationReport);
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", policyId + ".pdf");
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
		if (new File(pdfDocumentValuationReport).exists()) {
			// new File(pdfDocumentValuationReport).delete();
		}
	}

	@GetMapping(value = "getdeposit/{type}/{policyId}")
	public ApiResponseDto<List<PolicyDepositdto>> getDepositData(@PathVariable("type") String type,
			@PathVariable("policyId") Long policyId) {
		return policyService.getDepositData(type, policyId);
	}
	
	@GetMapping(value = "getlatestcontributiondetail/{masterPolicyId}")
	public ApiResponseDto<PolicyContributionDetailDto> getlatestcontributiondetail(
			@PathVariable("masterPolicyId") Long masterPolicyId) {
		return policyService.getlatestcontributiondetail(masterPolicyId);
	}

	@GetMapping(value = "getSummaryContriDetail/{policyId}")
	public ApiResponseDto<List<PolicyContrySummarydto>> getSummaryContriDetail(
			@PathVariable("policyId") Long policyId) {
		return policyService.getSummaryContriDetail(policyId);
	}

	@PostMapping(value = "/documentStorage", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ApiResponseDto<DocumentStoragedto> uploadDocumentStorage(@RequestParam MultipartFile file)
			throws IllegalStateException, IOException {
		System.out.println("File:" + file.getOriginalFilename());
		return policyService.uploadDocumentStorage(file);
	}

	// @PostMapping(value = "/documentStorage",consumes=
	// {MediaType.MULTIPART_FORM_DATA_VALUE},produces=
	// {MediaType.APPLICATION_JSON_VALUE})

	@GetMapping(value = "/download/policybondpdf/{id}")
	public ResponseEntity<byte[]> policybondpdf(@PathVariable("id") Long id) throws IOException {

		byte[] tt = policyService.policybondpdf(id);

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("application/nvd.pdf"));
		header.setContentLength(tt.length);
		header.set("Content-Disposition", "attachment; filename=policybond.pdf");

		return new ResponseEntity<>(tt, header, HttpStatus.OK);
	}

	@PostMapping(value = "getpolicybond/{policyId}")
	public ApiResponseDto<PolicyBondDetailDto> getPolicyBondDetails(@PathVariable("policyId") Long policyId) {

		return policyService.getPolicyBondDetails(policyId);
	}

	@GetMapping(value = "/get/memberDetailsById/{policyId}")
	public ResponseEntity<byte[]> getPolicyMemberDtls(@PathVariable("policyId") Long policyId) {
		byte[] bs = policyService.getPolicyMemberDtls(policyId);
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("application/nvd.ms-excel"));
		header.setContentLength(bs.length);
		header.set("Content-Disposition", "attachment; filename=ActiveMemberList.xlsx");
		return new ResponseEntity<>(bs, header, HttpStatus.OK);
//    	return policyService.getPolicyMemberDtls(policyId);
	}

	 @GetMapping(value = "getSingleSummaryContriDetail/{policyId}/{taggedStatus}")
		public ApiResponseDto<PolicyContrySummarydto> getSummaryContriDetailfield(@PathVariable("policyId") Long policyId,@PathVariable("taggedStatus") String taggedStatus) {
	    	
			return policyService.getSummaryContriDetailfield( policyId,taggedStatus);
		}
	 
		@GetMapping(value = "showDeposit/{proposalNo}/{policyNo}/{username}")
		public ApiResponseDto<List<DepositDto>> showDeposit(@PathVariable("proposalNo") String proposalNo,@PathVariable ("policyNo")String policyNo,@PathVariable ("username") String username) {
			ApiResponseDto<List<DepositDto>> getDepositDto=null;
			if(isDevEnvironment == true) {
				if(proposalNo.equals("null")) {
					getDepositDto = depositService.findAllByPolicyNumber(policyNo);
				
				}else {
					getDepositDto=depositService.findAllByProposalNumber(proposalNo);
				}
			}else {
				getDepositDto=accountingService.getDeposits(proposalNo,policyNo,username);
			}
			
			
		
			return getDepositDto;
		}

	@GetMapping(value = { "Adjustmentvoucher/premiumreceipt" })
	public void quotationValuationReport(@RequestParam("masterpolicyId") Long masterpolicyId,
			HttpServletRequest request, HttpServletResponse response) throws IOException {

		String premiumreceipt = policyPdfService.premiumreceipt(masterpolicyId);

		ServletContext context = request.getServletContext();
		// construct the complete absolute path of the file
		File downloadFile = new File(premiumreceipt);
		FileInputStream inputStream = new FileInputStream(downloadFile);
		// get MIME type of the file
		String mimeType = context.getMimeType(premiumreceipt);
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", masterpolicyId + ".pdf");
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
		if (new File(premiumreceipt).exists()) {
			// new File(pdfDocumentValuationReport).delete();
		}
	}

	// karthi -- start

	@GetMapping(value = { "generatecbpolicypdf" })
	public void getcandbsheetpdf(@RequestParam("policyId") Long policyId,
			@RequestParam("taggedStatusId") Long taggedStatusId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String getcandbsheetpdf = policyPdfService.getcandbsheetpdf(policyId, taggedStatusId);

		ServletContext context = request.getServletContext();
		// construct the complete absolute path of the file
		File downloadFile = new File(getcandbsheetpdf);
		FileInputStream inputStream = new FileInputStream(downloadFile);
		// get MIME type of the file
		String mimeType = context.getMimeType(getcandbsheetpdf);
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", policyId + ".pdf");
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
		if (new File(getcandbsheetpdf).exists()) {
			// new File(pdfDocumentValuationReport).delete();
		}
	}

	// c&b sheet pdf

//	@GetMapping(value = "getcbsheetpdf/{policyId}")
//	public ApiResponseDto<List<GenerateCBSheetPdfDto>> getcbsheetpdf(@PathVariable("policyId") Long policyId,
//			@PathVariable("taggedStatusId") Long taggedStatusId) {
//
//		return policyService.getcbsheetpdf(policyId, taggedStatusId);
//
//	}
	// karthi -- end

	@PostMapping(value = "updatePaymentAdjustmentforRenewal")
	public ApiResponseDto<RenewalPolicyTMPDto> updatePaymentAdjustmentforRenewal(
			@RequestBody PolicyContributionDetailDto policyContributionDetailDto) {
		return policyService.updatePaymentAdjustmentforRenewal(policyContributionDetailDto);
	}

	@GetMapping(value = "/getpolicynumberdetails/{policyNumber}")
	public ApiResponseDto<PolicyDto> findByPolicyNumber(@PathVariable("policyNumber") String policyNumber) {
		return policyService.findByPolicyNumber(policyNumber);
	}
	
	@GetMapping(value = {"generatepaymentvoucher"})
	public void quotationValuationReport( @RequestParam("username") String userName ,@RequestParam("policyId") Long policyId,HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		String pdfDocumentValuationReport=policyPdfService.generatepaymentvoucher(userName,policyId);
		
		ServletContext context = request.getServletContext();
		// construct the complete absolute path of the file
		File downloadFile = new File(pdfDocumentValuationReport);
		FileInputStream inputStream = new FileInputStream(downloadFile);
		// get MIME type of the file
		String mimeType = context.getMimeType(pdfDocumentValuationReport); 
		if (mimeType == null) {
		mimeType = "application/octet-stream";
	}
	// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", policyId+ ".pdf");
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
		if (new File(pdfDocumentValuationReport).exists()) {
			//new File(pdfDocumentValuationReport).delete();
			}
	}
	
	
	//start contribution
	@PostMapping("/runningstatement")
	public ApiResponseDto<List<ContributionResponseDto>> getContributionDetail(@RequestBody ContributionRequestDto contributionRequestDto){
		return policyService.getPolicyContributionDetails(contributionRequestDto);
		
	}
	
	@GetMapping("/getstatementperiods/{policyid}")
	public ApiResponseDto<List<PolicyStatementPeriod>> getPolicyStatementPeriod(@PathVariable("policyid") Long id) {
		return policyService.getPolicyStatementPeriod(id);
	}
	
	@GetMapping(value = "getSummaryContriDetailforrenewal/{tmppolicyId}")
	public ApiResponseDto<List<PolicyContrySummarydto>> getSummaryContriDetailForRenewal(
			@PathVariable("tmppolicyId") Long tmppolicyId) {
		return policyService.getSummaryContriDetailForRenewal(tmppolicyId);
	}
	
	@PostMapping(value="filterfornewsearchclaim")
	public ApiResponseDto<List<PolicyDto>> filterForClaim(@RequestBody PolicySearchDto policySearchDto){
		return policyService.filterForClaim(policySearchDto);
}
	@GetMapping(value="getpolicysubstatus/{policySubStatusId}")
	public Long getPolicySubStatus(@PathVariable("policySubStatusId") Long policySubStatusId) {
		return policyService.getPolicySubStatus(policySubStatusId);
		
	}
}
