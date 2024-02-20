package com.lic.epgs.gratuity.policy.renewalpolicy.entitiy;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue; 
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.lic.epgs.gratuity.policy.claim.entity.TempMemberSearchEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PMST_POLICY")
public class RenewalPolicySearchEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_POLICY_SEQ")
	@SequenceGenerator(name = "PMST_POLICY_SEQ", sequenceName = "PMST_POLICY_SEQ", allocationSize = 1)
	@Column(name = "POLICY_ID")
	private Long id;
	
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name = "CUSTOMER_NAME")
	private String customerName;
	
	@Column(name = "ANNUAL_RENEWAL_DATE")
	private Date annualrenewaldate;
	
	@Column(name = "INTERMEDIARY_NAME")
	private String intermediaryName;
	
	@Column(name = "LINE_OF_BUSINESS")
	private String lineOfBusiness;

	@Transient
	@Column(name = "PRODUCT_NAME")
	private String productName;
	
	@Transient
	@Column(name = "PRODUCT_VARIANT")
	private String productVariant;
	
	@Transient
	@Column(name = "UNIT_OFFICE")
	private String unitOffice;
	
	@Column(name = "POLICY_STATUS_ID")
	private Long policyStatusId;
	
	@Column(name = "POLICY_TAGGED_STATUS_ID")
	private Long policyTaggedStatusId;
	
	@Column(name = "POLICY_START_DATE")
	private Date policyStartDate;
	
	@Column(name = "POLICY_END_DATE")
	private Date policyEndDate;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Transient
	@Column(name = "UNIT_ID")
	private String unitId;
	
	@Column(name = "UNIT_CODE")
	private String unitCode;
	
	@Column(name = "PRODUCT_ID")
	private Long productId;
	
	@Column(name = "PRODUCT_VARIANT_ID")
	private Long productVariantId;
	
	@OneToMany(mappedBy = "policyTmp", cascade = CascadeType.ALL)
	private List<PolicyRenewalRemainderSearchEntity> policySearch; 
	

//	@OneToMany(mappedBy = "ardTmp", cascade = CascadeType.ALL)
//	private List<PolicyRenewalRemainderSearchEntity> policySearchannualrenewaldate;  
//	
	}
