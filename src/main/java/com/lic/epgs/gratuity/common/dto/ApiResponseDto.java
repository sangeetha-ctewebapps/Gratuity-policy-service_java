package com.lic.epgs.gratuity.common.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.gratuity.common.constants.HttpConstants;
import com.lic.epgs.gratuity.quotation.dto.QuotationDto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Gopi
 *
 */

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponseDto<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String status;
	private String message;
	private List<ErrorDto> error;
	private T data;
	
	public static <T> ApiResponseDto<T> success(T t) {
		ApiResponseDto<T> response = new ApiResponseDto<>();
		response.setStatus(HttpConstants.OK);
		response.setError(null);
		response.setMessage(HttpConstants.OK_MESSAGE);
		response.setData(t);
		return response;
	}
	
	public static <T> ApiResponseDto<T> created(T t) {
		ApiResponseDto<T> response = new ApiResponseDto<>();
		response.setStatus(HttpConstants.CREATED);
		response.setError(null);
		response.setMessage(HttpConstants.CREATED_MESSAGE);
		response.setData(t);
		return response;
	}
	
	public static <T> ApiResponseDto<T> notFound(T t) {
		ApiResponseDto<T> response = new ApiResponseDto<>();
		response.setStatus(HttpConstants.NOT_FOUND);
		response.setError(null);
		response.setMessage(HttpConstants.NOT_FOUND_MESSAGE);
		response.setData(t);
		return response;
	}
	
	public static <T> ApiResponseDto<T> error() {
		ApiResponseDto<T> response = new ApiResponseDto<>();
		response.setStatus(HttpConstants.SERVER_EXCEPTION);
		response.setError(null);
		response.setMessage(HttpConstants.SERVER_EXCEPTION_MESSAGE);
		response.setData(null);
		return response;
	}

	public static <T> ApiResponseDto<T> errorMessage(T t, String httpConstant, String message) {
		ApiResponseDto<T> response = new ApiResponseDto<>();
		response.setStatus(HttpConstants.BAD_REQUEST);
		response.setError(null);
		response.setMessage(message);
		response.setData(t);
		return response;
	}

	public static <T> ApiResponseDto<T> errorMsg(T t, String httpConstant, String message) {
		ApiResponseDto<T> response = new ApiResponseDto<>();
		response.setStatus(HttpConstants.NON_AUTHORITATIVE);
		response.setError(null);
		response.setMessage(HttpConstants.NON_AUTHORITATIVE_MESSAGE);
		response.setData(null);
		return response;
	}
	
}
