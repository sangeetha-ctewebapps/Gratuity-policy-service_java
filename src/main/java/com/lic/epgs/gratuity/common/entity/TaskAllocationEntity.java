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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="TASK_ALLOC")
public class TaskAllocationEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_ALLOC_ID_SEQ")
	@SequenceGenerator(name = "TASK_ALLOC_ID_SEQ", sequenceName = "TASK_ALLOC_ID_SEQ", allocationSize = 1)
	@Column(name="TASK_ALLOC_ID")
	private Long id;
	
	@Column(name="TASK_NUMBER")
	private Long taskNumber;
	
	@Column(name="MAKER_ID")
	private String makerId;
	
	@Column(name="CHECKER_ID")
	private String checkerid;
	
	@Column(name="TASK_STATUS")
	private String taskStatus;
	
	@Column(name="REQUEST_ID")
	private String requestId;
	
	@Column(name="OFFICE_ID")
	private Long officeId;
	
	@Column(name="LOCATION_TYPE")
	private String locationType;
	
	@Column(name="TASK_PROCESS_TASK_PR_ID")
	private Long taskProcessTaskPrId;
	
	@Column(name="FINANCIAL_APPROVER_ID")
	private String financialApproverId;
	
	@Column(name="MODULE_PRIMARY_ID")
	private Long modulePrimaryId;
	
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
