package com.lic.epgs.gratuity.simulation.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.policy.dto.ShowDepositDto;
import com.lic.epgs.gratuity.policy.repository.QuotationChargeRepository;
import com.lic.epgs.gratuity.simulation.dto.DepositAdjustementDto;
import com.lic.epgs.gratuity.simulation.dto.DepositDto;
import com.lic.epgs.gratuity.simulation.entity.DepositEntity;
import com.lic.epgs.gratuity.simulation.helper.DepositHelper;
import com.lic.epgs.gratuity.simulation.repository.DepositRepository;
import com.lic.epgs.gratuity.simulation.service.DepositService;

/**
 * @author Vigneshwaran
 *
 */

@Service
public class DepositServiceImpl implements DepositService {
	
	@Value("${app.commonModuleServiceEndpoint}")
	private String commonModuleServiceEndpoint;
	
	@Value("${app.isDevEnvironment}")
	private Boolean isDevEnvironment;
	
	
	@Autowired
	private DepositRepository depositRepository;
	
	@Autowired
	private QuotationChargeRepository quotationChargeRepository;

	@Override
	public ApiResponseDto<List<DepositDto>> findAllByProposalNumber(String proposalNo) {	
		ArrayList<DepositDto> getDepositDto = new ArrayList<>();	
		if(isDevEnvironment == false) {
		
//				ArrayList<DepositDto> getDepositDto = new ArrayList<>();

				try {

					URL url = new URL(commonModuleServiceEndpoint + "accountsgratuityservice/ePGS/Accounts/Gratuity/ShowDeposit");

					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type", "application/json");
					conn.setRequestProperty("Accept", "application/json");
					conn.setDoOutput(true);

					ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
					String json = ow.writeValueAsString(proposalNo);



					try (OutputStream os = conn.getOutputStream()) {
						byte[] input = json.getBytes("utf-8");
						os.write(input);
					}

					if (conn.getResponseCode() != 200) {
						throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
					}


					BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

					String output = br.readLine();

					conn.disconnect();

					ObjectMapper mapper = new ObjectMapper();

					JsonFactory factory = mapper.getFactory();

					JsonParser parser = factory.createParser(output);

					JsonNode actualObj = mapper.readTree(parser);

					System.out.println(actualObj);

//					output = "[{raj : 10},{push :10}]";

					ObjectMapper objectMapper = new ObjectMapper();

					JsonNode node = objectMapper.readValue(output, JsonNode.class);
					System.out.println(node.asText());
					
					List<DepositEntity> getDepositEntity =new ArrayList<DepositEntity>();
					for (JsonNode jsonNode : actualObj) {

						DepositDto depositDto = new DepositDto();
						depositDto.setCollectionNumber(jsonNode.get("collectionNo").asLong());
//						depositDto.setCollectionDate(jsonNode.get("collectionDate").toString());
						depositDto.setCollectionType(jsonNode.get("collectionType").asInt());
						depositDto.setCollectionMode(jsonNode.get("collectionMode").toString());
						depositDto.setCollectionAmount(jsonNode.get("collectionAmount").asDouble());
						depositDto.setCollectionStatus(jsonNode.get("collectionStatus").toString());
						depositDto.setUtilizedAmount(jsonNode.get("utilizedAmount").asDouble());
						depositDto.setBalanceAmount(jsonNode.get("balanceAmount").asDouble());
						
					
						String collectionDate=jsonNode.get("collectionDate").toString().replace("\"", "");

						Date collectiondAte = DateUtils.convertStringToDate1(collectionDate);
							
						System.out.println(collectiondAte);
			
						String vocEffDate=jsonNode.get("voucherEffectiveDate").toString().replace("\"", "");
						Date vocEffDateformate = DateUtils.convertStringToDate1(vocEffDate);
						
						
						String adjDate=jsonNode.get("adjustmentDate").toString().replace("\"", "");
						Date adjDateformate = DateUtils.convertStringToDate1(adjDate);
						
						depositDto.setCollectionDate(collectiondAte);
						depositDto.setVoucherEffectiveDate(vocEffDateformate);
						depositDto.setAdjustmentDate(adjDateformate);
						depositDto.setLockStatus(jsonNode.get("lockStatus").toString());
						depositDto.setAdjustmentAvailability(jsonNode.get("adjustmentAvailability").toString());
						depositDto.setAdjustmentNo(jsonNode.get("adjustmentNo").asLong());
						depositDto.setAdjuestmentAmount(jsonNode.get("adjustmentAmount").asDouble());
						depositDto.setAdjustmentStatus(jsonNode.get("adjustmentStatus").toString());			
						
						
						DepositEntity depositEntity = new DepositEntity();
						
						depositEntity.setAvailableAmount(depositDto.getBalanceAmount());
						depositEntity.setDepositAmount(depositDto.getBalanceAmount());
						depositEntity.setCollectionNumber(depositDto.getCollectionNumber().toString());
						depositEntity.setChequeRealisationDate(depositDto.getVoucherEffectiveDate());
						depositEntity.setCollectionStatus(depositDto.getCollectionStatus());
						depositEntity.setCreatedBy("maker");
						depositEntity.setTransactionMode(depositDto.getTransactionMode());
						depositEntity.setCollectionDate(collectiondAte);
						depositEntity.setCreatedDate(new Date());
						depositEntity.setIsActive(true);
						depositEntity.setProposalNumber(proposalNo);
						
						getDepositEntity.add(depositEntity);
						
						getDepositDto.add(depositDto);

					}
					depositRepository.saveAll(getDepositEntity);
					
					
					
					

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				List<DepositEntity> depositEntities = depositRepository.findAllByProposalNumber(proposalNo);
				if (depositEntities.isEmpty()) {
					return ApiResponseDto.notFound(null);
				} else {
					return ApiResponseDto.success(depositEntities.stream().map(DepositHelper::entityToDto)
							.collect(Collectors.toList()));
				}
		}
		else {
		
		List<DepositEntity> depositEntities = depositRepository.findAllByProposalNumber(proposalNo);
		if (depositEntities.isEmpty()) {
			return ApiResponseDto.notFound(null);
		} else {
			return ApiResponseDto.success(depositEntities.stream().map(DepositHelper::entityToDto)
					.collect(Collectors.toList()));
		}
		}
	}	

	@Override
	public ApiResponseDto<List<DepositAdjustementDto>> GetDepositAdjustment(Long id) {
		List<DepositAdjustementDto> addDepositAdjustementDto=new ArrayList<DepositAdjustementDto>();
		
		
		List<Double> getSumAmountCharged=quotationChargeRepository.findSumAmountCharged(id);
		for(Double getAmountCharged :getSumAmountCharged ) {
			DepositAdjustementDto depositAdjustementDto=new DepositAdjustementDto();
			depositAdjustementDto.setMasterQuotationId(id);
		depositAdjustementDto.setAvailableAmount(getAmountCharged);
		depositAdjustementDto.setDepositAmount(getAmountCharged);
		depositAdjustementDto.setChequeRealisationDate(new Date());
//		depositAdjustementDto.setCollectionDate(new Date());
		depositAdjustementDto.setCollectionStatus("A");
		depositAdjustementDto.setRemarks("simulation");
		depositAdjustementDto.setTransactionMode("N");
		depositAdjustementDto.setUsername("");
		Random r = new Random( System.currentTimeMillis() );
		    int getCollectionNumber= ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));	   
		depositAdjustementDto.setCollectionNumber((long) getCollectionNumber);
		addDepositAdjustementDto.add(depositAdjustementDto);
		}
		return ApiResponseDto.success(addDepositAdjustementDto);
	}
	

	


    @Override
	public ApiResponseDto<List<DepositDto>> findAllByPolicyNumber(String policyNumber ) {
    	

		ArrayList<DepositDto> getDepositDto = new ArrayList<>();
		
		if(isDevEnvironment == false) {
			try {
				
				URL url = new URL(commonModuleServiceEndpoint + "accountsgratuityservice/ePGS/Accounts/Gratuity/ShowDepositr?policyNo=" );
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");

				if (conn.getResponseCode() != 200) {
					
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				
				String output = br.readLine();
				
				conn.disconnect();

				ObjectMapper mapper = new ObjectMapper();
				
				JsonFactory factory = mapper.getFactory();
				
				JsonParser parser = factory.createParser(output);
				
				JsonNode actualObj = mapper.readTree(parser);
				
				System.out.println(actualObj);
				
				output = "[{raj : 10},{push :10}]";
				
				
				 ObjectMapper objectMapper = new ObjectMapper();
				 
				 JsonNode node = objectMapper.readValue(output, JsonNode.class);		 
				 System.out.println(node.asText());			
				for (JsonNode jsonNode : actualObj) {	
					
					
					 DepositDto depositDto = new DepositDto();
					
//				depositDto.setAvailableAmount(jsonNode.asDouble());
//				depositDto.setChequeRealisationDate(new Date());			
//				depositDto.setCollectionDate(new Date());
//				depositDto.setCollectionNumber(jsonNode.asText());	
				depositDto.setCollectionStatus(jsonNode.asText());
				depositDto.setDepositAmount(jsonNode.asDouble());	
				depositDto.setId(jsonNode.asLong());
				depositDto.setProposalNumber(jsonNode.asText());
				depositDto.setRemarks(jsonNode.asText());
				depositDto.setTransactionMode(jsonNode.asText());
				
				getDepositDto.add(depositDto);
					
				}
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return ApiResponseDto.success(getDepositDto);
		}
		else {
		
		List<DepositEntity> depositEntities = depositRepository.findAllByPolicyNumber(policyNumber);
		if (depositEntities.isEmpty()) {
			return ApiResponseDto.notFound(null);
		} else {
			return ApiResponseDto.success(depositEntities.stream().map(DepositHelper::entityToDto)
					.collect(Collectors.toList()));
		}
					}
    }

	@Override
	public ApiResponseDto<List<DepositAdjustementDto>> AdjustmentBasedTmppolicy(Long tmpPolicyId) {
		List<DepositAdjustementDto> addDepositAdjustementDto=new ArrayList<DepositAdjustementDto>();
		
		
		List<Double> getSumAmountCharged=quotationChargeRepository.findSumAmountChargedBasedTmpPolicy(tmpPolicyId);
		for(Double getAmountCharged :getSumAmountCharged ) {
			DepositAdjustementDto depositAdjustementDto=new DepositAdjustementDto();
			depositAdjustementDto.setTmpPolicyId(tmpPolicyId);
		depositAdjustementDto.setAvailableAmount(getAmountCharged);
		depositAdjustementDto.setDepositAmount(getAmountCharged);
		depositAdjustementDto.setChequeRealisationDate(new Date());
//		depositAdjustementDto.setCollectionDate(new Date());
		depositAdjustementDto.setCollectionStatus("A");
		depositAdjustementDto.setRemarks("simulation");
		depositAdjustementDto.setTransactionMode("N");
		depositAdjustementDto.setUsername("");
		Random r = new Random( System.currentTimeMillis() );
		    int getCollectionNumber= ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));	   
		depositAdjustementDto.setCollectionNumber((long) getCollectionNumber);
		addDepositAdjustementDto.add(depositAdjustementDto);
		}
		return ApiResponseDto.success(addDepositAdjustementDto);
	}
}
