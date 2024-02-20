package com.lic.epgs.gratuity.quotation.member.helper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.policy.member.entity.TempMemberEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberBulkErrorEntity;

public class MemberErrorWorkbookHelper {
	
	public void createHeader(Workbook workbook, Sheet sheet, boolean isSample) {
		Row header = sheet.createRow(0);
		CellStyle headerStyle = workbook.createCellStyle();
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFPalette palette = hwb.getCustomPalette();
		try {
			hwb.close();
		} catch (IOException e) {
			//do nothing
		}
		
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		HSSFColor fontColor = palette.findSimilarColor(0,0,0);
		font.setColor(fontColor.getIndex());
		font.setBold(false);
		headerStyle.setFont(font);
		
		Cell headerCell = header.createCell(0);
		int i=0;
		for (String col : headerTexts()) {
			if (isSample && col.equals("FAILED REASON")) continue;
			headerCell = header.createCell(i++);
			headerCell.setCellValue(col);
			headerCell.setCellStyle(headerStyle);
		}
	}
	
	public List<String> headerTexts(){
		return Arrays.asList("PROPOSAL / POLICY NUMBER", "LIC ID",	"EMPLOYEE CODE",	"FIRST NAME", "MIDDLE NAME", "LAST NAME", "DATE OF BIRTH", "DATE OF APPOINTMENT", "DOJ TO SCHEME",  "CATEGORY",	"SALARY",	"GENDER",  "SALARY FREQUENCY",	"PAN NUMBER",	"AADHAR NUMBER",	"CONTACT NUMBER",	"EMAIL ID",		"ADDRESS1 TYPE",	"ADDRESS1 PIN CODE",	"ADDRESS1 COUNTRY",	"ADDRESS1 STATE",	"ADDRESS1 DISTRICT",	"ADDRESS1 CITY",	"ADDRESS1 CONTACT NUMBER",	"ADDRESS1 LINE1",	"ADDRESS1 LINE2",	"ADDRESS1 LINE3",	"ADDRESS1 PRINTABLE",	"ADDRESS2 TYPE",	"ADDRESS2 PIN CODE",	"ADDRESS2 COUNTRY",	"ADDRESS2 STATE",	"ADDRESS2 DISTRICT",	"ADDRESS2 CITY",	"ADDRESS2 CONTACT NUMBER",	"ADDRESS2 LINE1",	"ADDRESS2 LINE2",	"ADDRESS2 LINE3",	"ADDRESS2 PRINTABLE",	"ADDRESS3 TYPE",	"ADDRESS3 PIN CODE",	"ADDRESS3 COUNTRY",	"ADDRESS3 STATE",	"ADDRESS3 DISTRICT",	"ADDRESS3 CITY",	"ADDRESS3 CONTACT NUMBER",	"ADDRESS3 LINE1",	"ADDRESS3 LINE2",	"ADDRESS3 LINE3",	"ADDRESS3 PRINTABLE",	"BANK1 ACCOUNT NUMBER",	"BANK1 ACCOUNT TYPE",	"BANK1 IFCS CODE",	"BANK1 NAME",	"BANK1 BRANCH",	"BANK2 ACCOUNT NUMBER",	"BANK2 ACCOUNT TYPE",	"BANK2 IFCS CODE",	"BANK2 NAME",	"BANK2 BRANCH",	"BANK3 ACCOUNT NUMBER",	"BANK3 ACCOUNT TYPE",	"BANK3 IFCS CODE",	"BANK3 NAME",	"BANK3 BRANCH",	"NOMINEE1 CODE",	"NOMINEE1 NAME",	"NOMINEE1 RELATION SHIP",	"NOMINEE1 CONTACT NUMBER",	"NOMINEE1 DATE OF BIRTH",	"NOMINEE1 PAN NUMBER","NOMINEE1 GENDER",	"NOMINEE1 AADHAR NUMBER",	"NOMINEE1 BANK ACCOUNT NUMBER",	"NOMINEE1 BANK ACCOUNT TYPE",	"NOMINEE1 IFSC CODE",	"NOMINEE1 BANK NAME",	"NOMINEE1 BANK BRANCH",	"NOMINEE1 PERCENTAGE",	"NOMINEE2 CODE",	"NOMINEE2 NAME",	"NOMINEE2 RELATION SHIP",	"NOMINEE2 CONTACT NUMBER",	"NOMINEE2 DATE OF BIRTH",	"NOMINEE2 PAN NUMBER","NOMINEE2 GENDER",	"NOMINEE2 AADHAR NUMBER",	"NOMINEE2 BANK ACCOUNT NUMBER",	"NOMINEE2 BANK ACCOUNT TYPE",	"NOMINEE2 IFSC CODE",	"NOMINEE2 BANK NAME",	"NOMINEE2 BANK BRANCH",	"NOMINEE2 PERCENTAGE",	"NOMINEE3 CODE",	"NOMINEE3 NAME",	"NOMINEE3 RELATION SHIP",	"NOMINEE3 CONTACT NUMBER",	"NOMINEE3 DATE OF BIRTH",	"NOMINEE3 PAN NUMBER","NOMINEE3 GENDER",	"NOMINEE3 AADHAR NUMBER",	"NOMINEE3 BANK ACCOUNT NUMBER",	"NOMINEE3 BANK ACCOUNT TYPE",	"NOMINEE3 IFSC CODE",	"NOMINEE3 BANK NAME",	"NOMINEE3 BANK BRANCH",	"NOMINEE3 PERCENTAGE",	"APPOINTEE1 MEMBER NOMINEE",	"APPOINTEE1 CODE",	"APPOINTEE1 NAME",	"APPOINTEE1 RELATION SHIP",	"APPOINTEE1 CONTACT NUMBER",	"APPOINTEE1 DATE OF BIRTH",	"APPOINTEE1 PAN NUMBER",	"APPOINTEE1 AADHAR NUMBER",	"APPOINTEE1 BANK ACCOUNT NUMBER",	"APPOINTEE1 ACCOUNT TYPE",	"APPOINTEE1 IFCS CODE",	"APPOINTEE1 BANK NAME",	"APPOINTEE1 BANK BRANCH",	"APPOINTEE2 MEMBER NOMINEE",	"APPOINTEE2 CODE",	"APPOINTEE2 NAME",	"APPOINTEE2 RELATION SHIP",	"APPOINTEE2 CONTACT NUMBER",	"APPOINTEE2 DATE OF BIRTH",	"APPOINTEE2 PAN NUMBER",	"APPOINTEE2 AADHAR NUMBER",	"APPOINTEE2 BANK ACCOUNT NUMBER",	"APPOINTEE2 ACCOUNT TYPE",	"APPOINTEE2 IFCS CODE",	"APPOINTEE2 BANK NAME",	"APPOINTEE2 BANK BRANCH",	"APPOINTEE3 MEMBER NOMINEE",	"APPOINTEE3 CODE",	"APPOINTEE3 NAME",	"APPOINTEE3 RELATION SHIP",	"APPOINTEE3 CONTACT NUMBER",	"APPOINTEE3 DATE OF BIRTH",	"APPOINTEE3 PAN NUMBER",	"APPOINTEE3 AADHAR NUMBER",	"APPOINTEE3 BANK ACCOUNT NUMBER",	"APPOINTEE3 ACCOUNT TYPE",	"APPOINTEE3 IFCS CODE",	"APPOINTEE3 BANK NAME",	"APPOINTEE3 BANK BRANCH",	"FAILED REASON");
	}
	
