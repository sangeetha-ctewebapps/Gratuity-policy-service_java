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
@Table(name = "MEMBER_CATEGORY")
public class MemberCategoryEntityVersion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_CATEGORY_ID_SEQ")
	@SequenceGenerator(name = "MEMBER_CATEGORY_ID_SEQ", sequenceName = "MEMBER_CATEGORY_ID_SEQ", allocationSize = 1)
	@Column(name = "MEMBER_CATEGORY_ID")
	private Long id;
	
	@Column(name = "QSTG_QUOTATION_ID")
	private Long qstgQuoationId;

	@Column(name = "QMST_QUOTATION_ID")
	private Long qmstQuotationId;

	@Column(name = "PSTG_POLICY_ID")
	private Long pstgPolicyId;
	
	@Column(name = "PMST_POLICY_ID")
	private Long pmstPolicyId;

	@Column(name = "PMST_TMP_POLICY_ID")
	private Long pmstTmpPolicy;

	@Column(name = "PMST_HIS_POLICY_ID")
	private Long pmstHisPolicyId;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	

}
