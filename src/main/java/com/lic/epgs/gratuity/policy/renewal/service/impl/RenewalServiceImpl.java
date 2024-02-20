package com.lic.epgs.gratuity.policy.renewal.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.policy.dto.GenerateCBSheetPdfDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.helper.PolicyHelper;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberDto;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberSearchDto;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.StagingPolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.helper.PolicyMemberHelper;
import com.lic.epgs.gratuity.policy.renewal.dto.RenewalPolicySearchDto;
import com.lic.epgs.gratuity.policy.renewal.helper.RenewalHelper;
import com.lic.epgs.gratuity.policy.renewal.service.RenewalService;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.helper.RenewalPolicyTMPMemberHelper;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.repository.RenewalPolicyTMPRepository;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;
import com.lic.epgs.gratuity.quotation.helper.QuotationHelper;
import com.lic.epgs.gratuity.quotation.member.helper.MemberErrorWorkbookHelper;
import com.lowagie.text.DocumentException;


@Service
public class RenewalServiceImpl implements RenewalService {
	
	@Autowired
	private RenewalPolicyTMPRepository renewalPolicyTMPRepository;
	
	@Autowired
	private MasterPolicyCustomRepository masterPolicyCustomRepository;
	
	@Autowired
	private MPHRepository mPHRepository;
	
	@Autowired
	private RenewalPolicyTMPMemberRepository renewalPolicyTMPMemberRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private MasterPolicyRepository masterPolicyRepository;
	
	@Value("${temp.pdf.location}")
	private String tempPdfLocation;
	

	@Override
	public byte[] downloadSampleRenewal(Long id) {
		 
         
		 MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository.findById(id);
		
		
		byte[] bytes = RenewalHelper.RenewalDocument(masterPolicyEntity);
		
		
		
		return bytes;
	}

	@Override
	public byte[] downloadSampleDynamic(Long id)  {
		
		 MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository.findById(id);
		
		
		byte[] bytes = RenewalHelper.RenewalDocumentDynamic(masterPolicyEntity);
		return bytes;
	}

	@Override
	public ApiResponseDto<List<PolicyDto>> fetchPolicyForNewRenewalQuotation(RenewalPolicySearchDto renewalPolicySearchDto) {
		if(renewalPolicySearchDto.getPolicyNumber().isEmpty()) {
			List<MasterPolicyEntity> fetchpolicyDetails = masterPolicyCustomRepository.fetchpolicyDetails();
			
			return ApiResponseDto.success(fetchpolicyDetails.stream().map(PolicyHelper::entityToDto).collect(Collectors.toList()));	
		}
		else {	
			List<MasterPolicyEntity> checkPolicyNoIsAlreadyProcessorNot = masterPolicyCustomRepository.findByPolicyNumberValidation(renewalPolicySearchDto.getPolicyNumber());
		  
			if(checkPolicyNoIsAlreadyProcessorNot.isEmpty()) {
				return ApiResponseDto.errorMessage(null, null, "Renewals Not eliglible");
			}
			
			List<MasterPolicyEntity> fetchpolicyDetails = masterPolicyCustomRepository.findByPolicyNumber(renewalPolicySearchDto.getPolicyNumber());
		if(fetchpolicyDetails.size()>0) {
			return ApiResponseDto.success(fetchpolicyDetails.stream().map(PolicyHelper::entityToDto).collect(Collectors.toList()));
		}
		else {
			return ApiResponseDto.errorMsg(null, null, "Renewals Already Processed for this Policy");
		}
		
		}
		
	}

	
	@Override
	public String getcandbsheetpdf(Long tmpPolicyId) throws IOException {
		
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
		
			quotationMasterData= renewalPolicyTMPRepository.findBytmpPolicyId(tmpPolicyId);
			
		generateCBSheetPdfDto = QuotationHelper.getcbObj(quotationMasterData);
		
		boolean showLogo = false;
		String reportBody = "";
		String reportStyles = "";
		String htmlContent2 = "</div></body></html>";
		
		reportBody = generatecbsheet(generateCBSheetPdfDto) + htmlContent2;

		String completehtmlContent = "<!DOCTYPE  html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><title>LIC ePGS</title><meta name=\"author\" content=\"LIC PNGS\"/><meta name=\"keywords\" content=\"Customer Communication\"/><meta name=\"description\" content=\"Report/Letter/Contract\"/>"
				+ "<style type=\"text/css\"> @page{margin:45pt 15pt 45pt 15pt;} body{border-width:0px;\r\n"
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
				+ " table thead tr td {padding-top:5px; padding-bottom:5px; border-bottom: 1px dashed #000000; border-top: 1px dashed #000000;}"
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

		String htmlFileLoc = tempPdfLocation + "/" + tmpPolicyId+ ".html";
		FileWriter fw = new FileWriter(htmlFileLoc);
		fw.write(completehtmlContent);
		fw.close();

		FileOutputStream fileOutputStream = new FileOutputStream(tempPdfLocation + "/" + tmpPolicyId+ ".pdf");
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

		return tempPdfLocation + "/" + tmpPolicyId + ".pdf";
	}

