package com.lic.epgs.gratuity.policy.integration.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author MOHANRAJ
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerBasicDetailsDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer customerId;
	private String customerCode;
	private Integer groupId;
	private String customerName;
	private Long customerStatus;
	private Long workflowStatus;
	private Long industryType;
	private String riskGroup;
	private Long customerType;
	private Long customerEntity;
	private String pan;
	private String tan;
	private String gstin;
	private String cin;
	private String customerLEI;
	private String alternateId;
	private String alternateIdReference;
	private String referenceNumber;
	private String countryCode;
	private String stdCode;
	private String landlineNumber;
	private String extNumber;
	private String emailID;
	private String faxNumber;
	private String blacklisted;
	private String privilegeCustomer;
	private String communicationChannelSMS;
	private String communicationChannelEmail;
	private String communicationChannelFax;
	private String communicationChannelPostal;
	private String communicationLanguage;
	private String rejectionReasonCode;
	private String rejectionRemarks;
	private String remarks;
	private Long channelColorCode;
	private Long channelUserId;
	private String createdBy;
	private String createOn;
	private String modifiedBy;
	private String modifiedOn;
	private String isActive;
//	private List<TrustDetailsDto> trustDetailList;
//	private transient List<SubCustomerBasicDetailsTempEntity> subCustomerBasicDetailsTempEntity;
	private String customerStatusName;
	private String workflowStatusName;
	private String industryTypeName;
	private String customerTypeName;
	private String customerEntityName;
	private Boolean isApprovedOrReject;
	private String tempCustomerId;
	private String unitCode;
	private String reopen;
}
