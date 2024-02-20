package com.lic.epgs.gratuity.policy.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.fasterxml.jackson.databind.JsonNode;
import com.lic.epgs.gratuity.accountingservice.dto.HSNCodeDto;
import com.lic.epgs.gratuity.accountingservice.service.AccountingService;
import com.lic.epgs.gratuity.common.entity.CommonMasterUnitEntity;
import com.lic.epgs.gratuity.common.repository.CommonMasterStateRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.service.CommonModuleService;
import com.lic.epgs.gratuity.common.utils.CommonUtils;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.policy.claim.dto.ClaimPayoutDto;
import com.lic.epgs.gratuity.policy.claim.dto.ClaimPayoutItemDto;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimHelper;
import com.lic.epgs.gratuity.policy.dto.AgeValuationDto;
import com.lic.epgs.gratuity.policy.dto.AgeValuationReportDto;
import com.lic.epgs.gratuity.policy.dto.GenerateCBSheetPdfDto;
import com.lic.epgs.gratuity.policy.dto.PremiumReceiptDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyContributionDetails;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.helper.PolicyHelper;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyContributionDetailRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.PolicyRepository;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;
import com.lic.epgs.gratuity.policy.service.PolicyPdfService;
import com.lic.epgs.gratuity.quotation.member.helper.BulkMemberUploadHelper;
import com.lowagie.text.DocumentException;


@Service
public class PolicyPdfServiceImpl implements PolicyPdfService {
	
	@Autowired
	private PolicyRepository policyRepository;
	
	@Autowired
	private MPHRepository mPHRepository;
	
	@Autowired
	private AccountingService accountingService;
	
	@Autowired
	private MasterPolicyContributionDetailRepository masterPolicyContributionDetailRepository;
	
	@Autowired
	private BulkMemberUploadHelper memberHelper;
	
	@Autowired
	private CommonUtils commonUtils;
	
	@Autowired
	private CommonModuleService commonModuleService;
	
	@Value("${app.commonModuleServiceEndpoint}")
	private String endPoint;
		
	@Value("${temp.pdf.location}")
	private String tempPdfLocation;
	
	@Autowired
	private CommonMasterUnitRepository commonMasterUnitRepository;
	
	@Autowired
	private CommonMasterStateRepository commonMasterStateRepository;
	
	@Autowired
	private MasterPolicyRepository masterPolicyRepository;
	
	@Autowired
	private MasterPolicyCustomRepository masterPolicyCustomRepository;
	
	public String generateReport(Long policyId, String reportType) throws IOException {
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
		
		
		AgeValuationDto ageValuationDto = new AgeValuationDto();
		List<Object[]> getAgeReport = policyRepository.getAgeReport(policyId);
 
	
//		List<GenerateCBSheetPdfDto> generateCBSheetPdfDto = new ArrayList<GenerateCBSheetPdfDto>();
//		List<Object[]> getcbsheet = policyRepository.getAgeReport(policyId);
		
		ageValuationDto = PolicyHelper.valuationObjtoDto(getAgeReport);
		MasterPolicyEntity masterPolicyEntitys =masterPolicyCustomRepository.findById(policyId);
	
		ageValuationDto.setAnnualRenewlDate(masterPolicyEntitys.getAnnualRenewlDate());
		ageValuationDto.setPolicyNumber(masterPolicyEntitys.getPolicyNumber());
		ageValuationDto.setCustomerCode(masterPolicyEntitys.getCustomerCode());	
		
		MPHEntity MPHentity = mPHRepository.findById(policyId).get();
		ageValuationDto.setMphName(MPHentity.getMphName());
		
		boolean showLogo = true;
		String reportBody = "";
		String reportStyles = "";
		String htmlContent2 = "</div></body></html>"; // DO NOT DISTRUB THIS LINE
		if (reportType.equals("agevaluationreport")) {
			reportBody = policyAgeValuationReport(ageValuationDto) + htmlContent2;
			showLogo = false;
			}
//		else if (reportType.equals("generatecbsheet")) {
//				reportBody = Generatecbsheet(policyId, generateCBSheetPdfDto) + htmlContent2;
//				showLogo = false;
		 else if (reportType.equals("policyBond")) {
//			reportBody = policyBondReport(quotationId) + htmlContent2;
//			showLogo = false;
		}
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
				+ " @media print { .pagebreak {page-break-after: always;} } "
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

		String htmlFileLoc = tempPdfLocation + "/" + policyId + ".html";
		FileWriter fw = new FileWriter(htmlFileLoc);
		fw.write(completehtmlContent);
		fw.close();

		FileOutputStream fileOutputStream = new FileOutputStream(tempPdfLocation + "/" + policyId + ".pdf");
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

		return tempPdfLocation + "/" + policyId + ".pdf";
	}


