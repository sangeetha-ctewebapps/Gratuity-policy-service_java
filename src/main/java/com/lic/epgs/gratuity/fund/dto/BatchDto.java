package com.lic.epgs.gratuity.fund.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;

/**
*
* @author Muruganandam
*
*/

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(alphabetic = true)
public class BatchDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long policyId;
	private String policyNumber;
	private String trnxDate;
	private String variant;
	private String policyType;
	private String unitId;
	
	private @Default Boolean isBatch = false;
	private @Default Boolean isAuto = true;
	private @Default Boolean recalculate = false;
	private @Default Boolean setOpeningBalance = false;
	
	@JsonProperty(value = "licId")
	private String memberId;
	private @Default String memberStatus = "Active";
}
