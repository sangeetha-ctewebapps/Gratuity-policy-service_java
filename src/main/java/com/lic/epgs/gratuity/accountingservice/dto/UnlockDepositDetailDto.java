package com.lic.epgs.gratuity.accountingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnlockDepositDetailDto {

	
	private Integer challanNo;
	private String collectionNo;
	private String productCode;
	private String userCode;
	private String variantCode;
	


}
