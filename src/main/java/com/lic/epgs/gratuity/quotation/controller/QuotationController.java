package com.lic.epgs.gratuity.quotation.controller;

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
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.lic.epgs.gratuity.common.search.SearchRequest;
import com.lic.epgs.gratuity.policy.dto.GenerateCBSheetPdfDto;
import com.lic.epgs.gratuity.policy.entity.StagingPolicyEntity;
import com.lic.epgs.gratuity.policy.renewal.dto.EmailPdfFileDto;
import com.lic.epgs.gratuity.policy.renewal.dto.EmailRequestPayload;
import com.lic.epgs.gratuity.policy.repository.custom.StagingPolicyCustomRepository;
import com.lic.epgs.gratuity.quotation.dto.NewQuotationDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationBasicDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationPDFGenerationDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationSearchDto;
import com.lic.epgs.gratuity.quotation.entity.MasterQuotationEntity;
import com.lic.epgs.gratuity.quotation.entity.QuotationEntity;
import com.lic.epgs.gratuity.quotation.repository.MasterQuotationRepository;
import com.lic.epgs.gratuity.quotation.repository.QuotationRepository;
import com.lic.epgs.gratuity.quotation.service.QuotationPdfService;
import com.lic.epgs.gratuity.quotation.service.QuotationService;
import com.lic.epgs.gratuity.quotation.service.impl.QuotationPdfServiceImpl;

