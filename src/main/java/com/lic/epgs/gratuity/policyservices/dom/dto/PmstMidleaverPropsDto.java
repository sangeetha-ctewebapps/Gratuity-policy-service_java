package com.lic.epgs.gratuity.policyservices.dom.dto;

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
public class PmstMidleaverPropsDto {

	private Long id;
	private Long tmpPolicyId;
	private String bulkOrIndividal;
	private String serviceNumber;
	private Boolean isPremiumRefund;
	private Long payoutNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date payOutDate;
	private Long statusId;
	private Boolean isActive;

	private String userName; // createdBy
	private Long pmstPolicyId;
	private Long pmstMemberId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date leavingDate;
	private Long leavingReasonId;
	private String leavingReasonOther;

	private RefundDto refundDto = new RefundDto();
}
