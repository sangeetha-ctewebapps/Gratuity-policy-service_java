package com.lic.epgs.gratuity.policyservices.dom.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="PMST_MIDLEAVER_MEMBER_PROPS")
public class PmstMidleaverMemberPropsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="MIDLEAVER_MEMBER_PROPS_ID")
	private Long id;
	
	@Column(name="TMP_MEMBER_ID")
	private Long tmpMemberId;
	
	@Column(name="LEAVING_DATE")
	private Date leavingDate;
	
	@Column(name="LEAVING_REASON_ID")
	private Long leavingReasonId;
	
	@Column(name="LEAVING_REASON_OTHER")
	private String leavingReasonOther; 
	
	@Column(name="LAST_PREM_COLLECTED_DATE")
	private Date lastPremiumCollectedDate;
	
	@Column(name="LAST_PREM_COLLECTED_AMT")
	private Double lastPremiumCollectedAmount;
	
	@Column(name="REFUND_PREM_AMOUNT")
	private Double refundPremiumAmount;
	
	@Column(name="REFUND_GST_AMOUNT")
	private Double refundGstAmount;
	
	@Column(name="IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
}
