package com.lic.epgs.gratuity.policy.claim.dto;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PdfGeneratorForTableDto {	
	private String licId;
	private String empName;
	private String empCode;
	private Double lcSumAssured; 
	private Double svMatWthd;
	private Double refund;
	private Double othAmt;
	private Double total;
	private String ref;
	private String date;
}
