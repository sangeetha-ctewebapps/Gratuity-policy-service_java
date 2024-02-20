package com.lic.epgs.gratuity.policyservices.conversion.constant;

import java.util.Arrays;
import java.util.List;

public class PolicyLevelConversionConstants {
	
	public static final String STATUS_PAYMENT_COMPLETED = "PAYMENT COMPLETED";
	public static final String SUB_STATUS_ENRICH_PENDING = "ENRICH PENDING";
	public static final String SUCCESS_RETRIVE_MSG = "Datas Retrived Successfully";
	public static final String FAILURE_RETRIVE_MSG = "Datas Retrived Unsuccessfully";
	
	public static final String IS_LATEST = "Y";
	public static final String ACTIVE = "4";
	public static final String INACTIVE = "INACTIVE";
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILURE = "FAILURE";
	public static final String ERROR = "ERROR";
	public static final String SAVE = "Saved Successfully";
	public static final String UPDATE = "Updated Successfully";
	public static final String INVALID_REQUEST = "Invalid Request";
	public static final String RETRIVED = "Retrived Successfully";
	
	public static final String  DRAFT = "1";
	public static final String CREATEDBY="1";
	public static final String MODIFIEDBY="2";
    public static final String REJECT = "5";
    public static final String SEND_FOR_APPROVAL = "2";
	public static final String SEND_TO_MAKER = "3";
	public static final String NOT_EXIT_DETAILS = "Details Not Existed";
	public static final String INVALID_CONVERSION = "Invalid ConversionId";
	public static final String MAKER = "MAKER";
	public static final String CHECKER = "CHECKER";
	public static final String APPROVED = "4";
	public static final String REJECTED = "5";
	public static final String ON_HOLDING = "3";
	public static final String DELETED="Data_Deleted";
	public static final String FAIL="FAIL";
	public static final String MESSEGE="success";
	public static final String ZERO_STRING = "0";
	public static final String ONE_STRING = "1";
	public static final String FETCH_MESSAGE="Fetched successfully";
	public static final String NO_RECORD_FOUND="No Record Found";
	public static final String INVALID_LOGIN = "Invalid Login User";
	public static final String ALREADY_CUSTOMER = "Already Existing Customer Name";
	public static final String POLICY_REJECTED = "Policy Conversion Rejected";
	public static final String POLICY_SEND_TO_MAKER = "Policy Conversion Send To Maker";
	public static final String INVALID_POLICYNO = "Invalid PolicyNo";
	public static final String POLICY_CONVERTED = "16";
	public static final String POLICY_VARIANT_DB = "37";
	public static final String POLICY_VARIANT_DC = "36";
	public static final String CONVERSION_STATUS = "conversionStatus";
	public static final String CONVERSION_APPROVED = "Approved";
	
	public static final String VARIANT = "PolicyVariant V3 is Not allowed";
	public static final Boolean TRUE = true;
	
	public static final String GetPolicyConversionDetailsDataTable = "getPolicyConversionDetailsDataTable";
	
	public static List<Integer> inProgressPolicyConversionChecker() {
		return Arrays.asList(2);
	}

	public static List<Integer> inProgressPolicyConversionMaker() {
		return Arrays.asList(1, 3);
	}
	
	public static List<Integer> existingDetails() {
		return Arrays.asList(4, 5);
	}
	
	

}