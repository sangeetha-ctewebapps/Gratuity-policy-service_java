package com.lic.epgs.gratuity.policy.integration.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.gratuity.common.entity.GratutityIcodesEntity;
import com.lic.epgs.gratuity.common.repository.GratutityIcodesRepository;
import com.lic.epgs.gratuity.mph.entity.MPHAddressEntity;
import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.repository.MPHRepository;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.integration.constants.AccountingIntegrationConstants;
import com.lic.epgs.gratuity.policy.integration.dto.AccountingIntegrationRequestDto;
import com.lic.epgs.gratuity.policy.integration.dto.ResponseDto;
import com.lic.epgs.gratuity.policy.integration.dto.SuperAnnuationResponseModel;
import com.lic.epgs.gratuity.policy.integration.service.AccountingIntegrationService;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policy.repository.custom.MasterPolicyCustomRepository;
import com.lic.epgs.gratuity.quotation.entity.QuotationEntity;
import com.lic.epgs.gratuity.quotation.repository.QuotationRepository;



@Service
public class AccountingIntegrationServiceImpl implements AccountingIntegrationService {
	
	
	@Autowired
	@Qualifier(value = "restTemplateService")
	private RestTemplate restTemplateService;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	MasterPolicyRepository policyMasterRepository;
	
	@Autowired
	MPHRepository mphMasterRepository;
	
	@Autowired
	GratutityIcodesRepository gratutityIcodesRepository;

	@Autowired
	private MasterPolicyCustomRepository masterPolicyCustomRepository;


	
	private final Logger logger = LogManager.getLogger(AccountingIntegrationServiceImpl.class);

	@Value("${app.commonModuleServiceEndpoint}")
	private String commonModuleServiceEndpoint;

	@Autowired
	private QuotationRepository quotationRepository;
	
