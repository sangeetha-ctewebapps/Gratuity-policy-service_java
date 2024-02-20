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
@Table(name="TASK_PROCESS")
public class TaskProcessEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_PR_ID_SEQ")
	@SequenceGenerator(name = "TASK_PR_ID_SEQ", sequenceName = "TASK_PR_ID_SEQ", allocationSize = 1)
	
	@Column(name="TASK_PR_ID")
	private Long id;
	
	@Column(name="PROCESS_NAME")
	private String processName;
	
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
