package com.lic.epgs.gratuity.policyservices.common.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Data
public class PolicyServiceMatrixDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String currentService;
	private String ongoingService;
	private String isAllowed;
	private Long isActive;
	

}
