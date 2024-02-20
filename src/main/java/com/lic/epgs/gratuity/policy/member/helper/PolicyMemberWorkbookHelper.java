package com.lic.epgs.gratuity.policy.member.helper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.policy.member.entity.StagingPolicyMemberEntity;
import com.lic.epgs.gratuity.quotation.member.entity.MemberBulkErrorEntity;
import com.lic.epgs.gratuity.quotation.member.helper.BulkMemberUploadHelper;

@Service
public class PolicyMemberWorkbookHelper {
	
	@Autowired
	private EntityManager entityManager;
	
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

	private List<String> headerTexts(){
	return Arrays.asList("PROPOSAL / POLICY NUMBER", "LIC ID", "EMPLOYEE CODE", "FIRST NAME", "MIDDLE NAME", "LAST NAME", "DATE OF BIRTH", "DATE OF APPOINTMENT", "DOJ TO SCHEME",  "CATEGORY", "SALARY", "GENDER",  "SALARY FREQUENCY", "PAN NUMBER", "AADHAR NUMBER", "CONTACT NUMBER", "EMAIL ID",	 "ADDRESS1 TYPE", "ADDRESS1 PIN CODE", "ADDRESS1 COUNTRY", "ADDRESS1 STATE", "ADDRESS1 DISTRICT", "ADDRESS1 CITY", "ADDRESS1 CONTACT NUMBER", "ADDRESS1 LINE1", "ADDRESS1 LINE2", "ADDRESS1 LINE3", "ADDRESS1 PRINTABLE", "ADDRESS2 TYPE", "ADDRESS2 PIN CODE", "ADDRESS2 COUNTRY", "ADDRESS2 STATE", "ADDRESS2 DISTRICT", "ADDRESS2 CITY", "ADDRESS2 CONTACT NUMBER", "ADDRESS2 LINE1", "ADDRESS2 LINE2", "ADDRESS2 LINE3", "ADDRESS2 PRINTABLE", "ADDRESS3 TYPE", "ADDRESS3 PIN CODE", "ADDRESS3 COUNTRY", "ADDRESS3 STATE", "ADDRESS3 DISTRICT", "ADDRESS3 CITY", "ADDRESS3 CONTACT NUMBER", "ADDRESS3 LINE1", "ADDRESS3 LINE2", "ADDRESS3 LINE3", "ADDRESS3 PRINTABLE", "BANK1 ACCOUNT NUMBER", "BANK1 ACCOUNT TYPE", "BANK1 IFCS CODE", "BANK1 NAME", "BANK1 BRANCH", "BANK2 ACCOUNT NUMBER", "BANK2 ACCOUNT TYPE", "BANK2 IFCS CODE", "BANK2 NAME", "BANK2 BRANCH", "BANK3 ACCOUNT NUMBER", "BANK3 ACCOUNT TYPE", "BANK3 IFCS CODE", "BANK3 NAME", "BANK3 BRANCH", "NOMINEE1 CODE", "NOMINEE1 NAME", "NOMINEE1 RELATION SHIP", "NOMINEE1 CONTACT NUMBER", "NOMINEE1 DATE OF BIRTH", "NOMINEE1 PAN NUMBER","NOMINEE1 GENDER", "NOMINEE1 AADHAR NUMBER", "NOMINEE1 BANK ACCOUNT NUMBER", "NOMINEE1 BANK ACCOUNT TYPE", "NOMINEE1 IFSC CODE", "NOMINEE1 BANK NAME", "NOMINEE1 BANK BRANCH", "NOMINEE1 PERCENTAGE", "NOMINEE2 CODE", "NOMINEE2 NAME", "NOMINEE2 RELATION SHIP", "NOMINEE2 CONTACT NUMBER", "NOMINEE2 DATE OF BIRTH", "NOMINEE2 PAN NUMBER","NOMINEE2 GENDER", "NOMINEE2 AADHAR NUMBER", "NOMINEE2 BANK ACCOUNT NUMBER", "NOMINEE2 BANK ACCOUNT TYPE", "NOMINEE2 IFSC CODE", "NOMINEE2 BANK NAME", "NOMINEE2 BANK BRANCH", "NOMINEE2 PERCENTAGE", "NOMINEE3 CODE", "NOMINEE3 NAME", "NOMINEE3 RELATION SHIP", "NOMINEE3 CONTACT NUMBER", "NOMINEE3 DATE OF BIRTH", "NOMINEE3 PAN NUMBER","NOMINEE3 GENDER", "NOMINEE3 AADHAR NUMBER", "NOMINEE3 BANK ACCOUNT NUMBER", "NOMINEE3 BANK ACCOUNT TYPE", "NOMINEE3 IFSC CODE", "NOMINEE3 BANK NAME", "NOMINEE3 BANK BRANCH", "NOMINEE3 PERCENTAGE", "APPOINTEE1 MEMBER NOMINEE", "APPOINTEE1 CODE", "APPOINTEE1 NAME", "APPOINTEE1 RELATION SHIP", "APPOINTEE1 CONTACT NUMBER", "APPOINTEE1 DATE OF BIRTH", "APPOINTEE1 PAN NUMBER", "APPOINTEE1 AADHAR NUMBER", "APPOINTEE1 BANK ACCOUNT NUMBER", "APPOINTEE1 ACCOUNT TYPE", "APPOINTEE1 IFCS CODE", "APPOINTEE1 BANK NAME", "APPOINTEE1 BANK BRANCH", "APPOINTEE2 MEMBER NOMINEE", "APPOINTEE2 CODE", "APPOINTEE2 NAME", "APPOINTEE2 RELATION SHIP", "APPOINTEE2 CONTACT NUMBER", "APPOINTEE2 DATE OF BIRTH", "APPOINTEE2 PAN NUMBER", "APPOINTEE2 AADHAR NUMBER", "APPOINTEE2 BANK ACCOUNT NUMBER", "APPOINTEE2 ACCOUNT TYPE", "APPOINTEE2 IFCS CODE", "APPOINTEE2 BANK NAME", "APPOINTEE2 BANK BRANCH", "APPOINTEE3 MEMBER NOMINEE", "APPOINTEE3 CODE", "APPOINTEE3 NAME", "APPOINTEE3 RELATION SHIP", "APPOINTEE3 CONTACT NUMBER", "APPOINTEE3 DATE OF BIRTH", "APPOINTEE3 PAN NUMBER", "APPOINTEE3 AADHAR NUMBER", "APPOINTEE3 BANK ACCOUNT NUMBER", "APPOINTEE3 ACCOUNT TYPE", "APPOINTEE3 IFCS CODE", "APPOINTEE3 BANK NAME", "APPOINTEE3 BANK BRANCH", "FAILED REASON");
	}


