package com.lic.epgs.gratuity.quotation.dto;

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
public class QuotationSearchDto {

	private String number;
	private String proposalNumber;
	private String customerCode;
	private Long unitOfficeId;
	private Long productId;
	private Long productVariantId;
	private Long businessTypeId;
	private Long statusId;
	private Long subStatusId;
	private String policyNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date fromDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date toDate;
	private Long taggedStatusId;
	private String sortBy;
	private String sortOrder;
	private String userName;
	private String unitCode;
	private String userType;
	private String moduleType;
	
}
