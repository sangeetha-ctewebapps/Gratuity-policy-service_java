package com.lic.epgs.gratuity.policy.claim.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.CommonMasterUnitEntity;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.service.CommonModuleService;
import com.lic.epgs.gratuity.common.utils.CommonUtils;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.common.utils.NumericUtils;
import com.lic.epgs.gratuity.fund.dto.CalculateResDto;
import com.lic.epgs.gratuity.fund.dto.CalculationReqDto;
import com.lic.epgs.gratuity.fund.service.FundService;
import com.lic.epgs.gratuity.mph.entity.TempMPHEntity;
import com.lic.epgs.gratuity.mph.repository.TempMPHRepository;
import com.lic.epgs.gratuity.policy.claim.dto.ClaimCalculationSheetDto;
import com.lic.epgs.gratuity.policy.claim.dto.ClaimPayoutDto;
import com.lic.epgs.gratuity.policy.claim.dto.ClaimPayoutItemDto;
import com.lic.epgs.gratuity.policy.claim.dto.GratuityCalculationsDto;
import com.lic.epgs.gratuity.policy.claim.dto.PdfGeneratorDto;
import com.lic.epgs.gratuity.policy.claim.dto.PdfGeneratorForTableDto;
import com.lic.epgs.gratuity.policy.claim.entity.TempPolicyClaimPropsEntity;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimHelper;
import com.lic.epgs.gratuity.policy.claim.repository.TempPolicyClaimPropsRepository;
import com.lic.epgs.gratuity.policy.claim.service.PolicyClaimPdfService;
import com.lic.epgs.gratuity.policy.dto.PremiumReceiptDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalGratuityBenefitTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.entity.RenewalsGratuityBenefitPropsTMPEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.repository.RenewalGratuityBenefitPropsTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.gratuitybenefit.repository.RenewalGratuityBenefitTMPRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberNomineeDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberNomineeEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.RenewalPolicyTMPRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.StagingPolicyRepository;
import com.lowagie.text.DocumentException;

@Service
public class PolicyClaimPdfServiceImpl implements PolicyClaimPdfService {

	@Autowired
	private TempPolicyClaimPropsRepository tempPolicyClaimPropsRepository;
	
	@Autowired
	private FundService fundService;
	
	@Autowired
	private CommonModuleService commonModuleService;
	
	@Autowired
	private RenewalPolicyTMPRepository renewalPolicyTMPRepository;
	
	@Autowired
	private TempMPHRepository tempMPHRepository;
	
	@Autowired
	private RenewalPolicyTMPMemberRepository renewalPolicyTMPMemberRepository;
	
	@Autowired
	private StagingPolicyRepository stagingPolicyRepository;
	
	@Autowired
	private MasterPolicyRepository masterPolicyRepository;
	
	@Value("${app.commonModuleServiceEndpoint}")
	private String endPoint;
	
	@Autowired
	private RenewalGratuityBenefitTMPRepository renewalGratuityBenefitTMPRepository;
	
	@Autowired
	private RenewalGratuityBenefitPropsTMPRepository renewalGratuityBenefitPropsTMPRepository;
	
	@Autowired
	private CommonMasterUnitRepository commonMasterUnitRepository;
	
	@Autowired
	private CommonUtils commonUtils;
	
	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;
	
	@Value("${temp.pdf.location}")
	private String tempPdfLocation;
	
	@Override
	public String claimForwardingLetter(String userName, String payoutNumber) throws IOException { 
		InputStream islicLogo = new ClassPathResource("liclogo.png").getInputStream();
		// Uncomment below line to add watermark to the pdf
		// InputStream islicWatermark = new
		// ClassPathResource("watermark.png").getInputStream();

		byte[] bytesLogo = IOUtils.toByteArray(islicLogo);
		// Uncomment below line to add watermark to the pdf
		// byte[] bytesWatermark = IOUtils.toByteArray(islicWatermark);

		String licLogo = Base64.getEncoder().encodeToString(bytesLogo);
		// Uncomment below line to add watermark to the pdf
		// String licWatermark = Base64.getEncoder().encodeToString(bytesWatermark);

		PdfGeneratorDto pdfGeneratorDto = new PdfGeneratorDto();
		
		List<Object[]> claimGenerationPdf = tempPolicyClaimPropsRepository.claimForward(payoutNumber);
		
		pdfGeneratorDto = PolicyClaimHelper.claimpdfDto(claimGenerationPdf); 

		CommonMasterUnitEntity pdfClaimForwardLetter = new CommonMasterUnitEntity();
		
		CommonMasterUnitEntity pdfClaimForward = null;
		if (isDevEnvironment) {
			pdfClaimForward = new CommonMasterUnitEntity();
		    pdfClaimForward.setDescription("Maker");
		    pdfClaimForward.setAddress1("LIC-ADDRESS 1");
		    pdfClaimForward.setAddress2("LIC-ADDRESS 2");
		    pdfClaimForward.setAddress3("LIC-ADDRESS 3");
		    pdfClaimForward.setCityName("KUMBAKONAM");
		    pdfClaimForward.setStateName("TAMILNADU");
		    pdfClaimForward.setPincode("600182");	  
		    } else {
			pdfClaimForward = commonMasterUnitRepository.findByUnitCode(pdfGeneratorDto.getUnitCode());
		}
	
		pdfClaimForwardLetter = PolicyClaimHelper.claimforward(pdfClaimForward);
		
		boolean showLogo = true;
		String reportBody = "";
		String reportStyles = "";
		String htmlContent2 = "</div></body></html>"; // DO NOT DISTRUB THIS LINE

			reportBody = generateclaimForwardingLetter(pdfGeneratorDto,pdfClaimForwardLetter,payoutNumber)+ htmlContent2;
					
			String completehtmlContent = "<!DOCTYPE  html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
					+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><title>LIC ePGS</title><meta name=\"author\" content=\"LIC PNGS\"/><meta name=\"keywords\" content=\"Customer Communication\"/><meta name=\"description\" content=\"Report/Letter/Contract\"/><style type=\"text/css\"> body{border-width:0px;\r\n"
					+ "border-style:solid;\r\n" + "border-color:#586ec5;} * {margin:0; padding:0; text-indent:0; }"
					+ " .s1 { color: #2E5396; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 16pt; }"
					+ " .s2 { color: #2E3599; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
					+ " p { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 10pt; margin:0pt; }"
					+ " .a { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
					+ " .s3 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
					+ " .s5 { color: #2E3599; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
					+ " .s6 { color: #120000; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
					+ " .s7 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
					+ " .s8 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; vertical-align: -2pt; }"
					+ " .s9 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
					+ " h1 { color: #120000; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
					+ " .s10 { color: #2D3499; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
					+ " .pt10 { padding-top: 10pt; }" + " .pb10 { padding-bottom: 10pt; }"
					+ " .pb50 { padding-bottom: 50pt; }"
					+ " table, tbody, td {vertical-align: top; overflow: visible; color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 10pt; margin:0pt; } "
					+ " @media print { .pagebreak {page-break-after: always;} } "
					+ " table thead tr td { padding-top:5px; padding-bottom:5px;border-bottom: 1px dashed #000000;}"
					// Uncomment below line to add watermark to the pdf
					// + " .watermark {\r\n background-image: url(\"data:image/png;base64," +
					// licWatermark + "\");} "
					+ " " + reportStyles + " " + " </style></head><body><div id=\"bg\" class=\"watermark\">"
					+ "<p style=\"padding-left: 5pt;text-indent: 0pt;text-align: left;\"/>";

			if (showLogo) {
				completehtmlContent = completehtmlContent
						+ "<p style=\"text-indent: 0pt;text-align: center;\"><span><table style=\"width:100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr style=\"display:flex;justify-content:space-between;align-items:center\"><td style=\"padding-left:24pt\"><img width=\"100\" height=\"63\" src=\"data:image/jpg;base64,"
						+ licLogo + "\"/></td>" + "</tr></table></span></p>";
			}
			completehtmlContent = completehtmlContent + "<p style=\"text-indent: 0pt;text-align: left;\"><br/></p>"
					+ reportBody + "";

			String htmlFileLoc = tempPdfLocation + "/" + payoutNumber + ".html";

			FileWriter fw = new FileWriter(htmlFileLoc);
			fw.write(completehtmlContent);
			fw.close();

			FileOutputStream fileOutputStream = new FileOutputStream(tempPdfLocation + "/" + payoutNumber + ".pdf");
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocument(new File(htmlFileLoc));
			renderer.layout();
			try {
				renderer.createPDF(fileOutputStream, false);
			} catch (DocumentException e) { 
				e.printStackTrace();
			}
			renderer.finishPDF();

			if (new File(htmlFileLoc).exists()) {
				new File(htmlFileLoc).delete();
			}

			return tempPdfLocation + "/" + payoutNumber + ".pdf";
		}
	
