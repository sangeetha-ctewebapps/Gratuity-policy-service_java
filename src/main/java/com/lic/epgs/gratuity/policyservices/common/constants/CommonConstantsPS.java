package com.lic.epgs.gratuity.policyservices.common.constants;

import java.util.Arrays;
import java.util.List;


public interface CommonConstantsPS {
	String ERROR = "Error";
	String FETCH_MESSAGE = "Fetched_Message";
	

	String DRAFT = "Draft";
	String PENDINGFORAPPROVAL = "Pending For Approval";
	String PENDINGWITHMODIFICATION = "Pending With Modification";
	
	String ONHOLD = "On Hold";
	String APPROVED = "Approved";
	String REJECTED = "Rejected";
	String PENDINGFORAPPROVER = "Pending For Approver";
	String CLOSE = "Close";
	String REOPEN = "Reopen";
	String INACTIVE = "InActive";
	String DEFUNCT = "Defunct";
	String PENDINGFORACTUARY = "Pending For Actuary";
	String ACTUARYPROCESSCOMPLETED = "Actuary Process Completed";
	String CHANGEDRAFT = "Draft";
	String CHANGEPENDINGFORAPPROVAL = "Pending For Approval";
	
	String CHANGEPENDINGWITHMODIFICATION = "Change Pending With Modification";
	

	String DRAFT_NO = "1";
	String PENDINGFORAPPROVAL_NO = "2";
	String PENDINGWITHMODIFICATION_NO = "3";
	String ONHOLD_NO = "3";
	String APPROVED_NO = "4";
	String REJECTED_NO = "5";
	String PENDINGFORAPPROVER_NO = "6";
	String CLOSE_NO = "7";
	String REOPEN_NO = "8";
	String INACTIVE_NO = "9";
	String DEFUNCT_NO = "10";
	String PENDINGFORACTUARY_NO = "11";
	String ACTUARYPROCESSCOMPLETED_NO = "12";
	String CHANGEDRAFT_NO = "13";
	String CHANGEPENDINGFORAPPROVAL_NO = "14";
	String CHANGEPENDINGWITHMODIFICATION_NO = "15";
	
	
	/**
	 *   serviceType 
	 **/
	
	String POLICY_CONVERSION_STATUS = "Policy Converted";
	String POLICY_MERGER_STATUS = "Policy Merged";
	String FREELOOK_CANCEL_STATUS = "Cancelled – FLC";
	String POLICY_DETAILS_CHANGE_STATUS = "Policy-Details Changed";
	String MEMBER_ADDITION_STATUS = "Member Added";
	String MEMBER_TRASFER_IN_OUT_STATUS = "Member Transferred";
	
	String POLICY_CONVERSION_STATUS_NO = "16";
	String POLICY_MERGER_STATUS_NO = "17";
	String FREELOOK_CANCEL_STATUS_NO = "18";
	String POLICY_DETAILS_CHANGE_STATUS_NO = "19";
	String MEMBER_ADDITION_STATUS_NO = "20";
	String MEMBER_TRASFER_IN_OUT_STATUS_NO = "21";
	
	/**
	 *  serviceStatus 
	 **/
	
	String SERVICE_ACTIVE = "Service - Active";
	String SERVICE_INACTIVE = "Service - InActive";
	
	String SERVICE_ACTIVE_NO = "22";
	String SERVICE_INACTIVE_NO = "23";
	
	
	String MAKER = "Maker";
	String CHECKER = "Checker";
	
//	Service Status
	String ACTIVE = "Active";
	String IN_ACTIVE = "In_Active";

	String ACTIVE_NO = "111";
	String IN_ACTIVE_NO = "222";
	
	String POLICY_CONVERSION_PREFIX = "SRVPC";
	String POLICY_MERGER_PREFIX = "SRVPM";
	String FREELOOK_CANCEL_PREFIX = "SRVFLC";
	String POLICY_DETAILS_CHANGE_PREFIX = "SRVPDC";
	String MEMBER_ADDITION_PREFIX = "SRVMOA";
	String MEMBER_TRASFER_IN_OUT_PREFIX = "SRVMTIO";
	String CLAIM_PREFIX = "SRVC";

//	Service Type
	String POLICY_CONVERSION_TYPE = "POLICYCONVERSION";
	String POLICY_MERGER_TYPE = "POLICYMERGER";
	String FREELOOK_CANCEL_TYPE = "FREELOOKCANCEL";
	String POLICY_DETAILS_CHANGE_TYPE = "POLICYDETAILSCHANGE";
	String MEMBER_ADDITION_TYPE = "MEMBEROFADDITION";
	String MEMBER_TRASFER_IN_OUT_TYPE = "MEMBERTRASFERINOUT";
	String CLAIM_TYPE = "CLAIMSRV";
	
	String ADJESTED = "Adjested";

	String SAVEMESSAGE = "Saved Successfully";
	String UPDATEMESSAGE = "Updated Successfully";
	String REMOVEDMESSAGE = "Removed Successfully";

	String RECORD_FOUND = "Record found";
	String NO_RECORD_FOUND = "No Record found";

	String SUCCESS = "SUCCESS";
	String FAIL = "FAIL";
	//String ERROR = "Error";
	String EXCEPTION = "exception occurs";
	String INVALIDREQUEST = "Invalid Request";
	String OK = "ok";

	String ZERO_STRING = "0";
	String ONE_STRING = "1";

