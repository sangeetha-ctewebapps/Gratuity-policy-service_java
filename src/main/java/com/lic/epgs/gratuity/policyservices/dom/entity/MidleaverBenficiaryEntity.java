package com.lic.epgs.gratuity.policyservices.dom.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "PMST_MIDLEAVER_BENFICIARY")
@Data
public class MidleaverBenficiaryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MIDLEAVER_BENFICIARY_ID")
	private Long id;
	
	@Column(name="TMP_MIDLEAVER_PROPS_ID")
	private Long tmpMlPropsId;
	
	@Column(name="MPH_TMP_BANK_ID")
	private Long mphTmpBankId;
	
	@Column(name="NEFT_STATUS_ID")
	private Long neftStatusId; 
	
	@Column(name="PARENT_ID")
	private Long parentId;
	
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