	private String policyAgeValuationReport(AgeValuationDto ageValuationDto) {
		
//		List<AgeValuationDto> ageValuationDto1=this.getDummyData();
		
		int pageno = 1;
		String reportBody = this.topPdf(ageValuationDto,pageno);
		pageno++;
		
		String table = 
			     
				"<table class=\"tableborder\"  style=\"width:100%;\">" 
				+"<thead>"
				+ "<tr style=\"text-align:right\" >"
				+ "<td style=\"font-weight: bold\">Age</td>"
				+ "<td style=\"font-weight: bold\">No</td>"
				+ "<td style=\"font-weight: bold\">Past Ser</td>"
				+ "<td style=\"font-weight: bold\">Salary</td>"
				+ "<td style=\"font-weight: bold\">Acr gty</td>"
				+ "<td style=\"font-weight: bold\">Ts gty</td>"
				+ "<td style=\"font-weight: bold\">Life Cover</td>"
				+ "<td style=\"font-weight: bold\">Lc Premium</td>" +"</tr>"
				+"</thead>"
				+"<tr>"
				+ "<td> <hr style=\"border: 1px dashed black;\"></hr> </td>"
				+ "<td> <hr style=\"border: 1px dashed black;\"></hr> </td>"
				+ "<td> <hr style=\"border: 1px dashed black;\"></hr> </td>"
				+ "<td> <hr style=\"border: 1px dashed black;\"></hr> </td>"
				+ "<td> <hr style=\"border: 1px dashed black;\"></hr> </td>"
				+ "<td> <hr style=\"border: 1px dashed black;\"></hr> </td>"
				+ "<td> <hr style=\"border: 1px dashed black;\"></hr> </td>"
				+ "<td> <hr style=\"border: 1px dashed black;\"></hr> </td>"
				+"</tr>";

		String detailTable = "";
		Double catageoryTotal = 0.0;
		Double total = 0.0;
		Long salaryTotal = 0l;
		Long acrGtyTotal = 0l;
        Long taGtyTotal = 0l;
		Long lifeCoverTotal = 0l;
		Double lcPremiumTotal = 0.0;

		int i = 1;

		for (AgeValuationReportDto get : ageValuationDto.getAgeValuationReportDto()) {
			DecimalFormat df = new DecimalFormat("#0.00"); 
			String getSalary = df.format(get.getSalary());	
			String getAccruedGratuity = df.format(get.getAccruedGratuity());	
			String getTotalServiceGratuity = df.format(get.getTotalServiceGratuity());	
			String getLifeCover = df.format(get.getLifeCover());	
			String getLifeCoverPremium = df.format(get.getLifeCoverPremium());	

			detailTable += "<tr style=\"text-align:right\">"
					 + "<td>" + get.getAge() + "</td>"
						+ "<td>" + get.getCount() + "</td>" + "<td>"
						+ "<td></td>"
						+ get.getPastService() + "</td>" + "<td>" + getSalary + "</td>"
						+ "<td>" + getAccruedGratuity + "</td>" + "<td>"
						+ getTotalServiceGratuity + "</td>" + "<td>" + getLifeCover
						+ "</td>" + "<td>" + getLifeCoverPremium + "</td>" + "</tr>";
			
//			String getLifeCover = df.format(get.getLifeCover());	
//			String getLifeCover = df.format(get.getLifeCover());	
//			String getLifeCover = df.format(get.getLifeCover());	

			
			catageoryTotal = catageoryTotal + get.getAge();
			total = total + get.getPastService();
			salaryTotal = salaryTotal + get.getSalary().longValue();
			acrGtyTotal  = (long) (acrGtyTotal + get.getAccruedGratuity().longValue());
			taGtyTotal = (long) (taGtyTotal + get.getTotalServiceGratuity());
			lifeCoverTotal = (long) (lifeCoverTotal + get.getLifeCover());
			lcPremiumTotal = lcPremiumTotal + get.getLifeCoverPremium();

			if (i % 45 == 0)
				detailTable += "</table>" + this.topPdf(ageValuationDto, pageno++) + table;
			i++;
			
		}
			

		// to adjust min height
//			for (int i = 1; i <= 5; i++) {
//
//				detailTable += "<tr>" 
//						+ "<td></td>" 
//						+ "<td class=\"tdrightalign\"></td>"
//						+ "<td class=\"tdrightalign\"></td>" 
//						+ "<td class=\"tdrightalign\"></td>"
//						+ "<td class=\"tdrightalign\"></td>"
//						+ "<td class=\"tdrightalign\"></td>"
//						+ "<td class=\"tdrightalign\"></td>" 
//						+ "<td class=\"tdrightalign\"></td>"
//						+ "</tr>";
//			}

//			detailTable += "<tr >"
//					+ "<th class=\"tdrightalign\">Catgeory</th>"
//					+ "<th class=\"tdrightalign \"> </th>"
//					+ "<th class=\"tdrightalign\">Total</th>"
//					+ "</tr>";
		DecimalFormat df = new DecimalFormat("#0.00"); 
		String getSalarytotal = df.format(salaryTotal);	
		String getacrGtyTotal = df.format(acrGtyTotal);	
		String gettaGtyTotal = df.format(taGtyTotal);	
		String getlifeCoverTotal = df.format(lifeCoverTotal);	
	
		detailTable += "<tr style=\"text-align:right\">" 
		         + "<td style=\"font-weight:bold\">"+ BigDecimal.valueOf(catageoryTotal).setScale(2, RoundingMode.UP).intValue() +"</td>"
			    +"<td> </td>" 
			    + "<td style=\"font-weight:bold\">"+ BigDecimal.valueOf(total).setScale(2, RoundingMode.UP).intValue() +"</td>"
			    + "<td style=\"font-weight:bold\">"+ getSalarytotal+"</td>"
			    + "<td style=\"font-weight:bold\">"+ getacrGtyTotal +"</td>"
			    + "<td style=\"font-weight:bold\">"+ gettaGtyTotal +"</td>"
			    + "<td style=\"font-weight:bold\">"+ getlifeCoverTotal +"</td>"
			    + "<td style=\"font-weight:bold\">"+  BigDecimal.valueOf(lcPremiumTotal).setScale(2, RoundingMode.UP).doubleValue()  +"</td>"
			    + "</tr>";

		detailTable += "<tfoot>" + "<tr></tr>" + "</tfoot>" + "</table>"
				+ "<hr style=\"border: 1px dashed black;\"></hr>";

//			String average = 
//					"<table class=\"tableborder\"  style=\"width:100%;height:10pt;\">"
//					+ "<tr>" 
//					+ "<th class=\"tdrightalign \">Average Age</th>"
//					+ "<th class=\"tdrightalign \">Average PS</th>" 
//					+ "<th class=\"tdrightalign \">Average Salry</th>"
//					+ "</tr> "
//					+ "</table>"
//					+ "<hr style=\"border: 1px dashed black;\"></hr>";
		
		reportBody = reportBody + table + detailTable;

		return reportBody;
	}


