package com.lic.epgs.gratuity.policy.renewal.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;

import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberDto;
import com.lic.epgs.gratuity.policy.member.dto.PolicyMemberSearchDto;
import com.lic.epgs.gratuity.policy.renewal.dto.RenewalPolicySearchDto;
import com.lic.epgs.gratuity.policy.renewal.service.RenewalService;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalPolicyTMPMemberDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;




@RestController
@CrossOrigin("*")
@RequestMapping("/api/policy/renewal")
public class RenewalController {
	
	

	@Autowired
	private RenewalService renewalService;

	@GetMapping("download/remainderrenewal/{id}")
	public ResponseEntity<byte[]> downloadSample(@PathVariable ("id") Long id) {

		byte[] tt = renewalService.downloadSampleRenewal(id);

		HttpHeaders header = new HttpHeaders();

		header.setContentType(MediaType.parseMediaType("application/nvd.pdf"));
		header.setContentLength(tt.length);
		header.set("Content-Disposition", "attachment; filename=renewal.pdf");

		return new ResponseEntity<>(tt, header, HttpStatus.OK);

	}

	@GetMapping("downloads/remaindernotice/{id}")
	public ResponseEntity<byte[]> downloadSampleDynamic(@PathVariable ("id") Long id) {

		byte[] tt = renewalService.downloadSampleDynamic(id);

		HttpHeaders header = new HttpHeaders();

		header.setContentType(MediaType.parseMediaType("application/nvd.pdf"));
		header.setContentLength(tt.length);
		header.set("Content-Disposition", "attachment; filename=renewal.pdf");

		return new ResponseEntity<>(tt, header, HttpStatus.OK);

	}

	
	@PostMapping("/fetchdetails")
	public ApiResponseDto<List<PolicyDto>> fetchPolicyForNewRenewalQuotation(@RequestBody RenewalPolicySearchDto renewalPolicySearchDto) {

		return renewalService.fetchPolicyForNewRenewalQuotation(renewalPolicySearchDto);
	}
	
	@GetMapping(value = {"generatecbRenewalpdf"})
	public void getcandbsheetpdf( @RequestParam("tmpPolicyId") Long tmpPolicyId,HttpServletRequest request,HttpServletResponse response) throws IOException {
	 
		String getcandbsheetpdf=renewalService.getcandbsheetpdf(tmpPolicyId);
		
		ServletContext context = request.getServletContext();
		// construct the complete absolute path of the file
		File downloadFile = new File(getcandbsheetpdf);
		FileInputStream inputStream = new FileInputStream(downloadFile);
		// get MIME type of the file
		String mimeType = context.getMimeType(getcandbsheetpdf); 
		if (mimeType == null) {
		mimeType = "application/octet-stream";
	}
	// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", tmpPolicyId+ ".pdf");
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
		if (new File(getcandbsheetpdf).exists()) {
			//new File(pdfDocumentValuationReport).delete();
			}
	}	

	@GetMapping("downloads/candbsheet/{tmpPolicyId}")
	public ResponseEntity<byte[]> findByPolicyId(@PathVariable ("tmpPolicyId") Long tmpPolicyId) {
		byte[] tt = null;
		
			tt = renewalService.findBytmpPolicyId(tmpPolicyId);
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("application/nvd.ms-excel"));
		header.setContentLength(tt.length);
		header.set("Content-Disposition", "attachment; filename=CandBsheet.xlsx");

		return new ResponseEntity<>(tt, header, HttpStatus.OK);
	}
	
	@GetMapping(value = { "renewalNoticepdf" })
	public void renewalNoticepdf(@RequestParam("Id") Long Id,HttpServletRequest request,HttpServletResponse response) throws IOException {

		String renewalNoticepdf = renewalService.renewalNoticepdf(Id);

		ServletContext context = request.getServletContext();
		// construct the complete absolute path of the file
		File downloadFile = new File(renewalNoticepdf);
		FileInputStream inputStream = new FileInputStream(downloadFile);
		// get MIME type of the file
		String mimeType = context.getMimeType(renewalNoticepdf);
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",Id + ".pdf");
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
		if (new File(renewalNoticepdf).exists()) {
			// new File(pdfDocumentValuationReport).delete();
		}
	}
	
	@GetMapping(value = { "renewalRemainderNoticepdf" })
	public void renewalRemainderNoticepdf(@RequestParam("Id") Long Id,HttpServletRequest request,HttpServletResponse response) throws IOException {

		String renewalRemainderNoticepdf = renewalService.renewalRemainderNoticepdf(Id);

		ServletContext context = request.getServletContext();
		// construct the complete absolute path of the file
		File downloadFile = new File(renewalRemainderNoticepdf);
		FileInputStream inputStream = new FileInputStream(downloadFile);
		// get MIME type of the file
		String mimeType = context.getMimeType(renewalRemainderNoticepdf);
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",Id + ".pdf");
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
		if (new File(renewalRemainderNoticepdf).exists()) {
			// new File(pdfDocumentValuationReport).delete();
		}
	}
	
	@PostMapping(value = "/filter/{type}")
    public ApiResponseDto<List<RenewalPolicyTMPMemberDto>> filter(@RequestBody RenewalPolicyTMPMemberDto renewalPolicyTMPMemberDto,
    		@PathVariable("type") String type) {
        return renewalService.filter(renewalPolicyTMPMemberDto, type);
    }
}
