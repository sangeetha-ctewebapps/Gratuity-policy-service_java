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
@Table(name = "PMST_MIDLEAVER_PROPS")
public class PmstMidleaverPropsEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PMST_MIDLEAVER_PROPS_ID")
    private Long id;
	
	@Column(name = "TMP_POLICY_ID")
	private Long tmpPolicyId;
	
	@Column(name = "BULK_OR_INDIVIDUAL")
	private String bulkOrIndividal;
	
	@Column(name = "SERVICE_NUMBER")
	private String serviceNumber;
	
	@Column(name="IS_PREMIUM_REFUND")
	private Boolean isPremiumRefund;
	
	@Column(name = "PAYOUT_NUMBER")
	private Long payoutNumber;
	
	@Column(name = "PAYOUT_DATE")
	private Date payoutDate;
	
	@Column(name = "STATUS_ID")
	private Long statusId;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "CREATED_BY")
	private String userName;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

}
