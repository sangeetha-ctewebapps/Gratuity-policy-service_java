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
@Table(name = "PRODUCT_DETAILS", schema = "faschemaname")
public class CommonMasterProductEntity {
	@Id
	@Column(name = "LEAD_PRODUCT_ID")
	private Long id;
	
	@Column(name = "AVAILABLE_FOR_NB")
	private String availableForNb;
	
	@Column(name = "EGI_OR_OGI")
	private String egiOrOgi;
	
	@Column(name = "PRODUCT_CODE")
	private String productCode;
	
	@Column(name = "PRODUCT_NAME")
	private String productName;
}
