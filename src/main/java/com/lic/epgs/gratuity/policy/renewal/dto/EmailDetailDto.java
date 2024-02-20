package com.lic.epgs.gratuity.policy.renewal.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailDetailDto {

	
	
  private String   to;
  private String   cc;
  private String   bcc;
  private String   subject;
  private String   emailBody;
  private String   pdfFile;
	
}
