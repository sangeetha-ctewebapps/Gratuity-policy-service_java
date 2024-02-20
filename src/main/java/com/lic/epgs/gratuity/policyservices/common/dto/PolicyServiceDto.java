package com.lic.epgs.gratuity.policyservices.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyServiceDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long serviceId;

	private String serviceNumber;
	private String serviceType;

	private String serviceDoneBy;
	private Date serviceEffectiveDate;

	private String requestReceivedBy;
	private Date requestReceivedDate;

	private String serviceStatus;
	private String workflowStatus;

	private Long policyId;
	private String policyNumber;
//	private Boolean isUsing;
	private Boolean isActive;

	private String unitCode;

	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "asia/kolkata")
	private Date createdOn;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "asia/kolkata")
	private Date modifiedOn;

//	private Set<TransferInAndOutDto> memberTransferOutTemp = new HashSet<>();
//	private Set<FreeLookCancellationDto> flcTemp = new HashSet<>();
//	private Set<PolicyLevelMergerDto> mergerTemp = new HashSet<>();
//	private Set<PolicyLevelConversionDto> conversionTemp = new HashSet<>();
//	private Set<PolicyServiceMemberAdditionDto> memberAdditionTemp = new HashSet<>();
//	private Set<PolicyDetailsChangeDto> policyDetailsChangeTempEntity = new HashSet<>();

}
