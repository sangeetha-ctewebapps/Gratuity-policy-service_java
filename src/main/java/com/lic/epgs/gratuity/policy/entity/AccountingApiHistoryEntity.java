package com.lic.epgs.gratuity.policy.entity;

import java.sql.Clob;
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
@Table(name = "ACCTNG_API_HISTORY")
public class AccountingApiHistoryEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCTNG_API_HISTORY_ID_SEQ")
	@SequenceGenerator(name = "ACCTNG_API_HISTORY_ID_SEQ", sequenceName = "ACCTNG_API_HISTORY_ID_SEQ", allocationSize = 1)
	
	@Column(name = "ACCTNG_API_HIS_ID")
	private Long id;
	
	@Column(name = "CALLED_DATE")
	private Date calledDate;
	
	@Column(name = "API_URL")
	private String apiUrl;
	
	@Column(name = "API_REQUEST_BODY")
	private String apiRequestBody;
	
	@Column(name = "API_RESPONSE_BODY")
	private String apiResponseBody;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	
}
