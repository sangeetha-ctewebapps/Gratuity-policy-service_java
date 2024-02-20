package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PSTG_DEPOSIT")
public class PolicyDeposit {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PSTG_DEPOSIT_ID_SEQ")
	@SequenceGenerator(name = "PSTG_DEPOSIT_ID_SEQ", sequenceName = "PSTG_DEPOSIT_ID_SEQ", allocationSize = 1)
	
	@Column(name = "DEPOSIT_ID")
	private Long id;
	@Column(name = "POLICY_ID")
	private Long policyId;
	@Column(name = "MASTER_POLICY_ID")
	private Long masterPolicyId;
	
	@Column(name = "TMP_POLICY_ID")
	private Long tmpPolicyId;
	
	@Column(name = "ADJUSTMENT_AMOUNT")
	private Double adjustmentAmount;
	@Column(name = "ADJ_CON_ID")
	private Long  adjConId;
	@Column(name = "AVAILABLE_AMOUNT")
	private Double availableAmount;
	@Column(name = "CHALLAN_NO")
	private String challanNo;
	@Column(name = "CHEQUE_REALISATION_DATE")
	private Date chequeRealistionDate;
	@Column(name = "COLLECTION_DATE")
	private Date collectionDate;
	@Column(name = "COLLECTION_NO")
	private Long collectionNo;
	@Column(name = "COLLECTION_STATUS")
	private String collectionStatus;
	@Column(name = "CONTRIBUTION_TYPE")
	private String contributionType;
	@Column(name = "DEPOSIT_AMOUNT")
	private Double depositAmount;
	@Column(name = "IS_DEPOSIT")
	private Boolean isDeposit;
	@Column(name = "REG_CON_ID")
	private Long regConId;
	@Column(name = "REMARK")
	private String remark;
	@Column(name = "STATUS")
	private String status;
	@Column(name = "TRANSACTION_MODE")
	private String transactionMode;
	@Column(name = "ZERO_ID")
	private Long zeroId;
	@Column(name = "IS_ACTIVE")
	private boolean isActive;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATED_DATE")
	private Date createDate;
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	@Column(name="CONTRIBUTION_DETAIL_ID")
	private Long contributionDetailId;
	
	
	
	
	

}
