package com.lic.epgs.gratuity.quotation.member.dto;

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
public class MemberSearchDto {
	private Long quotationId;
	private String licId;
	private String employeeCode;
	private String name;
	private String proposalNumber;
	private String loanNumber;
	private String panNumber;
	private String aadharNumber;
	private Long taggedStatusId;
	 private Long statusId;
}
