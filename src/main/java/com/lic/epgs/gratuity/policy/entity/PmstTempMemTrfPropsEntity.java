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
@Table(name="PMST_TMP_MEM_TRF_PROPS")
public class PmstTempMemTrfPropsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_MEM_TRF_PROPS_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_MEM_TRF_PROPS_ID_SEQ", sequenceName = "PMST_TMP_MEM_TRF_PROPS_ID_SEQ", allocationSize = 1)
	@Column(name="MEM_TRF_PROPS_ID")
	private Long id;
	
	@Column(name="TRF_REQUEST_NUMBER")
	private Integer trfRequestNumber;
	
	@Column(name="TRF_REQUEST_DATE")
	private Date trfRequestDate;
	
	@Column(name="TRF_PROCESSING_DATE")
	private Date trfProcessingDate;
	
	@Column(name="TRANSFER_TYPE")
	private Integer transferType;
	
	@Column(name="TOTAL_FUND_VALUE")
	private Integer totalFundValue;
	
	@Column(name="SOURCE_TMP_POLICY_ID")
	private Long sourceTmpPolicyID;
	
	@Column(name="DESTINATION_TMP_POLICY_ID")
	private Long destinationTmpPolicyID;
	
	@Column(name="STATUS_ID")
	private Integer StatusID;
	
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
