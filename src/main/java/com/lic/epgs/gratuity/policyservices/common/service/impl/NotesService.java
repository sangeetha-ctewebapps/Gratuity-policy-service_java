package com.lic.epgs.gratuity.policyservices.common.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.policyservices.common.dto.PolicyServiceStatusResponseDto;
import com.lic.epgs.gratuity.policyservices.common.entity.TempPolicyServiceNotes;
import com.lic.epgs.gratuity.policyservices.common.repository.TempPolicyServiceNotesRepository;

@Service
public class NotesService {

	@Autowired
	TempPolicyServiceNotesRepository tempPolicyServiceNotesRepository;

	public TempPolicyServiceNotes save(TempPolicyServiceNotes tempPolicyServiceNotes) {
		tempPolicyServiceNotes.setIsActive(true);
		tempPolicyServiceNotes.setCreatedDate(new Date());
		return tempPolicyServiceNotesRepository.save(tempPolicyServiceNotes);
	}

	public List<TempPolicyServiceNotes> getListNotes(Long referenceId, String policyNumber,
			String referenceServiceType) {
		return tempPolicyServiceNotesRepository
				.findByReferenceIdAndPolicyNumberAndReferenceServiceTypeAndIsActiveOrderByCreatedDateDesc(referenceId,
						policyNumber, referenceServiceType, true);
	}

	public Object saveCommonNotes(TempPolicyServiceNotes tempPolicyServiceNotes) {

		PolicyServiceStatusResponseDto response = new PolicyServiceStatusResponseDto();

		try {
			tempPolicyServiceNotes.setIsActive(true);
			tempPolicyServiceNotes.setCreatedDate(new Date());
			TempPolicyServiceNotes save = tempPolicyServiceNotesRepository.save(tempPolicyServiceNotes);

			response.setMessage("Save Successfully.");
			response.setStatus("SUCCESS");
			response.setResponseData(save);
			return response;
		} catch (Exception e) {
			response.setMessage("Failed to save.");
			response.setStatus("FAILED");
			return response;
		}
	}

	public Object getCommonNotesList(String serviceNumber) {
		PolicyServiceStatusResponseDto response = new PolicyServiceStatusResponseDto();

		try {
			List<TempPolicyServiceNotes> findByServiceNumberAndIsActive = tempPolicyServiceNotesRepository
					.findByServiceNumberAndIsActive(serviceNumber, true);
			response.setResponseData(findByServiceNumberAndIsActive);
			response.setStatus("SUCCESS");
			return response;
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus("FAILED");
			return response;
		}
	}

}