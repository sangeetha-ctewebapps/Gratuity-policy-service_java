package com.lic.epgs.gratuity.policy.renewal.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.common.repository.StandardCodeRepository;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.integration.dto.ResponseDto;
import com.lic.epgs.gratuity.policy.integration.serviceImpl.AccountingIntegrationServiceImpl;
import com.lic.epgs.gratuity.policy.renewal.dto.EmailPdfFileDto;
import com.lic.epgs.gratuity.policy.renewal.dto.EmailRequestDto;
import com.lic.epgs.gratuity.policy.renewal.dto.EmailRequestPayload;
import com.lic.epgs.gratuity.policy.renewal.dto.PolicyRenewalRemainderDto;
import com.lic.epgs.gratuity.policy.renewal.entity.EmailMessagesEntity;
import com.lic.epgs.gratuity.policy.renewal.entity.PolicyRenewalRemainderEntity;
import com.lic.epgs.gratuity.policy.renewal.repository.EmailMessagesRepository;
import com.lic.epgs.gratuity.policy.renewal.repository.PolicyRenewalRemainderRepository;
import com.lic.epgs.gratuity.policy.renewal.service.PolicyRenewalRemainderService;
import com.lic.epgs.gratuity.policy.renewal.service.impl.RenewalServiceImpl;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;
import com.lic.epgs.gratuity.policy.service.PolicyPdfService;
import com.lic.epgs.gratuity.policy.service.impl.PolicyServiceImpl;

@RestController
@CrossOrigin("*")
@RequestMapping({ "api/renewal" })
public class PolicyRenewalRemainderController {

	@Autowired
	RestTemplate restTemplate;

	@Value("${temp.pdf.location}")
	private String templocation;

	protected final Logger logger = LogManager.getLogger(getClass());

	private Long renewalEmail = 12L;
	private Long renewalReminderEmail = 13L;
	private Long renewalReminder = 14L;
	
	@Autowired
	private PolicyRenewalRemainderService policyRenewalRemainderService;

	@Autowired
	private PolicyRenewalRemainderRepository policyRenewalRemainderRepository;

	@Autowired
	private StandardCodeRepository standardCodeRepository;

	@Autowired
	private EmailMessagesRepository emailMessagesRepository;

	@Autowired
	private MPHRepository mPHRepository;

	@Autowired
	private PolicyServiceImpl policyServiceImpl;

	@Autowired
	private RenewalServiceImpl renewalServiceImpl;
	
	@Autowired
	private AccountingIntegrationServiceImpl accountingIntegrationServiceImpl;
	
	@Autowired
	private PolicyPdfService policyPdfService;
	
	@Autowired
	private MasterPolicyCustomRepository masterPolicyCustomRepository;

