package com.lic.epgs.gratuity.policy.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberPolicyDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String memberId;
	private String licId;
	private String policyId;
	private String unitId;
	private String variant;
}
