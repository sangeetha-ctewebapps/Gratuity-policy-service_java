package com.lic.epgs.gratuity.policyservices.policymodification.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Data
public class TempPolicyModificationDataTableResponseDto {

	private String mphCode;

	private String mphName;

	private String policyId;

	private String policyNumber;

	private Long totalRecords;

	private Long noOfPages;
	
	private Long status;

	private String statusTranc;
	

	public  String getStatusTranc() {
		if (status==11) {
			return "Draft"; 
		}
		
		if (status==22) {
			return "Pending For Approval"; 
		}
		
		if (status==33) {
			return "Approved"; 
		}
		
		if (status==44) {
			return "Rejected"; 
		}
		
		if (status==55) {
			return "Send To Maker"; 
		}
		return null; 
		
	}
	
	
	
	
}
