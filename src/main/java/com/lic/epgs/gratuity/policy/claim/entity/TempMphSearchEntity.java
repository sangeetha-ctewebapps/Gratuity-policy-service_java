package com.lic.epgs.gratuity.policy.claim.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.lic.epgs.gratuity.mph.entity.TempMPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHBankEntity;
import com.lic.epgs.gratuity.mph.entity.TempMPHRepresentativeEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PMST_TMP_MPH")
public class TempMphSearchEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_TMP_MPH_ID_SEQ")
	@SequenceGenerator(name = "PMST_TMP_MPH_ID_SEQ", sequenceName = "PMST_TMP_MPH_ID_SEQ", allocationSize = 1)
	@Column(name = "MPH_ID")
	private Long id;

	@Column(name = "MPH_NAME")
	private String mphName;

	@Column(name = "PAN")
	private String pan;

	@Column(name = "MPH_CODE")
	private String mphCode;

//	 (in master table)
	@OneToMany(mappedBy = "policyMPHTmp", cascade = CascadeType.ALL)
	private List<PolicyTempSearchEntity> policyMPHSearch;

}
