package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

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
public class DepositLockDto {

	private String collectionNo;
	private Integer challanNo;
	private String dueMonth;
	private String refNo;
	private String userCode;
	private String productCode;
	private String variantCode;

}
