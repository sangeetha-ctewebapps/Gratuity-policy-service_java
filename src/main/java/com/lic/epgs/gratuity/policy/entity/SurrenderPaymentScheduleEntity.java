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
@Table(name="SURRENDER_PAYMENT_SCHEDULE")
public class SurrenderPaymentScheduleEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SURRENDER_PAYMENT_SCHEDULE_ID_SEQ")
	@SequenceGenerator(name = "SURRENDER_PAYMENT_SCHEDULE_ID_SEQ", sequenceName = "SURRENDER_PAYMENT_SCHEDULE_ID_SEQ", allocationSize = 1)
	@Column(name="PAYMENT_SCHEDULE_ID")
	private Long id;
	
	@Column(name="SURRENDER_PROPS_ID")
	private Integer surrenderPropsID;
	
	@Column(name="SCHEDULED_DATE")
	private Date scheduledDate;
	
	@Column(name="SURRENDER_AMOUNT")
	private Integer surrenderAmount;
	
	@Column(name="PAYMENT_STATUS")
	private Integer paymentStatus;
	
	@Column(name="IS_ACTIVE")
	private Integer isActive;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	

}
