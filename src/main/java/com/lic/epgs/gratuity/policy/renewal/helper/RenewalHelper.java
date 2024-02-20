package com.lic.epgs.gratuity.policy.renewal.helper;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.renewal.dto.PmstPolicyDto;
import com.lic.epgs.gratuity.policy.renewal.dto.PolicyRenewalRemainderDto;
import com.lic.epgs.gratuity.policy.renewal.entity.PolicyRenewalRemainderEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.PolicySearchFilterDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyNewSearchFilterDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.PolicyTmpSearchEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicySearchEntity;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;

public class RenewalHelper {

	@Autowired
	MasterPolicyRepository masterPolicyRepository;

	public static byte[] RenewalDocument(MasterPolicyEntity masterPolicyEnty) {

		LocalDate dateObj = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String date = dateObj.format(formatter);
		Date annualRenewlDate = masterPolicyEnty.getAnnualRenewlDate();

		SimpleDateFormat formattedata = new SimpleDateFormat("dd-MM-yyyy");

		String format = formattedata.format(annualRenewlDate);
		
		


		return null;
	}

	public static byte[] RenewalDocumentDynamic(MasterPolicyEntity masterPolicyEntity) {

		
		LocalDate dateObj = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String date = dateObj.format(formatter);
		Date annualRenewlDate = masterPolicyEntity.getAnnualRenewlDate();

		SimpleDateFormat formattedata = new SimpleDateFormat("dd-MM-yyyy");

		String format = formattedata.format(annualRenewlDate);
		
		
		byte[] bytes = null;

		return bytes;
	}

	public static PmstPolicyDto entitytoDto(MasterPolicyEntity masterPolicyEntity) {

		return new ModelMapper().map(masterPolicyEntity, PmstPolicyDto.class);
	}

	public static RenewalPolicyNewSearchFilterDto entityToDto(RenewalPolicySearchEntity masterPolicyEntity) {

		return new ModelMapper().map(masterPolicyEntity, RenewalPolicyNewSearchFilterDto.class);
	}

	

	public static PolicyRenewalRemainderDto entityRenewaltoDto(PolicyRenewalRemainderEntity policyRenewalRemainderEntity) {
		return new ModelMapper().map(policyRenewalRemainderEntity, PolicyRenewalRemainderDto.class);
	}
	
	public static PolicySearchFilterDto tmpEntityToDto(PolicyTmpSearchEntity policyTmpSearchEntity) {

		return new ModelMapper().map(policyTmpSearchEntity, PolicySearchFilterDto.class);
	}
}
