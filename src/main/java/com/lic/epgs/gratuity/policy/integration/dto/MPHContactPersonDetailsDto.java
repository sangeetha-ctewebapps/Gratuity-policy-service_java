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
public class MPHContactPersonDetailsDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer contactPersonId;
	
	private String mphId;
	
	
	private String designation;
	
	private String countryCode;
	
	private String mobileNumber;
	
	private String landlineNumber;
	
	private String emailID;
	
	
	private String createdBy;
	
	private String createdOn;
	
	private String modifiedBy;
	
	private String modifiedOn;
	
	
	private String proposalId;
	
	private String stdCode;
	
	private String extNumber;
	
	private String firstName;
	private String middleName;
	private String lastName;
	private String salutation;
	private String contactName;
	private String city;
	private String flag;
	private String defaultValue;
	
	
	
	
	
	
	
	
	
	

}
