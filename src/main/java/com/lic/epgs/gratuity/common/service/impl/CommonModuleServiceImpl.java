package com.lic.epgs.gratuity.common.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.gratuity.accountingservice.dto.HSNCodeDto;
import com.lic.epgs.gratuity.common.service.CommonModuleService;

@Service
public class CommonModuleServiceImpl implements CommonModuleService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Value("${app.commonModuleServiceEndpoint}")
	private String commonModuleServiceEndpoint;

	@Override
	public String getProductCode(Long productId) {
		try {
			URL url = new URL(
					commonModuleServiceEndpoint + "/api/proposal/getProductDetailsById?productId=" + productId);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				logger.error("HTTP Status Error: [getProductCode] Expected 200, got " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);
			JsonNode getProduct=actualObj.path("responseData");
			return getProduct.path("productCode").textValue();

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	
	
	@Override
	public JsonNode getProductCodeJsonNode(Long productId) {
		try {
			URL url = new URL(
					commonModuleServiceEndpoint + "/api/proposal/getProductDetailsById?productId=" + productId);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				logger.error("HTTP Status Error: [getProductCode] Expected 200, got " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);
			JsonNode getProduct=actualObj.path("responseData");
			return getProduct;

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}


	@Override
	public String getVariantCode(Long variantId) {
		try {
			URL url = new URL(
					commonModuleServiceEndpoint + "/api/proposal/getProductDetailsById?productvariantId=" + variantId);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				logger.error("HTTP Status Error: [getVariantCode] Expected 200, got " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);
			JsonNode getProduct=actualObj.path("responseData");
			return getProduct.path("variantVersion").textValue();

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public JsonNode getProposalNumber(String proposalNumber) {
		try {
			URL url = new URL(commonModuleServiceEndpoint
					+ "/api/proposal/getProposalDetailsByProposalNumber?proposalNumber=" + proposalNumber);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				logger.error("HTTP Status Error: [getProductCode] Expected 200, got " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);
			return actualObj.path("responseData");
		

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
}
