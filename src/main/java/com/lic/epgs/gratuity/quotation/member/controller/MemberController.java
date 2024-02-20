package com.lic.epgs.gratuity.quotation.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.service.IntegrationService;
import com.lic.epgs.gratuity.quotation.member.dto.MemberAddressDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberAppointeeDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBankAccountDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBatchDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberBulkResponseDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberNomineeDto;
import com.lic.epgs.gratuity.quotation.member.dto.MemberSearchDto;
import com.lic.epgs.gratuity.quotation.member.service.MemberService;

/**
 * @author Gopi
 *
 */
@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/member/" })
public class MemberController {

	

	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private IntegrationService integrationService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@GetMapping("upload/{quotationId}/{type}/{tmpPolicyId}")
	public ApiResponseDto<MemberBulkResponseDto> getUploadedMemberData(@PathVariable("quotationId") Long quotationId,
			@PathVariable("type") String type,@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		return memberService.getUploadedMemberData(quotationId, type,tmpPolicyId);
	}

	@PostMapping("import/{quotationId}/{batchId}/{username}")
	public ApiResponseDto<MemberBulkResponseDto> importMemberData(@PathVariable("quotationId") Long quotationId,
			@PathVariable("batchId") Long batchId, @PathVariable("username") String username) {
		return memberService.importMemberData(quotationId, batchId, username);
	}

	@GetMapping("quotation/{quotationId}")
	public ApiResponseDto<List<MemberDto>> findAll(@PathVariable("quotationId") Long quotationId) {
		return memberService.findAll(quotationId);
	}

	@PostMapping(value = "/filter")
	public ApiResponseDto<List<MemberDto>> filter(@RequestBody MemberSearchDto memberSearchDto) {
		return memberService.filter(memberSearchDto);
	}

	@GetMapping("{memberId}/{taggedStatusId}")
	public ApiResponseDto<MemberDto> findById(@PathVariable("memberId") Long memberId,
			@PathVariable("taggedStatusId") Long taggedStatusId) {
		return memberService.findById(memberId, taggedStatusId);
	}	
	
	@GetMapping("downloads/errorlog/{batchId}")
	public ResponseEntity<byte[]> findAllErrors(@PathVariable("batchId") Long batchId) {
		byte[] tt = memberService.findAllErrors(batchId);

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("application/nvd.ms-excel"));
		header.setContentLength(tt.length);
		header.set("Content-Disposition", "attachment; filename=errors.xlsx");

