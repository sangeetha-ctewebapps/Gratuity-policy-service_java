package com.lic.epgs.gratuity.policy.claim.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GratuityCalculationsDto {	
	private Double gratuityamount;
	private Integer pastservice;
	private Long tmppolicyid;
	private Long tmpMemberId;
	private String modeOfExit;
	private Double salary;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfExist;
	
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date annualRenewalDate;
    private Double totalservicegtyARD;
    private Double totalservicegtryDOE;
	private Double refundPremium;
	private Double refundonGST;
	private Long pmstPolicyId;
	private Integer totalService;

}