/**
 * @author Gopi
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/quotation")
public class QuotationController {

	@Value("${app.commonModuleServiceEndpoint}")
	private String endPoint;

	@Value("${temp.pdf.location}")
	private String templocation;

	@Autowired
	private QuotationService quotationService;

	@Autowired
	private QuotationRepository quotationRepository;

	@Autowired
	private MasterQuotationRepository masterQuotationRepository;

	@Autowired
	private StagingPolicyCustomRepository stagingPolicyCustomRepository;
	
	@Autowired
	private QuotationPdfService quotationPdfService;

	@Autowired
	private QuotationPdfServiceImpl quotationPdfServiceImpl;
	
	@PostMapping(value = "{proposalNumber}")
	public ApiResponseDto<QuotationDto> create(@PathVariable("proposalNumber") String proposalNumber,
			@RequestBody NewQuotationDto newQuotationDto) {
		return quotationService.create(proposalNumber, newQuotationDto);
	}

	@GetMapping(value = "CheckPolicyExistForProposalNumber/{proposalNumber}")
	public ApiResponseDto<QuotationBasicDto> CheckPolicyExistForProposalNumber(
			@PathVariable("proposalNumber") String proposalNumber) {
		QuotationBasicDto quotationBasicDto = new QuotationBasicDto();
		List<StagingPolicyEntity> get = stagingPolicyCustomRepository.findinProposalnumberexitpolicyno(proposalNumber);
		if (get.size() > 0) {
			quotationBasicDto.setProposalStatus("true") ;
		} else {
			if(quotationRepository.checkInProposalQuotationCreatedCount(proposalNumber) ==0) {
				quotationBasicDto.setProposalStatus("false") ;
			}else {
				quotationBasicDto.setProposalStatus("QUOTATION");
				quotationBasicDto.setQuotationNumber(quotationRepository.getProposalQuotationCreatedNumber(proposalNumber));
			
			}
			
		}
		return ApiResponseDto.created(quotationBasicDto);
	}

	@PutMapping(value = "{id}")
	public ApiResponseDto<QuotationDto> update(@PathVariable("id") Long id,
			@RequestBody QuotationBasicDto quotationBasicDto) {
		return quotationService.update(id, quotationBasicDto);
	}

	@PostMapping(value = "activate/{id}")
	public ApiResponseDto<QuotationDto> activateMasterQuotation(@PathVariable("id") Long id) {
		return quotationService.activateMasterQuotation(id);
	}

	@GetMapping(value = "{id}/{type}")
	public ApiResponseDto<QuotationDto> findById(@PathVariable("id") Long id, @PathVariable("type") String type) {
		return quotationService.findById(id, type);
	}

	@GetMapping(value = "proposalnumber/{proposalNumber}")
	public ApiResponseDto<QuotationDto> findByQuotationNumber(@PathVariable("proposalNumber") String proposalNumber) {
		return quotationService.findByProposalNumber(proposalNumber);
	}

	@PostMapping(value = "/search")
	public Page<QuotationDto> search(@RequestBody SearchRequest request) {
		return quotationService.search(request);
	}

	@PostMapping(value = "/filter")
	public ApiResponseDto<List<QuotationDto>> filter(@RequestBody QuotationSearchDto quotationSearchDto) {
		return quotationService.filter(quotationSearchDto);
	}

	@PostMapping(value = "/associatebankaccount/{id}/{bankaccountid}/{modifiedBy}")
	public ApiResponseDto<QuotationDto> associateBankAccount(@PathVariable("id") Long id,
			@PathVariable("bankaccountid") Long bankAccountId, @PathVariable("modifiedBy") String modifiedBy) {
		return quotationService.associateBankAccount(id, bankAccountId, modifiedBy);
	}

	@PostMapping(value = "/associatecontactperson/{id}/{contactpersonid}/{modifiedBy}")
	public ApiResponseDto<QuotationDto> associateConcatPerson(@PathVariable("id") Long id,
			@PathVariable("contactpersonid") Long contactPersonId, @PathVariable("modifiedBy") String modifiedBy) {
		return quotationService.associateContactPerson(id, contactPersonId, modifiedBy);
	}

	@PostMapping(value = "/submitforapproval/{id}/{username}")
	public ApiResponseDto<QuotationDto> submitForApproval(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return quotationService.submitForApproval(id, username);
	}

	@PostMapping(value = "/sendbacktomaker/{id}/{username}")
	public ApiResponseDto<QuotationDto> sendBackToMaker(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return quotationService.sendBackToMaker(id, username);
	}

	@PostMapping(value = "/approve/{id}/{username}")
	public ApiResponseDto<QuotationDto> approve(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return quotationService.approve(id, username);
	}

	@PostMapping(value = "/reject/{id}/{username}")
	public ApiResponseDto<QuotationDto> reject(@PathVariable("id") Long id, @RequestBody QuotationDto quotationDto,
			@PathVariable("username") String username) {
		return quotationService.reject(id, username, quotationDto);
	}

	@PostMapping(value = "/clone/{id}/{type}/{username}")
	public ApiResponseDto<QuotationDto> clone(@PathVariable("id") Long id, @PathVariable("type") String type,
			@PathVariable("username") String username) {
		return quotationService.clone(id, type, username);
	}

	@PostMapping(value = "/escalatetoco/{id}/{username}")
	public ApiResponseDto<QuotationDto> escalateToCo(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return quotationService.escalateToCo(id, username);
	}

	@PostMapping(value = "/escalatetozo/{id}/{username}")
	public ApiResponseDto<QuotationDto> escalateToZo(@PathVariable("id") Long id,
			@PathVariable("username") String username) {
		return quotationService.escalateToZo(id, username);
	}

	// Master Quotation end-points
	@PutMapping(value = "/consent/{id}")
	public ApiResponseDto<QuotationDto> saveConsent(@PathVariable("id") Long id,
			@RequestBody QuotationDto quotationDto) {
		return quotationService.saveConsent(id, quotationDto);
	}

	@GetMapping(value = "generatepdf/{quotationId}/{taggedStatusId}")
	public ApiResponseDto<List<QuotationPDFGenerationDto>> generatePDF(@PathVariable("quotationId") Long quotationId,
			@PathVariable("taggedStatusId") Long taggedStatusId) {
		return quotationPdfService.generatePDF(quotationId, taggedStatusId);
	}

	@GetMapping(value = "adjustmentPDF/{quotationId}/{reportType}")
	public void adjustmentPDF(@PathVariable("quotationId") Long quotationId,
			@PathVariable("reportType") String reportType, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String pdfDocumentValuationReport = quotationPdfService.adjustmentVoucher(quotationId, reportType);

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
		String headerValue = String.format("attachment; filename=\"%s\"", quotationId + ".pdf");
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

	@GetMapping(value = { "downloadReport/report" })
	public void quotationValuationReport(@RequestParam("quotationId") Long quotationId,
			@RequestParam("reportType") String reportType, @RequestParam("tmpPolicyId") Long tmpPolicyId,@RequestParam("taggedStatusId") Long taggedStatusId,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		String headerValue=null;
		String pdfDocumentValuationReport = quotationPdfService.generateReport(quotationId,tmpPolicyId, reportType, taggedStatusId);
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
		
		if(tmpPolicyId==0) {
		 headerValue = String.format("attachment; filename=\"%s\"", quotationId + ".pdf");
		}else {
			 headerValue = String.format("attachment; filename=\"%s\"", tmpPolicyId + ".pdf");
		}
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

// Scheduled for 1 hour
//	@Scheduled(cron = "0 1 * * * ?") 

// Scheduled for 24 hour(12 AM)
	@Scheduled(cron = "0 1 1 ? * * ")
	public void scheduleTask() throws ParseException {

		List<MasterQuotationEntity> masterQuotationEntity = masterQuotationRepository.findNotInPolicy();

		for (MasterQuotationEntity getMasterQuotationEntity : masterQuotationEntity) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
			Date date1;
			Date date2;
			String createdDate = getMasterQuotationEntity.getCreatedDate().toString();

			String[] str_SplitEntryDate = createdDate.split(" ");

			String str_CurrentDate = timeStamp.toString();
			String[] str_SplitCurrentDate = str_CurrentDate.split(" ");

			date1 = sdf.parse(str_SplitEntryDate[0]);
			date2 = sdf.parse(str_SplitCurrentDate[0]);
			long diffInMillies = Math.abs(date1.getTime() - date2.getTime());
			long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

			System.out.println("  DAYS " + diff);
			if (diff > 30) {
				System.out.println("isactive " + getMasterQuotationEntity.getIsActive());
				getMasterQuotationEntity.setIsActive(false);
			}

			masterQuotationRepository.save(getMasterQuotationEntity);
		}

	}

	// karthi -- start

	@GetMapping(value = { "generatecbquotationpdf" })
	public void getcandbsheetpdf(@RequestParam("quotationId") Long quotationId,
			@RequestParam("taggedStatusId") Long taggedStatusId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String getcandbsheetpdf = quotationPdfService.getcandbsheetpdf(quotationId, taggedStatusId);

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
		String headerValue = String.format("attachment; filename=\"%s\"", quotationId + ".pdf");
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

	@GetMapping(value = "getcbsheetpdf/{quotationId}")
	public ApiResponseDto<List<GenerateCBSheetPdfDto>> getcbsheetpdf(@PathVariable("quotationId") Long quotationId,
			@PathVariable("taggedStatusId") Long taggedStatusId) {

		return quotationService.getcbsheetpdf(quotationId, taggedStatusId);

	}
	// karthi -- end

	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;

	@GetMapping("sendQuotationMail/{quotationId}/{taggedStatusId}")
	public String sendMailquatationId(@PathVariable("quotationId") Long quotationId,
			@PathVariable("taggedStatusId") Long taggedStatusId) throws IOException {

		String result = null;
		String generateReport = null;
		String quotationValuation = null;
		if (isDevEnvironment == false) {
			
			QuotationEntity quotationEntity = quotationRepository.findById(quotationId).get();
			
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

				actualObj.path("responseData").path("mphDetails").path("mphBasicDetails");
				actualObj.path("responseData").path("mphDetails").path("mphAddressDetails");
				mphRep = actualObj.path("responseData").path("mphDetails").path("mphContactDetails");
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			
			for (int i = 0; i < mphRep.size(); i++) {
				System.out.println(mphRep.get(i).path("emailID").textValue());
				if (mphRep.get(i).path("emailID").textValue() !=(null)) {
					
			generateReport = quotationPdfServiceImpl.getcandbsheetpdf(quotationId, taggedStatusId);
					
			File file = new File(generateReport);

			byte[] data = Files.readAllBytes(file.toPath());
			String s = Base64.getEncoder().encodeToString(data);

			EmailPdfFileDto emailPdftwoFileDto = new EmailPdfFileDto();

			emailPdftwoFileDto.setFileData(s);
			emailPdftwoFileDto.setFileName("QuotationEmail");
			emailPdftwoFileDto.setFileType("pdf");

			quotationValuation = quotationPdfServiceImpl.generateReport(quotationId,null, "valuationreport", taggedStatusId);

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

			emailRequest.setSubject("Renewal Reminder Email");
			emailRequest.setEmailBody("<html>\r\n" + "<head>\r\n" + "<title>Renewal Notice - Reminder</title>\r\n"
					+ "</head>\r\n" + "<body>\r\n" + "<p>Dear Policy Holder,</p>\r\n" + "\r\n" + "<p>\r\n"
					+ "Please find herewith the Policy Renewal Notice Reminder and Member List to renew your Policy.\r\n"
					+ "</p>\r\n" + "\r\n" + "<p>Thank you,</p>\r\n" + "</body>\r\n" + "</html>");
			emailRequest.setCc("ik0c113969@techmahindra.com");
			emailRequest.setPdfFile(emailPdfFileList);
			
			emailRequest.setTo(mphRep.get(i).path("emailID").textValue());
			System.out.println("MPH mail send successfully");
			break;				
		}else {
			System.out.println("MPH address is not there");
		}
	}
			RestTemplate restTemplate = new RestTemplate();
			String url = "https://email-api-uat.apps.nposepgs.licindia.com/api/email";
			try {
				new ObjectMapper().writer().writeValueAsString(emailRequest);
				result = restTemplate.postForObject(url, emailRequest, String.class);
			} catch (Exception e) {
				result = "Exception occured while sending email " + e.getMessage();
			}
		}
		return result;
	}
	
	@GetMapping(value = "deletestgdata/{proposalNumber}")
	public void get(@PathVariable("proposalNumber") String proposalNumber) {
		quotationService.deleteStaingDataByProposalNumber(proposalNumber);
	}
	
	@GetMapping(value="getquotationsubstatus/{subStatusId}")
	public Long getQuotationSubStatus(@PathVariable("subStatusId") Long subStatusId) {
		return quotationService.getQuotationSubStatus(subStatusId);
		
	}
}
