package com.lic.epgs.gratuity.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "PRODUCT_LEVEL_MASTER")
public class ProductFeatureEntity {

	@Id
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "PRODUCT_ID")
	private Long productId;
	
	@Column(name = "VARIANT_ID")
	private Long variantId;
	
	@Column(name = "CANMIDJOIN")
	private Boolean canMidJoin;
	
	@Column(name = "CANMIDLEAVE")
	private Boolean canMidLeave;
	
	@Column(name = "CANCONVERT")
	private Boolean canConvert;
	
	@Column(name = "CANMERGE")
	private Boolean canMerge;
	
	@Column(name = "CANNEWBUSINESS")
	private Boolean canNewBusiness;
	
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
	
	@Column(name = "EFFECTIVE_FROM_DATE")
	private Date effectiveFromDate;
	
	@Column(name = "EFFECTIVE_TO_DATE")
	private Date effectiveToDate;
	
	@Column(name = "VARIANT_NAME")
	private String variantName;
	
	@Column(name = "CAN_CONVERT_TO")
	private String canConvertTo;
	
	
	@Column(name = "CANLCPREM")
	private Long canlcprem;
	
	@Column(name = "LCPREM_LATEFEE_PERC")
	private Double lcpremLateFeePerc;
	
	@Column(name = "LCPREM_GRACE_PERIOD")
	private Long lcpremGracePeriod;
	
}
