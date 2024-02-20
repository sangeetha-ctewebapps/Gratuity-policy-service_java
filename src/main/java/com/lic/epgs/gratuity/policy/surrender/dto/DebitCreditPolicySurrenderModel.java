package com.lic.epgs.gratuity.policy.surrender.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class DebitCreditPolicySurrenderModel {

    private BigDecimal creditCoPaymentContraAc;
    private BigDecimal creditGstAccount;
    private BigDecimal creditMvaCharge;
    private BigDecimal creditOsPaymentsGenralCPC;
    private BigDecimal creditSurrenderOrExitLoad;
    private BigDecimal debitCoPaymentContraAc;
    private BigDecimal debitWholeSaleSurrenderScheme;
}
