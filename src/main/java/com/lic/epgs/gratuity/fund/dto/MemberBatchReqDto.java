package com.lic.epgs.gratuity.fund.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberBatchReqDto implements Serializable  {
	private static final long serialVersionUID = 1L;
	private Long policyId;
	private String trnxDate;
	private String variant;
	private String policyType;
	private String unitId;
	private String licId;
	private String memberStatus;
	private Boolean recalculate;
	private Boolean isBatch;
	private Boolean isAuto;
	private Boolean setOpeningBalance;
	private String batchNumber;
	private List<Long> policyIds;
}
