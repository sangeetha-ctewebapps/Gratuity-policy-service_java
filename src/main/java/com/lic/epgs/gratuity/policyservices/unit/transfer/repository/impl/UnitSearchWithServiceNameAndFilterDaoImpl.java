package com.lic.epgs.gratuity.policyservices.unit.transfer.repository.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.DocumentDetailsResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.ExactMatchTransferSearchRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.MemberTransferOverviewDetailsRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.ServiceMemberTransferDetailsResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferMemberSearchWithFilterRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferSearchWithServiceResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.SearchWithServiceNameAndFilterDao;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Repository
public class UnitSearchWithServiceNameAndFilterDaoImpl implements SearchWithServiceNameAndFilterDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public List<TransferSearchWithServiceResponse> searchWithServiceNumber(
			ExactMatchTransferSearchRequest exactMatchTransferSearchRequest) throws ParseException {

		List<TransferSearchWithServiceResponse> transferSearchWithServiceResponseList = new ArrayList<>();
		TransferSearchWithServiceResponse transferSearchWithServiceResponse = new TransferSearchWithServiceResponse(); 
		List<String> unitCode = new ArrayList<>();
		List<String> transferSubStatusList = new ArrayList<>();
		Boolean bFlag = false;
		
		if (!ObjectUtils.isEmpty(exactMatchTransferSearchRequest.getUnitCode())) {
			for (String string : exactMatchTransferSearchRequest.getUnitCode()) {
				unitCode.add(string);
			}
		}
		
		if (!ObjectUtils.isEmpty(exactMatchTransferSearchRequest.getTransferSubStatus())) {
			for (String string : exactMatchTransferSearchRequest.getTransferSubStatus()) {
				transferSubStatusList.add(string);
			}
		}
		
		StringBuilder sqlQuery = new StringBuilder(
				"SELECT TR.TRANSFER_REQUISITION_ID, TR.TRANSFER_REQUEST_NUMBER,TR.LIC_ID, TMP.MEMBER_NAME, TMP.MEMBER_STATUS, TR.TRANSFER_REQUEST_DATE,\r\n"
				+ "						TR.TRANSFER_SUB_STATUS , TPD.UNIT_OUT, TPD.UNIT_IN, POLICY_NUMBER_IN, TR.IS_BULK\r\n"
				+ "						FROM TRANSFER_REQUISITION TR\r\n"
				+ "						JOIN TRANSFER_POLICY_DETAIL TPD ON TPD.TRANSFER_REQUISITION_ID = TR.TRANSFER_REQUISITION_ID\r\n"
				+ "						JOIN TRANSFER_MEMBER_POLICY_DETAIL TMP ON TMP.TRANSFER_POLICY_DETAIL_ID = TPD.TRANSFER_POLICY_DETAIL_ID WHERE");
		if (exactMatchTransferSearchRequest.getTransferRequestNumber() != null) {
			if (bFlag) {
				sqlQuery.append(" WHERE ");
			}
			sqlQuery.append("  TR.TRANSFER_REQUEST_NUMBER  = '"
					+ exactMatchTransferSearchRequest.getTransferRequestNumber() + "'");
			bFlag = true;
		}
		if (!ObjectUtils.isEmpty(exactMatchTransferSearchRequest.getUnitCode()) && exactMatchTransferSearchRequest.getTransferType().equalsIgnoreCase("out")) {
			sqlQuery.append("and (TPD.UNIT_OUT) IN (:unitCode) ");
		}else if (!ObjectUtils.isEmpty(exactMatchTransferSearchRequest.getUnitCode()) && exactMatchTransferSearchRequest.getTransferType().equalsIgnoreCase("in")) {
			sqlQuery.append("and (TPD.UNIT_IN) IN (:unitCode) ");
		}
		if (!ObjectUtils.isEmpty(exactMatchTransferSearchRequest.getIsBulk())) {			
			sqlQuery.append("and TR.IS_BULK =:isBulk ");			
		}
		if (!ObjectUtils.isEmpty(exactMatchTransferSearchRequest.getTransferSubStatus())) {
			sqlQuery.append("and (TR.TRANSFER_SUB_STATUS) IN (:transferSubStatus) ");
		}
		
		
		Query query = entityManager.createNativeQuery(sqlQuery.toString());		
		
		if (!ObjectUtils.isEmpty(exactMatchTransferSearchRequest.getIsBulk())) {
			query.setParameter("isBulk", exactMatchTransferSearchRequest.getIsBulk());			
		}
		if (!ObjectUtils.isEmpty(exactMatchTransferSearchRequest.getUnitCode())) {
			query.setParameter("unitCode", unitCode);
		}				
		if (!ObjectUtils.isEmpty(exactMatchTransferSearchRequest.getTransferSubStatus())) {
			query.setParameter("transferSubStatus", exactMatchTransferSearchRequest.getTransferSubStatus());			
		}
	
		log.info("Query : "+sqlQuery);
		try {
			List<Object[]> obj = query.getResultList();

			if (!obj.isEmpty()) {
				for (Object[] record : obj) {					
					transferSearchWithServiceResponse.setTransferRequisitionId(
							String.valueOf(record[0]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[0])));
					transferSearchWithServiceResponse.setTransferRequestNumber(
							String.valueOf(record[1]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[1])));
					transferSearchWithServiceResponse.setLicId(
							String.valueOf(record[2]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[2])));
					transferSearchWithServiceResponse
							.setMemberName(String.valueOf(record[3]).equals("null") ? "" : String.valueOf(record[3]));
					transferSearchWithServiceResponse
							.setMemberStatus(String.valueOf(record[4]).equals("null") ? "" : String.valueOf(record[4]));
					transferSearchWithServiceResponse
							.setTransferRequestDate((Date) (record[5]));														
					transferSearchWithServiceResponse
							.setTransferSubStatus(String.valueOf(record[6]).equals("null") ? "" : String.valueOf(record[6]));
					transferSearchWithServiceResponse
						.setUnitOut(String.valueOf(record[7]).equals("null") ? "" : String.valueOf(record[7]));
					transferSearchWithServiceResponse
						.setUnitIn(String.valueOf(record[8]).equals("null") ? "" : String.valueOf(record[8]));
					transferSearchWithServiceResponse
						.setPolicyNumberIn(String.valueOf(record[9]).equals("null") ? "" : String.valueOf(record[9]));
					transferSearchWithServiceResponse
						.setIsBulk(String.valueOf(record[10]).equals("null") ? "" : String.valueOf(record[10]));
					
					transferSearchWithServiceResponseList.add(transferSearchWithServiceResponse);										
					log.info("Values : " + transferSearchWithServiceResponse);
				}
			}
		} catch (InputMismatchException e) {
			log.error("Exception occurred while searchWithServiceNumber due to : "+e.getMessage());
		}
		return transferSearchWithServiceResponseList;
	}

	@Override
	public List<TransferSearchWithServiceResponse> getTransferMemberDetailsWithFilter(
			TransferMemberSearchWithFilterRequest transferMemberSearchWithFilterRequest) {
		TransferSearchWithServiceResponse transferSearchRes = null;
		List<TransferSearchWithServiceResponse> transferResponseList = null;		
		List<String> unitCode = new ArrayList<>();
		List<String> transferSubStatusList = new ArrayList<>();
		//Boolean bFlag = false;
		
		if (!ObjectUtils.isEmpty(transferMemberSearchWithFilterRequest.getUnitCode())) {
			for (String string : transferMemberSearchWithFilterRequest.getUnitCode()) {
				unitCode.add(string);
			}
		}
		
		if (!ObjectUtils.isEmpty(transferMemberSearchWithFilterRequest.getTransferSubStatus())) {
			for (String string : transferMemberSearchWithFilterRequest.getTransferSubStatus()) {
				transferSubStatusList.add(string);
			}
		}
					
		StringBuilder sqlQuery = new StringBuilder(
				"SELECT TR.TRANSFER_REQUISITION_ID, TR.TRANSFER_REQUEST_NUMBER,TR.LIC_ID, TMP.MEMBER_NAME, TMP.MEMBER_STATUS, TR.TRANSFER_REQUEST_DATE,\r\n"
						+ "				TR.TRANSFER_SUB_STATUS , TPD.UNIT_OUT, TPD.UNIT_IN, POLICY_NUMBER_IN, TR.IS_BULK\r\n"
						+ "				FROM TRANSFER_REQUISITION TR\r\n"
						+ "				JOIN TRANSFER_POLICY_DETAIL TPD ON TPD.TRANSFER_REQUISITION_ID = TR.TRANSFER_REQUISITION_ID\r\n"
						+ "				JOIN TRANSFER_MEMBER_POLICY_DETAIL TMP ON TMP.TRANSFER_POLICY_DETAIL_ID = TPD.TRANSFER_POLICY_DETAIL_ID\r\n"
						+ "				WHERE");

		
		if (!ObjectUtils.isEmpty(transferMemberSearchWithFilterRequest.getUnitCode()) && transferMemberSearchWithFilterRequest.getTransferType().equalsIgnoreCase("out")) {
			sqlQuery.append(" (TPD.UNIT_OUT) IN (:unitCode) ");
		}else if (!ObjectUtils.isEmpty(transferMemberSearchWithFilterRequest.getUnitCode()) && transferMemberSearchWithFilterRequest.getTransferType().equalsIgnoreCase("in")) {
			sqlQuery.append(" (TPD.UNIT_IN) IN (:unitCode) ");
		}
		if (transferMemberSearchWithFilterRequest.getIsBulk() != null) {			
			sqlQuery.append("  and TR.IS_BULK = '" + transferMemberSearchWithFilterRequest.getIsBulk() + "'");			
		}
		if (transferMemberSearchWithFilterRequest.getLicId() != null) {			
			sqlQuery.append("  and TR.LIC_ID  = '" + transferMemberSearchWithFilterRequest.getLicId() + "'");			
		}
		if (!ObjectUtils.isEmpty(transferMemberSearchWithFilterRequest.getTransferSubStatus())) {
			sqlQuery.append("and (TR.TRANSFER_SUB_STATUS) IN (:transferSubStatus) ");
		}
		if (transferMemberSearchWithFilterRequest.getPolicyNumberOut() != null) {				
			sqlQuery.append(" and TPD.POLICY_NUMBER_OUT  = '" + transferMemberSearchWithFilterRequest.getPolicyNumberOut() + "'");			
		}					
		if (transferMemberSearchWithFilterRequest.getCreatedFromDate() != null
				&& transferMemberSearchWithFilterRequest.getCreatedToDate() != null) {			
			if (transferMemberSearchWithFilterRequest.getCreatedFromDate()
					.equals(transferMemberSearchWithFilterRequest.getCreatedToDate())) {
				sqlQuery.append(" and trunc(TR.CREATED_ON) = to_date('"+transferMemberSearchWithFilterRequest.getCreatedFromDate()+"','DD-MM-YYYY')");
				
			} else {
				sqlQuery.append(" and (TR.CREATED_ON BETWEEN to_date('"+transferMemberSearchWithFilterRequest.getCreatedFromDate()+"','DD-MM-YYYY')" + " and "
						+ "to_date('"+transferMemberSearchWithFilterRequest.getCreatedToDate()+"','DD-MM-YYYY'))");
				
			}
		}
		
		
		
		// sqlQuery.append("order by TICKET_NUMBER desc");
		Query query = entityManager.createNativeQuery(sqlQuery.toString());

//		if (!ObjectUtils.isEmpty(transferMemberSearchWithFilterRequest.getCreatedFromDate())) {
//			query.setParameter("createdFromDate", transferMemberSearchWithFilterRequest.getCreatedFromDate());
//		}
//		if (!ObjectUtils.isEmpty(transferMemberSearchWithFilterRequest.getCreatedToDate())) {
//			query.setParameter("createdToDate", transferMemberSearchWithFilterRequest.getCreatedToDate());
//		}
		if (!ObjectUtils.isEmpty(transferMemberSearchWithFilterRequest.getUnitCode())) {
			query.setParameter("unitCode", unitCode);
		}
		if (!ObjectUtils.isEmpty(transferMemberSearchWithFilterRequest.getTransferSubStatus())) {
			query.setParameter("transferSubStatus", transferMemberSearchWithFilterRequest.getTransferSubStatus());			
		}
		System.out.println(sqlQuery);
		try {
			List<Object[]> obj = query.getResultList();
			transferResponseList = new ArrayList<>();
			if (!obj.isEmpty()) {
				for (Object[] record : obj) {
					transferSearchRes = new TransferSearchWithServiceResponse();
					transferSearchRes.setTransferRequisitionId(
							String.valueOf(record[0]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[0])));
					transferSearchRes.setTransferRequestNumber(
							String.valueOf(record[1]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[1])));
					transferSearchRes.setLicId(
							String.valueOf(record[2]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[2])));
					transferSearchRes
							.setMemberName(String.valueOf(record[3]).equals("null") ? "" : String.valueOf(record[3]));
					transferSearchRes
							.setMemberStatus(String.valueOf(record[4]).equals("null") ? "" : String.valueOf(record[4]));
					transferSearchRes
							.setTransferRequestDate((Date) (record[5]));														
					transferSearchRes
							.setTransferSubStatus(String.valueOf(record[6]).equals("null") ? "" : String.valueOf(record[6]));
					transferSearchRes
							.setUnitOut(String.valueOf(record[7]).equals("null") ? "" : String.valueOf(record[7]));
					transferSearchRes
							.setUnitIn(String.valueOf(record[8]).equals("null") ? "" : String.valueOf(record[8]));
					transferSearchRes
							.setPolicyNumberIn(String.valueOf(record[9]).equals("null") ? "" : String.valueOf(record[9]));
					transferSearchRes
							.setIsBulk(String.valueOf(record[10]).equals("null") ? "" : String.valueOf(record[10]));					
					
					transferResponseList.add(transferSearchRes);
				}
			}

		} catch (NullPointerException nl) {
			log.error(" Renewal could not be fetched due to  {} ", nl.getMessage());
		} catch (Exception exception) {
			log.error(" Renewal could not be fetched due to {} ", exception.getMessage());
		}

		return transferResponseList;
	}

	
	@Override
	@Transactional
	public List<TransferSearchWithServiceResponse> getOverviewDetails(Long transferRequisitionId) {
		log.info("Entering into getOverviewDetails : transferRequisitionId : "+transferRequisitionId);
		TransferSearchWithServiceResponse transferSearchWithServiceResponse = null;

		List<TransferSearchWithServiceResponse> transferSearchWithServiceResponseList = new ArrayList<>();
		Boolean bFlag = false;
		StringBuilder sqlQuery = new StringBuilder(
				"SELECT TR.TRANSFER_REQUEST_NUMBER,TR.TRANSFER_REQUEST_DATE, TR.TRANSFER_SUB_STATUS, TR.LIC_ID, TMP.MEMBER_NAME, TR.IS_BULK, TPD.POLICY_NUMBER_IN\r\n"
				+ "									FROM TRANSFER_REQUISITION TR\r\n"
				+ "									JOIN TRANSFER_POLICY_DETAIL TPD ON TPD.TRANSFER_REQUISITION_ID = TR.TRANSFER_REQUISITION_ID\r\n"
				+ "									JOIN TRANSFER_MEMBER_POLICY_DETAIL TMP ON TMP.TRANSFER_POLICY_DETAIL_ID = TPD.TRANSFER_POLICY_DETAIL_ID\r\n"
				+ "									where");
		if (transferRequisitionId != null) {
			if (bFlag) {
				sqlQuery.append(" WHERE ");
			}
			sqlQuery.append("  TR.TRANSFER_REQUISITION_ID  = '"
					+ transferRequisitionId + "'");
			bFlag = true;
		}
		Query query = entityManager.createNativeQuery(sqlQuery.toString());
		log.info("Query : "+sqlQuery);
		try {
			List<Object[]> obj = query.getResultList();

			if (!obj.isEmpty()) {
				for (Object[] record : obj) {
					transferSearchWithServiceResponse = new TransferSearchWithServiceResponse();
					
					transferSearchWithServiceResponse.setTransferRequisitionId(transferRequisitionId);
					transferSearchWithServiceResponse.setTransferRequestNumber(String.valueOf(record[0]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[0])));
					transferSearchWithServiceResponse.setTransferRequestDate((Date) (record[1]));
					transferSearchWithServiceResponse.setTransferSubStatus(String.valueOf(record[2]).equals("null") ? "" : String.valueOf(record[2]));
					transferSearchWithServiceResponse.setLicId(String.valueOf(record[3]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[3])));
					transferSearchWithServiceResponse.setMemberName(String.valueOf(record[4]).equals("null") ? "" : String.valueOf(record[4]));
					transferSearchWithServiceResponse.setIsBulk(String.valueOf(record[5]).equals("null") ? "" : String.valueOf(record[5]));
					transferSearchWithServiceResponse.setPolicyNumberIn(String.valueOf(record[6]).equals("null") ? "" : String.valueOf(record[6]));
					transferSearchWithServiceResponseList.add(transferSearchWithServiceResponse);
					log.info("Values : " + transferSearchWithServiceResponse);
				}
			}
		} catch (InputMismatchException e) {
			log.error("Error occurred while getting Benefit Details");
		}
		return transferSearchWithServiceResponseList;
	}

	@Override
	@Transactional
	public List<ServiceMemberTransferDetailsResponse> getSeviceDetails(Long transferRequisitionId) {
		log.info("Entering into getSeviceDetails : transferRequisitionId : "+transferRequisitionId);
		ServiceMemberTransferDetailsResponse serviceMemberTransferDetailsResponse = null;

		List<ServiceMemberTransferDetailsResponse> serviceMemberTransferDetailsResponseList = new ArrayList<>();
		Boolean bFlag = false;
		StringBuilder sqlQuery = new StringBuilder(
				"SELECT TR.ROLE, TR.LOCATION_TYPE, TR.LIC_ID, TPD.POLICY_NUMBER_IN,TPD.POLICY_NUMBER_OUT, TPD.MPH_NAME_IN, TPD.MPH_NAME_OUT, TPD.PRODUCT_NAME_IN,\r\n"
				+ "											TPD.PRODUCT_NAME_OUT,TPD.PRODUCT_VARIANT_IN, TPD.PRODUCT_VARIANT_OUT, TPD.FREQUENCY,\r\n"
				+ "											TPD.UNIT_IN,TPD.UNIT_OUT, TPD.ACCRUED_INTEREST_AMOUNT,TMP.CATEGORY_IN, TMP.CATEGORY_OUT, TMP.MEMBER_NAME, TMP.MEMBER_STATUS, TMP.PREMIUM_AMOUNT,\r\n"
				+ "											TMP.GST_ON_PREMIUN, TMP.ACCRUED_GRATUITY_EXISTING, TMP.DATE_OF_BIRTH, TMP.PAN_NUMBER, TMP.MEMBER_ID,TMP.COMPLETED_YEARS_OF_SERVICE,\r\n"
				+ "											TMP.DATE_OF_JOINING, TMP.TRANSFER_OUT_EFFECTIVE_DATE, TR.CREATED_BY, TMP.ACCRUED_GRATUITY_NEW,TMP.IS_ACCRUED_GRATUITY_MODIFIED,TMP.IS_PREMIUM_REFUND,\r\n"
				+ "											TPD.TEMP_POLICY_ID, TMP.ACCRUED_GRATUITY_IN, TMP.LIC_PREMIUM_IN, TMP.GST_ON_PREMIUN_IN, TMP.RETAIN_LIC_ID, TMP.IS_LIC_ID_EXIST,\r\n"
				+ "								            TMP.TO_MPH_REFUND, TMP.ADJUSTED_FOR_DATE, TPD.POLICY_ID_IN, TPD.POLICY_ID_OUT, TR.IS_BULK, TPD.PRODUCT_ID_IN, TPD.PRODUCT_ID_OUT\r\n"
				+ "											FROM TRANSFER_REQUISITION TR\r\n"
				+ "											JOIN TRANSFER_POLICY_DETAIL TPD ON TPD.TRANSFER_REQUISITION_ID = TR.TRANSFER_REQUISITION_ID\r\n"
				+ "											JOIN TRANSFER_MEMBER_POLICY_DETAIL TMP ON TMP.TRANSFER_POLICY_DETAIL_ID = TPD.TRANSFER_POLICY_DETAIL_ID\r\n"
				+ "											WHERE");
		
		if (transferRequisitionId != null) {
			if (bFlag) {
				sqlQuery.append(" WHERE ");
			}
			sqlQuery.append("  TR.TRANSFER_REQUISITION_ID  = '"
					+ transferRequisitionId + "'");
			bFlag = true;
		}
		Query query = entityManager.createNativeQuery(sqlQuery.toString());
		System.out.println(sqlQuery);
		try {
			List<Object[]> obj = query.getResultList();

			if (!obj.isEmpty()) {
				for (Object[] record : obj) {
					serviceMemberTransferDetailsResponse = new ServiceMemberTransferDetailsResponse();
					
					serviceMemberTransferDetailsResponse.setRole(String.valueOf(record[0]).equals("null") ? "" : String.valueOf(record[0]));
					serviceMemberTransferDetailsResponse.setLocationType(String.valueOf(record[1]).equals("null") ? "" : String.valueOf(record[1]));
					serviceMemberTransferDetailsResponse.setMemberLicId(String.valueOf(record[2]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[2])));
					serviceMemberTransferDetailsResponse.setPolicyNumberIn(String.valueOf(record[3]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[3])));
					serviceMemberTransferDetailsResponse.setPolicyNumberOut(String.valueOf(record[4]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[4])));
					serviceMemberTransferDetailsResponse.setMphNameIn(String.valueOf(record[5]).equals("null") ? "" : String.valueOf(record[5]));
					serviceMemberTransferDetailsResponse.setMphNameOut(String.valueOf(record[6]).equals("null") ? "" : String.valueOf(record[6]));
					serviceMemberTransferDetailsResponse.setProductIn(String.valueOf(record[7]).equals("null") ? "" : String.valueOf(record[7]));
					serviceMemberTransferDetailsResponse.setProductOut(String.valueOf(record[8]).equals("null") ? "" : String.valueOf(record[8]));
					serviceMemberTransferDetailsResponse.setVariantIn(String.valueOf(record[9]).equals("null") ? "" : String.valueOf(record[9]));
					serviceMemberTransferDetailsResponse.setVariantOut(String.valueOf(record[10]).equals("null") ? "" : String.valueOf(record[10]));
					serviceMemberTransferDetailsResponse.setFrequencyIn(String.valueOf(record[11]).equals("null") ? "" : String.valueOf(record[11]));
					serviceMemberTransferDetailsResponse.setUnitCodeIn(String.valueOf(record[12]).equals("null") ? "" : String.valueOf(record[12]));
					serviceMemberTransferDetailsResponse.setUnitCodeOut(String.valueOf(record[13]).equals("null") ? "" : String.valueOf(record[13]));
					serviceMemberTransferDetailsResponse.setTotalInterestAccrued(String.valueOf(record[14]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[14])));
					serviceMemberTransferDetailsResponse.setCategoryIn(String.valueOf(record[15]).equals("null") ? "" : String.valueOf(record[15]));
					serviceMemberTransferDetailsResponse.setCategoryOut(String.valueOf(record[16]).equals("null") ? "" : String.valueOf(record[16]));
					serviceMemberTransferDetailsResponse.setMemberName(String.valueOf(record[17]).equals("null") ? "" : String.valueOf(record[17]));
					serviceMemberTransferDetailsResponse.setMemberStatus(String.valueOf(record[18]).equals("null") ? "" : String.valueOf(record[18]));
					serviceMemberTransferDetailsResponse.setRefundPremiumOut(String.valueOf(record[19]).equals("null") ? 0D : Double.parseDouble(String.valueOf(record[19])));
					serviceMemberTransferDetailsResponse.setRefundGstOut(String.valueOf(record[20]).equals("null") ? 0D : Double.parseDouble(String.valueOf(record[20])));
					serviceMemberTransferDetailsResponse.setAccruedGratuityExisting(String.valueOf(record[21]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[21])));
					serviceMemberTransferDetailsResponse.setDateOfBirth((Date) (record[22]));
					serviceMemberTransferDetailsResponse.setPanNumber(String.valueOf(record[23]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[23])));
					serviceMemberTransferDetailsResponse.setMemberId(String.valueOf(record[24]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[24])));
					serviceMemberTransferDetailsResponse.setPastSericve(String.valueOf(record[25]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[25])));
					serviceMemberTransferDetailsResponse.setDateOfJoining((Date) (record[26]));
					serviceMemberTransferDetailsResponse.setTransferEffectiveDate((Date) (record[27]));
					serviceMemberTransferDetailsResponse.setUserName(String.valueOf(record[28]).equals("null") ? "" : String.valueOf(record[28]));
					serviceMemberTransferDetailsResponse.setAccruedGratuityNew(String.valueOf(record[29]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[29])));
					serviceMemberTransferDetailsResponse.setIsAccruedGratuityModified(String.valueOf(record[30]).equals("null") ? "" : String.valueOf(record[30]));
					serviceMemberTransferDetailsResponse.setIsPremiumRefund(String.valueOf(record[31]).equals("null") ? "" : String.valueOf(record[31]));
					serviceMemberTransferDetailsResponse.setTempPolicyId(String.valueOf(record[32]).equals("null") ? null : Long.parseLong(String.valueOf(record[32])));					
					serviceMemberTransferDetailsResponse.setAccruedGratuityIn(String.valueOf(record[33]).equals("null") ? 0D : Double.parseDouble(String.valueOf(record[33])));
					serviceMemberTransferDetailsResponse.setPremiumAmountIn(String.valueOf(record[34]).equals("null") ? 0D : Double.parseDouble(String.valueOf(record[34])));
					serviceMemberTransferDetailsResponse.setGstOnPremiumIn(String.valueOf(record[35]).equals("null") ? 0D : Double.parseDouble(String.valueOf(record[35])));
					serviceMemberTransferDetailsResponse.setRetainLicId(String.valueOf(record[36]).equals("null") ? null : String.valueOf(record[36]));
					serviceMemberTransferDetailsResponse.setIsLicIdExist(String.valueOf(record[37]).equals("null") ? null : String.valueOf(record[37]));
					serviceMemberTransferDetailsResponse.setToMphRefund(String.valueOf(record[38]).equals("null") ? "" : String.valueOf(record[38]));
					serviceMemberTransferDetailsResponse.setAdjustedForDate((Date) (record[39]));
					serviceMemberTransferDetailsResponse.setPolicyIdIn(String.valueOf(record[40]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[40])));
					serviceMemberTransferDetailsResponse.setPolicyIdOut(String.valueOf(record[41]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[41])));
					serviceMemberTransferDetailsResponse.setIsBulk(String.valueOf(record[42]).equals("null") ? "" : String.valueOf(record[42]));
					serviceMemberTransferDetailsResponse.setProductIdIn(String.valueOf(record[43]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[43])));
					serviceMemberTransferDetailsResponse.setProductIdOut(String.valueOf(record[44]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[44])));
					
					serviceMemberTransferDetailsResponseList.add(serviceMemberTransferDetailsResponse);					
					log.info("Values" + serviceMemberTransferDetailsResponse);
				}
			}
		} catch (InputMismatchException e) {
			log.error("Error occurred while getting Benefit Details");
		}
		return serviceMemberTransferDetailsResponseList;
	}

	
	@Override
	@Transactional
	public List<DocumentDetailsResponse> getDocumentDetails(Long transferRequisitionId) {
		log.info("Entering into getDocumentDetails : transferRequisitionId : "+transferRequisitionId);
		DocumentDetailsResponse documentDetailsResponse = null;

		List<DocumentDetailsResponse> documentDetailsResponseList = new ArrayList<>();
		Boolean bFlag = false;
		StringBuilder sqlQuery = new StringBuilder(
				"SELECT TDD.TRANSFER_DOCUMENT_DETAIL_ID,TDD.TRANSFER_REQUISITION_ID,TDD.PICKLIST_ITEM_ID,TDD.IS_RECEIVED,TDD.REMARKS,TDD.CREATED_BY,TDD.CREATED_ON,MODIFIED_ON,\r\n"
				+ "TDD.MODIFIED_BY,TDD.IS_UPLOADED,TDD.DOCUMENT_INDEX,TDD.FOLDER_INDEX_NO \r\n"
				+ "FROM TRANSFER_DOCUMENT_DETAIL TDD\r\n"
				+ "WHERE");
		
		if (transferRequisitionId != null) {
			if (bFlag) {
				sqlQuery.append(" WHERE ");
			}
			sqlQuery.append("  TDD.TRANSFER_REQUISITION_ID  = '"
					+ transferRequisitionId + "'");
			bFlag = true;
		}
		Query query = entityManager.createNativeQuery(sqlQuery.toString());
		System.out.println(sqlQuery);
		try {
			List<Object[]> obj = query.getResultList();

			if (!obj.isEmpty()) {
				for (Object[] record : obj) {
					documentDetailsResponse = new DocumentDetailsResponse();
					
					documentDetailsResponse.setTransferDocumentDetailId(String.valueOf(record[0]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[0])));
					documentDetailsResponse.setTransferRequisitionId(String.valueOf(record[1]).equals("null") ? 0L : Long.parseLong(String.valueOf(record[1])));
					documentDetailsResponse.setPicklistItemId(String.valueOf(record[2]).equals("null") ? "" : String.valueOf(record[2]));
					documentDetailsResponse.setIsReceived(String.valueOf(record[3]).equals("null") ? "" : String.valueOf(record[3]));
					documentDetailsResponse.setRemarks(String.valueOf(record[4]).equals("null") ? "" : String.valueOf(record[4]));
					documentDetailsResponse.setCreatedBy(String.valueOf(record[5]).equals("null") ? "" : String.valueOf(record[5]));
					documentDetailsResponse.setCreatedOn((Date) (record[6]));
					documentDetailsResponse.setModifiedOn((Date) (record[7]));
					documentDetailsResponse.setModifiedBy(String.valueOf(record[8]).equals("null") ? "" : String.valueOf(record[8]));
					documentDetailsResponse.setIsUploaded(String.valueOf(record[9]).equals("null") ? "" : String.valueOf(record[9]));
					documentDetailsResponse.setDocumentIndex(String.valueOf(record[10]).equals("null") ? "" : String.valueOf(record[10]));
					documentDetailsResponse.setFolderIndexNo(String.valueOf(record[11]).equals("null") ? "" : String.valueOf(record[11]));
					
					
					documentDetailsResponseList.add(documentDetailsResponse);
					
					log.info("Values" + documentDetailsResponse);
				}
			}
		} catch (InputMismatchException e) {
			log.error("Error occurred while getting Benefit Details");
		}
		return documentDetailsResponseList;
	}
}