		return new ResponseEntity<>(tt, header, HttpStatus.OK);
	}
	
	@GetMapping("domdownloads/errorlog/{batchId}")
	public ResponseEntity<byte[]> findAlldomErrors(@PathVariable("batchId") Long batchId) {
		byte[] tt = memberService.findAlldomErrors(batchId);

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("application/nvd.ms-excel"));
		header.setContentLength(tt.length);
		header.set("Content-Disposition", "attachment; filename=errors.xlsx");

		return new ResponseEntity<>(tt, header, HttpStatus.OK);
	}

	@GetMapping("downloads/sample")
	public ResponseEntity<byte[]> downloadSample() {
		byte[] tt = memberService.downloadSample();

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("application/nvd.ms-excel"));
		header.setContentLength(tt.length);
		header.set("Content-Disposition", "attachment; filename=errors.xlsx");

		return new ResponseEntity<>(tt, header, HttpStatus.OK);
	}
	
	@GetMapping("domdownloads/sample")
	public ResponseEntity<byte[]> downloadMidLeaverSample() {
		byte[] tt = memberService.downloadMidLeaverSample();

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("application/nvd.ms-excel"));
		header.setContentLength(tt.length);
		header.set("Content-Disposition", "attachment; filename=errors.xlsx");

		return new ResponseEntity<>(tt, header, HttpStatus.OK);
	}
	
	@GetMapping("downloads/sampleforclaim")
	public ResponseEntity<byte[]> downloadSampleForClaim() {
		byte[] tt = memberService.downloadSampleforCalim();

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("application/nvd.ms-excel"));
		header.setContentLength(tt.length);
		header.set("Content-Disposition", "attachment; filename=sample.xlsx");

		return new ResponseEntity<>(tt, header, HttpStatus.OK);
	}

	@DeleteMapping("{batchId}")
	public ApiResponseDto<MemberBulkResponseDto> deleteBatch(@PathVariable("batchId") Long batchId) {
		return memberService.deleteBatch(batchId);
	}

	// Single Member Upload Detail

	@PostMapping("uploadmember")
	public ApiResponseDto<MemberDto> uploadMember(@RequestBody MemberDto memberDto) {
		return memberService.uploadMember(memberDto);

	}

	@PutMapping("updatemember/{id}")
	public ApiResponseDto<MemberDto> updateMember(@PathVariable(value = "id") Long id,
			@RequestBody MemberDto memberDto) {
		return memberService.updateMember(id, memberDto);

	}

	// Single Member Address Upload Detail
	@PostMapping("uploadmemberaddress/{memberId}")
	public ApiResponseDto<List<MemberAddressDto>> uploadMemberAddress(@RequestBody MemberAddressDto memberAddressDto,
			@PathVariable(value = "memberId") Long memberId) {
		return memberService.uploadMemberAddress(memberAddressDto, memberId);
	}

	@PutMapping("updatememberaddress/{id}")
	public ApiResponseDto<List<MemberAddressDto>> updateMemberAddress(@PathVariable(value = "id") Long id,
			@RequestBody MemberAddressDto memberAddressDto) {
		return memberService.updateMemberAddress(id, memberAddressDto);
	}
	
	@DeleteMapping("deletememberaddress/{id}")
	public ApiResponseDto<List<MemberAddressDto>> deleteMemberAddress(@PathVariable(value ="id") Long id){	
		return memberService.deleteMemberAddress( id);
	}

	// Single Member Bank Upload Detail
	@PostMapping("uploadmemberbank/{memberId}")
	public ApiResponseDto<List<MemberBankAccountDto>> uploadMemberBank(@RequestBody MemberBankAccountDto memberBankAccountDto,
			@PathVariable(value = "memberId") Long memberId) {
		return memberService.uploadMemberBank(memberBankAccountDto, memberId);
	}

	@PutMapping("updatememberbank/{id}")
	public ApiResponseDto<List<MemberBankAccountDto>> updateMemberBank(@PathVariable(value = "id") Long id,
			@RequestBody MemberBankAccountDto memberBankAccountDto) {
		return memberService.updateMemberBank(id, memberBankAccountDto);
	}
	
	@DeleteMapping("deletememberbank/{id}")
	public ApiResponseDto<List<MemberBankAccountDto>> deleteMemberBank(@PathVariable(value = "id") Long id){
		return memberService.deleteMemberBank(id);
		
	}

	// Single Nominee Upload Detail
	@PostMapping("uploadmembernominee/{memberId}")
	public ApiResponseDto<List<MemberNomineeDto>> uploadMemberNominee(@RequestBody MemberNomineeDto memberNomineeDto,
			@PathVariable(value = "memberId") Long memberId) {
		return memberService.uploadMemberNominee(memberNomineeDto, memberId);
	}

	@PutMapping("updatemembernominee/{id}")
	public ApiResponseDto<List<MemberNomineeDto>> updateMemberNominee(@RequestBody MemberNomineeDto memberNomineeDto,
			@PathVariable(value = "id") Long id) {
		return memberService.updateMemberNominee(memberNomineeDto, id);
	}
	
	@DeleteMapping("deletemembernominee/{id}")
	public ApiResponseDto<List<MemberNomineeDto>> deleteMemberNominee(@PathVariable (value = "id") Long id){
		
		return  memberService.deleteMemberNominee(id);
	}

	// Single Appointee Upload Detail
	@PostMapping("uploadmemberappointee/{nomineeId}/{memberId}")
	public ApiResponseDto<List<MemberAppointeeDto>> uploadMemberAppointee(@RequestBody MemberAppointeeDto memberAppointeeDto,
			@PathVariable(value = "nomineeId") Long nomineeId, @PathVariable(value = "memberId") Long memberId) {
		return memberService.uploadMemberAppointee(memberAppointeeDto, nomineeId, memberId);
	}

	@PutMapping("updatememberappointee/{id}")
	public ApiResponseDto<List<MemberAppointeeDto>> updateMemberAppointee(@RequestBody MemberAppointeeDto memberAppointeeDto,
			@PathVariable(value = "id") Long id) {
		return memberService.updateMemberAppointee(memberAppointeeDto, id);
	}
	
	@DeleteMapping("deletememberappointee/{id}")
	public ApiResponseDto<List<MemberAppointeeDto>> deleteMemberAppointee(@PathVariable(value ="id") Long id ){
		
		return memberService.deleteMemberAppointee(id);
	}
	
	@GetMapping("membercodestatus/{quotationId}/{code}/{memberId}")
	public ApiResponseDto<MemberDto>memberCodeStatus(@PathVariable(value = "quotationId") Long quotationId,@PathVariable(value = "code") String code,@PathVariable(value = "memberId") Long memberId){
		return memberService.memberCodeStatus(quotationId,code,memberId);
	}
	
	@GetMapping("membercount/{quotationId}")
	public ApiResponseDto<MemberBatchDto>memberTotalCount(@PathVariable(value = "quotationId") Long quotationId){
		return memberService.memberTotalCount(quotationId);
	}

	//Bulk Upload
	@PostMapping("upload")
	public ApiResponseDto<MemberBulkResponseDto> uploadMemberData(@RequestParam Long quotationId,
			@RequestParam String username, @RequestParam Long tmpPolicyId, @RequestBody MultipartFile file)
			throws IllegalStateException, IOException {
		return integrationService.uploadMemberData(quotationId, username, tmpPolicyId, file);
	}

	@PostMapping("uploadClaim")
	public ApiResponseDto<MemberBulkResponseDto> uploadMemberDataClaim(@RequestParam Long pmstpolicyId,
			@RequestParam String username, @RequestBody MultipartFile file) throws IllegalStateException, IOException {
		return integrationService.uploadMemberDataClaim(pmstpolicyId, username, file);
	}

	@DeleteMapping("deletememberdetail/{id}")
	public ApiResponseDto<List<MemberDto>> delete(@PathVariable(value="id") Long id) {
		return memberService.delete(id);
	}
	
	@GetMapping("downloads/errorlogforClaims/{batchId}")
	public ResponseEntity<byte[]> findforclaimsErrors(@PathVariable("batchId") Long batchId) {
		byte[] tt = memberService.findforclaimsErrors(batchId);

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("application/nvd.ms-excel"));
		header.setContentLength(tt.length);
		header.set("Content-Disposition", "attachment; filename=errors.xlsx");

		return new ResponseEntity<>(tt, header, HttpStatus.OK);
	}
	
	@GetMapping("employeecodeexists/{masterPolicyId}/{tmpPolicyId}/{tmpMemberId}/{employeecode}")
	public ApiResponseDto<Boolean> employeeCodeExists(@PathVariable("masterPolicyId") Long masterPolicyId, 
			@PathVariable("tmpPolicyId") Long tmpPolicyId,
			@PathVariable("tmpMemberId") Long tmpMemberId,
			@PathVariable("employeecode") String employeeCode) {
		return memberService.isEmployeeCodeExist(masterPolicyId, tmpPolicyId, tmpMemberId, employeeCode);
	}
	
	@GetMapping("employeepanexists/{masterPolicyId}/{tmpPolicyId}/{tmpMemberId}/{employeepan}")
	public ApiResponseDto<Boolean> employeePanExists(@PathVariable("masterPolicyId") Long masterPolicyId, 
			@PathVariable("tmpPolicyId") Long tmpPolicyId,
			@PathVariable("tmpMemberId") Long tmpMemberId,
			@PathVariable("employeepan") String employeePan) {
		return memberService.isEmployeePanExist(masterPolicyId, tmpPolicyId, tmpMemberId, employeePan);
	}
	
	@GetMapping("employeeaadharexists/{masterPolicyId}/{tmpPolicyId}/{tmpMemberId}/{employeeaadhar}")
	public ApiResponseDto<Boolean> employeeAadharExists(@PathVariable("masterPolicyId") Long masterPolicyId, 
			@PathVariable("tmpPolicyId") Long tmpPolicyId,
			@PathVariable("tmpMemberId") Long tmpMemberId,
			@PathVariable("employeeaadhar") String employeeAadhar) {
		return memberService.isEmployeeAadharExist(masterPolicyId, tmpPolicyId, tmpMemberId, employeeAadhar);
	}
}
