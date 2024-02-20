package com.lic.epgs.gratuity.policy.renewalpolicy.member.dto;

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
public class RenewalMemberImportDto {
	
	  private Long policyId;
		private Long tmpPolicyId;
		private String username;
		private Long batchId;
		private String renewalUploadType;
		

}
