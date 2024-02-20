package com.lic.epgs.gratuity.simulation.dto;

import java.util.Date;

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
public class DepositDto {
	private Long id;
	private Long collectionNumber;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date collectionDate;
	private Integer collectionType;
	private String collectionMode;
	private Double collectionAmount;
	private String collectionStatus;
	private Double utilizedAmount;
	private Double balanceAmount;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date voucherEffectiveDate;
	private String lockStatus;
	private String adjustmentAvailability;
	private Long adjustmentNo;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date adjustmentDate;
	private Double adjuestmentAmount;
	private String adjustmentStatus;

	private String proposalNumber;
	private Double depositAmount;

	private String remarks;
	private String transactionMode;
	private String accountAPIID;

}
