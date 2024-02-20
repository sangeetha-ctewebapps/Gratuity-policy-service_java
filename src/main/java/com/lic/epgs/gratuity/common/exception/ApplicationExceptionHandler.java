package com.lic.epgs.gratuity.common.exception;

import java.util.Date;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.gratuity.common.constants.HttpConstants;
import com.lic.epgs.gratuity.common.dto.ApplicationErrorDto;
import com.lic.epgs.gratuity.common.entity.ApplicationErrorLogEntity;
import com.lic.epgs.gratuity.common.repository.ApplicationErrorLogRepository;

/**
 * @author Gopi
 *
 */

@RestControllerAdvice
public class ApplicationExceptionHandler {
	
	protected final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private ApplicationErrorLogRepository appErrorLogRepository;
	
	@Autowired
	@Qualifier("jsonObjectMapper")
	private ObjectMapper jsonObjectMapper;
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ApplicationErrorDto exceptionHandler(DataIntegrityViolationException exception, WebRequest request) {
		logger.error("DataIntegrityViolationException Exception Handler: ", exception);
		return ApplicationErrorDto.builder()
				.errorCode(HttpConstants.SERVER_EXCEPTION)
				.errorMessage(exceptionName(ExceptionUtils.getRootCauseMessage(exception)))
				.build();
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ApplicationErrorDto exceptionHandler(HttpMessageNotReadableException exception, WebRequest request) {
		logger.error("HttpMessageNotReadableException Exception Handler: ", exception);
		return ApplicationErrorDto.builder()
				.errorCode(HttpConstants.SERVER_EXCEPTION)
				.errorMessage(exceptionName(ExceptionUtils.getRootCauseMessage(exception)))
				.build();
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ApplicationErrorDto exceptionHandler(IllegalArgumentException exception, WebRequest request) {
		logger.error("Exception Exception Handler: ", exception);
		return ApplicationErrorDto.builder()
				.errorCode(HttpConstants.SERVER_EXCEPTION)
				.errorMessage(exceptionName(ExceptionUtils.getRootCauseMessage(exception)))
				.build();
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ApplicationErrorDto exceptionHandler(NoSuchElementException exception, WebRequest request) {
		logger.error("NoSuchElementException Exception Handler: ", exception);
		return ApplicationErrorDto.builder()
				.errorCode(HttpConstants.SERVER_EXCEPTION)
				.errorMessage(exceptionName(ExceptionUtils.getRootCauseMessage(exception)))
				.build();
	}
	
	private Long saveException(IllegalArgumentException exception, WebRequest request) {
		try {
			ApplicationErrorLogEntity applicationErrorLogEntity = jsonObjectMapper.readValue(
					request.getHeader("api-request"), ApplicationErrorLogEntity.class);
			
			if (applicationErrorLogEntity == null)
				applicationErrorLogEntity = new ApplicationErrorLogEntity();
			
			applicationErrorLogEntity.setUri(request.getDescription(true));
			
			String stackTrace = exception.getStackTrace().toString();
			if (StringUtils.isNotBlank(stackTrace)) {
				if (stackTrace.length() > 4000) {
					applicationErrorLogEntity.setErrorMessage(stackTrace.substring(0, 3999));
				} else {
					applicationErrorLogEntity.setErrorMessage(stackTrace);
				}
			}

			applicationErrorLogEntity.setCreatedOn(new Date());
			appErrorLogRepository.save(applicationErrorLogEntity);
			return applicationErrorLogEntity.getId();
		} catch (JsonProcessingException e) {
			logger.error("GlobalExceptionHandler::saveException ", e);
		}
		return null;
	}
	
	private static String exceptionName(String exceptionMessage) {
		if (StringUtils.isNotBlank(exceptionMessage)) {
			String delimiter = "Exception:";
			String[] str = exceptionMessage.split(delimiter, 0);
			return str != null && str.length >= 1 ? StringUtils.trim(str[1]) : "";
		} else {
			return "Internal Server Error";
		}
	}
}