	@PostMapping(value = "createrenewal")
	public ApiResponseDto<PolicyRenewalRemainderDto> createrenewal(
			@RequestBody PolicyRenewalRemainderDto policyRenewalRemainderDto) {
		return policyRenewalRemainderService.createrenewal(policyRenewalRemainderDto);
	}
	@Value("${app.isDevEnvironmentForRenewals}")
	private Boolean isDevEnvironmentForRenewals;
//	@Scheduled(cron = "0 1 * * * ?")
	@GetMapping("updateStatusremainder")
	public String scheduleTask() throws ParseException {
		StandardCodeEntity standardCodeEntity = standardCodeRepository.findById(renewalEmail).get();
		List<MasterPolicyEntity> masterPolicyEntity = masterPolicyCustomRepository
				.findNotInPolicyRenewal(standardCodeEntity.getValue());
		Long emailId = 1L;
		if (masterPolicyEntity.size() != 0) {
			for (MasterPolicyEntity getMasterPolicyEntity : masterPolicyEntity) {

				EmailMessagesEntity emailMessageEntity = emailMessagesRepository.findById(emailId).get();
				MPHEntity mPHEntity = mPHRepository.findById(getMasterPolicyEntity.getMasterpolicyHolder()).get();
				

//				byte[] bytes = RenewalHelper.RenewalDocument();
//				MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//				MimeMessageHelper mimeMessageHelper;
//				mPHEntity.setEmailId("vigneshvasu108@gmail.com");
				if(isDevEnvironmentForRenewals == false) {
				try {

					ResponseDto responseDto=accountingIntegrationServiceImpl.commonmasterserviceUnitByCode(getMasterPolicyEntity.getUnitCode());

					String result = null;

					byte[] data1 =  policyServiceImpl.getPolicyMemberDtls(getMasterPolicyEntity.getId());
					
					String bs=Base64.getEncoder().encodeToString(data1);
					
					EmailPdfFileDto emailPdfFileDto1 = new EmailPdfFileDto();
					emailPdfFileDto1.setFileData(bs);
					emailPdfFileDto1.setFileName("Active Member List");
					emailPdfFileDto1.setFileType("Excel");
					
					String renewalnotice=renewalServiceImpl.renewalNoticepdf(getMasterPolicyEntity.getId());
					File file = new File(renewalnotice);
					  
				    byte[] data = Files.readAllBytes(file.toPath());
				    String s= Base64.getEncoder().encodeToString(data);

					EmailPdfFileDto emailPdfFileDto = new EmailPdfFileDto();
					emailPdfFileDto.setFileData(s);
					emailPdfFileDto.setFileName("Renewal Notice - Reminder");
					emailPdfFileDto.setFileType("pdf");

					ArrayList<EmailPdfFileDto> emailPdfFileList = new ArrayList<>();
					emailPdfFileList.add(emailPdfFileDto);
					emailPdfFileList.add(emailPdfFileDto1);
					
					EmailRequestPayload emailRequest = new EmailRequestPayload();
					if(mPHEntity.getEmailId()!=null) {
						emailRequest.setTo(mPHEntity.getEmailId());
					}
					else{
						emailRequest.setTo(responseDto.getEmailId());
					}
					emailRequest.setSubject("Renewal Notice Email");
					emailRequest.setEmailBody("<html>\r\n" + "<head>\r\n" + "<title>Renewal Notice - Reminder</title>\r\n"
							+ "</head>\r\n" + "<body>\r\n" + "<p>Dear Policy Holder,</p>\r\n" + "\r\n" + "<p>\r\n"
							+ "Please find herewith the Policy Renewal Notice Reminder and Member List to renew your Policy.\r\n"
							+ "</p>\r\n" + "\r\n" + "<p>Thank you,</p>\r\n" + "</body>\r\n" + "</html>");
					emailRequest.setPdfFile(emailPdfFileList);

					RestTemplate restTemplate = new RestTemplate();
					String url = "https://email-api-uat.apps.nposepgs.licindia.com/api/email";
					try {
						new ObjectMapper().writer().writeValueAsString(emailRequest);
						result = restTemplate.postForObject(url, emailRequest, String.class);
					} catch (Exception e) {
						result = "Exception occured while sending email " + e.getMessage();
					}
				return result;
				}catch(Exception e) {
					
				}
				}
					if (policyRenewalRemainderRepository
							.findByPolicyandType(getMasterPolicyEntity.getId(), emailMessageEntity.getSubject(),getMasterPolicyEntity.getAnnualRenewlDate())
							.size() == 0) {
						PolicyRenewalRemainderEntity policyRenewalRemainderEntity = new PolicyRenewalRemainderEntity();
						policyRenewalRemainderEntity.setPolicyId(getMasterPolicyEntity.getId());
						policyRenewalRemainderEntity.setAnnualrenewaldate(getMasterPolicyEntity.getAnnualRenewlDate());
						policyRenewalRemainderEntity.setEmailResponse("success");
						policyRenewalRemainderEntity.setEmailSubject(emailMessageEntity.getSubject());
						policyRenewalRemainderEntity.setEmailTo(mPHEntity.getEmailId());
						policyRenewalRemainderEntity.setIsActive(true);
						policyRenewalRemainderEntity.setCreatedBy("maker");
						policyRenewalRemainderEntity.setRemindedDateTime(new Date());
						policyRenewalRemainderEntity.setCreatedDate(new Date());
						policyRenewalRemainderRepository.save(policyRenewalRemainderEntity);
						System.out.println(policyRenewalRemainderEntity.getId());
					}
			
//					 javaMailSender.send(mimeMessageHelper.getMimeMessage());
					// return "Mail sent Successfully";
					System.out.println("Mail sent Successfully");
				} 
			}
	
	
		StandardCodeEntity standardCodeEntityRemainder = standardCodeRepository.findById(renewalReminderEmail).get();
		List<MasterPolicyEntity> masterPolicyEntityRemainder = masterPolicyCustomRepository
				.findNotInPolicyRenewal(standardCodeEntityRemainder.getValue());
	
		if (masterPolicyEntityRemainder.size() != 0) {
			for (MasterPolicyEntity getMasterPolicyEntity : masterPolicyEntityRemainder) {
				MPHEntity mPHEntity = mPHRepository.findById(getMasterPolicyEntity.getMasterpolicyHolder()).get();
				EmailMessagesEntity emailMessageEntity = emailMessagesRepository.findById(2L).get();  
				
				ResponseDto responseDto=accountingIntegrationServiceImpl.commonmasterserviceUnitByCode(getMasterPolicyEntity.getUnitCode());

//				mPHEntity.setEmailId("vigneshvasu108@gmail.com");
				if(isDevEnvironmentForRenewals == false) {
				try {
					
					String result = null;
					
//					byte[] bytes = RenewalHelper.RenewalDocumentDynamic(getMasterPolicyEntity);

					byte[] data1 =  policyServiceImpl.getPolicyMemberDtls(getMasterPolicyEntity.getId());
					
					String bs=Base64.getEncoder().encodeToString(data1);
					
					EmailPdfFileDto emailPdfFileDto1 = new EmailPdfFileDto();
					emailPdfFileDto1.setFileData(bs);
					emailPdfFileDto1.setFileName("Active Member List");
					emailPdfFileDto1.setFileType("Excel");
					
					String renewalnotice=renewalServiceImpl.renewalRemainderNoticepdf(getMasterPolicyEntity.getId());
					File file = new File(renewalnotice);
					  
				    byte[] data = Files.readAllBytes(file.toPath());
				    String s= Base64.getEncoder().encodeToString(data);

					EmailPdfFileDto emailPdfFileDto = new EmailPdfFileDto();
					emailPdfFileDto.setFileData(s);
					emailPdfFileDto.setFileName("Renewal Remainder Notice - Reminder");
					emailPdfFileDto.setFileType("pdf");

					ArrayList<EmailPdfFileDto> emailPdfFileList = new ArrayList<>();
					emailPdfFileList.add(emailPdfFileDto);
					
					EmailRequestPayload emailRequest = new EmailRequestPayload();
					
					if(mPHEntity.getEmailId()!=null) {
						emailRequest.setTo(mPHEntity.getEmailId());
					}
					else{
						emailRequest.setTo(responseDto.getEmailId());
					}
					
					emailRequest.setSubject("Renewal Notice Email");
					emailRequest.setEmailBody("<html>\r\n" + "<head>\r\n" + "<title>Renewal Notice - Reminder</title>\r\n"
							+ "</head>\r\n" + "<body>\r\n" + "<p>Dear Policy Holder,</p>\r\n" + "\r\n" + "<p>\r\n"
							+ "Please find herewith the Policy Renewal Notice Reminder and Member List to renew your Policy.\r\n"
							+ "</p>\r\n" + "\r\n" + "<p>Thank you,</p>\r\n" + "</body>\r\n" + "</html>");
					emailRequest.setPdfFile(emailPdfFileList);

					RestTemplate restTemplate = new RestTemplate();
					String url = "https://email-api-uat.apps.nposepgs.licindia.com/api/email";
					try {
						new ObjectMapper().writer().writeValueAsString(emailRequest);
						result = restTemplate.postForObject(url, emailRequest, String.class);
					} catch (Exception e) {
						result = "Exception occured while sending email " + e.getMessage();
					}
				return result;
				}catch(Exception e) {
					
				}
				}
					if (policyRenewalRemainderRepository
							.findByPolicyandType(getMasterPolicyEntity.getId(), emailMessageEntity.getSubject(),getMasterPolicyEntity.getAnnualRenewlDate())
							.size() == 0) {
						PolicyRenewalRemainderEntity policyRenewalRemainderEntity = new PolicyRenewalRemainderEntity();
						policyRenewalRemainderEntity.setPolicyId(getMasterPolicyEntity.getId());
						policyRenewalRemainderEntity.setAnnualrenewaldate(getMasterPolicyEntity.getAnnualRenewlDate());
						policyRenewalRemainderEntity.setEmailResponse("success");
						policyRenewalRemainderEntity.setEmailSubject(emailMessageEntity.getSubject());
						policyRenewalRemainderEntity.setEmailTo(mPHEntity.getEmailId());
						policyRenewalRemainderEntity.setIsActive(true);
						policyRenewalRemainderEntity.setCreatedBy("maker");
						policyRenewalRemainderEntity.setRemindedDateTime(new Date());
						policyRenewalRemainderEntity.setCreatedDate(new Date());
//					 javaMailSender.send(mimeMessageHelper.getMimeMessage());
//					// return "Mail sent Successfully";
						policyRenewalRemainderRepository.save(policyRenewalRemainderEntity);
					}
					System.out.println("Mail sent Successfully");
				} 
			}
		
		StandardCodeEntity standardCodeRenewalEntity = standardCodeRepository.findById(renewalReminder).get();
		List<MasterPolicyEntity> masterPolicyRenewalEntity = masterPolicyCustomRepository
				.findNotInPolicyRenewal(standardCodeRenewalEntity.getValue());
		if (masterPolicyRenewalEntity.size() != 0) {
			for (MasterPolicyEntity getMasterPolicyEntity : masterPolicyRenewalEntity) {

				EmailMessagesEntity emailMessageEntity = emailMessagesRepository.findById(emailId).get();
				MPHEntity mPHEntity = mPHRepository.findById(getMasterPolicyEntity.getMasterpolicyHolder()).get();
				

//				byte[] bytes = RenewalHelper.RenewalDocument();
//				MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//				MimeMessageHelper mimeMessageHelper;
//				mPHEntity.setEmailId("vigneshvasu108@gmail.com");
				if(isDevEnvironmentForRenewals == false) {
				try {

					ResponseDto responseDto=accountingIntegrationServiceImpl.commonmasterserviceUnitByCode(getMasterPolicyEntity.getUnitCode());

					String result = null;

					byte[] data1 =  policyServiceImpl.getPolicyMemberDtls(getMasterPolicyEntity.getId());
					
					String bs=Base64.getEncoder().encodeToString(data1);
					
					EmailPdfFileDto emailPdfFileDto1 = new EmailPdfFileDto();
					emailPdfFileDto1.setFileData(bs);
					emailPdfFileDto1.setFileName("Active Member List");
					emailPdfFileDto1.setFileType("Excel");
					
					String renewalnotice=renewalServiceImpl.renewalNoticepdf(getMasterPolicyEntity.getId());
					File file = new File(renewalnotice);
					  
				    byte[] data = Files.readAllBytes(file.toPath());
				    String s= Base64.getEncoder().encodeToString(data);

					EmailPdfFileDto emailPdfFileDto = new EmailPdfFileDto();
					emailPdfFileDto.setFileData(s);
					emailPdfFileDto.setFileName("Renewal Notice - Reminder");
					emailPdfFileDto.setFileType("pdf");

					ArrayList<EmailPdfFileDto> emailPdfFileList = new ArrayList<>();
					emailPdfFileList.add(emailPdfFileDto);
					emailPdfFileList.add(emailPdfFileDto1);
					
					EmailRequestPayload emailRequest = new EmailRequestPayload();
					if(mPHEntity.getEmailId()!=null) {
						emailRequest.setTo(mPHEntity.getEmailId());
					}
					else{
						emailRequest.setTo(responseDto.getEmailId());
					}
					emailRequest.setSubject("Renewal Notice Email");
					emailRequest.setEmailBody("<html>\r\n" + "<head>\r\n" + "<title>Renewal Notice - Reminder</title>\r\n"
							+ "</head>\r\n" + "<body>\r\n" + "<p>Dear Policy Holder,</p>\r\n" + "\r\n" + "<p>\r\n"
							+ "Please find herewith the Policy Renewal Notice Reminder and Member List to renew your Policy.\r\n"
							+ "</p>\r\n" + "\r\n" + "<p>Thank you,</p>\r\n" + "</body>\r\n" + "</html>");
					emailRequest.setPdfFile(emailPdfFileList);

					RestTemplate restTemplate = new RestTemplate();
					String url = "https://email-api-uat.apps.nposepgs.licindia.com/api/email";
					try {
						new ObjectMapper().writer().writeValueAsString(emailRequest);
						result = restTemplate.postForObject(url, emailRequest, String.class);
					
					} catch (Exception e) {
						result = "Exception occured while sending email " + e.getMessage();
					}
				return result;
				}catch(Exception e) {
					
				}
				}
				System.out.println("Mail sent Successfully");
					if (policyRenewalRemainderRepository
							.findByPolicyandType(getMasterPolicyEntity.getId(), emailMessageEntity.getSubject(),getMasterPolicyEntity.getAnnualRenewlDate())
							.size() == 0) {
						PolicyRenewalRemainderEntity policyRenewalRemainderEntity = new PolicyRenewalRemainderEntity();
						policyRenewalRemainderEntity.setPolicyId(getMasterPolicyEntity.getId());
						policyRenewalRemainderEntity.setAnnualrenewaldate(getMasterPolicyEntity.getAnnualRenewlDate());
						policyRenewalRemainderEntity.setEmailResponse("success");
						policyRenewalRemainderEntity.setEmailSubject(emailMessageEntity.getSubject());
						policyRenewalRemainderEntity.setEmailTo(mPHEntity.getEmailId());
						policyRenewalRemainderEntity.setIsActive(true);
						policyRenewalRemainderEntity.setCreatedBy("maker");
						policyRenewalRemainderEntity.setRemindedDateTime(new Date());
						policyRenewalRemainderEntity.setCreatedDate(new Date());
						policyRenewalRemainderRepository.save(policyRenewalRemainderEntity);
						System.out.println(policyRenewalRemainderEntity.getId());
					}
			
//					 javaMailSender.send(mimeMessageHelper.getMimeMessage());
					// return "Mail sent Successfully";
					System.out.println("Mail sent Successfully");
				} 
			}
		return null;
	}


