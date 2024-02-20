package com.lic.epgs.gratuity.policy.entity;

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
@Table(name="SURRENDER _RATE_CONFIG")
public class SurrenderRateConfigEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SURRENDER _RATE_CONFIG_ID_SEQ")
	@SequenceGenerator(name = "SURRENDER _RATE_CONFIG_ID_SEQ", sequenceName = "SURRENDER _RATE_CONFIG_ID_SEQ", allocationSize = 1)
	@Column(name="SURRENDER_RATE_ID")
	private Long id;
	
	@Column(name="FROM_VALUE")
	private Integer fromValue;
	
	@Column(name="TO_VALUE")
	private Integer toValue;
	
	@Column(name="PERCENTAGE")
	private Integer percentage;
	
	@Column(name="IS_ACTIVE")
	private Integer isActive;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
}
