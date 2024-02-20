package com.lic.epgs.gratuity.fund.dto;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Muruganandam
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DocumentDto {
	private byte[] bytes;
	private InputStream in;
	private String fileName;
	private String fileType;
	private String status;
	private Integer statusId;
	private String exception;
}
