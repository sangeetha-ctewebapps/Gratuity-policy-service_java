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
@Table(name="PMST_TMP_SURRENDER_PROPS")
public class PmstTmpSurrenderPropsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_SURRENDER_PROPS_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_SURRENDER_PROPS_ID_SEQ", sequenceName = "PMST_TMP_SURRENDER_PROPS_ID_SEQ", allocationSize = 1)
	@Column(name="SURRENDER_PROPS_ID")
	private Long id;
	
	@Column(name="SURRENDER_REQUEST_NUMBER")
	private Integer surrenderRequestNumber;
	
	@Column(name="SURRENDER_REQUEST_DATE")
	private Date surrenderRequestDate;
	
	@Column(name="SURRENDER_PROCESSING_DATE")
	private Date surrenderProcessingDate;
	
	@Column(name="PMST_TMP_POLICY_ID")
	private Long pmstTempPolicyID;
	
	@Column(name="SURRENDER_TYPE")
	private Integer surrenderType;
	
	@Column(name="SURRENDER_AMOUNT")
	private Integer surrenderAmount;
	
	@Column(name="NO_OF_INSTALLMENTS")
	private Integer noOfInstallments;
	
	@Column(name="STATUS_ID")
	private Integer statusID;
	
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
