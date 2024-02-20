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
public class NewQuotationDto {
	
	private String customerCode;
	private Long unitOfficeId;
	private Long productId;
	private Long productVariantId;
	private Long businessTypeId;
	private Long riskGroupId;
	private Long groupSizeId;
	private Long groupSumAssuredId;
	private Long frequencyId;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfCommencement;
	private Long gstApplicableId;
	private Long categoryForGstNonApplicableId;
	 private  Long payto;
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private String createdBy;
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private String modifiedBy;
	private String unitCode;
	private  String industryType;
	
}