	private String topPdf(AgeValuationDto ageValuationDto, int pageno) {
		String page_break = "";
		String getAnnualRenewlDate=DateUtils.dateToStringDDMMYYYY(ageValuationDto.getAnnualRenewlDate());

		if (pageno > 1)
			page_break = "<p class=\"pagebreak\"></p>";
		
		return page_break + "<div class=\"container\">"

				+ "<div class=\"row justify-content-md-center\">"

				+ "<div class=\"col-sm\">"
				+ "LIC of India - P&amp;GS Department &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; P &amp; Gs Dept Chennai Do I &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "Date : " + DateUtils.dateToStringDDMMYYYY(new Date()) + "</div> "

				+ "<div class=\"col-sm\">"
				+ "Cost &amp; Benefits Schedule &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Group Gratuity Cash Accumulation &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Page :"+pageno+""
				+ "</div>"

				+ "<div class=\"col-sm\"> " + "Coustomer Code: "+ageValuationDto.getCustomerCode()
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "
				+ "MPH Name :"+ageValuationDto.getMphName()+ "</div>"

				+ "<div class=\"col-sm\"> " + "Policy No : "+ageValuationDto.getPolicyNumber()
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "
				+ "Premium Mode Y " + "&nbsp; " + "Annual Renewal :"+getAnnualRenewlDate+ "</div><br></br>"

				+ "<div class=\"col-sm\" style=\"text-align:center;\"> AGE GROUP WISE CALCULATION SHEET" + "</div>"

				+ "</div>" + "</div><br></br>" + "<hr style=\"border: 1px dashed black;\"></hr>";
	}
	
	@Override
	public String premiumreceipt(Long masterpolicyId) throws IOException  {
		
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
//		List<AgeValuationReportDto> ageValuationReportDto = new ArrayList<AgeValuationReportDto>();
//		List<Object[]> getAgeReport = policyRepository.getAgeReport(masterpolicyId);

//		ageValuationReportDto = PolicyHelper.valuationObjtoDto(getAgeReport);
		
		
		List<PremiumReceiptDto> premiumReceiptDto = PolicyHelper.getPRobjecttoDto(
				policyRepository.findBypremiumreceiptdata(masterpolicyId));
		
		
		
		boolean showLogo = true;
		String reportBody = "";
		String reportStyles = "";
		String htmlContent2 = "</div></body></html>";
		
		reportBody = premiumadjustmentvoucher(premiumReceiptDto.get(0), masterpolicyId) + htmlContent2;

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

		if (showLogo) {
			completehtmlContent = completehtmlContent
					+ "<p style=\"text-indent: 0pt;text-align: center;\"><span><table style=\"width:100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr style=\"display:flex;justify-content:space-between;align-items:center\"><td style=\"padding-left:24pt\"><img width=\"100\" height=\"63\" src=\"data:image/jpg;base64,"
					+ licLogo + "\"/></td>" + "</tr></table></span></p>";
		}
		completehtmlContent = completehtmlContent + "<p style=\"text-indent: 0pt;text-align: left;\"><br/></p>"
				+ reportBody + "";

		String htmlFileLoc = tempPdfLocation + "/" + masterpolicyId + ".html";
		FileWriter fw = new FileWriter(htmlFileLoc);
		fw.write(completehtmlContent);
		fw.close();

		FileOutputStream fileOutputStream = new FileOutputStream(tempPdfLocation + "/" + masterpolicyId + ".pdf");
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

		return tempPdfLocation + "/" + masterpolicyId + ".pdf";
	}
	
	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;

	private String premiumadjustmentvoucher(PremiumReceiptDto premiumReceiptDto,Long masterpolicyId ) {
		
		JsonNode mphBasic =null;
		JsonNode actualobj = commonModuleService.getProposalNumber(premiumReceiptDto.getProposalNumber());
		System.out.println(actualobj);
		
		mphBasic =	actualobj.path("mphDetails").path("mphProductDetails");
		actualobj.path("mphDetails").path("mphContactDetails");
		
		PremiumReceiptDto premiumReceiptDtos = PolicyHelper.getobjmph(mphBasic); 

		String productCode = commonModuleService.getProductCode(premiumReceiptDto.getProductId()); 
		        
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
			pdfClaimForward = commonMasterUnitRepository.findByUnitCode(premiumReceiptDto.getUnitCode());
		}
	
		pdfClaimForwardLetter = PolicyClaimHelper.claimforward(pdfClaimForward);
		
		int pageno = 1;

		String reportBody = this.toppdfs(premiumReceiptDto,premiumReceiptDtos,pdfClaimForwardLetter,pageno,productCode);
				
		masterPolicyRepository.getPolicyIdClaim(premiumReceiptDto.getPolicyNumber()); 
		
				String getAnnualRenewalDate=DateUtils.dateToStringDDMMYYYY(premiumReceiptDto.getAnnualRenewalDate());
				String getCollectionDate=DateUtils.dateToStringDDMMYYYY(premiumReceiptDto.getCollectionDate());
				String getPremiumAdjustment=DateUtils.dateToStringDDMMYYYY(premiumReceiptDto.getPremiumAdjustment());
				String NextArd=DateUtils.dateToStringDDMMYYYY(premiumReceiptDto.getNextArd());	
				
			    String table1="<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
					      +"<tr style=\"text-align: center; vertical-align: middle;\">"
					      +"<th style=\"width=16.70%\">Deposit Nos</th>"
					      +"<th style=\"width=16.70%\">Deposit Date</th>"
					      +"<th style=\"width=16.70%\">Deposit Amount</th>"
					      +"<th style=\"width=16.70%\">Deposit Nos.</th>"
					      +"<th style=\"width=16.70%\">Deposit Date</th>"
					      +"<th style=\"width=16.70%\">Deposit Amount</th>"
					      +"</tr>";
//			    PremiumDepositDto get : premiumReceiptDto.getDepositDto()	    
			    for (int i=1;i<2;i++) {
			    	table1 += "<tr>"
			    	+ "<td style=\"text-align:center;\">"+premiumReceiptDto.getCollectionNumber()+"</td>"
			    	+ "<td style=\"text-align:center;\">"+getCollectionDate+"</td>"
			    	+ "<td style=\"text-align:center;\">"+premiumReceiptDto.getAvailableAmount()+"</td>";
//			    	+ "<td>"+premiumReceiptDto.getCollectionNumber()+"</td>"
//			    	+ "<td>"+getCollectionDate+"</td>"
//			    	+ "<td>"+premiumReceiptDto.getAvailableAmount()+"</td>";
			    	table1 += "</tr>";
			    }
					    
