package com.lic.epgs.gratuity.simulation.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositAdjustementDto {
	private Long masterQuotationId;
	private Long stagingPolicyId;
	private Long tmpPolicyId;
	private Double depositAmount;
	private Double availableAmount;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date chequeRealisationDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date collectionDate;
	
	private Long collectionNumber;
	private String collectionStatus;
	private String remarks;
	private String transactionMode;
	private String username;
	private String createdBy;
	

	private Integer collectionType;
	private String collectionMode;
	private Double collectionAmount;

	private Double utilizedAmount;
	private Double balanceAmount;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date voucherEffectiveDate;
	private String lockStatus;
	private String adjustmentAvailability;
	private Long adjustmentNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date adjustmentDate;
	private Double adjuestmentAmount;
	private String adjustmentStatus;
}
