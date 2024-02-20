package com.lic.epgs.gratuity.quotation.member.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Gopi
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberBulkResponseDto extends MemberBatchDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long quotationId;
	private Long tmpPolicyId;
	private Long pmstPolicyId;
	private String transactionStatus;
	private String transactionMessage;
	private List<MemberErrorDto> errors;
	
}
