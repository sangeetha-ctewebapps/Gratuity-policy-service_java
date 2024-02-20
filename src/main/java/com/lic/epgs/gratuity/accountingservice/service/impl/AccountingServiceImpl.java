package com.lic.epgs.gratuity.accountingservice.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.lic.epgs.gratuity.accountingservice.dto.AcctClaimResponseDto;
import com.lic.epgs.gratuity.accountingservice.dto.AcctCrDrResDto;
import com.lic.epgs.gratuity.accountingservice.dto.ClaimLiabilityBookingReqDto;
import com.lic.epgs.gratuity.accountingservice.dto.ClaimReverseLiabilityReqDto;
import com.lic.epgs.gratuity.accountingservice.dto.ContraDebitCreditResponse;
import com.lic.epgs.gratuity.accountingservice.dto.DebitCreditResponse;
import com.lic.epgs.gratuity.accountingservice.dto.GlTransactionModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.HSNCodeDto;
import com.lic.epgs.gratuity.accountingservice.dto.IssuePolicyDto;
import com.lic.epgs.gratuity.accountingservice.dto.JournalVoucherDetailModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.MidLeaverContributuionPremuimAndGstDto;
import com.lic.epgs.gratuity.accountingservice.dto.NewBusinessContributionAndLifeCoverAdjustmentDto;
import com.lic.epgs.gratuity.accountingservice.dto.PolicySurrenderAccountingResponse;
import com.lic.epgs.gratuity.accountingservice.dto.RenewalContributionAdjustRequestModelDto;
import com.lic.epgs.gratuity.accountingservice.dto.UnlockDepositDetailDto;
import com.lic.epgs.gratuity.accountingservice.entity.AcctClaimResponseEntity;
import com.lic.epgs.gratuity.accountingservice.entity.AcctCrDrEntity;
import com.lic.epgs.gratuity.accountingservice.entity.AcctDepositResponseEntity;
import com.lic.epgs.gratuity.accountingservice.entity.AcctResponseEntity;
import com.lic.epgs.gratuity.accountingservice.repository.AccountingApiHistoryRepository;
import com.lic.epgs.gratuity.accountingservice.repository.AcctClaimResponseRepository;
import com.lic.epgs.gratuity.accountingservice.repository.AcctDepositResponseRepository;
import com.lic.epgs.gratuity.accountingservice.repository.AcctResponseRepository;
import com.lic.epgs.gratuity.accountingservice.service.AccountingService;
import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.common.entity.GratutityIcodesEntity;
import com.lic.epgs.gratuity.common.repository.CommonMasterStateRepository;
import com.lic.epgs.gratuity.common.repository.CommonMasterUnitRepository;
import com.lic.epgs.gratuity.common.repository.GratutityIcodesRepository;
import com.lic.epgs.gratuity.common.service.CommonModuleService;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.policy.dto.ShowDepositDto;
import com.lic.epgs.gratuity.policy.dto.ShowDepositLockDto;
import com.lic.epgs.gratuity.policy.entity.AccountingApiHistoryEntity;
import com.lic.epgs.gratuity.policy.integration.dto.ResponseDto;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.surrender.dto.SurrenderAccountingIntegrationRequestModel;
import com.lic.epgs.gratuity.policyservices.merger.dto.GlTransactionModel;
import com.lic.epgs.gratuity.policyservices.merger.dto.MergePolicyRequestModel;
import com.lic.epgs.gratuity.simulation.dto.DepositDto;

