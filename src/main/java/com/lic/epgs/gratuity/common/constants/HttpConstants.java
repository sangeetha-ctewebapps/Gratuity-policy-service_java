package com.lic.epgs.gratuity.common.constants;

/**
 * @author Gopi
 *
 */
public interface HttpConstants {
	public static final String OK = "200";
	public static final String OK_MESSAGE = "success";

	public static final String CREATED = "201";
	public static final String CREATED_MESSAGE = "success";

	public static final String NOT_FOUND = "404";
	public static final String NOT_FOUND_MESSAGE = "success";

	public static final String BAD_REQUEST = "400";
	public static final String BAD_REQUEST_MESSAGE = "badrequest";

	public static final String SERVER_EXCEPTION = "500";
	public static final String SERVER_EXCEPTION_MESSAGE = "exception";

	public static final String NON_AUTHORITATIVE = "203";
	public static final String NON_AUTHORITATIVE_MESSAGE = "exception";
	public static final String DATA = "DATA";
	public static final String MESSAGE = "message";

	//// ---------- For Member Upload Starts ----------

	// Spark Constant
	public String SPARK_DRIVER = "driver";
	public String SPARK_USER = "user";
	public String SPARK_PASSWORD = "password";

	public String APPROVED = "Approved";
	public String REJECT = "Reject";

	public String STATUS = "Success";
	public String FETCH = "Record found";

	String LIC_SEQ = "LIC";
	public static String POLICY_MODULE = "POLICY";
	public static String ONBORDING_MODULE = "CLAIM_OB";
	public static String PAYOUT = "PAYOUT";

	public static String EMPTY_STRING = "";
	public static String MID_LEAVER_SERVICE_NO = "ML-SERVICE-NUMBER";
	public static String CLAIMINTI = "CLAIM_INTI";
	public static String CHALLEN_NO = "CHALLAN NUMBER";
	public static String CONTRIBUTION_ADJUSTMENT = "CONTRIBUTION_ADJ";
	public static String PREM_COLLECTION = "LIFE COVER PREMIUM ADJUSTMENT";
	public static String MERGER = "MERGER";
	public static String FREE_LOOK_CANCELLATION = "FREE LOOK CANCELLATION";
	public static Long CONTRIBUTIONFREQUENCY_ANNUALID = 9L;

	public String newBusinessQuarter = "Q0";

	//// ---------- For Member Upload Starts ----------
}
