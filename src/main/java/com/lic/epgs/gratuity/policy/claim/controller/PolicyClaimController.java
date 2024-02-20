package com.lic.epgs.gratuity.policy.claim.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.ApiValidationResponse;
import com.lic.epgs.gratuity.fund.dto.CalculateResDto;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.policy.claim.dto.GratuityCalculationsDto;
import com.lic.epgs.gratuity.policy.claim.dto.TempPolicyClaimBeneficiaryDto;
import com.lic.epgs.gratuity.policy.claim.dto.TempPolicyClaimPropsDto;
import com.lic.epgs.gratuity.policy.claim.dto.TempPolicyClaimPropsSearchDto;
import com.lic.epgs.gratuity.policy.claim.entity.PolicyTempSearchEntity;
import com.lic.epgs.gratuity.policy.claim.service.PolicyClaimPdfService;
import com.lic.epgs.gratuity.policy.claim.service.PolicyClaimService;
import com.lic.epgs.gratuity.policy.renewal.dto.EmailPdfFileDto;
import com.lic.epgs.gratuity.policy.renewal.dto.EmailRequestPayload;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationPDFGenerationDto;
import com.lic.epgs.gratuity.quotation.entity.QuotationEntity;
import com.lic.epgs.gratuity.quotation.repository.QuotationRepository;
import com.lic.epgs.gratuity.quotation.service.impl.QuotationPdfServiceImpl;
import com.lic.epgs.gratuity.quotation.service.impl.QuotationServiceImpl;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/claim")
public class PolicyClaimController {

	@Value("${app.commonModuleServiceEndpoint}")
	private String endPoint;
	
	@Autowired
	private QuotationRepository quotationRepository;
	
	@Autowired
	private QuotationServiceImpl quotationServiceImpl;
	
	@Autowired
	private QuotationPdfServiceImpl quotationPdfServiceImpl;
	
	@Autowired
	private PolicyClaimService policyClaimService;
	
	@Autowired
	private PolicyClaimPdfService PolicyClaimPdfService;

	@Autowired
	private MPHRepository mPHRepository;
	
	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;
	
	@PostMapping("createclaimforindividual")
	public ApiResponseDto<TempPolicyClaimPropsDto> createClaimforindividual(
			@RequestBody TempPolicyClaimPropsDto tempPolicyClaimPropsDto) {
		return policyClaimService.createClaimforindividual(tempPolicyClaimPropsDto);
	}

	@GetMapping("getindividualonboardingno/{onboardNumber}")
	public ApiResponseDto<TempPolicyClaimPropsDto> getIndividualOnboardingno(
			@PathVariable(value = "onboardNumber") String onboardNumber) {

		return policyClaimService.getIndividualOnboardingno(onboardNumber);
	}
	
	@GetMapping("getindividualIntimationno/{intimationon}")
	public ApiResponseDto<TempPolicyClaimPropsDto> getIndividualIntimationno(
			@PathVariable(value = "intimationon") String intimationon) {

		return policyClaimService.getIndividualIntimationNo(intimationon);
	}

	
	@GetMapping("getclaimbasedonstatusids/{claimstatusids}")
	public ApiResponseDto<List<TempPolicyClaimPropsDto>> getClaimBasedonStatusIds(
			@PathVariable(value = "claimstatusids") Long[] claimstatusids) {
		return policyClaimService.getClaimBasedonStatusIds(claimstatusids);
	}

	@GetMapping("onboardingclaimcancel/{onboardNumber}")
	public ApiResponseDto<TempPolicyClaimPropsDto> onboardingClaimCancel(
			@PathVariable(value = "onboardNumber") String onboardNumber) {
		return policyClaimService.onboardingClaimCancel(onboardNumber);
	}

	@PostMapping("filterclaimpropsbasedontype")
	public ApiResponseDto<List<TempPolicyClaimPropsDto>> filter(
			@RequestBody TempPolicyClaimPropsSearchDto policyClaimPropsSearchDto) {
		return policyClaimService.filter(policyClaimPropsSearchDto);
	}

	@PutMapping("updateIntimationDocumentsDetails/{propsId}")
	public ApiResponseDto<TempPolicyClaimPropsDto> updateIntimationDocumentsDetails(@PathVariable("propsId") Long id,
			@RequestBody TempPolicyClaimPropsDto tempPolicyClaimPropsDto) {
		return policyClaimService.updateIntimationDocumentsDetails(id, tempPolicyClaimPropsDto);
	}

