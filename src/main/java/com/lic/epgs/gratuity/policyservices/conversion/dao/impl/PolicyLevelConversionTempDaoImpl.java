package com.lic.epgs.gratuity.policyservices.conversion.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalPolicyTMPDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.dto.RenewalQuotationSearchDTo;
import com.lic.epgs.gratuity.policy.renewalpolicy.entitiy.RenewalPolicyTMPEntity;
import com.lic.epgs.gratuity.policyservices.conversion.constant.PolicyLevelConversionConstants;
import com.lic.epgs.gratuity.policyservices.conversion.dao.PolicyConversionDao;
import com.lic.epgs.gratuity.policyservices.conversion.dto.datatable.GetPolicyConversionDetailsRequestDataTableDto;
import com.lic.epgs.gratuity.policyservices.conversion.dto.datatable.GetPolicyConversionDetailsResponseDataTableDto;

import lombok.RequiredArgsConstructor;
import scala.collection.mutable.StringBuilder;

@Repository
@RequiredArgsConstructor
@PropertySource(value = "classpath:queries/policy-conversion.properties", ignoreResourceNotFound = true)
public class PolicyLevelConversionTempDaoImpl implements PolicyConversionDao {

	JdbcTemplate jdbcTemplate;

	@Autowired
	public PolicyLevelConversionTempDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Autowired
	private Environment env;

	protected final Logger logger = LogManager.getLogger(getClass());

	private Long[] listOfStatus;