	public SuperAnnuationResponseModel getMphAndIcodeDetails(AccountingIntegrationRequestDto accountingIntegrationRequestDto) {
		logger.info("ProposalMakerServiceImpl------getMphAndIcodeDetail-------Started");
		SuperAnnuationResponseModel superAnnuationResponseModel = new SuperAnnuationResponseModel();
		try {
			JsonNode mphBasic = null;
			JsonNode mphAdds = null;
			JsonNode mphRep = null;
			JsonNode mphProduct = null;
			JsonNode mphProductVarient = null;
			QuotationEntity quotationEntity=quotationRepository.findByProposalNumberisActive(accountingIntegrationRequestDto.getProposalNumber());
//			JsonNode mphRep=null;
//			ProposalAnnuityDto proposalAnnuityDto = proposalMakerServiceImpl.getProposalDetailsForIntegration(accountingIntegrationRequestDto.getProposalNumber());
			try {
				URL url = new URL(commonModuleServiceEndpoint
						+ "/api/proposal/getProposalDetailsByProposalNumber?proposalNumber=" + accountingIntegrationRequestDto.getProposalNumber());
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
				
				
				mphBasic = actualObj.path("responseData").path("mphDetails").path("mphBasicDetails");
				mphAdds = actualObj.path("responseData").path("mphDetails").path("mphAddressDetails");
//				mphRep = actualObj.path("responseData").path("mphDetails").path("mphChannelDetails");
				 mphRep = actualObj.path("responseData").path("mphDetails").path("mphContactDetails");
				 
				 mphProduct=actualObj.path("responseData").path("mphDetails").path("productDetails");
				 mphProductVarient=actualObj.path("responseData").path("mphDetails").path("productVariantDetails");
				 
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(mphBasic != null) {
				superAnnuationResponseModel.setSchemeName(AccountingIntegrationConstants.SCHEME_NAME);
				superAnnuationResponseModel.setMphCode(mphBasic.path("mphCode").textValue());
				superAnnuationResponseModel.setMphName(mphBasic.path("mphName").textValue());
				superAnnuationResponseModel.setMphNo(accountingIntegrationRequestDto.getProposalNumber());
			JsonNode mPHContactPersonDetailsDtoList = mphRep;
            if(!mPHContactPersonDetailsDtoList.isEmpty()) {
            	for (int i = 0; i < mPHContactPersonDetailsDtoList.size(); i++) {
//			for(MPHContactPersonDetailsDto mPHContactPersonDetailsDto : mPHContactPersonDetailsDtoList) {
				superAnnuationResponseModel.setMphMobileNo(mphRep.get(i).path("mobileNumber").asLong());
				superAnnuationResponseModel.setMphEmail(mphRep.get(i).path("emailID").textValue());
            }
            }
            JsonNode mPHAddressDetailsDtoList =mphAdds;
			if(!mPHAddressDetailsDtoList.isEmpty()) {
//            for(MPHAddressDetailsDto mPHAddressDetailsDto : mPHAddressDetailsDtoList) {
				for (int i = 0; i < mPHAddressDetailsDtoList.size(); i++) {
            	superAnnuationResponseModel.setMphAddress1(mphAdds.get(i).path("address1").textValue());
            	superAnnuationResponseModel.setMphAddress2(mphAdds.get(i).path("address2").textValue());
            	superAnnuationResponseModel.setDistrict(mphAdds.get(i).path("district").textValue());
            	superAnnuationResponseModel.setStateName(mphAdds.get(i).path("state").textValue());
            	superAnnuationResponseModel.setPinCode((Long) mphAdds.get(i).path("pinCode").numberValue());
			}
			}
			
			ResponseDto responseDto=commonmasterserviceUnitByCode(quotationEntity.getUnitCode());
			if(responseDto != null) {
				superAnnuationResponseModel.setServicingUnitName(responseDto.getDescription());
				superAnnuationResponseModel.setServicingUnitAddress1(responseDto.getAddress1());
				superAnnuationResponseModel.setServicingUnitAddress2(responseDto.getAddress2());
				superAnnuationResponseModel.setServicingUnitAddress3(responseDto.getAddress3());
				superAnnuationResponseModel.setServicingUnitAddress4(responseDto.getAddress4());
				superAnnuationResponseModel.setServicingUnitCity(responseDto.getCityName());
				superAnnuationResponseModel.setServicingUnitPincode(responseDto.getPincode());
				superAnnuationResponseModel.setServicingUnitEmail(responseDto.getEmailId());
				superAnnuationResponseModel.setServicingUnitPhoneNo(responseDto.getTelephoneNo());
				superAnnuationResponseModel.setOperatingUnitType(responseDto.getDescription());
				superAnnuationResponseModel.setUnitCode(responseDto.getUnitCode());
			}
			GratutityIcodesEntity gratutityIcodesEntity = gratutityIcodesRepository.findByProductIdVariantId(mphProduct.path("leadProductId").longValue(), mphProductVarient.path("leadVariantId").longValue());
		
			superAnnuationResponseModel.setICodeForBusinessSegment(gratutityIcodesEntity.getIcodeBusinessSegment().intValue());
			superAnnuationResponseModel.setICodeForInvestmentPortfolio(1);
			superAnnuationResponseModel.setICodeForLob(gratutityIcodesEntity.getIcodeBusinessLine().intValue());
			superAnnuationResponseModel.setICodeForBusinessType(gratutityIcodesEntity.getIcodeBuinessType().intValue());
			superAnnuationResponseModel.setICodeForParticipatingType(gratutityIcodesEntity.getIcodeParticipatingType().intValue());
			superAnnuationResponseModel.setICodeForProductLine(gratutityIcodesEntity.getIcodeProductLine().intValue());
			superAnnuationResponseModel.setICodeForVariant(gratutityIcodesEntity.getIcodeVarient().toString());

			}
		}
		catch (Exception ue) {
			logger.error( "getMphAndIcodeDetail", ue);
		}
		logger.info("ProposalMakerServiceImpl ------ getMphAndIcodeDetail----------- Ended");
		return superAnnuationResponseModel;
	}
//	
//	public ProposalAnnuityDto getDetails(String proposalNumber) {
//		ProposalAnnuityDto proposalAnnuityDto = null;
//		try {
//			String url = environment.getProperty("PROPOSAL_DETAILSBY_PROPOSALNUMBER");
//		 
//			if (StringUtils.isNotBlank(url)) {
//				proposalAnnuityDto = restTemplateService.exchange(url + proposalNumber, HttpMethod.GET, null, ProposalAnnuityDto.class)
//						.getBody();
//				if (proposalAnnuityDto == null) {
//					proposalAnnuityDto = new ProposalAnnuityDto();
//				}
//				
//			}
//			
//		} catch (HttpStatusCodeException e) {
//			logger.info("CommonServiceImpl-getDetails-Error:");
//		}
//		logger.info("CommonServiceImpl-getDetails-End");
//		 
//		 return proposalAnnuityDto;
//	}
//	
	
	
	
	
	
	
		public ResponseDto commonmasterserviceUnitByCode(String unitCode) {
			ResponseDto responseDto=null;
			logger.info("CommonServiceImpl-commonmasterserviceUnitByCode-Start");

			try {
				String url = environment.getProperty("COMMON_MASTER_UNITBY_CODE");

				if (StringUtils.isNotBlank(url)) {

					responseDto = restTemplateService.exchange(url + unitCode, HttpMethod.GET, null, ResponseDto.class)
							.getBody();
					if (responseDto == null) {
						responseDto = new ResponseDto();
					}
				}
			} catch (HttpStatusCodeException e) {

				logger.info("CommonServiceImpl-commonmasterserviceUnitByCode-Error:");
			}
			logger.info("CommonServiceImpl-commonmasterserviceUnitByCode-End");

			return responseDto;
		}


@Override
public SuperAnnuationResponseModel getMphAndIcodeDetail(
		AccountingIntegrationRequestDto accountingIntegrationRequestDto) {
	logger.info("ProposalMakerServiceImpl------getMphAndIcodeDetail-------Started");
	SuperAnnuationResponseModel superAnnuationResponseModel = new SuperAnnuationResponseModel();
	 try {
		 
		 MasterPolicyEntity object = masterPolicyCustomRepository.findPolicyDetail(accountingIntegrationRequestDto.getPolicyNumber());
		 MPHEntity getMphEntity =mphMasterRepository.findById(object.getMasterpolicyHolder()).get();
		 
		 if(getMphEntity != null) {
		 
		
		 
			 superAnnuationResponseModel.setSchemeName(AccountingIntegrationConstants.SCHEME_NAME);
			 superAnnuationResponseModel.setMphCode(getMphEntity.getMphCode());
			 superAnnuationResponseModel.setMphName(getMphEntity.getMphName());
			 superAnnuationResponseModel.setMphNo(object.getPolicyNumber());
	
			 if(getMphEntity.getMphAddresses().size() >0) {
				 
				 for(MPHAddressEntity mphAddressEntity :getMphEntity.getMphAddresses()) {
					 if(mphAddressEntity.getAddressLine1()!=null || mphAddressEntity.getAddressLine2() != null ||
							 mphAddressEntity.getDistrict()!=null ||mphAddressEntity.getStateId() != null ) {
				    superAnnuationResponseModel.setMphAddress1(mphAddressEntity.getAddressLine1());
	            	superAnnuationResponseModel.setMphAddress2(mphAddressEntity.getAddressLine2());
	            	superAnnuationResponseModel.setDistrict(mphAddressEntity.getDistrict());
	            	superAnnuationResponseModel.setState(mphAddressEntity.getStateId());
	            	superAnnuationResponseModel.setPinCode(mphAddressEntity.getPincode());
	            	break;
					 }
				 }
			 }
			 
			 ResponseDto responseDto=commonmasterserviceUnitByCode(object.getUnitCode());
				if(responseDto != null) {
					superAnnuationResponseModel.setServicingUnitName(responseDto.getDescription());
					superAnnuationResponseModel.setServicingUnitAddress1(responseDto.getAddress1());
					superAnnuationResponseModel.setServicingUnitAddress2(responseDto.getAddress2());
					superAnnuationResponseModel.setServicingUnitAddress3(responseDto.getAddress3());
					superAnnuationResponseModel.setServicingUnitAddress4(responseDto.getAddress4());
					superAnnuationResponseModel.setServicingUnitCity(responseDto.getCityName());
					superAnnuationResponseModel.setServicingUnitPincode(responseDto.getPincode());
					superAnnuationResponseModel.setServicingUnitEmail(responseDto.getEmailId());
					superAnnuationResponseModel.setServicingUnitPhoneNo(responseDto.getTelephoneNo());
					superAnnuationResponseModel.setOperatingUnitType(responseDto.getDescription());
					superAnnuationResponseModel.setUnitCode(responseDto.getUnitCode());
				}
				GratutityIcodesEntity gratutityIcodesEntity = gratutityIcodesRepository.findByProductIdVariantId(object.getProductId(),object.getProductVariantId());
				
				superAnnuationResponseModel.setICodeForBusinessSegment(gratutityIcodesEntity.getIcodeBusinessSegment().intValue());
				superAnnuationResponseModel.setICodeForInvestmentPortfolio(1);
				superAnnuationResponseModel.setICodeForLob(gratutityIcodesEntity.getIcodeBusinessLine().intValue());
				superAnnuationResponseModel.setICodeForBusinessType(gratutityIcodesEntity.getIcodeBuinessType().intValue());
				superAnnuationResponseModel.setICodeForParticipatingType(gratutityIcodesEntity.getIcodeParticipatingType().intValue());
				superAnnuationResponseModel.setICodeForProductLine(gratutityIcodesEntity.getIcodeProductLine().intValue());
				superAnnuationResponseModel.setICodeForVariant(gratutityIcodesEntity.getIcodeVarient().toString());

		 }
		 
		 
	 }
	 catch (Exception ue) {
			logger.error( "getMphAndIcodeDetail", ue);
		}
		logger.info("ProposalMakerServiceImpl ------ getMphAndIcodeDetail----------- Ended");
	return superAnnuationResponseModel;
}
		


}
