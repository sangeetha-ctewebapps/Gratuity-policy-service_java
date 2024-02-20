package com.lic.epgs.gratuity.policy.renewal.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.integration.serviceImpl.AccountingIntegrationServiceImpl;
import com.lic.epgs.gratuity.policy.renewal.controller.PolicyRenewalRemainderController;
import com.lic.epgs.gratuity.policy.renewal.dto.EmailPdfFileDto;
import com.lic.epgs.gratuity.policy.renewal.dto.EmailRequestDto;
import com.lic.epgs.gratuity.policy.renewal.dto.EmailRequestPayload;
import com.lic.epgs.gratuity.policy.renewal.dto.PolicyRenewalRemainderDto;
import com.lic.epgs.gratuity.policy.renewal.entity.PolicyRenewalRemainderEntity;
import com.lic.epgs.gratuity.policy.renewal.helper.RenewalHelper;
import com.lic.epgs.gratuity.policy.renewal.repository.EmailMessagesRepository;
import com.lic.epgs.gratuity.policy.renewal.repository.PolicyRenewalRemainderRepository;
import com.lic.epgs.gratuity.policy.renewal.service.PolicyRenewalRemainderService;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;
import com.lic.epgs.gratuity.policy.service.PolicyPdfService;
import com.lic.epgs.gratuity.policy.service.PolicyService;
import com.lic.epgs.gratuity.policy.service.impl.PolicyServiceImpl;
import com.lic.epgs.gratuity.quotation.dto.QuotationAdjustmentPDFDto;
import com.lic.epgs.gratuity.quotation.entity.MasterQuotationEntity;
import com.lic.epgs.gratuity.quotation.repository.MasterQuotationRepository;
import com.lic.epgs.gratuity.quotation.service.QuotationPdfService;

@Service
public class PolicyRenewalRemainderServiceImpl implements PolicyRenewalRemainderService {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private PolicyRenewalRemainderRepository policyRenewalRemainderRepository;
	
	@Autowired
	private MasterPolicyRepository masterPolicyRepository;
	
	@Autowired
	private MPHRepository mphRepository;

	@Autowired
	private PolicyServiceImpl policyServiceImpl;

	@Autowired
	private RenewalServiceImpl renewalServiceImpl;
	
	@Autowired
	private MasterQuotationRepository masterQuotationRepository;
	
	@Autowired
	private QuotationPdfService quotationPdfService;
	
	@Autowired
	private PolicyService policyService;
	

	@Override
	public ApiResponseDto<PolicyRenewalRemainderDto> createrenewal(
			PolicyRenewalRemainderDto policyRenewalRemainderDto) {
		PolicyRenewalRemainderEntity policyRenewalRemainderEntity = new ModelMapper().map(policyRenewalRemainderDto,
				PolicyRenewalRemainderEntity.class);

		policyRenewalRemainderEntity.setCreatedDate(new Date());
		policyRenewalRemainderEntity.setCreatedBy(policyRenewalRemainderDto.getCreatedBy());
		policyRenewalRemainderEntity.setRemindedDateTime(new Date());
		policyRenewalRemainderEntity.setIsActive(true);
		policyRenewalRemainderRepository.save(policyRenewalRemainderEntity);

		return ApiResponseDto.success(RenewalHelper.entityRenewaltoDto(policyRenewalRemainderEntity));
	}

	@Override
	public ApiResponseDto<?> sendMailForPolicy(Long policyId, Long quotationId) throws IOException {
		EmailRequestDto emailRequestDto = new EmailRequestDto();
		MPHEntity getMail = null;
		if(policyId != 0) {
		MasterPolicyEntity getMph = masterPolicyRepository.findById(policyId).get();
		getMail = mphRepository.findById(getMph.getMasterpolicyHolder()).get();
		}else {
			MasterQuotationEntity getMph =masterQuotationRepository.findById(quotationId).get();
			
		}
		
		String mailId = getMail.getEmailId();
		emailRequestDto.setPolicyId(policyId);
		emailRequestDto.setQuotationid(quotationId);
		emailRequestDto.setEmailId(mailId);
		if(mailId != null) {
			  return ApiResponseDto.success(this.sendMailForPolicyMail(emailRequestDto));
		 }else {
			 return ApiResponseDto.errorMessage(null, null, "MailId Not Present");
		 }
	}

