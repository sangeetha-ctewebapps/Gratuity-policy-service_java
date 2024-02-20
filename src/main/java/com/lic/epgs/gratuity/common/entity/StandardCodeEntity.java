package com.lic.epgs.gratuity.common.entity;

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

/**
 * @author Gopi
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "STANDARDCODE")
public class StandardCodeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STANDARDCODE_ID_SEQUENCE")
	@SequenceGenerator(name = "STANDARDCODE_ID_SEQUENCE", sequenceName = "STANDARDCODE_ID_SEQUENCE", allocationSize = 1)
	@Column(name = "STANDARDCODE_ID")
	private Long id;
	
	@Column(name = "STANDARDCODE_NAME", length = 64)
	private String name;
	
	@Column(name = "STANDARDCODE_DESCRIPTION", length = 128)
	private String description;
	
	@Column(name = "STANDARDCODE_VALUE", length = 32)
	private String value;
	
	@Column(name = "DATA_TYPE", length = 16)
	private String dataType;
	
	@Column(name = "EFFECTIVE_FROM")
	private Date effectiveFrom;
	
	@Column(name = "EFFECTIVE_TO")
	private Date effectiveTo;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "CREATED_BY", length = 128)
	private String createdBy;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY", length = 128)
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
}