	public void createDetailRow(XSSFWorkbook workbook, XSSFSheet sheet,
			 List<Object[]> memberData) {

		int rowNumber = 1;
		int colNumber = 0;
		Row header = null;
		Cell headerCell = null;
		
		if (memberData.size() == 0) {
			colNumber = 0;
			header = sheet.createRow(rowNumber++);
			
			headerCell = header.createCell(colNumber++);
			headerCell.setCellValue("There is no errors associated with this upload");
		}
				
		for (Object[] obj : memberData) {
			colNumber = 0;
			header = sheet.createRow(rowNumber++);
			for (int i = 0; i < obj.length; i++) {
				if(i==0) continue;
				if (obj[i] == null) {
					colNumber++;
				} else {
					headerCell = header.createCell(colNumber++);headerCell.setCellValue(obj[i].toString());
				}
				int length = obj.length-1;
				if(i == length) {
				Long memberId= Long.parseLong(obj[0].toString());
				List<Object[]> memAddress = getmemberAddressData(memberId);
				String get=obj[0].toString(); 
				Long getLong=Long.parseLong(get);
				System.out.println(getLong); 
				if (memAddress.size() != 0){ 
					int memadd = memAddress.size();
					for (Object[] objAddress : memAddress) { 
						int j=0; 
						for ( j =0; j <objAddress.length; j++) {
							if (objAddress[j] == null) {
								colNumber++; } 
							else { headerCell = header.createCell(colNumber++);
							headerCell.setCellValue(objAddress[j].toString()); 
							} } 
						memadd --;
						int length1 = objAddress.length;
						if(memadd == 0 && j == length1 ) {
							memberId= Long.parseLong(obj[0].toString());
							List<Object[]> memBank = getBankData(memberId);
							System.out.println( memBank.size());
							get=obj[0].toString(); 
							getLong=Long.parseLong(get);
							System.out.println(getLong); 
							if (memBank.size() != 0){ 
								colNumber =44;
								int bankDtls = memBank.size();
								for (Object[] objBankDtls : memBank) {	
									int k=0; 
									for ( k = 0; k <objBankDtls.length; k++) {
										if (objBankDtls[k] == null) {
											colNumber++; } 
										else { headerCell = header.createCell(colNumber++);
										headerCell.setCellValue(objBankDtls[k].toString()); 
										} } 
									bankDtls--;
									int length2 = objBankDtls.length;
									if(bankDtls == 0 && k == length2 ) {
										memberId= Long.parseLong(obj[0].toString());
										List<Object[]> nomineeData = getNomineeExcelData(memberId);
										System.out.println( nomineeData.size());
										get=obj[0].toString(); 
										getLong=Long.parseLong(get);
										System.out.println(getLong); 
										if (nomineeData.size() != 0){ 
											colNumber =59;
											int nomDtls = nomineeData.size();
											for (Object[] objNomineeDtls : nomineeData) {	
												int l=0; 
												for ( l = 0; l <objNomineeDtls.length; l++) {
													if (objNomineeDtls[l] == null) {
														colNumber++; } 
													else { headerCell = header.createCell(colNumber++);
													headerCell.setCellValue(objNomineeDtls[l].toString()); 
													} } 
												nomDtls--;	
												int length3 = objNomineeDtls.length;
												if(bankDtls == 0 && l == length3 ) {
													memberId= Long.parseLong(obj[0].toString());
													List<Object[]> appoiData = getAppointeeExcelData(memberId);
													System.out.println( appoiData.size());
													get=obj[0].toString(); 
													getLong=Long.parseLong(get);
													System.out.println(getLong); 
													if (appoiData.size() != 0){ 
														int appoiDtls = appoiData.size();
														colNumber =98;
														for (Object[] objappoDtls : appoiData) {	
															int m=0; 
															for ( m = 0; m <objappoDtls.length; m++) {
																if (objappoDtls[m] == null) {
																	colNumber++; } 
																else { headerCell = header.createCell(colNumber++);
																headerCell.setCellValue(objappoDtls[m].toString()); 
																} } 
															appoiDtls--;		
											}
												} }
										
										} }
						}	
							} }
				}
				obj[0].toString();
				
					}}}}}
					
							
//			StagingPolicyMemberEntity stagingPolicyMemberEntity = policyMemberDetails.stream()
//					//.filter(error -> obj[0].equals(error.getStagingId()))
//					.findAny().orElse(null);
//			headerCell = header.createCell(colNumber++);
	//	headerCell.setCellValue(stagingPolicyMemberEntity);
				}
	@SuppressWarnings("unchecked")
	public List<Object[]> getmemberAddressData(Long memberId) {
		System.out.println("iddddddddddddddddddd"+memberId);
		return entityManager.createNativeQuery("SELECT ADDRESS_TYPE_ID As addressTypeId,PIN_CODE As pincode,COUNTRY As country,STATE_NAME As stateName,DISTRICT As district,CITY As city,ADDRESS1 As address1,ADDRESS2 As address2,ADDRESS3 As address3 FROM PMST_MEMBER_ADDRESS where MEMBER_ID = :MEMBERID")
		          .setParameter("MEMBERID", memberId)
		          .getResultList();	
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getBankData(Long memberId) {
		return entityManager.createNativeQuery("SELECT BANK_ACCOUNT_NUMBER,ACCOUNT_TYPE,IFSC_CODE,BANK_NAME,BANK_BRANCH FROM PMST_MEMBER_BANK_ACCOUNT where MEMBER_ID = :MEMBERID")
				.setParameter("MEMBERID", memberId)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getNomineeExcelData(Long memberId) {
		return entityManager.createNativeQuery("SELECT MEMBER_NOMINEE_ID,NOMINEE_NAME,RELATIONSHIP,NOMINEE_PHONE,DATE_OF_BIRTH,PAN_NUMBER,NOMINEE_AADHAR_NUMBER,BANK_ACCOUNT_NUMBER,ACCOUNT_TYPE,IFSC_CODE,BANK_NAME,BANK_BRANCH,PERCENTAGE FROM PMST_MEMBER_NOMINEE where MEMBER_ID = :MEMBERID")
				.setParameter("MEMBERID", memberId)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getAppointeeExcelData(Long memberId) {
		return entityManager.createNativeQuery("SELECT MEMBER_NOMINEE_ID,CODE,NAME,RELATIONSHIP,CONTACT_NUMBER,DATE_OF_BIRTH,PAN_NUMBER,AADHAR_NUMBER,BANK_ACCOUNT_NUMBER,ACCOUNT_TYPE,IFSC_CODE,BANK_NAME,BANK_BRANCH,PERCENTAGE  FROM PMST_MEMBER_APPOINTEE where MEMBER_ID = :MEMBERID")
				.setParameter("MEMBERID", memberId)
				.getResultList();
	}
	
	}