	String EXISTING = "existing";
	String INPROGRESS = "inprogress";
	String CREATED = "CREATED";
	String AUTHORIZATION = "Authorization";
	String CORELATIONID = "corelationid";
	String BUSINESSCORELATIONID = "businesscorelationid";

	String DEPOSIT_NOT_EXITED = "Deposit Not Exited";

	String ACC_DEPO_TRNS_LOCK_MSG = "Records locked successfully";
	String ACC_DEPO_TRNS_UNLOCK_MSG = "Records unlocked successfully";

	String POLICY_BANK_ID_NOT_FOUND = "Policy Bank details ID not found";
	String POLICY_BANK_ID_EMPTY = "Policy Bank details ID is Empty";

	String QUOTATION_NUMBER_EMPTY = "Quotation Number shound not be Empty";
	String POLICY_NUMBER_EMPTY = "Policy Number shound not be Empty";
	String POLICY_INVALID = "Invalid Policy Number";
	String QUOTATION_INVALID = "Invalid Quatation Number";
	String DEPOSIT_INVALID = "Invalid Deposit Number";

	String INVALID_LOGIN = "Invalid Login User";
	String INVALID_CONVERSIONID = "Invalid ConversionId";
	String INVALID_MERGER = "Invalid Merger";
	String ALREADY_CUSTOMER = "Already Existing Customer Name";

	String STATUS_PAYMENT_COMPLETED = "PAYMENT COMPLETED";
	String SUB_STATUS_ENRICH_PENDING = "ENRICH PENDING";
	
	String SUCCESS_RETRIVE_MSG = "Datas Retrived Successfully";
	String FAILURE_RETRIVE_MSG = "Datas Retrived Unsuccessfully";

	String IS_LATEST = "Y";

	public List<Integer> INPROGRESS_LOAD_MAKER_LIST = Arrays.asList(1, 3);
	
	public List<Integer> EXISTING_LOAD_MAKER_LIST = Arrays.asList(4,5,16,17,18);
	
	
	public List<Integer> INPROGRESS_LOAD_CHECKER_LIST = Arrays.asList(2);
	
	public List<Integer> EXISTING_LOAD_CHECKER_LIST = Arrays.asList(4,5,16,17,18);


	public List<Integer> EXISTING_POLICY_LOAD_LIST_NEWPAGE = Arrays.asList(4);
	

	public List<Integer> INPROGRESS_QUOTATION_LOAD_MAKER_LIST = Arrays.asList(1, 3, 8);
	
	public List<Integer> EXISTING_QUOTATION_LOAD_MAKER_LIST = Arrays.asList(4,5);
	
	
	

	
	/**	transfer in and out start **/
	
	public static final String TRANSFER_OUT_DRAFT_STATUS = "1";
	public static final String TRANSFER_OUT_PENDING_FOR_APPROVER_STATUS = "2";
	public static final String TRANSFER_OUT_PENDING_FOR_MODIFICATION_STATUS = "3";
	public static final String TRANSFER_OUT_APPROVED_STATUS = "4";
	public static final String TRANSFER_OUT_REJECTED_STATUS = "5";
	public static final String TRANSFER_IN_DRAFT_STATUS = "21";
	public static final String TRANSFER_IN_PENDING_FOR_APPROVER_STATUS = "22";
	public static final String TRANSFER_IN_DRAFT_DES_STATUS = "19";
	public static final String TRANSFER_IN_PENDING_FOR_APPROVER_DES_STATUS = "20";
	public static final String POLICY_MEMBER_TRANSFERED_APPROVED_STATUS= "23";
	public static final String POLICY_MEMBER_TRANSFERED_REJECTED_STATUS= "24";
	
	
	public static final String INVALID_MEMBER = "Invalid Member Details";
	public static final String INVALID_QUATATION_TYPE = "Only Allowed  Define Contribution(DC) Policy ";
	public static final String INVALID_CATEGORY = "Invalid Category";
	public static final String TRANSFER_OUT_PENDING_APPROVAL =" has been send to Checker for Transfer Out Approval.";
	public static final String TRANSFER_OUT_ON_HOLD =" has been send to Maker for Transfer-Out Process.";
	public static final String TRANSFER_OUT_APPROVED =" Transfer-Out Approved.";
	public static final String TRANSFER_OUT_REJECTED =" Transfer-Out Rejected.";
	public static final String TRANSFER_IN_PENDING_APPROVAL =" has been send to Checker for Transfer-In Approval.";
	public static final String TRANSFER_IN_APPROVED =" Transfer-In Approved.";
	public static final String TRANSFER_IN_REJECTED =" Transfer-In Rejected.";
	
	public static final String INVALID_SRC_POLICY = "Invalid Source Policy";
	public static final String INVALID_DES_POLICY =  "Invalid Destination Policy";

	public static List<String> inprogressChecker() {
		return Arrays.asList("2","20","21","22");
	}
	public static List<String> inprogressMaker() {
		return Arrays.asList("1","3","19","21","22");
	}
	
	public static List<String> existingChecker() {
		return Arrays.asList("4","5");
	}

	/**	transfer in and out end **/

	
	String VAlIDATE_POLICY = "PolicyNumber Should Not be Same";
	String VAlIDATE_POLICY_TYPE = "PolicyType Should be Same";
	String NO_RESULT = "No_Data";
	

}