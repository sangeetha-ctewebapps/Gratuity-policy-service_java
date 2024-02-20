package com.lic.epgs.gratuity.policy.renewalpolicy.entitiy;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.lic.epgs.gratuity.policy.claim.entity.PolicyTempSearchEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="PMST_RENEWAL_REMINDERS")
public class PolicyRenewalRemainderSearchEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RENEWAL_REMINDER_ID_SEQ")
	@SequenceGenerator(name = "RENEWAL_REMINDER_ID_SEQ", sequenceName = "RENEWAL_REMINDER_ID_SEQ", allocationSize = 1)
	@Column(name = "RENEWAL_REMINDER_ID")
	private Long id;
	
	@Column(name = "ANNUAL_RENEWAL_DATE")
	private Date annualrenewaldate;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POLICY_ID")
	private RenewalPolicySearchEntity policyTmp; 
}
