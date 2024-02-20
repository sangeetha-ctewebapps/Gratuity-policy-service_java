package com.lic.epgs.gratuity.policy.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D Date:16-09-2022
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MPHDataDto {
	
	private String schemeName;

	private String mphCode;

	private String mphName;

	private String mphNo;

	private Long mphMobileNo;

	private String mphEmail;

	private String mphAddress1;

	private String mphAddress2;

	private String district;

	private String state;

	private Long pinCode;

}
