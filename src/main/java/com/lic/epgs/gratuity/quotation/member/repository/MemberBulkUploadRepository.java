package com.lic.epgs.gratuity.quotation.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.quotation.member.entity.MemberBulkEntity;

public interface MemberBulkUploadRepository extends JpaRepository<MemberBulkEntity, Long> {

	@Query(value = "SELECT * FROM QSTG_MEMBER_BULK WHERE QUOTATION_ID=?1 and IS_ACTIVE=?2", nativeQuery = true)
	List<Object> findbyTempQuotationId(Integer quotationId, Boolean isActive);

	@Modifying
	@Query(value = "INSERT INTO QSTG_MEMBER qm (MEMBER_ID, STATUS_ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, AADHAR_NUMBER, BASIC_SALARY, CATEGORY_ID, EMPLOYEE_CODE, "
			+ "LANDLINE_NO, CREATED_BY, CREATED_DATE, DATE_OF_BIRTH, DATE_OF_APPOINTMENT, EMAIL_ID, GENDER_ID, IS_ACTIVE, LIC_ID, PROPOSAL_POLICY_NUMBER, "
			+ "DOJ_TO_SCHEME, SALARY_FREQUENCY, MEMBER_BATCH_ID, PAN_NUMBER, QUOTATION_ID) "
			+ "SELECT QSTG_MEMBER_ID_SEQ.nextval, STATUS_ID , FIRST_NAME, MIDDLE_NAME, LAST_NAME, AADHAR_NUMBER, BASIC_SALARY, CATEGORY_ID, EMPLOYEE_CODE, "
			+ "CONTACT_NUMBER, CREATED_BY, CREATED_DATE, DATE_OF_BIRTH, DATE_OF_APPOINTMENT, EMAIL_ID, GENDER_ID, IS_ACTIVE, LIC_ID, PROPOSAL_POLICY_NUMBER, "
			+ "DOJ_TO_SCHEME, SALARY_FREQUENCY, MEMBER_BATCH_ID, PAN_NUMBER, QUOTATION_ID  "
			+ "FROM (SELECT 146 STATUS_ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, AADHAR_NUMBER, BASIC_SALARY, CATEGORY_ID, EMPLOYEE_CODE, "
			+ "CONTACT_NUMBER, ?3 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE, DATE_OF_BIRTH, DATE_OF_APPOINTMENT, EMAIL_ID, GENDER_ID, IS_ACTIVE, LIC_ID, PROPOSAL_POLICY_NUMBER, "
			+ "DOJ_TO_SCHEME, SALARY_FREQUENCY, MEMBER_BATCH_ID, PAN_NUMBER, ?2 QUOTATION_ID "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "WHERE RECORD_STATUS = 'G' AND IS_ACTIVE = 1 AND MEMBER_BATCH_ID = ?1 ORDER BY EMPLOYEE_CODE)", nativeQuery = true)
	void bulkInsertMembers(Long memberBatchId, Long quotationId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO QSTG_MEMBER_ADDRESS qma (MEMBER_ADDRESS_ID, MEMBER_ID, ADDRESS_TYPE_ID, PIN_CODE, COUNTRY_ID, STATE_ID, "
			+ "DISTRICT, CITY, CONTACT_NUMBER, ADDRESS1, ADDRESS2, ADDRESS3, "
			+ "IS_PRINTABLE, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT QSTG_MEMBER_ADDRESS_ID_SEQ.nextval, MEMBER_ID, ADDRESS_TYPE_ID, PIN_CODE, COUNTRY_ID, STATE_ID, DISTRICT, CITY, "
			+ "CONTACT_NUMBER, ADDRESS1, ADDRESS2, ADDRESS3, IS_PRINTABLE, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, qmbs.ADDRESS1_ADDRESS_TYPE_ID ADDRESS_TYPE_ID, qmbs.ADDRESS1_PIN_CODE PIN_CODE, qmbs.ADDRESS1_COUNTRY_ID COUNTRY_ID, "
			+ "qmbs.ADDRESS1_STATE_ID STATE_ID, qmbs.ADDRESS1_DISTRICT DISTRICT, qmbs.ADDRESS1_CITY CITY, qmbs.ADDRESS1_CONTACT_NUMBER CONTACT_NUMBER, "
			+ "qmbs.ADDRESS1_ADDRESS1 ADDRESS1, qmbs.ADDRESS1_ADDRESS2 ADDRESS2, qmbs.ADDRESS1_ADDRESS3 ADDRESS3, "
			+ "qmbs.ADDRESS1_PRINTABLE IS_PRINTABLE, qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN QSTG_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.ADDRESS1_ADDRESS_TYPE IS NOT NULL "
			+ "ORDER BY qmbs.EMPLOYEE_CODE)", nativeQuery = true)
	void bulkInsertMemberAddress1(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO QSTG_MEMBER_ADDRESS qma (MEMBER_ADDRESS_ID, MEMBER_ID, ADDRESS_TYPE_ID, PIN_CODE, COUNTRY_ID, STATE_ID, "
			+ "DISTRICT, CITY, CONTACT_NUMBER, ADDRESS1, ADDRESS2, ADDRESS3, "
			+ "IS_PRINTABLE, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT QSTG_MEMBER_ADDRESS_ID_SEQ.nextval, MEMBER_ID, ADDRESS_TYPE_ID, PIN_CODE, COUNTRY_ID, STATE_ID, DISTRICT, CITY, "
			+ "CONTACT_NUMBER, ADDRESS1, ADDRESS2, ADDRESS3, IS_PRINTABLE, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, qmbs.ADDRESS2_ADDRESS_TYPE_ID ADDRESS_TYPE_ID, qmbs.ADDRESS2_PIN_CODE PIN_CODE, qmbs.ADDRESS2_COUNTRY_ID COUNTRY_ID, "
			+ "qmbs.ADDRESS2_STATE_ID STATE_ID, qmbs.ADDRESS2_DISTRICT DISTRICT, qmbs.ADDRESS2_CITY CITY, qmbs.ADDRESS2_CONTACT_NUMBER CONTACT_NUMBER, "
			+ "qmbs.ADDRESS2_ADDRESS1 ADDRESS1, qmbs.ADDRESS2_ADDRESS2 ADDRESS2, qmbs.ADDRESS2_ADDRESS3 ADDRESS3, "
			+ "qmbs.ADDRESS2_PRINTABLE IS_PRINTABLE, qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN QSTG_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.ADDRESS2_ADDRESS_TYPE IS NOT NULL "
			+ "ORDER BY qmbs.EMPLOYEE_CODE)", nativeQuery = true)
	void bulkInsertMemberAddress2(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO QSTG_MEMBER_ADDRESS qma (MEMBER_ADDRESS_ID, MEMBER_ID, ADDRESS_TYPE_ID, PIN_CODE, COUNTRY_ID, STATE_ID, "
			+ "DISTRICT, CITY, CONTACT_NUMBER, ADDRESS1, ADDRESS2, ADDRESS3, "
			+ "IS_PRINTABLE, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT QSTG_MEMBER_ADDRESS_ID_SEQ.nextval, MEMBER_ID, ADDRESS_TYPE_ID, PIN_CODE, COUNTRY_ID, STATE_ID, DISTRICT, CITY, "
			+ "CONTACT_NUMBER, ADDRESS1, ADDRESS2, ADDRESS3, IS_PRINTABLE, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, qmbs.ADDRESS3_ADDRESS_TYPE_ID ADDRESS_TYPE_ID, qmbs.ADDRESS3_PIN_CODE PIN_CODE, qmbs.ADDRESS3_COUNTRY_ID COUNTRY_ID, "
			+ "qmbs.ADDRESS3_STATE_ID STATE_ID, qmbs.ADDRESS3_DISTRICT DISTRICT, qmbs.ADDRESS3_CITY CITY, qmbs.ADDRESS3_CONTACT_NUMBER CONTACT_NUMBER, "
			+ "qmbs.ADDRESS3_ADDRESS1 ADDRESS1, qmbs.ADDRESS3_ADDRESS2 ADDRESS2, qmbs.ADDRESS3_ADDRESS3 ADDRESS3, "
			+ "qmbs.ADDRESS3_PRINTABLE IS_PRINTABLE, qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN QSTG_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.ADDRESS3_ADDRESS_TYPE IS NOT NULL "
			+ "ORDER BY qmbs.EMPLOYEE_CODE)", nativeQuery = true)
	void bulkInsertMemberAddress3(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO QSTG_MEMBER_BANK_ACCOUNT qmba (MEMBER_BANK_ACCOUNT_ID, MEMBER_ID, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE_ID, IFSC_CODE, "
			+ "BANK_NAME_ID, BANK_BRANCH_ID, BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT QSTG_MEMBER_BANK_ACC_ID_SEQ.nextval, MEMBER_ID, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE_ID, IFSC_CODE, "
			+ "BANK_NAME_ID, BANK_BRANCH_ID, BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, qmbs.BANK1_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, qmbs.BANK1_ACCOUNT_TYPE_ID ACCOUNT_TYPE_ID, "
			+ "qmbs.BANK1_IFCS_CODE IFSC_CODE, qmbs.BANK1_BANK_NAME_ID BANK_NAME_ID, qmbs.BANK1_BANK_BRANCH_ID BANK_BRANCH_ID, "
			+ "qmbs.BANK1_BANK_NAME BANK_NAME, qmbs.BANK1_BANK_BRANCH BANK_BRANCH, qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN QSTG_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.BANK1_BANK_ACCOUNT_NUMBER IS NOT NULL)", nativeQuery = true)
	void bulkInsertMemberBank1(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO QSTG_MEMBER_BANK_ACCOUNT qmba (MEMBER_BANK_ACCOUNT_ID, MEMBER_ID, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE_ID, IFSC_CODE, "
			+ "BANK_NAME_ID, BANK_BRANCH_ID, BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT QSTG_MEMBER_BANK_ACC_ID_SEQ.nextval, MEMBER_ID, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE_ID, IFSC_CODE, "
			+ "BANK_NAME_ID, BANK_BRANCH_ID, BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, qmbs.BANK2_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, qmbs.BANK2_ACCOUNT_TYPE_ID ACCOUNT_TYPE_ID, "
			+ "qmbs.BANK2_IFCS_CODE IFSC_CODE, qmbs.BANK2_BANK_NAME_ID BANK_NAME_ID, qmbs.BANK2_BANK_BRANCH_ID BANK_BRANCH_ID, "
			+ "qmbs.BANK2_BANK_NAME BANK_NAME, qmbs.BANK2_BANK_BRANCH BANK_BRANCH, qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN QSTG_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.BANK2_BANK_ACCOUNT_NUMBER IS NOT NULL)", nativeQuery = true)
	void bulkInsertMemberBank2(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO QSTG_MEMBER_BANK_ACCOUNT qmba (MEMBER_BANK_ACCOUNT_ID, MEMBER_ID, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE_ID, IFSC_CODE, "
			+ "BANK_NAME_ID, BANK_BRANCH_ID, BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT QSTG_MEMBER_BANK_ACC_ID_SEQ.nextval, MEMBER_ID, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE_ID, IFSC_CODE, "
			+ "BANK_NAME_ID, BANK_BRANCH_ID, BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, qmbs.BANK3_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, qmbs.BANK3_ACCOUNT_TYPE_ID ACCOUNT_TYPE_ID, "
			+ "qmbs.BANK3_IFCS_CODE IFSC_CODE, qmbs.BANK3_BANK_NAME_ID BANK_NAME_ID, qmbs.BANK3_BANK_BRANCH_ID BANK_BRANCH_ID, "
			+ "qmbs.BANK3_BANK_NAME BANK_NAME, qmbs.BANK3_BANK_BRANCH BANK_BRANCH, qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN QSTG_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.BANK3_BANK_ACCOUNT_NUMBER IS NOT NULL)", nativeQuery = true)
	void bulkInsertMemberBank3(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO QSTG_MEMBER_NOMINEE qmn (MEMBER_NOMINEE_ID, MEMBER_ID, CODE, NAME, RELATIONSHIP_ID, CONTACT_NUMBER, DATE_OF_BIRTH, "
			+ "PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, BANK_ACCOUNT_TYPE_ID, IFSC_CODE, BANK_NAME_ID, BANK_BRANCH_ID, BANK_NAME, BANK_BRANCH, "
			+ "PERCENTAGE, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT QSTG_MEMBER_NOMINEE_ID_SEQ.nextval, MEMBER_ID, CODE, NAME, RELATIONSHIP_ID, CONTACT_NUMBER, DATE_OF_BIRTH, "
			+ "PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, BANK_ACCOUNT_TYPE_ID, IFSC_CODE, BANK_NAME_ID, BANK_BRANCH_ID, BANK_NAME, BANK_BRANCH, "
			+ "PERCENTAGE, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, NOMINEE1_CODE CODE, NOMINEE1_NAME NAME, NOMINEE1_RELATION_SHIP_ID RELATIONSHIP_ID, NOMINEE1_CONTACT_NUMBER CONTACT_NUMBER, "
			+ "NOMINEE1_DATE_OF_BIRTH DATE_OF_BIRTH, NOMINEE1_PAN_NUMBER PAN_NUMBER, NOMINEE1_AADHAR_NUMBER AADHAR_NUMBER, NOMINEE1_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, "
			+ "NOMINEE1_BANK_ACCOUNT_TYPE_ID BANK_ACCOUNT_TYPE_ID, NOMINEE1_IFCS_CODE IFSC_CODE, NOMINEE1_BANK_NAME_ID BANK_NAME_ID, NOMINEE1_BANK_BRANCH_ID BANK_BRANCH_ID, "
			+ "NOMINEE1_BANK_NAME BANK_NAME, NOMINEE1_BANK_BRANCH BANK_BRANCH, NOMINEE1_PERCENTAGE PERCENTAGE, qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN QSTG_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.NOMINEE1_NAME IS NOT NULL)", nativeQuery = true)
	void bulkInsertMemberNominee1(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO QSTG_MEMBER_NOMINEE qmn (MEMBER_NOMINEE_ID, MEMBER_ID, CODE, NAME, RELATIONSHIP_ID, CONTACT_NUMBER, DATE_OF_BIRTH, "
			+ "PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, BANK_ACCOUNT_TYPE_ID, IFSC_CODE, BANK_NAME_ID, BANK_BRANCH_ID, BANK_NAME, BANK_BRANCH, "
			+ "PERCENTAGE, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT QSTG_MEMBER_NOMINEE_ID_SEQ.nextval, MEMBER_ID, CODE, NAME, RELATIONSHIP_ID, CONTACT_NUMBER, DATE_OF_BIRTH, "
			+ "PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, BANK_ACCOUNT_TYPE_ID, IFSC_CODE, BANK_NAME_ID, BANK_BRANCH_ID, BANK_NAME, BANK_BRANCH, "
			+ "PERCENTAGE, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, NOMINEE2_CODE CODE, NOMINEE2_NAME NAME, NOMINEE2_RELATION_SHIP_ID RELATIONSHIP_ID, NOMINEE2_CONTACT_NUMBER CONTACT_NUMBER, "
			+ "NOMINEE2_DATE_OF_BIRTH DATE_OF_BIRTH, NOMINEE2_PAN_NUMBER PAN_NUMBER, NOMINEE2_AADHAR_NUMBER AADHAR_NUMBER, NOMINEE2_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, "
			+ "NOMINEE2_BANK_ACCOUNT_TYPE_ID BANK_ACCOUNT_TYPE_ID, NOMINEE2_IFCS_CODE IFSC_CODE, NOMINEE2_BANK_NAME_ID BANK_NAME_ID, NOMINEE2_BANK_BRANCH_ID BANK_BRANCH_ID, "
			+ "NOMINEE2_BANK_NAME BANK_NAME, NOMINEE2_BANK_BRANCH BANK_BRANCH, NOMINEE2_PERCENTAGE PERCENTAGE, qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN QSTG_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.NOMINEE2_NAME IS NOT NULL)", nativeQuery = true)
	void bulkInsertMemberNominee2(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO QSTG_MEMBER_NOMINEE qmn (MEMBER_NOMINEE_ID, MEMBER_ID, CODE, NAME, RELATIONSHIP_ID, CONTACT_NUMBER, DATE_OF_BIRTH, "
			+ "PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, BANK_ACCOUNT_TYPE_ID, IFSC_CODE, BANK_NAME_ID, BANK_BRANCH_ID, BANK_NAME, BANK_BRANCH, "
			+ "PERCENTAGE, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT QSTG_MEMBER_NOMINEE_ID_SEQ.nextval, MEMBER_ID, CODE, NAME, RELATIONSHIP_ID, CONTACT_NUMBER, DATE_OF_BIRTH, "
			+ "PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, BANK_ACCOUNT_TYPE_ID, IFSC_CODE, BANK_NAME_ID, BANK_BRANCH_ID, BANK_NAME, BANK_BRANCH, "
			+ "PERCENTAGE, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, NOMINEE3_CODE CODE, NOMINEE3_NAME NAME, NOMINEE3_RELATION_SHIP_ID RELATIONSHIP_ID, NOMINEE3_CONTACT_NUMBER CONTACT_NUMBER, "
			+ "NOMINEE3_DATE_OF_BIRTH DATE_OF_BIRTH, NOMINEE3_PAN_NUMBER PAN_NUMBER, NOMINEE3_AADHAR_NUMBER AADHAR_NUMBER, NOMINEE3_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, "
			+ "NOMINEE3_BANK_ACCOUNT_TYPE_ID BANK_ACCOUNT_TYPE_ID, NOMINEE3_IFCS_CODE IFSC_CODE, NOMINEE3_BANK_NAME_ID BANK_NAME_ID, NOMINEE3_BANK_BRANCH_ID BANK_BRANCH_ID, "
			+ "NOMINEE3_BANK_NAME BANK_NAME, NOMINEE3_BANK_BRANCH BANK_BRANCH, NOMINEE3_PERCENTAGE PERCENTAGE, qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN QSTG_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.NOMINEE3_NAME IS NOT NULL)", nativeQuery = true)
	void bulkInsertMemberNominee3(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO QSTG_MEMBER_APPOINTEE qma (MEMBER_APPOINTEE_ID, MEMBER_ID, MEMBER_NOMINEE_ID, CODE, NAME, RELATION_SHIP_ID, CONTACT_NUMBER, "
			+ "DATE_OF_BIRTH, PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE_ID, IFSC_CODE, BANK_NAME_ID, BANK_BRANCH_ID, "
			+ "BANK_NAME, BANK_BRANCH,IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT QSTG_MEMBER_APPOINTEE_ID_SEQ.nextval, MEMBER_ID, MEMBER_NOMINEE_ID, CODE, NAME, RELATION_SHIP_ID, CONTACT_NUMBER, "
			+ "DATE_OF_BIRTH, PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE_ID, IFSC_CODE, BANK_NAME_ID, BANK_BRANCH_ID, "
			+ "BANK_NAME, BANK_BRANCH,IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID MEMBER_ID, qmn.MEMBER_NOMINEE_ID MEMBER_NOMINEE_ID, qmbs.APPOINTEE1_CODE CODE, qmbs.APPOINTEE1_NAME NAME, "
			+ "qmbs.APPOINTEE1_RELATION_SHIP_ID RELATION_SHIP_ID, qmbs.APPOINTEE1_CONTACT_NUMBER CONTACT_NUMBER, qmbs.APPOINTEE1_DATE_OF_BIRTH DATE_OF_BIRTH, "
			+ "qmbs.APPOINTEE1_PAN_NUMBER PAN_NUMBER, qmbs.APPOINTEE1_AADHAR_NUMBER AADHAR_NUMBER, qmbs.APPOINTEE1_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, "
			+ "qmbs.APPOINTEE1_ACCOUNT_TYPE_ID ACCOUNT_TYPE_ID, qmbs.APPOINTEE1_IFCS_CODE IFSC_CODE, qmbs.APPOINTEE1_BANK_NAME_ID BANK_NAME_ID, "
			+ "qmbs.APPOINTEE1_BANK_BRANCH_ID BANK_BRANCH_ID, qmbs.APPOINTEE1_BANK_NAME BANK_NAME, qmbs.APPOINTEE1_BANK_BRANCH BANK_BRANCH, "
			+ "qmbs.IS_ACTIVE IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN QSTG_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "JOIN QSTG_MEMBER_NOMINEE qmn ON qmn.MEMBER_ID = qm.MEMBER_ID "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmn.NAME = qmbs.APPOINTEE1_MEMBER_NOMINEE AND  qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.APPOINTEE1_CODE IS NOT NULL "
			+ "AND qmbs.APPOINTEE1_MEMBER_NOMINEE IS NOT NULL)", nativeQuery = true)
	void bulkInsertMemberAppointee1(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO QSTG_MEMBER_APPOINTEE qma (MEMBER_APPOINTEE_ID, MEMBER_ID, MEMBER_NOMINEE_ID, CODE, NAME, RELATION_SHIP_ID, CONTACT_NUMBER, "
			+ "DATE_OF_BIRTH, PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE_ID, IFSC_CODE, BANK_NAME_ID, BANK_BRANCH_ID, "
			+ "BANK_NAME, BANK_BRANCH,IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT QSTG_MEMBER_APPOINTEE_ID_SEQ.nextval, MEMBER_ID, MEMBER_NOMINEE_ID, CODE, NAME, RELATION_SHIP_ID, CONTACT_NUMBER, "
			+ "DATE_OF_BIRTH, PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE_ID, IFSC_CODE, BANK_NAME_ID, BANK_BRANCH_ID, "
			+ "BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID MEMBER_ID, qmn.MEMBER_NOMINEE_ID MEMBER_NOMINEE_ID, qmbs.APPOINTEE2_CODE CODE, qmbs.APPOINTEE2_NAME NAME, "
			+ "qmbs.APPOINTEE2_RELATION_SHIP_ID RELATION_SHIP_ID, qmbs.APPOINTEE2_CONTACT_NUMBER CONTACT_NUMBER, qmbs.APPOINTEE2_DATE_OF_BIRTH DATE_OF_BIRTH, "
			+ "qmbs.APPOINTEE2_PAN_NUMBER PAN_NUMBER, qmbs.APPOINTEE2_AADHAR_NUMBER AADHAR_NUMBER, qmbs.APPOINTEE2_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, "
			+ "qmbs.APPOINTEE2_ACCOUNT_TYPE_ID ACCOUNT_TYPE_ID, qmbs.APPOINTEE2_IFCS_CODE IFSC_CODE, qmbs.APPOINTEE2_BANK_NAME_ID BANK_NAME_ID, "
			+ "qmbs.APPOINTEE2_BANK_BRANCH_ID BANK_BRANCH_ID, qmbs.APPOINTEE2_BANK_NAME BANK_NAME, qmbs.APPOINTEE2_BANK_BRANCH BANK_BRANCH, "
			+ "qmbs.IS_ACTIVE IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN QSTG_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "JOIN QSTG_MEMBER_NOMINEE qmn ON qmn.MEMBER_ID = qm.MEMBER_ID "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmn.NAME = qmbs.APPOINTEE2_MEMBER_NOMINEE AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.APPOINTEE2_CODE IS NOT NULL "
			+ "AND qmbs.APPOINTEE2_MEMBER_NOMINEE IS NOT NULL)", nativeQuery = true)
	void bulkInsertMemberAppointee2(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO QSTG_MEMBER_APPOINTEE qma (MEMBER_APPOINTEE_ID, MEMBER_ID, MEMBER_NOMINEE_ID, CODE, NAME, RELATION_SHIP_ID, CONTACT_NUMBER, "
			+ "DATE_OF_BIRTH, PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE_ID, IFSC_CODE, BANK_NAME_ID, BANK_BRANCH_ID, "
			+ "BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT QSTG_MEMBER_APPOINTEE_ID_SEQ.nextval, MEMBER_ID, MEMBER_NOMINEE_ID, CODE, NAME, RELATION_SHIP_ID, CONTACT_NUMBER, "
			+ "DATE_OF_BIRTH, PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE_ID, IFSC_CODE, BANK_NAME_ID, BANK_BRANCH_ID, "
			+ "BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID MEMBER_ID, qmn.MEMBER_NOMINEE_ID MEMBER_NOMINEE_ID, qmbs.APPOINTEE3_CODE CODE, qmbs.APPOINTEE3_NAME NAME, "
			+ "qmbs.APPOINTEE3_RELATION_SHIP_ID RELATION_SHIP_ID, qmbs.APPOINTEE3_CONTACT_NUMBER CONTACT_NUMBER, qmbs.APPOINTEE3_DATE_OF_BIRTH DATE_OF_BIRTH, "
			+ "qmbs.APPOINTEE3_PAN_NUMBER PAN_NUMBER, qmbs.APPOINTEE3_AADHAR_NUMBER AADHAR_NUMBER, qmbs.APPOINTEE3_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, "
			+ "qmbs.APPOINTEE3_ACCOUNT_TYPE_ID ACCOUNT_TYPE_ID, qmbs.APPOINTEE3_IFCS_CODE IFSC_CODE, qmbs.APPOINTEE3_BANK_NAME_ID BANK_NAME_ID, "
			+ "qmbs.APPOINTEE3_BANK_BRANCH_ID BANK_BRANCH_ID, qmbs.APPOINTEE3_BANK_NAME BANK_NAME, qmbs.APPOINTEE3_BANK_BRANCH BANK_BRANCH, "
			+ "qmbs.IS_ACTIVE IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN QSTG_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "JOIN QSTG_MEMBER_NOMINEE qmn ON qmn.MEMBER_ID = qm.MEMBER_ID "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmn.NAME = qmbs.APPOINTEE3_MEMBER_NOMINEE AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.APPOINTEE3_CODE IS NOT NULL "
			+ "AND qmbs.APPOINTEE3_MEMBER_NOMINEE IS NOT NULL)", nativeQuery = true)
	void bulkInsertMemberAppointee3(Long memberBatchId, String createdBy);

//AOM

	@Modifying
	@Query(value = "INSERT INTO PMST_TMP_MEMBER qm (MEMBER_ID, STATUS_ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, AADHAR_NUMBER, BASIC_SALARY, CATEGORY_ID, EMPLOYEE_CODE, "
			+ "LANDLINE_NO, CREATED_BY, CREATED_DATE, DATE_OF_BIRTH, DATE_OF_APPOINTMENT, EMAIL_ID, GENDER_ID, IS_ACTIVE, LIC_ID, PROPOSAL_POLICY_NUMBER, "
			+ "DOJ_TO_SCHEME, SALARY_FREQUENCY, MEMBER_BATCH_ID, PAN_NUMBER, TMP_POLICY_ID,NEW_LIFE ) "
			+ "SELECT PMST_MEMBER_ID_SEQ.nextval, STATUS_ID , FIRST_NAME, MIDDLE_NAME, LAST_NAME, AADHAR_NUMBER, BASIC_SALARY, CATEGORY_ID, EMPLOYEE_CODE, "
			+ "CONTACT_NUMBER, CREATED_BY, CREATED_DATE, DATE_OF_BIRTH, DATE_OF_APPOINTMENT, EMAIL_ID, GENDER_ID, IS_ACTIVE, LIC_ID, PROPOSAL_POLICY_NUMBER, "
			+ "DOJ_TO_SCHEME, SALARY_FREQUENCY, MEMBER_BATCH_ID, PAN_NUMBER, TMP_POLICY_ID ,'Y' AS NEW_LIFE "
			+ "FROM (SELECT 146 STATUS_ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, AADHAR_NUMBER, BASIC_SALARY, CATEGORY_ID, EMPLOYEE_CODE, "
			+ "CONTACT_NUMBER, ?3 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE, DATE_OF_BIRTH, DATE_OF_APPOINTMENT, EMAIL_ID, GENDER_ID, IS_ACTIVE, LIC_ID, PROPOSAL_POLICY_NUMBER, "
			+ "DOJ_TO_SCHEME, SALARY_FREQUENCY, MEMBER_BATCH_ID, PAN_NUMBER, ?2 TMP_POLICY_ID "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "WHERE RECORD_STATUS = 'G' AND IS_ACTIVE = 1 AND MEMBER_BATCH_ID = ?1 ORDER BY EMPLOYEE_CODE)", nativeQuery = true)
	void InsertBulkMembers(Long memberBatchId, Long tmpPolicyId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO PMST_TMP_MEMBER_ADDRESS qma (MEMBER_ADDRESS_ID, MEMBER_ID, ADDRESS_TYPE_ID, PIN_CODE, "
			+ "DISTRICT, CITY, ADDRESS1, ADDRESS2, ADDRESS3, " + "IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT PMST_TMP_MBR_ADDR_ID_SEQ.nextval, MEMBER_ID, ADDRESS_TYPE_ID, PIN_CODE, DISTRICT, CITY, "
			+ "ADDRESS1, ADDRESS2, ADDRESS3, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, qmbs.ADDRESS1_ADDRESS_TYPE_ID ADDRESS_TYPE_ID, qmbs.ADDRESS1_PIN_CODE PIN_CODE, "
			+ "qmbs.ADDRESS1_DISTRICT DISTRICT, qmbs.ADDRESS1_CITY CITY, "
			+ "qmbs.ADDRESS1_ADDRESS1 ADDRESS1, qmbs.ADDRESS1_ADDRESS2 ADDRESS2, qmbs.ADDRESS1_ADDRESS3 ADDRESS3, "
			+ "qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE " + "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN PMST_TMP_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.ADDRESS1_ADDRESS_TYPE IS NOT NULL "
			+ "ORDER BY qmbs.EMPLOYEE_CODE)", nativeQuery = true)
	void InsertBulkMemberAddress1(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO PMST_TMP_MEMBER_ADDRESS qma (MEMBER_ADDRESS_ID, MEMBER_ID, ADDRESS_TYPE_ID, PIN_CODE, "
			+ "DISTRICT, CITY, ADDRESS1, ADDRESS2, ADDRESS3, " + "IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT PMST_TMP_MBR_ADDR_ID_SEQ.nextval, MEMBER_ID, ADDRESS_TYPE_ID, PIN_CODE, " + "DISTRICT, CITY, "
			+ "ADDRESS1, ADDRESS2, ADDRESS3, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, qmbs.ADDRESS2_ADDRESS_TYPE_ID ADDRESS_TYPE_ID, qmbs.ADDRESS2_PIN_CODE PIN_CODE, "
			+ " qmbs.ADDRESS2_DISTRICT DISTRICT, qmbs.ADDRESS2_CITY CITY, "
			+ "qmbs.ADDRESS2_ADDRESS1 ADDRESS1, qmbs.ADDRESS2_ADDRESS2 ADDRESS2, qmbs.ADDRESS2_ADDRESS3 ADDRESS3, "
			+ "qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE " + "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN PMST_TMP_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.ADDRESS2_ADDRESS_TYPE IS NOT NULL "
			+ "ORDER BY qmbs.EMPLOYEE_CODE)", nativeQuery = true)
	void InsertBulkMemberAddress2(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO PMST_TMP_MEMBER_ADDRESS qma (MEMBER_ADDRESS_ID, MEMBER_ID, ADDRESS_TYPE_ID, PIN_CODE, "
			+ "DISTRICT, CITY, ADDRESS1, ADDRESS2, ADDRESS3, " + "IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT PMST_TMP_MBR_ADDR_ID_SEQ.nextval, MEMBER_ID, ADDRESS_TYPE_ID, PIN_CODE, DISTRICT, CITY, "
			+ "ADDRESS1, ADDRESS2, ADDRESS3, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, qmbs.ADDRESS3_ADDRESS_TYPE_ID ADDRESS_TYPE_ID, qmbs.ADDRESS3_PIN_CODE PIN_CODE, "
			+ "qmbs.ADDRESS3_DISTRICT DISTRICT, qmbs.ADDRESS3_CITY CITY, "
			+ "qmbs.ADDRESS3_ADDRESS1 ADDRESS1, qmbs.ADDRESS3_ADDRESS2 ADDRESS2, qmbs.ADDRESS3_ADDRESS3 ADDRESS3, "
			+ "qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE " + "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN PMST_TMP_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.ADDRESS3_ADDRESS_TYPE IS NOT NULL "
			+ "ORDER BY qmbs.EMPLOYEE_CODE)", nativeQuery = true)
	void InsertBulkMemberAddress3(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO PMST_TMP_MEMBER_BANK_ACCOUNT qmba (MEMBER_BANK_ACCOUNT_ID, MEMBER_ID, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, "
			+ "BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT PMST_TMP_MBR_BNK_ACC_ID_SEQ.nextval, MEMBER_ID, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, "
			+ "BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, qmbs.BANK1_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, qmbs.BANK1_ACCOUNT_TYPE_ID ACCOUNT_TYPE, "
			+ "qmbs.BANK1_IFCS_CODE IFSC_CODE, "
			+ "qmbs.BANK1_BANK_NAME BANK_NAME, qmbs.BANK1_BANK_BRANCH BANK_BRANCH, qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN PMST_TMP_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.BANK1_BANK_ACCOUNT_NUMBER IS NOT NULL)", nativeQuery = true)
	void InsertBulkMemberBank1(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO PMST_TMP_MEMBER_BANK_ACCOUNT qmba (MEMBER_BANK_ACCOUNT_ID, MEMBER_ID, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, "
			+ "BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT PMST_TMP_MBR_BNK_ACC_ID_SEQ.nextval, MEMBER_ID, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, "
			+ "BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, qmbs.BANK2_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, qmbs.BANK2_ACCOUNT_TYPE ACCOUNT_TYPE, "
			+ "qmbs.BANK2_IFCS_CODE IFSC_CODE, "
			+ "qmbs.BANK2_BANK_NAME BANK_NAME, qmbs.BANK2_BANK_BRANCH BANK_BRANCH, qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN PMST_TMP_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.BANK2_BANK_ACCOUNT_NUMBER IS NOT NULL)", nativeQuery = true)
	void InsertBulkMemberBank2(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO PMST_TMP_MEMBER_BANK_ACCOUNT qmba (MEMBER_BANK_ACCOUNT_ID, MEMBER_ID, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, "
			+ "BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT PMST_TMP_MBR_BNK_ACC_ID_SEQ.nextval, MEMBER_ID, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, "
			+ "BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, qmbs.BANK3_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, qmbs.BANK3_ACCOUNT_TYPE ACCOUNT_TYPE, "
			+ "qmbs.BANK3_IFCS_CODE IFSC_CODE, "
			+ "qmbs.BANK3_BANK_NAME BANK_NAME, qmbs.BANK3_BANK_BRANCH BANK_BRANCH, qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN PMST_TMP_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.BANK3_BANK_ACCOUNT_NUMBER IS NOT NULL)", nativeQuery = true)
	void InsertBulkMemberBank3(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO PMST_TMP_MEMBER_NOMINEE qmn (MEMBER_NOMINEE_ID, MEMBER_ID, RELATIONSHIP, DATE_OF_BIRTH, "
			+ "PAN_NUMBER, NOMINEE_AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, BANK_NAME, BANK_BRANCH, "
			+ "PERCENTAGE, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT PMST_TMP_MBR_NOMINEE_ID_SEQ.nextval, MEMBER_ID, RELATIONSHIP, DATE_OF_BIRTH, "
			+ "PAN_NUMBER, NOMINEE_AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, BANK_NAME, BANK_BRANCH, "
			+ "PERCENTAGE, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, NOMINEE1_RELATION_SHIP_ID RELATIONSHIP, "
			+ "NOMINEE1_DATE_OF_BIRTH DATE_OF_BIRTH, NOMINEE1_PAN_NUMBER PAN_NUMBER, NOMINEE1_AADHAR_NUMBER NOMINEE_AADHAR_NUMBER, NOMINEE1_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, "
			+ "NOMINEE1_BANK_ACCOUNT_TYPE AS ACCOUNT_TYPE, NOMINEE1_IFCS_CODE IFSC_CODE, "
			+ "NOMINEE1_BANK_NAME BANK_NAME, NOMINEE1_BANK_BRANCH BANK_BRANCH, NOMINEE1_PERCENTAGE PERCENTAGE, qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN PMST_TMP_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.NOMINEE1_NAME IS NOT NULL)", nativeQuery = true)
	void InsertBulkMemberNominee1(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO PMST_TMP_MEMBER_NOMINEE qmn (MEMBER_NOMINEE_ID, MEMBER_ID, RELATIONSHIP, DATE_OF_BIRTH, "
			+ "PAN_NUMBER, NOMINEE_AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, BANK_NAME, BANK_BRANCH, "
			+ "PERCENTAGE, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT PMST_TMP_MBR_NOMINEE_ID_SEQ.nextval, MEMBER_ID, RELATIONSHIP, DATE_OF_BIRTH, "
			+ "PAN_NUMBER, NOMINEE_AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, BANK_NAME, BANK_BRANCH, "
			+ "PERCENTAGE, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, NOMINEE2_RELATION_SHIP_ID RELATIONSHIP, "
			+ "NOMINEE2_DATE_OF_BIRTH DATE_OF_BIRTH, NOMINEE2_PAN_NUMBER PAN_NUMBER, NOMINEE2_AADHAR_NUMBER NOMINEE_AADHAR_NUMBER, NOMINEE2_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, "
			+ "NOMINEE2_BANK_ACCOUNT_TYPE AS ACCOUNT_TYPE, NOMINEE2_IFCS_CODE IFSC_CODE, "
			+ "NOMINEE2_BANK_NAME BANK_NAME, NOMINEE2_BANK_BRANCH BANK_BRANCH, NOMINEE2_PERCENTAGE PERCENTAGE, qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN PMST_TMP_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.NOMINEE2_NAME IS NOT NULL)", nativeQuery = true)
	void InsertBulkMemberNominee2(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO PMST_TMP_MEMBER_NOMINEE qmn (MEMBER_NOMINEE_ID, MEMBER_ID, RELATIONSHIP, DATE_OF_BIRTH, "
			+ "PAN_NUMBER, NOMINEE_AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, BANK_NAME, BANK_BRANCH, "
			+ "PERCENTAGE, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT PMST_TMP_MBR_NOMINEE_ID_SEQ.nextval, MEMBER_ID, RELATIONSHIP, DATE_OF_BIRTH, "
			+ "PAN_NUMBER, NOMINEE_AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, BANK_NAME, BANK_BRANCH, "
			+ "PERCENTAGE, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID, NOMINEE3_RELATION_SHIP_ID RELATIONSHIP, "
			+ "NOMINEE3_DATE_OF_BIRTH DATE_OF_BIRTH, NOMINEE3_PAN_NUMBER PAN_NUMBER, NOMINEE3_AADHAR_NUMBER NOMINEE_AADHAR_NUMBER, NOMINEE3_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, "
			+ "NOMINEE3_BANK_ACCOUNT_TYPE AS ACCOUNT_TYPE, NOMINEE3_IFCS_CODE IFSC_CODE, "
			+ "NOMINEE3_BANK_NAME BANK_NAME, NOMINEE3_BANK_BRANCH BANK_BRANCH, NOMINEE3_PERCENTAGE PERCENTAGE, qmbs.IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN PMST_TMP_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.NOMINEE3_NAME IS NOT NULL)", nativeQuery = true)
	void InsertBulkMemberNominee3(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO PMST_TMP_MEMBER_APPOINTEE qma (MEMBER_APPOINTEE_ID, MEMBER_ID, MEMBER_NOMINEE_ID, CODE, NAME, RELATIONSHIP, CONTACT_NUMBER, "
			+ "DATE_OF_BIRTH, PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, "
			+ "BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT PMST_TMP_MBR_APPOINTEE_ID_SEQ.nextval, MEMBER_ID, MEMBER_NOMINEE_ID, CODE, NAME, RELATIONSHIP, CONTACT_NUMBER, "
			+ "DATE_OF_BIRTH, PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, "
			+ "BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID MEMBER_ID, qmn.MEMBER_NOMINEE_ID MEMBER_NOMINEE_ID, qmbs.APPOINTEE1_CODE CODE, qmbs.APPOINTEE1_NAME NAME, "
			+ "qmbs.APPOINTEE1_RELATION_SHIP RELATIONSHIP, qmbs.APPOINTEE1_CONTACT_NUMBER CONTACT_NUMBER, qmbs.APPOINTEE1_DATE_OF_BIRTH DATE_OF_BIRTH, "
			+ "qmbs.APPOINTEE1_PAN_NUMBER PAN_NUMBER, qmbs.APPOINTEE1_AADHAR_NUMBER AADHAR_NUMBER, qmbs.APPOINTEE1_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, "
			+ "qmbs.APPOINTEE1_ACCOUNT_TYPE_ID ACCOUNT_TYPE, qmbs.APPOINTEE1_IFCS_CODE IFSC_CODE, "
			+ "qmbs.APPOINTEE1_BANK_NAME BANK_NAME, qmbs.APPOINTEE1_BANK_BRANCH BANK_BRANCH, "
			+ "qmbs.IS_ACTIVE IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN PMST_TMP_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "JOIN PMST_TMP_MEMBER_NOMINEE qmn ON qmn.MEMBER_ID = qm.MEMBER_ID\r\n"
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmn.NOMINEE_NAME = qmbs.APPOINTEE1_MEMBER_NOMINEE AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.APPOINTEE1_CODE IS NOT NULL "
			+ "AND qmbs.APPOINTEE1_MEMBER_NOMINEE IS NOT NULL)", nativeQuery = true)
	void InsertBulkMemberAppointee1(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO PMST_TMP_MEMBER_APPOINTEE qma (MEMBER_APPOINTEE_ID, MEMBER_ID, MEMBER_NOMINEE_ID, CODE, NAME, RELATIONSHIP, CONTACT_NUMBER, "
			+ "DATE_OF_BIRTH, PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, "
			+ "BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT PMST_TMP_MBR_APPOINTEE_ID_SEQ.nextval, MEMBER_ID, MEMBER_NOMINEE_ID, CODE, NAME, RELATIONSHIP, CONTACT_NUMBER, "
			+ "DATE_OF_BIRTH, PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, "
			+ "BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID MEMBER_ID, qmn.MEMBER_NOMINEE_ID MEMBER_NOMINEE_ID, qmbs.APPOINTEE2_CODE CODE, qmbs.APPOINTEE2_NAME NAME, "
			+ "qmbs.APPOINTEE2_RELATION_SHIP RELATIONSHIP, qmbs.APPOINTEE2_CONTACT_NUMBER CONTACT_NUMBER, qmbs.APPOINTEE2_DATE_OF_BIRTH DATE_OF_BIRTH, "
			+ "qmbs.APPOINTEE2_PAN_NUMBER PAN_NUMBER, qmbs.APPOINTEE2_AADHAR_NUMBER AADHAR_NUMBER, qmbs.APPOINTEE2_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, "
			+ "qmbs.APPOINTEE2_ACCOUNT_TYPE_ID ACCOUNT_TYPE, qmbs.APPOINTEE2_IFCS_CODE IFSC_CODE, "
			+ "qmbs.APPOINTEE2_BANK_NAME BANK_NAME, qmbs.APPOINTEE2_BANK_BRANCH BANK_BRANCH, "
			+ "qmbs.IS_ACTIVE IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN PMST_TMP_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "JOIN PMST_TMP_MEMBER_NOMINEE qmn ON qmn.MEMBER_ID = qm.MEMBER_ID "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmn.NOMINEE_NAME = qmbs.APPOINTEE2_MEMBER_NOMINEE AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.APPOINTEE2_CODE IS NOT NULL "
			+ "AND qmbs.APPOINTEE2_MEMBER_NOMINEE IS NOT NULL)", nativeQuery = true)
	void InsertBulkMemberAppointee2(Long memberBatchId, String createdBy);

	@Modifying
	@Query(value = "INSERT INTO PMST_TMP_MEMBER_APPOINTEE qma (MEMBER_APPOINTEE_ID, MEMBER_ID, MEMBER_NOMINEE_ID, CODE, NAME, RELATIONSHIP, CONTACT_NUMBER, "
			+ "DATE_OF_BIRTH, PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, "
			+ "BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE) "
			+ "SELECT PMST_TMP_MBR_APPOINTEE_ID_SEQ.nextval, MEMBER_ID, MEMBER_NOMINEE_ID, CODE, NAME, RELATIONSHIP, CONTACT_NUMBER, "
			+ "DATE_OF_BIRTH, PAN_NUMBER, AADHAR_NUMBER, BANK_ACCOUNT_NUMBER, ACCOUNT_TYPE, IFSC_CODE, "
			+ "BANK_NAME, BANK_BRANCH, IS_ACTIVE, CREATED_BY, CREATED_DATE "
			+ "FROM (SELECT qm.MEMBER_ID MEMBER_ID, qmn.MEMBER_NOMINEE_ID MEMBER_NOMINEE_ID, qmbs.APPOINTEE3_CODE CODE, qmbs.APPOINTEE3_NAME NAME, "
			+ "qmbs.APPOINTEE3_RELATION_SHIP RELATIONSHIP, qmbs.APPOINTEE3_CONTACT_NUMBER CONTACT_NUMBER, qmbs.APPOINTEE3_DATE_OF_BIRTH DATE_OF_BIRTH, "
			+ "qmbs.APPOINTEE3_PAN_NUMBER PAN_NUMBER, qmbs.APPOINTEE3_AADHAR_NUMBER AADHAR_NUMBER, qmbs.APPOINTEE3_BANK_ACCOUNT_NUMBER BANK_ACCOUNT_NUMBER, "
			+ "qmbs.APPOINTEE3_ACCOUNT_TYPE_ID ACCOUNT_TYPE, qmbs.APPOINTEE3_IFCS_CODE IFSC_CODE, "
			+ "qmbs.APPOINTEE3_BANK_NAME BANK_NAME, qmbs.APPOINTEE3_BANK_BRANCH BANK_BRANCH, "
			+ "qmbs.IS_ACTIVE IS_ACTIVE, ?2 CREATED_BY, CURRENT_TIMESTAMP CREATED_DATE "
			+ "FROM QSTG_MEMBER_BULK_STG qmbs "
			+ "JOIN PMST_TMP_MEMBER qm ON qm.MEMBER_BATCH_ID = qmbs.MEMBER_BATCH_ID AND qm.EMPLOYEE_CODE = qmbs.EMPLOYEE_CODE "
			+ "JOIN PMST_TMP_MEMBER_NOMINEE qmn ON qmn.MEMBER_ID = qm.MEMBER_ID "
			+ "WHERE qmbs.RECORD_STATUS = 'G' AND qmbs.IS_ACTIVE = 1 AND qmn.NOMINEE_NAME = qmbs.APPOINTEE3_MEMBER_NOMINEE AND qmbs.MEMBER_BATCH_ID = ?1 AND qmbs.APPOINTEE3_CODE IS NOT NULL "
			+ "AND qmbs.APPOINTEE3_MEMBER_NOMINEE IS NOT NULL)", nativeQuery = true)
	void InsertBulkMemberAppointee3(Long memberBatchId, String createdBy);
}