	@PutMapping("intimationClaimUpdate/{onboardNumber}")
	public ApiResponseDto<TempPolicyClaimPropsDto> intimationClaimUpdate(@PathVariable("onboardNumber") String propsId,
			@RequestBody TempPolicyClaimPropsDto tempPolicyClaimPropsDto) {
		return policyClaimService.intimationClaimUpdate(propsId, tempPolicyClaimPropsDto);
	}

	@PutMapping("claimStatusChange/{propsId}/{statusId}")
	public ApiResponseDto<TempPolicyClaimPropsDto> claimStatusChange(@PathVariable("propsId") Long id,
			@PathVariable("statusId") Long StatusId, @RequestBody TempPolicyClaimPropsDto tempPolicyClaimPropsDto) {
		return policyClaimService.claimstatuschange(id, StatusId, tempPolicyClaimPropsDto);
	}

	@PostMapping("beneficiarydetailssave")
	public ApiResponseDto<List<TempPolicyClaimBeneficiaryDto>> beneficiaryDetailsSave(
			@RequestBody TempPolicyClaimBeneficiaryDto tempPolicyClaimBeneficiaryDto) {
		return policyClaimService.beneficiaryDetailsSave(tempPolicyClaimBeneficiaryDto);
	}

	@PutMapping("beneficiarydetailsupdate/{id}")
	public ApiResponseDto<TempPolicyClaimBeneficiaryDto> beneficiaryDetailsUpdate(
			@RequestBody TempPolicyClaimBeneficiaryDto tempPolicyClaimBeneficiaryDto, @PathVariable Long id) {
		return policyClaimService.beneficiaryDetailsUpdate(tempPolicyClaimBeneficiaryDto, id);
	}

	@GetMapping("beneficiarydetailsget/{id}")
	public ApiResponseDto<List<TempPolicyClaimBeneficiaryDto>> beneficiaryDetailsget(@PathVariable Long id) {
		return policyClaimService.beneficiaryDetailsget(id);
	}


	@PostMapping("calculategratuity")
	public ApiResponseDto<GratuityCalculationsDto> calculategratuity(
			@RequestBody GratuityCalculationsDto gratitutyCalculationDto) {
		return policyClaimService.calculategratuity(gratitutyCalculationDto);
	}
	
	
	@PostMapping("refundcalculation")
	public ApiResponseDto<GratuityCalculationsDto> RefundCalculation(@RequestBody GratuityCalculationsDto gratitutyCalculationDto) {
		return policyClaimService.RefundCalculation(gratitutyCalculationDto);
	}
	
