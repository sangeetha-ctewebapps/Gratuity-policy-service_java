package com.lic.epgs.gratuity.quotation.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.Date;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lic.epgs.gratuity.common.utils.NumericUtils;
import com.lic.epgs.gratuity.quotation.dto.AdjustmentVoucherDetailDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationAdjustmentPDFDto;
import com.lic.epgs.gratuity.quotation.service.QuotationPdfService;
import com.lowagie.text.DocumentException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.CommonMasterUnitEntity;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.repository.MemberCategoryRepository;
import com.lic.epgs.gratuity.common.service.CommonModuleService;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.policy.claim.dto.PdfGeneratorDto;
import com.lic.epgs.gratuity.policy.claim.dto.PdfGeneratorForTableDto;
import com.lic.epgs.gratuity.policy.claim.helper.PolicyClaimHelper;
import com.lic.epgs.gratuity.policy.dto.GenerateCBSheetPdfDto;
import com.lic.epgs.gratuity.policy.dto.PolicyBondDetailDto;
import com.lic.epgs.gratuity.policy.helper.PolicyHelper;
import com.lic.epgs.gratuity.policy.integration.dto.AccountingIntegrationRequestDto;
import com.lic.epgs.gratuity.policy.integration.dto.SuperAnnuationResponseModel;
import com.lic.epgs.gratuity.policy.integration.service.AccountingIntegrationService;
import com.lic.epgs.gratuity.policy.lifecover.repository.PolicyLifeCoverRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.service.RenewalPolicyService;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.quotation.dto.BenefitValuationDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationPDFGenerationDto;
import com.lic.epgs.gratuity.quotation.entity.MasterQuotationEntity;
import com.lic.epgs.gratuity.quotation.entity.QuotationEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.GratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitPropsEntity;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.repository.GratuityBenefitRepository;
import com.lic.epgs.gratuity.quotation.gratuitybenefit.repository.MasterGratuityBenefitRepository;
import com.lic.epgs.gratuity.quotation.helper.QuotationHelper;
import com.lic.epgs.gratuity.quotation.lifecover.entity.LifeCoverEntity;
import com.lic.epgs.gratuity.quotation.lifecover.entity.MasterLifeCoverEntity;
import com.lic.epgs.gratuity.quotation.lifecover.repository.LifeCoverEntityRepository;
import com.lic.epgs.gratuity.quotation.lifecover.repository.MasterLifeCoverEntityRepository;
import com.lic.epgs.gratuity.quotation.member.helper.BulkMemberUploadHelper;
import com.lic.epgs.gratuity.quotation.member.repository.MasterMemberRepository;
import com.lic.epgs.gratuity.quotation.repository.MasterQuotationRepository;
import com.lic.epgs.gratuity.quotation.repository.QuotationRepository;
import com.lic.epgs.gratuity.quotation.valuation.entity.MasterValuationEntity;
import com.lic.epgs.gratuity.quotation.valuation.entity.ValuationEntity;
import com.lic.epgs.gratuity.quotation.valuation.repository.MasterValuationRepository;
import com.lic.epgs.gratuity.quotation.valuation.repository.ValuationRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.MasterValuationBasicEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.entity.ValuationBasicEntity;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.MasterValuationBasicRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.MasterValuationWithdrawalRateRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.ValuationBasicRepository;
import com.lic.epgs.gratuity.quotation.valuationmatrix.repository.ValuationWithdrawalRateRepository;

@Service
public class QuotationPdfServiceImpl implements QuotationPdfService {

	@Value("${temp.pdf.location}")
	private String tempPdfLocation;
	
	@Autowired
	private AccountingIntegrationService accountingIntegrationService;
	
	@Autowired
	private QuotationRepository quotationRepository;

	@Autowired
	private MasterQuotationRepository masterQuotationRepository;

	@Autowired
	private LifeCoverEntityRepository lifeCoverEntityRepository;

	@Autowired
	private MasterLifeCoverEntityRepository masterLifeCoverEntityRepository;

	@Autowired
	private GratuityBenefitRepository gratuityBenefitRepository;
	
	@Autowired
	private MasterGratuityBenefitRepository masterGratuityBenefitRepository;

	@Autowired
	private PolicyLifeCoverRepository policyLifeCoverRepository;

	@Autowired
	private MasterPolicyRepository masterPolicyRepository;

	@Autowired
	private MasterMemberRepository masterMemberRepository;

	@Autowired
	private ValuationRepository valuationRepository;

	@Autowired
	private MasterValuationRepository masterValuationRepository;

	@Autowired
	private ValuationBasicRepository valuationBasicRepository;

	@Autowired
	private MasterValuationBasicRepository masterValuationBasicRepository;

	@Autowired
	private ValuationWithdrawalRateRepository valuationWithdrawalRateRepository;

	@Autowired
	private MasterValuationWithdrawalRateRepository masterValuationWithdrawalRateRepository;

	@Autowired
	private MemberCategoryRepository memberCategoryRepository;
	
	@Autowired
	private CommonMasterUnitRepository commonMasterUnitRepository;

	@Autowired
	private CommonModuleService commonModuleService;
	
	@Autowired
	private BulkMemberUploadHelper memberHelper;

	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;
	
	@Value("${app.commonModuleServiceEndpoint}")
	private String endPoint;
	
