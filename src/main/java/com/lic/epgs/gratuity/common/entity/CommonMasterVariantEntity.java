package com.lic.epgs.gratuity.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Builder
@Entity
@Table(name = "PRODUCT_VARIANT_DETAILS", schema = "faschemaname")
public class CommonMasterVariantEntity {
	@Id
	@Column(name = "LEAD_VARIANT_ID")
	private Long id;
	
	@Column(name = "LEAD_PRODUCT_ID")
	private Long productId;
	
	@Column(name = "MKGT_LOB")
	private String mkgtLob;
	
	@Column(name = "PRODUCT_TYPE")
	private String productType;
	
	@Column(name = "SUB_CATEGORY")
	private String subCategory;
	
	@Column(name = "UIN")
	private String uin;
	
	@Column(name = "VARIANT_NAME")
	private String variantName;
	
	@Column(name = "VARIANT_VERSION")
	private String variantVersion;
}
