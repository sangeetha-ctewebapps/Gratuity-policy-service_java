package com.lic.epgs.gratuity.policy.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.policyservices.premiumcollection.dto.PremiumCollectionDuesDto;
import com.lic.epgs.gratuity.simulation.dto.DepositAdjustementDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyContributionDetailDto {
	private Long id;
	private Long masterQuotationId;
	private Long tmpPolicyId;
	private Double lifePremium;
	private String challanNo;
	private Double gst;
	private Double pastService;
	private Double currentServices;
	private String stampType;
	private boolean isActive;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date createdDate;
	private String ModifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date modifiedDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date proposalSignDate;
	private Long policyId;

	private List<DepositAdjustementDto> depositAdjustmentDto;
	private String entryType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date adjustmentForDate;
	private List<PremiumCollectionDuesDto> premiumCollectionDuesDto;
	private Double lateFee;
	private Double gstOnLateFee;
	private Boolean waiveLateFee;
	private String financialYear;
	private Double pastServiceBalance;
	private Double currentServiceBalance;

}