	@Override
	public String sendMailForPolicyMail(EmailRequestDto emailRequestDto) throws IOException  {
     String result = null;
     byte[] data1 ;
     EmailRequestPayload emailRequest = new EmailRequestPayload();
     if(emailRequestDto.getQuotationid()>0) {
	//	String pdfDocumentValuationReport = quotationPdfService.generateReport(emailRequestDto.getQuotationid(),"valuationreport", 79L);
				
	//	String getcandbsheetpdf = quotationPdfService.getcandbsheetpdf(emailRequestDto.getQuotationid(), 79L);
		data1 = policyService.findByquotationId(emailRequestDto.getQuotationid(), 79L);
		
        String bs=Base64.getEncoder().encodeToString(data1);
		
		EmailPdfFileDto emailPdfFileDto1 = new EmailPdfFileDto();
		emailPdfFileDto1.setFileData(bs);
		emailPdfFileDto1.setFileName("Active Member List");
		emailPdfFileDto1.setFileType("Excel");
		
		String getcandbsheetpdf = null;
		String pdfDocumentValuationReport = null;
		try {
			getcandbsheetpdf = quotationPdfService.getcandbsheetpdf(emailRequestDto.getQuotationid(), 79L);
			pdfDocumentValuationReport = quotationPdfService.generateReport(emailRequestDto.getQuotationid(),null,
					"valuationreport", 79L);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file = new File(getcandbsheetpdf);
		File file1 = new File(pdfDocumentValuationReport);
		  
	    byte[] data = null;
	    byte[] data2 = null;
		try {
			data = Files.readAllBytes(file.toPath());
			data2 = Files.readAllBytes(file1.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	    String s= Base64.getEncoder().encodeToString(data);
	    String s1= Base64.getEncoder().encodeToString(data2);
        //candbsheetpdf
		EmailPdfFileDto emailPdfFileDto = new EmailPdfFileDto();
		emailPdfFileDto.setFileData(s);
		emailPdfFileDto.setFileName("Candbsheetpdf");
		emailPdfFileDto.setFileType("pdf");
		
		
		
		//pdfDocumentValuationReport
		EmailPdfFileDto emailPdfFileDto2 = new EmailPdfFileDto();
		emailPdfFileDto2.setFileData(s1);
		emailPdfFileDto2.setFileName("PdfDocumentValuation");
		emailPdfFileDto2.setFileType("pdf");

		ArrayList<EmailPdfFileDto> emailPdfFileList = new ArrayList<>();
		emailPdfFileList.add(emailPdfFileDto);
		emailPdfFileList.add(emailPdfFileDto1);
		emailPdfFileList.add(emailPdfFileDto2);
		
	
		
		emailRequest.setTo(emailRequestDto.getEmailId());
		
		emailRequest.setSubject("Remainder Notice  Email");
//		emailRequest.setEmailBody("<html>\r\n" + "<head>\r\n" + "<title>Remainder Notice  - Reminder</title>\r\n"
//				+ "</head>\r\n" + "<body>\r\n" + "<p>Dear Policy Holder,</p>\r\n" + "\r\n" + "<p>\r\n"
//				+ "Please find herewith the Policy Remainder Notice  Reminder and Member List to renew your Policy.\r\n"
//				+ "</p>\r\n" + "\r\n" + "<p>Thank you,</p>\r\n" + "</body>\r\n" + "</html>");
		emailRequest.setEmailBody("<html><style>p {font-size: 16px;font-family: \"Book Antiqua\",serif;display: block;}</style><head><title>Renewal Notice - Reminder</title></head>\\n<body><p>Dear Policy Holder, <br/><br/>\nPlease find herewith the Policy Renewal Notice Reminder and Member List to renew your Policy.\n<br/><br/><br/>Thanking You,<br/>Team LIC.<br/>In-charge<br/>LIC P&GS Unit: G302\\n<br/><br/>Note: This is an electronic message.  Please do not reply to this email.<br/></p></body></html>");

		emailRequest.setPdfFile(emailPdfFileList);
		
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://email-api-uat.apps.nposepgs.licindia.com/api/email";
		try {
			new ObjectMapper().writer().writeValueAsString(emailRequest);
			result = restTemplate.postForObject(url, emailRequest, String.class);
		} catch (Exception e) {
			result = "Exception occured while sending email " + e.getMessage();
		}
     }
     
		else {
        data1=  policyServiceImpl.getPolicyMemberDtls(emailRequestDto.getPolicyId());
		
		String bs=Base64.getEncoder().encodeToString(data1);
		
		EmailPdfFileDto emailPdfFileDto1 = new EmailPdfFileDto();
		emailPdfFileDto1.setFileData(bs);
		emailPdfFileDto1.setFileName("Active Member List");
		emailPdfFileDto1.setFileType("Excel");
		
		String renewalnotice = null;
		try {
			renewalnotice = renewalServiceImpl.renewalRemainderNoticepdf(emailRequestDto.getPolicyId());
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file = new File(renewalnotice);
		  
	    byte[] data = null;
		try {
			data = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	    String s= Base64.getEncoder().encodeToString(data);

		EmailPdfFileDto emailPdfFileDto = new EmailPdfFileDto();
		emailPdfFileDto.setFileData(s);
		emailPdfFileDto.setFileName("Renewal Remainder Notice - Reminder");
		emailPdfFileDto.setFileType("pdf");

		ArrayList<EmailPdfFileDto> emailPdfFileList = new ArrayList<>();
		emailPdfFileList.add(emailPdfFileDto);
		emailPdfFileList.add(emailPdfFileDto1);
		
		
		
		if(emailRequestDto.getEmailId() !=null) {
			emailRequest.setTo(emailRequestDto.getEmailId());
		}
		else{
	//		emailRequest.setTo(responseDto.getEmailId());
		}
		emailRequest.setCc("");
		emailRequest.setSubject("Renewal Notice Email");
//		emailRequest.setEmailBody("<html>\r\n" + "<head>\r\n" + "<title>Renewal Notice - Reminder</title>\r\n"
//				+ "</head>\r\n" + "<body>\r\n" + "<p>Dear Policy Holder,</p>\r\n" + "\r\n" + "<p>\r\n"
//				+ "Please find herewith the Policy Renewal Notice Reminder and Member List to renew your Policy.\r\n"
//				+ "</p>\r\n" + "\r\n" + "<p>Thank you,</p>\r\n" + "</body>\r\n" + "</html>");
		
		emailRequest.setEmailBody("<html><style>p {font-size: 16px;font-family: \"Book Antiqua\",serif;display: block;}</style><head><title>Renewal Notice - Reminder</title></head><body><p>Dear Policy Holder, <br/><br/>\nPlease find herewith the Policy Renewal Notice Reminder and Member List to renew your Policy.\n<br/><br/><br/>Thanking You,<br/>Team LIC.<br/>In-charge<br/>LIC P&GS Unit: G302<br/><br/>Note: This is an electronic message.  Please do not reply to this email.<br/></p></body></html>");
		emailRequest.setPdfFile(emailPdfFileList);
		}
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://email-api-uat.apps.nposepgs.licindia.com/api/email";
		try {
			new ObjectMapper().writer().writeValueAsString(emailRequest);
			result = restTemplate.postForObject(url, emailRequest, String.class);
		} catch (Exception e) {
			result = "Exception occured while sending email " + e.getMessage();
		}
	return result;
	}

	

	
	

}
