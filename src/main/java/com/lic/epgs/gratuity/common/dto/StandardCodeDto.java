package com.lic.epgs.gratuity.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Gopi
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StandardCodeDto {

	private Long id;
	private String name;
	private String value;
	private String dataType;
	private Date effectiveFrom;
	private Date effectiveTo;
}
