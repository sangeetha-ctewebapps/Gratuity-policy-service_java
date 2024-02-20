package com.lic.epgs.gratuity.policy.renewalpolicy.aocmcalculation.entity;

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
@Table(name = "AOCM_EXPENSE_RATE")
public class AOCMExpenseRateEntity {
	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AOCM_EXPENSE_RATE_ID_SEQ")
	@SequenceGenerator(name = "AOCM_EXPENSE_RATE_ID_SEQ", sequenceName = "AOCM_EXPENSE_RATE_ID_SEQ", allocationSize = 1)
	@Column(name = "AOCM_EXPENSE_RATE_ID")
	private Long Id;
	
	@Column(name = "FROM_GROUP_SIZE")
	private Long fromGroupSize;
	
	@Column(name = "VARIANT")
	private String variant;
	
	@Column(name ="TO_GROUP_SIZE")
	private Long toGroupSize;
	
	@Column(name ="PREMIUM_RELATED_EXPENSE")
	private Double premiumRelatedExpense;
	

	@Column(name ="SA_RELEATED_EXPENSE")
	private Double saReleatedExpense;
	
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "CREATED_BY", length = 128)
	private String createdBy;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY", length = 128)
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
}
