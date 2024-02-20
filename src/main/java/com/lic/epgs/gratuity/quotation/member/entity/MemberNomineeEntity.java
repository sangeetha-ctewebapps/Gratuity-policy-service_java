package com.lic.epgs.gratuity.quotation.member.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ismail Khan A
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "QSTG_MEMBER_NOMINEE")
public class MemberNomineeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QSTG_MEMBER_NOMINEE_ID_SEQ")
	@SequenceGenerator(name = "QSTG_MEMBER_NOMINEE_ID_SEQ", sequenceName = "QSTG_MEMBER_NOMINEE_ID_SEQ", allocationSize = 1)
	@Column(name = "MEMBER_NOMINEE_ID")	
	private Long id;
	
	@ManyToOne
    @JoinColumn(name="MEMBER_ID", nullable=false)
    private MemberEntity member;
	
	@Column(name = "CODE")	private String code;
	@Column(name = "NAME")	private String name;
	@Column(name = "RELATIONSHIP_ID")	private Long relationshipId;
	@Column(name = "CONTACT_NUMBER")	private String contactNumber;
	@Column(name = "DATE_OF_BIRTH")	private Date dateOfBirth;
	@Column(name = "PAN_NUMBER")	private String panNumber;
	@Column(name = "AADHAR_NUMBER")	private String aadharNumber;
	@Column(name = "BANK_ACCOUNT_NUMBER")	private String bankAccountNumber;
	@Column(name = "BANK_ACCOUNT_TYPE_ID")	private Long bankAccountTypeId;
	@Column(name = "IFSC_CODE")	private String ifscCode;
	@Column(name = "BANK_NAME_ID")	private Long bankNameId;
	@Column(name = "BANK_BRANCH_ID")	private Long bankBranchId;
	@Column(name = "PERCENTAGE")	private double percentage;
	@Column(name = "IS_ACTIVE")	private Boolean isActive;
	@Column(name = "CREATED_BY")	private String createdBy;
	@Column(name = "CREATED_DATE")	private Date createdDate;
	@Column(name = "MODIFIED_BY")	private String modifiedBy;
	@Column(name = "MODIFIED_DATE")	private Date modifiedDate;

	
	@Column(name = "BANK_NAME")
	private String bankName;

	@Column(name = "BANK_BRANCH")
	private String bankBranch;
	
	@Column(name="GENDER_ID")
	private Long genderId;
}
