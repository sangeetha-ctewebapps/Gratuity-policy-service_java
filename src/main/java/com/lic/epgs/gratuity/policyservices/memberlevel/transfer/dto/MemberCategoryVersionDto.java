package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto;

import java.util.Date;
import java.util.List;

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
public class MemberCategoryVersionDto {
	private Long id;
	private Long qstgQuoationId;
	private Long qmstQuotationId;
	private Long pstgPolicyId;
	private Long pmstPolicyId;
	private Long pmstTmpPolicy;
	private Long pmstHisPolicyId;
	private String name;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
}
