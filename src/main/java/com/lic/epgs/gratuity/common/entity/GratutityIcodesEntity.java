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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "GRATUITY_ICODES")
public class GratutityIcodesEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GRATUITY_ICODES_ID_SEQ")
	@SequenceGenerator(name = "GRATUITY_ICODES_ID_SEQ", sequenceName = "GRATUITY_ICODES_ID_SEQ", allocationSize = 1)
	@Column(name = "ICODE_ID")
	private Long id;
	
	@Column(name = "VARIANT_NAME")
	private String variantName;
	@Column(name = "ICODE_BUSINESS_LINE")
	private Long icodeBusinessLine;
	@Column(name = "ICODE_PRODUCT_LINE")
	private Long icodeProductLine;
	@Column(name = "ICODE_VARIENT")
	private Long icodeVarient;
	@Column(name = "ICODE_BUSINESS_TYPE")
	private Long icodeBuinessType;
	@Column(name = "ICODE_PARTICIPATING_TYPE")
	private Long icodeParticipatingType;
	@Column(name = "ICODE_BUSINESS_SEGMENT")
	private Long icodeBusinessSegment;
	@Column(name = "IS_ACTIVE")
	private Long isActive;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	@Column(name = "PRODUCT_ID")
	private Long productId;
	@Column(name = "VARIANT_ID")
	private Long variantId;
	
	
}
