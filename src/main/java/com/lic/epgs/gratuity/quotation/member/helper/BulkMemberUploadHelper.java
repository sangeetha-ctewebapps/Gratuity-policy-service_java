package com.lic.epgs.gratuity.quotation.member.helper;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;
import com.lic.epgs.gratuity.policy.member.repository.PolicyMemberRepository;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.dto.RenewalMemberImportDto;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberEntity;
import com.lic.epgs.gratuity.policy.renewalpolicy.member.repository.RenewalPolicyTMPMemberRepository;
import com.lic.epgs.gratuity.quotation.member.config.TaskThread;
import com.lic.epgs.gratuity.quotation.member.config.ThreadPoolExecutorUtil;
import com.lic.epgs.gratuity.quotation.member.entity.MemberBulkStgEntity;
import com.lic.epgs.gratuity.quotation.member.repository.MasterMemberRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberBulkStgRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberBulkUploadRepository;
import com.lic.epgs.gratuity.quotation.member.repository.MemberRepository;
import com.lic.epgs.gratuity.quotation.member.service.BulkMemberUploadHelperIntf;

/**
 * @author Gopi
 *
 */

@Service
public class BulkMemberUploadHelper implements BulkMemberUploadHelperIntf {

	protected final static Logger logger = LogManager.getLogger(BulkMemberUploadHelper.class);

	// @Value("${spring.datasource.url}")
	private String dbUrl;

	// @Value("${spring.datasource.driver-class-name}")
	private String dbDriver;

	// @Value("${spring.datasource.username}")
	private String dbUsername;

	// @Value("${spring.datasource.password}")
	private String dbPassword;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ThreadPoolExecutorUtil threadPoolExecutorUtil;

	@Autowired
	private MemberBulkStgRepository memberBulkStgRepository;

	@Autowired
	private PolicyMemberRepository policyMemberRepository;

	@Autowired
	private MasterMemberRepository masterMemberRepository;

	@Autowired
	private RenewalPolicyTMPMemberRepository renewalPolicyTMPMemberRepository;

	@Autowired
	private EntityManager entityManager;

	public void saveBulkMembersAsync(Long quotationId, Long batchId) {
		logger.info("QuotationBulkMemberUploadHelper -- saveBulkMembersAsync --start");
		TaskThread taskThread = new TaskThread(quotationId, jdbcTemplate, memberRepository, batchId);
		threadPoolExecutorUtil.executeTask(taskThread);
	}

	@Autowired
	private MemberBulkUploadRepository memberBulkUploadRepository;

	public void saveMembersFromStg(Long quotationId, Long batchId, String username) {
		logger.info("QuotationBulkMemberUploadHelper -- saveMember --started");

		memberBulkUploadRepository.bulkInsertMembers(batchId, quotationId, username);
		memberBulkUploadRepository.bulkInsertMemberAddress1(batchId, username);
		memberBulkUploadRepository.bulkInsertMemberAddress2(batchId, username);
		memberBulkUploadRepository.bulkInsertMemberAddress3(batchId, username);
		memberBulkUploadRepository.bulkInsertMemberBank1(batchId, username);
		memberBulkUploadRepository.bulkInsertMemberBank2(batchId, username);
		memberBulkUploadRepository.bulkInsertMemberBank3(batchId, username);
		memberBulkUploadRepository.bulkInsertMemberNominee1(batchId, username);
		memberBulkUploadRepository.bulkInsertMemberNominee2(batchId, username);
		memberBulkUploadRepository.bulkInsertMemberNominee3(batchId, username);
		memberBulkUploadRepository.bulkInsertMemberAppointee1(batchId, username);
		memberBulkUploadRepository.bulkInsertMemberAppointee2(batchId, username);
		memberBulkUploadRepository.bulkInsertMemberAppointee3(batchId, username);
		memberBulkStgRepository.inActivateRecordStatus(batchId, username);

		logger.info("QuotationBulkMemberUploadHelper -- saveMember --Ended");

	}

//AOM
	@Autowired
	private MemberBulkUploadRepository bulkMemberUploadRepository;