	private String generateclaimForwardingLetter(PdfGeneratorDto pdfGeneratorDto,CommonMasterUnitEntity pdfClaimForwardLetter,String payoutNumber) {

		int pageno = 1;

		String reportBody = this.topPdfs(pdfGeneratorDto,pdfClaimForwardLetter,pageno);

		String table = 
				"<table class=\"tableborder\"  style=\"width:100%;\">"
			+"<thead>"
			+ "<tr>" 
			+ "<td class=\"tdrightalign\">LIC ID</td>" 
			+ "<td class=\"tdrightalign \">Emp Name</td>"
			+ "<td class=\"tdrightalign \">EMP NO </td>" 
			+ "<td style=\"text-align:right;\">LCSA</td>"
			+ "<td style=\"text-align:right;\">SV/Mat/Wthd</td>" 
			+ "<td style=\"text-align:right;\">REFUND</td>"
			+ "<td style=\"text-align:right;\">OTH AMT</td>" 
			+ "<td style=\"text-align:right;\">TOTAL</td>"
			+ "</tr>"
			+"</thead>";
//		+ "<hr style=\"border: 1px dashed black;\"></hr>";

			
			
//			+"<hr style=\"border: 1px dashed black;\"></hr>";
		
//		table += "<table class=\"tableborder\"  style=\"width:100%;\">";

		String detailTable = "";
		Double total = 0.0;
		Double LCSA = 0.0;
		Double SVMatWthd = 0.0;
		Double REFUND = 0.0;
		Double OTHAMT = 0.0;
		Double lcPremiumTotal = 0.0;

		for (PdfGeneratorForTableDto get : pdfGeneratorDto.getPdfGeneratorForTable()) {

		Double total1 = (get.getLcSumAssured()!= null ? get.getLcSumAssured():0.0)+(get.getSvMatWthd()!= null ?get.getSvMatWthd():0.0)+(get.getRefund()!= null ?get.getRefund():0.0)+(get.getOthAmt()!= null ? get.getOthAmt():0.0);
					
			detailTable += "<tr>"
					+ "<td style=\"text-align:left;\">" + get.getLicId() + "</td>"
					+ "<td style=\"text-align:left;\">" + get.getEmpName() + "</td>"
					+ "<td style=\"text-align:left;\">" + get.getEmpCode() + "</td>"
					+ "<td style=\"text-align:right;\">" + (get.getLcSumAssured()!= null ? get.getLcSumAssured():0.0)+ "</td>"
					+ "<td style=\"text-align:right;\">" +(get.getSvMatWthd()!= null ?get.getSvMatWthd():0.0)+ "</td>"
					+ "<td style=\"text-align:right;\">" +BigDecimal.valueOf(get.getRefund()).setScale(2, RoundingMode.UP).doubleValue()+"</td>"
					+ "<td style=\"text-align:right;\">" +( get.getOthAmt()!= null ? get.getOthAmt():0 )+ "</td>"
					+ "<td style=\"text-align:right;\">" + BigDecimal.valueOf(total1).setScale(2, RoundingMode.UP).doubleValue() + "</td>" 
					+ "</tr>";
					
//			total = total + get.getTotal();	
			
		detailTable += "<tr >"
				+ "   <td >TOTAL: </td>"
				+ "   <td ></td>"
				+ "   <td ></td>"
				+ "    <td style=\"text-align:right;\">" + get.getLcSumAssured() + " </td>"
				+ "     <td style=\"text-align:right;\">" + get.getSvMatWthd() + "</td>"
				+ "      <td style=\"text-align:right;\">"+ BigDecimal.valueOf(get.getRefund()).setScale(2, RoundingMode.UP).doubleValue()+ "</td>"
				+ "       <td style=\"text-align:right;\">"+( get.getOthAmt()!= null ? get.getOthAmt():0 )+"</td>"
				+ "     <td style=\"text-align:right;\">"+BigDecimal.valueOf(total1).setScale(2, RoundingMode.UP).doubleValue()+"</td>"
				+ "</tr>" 
				+ "</table>";
		}
		detailTable += "<tfoot>" 
					+ "<tr></tr>" 
					+ "</tfoot>" 
//					+ "</table>"
					+"<hr style=\"border: 1px dashed black;\"></hr>"	
					+"<p> &nbsp; </p>"
					+"<p> &nbsp; </p>"
					+"<p style=\"text-align:right;\">Yours Faithfully</p>"		
					+"<p> &nbsp; </p>"
					+"<p> &nbsp; </p>"
					+"<p> &nbsp; </p>"
					+"<p style=\"text-align:right;\"> Manager (PNGS)</p>";


		return reportBody+table+detailTable;
	}

	private String topPdfs(PdfGeneratorDto pdfGeneratorDto,CommonMasterUnitEntity pdfClaimForwardLetter,int pageno) {
		String page_break = "";
		String getPayoutDate=DateUtils.dateToStringDDMMYYYY(pdfGeneratorDto.getPayoutDate());

		if (pageno > 1)
			page_break = "<p class=\"pagebreak\"></p>";

		return page_break +"<p style=\"text-align:center;\">LIFE INSURANCE CORPORATION OF INDIA</p>"
		+ "<p style=\"text-align:center;\">P&mp;GS UNIT, "+pdfClaimForwardLetter.getAddress1()+",</p>"
		+ "<p style=\"text-align:center;\">"+pdfClaimForwardLetter.getAddress2()+"</p>"
		+ "<p style=\"text-align:center;\">"+pdfClaimForwardLetter.getAddress3()+"</p>"
		+ "<p style=\"text-align:center;\">"+pdfClaimForwardLetter.getCityName()+"</p>"
		+ "<p style=\"text-align:center;\"> "+pdfClaimForwardLetter.getStateName()+" - "+pdfClaimForwardLetter.getPincode()+"</p>" + " <br/>"

//		+ "           <div class=\"text\">" + "                    <div class=\"row\">\r\n"
//		+ "           <div class=\"col-md-2\" style=\"text-align:left;\"> Ref :PNGS/U558057/511173/14081                    Date:16/03/2023 </div>"
				
 		+ " <table style=\"width: 115%;\">\r\n"
 		+ " <tr><td>Ref :PNGS/"+pdfClaimForwardLetter.getDescription()+"/"+pdfGeneratorDto.getPolicyNumber()+"/"+pdfGeneratorDto.getPayoutNumber()+"</td>\r\n"
 		+ " <td class=\"alignRight\">Date :"+getPayoutDate+"</td></tr>\r\n"
 		+ " </table>"		
        +" <hr style=\"border: 1px dashed black;\"></hr> "

       
        +"<p>&nbsp;</p>"
		+ "    <div class=\"row\">\r\n" 
		+ "                    <div class=\"col-md-12\">\r\n"
		+ "                        <p class=\"header\"  style=\"text-align:left;\">"+pdfGeneratorDto.getMphName()+"</p><br />"
		+ "                        <p style=\"text-align:left;\">"+pdfGeneratorDto.getMphAddress1()+" </p>"
		+ "                        <p style=\"text-align:left;\">"+pdfGeneratorDto.getMphAddress2()+"</p>\r\n"
		+ "                        <p style=\"text-align:left;\">"+pdfGeneratorDto.getMphAddress3()+"</p>"
		+ "                        <p style=\"text-align:left;\">"+pdfGeneratorDto.getCity()+"</p>"
		+ "                        <p style=\"text-align:left;\">"+pdfGeneratorDto.getPincode()+"</p>"

		+ "                        <br />" 
		+ "                        <p style=\"text-align:left;\">507 101</p>"
		+ "                    </div>" 
		+ "                </div>  "

		+ "                <br />"

		+ "<p style=\"text-align:left;\">Dear Sir / Madam</p>"

		+ "                <br />"

		+ "<p style=\"text-align:center;\">Reg :      Claim under Master policy no "+pdfGeneratorDto.getPolicyNumber()+" .</p>"

		+ "    <div class=\"row\">\r\n" + "                  "
		+ "                        <div class=\"col-md-12\">\r\n"
		+ "                        <p style=\"text-align:left;\">We are forwarding cheque no _________/crediting to your Bank</p>"
		+ "                        <p style=\"text-align:left;\">Acc No. "+pdfGeneratorDto.getAccNo()+" of "+pdfGeneratorDto.getBankName()+" </p>"
		+ "                        <p  style=\"text-align:left;\">IFSC Code : "+pdfGeneratorDto.getIfcsCode()+"</p>\r\n"
		+ "                    </div>" 
		+ "                </div> "

		+ "                <br />"

		+ "<p style=\"text-align:left;\">The amounts are as per particulars listed below</p>"
		+"<p> &nbsp; </p>"
		+" <hr style=\"border: 1px dashed black;\"></hr> ";
	}

