package com.lic.epgs.gratuity.policy.surrender.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"surrenderId", "surrenderNumber", "surrenderDate", "surrenderStatus", "responseMessage", "responseCode"})
public class SaveSurrenderResponse {
	
	@JsonProperty("surrenderId")
	private Long surrenderId;
	
	@JsonProperty("surrenderNumber")
	private Long surrenderNumber;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone="IST")
	@JsonProperty("surrenderDate")
	private Date surrenderDate;
	
	@JsonProperty("surrenderStatus")
	private String surrenderStatus;
	
	@JsonProperty("responseMessage")
	private String responseMessage;
	
	@JsonProperty("responseCode")
	private int responseCode;
	
	public Long getSurrenderId() {
		return surrenderId;
	}
	public void setSurrenderId(Long surrenderId) {
		this.surrenderId = surrenderId;
	}
	
	public Long getSurrenderNumber() {
		return surrenderNumber;
	}
	public void setSurrenderNumber(Long surrenderNumber) {
		this.surrenderNumber = surrenderNumber;
	}
	
	public Date getSurrenderDate() {
		return surrenderDate;
	}
	public void setSurrenderDate(Date surrenderDate) {
		this.surrenderDate = surrenderDate;
	}
	
	public String getSurrenderStatus() {
		return surrenderStatus;
	}
	public void setSurrenderStatus(String surrenderStatus) {
		this.surrenderStatus = surrenderStatus;
	}
	
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	
}
