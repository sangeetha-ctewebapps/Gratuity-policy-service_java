package com.lic.epgs.gratuity.fund.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.common.utils.ExecutorUtils;
import com.lic.epgs.gratuity.fund.dto.BatchDto;
import com.lic.epgs.gratuity.fund.dto.CalculateResDto;
import com.lic.epgs.gratuity.fund.dto.CalculationReqDto;
import com.lic.epgs.gratuity.fund.dto.ClaimReqDto;
import com.lic.epgs.gratuity.fund.dto.ClaimResDto;
import com.lic.epgs.gratuity.fund.dto.DocumentDto;
import com.lic.epgs.gratuity.fund.dto.FreelookCancellationReqDto;
import com.lic.epgs.gratuity.fund.dto.FundBatchResponseDto;
import com.lic.epgs.gratuity.fund.dto.JsonResponse;
import com.lic.epgs.gratuity.fund.dto.PolicyFundReportDto;
import com.lic.epgs.gratuity.fund.dto.PolicyFundStmtRequestDto;
import com.lic.epgs.gratuity.fund.dto.PolicyFundStmtResponseDto;
import com.lic.epgs.gratuity.fund.dto.PolicyStmtDto;
import com.lic.epgs.gratuity.fund.dto.PolicyStmtGenReqDto;
import com.lic.epgs.gratuity.fund.dto.PolicyStmtGenRespDto;
import com.lic.epgs.gratuity.fund.entity.FundBatchPolicyEntity;
import com.lic.epgs.gratuity.fund.entity.FundBatchSummaryEntity;
import com.lic.epgs.gratuity.fund.repository.FundBatchPolicyRepository;
import com.lic.epgs.gratuity.fund.repository.FundBatchSummaryRepository;
import com.lic.epgs.gratuity.fund.service.FundService;
import com.lic.epgs.gratuity.policy.integration.dto.InterestFundResponseDto;
import com.lic.epgs.gratuity.policy.repository.MasterPolicyRepository;
import com.lic.epgs.gratuity.policyservices.common.constants.CommonConstantsPS;

