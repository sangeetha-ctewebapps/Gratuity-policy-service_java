package com.lic.epgs.gratuity.policyservices.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyServiceStatusDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String code;
	private String type;
	private String description;
	private String description1;
	private String name;
	private Boolean isActive;

}
