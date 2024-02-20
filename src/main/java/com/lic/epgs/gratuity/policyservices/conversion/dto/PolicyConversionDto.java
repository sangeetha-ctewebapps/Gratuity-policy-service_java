package com.lic.epgs.gratuity.policyservices.conversion.dto;

import java.io.Serializable;
import java.util.Date;

import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolicyConversionDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long conversionId;
	private Long conversionRequestNumber;
	private Date conversionRequestDate;
	private Date policyConversionDate;
	private Double totalFundValue;
	private Long tmpPolicyID;
	private Long newMasterPolicyID;
	private Long statusID;
	private Boolean isActive;
	private String createdBy;
	private String modifiedBy;
	private String valuationType;
	private String variant;
	private String previousFundBalance;
	private Boolean conversionProcessing; 
	private Long conversionType;
	private Long pmstPolicyId;
	private RenewalPolicyTMPDto renewalPolicyTMPDto;
}
