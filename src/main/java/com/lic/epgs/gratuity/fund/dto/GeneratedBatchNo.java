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
public class GeneratedBatchNo {
	private List<String> generatedBatchNo;

	public void addBatchNo(String batchNo) {
		if (generatedBatchNo == null) {
			generatedBatchNo = new ArrayList<String>();
			generatedBatchNo.add(batchNo);
		}
	}
}
