package com.lic.epgs.gratuity.policyservices.merger.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QuotationStatusDto {

	
    private Long id;
	
	private Long pickListId;
	
	private String name;
	
	private String description;
	
	private Long displayOrder;
	
	private Long parentId;
	
	private Boolean isActive;
	
	private String createdBy;
	
	private Date createdDate;
	
	private String modifiedBy;
	
	private Date modifiedDate;
}
