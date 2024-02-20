package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "PSTG_CONTRIBUTION_DETAIL")
public class PolicyContriDetailEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PSTG_CONTRIB_DTL_ID_SEQ")
	@SequenceGenerator(name = "PSTG_CONTRIB_DTL_ID_SEQ", sequenceName = "PSTG_CONTRIB_DTL_ID_SEQ", allocationSize = 1)
	@Column(name = "CONTRIBUTION_DETAIL_ID")
	private Long id;

	@Column(name = "MASTER_QUOTATION_ID")
	private Long masterQuotationId;

	@Column(name = "TMP_POLICY_ID")
	private Long tmpPolicyId;
	
	@Column(name = "CHALLAN_NO")
	private String challanNo;
	
	@Column(name = "LIFE_PREMIUM")
	private Double lifePremium;

	@Column(name = "GST")
	private Double gst;

	@Column(name = "PAST_SERVICE")
	private Double pastService;

	@Column(name = "CURRENT_SERVICE")
	private Double currentServices;
	
	@Column(name = "STAMP_TYPE")
	private String stampType;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String ModifiedBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name ="POLICY_ID")
	private Long policyId;
	
	@Column(name="ENTRY_TYPE")
	private String entryType;

	@Column(name="ADJUSTMENT_FOR_DATE")
	private Date adjustmentForDate;
	
	@Column(name="FINANCIAL_YEAR")
	private String financialYear;
}
