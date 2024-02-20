package com.lic.epgs.gratuity.policy.claim.entity;

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
@Table(name = "PMST_TMP_CLAIM_BENEFICIARY")
public class TempPolicyClaimBeneficiaryEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_CLAIM_BENEFICI_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_CLAIM_BENEFICI_ID_SEQ", sequenceName = "PMST_TMP_CLAIM_BENEFICI_ID_SEQ", allocationSize = 1)
	@Column(name = "CLAIM_BENEFICIARY_ID")
	private Long id;
	
	@Column(name = "CLAIM_PROPS_ID")
	private Long claimPropsId;
	
	@Column(name = "BENEFICIARY_TYPE")
	private Long beneficiaryType;
	
	@Column(name = "MPH_TMP_BANK_ID")
	private String mphTmpBankId;
	
	@Column(name = "MEMBER_TMP_BANK_ID")
	private Long memberTmpBankId;
	
	@Column(name = "NOMINEE_TMP_BANK_ID")
	private Long nomineeTmpBankId;

	@Column(name = "CREATED_BY", length = 128)
	private String createdBy;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY", length = 128)
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "APPOINTEE_TMP_BANK_ID")
	 private Long appointeeTmpBankId;
	
	@Column(name = "PARENT_ID")
	private Long parentId;


}
