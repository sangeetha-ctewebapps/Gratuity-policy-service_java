package com.lic.epgs.gratuity.policy.renewalpolicy.service;

import java.util.List;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.mph.dto.TemptMPHDto;
import com.lic.epgs.gratuity.policy.dto.MasterPolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.endorsement.dto.EndorsementDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.PolicySearchFilterDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.QuotationRenewalDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyNewSearchFilterDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalQuotationSearchDTo;
import com.lic.epgs.gratuity.quotation.dto.QuotationPDFGenerationDto;

public interface RenewalPolicyService {

//	ApiResponseDto<List<RenewalPolicyTMPDto>> fetchPolicyForRenewalQuotation(
//			RenewalQuotationSearchDTo renewalQuotationSearchDTo);

	ApiResponseDto<RenewalPolicyTMPDto> CreateRenewalforQuotation(QuotationRenewalDto quotationRenewalDto);

	ApiResponseDto<List<RenewalPolicyTMPDto>> processingfilter(RenewalQuotationSearchDTo renewalQuotationSearchDTo);

	ApiResponseDto<RenewalPolicyTMPDto> findByTMPId(Long id);
	
	ApiResponseDto<TemptMPHDto> findByTempMPHIdonTempId(Long id);
		
	ApiResponseDto<RenewalPolicyTMPDto> sentForApproval(Long id, String username);

	ApiResponseDto<RenewalPolicyTMPDto> sendBacktoMaker(Long id, String username);

	ApiResponseDto<RenewalPolicyTMPDto> forApprove(Long id, String username);

	ApiResponseDto<RenewalPolicyTMPDto> forReject(Long id, String username);

	ApiResponseDto<RenewalPolicyTMPDto> sentPolicyForApproval(Long id, String username);

	ApiResponseDto<RenewalPolicyTMPDto> sentPolicyBacktoMaker(Long id, String username);

	ApiResponseDto<RenewalPolicyTMPDto> sentPolicyforApprove(Long tempid , String username);

	ApiResponseDto<RenewalPolicyTMPDto> sentPolicyforReject(Long id, String username, RenewalPolicyTMPDto renewalPolicyTMPDto);
	
	
	byte[] findByPolicyId(Long tmpPolicyId);
	ApiResponseDto<RenewalPolicyTMPDto> updaterenewalquotationbasic(Long id, QuotationRenewalDto quotationRenewalDto);


	ApiResponseDto<RenewalPolicyTMPDto> getpolicyTmpId(Long id);

	ApiResponseDto<PolicyDto> renewalpolicyapprove(Long tempid, String username);

	ApiResponseDto<List<QuotationPDFGenerationDto>> generateReportRenewalQuotationPDF(Long tmpPolicyId);

	ApiResponseDto<List<RenewalPolicyTMPDto>> renewalSearchFilters(RenewalQuotationSearchDTo renewalQuotationSearchDTo);

	ApiResponseDto<List<RenewalPolicyNewSearchFilterDto>> filterForRenewal(
			RenewalPolicyNewSearchFilterDto renewalPolicyNewSearchFilterDto);

	ApiResponseDto<List<PolicySearchFilterDto>> filter(PolicySearchFilterDto policyTmpServiceDto);

	ApiResponseDto<List<RenewalPolicyTMPDto>> fetchPolicyForRenewalQuotation(
			RenewalQuotationSearchDTo renewalQuotationSearchDTo);

	Boolean ARDStatus(Long masterpolicyid);

	Long getRenewalSubStatus(Long quotationSubStatusId);

	Boolean discardRenewal(Long renewalTmpPolicyId);

//	ApiResponseDto<List<RenewalPolicyTMPDto>> fetchPolicyForRenewalQuotation(
//			RenewalQuotationSearchDTo renewalQuotationSearchDTo);

	}