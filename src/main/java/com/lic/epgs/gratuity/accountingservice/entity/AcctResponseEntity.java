package com.lic.epgs.gratuity.accountingservice.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ACCT_RESPONSE")
public class AcctResponseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCT_RESPONSE_ID_SEQ")
	@SequenceGenerator(name = "ACCT_RESPONSE_ID_SEQ", sequenceName = "ACCT_RESPONSE_ID_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "MODULE")
	private String module;
	
	@Column(name = "REFERENCE_ID")
	private Long referenceId;
	
	@Column(name = "REFERENCE_VALUE")
	private String referenceValue;
	
	@OneToMany(mappedBy = "acctResponseEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<AcctCrDrEntity> acctCrDrEntities;
}
