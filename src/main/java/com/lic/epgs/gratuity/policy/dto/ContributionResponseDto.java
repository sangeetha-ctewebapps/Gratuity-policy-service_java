package com.lic.epgs.gratuity.policy.dto;

import java.io.Serializable;
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
public class ContributionResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String sNo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
    private Date date;
    private String type;
    private String employerContribution;
    private String employeeContribution;
    private String voluntaryContribution;
    private String totalAmount;
    private String closingBalance;
    private String contributionId;
}