			    table1+="</table><p>&nbsp;</p>";
			    
			    String reportBody1= "<table style=\"width: 120%;\">\r\n"
					    + " <tr><td>Annual Renewal Date: "+getAnnualRenewalDate+"</td>\r\n"
					    + " <td class=\"alignRight\">Premium Adjustment For: "+getPremiumAdjustment+"</td></tr>\r\n"
					    + " </table>"
					    
			    		+ " <table style=\"width: 120%;\">\r\n"
			    		+ " <tr><td>Next Premium Due: "+NextArd+"</td>\r\n"
			    		+ " <td class=\"alignRight\">Premium Mode: "+premiumReceiptDto.getPremiumMode()+"</td></tr>\r\n"
			    		+ " </table>";
			    
			    Long total1 = premiumReceiptDto.getLifePremium();
				
			    Double totalFund = premiumReceiptDto.getPastService().doubleValue()+premiumReceiptDto.getCurrentService().doubleValue();
			    
			    Double totalpremium = totalFund.doubleValue()+premiumReceiptDto.getLifePremium();
			    
			    String words = commonUtils.convertAmountToWords(totalpremium.intValue());

			    DecimalFormat df = new DecimalFormat("#0.00"); 
				String gettotalFund = df.format(totalFund);
				String getLifePremium = df.format(premiumReceiptDto.getLifePremium().doubleValue());
				String totaltermInsurance = df.format(total1.doubleValue());
				String totalpremiumfinal = df.format(totalpremium);

			    String table3="<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
			    +"<tr style=\"text-align: center; vertical-align: middle;\">"
			    +"<td style=\"width=20%\">Description</td>"
			    +"<td style=\"width=20%\">PREMIUM</td>"
			    +"<td style=\"width=60%\" colspan=\"5\"> GST </td>"
			    +"</tr>"
			    +"<tr style=\"text-align: center; vertical-align: middle;\">"
			    +"<td style=\"width=20%\"></td>"
			    +"<td style=\"width=20%\"> </td>"
			    +"<td style=\"width=12%\">CGST</td>"
			    +"<td style=\"width=12%\">SGST</td>"
			    +"<td style=\"width=12%\">UGST</td>"
			    +"<td style=\"width=12%\">IGST</td>"
			    +"<td style=\"width=12%\">TOTAL</td>"
			    +"</tr>"  
			    +"<tr>"
			    +"<td style=\"text-align: left;\">FUNDED</td>"
			    +"<td style=\"text-align: right;\">"+gettotalFund+"</td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"text-align: right;\">"+gettotalFund+"</td>"
			    +"</tr>";
			    if (isDevEnvironment == false) {						
					MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository.findById(masterpolicyId);		
					MasterPolicyContributionDetails masterPolicyContributionDetails = masterPolicyContributionDetailRepository
							.findBymasterPolicyId(masterpolicyId);					
					Double getAdjustment = masterPolicyContributionDetails.getLifePremium()
							+ masterPolicyContributionDetails.getPastService()
							+ masterPolicyContributionDetails.getCurrentServices()+masterPolicyContributionDetails.getGst();
			
					String getStateName = commonMasterUnitRepository.getStateNameByUnitCode(masterPolicyEntity.getUnitCode());
					String getUnitStateType = commonMasterStateRepository.getStateType(getStateName);
					String getMPHStateType = null;
					MPHEntity getMPHEntity = mPHRepository.findById(masterPolicyEntity.getMasterpolicyHolder()).get();
				for (MPHAddressEntity getMPHAddressEntity : getMPHEntity.getMphAddresses()) {
					if (getMPHAddressEntity.getStateName() != null || getMPHAddressEntity.getAddressLine1() != null) {
						getMPHAddressEntity.getAddressLine1();
						getMPHStateType = commonMasterStateRepository.getStateType(getMPHAddressEntity.getStateName());
						break;
					}
				}
			
				HSNCodeDto hSNCodeDto = accountingService.getHSNCode();
				Map<String, Double> gstComponents = accountingService.getGstComponents(getUnitStateType, getMPHStateType, hSNCodeDto, getAdjustment.doubleValue());
				gstComponents.get("CGST").longValue();
				
				gstComponents.get("CGST").longValue();
				gstComponents.get("SGST").longValue();
				gstComponents.get("UTGST").longValue();
				gstComponents.get("IGST").longValue();
				
				//checking for values
				
				Double TOTAL =premiumReceiptDto.getLifePremium().doubleValue()+gstComponents.get("CGST").doubleValue()+gstComponents.get("SGST").doubleValue()+gstComponents.get("UTGST").doubleValue()+gstComponents.get("IGST").doubleValue();
				
				String CGST = df.format(gstComponents.get("CGST").doubleValue());
				String SGST = df.format(gstComponents.get("SGST").doubleValue());
				String UTGST = df.format(gstComponents.get("UTGST").doubleValue());
				String IGST = df.format(gstComponents.get("IGST").doubleValue());


				CGST = (CGST.equals("0.00")?"":CGST);
				SGST = (SGST.equals("0.00")?"":SGST);
				UTGST = (UTGST.equals("0.00")?"":UTGST);
				IGST = (IGST.equals("0.00")?"":IGST);
	
				table3+="<tr>"
			    +"<td style=\"text-align: left;\">TERM INSURANCE </td>"
			    +"<td style=\"text-align: right;\">"+getLifePremium+"</td>"
			    +"<td style=\"text-align: right;\">"+CGST +"</td>"
			    +"<td style=\"text-align: right;\">"+SGST+"</td>"
			    +"<td style=\"text-align: right;\">"+UTGST+"</td>"
			    +"<td style=\"text-align: right;\">"+IGST+"</td>"
			    +"<td style=\"text-align: right;\">"+TOTAL+"</td>"
			    +"</tr>";
			    }
			    Double FinalTotal = totalFund.doubleValue()+total1;
			    String FinalTotal1 = df.format(FinalTotal);
			    if(isDevEnvironment == true)	{		    
			    table3+="<tr>"
			    +"<td style=\"text-align: left;\">TERM INSURANCE</td>"
			    +"<td style=\"text-align: right;\">"+getLifePremium+"</td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"text-align: right;\">"+totaltermInsurance+"</td>"
			    +"</tr>";
			    }		    
			    table3+="<tr>"
			    +"<td style=\"text-align: left;\">ANNUITY PURCHASE PRICE</td>"
			    +"<td style=\"width=20%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"</tr>"
			    +"<tr>"
			    +"<td style=\"text-align: left;\">LATE FEE ON PREMIUM </td>"
			    +"<td style=\"width=20%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"</tr>"
			    +"<tr>"
			    +"<td style=\"text-align: left;\">OTHERS</td>"
			    +"<td style=\"width=20%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"<td style=\"width=12%\"></td>"
			    +"</tr>"
			    +"<tr>"
			    +"<td style=\"text-align: left;\">TOTAL</td>"
			    +"<td style=\"text-align: right;\">"+totalpremiumfinal+"</td>"
			    +"<td style=\"text-align: right;\"></td>"
			    +"<td style=\"text-align: right;\"></td>"
			    +"<td style=\"text-align: right;\"></td>"
			    +"<td style=\"text-align: right;\"></td>"
			    +"<td style=\"text-align: right;\">"+FinalTotal1+"</td>"
			    +"</tr>"
			    +"</table>"
						
