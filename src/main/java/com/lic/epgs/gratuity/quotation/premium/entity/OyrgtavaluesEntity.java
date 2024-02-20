package com.lic.epgs.gratuity.quotation.premium.entity;

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
@Table(name = "OYRGTAVALUES")
public class OyrgtavaluesEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "OYRGTAVALUES_ID")
	private Long id;
	
	@Column(name = "AGE")
	private Long age;
	
	@Column(name = "SELECTOR")
	private String selector;
	
	@Column(name = "STARTLIVES")
	private Long startlives;
	
	@Column(name = "ENDLIVES")
	private Long endlives;
	
	@Column(name = "ENUMERATOR")
	private  Long enumerator;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "CREATED_DATE")
	private Date  createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String  modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date  modifiedDate;
}