	public void saveBulkMembersFromStg(Long tmpPolicyId, Long batchId, String username) {
		logger.info("ServiceBulkMemberUploadHelper -- saveMember --started");

		bulkMemberUploadRepository.InsertBulkMembers(batchId, tmpPolicyId, username);
		bulkMemberUploadRepository.InsertBulkMemberAddress1(batchId, username);
		bulkMemberUploadRepository.InsertBulkMemberAddress2(batchId, username);
		bulkMemberUploadRepository.InsertBulkMemberAddress3(batchId, username);
		bulkMemberUploadRepository.InsertBulkMemberBank1(batchId, username);
		bulkMemberUploadRepository.InsertBulkMemberBank2(batchId, username);
		bulkMemberUploadRepository.InsertBulkMemberBank3(batchId, username);
		bulkMemberUploadRepository.InsertBulkMemberNominee1(batchId, username);
		bulkMemberUploadRepository.InsertBulkMemberNominee2(batchId, username);
		bulkMemberUploadRepository.InsertBulkMemberNominee3(batchId, username);
		bulkMemberUploadRepository.InsertBulkMemberAppointee1(batchId, username);
		bulkMemberUploadRepository.InsertBulkMemberAppointee2(batchId, username);
		bulkMemberUploadRepository.InsertBulkMemberAppointee3(batchId, username);
		memberBulkStgRepository.inActivateRecordStatus(batchId, username);

		logger.info("ServiceBulkMemberUploadHelper -- saveMember --Ended");
	}

//DOM
	public void savedomMembersFromStg(Long tmpPolicyId, Long batchId, String username) {
		logger.info("ServiceBulkMemberUploadHelper -- saveMember --started");

		bulkMemberUploadRepository.InsertBulkMembers(batchId, tmpPolicyId, username);

		memberBulkStgRepository.inActivateRecordStatus(batchId, username);

		logger.info("ServiceBulkMemberUploadHelper -- saveMember --Ended");
	}

