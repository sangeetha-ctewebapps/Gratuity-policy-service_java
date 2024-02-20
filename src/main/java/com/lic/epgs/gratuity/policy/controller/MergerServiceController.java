package com.lic.epgs.gratuity.policy.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.dto.MemberCategoryDto;
import com.lic.epgs.gratuity.common.entity.MemberCategoryEntity;
import com.lic.epgs.gratuity.policy.claim.dto.GratuityCalculationsDto;
import com.lic.epgs.gratuity.policy.dto.MasterPolicyMergerDto;
import com.lic.epgs.gratuity.policy.dto.MergerNewSearchFilterDto;
import com.lic.epgs.gratuity.policy.dto.PmstTmpMergerPropsDto;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailDto;
import com.lic.epgs.gratuity.policy.dto.PolicyContributionDetailMergerDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.entity.PmstTmpMergerPropsEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.service.MergerService;
import com.lic.epgs.gratuity.policyservices.common.dto.PremiumGstRefundDto;
import com.lic.epgs.gratuity.quotation.valuationmatrix.dto.ValuationMatrixDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/merger/")
public class MergerServiceController {
	
	@Autowired
 private MergerService mergerService;
	

	
	@PostMapping("/getexitingpolicynofornew")
	public ApiResponseDto<List<PolicyDto>> GetExitingPolicyNoforNew(@RequestBody MergerNewSearchFilterDto mergerNewSearchFilterDto){
		return mergerService.GetExitingPolicyNoinMaster(mergerNewSearchFilterDto);
	}
	
	
	@GetMapping("/inprogressSearchByPolicynumber/{policyNumber}")
	public ApiResponseDto<RenewalPolicyTMPDto> InprogressSearchByPolicyNumber(@PathVariable String policyNumber){
		return mergerService.inprogressSearchByPolicynumber(policyNumber);
	}
	
	@GetMapping("/memberCategoryDetailsByPolicynumber/{policyNumber}")
	public ApiResponseDto<List<MemberCategoryDto>> MemberCategoryDetailsByPolicyNumber(@PathVariable String policyNumber){
		return mergerService.memberCategoryDetailsByPolicynumber(policyNumber);
	}
	
	@GetMapping("/mergerPropsDetailsByPolicyNumber/{policyNumber}")
	public ApiResponseDto<List<PmstTmpMergerPropsDto>> MergerPropsDetailsByPolicyNumber(@PathVariable String policyNumber){
		return mergerService.mergerPropsDetailsByPolicyNumber(policyNumber);
	}
	
	@GetMapping("/newsearchvalidationusingtype/{policyNumber}/{servicetype}")
	public ApiResponseDto<PolicyDto> SearchFilterListPolicy(@PathVariable String policyNumber,@PathVariable String servicetype) {
		
		return mergerService.SearchFilterListPolicy(policyNumber,servicetype);
	}
	
	// add request body
	
	@GetMapping("/inprogresssearchfilter")
	public ApiResponseDto<List<PmstTmpMergerPropsDto>> inprogresssearchfilter(@RequestBody PmstTmpMergerPropsDto pmstTmpMergerPropsDto) {
		
		return mergerService.inprogresssearchfilter(pmstTmpMergerPropsDto);
	}
	
	// add request body

	@GetMapping("ExistingSearchFilter/{policyNumber}")
	public ApiResponseDto<List<PmstTmpMergerPropsDto>>getExistingSerchFilter(@PathVariable("policyNumber") String policyNumber ){
		return mergerService.getExistingSearchFilter(policyNumber);
		}
	
	
	
	//Save Merger and to conver to tmptable
	
	@PostMapping("saveMerger")
	public ApiResponseDto<PmstTmpMergerPropsDto> SaveMergerinService(@RequestBody PmstTmpMergerPropsDto pmstTmpMergerPropsDto){
		return mergerService.saveMerger(pmstTmpMergerPropsDto);
	}
	