	public void createHeaderdom(Workbook workbook, Sheet sheet, boolean isSample) {
		Row header = sheet.createRow(0);
		CellStyle headerStyle = workbook.createCellStyle();
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFPalette palette = hwb.getCustomPalette();
		try {
			hwb.close();
		} catch (IOException e) {
			//do nothing
		}
		
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		HSSFColor fontColor = palette.findSimilarColor(0,0,0);
		font.setColor(fontColor.getIndex());
		font.setBold(false);
		headerStyle.setFont(font);
		
		Cell headerCell = header.createCell(0);
		int i=0;
		for (String col : headerText()) {
			if (isSample && col.equals("FAILED REASON")) continue;
			headerCell = header.createCell(i++);
			headerCell.setCellValue(col);
			headerCell.setCellStyle(headerStyle);
		}
	}
	
	public List<String> headerText() {
		return Arrays.asList("DATE OF LEAVING", "REASON FOR LEAVING", "REASON FOR LEAVING OTHER", "LIC ID", "EMPLOYEE CODE", "FAILED REASON");
	}
	
	
	public void createDetailRow(Workbook workbook, Sheet sheet, 
			List<MemberBulkErrorEntity> errornousInfo, List<Object[]> errornousData) {
		int rowNumber = 1;
		int colNumber = 0;
		Row header = null;
		Cell headerCell = null;
		
		if (errornousData.size() == 0) {
			colNumber = 0;
			header = sheet.createRow(rowNumber++);
			
			headerCell = header.createCell(colNumber++);
			headerCell.setCellValue("There is no errors associated with this upload");
		}
				
		for (Object[] obj : errornousData) {
			colNumber = 0;
			header = sheet.createRow(rowNumber++);
			
			for (int i = 0; i < obj.length-3; i++) {
				if (i==0) continue;
				if (obj[i] == null) {
					colNumber++;
				} else {
					Date get = null;
					if (colNumber == 6 || colNumber == 7 || colNumber == 8) {
						get = DateUtils.convertStringToDateYYYYMMDD_HHMMSS_HYHEN(obj[i].toString());
						headerCell = header.createCell(colNumber++);
						headerCell.setCellValue(DateUtils.dateToStringDDMMYYYY(get));
					} else {
						headerCell = header.createCell(colNumber++);
						headerCell.setCellValue(obj[i].toString());
					}
				}
			}
							
			MemberBulkErrorEntity memberBulkErrorEntity = errornousInfo.stream()
					.filter(error -> obj[0].equals(error.getStagingId()))
					.findAny().orElse(null);
			headerCell = header.createCell(colNumber++);
			headerCell.setCellValue(memberBulkErrorEntity.getError());
		}
	}
	
