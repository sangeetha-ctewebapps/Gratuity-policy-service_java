package com.lic.epgs.gratuity.notifyDomain.dto;



import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D Date:26-09-2022
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseDto {
	
	private String collectionNo;
  
	private Integer status;
	
	private BigInteger demandNo;
	
    private String refNo;
	

}
