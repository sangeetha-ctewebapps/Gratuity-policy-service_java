package com.lic.epgs.gratuity.policy.integration.dto;

import java.util.List;


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
public class ProposalAnnuityDto {
	
	private String proposalNumber;
	private CustomerBasicDetailsDto customerBasicDetails;
	private List<MPHAddressDetailsDto> mphAddressDetails;
	private List<MPHContactPersonDetailsDto> mphContactDetails;
	private ProposalBasicDetailsDto proposalBasicDetails;

}
