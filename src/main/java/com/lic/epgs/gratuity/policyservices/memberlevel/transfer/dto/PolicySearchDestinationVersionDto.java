package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import java.util.Date;
import java.util.List;

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
public class PolicySearchDestinationVersionDto {
	private String policyNumber;
	private String userName;
	private String unitCode;
	private String userType;
	private String proposalNumber;
	private String customerName;
	private String intermediaryName;
	private Long lineOfBusinessId;
	private Long productId;
	private Long productVariant;
	private Long unitOffice;
//	private Long policyStatus;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyStartDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyEndDate;
	private Long taggedStatusId;
	private Long policyStatusId;
	private String pan;
	private Long policySubStatusId;
	private List<Long> policyStatusIdList;
}
