package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

@Getter
@Setter
public class TransferRefundCalculationDTO {
    private Long policyId;
    private long memberId;
    private String modeOfExit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
    private Date dateOfExit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
    private Date annualRenewalDate;
    private String productVariantOut;    
    private String productIdOut;    
    private String unitIn;
    private String unitOut;
    private String policyNumberOut;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

    }
}
