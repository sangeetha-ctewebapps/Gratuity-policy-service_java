package com.lic.epgs.gratuity.policy.integration.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MPHAddressDetailsDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	
	private Integer addressId;
	
	
	private String mphId;
	
	
	private String addressType;
	

	private String pinCode;
	

	private String zipCode;
	

	private String country;
	
	private String state;
	

	private String district;
	

	private String address1	;
	
	
	private	String address2;
	
	private	String address3;
	
	private	String createdBy;
	
	private	String createdOn;
	
	private	String modifiedBy;
	
	private	String modifiedOn;
	private	String proposalId;
	private String city;
	private String locality;
	private String flag;
	private String defaultValue;
	private String addressTypeName;
	
	
	
	
	
	
}