	private List<MemberBulkStgEntity> getQuotationMembers(Long batchId) {
		return jdbcTemplate.query(
				"SELECT * FROM QSTG_MEMBER_BULK_STG WHERE RECORD_STATUS='G' AND IS_ACTIVE=1 AND MEMBER_BATCH_ID=? ORDER BY EMPLOYEE_CODE",
				BeanPropertyRowMapper.newInstance(MemberBulkStgEntity.class), batchId);
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getErrorForExcelData(Long batchId) {
		System.out.println("BATCH ID 1 ");
		System.out.println(batchId);
		return entityManager.createQuery("select m.memberBulkStgId, m.proposalPolicyNumber, m.licId, m.employeeCode, "
				+ "m.firstName, m.middleName, m.lastName,"
				+ "m.dateOfBirth, m.dateOfAppointment, m.dojToScheme, m.category, m.basicSalary, "
				+ "m.gender, m.salaryFrequency, m.panNumber, m.aadharNumber, m.contactNumber, m.emailId, "
				+ "m.address1AddressType, m.address1PinCode, "
				+ "m.address1Country, m.address1State, m.address1District, m.address1City, "
				+ "m.address1ContactNumber, m.address1Address1, m.address1Address2, m.address1Address3, "
				+ "m.address1Printable, m.address2AddressType, m.address2PinCode, "
				+ "m.address2Country, m.address2State, m.address2District, "
				+ "m.address2City, m.address2ContactNumber, m.address2Address1, m.address2Address2, "
				+ "m.address2Address3, m.address2Printable, m.address3AddressType, "
				+ "m.address3PinCode, m.address3Country, m.address3State, "
				+ "m.address3District, m.address3City, m.address3ContactNumber, m.address3Address1, "
				+ "m.address3Address2, m.address3Address3, m.address3Printable, m.bank1BankAccountNumber, "
				+ "m.bank1AccountType, m.bank1IfcsCode, m.bank1BankName, "
				+ "m.bank1BankBranch, m.bank2BankAccountNumber, "
				+ "m.bank2AccountType, m.bank2IfcsCode, m.bank2BankName, "
				+ "m.bank2BankBranch, m.bank3BankAccountNumber, m.bank3AccountType, "
				+ "m.bank3IfcsCode, m.bank3BankName, m.bank3BankBranch, "
				+ "m.nominee1Code, m.nominee1Name, m.nominee1RelationShip, "
				+ "m.nominee1ContactNumber, m.nominee1DateOfBirth, m.nominee1PanNumber,m.nominee1GenderId, m.nominee1AadharNumber, "
				+ "m.nominee1BankAccountNumber, m.nominee1BankAccountType, "
				+ "m.nominee1IfcsCode, m.nominee1BankName, "
				+ "m.nominee1BankBranch, m.nominee1Percentage, m.nominee2Code, m.nominee2Name, "
				+ "m.nominee2RelationShip, m.nominee2ContactNumber, m.nominee2DateOfBirth, "
				+ "m.nominee2PanNumber,m.nominee2GenderId, m.nominee2AadharNumber, m.nominee2BankAccountNumber, "
				+ "m.nominee2BankAccountType, m.nominee2IfcsCode, "
				+ "m.nominee2BankName, m.nominee2BankBranch, m.nominee2Percentage, "
				+ "m.nominee3Code, m.nominee3Name, m.nominee3RelationShip, "
				+ "m.nominee3ContactNumber, m.nominee3DateOfBirth, m.nominee3PanNumber,m.nominee3GenderId, m.nominee3AadharNumber, "
				+ "m.nominee3BankAccountNumber, m.nominee3BankAccountType, "
				+ "m.nominee3IfcsCode, m.nominee3BankName, "
				+ "m.nominee3BankBranch, m.nominee3Percentage, m.appointee1MemberNominee, "
				+ "m.appointee1Code, m.appointee1Name, m.appointee1RelationShip, "
				+ "m.appointee1ContactNumber, m.appointee1DateOfBirth, m.appointee1PanNumber, m.appointee1AadharNumber, "
				+ "m.appointee1BankAccountNumber, m.appointee1AccountType, "
				+ "m.appointee1IfcsCode, m.appointee1BankName, " + "m.appointee1BankBranch, m.appointee1Percentage, "
				+ "m.appointee2MemberNominee, m.appointee2Code, m.appointee2Name, "
				+ "m.appointee2RelationShip, m.appointee2ContactNumber, m.appointee2DateOfBirth, m.appointee2PanNumber, "
				+ "m.appointee2AadharNumber, m.appointee2BankAccountNumber, "
				+ "m.appointee2AccountType, m.appointee2IfcsCode, m.appointee2BankName, "
				+ "m.appointee2BankBranch, m.appointee2Percentage, "
				+ "m.appointee3MemberNominee, m.appointee3Code, m.appointee3Name, "
				+ "m.appointee3RelationShip, m.appointee3ContactNumber, "
				+ "m.appointee3DateOfBirth, m.appointee3PanNumber, m.appointee3AadharNumber, "
				+ "m.appointee3BankAccountNumber,  m.appointee3AccountType, "
				+ "m.appointee3IfcsCode, m.appointee3BankName,  m.appointee3BankBranch, " + "m.appointee3Percentage "
				+ "from MemberBulkStgEntity m where m.recordStatus='B' and m.memberBatchId = :batchId")
				.setParameter("batchId", batchId).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getErrorData(Long batchId) {
		System.out.println("BATCH ID 2 ");
		;
		System.out.println(batchId);
		return entityManager.createQuery(
				"select m.memberBulkStgId, m.memberBatchId, m.proposalPolicyNumber, m.licId, m.employeeCode, "
						+ "m.firstName, m.middleName, m.lastName, "
						+ "m.dateOfBirth, m.dateOfAppointment, m.dojToScheme, m.categoryId, m.category, m.basicSalary, "
						+ "m.genderId, m.gender, m.salaryFrequency, m.panNumber, m.aadharNumber, m.contactNumber, m.emailId, "
						+ "m.address1AddressTypeId, m.address1AddressType, m.address1PinCode, m.address1CountryId, "
						+ "m.address1Country, m.address1StateId, m.address1State, m.address1District, m.address1City, "
						+ "m.address1ContactNumber, m.address1Address1, m.address1Address2, m.address1Address3, "
						+ "m.address1Printable, " // 30 to 43 address 1
						+ "m.address2AddressTypeId, m.address2AddressType, m.address2PinCode, "
						+ "m.address2CountryId, m.address2Country, m.address2StateId, m.address2State, m.address2District, "
						+ "m.address2City, m.address2ContactNumber, m.address2Address1, m.address2Address2, "
						+ "m.address2Address3, m.address2Printable, " // 44 to 57 address 2
						+ "m.address3AddressTypeId, m.address3AddressType, "
						+ "m.address3PinCode, m.address3CountryId, m.address3Country, m.address3StateId, m.address3State, "
						+ "m.address3District, m.address3City, m.address3ContactNumber, m.address3Address1, "
						+ "m.address3Address2, m.address3Address3, m.address3Printable, " // 58 to 71 address 3
						+ "m.bank1BankAccountNumber, "
						+ "m.bank1AccountTypeId, m.bank1AccountType, m.bank1IfcsCode, m.bank1BankNameId, m.bank1BankName, "
						+ "m.bank1BankBranchId, m.bank1BankBranch, " // 72 to 79 bank 1
						+ "m.bank2BankAccountNumber, m.bank2AccountTypeId, "
						+ "m.bank2AccountType, m.bank2IfcsCode, m.bank2BankNameId, m.bank2BankName, m.bank2BankBranchId, "
						+ "m.bank2BankBranch, " // 80 to 87 bank 2
						+ "m.bank3BankAccountNumber, m.bank3AccountTypeId, m.bank3AccountType, "
						+ "m.bank3IfcsCode, m.bank3BankNameId, m.bank3BankName, m.bank3BankBranchId, m.bank3BankBranch, " // 88
																															// to
																															// 95
																															// bank
																															// 3
						+ "m.nominee1Code, m.nominee1Name, m.nominee1RelationShipId, m.nominee1RelationShip, "
						+ "m.nominee1ContactNumber, m.nominee1DateOfBirth, m.nominee1PanNumber, m.nominee1AadharNumber, "
						+ "m.nominee1BankAccountNumber, m.nominee1BankAccountTypeId, m.nominee1BankAccountType, "
						+ "m.nominee1IfcsCode, m.nominee1BankNameId, m.nominee1BankName, m.nominee1BankBranchId, "
						+ "m.nominee1BankBranch, m.nominee1Percentage, " // 96 to 112 nominee 1
						+ "m.nominee2Code, m.nominee2Name, "
						+ "m.nominee2RelationShipId, m.nominee2RelationShip, m.nominee2ContactNumber, m.nominee2DateOfBirth, "
						+ "m.nominee2PanNumber, m.nominee2AadharNumber, m.nominee2BankAccountNumber, "
						+ "m.nominee2BankAccountTypeId, m.nominee2BankAccountType, m.nominee2IfcsCode, m.nominee2BankNameId, "
						+ "m.nominee2BankName, m.nominee2BankBranchId, m.nominee2BankBranch, m.nominee2Percentage, " // 113
																														// to
																														// 129
																														// nominee
																														// 2
						+ "m.nominee3Code, m.nominee3Name, m.nominee3RelationShipId, m.nominee3RelationShip, "
						+ "m.nominee3ContactNumber, m.nominee3DateOfBirth, m.nominee3PanNumber, m.nominee3AadharNumber, "
						+ "m.nominee3BankAccountNumber, m.nominee3BankAccountTypeId, m.nominee3BankAccountType, "
						+ "m.nominee3IfcsCode, m.nominee3BankNameId, m.nominee3BankName, m.nominee3BankBranchId, "
						+ "m.nominee3BankBranch, m.nominee3Percentage, " // 130 to 146 nominee 3
						+ "m.appointee1MemberNomineeId, m.appointee1MemberNominee, "
						+ "m.appointee1Code, m.appointee1Name, m.appointee1RelationShipId, m.appointee1RelationShip, "
						+ "m.appointee1ContactNumber, m.appointee1DateOfBirth, m.appointee1PanNumber, m.appointee1AadharNumber, "
						+ "m.appointee1BankAccountNumber, m.appointee1AccountTypeId, m.appointee1AccountType, "
						+ "m.appointee1IfcsCode, m.appointee1BankNameId, m.appointee1BankName, m.appointee1BankBranchId, "
						+ "m.appointee1BankBranch, m.appointee1Percentage, " // 147 to 165 appointee 1
						+ "m.appointee2MemberNomineeId, "
						+ "m.appointee2MemberNominee, m.appointee2Code, m.appointee2Name, m.appointee2RelationShipId, "
						+ "m.appointee2RelationShip, m.appointee2ContactNumber, m.appointee2DateOfBirth, m.appointee2PanNumber, "
						+ "m.appointee2AadharNumber, m.appointee2BankAccountNumber, m.appointee2AccountTypeId, "
						+ "m.appointee2AccountType, m.appointee2IfcsCode, m.appointee2BankNameId, m.appointee2BankName, "
						+ "m.appointee2BankBranchId, m.appointee2BankBranch, m.appointee2Percentage, " // 166 to 184
																										// appointee 2
						+ "m.appointee3MemberNomineeId, m.appointee3MemberNominee, m.appointee3Code, m.appointee3Name, "
						+ "m.appointee3RelationShipId, m.appointee3RelationShip, m.appointee3ContactNumber, "
						+ "m.appointee3DateOfBirth, m.appointee3PanNumber, m.appointee3AadharNumber, "
						+ "m.appointee3BankAccountNumber, m.appointee3AccountTypeId, m.appointee3AccountType, "
						+ "m.appointee3IfcsCode, m.appointee3BankNameId, m.appointee3BankName, m.appointee3BankBranchId, m.appointee3BankBranch, "
						+ "m.appointee3Percentage, " // 185 to 203 appointee 3
						+ "m.recordStatus, m.errorCode, m.isActive, m.createdBy, "
						+ "m.createdDate, m.modifiedBy, m.modifiedDate "
						+ "from MemberBulkStgEntity m where m.recordStatus='B' and m.memberBatchId = :batchId")
				.setParameter("batchId", batchId).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getdomErrorForExcelData(Long batchId) {
		System.out.println("BATCH ID 1 ");
		System.out.println(batchId);
		return entityManager.createQuery("select m.memberBulkStgId, m.dateOfExit, "					//m.proposalPolicyNumber, 
				+ "m.reasonForDeath, m.reasonForDeathOther, m.licId, m.employeeCode, "
				 + "m.recordStatus, m.errorCode "
				+ "from MemberBulkStgEntity m where m.recordStatus='B' and m.memberBatchId = :batchId")
				.setParameter("batchId", batchId).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getdomErrorData(Long batchId) {
		System.out.println("BATCH ID 2 ");
		System.out.println(batchId);
		return entityManager.createQuery("select m.memberBulkStgId, m.dateOfExit, "
				+ "m.reasonForDeath, m.reasonForDeathOther, m.licId, m.employeeCode, "
				 + "m.recordStatus, m.errorCode "
					   + "from MemberBulkStgEntity m where m.recordStatus='B' and m.memberBatchId = :batchId")
				.setParameter("batchId", batchId).getResultList();
	}

	public List<Object[]> getPolicyMasterDataForExcel(Long policyId) {

		return policyMemberRepository.findBypolicyId1(policyId);
	}

	public List<Object[]> getPolicyStagingDataForExcel(Long policyId) {
		return policyMemberRepository.findBypolicyId2(policyId);
	}

	public List<Object[]> getQuotationMasterDataForExcel(Long quotationId) {

		return masterMemberRepository.findByQuotationId1(quotationId);
	}

	public List<Object[]> getQuotationStagingDataForExcel(Long quotationId) {
		return masterMemberRepository.findByQuotationId2(quotationId);
	}

	public List<Object[]> getRenewalQuotationDataForExcel(Long tmpPolicyId) {

		return renewalPolicyTMPMemberRepository.findByTempPolicyID(tmpPolicyId);
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getmemberExcelData(Long policyId) {
		return entityManager
				.createNativeQuery("Select pm.MEMBER_ID  AS id,pp.POLICY_NUMBER As policyNumber,pm.LIC_ID As licId,\r\n"
						+ "pm.EMPLOYEE_CODE As employeeCode,pm.FIRST_NAME As firstName,\r\n"
						+ "pm.MIDDLE_NAME As middleName,pm.LAST_NAME As lastName,\r\n"
						+ "to_char(trunc(pm.DATE_OF_BIRTH), 'DD/MM/YYYY') AS dateOfBirth,"
						+ "to_char(trunc(pm.DATE_OF_APPOINTMENT), 'DD/MM/YYYY') AS dateofAppointment,"
						+ "to_char(trunc(pm.DOJ_TO_SCHEME), 'DD/MM/YYYY') AS dojOfScheme,"
						+ "mc.NAME as catogeryId,\r\n"
						+ "pm.BASIC_SALARY as basicSalary,pli.PICK_LIST_ITEM_NAME  as gender,\r\n"
						+ "pm.SALARY_FREQUENCY as salaryFrequenc,pm.PAN_NUMBER As panNumber,\r\n"
						+ "pm.AADHAR_NUMBER As aadharNumber, pm.MOBILE_NO As mobileNo,pm.EMAIL_ID \r\n"
						+ "	As emailId From PMST_MEMBER pm JOIN PICK_LIST_ITEM pli ON \r\n"
						+ "	pm.GENDER_ID = pli.PICK_LIST_ITEM_ID \r\n"
						+ "	JOIN MEMBER_CATEGORY mc ON mc.MEMBER_CATEGORY_ID = pm.CATEGORY_ID \r\n"
						+ "	JOIN PMST_POLICY pp ON pp.POLICY_ID = pm.POLICY_ID \r\n"
						+ "	where pm.POLICY_ID =:POLICYID and pm.IS_ACTIVE=1   ORDER BY EMPLOYEE_CODE")
				.setParameter("POLICYID", policyId).getResultList();
	}

	public void saveMembersFromStgMember(RenewalMemberImportDto renewalMemberImportDto) {
//				logger.info("QuotationBulkMemberUploadHelper -- saveMember --started");
		final List<RenewalPolicyTMPMemberEntity> policyTMPMemberEntities = new ArrayList<>();
		List<MemberBulkStgEntity> temps = getQuotationMembers(renewalMemberImportDto.getBatchId());
		List<PolicyMemberEntity> policyMember = policyMemberRepository
				.findBypolicyId(renewalMemberImportDto.getPolicyId());

		for (MemberBulkStgEntity temp : temps) {
			Long masterMemberId = null;
			Long masterPolicyId = null;
			for (PolicyMemberEntity policyMemberEntity : policyMember) {

				if (policyMemberEntity.getEmployeeCode().equals(temp.getEmployeeCode())
						&& policyMemberEntity.getLicId().equals(temp.getLicId())) {
					temp.setRecordStatus("U");
					masterMemberId = policyMemberEntity.getId();
					masterPolicyId = policyMemberEntity.getPolicyId();
					if (policyMemberEntity.getAadharNumber() != temp.getAadharNumber()) {
						temp.setRecordStatus("U");

					}
					if (policyMemberEntity.getProposalPolicyNumber() != temp.getProposalPolicyNumber()) {
						temp.setRecordStatus("U");
					}
					if (policyMemberEntity.getFirstName() != temp.getFirstName()) {
						temp.setRecordStatus("U");
					}

					if (policyMemberEntity.getMiddleName() != temp.getMiddleName()) {
						temp.setRecordStatus("U");
					}
					if (policyMemberEntity.getLastName() != temp.getLastName()) {
						temp.setRecordStatus("U");
					}
					if (policyMemberEntity.getDateOfBirth() != temp.getDateOfBirth()) {
						temp.setRecordStatus("U");
					}
					if (policyMemberEntity.getDateOfAppointment() != temp.getDateOfAppointment()) {
						temp.setRecordStatus("U");
					}
					if (policyMemberEntity.getBasicSalary() != temp.getBasicSalary()) {
						temp.setRecordStatus("U");
					}
					if (policyMemberEntity.getSalaryFrequency() != temp.getSalaryFrequency()) {
						temp.setRecordStatus("U");
					}
					if (policyMemberEntity.getPanNumber() != temp.getPanNumber()) {
						temp.setRecordStatus("U");
					}
					if (policyMemberEntity.getAadharNumber() != temp.getAadharNumber()) {
						temp.setRecordStatus("U");
					}
					if (policyMemberEntity.getEmailId() != temp.getEmailId()) {
						temp.setRecordStatus("U");
					}
					if (policyMemberEntity.getCategoryId() != temp.getCategoryId()) {
						temp.setRecordStatus("U");
					}
					if (policyMemberEntity.getGenderId() != temp.getGenderId()) {
						temp.setRecordStatus("U");

					}

					break;
				}

				if (temp.getLicId() == null) {
					temp.setRecordStatus("N");
					break;
				}

//						else {
//							policyMemberEntity.setIsActive(false);
//							policyMemberRepository.save(policyMemberEntity);
//									break;
//						}

			}
			policyTMPMemberEntities.add(temp.toPolicyTmpEntity(masterMemberId, masterPolicyId, renewalMemberImportDto));
		}

		if (policyTMPMemberEntities.size() == temps.size()) {
			// in-activate previous uploads & imports
			/*
			 * memberBatchFileRepository.inactiveByQuotationId(quotationId, batchId);
			 * memberBatchRepository.inactiveByQuotationId(quotationId, batchId);
			 * memberAddressRepository.inactiveByQuotationId(quotationId);
			 * memberAppointeeRepository.inactiveByQuotationId(quotationId);
			 * memberBankAccountRepository.inactiveByQuotationId(quotationId);
			 * memberNomineeRepository.inactiveByQuotationId(quotationId);;
			 * memberRepository.inactiveByQuotationId(quotationId);
			 */

			// save new import
			renewalPolicyTMPMemberRepository.saveAll(policyTMPMemberEntities);
			memberBulkStgRepository.inActivateRecordStatus(renewalMemberImportDto.getBatchId(),
					renewalMemberImportDto.getUsername());
		}
	}

//			Claims Bulk started

	@SuppressWarnings("unchecked")
	public List<Object[]> getErrorForClaimsExcelData(Long batchId) {
		return entityManager.createQuery(
				"select m.memberBulkStgId,m.dateOfExit,m.modeOfExitName,m.reasonForDeath,m.reasonForDeathOther,\r\n"
						+ " m.dateOfDeath,m.placeOfDeath,m.licId,m.employeeCode "
						+ "from MemberBulkStgEntity m where m.recordStatus='B' and m.memberBatchId = :batchId")
				.setParameter("batchId", batchId).getResultList();
	}

}