	public List<GetPolicyConversionDetailsResponseDataTableDto> getPolicyConversionDetailsDataTable(
			GetPolicyConversionDetailsRequestDataTableDto getPolicyConversionDetailsRequestDataTableDto) {

		String query = env.getProperty(PolicyLevelConversionConstants.GetPolicyConversionDetailsDataTable);
		logger.info("GET_POLICY_CONVERSION_DETAILS_DATATABLE---   {}", query);
		jdbcTemplate.setFetchSize(900000);

		if (getPolicyConversionDetailsRequestDataTableDto.getListOfStatus().length > 0
				&& getPolicyConversionDetailsRequestDataTableDto.getListOfStatus() != null) {

			StringBuilder array = new StringBuilder();
			listOfStatus = getPolicyConversionDetailsRequestDataTableDto.getListOfStatus();
			for (Long long1 : listOfStatus) {
				array.append(long1 + ",");
			}

			query = query.replaceAll("newQuery",
					"    AND pct.CONVERSION_STATUS IN(" + array.substring(0, array.length() - 1) + ")    ");
		} else {
			query = query.replaceAll("newQuery", "");
		}

		logger.info("GET_POLICY_CONVERSION_DETAILS_DATATABLE after---   {}", query);

		return jdbcTemplate.query(query.toString(), new Object[] {

				getPolicyConversionDetailsRequestDataTableDto.getConversionId(),
				getPolicyConversionDetailsRequestDataTableDto.getConversionId(),

				getPolicyConversionDetailsRequestDataTableDto.getConversionStatus(),
				getPolicyConversionDetailsRequestDataTableDto.getConversionStatus(),

				getPolicyConversionDetailsRequestDataTableDto.getConversionWorkflowStatus(),
				getPolicyConversionDetailsRequestDataTableDto.getConversionWorkflowStatus(),

				getPolicyConversionDetailsRequestDataTableDto.getColumnSort(),
				getPolicyConversionDetailsRequestDataTableDto.getColumnSort(),

				getPolicyConversionDetailsRequestDataTableDto.getStart(),
				getPolicyConversionDetailsRequestDataTableDto.getEnd()

		}, new int[] { Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER,

				Types.INTEGER, Types.INTEGER,

				Types.INTEGER, Types.INTEGER }

				, new RowMapper<GetPolicyConversionDetailsResponseDataTableDto>() {
					@Override
					public GetPolicyConversionDetailsResponseDataTableDto mapRow(ResultSet rs, int arg1)
							throws SQLException {
						GetPolicyConversionDetailsResponseDataTableDto responseDataTable = new GetPolicyConversionDetailsResponseDataTableDto();

						responseDataTable.setConversionId(rs.getLong("CONVERSION_ID"));
						responseDataTable.setServiceId(rs.getLong("SERVICE_ID"));
						responseDataTable.setConversionDate(rs.getDate("CONVERSION_DATE"));
						responseDataTable.setPrevPolicyId(rs.getLong("PREV_POLICY_ID"));
						responseDataTable.setPrevPolicyNo(rs.getString("PREV_POLICY_NO"));
						responseDataTable.setClaimPending(rs.getString("CLAIM_PENDING"));
						responseDataTable.setNewPolicyId(rs.getLong("NEW_POLICY_ID"));
						responseDataTable.setNewPolicyNo(rs.getString("NEW_POLICY_NO"));
						responseDataTable.setNewPolicyAnnualRenewalDate(rs.getDate("NEW_POLICY_ARD"));
						responseDataTable.setNewpolicyStatus(rs.getLong("NEW_POLICY_STATUS"));
						responseDataTable.setNewpolicyIssueDate(rs.getDate("NEW_POLICY_ISSUE_DT"));
						responseDataTable.setNewPolicyMphCode(rs.getString("NEW_POLICY_MPH_CODE"));
						responseDataTable.setNewPolicyMphName(rs.getString("NEW_POLICY_MPH_NAME"));
						responseDataTable.setNewPolicyProduct(rs.getLong("NEW_POLICY_PROD"));
						responseDataTable.setNewPolicyVariant(rs.getLong("NEW_POLICY_VARIANT"));
						responseDataTable.setNoOfCatalogue(rs.getLong("NO_OF_CATG"));
						responseDataTable.setPrevFundBalancde(rs.getLong("PREV_FUND_BALANCE"));
						responseDataTable.setPrevPolicyNo(rs.getString("PREV_POLICY_NO"));
						responseDataTable.setCreatedBy(rs.getString("CREATED_BY"));
						responseDataTable.setCreatedOn(rs.getString("CREATED_ON"));
						responseDataTable.setModifiedBy(rs.getString("MODIFIED_BY"));
						responseDataTable.setModifiedOn(rs.getDate("MODIFIED_ON"));
						responseDataTable.setConversionStatus(rs.getInt("CONVERSION_STATUS"));
						responseDataTable.setConversionWorkflowStatus(rs.getInt("CONVERSION_WORKFLOW_STATUS"));
						responseDataTable.setIsActive(rs.getBoolean("IS_ACTIVE"));
						responseDataTable.setUnitCode(rs.getString("UNIT_CODE"));
						responseDataTable.setRejectionReasonCode(rs.getString("REJECTION_REASON_CODE"));
						responseDataTable.setRejectionRemarks(rs.getString("REJECTION_REMARKS"));
//						responseDataTable.setStatusDescription(rs.getString("STATUS_DESCRIPTION"));

						responseDataTable.setRecordsTotal(rs.getLong("TOTAL_COUNT"));
						responseDataTable.setNoOfPages(rs.getLong("NO_OF_PAGES"));

						return responseDataTable;
					}
				});

	}

