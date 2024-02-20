package com.lic.epgs.gratuity.policy.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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
@Table(name="PMST_TMP_CONVERSION_PROPS")
public class PmstTmpConversionPropsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_CONVERSION_PROPS_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_CONVERSION_PROPS_ID_SEQ", sequenceName = "PMST_TMP_CONVERSION_PROPS_ID_SEQ", allocationSize = 1)
	@Column(name="TMP_CONVERSION_PROPS_ID")
	private Long id;
	
	@Column(name="CONVERSION_REQUEST_NUMBER")
	private Long conversionRequestNumber;
	
	@Column(name="CONVERSION_REQUEST_DATE")
	private Date conversionRequestDate;
	
	@Column(name="POLICY_CONVERSION_DATE")
	private Date policyConversionDate;
	
	@Column(name="TOTAL_FUND_VALUE")
	private Double totalFundValue;
	
	@Column(name="PMST_TMP_POLICY_ID")
	private Long tmpPolicyID;
	
	@Column(name="NEW_MASTER_POLICY_ID")
	private Long newMasterPolicyID;
	
	@Column(name ="PMST_POLICY_ID")
	private Long pmstPolicyId;
	
	@Column(name="STATUS_ID")
	private Long statusID;
	
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
	
	@Column(name = "valuation_type")
	private String valuationType;
	
	@Column(name = "variant")
	private String variant;
	
	@Column(name = "previous_fund_balance")
	private String previousFundBalance;
	
	@Column(name="CONVERSION_PROCESSING")
	private Boolean conversionProcessing; 
	
	@Column(name="CONVERSION_TYPE")
	private Long conversionType;
}