	@PostMapping("updateMerger")
	public ApiResponseDto<PmstTmpMergerPropsDto> updateMergerinService(@RequestBody PmstTmpMergerPropsDto pmstTmpMergerPropsDto){
		return mergerService.updateMergerinService(pmstTmpMergerPropsDto);
	}
	
	
	@PostMapping("approveMerger")
	public ApiResponseDto<PmstTmpMergerPropsDto> approvemerger(@RequestBody PmstTmpMergerPropsDto pmstTmpMergerPropsDto){
		return mergerService.approvemerger(pmstTmpMergerPropsDto);
	}
	//not use
	
	@PostMapping("mergerpremium/mergemembesourcetodes")
	public ApiResponseDto<PmstTmpMergerPropsDto> mergemembesourcetodes(@RequestBody PmstTmpMergerPropsDto pmstTmpMergerPropsDto){
		return mergerService.mergemembesourcetodes(pmstTmpMergerPropsDto);
	}
	
	@PutMapping("mergerStatusUpdate")
	public ApiResponseDto<PmstTmpMergerPropsDto> mergerStatusUpdate(@RequestBody PmstTmpMergerPropsDto pmstTmpMergerPropsDto){
		return mergerService.mergerStatusUpdate(pmstTmpMergerPropsDto);
	}


@GetMapping(value = {"download/mergeradjustmentvoucherpdf"})
	public void quotationValuationReport( @RequestParam("policyId") Long policyId, @RequestParam("reportType") String reportType,HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		String pdfDocumentValuationReport=mergerService.generateReport(policyId,reportType);
		
		ServletContext context = request.getServletContext();
		// construct the complete absolute path of the file
		File downloadFile = new File(pdfDocumentValuationReport);
		FileInputStream inputStream = new FileInputStream(downloadFile);
		// get MIME type of the file
		String mimeType = context.getMimeType(pdfDocumentValuationReport); 
		if (mimeType == null) {
		mimeType = "application/octet-stream";
	}
	// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", policyId+ ".pdf");
		response.setHeader(headerKey, headerValue);
			// get output stream of the response
		OutputStream outStream = response.getOutputStream();
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
		outStream.write(buffer, 0, bytesRead);
		}
		inputStream.close();
		outStream.close();
		if (new File(pdfDocumentValuationReport).exists()) {
			//new File(pdfDocumentValuationReport).delete();
			}
	}
	
	@GetMapping(value = {"download/finalpremiumreceiptpdf"})
	public void quotationValuationReport( @RequestParam("policyId") Long policyId,HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		String premiumreceipt=mergerService.premiumreceipt(policyId);
		
		ServletContext context = request.getServletContext();
		// construct the complete absolute path of the file
		File downloadFile = new File(premiumreceipt);
		FileInputStream inputStream = new FileInputStream(downloadFile);
		// get MIME type of the file
		String mimeType = context.getMimeType(premiumreceipt); 
		if (mimeType == null) {
		mimeType = "application/octet-stream";
	}
	// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", policyId+ ".pdf");
		response.setHeader(headerKey, headerValue);
			// get output stream of the response
		OutputStream outStream = response.getOutputStream();
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
		outStream.write(buffer, 0, bytesRead);
		}
		inputStream.close();
		outStream.close();
		if (new File(premiumreceipt).exists()) {
			//new File(pdfDocumentValuationReport).delete();
			}
	}
	
	@GetMapping("gettmpMemberPropsDetails/{mergerid}")
	public ApiResponseDto<PmstTmpMergerPropsDto> gettmpMemberPropsDetails(
			@PathVariable(value = "mergerid") Long mergerid) {
		return mergerService.gettmpMemberPropsDetails(mergerid);
	}

	@PutMapping("updatemerger")
	public ApiResponseDto<PmstTmpMergerPropsDto> updatemerger(
			@RequestBody PmstTmpMergerPropsDto pmstTmpMergerPropsDto) {
		return mergerService.updateid(pmstTmpMergerPropsDto);

	}
	
	
	//general refund calculation
	@PostMapping("generalrefundcalculation")
	public ApiResponseDto<GratuityCalculationsDto> generalRefundcalculation(@RequestBody GratuityCalculationsDto gratitutyCalculationDto) {
		return mergerService.generalRefundCalculation(gratitutyCalculationDto);
	}
	
	
	@PostMapping(value = "mergerApprove/{mergerPropsId}/{username}")
	public ApiResponseDto<PmstTmpMergerPropsDto> mergerApprove(@PathVariable("mergerPropsId") Long mergerPropsId,
			@PathVariable("username") String username) {
		return mergerService.mergerApprove(mergerPropsId, username);
	}
	
	@PostMapping("/mergerForApprove/{sourceTmpPolicyID}/{username}")
	public ApiResponseDto<PmstTmpMergerPropsDto> forApprove(@PathVariable("sourceTmpPolicyID") Long sourceTmpPolicyID, @PathVariable("username") String username){
		return mergerService.forApprove(sourceTmpPolicyID,username);
	}
	
	@PostMapping("/mergerForReject/{sourceTmpPolicyID}/{username}")
	public ApiResponseDto<PmstTmpMergerPropsDto> forReject(@PathVariable("sourceTmpPolicyID") Long sourceTmpPolicyID, @PathVariable("username") String username){
		return mergerService.forReject(sourceTmpPolicyID,username);
	}
	
	@PostMapping("/mergerSendBackToMaker/{sourceTmpPolicyID}/{username}")
	public ApiResponseDto<PmstTmpMergerPropsDto> sendBackToMaker(@PathVariable("sourceTmpPolicyID") Long sourceTmpPolicyID, @PathVariable("username") String username){
		return mergerService.sendBackToMaker(sourceTmpPolicyID,username);
	}
	
	@PostMapping("/mergerSentForApproval/{sourceTmpPolicyID}/{username}/{destinationTmpPolicyId}")
	public ApiResponseDto<PmstTmpMergerPropsDto> sentForApproval(
			@PathVariable("sourceTmpPolicyID") Long sourceTmpPolicyID, @PathVariable("username") String username,
			@PathVariable("destinationTmpPolicyId") Long destinationTmpPolicyId) {
		return mergerService.sendForApproval(sourceTmpPolicyID,username,destinationTmpPolicyId);
	}
	
	@PostMapping(value = "makePaymentAdjustmentForMerger")
	public ApiResponseDto<RenewalPolicyTMPDto> makePaymentAdjustmentForMerger(
			@RequestBody PolicyContributionDetailMergerDto policyContributionDetailDto) {
		return mergerService.makePaymentAdjustmentForMerger(policyContributionDetailDto);
	}
	
	@PostMapping(value = "updatePaymentAdjustmentForMerger")
	public ApiResponseDto<RenewalPolicyTMPDto> updatePaymentAdjustmentForMerger(
			@RequestBody PolicyContributionDetailMergerDto policyContributionDetailDto) {
		return mergerService.updatePaymentAdjustmentForMerger(policyContributionDetailDto);
	}
	
	@PostMapping(value = "generateValuationForMerger")
	public ApiResponseDto<ValuationMatrixDto> generateValuationForMerger(@RequestBody PmstTmpMergerPropsDto pmstTmpMergerPropsDto) {
		return mergerService.generateValuationForMerger(pmstTmpMergerPropsDto);
	}
	
	@GetMapping(value = "getValuationMatrixForMerger/{tmpPolicyId}")
	public ApiResponseDto<ValuationMatrixDto> getValuationMatrixForMerger(@PathVariable("tmpPolicyId") Long tmpPolicyId) {
		return mergerService.getValuationMatrixForMerger(tmpPolicyId);
	}
	
	@GetMapping(value = "getOverViewOfGstAndPremium/{policyNumber}")
	public ApiResponseDto<PremiumGstRefundDto> getOverViewOfGstAndPremium(@PathVariable("policyNumber") String policyNumber){
		return mergerService.getOverViewOfGstAndPremium(policyNumber);
	}
	
	
}
