package com.lic.epgs.gratuity.accountingservice.dto;



import java.util.Date;

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

public class PayoutSpResponseDto {
	private Long Id ;
	private String payoutNumber ;
	private Date payOutDate ;
	private Long journalNo ;
	private Long debitAccount ;
	private Long creditAccount ;
	private Long totalAmount ;
	private String creditCode ;
	private String message ;
	private String status ;
	private String statusCode ;
	private Long sqlCode ;
	private String sqlErrorMessage ;
	private Long isActive ;
	private String createdBy ;
	private Date createdDate ;
	private String modifiedBy ;
    private Date modifiedDate ;
	


}
