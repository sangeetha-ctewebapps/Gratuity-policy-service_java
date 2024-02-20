package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferInCalculateDTO {
    private Long pmstPolicyId;
    private Long pmstMemberId;
    private Long transferRequisitionId;
    private String createdBy;
}