	@Override
	public String adjustmentVoucher(Long adjustmentId,String reportType) throws IOException {


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

		if (reportType.equals("regularAdjustment")) {
			showLogo = false;
			reportBody = generateAdjustment(reportType,licLogo) + htmlContent2;

		} 


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

		String htmlFileLoc = tempPdfLocation + "/" + adjustmentId + ".html";

		FileWriter fw = new FileWriter(htmlFileLoc);
		fw.write(completehtmlContent);
		fw.close();

		FileOutputStream fileOutputStream = new FileOutputStream(tempPdfLocation + "/" + adjustmentId + ".pdf");
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

		return tempPdfLocation + "/" + adjustmentId + ".pdf";
		}

	
	private String generateAdjustment(String reportType, String licLogo) {
		QuotationAdjustmentPDFDto spPdfDto = new QuotationAdjustmentPDFDto();
		String reportBody1 = "<table style=\"width:100%;\">" + "<tr>" + "<td style=\"width:25%;\">" + "</td>"
				+ "<td style=\"width:50%;text-align:center;\">"
				+ "<img width=\"100\" height=\"63\" src=\"data:image/jpg;base64," + licLogo + "\"/>" + "</td>"
				+ "<td style=\"width:25%;vertical-align: bottom;\">"
				+ "<div style=\"padding:3pt;border:1pt;border-style:solid;border-color:#000;text-align:center;\">ADJUSTMENT VOUCHER</div>"
				+ "</td>" + "</tr>" + "</table>" + "<table style=\"width:100%;\">" + "<tr>"
				+ "<td style=\"width:70%;\">P &amp; GS Dept. @PNGSDEPT</td>"
				+ "<td style=\"width:30%;\">Voucher No. @VOUCHERNO</td>" + "</tr>" + "<tr>"
				+ "<td>Divl. Name: @DIVLNAME</td>" + "<td>Date: @VOUCHERDATE</td>" + "</tr>" + "</table>"
				+ "<p class=\"pb10\" style=\"text-align:center;\">To Cash / Banking Section</p>"
				+ "<p class=\"pb10\">Please adjust / Issue Crossed / Order Cheque / M O / Postage Stamps favouring: @FAVOURING</p>"
				+ "<p class=\"pb10\">@AMOUNTINWORDS</p>" + "<table style=\"width:100%;\">" + "<tr>"
				+ "<td style=\"width:50%;padding-bottom:10pt;\">Policy No.: @POLICYNUMBER</td>"
				+ "<td style=\"width:50%;\">Scheme Name: @SCHEMENAME</td>" + "</tr>" + "</table>"
				
				+ "<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\" >"
				+ "<tr>" + "<td style=\"width=25%\">Head of Account</td>" + "<td style=\"width=25%\">Code</td>" + "<td style=\"width=25%\">Debit Amount</td>"
				+ "<td style=\"width=25%\">Credit Amount</td></tr>"
				+ "<tr>" + "<td style=\"width=25%\">Deposit Policy</td>" + "<td style=\"width=25%\"></td>" + "<td style=\"width=25%\"></td>"
				+ "<td style=\"width=25%\"></td></tr>"
				
				+ "<tr>" + "<td style=\"width=25%\">Single Premium</td>" + "<td style=\"width=25%\"></td>" + "<td style=\"width=25%\"></td>"
				+ "<td style=\"width=25%\"></td></tr>"
				
				+ "<tr>" + "<td style=\"width=25%\">Single Premium Test</td>" + "<td style=\"width=25%\"></td>" + "<td style=\"width=25%\"></td>"
				+ "<td style=\"width=25%\"></td></tr>"
				
				+ "<tr>" + "<td style=\"width=50%\" colspan =\"2\">Total</td>" + "<td style=\"width=25%\"></td>"
				+ "<td style=\"width=25%\"></td></tr>"
				+"</table>";

		
//				class=\"tableborder\" callpadding=\"4\" cellspacing=\"0\" style=\"width:100%;min-height:100pt;\"
				
		BigDecimal debitTotal = BigDecimal.ZERO;
		BigDecimal creditTotal = BigDecimal.ZERO;
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		String detailTable = "";
		for (AdjustmentVoucherDetailDto adjustmentVoucherDetailDto : spPdfDto.getVoucherDetail()) {
			detailTable += "<tr>" + "<td class=\"rightborder\">" + adjustmentVoucherDetailDto.getHeadOfAccount()
					+ "</td>" + "<td class=\"rightborder\">" + adjustmentVoucherDetailDto.getCode() + "</td>"
					+ "<td class=\"tdrightalign rightborder\">"
					+ formatter.format(adjustmentVoucherDetailDto.getDebitBigdecimal()) + "</td>"
					+ "<td class=\"tdrightalign\">" + formatter.format(adjustmentVoucherDetailDto.getCreditBigDecimal())
					+ "</td>" + "</tr>";
			debitTotal = debitTotal.add(adjustmentVoucherDetailDto.getDebitBigdecimal());
			creditTotal = creditTotal.add(adjustmentVoucherDetailDto.getCreditBigDecimal());
		}

		// to adjust min height
		for (int i = 1; i <= 5; i++) {
			detailTable += "<tr>" + "<td class=\"rightborder\"></td>" + "<td class=\"rightborder\"></td>"
					+ "<td class=\"tdrightalign rightborder\"></td>" + "<td class=\"tdrightalign\"></td>" + "</tr>";
		}
		detailTable += "<tfoot>" + "<tr>" + "<td colspan=\"2\">Total</td>" + "<td class=\"tdrightalign\">"
				+ formatter.format(debitTotal) + "</td>" + "<td class=\"tdrightalign\">" + formatter.format(creditTotal)
				+ "</td>" + "</tr>" + "</tfoot>" ;

		String summaryTable = "<p style=\"padding-top:10pt;\">As per the following details</p>"
				+"<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
				+"<tr>"
				+"<td>"
				+"<table width=\"100%\">"
				
				+"<tr>"
				+"<td colspan=\"10\">"
				+"Regular Deposit Adjustment"
				+"</td>"
				+"</tr>"
				
				+"<tr>"
				+"<td >"
				+"Balance Deposit: .00"
				+"</td>"
				+"</tr>"
				
				+"<tr colspan=\"10\">"
				+"<td width=\"13%\">"
				+"ARD: "
				+"</td>"
				+"<td colspan=\"2\" width=\"13%\">"
				+"01/04/2023"
				+"</td>"
				+"<td colspan=\"2\" width=\"13%\" >"
				+"Mode:"
				+"</td>"
				+"<td colspan=\"2\" width=\"13%\">"
				+"Cash"
				+"</td>"
				+"<td colspan=\"2\" width=\"13%\">"
				+"Due For:"
				+"</td>"
				+"<td colspan=\"2\" width=\"13%\">"
				+"Next Year"
				+"</td>"
				+"</tr>"
				

				+"<tr>"
				+"<td colspan=\"10\">"
				+"Details Adjustment"
				+"</td>"
				+"</tr>"

				+"<tr colspan=\"10\">"
				+"<td width=\"5%\">"
				+"NO"
				+"</td>"
				+"<td width=\"5%\">"
				+""
				+"</td>"
				+"<td width=\"5%\">"
				+"Date"
				+"</td>"
				+"<td width=\"5%\">"
				+""
				+"</td>"
				+"<td width=\"5%\">"
				+"Amount"
				+"</td>"
				+"<td width=\"5%\">"
				+""
				+"</td>"
				+"<td width=\"5%\">"
				+"Adjusted"
				+"</td>"
				+"<td width=\"5%\">"
				+""
				+"</td>"
				+"<td width=\"5%\">"
				+"Balance"
				+"</td>"
				+"<td width=\"5%\">"
				+""
				+"</td>"
				+"</tr>"

				+"</table>"
				+"</td>"
				+"</tr>"
				+"</table>";
//				+ "<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"1\" cellspacing=\"0\" >";
		// to adjust min height
//		for (int i = 1; i <= 1; i++) {
//			summaryTable += "<tr><td></td></tr>";
//		}
//
//		summaryTable += "<tr><td><p class=\"pb10\"> @ADJUSTMENTTYPELABEL</p><p>Balance Deposit: "
//				+ formatter.format(spPdfDto.getBalanceDeposit()) + "</p></td></tr>";
//		summaryTable += "<tr><td><table style=\"width:75%;\"><tr>";
//		summaryTable += "<td>ARD: @ANNUALRENEWALDATE</td>";
//		summaryTable += "<td>Mode: @MODE</td>";
//		summaryTable += "<td>Due For: @DUEFOR</td>";
//		summaryTable += "</tr></table></td></tr>";
//		summaryTable += "<tr><td><p>Details of Adjustment</p></td></tr>";
//		summaryTable += "<table style=\"width:100%;\">";
//		summaryTable += "<tr><td class=\"bottomborder\">No</td><td class=\"bottomborder\">Date</td><td class=\"bottomborder tdrightalign\">Amount</td><td class=\"bottomborder tdrightalign\">Adjusted</td><td class=\"bottomborder tdrightalign\">Balance</td></tr>";

		
		
//		for (SupplementaryAdjustmentDto supplementaryAdjustmentDto : spPdfDto.getSupplymentary()) {
//			summaryTable += "<tr>" + "<td>" + supplementaryAdjustmentDto.getDetailsNo() + "</td>" + "<td>"
//					+ supplementaryAdjustmentDto.getDetailsDate() + "</td>" + "<td class=\"tdrightalign\">"
//					+ formatter.format(supplementaryAdjustmentDto.getDetailsAmount()) + "</td>"
//					+ "<td class=\"tdrightalign\">" + formatter.format(supplementaryAdjustmentDto.getDetailsAdjusted())
//					+ "</td>" + "<td class=\"tdrightalign\">"
//					+ formatter.format(supplementaryAdjustmentDto.getDetailsBalance()) + "</td>" + "</tr>";
//		}
//
//		summaryTable += "</table>";
//		// to adjust min height
//		for (int i = 1; i <= 1; i++) {
//			summaryTable += "<tr><td></td></tr>";
//		}
//		summaryTable += "</table>";

		String footerTable = "<p class=\"pb10\">&nbsp;</p>" + "<table style=\"width:100%;\">" + "<tr>"
				+ "<td style=\"width:50%\">" + "<p class=\"signaturepadding\">Prepared By: " + (spPdfDto.getPreparedBy()!=null ? spPdfDto.getPreparedBy() : "")
				+ "</p>" + "<p class=\"signaturepadding\">Checked By: " + (spPdfDto.getCheckedBy()!=null ? spPdfDto.getCheckedBy() : "")  + "</p>"
				+ "<p class=\"pb10\">Date: " + spPdfDto.getDate() + "</p>" + "<p class=\"pb10\">Cheque No: "
				+ (spPdfDto.getChequeNumber()!=null ? spPdfDto.getChequeNumber() : "") + "</p>" + "<p>Drawn on: " + (spPdfDto.getDrawn()!=null ? spPdfDto.getDrawn() : "") + "</p>" + "</td>"
				
				+ "<td>" + "<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\" >" + "<tr>" + "<td>"
				+ "<table class=\"tableborder\" callpadding=\"0\" cellspacing=\"0\" style=\"width:100%;min-height:100pt;\">"
				+ "<tr>" + "<th>" + "<p class=\"pb10\" style=\"text-align:center;\">PAY</p>"
				+ "<p class=\"pb10\">Sign:</p>" + "<p>Date:" + (spPdfDto.getPayDate()!=null ? spPdfDto.getPayDate() : "") + "</p>" + "</th>" + "<th>"
				+ "<p class=\"pb10\" style=\"text-align:center;\">PAID</p>" + "<p class=\"signaturepadding\">Sign:</p>"
				+ "<p>Date:" + (spPdfDto.getPaidDate()!=null ? spPdfDto.getPaidDate() : "") + "</p>" + "</th>" + "</tr>" + "<tr>" + "<th colspan=\"2\">"
				+"<hr style=\"border: 1px solid black;\"></hr>"
				+ "<p class=\"pt10\" style=\"text-align:center;\">Initials of Officers Signing cheques</p>"
				+ "</th>"
				+ "</tr>" + "</table>" + "</td>" + "</tr>" + "</table>" + "</td>" + "</tr>" + "</table>";

		String adjustmenttypeLabel = new String();
		if (reportType.equalsIgnoreCase("regularAdjustment")) { 
			adjustmenttypeLabel = "Regular Deposit Adjustment";
		} else if (reportType.equalsIgnoreCase("subsequentAdjustment")) {
			adjustmenttypeLabel = "Supplementary Deposit Adjustment";
		} else if (reportType.equalsIgnoreCase("NB")) {
			adjustmenttypeLabel = "New Business Deposit Adjustment";
		}

		String mode = null;
		if (spPdfDto.getMode() != null) {
			if ("1".equals(spPdfDto.getMode())) {
				mode = "Monthly";
			} else if ("2".equals(spPdfDto.getMode())) {
				mode = "Quarterly";
			} else if ("4".equals(spPdfDto.getMode())) {
				mode = "Half-Yearly";
			} else if ("3".equals(spPdfDto.getMode())) {
				mode = "Yearly";
			}
		} else {
			mode = "";
		}

		String reportBody = reportBody1 + detailTable + summaryTable + footerTable;
		reportBody = reportBody.replaceAll("@ADJUSTMENTTYPELABEL", adjustmenttypeLabel)
				.replaceAll("@PNGSDEPT", spPdfDto.getPngsDept() != null ? spPdfDto.getPngsDept() : "PNGSDEPT")
				.replaceAll("@VOUCHERNO", spPdfDto.getVoucherNumber() != null ? spPdfDto.getVoucherNumber() : "")
				.replaceAll("@DIVLNAME", spPdfDto.getDivisionalName() != null ? spPdfDto.getDivisionalName() : "")
				.replaceAll("@VOUCHERDATE", spPdfDto.getVoucherDate() != null ? spPdfDto.getVoucherDate() : "")
				.replaceAll("@FAVOURING", spPdfDto.getFavouringName() != null ? spPdfDto.getFavouringName() : "")
				.replaceAll("@AMOUNTINWORDS", spPdfDto.getAmountInWords() != null ? spPdfDto.getAmountInWords() : "")
				.replaceAll("@POLICYNUMBER", spPdfDto.getPolicyNumber() != null ? spPdfDto.getPolicyNumber() : "")
				.replaceAll("@SCHEMENAME", spPdfDto.getSchemeName() != null ? spPdfDto.getSchemeName() : "")
				.replaceAll("@BALANCEDEPOSIT",
						spPdfDto.getBalanceDeposit() != null
								? NumericUtils.convertBigDecimalToString(spPdfDto.getBalanceDeposit())
								: "")
				.replaceAll("@ANNUALRENEWALDATE", spPdfDto.getArd() != null ? spPdfDto.getArd() : "")
				.replaceAll("@MODE", mode != null ? mode : "")
				.replaceAll("@DUEFOR", spPdfDto.getDuedate() != null ? spPdfDto.getDuedate() : "")
				.replaceAll("@VOUCHERNO", spPdfDto.getVoucherNumber() != null ? spPdfDto.getVoucherNumber() : "")
				.replaceAll("@VOUCHERDATE", spPdfDto.getVoucherDate() != null ? spPdfDto.getVoucherDate() : "")
				.replaceAll("@PREPAREDBY", spPdfDto.getPreparedBy() != null ? spPdfDto.getPreparedBy() : "");
		return reportBody.replace("\\", "").replaceAll("\t", "");
		}

