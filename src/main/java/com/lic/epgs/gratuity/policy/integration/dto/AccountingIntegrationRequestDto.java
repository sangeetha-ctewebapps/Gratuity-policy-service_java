package com.lic.epgs.gratuity.policy.integration.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D Date:17-10-2022
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountingIntegrationRequestDto {
	
	private String policyNumber;
	private String proposalNumber;
	

}
