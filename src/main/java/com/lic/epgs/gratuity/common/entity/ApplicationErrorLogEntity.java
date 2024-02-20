package com.lic.epgs.gratuity.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Gopi
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "APP_ERROR_LOG")
public class ApplicationErrorLogEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "APP_ERROR_LOG_ID")
	private Long id;
	
	@Column(name = "TXN_TYPE", length = 255)
	private String txnType;

	@Column(name = "TXN_ID")
	private Long txnId;

	@Column(name = "MODULE", length = 100)
	private String module;

	@Column(name = "URI", length = 500)
	private String uri;

	@Column(name = "TXN_NUMBER", length = 100)
	private String txnNumber;

	@Column(name = "ERROR_MESSSAGE", length = 4000)
	private String errorMessage;

	@Column(name = "REMARKS", length = 4000)
	private String remarks;

	@Column(name = "CREATED_ON")
	private Date createdOn;
	
}