	@Override
	public String generateReport(String username, String payoutNumber) throws IOException {

//		reportType = "payoutVoucherWithdrawal";
	InputStream islicLogo = new ClassPathResource("liclogo.png").getInputStream();
	// Uncomment below line to add watermark to the pdf
	// InputStream islicWatermark = new
	// ClassPathResource("watermark.png").getInputStream();

	byte[] bytesLogo = IOUtils.toByteArray(islicLogo); 
	// Uncomment below line to add watermark to the pdf
	// byte[] bytesWatermark = IOUtils.toByteArray(islicWatermark);

	String licLogo = Base64.getEncoder().encodeToString(bytesLogo);
	// Uncomment below line to add watermark to the pdf
	// String licWatermark = Base64.getEncoder().encodeToString(bytesWatermark);

	ClaimPayoutDto claimPayoutDto = new ClaimPayoutDto();
	List<Object[]> claimreport = tempPolicyClaimPropsRepository.claimpayoutreport(payoutNumber);

	claimPayoutDto = PolicyClaimHelper.valuationObjtoDto(claimreport);
	
	CommonMasterUnitEntity pdfClaimForwardLetter = new CommonMasterUnitEntity();
	
	CommonMasterUnitEntity pdfClaimForward = null;
	if (isDevEnvironment) {
		pdfClaimForward = new CommonMasterUnitEntity();
	    pdfClaimForward.setDescription("Maker");
	    pdfClaimForward.setAddress1("LIC-ADDRESS 1");
	    pdfClaimForward.setAddress2("LIC-ADDRESS 2");
	    pdfClaimForward.setAddress3("LIC-ADDRESS 3");
	    pdfClaimForward.setCityName("KUMBAKONAM");
	    pdfClaimForward.setStateName("TAMILNADU");
	    pdfClaimForward.setPincode("600182");	  
	    } else {
		pdfClaimForward = commonMasterUnitRepository.findByUnitCode(claimPayoutDto.getUnitCode());
	}

	pdfClaimForwardLetter = PolicyClaimHelper.claimforward(pdfClaimForward);
	


	List<Object[]> claimPayoutItem = tempPolicyClaimPropsRepository.claimvoucher(claimPayoutDto.getClaimPropsId());

	claimPayoutDto.setClaimPayoutItemDto(PolicyClaimHelper.claimvalObjtoDto(claimPayoutItem));
	
	String productCode = commonModuleService.getProductCode(claimPayoutDto.getProductId());
	
	boolean showLogo = true;
	String reportBody = "";
	String reportStyles = "";
	String htmlContent2 = "</div></body></html>"; // DO NOT DISTRUB THIS LINE

		reportBody = GeneratepaymentvoucherReport(payoutNumber,productCode,claimPayoutDto, pdfClaimForwardLetter) + htmlContent2;

		reportStyles = quotationValuationReportStyles();
		showLogo = false;
//		} else if (reportType.equals("policyBond")) {
//			String getPolicyDetail=masterPolicyRepository.getBondDetail(quotationIdorPolicy);
//
//			String[] get = getPolicyDetail.toString().split(",");
				
				JsonNode mphBasic = null;
				JsonNode mphAdds = null;
				JsonNode mphRep =null; 
				//
//				//
//				PolicyLifeCoverEntity  policyLifeCoverEntity =policyLifeCoverRepository.findByPolicyId1(quotationIdorPolicy);
//				PolicyBondDetailDto policyBondDetailDto =PolicyHelper.getPolicyBondDtoDetail(get,mphBasic,mphAdds,mphRep,policyLifeCoverEntity,policyLifeCoverRepository.maxRetirement(quotationIdorPolicy));
//				
//			reportBody = QuotationHelper.policyBondReport(policyBondDetailDto) + htmlContent2;
//			showLogo = false;
//		}
//		else if (reportType.equals("payoutVoucherWithdrawal")) {
//			reportBody = payoutVoucherWithdrawal(claimId) + htmlContent2;
//			reportStyles = payoutVoucherWithdrawalStyles();
//			showLogo = false;
//		}

	String completehtmlContent = "<!DOCTYPE  html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
			+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><title>LIC ePGS</title><meta name=\"author\" content=\"LIC PNGS\"/><meta name=\"keywords\" content=\"Customer Communication\"/><meta name=\"description\" content=\"Report/Letter/Contract\"/><style type=\"text/css\"> body{border-width:0px;\r\n"
			+ "border-style:solid;\r\n" + "border-color:#586ec5;} * {margin:0; padding:0; text-indent:0; }"
			+ " .s1 { color: #2E5396; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 16pt; }"
			+ " .s2 { color: #2E3599; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
			+ " p { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 10pt; margin:0pt; }"
			+ " .a { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
			+ " .s3 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
			+ " .s5 { color: #2E3599; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
			+ " .s6 { color: #120000; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
			+ " .s7 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
			+ " .s8 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; vertical-align: -2pt; }"
			+ " .s9 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
			+ " h1 { color: #120000; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
			+ " .s10 { color: #2D3499; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
			+ " .pt10 { padding-top: 10pt; }" + " .pb10 { padding-bottom: 10pt; }"
			+ " .pb50 { padding-bottom: 50pt; }"
			+ " table, tbody, td {vertical-align: top; overflow: visible; color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 10pt; margin:0pt; } "
			+ " table thead tr td { padding-top:5px; padding-bottom:5px;border-bottom: 1px dashed #000000;border-top: 1px dashed #000000;}"
			// Uncomment below line to add watermark to the pdf
			// + " .watermark {\r\n background-image: url(\"data:image/png;base64," +
			// licWatermark + "\");} "
			+ " " + reportStyles + " " + " </style></head><body><div id=\"bg\" class=\"watermark\">"
			+ "<p style=\"padding-left: 5pt;text-indent: 0pt;text-align: left;\"/>";

	if (showLogo) {
		completehtmlContent = completehtmlContent
				+ "<p style=\"text-indent: 0pt;text-align: center;\"><span><table style=\"width:100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr style=\"display:flex;justify-content:space-between;align-items:center\"><td style=\"padding-left:24pt\"><img width=\"100\" height=\"63\" src=\"data:image/jpg;base64,"
				+ licLogo + "\"/></td>" + "</tr></table></span></p>";
	}
	completehtmlContent = completehtmlContent + "<p style=\"text-indent: 0pt;text-align: left;\"><br/></p>"
			+ reportBody + "";

	String htmlFileLoc = tempPdfLocation + "/" + payoutNumber + ".html";

	FileWriter fw = new FileWriter(htmlFileLoc);
	fw.write(completehtmlContent);
	fw.close();

	FileOutputStream fileOutputStream = new FileOutputStream(tempPdfLocation + "/" + payoutNumber + ".pdf");
	ITextRenderer renderer = new ITextRenderer();
	renderer.setDocument(new File(htmlFileLoc));
	renderer.layout();
	try {
		renderer.createPDF(fileOutputStream, false);
	} catch (DocumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	renderer.finishPDF();

	if (new File(htmlFileLoc).exists()) {
		new File(htmlFileLoc).delete();
	}

	return tempPdfLocation + "/" + payoutNumber + ".pdf";
}
	
	private String GeneratepaymentvoucherReport(String payoutNumber,String productCode,ClaimPayoutDto claimPayoutDto,CommonMasterUnitEntity pdfClaimForwardLetter) {
		int pageno = 1;

		String reportBody = this.toppdf(claimPayoutDto,productCode,payoutNumber,pdfClaimForwardLetter,pageno);
		
		String detailTable = "";
		Double debitTotal = 0.0;
		Double CreditTotal = 0.0;
		
		for (ClaimPayoutItemDto get : claimPayoutDto.getClaimPayoutItemDto()) {	
			debitTotal=debitTotal+get.getDebitAmount();
			CreditTotal=CreditTotal+get.getCreditAmount();
			}	
		DecimalFormat fdamount = new DecimalFormat("#0.00");
		String debitAMOUNT = fdamount.format(debitTotal);
		String creditAMOUNT = fdamount.format(CreditTotal);
		
		detailTable = "<table class=\"tableborder\" callpadding=\"0\" cellspacing=\"0\" style=\"width:100%;\">"
				+"<thead>"
				+ "<tr>" 
				+ "<td>Head of Account</td>" 
				+ "<td>Code</td>" 
				+ "<td class=\"tdrightalign\">Debit Amount</td>"
				+ "<td class=\"tdrightalign\">Credit Amount</td></tr>" 
				+"</thead>"
				;
		
		for (ClaimPayoutItemDto get : claimPayoutDto.getClaimPayoutItemDto()) {
			if(get.getDebitCodeDescribtion()!=null && get.getDebitCodeDescribtion() !="") {
			detailTable += 
					"<tr>"
					+ "<td>"+(get.getDebitcode()!= null ? get.getDebitcode():"")+"</td>"
					+ "<td>"+(get.getDebitCodeDescribtion()!= null ? get.getDebitCodeDescribtion(): get.getCreditCodeDescribtion())+"</td>" 
				    + "<td class=\"tdrightalign\">"+(get.getDebitAmount()!= null ? get.getDebitAmount():"")+"</td>" 
				    	+	"<td class=\"tdrightalign\">"+(get.getCreditAmount()!= null ? get.getCreditAmount():"")+"</td>" 
					+ "</tr>" 
					;
			}else {
				detailTable += 
						"<tr>"
						+ "<td>"+(get.getCreditcode()!= null ? get.getCreditcode():"")+"</td>"
						+ "<td>"+(get.getCreditCodeDescribtion()!= null ? get.getCreditCodeDescribtion():"")+"</td>" 
						+ "<td></td>" 
						+ "<td class=\"tdrightalign\">"+(get.getCreditAmount()!= null ? get.getCreditAmount():"")+"</td>" 
						+ "</tr>" 
						;
			}
			
//			debitTotal = debitTotal + BigDecimal.valueOf(get.getDebitAmount()).setScale(2, RoundingMode.UP).doubleValue();
//							CreditTotal = CreditTotal + get.getCreditAmount();

		
//		for (ClaimPayoutItemDto get : claimPayoutDto.getClaimPayoutItemDto()) {
//
//		if(get.getDebitAmount()!=null) {
//		detailTable+=		
//				 "<tr>" + "<td style=\"width:25%;\">Total</td>" 
//				+ "<td style=\"width:25%;\"></td>"
//				+ "<td style=\"width:25%;\">"+debitTotal+"</td></tr>";
//				}else {
//					detailTable+=		
//					"<tr>" + "<td style=\"width:25%;\">Total</td>" 
//							+ "<td style=\"width:25%;\"></td>"
//							+ "<td style=\"width:25%;\"></td>" 
//							+ "<td style=\"width:25%;\">"+CreditTotal+"</td></tr>";
//				}
//		}
 
//	    detailTable+=
//		"<tr>"
//		+ "<td style=\"width=20%\">Total</td>"
//		+ "<td style=\"width=20%\"></td>"
//		+ "<td class=\"tdrightalign\">"+(get.getDebitAmount()!= null ? get.getDebitAmount():"")+"</td>" 
//		+ "<td class=\"tdrightalign\">"+(get.getCreditAmount()!= null ? get.getCreditAmount():"")+"</td>" 
//		+ "</tr>" ;
//		+ " <hr style=\"border: 1px solid black;\"></hr> "

		}
		  detailTable+=
					"<tr>"
					+ "<td style=\"width=50%;\">Total</td>"
					+ "<td style=\"width=50%;\"></td>"
					+ "<td style=\"text-align:right;border-top:1px solid;\">"+(debitAMOUNT!=null?debitAMOUNT:0.0)+"</td>" 
					+ "<td style=\"text-align:right;border-top:1px solid;\">"+(creditAMOUNT!= null ?creditAMOUNT:0.0)+"</td>" 
					+ "</tr>";
		detailTable+= "</table>";

		String payoutDate=DateUtils.dateToStringDDMMYYYY(claimPayoutDto.getPayoutDate());
		
		String reportbody1= "<p>&nbsp;</p>" + "<p>As per the following details</p>" + "<p>&nbsp;</p>"

				+ "<table class=\"tableborder\" callpadding=\"0\" cellspacing=\"0\" style=\"width:100%;min-height:20pt;\">"
				+ "<p>&nbsp;</p>" + "<p>&nbsp;</p>" + "<p>Voucher No :"+payoutNumber+" </p>" + "<p>Voucher Date :"+payoutDate+" </p>"
				+ "<p>Prepared By :"+pdfClaimForwardLetter.getDescription()+" </p>" + "<p>&nbsp;</p>" + "<p>&nbsp;</p>" + "</table>";

		String footerTable = "<p class=\"pb10\">&nbsp;</p><table style=\"width:100%;\"><tr><td style=\"width:50%\">"

				+ "<p class=\"signaturepadding\">Prepared By:"+pdfClaimForwardLetter.getDescription()+" </p>"

				+ "<p>&nbsp;</p>"

				+ "<p class=\"signaturepadding\">Checked By: </p>"

				+ "<p>&nbsp;</p>"

				+ "<p class=\"pb10\">Date: </p>"

				+ "<p>&nbsp;</p>"

				+ "<p class=\"pb10\">Cheque No: </p>" + "<p>Drawn on: </p>"

				+ "</td><td><table style=\"width:100%;\"><tr><td>"

				+ "<table class=\"tableborder\" callpadding=\"0\" cellspacing=\"0\" style=\"width:100%;min-height:100pt;\">"

				+ "<tr><th>"

				+ " <p class=\"pb10\" style=\"text-align:center;\">PAY</p>"

				+ "<p class=\"pb10\">Sign:</p>"

				+ "<p class=\"pb10\">Design:</p>"

				+ "<p>Date:</p>"

				+ "</th>"

				+ "<th>"

				+ "<p class=\"pb10\" style=\"text-align:center;\">PAID</p>"

				+ "<p class=\"signaturepadding\">Sign:</p>"

				+ "<p>&nbsp;</p>"

				+ "<p>Date:</p>" + "</th></tr>" + "<tr><th colspan=\"2\">"

				+ "<p>&nbsp;</p>"

				+ "<p class=\"pt10\" style=\"text-align:center;\">Initials of Officers Signing cheques</p>"

				+ "</th></tr>" + "</table></td></tr></table>" + "</td></tr>" + "</table>";

		return reportBody + detailTable + reportbody1 + footerTable;
	}

	private String toppdf(ClaimPayoutDto claimPayoutDto,String productCode,String payoutNumber,CommonMasterUnitEntity data,int pageno) {
		String get1=productCode.replace('"',' ');
		String amountInWords = "";
		Double amount = 0.0;
		for (ClaimPayoutItemDto get : claimPayoutDto.getClaimPayoutItemDto()) {
			amount = (get.getDebitAmount()!=null ? get.getDebitAmount() : 0.0 );
		}
		amountInWords = commonUtils.convertAmountToWords(amount.intValue());
		String payoutDate=DateUtils.dateToStringDDMMYYYY(claimPayoutDto.getPayoutDate());
		String page_break = "";
		if (pageno > 1)
			page_break = "<p class=\"pagebreak\"></p>";

		return page_break + "<p class=\"pb10\" style=\"text-align:center;\"><u>PAYMENT / ADJUSTMENT VOUCHER</u></p>"

				+ "<table style=\"width:100%;\">"
				+ "<tr><td style=\"width:70%;\">P &amp; GS Dept : "+data.getDescription()+"</td><td style=\"width:30%;\">Voucher No. "+payoutNumber+"</td></tr>"
				+ "<tr><td>Divl. Name: </td><td>Date:"+payoutDate+"</td></tr>" + "</table>"

				+ "<p>&nbsp;</p>"

				+ "<p class=\"pb10\" style=\"text-align:center;\">To Cash / Banking Section</p>"
				+ "<p> please Adjust /Issue Crossed / Order Cheque / Cash / M.O / Postage stamps favouring </p>"
				+ "<p> "+claimPayoutDto.getMphName()+" </p>"
				+ "<p>&nbsp;</p>" + "<p> For "+amountInWords+" </p>" + "<p>&nbsp;</p>"

				+ "<table style=\"width:100%;\">" + "<tr><td style=\"text-align:left;\">Policy No :"+get1+"/"
				+ claimPayoutDto.getPolicyNumber() + "</td><td style=\"text-align:right;\">Scheme Name:"+get1+"</td></tr>"
				+ "</table>";
	}

	private String quotationValuationReportStyles() {
		return "" + " .tableborder," + " .tableborder tr th,"
				+ " .tableborder tfoot tr td { border-width:1pt; border-style:double; border-right: #FFFFFF; border-left: #FFFFFF; padding:5pt; border-spacing:0pt; border-collapse: collapse; }"
				+ " .tableborder tr td { padding:5pt; }" + " .tdrightalign { text-align:right; } "
				+ " .showborder { border-width:1pt; border-style:double; padding:5pt; border-spacing:0pt; border-collapse: collapse; }";

	}

	@Override
	public ApiResponseDto<CalculateResDto> GetFundSize(Long policyid) {
		CalculationReqDto calculationReqDto = new CalculationReqDto();
		calculationReqDto.setPolicyId(policyid);
		calculationReqDto.setTransactionDate(new Date());
		calculationReqDto.setBatch(false);
		calculationReqDto.setAuto(true);
		calculationReqDto.setRecalculate(true);

		return ApiResponseDto.success(fundService.calculate(calculationReqDto));
	}

	/*
	 * @Value("${temp.pdf.location}") private String tempPdfLocation;
	 */

	public String claimCalculationSheetGenerateReport(String onbotdnumber) throws IOException {

//				reportType = "payoutVoucherWithdrawal";
		InputStream islicLogo = new ClassPathResource("liclogo.png").getInputStream();

		// Uncomment below line to add watermark to the pdf
		// InputStream islicWatermark = new
		// ClassPathResource("watermark.png").getInputStream();

		byte[] bytesLogo = IOUtils.toByteArray(islicLogo);

		// Uncomment below line to add watermark to the pdf
		// byte[] bytesWatermark = IOUtils.toByteArray(islicWatermark);

		String licLogo = Base64.getEncoder().encodeToString(bytesLogo);

		// Uncomment below line to add watermark to the pdf
		// String licWatermark = Base64.getEncoder().encodeToString(bytesWatermark);

		boolean showLogo = true;
		String reportBody = "";
		String reportStyles = "";
		String htmlContent2 = "</div></body></html>"; // DO NOT DISTRUB THIS LINE

		reportBody = claimCalculation(onbotdnumber) + htmlContent2;

		 /* 
		 * 
		 * // PolicyLifeCoverEntity policyLifeCoverEntity = policyLifeCoverRepository
		 * .findByPolicyId1(quotationIdorPolicy); PolicyBondDetailDto
		 * policyBondDetailDto = PolicyHelper.getPolicyBondDtoDetail(get, mphBasic,
		 * mphAdds, mphRep, policyLifeCoverEntity,
		 * policyLifeCoverRepository.maxRetirement(quotationIdorPolicy));
		 * 
		 * reportBody = QuotationHelper.policyBondReport(policyBondDetailDto) +
		 * htmlContent2; showLogo = false; }
		 * 
		 * 
		 */

//				else if (reportType.equals("payoutVoucherWithdrawal")) {
//					reportBody = payoutVoucherWithdrawal(claimId) + htmlContent2;
//					reportStyles = payoutVoucherWithdrawalStyles();
//					showLogo = false;
//				}

		String completehtmlContent = "<!DOCTYPE  html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><title>LIC ePGS</title><meta name=\"author\" content=\"LIC PNGS\"/><meta name=\"keywords\" content=\"Customer Communication\"/><meta name=\"description\" content=\"Report/Letter/Contract\"/><style type=\"text/css\"> body{border-width:0px;\r\n"
				+ "border-style:solid;\r\n" + "border-color:#586ec5;} * {margin:0; padding:0; text-indent:0; }"
				+ " .s1 { color: #2E5396; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 16pt; }"
				+ " .s2 { color: #2E3599; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " p { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 10pt; margin:0pt; }"
				+ " .a { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " .s3 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " .s5 { color: #2E3599; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ " .s6 { color: #120000; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ " .s7 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " .s8 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; vertical-align: -2pt; }"
				+ " .s9 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " h1 { color: #120000; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ " .s10 { color: #2D3499; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ " .pt10 { padding-top: 10pt; }" + " .pb10 { padding-bottom: 10pt; }"
				+ " .pb50 { padding-bottom: 50pt; }"
				+ " table, tbody, td {vertical-align: top; overflow: visible; color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 10pt; margin:0pt; } "
				// Uncomment below line to add watermark to the pdf
				// + " .watermark {\r\n background-image: url(\"data:image/png;base64," +
				// licWatermark + "\");} "
				+ " " + reportStyles + " " + " </style></head><body><div id=\"bg\" class=\"watermark\">"
				+ "<p style=\"padding-left: 5pt;text-indent: 0pt;text-align: left;\"/>";

		/*
		 * if (showLogo) { completehtmlContent = completehtmlContent +
		 * "<p style=\"text-indent: 0pt;text-align: center;\"><span><table style=\"width:100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr style=\"display:flex;justify-content:space-between;align-items:center\"><td style=\"padding-left:24pt\"><img width=\"100\" height=\"63\" src=\"data:image/jpg;base64,"
		 * + licLogo + "\"/></td>" + "</tr></table></span></p>"; }
		 */
		completehtmlContent = completehtmlContent + "<p style=\"text-indent: 0pt;text-align: left;\"><br/></p>"
				+ reportBody + "";

		String htmlFileLoc = tempPdfLocation + "/" + onbotdnumber + ".html";

		FileWriter fw = new FileWriter(htmlFileLoc);
		fw.write(completehtmlContent);
		fw.close();

		FileOutputStream fileOutputStream = new FileOutputStream(tempPdfLocation + "/" + onbotdnumber + ".pdf");
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(new File(htmlFileLoc));
		renderer.layout();
		try {
			renderer.createPDF(fileOutputStream, false);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		renderer.finishPDF();

		if (new File(htmlFileLoc).exists()) {
			new File(htmlFileLoc).delete();
		}

		return tempPdfLocation + "/" + onbotdnumber + ".pdf";
	}

	private String policyBondReport(Long quotationId) {

		String reportBody = "<p>Life Insurance Corporation of India Pension and Group Schemes Department</p>";

		return reportBody.replace("\\", "").replaceAll("\t", "");
	}

	private String claimCalculation(String onboardNumber) {
		ClaimCalculationSheetDto claimCalculationSheetDto = new ClaimCalculationSheetDto();

		List<Object> findClainCalculation = tempPolicyClaimPropsRepository.findClainCalculation(onboardNumber);

		TempPolicyClaimPropsEntity tempPolicyClaimPropsEntity = tempPolicyClaimPropsRepository
				.findByOnboardNumberisActive(onboardNumber);
		claimCalculationSheetDto.setRefundPremiumAmt(tempPolicyClaimPropsEntity.getRefundPremiumAmount());
		claimCalculationSheetDto.setModifiedGratuityAmount(tempPolicyClaimPropsEntity.getModifiedGratuityAmount());
		if (claimCalculationSheetDto.getModifiedGratuityAmount() == null) {
			claimCalculationSheetDto
					.setModifiedGratuityAmount(tempPolicyClaimPropsEntity.getGratuityAmtOnDateExit() == null ? 0.0
							: tempPolicyClaimPropsEntity.getGratuityAmtOnDateExit());
		}

		
		RenewalPolicyTMPEntity renewalPolicyTMPEntity = renewalPolicyTMPRepository
				.findById(tempPolicyClaimPropsEntity.getTmpPolicyId()).get();
		RenewalPolicyTMPMemberEntity getRenewalPolicyTMPMemberEntity=renewalPolicyTMPMemberRepository.findById(tempPolicyClaimPropsEntity.getTmpMemberId()).get();	
		
		RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity
		         =renewalGratuityBenefitPropsTMPRepository.findgratuitysalary(tempPolicyClaimPropsEntity.getTmpMemberId(),tempPolicyClaimPropsEntity.getTmpPolicyId());
		renewalsGratuityBenefitPropsTMPEntity.getGratuityCellingAmount();
		System.out.println(renewalsGratuityBenefitPropsTMPEntity.getGratuityCellingAmount());
		
		for (Object object : findClainCalculation) {

			Object[] ob = (Object[]) object;

			claimCalculationSheetDto.setMphName((String.valueOf(ob[0])));
			
			claimCalculationSheetDto.setIntimationNumber((String.valueOf(ob[1])));
			
			claimCalculationSheetDto.setIntimationDate((Date) (ob[2]));
			
			claimCalculationSheetDto.setPolicyNo(String.valueOf(ob[3]));

			claimCalculationSheetDto.setAnnualRenewalDate((Date) ob[4]);

			claimCalculationSheetDto.setDateOfBirth((Date) ob[5]);

			claimCalculationSheetDto.setDateOfAppointment((Date) (ob[6]));
			
			claimCalculationSheetDto.setDateOfexit((Date) (ob[7]));
			
			claimCalculationSheetDto.setModeOfExit((String.valueOf(ob[9])));
			
			claimCalculationSheetDto.setCaseOfDeath(String.valueOf(ob[10]));
			
			claimCalculationSheetDto.setReasonForDeath(String.valueOf(ob[11]));
			
			claimCalculationSheetDto.setPlaceOfDeath((String.valueOf(ob[12])));

			claimCalculationSheetDto.setEmployeeCode((String.valueOf(ob[13])));

			claimCalculationSheetDto.setLicId((String.valueOf(ob[14])));

			claimCalculationSheetDto.setMemberName((String.valueOf(ob[15])));

			claimCalculationSheetDto
			.setSalaryAsOnDateOFExit(NumericUtils.convertStringToDouble(String.valueOf(ob[16])));
			
			claimCalculationSheetDto.setSalaryAsOnArd(NumericUtils.convertStringToDouble(String.valueOf(ob[17])));

			claimCalculationSheetDto.setLcsaPayable(NumericUtils.convertStringToDouble(String.valueOf(ob[18])));
			
			claimCalculationSheetDto.setRefundPremiumAmt(NumericUtils.convertStringToDouble(String.valueOf(ob[19])));
			
			claimCalculationSheetDto.setRefundGstAmount(NumericUtils.convertStringToDouble(String.valueOf(ob[20])));
			
			claimCalculationSheetDto.setCategoryName((String.valueOf(ob[21])));
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getRenewalPolicyTMPMemberEntity.getDateOfBirth());
		calendar.add(Calendar.YEAR,renewalsGratuityBenefitPropsTMPEntity.getRetirementAge());
		Date newRetirementDate = calendar.getTime();
		
		LocalDateTime getRetirement=DateUtils.dateToLocalDateTime(newRetirementDate);
		LocalDateTime getDateofAppointee=DateUtils.dateToLocalDateTime(getRenewalPolicyTMPMemberEntity.getDateOfAppointment());
			String[] getRetirementSpt=getRetirement.toString().split("T");	
			String[] getDateOfAppointeeSpt=getDateofAppointee.toString().split("T");	
			System.out.println(getRetirementSpt[0]);	
			System.out.println(getDateOfAppointeeSpt[0]);	
			LocalDate startDate = LocalDate.parse(getDateOfAppointeeSpt[0]);
		LocalDate endDate = LocalDate.parse(getRetirementSpt[0]);
		Period period = Period.between(startDate, endDate);
		
		GratuityCalculationsDto gratitutyCalculationDto = new GratuityCalculationsDto();
		gratitutyCalculationDto.setDateOfExist(tempPolicyClaimPropsEntity.getDateOfExit());
		gratitutyCalculationDto.setTmppolicyid(tempPolicyClaimPropsEntity.getTmpPolicyId());
//		gratitutyCalculationDto.setSalary(claimCalculationSheetDto.getSalaryAsOnArd());
//		gratitutyCalculationDto.setPastservice(period.getYears());
		
		ApiResponseDto<GratuityCalculationsDto> ARD = calculategratuity(gratitutyCalculationDto);

		GratuityCalculationsDto gratitutyCalculationDOA = new GratuityCalculationsDto();
		gratitutyCalculationDOA.setDateOfExist(tempPolicyClaimPropsEntity.getDateOfExit());
		gratitutyCalculationDOA.setTmppolicyid(tempPolicyClaimPropsEntity.getTmpPolicyId());
//		gratitutyCalculationDOA.setPastservice(period.getYears());
		gratitutyCalculationDOA.setSalary(tempPolicyClaimPropsEntity.getSalaryAsOnDateOfExit());
		
		ApiResponseDto<GratuityCalculationsDto> DOA = calculategratuity(gratitutyCalculationDOA);
		
		
		GratuityCalculationsDto totalGratitutyCalculationDto = new GratuityCalculationsDto();
		totalGratitutyCalculationDto.setDateOfExist(newRetirementDate);
		totalGratitutyCalculationDto.setTmppolicyid(tempPolicyClaimPropsEntity.getTmpPolicyId());
		totalGratitutyCalculationDto.setSalary(claimCalculationSheetDto.getSalaryAsOnArd());
		totalGratitutyCalculationDto.setTotalService(period.getYears());
		
		ApiResponseDto<GratuityCalculationsDto> totalARD = calculategratuityforTotal(totalGratitutyCalculationDto);
		if(totalARD.getData().getGratuityamount()>renewalsGratuityBenefitPropsTMPEntity.getGratuityCellingAmount()) {
			totalARD.getData().setTotalservicegtyARD(renewalsGratuityBenefitPropsTMPEntity.getGratuityCellingAmount());
		}else {
			totalARD.getData().setTotalservicegtyARD(totalARD.getData().getGratuityamount());
		}
		GratuityCalculationsDto totalGratitutyCalculationDOA = new GratuityCalculationsDto();
		totalGratitutyCalculationDOA.setDateOfExist(tempPolicyClaimPropsEntity.getDateOfExit());
		totalGratitutyCalculationDOA.setTmppolicyid(tempPolicyClaimPropsEntity.getTmpPolicyId());
		totalGratitutyCalculationDOA.setTotalService(period.getYears());
		totalGratitutyCalculationDOA.setSalary(tempPolicyClaimPropsEntity.getSalaryAsOnDateOfExit());
		
		ApiResponseDto<GratuityCalculationsDto> totalDOA = calculategratuityforTotal(totalGratitutyCalculationDOA);
		if(totalDOA.getData().getGratuityamount()>renewalsGratuityBenefitPropsTMPEntity.getGratuityCellingAmount()) {
			totalDOA.getData().setTotalservicegtryDOE(renewalsGratuityBenefitPropsTMPEntity.getGratuityCellingAmount());
		}else {
			totalDOA.getData().setTotalservicegtryDOE(totalDOA.getData().getGratuityamount());
		}
		
		Set<String> combination = new LinkedHashSet<>();
		Set<Double> percentage = new LinkedHashSet<>();
		if (renewalPolicyTMPEntity.getPayto() == 199) {
			Optional<TempMPHEntity> tempMPHEntity = tempMPHRepository
					.findById(renewalPolicyTMPEntity.getMasterpolicyHolder());
			if (tempMPHEntity.isPresent()) {
				claimCalculationSheetDto.setMphName(tempMPHEntity.get().getMphName());
				claimCalculationSheetDto.setPercentage(100l);
			}
		}

		else if (renewalPolicyTMPEntity.getPayto() == 200 && tempPolicyClaimPropsEntity.getModeOfExit() == 193) {
			//		if (tempPolicyClaimPropsEntity.getModeOfExit() == 193) {
				RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = renewalPolicyTMPMemberRepository
						.findBytmpPolicyId1(tempPolicyClaimPropsEntity.getTmpPolicyId());
				List<RenewalPolicyTMPMemberNomineeDto> getRenewalPolicyTMPMemberNomineeDto = new ArrayList<RenewalPolicyTMPMemberNomineeDto>();
				for (RenewalPolicyTMPMemberNomineeEntity renewalPolicyTMPMemberNomineeEntity : renewalPolicyTMPMemberEntity
						.getNominees()) {
					RenewalPolicyTMPMemberNomineeDto newRenewalPolicyTmpMember = new ModelMapper()
							.map(renewalPolicyTMPMemberNomineeEntity, RenewalPolicyTMPMemberNomineeDto.class);
					getRenewalPolicyTMPMemberNomineeDto.add(newRenewalPolicyTmpMember);

				}
				claimCalculationSheetDto.setGetNomineeDetail(getRenewalPolicyTMPMemberNomineeDto);
		//	}

		} else {
			RenewalPolicyTMPMemberEntity renewalPolicyTMPMemberEntity = renewalPolicyTMPMemberRepository
					.findBytmpPolicyId1(tempPolicyClaimPropsEntity.getTmpPolicyId());

			String lastName = (renewalPolicyTMPMemberEntity.getLastName() != null) ? renewalPolicyTMPMemberEntity.getLastName() : " ";
			claimCalculationSheetDto.setPercentage(100l);
			claimCalculationSheetDto.setMphName(
					renewalPolicyTMPMemberEntity.getFirstName() + " " + lastName);
		}

		Double totalPayable = claimCalculationSheetDto.getLcsaPayable() + claimCalculationSheetDto.getRefundPremiumAmt()
				+ claimCalculationSheetDto.getModifiedGratuityAmount() + claimCalculationSheetDto.getRefundGstAmount() ;

		
		
		String proposalNo= stagingPolicyRepository.getProposaNumber(claimCalculationSheetDto.getPolicyNo());
		
		Long policyId = masterPolicyRepository.getPolicyIdClaim(claimCalculationSheetDto.getPolicyNo()); 

		ApiResponseDto<CalculateResDto> calculateResDto = this.GetFundSize(policyId);
		
		calculateResDto.getData().getAvailableBalance();
		
		DecimalFormat df = new DecimalFormat("#0.00"); 
		String getAvailableBalance = df.format(calculateResDto.getData().getAvailableBalance());
		String getSalaryAsOnArd = df.format(claimCalculationSheetDto.getSalaryAsOnArd());
		String getSalaryAsOnDateOFExit = df.format(claimCalculationSheetDto.getSalaryAsOnDateOFExit());
		String getDoaGratuityamount = df.format(DOA.getData().getGratuityamount());		
		String getLcsaPayable = df.format(claimCalculationSheetDto.getLcsaPayable());				
		String getModifiedGratuityAmount = df.format(claimCalculationSheetDto.getModifiedGratuityAmount());			
		String getRefundPremiumAmt = df.format(claimCalculationSheetDto.getRefundPremiumAmt());				
		String getRefundGstAmount = df.format(claimCalculationSheetDto.getRefundGstAmount());		
			 	
		String getGratuityamountARD = df.format( totalARD.getData().getGratuityamount());		
		String getGratuityamountDOA = df.format( totalDOA.getData().getGratuityamount());		
		String getGratuityamount = df.format( ARD.getData().getGratuityamount());		

		JsonNode mphBasic = null;
		
		JsonNode actualobj = commonModuleService.getProposalNumber(proposalNo);
		mphBasic =	actualobj.path("mphDetails").path("mphProductDetails");

		if (mphBasic != null) {
			claimCalculationSheetDto.setProductCode(mphBasic.path("productCode").textValue());
			}
		
		String reportBody = "<p style=\"text-align:center;\">E N D O R S E M E N T</p>"

				+ "<p style=\"text-align:center;\">*********************</p>"

				+ "<p style=\"text-align:left;\">Annual Renewal Date : 05/04/2023</p>" + "<p class=\"pb10\">&nbsp;</p>"
				+ "<p style=\"text-align:left;\" >  IT IS HEREBY DECLARED THAT, as on the above renewal date the total benefits assured under Master Policy in respect  of all eligible members including new entrants"
				+ "and  exculuding withdrawals by retirements, death and leaving service ,as detailed in the Schedule of the Costs and benifits issued to the Grantees , are  as follows :-</p>"
				+ "<p class=\"pb10\">&nbsp;</p>";

		String reportBody2 = "<table class=\"tableborder\" callpadding=\"0\" cellspacing=\"0\" style=\"width:100%;min-height:100pt;\">"

				+ "<tr>" + "<th >No.of Members </th>" + "<th class=\"tdrightalign \">Sum Assured Rs.</th>"
				+ "<th class=\"tdrightalign \">Annulity Per Annum Rs. Amount</th>"
				+ "<th class=\"tdrightalign \">Amount Of  Annual Rs.</th>"
				+ "<th class=\"tdrightalign \">Premium Adjusted Single Rs.</th>" + "</tr>";

		String detailTable = "";

		for (int i = 1; i <= 2; i++) {

			detailTable += "<tr>" + "<td >1</td>" +

					"<td class=\"tdrightalign\">30000</td>"

					+ "<td class=\"tdrightalign\">5000</td>"

					+ "<td class=\"tdrightalign\">4000.</td>"

					+ "<td class=\"tdrightalign\">60000</td>" + "</tr>";
		}

		// to adjust min height
		for (int i = 1; i <= 5; i++) {
			detailTable += "<tr>" + "<td ></td>" + "<td class=\"tdrightalign\"></td>"
					+ "<td class=\"tdrightalign\"></td>" + "<td class=\"tdrightalign\"></td>"
					+ "<td class=\"tdrightalign\"></td>" + "</tr>";
		}
		detailTable += "<tfoot>" + "<tr>" + "</tr>" + "</tfoot>" + "</table>";

		String caseOfDeath = (claimCalculationSheetDto.getCaseOfDeath() != "null") ? claimCalculationSheetDto.getCaseOfDeath() : " ";
		String placeOfDeath = (claimCalculationSheetDto.getPlaceOfDeath() != "null") ? claimCalculationSheetDto.getPlaceOfDeath() : " ";
        Double refundGst = (claimCalculationSheetDto.getRefundGstAmount() != null) ? claimCalculationSheetDto.getRefundGstAmount() : 0.0;

		String reportBody8 = "<div class=\"col-md-12\">"
				+ "<p  style=\"text-align:center;\">LIFE INSURANCE CORPORATION OF INDIA</p>"

				+ " <p style=\"text-align:center;\">P &amp; GS Department</p>"

				+ "</div>"

				+ "<p style=\"text-align:center;\">  Claim Calculation Sheet</p>"

				+ "<hr style=\"border: 1px dashed black;\"></hr>"

				+ " <table style=\"width:100%\">"

				+ "<tr>\r\n" + "<td>Customer Name</td>\r\n" + "<td>:</td>\r\n" 
				+ "<td>"+ claimCalculationSheetDto.getMphName() + " </td>\r\n" + "</tr>\r\n"
				
				+ "<tr>\r\n"+ "<td>Intimation No /Date  </td>\r\n" + "<td>:</td>\r\n" 
				+ "<td> "+ claimCalculationSheetDto.getIntimationNumber() + "/   "
				+ DateUtils.dateToStringDDMMYYYY(claimCalculationSheetDto.getIntimationDate()) + "</td>\r\n"+ "</tr>\r\n"
				
				+ "<tr>\r\n" + "<td>Date of Calculation </td>\r\n" + "<td>:</td>\r\n" + "<td>"
				+ DateUtils.dateToStringDDMMYYYY(new Date()) + "</td>" + "</tr>\r\n"

				+ "</table>" + "<hr style=\"border: 1px dashed black;\"></hr>" 

				+ "<p style=\"text-align:left;\">  Policy &amp; Employee Details </p>" 

				+ "<hr style=\"border: 1px dashed black;\"></hr>"

				+ " <table style=\"width:100%\">\r\n"

				+ "<tr>" + "<td>Policy No :</td>" + "<td>:</td>\r\n" + "<td>" + claimCalculationSheetDto.getPolicyNo()
				+ "</td>" + "<td>Date of birth  </td>" + "<td>:</td>"+"<td>"
				+ DateUtils.dateToStringDDMMYYYY(claimCalculationSheetDto.getDateOfBirth()) + "</td>" + "</tr>"
//set values scheme
				+ "<tr>" + "<td>Scheme </td>" + "<td>: </td>\r\n" + "<td>" +  claimCalculationSheetDto.getProductCode()  + "</td>" + "<td>Date of appt  </td>"
				+ "<td>:</td>" + "<td>"
				+ DateUtils.dateToStringDDMMYYYY(claimCalculationSheetDto.getDateOfAppointment()) + "</td>" + "</tr>"
				
				+ "<tr>" + "<td>ARD</td>" + "<td>:</td>\r\n" + "<td>"
				+ DateUtils.dateToStringDDMMYYYY(renewalPolicyTMPEntity.getPolicyStartDate()) + "</td>"
				+ "<td>Date of exit\r\n" + "</td>" + "<td>:</td>" + "<td>"
				+ DateUtils.dateToStringDDMMYYYY(claimCalculationSheetDto.getDateOfexit()) + "</td>" + "</tr>"

				+ "<tr>" + "<td>Mode</td>" + "<td>:</td>\r\n" + "<td>Y</td>" + "<td>Mode of exit\r\n" + "</td>"
				+ "<td>:</td>" + "<td>" + claimCalculationSheetDto.getModeOfExit() + "</td>" + "</tr>" 
				+ "<tr>"+ "<td>Lst prm pd </td>" + "<td>:</td>\r\n" + "<td>" + DateUtils.dateToStringDDMMYYYY(renewalPolicyTMPEntity.getLastPremiumPaid())
				+ " \r\n" + "</td>" + "<td>Cause of Death\r\n" + "</td>" + "<td>:</td>" + "<td>"
				+ caseOfDeath + "</td>" + "</tr>"

				+ "<tr>" + "<td>Fund size \r\n" + "</td>" + "<td>:</td>\r\n" + "<td>" +getAvailableBalance+ "\r\n" + "</td>"
				+ "<td>Place of Death/Acc </td>" + "<td>:</td>" + "<td> " + placeOfDeath+ "</td>" + "</tr>" 
				
				+ "<tr>" + "<td>(excluding interest)\r\n" + "</td>" + "<td></td>\r\n" + "<td>" + ""
				+ "</td>" + "<td>Date of Accident</td>" + "<td>:</td>" + "<td> " + " " + " </td>" + "</tr>"
//>Date of Accident set Date,status,Lst prm pd
				+ "<tr>" + "<td>Employee Name</td>" + "<td>:</td>\r\n" + "<td>"
				+ claimCalculationSheetDto.getMemberName() + " </td>" + "<td>Status</td>" + "<td>:</td>" + "<td>"
				+ "paid" + "</td>" + "</tr>" 
				
				+ "<tr>" + "<td>Employee No </td>" + "<td>:</td>\r\n" + "<td>"
				+ claimCalculationSheetDto.getEmployeeCode() + "</td>"

				+ "<td></td>" + "<td></td>" + "<td></td>" + "</tr>"

				+ "<tr>" + "<td>Lic id\r\n" + "</td>" + "<td>:</td>\r\n" + "<td>" + claimCalculationSheetDto.getLicId()
				+ "</td>" + "<td></td>" + "<td></td>" + "<td></td>" + "</tr>" + "</table>"

				+ "<hr style=\"border: 1px dashed black;\"></hr>"

				+ "<p style=\"text-align:left;\">  Calculations :\r\n" + "<br/>" + " </p>"

				 + "<hr style=\"border: 1px dashed black;\"></hr>"

				+ "<table style=\"width:100%\">\r\n"

				+ "<tr>" + "<td style=\"width:30%\">Category</td>" + "<td style=\"text-align:right;width:10%;\">"
				+claimCalculationSheetDto.getCategoryName()+"</td>"+ "<td style=\"width:60%\"></td>"+"</tr>" 
				
				+ "<tr>"+ "<td style=\"width:30%\">Salary as on last ard </td>" + "<td style=\"text-align:right;width:10%;\">"
				+ getSalaryAsOnArd + "</td>" + "<td style=\"width:60%\"></td>"+"</tr>" 
				
				+ "<tr>"+ "<td style=\"width:30%\">Salary as on (DOE) </td>" + "<td style=\"text-align:right;width:10%;\">"
				+ getSalaryAsOnDateOFExit + "</td>" + "<td style=\"width:60%\"></td>"+"</tr>" 
				
				+ "<tr>"+ "<td style=\"width:30%\">Past service (ARD)</td>" + "<td style=\"text-align:right;width:10%;\">" 
				+ ARD.getData().getPastservice()+ "</td>" + "<td style=\"width:60%\"></td>"+"</tr>" 
				
				+ "<tr>" + "<td style=\"width:30%\">Past service (DOE)</td>" 
				+ "<td style=\"text-align:right;width:10%;\">" + DOA.getData().getPastservice() + "</td>" + "<td style=\"width:60%\"></td>"+"</tr>" 
				
				+ "<tr>"+ "<td style=\"width:30%\">Total Sevice</td>" + "<td style=\"text-align:right;width:10%;\">" 
				+ period.getYears() + "</td>" + "<td style=\"width:60%\"></td>"+"</tr>" 
				
				+ "<tr>"+ "<td style=\"width:30%\"> Past service Gty (ARD Salary)" + "</td>" + "<td style=\"text-align:right;width:10%;\">"
				+ getGratuityamount + "</td>" + "<td style=\"width:60%\"></td>"+"</tr>" 
				
				+ "<tr>"+ "<td style=\"width:30%\">Total Service Gty (ARD Salary) </td>" + "<td style=\"text-align:right;width:10%;\">"
				+ getGratuityamountARD + "</td>" + "<td style=\"width:60%\"></td>"+"</tr>"
//set values all
				+ "<tr>" + "<td style=\"width:30%\">Total Service Gty (DOE Salary)</td>" + "<td style=\"text-align:right;width:10%;\">"
				+ getGratuityamountDOA + "</td>" + "<td style=\"width:60%\"></td>"+"</tr>"
				
				+ "<tr>"+ "<td style=\"width:30%;\">User Entered Gratuity Amount</td>" + "<td style=\"text-align:right;width:10%;\">" +  getModifiedGratuityAmount 
				+ "</td>" +"<td style=\"width:60%\"></td>"+ "</tr>"



				+"<tr>"
				+ "<td> <hr style=\"border: 1px dashed black;\">" + "</hr> </td> "
				+ "<td> <hr style=\"border: 1px dashed black;\">" + "</hr> </td>"
				+ "<td> <hr style=\"border: 1px dashed black;\">" + "</hr> </td>"
				+ "<td> <hr style=\"border: 1px dashed black;\">" + "</hr> </td>"
				+ "</tr>"

				
				+ "<tr>" + "<td style=\"width:30%;\">LCSA payable</td>" + "<td style=\"text-align:right;width:10%;\">" 
				+ getLcsaPayable + "</td>" +"<td style=\"width:60%\"></td>"+ "</tr>" 
				
				+ "<tr>"+ "<td style=\"width:30%;\">Additional LCSA </td>" + "<td style=\"text-align:right;width:10%;\">"
				+ "0.00" + "</td>" +"<td style=\"width:60%\"></td>"+ "</tr>" 
				
				+ "<tr>"+ "<td style=\"width:30%;\"> Accident Benefit </td>" + "<td style=\"text-align:right;width:10%;\">"
				+ "0.00" + "</td>" +"<td style=\"width:60%\"></td>"+ "</tr>" 
				
				+ "<tr>"+ "<td style=\"width:30%;\">NON RISK amt</td>" + "<td style=\"text-align:right;width:10%;\">" +  getModifiedGratuityAmount 
				+ "</td>" +"<td style=\"width:60%\"></td>"+ "</tr>" 
				
				+ "<tr>" + "<td style=\"width:30%;\">Refund of prem</td>" 
				+  "<td style=\"text-align:right;width:10%;\">" + getRefundPremiumAmt + "</td>" +"<td style=\"width:60%\"></td>"+ "</tr>" 
				
				+ "<tr>"+ "<td style=\"width:30%;\"> Refund of GST on prem" + "</td>" + "<td style=\"text-align:right;width:10%;\">"
				+ getRefundGstAmount + "</td>" +"<td style=\"width:60%\"></td>"+ "</tr>" 
				
				+ "<tr>"+ "<td style=\"width:30%;\">TOTAL payable </td>" + "<td style=\"text-align:right;width:10%;\">"
				+ df.format(totalPayable) + "</td>" +"<td style=\"width:60%\"></td>"+ "</tr>"
				
				+ "<tr>" + "<td></td>" + "</tr>" + "</table>"

				+ "<hr style=\"border: 1px dashed black;\"></hr>" 

//                + " <table style=\"width:100%\">\r\n"
//                +  "<tr>" 
//                + "<td>" +"Beneficiary Details"+ "</td>"
//                +"</tr>" 
//                +"<tr>"
//                + "<td style=\"width:30%;\">Beneficiary Name</td>"  
//                + "<td style=\"text-align:right;width:10%;\">" 
//                + "Percent" + "</td>"+"<td style=\"width:60%\"></td>"+ "</tr>"
                + " <table style=\"width:100%\">"
                +"</table>"

		
//		if (claimCalculationSheetDto.getGetNomineeDetail() != null) {
//		for (int i = 0; i < 3; i++) {
//			for (RenewalPolicyTMPMemberNomineeDto getEachDto : claimCalculationSheetDto.getGetNomineeDetail()) {
//				reportBody8 += "<tr>" + "<td>" + getEachDto.getNomineeName() + " " + "</td>"
//						+ "<td style=\"text-align:right;\">"+ getEachDto.getPercentage().intValue() + " " + "</td></tr>";
//			}
//		}
//		} else {
//
//			reportBody8 += "<tr><td>" +  claimCalculationSheetDto.getMphName() + "</td><td style=\"text-align:right;\">" + claimCalculationSheetDto.getPercentage() + ""
//					 + "</td></tr>";
//		}
//		reportBody8 += "</table>"

//				+ " <hr style=\"border: 1px dashed black;\"></hr> "

				+ "<br/>"
				+ "<p style=\"text-align:left;\">  ** Cause of Death -- " + caseOfDeath +" </p>"
				+ "<br/>"
				+ "<p style=\"text-align:left;\">  We may pay gratuity/S.V. Claim/Maturity Claim/Death Claim of Rs." + df.format(totalPayable) +  "</p>"

				+ "<br/>" + "<br/>" + "<br/>"

				+ "<p style=\"text-align:left;\">  Prepared by&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;  Checked by &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;   Passed by &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;</p>";

		// return reportBody.replace("\\", "").replaceAll("\t", "") + reportBody2 +
		// detailTable + reportBody3 +reportBody4;

		return reportBody8;

	}
	
	private ApiResponseDto<GratuityCalculationsDto> calculategratuityforTotal(
			GratuityCalculationsDto totalGratitutyCalculationDto) {


		RenewalPolicyTMPMemberEntity getRenewalPolicyTMPMemberEntity = renewalPolicyTMPMemberRepository
				.findBytmpPolicyId1(totalGratitutyCalculationDto.getTmppolicyid());
		
		Integer totalService=0;
	
		totalService = totalGratitutyCalculationDto.getTotalService();		
		

		double gratuityAmount = 0.0;
		Double calcgvalue = 0.0;

		List<RenewalGratuityBenefitTMPEntity> getRenewalGratuityBenefitTMPEntities = renewalGratuityBenefitTMPRepository
				.findBytmpPolicyIdAndCategoryId(totalGratitutyCalculationDto.getTmppolicyid(),
						getRenewalPolicyTMPMemberEntity.getCategoryId());
		for (RenewalGratuityBenefitTMPEntity renewalGratuityBenefitTMPEntity : getRenewalGratuityBenefitTMPEntities) {
			renewalGratuityBenefitTMPEntity.getGratutiyBenefitTypeId();
			if (renewalGratuityBenefitTMPEntity.getGratutiyBenefitTypeId() == 25
					|| renewalGratuityBenefitTMPEntity.getGratutiyBenefitTypeId() == 26) {
				for (RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity : renewalGratuityBenefitTMPEntity
						.getRenewalgratuityPropsBenefit()) {

					gratuityAmount = gratuityAmount + ((renewalsGratuityBenefitPropsTMPEntity.getNumberOfDaysWage()
							.doubleValue()
							/ renewalsGratuityBenefitPropsTMPEntity.getNumberOfWorkingDaysPerMonth().doubleValue())
							* totalGratitutyCalculationDto.getSalary()) * totalService.doubleValue();

				}
			}
			if (renewalGratuityBenefitTMPEntity.getGratutiyBenefitTypeId() == 27
					&& renewalGratuityBenefitTMPEntity.getGratutiySubBenefitId() == 185) {
				for (RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity : renewalGratuityBenefitTMPEntity
						.getRenewalgratuityPropsBenefit()) {

					if (renewalsGratuityBenefitPropsTMPEntity.getNumberOfYearsOfService() >= totalService
							|| renewalsGratuityBenefitPropsTMPEntity.getNumberOfYearsOfService() <= totalService) {

						calcgvalue = calcgvalue + ((renewalsGratuityBenefitPropsTMPEntity.getNumberOfDaysWage()
								.doubleValue()
								/ renewalsGratuityBenefitPropsTMPEntity.getNumberOfWorkingDaysPerMonth().doubleValue())
								* totalGratitutyCalculationDto.getSalary())
								* renewalsGratuityBenefitPropsTMPEntity.getNumberOfYearsOfService().doubleValue();
						gratuityAmount = gratuityAmount + calcgvalue;
						break;
					}
				}
			}
			if (renewalGratuityBenefitTMPEntity.getGratutiyBenefitTypeId() == 27
					&& renewalGratuityBenefitTMPEntity.getGratutiySubBenefitId() == 186) {
				for (RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity : renewalGratuityBenefitTMPEntity
						.getRenewalgratuityPropsBenefit()) {

					if (renewalsGratuityBenefitPropsTMPEntity.getNumberOfYearsOfService() <= totalService) {

						calcgvalue = calcgvalue
								+ ((renewalsGratuityBenefitPropsTMPEntity.getNumberOfDaysWage().doubleValue()
										/ renewalsGratuityBenefitPropsTMPEntity.getNumberOfWorkingDaysPerMonth()
												.doubleValue()
										* totalGratitutyCalculationDto.getSalary())
										* renewalsGratuityBenefitPropsTMPEntity.getNumberOfYearsOfService()
												.doubleValue());
						gratuityAmount = gratuityAmount + calcgvalue;
					}
				}
			}
		}
		GratuityCalculationsDto gratutityCalculationDto = new GratuityCalculationsDto();
		double roundValGratuityAmt = Math.round(gratuityAmount);
		gratutityCalculationDto.setGratuityamount(roundValGratuityAmt);
		gratutityCalculationDto.setTotalService(totalService);
		return ApiResponseDto.created(gratutityCalculationDto);
	
	}

	@Override
	public ApiResponseDto<GratuityCalculationsDto> calculategratuity(GratuityCalculationsDto gratitutyCalculationDto) {

		RenewalPolicyTMPMemberEntity getRenewalPolicyTMPMemberEntity = renewalPolicyTMPMemberRepository
				.findBytmpPolicyId1(gratitutyCalculationDto.getTmppolicyid());
		if (gratitutyCalculationDto.getSalary() == null) {
			gratitutyCalculationDto.setSalary(getRenewalPolicyTMPMemberEntity.getBasicSalary());
		}
		Integer pastservice = 0;
		Date dateOfAppointmentdiff = null;
		String dateOfExitdiff = null;
	
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date dateOfAppointment = getRenewalPolicyTMPMemberEntity.getDateOfAppointment();
		Date str_DateofExit = gratitutyCalculationDto.getDateOfExist();

//		String str_DateofExit = gratitutyCalculationDto.getDateOfExist().toString();
//		DateTimeFormatter f = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss").withLocale(Locale.US);
//		ZonedDateTime zdt = ZonedDateTime.parse(str_DateofExit, f);
		String[] get = dateOfAppointment.toString().split(" ");
		System.out.println(get[0]);
		LocalDate startDate = LocalDate.parse(get[0]);

//		LocalDate ld = zdt.toLocalDate();
		try {
//			dateOfAppointmentdiff = sdf.parse(dateOfAppointment.toString());
			dateOfExitdiff = sdf.format(str_DateofExit);
//			dateOfExitdiff = ld;

		} catch (Exception e) {

		}

//		String[] getstr_DateofExit=str_DateofExit.toString().split(" ");
//		System.out.println(getstr_DateofExit[0]);

		LocalDate endDate = LocalDate.parse(dateOfExitdiff);
		Period period = Period.between(startDate, endDate);
//		String str_DateofAppoint = dateOfAppointmentdiff.toString();
//		ZonedDateTime doa = ZonedDateTime.parse(str_DateofAppoint, f);
//
//		LocalDate ldDateofappoint = doa.toLocalDate();

//		String dateofExit = dateOfExitdiff.toString();
//		ZonedDateTime doe = ZonedDateTime.parse(dateofExit, f);
//
//		LocalDate ldDateofExit = doe.toLocalDate();

		// Calculate the difference between the two dates
//		Period period = Period.between(startDate, endDate);

		// Output the result
		System.out.printf("The difference between %s and %s is %d years, %d months, and %d days.",
				dateOfAppointmentdiff, dateOfExitdiff, period.getYears(), period.getMonths(), period.getDays());
		
	
			pastservice = period.getYears();
		
			
		double gratuityAmount = 0.0;
		Double calcgvalue = 0.0;

		List<RenewalGratuityBenefitTMPEntity> getRenewalGratuityBenefitTMPEntities = renewalGratuityBenefitTMPRepository
				.findBytmpPolicyIdAndCategoryId(gratitutyCalculationDto.getTmppolicyid(),
						getRenewalPolicyTMPMemberEntity.getCategoryId());
		for (RenewalGratuityBenefitTMPEntity renewalGratuityBenefitTMPEntity : getRenewalGratuityBenefitTMPEntities) {
			renewalGratuityBenefitTMPEntity.getGratutiyBenefitTypeId();
			if (renewalGratuityBenefitTMPEntity.getGratutiyBenefitTypeId() == 25
					|| renewalGratuityBenefitTMPEntity.getGratutiyBenefitTypeId() == 26) {
				for (RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity : renewalGratuityBenefitTMPEntity
						.getRenewalgratuityPropsBenefit()) {

					gratuityAmount = gratuityAmount + ((renewalsGratuityBenefitPropsTMPEntity.getNumberOfDaysWage()
							.doubleValue()
							/ renewalsGratuityBenefitPropsTMPEntity.getNumberOfWorkingDaysPerMonth().doubleValue())
							* gratitutyCalculationDto.getSalary()) * pastservice.doubleValue();

				}
			}
			if (renewalGratuityBenefitTMPEntity.getGratutiyBenefitTypeId() == 27
					&& renewalGratuityBenefitTMPEntity.getGratutiySubBenefitId() == 185) {
				for (RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity : renewalGratuityBenefitTMPEntity
						.getRenewalgratuityPropsBenefit()) {

					if (renewalsGratuityBenefitPropsTMPEntity.getNumberOfYearsOfService() >= pastservice
							|| renewalsGratuityBenefitPropsTMPEntity.getNumberOfYearsOfService() <= pastservice) {

						calcgvalue = calcgvalue + ((renewalsGratuityBenefitPropsTMPEntity.getNumberOfDaysWage()
								.doubleValue()
								/ renewalsGratuityBenefitPropsTMPEntity.getNumberOfWorkingDaysPerMonth().doubleValue())
								* gratitutyCalculationDto.getSalary())
								* renewalsGratuityBenefitPropsTMPEntity.getNumberOfYearsOfService().doubleValue();
						gratuityAmount = gratuityAmount + calcgvalue;
						break;
					}
				}
			}
			if (renewalGratuityBenefitTMPEntity.getGratutiyBenefitTypeId() == 27
					&& renewalGratuityBenefitTMPEntity.getGratutiySubBenefitId() == 186) {
				for (RenewalsGratuityBenefitPropsTMPEntity renewalsGratuityBenefitPropsTMPEntity : renewalGratuityBenefitTMPEntity
						.getRenewalgratuityPropsBenefit()) {

					if (renewalsGratuityBenefitPropsTMPEntity.getNumberOfYearsOfService() <= pastservice) {

						calcgvalue = calcgvalue
								+ ((renewalsGratuityBenefitPropsTMPEntity.getNumberOfDaysWage().doubleValue()
										/ renewalsGratuityBenefitPropsTMPEntity.getNumberOfWorkingDaysPerMonth()
												.doubleValue()
										* gratitutyCalculationDto.getSalary())
										* renewalsGratuityBenefitPropsTMPEntity.getNumberOfYearsOfService()
												.doubleValue());
						gratuityAmount = gratuityAmount + calcgvalue;
					}
				}
			}
		}
		GratuityCalculationsDto gratutityCalculationDto = new GratuityCalculationsDto();
		double roundValGratuityAmt = Math.round(gratuityAmount);
		gratutityCalculationDto.setGratuityamount(roundValGratuityAmt);
		gratutityCalculationDto.setPastservice(pastservice);
		return ApiResponseDto.created(gratutityCalculationDto);
	}
	
}
