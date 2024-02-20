package com.lic.epgs.gratuity.quotation.valuationmatrix.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ismail Khan A
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValuationMatrixDto {
	
	private Long id;
	private Long quotationId;
	private Long valuationTypeId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date valuationDate;
	private Float currentServiceLiability;
	private Float pastServiceLiability;
	private Float futureServiceLiability;
	private Float premium;
	private Float gst;
	private Double totalPremium;
	private Float amountReceived;
	private Float amountPayable;
	private Float balanceToBePaid;
	private Double totalSumAssured;
	private String createdBy;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedDate;
	private Long tmpPolicyId;
}