	@Override
	public List<RenewalPolicyTMPDto> inprogressAndExistingConversionDetails(RenewalQuotationSearchDTo renewalQuotationSearchDTo) {
		String query = "SELECT a.*, pmst_srv.*,conv.* \r\n"
				+ "		  , CEIL(total_count / 10) AS no_of_pages\r\n"
				+ "			FROM (\r\n"
				+ "				  SELECT\r\n"
				+ "			    pmst.*,\r\n"
				+ "			    row_number() OVER (ORDER BY pmst.POLICY_ID) AS rowno,\r\n"
				+ "			    count(1) OVER () AS total_count\r\n"
				+ "			  FROM PMST_TMP_POLICY pmst\r\n"
				+ " WHERE   "
				+ " 1=(CASE WHEN ? IS NOT NULL THEN CASE WHEN TRIM(COALESCE(pmst.CUSTOMER_NAME,'x')) = ? THEN 1 ELSE 0 END ELSE 1 END) \r\n"
				+ "and 1=(CASE WHEN ? IS NOT NULL THEN CASE WHEN TRIM(COALESCE(pmst.INTERMEDIARY_NAME,'x')) = ? THEN 1 ELSE 0 END ELSE 1 END)\r\n"
				+ "and 1=(CASE WHEN ? IS NOT NULL THEN CASE WHEN TRIM(COALESCE(pmst.LINE_OF_BUSINESS,'x')) = ? THEN 1 ELSE 0 END ELSE 1 END)\r\n"
				+ "and 1=(CASE WHEN ? IS NOT NULL THEN CASE WHEN TRIM(COALESCE(pmst.PRODUCT_ID,-99)) = ? THEN 1 ELSE 0 END ELSE 1 END)		\r\n"
				+ "and 1=(CASE WHEN ? IS NOT NULL THEN CASE WHEN TRIM(COALESCE(pmst.PRODUCT_VARIANT_ID,-99)) = ? THEN 1 ELSE 0 END ELSE 1 END) \r\n"
				+ "  newQuery \r\n" 
				+ "  statusList \r\n"
				+ "				  ORDER BY\r\n"
				+ "			  CASE WHEN 1 = 1 THEN pmst.POLICY_ID END ASC,\r\n"
				+ "			    CASE WHEN 11 = 11 THEN pmst.POLICY_ID END DESC\r\n"
				+ "				) a\r\n"
				+ "		JOIN PMST_POLICY_SERVICE pmst_srv ON a.POLICY_SERVICE_ID = pmst_srv.POLICY_SERVICE_ID\r\n"
				+ "		JOIN PMST_TMP_CONVERSION_PROPS conv ON conv.PMST_TMP_POLICY_ID = a.POLICY_ID and conv.IS_ACTIVE=1 \r\n"
				+ "            WHERE rowno >= 0 AND rowno <= 200";
		
		jdbcTemplate.setFetchSize(900000);
		
		

		
		if (renewalQuotationSearchDTo.getPolicyNumber() != null
				&& !renewalQuotationSearchDTo.getPolicyNumber().isEmpty()) {

			if (renewalQuotationSearchDTo.getPolicyNumber().length() == 4) {
				query = query.replaceAll("newQuery",
						" And  substr(pmst.POLICY_NUMBER,-4) = " + renewalQuotationSearchDTo.getPolicyNumber() + "  ");
			} else {
				query = query.replaceAll("newQuery",
						" And  pmst.policy_number = " + renewalQuotationSearchDTo.getPolicyNumber() + "  ");
			}

		} else {
			query = query.replaceAll("newQuery", "");
		}

		if (renewalQuotationSearchDTo.getStatusIdList().length > 0
				&& renewalQuotationSearchDTo.getStatusIdList() != null) {

			StringBuilder array = new StringBuilder();
			listOfStatus = renewalQuotationSearchDTo.getStatusIdList();
			for (Long long1 : listOfStatus) {
				array.append(long1 + ",");
			}


			query = query.replaceAll("statusList",
					"    AND pmst.QUOTATION_STATUS_ID IN(" + array.substring(0, array.length() - 1) + ")    ");
		} else {
			query = query.replaceAll("statusList", "");
		}
		logger.info("GET_POLICY_CONVERSION_DETAILS_DATATABLE---   {}", query);
		
		return jdbcTemplate.query(query.toString(), 
				new Object[] { 
						renewalQuotationSearchDTo.getCustomerName(), renewalQuotationSearchDTo.getCustomerName(),
						renewalQuotationSearchDTo.getIntermediaryName(), renewalQuotationSearchDTo.getIntermediaryName(),
						renewalQuotationSearchDTo.getLineOfBusiness(),renewalQuotationSearchDTo.getLineOfBusiness(),
						renewalQuotationSearchDTo.getProductId(),renewalQuotationSearchDTo.getProductId(),
						renewalQuotationSearchDTo.getProductVariant(),renewalQuotationSearchDTo.getProductVariant()

				}, new int[] { 
						Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR, Types.VARCHAR,
						Types.INTEGER, Types.INTEGER,
						Types.INTEGER, Types.INTEGER
						}

				, new RowMapper<RenewalPolicyTMPDto>() {
					@Override
					public RenewalPolicyTMPDto mapRow(ResultSet rs, int arg1)
							throws SQLException {
						RenewalPolicyTMPDto responseDataTable = new RenewalPolicyTMPDto();

						responseDataTable.setId(rs.getLong("POLICY_ID"));
						responseDataTable.setMasterpolicyHolder(rs.getLong("MPH_ID"));
						responseDataTable.setPolicyNumber(rs.getString("POLICY_NUMBER"));
						responseDataTable.setPolicyServiceId(rs.getLong("POLICY_SERVICE_ID"));
//						responseDataTable.setQuotationNumber(rs.getString("QUOTATION_NUMBER"));
						responseDataTable.setMasterQuotationId(rs.getLong("MASTER_QUOTATION_ID"));
						responseDataTable.setMasterPolicyId(rs.getLong("MASTER_POLICY_ID"));
						responseDataTable.setTmpPolicyId(rs.getLong("TMP_POLICY_ID"));
						responseDataTable.setGstApplicableId(rs.getLong("GST_APPLICABLE_ID"));
						responseDataTable.setQuotationDate(rs.getDate("QUOTATION_DATE"));
						responseDataTable.setPolicyDispatchDate(rs.getDate("POLICY_DISPATCH_DATE"));
						responseDataTable.setPolicyRecievedDate(rs.getDate("POLICY_RECIEVED_DATE"));
						responseDataTable.setPolicyStartDate(rs.getDate("POLICY_START_DATE"));
						responseDataTable.setPolicyEndDate(rs.getDate("POLICY_END_DATE"));
						responseDataTable.setAnnualRenewlDate(rs.getDate("ANNUAL_RENEWAL_DATE"));
						responseDataTable.setLastPremiumPaid(rs.getDate("LAST_PREMIUM_PAID"));
						responseDataTable.setMaximumStampFee(rs.getFloat("MAXIMUM_STAMP_FEE"));
						responseDataTable.setTotalMember(rs.getLong("TOTAL_MEMBER"));
						responseDataTable.setPolicyStatusId(rs.getLong("POLICY_STATUS_ID"));
						responseDataTable.setPolicySubStatusId(rs.getLong("POLICY_SUB_STATUS_ID"));
						responseDataTable.setPolicytaggedStatusId(rs.getLong("POLICY_TAGGED_STATUS_ID"));
						responseDataTable.setProductId(rs.getLong("PRODUCT_ID"));
//						responseDataTable.setProductName(rs.getString("PRODUCT_NAME"));
//						responseDataTable.setProductVariant(rs.getString("PRODUCT_VARIANT"));
						responseDataTable.setProductVariantId(rs.getLong("PRODUCT_VARIANT_ID"));
						responseDataTable.setLineOfBusiness(rs.getString("LINE_OF_BUSINESS"));
						responseDataTable.setContactPersonId(rs.getLong("CONTACT_PERSON_ID"));
						responseDataTable.setBankAccountId(rs.getLong("BANK_ACCOUNT_ID"));
						responseDataTable.setIsApprovalEscalatedToGo(rs.getLong("IS_APPROVAL_ESCALATED_TO_CO"));
						responseDataTable.setIsApprovalEscalatedToZo(rs.getLong("IS_APPROVAL_ESCALATED_TO_ZO"));
						responseDataTable.setIsMinPaymentRecevied(rs.getLong("IS_MIN_PAYMENT_RECEIVED"));
						responseDataTable.setIsFullPaymentReceived(rs.getLong("IS_FULL_PAYMENT_RECEIVED"));
//						responseDataTable.setUnitId(rs.getString("UNIT_ID"));
						responseDataTable.setUnitCode(rs.getString("UNIT_CODE"));
//						responseDataTable.setUnitOffice(rs.getString("UNIT_OFFICE"));
						responseDataTable.setMarketingOfficerCode(rs.getString("MARKETING_OFFICER_CODE"));
						responseDataTable.setMarketingOfficerName(rs.getString("MARKETING_OFFICER_NAME"));
						responseDataTable.setIntermediaryCode(rs.getString("INTERMEDIARY_CODE"));
						responseDataTable.setIntermediaryName(rs.getString("INTERMEDIARY_NAME"));
						responseDataTable.setAdvanceOrArrears(rs.getString("ADVANCE_OR_ARREARS"));
						responseDataTable.setContributionFrequencyId(rs.getLong("CONTRIBUTION_FREQUENCY_ID"));
						responseDataTable.setMemberUniqueIdendifierId(rs.getLong("MEMBER_UNIQUE_IDENTIFIER_ID"));
						responseDataTable.setRiskCommencementDate(rs.getDate("RISK_COMMENCEMENT_DATE"));
						responseDataTable.setIsActive(rs.getBoolean("IS_ACTIVE"));
						responseDataTable.setCreatedBy(rs.getString("CREATED_BY"));
						responseDataTable.setCreatedDate(rs.getDate("CREATED_DATE"));
						responseDataTable.setModifiedBy(rs.getString("MODIFIED_BY"));
						responseDataTable.setModifiedDate(rs.getDate("MODIFIED_DATE"));
						responseDataTable.setLastLicId(rs.getLong("LAST_LIC_ID"));
						responseDataTable.setCustomerName(rs.getString("CUSTOMER_NAME"));
						responseDataTable.setCustomerCode(rs.getString("CUSTOMER_CODE"));
						responseDataTable.setIndustryType(rs.getString("INDUSTRY_TYPE"));
						responseDataTable.setIsStdValuesDeviated(rs.getBoolean("IS_STD_VALUES_DEVIATED"));
						responseDataTable.setQuotationStatusId(rs.getLong("QUOTATION_STATUS_ID"));
						responseDataTable.setQuotationSubStatusId(rs.getLong("QUOTATION_SUB_STATUS_ID"));
						responseDataTable.setQuotationTaggedStatusId(rs.getLong("QUOTATION_TAGGED_STATUS_ID"));
						responseDataTable.setRejectedReason(rs.getString("REJECTED_REASON"));
						responseDataTable.setRejectedRemarks(rs.getString("REJECTED_REMARKS"));
						responseDataTable.setPayto(rs.getLong("PAYTO"));
						responseDataTable.setConversionId(rs.getLong("TMP_CONVERSION_PROPS_ID"));
						responseDataTable.setQuotationNumber(rs.getLong("QUOTATION_NUMBER"));
//						responseDataTable.setSourcePolicyId(rs.getString("SOURCE_POLICY_ID"));
//						responseDataTable.setDestinationPolicyId(rs.getString("DESTINATION_POLICY_ID"));
//						responseDataTable.setSourceTmpPolicyId(rs.getString("SOURCE_TMP_POLICY_ID"));
//						responseDataTable.setDestinationTmpPolicyId(rs.getString("DESTINATION_TMP_POLICY_ID"));
//						responseDataTable.setDestinationPolicyNumber(rs.getString("DestinationPolicyNumber"));
						

//						responseDataTable.setRecordsTotal(rs.getLong("TOTAL_COUNT"));
//						responseDataTable.setNoOfPages(rs.getLong("NO_OF_PAGES"));

						return responseDataTable;
					}
				});
		
		
		
	
	}