	public void createDetailRowForDom(Workbook workbook, Sheet sheet, 
			List<MemberBulkErrorEntity> errornousInfo, List<Object[]> errornousData) {
		int rowNumber = 1;
		int colNumber = 0;
		Row header = null;
		Cell headerCell = null;
		
		if (errornousData.size() == 0) {
			colNumber = 0;
			header = sheet.createRow(rowNumber++);
			
			headerCell = header.createCell(colNumber++);
			headerCell.setCellValue("There is no errors associated with this upload");
		}
				
		for (Object[] obj : errornousData) {
			colNumber = 0;
			header = sheet.createRow(rowNumber++);
			
			for (int i = 0; i < obj.length-2; i++) {
				if (i==0) continue;
				if (obj[i] == null) {
					colNumber++;
				} else {
						headerCell = header.createCell(colNumber++);
						headerCell.setCellValue(obj[i].toString());
					}
				}
			
			MemberBulkErrorEntity memberBulkErrorEntity = errornousInfo.stream()
					.filter(error -> obj[0].equals(error.getStagingId()))
					.findAny().orElse(null);
			headerCell = header.createCell(colNumber++);
			headerCell.setCellValue(memberBulkErrorEntity.getError());
		}
	}

	public void createHeaderForCandBSheet(Workbook workbook, Sheet sheet, boolean isSample) {
		Row header = sheet.createRow(0);
		CellStyle headerStyle = workbook.createCellStyle();
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFPalette palette = hwb.getCustomPalette();
		try {
			hwb.close();
		} catch (IOException e) {
			//do nothing
		}
		
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		HSSFColor fontColor = palette.findSimilarColor(0,0,0);
		font.setColor(fontColor.getIndex());
		font.setBold(false);
		headerStyle.setFont(font);
		
		Cell headerCell = header.createCell(0);
		int i=0;
		for (String col : headerTextsforSheet()) {
			if (isSample && col.equals("FAILED REASON")) continue;
			headerCell = header.createCell(i++);
			headerCell.setCellValue(col);
			headerCell.setCellStyle(headerStyle);
		}
		
	}

	private  List<String> headerTextsforSheet() {
	
		 return Arrays.asList("licId", "name" ,"employeeNo" ,"customerCode","customerName","policyNumber", "dateofBirth" ,"dateofAppointment", "dojofScheme", "salary" , "pastService" , "totalService" , "tsg" , "psg" , "lifeCover" ,"ard", "age","lcp","category","psb","csb");
	}

	
	
