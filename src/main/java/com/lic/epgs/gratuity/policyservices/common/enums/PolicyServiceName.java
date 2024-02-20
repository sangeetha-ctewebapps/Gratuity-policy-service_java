package com.lic.epgs.gratuity.policyservices.common.enums;

public enum PolicyServiceName {

	CONVERSION("CONVERSION"), 
	SURRENDER("SURRENDER"), 
	MERGER("MERGER"), 
	REGULAR_ADJUSTMENT("REGULAR ADJUSTMENT"),
	MEMBER_ADDITION("MEMBER_ADDITION"), 
	MID_LEAVER("MID LEAVER"),
	SUBSEQUENT_ADJUSTMENT("SUBSEQUENT ADJUSTMENT"), 
	CLAIM("CLAIM"),
	POLICY_DETAILS_CHANGE("POLICYDETAILSCHANGE"), 
	FREELOOKCANCEL("FREELOOKCANCEL"),
	MEMBERTRASFERINOUT("MEMBERTRASFERINOUT");

	private String name;

	 public String getName()
	    {
	        return this.name;
	    }
	  
	private PolicyServiceName(String name) {
		this.name = name;
	}
}
