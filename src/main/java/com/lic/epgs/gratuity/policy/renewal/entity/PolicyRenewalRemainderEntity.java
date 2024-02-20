package com.lic.epgs.gratuity.policy.renewal.entity;

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
@Table(name = "PMST_RENEWAL_REMINDERS")
public class PolicyRenewalRemainderEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RENEWAL_REMINDER_ID_SEQ")
	@SequenceGenerator(name = "RENEWAL_REMINDER_ID_SEQ", sequenceName = "RENEWAL_REMINDER_ID_SEQ", allocationSize = 1)
	@Column(name = "RENEWAL_REMINDER_ID")
	private Long id;
	
	@Column(name = "ANNUAL_RENEWAL_DATE")
	private Date annualrenewaldate;
	
	@Column(name = "CREATED_BY", length = 128)
	private String createdBy;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "EMAIL_RESPONSE")
	private String emailResponse;
	
	@Column(name = "EMAIL_SUBJECT")
	private String emailSubject;
	
	@Column(name = "EMAIL_To")
	private String emailTo;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "MODIFIED_BY", length = 128)
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name = "REMINDED_DATE_TIME")
	private Date remindedDateTime;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
}
