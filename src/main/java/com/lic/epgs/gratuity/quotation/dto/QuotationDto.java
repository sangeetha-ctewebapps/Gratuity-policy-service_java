package com.lic.epgs.gratuity.quotation.dto;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Gopi
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationDto {

	private Long id;
	private String number;
	private String proposalNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date date;
	private Long statusId;
	private Long subStatusId;
	private Long taggedStatusId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date dateOfCommencement;
	private Long gstApplicableId;
	private Long categoryForGstNonApplicableId;
	private Long contactPersonId;
	private Long bankAccountId;
	private String customerCode;
	private Long unitOfficeId;
	private Long productId;
	private Long productVariantId;
	private Long businessTypeId;
	private Long riskGroupId;
	private Long groupSizeId;
	private Long groupSumAssuredId;
	private Long frequencyId;
	private String rejectedReason;
	private String rejectedRemarks;
	
	private Boolean isStdValuesDeviated;

	private Boolean isEscalatedToCO;
	private Boolean isEscalatedToZO;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date approvalDate;
	private Boolean consentReceived;
	
	private Long  consentReasonId;
	private String consentRemarks;
	private Long tempQuotationId;
	private Boolean isPolicyCreated;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date createdDate;
	private Long memberUniqueIdentifierId;
	
	private Boolean isActive;
	private String createdBy;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date modifiedDate;
	private  Long payto; 
	 
	private String unitCode;
	private  String industryType;
	private String zoEscalationReason;
	private Integer fclUptoAge;
	private Boolean reinsuranceApplicable;
	private Boolean midLeaverAllowed;
	private Boolean midJoinerAllowed;
	
	
}
