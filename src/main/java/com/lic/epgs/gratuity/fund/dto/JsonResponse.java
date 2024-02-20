package com.lic.epgs.gratuity.fund.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String response;
	private List<Object> data;
	private List<String> errorList;
	
	public void addSuccessData(Object t) {
		try {
			if(null == this.data) {
				this.data = new ArrayList<Object>();
				this.data.add(t);
			}else {
				this.data.add(t);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setSuccessData(Object t) {
		try {
			if(null == this.data) {
				this.data = new ArrayList<Object>();
				this.data.add(t);
			}else {
				this.data.clear();
				this.data.add(t);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setSuccessDataList(List<Object> t) {
		try {
			if(null == this.data) {
				this.data = t;
			}else {
				this.data = new ArrayList<Object>();
				this.data = t;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setErrorResponse(String error) {
		try {
			if(null == this.errorList) {
				this.errorList = new ArrayList<String>();
				this.errorList.add(error);
			}else {
				this.errorList.clear();
				this.errorList.add(error);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addErrorResponse(String error) {
		try {
			if(null == this.errorList) {
				this.errorList = new ArrayList<String>();
				this.errorList.add(error);
			}else {
				this.errorList.add(error);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setSuccess(Boolean res) {
		try {
			if(res) {
				this.response = "Success";
			}else {
				this.response = "Failure";
				this.data.clear();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setFailureData(Exception e) {
		try {
			String error = e.getMessage();
			if(null == this.errorList) {
				this.errorList = new ArrayList<String>();
				this.errorList.add(error);
				this.response = "Failure";
			}else {
				this.errorList.add(error);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