	@GetMapping(value = {"generatemultipleclaimpaymentvoucher"})
	public void quotationValuationReport( @RequestParam("username") String userName ,@RequestParam("payoutnumber") String payoutNumber,HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		String pdfDocumentValuationReport=PolicyClaimPdfService.generateReport(userName,payoutNumber);
		
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
		String headerValue = String.format("attachment; filename=\"%s\"", payoutNumber+ ".pdf");
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

	

	@GetMapping(value = {"claimcalculationSheet/downloadReport/report"})
	public void quotationValuationReport( @RequestParam("onboardNumber") String onboardNumber,HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		String pdfDocumentValuationReport=PolicyClaimPdfService.claimCalculationSheetGenerateReport(onboardNumber);
		
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
				String headerValue = String.format("attachment; filename=\"%s\"", onboardNumber+ ".pdf");
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
	
	
	@GetMapping("getfundsize/{policyid}")
	public ApiResponseDto<CalculateResDto> GetFundSize(@PathVariable Long policyid) {
		return policyClaimService.GetFundSize(policyid);
	}
	
	@GetMapping("validateforonboarding/{memberid}/{policyid}")
	public ApiResponseDto<?> ValidateForOnboarding(@PathVariable("memberid") Long memberid,@PathVariable("policyid") Long policyid ){
		return policyClaimService.ValidateForOnboarding(memberid,policyid);
		
	}
	
	 @PostMapping(value="payoutapprove/{propsId}/{username}")
		public ApiResponseDto<TempPolicyClaimPropsDto> claimStatusChange(@PathVariable("propsId") Long id,
				@PathVariable("username") String username){
					return policyClaimService.payoutapprove(id,username);
		 
	 }

		
		@PostMapping("importforClaim/{pmstPolicyId}/{batchId}/{username}")
		public ApiResponseDto<List<TempPolicyClaimPropsDto>> importClaimData(@PathVariable("pmstPolicyId") Long pmstPolicyId,
				@PathVariable("batchId") Long batchId, @PathVariable("username") String username) {
			return policyClaimService.importClaimData(pmstPolicyId, batchId, username);
		}
		
		@GetMapping(value = { "downloadReportClaims/report" })
		public void claimForwardingLetter(@RequestParam("userName") String userName,
				@RequestParam("payoutNumber") String payoutNumber,
				HttpServletRequest request, HttpServletResponse response) throws IOException {

			String claimForwardingLetter = PolicyClaimPdfService.claimForwardingLetter(userName,payoutNumber);
			ServletContext context = request.getServletContext();
			// construct the complete absolute path of the file
			File downloadFile = new File(claimForwardingLetter);
			FileInputStream inputStream = new FileInputStream(downloadFile);
			// get MIME type of the file
			String mimeType = context.getMimeType(claimForwardingLetter);
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}
			// set content attributes for the response
			response.setContentType(mimeType);
			response.setContentLength((int) downloadFile.length());
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", payoutNumber + ".pdf");
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
			if (new File(claimForwardingLetter).exists()) {
				// new File(pdfDocumentValuationReport).delete();
			}
		}
		
		
		@PostMapping("/validationforOnboardingCondition")
		public ApiResponseDto<ApiValidationResponse> ValidationforOnboardingCondition (@RequestBody TempPolicyClaimPropsDto tempPolicyClaimPropsDto){
			return policyClaimService.ValidationforOnboardingCondition(tempPolicyClaimPropsDto);
		}
		
		
		@GetMapping("validationforclaimOnboarding/{memberid}/{policyid}")
       public ApiResponseDto<?>claimsOnboarding (@PathVariable(value="memberid") Long memberid,@PathVariable(value="policyid") Long policyid ){
			return policyClaimService.validationforclaimOnboarding(memberid,policyid);
			
		}
		
		@DeleteMapping("deletebeneficiary/{propsId}/{memberBankId}/{nomineeId}/{appointeeId}")
		public ApiResponseDto<RenewalPolicyTMPMemberDto> deleteBeneficiary(
				@PathVariable(value = "propsId") Long propsId,@PathVariable(value = "memberBankId") Long memberBankId,@PathVariable(value = "nomineeId") Long nomineeId,@PathVariable(value = "appointeeId") Long appointeeId) {
			return policyClaimService.deleteBeneficiary(propsId,memberBankId,nomineeId,appointeeId);
		}
		
		
		@GetMapping("sendEmailQuotation/{quotationId}/{taggedStatusId}")
		public ApiResponseDto <?> sendMailquatationId(@PathVariable("quotationId") Long quotationId,
				@PathVariable("taggedStatusId") Long taggedStatusId) throws IOException {

			String result = null;
			String generateReport = null;
			String quotationValuation = null;
				
				QuotationEntity quotationEntity = quotationRepository.findById(quotationId).get();
				JsonNode getResponse = null;
				JsonNode mphBasic = null;
				JsonNode mphAdds = null;
				JsonNode mphRep = null;
				EmailRequestPayload emailRequest = new EmailRequestPayload();
				try {
					URL url = new URL(endPoint + "/api/proposal/getProposalDetailsByProposalNumber?proposalNumber="
							+ quotationEntity.getProposalNumber());
					System.out.println(quotationEntity.getProposalNumber());
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Accept", "application/json");

					if (conn.getResponseCode() != 200) {
						throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
					}

					BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
					String output = br.readLine();
					conn.disconnect();

					ObjectMapper mapper = new ObjectMapper();
					JsonFactory factory = mapper.getFactory();
					JsonParser parser = factory.createParser(output);
					JsonNode actualObj = mapper.readTree(parser);
					System.out.println(actualObj);
					getResponse= actualObj.path("responseData");
					mphBasic = actualObj.path("responseData").path("mphDetails").path("mphBasicDetails");
					mphAdds = actualObj.path("responseData").path("mphDetails").path("mphAddressDetails");
					mphRep = actualObj.path("responseData").path("mphDetails").path("mphContactDetails");
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				QuotationPDFGenerationDto newQuotationPDFGenerationDto = new QuotationPDFGenerationDto();
			
				if(!getResponse.toString().equals("null")) {
				newQuotationPDFGenerationDto.setMphName(mphBasic.path("mphName").textValue());
				newQuotationPDFGenerationDto.setEmail(mphRep.get(0).path("emailID").textValue());
				
				if (newQuotationPDFGenerationDto.getEmail()!=null) {

				if (isDevEnvironment==false) {
											
				generateReport = quotationPdfServiceImpl.getcandbsheetpdf(quotationId, taggedStatusId);
				File file = new File(generateReport);

				byte[] data = Files.readAllBytes(file.toPath());
				String s = Base64.getEncoder().encodeToString(data);

				EmailPdfFileDto emailPdftwoFileDto = new EmailPdfFileDto();

				emailPdftwoFileDto.setFileData(s);
				emailPdftwoFileDto.setFileName("QuotationEmail");
				emailPdftwoFileDto.setFileType("pdf");

				quotationValuation = quotationPdfServiceImpl.generateReport(quotationId,null,"valuationreport", taggedStatusId);

				File file1 = new File(quotationValuation);

				byte[] data1 = Files.readAllBytes(file1.toPath());
				String s1 = Base64.getEncoder().encodeToString(data1);

				EmailPdfFileDto emailPdftwoFileDto1 = new EmailPdfFileDto();

				emailPdftwoFileDto1.setFileData(s1);
				emailPdftwoFileDto1.setFileName("QuotationEmail");
				emailPdftwoFileDto1.setFileType("pdf");

				ArrayList<EmailPdfFileDto> emailPdfFileList = new ArrayList<>();
				emailPdfFileList.add(emailPdftwoFileDto);
				emailPdfFileList.add(emailPdftwoFileDto1);

				emailRequest.setSubject("MPH");
				emailRequest.setEmailBody("<html>\r\n" + "<head>\r\n" + "<title>MPH NAME :"+newQuotationPDFGenerationDto.getMphName()+" </title>\r\n"
						+ "</head>\r\n" + "<body>\r\n" + "<p>\r\n"
						+ "Findwith the C& B Sheet and Valuation report for you Quotation Number ."+quotationEntity.getNumber()+" \r\n"
						+ "</p>\r\n" + "\r\n" + "<p>Thank you,</p>\r\n" + "</body>\r\n" + "</html>");
				emailRequest.setCc("ik0c113969@techmahindra.com");
				emailRequest.setPdfFile(emailPdfFileList);
				
				emailRequest.setTo(newQuotationPDFGenerationDto.getEmail());
				System.out.println("Email has been to sent to MPH successfully");
				
				RestTemplate restTemplate = new RestTemplate();
				String url = "https://email-api-uat.apps.nposepgs.licindia.com/api/email";
				try {
					new ObjectMapper().writer().writeValueAsString(emailRequest);
					result = restTemplate.postForObject(url, emailRequest, String.class);
					return ApiResponseDto.success("Email has been to sent to MPH successfully");
				} catch (Exception e) {
					result = "Exception occured while sending email " + e.getMessage();
				}
			}else {
				return ApiResponseDto.errorMessage(null,null,"Development Environment is true..Email is not sent");				
			}
		}else {
			return ApiResponseDto.errorMessage(null,null,"MPH email is not available");				
		}
}else {
	return ApiResponseDto.errorMessage(null,null,"MPH detail is Empty or null for the proposal number");			
}
				return ApiResponseDto.success(null);			
		
		
		}
		
		
		
		@PostMapping("filterforclaim")
		public ApiResponseDto<List<TempPolicyClaimPropsDto>> claimFilterSearch(
				@RequestBody TempPolicyClaimPropsSearchDto policyClaimPropsSearchDto) {
			return policyClaimService.claimFilterSearch(policyClaimPropsSearchDto);

		}
		
		@GetMapping(value="getclaimsubstatus/{claimStatusId}")
		public Long getClaimSubStatus(@PathVariable("claimStatusId" )Long claimStatusId) {
			return policyClaimService.getClaimSubStatus(claimStatusId);
			
		}
		
		
		
		
		
		
		
		
		
		
		
}
