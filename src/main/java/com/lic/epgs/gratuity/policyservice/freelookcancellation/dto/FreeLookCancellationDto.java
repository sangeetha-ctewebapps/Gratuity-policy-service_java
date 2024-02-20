package com.lic.epgs.gratuity.policyservice.freelookcancellation.dto;

import java.util.Date;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FreeLookCancellationDto {
	private Long id;
	private Long policyId;
	private String freeLookCancellationRequestNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "asia/kolkata")
	private Date freeLookCancellationRequestDate;
	private Long calculateFreeLookCancellationPerHardCopy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "asia/kolkata")
	private Date policyDocumentDispatchDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "asia/kolkata")
	private Date policyDocumentReceivedDate;
	private Long stampDuty;
	private Double medicalTestExpenses;
	private Double lifeCoverPremium;
	private Double debitPremiumFromMph;
	private Double creditPremiumToMph;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "asia/kolkata")
	private Date effectiveCancelDate;
	private String freeLookStatus;
	private String rejectionReasonCode;
	private String rejectionRemarks;
	private Double totalContribution;
	private Double totalFundValue;
	private Double totalAccuredIntrest;
	private Double totalRefundAmount;
	private String workFlowStatus;
	private Long isActive;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "asia/kolkata")
	private Date createdDate;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "asia/kolkata")
	private Date modifiedDate;
	private Double gst;
	private Double debitGstFromMph;
	private Double creditGstToMph;
	
}
