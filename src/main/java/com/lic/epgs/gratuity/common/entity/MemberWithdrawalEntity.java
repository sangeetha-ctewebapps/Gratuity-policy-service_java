package com.lic.epgs.gratuity.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "MEMBER_WITHDRAWAL_RATE")
public class MemberWithdrawalEntity {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_WITHDRAWAL_RATE_ID_SEQ")
	@SequenceGenerator(name = "MEMBER_WITHDRAWAL_RATE_ID_SEQ", sequenceName = "MEMBER_WITHDRAWAL_RATE_ID_SEQ", allocationSize = 1)
	@Column(name = "MEMBER_WITHDRAWAL_RATE_ID")
	private Long id;
	
	@Column(name="AGE_FROM")
	private Long ageFrom;

	@Column(name = "AGE_TO")
	private Long ageTo;
	
	@Column(name="RATE")
	private Float rate;
	
	@Column(name="EFFECTIVE_FROM")
	private Date effectiveFrom;
	
	@Column(name="EFFECTIVE_TO")
	private Date effectiveTo;
	
	@Column(name="IS_ACTIVE")
	private boolean isActive;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	
}