@Service
public class FundServiceImpl implements FundService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Value("${app.fundEndpoint}")
	private String fundEndpoint;

	@Autowired
	Environment environment;

	@Autowired
	private FundBatchPolicyRepository fundBatchPolicyRepository;

	@Autowired
	private FundBatchSummaryRepository fundBatchSummaryRepository;

	@Autowired
	private MasterPolicyRepository masterPolicyRepository;

	@Override
	public boolean setCreditEntries(Long policyId, Date date) {
		try {
			URL url = new URL(fundEndpoint + "transactionEntries/" + policyId + "/" + DateUtils.getFinancialYear(date));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			if (conn.getResponseCode() != 200) {
				logger.info(
						"Error in FundServiceImpl->setCreditEntries. Expected 200, received " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			conn.disconnect();

			return true;
		} catch (MalformedURLException e) {
			logger.info("Error in FundServiceImpl->setCreditEntries. " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.info("Error in FundServiceImpl->setCreditEntries. " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public CalculateResDto calculate(CalculationReqDto calculationReqDto) {
		try {
			URL url = new URL(fundEndpoint + "policy/batch/policyBatchExecution");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(calculationReqDto);

			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = json.getBytes("utf-8");
				os.write(input);
			}

			if (conn.getResponseCode() != 200) {
				logger.info("Error in FundServiceImpl->calculate. Expected 200, received " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);

			JsonNode node = actualObj.path("responseData").get(0);

			if (actualObj.path("status").textValue() == "ERROR") {
				logger.info("Error in FundServiceImpl->calculate. Expected 200, received " + conn.getResponseCode());
				throw new RuntimeException("Status Error : " + actualObj.path("status").textValue());
			}
			CalculateResDto calculateResDto = new CalculateResDto();
			calculateResDto.setAirAmount(node.get("airAmount").asDouble());
			calculateResDto.setAvailableBalance(node.get("availableBalance").asDouble());
			calculateResDto.setFinancialYear(node.get("financialYear").toString());
			calculateResDto.setFrequencyPeriod(node.get("frequencyPeriod").toString());
			calculateResDto.setFundSummaryId(node.get("fundSummaryId").asLong());
			calculateResDto.setInterestRate(node.get("interestRate").asDouble());
			calculateResDto.setMfrAmount(node.get("mfrAmount").asDouble());
			calculateResDto.setOpeningBalanceAmount(node.get("openingBalanceAmount").asDouble());
			calculateResDto.setOpeningBalanceInterestAmount(node.get("openingBalanceInterestAmount").asDouble());
			calculateResDto.setPolicyAccountValue(node.get("policyAccountValue").asDouble());
			calculateResDto.setPolicyId(node.get("policyId").asLong());
			calculateResDto.setPolicyNumber(node.get("policyNumber").toString());
			calculateResDto.setRaAmount(node.get("raAmount").asDouble());
			calculateResDto.setReconDays(node.get("reconDays").asLong());
			calculateResDto.setRemarks(node.get("remarks").toString());
			calculateResDto.setTotalContributionInterestAmount(node.get("totalContributionInterestAmount").asDouble());
			calculateResDto.setTotalContributionReceivedAmount(node.get("totalContributionReceivedAmount").asDouble());
			calculateResDto.setTotalDebitAmount(node.get("totalDebitAmount").asDouble());
			calculateResDto.setTotalDebitInterestAmount(node.get("totalDebitInterestAmount").asDouble());
			calculateResDto.setTotalFmcChargeAmount(node.get("totalFmcChargeAmount").asDouble());
			calculateResDto.setTotalFmcGstAmount(node.get("totalFmcGstAmount").asDouble());

			return calculateResDto;
		} catch (MalformedURLException e) {
			logger.info("Error in FundServiceImpl->calculate. " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.info("Error in FundServiceImpl->calculate. " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ClaimResDto setClaimEntry(ClaimReqDto claimReqDto) {
		try {
			URL url = new URL(fundEndpoint + "support/processClaim");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(claimReqDto);

			System.out.println("json:::::" + json);

			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = json.getBytes("utf-8");
				os.write(input);
			}

			if (conn.getResponseCode() != 200) {
				logger.info(
						"Error in FundServiceImpl->setClaimEntry. Expected 200, received " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			JsonNode actualObj = mapper.readTree(parser);

			JsonNode node = actualObj.path("responseData").get(0);
			ClaimResDto claimResDto = new ClaimResDto();
			claimResDto.setId(node.get("id").longValue());
			claimResDto.setPolicyId(node.get("policyId").longValue());
			claimResDto.setTransactionAmount(node.get("txnAmount").asDouble());
			claimResDto.setClosingBalance(node.get("closingBalance").asDouble());
			claimResDto.setOpeningBalance(node.get("openingBalance").asDouble());
			claimResDto.setStatus(actualObj.get("status").toString());

			return claimResDto;
		} catch (MalformedURLException e) {
			logger.info("Error in FundServiceImpl->setClaimEntry. " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.info("Error in FundServiceImpl->setClaimEntry. " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean setFreelookCancellation(FreelookCancellationReqDto freelookCancellationReqDto) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setMerger(Long serviceId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setConversion(Long serviceId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PolicyStmtGenRespDto generateFundStatement(PolicyStmtGenReqDto requestDto) {
		PolicyStmtGenRespDto response = new PolicyStmtGenRespDto();
		if (requestDto.getUnitId() == null || requestDto.getUnitId().trim().isEmpty()) {
			response.addErrorResponse("Invalid Unit Id");
			return response;
		}
		if (requestDto.getVariant() == null || requestDto.getVariant().trim().isEmpty()) {
			response.addErrorResponse("Invalid Variant");
			return response;
		}

		String generatedBatchNo = DateUtils.uniqueNoYYYMMYDDMilli();

		ExecutorUtils.getSingleExecutor().submit(() -> {
			processAsyncrestCallGratuity(requestDto, generatedBatchNo);
		});

		response.addBatchNo(generatedBatchNo);
		return response;
	}

	private void processAsyncrestCallGratuity(PolicyStmtGenReqDto requestDto, String generatedBatchNo) {
		String noOfPolicies = String.valueOf(requestDto.getPolicyId().size());
		AtomicInteger noOfSuccess = new AtomicInteger();
		AtomicInteger noOfFailed = new AtomicInteger();
		String batchStatus = "InProgress";
		String generatedDate = DateUtils.dateToStringDDMMYYYY(new Date());
		String generatedBy = "GA_BATCH";

		List<FundBatchPolicyEntity> fundBatchPolicyEntites = new ArrayList<>();
		requestDto.getPolicyId().forEach(e -> {
			FundBatchPolicyEntity fundBatchPolicyEntity = new FundBatchPolicyEntity();
			fundBatchPolicyEntity.setBatchNo(generatedBatchNo);
			fundBatchPolicyEntity.setPolicyId(e);
			fundBatchPolicyEntity.setTrnxDate(requestDto.getTrnxDate());
			fundBatchPolicyEntity.setVariant(requestDto.getVariant());
			fundBatchPolicyEntity.setUnitId(requestDto.getUnitId());
			fundBatchPolicyEntity.setPolicyType(requestDto.getPolicyType());
			fundBatchPolicyEntity.setStatus("InProgress");

			fundBatchPolicyEntites.add(fundBatchPolicyEntity);
		});
		fundBatchPolicyRepository.saveAllAndFlush(fundBatchPolicyEntites);

		FundBatchSummaryEntity fundBatchSummaryEntity = new FundBatchSummaryEntity();
		fundBatchSummaryEntity.setBatchNo(generatedBatchNo);
		fundBatchSummaryEntity.setBatchStatus(batchStatus);
		fundBatchSummaryEntity.setGeneratedBy(generatedBy);
		fundBatchSummaryEntity.setNoOFPolicies(BigDecimal.valueOf(Double.valueOf(noOfPolicies)));
		fundBatchSummaryEntity.setGeneratedDate(generatedDate);
		fundBatchSummaryEntity.setNoOFMembers(BigDecimal.valueOf(0));

		fundBatchSummaryRepository.saveAndFlush(fundBatchSummaryEntity);

		String url = fundEndpoint + "policy/batch/policyBatchExecution";
		requestDto.getPolicyId().forEach(e -> {
			BatchDto batchDto = new BatchDto();
			batchDto.setPolicyId(Long.valueOf(e));
			batchDto.setPolicyNumber("");
			batchDto.setTrnxDate(requestDto.getTrnxDate());
			batchDto.setVariant(requestDto.getVariant());
			batchDto.setPolicyType(requestDto.getPolicyType());
			batchDto.setIsBatch(true);
			batchDto.setIsAuto(true);
			batchDto.setRecalculate(true);
			batchDto.setUnitId(requestDto.getUnitId());
			batchDto.setMemberStatus("Active");

			RestTemplate rest = new RestTemplate();
			HttpHeaders headers = getAuthHeaders();
			HttpEntity<BatchDto> entity = new HttpEntity<>(batchDto, headers);
			ParameterizedTypeReference<FundBatchResponseDto<List<InterestFundResponseDto>>> typeRef = new ParameterizedTypeReference<FundBatchResponseDto<List<InterestFundResponseDto>>>() {
			};

			FundBatchResponseDto<List<InterestFundResponseDto>> responseDto = new FundBatchResponseDto<>();

			try {
				responseDto = rest.exchange(url, HttpMethod.POST, entity, typeRef).getBody();

				List<InterestFundResponseDto> responseData = responseDto.getResponseData();
				if (responseData != null && responseData.size() > 0) {
					String statusReturned = responseData.get(0).getBatchStatus();

					if (null != statusReturned && "ERROR".equalsIgnoreCase(statusReturned)) {
						noOfFailed.addAndGet(1);
						FundBatchPolicyEntity fundBatchPolicyEntity = fundBatchPolicyRepository
								.findByPolicyIdAndBatchNo(e, generatedBatchNo);
						fundBatchPolicyEntity.setStatus("ERROR: PolicyBatchExecution");
						fundBatchPolicyRepository.save(fundBatchPolicyEntity);
					} else if (null != statusReturned && "SUCCESS".equalsIgnoreCase(statusReturned)) {
						FundBatchPolicyEntity fundBatchPolicyEntity = fundBatchPolicyRepository
								.findByPolicyIdAndBatchNo(e, generatedBatchNo);
						if (setOpeningCreditAsyncGratuity(generatedBatchNo, batchDto)) {
							noOfSuccess.addAndGet(1);
							fundBatchPolicyEntity.setStatus("SUCCESS");
						} else {
							noOfFailed.addAndGet(1);
							fundBatchPolicyEntity.setStatus("ERROR: SetOpeningCredit");
						}
						fundBatchPolicyRepository.save(fundBatchPolicyEntity);
					}
				}
			} catch (Exception ex) {
				logger.error(ex);
				noOfFailed.addAndGet(1);
				FundBatchPolicyEntity fundBatchPolicyEntity = fundBatchPolicyRepository.findByPolicyIdAndBatchNo(e,
						generatedBatchNo);
				fundBatchPolicyEntity.setStatus("ERROR: " + ex.getMessage().substring(0, Math.min(ex.getMessage().length(), 210)));
				fundBatchPolicyRepository.save(fundBatchPolicyEntity);
			}
		});

		try {
			FundBatchSummaryEntity summaryEntities = fundBatchSummaryRepository.findByBatchNo(generatedBatchNo);
			if (summaryEntities != null) {
				summaryEntities.setNoOFFailurePolicies(BigDecimal.valueOf(noOfFailed.intValue()));
				summaryEntities.setNoOFSuccessPolicies(BigDecimal.valueOf(noOfSuccess.intValue()));
				summaryEntities.setBatchStatus("Completed");
				fundBatchSummaryRepository.save(summaryEntities);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private boolean setOpeningCreditAsyncGratuity(String batchNo, BatchDto batchDto) {
		String url = fundEndpoint + "policy/batch/setCreditOpeningByPolicyId";
		batchDto.setSetOpeningBalance(true);

		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = getAuthHeaders();
		HttpEntity<BatchDto> entity = new HttpEntity<>(batchDto, headers);
		ParameterizedTypeReference<FundBatchResponseDto<InterestFundResponseDto>> typeRef = new ParameterizedTypeReference<FundBatchResponseDto<InterestFundResponseDto>>() {
		};

		try {
			FundBatchResponseDto<InterestFundResponseDto> responseDto = rest
					.exchange(url, HttpMethod.POST, entity, typeRef).getBody();
			InterestFundResponseDto responseData = responseDto.getResponseData();
			if (responseData != null) {
				String statusReturned = responseDto.getResponseData().getBatchStatus();
				if (null != statusReturned && "ERROR".equalsIgnoreCase(statusReturned)) {
					return false;
				} else if (null != statusReturned && "SUCCESS".equalsIgnoreCase(statusReturned)) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public HttpHeaders getAuthHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(CommonConstantsPS.AUTHORIZATION, "Bearer ");
		headers.add(CommonConstantsPS.CORELATIONID, UUID.randomUUID().toString());
		headers.add(CommonConstantsPS.BUSINESSCORELATIONID, UUID.randomUUID().toString());
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	@Override
	public JsonResponse fetchBatchStatus(String batchNo) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			List<FundBatchSummaryEntity> fundBatchSummaryEntities = new ArrayList<>();
			fundBatchSummaryEntities = fundBatchSummaryRepository.findBatchDetailsByBatchNo(batchNo);
			jsonResponse.setSuccess(true);
			jsonResponse.setSuccessData((Object) fundBatchSummaryEntities.get(0));
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return jsonResponse;
	}

	@Override
	public PolicyFundStmtResponseDto fetchDetailsFrPolicyFundStatement(PolicyFundStmtRequestDto requestDto) {
		PolicyFundStmtResponseDto responseDto = new PolicyFundStmtResponseDto();
		List<PolicyStmtDto> listOfPolicies = new ArrayList<>();
		try {
			List<Object[]> policyDetails = masterPolicyRepository.findByYearQtrVariant(requestDto.getUnitId(),
					requestDto.getFinancialYear(), requestDto.getFrequency(), requestDto.getVariant());
			BeanUtils.copyProperties(requestDto, responseDto);
			if (!policyDetails.isEmpty()) {
				for (Object[] object : policyDetails) {
					PolicyStmtDto policyStmtDto = new PolicyStmtDto();
					policyStmtDto.setPolicyId(String.valueOf(object[0]));
					policyStmtDto.setPolicyNumber(String.valueOf(object[1]));
//					Default all flags are false
					policyStmtDto.setIsOsClaim(false);
					policyStmtDto.setIsOSDeposit(false);
					if (String.valueOf(object[2]).equals("1")) {
						policyStmtDto.setIsFundGenerated(true);
						policyStmtDto.setRemarks("Statement already generated");
					} else if (String.valueOf(object[2]).equals("0")) {
						policyStmtDto.setIsFundGenerated(false);
					}
					listOfPolicies.add(policyStmtDto);
				}
			}

		} catch (Exception e1) {
			logger.error(e1.getMessage());
			e1.printStackTrace();
		}
		responseDto.setListOfPolicies(listOfPolicies);
		return responseDto;
	}

	@Override
	public DocumentDto downloadPolicyFundStatementForPolicyId(String policyId) {
		DocumentDto documentDto = new DocumentDto();
		logger.info("downloadPolicyFundStatementForPolicyId:Start:{}", policyId);
		try {
			List<PolicyFundReportDto> statementList = policyFundStatementQuery(policyId);
			ByteArrayInputStream in = null;
			in = convertPolicyFundListToInputStream(statementList);

			String filename = "PolicyFund: " + policyId + "_" + DateUtils.uniqueNo() + ".csv";
			InputStreamResource file = new InputStreamResource(in);

			byte[] byteArray = IOUtils.toByteArray(file.getInputStream());
			documentDto.setIn(in);
			documentDto.setBytes(byteArray);
			documentDto.setFileName(filename);
			documentDto.setFileType("csv");
			documentDto.setStatus("SUCCESS");
			documentDto.setStatusId(1);
		} catch (IllegalArgumentException e) {
			documentDto.setStatus("ERROR");
			documentDto.setStatusId(0);
			documentDto.setException(e.getMessage());
			logger.error("csv", e);
		} catch (IOException e) {
			logger.error("Exception: ", e);
			e.printStackTrace();
		} finally {
			logger.info("downloadPolicyFundStatementForPolicyId:End:{}", policyId);
		}
		return documentDto;
	}

	private List<PolicyFundReportDto> policyFundStatementQuery(String policyId) {
		List<PolicyFundReportDto> response = new ArrayList<>();
		List<Object[]> data = masterPolicyRepository.downloadPolicyFundByPolicyId(policyId);
		if (null != data) {

			for (Object[] o : data) {
				PolicyFundReportDto r = new PolicyFundReportDto();
				r.setPolicyNumber((String) o[0]);
				r.setLicId((String) o[1]);
				r.setFirstName((String) o[2]);
				r.setOpeningBalance(String.valueOf((BigDecimal) o[3]));
				r.setTotalContribution(String.valueOf((BigDecimal) o[4]));
				r.setOpeningBalanceInt(String.valueOf((BigDecimal) o[5]));
				r.setIntrestContribution(String.valueOf((BigDecimal) o[6]));
				r.setFmc(String.valueOf((BigDecimal) o[7]));
				r.setGst(String.valueOf((BigDecimal) o[8]));
				r.setClosingBalance(String.valueOf((BigDecimal) o[9]));
				r.setTransFromDate(((Timestamp) o[10]).toString());
				r.setTransToDate(((Timestamp) o[11]).toString());
				r.setUnitId((String) o[12]);
				response.add(r);
			}
		}
		return response;
	}

	@SuppressWarnings("deprecation")
	private ByteArrayInputStream convertPolicyFundListToInputStream(List<PolicyFundReportDto> report)
			throws IOException {
		final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {

			csvPrinter.printRecord(Arrays.asList("POLICY NUMBER", "LIC ID", "EMPLOYEE NAME", "OPENING BAL",
					"CONTRIBUTION", "INTERST ON OB", "INTEREST CONTRIB", "FMC", "GST", "CLOSING BAL", "FROM DATE",
					"TO DATE", "UNIT ID"));

			for (PolicyFundReportDto account : report) {
				List<String> data = Arrays.asList(account.getPolicyNumber(), account.getLicId(), account.getFirstName(),
						account.getOpeningBalance(), account.getTotalContribution(), account.getOpeningBalanceInt(),
						account.getIntrestContribution(), account.getFmc(), account.getGst(),
						account.getClosingBalance(), account.getTransFromDate(), account.getTransToDate(),
						account.getUnitId());
				csvPrinter.printRecord(data);
			}
			csvPrinter.flush();
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new IOException(e);
		}
	}
}