			    +"<p>&nbsp;</p>";
			    String body="<p>Address of the policy holder: "+premiumReceiptDto.getAddressLine1()+","+premiumReceiptDto.getAddressLine2()+","+premiumReceiptDto.getAddressLine3()+" </p>"		
			   	+"<p>&nbsp;</p>" 
				+"<p> GST no. of Customer: </p>"
				 
				+"<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
			    +"<tr>"
			    +"<td style=\"width=30%\">Total Premium Amount</td>"
			    +"<td style=\"width=70%\">Rs. "+totalpremium.longValue()+" &amp; "+words+" </td>"
			    +"</tr>"
			    +"</table>"
			+"<p>This Final premium receipt is electronically generated on date :"+DateUtils.dateToStringDDMMYYYY(new Date())+" and does not </p>"
			+"<p>require signature.</p>"
			+"<p>{If premium amount  Rs. 5000 and other than Mudrank.</p>"
			+"<p>The Physical receipt with revenue stamp will be issued shortly.}will not come in print</p>"
			+"<p>&nbsp;</p>"
			+"<p style=\"text-align:right;\">Mudrank</p>"
			+"<p>&nbsp;</p>"
			+"<p style=\"text-align:right;\">Revenue Stamp</p>"		
			+"<p>&nbsp;</p>";
			
			    String table4="<table align=\"right\" width=\"5%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
						+"<tr>"
						+"<td > &nbsp;&nbsp; </td>" 
						+"</tr>"
						+"</table>";
			
		return reportBody+table1+reportBody1+table3+body+table4;
		}
	
//		return null;
//	}
	
private String toppdfs(PremiumReceiptDto premiumReceiptDto,PremiumReceiptDto premiumReceiptDtos,CommonMasterUnitEntity pdfClaimForwardLetter,int pageno,String productCode) {
	
		String getCreatedDate=DateUtils.dateToStringDDMMYYYY(premiumReceiptDto.getCreatedDate());

		String product = productCode.replace('"', ' ');
	
		String page_break = "";
		if (pageno > 1)
		page_break = "<p class=\"pagebreak\"></p>";
	
		return page_break + "<p style=\"text-align:center;\">PENSION AND GROUP SCHEME DEPARTMENT</p>"
		+ " <p>&nbsp;</p>"
		+ " <table style=\"width: 100%;\">\r\n"
		+ " <tr><td>Date of Adjustment:"+getCreatedDate+"</td>\r\n"
		+ " <td style=\"text-align:right;\">Servicing Unit:P&amp;GS UNIT,"+pdfClaimForwardLetter.getDescription()+"</td></tr>\r\n"
		+ " </table>"
    
    	+ " <table style=\"width: 100%;\">\r\n"
    	+ " <tr><td>Adjustment No:"+premiumReceiptDto.getContributionId()+"</td>\r\n"
    	+ " <td style=\"text-align:right;\">Address of Unit:</td></tr>\r\n"
    	+ " </table>"
    	+ " <table style=\"width: 100%;\">"
    	+ " <tr><td style=\"text-align:right;\">"+pdfClaimForwardLetter.getAddress1()+"</td></tr>\r\n"
    	+ " <tr><td style=\"text-align:right;\">"+pdfClaimForwardLetter.getAddress2()+"</td></tr>\r\n"
    	+ " <tr><td style=\"text-align:right;\">"+pdfClaimForwardLetter.getAddress3()+"</td>\r\n"
    	+ "</tr>"
    	+ " </table>"

    
    	+ " <table style=\"width: 150%;\">\r\n"
    	+ " <tr><td>Unit GST Number:</td>\r\n"
    	+ " </tr>"
    	+ " </table>"
    
		+ " <p style=\"text-align:center;\">Premium Receipt</p>"
		+ " <table style=\"width: 180%;\">\r\n"
		+ " <tr><td>MPH Name :"+premiumReceiptDto.getMphname()+"</td>\r\n"
		+ " <td class=\"alignRight\">Mode of Collection of Deposit:</td></tr>\r\n"
	    + " </table>"
    
		+ " <p>&nbsp;</p>"

		+"<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
		+"<tr style=\"text-align: center; vertical-align: middle;\">"
		+"<td style=\"width=16.70%\">Policy no.</td>"
		+"<td style=\"width=16.70%\">"+premiumReceiptDto.getPolicyNumber()+"</td>"
		+"<td style=\"width=16.70%\">UIN</td>"
		+"<td style=\"width=16.70%\">"+premiumReceiptDtos.getUin()+"</td>"
		+"<td style=\"width=16.70%\">Name of Plan</td>"
		+"<td style=\"width=16.70%\">"+product+"</td>"
		+"</tr>"
		+"</table>"
		+"<p>&nbsp;</p>";
	}