	public String generateReport(Long quotationIdorPolicy,Long tmpPolicyId, String reportType,Long taggedStatusId) throws IOException {
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

		boolean showLogo = true;
		String reportBody = "";
		String reportStyles = "";
		String htmlContent2 = "</div></body></html>"; // DO NOT DISTRUB THIS LINE

		if (reportType.equals("valuationreport")) {
			reportBody = quotationValuationReport(quotationIdorPolicy,tmpPolicyId,taggedStatusId) + htmlContent2;

			reportStyles = quotationValuationReportStyles();
			showLogo = false;
		}
		if (reportType.equals("claimforward")) {
			showLogo = false;
			reportBody = generatemultipleclaimsforwardingletter() + htmlContent2;

		} else if (reportType.equals("policyBond")) {
			String getPolicyDetail = masterPolicyRepository.getBondDetail(quotationIdorPolicy);

			String[] get = getPolicyDetail.toString().split(",");

			JsonNode mphBasic = null;
			JsonNode mphAdds = null;
			JsonNode mphRep = null;
			JsonNode mphcont = null;

			//
			try {
				URL url = new URL(endPoint
						+ "/api/proposal/getProposalDetailsByProposalNumber?proposalNumber=" + get[3]);
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

				mphBasic = actualObj.path("responseData").path("mphDetails").path("mphBasicDetails");
				mphAdds = actualObj.path("responseData").path("mphDetails").path("mphAddressDetails");
				mphRep = actualObj.path("responseData").path("mphDetails").path("mphChannelDetails");
				mphcont = actualObj.path("responseData").path("mphDetails").path("mphProductDetails");

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//
		
			Long getLCSumAssure=masterQuotationRepository.findByProposalNumberforPolicy(get[3]);
			            
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
				pdfClaimForward = commonMasterUnitRepository.findByUnitCode(get[10]);
			}
		
			pdfClaimForwardLetter = PolicyClaimHelper.claimforward(pdfClaimForward);
			
			PolicyBondDetailDto policyBondDetailDto = PolicyHelper.getPolicyBondDtoDetail(get, mphBasic, mphAdds,mphcont,pdfClaimForwardLetter,
					mphRep, policyLifeCoverRepository.maxRetirement(quotationIdorPolicy),getLCSumAssure);

			reportBody = QuotationHelper.policyBondReport(policyBondDetailDto) + htmlContent2;
			showLogo = false;
			
			
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

		String htmlFileLoc = tempPdfLocation + "/" + quotationIdorPolicy + ".html";

		FileWriter fw = new FileWriter(htmlFileLoc);
		fw.write(completehtmlContent);
		fw.close();

		FileOutputStream fileOutputStream = new FileOutputStream(tempPdfLocation + "/" + quotationIdorPolicy + ".pdf");
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

		return tempPdfLocation + "/" + quotationIdorPolicy + ".pdf";
	}

	private String policyBondReport() {

		String reportBody = "<p>Life Insurance Corporation of India Pension and Group Schemes Department</p>";

		return reportBody.replace("\\", "").replaceAll("\t", "");
	}

@Autowired
RenewalPolicyService renewalPolicyService;
	
	public String quotationValuationReport(Long quotationId, Long tmpPolicyId,Long taggedStatusId) {
		ApiResponseDto<List<QuotationPDFGenerationDto>> getPolicyBondDetail=null;
		if (tmpPolicyId==0) {
			if (taggedStatusId == 79) {
				getPolicyBondDetail = this.generatePDF(quotationId, taggedStatusId);
			} else {
				getPolicyBondDetail = this.generatePDFStaging(quotationId);
			}
		}else {
			getPolicyBondDetail=renewalPolicyService.generateReportRenewalQuotationPDF(tmpPolicyId);
		}
		QuotationPDFGenerationDto data = getPolicyBondDetail.getData().get(0);
	
	//Date of commence start
		
		String getDateOfApproval=DateUtils.dateToStringDDMMYYYY(data.getDateOfApproval());
		String getDateOfCommencement=DateUtils.dateToStringDDMMYYYY(data.getDateOfCommencement());
//		String dateofCommancement=data.getDateOfCommencement().toString();
//		String[] getdateofCommancement=dateofCommancement.split(" ");
					
		String substring = data.getQuotationNumber().substring(Math.max(data.getQuotationNumber().length() - 2, 0));

	Long totalAmountPayable=data.getPastServiceBenefit()+data.getCurrentServiceBenefit()+ data.getLcPremium()+data.getGst();
		String reportBody4 = "     <div class=\"row\">\r\n"

				+ "                    <div class=\"col-md-12\">\r\n"
				+ "                        <h2 class=\"header\"  style=\"text-align:center;\">LIFE INSURANCE CORPORATION OF INDIA</h2><br />"
				+ "                        <h5 style=\"text-align:center;\"></h5>"
				+ "                        <!-- <h5>P&GS DEPT MDO-I</h5> -->"
				+ "                        <h5  style=\"text-align:center;\">"+data.getCommonMasterUnitEntity().getAddress1()+"</h5>\r\n"
				+ "                        <h5  style=\"text-align:center;\">"+data.getCommonMasterUnitEntity().getAddress2()+"</h5>"
				+ "                        <h5  style=\"text-align:center;\">"+data.getCommonMasterUnitEntity().getAddress3()+"</h5>"
				+ "                    </div>" + "                </div>  "

				+ "                <br />"

				
				+ " <table style=\"width: 125%;\">\r\n"
			    		+ " <tr><td> Ref:  "+data.getProposalNumber() + "/" + substring+"</td>\r\n"
			    		+ " <td class=\"alignRight\">Date : "+getDateOfApproval+"</td></tr>\r\n"
			    		+ " </table>"

				+ " <div class=\"row pb-10\">"

				+ "<hr style=\"border: 1px dashed black;\"></hr>"

				+ "                    </div> <div class=\"lines pb-10\"></div>\r\n"
				+ "                    <div class=\"row\">\r\n"
				+ "                        <div class=\"col-md-2\"  style=\"text-align:left;\">Phone No :"
				+ data.getMphMobileNo() + "</div>"
				// + " <div class=\"col-md-2\" >:@telephoneNo</div>\r\n"
				+ "                    </div>\r\n" + "                    <div class=\"row pb-10\">\r\n"
				+ "                        <div class=\"col-md-2\"  style=\"text-align:left;\">Email : "
				+ data.getMphEmail() + "</div>\r\n"
				// + " <div class=\"col-md-10\">:@ emailId</div>\r\n"
				+ "                    </div>" + "<hr style=\"border: 1px dashed black;\"></hr> \r\n"
				+ "                    <div class=\"lines pb-10\"></div>\r\n" + "                    <br />\r\n"
				+ "                    <div class=\"row\">\r\n"
				+ "                        <div class=\"col-md-12\" style=\"text-align:left;\"> " + data.getMphName()
				+ "</div>\r\n" + "                    </div> <br />\r\n" + "                    <div class=\"row\">\r\n"
				+ "                        <div class=\"col-md-12\" style=\"text-align:left;\">" + (data.getMphAddress1()!=null ?data.getMphAddress1():"")
				+ "</div>\r\n" + "                    </div>\r\n" + "                    <div class=\"row\">\r\n"
				+ "                        <div class=\"col-md-12\" style=\"text-align:left;\">" + (data.getMphAddress2()!=null ? data.getMphAddress2():"")
				+ "</div>\r\n" + "                    </div>\r\n" + "                    <div class=\"row\">\r\n"
				+ "                        <div class=\"col-md-12\" style=\"text-align:left;\">" + (data.getMphAddress3()!=null ?data.getMphAddress3():"")
				+ "</div>\r\n" + "                    </div>\r\n" + "                    <div class=\"row\">\r\n"
				+ "                        <div class=\"col-md-12\" style=\"text-align:left;\">" + data.getMphEmail()
				+ "</div>\r\n"

				+ "                    </div> <br /> <br />  <div class=\"row\">\r\n"

				+ "                        <div class=\"pl-10\">Dear Sir/Madam,</div>" + "                    </div>"
				+ "                    <br />  <div class=\"row pb-10\">"
				+ "                        <div class=\"col-md-12 pl-15\">RE: Group Gratuity Scheme of Your Employees</div>"

				+ "<hr style=\"border: 1px dashed black;\"></hr>  "
				+ "                    </div>  <table style=\"width:70%\">\r\n"

				+ " <div class=\"lines pb-10\"></div> "

				+ "  <div class=\"lines pb-10\"></div>\r\n"

				+ "<tr>\r\n" + "<td>1&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Proposal No/Quot. No </td>\r\n" + "<td>:</td>\r\n" + "<td>"
				+ data.getProposalNumber()+"/"+substring+"</td>\r\n" + "</tr>\r\n"

				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;DATE OF COMMENCEMENT </td>\r\n" + "<td>:</td>\r\n" + "<td>"
				+ getDateOfCommencement + "</td>\r\n" + "</tr>\r\n"

				+ " <br></br>"

				+ "<tr>\r\n" + "<td>2&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MEMBERSHIP DATA</td></tr>\r\n" 
				+ "<tr><td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Number Of Members  </td>\r\n" + "<td>:</td>\r\n" + "<td>"
				+ data.getTotalMember() + "</td>\r\n" + "</tr>\r\n"

				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Average age  </td>\r\n" + "<td>:</td>\r\n" + "<td>" +BigDecimal.valueOf(data.getAverageAge()).setScale(2, RoundingMode.UP).doubleValue()
				+ "</td>\r\n" + "</tr>\r\n"

				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Average Monthly Salary  </td>\r\n" + "<td>:</td>\r\n" + "<td>"
				+BigDecimal.valueOf(data.getAvgMonthlySalary()).setScale(2, RoundingMode.UP).doubleValue()
				+ "</td>\r\n" + "</tr>\r\n"

				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Average Past Service </td>\r\n" + "<td>:</td>\r\n" + "<td>"
				+BigDecimal.valueOf(data.getAvgPastService()).setScale(2, RoundingMode.UP).doubleValue()
			    + "</td>\r\n" + "</tr>\r\n"

				+ " <br></br>"  

				+ "<tr>\r\n" + "<td>3&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;VALUATION METHOD </td>\r\n" + "<td>:</td>\r\n" + "<td>" + "Projected Unit Credit Method"
				+ "</td>\r\n" + "</tr>\r\n"

				+ " <br></br>"	

				+ "<tr>\r\n" + "<td>4&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ACTUARIAL ASSUMPTIONS</td>\r\n" +   "</tr>\r\n"

				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Mortality Rate</td>\r\n" + "<td>:</td>\r\n" + "<td>" +"LIC(2006-08) ultimate"
				+ "</td>\r\n" + "</tr>\r\n"

				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Withdrawal Rate </td>\r\n" + "<td>:</td>\r\n" + "<td>" +""+(data.getMinandmaxRate()!= null ? data.getMinandmaxRate():"")+" depending on age"
				+ "</td>\r\n" + "</tr>\r\n"
//				
//				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Mortality Rate : LIC(2006-08) ultimate\r\n"+ "</td>\r\n" +   "</tr>\r\n"
//
//				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Withdrawal Rate : 1% to 3% depending on age\r\n"+ "</td>\r\n" +   "</tr>\r\n"

				
				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Discount Rate </td>\r\n" + "<td>:</td>\r\n" + "<td>" + data.getDiscountRate()+"% p. a."
				+ "</td>\r\n" + "</tr>\r\n"


				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Salary Escalation</td>\r\n" + "<td>:</td>\r\n" + "<td>" + data.getSalaryEscalation().longValue()
				+ "%</td>\r\n" + "</tr>\r\n"

				+ " <br></br>" 

				+ "<tr>\r\n" + "<td>5&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;RESULTS OF VALUATION</td></tr>\r\n" 
				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;a. PV of Past Service Benefit</td>\r\n" + "<td>:</td>\r\n" + "<td>"+data.getPastServiceBenefit()+"</td>\r\n"
				+ "</tr>\r\n"
				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;b. Current Service Cost</td>\r\n" + "<td>:</td>\r\n" + "<td>"+data.getCurrentServiceBenefit()+"</td>\r\n"
				+ "</tr>\r\n"

				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;c. Total Service Gratuity</td>\r\n" + "<td>:</td>\r\n" + "<td>"+data.getTotalServiceGratuity()+"</td>\r\n"
				+ "</tr>\r\n"

				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;d. Accrued Gratuity</td>\r\n" + "<td>:</td>\r\n" + "<td>" + data.getAccuredGratuity()
				+ "</td>\r\n" + "</tr>\r\n"

				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;e. LCSA</td>\r\n" + "<td>:</td>\r\n" + "<td>"+data.getLcas()+"</td>\r\n" + "</tr>\r\n"

				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;f. LC Premium</td>\r\n" + "<td>:</td>\r\n" + "<td>"+data.getLcPremium()+"</td>\r\n" + "</tr>\r\n"

				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;g. GST @18%</td>\r\n" + "<td>:</td>\r\n" + "<td>"+data.getGst()+"</td>\r\n" + "</tr>\r\n"

				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(S Tax + Ec / SB Cess + KK Cess)</td>\r\n" + "<td>:</td>\r\n"
				+ "<td>"+data.getGst()+" + "+0+" + "+0+" </td>\r\n" + "</tr>\r\n"

				+ " <br></br>"
				
				+ "<tr>\r\n" + "<td>6&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;RECOMMENDED CONTRIBUTION RATE</td>\r\n" + "<td>:</td></tr>\r\n" 
				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;a. Initial Contribution (Rs.)</td>\r\n" + "<td>:</td>\r\n" + "<td>"+data.getPastServiceBenefit()+"</td>\r\n"
				+ "</tr>\r\n"

				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;b. Additional Contribution for existing fund</td>\r\n" + "<td>:</td>\r\n"
				+ "<td> "+0+"</td>\r\n" + "</tr>\r\n"

				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;c. Current Service Cost</td>\r\n" + "<td>:</td>\r\n" + "<td>"+data.getCurrentServiceBenefit()+"</td>\r\n"
				+ "</tr>\r\n"

				+ " <br></br>"

				+ "<tr>\r\n" + "<td>7&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total Amount Payable</td>\r\n" + "<td>:</td>\r\n" + "<td>"+totalAmountPayable+"</td>\r\n"
				+ "</tr>\r\n"

				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(6.a + 6.c + 5.f + 5.g)</td></tr>\r\n"

				+ " <br></br>"
				+ " <br></br>"
				+ " <br></br>"
				+ " <br></br>"+ " <br></br>"
				+ " <br></br>"
				
				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Proposal No/Quot. No</td>\r\n" + "<td>:</td>\r\n" + "<td>"
				+ data.getProposalNumber()+"/"+substring+ "</td>\r\n" + "</tr>\r\n"

				+ "<tr>\r\n" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;DATE OF COMMENCEMENT</td>\r\n" + "<td>:</td>\r\n" + "<td>"
				+ getDateOfCommencement+ "</td>\r\n" + "</tr>\r\n"

				+ "</table>"

				+ " <br></br>" + " <br></br>"

				+ "                    <ol>\r\n" + "                        <li class=\"pb-10\">\r\n"

				+ "                            <div class=\"row\">\r\n"

				+ "                            </div>\r\n"

				+ "                            <div class=\"row\">\r\n"
				
				+ "                            </div>"

				+ " </li>   <li class=\"pb-10\">\r\n" + "                            <div class=\"row\">\r\n"
				+ "                            </div>\r\n"

				+ "							\r\n" + "                            <div class=\"row\">\r\n"
				
				+ "                            </div>\r\n" + "\r\n"
				+ "                            <div class=\"row\">\r\n"
				
				+ "                            </div> " + " </li>  <li class=\"pb-10\">\r\n"
				
				+ "							\r\n" + "                            <div class=\"row\">\r\n"
				
				+ "                            </div>\r\n" + "\r\n"
				+ "                            <div class=\"row\">\r\n"
				
				+ "                            </div>\r\n" + "                            <div class=\"row\">\r\n"
				
				+ "                            </div>\r\n" + "                            <div class=\"row\">\r\n"
				
				+ "                            </div>\r\n"

				+ "                        </li> "

				// +"<br /> <br />"

				+ " <li class=\"pb-10\">\r\n" + "                            <div class=\"row\">\r\n"
				
				+ "                            </div>\r\n" + "                        </li> "

				+ "<li class=\"pb-10\">\r\n" + "                            <div class=\"row\">\r\n"
				// + " <div class=\"col-md-6\"> 4. ACTUARIAL ASSUMPTIONS </div>\r\n"
				+ "                            </div>\r\n" + "                            <div class=\"row\">\r\n"
				
				+ "                            </div>\r\n" + "                            <div class=\"row\">\r\n"
				
				+ "                            </div>\r\n" + "                            <div class=\"row\">\r\n"
				
				+ "                            </div>\r\n" + "                            <div class=\"row\">\r\n"
				
				+ "                            </div>\r\n" + "                        </li> \r\n"

				+ "                        <li class=\"pb-10\">\r\n"
				+ "                            <div class=\"row\">\r\n"
				// + " <div class=\"col-md-12\"> 5. RESULTS OF VALUATION</div>\r\n"
				+ "                            </div>\r\n" + "                            <ul>\r\n"
				+ "                                <li style=\"list-style-type:lower-alpha\">\r\n"
				+ "                                    <div class=\"row\">\r\n"
				
				+ "                                    </div>\r\n"
				+ "                                </li>  <li style=\"list-style-type:lower-alpha\">\r\n"
				+ "                                    <div class=\"row\">\r\n"
				
				+ "                                    </div>\r\n" + "                                </li>\r\n"
				+ "                                <li style=\"list-style-type:lower-alpha\">\r\n"
				+ "                                    <div class=\"row\">\r\n"
				
				+ "                                    </div>\r\n"
				+ "                                </li> <li style=\"list-style-type:lower-alpha\">\r\n"
				+ "                                    <div class=\"row\">\r\n"
				
				+ "                                    </div>\r\n" + "                                </li>\r\n"

				+ "                                <li style=\"list-style-type:lower-alpha\">\r\n"
				+ "                                    <div class=\"row\">\r\n"

				+ "                                    </div>\r\n"

				+ "                                </li> "

				+ " <li style=\"list-style-type:lower-alpha\">\r\n"
				+ "                                    <div class=\"row\">\r\n"
			
				+ "                                    </div>\r\n" + "                                </li>\r\n"
				+ "                                <li style=\"list-style-type:lower-alpha\">\r\n"

				+ "                                    <div class=\"row\">\r\n"
				
				+ "                                    </div>\r\n" + "                                </li> "

				+ " <div class=\"row\">\r\n"
				
				+ "                                </div>\r\n"

				+ "                            </ul>\r\n"

				+ "                        </li>    <li class=\"pb-10\">\r\n"
				+ "                            <div class=\"row\">\r\n"

				+ "                            </div>\r\n"

				+ "                            <ul>\r\n"

				+ "                                <li style=\"list-style-type:lower-alpha\">\r\n"

				+ "                                    <div class=\"row\">\r\n"

				+ "                                    </div>\r\n"
				+ "                                </li>  <li style=\"list-style-type:lower-alpha\">\r\n"
				+ "                                    <div class=\"row\">\r\n"
			
				+ "                                    </div>\r\n"
				+ "                                </li> <li style=\"list-style-type:lower-alpha\">\r\n"
				+ "                                    <div class=\"row\">\r\n"
				
				+ "                                    </div>\r\n" + "                                </li>\r\n"
				+ "                            </ul>\r\n"

				+ "                        </li> "

				+ " <li class=\"pb-10\">\r\n" + "                            <div class=\"row\">\r\n"
				
				+ "                            </div>\r\n" + "                            <div class=\"row\">\r\n"
				+ "                            </div>\r\n"

				+ "                        </li>    <div class=\"lines pb-10\"></div>"

				+ "                            <div class=\"row float-rigth\">\r\n"

				+ "                                <div class=\"col-md-5\"></div>\r\n"

				+ "                            </div> <div class=\"row float-rigth\">\r\n"
				+ "                                <div class=\"col-md-5\"></div>\r\n"

				+ "                            </div>\r\n"

				+ "<hr style=\"border: 1px dashed black;\"></hr>  "

				+ " <div class=\"lines pb-10\"></div> "
				+ "                        <div class=\"lines pb-10\"></div>\r\n" + "                        <li>\r\n"
				+ "                            <div class=\"row\">\r\n"
				+ "                                <div class=\"col-md-6\">8&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Benefits Valued</div>"
				+ "                            </div>"

				+ "                        </li>   <div class=\"table-responsive pt-3\">\r\n"

				+ "                    <table class=\"tableborder\"  style=\"width:100%;\">\r\n"

				+ "                        <thead>" + "                            <tr>"
				+ "                                <th style=\"text-align:right;\">Category</th>"
				+ "                                <th style=\"text-align:right;\">NRA</th>\r\n"
				+ "                                <th style=\"text-align:right;\">Gratuity Ceiling </th>"
				+ "                                <th style=\"text-align:right;\">Slab</th>\r\n"
				+ "                                <th style=\"text-align:right;\">Rate</th>\r\n"
				+ "                                <th style=\"text-align:right;\">LCSA Ceiling</th>"
				+ "                                <th style=\"text-align:right;\">RTA TABLE</th>" + "                            </tr>"
				+ "                        </thead>" + "                        <tbody>";

		QuotationPDFGenerationDto quotationPDFGenerationDto = getPolicyBondDetail.getData().get(0);

		for (BenefitValuationDto benefitValuationDto : quotationPDFGenerationDto.getBenefitValuation()) {

			reportBody4 = reportBody4 + "<tr >" 
					+ "<td style=\"text-align:right;\">"+ benefitValuationDto.getCategoryName() + "</td>\r\n" 
					+ "<td style=\"text-align:right;\">"+ benefitValuationDto.getRetirementAge() + "</td>\r\n" 
					+ "<td style=\"text-align:right;\">"+ benefitValuationDto.getGratuityCellingAmount() + "</td>"
					+ "<td style=\"text-align:right;\"> @99</td>"
					+ "<td style=\"text-align:right;\"> "+ benefitValuationDto.getNumberOfDaysWage() + "</td>" 
					+ "<td style=\"text-align:right;\"> "+ benefitValuationDto.getLcas() + "</td>" 
					+ "<td style=\"text-align:right;\"> "+ benefitValuationDto.getRateTable() + "</td>" 
					+ "</tr>";
		}

		reportBody4 = reportBody4 + "</tbody>"

				+ "                    </table>" + "                </div>"

				+ "<hr style=\"border: 1px dashed black;\"></hr>  "
				+ "  </ol>   <div class=\"lines pb-10\"></div><br />\r\n"
				+ "                <div class=\"text pb-10\">\r\n"
				+ "                    Please note that the contribution rate may change in future depending upon the experience of the\r\n"
				+ "                    scheme. It is necessary to carry out the Actuarial Valuation Periodically. It may be noted that\r\n"
				+ "                    the above results are as per the Actuarial Valuation which is based upon certain assumptions about\r\n"
				+ "                    future\r\n"
				+ "                    experience of the scheme. Further, the results\r\n"
				+ "                    are particularly sensitive to the difference between assumed valuation\r\n"
				+ "                    rate of discount and the assumed rate of escalation in salary.\r\n"
				+ "                    The valuation is done on the basis of members data.\r\n"
				+ "                </div>\r\n" + "                <br />\r\n"
				+ "                <div class=\"row pb-10\">\r\n"
				+ "                    <div class=\"col-md-12 text text-bold\">Yours Faithfully,</div>\r\n"
				+ "                </div><br />\r\n" + "                <div class=\"row\">\r\n"
				+ "                    <div class=\"col-md-12 text text-bold\">MANAGER (P&mp;GS)</div>\r\n"
				+ "                </div>";

		 return reportBody4.replace("\\", "").replaceAll("\t", "");

//		return reportBody3;

	}


	private ApiResponseDto<List<QuotationPDFGenerationDto>> generatePDFStaging(Long quotationId) {
	
			String getPdfGeneration = masterMemberRepository.generatePDFDataforStaging(quotationId);
			String minandmaxValuationWithdrawl=valuationWithdrawalRateRepository.findMinAndMax(quotationId);
		
		System.out.print(getPdfGeneration);
		String[] get = getPdfGeneration.toString().split(",");
		List<BenefitValuationDto> addbenefitValuationDto = new ArrayList<BenefitValuationDto>();
		List<QuotationPDFGenerationDto> addQuotationPDFGenerationDto = new ArrayList<QuotationPDFGenerationDto>();
		QuotationEntity quotationEntity = quotationRepository.findById(quotationId).get();
		QuotationDto masterQuotationEntity=new ModelMapper().map(quotationEntity, QuotationDto.class);
		JsonNode mphBasic = null;
		JsonNode mphAdds = null;
		JsonNode mphcont = null;

		try {
			URL url = new URL(endPoint + "/api/proposal/getProposalDetailsByProposalNumber?proposalNumber="
					+ masterQuotationEntity.getProposalNumber());
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

			mphBasic = actualObj.path("responseData").path("mphDetails").path("mphBasicDetails");
			mphAdds = actualObj.path("responseData").path("mphDetails").path("mphAddressDetails");
			mphcont = actualObj.path("responseData").path("mphDetails").path("mphContactDetails");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
			pdfClaimForward = commonMasterUnitRepository.findByUnitCode(quotationEntity.getUnitCode());
		}
	
		pdfClaimForward = PolicyClaimHelper.claimforward(pdfClaimForward);
		
		List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository.findByqstgQuoationId(quotationId);
		List<LifeCoverEntity> masterLifeCoverEntity =lifeCoverEntityRepository
				.findByQuotationId(quotationId);
		List<GratuityBenefitEntity> masterGratuityBenefitEntity = gratuityBenefitRepository
				.findByQuotationId(quotationId);
		


		QuotationPDFGenerationDto newQuotationPDFGenerationDto = new QuotationPDFGenerationDto();
		for (MemberCategoryEntity getMemberCategory : memberCategoryEntity) {
			for (LifeCoverEntity getMasterLifeCoverEntity : masterLifeCoverEntity) {
				for (GratuityBenefitEntity getGratuityBenefitEntity : masterGratuityBenefitEntity) {

					for (GratuityBenefitPropsEntity getMasterGratuityBenefitProps : getGratuityBenefitEntity
							.getGratuityBenefits()) {

						if (getGratuityBenefitEntity.getCategoryId().equals(getMemberCategory.getId())
								&& getMasterLifeCoverEntity.getCategoryId().equals(getMemberCategory.getId())) {

							ValuationBasicEntity getMasterValuationBasicEntity = valuationBasicRepository
									.findByQuotationId(getMemberCategory.getQstgQuoationId()).get();
							ValuationEntity getMasterValuation = valuationRepository
									.findByQuotationId(getMemberCategory.getQstgQuoationId()).get();

							newQuotationPDFGenerationDto.setTotalMember(Long.parseLong(get[0]));
							newQuotationPDFGenerationDto.setAverageAge(Float.parseFloat(get[1]));
							newQuotationPDFGenerationDto.setAvgMonthlySalary(Float.parseFloat(get[2]));
							newQuotationPDFGenerationDto.setAvgPastService(Float.parseFloat(get[3]));
							newQuotationPDFGenerationDto.setTotalServiceGratuity(Long.toString((long)(Double.parseDouble(get[4]))));
							newQuotationPDFGenerationDto.setAccuredGratuity((long)(Double.parseDouble(get[5])));
							newQuotationPDFGenerationDto.setLcas((long)(Double.parseDouble(get[6])));
							newQuotationPDFGenerationDto.setLcPremium((long)(Double.parseDouble(get[7])));
							newQuotationPDFGenerationDto.setGst((long)(Double.parseDouble(get[8])));
							newQuotationPDFGenerationDto.setPastServiceBenefit((long)(Double.parseDouble(get[9])));
							newQuotationPDFGenerationDto.setCurrentServiceBenefit((long)(Double.parseDouble(get[10])));
							newQuotationPDFGenerationDto
									.setSalaryEscalation(getMasterValuation.getSalaryEscalation() * 100);
							newQuotationPDFGenerationDto.setDiscountRate(getMasterValuation.getDiscountRate()*100);
							newQuotationPDFGenerationDto.setMphName(mphBasic.path("mphName").textValue());
							newQuotationPDFGenerationDto.setMphEmail(mphcont.get(0).path("emailID").textValue());
							newQuotationPDFGenerationDto.setMphMobileNo(mphcont.get(0).path("mobileNumber").asLong());
							newQuotationPDFGenerationDto.setMphAddress1(mphAdds.get(0).path("address1").textValue());
							newQuotationPDFGenerationDto.setMphAddress2(mphAdds.get(0).path("address2").textValue());
							newQuotationPDFGenerationDto.setMphAddress3(mphAdds.get(0).path("address3").textValue());
							newQuotationPDFGenerationDto
									.setMphAddressType(mphAdds.get(0).path("addressType").textValue());
							newQuotationPDFGenerationDto.setDateOfApproval(masterQuotationEntity.getCreatedDate());
							newQuotationPDFGenerationDto.setProposalNumber(masterQuotationEntity.getProposalNumber());
							newQuotationPDFGenerationDto.setQuotationNumber(masterQuotationEntity.getNumber());
							newQuotationPDFGenerationDto
									.setDateOfCommencement(masterQuotationEntity.getDateOfCommencement());
							 newQuotationPDFGenerationDto.setMinandmaxRate(minandmaxValuationWithdrawl);
						        System.out.println(newQuotationPDFGenerationDto.getTotalServiceGratuity());
						    
							BenefitValuationDto benefitValuationDto = new BenefitValuationDto();
							benefitValuationDto.setCategoryId(getMemberCategory.getId());
							benefitValuationDto.setCategoryName(getMemberCategory.getName());
							benefitValuationDto.setRetirementAge(getMasterGratuityBenefitProps.getRetirementAge());
							benefitValuationDto.setRateTable(getMasterValuationBasicEntity.getRateTable());
							benefitValuationDto.setLcas(getMasterLifeCoverEntity.getMaximumSumAssured());
							benefitValuationDto
									.setGratuityCellingAmount(getMasterGratuityBenefitProps.getGratuityCellingAmount());
							benefitValuationDto
									.setNumberOfDaysWage(getMasterGratuityBenefitProps.getNumberOfDaysWage());
							benefitValuationDto.setNumberOfWorkingDaysPerMonth(
									getMasterGratuityBenefitProps.getNumberOfWorkingDaysPerMonth());
							addbenefitValuationDto.add(benefitValuationDto);

						}
					}
				}
			}
			newQuotationPDFGenerationDto.setCommonMasterUnitEntity(pdfClaimForward);		
			newQuotationPDFGenerationDto.setBenefitValuation(addbenefitValuationDto);

		}
		addQuotationPDFGenerationDto.add(newQuotationPDFGenerationDto);

		return ApiResponseDto.success(addQuotationPDFGenerationDto);
}

	private String quotationValuationReportStyles() {

		// ApiResponseDto<List<QuotationPDFGenerationDto>> getPolicyBondDetail =
		// this.generatePDF(quotationId);

		return "" + " .tableborder," + " .tableborder tr th,"
				+ " .tableborder tfoot tr td { border-width:1pt; border-style:double; border-right: #FFFFFF; border-left: #FFFFFF; padding:5pt; border-spacing:0pt; border-collapse: collapse; }"
				+ " .tableborder tr td { padding:10pt; }" + " .tdcenteralign { text-align:center; } "
				+ " .showborder { border-width:1pt; border-style:double; padding:5pt; border-spacing:0pt; border-collapse: collapse; }";

	}

	private String generatemultipleclaimsforwardingletter() {

		String reportBody2 = "<p style=\"text-align:center;\">LIFE INSURANCE CORPORATION OF INDIA</p>"
				+ "<p style=\"text-align:center;\">P&mp;GS UNIT, HYDERABAD,</p>"
				+ "<p style=\"text-align:center;\">2nd floor Jeevan Prakash</p>"
				+ "<p style=\"text-align:center;\">Secretariat Road</p>"
				+ "<p style=\"text-align:center;\">Saifabad</p>"
				+ "<p style=\"text-align:center;\"> HYDERABAD - 500 063</p>" + " <br/>"

//				+ "           <div class=\"text\">" + "                    <div class=\"row\">\r\n"
//				+ "           <div class=\"col-md-2\" style=\"text-align:left;\"> Ref :PNGS/U558057/511173/14081                    Date:16/03/2023 </div>"
                +" <hr style=\"border: 1px dashed black;\"></hr> "; 

		String reportBody3 = "    <div class=\"row\">\r\n" 
				+ "                    <div class=\"col-md-12\">\r\n"
				+ "                        <p class=\"header\"  style=\"text-align:left;\">TRUSTESS SINGARENT COLLIERIES CO. LTD., EMP.GRATUITY SC</p><br />"
				+ "                        <p style=\"text-align:left;\">P O Kothagudem </p>"
				+ "                        <p style=\"text-align:left;\">KOTHGEDEM COLLIERIES</p>\r\n"
				+ "                        <p style=\"text-align:left;\">PIN 507 101</p>"
				+ "                        <br />" 
				+ "                        <p style=\"text-align:left;\">507 101</p>"
				+ "                    </div>" 
				+ "                </div>  "

				+ "                <br />"

				+ "<p style=\"text-align:left;\">Dear Sir / Madam</p>"

				+ "                <br />"

				+ "<p style=\"text-align:left;\">Reg :      Claim under Master policy no 511173 .</p>";

		String reportBody4 = "    <div class=\"row\">\r\n" + "                  "
				+ "                        <div class=\"col-md-12\">\r\n"
				+ "                        <p style=\"text-align:left;\">We are forwarding cheque no _________/crediting to your Bank</p>"
				+ "                        <p style=\"text-align:left;\">Acc No. 52131916722 of STATE BANK OF INDIA. </p>"
				+ "                        <p  style=\"text-align:left;\">IFSC Code : SBIN0020160</p>\r\n"
				+ "                    </div>" 
				+ "                </div> "

				+ "                <br />"

				+ "<p style=\"text-align:left;\">The amounts are as per particulars listed below</p>"
				+" <hr style=\"border: 1px dashed black;\"></hr> ";

		
		
		String table = 
					"<table class=\"tableborder\"  style=\"width:100%;\">"
				+ "<tr>" 
				+ "<th class=\"tdrightalign\">LIC ID</th>" 
				+ "<th class=\"tdrightalign \">Emp Name</th>"
				+ "<th class=\"tdrightalign \">EMP NO </th>" 
				+ "<th class=\"tdrightalign \">LCSA</th>"
				+ "<th class=\"tdrightalign \">SV/Mat/Wthd</th>" 
				+ "<th class=\"tdrightalign \">REFUND</th>"
				+ "<th class=\"tdrightalign \">OTH AMT</th>" 
				+ "<th class=\"tdrightalign \">TOTAL</th>"
				+ "</tr>"
				+ "</table>"
				+"<hr style=\"border: 1px dashed black;\"></hr>";
		
		table +=  "<table class=\"tableborder\"  style=\"width:100%;\">";

		String detailTable = "";
		Double total = 0.0;
		PdfGeneratorDto pdfGeneratorDto = new PdfGeneratorDto();

		List<PdfGeneratorForTableDto> getpdfGenerate = new ArrayList<PdfGeneratorForTableDto>();

		PdfGeneratorForTableDto get1 = new PdfGeneratorForTableDto();
		get1.setLicId("22382");
		get1.setEmpName("Jumoju Prabhakar");
		get1.setEmpCode("905315");
		get1.setLcSumAssured(0.0);
		get1.setSvMatWthd(0.0);
		get1.setRefund(0.0);
		get1.setOthAmt(0.0);
		get1.setTotal(1668070.00);

		getpdfGenerate.add(get1);

		PdfGeneratorForTableDto get2 = new PdfGeneratorForTableDto();
		get2.setLicId(" 29238");
		get2.setEmpName("Durgam Venkateshwarlu");
		get2.setEmpCode("1000485");
		get2.setLcSumAssured(0.0);
		get2.setSvMatWthd(0.0);
		get2.setRefund(0.0);
		get2.setOthAmt(0.0);
		get2.setTotal(1573312.00);

		getpdfGenerate.add(get2);

		pdfGeneratorDto.setPdfGeneratorForTable(getpdfGenerate);
		for (PdfGeneratorForTableDto pdfGeneratorForTableDto : pdfGeneratorDto.getPdfGeneratorForTable()) {

			detailTable += "<tr>"
					+ "<td class=\"tdrightalign\">" + pdfGeneratorForTableDto.getLicId() + "</td>"
					+ "<td class=\"tdrightalign\">" + pdfGeneratorForTableDto.getEmpName() + "</td>"
					+ "<td class=\"tdrightalign\">" + pdfGeneratorForTableDto.getEmpCode()+ "</td>"
					+ "<td class=\"tdrightalign\">" + pdfGeneratorForTableDto.getLcSumAssured() + "</td>"
					+ "<td class=\"tdrightalign\">" + pdfGeneratorForTableDto.getSvMatWthd() + "</td>"
					+ "<td class=\"tdrightalign\">" + pdfGeneratorForTableDto.getRefund()+ "</td>"
					+ "<td class=\"tdrightalign\">" + pdfGeneratorForTableDto.getOthAmt() + "</td>"
					+ "<td class=\"tdrightalign\">" + pdfGeneratorForTableDto.getTotal() + "</td>" 
					+ "</tr>";
			
			total = total + pdfGeneratorForTableDto.getTotal();
	
		}
		
		detailTable += "<tr >"
				+ "   <th >TOTAL: </th>"
				+ "    <th >LCSA:2263710 </th>"
				+ "     <th >SV/MAT/WD:11400128</th>"
				+ "      <th>Refnd : 0</th>"
				+ "       <th>Other: 0</th>"
				+ "     <th>Total:"+total+"</th>"
				+ "</tr>" ;
		
		detailTable += "<tfoot>" 
					+ "<tr></tr>" 
					+ "</tfoot>" 
					+ "</table>"
					+"<hr style=\"border: 1px dashed black;\"></hr>"					
					+"<p style=\"text-align:right;\">Yours Faithfully</p>"							
					+"<p style=\"text-align:right;\"> Manager (PNGS)</p>";


		return reportBody2 + reportBody3 + reportBody4+table+detailTable;
	}
	
	@Override
	public ApiResponseDto<List<QuotationPDFGenerationDto>> generatePDF(Long quotationId, Long taggedStatusId) {
		String getPdfGeneration=null;
		String minandmaxValuationWithdrawl=null;
	
		 getPdfGeneration = masterMemberRepository.generatePDFData(quotationId);
		 minandmaxValuationWithdrawl=masterValuationWithdrawalRateRepository.findMinAndMax(quotationId);
		
		System.out.print(getPdfGeneration);
		String[] get = getPdfGeneration.toString().split(",");
		List<BenefitValuationDto> addbenefitValuationDto = new ArrayList<BenefitValuationDto>();
		List<QuotationPDFGenerationDto> addQuotationPDFGenerationDto = new ArrayList<QuotationPDFGenerationDto>();
		MasterQuotationEntity newmasterQuotationEntity = masterQuotationRepository.findById(quotationId).get();
		QuotationDto masterQuotationEntity=new ModelMapper().map(newmasterQuotationEntity, QuotationDto.class);
		JsonNode mphBasic = null;
		JsonNode mphAdds = null;
		JsonNode mphcont = null;
		
		JsonNode actualobj = commonModuleService.getProposalNumber(masterQuotationEntity.getProposalNumber());
		mphBasic =	actualobj.path("mphDetails").path("mphBasicDetails");
		mphcont = actualobj.path("mphDetails").path("mphContactDetails");
		mphAdds = actualobj.path("mphDetails").path("mphAddressDetails");        
 
		
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
			pdfClaimForward = commonMasterUnitRepository.findByUnitCode(newmasterQuotationEntity.getUnitCode());
		}
	
		pdfClaimForward = PolicyClaimHelper.claimforward(pdfClaimForward);

		List<MemberCategoryEntity> memberCategoryEntity = memberCategoryRepository.findByqmstQuotationId(quotationId);
		List<MasterLifeCoverEntity> masterLifeCoverEntity = masterLifeCoverEntityRepository
				.findByQuotationId(quotationId);
		List<MasterGratuityBenefitEntity> masterGratuityBenefitEntity = masterGratuityBenefitRepository
				.findByQuotationId(quotationId);
		QuotationPDFGenerationDto newQuotationPDFGenerationDto = new QuotationPDFGenerationDto();
		for (MemberCategoryEntity getMemberCategory : memberCategoryEntity) {
			for (MasterLifeCoverEntity getMasterLifeCoverEntity : masterLifeCoverEntity) {
				for (MasterGratuityBenefitEntity getGratuityBenefitEntity : masterGratuityBenefitEntity) {

					for (MasterGratuityBenefitPropsEntity getMasterGratuityBenefitProps : getGratuityBenefitEntity
							.getGratuityBenefits()) {

						if (getGratuityBenefitEntity.getCategoryId().equals(getMemberCategory.getId())
								&& getMasterLifeCoverEntity.getCategoryId().equals(getMemberCategory.getId())) {

							MasterValuationBasicEntity getMasterValuationBasicEntity = masterValuationBasicRepository
									.findByQuotationId(getMemberCategory.getQmstQuotationId()).get();
							MasterValuationEntity getMasterValuation = masterValuationRepository
									.findByQuotationId(getMemberCategory.getQmstQuotationId()).get();						
							
							newQuotationPDFGenerationDto.setTotalMember(Long.parseLong(get[0]));
							newQuotationPDFGenerationDto.setAverageAge(Float.parseFloat(get[1]));
							newQuotationPDFGenerationDto.setAvgMonthlySalary(Float.parseFloat(get[2]));
							newQuotationPDFGenerationDto.setAvgPastService(Float.parseFloat(get[3]));
							newQuotationPDFGenerationDto.setTotalServiceGratuity(Long.toString((long)(Double.parseDouble(get[4]))));
							newQuotationPDFGenerationDto.setAccuredGratuity((long)(Double.parseDouble(get[5])));
							newQuotationPDFGenerationDto.setLcas((long)(Double.parseDouble(get[6])));
							newQuotationPDFGenerationDto.setLcPremium((long)(Double.parseDouble(get[7])));
							newQuotationPDFGenerationDto.setGst((long)(Double.parseDouble(get[8])));
							newQuotationPDFGenerationDto.setPastServiceBenefit((long)(Double.parseDouble(get[9])));
							newQuotationPDFGenerationDto.setCurrentServiceBenefit((long)(Double.parseDouble(get[10])));
							newQuotationPDFGenerationDto
									.setSalaryEscalation(getMasterValuation.getSalaryEscalation() * 100);
							newQuotationPDFGenerationDto.setDiscountRate(getMasterValuation.getDiscountRate()*100);
							if(mphBasic!=null) {
							newQuotationPDFGenerationDto.setMphName(mphBasic.path("mphName").textValue());
							}else {
							newQuotationPDFGenerationDto.setMphName("");
							}
							newQuotationPDFGenerationDto.setMphEmail(mphcont.get(0).path("emailID").textValue());
							newQuotationPDFGenerationDto.setMphMobileNo(mphcont.get(0).path("mobileNumber").asLong());
							newQuotationPDFGenerationDto.getMphAddress1();
							newQuotationPDFGenerationDto.setMphAddress1(mphAdds.get(0).path("address1").textValue());
							newQuotationPDFGenerationDto.setMphAddress2(mphAdds.get(0).path("address2").textValue());
							newQuotationPDFGenerationDto.setMphAddress3(mphAdds.get(0).path("address3").textValue());
							newQuotationPDFGenerationDto
									.setMphAddressType(mphAdds.get(0).path("addressType").textValue());
							newQuotationPDFGenerationDto.setDateOfApproval(masterQuotationEntity.getApprovalDate());
							newQuotationPDFGenerationDto.setProposalNumber(masterQuotationEntity.getProposalNumber());
							newQuotationPDFGenerationDto.setQuotationNumber(masterQuotationEntity.getNumber());
							newQuotationPDFGenerationDto
									.setDateOfCommencement(masterQuotationEntity.getDateOfCommencement());
						        System.out.println(newQuotationPDFGenerationDto.getTotalServiceGratuity());
						    newQuotationPDFGenerationDto.setMinandmaxRate(minandmaxValuationWithdrawl);
							BenefitValuationDto benefitValuationDto = new BenefitValuationDto();
							benefitValuationDto.setCategoryId(getMemberCategory.getId());
							benefitValuationDto.setCategoryName(getMemberCategory.getName());
							benefitValuationDto.setRetirementAge(getMasterGratuityBenefitProps.getRetirementAge());
							benefitValuationDto.setRateTable(getMasterValuationBasicEntity.getRateTable());
							benefitValuationDto.setLcas(getMasterLifeCoverEntity.getMaximumSumAssured());
							benefitValuationDto
									.setGratuityCellingAmount(getMasterGratuityBenefitProps.getGratuityCellingAmount());
							benefitValuationDto
									.setNumberOfDaysWage(getMasterGratuityBenefitProps.getNumberOfDaysWage());
							benefitValuationDto.setNumberOfWorkingDaysPerMonth(
									getMasterGratuityBenefitProps.getNumberOfWorkingDaysPerMonth());
							addbenefitValuationDto.add(benefitValuationDto);

						}
					}
				}
			}
			newQuotationPDFGenerationDto.setCommonMasterUnitEntity(pdfClaimForward);		
			newQuotationPDFGenerationDto.setBenefitValuation(addbenefitValuationDto);

		}
		addQuotationPDFGenerationDto.add(newQuotationPDFGenerationDto);

		return ApiResponseDto.success(addQuotationPDFGenerationDto);
	}
	
