package com.lic.epgs.gratuity.policy.member.dto;

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
public class PolicyMemberSearchDto {
	private Long policyId;
	private String licId;
	private String employeeCode;
	private String firstName;
	private String middleName;
	private String lastName;
	private String Name;
	private Long memberStatus;
	//private String proposalNumber;
	//private String loanNumber;
	private String panNumber;
	private String aadharNumber;
}