@Override
public String getcandbsheetpdf(Long policyId,Long taggedStatus) throws IOException {

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

		List<GenerateCBSheetPdfDto> generateCBSheetPdfDto = new ArrayList<GenerateCBSheetPdfDto>();
				
		List<Object[]> policyMasterData =null;
		if(taggedStatus == 139) {
				policyMasterData= memberHelper.getPolicyMasterDataForExcel(policyId);
				
		}
		if(taggedStatus == 138) {
			policyMasterData= memberHelper.getPolicyStagingDataForExcel(policyId);
		}
		generateCBSheetPdfDto = PolicyHelper.getcbObjtoDto(policyMasterData);
	
		boolean showLogo = false;
		String reportBody = "";
		String reportStyles = "";
		String htmlContent2 = "</div></body></html>";
		
		reportBody = generatecbsheet(generateCBSheetPdfDto) + htmlContent2;

	String completehtmlContent = "<!DOCTYPE  html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
			+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><title>LIC ePGS</title><meta name=\"author\" content=\"LIC PNGS\"/><meta name=\"keywords\" content=\"Customer Communication\"/><meta name=\"description\" content=\"Report/Letter/Contract\"/>"
			+ "<style type=\"text/css\"> @page{size: A4 landscape;} body{border-width:0px;\r\n"
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
			+ " table, tbody, td {vertical-align: top; overflow: visible; color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 8pt; margin:0pt; } "
			+ " @media print { .pagebreak {page-break-after: always;} } "
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
	
		String htmlFileLoc = tempPdfLocation + "/" +policyId +".html";
		FileWriter fw = new FileWriter(htmlFileLoc);
		fw.write(completehtmlContent);
		fw.close();
	
		FileOutputStream fileOutputStream = new FileOutputStream(tempPdfLocation + "/" + policyId + ".pdf");
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
	System.out.println(tempPdfLocation +"/"+ policyId +".pdf");
		return tempPdfLocation +"/"+ policyId +".pdf";
	}

		private String generatecbsheet(List<GenerateCBSheetPdfDto> generateCBSheetPdfDtos) {
			
//			List<GenerateCBSheetPdfDto> generateCBSheetPdfDtos = new 
//					this.getDummyData();

			int pageno = 1;
			String reportBody = this.topPdf(generateCBSheetPdfDtos.get(0), pageno);
			pageno++;
			
			String table = 
				     
					"<table class=\"tableborder\"  style=\"width:100%;\">" 
					+"<thead>"
					+ "<tr style=\"text-align:right\">"
					+ "<td style=\"text-align:left\">LIC ID</td>" + "<td style=\"text-align:left\">EMP NO</td>"
					+ "<td style=\"text-align:left\">NAME</td>" + "<td>CAT</td>"
					+ "<td>DOB</td>" + "<td>DOA</td>"
					+ "<td>AGE</td>" + "<td>SALARY</td>"
					+ "<td>P S</td>" + "<td>T S</td>"
					+ "<td>TOT GRTY</td>" + "<td>ACC GRTY</td>"
					+ "<td>LIFE COVER</td>" + "<td>LCP</td>"
					+ "<td>PSB</td>"  + "<td>CSB</td>"      
					+ "</tr>"
					+"</thead>";
			 
			String detailTable = "";
//			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
			Double averageAge=0.0;
			Double averageMonthlySalary=0.0;
			Double averagePastService =0.0;
			int i = 1;
			
			//Total
			Double totalMonthlySalary=0.0;
			Double totalTotalGratuity = 0.0;
			Double totalAccGratuity = 0.0;
			Double totalLifeCover = 0.0;
			Double totalLcp = 0.0;
			Double totalPsb = 0.0;
			Double totalCsb = 0.0;
			
			for (GenerateCBSheetPdfDto get : generateCBSheetPdfDtos ) {
	
				detailTable +=
						"<tr style=\"text-align:right\">"
						+ "<td style=\"text-align:left\">"+ get.getLicId()+"</td>" + "<td style=\"text-align:left\">"+ get.getEmployeeNo()+"</td>"
						+ "<td style=\"text-align:left\">"+ get.getName()+" </td>" + "<td>"+ get.getCategory()+"</td>"
						+ "<td>"+ get.getDob() +"</td>" + "<td>"+ get.getDoa()+"</td>"
						+ "<td>"+ get.getAge()+"</td>" + "<td>"+ get.getSalary()+"</td>"
						+ "<td>"+ get.getPastService()+"</td>" + "<td>"+ get.getTotalService()+"</td>"
						+ "<td>"+ get.getTotalServiceGratuity().intValue()+"</td>" + "<td>"+ get.getPastServiceGratuity().intValue()+"</td>"
						+ "<td>"+ get.getLifeCover().intValue()+"</td>" + "<td>"+ get.getLifeCoverPremium().intValue()+"</td>"
						+ "<td>"+ get.getPastServiceBenefit().intValue()+"</td>" + "<td>"+get.getCurrentServiceBenefit().intValue()+"</td>"
						+ "</tr>";
				
				averageAge = averageAge+get.getAge().doubleValue()/generateCBSheetPdfDtos.size();
				averageMonthlySalary =averageMonthlySalary+  get.getSalary()/generateCBSheetPdfDtos.size();
				averagePastService =averagePastService+get.getPastService().doubleValue()/generateCBSheetPdfDtos.size();
				
				totalMonthlySalary = totalMonthlySalary + Double.valueOf(get.getSalary());
				totalTotalGratuity = totalTotalGratuity+Double.valueOf(get.getTotalServiceGratuity());
				totalAccGratuity = totalAccGratuity + Double.valueOf(get.getPastServiceGratuity());
				totalLifeCover = totalLifeCover+Double.valueOf(get.getLifeCover());
				totalLcp = totalLcp + Double.valueOf(get.getLifeCoverPremium());
				totalPsb += (get.getPastServiceBenefit() != null) ? Double.valueOf(get.getPastServiceBenefit()) : 0;
				totalCsb += (get.getCurrentServiceBenefit() != null) ? Double.valueOf(get.getPastServiceBenefit()) : 0;
				
				if (i % 35 == 0 && generateCBSheetPdfDtos.size() != i)
					detailTable += "</table>" + this.topPdf(get, pageno++) + table;
				i++;
				
				if (i == generateCBSheetPdfDtos.size()+1) {
					detailTable +=
							"<tr style=\"text-align:right\">"
							+ "<td></td>" + "<td></td>"
							+ "<td style=\"font-weight:bold; text-align:left\">Average</td>" + "<td></td>"
							+ "<td></td>" + "<td></td>"
							+ "<td style=\"font-weight:bold\">"+ BigDecimal.valueOf(averageAge).setScale(2, RoundingMode.UP).doubleValue() +"</td>" + "<td style=\"font-weight:bold\">"+ BigDecimal.valueOf(averageMonthlySalary).setScale(2, RoundingMode.UP).doubleValue() +"</td>"
							+ "<td style=\"font-weight:bold\">"+ BigDecimal.valueOf(averagePastService).setScale(2, RoundingMode.UP).doubleValue() +"</td>" + "<td style=\"text-align:right\"></td>"
							+ "<td style=\"text-align:right\"></td>" + "<td style=\"text-align:right\"></td>"
							+ "<td style=\"text-align:right\"></td>" + "<td style=\"text-align:right\"></td>"
							+ "</tr>";
					
					detailTable  +=
							
							"<tr style=\"text-align:right\">"
							+ "<td></td>" 
							+ "<td style=\"text-align:left\"></td>"
							+ "<td style=\"font-weight:bold; text-align:left\">Total</td>" + "<td></td>"
							+ "<td></td>"
							+ "<td style=\"text-align:right\"></td>"
							+ "<td style=\"text-align:right\"></td>"
							+ "<td style=\"font-weight:bold\">"+ Math.round(totalMonthlySalary)+"</td>"
							+ "<td></td>"
						    + "<td style=\"text-align:right\"></td>" 
							+ "<td style=\"font-weight:bold\">"+ Math.round(totalTotalGratuity)+"</td>"  
							+ "<td style=\"font-weight:bold\">"+  Math.round(totalAccGratuity)+"</td>" 
							+ "<td style=\"font-weight:bold\">"+  Math.round(totalLifeCover)+"</td>" 
							+ "<td style=\"font-weight:bold\">"+  Math.round(totalLcp)+"</td>" 
							+ "<td style=\"font-weight:bold\">"+  Math.round(totalPsb)+"</td>" 
						    + "<td style=\"font-weight:bold\">"+  Math.round(totalCsb)+"</td>"
							+ "<td style=\"text-align:right\"></td>" + "<td style=\"text-align:right\"></td>"
							+ "<td style=\"text-align:right\"></td>" + "<td style=\"text-align:right\"></td>"
							+ "</tr>";
				}
			}
			
			detailTable += "<tfoot>" + "<tr></tr>" + "</tfoot>" + "</table>"
					+ "<hr style=\"border: 1px dashed black;\"></hr>";
			
			return reportBody+table+detailTable;
	
		}
		
		private String topPdf(GenerateCBSheetPdfDto generateCBSheetPdfDtos, int pageno) {
			String page_break = "";
			if (pageno > 1)
				page_break = "<p class=\"pagebreak\"></p>";

			return page_break + "<table class=\"tableborder\"  style=\"width:100%;\" >"
			+"<tr>"
			+"<td style=\"text-align:left\">Life Insurance Corporation of India</td>"
			+"<td>&nbsp;</td>"
			+"<td style=\"text-align:right\">Date : "+ DateUtils.dateToStringDDMMYYYY(new Date()) +"</td>"
			+"</tr>"
			+"<tr>"
			+"<td style=\"width:33%;\">Group Schemes Department</td>"
			+"<td style=\"text-align:center\">Group Gratuity Cash Accumulation Scheme</td>"
			+"<td style=\"text-align:right\">Page :"+pageno+"</td>"
			+"</tr>"
			+"<tr>"
			+"<td>Cost &amp; Benefits Schedule</td>"
			+"</tr>"
			+"<tr>"
			+"<td>Customer Code : "+generateCBSheetPdfDtos.getCustomerCode()+" </td>"
			+"<td style=\"text-align:center\">Customer Name : "+generateCBSheetPdfDtos.getCustomerName()+" </td>"
			+"</tr>"
			+"<tr>"
			+"<td>Annual Renewal Date : "+generateCBSheetPdfDtos.getAnnualRenewlDate()+" </td>"
			+"</tr>"
			+"<p>&nbsp;</p>"
			+"<tr>"
			+"<td>policy No:"+ generateCBSheetPdfDtos.getPolicyNumber() +" </td>"
			+"</tr>"
			+"</table>";

		}


		@Override
		public String generatepaymentvoucher(String userName, Long policyId) throws IOException {
		
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
			List<Object[]> claimreport = policyRepository.payoutvoucherReport(policyId);

			claimPayoutDto = PolicyClaimHelper.voucherObjtoDto(claimreport);
			
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
			
		

			List<Object[]> claimPayoutItem = policyRepository.paymentvoucher(claimPayoutDto.getContributionDetailId());
			
			claimPayoutDto.setClaimPayoutItemDto(PolicyClaimHelper.claimvalObjtoDto(claimPayoutItem));
		
			String productCode = commonModuleService.getProductCode(claimPayoutDto.getProductId());
			
			boolean showLogo = true;
			String reportBody = "";
			String reportStyles = "";
			String htmlContent2 = "</div></body></html>"; // DO NOT DISTRUB THIS LINE

				reportBody = GeneratepaymentvoucherReport(policyId,productCode,claimPayoutDto,pdfClaimForwardLetter) + htmlContent2;

				reportStyles = quotationValuationReportStyles();
				showLogo = false;
//				} else if (reportType.equals("policyBond")) {
//					String getPolicyDetail=masterPolicyRepository.getBondDetail(quotationIdorPolicy);
	//
//					String[] get = getPolicyDetail.toString().split(",");
						
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

			String htmlFileLoc = tempPdfLocation + "/" + policyId + ".html";

			FileWriter fw = new FileWriter(htmlFileLoc);
			fw.write(completehtmlContent);
			fw.close();

			FileOutputStream fileOutputStream = new FileOutputStream(tempPdfLocation + "/" + policyId + ".pdf");
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

			return tempPdfLocation + "/" + policyId + ".pdf";
		}

		private String GeneratepaymentvoucherReport(Long policyId,String productCode,ClaimPayoutDto claimPayoutDto,CommonMasterUnitEntity pdfClaimForwardLetter) {
			int pageno = 1;

			String reportBody = this.toppdf(claimPayoutDto,productCode,pdfClaimForwardLetter,pageno);
			
			String detailTable = "";
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
				DecimalFormat fddebit = new DecimalFormat("#0.00");
				   String debitAMOUNT = fddebit.format(get.getDebitAmount());
				   String creditAMOUNT = fddebit.format(get.getCreditAmount());
				if(get.getDebitCodeDescribtion()!=null && StringUtils.isNotBlank(get.getDebitCodeDescribtion())) {
				detailTable += 
						"<tr>"
						+ "<td>"+(get.getDebitcode()!= null ? get.getDebitcode():"")+"</td>"
						+ "<td>"+(get.getDebitCodeDescribtion()!= null ? get.getDebitCodeDescribtion():"")+"</td>" 
					    + "<td class=\"tdrightalign\">"+(debitAMOUNT!= null ? debitAMOUNT:"")+"</td>" 
						+ "</tr>" 
						;
				}else {
					detailTable += 
							"<tr>"
							+ "<td>"+(get.getCreditcode()!= null ? get.getCreditcode():"")+"</td>"
							+ "<td>"+(get.getCreditCodeDescribtion()!= null ? get.getCreditCodeDescribtion():"")+"</td>" 
							+ "<td></td>" 
							+ "<td class=\"tdrightalign\">"+(creditAMOUNT!= null ? creditAMOUNT:"")+"</td>" 
							+ "</tr>" 
							;
				}
			}
			
			Double debit = 0.0;
			Double credit = 0.0;
				for (ClaimPayoutItemDto get : claimPayoutDto.getClaimPayoutItemDto()) {	
					debit=debit+get.getDebitAmount();
					credit=credit+get.getCreditAmount();
					}	
				DecimalFormat fdamount = new DecimalFormat("#0.00");
				String debitAMOUNT = fdamount.format(debit);
				String creditAMOUNT = fdamount.format(credit);
			
