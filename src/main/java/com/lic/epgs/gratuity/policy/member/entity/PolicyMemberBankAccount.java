package com.lic.epgs.gratuity.policy.member.entity;

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
@Table(name = "PMST_MEMBER_BANK_ACCOUNT")
public class PolicyMemberBankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_MBR_BNK_ACC_ID_SEQ")
	@SequenceGenerator(name = "PMST_MBR_BNK_ACC_ID_SEQ", sequenceName = "PMST_MBR_BNK_ACC_ID_SEQ", allocationSize = 1)
	@Column(name = "MEMBER_BANK_ACCOUNT_ID")	private Long id;
	
	@ManyToOne
    @JoinColumn(name="MEMBER_ID", nullable=false)
    private PolicyMemberEntity member;
	
	@Column(name = "BANK_NAME")	private String bankName;
	@Column(name = "BANK_ACCOUNT_NUMBER")	private String bankAccountNumber;
	@Column(name = "ACCOUNT_TYPE")	private String accountType;
	@Column(name = "BANK_BRANCH")	private String bankBranch;
	@Column(name = "BANK_ADDRESS")	private String bankAddress;
	@Column(name = "COUNTRY_CODE")	private String countryCode;
	@Column(name = "STD_CODE")	private String stdCode;
	@Column(name = "IFSC_CODE_AVAILABLE")	private String ifscCodeAvailable;
	@Column(name = "IFSC_CODE")	private String ifscCode;
	@Column(name = "EMAIL_ID")	private String emailId;
	@Column(name = "LANDLINE_NUMBER")	private String landlineNumber;
	@Column(name = "IS_ACTIVE")	private Boolean isActive;
	@Column(name = "CREATED_BY")	private String createdBy;
	@Column(name = "CREATED_DATE")	private Date createdDate;
	@Column(name = "MODIFIED_BY")	private String modifiedBy;
	@Column(name = "MODIFIED_DATE")	private Date modifiedDate;
	


	
}
