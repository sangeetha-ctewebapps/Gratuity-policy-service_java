package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Nandini.R Date:12-08-2023
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TRANSFER_MEMBER_POLICY_DETAIL")

public class TransferMemberPolicyDetailEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANSFER_MEMBER_POLICY_DETAIL_ID_SEQUENCE")
	@SequenceGenerator(name = "TRANSFER_MEMBER_POLICY_DETAIL_ID_SEQUENCE", sequenceName = "TRANSFER_MEMBER_POLICY_DETAIL_ID_SEQUENCE", allocationSize = 1)
	@Column(name = "TRANSFER_MEMBER_POLICY_DETAIL_ID", unique = true, nullable = false, updatable = false, length = 10)
	private Long transferMemberPolicyDetailId;

	@Column(name = "TRANSFER_REQUISITION_ID")
	private Long transferRequisitionId;

	@Column(name = "TRANSFER_POLICY_DETAIL_ID")
	private Long transferPolicyDetailId;

	@Column(name = "LIC_ID")
	private Long licId;

	@Column(name = "MEMBER_NAME")
	private String memberName;

	@Column(name = "MEMBER_STATUS")
	private String memberStatus;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "IST")
	@Column(name = "DATE_OF_BIRTH")
	private Date dateOfBirth;

	@Column(name = "PAN_NUMBER")
	private String panNumber;

	@Column(name = "UNIT_IN")
	private String unitIn;
	
	@Column(name = "UNIT_OUT")
	private String unitOut;

	@Column(name = "MEMBER_ID")
	private Long memberId;

	@Column(name = "CATEGORY_OUT")
	private String categoryOut;
	
	@Column(name = "CATEGORY_IN")
	private String categoryIn;

	@Column(name = "ACCRUED_GRATUITY_EXISTING")
	private Long accruedGratuityExisting;
	
	@Column(name = "ACCRUED_GRATUITY_NEW")
	private Long accruedGratuityNew;
	
	@Column(name = "IS_ACCRUED_GRATUITY_MODIFIED")
	private String isAccruedGratuityModified;
	
	@Column(name = "IS_PREMIUM_REFUND")
	private String isPremiumRefund;

	@Column(name = "LIFE_COVER_BENEFIT")
	private String lifeCoverBenefit;

	@Column(name = "PREMIUM_AMOUNT")
	private Double premiumAmount;

	@Column(name = "GST_ON_PREMIUN")
	private Double gstOnPremium;
	
	@Column(name = "GST_ON_PREMIUN_IN")
	private Double gstOnPremiumIn;

	@Column(name = "COMPLETED_YEARS_OF_SERVICE")
	private Long completedYearsOfService;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "IST")
	@Column(name = "DATE_OF_JOINING")
	private Date dateOfJoining;

	@Column(name = "LAST_DRAWN_SALARY")
	private Long lastDrawnSalary;
	
	@Column(name = "ACCRUED_GRATUITY_IN")
	private Double accruedGratuityIn;
	
	@Column(name = "LIC_PREMIUM_IN")
	private Double licPremiumIn;
	
	@Column(name = "PAST_SERVICE_BENEFIT")
	private Double pastServiceBenefit;
	
	@Column(name = "CURRENT_SERVICE_BENEFIT")
	private Double currentServiceBenefit;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name = "TRANSFER_OUT_EFFECTIVE_DATE")
	private Date transferOutEffectiveDate;

	@Column(name = "RETAIN_LIC_ID")
	private String retainLicId;
	
	@Column(name = "IS_LIC_ID_EXIST")
	private String isLicIdExist;
	
	@Column(name = "TO_MPH_REFUND")
	private String toMphRefund;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name = "ADJUSTED_FOR_DATE")
	private Date adjustedForDate;
	
				
	@Column(name = "CREATED_BY")
	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "IST")
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "IST")
	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

}
