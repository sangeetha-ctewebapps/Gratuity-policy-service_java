package com.lic.epgs.gratuity.dummy.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.repository.CommonMasterProductRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterStateRepository;

@RestController
@RequestMapping("api/doesitwork")
public class DummyController {
	
	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;
	
	@Value("${app.fundEndpoint}")
	private String fundEndpoint;
	
	@Value("${app.bulkServiceEndPoint}")
	private String bulkService;
	
	@Value("${app.commonModuleServiceEndpoint}")
	private String commonModuleServiceEndpoint;
	
	@Value("${app.accountingServiceEndpoint}")
	private String accountingServiceEndpoint;
	
	@Autowired
	private CommonMasterStateRepository commonMasterStateRepository;
	
	@Autowired
	private CommonMasterProductRepository commonMasterProductRepository;
	
	@GetMapping("/")
	public String gratuityServiceWorks() {
		StringBuffer r = new StringBuffer();
		r.append("Gratuity Service: Works!");
		r.append(", Bulk Service: ");
		r.append(bulkServiceWorks());
		r.append(", Fund Service: ");
		r.append(fundServiceWorks());
		r.append(", Common Service: ");
		r.append(commonServiceWorks());
		r.append(", Accounting Service: ");
		r.append(accountingServiceWorks());
		r.append(", HSN Code Service: ");
		r.append(hsnCodeServiceWorks());
		r.append(", Lic Common DB Connection: ");
		r.append(licCommonDatabase());
		r.append(", Customer Common DB Connection: ");
		r.append(customerCommonDatabase());
		
		return r.toString();
	}
	
	
	private String fundServiceWorks() {
		try {
			URL url = new URL(fundEndpoint + "log");
			System.out.println(url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
			
			if (conn.getResponseCode() != 200) {
				return "Expected 200, received " + conn.getResponseCode();
			}

			return "Works!";
		} catch (MalformedURLException e) {
			return "Error: " + e.getMessage();
		} catch (IOException e) {
			return "Error: " + e.getMessage();
		}
	}
	
	private String bulkServiceWorks() {
		try {
			URL url = new URL(bulkService + "/api/member/upload");
			System.out.println(url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
			
			if (conn.getResponseCode() != 405) {
				return "Expected 405, received " + conn.getResponseCode();
			}

			return "Works";
		} catch (MalformedURLException e) {
			return "Error: " + e.getMessage();
		} catch (IOException e) {
			return "Error: " + e.getMessage();
		}
	}

	private String commonServiceWorks() {
		try {
			URL url = new URL(commonModuleServiceEndpoint);
			System.out.println(url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
			
			if (conn.getResponseCode() != 200) {
				return "Expected 200, received " + conn.getResponseCode();
			}

			return "Works!";
		} catch (MalformedURLException e) {
			return "Error: " + e.getMessage();
		} catch (IOException e) {
			return "Error: " + e.getMessage();
		}
	}
	
	private String accountingServiceWorks() {
		try {
			URL url = new URL(accountingServiceEndpoint);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
			
			if (conn.getResponseCode() != 200) {
				return "Expected 200, received " + conn.getResponseCode();
			}
			
			return "Works!";
		} catch (MalformedURLException e) {
			return "Error: " + e.getMessage();
		} catch (IOException e) {
			return "Error: " + e.getMessage();
		}
	}
	
	private String hsnCodeServiceWorks() {
		try {
			URL url = new URL(accountingServiceEndpoint + "/accountingcoreservice/ePGS/Accounts/HsnCode/HSNDetail?hsnCode=997132");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
			
			if (conn.getResponseCode() != 200) {
				return "Expected 200, received " + conn.getResponseCode();
			}
			
			return "Works!";
		} catch (MalformedURLException e) {
			return "Error: " + e.getMessage();
		} catch (IOException e) {
			return "Error: " + e.getMessage();
		}
	}
	
	private String licCommonDatabase() {
		try {
			commonMasterStateRepository.getStateCode("Tamil Nadu");
			return "Works!";
		} catch (Exception e) {
			return "Error: " + e.getMessage();
		}
	}
	
	private String customerCommonDatabase() {
		try {
			commonMasterProductRepository.findById(8L);
			return "Works!";
		} catch (Exception e) {
			return "Error: " + e.getMessage();
		}
	}
}
