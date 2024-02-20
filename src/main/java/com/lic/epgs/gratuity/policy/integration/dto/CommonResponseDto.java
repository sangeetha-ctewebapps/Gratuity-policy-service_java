package com.lic.epgs.gratuity.policy.integration.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * @author Logesh.D Date:26-05-2022
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommonResponseDto<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private transient T responseData;
	private String transactionMessage;
	private String transactionStatus;
	
	
	

}