	public void createHeaderForCandBSheetquotation(Workbook workbook, Sheet sheet, boolean isSample) {
		Row header = sheet.createRow(0);
		CellStyle headerStyle = workbook.createCellStyle();
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFPalette palette = hwb.getCustomPalette();
		try {
			hwb.close();
		} catch (IOException e) {
			//do nothing
		}
		
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		HSSFColor fontColor = palette.findSimilarColor(0,0,0);
		font.setColor(fontColor.getIndex());
		font.setBold(false);
		headerStyle.setFont(font);
		
		Cell headerCell = header.createCell(0);
		int i=0;
		for (String col : headerTextsforSheetquotation()) {
			if (isSample && col.equals("FAILED REASON")) continue;
			headerCell = header.createCell(i++);
			headerCell.setCellValue(col);
			headerCell.setCellStyle(headerStyle);
		}
		
	}
	
	private  List<String> headerTextsforSheetquotation() {
		
		 return Arrays.asList("licId", "name" ,"employeeNo" ,"quotationNumber", "dateofBirth" ,"dateofAppointment", "dojofScheme", "salary" , "pastService" , "totalService" , "tsg" , "psg" , "lifeCover" ,"ard", "age","lcp","category","psb","csb","customerCode");
	}

	public void createDetailRow(Workbook workbook, Sheet sheet, List<Object[]> policyandQuotationandRQMasterData) {
		int rowNumber = 1;
		int colNumber = 0;
		Row header = null;
		Cell headerCell = null;
		
		if (policyandQuotationandRQMasterData.size() == 0) {
			colNumber = 0;
			header = sheet.createRow(rowNumber++);
			
			headerCell = header.createCell(colNumber++);
			headerCell.setCellValue("There is no data associated with this upload");
		}
				
		for (Object[] obj : policyandQuotationandRQMasterData) {
			colNumber = 0;
			header = sheet.createRow(rowNumber++);
			
			for (int i = 0; i < obj.length; i++) {
			
				if (obj[i] == null) {
					colNumber++;
				} else {
					headerCell = header.createCell(colNumber++);headerCell.setCellValue(obj[i].toString());
				}
			}
							
			
			headerCell = header.createCell(colNumber++);
		
		}
		
	}
	
	
	public void createHeaderforClaim(Workbook workbook, Sheet sheet, boolean isSample) {
		Row header = sheet.createRow(0);
		CellStyle headerStyle = workbook.createCellStyle();
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFPalette palette = hwb.getCustomPalette();
		try {
			hwb.close();
		} catch (IOException e) {
			//do nothing
		}
		
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		HSSFColor fontColor = palette.findSimilarColor(0,0,0);
		font.setColor(fontColor.getIndex());
		font.setBold(false);
		headerStyle.setFont(font);
		
		Cell headerCell = header.createCell(0);
		int i=0;
		for (String col : headerClaimTexts()) {
			if (isSample && col.equals("FAILED REASON")) continue;
			headerCell = header.createCell(i++);
			headerCell.setCellValue(col);
			headerCell.setCellStyle(headerStyle);
		}
	}

	private List<String> headerClaimTexts() {
		return Arrays.asList("DATE OF EXIT","MODE OF EXIT","REASON FOR DEATH","REASON FOR DEATH OTHER","DATE OF DEATH","PLACE OF DEATH","LIC ID",	"EMPLOYEE CODE","FAILED REASON");
	}

	public void createHeaderforClaims(Workbook workbook, Sheet sheet, List<MemberBulkErrorEntity> errornousInfo,
			List<Object[]> errornousData) {
		int rowNumber = 1;
		int colNumber = 0;
		Row header = null;
		Cell headerCell = null;
		
		if (errornousData.size() == 0) {
			colNumber = 0;
			header = sheet.createRow(rowNumber++);
			
			headerCell = header.createCell(colNumber++);
			headerCell.setCellValue("There is no errors associated with this upload");
		}
				
		for (Object[] obj : errornousData) {
			colNumber = 0;
			header = sheet.createRow(rowNumber++);
			
			for (int i = 0; i < obj.length; i++) {
				if (i==0) continue;
				if (obj[i] == null) {
					colNumber++;
				} else {
					headerCell = header.createCell(colNumber++);headerCell.setCellValue(obj[i].toString());
				}
			}
							
			MemberBulkErrorEntity memberBulkErrorEntity = errornousInfo.stream()
					.filter(error -> obj[0].equals(error.getStagingId()))
					.findAny().orElse(null);
			headerCell = header.createCell(colNumber++);
			headerCell.setCellValue(memberBulkErrorEntity.getError());
		}
	}

