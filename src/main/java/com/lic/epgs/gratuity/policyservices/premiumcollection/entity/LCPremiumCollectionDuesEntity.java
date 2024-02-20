package com.lic.epgs.gratuity.policyservices.premiumcollection.entity;

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
@Table(name = "PMST_LC_PREM_COLL_DUES")
public class LCPremiumCollectionDuesEntity {	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "LC_PREM_COLL_DUES_ID")
	private Long id;
	
	@Column(name="LC_PREM_COLL_PROPS_ID")
	private Long lcPremCollPropsId;
	@Column(name="DUE_DATE")
	private Date dueDate;
	@Column(name="LC_PREMIUM_AMOUNT")
	private Double lcPremiumAmount;
	@Column(name="GST_ON_LC_PREMIUM")
	private Double gstOnLCPremium;
	@Column(name="LATE_FEE")
	private Double lateFee;
	@Column(name="GST_ON_LATE_FEE")
	private Double gstOnLateFee;
	@Column(name="TOTAL_DUE_AMT")
	private Double totalDueAmt;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
}
