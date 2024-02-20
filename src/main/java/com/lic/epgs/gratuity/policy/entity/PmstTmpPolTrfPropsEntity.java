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
@Table(name="PMST_TMP_POL_TRF_PROPS")
public class PmstTmpPolTrfPropsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_POL_TRF_PROPS_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_POL_TRF_PROPS_ID_SEQ", sequenceName = "PMST_TMP_POL_TRF_PROPS_ID_SEQ", allocationSize = 1)
	@Column(name="POL_TRF_PROPS_ID")
	private Long id;
	
	@Column(name="POL_TRF_REQUEST_NUMBER")
	private Integer polTrfRequestNumber;
	
	@Column(name="POL_TRF_REQUEST_DATE")
	private Date polTrfrequestDate;
	
	@Column(name="POL_TRF_PROCESSING_DATE")
	private Date poltrfProcessingDate;
	
	@Column(name="PMST_TMP_POLICY_ID")
	private Long pmstTmpPolicyID;
	
	@Column(name="DESTINATION_UNIT_CODE")
	private String destinationUnitCode;
	
	@Column(name="TOTAL_FUND_VALUE")
	private Integer totalFundValue;
	
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
