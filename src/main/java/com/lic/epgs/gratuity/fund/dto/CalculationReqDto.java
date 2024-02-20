package com.lic.epgs.gratuity.fund.dto;

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
public class CalculationReqDto {
	private Long policyId;
	private String policyNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date transactionDate;
	private String variant;
	private String policyType = "DB";
	private boolean isBatch;
	private boolean isAuto;
	private boolean recalculate;
	private String unitId;
	@Override
	public String toString() {
		return "CalculationReqDto [policyId=" + policyId + ", policyNumber=" + policyNumber + ", transactionDate="
				+ transactionDate + ", variant=" + variant + ", policyType=" + policyType + ", isBatch=" + isBatch
				+ ", isAuto=" + isAuto + ", recalculate=" + recalculate + ", unitId=" + unitId + "]";
	}
	
	
}