	@GetMapping("sendMailForPolicy/{policyId}/{taggedStatusId}")
	public String sendMailForPolicyId(@PathVariable("policyId") Long policyId,
			@PathVariable("taggedStatusId") Long taggedStatusId) throws IOException {

////				byte[] bytes = RenewalHelper.RenewalDocument();Z vb
////				MimeMessage mimeMessage = javaMailSender.createMimeMessage();
////				MimeMessageHelper mimeMessageHelper;
//	
//		/*EmailDetailDto emailDetailDto = new EmailDetailDto();
//		String output = "INIT";
//		
//				try {
//					// Setting multipart as true for attachments to
//					// be send
////					mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
////					mimeMessageHelper.setFrom(sender);
////					mimeMessageHelper.setSubject(emailMessageEntity.getSubject());
////					mimeMessageHelper.setTo(mPHEntity.getEmailId());
////					mimeMessageHelper.setText(emailMessageEntity.getContent(), true);
//		
////					final InputStreamSource attachmentSource = new ByteArrayResource(bytes);
//					// Adding the attachment
////	                ByteArrayDataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
////	                mimeMessageHelper.addAttachment("RenewalAttachment", dataSource);
//					
////					FileSystemResource file = new FileSystemResource(new File(bytes));
////
////					mimeMessageHelper.addAttachment(file.getFilename(), file);
////
////					FileSystemResource file1 = new FileSystemResource(new File(details.getAttachmentrenewal()));
////
////					mimeMessageHelper.addAttachment(file1.getFilename(), file1);
//
//					// Sending the mail
////					javaMailSender.send(mimeMessage);
//				
//					if(isDevEnvironment == true) {
//					try {
//			
//						URL url = new URL("https://email-api-uat.apps.nposepgs.licindia.com/api/email");
//						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//						conn.setRequestMethod("POST");
//						conn.setRequestProperty("Content-Type", "application/json");
//						conn.setRequestProperty("Accept", "application/json");
//						conn.setDoOutput(true);
//						ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//						String json = ow.writeValueAsString(emailDetailDto);
//						try (OutputStream os = conn.getOutputStream()) {
//							byte[] input = json.getBytes("utf-8");	
//							os.write(input);
//						}
//						if (conn.getResponseCode() != 200) {
//							throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
//						}
//						BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
//						output = br.readLine();
//						conn.disconnect();
//						ObjectMapper mapper = new ObjectMapper();
//						JsonFactory factory = mapper.getFactory();
//						JsonParser parser = factory.createParser(output);
//						JsonNode actualObj = mapper.readTree(parser);
//						System.out.println(actualObj);
//						System.out.println("Mail sent Successfully");
//					} catch (MalformedURLException e) {
//						output = e.getMessage();
//						e.printStackTrace();
//					} catch (IOException e) {
//						output = e.getMessage();
//						e.printStackTrace();
//					}
//}
////					 javaMailSender.send(mimeMessageHelper.getMimeMessage());
//					// return "Mail sent Successfully";
//				} catch (Exception e) {
//				}			
//				try {
//				String url = "https://email-api-uat.apps.nposepgs.licindia.com/api/email";
//				if (StringUtils.isNotBlank(url)) {
//					url = url + emailDetailDto;
//				      HttpHeaders headers = new HttpHeaders();
//				      
//				      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//				      
//				      HttpEntity<EmailDetailDto> entity = new HttpEntity<EmailDetailDto>(emailDetailDto,headers);
//					   restTemplate.exchange(url, HttpMethod.POST, entity,  String.class).getBody();
//				}
//				}catch (Exception e) {
//					e.getMessage();	
//					e.printStackTrace();
//				}		
//				
//				*/  
//		

		String result = null;
		String generateReport = null;

		if (isDevEnvironmentForRenewals == false) {

			logger.info("Sending Email start");
			generateReport = policyPdfService.getcandbsheetpdf(policyId, taggedStatusId);
			logger.info(generateReport);
		    File file = new File(generateReport);
		    logger.info(file);
		    
		    byte[] data = Files.readAllBytes(file.toPath());
		    logger.info(data);
		    String s= Base64.getEncoder().encodeToString(data);
			logger.info(s);

			EmailPdfFileDto emailPdfFileDto = new EmailPdfFileDto();
			logger.info(emailPdfFileDto);
			emailPdfFileDto.setFileData(s);
			logger.info(emailPdfFileDto.getFileData());
			emailPdfFileDto.setFileName("Renewal Notice - Reminder");
			logger.info(emailPdfFileDto.getFileName());
			emailPdfFileDto.setFileType("pdf");
			logger.info(emailPdfFileDto.getFileType());

			ArrayList<EmailPdfFileDto> emailPdfFileList = new ArrayList<>();
			emailPdfFileList.add(emailPdfFileDto);
			logger.info(emailPdfFileList);
			
			EmailRequestPayload emailRequest = new EmailRequestPayload();
			logger.info(emailRequest);
			emailRequest.setTo("GL0C113970@TechMahindra.com");
			logger.info(emailRequest.getTo());
			emailRequest.setSubject("Renewal Reminder Email");
			logger.info(emailRequest.getSubject());
			emailRequest.setEmailBody("<html>\r\n" + "<head>\r\n" + "<title>Renewal Notice - Reminder</title>\r\n"
					+ "</head>\r\n" + "<body>\r\n" + "<p>Dear Policy Holder,</p>\r\n" + "\r\n" + "<p>\r\n"
					+ "Please find herewith the Policy Renewal Notice Reminder and Member List to renew your Policy.\r\n"
					+ "</p>\r\n" + "\r\n" + "<p>Thank you,</p>\r\n" + "</body>\r\n" + "</html>");
			logger.info(emailRequest.getEmailBody());
			logger.info(emailRequest.getCc());
			emailRequest.setPdfFile(emailPdfFileList);
			logger.info(emailRequest.getPdfFile());

			RestTemplate restTemplate = new RestTemplate();
			logger.info(restTemplate);
			String url = "https://email-api-uat.apps.nposepgs.licindia.com/api/email";
			logger.info(url);
			try {
				new ObjectMapper().writer().writeValueAsString(emailRequest);
				result = restTemplate.postForObject(url, emailRequest, String.class);
				logger.info(result);
			} catch (Exception e) {
				result = "Exception occured while sending email " + e.getMessage();
				logger.info(result);
			}
		}
		logger.info(result);
		return result;
	}

//	@Autowired RestTemplate restTemplate1;
//	 
//	@Value("${app.isDevEnvironment}")private Boolean isDevEnvironment;
//	@GetMapping("sendMailAttachment/{policyId}/{taggedStatusId}")
//	public String sendMailForPolicyId1( @PathVariable("policyId") Long policyId,@PathVariable("taggedStatusId") Long taggedStatusId){
//		
//		 String result = null;
//		 String generateReport =null;
//				
//		if(isDevEnvironment == false) { 
////			try {
////				 generateReport = policyServiceImpl.getcandbsheetpdf(policyId, taggedStatusId);
////			} catch (IOException e1) {
////				e1.printStackTrace();
////			}	
//		      	ArrayList<EmailPdfFileDto> emailPdfFileList = new ArrayList<>(); 	
//		      	EmailPdfFileDto emailPdfFileDto = new EmailPdfFileDto();
//		      
//				emailPdfFileDto.setFileData(generateReport);
//		      	emailPdfFileDto.setFileName("policy doc");
//		      	emailPdfFileDto.setFileType("pdf");	      	
//		      	emailPdfFileList.add(emailPdfFileDto);
//				EmailRequestPayload emailRequest = new EmailRequestPayload();
//				                                                               
//				emailRequest.setTo("GL0C113970@TechMahindra.com");
//				emailRequest.setSubject("Renewal Reminder Email");
//				emailRequest.setEmailBody("Test Email");
//				//arrayList.clear();
//				emailRequest.setPdfFile(emailPdfFileList);
//				
//				RestTemplate restTemplate=new RestTemplate();
//				String url = "https://email-api-uat.apps.nposepgs.licindia.com/api/email";	
//				try {
//	           new ObjectMapper().writer().writeValueAsString(emailRequest);
//					 result = restTemplate.postForObject(url, emailRequest, String.class);
//				} catch (Exception e) {
//					result="Exception occured while sending email "+e.getMessage();
//				}
//		}
//				return result;
//			}

	
	@GetMapping("sendMailForPolicyId/{policyId}/{quotationId}")
	public ApiResponseDto<?> sendMailForPolicy(@PathVariable("policyId") Long policyId,@PathVariable("quotationId") Long quotationId) throws IOException {
		
		
		return  policyRenewalRemainderService.sendMailForPolicy(policyId,quotationId);
		
	}
	
	@PostMapping("sendMailForPolicyMail")
	public String sendMailForPolicyMail (@RequestBody EmailRequestDto emailRequestDto) throws IOException{
		
		return policyRenewalRemainderService.sendMailForPolicyMail(emailRequestDto);
		
	}
	
	
}