@Service
public class AccountingServiceImpl implements AccountingService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private AccountingApiHistoryRepository accountingApiHistoryRepository;

	@Autowired
	private AcctResponseRepository acctResponseRepository;

	@Autowired
	private AcctDepositResponseRepository acctDepositResponseRepository;

	@Autowired
	private AcctClaimResponseRepository acctClaimResponseRepository;

	@Autowired
	private GratutityIcodesRepository gratutityIcodesRepository;

	@Autowired
	private CommonModuleService commonModuleService;
	
	@Autowired
	private PolicyMemberRepository policyMemberRepository;

	@Value("${app.accountingServiceEndpoint}")
	private String accountingServiceEndpoint;

	@Value("${app.commanmasterunitcode}")
	private String commanmasterunitcode;

	@Autowired
	private CommonMasterUnitRepository commonMasterUnitRepository;

	@Autowired
	private CommonMasterStateRepository commonMasterStateRepository;

	@Override
	public ApiResponseDto<List<DepositDto>> getDeposits(String proposalNo, String policyNo, String username) {
		ShowDepositDto showDepositDto = new ShowDepositDto();
		showDepositDto.setProposalNo(proposalNo);
		showDepositDto.setPolicyNo(policyNo);
		showDepositDto.setUserName(username);
		try {
			URL url = new URL(
					accountingServiceEndpoint + "/accountsgratuityservice/ePGS/Accounts/Gratuity/ShowDeposit");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(showDepositDto);

			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = json.getBytes("utf-8");
				os.write(input);
			}

			if (conn.getResponseCode() != 200) {
				logger.error("HTTP Status Error: [getDeposits] Expected 200, got " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();
			AccountingApiHistoryEntity accountingApiHistoryEntity = saveMessage(showDepositDto.toString(), output,
					username);

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);

			ArrayList<DepositDto> getDepositDto = new ArrayList<>();
			for (JsonNode jsonNode : actualObj) {
				DepositDto depositDto = new DepositDto();
				depositDto.setCollectionNumber(jsonNode.get("collectionNo").asLong());
				depositDto.setCollectionType(jsonNode.get("collectionType").asInt());
				depositDto.setCollectionMode(jsonNode.get("collectionMode").textValue());
				depositDto.setCollectionAmount(jsonNode.get("collectionAmount").asDouble());
				depositDto.setCollectionStatus(jsonNode.get("collectionStatus").textValue());
				depositDto.setUtilizedAmount(jsonNode.get("utilizedAmount").asDouble());
				depositDto.setBalanceAmount(jsonNode.get("balanceAmount").asDouble());
				depositDto.setDepositAmount(depositDto.getBalanceAmount());

				String collectionDate = jsonNode.get("collectionDate").toString().replace("\"", "");

				Date collectiondAte = DateUtils.convertStringToDate1(collectionDate);

				String vocEffDate = jsonNode.get("voucherEffectiveDate").toString().replace("\"", "");
				Date vocEffDateformate = DateUtils.convertStringToDate1(vocEffDate);
				if (!jsonNode.get("adjustmentDate").toString().equals("null")) {

					String adjDate = jsonNode.get("adjustmentDate").toString().replace("\"", "");
					Date adjDateformate = DateUtils.convertStringToDate1(adjDate);
					depositDto.setAdjustmentDate(adjDateformate);
				}
				depositDto.setCollectionDate(collectiondAte);
				depositDto.setVoucherEffectiveDate(vocEffDateformate);

				depositDto.setLockStatus(jsonNode.get("lockStatus").toString());
				depositDto.setAdjustmentAvailability(jsonNode.get("adjustmentAvailability").toString());
				depositDto.setAdjustmentNo(jsonNode.get("adjustmentNo").asLong());
				depositDto.setAdjuestmentAmount(jsonNode.get("adjustmentAmount").asDouble());
				depositDto.setAdjustmentStatus(jsonNode.get("adjustmentStatus").toString());
				depositDto.setAccountAPIID(accountingApiHistoryEntity.getId().toString());
				getDepositDto.add(depositDto);
			}
			return ApiResponseDto.success(getDepositDto);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	// Lock API
	public ResponseDto lockDeposits(List<ShowDepositLockDto> showDepositLockDto, String username) {
		ResponseDto responseDto = new ResponseDto();
		try {
			URL url = new URL(
					accountingServiceEndpoint + "/accountsgratuityservice/ePGS/Accounts/Gratuity/LockMultipleDeposit");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(showDepositLockDto);

			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = json.getBytes("utf-8");
				os.write(input);
			}

			if (conn.getResponseCode() != 202 && conn.getResponseCode() != 200) {
				logger.error("HTTP Status Error: [lockDeposits] Expected 200, got " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();

			conn.disconnect();
			saveMessage(showDepositLockDto.toString(), output, username);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return responseDto;

	}

	// Unlock API for Reject
	@Override
	public ResponseDto unlockDeposits(List<UnlockDepositDetailDto> showDepositLockDto, String username) {
		ResponseDto responseDto = new ResponseDto();
		try {

			URL url = new URL(accountingServiceEndpoint
					+ "/accountsgratuityservice/ePGS/Accounts/Gratuity/RejectMultipleAdjustment");
			logger.info("Unlock Deposit Url : " + url);
			String unlockPayloadJson = new ObjectMapper().writeValueAsString(showDepositLockDto);
			logger.info("Unlock Deposit Payload: " + unlockPayloadJson);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(showDepositLockDto);

			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = json.getBytes("utf-8");
				os.write(input);
			}

			if (conn.getResponseCode() != 202 && conn.getResponseCode() != 200) {
				logger.info("HTTP Status Error: [unlockDeposits] Expected 200, got " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();

			conn.disconnect();
			logger.info("Unlock Deposit Input : " + showDepositLockDto.toString());
			saveMessage(showDepositLockDto.toString(), output, username);
			logger.info("Unlock Deposit Response : " + output);
			responseDto.setStatus("TRUE");
		} catch (MalformedURLException e) {
			logger.info(e.getMessage());
			responseDto.setStatus("FALSE");
		} catch (IOException e) {
			logger.info(e.getMessage());
			responseDto.setStatus("FALSE");
		}

		return responseDto;
	}

	@Override
	public HSNCodeDto getHSNCode() {
		try {
			URL url = new URL(accountingServiceEndpoint
					+ "/accountingcoreservice/ePGS/Accounts/HsnCode/HSNDetail?hsnCode=997132");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				logger.error("HTTP Status Error: [getHSNCode] Expected 200, got " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);

			HSNCodeDto hSNCodeDto = new HSNCodeDto();
			hSNCodeDto.setCgstRate(actualObj.path("cgstRate").longValue());
			hSNCodeDto.setHsnCode(actualObj.path("hsnCode").textValue());
			hSNCodeDto.setIgstRate(actualObj.path("igstRate").longValue());
			hSNCodeDto.setUtgstRate(actualObj.path("utgstRate").longValue());
			hSNCodeDto.setSgstRate(actualObj.path("sgstRate").longValue());
			hSNCodeDto.setDescription(actualObj.path("description").toString());

			return hSNCodeDto;

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public Boolean consumeDeposits(NewBusinessContributionAndLifeCoverAdjustmentDto getDetailsDto,Long masterId) {

		String message = null;

		try {
			URL url = new URL(accountingServiceEndpoint
					+ "/accountsgratuityservice/ePGS/Accounts/Gratuity/NewBusinessContributionAndLifeCoverAdjustmentApprove");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(getDetailsDto);
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = json.getBytes("utf-8");
				os.write(input);
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);

			saveMessage(json, output, "SYSTEM");
			saveDepositAdjustmentResponse(output, "PREMIUM-ADJUST", Long.parseLong(getDetailsDto.getRefNo()),
					"PSTG-CONT-DET-ID",masterId);

			message = "Success";

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
			message = "FAILED";
		} catch (IOException e) {
			logger.error(e.getMessage());
			message = "FAILED";
		}

		if (message.equals("FAILED")) {
			return false;
		} else
			return true;

	}

	public void policyIssuance(IssuePolicyDto issuePolicyDto) {

		try {
			URL url = new URL(
					accountingServiceEndpoint + "/accountsgratuityservice/ePGS/Accounts/Gratuity/IssuePolicy");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(issuePolicyDto);

			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = json.getBytes("utf-8");
				os.write(input);
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();

			conn.disconnect();
			saveMessage(json.toString(), output, "SYSTEM");
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public Map<String, Double> getGstComponents(String unitStateType, String mphStateType, HSNCodeDto hSNCodeDto,
			Double adjustment) {
		Map<String, Double> gstRates = new HashMap<>();

		if (unitStateType.equals(mphStateType)) {
			if (unitStateType.equals("S") && mphStateType.equals("S")) {
				gstRates.put("CGST", (hSNCodeDto.getCgstRate() / 100) * adjustment);
				gstRates.put("SGST", (hSNCodeDto.getSgstRate() / 100) * adjustment);
				gstRates.put("UTGST", 0.0);
				gstRates.put("IGST", 0.0);
			}
			if (unitStateType.equalsIgnoreCase("UT") && mphStateType.equalsIgnoreCase("UT")) {
				gstRates.put("CGST", (hSNCodeDto.getCgstRate() / 100) * adjustment);
				gstRates.put("UTGST", (hSNCodeDto.getUtgstRate() / 100) * adjustment);
				gstRates.put("SGST", 0.0);
				gstRates.put("IGST", 0.0);
			}
		} else {
			gstRates.put("IGST", (hSNCodeDto.getIgstRate() / 100) * adjustment);
			gstRates.put("SGST", 0.0);
			gstRates.put("CGST", 0.0);
			gstRates.put("UTGST", 0.0);
		}

		return gstRates;
	}

	@Override
	public AcctClaimResponseDto intimateDeathClaim(ClaimLiabilityBookingReqDto claimLiabilityBookingReqDto,
			String username, String module, Long referenceId, String referenceValue) {
		try {
			URL url = new URL(accountingServiceEndpoint
					+ "/accountsgratuityservice/ePGS/Accounts/Gratuity/deathClaimLiabilityBookingForCourtCase");
			return intimateClaimLiability(url, claimLiabilityBookingReqDto, username, module, referenceId,
					referenceValue);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public AcctClaimResponseDto intimateDeathClaimforNonCourt(ClaimLiabilityBookingReqDto claimLiabilityBookingReqDto,
			String username, String module, Long referenceId, String referenceValue) {
		try {
			URL url = new URL(accountingServiceEndpoint
					+ "/accountsgratuityservice/ePGS/Accounts/Gratuity/deathClaimLiabilityBookingForNonCourtCase");
			return intimateClaimLiability(url, claimLiabilityBookingReqDto, username, module, referenceId,
					referenceValue);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public AcctClaimResponseDto intimateMaturityClaim(ClaimLiabilityBookingReqDto claimLiabilityBookingReqDto,
			String username, String module, Long referenceId, String referenceValue) {
		try {
			URL url = new URL(accountingServiceEndpoint
					+ "/accountsgratuityservice/ePGS/Accounts/Gratuity/maturityOrRetirementClaimCourtCaseLiability");
			return intimateClaimLiability(url, claimLiabilityBookingReqDto, username, module, referenceId,
					referenceValue);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	// for maturity =maturityOrRetirementClaimNon_CourtCaseLiability
	@Override
	public AcctClaimResponseDto intimateMaturityClaimforNonCourt(
			ClaimLiabilityBookingReqDto claimLiabilityBookingReqDto, String username, String module, Long referenceId,
			String referenceValue) {
		try {
			URL url = new URL(accountingServiceEndpoint
					+ "/accountsgratuityservice/ePGS/Accounts/Gratuity/maturityOrRetirementClaimNonCourtCaseLiability");
			return intimateClaimLiability(url, claimLiabilityBookingReqDto, username, module, referenceId,
					referenceValue);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public AcctClaimResponseDto intimateWithdrawalClaim(ClaimLiabilityBookingReqDto claimLiabilityBookingReqDto,
			String username, String module, Long referenceId, String referenceValue) {
		try {
			URL url = new URL(accountingServiceEndpoint
					+ "/accountsgratuityservice/ePGS/Accounts/Gratuity/withdrawalClaimLiabilityBookingForCourtCase");
			return intimateClaimLiability(url, claimLiabilityBookingReqDto, username, module, referenceId,
					referenceValue);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public AcctClaimResponseDto intimateWithdrawalClaimforNonClaim(
			ClaimLiabilityBookingReqDto claimLiabilityBookingReqDto, String username, String module, Long referenceId,
			String referenceValue) {
		try {
			URL url = new URL(accountingServiceEndpoint
					+ "/accountsgratuityservice/ePGS/Accounts/Gratuity/withdrawalClaimLiabilityBookingForNonCourtCase");
			return intimateClaimLiability(url, claimLiabilityBookingReqDto, username, module, referenceId,
					referenceValue);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	// Reversal for Non and Court case Death
	@Override
	public AcctClaimResponseDto reverseDeathClaimIntimation(ClaimReverseLiabilityReqDto claimReverseLiabilityReqDto,
			String username, String module, Long referenceId, String referenceValue) {
		try {
			URL url = new URL(accountingServiceEndpoint
					+ "/accountsgratuityservice/ePGS/Accounts/Gratuity/deathClaimLiabilityReversalForCourtCase");
			return intimateClaimReversalLiability(url, claimReverseLiabilityReqDto, username, module, referenceId,
					referenceValue);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public AcctClaimResponseDto reverseDeathClaimIntimationNonCourtCase(
			ClaimReverseLiabilityReqDto claimReverseLiabilityReqDto, String username, String module, Long referenceId,
			String referenceValue) {
		try {
			URL url = new URL(accountingServiceEndpoint
					+ "/accountsgratuityservice/ePGS/Accounts/Gratuity/deathClaimLiabilityReversalForNonCourtCase");
			return intimateClaimReversalLiability(url, claimReverseLiabilityReqDto, username, module, referenceId,
					referenceValue);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	// reversal Death ENd Vignesh

	@Override
	public AcctClaimResponseDto reverseMaturityClaimIntimation(ClaimReverseLiabilityReqDto claimReverseLiabilityReqDto,
			String username, String module, Long referenceId, String referenceValue) {
		try {
			URL url = new URL(accountingServiceEndpoint
					+ "/accountsgratuityservice/ePGS/Accounts/Gratuity/maturityOrRetirementReverseClaimCourtCaseLiability");
			return intimateClaimReversalLiability(url, claimReverseLiabilityReqDto, username, module, referenceId,
					referenceValue);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	// fOR Reversal NON Court Claim IN Maturity
	@Override
	public AcctClaimResponseDto reverseMaturityClaimIntimationforNonCourt(
			ClaimReverseLiabilityReqDto claimReverseLiabilityReqDto, String username, String module, Long referenceId,
			String referenceValue) {
		try {
			URL url = new URL(accountingServiceEndpoint
					+ "/accountsgratuityservice/ePGS/Accounts/Gratuity/maturityOrRetirementReverseClaimNonCourtCaseLiability");
			return intimateClaimReversalLiability(url, claimReverseLiabilityReqDto, username, module, referenceId,
					referenceValue);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public AcctClaimResponseDto reverseWithdrawalClaimIntimation(
			ClaimReverseLiabilityReqDto claimReverseLiabilityReqDto, String username, String module, Long referenceId,
			String referenceValue) {
		try {
			URL url = new URL(accountingServiceEndpoint
					+ "/accountsgratuityservice/ePGS/Accounts/Gratuity/withdrawalClaimLiabilityReversalForCourtCase");
			return intimateClaimReversalLiability(url, claimReverseLiabilityReqDto, username, module, referenceId,
					referenceValue);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		}
		return null;

	}

	@Override
	public AcctClaimResponseDto reverseWithdrawalClaimIntimationforNonCourt(
			ClaimReverseLiabilityReqDto claimReverseLiabilityReqDto, String username, String module, Long referenceId,
			String referenceValue) {
		try {
			URL url = new URL(accountingServiceEndpoint
					+ "/accountsgratuityservice/ePGS/Accounts/Gratuity/withdrawalClaimLiabilityReversalForNonCourtCase");
			return intimateClaimReversalLiability(url, claimReverseLiabilityReqDto, username, module, referenceId,
					referenceValue);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private AcctClaimResponseDto intimateClaimLiability(URL url,
			ClaimLiabilityBookingReqDto claimLiabilityBookingReqDto, String username, String module, Long referenceId,
			String referenceValue) {
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(claimLiabilityBookingReqDto);
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = json.getBytes("utf-8");
				os.write(input);
			}

			if (conn.getResponseCode() != 200 && conn.getResponseCode() != 202) {
				logger.error("HTTP Status Error: [intimateClaimLiability] Expected 200, got " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();

			conn.disconnect();
			saveMessage(claimLiabilityBookingReqDto.toString(), output, username);
			saveClaimResponse(output, module, referenceId, referenceValue);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private AcctClaimResponseDto intimateClaimReversalLiability(URL url,
			ClaimReverseLiabilityReqDto claimReverseLiabilityReqDto, String username, String module, Long referenceId,
			String referenceValue) {
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(claimReverseLiabilityReqDto);
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = json.getBytes("utf-8");
				os.write(input);
			}

			if (conn.getResponseCode() != 200 && conn.getResponseCode() != 202) {
				logger.error("HTTP Status Error: [intimateClaimReversalLiability] Expected 200, got "
						+ conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();

			conn.disconnect();
			saveMessage(claimReverseLiabilityReqDto.toString(), output, username);
			saveClaimResponse(output, module, referenceId, referenceValue);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private void saveDepositAdjustmentResponse(String output, String module, Long referenceId, String referenceValue,Long masterId) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);

			AcctResponseEntity acctResponseEntity = new AcctResponseEntity();
			acctResponseEntity.setModule(module);
			acctResponseEntity.setReferenceId(referenceId);
			acctResponseEntity.setReferenceValue(referenceValue);

			AcctDepositResponseEntity acctDepositResponseEntity = new AcctDepositResponseEntity();
			acctDepositResponseEntity.setAcctResponseEntity(acctResponseEntity);
			acctDepositResponseEntity.setAdjustmentNumber(Long.valueOf(actualObj.get("adjustmentNo").asInt()));
			acctDepositResponseEntity.setVoucherNumber(actualObj.get("voucherNo").asText());
			acctDepositResponseEntity.setVoucherAmount(actualObj.get("voucherAmount").doubleValue());
			if (!actualObj.get("voucherEffectiveDate").toString().equals("null")) {
				acctDepositResponseEntity.setVoucherEffectiveDate(
						DateUtils.convertStringYYYYMMDDTHHMMSSSSZ1(actualObj.get("voucherEffectiveDate").asText()));
			}
			;
			acctDepositResponseEntity.setGstInvoiceNumber(actualObj.get("gstInvoiceNumber").asText());
			policyMemberRepository.updateInvoiceNumberNo(masterId,acctDepositResponseEntity.getGstInvoiceNumber());
			
			
			Set<AcctCrDrEntity> acctCrDrEntities = new HashSet<>();
			JsonNode details = actualObj.get("debitCreditSummaryModels");
			for (JsonNode detail : details) {
				AcctCrDrEntity acctCrDrEntity = new AcctCrDrEntity();
				if (detail.get("debitCode") != null)
					acctCrDrEntity.setDebitCode(detail.get("debitCode").textValue());
				acctCrDrEntity.setDebitAmount(detail.get("debitAmount").doubleValue());
				if (detail.get("debitCodeDescription") != null)
					acctCrDrEntity.setDebitCodeDescription(detail.get("debitCodeDescription").textValue());
				acctCrDrEntity.setCreditCode(detail.get("creditCode").textValue());
				acctCrDrEntity.setCreditAmount(detail.get("creditAmount").doubleValue());
				acctCrDrEntity.setCreditCodeDescription(detail.get("creditCodeDescription").textValue());
				acctCrDrEntity.setAcctResponseEntity(acctResponseEntity);
				acctCrDrEntities.add(acctCrDrEntity);
			}
			acctResponseEntity.setAcctCrDrEntities(acctCrDrEntities);
			acctResponseEntity = acctResponseRepository.save(acctResponseEntity);
			acctDepositResponseEntity.setAcctResponseEntity(acctResponseEntity);
			acctDepositResponseRepository.save(acctDepositResponseEntity);

		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void saveClaimResponse(String output, String module, Long referenceId, String referenceValue) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);

			AcctResponseEntity acctResponseEntity = new AcctResponseEntity();
			acctResponseEntity.setModule(module);
			acctResponseEntity.setReferenceId(referenceId);
			acctResponseEntity.setReferenceValue(referenceValue);

			AcctClaimResponseEntity acctClaimResponseEntity = new AcctClaimResponseEntity();
			acctClaimResponseEntity.setDebitCode(actualObj.get("DebitICode").textValue());
			acctClaimResponseEntity.setCreditCode(actualObj.get("CreditICode").textValue());
			if (actualObj.get("VoucherAmount") == null) {
				acctClaimResponseEntity.setPayoutAmount(actualObj.get("PayoutAmount").doubleValue());
			} else {
				acctClaimResponseEntity.setPayoutAmount(actualObj.get("VoucherAmount").doubleValue());
			}
			acctClaimResponseEntity.setJournalNumber(actualObj.get("JournalNumber").textValue());

			Set<AcctCrDrEntity> acctCrDrEntities = new HashSet<>();
			JsonNode details = actualObj.get("ruleCodeDetails");
			for (JsonNode detail : details) {
				AcctCrDrEntity acctCrDrEntity = new AcctCrDrEntity();
				acctCrDrEntity.setDebitCode(detail.get("debitCode").textValue());
				acctCrDrEntity.setDebitAmount(detail.get("debitAmount").doubleValue());
				acctCrDrEntity.setDebitCodeDescription(detail.get("debitCodeDescription").textValue());
				acctCrDrEntity.setCreditCode(detail.get("creditCode").textValue());
				acctCrDrEntity.setCreditAmount(detail.get("creditAmount").doubleValue());
				acctCrDrEntity.setCreditCodeDescription(detail.get("creditCodeDescription").textValue());
				acctCrDrEntity.setAcctResponseEntity(acctResponseEntity);
				acctCrDrEntities.add(acctCrDrEntity);
			}

			acctResponseEntity.setAcctCrDrEntities(acctCrDrEntities);
			acctResponseEntity = acctResponseRepository.save(acctResponseEntity);

			acctClaimResponseEntity.setAcctResponseEntity(acctResponseEntity);
			acctClaimResponseRepository.save(acctClaimResponseEntity);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private AccountingApiHistoryEntity saveMessage(String request, String response, String username) {
		AccountingApiHistoryEntity accountingApiHistoryEntity = new AccountingApiHistoryEntity();
		accountingApiHistoryEntity.setApiRequestBody(request.toString());
		accountingApiHistoryEntity.setCreatedBy(username);
		accountingApiHistoryEntity.setCreatedDate(new Date());
		accountingApiHistoryEntity.setApiResponseBody(response);
		return accountingApiHistoryRepository.save(accountingApiHistoryEntity);
	}

	@Override
	public GlTransactionModelDto getGlTransactionModel(Long productId, Long variantId, String unitCode,
			String naration) {
		GratutityIcodesEntity gratutityIcodesEntity = gratutityIcodesRepository.findByProductIdVariantId(productId,
				variantId);
		String unitStateName = commonMasterUnitRepository.getStateNameByUnitCode(unitCode);
		String unitStateType = commonMasterStateRepository.getStateType(unitStateName);

		GlTransactionModelDto glTransactionModelDto = new GlTransactionModelDto();
		glTransactionModelDto.setBankAccountType("AC4");
		glTransactionModelDto.setICodeForBusinessSegment(gratutityIcodesEntity.getIcodeBusinessSegment());
		glTransactionModelDto.setICodeForInvestmentPortfolio(1L);
		glTransactionModelDto.setICodeForLob(gratutityIcodesEntity.getIcodeBusinessLine());
		glTransactionModelDto.setICodeForBusinessType(gratutityIcodesEntity.getIcodeBuinessType());
		glTransactionModelDto.setICodeForParticipatingType(gratutityIcodesEntity.getIcodeParticipatingType());
		glTransactionModelDto.setICodeForProductLine(gratutityIcodesEntity.getIcodeProductLine());
		glTransactionModelDto.setICodeForVariant(gratutityIcodesEntity.getIcodeVarient().toString());
		glTransactionModelDto.setNarration(naration);
		glTransactionModelDto.setOperatingUnit(unitCode);
		glTransactionModelDto.setOperatingUnitType("UO");
		glTransactionModelDto.setSubRefType("A");

		return glTransactionModelDto;
	}

	@Override
	public JournalVoucherDetailModelDto getJournalVoucherDetailModel(String lineOfBusiness, Long productId,
			Long variantId) {
		JournalVoucherDetailModelDto journalVoucherDetailModelDto = new JournalVoucherDetailModelDto();
		journalVoucherDetailModelDto.setLineOfBusiness(lineOfBusiness);
		journalVoucherDetailModelDto.setProduct(commonModuleService.getProductCode(productId));
		journalVoucherDetailModelDto.setProductVariant(commonModuleService.getVariantCode(variantId));

		return journalVoucherDetailModelDto;
	}

	@Override
	public void consumeDeposit(RenewalContributionAdjustRequestModelDto renewalContributionAdjustRequestModelDto,Long masterId) {
		try {
			URL url = new URL(accountingServiceEndpoint
					+ "/accountsgratuityservice/ePGS/Accounts/Gratuity/RenewalContributionAndLifeCoverAdjustmentApprove");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(renewalContributionAdjustRequestModelDto);
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = json.getBytes("utf-8");
				os.write(input);
			}

			if (conn.getResponseCode() != 200 && conn.getResponseCode() != 202) {
				logger.error("HTTP Status Error: [intimateClaimReversalLiability] Expected 200, got "
						+ conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);

			saveMessage(renewalContributionAdjustRequestModelDto.toString(), output, "SYSTEM");
			saveDepositAdjustmentResponse(output, "PREMIUM-ADJUST",
					Long.parseLong(renewalContributionAdjustRequestModelDto.getRefNo()), "PSTG-CONT-DET-ID",masterId);

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	@Override
	public ResponseDto commonmasterserviceAllUnitCode(String unitCode) {
		ResponseDto responseDto = new ResponseDto();
		try {
			URL url = new URL(commanmasterunitcode + "/unit");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				logger.error("HTTP Status Error: [getHSNCode] Expected 200, got " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);

			for (JsonNode jsonNode : actualObj) {
				System.out.println(unitCode.equals(jsonNode.path("unitCode").textValue()));

				if (unitCode.equals(jsonNode.path("unitCode").textValue())) {
					responseDto.setAddress1(jsonNode.path("address1").textValue());
					responseDto.setUnitId(jsonNode.path("unitId").textValue());
					responseDto.setUnitCode(jsonNode.path("unitCode").textValue());
					responseDto.setDescription(jsonNode.path("description").textValue());
					responseDto.setIsActive(jsonNode.path("isActive").textValue());
					responseDto.setIsDeleted(jsonNode.path("isDeleted").textValue());
					responseDto.setPincode(jsonNode.path("pincode").textValue());
					responseDto.setCityName(jsonNode.path("cityName").textValue());
					responseDto.setDistrictName(jsonNode.path("districtName").textValue());
					responseDto.setStateName(jsonNode.path("stateName").textValue());
					responseDto.setAddress1(jsonNode.path("address1").textValue());
					responseDto.setAddress2(jsonNode.path("address2").textValue());
					responseDto.setAddress3(jsonNode.path("address3").textValue());
					responseDto.setAddress4(jsonNode.path("address4").textValue());
					responseDto.setEmailId(jsonNode.path("emailId").textValue());
					responseDto.setGstIn(jsonNode.path("gstIn").textValue());
					responseDto.setPan(jsonNode.path("pan").textValue());
//					responseDto.setSenderLEI(jsonNode.path("pan").textValue());
					break;

				}
			}

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return responseDto;

	}

	public Double getGSTRate(String unitStateType, String mphStateType, HSNCodeDto hSNCodeDto) {
		Double gstRate = 0.0;
		if (unitStateType.equals(mphStateType)) {
			if (unitStateType.equals("S") && mphStateType.equals("S")) {
				gstRate = (hSNCodeDto.getCgstRate() / 100) + (hSNCodeDto.getSgstRate() / 100);
			}
			if (unitStateType.equalsIgnoreCase("UT") && mphStateType.equalsIgnoreCase("UT")) {
				gstRate = (hSNCodeDto.getCgstRate() / 100) + (hSNCodeDto.getUtgstRate() / 100);
			}
		} else {
			gstRate = hSNCodeDto.getIgstRate() / 100;
		}
		return gstRate;
	}

	@Override
	public void consumeDepositsforSubsequence(NewBusinessContributionAndLifeCoverAdjustmentDto getDetailsDto,Long masterId) {

		try {
			URL url = new URL(accountingServiceEndpoint
					+ "/accountsgratuityservice/ePGS/Accounts/Gratuity/SubsequentOrRegularAndLifeCoverAdjustmentApprove");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(getDetailsDto);
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = json.getBytes("utf-8");
				os.write(input);
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);
			if (actualObj.get("status").textValue() == "Failed") {
				throw new RuntimeException("AccountingFailed : Response Status is : " + actualObj.get("status"));
			}
			saveMessage(json, output, "SYSTEM");
			saveDepositAdjustmentResponse(output, "PREMIUM-ADJUST", Long.parseLong(getDetailsDto.getRefNo()),
					"PSTG-CONT-DET-ID",masterId);

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public GlTransactionModel glTransactionModel(Long productId, Long productVariantId, String unitCode,
			String naration) {
		GratutityIcodesEntity gratutityIcodesEntity = gratutityIcodesRepository.findByProductIdVariantId(productId,
				productVariantId);
		String unitStateName = commonMasterUnitRepository.getStateNameByUnitCode(unitCode);
		String unitStateType = commonMasterStateRepository.getStateType(unitStateName);

		GlTransactionModel glTransactionModel = new GlTransactionModel();
		glTransactionModel.setBankAccountType("AC4");
		glTransactionModel.setICodeForBusinessSegment(gratutityIcodesEntity.getIcodeBusinessSegment());
		glTransactionModel.setICodeForInvestmentPortfolio(1L);
		glTransactionModel.setICodeForLob(gratutityIcodesEntity.getIcodeBusinessLine());
		glTransactionModel.setICodeForBusinessType(gratutityIcodesEntity.getIcodeBuinessType());
		glTransactionModel.setICodeForParticipatingType(gratutityIcodesEntity.getIcodeParticipatingType());
		glTransactionModel.setICodeForProductLine(gratutityIcodesEntity.getIcodeProductLine());
		glTransactionModel.setICodeForVariant(gratutityIcodesEntity.getIcodeVarient().toString());
		glTransactionModel.setNarration(naration);
		glTransactionModel.setOperatingUnit(unitCode);
		glTransactionModel.setOperatingUnitType("UO");
		glTransactionModel.setSubRefType("A");
		glTransactionModel.setIsLegacy("NO");
		glTransactionModel.setProductCode(productId.toString());
		glTransactionModel.setVariantCode(productVariantId.toString());
		glTransactionModel.setUnitCode(unitCode);
		glTransactionModel.setUserCode(null);// ??

		return glTransactionModel;

	}

	@Override
	public void consumeDeposit(MergePolicyRequestModel mergePolicyRequestModel,Long masterId) {
		try {
			URL url = new URL(
					accountingServiceEndpoint + "/accountsgratuityservice/ePGS/Accounts/Gratuity/MergePolicy");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(mergePolicyRequestModel);
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = json.getBytes("utf-8");
				os.write(input);
			}
			System.out.println(json);

			if (conn.getResponseCode() != 200 && conn.getResponseCode() != 202) {
				logger.error("HTTP Status Error: [intimateClaimReversalLiability] Expected 200, got "
						+ conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);

			saveMessage(mergePolicyRequestModel.toString(), output, "SYSTEM");
			saveDepositAdjustmentResponse(output, "POLICY-MERGER", Long.parseLong(mergePolicyRequestModel.getRefNo()),
					"PSTG-CONT-DET-ID",masterId);

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public Boolean policySurrender(
			SurrenderAccountingIntegrationRequestModel surrenderAccountingIntegrationRequestModel) {
		String message = null;

		try {
			URL url = new URL(
					accountingServiceEndpoint + "/accountsgratuityservice/ePGS/Accounts/Gratuity/PolicySurrender");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(surrenderAccountingIntegrationRequestModel);
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = json.getBytes("utf-8");
				os.write(input);
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);

			saveMessage(json, output, "SYSTEM");
			if (actualObj.get("responseStatus") != null
					&& actualObj.get("responseStatus").textValue().equalsIgnoreCase("Failed")) {
				message = "FAILED";
			} else {
				savePolicySurrenderResponse(output, "POLICY-SURRENDER",
						Long.parseLong(surrenderAccountingIntegrationRequestModel.getRefNo()), "PMST-CONT-DET-ID");
				message = "Success";
			}

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
			message = "FAILED";
		} catch (IOException e) {
			logger.error(e.getMessage());
			message = "FAILED";
		} catch (Exception e) {
			logger.error(e.getMessage());
			message = "FAILED";
		}

		if (message.equals("FAILED")) {
			return false;
		} else
			return true;

	}

	private PolicySurrenderAccountingResponse savePolicySurrenderResponse(String output, String module,
			Long referenceId, String referenceValue) {
		PolicySurrenderAccountingResponse policySurrenderAccountingResponse = new PolicySurrenderAccountingResponse();
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);

			policySurrenderAccountingResponse.setAdjustmentNo(Long.valueOf(actualObj.get("adjustmentNo").asInt()));
			policySurrenderAccountingResponse.setPolicyNumber(actualObj.get("policyNumber").textValue());
			policySurrenderAccountingResponse.setVoucherNo(actualObj.get("voucherNo").textValue());
			policySurrenderAccountingResponse
					.setVoucherEffectiveDate(actualObj.get("voucherEffectiveDate").textValue());
			policySurrenderAccountingResponse.setVoucherAmount(actualObj.get("voucherAmount").doubleValue());

			Set<AcctCrDrResDto> acctCrDrEntities = new HashSet<>();
			JsonNode details = actualObj.get("debitCreditResponseModels");
			for (JsonNode detail : details) {
				AcctCrDrResDto acctCrDrEntity = new AcctCrDrResDto();
				if (detail.get("debitCode") != null)
					acctCrDrEntity.setDebitCode(detail.get("debitCode").textValue());
				acctCrDrEntity.setDebitAmount(detail.get("debitAmount").doubleValue());
				if (detail.get("debitCodeDescription") != null)
					acctCrDrEntity.setDebitCodeDescription(detail.get("debitCodeDescription").textValue());
				if (detail.get("creditCode") != null)
					acctCrDrEntity.setCreditCode(detail.get("creditCode").textValue());
				acctCrDrEntity.setCreditAmount(detail.get("creditAmount").doubleValue());
				if (detail.get("creditCodeDescription") != null)
					acctCrDrEntity.setCreditCodeDescription(detail.get("creditCodeDescription").textValue());
				acctCrDrEntities.add(acctCrDrEntity);
			}
			DebitCreditResponse debitCreditResponse = new DebitCreditResponse();
			debitCreditResponse.setContraCrDrEntries(acctCrDrEntities);
			policySurrenderAccountingResponse.setDebitCreditResponseModels(debitCreditResponse);
			policySurrenderAccountingResponse.setContraVoucherNo(actualObj.get("contraVoucherNo").textValue());
			policySurrenderAccountingResponse
					.setContraVoucherEffectiveDate(actualObj.get("contraVoucherEffectiveDate").textValue());
			policySurrenderAccountingResponse
					.setContraVoucherAmount(actualObj.get("contraVoucherAmount").doubleValue());

			Set<AcctCrDrResDto> contraCrDrEntities = new HashSet<>();
			JsonNode details1 = actualObj.get("contraDebitCreditResponseModels");
			for (JsonNode detail : details1) {
				AcctCrDrResDto acctCrDrEntity = new AcctCrDrResDto();
				if (detail.get("debitCode") != null)
					acctCrDrEntity.setDebitCode(detail.get("debitCode").textValue());
				acctCrDrEntity.setDebitAmount(detail.get("debitAmount").doubleValue());
				if (detail.get("debitCodeDescription") != null)
					acctCrDrEntity.setDebitCodeDescription(detail.get("debitCodeDescription").textValue());
				if (detail.get("creditCode") != null)
					acctCrDrEntity.setCreditCode(detail.get("creditCode").textValue());
				acctCrDrEntity.setCreditAmount(detail.get("creditAmount").doubleValue());
				if (detail.get("creditCodeDescription") != null)
					acctCrDrEntity.setCreditCodeDescription(detail.get("creditCodeDescription").textValue());
				contraCrDrEntities.add(acctCrDrEntity);
			}

			ContraDebitCreditResponse contraDebitCreditResponse = new ContraDebitCreditResponse();
			contraDebitCreditResponse.setContraCrDrEntries(contraCrDrEntities);

			policySurrenderAccountingResponse.setContraDebitCreditResponseModels(contraDebitCreditResponse);

			policySurrenderAccountingResponse.setGstRefNo(actualObj.get("gstRefNo").textValue());
			policySurrenderAccountingResponse.setGstInvoiceNo(actualObj.get("gstInvoiceNo").textValue());
			if (actualObj.get("adjustmentNo") != null)
				policySurrenderAccountingResponse.setResponseStatus(actualObj.get("responseStatus").textValue());
			policySurrenderAccountingResponse.setResponseMessage(actualObj.get("responseMessage").textValue());
			if (actualObj.get("beneficiaryPaymentId") != null)
				policySurrenderAccountingResponse
						.setBeneficiaryPaymentId(Long.valueOf(actualObj.get("beneficiaryPaymentId").asInt()));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return policySurrenderAccountingResponse;
	}
	

	@Override
	public void domAccounting(MidLeaverContributuionPremuimAndGstDto midLeaverContributuionPremuimAndGstDto) {
		try {
			URL url = new URL(accountingServiceEndpoint
					+ "/accountsgratuityservice/ePGS/Accounts/Gratuity/memberSurrenderApprove");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			System.out.println("Acounting dto============>" + midLeaverContributuionPremuimAndGstDto);

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(midLeaverContributuionPremuimAndGstDto);
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			json = json.replaceFirst("icode", "iCode");
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = json.getBytes("utf-8");
				os.write(input);
			}

			if (conn.getResponseCode() != 200 && conn.getResponseCode() != 202) {
				logger.error("HTTP Status Error: [intimateClaimReversalLiability] Expected 200, got "
						+ conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);

			saveMessage(midLeaverContributuionPremuimAndGstDto.toString(), output, "SYSTEM");
			saveDepositAdjustmentResponse1(output, "MID-LEAVER",
					Long.parseLong(midLeaverContributuionPremuimAndGstDto.getRefNo()), "MID_LEAVER_PROPS");

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	private void saveDepositAdjustmentResponse1(String output, String module, Long referenceId, String referenceValue) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);

			AcctResponseEntity acctResponseEntity = new AcctResponseEntity();
			acctResponseEntity.setModule(module);
			acctResponseEntity.setReferenceId(referenceId);
			acctResponseEntity.setReferenceValue(referenceValue);

			AcctDepositResponseEntity acctDepositResponseEntity = new AcctDepositResponseEntity();
			acctDepositResponseEntity.setAcctResponseEntity(acctResponseEntity);
			acctDepositResponseEntity.setAdjustmentNumber(Long.valueOf(actualObj.get("adjustmentNo").asInt()));
			acctDepositResponseEntity.setVoucherNumber(actualObj.get("voucherNo").asText());
			acctDepositResponseEntity.setVoucherAmount(actualObj.get("voucherAmount").doubleValue());
			if (!actualObj.get("voucherEffectiveDate").toString().equals("null")) {
				acctDepositResponseEntity.setVoucherEffectiveDate(
						DateUtils.convertStringYYYYMMDDTHHMMSSSSZ1(actualObj.get("voucherEffectiveDate").asText()));
			}
			;
			acctDepositResponseEntity.setGstInvoiceNumber(actualObj.get("gstInvoiceNo").asText());

			Set<AcctCrDrEntity> acctCrDrEntities = new HashSet<>();
			JsonNode details = actualObj.get("debitCreditResponseModels");
			for (JsonNode detail : details) {
				AcctCrDrEntity acctCrDrEntity = new AcctCrDrEntity();
				if (detail.get("debitCode") != null)
					acctCrDrEntity.setDebitCode(detail.get("debitCode").textValue());
				acctCrDrEntity.setDebitAmount(detail.get("debitAmount").doubleValue());
				if (detail.get("debitCodeDescription") != null)
					acctCrDrEntity.setDebitCodeDescription(detail.get("debitCodeDescription").textValue());
				acctCrDrEntity.setCreditCode(detail.get("creditCode").textValue());
				acctCrDrEntity.setCreditAmount(detail.get("creditAmount").doubleValue());
				acctCrDrEntity.setCreditCodeDescription(detail.get("creditCodeDescription").textValue());
				acctCrDrEntity.setAcctResponseEntity(acctResponseEntity);
				acctCrDrEntities.add(acctCrDrEntity);
			}
			acctResponseEntity.setAcctCrDrEntities(acctCrDrEntities);
			acctResponseEntity = acctResponseRepository.save(acctResponseEntity);
			acctDepositResponseEntity.setAcctResponseEntity(acctResponseEntity);
			acctDepositResponseRepository.save(acctDepositResponseEntity);

		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

}