			private String generatecbsheet(List<GenerateCBSheetPdfDto> generateCBSheetPdfDtos) {
				
//				List<GenerateCBSheetPdfDto> generateCBSheetPdfDtos = this.getDummyData();

				int pageno = 1;
				String reportBody = this.topPdf(generateCBSheetPdfDtos.get(0), pageno);
				pageno++;
				
				String table = 
						        
								"<table class=\"tableborder\"  style=\"width:100%;\">" 
								+"<thead>"
								+ "<tr>"
								+ "<td>LIC ID</td>" + "<td>EMP NO</td>"
								+ "<td>NAME</td>" + "<td>CAT.</td>"
								+ "<td>DOB</td>" + "<td>DOA</td>"
								+ "<td>AGE</td>" + "<td>SALARY</td>"
								+ "<td class=\"tdrightalign \">P S</td>" + "<td class=\"tdrightalign \">T S</td>"
								+ "<td class=\"tdrightalign \">TOT GRTY</td>" + "<td class=\"tdrightalign \">ACC GRTY</td>"
								+ "<td class=\"tdrightalign \">LIFE COVER</td>" + "<td class=\"tdrightalign \">LCP</td>"
								+ "</tr>"
								+"</thead>";
								
				
				String detailTable = "";
//				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
				Double averageAge=0.0;
				Double averageMonthlySalary=0.0;
				Double averagePastService =0.0;
				
				int i = 1;
				for (GenerateCBSheetPdfDto get : generateCBSheetPdfDtos ) {
					detailTable +=
							"<tr>"
							+ "<td>"+ get.getLicId()+"</td>" + "<td>"+ get.getEmployeeNo()+"</td>"
							+ "<td>"+ get.getName()+" </td>" + "<td>"+ get.getCategory()+"</td>"
							+ "<td>"+ get.getDob() +"</td>" + "<td>"+ get.getDoa()+"</td>"
							+ "<td>"+ get.getAge()+"</td>" + "<td>"+ get.getSalary()+"</td>"
							+ "<td style=\"text-align:left\">"+ get.getPastService()+"</td>" + "<td style=\"text-align:right\">"+ get.getTotalService()+"</td>"
							+ "<td style=\"text-align:left\">"+ get.getTotalServiceGratuity()+"</td>" + "<td style=\"text-align:right\">"+ get.getPastServiceGratuity()+"</td>"
							+ "<td style=\"text-align:left\">"+ get.getLifeCover()+"</td>" + "<td style=\"text-align:right\">"+ get.getLifeCoverPremium()+"</td>"
							+ "</tr>";
					
					averageAge = averageAge+get.getAge().doubleValue()/generateCBSheetPdfDtos.size();
					averageMonthlySalary =averageMonthlySalary+  get.getSalary()/generateCBSheetPdfDtos.size();
					averagePastService =averagePastService+get.getPastService().doubleValue()/generateCBSheetPdfDtos.size();
					
					if (i % 60 == 0 && generateCBSheetPdfDtos.size() != i)
						detailTable += "</table>" + this.topPdf(get, pageno++) + table;
					i++;
			
				if (i == generateCBSheetPdfDtos.size()+1) {
					detailTable +=
							"<tr>"
							+ "<td></td>" + "<td></td>"
							+ "<td></td>" + "<td></td>"
							+ "<td></td>" + "<td></td>"
							+ "<td style=\"font-weight:bold\">"+ BigDecimal.valueOf(averageAge).setScale(2, RoundingMode.UP).doubleValue() +"</td>" + "<td style=\"font-weight:bold\">"+ BigDecimal.valueOf(averageMonthlySalary).setScale(2, RoundingMode.UP).doubleValue() +"</td>"
							+ "<td style=\"font-weight:bold\">"+ BigDecimal.valueOf(averagePastService).setScale(2, RoundingMode.UP).doubleValue() +"</td>" + "<td style=\"text-align:right\"></td>"
							+ "<td style=\"font-weight:bold\"></td>" + "<td style=\"text-align:right\"></td>"
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
				+"<td>Date of commencement : "+generateCBSheetPdfDtos.getAnnualRenewlDate()+" </td>"
				+"</tr>"
				+"<p>&nbsp;</p>"
				+"<tr>"
				+"<td>Quotation No :"+ generateCBSheetPdfDtos.getQuotationNumber()+" </td>"
				+"</tr>"
				+"</table>";

			}

			@Override
			public byte[] findBytmpPolicyId(Long tmpPolicyId) {
				List<Object[]> renewalQuotationMasterData = renewalPolicyTMPMemberRepository.findByTmpPolicyID(tmpPolicyId);
				new ArrayList<GenerateCBSheetPdfDto>();
				List<Object[]> quotationMasterData =null;
				
					quotationMasterData= renewalPolicyTMPRepository.findBytmpPolicyId(tmpPolicyId);

				QuotationHelper.getcbObj(quotationMasterData);
				
				Workbook workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Sheet1");
				MemberErrorWorkbookHelper memberErrorWorkbookHelper = new MemberErrorWorkbookHelper();
				memberErrorWorkbookHelper.createHeaderForCandBSheet(workbook, sheet, false);
				memberErrorWorkbookHelper.createDetailRow(workbook, sheet, renewalQuotationMasterData);

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				try {
					workbook.write(out);
					out.close();
					return out.toByteArray();
				} catch (IOException e) {
					return null;
				}	
			}

			@Override
			public String renewalNoticepdf(Long id) throws IOException {
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
//				List<AgeValuationReportDto> ageValuationReportDto = new ArrayList<AgeValuationReportDto>();
//				List<Object[]> getAgeReport = policyRepository.getAgeReport(masterpolicyId);

//				ageValuationReportDto = PolicyHelper.valuationObjtoDto(getAgeReport);
				boolean showLogo = false;
				String reportBody = "";
				String reportStyles = "";
				String htmlContent2 = "</div></body></html>";
				
				reportBody = renewalnoticepdf(id) + htmlContent2;

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

				String htmlFileLoc = tempPdfLocation + "/" + id + ".html";
				FileWriter fw = new FileWriter(htmlFileLoc);
				fw.write(completehtmlContent);
				fw.close();

				FileOutputStream fileOutputStream = new FileOutputStream(tempPdfLocation + "/" + id + ".pdf");
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

				return tempPdfLocation + "/" + id + ".pdf";
			}

			private String renewalnoticepdf(Long id) {
				
				MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository.findById(id);
								
				String reportBody=null;
				MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.getId()).get();
				Set<MPHAddressEntity>  get= mPHEntity.getMphAddresses();
				for(MPHAddressEntity mPHAddressEntity :get) {
					Date annualDate =masterPolicyEntity.getAnnualRenewlDate();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  
					String dateString = sdf.format(annualDate);
				if(mPHAddressEntity.getAddressLine1() !=null || mPHAddressEntity.getAddressLine2()!=null || mPHAddressEntity.getAddressLine3() !=null) {
				
				 reportBody =	"<p style=\"text-align:center;\">Life Insurance Corporation of India</p>"
									+"<p style=\"text-align:center;\">P &amp; GS DEPARTMENT,DELHI DO I</p>"
									+"<p style=\"text-align:center;\">Pension and Group Schemes Department</p>"
									+"<p style=\"text-align:center;\">6TH &amp; 7th FLOOR, JEEVAN PRAKASH,</p>"
									+"<p style=\"text-align:center;\">25 K G MARG, NEW DELHI- 110001</p>"
									+"<p> &nbsp; </p>"
									 +"<table style=\"width: 120%;\">\r\n"
									    + "  <tr><td>Ref : P&amp;GS/ GGS/"+masterPolicyEntity.getPolicyNumber()+"</td>"
									    + "  <td class=\"alignRight\">Date :"+ DateUtils.dateToStringDDMMYYYY(new Date())+"</td></tr>"
									    + "</table>"
										+"<p> &nbsp; </p>"
										+"<p style=\"text-align:left;\">"+mPHAddressEntity.getAddressLine1()+"</p>"
										+"<p style=\"text-align:left;\">"+mPHAddressEntity.getAddressLine2()+"</p>"
										+"<p style=\"text-align:left;\">"+mPHAddressEntity.getAddressLine3()+"</p>"
										+"<p style=\"text-align:left;\"></p>"
										+"<p style=\"text-align:left;\"></p>"
										+"<p> &nbsp; </p>"
										+"<p> &nbsp; </p>"
										+"<p style=\"text-align:center;\"> Subject: Renewal Notice </p>"
										+"<p> &nbsp; </p>"
										+"<p style=\"text-align:left;\">Dear Sirs,</p>"
										+"<p style=\"text-align:center;\">Re : Master Policy No. "+ masterPolicyEntity.getPolicyNumber()+"</p>"
										+"<p style=\"text-align:center;\">Annual Renewal Date : "+dateString+"</p>"
										+"<p> &nbsp; </p>"
										+"<p> &nbsp; </p>"
										+"<table style=\"width: 150%;\">\r\n"
										+" <tr><td> Greetings from Life Insurance Corporation of India!</td></tr>"
										+" <tr><td> This is to inform you that the above scheme is due for renewal as on "+dateString+"</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> To enable us to renew the scheme, we request you to comply with the following requirements:</td></tr>"	
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> 1.Data of the existing members on "+dateString+"</td></tr>"
										+" <tr><td> as a soft copy in an excel file in the prescribed format.</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> 2.In the meantime, you may remit an adhoc amount as per last year premium quote towards the </td></tr>"
										+" <tr><td> premium for continuation of the Term insurance cover. Please note that the demand is on the</td></tr>"
										+" <tr><td> basis of last year valuation. The actual amount shall be calculated after receipt of data.</td></tr>"
										+" <tr><td> The remittance may be made by NEFT/RTGS to our bank account.</td></tr>"
										+" <tr><td> Confirmation of remittance has to be sent to us to the email id mentioned above giving UTR</td></tr>"
										+" <tr><td> Number, Date of remittance, Policy number, Type of scheme and Amount.</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> 3.It may please be noted that no grace period facility is available under Cash Accumulation System</td></tr>"
										+" <tr><td> and hence the contribution should reach us on or before due date. However, for risk cover </td></tr>"
										+" <tr><td> premium, a grace period of 30 days is allowed. In case of non-payment of risk premium and non </td></tr>"
										+" <tr><td> submission of employee's data within this 30 days period, the Life Cover Benefit will be reduced </td></tr>"
										+" <tr><td> to Rs. 5000/- per member and the renewal premium for the Reduced Life cover Benefits shall be </td></tr>"
										+" <tr><td> auto debited from the fund from ARD on monthly basis. Once the risk cover under this scheme</td></tr>"
										+" <tr><td> is reduced then the same can only be restored from prospective ARD subject to consent of the</td></tr>"
										+" <tr><td> corporation. In between entrants shall also be allowed with the minimum sum assured of Rs.5000/-</td></tr>"
										+" <tr><td> only for such cases till the next ARDs.</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> Assuring you of our best services always</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> Yours Faithfully,</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> Manager (P &amp; GS)</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"

										+ "</table>";
				}
				}
				return reportBody;
			}

			@Override
			public String renewalRemainderNoticepdf(Long id) throws IOException {
				
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
//				List<AgeValuationReportDto> ageValuationReportDto = new ArrayList<AgeValuationReportDto>();
//				List<Object[]> getAgeReport = policyRepository.getAgeReport(masterpolicyId);

//				ageValuationReportDto = PolicyHelper.valuationObjtoDto(getAgeReport);
				boolean showLogo = false;
				String reportBody = "";
				String reportStyles = "";
				String htmlContent2 = "</div></body></html>";
				
				reportBody = renewalRemaindernoticepdf(id) + htmlContent2;

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

				String htmlFileLoc = tempPdfLocation + "/" + id + ".html";
				FileWriter fw = new FileWriter(htmlFileLoc);
				fw.write(completehtmlContent);
				fw.close();

				FileOutputStream fileOutputStream = new FileOutputStream(tempPdfLocation + "/" + id + ".pdf");
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

				return tempPdfLocation + "/" + id + ".pdf";
			}

			private String renewalRemaindernoticepdf(Long id) {
				MasterPolicyEntity masterPolicyEntity = masterPolicyCustomRepository.findById(id);
				String reportBody=null;
				MPHEntity mPHEntity = mPHRepository.findById(masterPolicyEntity.getId()).get();
				Set<MPHAddressEntity>  get= mPHEntity.getMphAddresses();
				for(MPHAddressEntity mPHAddressEntity :get) {
				Date annualDate =masterPolicyEntity.getAnnualRenewlDate();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  
				String dateString = sdf.format(annualDate);
				String addressLine1 = mPHAddressEntity.getAddressLine1() != null ? mPHAddressEntity.getAddressLine1() : "";
				String addressLine2 = mPHAddressEntity.getAddressLine2() != null ? mPHAddressEntity.getAddressLine2() : "";
				String addressLine3 = mPHAddressEntity.getAddressLine3() != null ? mPHAddressEntity.getAddressLine3() : "";
				if(mPHAddressEntity.getAddressLine1() !=null || mPHAddressEntity.getAddressLine2()!=null || mPHAddressEntity.getAddressLine3() !=null) {
				
				 reportBody =	"<p style=\"text-align:center;\">Life Insurance Corporation of India</p>"
									+"<p style=\"text-align:center;\">P &amp; GS DEPARTMENT,DELHI DO I</p>"
									+"<p style=\"text-align:center;\">Pension and Group Schemes Department</p>"
									+"<p style=\"text-align:center;\">6TH &amp; 7th FLOOR, JEEVAN PRAKASH,</p>"
									+"<p style=\"text-align:center;\">25 K G MARG, NEW DELHI- 110001</p>"
									+"<p> &nbsp; </p>"
									 +"<table style=\"width: 120%;\">\r\n"
									    + "  <tr><td>Ref : P&amp;GS/ GGS/"+masterPolicyEntity.getPolicyNumber()+"</td>"
									    + "  <td class=\"alignRight\">Date :"+ DateUtils.dateToStringDDMMYYYY(new Date())+"</td></tr>"
									    + "</table>"
										+"<p> &nbsp; </p>"
										+"<p style=\"text-align:left;\">"+addressLine1+"</p>"
										+"<p style=\"text-align:left;\">"+addressLine2+"</p>"
										+"<p style=\"text-align:left;\">"+addressLine3+"</p>"
										+"<p style=\"text-align:left;\"></p>"
										+"<p style=\"text-align:left;\"></p>"
										+"<p> &nbsp; </p>"
										+"<p> &nbsp; </p>"
										+"<p style=\"text-align:center;\"> Subject: Renewal Remainder Notice </p>"
										+"<p> &nbsp; </p>"
										+"<p style=\"text-align:left;\">Dear Sirs,</p>"
										+"<p style=\"text-align:center;\">Re : Master Policy No. "+ masterPolicyEntity.getPolicyNumber()+"</p>"
										+"<p style=\"text-align:center;\">Annual Renewal Date : "+dateString+"</p>"
										+"<p> &nbsp; </p>"
										+"<p> &nbsp; </p>"
										+"<table style=\"width: 150%;\">\r\n"
										+" <tr><td> Greetings from Life Insurance Corporation of India!</td></tr>"
										+" <tr><td> This is to inform you that the above scheme is due for renewal as on "+dateString+"</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> To enable us to renew the scheme, we request you to comply with the following requirements:</td></tr>"	
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> 1.Data of the existing members on "+dateString+"</td></tr>"
										+" <tr><td> as a soft copy in an excel file in the prescribed format.</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> 2.In the meantime, you may remit an adhoc amount as per last year premium quote towards the </td></tr>"
										+" <tr><td> premium for continuation of the Term insurance cover. Please note that the demand is on the</td></tr>"
										+" <tr><td> basis of last year valuation. The actual amount shall be calculated after receipt of data.</td></tr>"
										+" <tr><td> The remittance may be made by NEFT/RTGS to our bank account.</td></tr>"
										+" <tr><td> Confirmation of remittance has to be sent to us to the email id mentioned above giving UTR</td></tr>"
										+" <tr><td> Number, Date of remittance, Policy number, Type of scheme and Amount.</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> 3.It may please be noted that no grace period facility is available under Cash Accumulation System</td></tr>"
										+" <tr><td> and hence the contribution should reach us on or before due date. However, for risk cover </td></tr>"
										+" <tr><td> premium, a grace period of 30 days is allowed. In case of non-payment of risk premium and non </td></tr>"
										+" <tr><td> submission of employee's data within this 30 days period, the Life Cover Benefit will be reduced </td></tr>"
										+" <tr><td> to Rs. 5000/- per member and the renewal premium for the Reduced Life cover Benefits shall be </td></tr>"
										+" <tr><td> auto debited from the fund from ARD on monthly basis. Once the risk cover under this scheme</td></tr>"
										+" <tr><td> is reduced then the same can only be restored from prospective ARD subject to consent of the</td></tr>"
										+" <tr><td> corporation. In between entrants shall also be allowed with the minimum sum assured of Rs.5000/-</td></tr>"
										+" <tr><td> only for such cases till the next ARDs.</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> Assuring you of our best services always</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> Yours Faithfully,</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"
										+" <tr><td> Manager (P &amp; GS)</td></tr>"
										+" <tr><td> &nbsp;</td></tr>"

										+ "</table>";
				}
				}
				return reportBody;
			}

			@Override
			public ApiResponseDto<List<RenewalPolicyTMPMemberDto>> filter(
					RenewalPolicyTMPMemberDto renewalPolicyTMPMemberDto, String type) {
				List<RenewalPolicyTMPMemberEntity> entities = new ArrayList<RenewalPolicyTMPMemberEntity>();

			
					if (type.equals("INPROGRESS")) {
						List<Predicate> predicates = new ArrayList<>();
						CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
						CriteriaQuery<RenewalPolicyTMPMemberEntity> createQuery = criteriaBuilder.createQuery(RenewalPolicyTMPMemberEntity.class);
						Root<RenewalPolicyTMPMemberEntity> root = createQuery.from(RenewalPolicyTMPMemberEntity.class);
						
						predicates.add(criteriaBuilder.equal(root.get("tmpPolicyId"),
								renewalPolicyTMPMemberDto.getTmpPolicyId()));
						if (StringUtils.isNotBlank(renewalPolicyTMPMemberDto.getLicId())) {
							predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("licId")),
									renewalPolicyTMPMemberDto.getLicId().toLowerCase()));
						}
						if (renewalPolicyTMPMemberDto.getId() != null) {
							predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("id")), renewalPolicyTMPMemberDto.getId()));
						}
						if (StringUtils.isNotBlank(renewalPolicyTMPMemberDto.getEmployeeCode())) {
							predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("employeeCode")),
									renewalPolicyTMPMemberDto.getEmployeeCode().toLowerCase()));
						}
						if (StringUtils.isNotBlank(renewalPolicyTMPMemberDto.getFirstName())) {
							predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName"))
									,"%"+renewalPolicyTMPMemberDto.getFirstName().toLowerCase()+"%"));
						}
						if (StringUtils.isNotBlank(renewalPolicyTMPMemberDto.getMiddleName())) {
							predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("middleName"))
									,"%"+renewalPolicyTMPMemberDto.getMiddleName().toLowerCase()+"%"));
						}
						if (StringUtils.isNotBlank(renewalPolicyTMPMemberDto.getLastName())) {
							predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName"))
									,"%"+renewalPolicyTMPMemberDto.getLastName().toLowerCase()+"%"));
						}
						if (StringUtils.isNotBlank(renewalPolicyTMPMemberDto.getFirstName())) {
							Predicate predicateForFirstName = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName"))
									,"%"+renewalPolicyTMPMemberDto.getFirstName().toLowerCase()+"%");
							
							Predicate predicateForMiddleName = criteriaBuilder.like(criteriaBuilder.lower(root.get("middleName"))
									,"%"+renewalPolicyTMPMemberDto.getMiddleName().toLowerCase()+"%");
							
							Predicate predicateForLastName = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName"))
									,"%"+renewalPolicyTMPMemberDto.getLastName().toLowerCase()+"%");
							
							predicates.add(criteriaBuilder.or(predicateForFirstName, predicateForMiddleName, 
									predicateForLastName));
						}
						if (StringUtils.isNotBlank(renewalPolicyTMPMemberDto.getPanNumber())) {
							predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("panNumber")),
									renewalPolicyTMPMemberDto.getPanNumber().toLowerCase()));
						}
						if (StringUtils.isNotBlank(renewalPolicyTMPMemberDto.getAadharNumber())) {
							predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("aadharNumber")),
									renewalPolicyTMPMemberDto.getAadharNumber().toLowerCase()));
						}
						if (renewalPolicyTMPMemberDto.getStatusId() != null) {
							predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("statusId")), renewalPolicyTMPMemberDto.getStatusId()));
						}
						createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
						entities = entityManager.createQuery(createQuery).getResultList();					
				
			}
					return ApiResponseDto.success(RenewalPolicyTMPMemberHelper.fromStagingEntities(entities));
             
		
			}	
			
}