//				+ "<td style=\"width=20%;border-top:1px solid;\">Total</td>"
//				+ "<td style=\"width=20%;border-top:1px solid;\"></td>"
//				+ "<td class=\"tdrightalign\" style=\border-top:1px solid;\">"+(debitAMOUNT!=null?debitAMOUNT:0.0)+"</td>" 
//				+ "<td class=\"tdrightalign\" style=\border-top:1px solid;\">"+(creditAMOUNT!= null ?creditAMOUNT:0.0)+"</td>" 
//				+ "</tr>";

		    detailTable+=
			"<tr>"
			+ "<td style=\"width=50%;\">Total</td>"
			+ "<td style=\"width=50%;\"></td>"
			+ "<td style=\"text-align:right;border-top:1px solid;\">"+(debitAMOUNT!=null?debitAMOUNT:0.0)+"</td>" 
			+ "<td style=\"text-align:right;border-top:1px solid;\">"+(creditAMOUNT!= null ?creditAMOUNT:0.0)+"</td>" 
			+ "</tr>";
		    
			detailTable+= "</table>";

			String challanDate=DateUtils.dateToStringDDMMYYYY(claimPayoutDto.getChallanDate());
			
			String reportbody1= "<p>&nbsp;</p>" + "<p>As per the following details</p>" + "<p>&nbsp;</p>"

					+ "<table class=\"tableborder\" callpadding=\"0\" cellspacing=\"0\" style=\"width:100%;min-height:20pt;\">"
					+ "<p>&nbsp;</p>" + "<p>&nbsp;</p>" + "<p>Voucher No :"+claimPayoutDto.getChallanNo()+" </p>" + "<p>Voucher Date :"+challanDate+" </p>"
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

		private String toppdf(ClaimPayoutDto claimPayoutDto,String productCode,CommonMasterUnitEntity data,int pageno) {
			String get1=productCode.replace('"',' ');
			String amountInWords = "";
			Double debit = 0.0;
			Double credit = 0.0;
				for (ClaimPayoutItemDto get : claimPayoutDto.getClaimPayoutItemDto()) {	
					debit=debit+get.getDebitAmount();
					credit=credit+get.getCreditAmount();
					}
				if(debit>0.0) {
				amountInWords = commonUtils.convertAmountToWords(debit.intValue());
				}else {
				amountInWords = commonUtils.convertAmountToWords(credit.intValue());	
				}
					    
			String challanDate=DateUtils.dateToStringDDMMYYYY(claimPayoutDto.getChallanDate());
			String page_break = "";
			if (pageno > 1)
				page_break = "<p class=\"pagebreak\"></p>";

			return page_break + "<p class=\"pb10\" style=\"text-align:center;\"><u>PAYMENT / ADJUSTMENT VOUCHER</u></p>"

					+ "<table style=\"width:100%;\">"
					+ "<tr><td style=\"width:70%;\">P &amp; GS Dept : "+data.getDescription()+"</td><td style=\"width:30%;\">Voucher No."+claimPayoutDto.getChallanNo()+" </td></tr>"
					+ "<tr><td>Divl. Name: </td><td>Date:"+challanDate+"</td></tr>" + "</table>"

					+ "<p>&nbsp;</p>"

					+ "<p class=\"pb10\" style=\"text-align:center;\">To Cash / Banking Section</p>"
					+ "<p> please Adjust /Issue Crossed / Order Cheque / Cash / M.O / Postage stamps favouring </p>"
					+ "<p> "+claimPayoutDto.getMphName()+" </p>"
					+ "<p>&nbsp;</p>" 
					+ "<p> For " + amountInWords + "</p>" 
					+ "<p>&nbsp;</p>"

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

}
