package com.lic.epgs.gratuity.fund.dto;

import java.util.ArrayList;
import java.util.List;

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
public class PolicyStmtGenRespDto {

	private List<String> errorList;
	private GeneratedBatchNo batchNoObj;

	public void addErrorResponse(String error) {
		try {
			if (null == this.errorList) {
				this.errorList = new ArrayList<String>();
				this.errorList.add(error);
			} else {
				this.errorList.add(error);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addBatchNo(String batchNo) {
		if (null == this.batchNoObj) {
			this.batchNoObj = new GeneratedBatchNo();
		}
		batchNoObj.addBatchNo(batchNo);
	}

}