	@Override
	public List<RenewalPolicyTMPDto> inprogressAndExistingConversionProcessing(
			RenewalQuotationSearchDTo renewalQuotationSearchDTo) {
		String query = "SELECT a.*, pmst_srv.*,conv.* \r\n"
				+ "		  , CEIL(total_count / 10) AS no_of_pages\r\n"
				+ "			FROM (\r\n"
				+ "				  SELECT\r\n"
				+ "			    pmst.*,\r\n"
				+ "			    row_number() OVER (ORDER BY pmst.POLICY_ID) AS rowno,\r\n"
				+ "			    count(1) OVER () AS total_count\r\n"
				+ "			  FROM PMST_TMP_POLICY pmst\r\n"
				+ " WHERE   "
				+ " 1=(CASE WHEN ? IS NOT NULL THEN CASE WHEN TRIM(COALESCE(pmst.CUSTOMER_NAME,'x')) = ? THEN 1 ELSE 0 END ELSE 1 END) \r\n"
				+ "and 1=(CASE WHEN ? IS NOT NULL THEN CASE WHEN TRIM(COALESCE(pmst.INTERMEDIARY_NAME,'x')) = ? THEN 1 ELSE 0 END ELSE 1 END)\r\n"
				+ "and 1=(CASE WHEN ? IS NOT NULL THEN CASE WHEN TRIM(COALESCE(pmst.LINE_OF_BUSINESS,'x')) = ? THEN 1 ELSE 0 END ELSE 1 END)\r\n"
				+ "and 1=(CASE WHEN ? IS NOT NULL THEN CASE WHEN TRIM(COALESCE(pmst.PRODUCT_ID,-99)) = ? THEN 1 ELSE 0 END ELSE 1 END)		\r\n"
				+ "and 1=(CASE WHEN ? IS NOT NULL THEN CASE WHEN TRIM(COALESCE(pmst.PRODUCT_VARIANT_ID,-99)) = ? THEN 1 ELSE 0 END ELSE 1 END) \r\n"
				+ "  newQuery \r\n" 
				+ "  statusList \r\n"
				+ "				  ORDER BY\r\n"
				+ "			  CASE WHEN 1 = 1 THEN pmst.POLICY_ID END ASC,\r\n"
				+ "			    CASE WHEN 11 = 11 THEN pmst.POLICY_ID END DESC\r\n"
				+ "				) a\r\n"
				+ "		JOIN PMST_POLICY_SERVICE pmst_srv ON a.POLICY_SERVICE_ID = pmst_srv.POLICY_SERVICE_ID\r\n"
				+ "		JOIN PMST_TMP_CONVERSION_PROPS conv ON conv.PMST_TMP_POLICY_ID = a.POLICY_ID and conv.IS_ACTIVE=1 \r\n"
				+ "            WHERE rowno >= 0 AND rowno <= 200";
		
		jdbcTemplate.setFetchSize(900000);
		
		

		
		if (renewalQuotationSearchDTo.getPolicyNumber() != null
				&& !renewalQuotationSearchDTo.getPolicyNumber().isEmpty()) {

			if (renewalQuotationSearchDTo.getPolicyNumber().length() == 4) {
				query = query.replaceAll("newQuery",
						" And  substr(pmst.POLICY_NUMBER,-4) = " + renewalQuotationSearchDTo.getPolicyNumber() + "  ");
			} else {
				query = query.replaceAll("newQuery",
						" And  pmst.policy_number = " + renewalQuotationSearchDTo.getPolicyNumber() + "  ");
			}

		} else {
			query = query.replaceAll("newQuery", "");
		}

		if (renewalQuotationSearchDTo.getStatusIdList().length > 0
				&& renewalQuotationSearchDTo.getStatusIdList() != null) {

			StringBuilder array = new StringBuilder();
			listOfStatus = renewalQuotationSearchDTo.getStatusIdList();
			for (Long long1 : listOfStatus) {
				array.append(long1 + ",");
			}


			query = query.replaceAll("statusList",
					"    AND pmst.POLICY_STATUS_ID IN(" + array.substring(0, array.length() - 1) + ")    ");
		} else {
			query = query.replaceAll("statusList", "");
		}
		logger.info("GET_POLICY_CONVERSION_DETAILS_DATATABLE---   {}", query);
		
		return jdbcTemplate.query(query.toString(), 
				new Object[] { 
						renewalQuotationSearchDTo.getCustomerName(), renewalQuotationSearchDTo.getCustomerName(),
						renewalQuotationSearchDTo.getIntermediaryName(), renewalQuotationSearchDTo.getIntermediaryName(),
						renewalQuotationSearchDTo.getLineOfBusiness(),renewalQuotationSearchDTo.getLineOfBusiness(),
						renewalQuotationSearchDTo.getProductId(),renewalQuotationSearchDTo.getProductId(),
						renewalQuotationSearchDTo.getProductVariant(),renewalQuotationSearchDTo.getProductVariant()

				}, new int[] { 
						Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR, Types.VARCHAR,
						Types.INTEGER, Types.INTEGER,
						Types.INTEGER, Types.INTEGER
						}

				, new RowMapper<RenewalPolicyTMPDto>() {
					@Override
					public RenewalPolicyTMPDto mapRow(ResultSet rs, int arg1)
							throws SQLException {
						RenewalPolicyTMPDto responseDataTable = new RenewalPolicyTMPDto();

						responseDataTable.setId(rs.getLong("POLICY_ID"));
						responseDataTable.setMasterpolicyHolder(rs.getLong("MPH_ID"));
						responseDataTable.setPolicyNumber(rs.getString("POLICY_NUMBER"));
						responseDataTable.setPolicyServiceId(rs.getLong("POLICY_SERVICE_ID"));
//						responseDataTable.setQuotationNumber(rs.getString("QUOTATION_NUMBER"));
						responseDataTable.setMasterQuotationId(rs.getLong("MASTER_QUOTATION_ID"));
						responseDataTable.setMasterPolicyId(rs.getLong("MASTER_POLICY_ID"));
						responseDataTable.setTmpPolicyId(rs.getLong("TMP_POLICY_ID"));
						responseDataTable.setGstApplicableId(rs.getLong("GST_APPLICABLE_ID"));
						responseDataTable.setQuotationDate(rs.getDate("QUOTATION_DATE"));
						responseDataTable.setPolicyDispatchDate(rs.getDate("POLICY_DISPATCH_DATE"));
						responseDataTable.setPolicyRecievedDate(rs.getDate("POLICY_RECIEVED_DATE"));
						responseDataTable.setPolicyStartDate(rs.getDate("POLICY_START_DATE"));
						responseDataTable.setPolicyEndDate(rs.getDate("POLICY_END_DATE"));
						responseDataTable.setAnnualRenewlDate(rs.getDate("ANNUAL_RENEWAL_DATE"));
						responseDataTable.setLastPremiumPaid(rs.getDate("LAST_PREMIUM_PAID"));
						responseDataTable.setMaximumStampFee(rs.getFloat("MAXIMUM_STAMP_FEE"));
						responseDataTable.setTotalMember(rs.getLong("TOTAL_MEMBER"));
						responseDataTable.setPolicyStatusId(rs.getLong("POLICY_STATUS_ID"));
						responseDataTable.setPolicySubStatusId(rs.getLong("POLICY_SUB_STATUS_ID"));
						responseDataTable.setPolicytaggedStatusId(rs.getLong("POLICY_TAGGED_STATUS_ID"));
						responseDataTable.setProductId(rs.getLong("PRODUCT_ID"));
//						responseDataTable.setProductName(rs.getString("PRODUCT_NAME"));
//						responseDataTable.setProductVariant(rs.getString("PRODUCT_VARIANT"));
						responseDataTable.setProductVariantId(rs.getLong("PRODUCT_VARIANT_ID"));
						responseDataTable.setLineOfBusiness(rs.getString("LINE_OF_BUSINESS"));
						responseDataTable.setContactPersonId(rs.getLong("CONTACT_PERSON_ID"));
						responseDataTable.setBankAccountId(rs.getLong("BANK_ACCOUNT_ID"));
						responseDataTable.setIsApprovalEscalatedToGo(rs.getLong("IS_APPROVAL_ESCALATED_TO_CO"));
						responseDataTable.setIsApprovalEscalatedToZo(rs.getLong("IS_APPROVAL_ESCALATED_TO_ZO"));
						responseDataTable.setIsMinPaymentRecevied(rs.getLong("IS_MIN_PAYMENT_RECEIVED"));
						responseDataTable.setIsFullPaymentReceived(rs.getLong("IS_FULL_PAYMENT_RECEIVED"));
//						responseDataTable.setUnitId(rs.getString("UNIT_ID"));
						responseDataTable.setUnitCode(rs.getString("UNIT_CODE"));
//						responseDataTable.setUnitOffice(rs.getString("UNIT_OFFICE"));
						responseDataTable.setMarketingOfficerCode(rs.getString("MARKETING_OFFICER_CODE"));
						responseDataTable.setMarketingOfficerName(rs.getString("MARKETING_OFFICER_NAME"));
						responseDataTable.setIntermediaryCode(rs.getString("INTERMEDIARY_CODE"));
						responseDataTable.setIntermediaryName(rs.getString("INTERMEDIARY_NAME"));
						responseDataTable.setAdvanceOrArrears(rs.getString("ADVANCE_OR_ARREARS"));
						responseDataTable.setContributionFrequencyId(rs.getLong("CONTRIBUTION_FREQUENCY_ID"));
						responseDataTable.setMemberUniqueIdendifierId(rs.getLong("MEMBER_UNIQUE_IDENTIFIER_ID"));
						responseDataTable.setRiskCommencementDate(rs.getDate("RISK_COMMENCEMENT_DATE"));
						responseDataTable.setIsActive(rs.getBoolean("IS_ACTIVE"));
						responseDataTable.setCreatedBy(rs.getString("CREATED_BY"));
						responseDataTable.setCreatedDate(rs.getDate("CREATED_DATE"));
						responseDataTable.setModifiedBy(rs.getString("MODIFIED_BY"));
						responseDataTable.setModifiedDate(rs.getDate("MODIFIED_DATE"));
						responseDataTable.setLastLicId(rs.getLong("LAST_LIC_ID"));
						responseDataTable.setCustomerName(rs.getString("CUSTOMER_NAME"));
						responseDataTable.setCustomerCode(rs.getString("CUSTOMER_CODE"));
						responseDataTable.setIndustryType(rs.getString("INDUSTRY_TYPE"));
						responseDataTable.setIsStdValuesDeviated(rs.getBoolean("IS_STD_VALUES_DEVIATED"));
						responseDataTable.setQuotationStatusId(rs.getLong("QUOTATION_STATUS_ID"));
						responseDataTable.setQuotationSubStatusId(rs.getLong("QUOTATION_SUB_STATUS_ID"));
						responseDataTable.setQuotationTaggedStatusId(rs.getLong("QUOTATION_TAGGED_STATUS_ID"));
						responseDataTable.setRejectedReason(rs.getString("REJECTED_REASON"));
						responseDataTable.setRejectedRemarks(rs.getString("REJECTED_REMARKS"));
						responseDataTable.setPayto(rs.getLong("PAYTO"));
						responseDataTable.setConversionId(rs.getLong("TMP_CONVERSION_PROPS_ID"));
						responseDataTable.setQuotationNumber(rs.getLong("QUOTATION_NUMBER"));
//						responseDataTable.setSourcePolicyId(rs.getString("SOURCE_POLICY_ID"));
//						responseDataTable.setDestinationPolicyId(rs.getString("DESTINATION_POLICY_ID"));
//						responseDataTable.setSourceTmpPolicyId(rs.getString("SOURCE_TMP_POLICY_ID"));
//						responseDataTable.setDestinationTmpPolicyId(rs.getString("DESTINATION_TMP_POLICY_ID"));
//						responseDataTable.setDestinationPolicyNumber(rs.getString("DestinationPolicyNumber"));
						

//						responseDataTable.setRecordsTotal(rs.getLong("TOTAL_COUNT"));
//						responseDataTable.setNoOfPages(rs.getLong("NO_OF_PAGES"));

						return responseDataTable;
					}
				});
		
		
		
	
	}

}
