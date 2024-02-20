package com.lic.epgs.gratuity.policy.renewalpolicy.member.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberAppointeeEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberBankAccount;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberNomineeEntity;

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
@Table(name = "PMST_TMP_MEMBER_ADDRESS")
public class RenewalPolicyTMPMemberAddressEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_MBR_ADDR_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_MBR_ADDR_ID_SEQ", sequenceName = "PMST_TMP_MBR_ADDR_ID_SEQ", allocationSize = 1)
	@Column(name = "MEMBER_ADDRESS_ID")	private Long id;
	
	@ManyToOne
    @JoinColumn(name="MEMBER_ID", nullable=false)
    private RenewalPolicyTMPMemberEntity member;
	
	@Column(name = "ADDRESS_TYPE_ID")	private Long addressTypeId;
	@Column(name = "PIN_CODE")	private String pinCode;
	@Column(name = "COUNTRY")	private String country;
	@Column(name = "STATE_NAME")	private String stateName;
	@Column(name = "DISTRICT")	private String district;
	@Column(name = "CITY")	private String city;
	@Column(name = "ADDRESS1")	private String address1;
	@Column(name = "ADDRESS2")	private String address2;
	@Column(name = "ADDRESS3")	private String address3;
	@Column(name = "IS_ACTIVE")	private Boolean isActive;
	@Column(name = "CREATED_BY")	private String createdBy;
	@Column(name = "CREATED_DATE")	private Date createdDate;
	@Column(name = "MODIFIED_BY")	private String modifiedBy;
	@Column(name = "MODIFIED_DATE")	private Date modifiedDate;
	@Column(name = "PMST_MEMBER_ADDRESS_ID")	private Long pmstMemberAddressId;
	@Column(name = "RECORD_TYPE")	private String recordType;
	@Column(name = "PMST_HIS_MEMBER_ADDRESS_ID")	private Long pmstHisMemberAddressId;
}
