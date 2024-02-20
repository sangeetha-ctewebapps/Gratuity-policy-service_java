package com.lic.epgs.gratuity.policyservices.merger.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PolicyLevelMergerSearchDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String policyNumber;
	private String mphCode;
	private String mphName;
	private String roleType;
	private String product;
	private String lineOfBusiness;
	private String mergeStatus;
	private String unitCode;
	private String mergeId;
	

}
