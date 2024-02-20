package com.lic.epgs.gratuity.policyservices.policymodification.dto;

public class StatusMaster {
	public static final Long DRAFT = 11L;
	public static final Long PENDING_FOR_APPROVAL = 22L;
	public static final Long APPROVED = 33L;
	public static final Long REJECTED = 44L;
	public static final Long SEND_TO_MAKER = 55L;
	
	
	public static  String getStatusTranc(Long sId) 
	{
		if (sId==null) {
			return null; 
		}
		if (sId==11) {
			return "Draft"; 
		}
		
		if (sId==22) {
			return "Pending For Approval"; 
		}
		
		if (sId==33) {
			return "Approved"; 
		}
		
		if (sId==44) {
			return "Rejected"; 
		}
		
		if (sId==55) {
			return "Send To Maker"; 
		}
		return null; 
		
	}
}
