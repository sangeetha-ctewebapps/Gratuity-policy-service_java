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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "PMST_TMP_MEMBER_NOMINEE")
public class TempMemberNomineeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_MBR_NOMINEE_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_MBR_NOMINEE_ID_SEQ", sequenceName = "PMST_TMP_MBR_NOMINEE_ID_SEQ", allocationSize = 1)
	@Column(name = "MEMBER_NOMINEE_ID")	private Long id;
	
	@ManyToOne
    @JoinColumn(name="MEMBER_ID", nullable=false)
    private TempMemberEntity tempMember;
	
	@Column(name = "NOMINEE_NAME")	private String nomineeName;
	@Column(name = "RELATIONSHIP")	private String relationship;
	@Column(name = "ADDRESS_ONE")	private String addressOne;
	@Column(name = "ADDRESS_TWO")	private String addressTwo;
	@Column(name = "ADDRESS_THREE")	private String addressThree;
	@Column(name = "NOMINEE_DISTRICT")	private String nomineeDistrict;
	@Column(name = "NOMINEE_STATE")	private String nomineeState;
	@Column(name = "NOMINEE_COUNTRY")	private String nomineeCountry;
	@Column(name = "NOMINEE_PINCODE")	private Long nomineePincode;
	@Column(name = "NOMINEE_EMAIL_ID")	private String nomineeEmailId;
	@Column(name = "NOMINEE_PHONE")	private Long nomineePhone;
	@Column(name = "DATE_OF_BIRTH")	private Date dateOfBirth;
	@Column(name = "AGE")	private Long age;
	@Column(name = "NOMINEE_AADHAR_NUMBER")	private String nomineeAadharNumber;
	@Column(name = "PERCENTAGE")	private double percentage;
	@Column(name = "IS_ACTIVE")	private Boolean isActive;
	@Column(name = "CREATED_BY")	private String createdBy;
	@Column(name = "CREATED_DATE")	private Date createdDate;
	@Column(name = "MODIFIED_BY")	private String modifiedBy;
	@Column(name = "MODIFIED_DATE")	private Date modifiedDate;
	@Column(name = "PMST_MEMBER_NOMINEE_ID")	private Long pmstMemberNomineeId;
	@Column(name = "RECORD_TYPE")	private Long recordType;
	@Column(name = "PAN_NUMBER") 	private String panNumber;
	@Column(name = "GENDER_ID") 	private Long genderId; 

}