	@Override
	public String getcandbsheetpdf(Long quotationId, Long taggedStatusId) throws IOException {
			

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
		List<Object[]> quotationMasterData =null;
		if(taggedStatusId == 79){
			quotationMasterData= memberHelper.getQuotationMasterDataForExcel(quotationId);
		}
		if(taggedStatusId == 78) {
			quotationMasterData= memberHelper.getQuotationStagingDataForExcel(quotationId);
		}
		generateCBSheetPdfDto = QuotationHelper.getcbObjtoDto(quotationMasterData);
		
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
				+ " table thead tr td { padding-top:5px; padding-bottom:5px; border-bottom: 1px dashed #000000; border-top: 1px dashed #000000;}"
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

		String htmlFileLoc = tempPdfLocation + "/" + quotationId+ ".html";
		FileWriter fw = new FileWriter(htmlFileLoc);
		fw.write(completehtmlContent);
		fw.close();

		FileOutputStream fileOutputStream = new FileOutputStream(tempPdfLocation + "/" + quotationId+ ".pdf");
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

		return tempPdfLocation + "/" + quotationId + ".pdf";
	}

			private String generatecbsheet(List<GenerateCBSheetPdfDto> generateCBSheetPdfDtos) {
				
//				List<GenerateCBSheetPdfDto> generateCBSheetPdfDtos = this.getDummyData();

				int pageno = 1;
				String reportBody = this.topPdf(generateCBSheetPdfDtos.get(0), pageno);
				pageno++;
				
				String table = 
						        
								"<table class=\"tableborder\"  style=\"width:100%;\">" 
								+"<thead>"
								+ "<tr style=\"text-align:right\">"
								+ "<td style=\"text-align:left\">LIC ID</td>" + "<td style=\"text-align:left\">EMP NO</td>"
								+ "<td style=\"text-align:left\">NAME</td>" + "<td>CAT.</td>"
								+ "<td>DOB</td>" + "<td>DOA</td>"
								+ "<td>AGE</td>" + "<td>SALARY</td>"
								+ "<td>P S</td>" + "<td>T S</td>"
								+ "<td>TOT GRTY</td>" + "<td>ACC GRTY</td>"
								+ "<td>LIFE COVER</td>" + "<td>LCP</td>"
								+ "<td>PSB</td>" + "<td>CSB</td>"
								+ "<td></td>"
								+ "</tr>"
								+"</thead>";
												

				String detailTable = "";
				String hr = "<hr style=\"border: 1px dashed black;\"></hr>";
//				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
				Double averageAge=0.0;
				Double averageMonthlySalary=0.0;
				Double averagePastService =0.0;
				
				
				//Total
				Double totalMonthlySalary=0.0;
				Double totalTotalGratutity = 0.0;
				Double totalAccGratuity = 0.0;
				Double totalLifeCover = 0.0;
				Double totalLcp = 0.0;
				Double totalPsb = 0.0;
				Double totalCsb = 0.0;
					
				int i = 1;
				for (GenerateCBSheetPdfDto get : generateCBSheetPdfDtos ) {
					detailTable +=
							"<tr style=\"text-align:right\">"
							+ "<td>"+ get.getLicId()+"</td>" + "<td style=\"text-align:left\">"+ get.getEmployeeNo()+"</td>"
							+ "<td style=\"text-align:left\">"+ get.getName()+" </td>" + "<td>"+ get.getCategory()+"</td>"
							+ "<td>"+ get.getDob() +"</td>" + "<td>"+ get.getDoa()+"</td>"
							+ "<td>"+ get.getAge()+"</td>" + "<td>"+ get.getSalary()+"</td>"
							+ "<td>"+ get.getPastService()+"</td>" + "<td>"+ get.getTotalService()+"</td>"
							+ "<td>"+ get.getTotalServiceGratuity().intValue()+"</td>" + "<td>"+ get.getPastServiceGratuity().intValue()+"</td>"
							+ "<td>"+ get.getLifeCover().intValue()+"</td>" + "<td>"+ get.getLifeCoverPremium().intValue()+"</td>"
						    + "<td>"+ get.getPastServiceBenefit().intValue()+"</td>" + "<td>"+ get.getCurrentServiceBenefit().intValue()+"</td>"
							+ "</tr>";
					 
					averageAge = averageAge+get.getAge().doubleValue()/generateCBSheetPdfDtos.size();
					averageMonthlySalary =averageMonthlySalary+  get.getSalary()/generateCBSheetPdfDtos.size();
					averagePastService =averagePastService+get.getPastService().doubleValue()/generateCBSheetPdfDtos.size();
					
					//list<dto> amount - Double
					//Double cummulativeTotal = cummulativeTotal + amount
					//printing... amount.IntegerValue() ..... cummulativeTotal.IntegerValue)
					
					
					
					//Calculate Total
					totalMonthlySalary = totalMonthlySalary + Double.valueOf(get.getSalary());
					totalTotalGratutity = totalTotalGratutity+Double.valueOf(get.getTotalServiceGratuity());
					totalAccGratuity = totalAccGratuity +Double.valueOf( get.getPastServiceGratuity());
					totalLifeCover = totalLifeCover+Double.valueOf(get.getLifeCover());
					totalLcp = totalLcp + Double.valueOf(get.getLifeCoverPremium());
					totalPsb = totalPsb +Double.valueOf(get.getPastServiceBenefit());
					totalCsb = totalCsb +Double.valueOf(get.getCurrentServiceBenefit());
					
					
					if (i % 35== 0 && generateCBSheetPdfDtos.size() != i)
						detailTable += "</table>" + this.topPdf(get, pageno++) + table;
					i++;
					
					if (i == generateCBSheetPdfDtos.size()+1) {
						detailTable +=
								"<tr style=\"text-align:right\">"
								+ "<td></td>"	
								+ "<td style=\"text-align:left\"></td>"
								+ "<td style=\"font-weight:bold; text-align:left\">Average</td>" + "<td></td>"
								+ "<td></td>" + "<td></td>"
								+ "<td style=\"font-weight:bold\">"+BigDecimal.valueOf(averageAge).setScale(2, RoundingMode.UP).doubleValue() +"</td>" + "<td style=\"font-weight:bold\">"+ BigDecimal.valueOf(averageMonthlySalary).setScale(2, RoundingMode.UP).doubleValue() +"</td>"
								+ "<td style=\"font-weight:bold\">"+ BigDecimal.valueOf(averagePastService).setScale(2, RoundingMode.UP).doubleValue()  +"</td>" + "<td style=\"text-align:right\"></td>"
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
								+ "<td style=\"font-weight:bold\">"+ Math.round(totalMonthlySalary) +"</td>"
								+ "<td></td>" 
							    + "<td style=\"text-align:right\"></td>" 
								+ "<td style=\"font-weight:bold\">"+Math.round(totalTotalGratutity)+"</td>"  
								+ "<td style=\"font-weight:bold\">"+Math.round(totalAccGratuity)+"</td>" 
								+ "<td style=\"font-weight:bold\">"+ Math.round(totalLifeCover) +"</td>" 
								+ "<td style=\"font-weight:bold\">"+ Math.round(totalLcp)   +"</td>" 
							    + "<td style=\"font-weight:bold\">"+Math.round(totalPsb)+"</td>" 
								+ "<td style=\"font-weight:bold\">"+Math.round(totalCsb)+"</td>"
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
				AccountingIntegrationRequestDto accountingIntegrationRequestDto = new AccountingIntegrationRequestDto();
				accountingIntegrationRequestDto.setProposalNumber(generateCBSheetPdfDtos.getProposalNumber());
				
				SuperAnnuationResponseModel get=accountingIntegrationService.getMphAndIcodeDetails(accountingIntegrationRequestDto);
				String cust = get.getMphName();
			    if(cust == null) {
			    	cust = "";
			    }else {
			    	cust = get.getMphName();
			    }
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
				+"<td style=\"text-align:center\">Customer Name : "+ cust  +" </td>"
				+"</tr>"
				+"<tr>"
				+"<td>Date of commencement : "+generateCBSheetPdfDtos.getAnnualRenewlDate()+" </td>"
				+"</tr>"
				+"<p>&nbsp;</p>"
				+"<tr>"
				+"<td>Quotation No :"+ generateCBSheetPdfDtos.getQuotationNumber()+" </td>"
				+"</tr>"
				+"</table>";

			}
	
}
