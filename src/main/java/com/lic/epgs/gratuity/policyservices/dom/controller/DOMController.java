package com.lic.epgs.gratuity.policyservices.dom.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lic.epgs.gratuity.accountingservice.dto.MidLeaverContributuionPremuimAndGstDto;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.service.IntegrationService;
import com.lic.epgs.gratuity.mph.dto.TempMPHBankDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.PolicyTmpSearchDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policyservices.aom.dto.AOMSearchDto;
import com.lic.epgs.gratuity.policyservices.dom.dto.DOMSearchDto;
import com.lic.epgs.gratuity.policyservices.dom.dto.MemberLeaverDto;
import com.lic.epgs.gratuity.policyservices.dom.dto.MidleaverBenficiaryDto;
import com.lic.epgs.gratuity.policyservices.dom.dto.PmstMidleaverPropsDto;
import com.lic.epgs.gratuity.policyservices.dom.service.DOMService;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBulkResponseDto;
import com.lic.epgs.gratuity.quotation.member.service.MemberService;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/service/midleaver/" })
public class DOMController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private DOMService domService;

	@Autowired
	private IntegrationService integrationService;

	@PostMapping("createService")
	public ApiResponseDto<PmstMidleaverPropsDto> createService(
			@RequestBody PmstMidleaverPropsDto PmstMidleaverPropsDto) {
		return domService.masterPolicyForCreateServiceforIndividual(PmstMidleaverPropsDto);
	}

	@GetMapping(value = "getOverView/{tmpPolicyId}")
	public ApiResponseDto<PmstMidleaverPropsDto> getOverView(@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		return domService.getOverView(tmpPolicyId);
	}

	@GetMapping(value = "getMemberList/{tmpPolicyId}")
	public ApiResponseDto<List<MemberLeaverDto>> getMemberList(@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		return domService.getMemberList(tmpPolicyId);
	}

	@PutMapping(value = "saveRefund/{tmpPolicyId}/{isPremiumRefund}/{userName}")
	public ApiResponseDto<PmstMidleaverPropsDto> saveRefund(@PathVariable("tmpPolicyId") Long tmpPolicyId,
			@PathVariable("isPremiumRefund") Boolean isPremiumRefund, @PathVariable("userName") String userName) {
		return domService.saveRefund(tmpPolicyId, isPremiumRefund, userName);
	}

	@GetMapping(value = "getBeneficiaries/{tmpPolicyId}/{userName}")
	public ApiResponseDto<List<TempMPHBankDto>> getBeneficiaries(@PathVariable("tmpPolicyId") Long tmpPolicyId,
			@PathVariable("userName") String userName) {
		return domService.getBeneficiaries(tmpPolicyId, userName);
	}

	@GetMapping(value = "search/{policyNumber}/{type}")
	public ApiResponseDto<List<PolicyTmpSearchDto>> searchbyPolicyNumber(
			@PathVariable("policyNumber") String policyNumber, @PathVariable("type") String type) {
		return domService.searchByPolicyNumber(type, policyNumber);
	}

	@PostMapping(value = "search/other/{type}")
	public ApiResponseDto<List<AOMSearchDto>> searchbyOtherParams(@PathVariable("type") String type,
			@RequestBody DOMSearchDto domSearchDto) {
		return domService.otherSearchByPolicyNumber(type, domSearchDto);
	}

	@PostMapping(value = "submitForApproval/{tmpPolicyId}/{userName}")
	public ApiResponseDto<PmstMidleaverPropsDto> midleaverSubmitForApproval(
			@PathVariable("tmpPolicyId") Long tmpPolicyId, @PathVariable("userName") String userName) {
		return domService.midleaverSubmitForApproval(tmpPolicyId, userName);
	}

	@PostMapping(value = "sendBackToMaker/{tmpPolicyId}/{userName}")
	public ApiResponseDto<PmstMidleaverPropsDto> midleaverSendBackToMaker(@PathVariable("tmpPolicyId") Long tmpPolicyId,
			@PathVariable("userName") String userName) {
		return domService.midleaverSendBackToMaker(tmpPolicyId, userName);
	}

	@PostMapping(value = "reject/{tmpPolicyId}/{userName}")
	public ApiResponseDto<PmstMidleaverPropsDto> midleaverReject(@PathVariable("tmpPolicyId") Long tmpPolicyId,
			@PathVariable("userName") String userName) {
		return domService.midleaverReject(tmpPolicyId, userName);
	}

	@PostMapping(value = "approve/{tmpPolicyId}/{userName}")
	public ApiResponseDto<PmstMidleaverPropsDto> midleaverApprove(@PathVariable("tmpPolicyId") Long tmpPolicyId,
			@PathVariable("userName") String userName) {
		return domService.midleaverApprove(tmpPolicyId, userName);
	}

	/*
	 * @PostMapping(value = "approve/{tmpPolicyId}/{userName}") public
	 * ApiResponseDto<PmstMidleaverPropsDto>
	 * midleaverApprove(@PathVariable("tmpPolicyId") Long tmpPolicyId,
	 * 
	 * @PathVariable("userName") String userName) throws JsonProcessingException {
	 * return domService.midleaverApprove(tmpPolicyId, userName); }
	 */

	@PostMapping(value = "payout/sendForApproval/{tmpPolicyId}/{userName}")
	public ApiResponseDto<PmstMidleaverPropsDto> payoutSendForApproval(@PathVariable("tmpPolicyId") Long tmpPolicyId,
			@PathVariable("userName") String userName) {
		return domService.payoutSendForApproval(tmpPolicyId, userName);
	}

	@PostMapping(value = "payout/sendBackToMaker/{tmpPolicyId}/{userName}")
	public ApiResponseDto<PmstMidleaverPropsDto> payoutSendBackToMaker(@PathVariable("tmpPolicyId") Long tmpPolicyId,
			@PathVariable("userName") String userName) {
		return domService.payoutSendBackToMaker(tmpPolicyId, userName);
	}

	@PostMapping(value = "payout/reject/{tmpPolicyId}/{userName}")
	public ApiResponseDto<PmstMidleaverPropsDto> payoutReject(@PathVariable("tmpPolicyId") Long tmpPolicyId,
			@PathVariable("userName") String userName) {
		return domService.payoutReject(tmpPolicyId, userName);
	}

	@PostMapping(value = "payout/approve/{tmpPolicyId}/{userName}")
	public ApiResponseDto<PmstMidleaverPropsDto> payoutApprove(@PathVariable("tmpPolicyId") Long tmpPolicyId,
			@PathVariable("userName") String userName) {
		return domService.payoutApprove(tmpPolicyId, userName);
	}

	@PostMapping("uploadDOMMember")
	public ApiResponseDto<MemberBulkResponseDto> upload(@RequestParam Long pmstpolicyId, @RequestParam String username,
			@RequestBody MultipartFile file) throws IllegalStateException, IOException {
		return integrationService.uploadMemberDataDOMService(pmstpolicyId, username, file);
	}

	@PostMapping("import/{pmstPolicyId}/{batchId}/{username}")
	public ApiResponseDto<PmstMidleaverPropsDto> importMemberData(@PathVariable("pmstPolicyId") Long pmstPolicyId,
			@PathVariable("batchId") Long batchId, @PathVariable("username") String username) {
		return domService.importMemberData(pmstPolicyId, batchId, username);
	}

	@GetMapping("download/sample")
	public ResponseEntity<byte[]> downloadSample() {
		byte[] tt = memberService.downloadMidLeaverSample();

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("application/nvd.ms-excel"));
		header.setContentLength(tt.length);
		header.set("Content-Disposition", "attachment; filename=MidLeaverSample.xlsx");

		return new ResponseEntity<>(tt, header, HttpStatus.OK);
	}

	@GetMapping("download/membererror")
	public ResponseEntity<byte[]> downloadMemberError() {
		return null;
	}

	@GetMapping("download/memberlist/{tmpPolicyId}")
	public ResponseEntity<byte[]> downloadMember(@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		byte[] tt = memberService.downloadMidLeaverMemberDetail(tmpPolicyId);

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("application/nvd.ms-excel"));
		header.setContentLength(tt.length);
		header.set("Content-Disposition", "attachment; filename=MidLeaverDetail.xlsx");

		return new ResponseEntity<>(tt, header, HttpStatus.OK);
	}

	@GetMapping(value = "/getFromMakerBucket/{masterPolicyId}/{tmpPolicyId}")
	public ApiResponseDto<RenewalPolicyTMPDto> getInMakerBucket(@PathVariable("masterPolicyId") Long masterPolicyId,
			@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		return domService.findMidLeaversInMakerBucket(masterPolicyId, tmpPolicyId);
	}

	@PostMapping(value = "save/{tmpPolicyId}/{mphTmpBankId}/{userName}")
	public ApiResponseDto<MidleaverBenficiaryDto> saveBenficiary(@PathVariable("tmpPolicyId") Long tmpPolicyId,
			@PathVariable("mphTmpBankId") Long mphTmpBankId, @PathVariable("userName") String userName) {
		return domService.saveMidleaverBenficiary(tmpPolicyId, mphTmpBankId, userName);
	}

	@PutMapping(value = "update/{tmpPolicyId}/{mphTmpBankId}/{userName}")
	public ApiResponseDto<MidleaverBenficiaryDto> updateBenficiary(@PathVariable("tmpPolicyId") Long tmpPolicyId,
			@PathVariable("mphTmpBankId") Long mphTmpBankId, @PathVariable("userName") String userName) {
		return domService.updateMidLeaverBenficiary(tmpPolicyId, mphTmpBankId, userName);
	}

	@GetMapping(value = "getBenificaryBankDetail/{tmpPolicyId}")
	public ApiResponseDto<TempMPHBankDto> getBenificaryBankDetail(@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		return domService.getMidLeaverBenficiary(tmpPolicyId);

	}

	@PostMapping(value = "discardMidLeaver/{tmpPolicyId}/{userName}")
	public ApiResponseDto<PmstMidleaverPropsDto> discard(@PathVariable("tmpPolicyId") Long tmpPolicyId,
			@PathVariable("userName") String userName) {
		return domService.discard(tmpPolicyId, userName);

	}

	@PostMapping(value = "midleaverDto")
	public void String(@RequestBody MidLeaverContributuionPremuimAndGstDto ss) {

		System.out.println("ss" + ss);
	}

}