	public void createclaimsHeader(Workbook workbook, Sheet sheet, boolean b) {
		Row header = sheet.createRow(0);
		CellStyle headerStyle = workbook.createCellStyle();
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFPalette palette = hwb.getCustomPalette();
		try {
			hwb.close();
		} catch (IOException e) {
			//do nothing
		}
		
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		HSSFColor fontColor = palette.findSimilarColor(0,0,0);
		font.setColor(fontColor.getIndex());
		font.setBold(false);
		headerStyle.setFont(font);
		
		Cell headerCell = header.createCell(0);
		int i=0;
		for (String col : headersTexts()) {
			if (b && col.equals("FAILED REASON")) continue;
			headerCell = header.createCell(i++);
			headerCell.setCellValue(col);
			headerCell.setCellStyle(headerStyle);
		}
	}
	public List<String> headersTexts(){
		return Arrays.asList("DATE OF EXIT", "MODE OF EXIT" ,"REASON FOR DEATH","REASON FOR DEATH OTHER","DATE OF DEATH","PLACE OF DEATH","LIC ID","EMPLOYEE CODE","FAILED REASON ");
	}	
	
	
	public void createMidLeaverHeader(Workbook workbook, Sheet sheet, boolean isSample) {
		Row header = sheet.createRow(0);
		CellStyle headerStyle = workbook.createCellStyle();
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFPalette palette = hwb.getCustomPalette();
		try {
			hwb.close();
		} catch (IOException e) {
			//do nothing
		}
		
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		HSSFColor fontColor = palette.findSimilarColor(0,0,0);
		font.setColor(fontColor.getIndex());
		font.setBold(false);
		headerStyle.setFont(font);
		
		Cell headerCell = header.createCell(0);
		int i=0;
		for (String col : headerMidLeaverTexts()) {
			if (isSample && col.equals("FAILED REASON")) continue;
			headerCell = header.createCell(i++);
			headerCell.setCellValue(col);
			headerCell.setCellStyle(headerStyle);
		}
	}
	
	public List<String> headerMidLeaverTexts(){
		return Arrays.asList("DATE OF LEAVING", "REASON FOR LEAVING", "REASON FOR LEAVING OTHER", "LIC ID", "EMPLOYEE CODE", "FAILED REASON");
	}
	
	public void createMidLeaverDetailHeader(Workbook workbook, Sheet sheet, boolean isSample) {
		Row header = sheet.createRow(0);
		CellStyle headerStyle = workbook.createCellStyle();
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFPalette palette = hwb.getCustomPalette();
		try {
			hwb.close();
		} catch (IOException e) {
			//do nothing
		}
		
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		HSSFColor fontColor = palette.findSimilarColor(0,0,0);
		font.setColor(fontColor.getIndex());
		font.setBold(false);
		headerStyle.setFont(font);
		
		Cell headerCell = header.createCell(0);
		int i=0;
		for (String col : headerMidLeaverDetailTexts()) {
			if (isSample && col.equals("FAILED REASON")) continue;
			headerCell = header.createCell(i++);
			headerCell.setCellValue(col);
			headerCell.setCellStyle(headerStyle);
		}
	}
	
	public List<String> headerMidLeaverDetailTexts(){
		return Arrays.asList("Employee Code", "LIC ID", "Member Status", "Date of Appointment", "Date of Leaving", "Date of Adjustment", "Last Premium Collected", "Last GST Collected", "Premium Refundable", "GST Refundable");
	}
	
	public void createMidLeaverDetailRow(Workbook workbook, Sheet sheet, 
			List<Object[]> tempMemberEntities) {
		int rowNumber = 1;
		int colNumber = 0;
		Row header = null;
		Cell headerCell = null;
		
		if (tempMemberEntities.size() == 0) {
			colNumber = 0;
			header = sheet.createRow(rowNumber++);
			
			headerCell = header.createCell(colNumber++);
			headerCell.setCellValue("There is no data associated with this download");
		}
				
		for (Object[] obj : tempMemberEntities) {
			colNumber = 0;
			header = sheet.createRow(rowNumber++);
			
			for (int i = 0; i < obj.length; i++) {
			
				if (obj[i] == null) {
					colNumber++;
				} else {
					headerCell = header.createCell(colNumber++);
					headerCell.setCellValue(obj[i].toString());
				}
			}
			headerCell = header.createCell(colNumber++);
		}
	}
}
