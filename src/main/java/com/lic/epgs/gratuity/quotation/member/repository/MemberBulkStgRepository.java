package com.lic.epgs.gratuity.quotation.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.member.entity.MemberBulkStgEntity;

/**
 * @author Gopi
 *
 */

@Repository
public interface MemberBulkStgRepository extends JpaRepository<MemberBulkStgEntity, Long> {
	
	@Query("SELECT MAX(memberBulkStgId) FROM MemberBulkStgEntity")
	Long getMaxStgId();
	
	@Query(value="SELECT COUNT(*) FROM QSTG_MEMBER_BULK_STG WHERE RECORD_STATUS='G' AND MEMBER_BATCH_ID=?", nativeQuery=true)
	Long getGoodRecordsCount(Long batchId);
	
	@Query(value="SELECT COUNT(*) FROM QSTG_MEMBER_BULK_STG WHERE RECORD_STATUS='B' AND MEMBER_BATCH_ID=?", nativeQuery=true)
	Long getBadRecordsCount(Long batchId);
	
	@Query(value="SELECT * FROM QSTG_MEMBER_BULK_STG WHERE RECORD_STATUS='B' AND MEMBER_BATCH_ID=?", nativeQuery=true)
	List<MemberBulkStgEntity> getBadRecords(Long batchId);
	
//	@Modifying
//	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.CATEGORY_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
//			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.CATEGORY)) AND pli.PICK_LIST_ID=31) "
//			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
//	void updateCategoryId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.CATEGORY_ID = (SELECT mc.MEMBER_CATEGORY_ID FROM MEMBER_CATEGORY mc "
			+ "WHERE LOWER(TRIM(mc.NAME)) = LOWER(TRIM(mbs.CATEGORY)) AND mc.QSTG_QUOTATION_ID = :quotationId) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateCategoryId(@Param("batchId") Long batchId, @Param("quotationId") Long quotationId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.GENDER_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.GENDER)) AND pli.PICK_LIST_ID=30) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateGenderId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.TERM_TYPE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.TERM_TYPE)) AND pli.PICK_LIST_ID=22) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateTermTypeId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.BORROWER_TYPE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.BORROWER_TYPE)) AND pli.PICK_LIST_ID=23) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateBorrowerTypeId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.TYPE_OF_LIFE_ASSURED_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.TYPE_OF_LIFE_ASSURED)) AND pli.PICK_LIST_ID=24) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateTypeOfLifeAssured(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.MORATORIUM_PERIOD_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.MORATORIUM_PERIOD)) AND pli.PICK_LIST_ID=25) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateMoratoriumPeriod(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.FCL_TYPE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.FCL_TYPE)) AND pli.PICK_LIST_ID=6) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateFclTypeId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.ADDRESS1_ADDRESS_TYPE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.ADDRESS1_ADDRESS_TYPE)) AND pli.PICK_LIST_ID=26) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAddress1AddressTypeId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.ADDRESS1_COUNTRY_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.ADDRESS1_COUNTRY)) AND pli.PICK_LIST_ID=27) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAddress1CountryId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.ADDRESS1_STATE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.ADDRESS1_STATE)) AND pli.PICK_LIST_ID=29) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAddress1StateId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.ADDRESS2_ADDRESS_TYPE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.ADDRESS2_ADDRESS_TYPE)) AND pli.PICK_LIST_ID=26) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAddress2AddressTypeId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.ADDRESS2_COUNTRY_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.ADDRESS2_COUNTRY)) AND pli.PICK_LIST_ID=27) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAddress2CountryId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.ADDRESS2_STATE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.ADDRESS2_STATE)) AND pli.PICK_LIST_ID=29) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAddress2StateId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.ADDRESS3_ADDRESS_TYPE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.ADDRESS3_ADDRESS_TYPE)) AND pli.PICK_LIST_ID=26) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAddress3AddressTypeId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.ADDRESS3_COUNTRY_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.ADDRESS3_COUNTRY)) AND pli.PICK_LIST_ID=27) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAddress3CountryId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.ADDRESS3_STATE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.ADDRESS3_STATE)) AND pli.PICK_LIST_ID=29) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAddress3StateId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.BANK1_ACCOUNT_TYPE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.BANK1_ACCOUNT_TYPE)) AND pli.PICK_LIST_ID=32) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateBank1AccountTypeId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.BANK1_BANK_NAME_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.BANK1_BANK_NAME)) AND pli.PICK_LIST_ID=33) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateBank1BankNameId(@Param("batchId") Long batchId);

	//TODO: add parent id in picklistitem
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.BANK1_BANK_BRANCH_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.BANK1_BANK_BRANCH)) AND pli.PICK_LIST_ID=34) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateBank1BankBranchId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.BANK2_ACCOUNT_TYPE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.BANK2_ACCOUNT_TYPE)) AND pli.PICK_LIST_ID=32) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateBank2AccountTypeId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.BANK2_BANK_NAME_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.BANK2_BANK_NAME)) AND pli.PICK_LIST_ID=33) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateBank2BankNameId(@Param("batchId") Long batchId);

	//TODO: add parent id in picklistitem
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.BANK2_BANK_BRANCH_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.BANK2_BANK_BRANCH)) AND pli.PICK_LIST_ID=34) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateBank2BankBranchId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.BANK3_ACCOUNT_TYPE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.BANK3_ACCOUNT_TYPE)) AND pli.PICK_LIST_ID=32) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateBank3AccountTypeId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.BANK3_BANK_NAME_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.BANK3_BANK_NAME)) AND pli.PICK_LIST_ID=33) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateBank3BankNameId(@Param("batchId") Long batchId);

	//TODO: add parent id in picklistitem
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.BANK3_BANK_BRANCH_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.BANK3_BANK_BRANCH)) AND pli.PICK_LIST_ID=34) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateBank3BankBranchId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.NOMINEE1_RELATION_SHIP_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.NOMINEE1_RELATION_SHIP)) AND pli.PICK_LIST_ID=35) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateNominee1RelationShipId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.NOMINEE1_BANK_ACCOUNT_TYPE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.NOMINEE1_BANK_ACCOUNT_TYPE)) AND pli.PICK_LIST_ID=32) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateNominee1BankAccountTypeId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.NOMINEE1_BANK_NAME_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.NOMINEE1_BANK_NAME)) AND pli.PICK_LIST_ID=33) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateNominee1BankNameId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.NOMINEE1_BANK_BRANCH_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.NOMINEE1_BANK_BRANCH)) AND pli.PICK_LIST_ID=34) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateNominee1BankBranchId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.NOMINEE2_RELATION_SHIP_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.NOMINEE2_RELATION_SHIP)) AND pli.PICK_LIST_ID=35) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateNominee2RelationShipId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.NOMINEE2_BANK_ACCOUNT_TYPE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.NOMINEE2_BANK_ACCOUNT_TYPE)) AND pli.PICK_LIST_ID=32) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateNominee2BankAccountTypeId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.NOMINEE2_BANK_NAME_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.NOMINEE2_BANK_NAME)) AND pli.PICK_LIST_ID=33) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateNominee2BankNameId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.NOMINEE2_BANK_BRANCH_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.NOMINEE2_BANK_BRANCH)) AND pli.PICK_LIST_ID=34) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateNominee2BankBranchId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.NOMINEE3_RELATION_SHIP_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.NOMINEE3_RELATION_SHIP)) AND pli.PICK_LIST_ID=35) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateNominee3RelationShipId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.NOMINEE3_BANK_ACCOUNT_TYPE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.NOMINEE3_BANK_ACCOUNT_TYPE)) AND pli.PICK_LIST_ID=32) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateNominee3BankAccountTypeId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.NOMINEE3_BANK_NAME_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.NOMINEE3_BANK_NAME)) AND pli.PICK_LIST_ID=33) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateNominee3BankNameId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.NOMINEE3_BANK_BRANCH_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.NOMINEE3_BANK_BRANCH)) AND pli.PICK_LIST_ID=34) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateNominee3BankBranchId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.APPOINTEE1_RELATION_SHIP_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.APPOINTEE1_RELATION_SHIP)) AND pli.PICK_LIST_ID=35) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAppointee1RelationShipId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.APPOINTEE1_ACCOUNT_TYPE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.APPOINTEE1_ACCOUNT_TYPE)) AND pli.PICK_LIST_ID=32) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAppointee1AccountTypeId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.APPOINTEE1_BANK_NAME_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.APPOINTEE1_BANK_NAME)) AND pli.PICK_LIST_ID=33) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAppointee1BankNameId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.APPOINTEE1_BANK_BRANCH_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.APPOINTEE1_BANK_BRANCH)) AND pli.PICK_LIST_ID=34) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAppointee1BankBranchId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.APPOINTEE2_RELATION_SHIP_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.APPOINTEE2_RELATION_SHIP)) AND pli.PICK_LIST_ID=35) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAppointee2RelationShipId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.APPOINTEE2_ACCOUNT_TYPE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.APPOINTEE2_ACCOUNT_TYPE)) AND pli.PICK_LIST_ID=32) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAppointee2AccountTypeId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.APPOINTEE2_BANK_NAME_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.APPOINTEE2_BANK_NAME)) AND pli.PICK_LIST_ID=33) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAppointee2BankNameId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.APPOINTEE2_BANK_BRANCH_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.APPOINTEE2_BANK_BRANCH)) AND pli.PICK_LIST_ID=34) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAppointee2BankBranchId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.APPOINTEE3_RELATION_SHIP_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.APPOINTEE3_RELATION_SHIP)) AND pli.PICK_LIST_ID=35) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAppointee3RelationShipId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.APPOINTEE3_ACCOUNT_TYPE_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.APPOINTEE3_ACCOUNT_TYPE)) AND pli.PICK_LIST_ID=32) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAppointee3AccountTypeId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.APPOINTEE3_BANK_NAME_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.APPOINTEE3_BANK_NAME)) AND pli.PICK_LIST_ID=33) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAppointee3BankNameId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.APPOINTEE3_BANK_BRANCH_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.APPOINTEE3_BANK_BRANCH)) AND pli.PICK_LIST_ID=34) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateAppointee3BankBranchId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG SET RECORD_STATUS ='B', ERROR_CODE = 'PER' "
			+ "WHERE COALESCE(NOMINEE1_PERCENTAGE, 0)+COALESCE(NOMINEE2_PERCENTAGE, 0) + "
			+ "COALESCE(NOMINEE3_PERCENTAGE, 0) != 0 AND COALESCE(NOMINEE1_PERCENTAGE, 0) "
			+ "+COALESCE(NOMINEE2_PERCENTAGE, 0) + COALESCE(NOMINEE3_PERCENTAGE, 0) != 100 "
			+ "AND MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void findTotalPercentage(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG SET RECORD_STATUS  = 'B', ERROR_CODE = 'DUP' "
			+ "WHERE rowid IN (SELECT ROWID  FROM ( SELECT EMPLOYEE_CODE,MEMBER_BATCH_ID, ROW_NUMBER () "
			+ "OVER (PARTITION BY EMPLOYEE_CODE,MEMBER_BATCH_ID ORDER BY EMPLOYEE_CODE) AS ROW_NUMBER FROM "
			+ "QSTG_MEMBER_BULK_STG) WHERE ROW_NUMBER > 1 AND MEMBER_BATCH_ID= :batchId)", nativeQuery=true)
	void markDuplicateRecords(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG SET RECORD_STATUS  = 'B', ERROR_CODE = 'AGE' "
			+ "WHERE floor(months_between(sysdate,DATE_OF_BIRTH)/12) < 18 "
			+ "AND MEMBER_BATCH_ID= :batchId", nativeQuery=true)
	void ageValidation(@Param("batchId") Long batchId);
	
//	nominee newly added column in qstg_member_bulk
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.NOMINEE1_GENDER_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.NOMINEE1_GENDER)) AND pli.PICK_LIST_ID=27) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateNominee1GenderId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.NOMINEE2_GENDER_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.NOMINEE2_GENDER)) AND pli.PICK_LIST_ID=27) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateNominee2GenderId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.NOMINEE3_GENDER_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.NOMINEE3_GENDER)) AND pli.PICK_LIST_ID=27) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateNominee3GenderId(@Param("batchId") Long batchId);
	
// nominee end 
	
	@Modifying
//	@Query(value="UPDATE MemberBulkStgEntity mbs SET mbs.recordStatus = 'B' WHERE "
//			+ "(mbs.categoryId IS NULL OR  mbs.genderId IS NULL OR  mbs.termTypeId IS NULL OR  mbs.typeOfLifeAssuredId IS NULL "
//			+ "OR  mbs.address1AddressTypeId IS NULL OR  mbs.address1CountryId IS NULL "
//			+ "OR mbs.address1StateId IS NULL OR mbs.bank1AccountTypeId IS NULL OR mbs.bank1BankNameId IS NULL "
//			+ "OR mbs.bank1BankBranchId IS NULL OR mbs.nominee1RelationShip IS NULL OR mbs.nominee1BankAccountType IS NULL "
//			+ "OR mbs.nominee1BankNameId IS NULL OR mbs.nominee1BankBranchId IS NULL "
//			+ "OR mbs.appointee1AccountTypeId IS NULL OR mbs.appointee1BankNameId IS NULL OR mbs.appointee1BankBranchId IS NULL "
//			+ "OR mbs.appointee1RelationShipId IS NULL ) "
//			+ "AND mbs.memberBatchId = :batchId")
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.RECORD_STATUS = 'B' WHERE "
            + "((mbs.CATEGORY_ID IS NULL OR  mbs.GENDER_ID IS NULL "
            //Address 1
            + "OR (mbs.ADDRESS1_ADDRESS_TYPE_ID IS NULL AND mbs.ADDRESS1_ADDRESS_TYPE || mbs.ADDRESS1_PIN_CODE "
            + "|| mbs.ADDRESS1_COUNTRY  || mbs.ADDRESS1_STATE || mbs.ADDRESS1_DISTRICT || mbs.ADDRESS1_CITY "
            + "|| mbs.ADDRESS1_CONTACT_NUMBER || mbs.ADDRESS1_ADDRESS1 || mbs.ADDRESS1_ADDRESS2 "
            + "|| mbs.ADDRESS1_ADDRESS3 IS NOT NULL)" 
            //Address 2
            + "OR (mbs.ADDRESS2_ADDRESS_TYPE_ID IS NULL AND mbs.ADDRESS2_ADDRESS_TYPE || mbs.ADDRESS2_PIN_CODE "
            + "|| mbs.ADDRESS2_COUNTRY || mbs.ADDRESS2_STATE || mbs.ADDRESS2_DISTRICT || mbs.ADDRESS2_CITY "
            + "|| mbs.ADDRESS2_ADDRESS3 IS NOT NULL) "
            //Address 3
            + "OR (mbs.ADDRESS3_ADDRESS_TYPE_ID IS NULL AND mbs.ADDRESS3_ADDRESS_TYPE || mbs.ADDRESS3_PIN_CODE "
            + "|| mbs.ADDRESS3_COUNTRY || mbs.ADDRESS3_STATE || mbs.ADDRESS3_DISTRICT || mbs.ADDRESS3_CITY "
            + "|| mbs.ADDRESS2_CONTACT_NUMBER || mbs.ADDRESS3_ADDRESS1 || mbs.ADDRESS3_ADDRESS2 "
            + "|| mbs.ADDRESS3_ADDRESS3 IS NOT NULL) "
            //Bank Account 1
            + "OR ((mbs.BANK1_ACCOUNT_TYPE_ID IS NULL AND mbs.BANK1_BANK_NAME_ID IS NULL AND mbs.BANK1_BANK_BRANCH_ID IS NULL ) "
            + "AND mbs.BANK1_ACCOUNT_TYPE || mbs.BANK1_IFCS_CODE || mbs.BANK1_BANK_NAME || mbs.BANK1_BANK_BRANCH IS NOT NULL) "
            //Bank Account 2
            + "OR ((mbs.BANK2_ACCOUNT_TYPE_ID IS NULL AND mbs.BANK2_BANK_NAME_ID IS NULL AND mbs.BANK2_BANK_BRANCH_ID IS NULL) "
            + "AND mbs.BANK2_ACCOUNT_TYPE || mbs.BANK2_IFCS_CODE || mbs.BANK2_BANK_NAME || mbs.BANK2_BANK_BRANCH IS NOT NULL) "
            //Bank Account 3
            + "OR ((mbs.BANK3_ACCOUNT_TYPE_ID IS NULL AND mbs.BANK3_BANK_NAME_ID IS NULL AND mbs.BANK3_BANK_BRANCH_ID IS NULL ) "
            + "AND mbs.BANK3_ACCOUNT_TYPE || mbs.BANK3_IFCS_CODE || mbs.BANK3_BANK_NAME || mbs.BANK3_BANK_BRANCH IS NOT NULL) "
            //Nominee 1
            + "OR ((mbs.NOMINEE1_RELATION_SHIP_ID IS NULL OR mbs.NOMINEE1_BANK_ACCOUNT_TYPE_ID IS NULL "
            + "OR mbs.NOMINEE1_BANK_NAME_ID IS NULL OR mbs.NOMINEE1_BANK_BRANCH_ID IS NULL) AND mbs.NOMINEE1_RELATION_SHIP || "
            + "mbs.NOMINEE1_CONTACT_NUMBER || mbs.NOMINEE1_DATE_OF_BIRTH || mbs.NOMINEE1_PAN_NUMBER || mbs.NOMINEE1_AADHAR_NUMBER || "
            + "mbs.NOMINEE1_BANK_ACCOUNT_NUMBER || mbs.NOMINEE1_BANK_ACCOUNT_TYPE || mbs.NOMINEE1_IFCS_CODE || mbs.NOMINEE1_BANK_NAME || "
            + "mbs.NOMINEE1_BANK_BRANCH || mbs.NOMINEE1_PERCENTAGE  || mbs.NOMINEE1_CODE IS NOT NULL) "
            //Nominee 2
            + "OR ((mbs.NOMINEE2_RELATION_SHIP_ID IS NULL OR mbs.NOMINEE2_BANK_ACCOUNT_TYPE_ID IS NULL "
            + "OR mbs.NOMINEE2_BANK_NAME_ID IS NULL OR mbs.NOMINEE2_BANK_BRANCH_ID IS NULL) AND mbs.NOMINEE2_RELATION_SHIP || "
            + "mbs.NOMINEE2_CONTACT_NUMBER || mbs.NOMINEE2_DATE_OF_BIRTH || mbs.NOMINEE2_PAN_NUMBER || mbs.NOMINEE2_AADHAR_NUMBER || "
            + "mbs.NOMINEE2_BANK_ACCOUNT_NUMBER || mbs.NOMINEE2_BANK_ACCOUNT_TYPE || mbs.NOMINEE2_IFCS_CODE || mbs.NOMINEE2_BANK_NAME || "
            + "mbs.NOMINEE2_BANK_BRANCH || mbs.NOMINEE2_PERCENTAGE || mbs.NOMINEE2_CODE  IS NOT NULL ) "
            //Nominee 3
            + "OR ((mbs.NOMINEE3_RELATION_SHIP_ID IS NULL OR mbs.NOMINEE3_BANK_ACCOUNT_TYPE_ID IS NULL "
            + "OR mbs.NOMINEE3_BANK_NAME_ID IS NULL OR mbs.NOMINEE3_BANK_BRANCH_ID IS NULL) AND mbs.NOMINEE3_RELATION_SHIP || "
            + "mbs.NOMINEE3_CONTACT_NUMBER || mbs.NOMINEE3_DATE_OF_BIRTH || mbs.NOMINEE3_PAN_NUMBER || mbs.NOMINEE3_AADHAR_NUMBER || "
            + "mbs.NOMINEE3_BANK_ACCOUNT_NUMBER || mbs.NOMINEE3_BANK_ACCOUNT_TYPE || mbs.NOMINEE3_IFCS_CODE || mbs.NOMINEE3_BANK_NAME || "
            + "mbs.NOMINEE3_BANK_BRANCH || mbs.NOMINEE3_PERCENTAGE  || mbs.NOMINEE3_CODE IS NOT NULL) "
            //Appointee 1
            + "OR ((mbs.APPOINTEE1_RELATION_SHIP_ID IS NULL OR mbs.APPOINTEE1_ACCOUNT_TYPE_ID IS NULL "
            + "OR mbs.APPOINTEE1_BANK_NAME_ID IS NULL OR mbs.APPOINTEE1_BANK_NAME_ID IS NULL) AND mbs.APPOINTEE1_MEMBER_NOMINEE || "
            + "mbs.APPOINTEE1_CODE || mbs.APPOINTEE1_NAME || mbs.APPOINTEE1_RELATION_SHIP || mbs.APPOINTEE1_CONTACT_NUMBER || "
            + "mbs.APPOINTEE1_DATE_OF_BIRTH || mbs.APPOINTEE1_PAN_NUMBER || mbs.APPOINTEE1_AADHAR_NUMBER || mbs.APPOINTEE1_BANK_ACCOUNT_NUMBER || "
            + "mbs.APPOINTEE1_ACCOUNT_TYPE || mbs.APPOINTEE1_IFCS_CODE || mbs.APPOINTEE1_BANK_NAME || mbs.APPOINTEE1_BANK_BRANCH "
            + "|| mbs.APPOINTEE1_PERCENTAGE IS NOT NULL) "
            //Appointee 2
            + "OR ((mbs.APPOINTEE2_RELATION_SHIP_ID IS NULL OR mbs.APPOINTEE2_ACCOUNT_TYPE_ID IS NULL "
            + "OR mbs.APPOINTEE2_BANK_NAME_ID IS NULL OR mbs.APPOINTEE2_BANK_NAME_ID IS NULL) AND mbs.APPOINTEE2_MEMBER_NOMINEE || "
            + "mbs.APPOINTEE2_CODE || mbs.APPOINTEE2_NAME || mbs.APPOINTEE2_RELATION_SHIP || mbs.APPOINTEE2_CONTACT_NUMBER || "
            + "mbs.APPOINTEE2_DATE_OF_BIRTH || mbs.APPOINTEE2_PAN_NUMBER || mbs.APPOINTEE2_AADHAR_NUMBER || mbs.APPOINTEE2_BANK_ACCOUNT_NUMBER || "
            + "mbs.APPOINTEE2_ACCOUNT_TYPE || mbs.APPOINTEE2_IFCS_CODE || mbs.APPOINTEE2_BANK_NAME || mbs.APPOINTEE2_BANK_BRANCH "
            + "|| mbs.APPOINTEE2_PERCENTAGE IS NOT NULL) "
            //Appointee 3
            + "OR ((mbs.APPOINTEE3_RELATION_SHIP_ID IS NULL OR mbs.APPOINTEE3_ACCOUNT_TYPE_ID IS NULL "
            + "OR mbs.APPOINTEE3_BANK_NAME_ID IS NULL OR mbs.APPOINTEE3_BANK_NAME_ID IS NULL) AND mbs.APPOINTEE3_MEMBER_NOMINEE || "
            + "mbs.APPOINTEE3_CODE || mbs.APPOINTEE3_NAME || mbs.APPOINTEE3_RELATION_SHIP || mbs.APPOINTEE3_CONTACT_NUMBER || "
            + "mbs.APPOINTEE3_DATE_OF_BIRTH || mbs.APPOINTEE3_PAN_NUMBER || mbs.APPOINTEE3_AADHAR_NUMBER || mbs.APPOINTEE3_BANK_ACCOUNT_NUMBER || "
            + "mbs.APPOINTEE3_ACCOUNT_TYPE || mbs.APPOINTEE3_IFCS_CODE || mbs.APPOINTEE3_BANK_NAME || mbs.APPOINTEE3_BANK_BRANCH "
            + "|| mbs.APPOINTEE3_PERCENTAGE IS NOT NULL)) "
            + "AND mbs.MEMBER_BATCH_ID = ?1)", nativeQuery=true)
	void markBadRecords(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value = "UPDATE MemberBulkStgEntity mbs SET mbs.isActive=0, mbs.modifiedDate=CURRENT_TIMESTAMP, mbs.modifiedBy=:username WHERE mbs.memberBatchId=:batchId")
	void inActivateRecordStatus(@Param("batchId") Long batchId, @Param("username") String username);
	
	@Modifying
	@Query(value = "DELETE FROM MemberBulkStgEntity mbs WHERE mbs.memberBatchId=:batchId")
	void deleteByBatchId(Long batchId);
	
	@Query(value="SELECT LIFE_COVER_TYPE_ID FROM QSTG_LIFE_COVER WHERE QUOTATION_ID IN "
			+ "(SELECT QUOTATION_ID FROM QSTG_MEMBER_BATCH  WHERE BATCH_ID = ?) ", nativeQuery=true)
	Long findLifeCoverageId(Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.CATEGORY_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.CATEGORY)) AND pli.PICK_LIST_ID=12) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateFlatCategoryId(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.CATEGORY_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.CATEGORY)) AND pli.PICK_LIST_ID=36) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateFSGCategoryId(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="Update QSTG_MEMBER_BULK_STG SET RECORD_STATUS = 'B',ERROR_CODE='PNV' "
			+ "WHERE MEMBER_BATCH_ID  = :batchId AND PROPOSAL_POLICY_NUMBER NOT IN "
			+ "(SELECT PROPOSAL_NUMBER  FROM QSTG_QUOTATION WHERE QUOTATION_ID IN (SELECT QUOTATION_ID FROM "
			+ "QSTG_MEMBER_BATCH WHERE batch_id = :batchId))",nativeQuery = true)
	void updateRecordStatus(@Param("batchId") Long batchId);
	
	@Modifying
	@Query(value="Update QSTG_MEMBER_BULK_STG SET RECORD_STATUS = 'B',ERROR_CODE='PNV' "
			+ "WHERE MEMBER_BATCH_ID  = :batchId AND PROPOSAL_POLICY_NUMBER NOT IN "
			+ "(SELECT POLICY_NUMBER  FROM PMST_TMP_POLICY WHERE POLICY_ID IN (SELECT TMP_POLICY_ID FROM "
			+ "QSTG_MEMBER_BATCH WHERE batch_id = :batchId))",nativeQuery = true)
	void updateRecordStatusForTmp(@Param("batchId") Long batchId);

	@Modifying
	@Query(value="Update QSTG_MEMBER_BULK_STG qmbss SET RECORD_STATUS = 'B',ERROR_CODE='RAS'\r\n"
			+ "WHERE qmbss.MEMBER_BULK_STG_ID  in\r\n"
			+ "(SELECT  x.MEMBER_BULK_STG_ID\r\n"
			+ "FROM\r\n"
			+ "(SELECT  qlc.RETIREMENT_AGE,qlc.CATEGORY_ID,qmbs.MEMBER_BULK_STG_ID,qmbs.MEMBER_BATCH_ID,\r\n"
			+ "(EXTRACT (DAY FROM Current_Date )/365 + EXTRACT ( MONTH FROM Current_Date )/12 + EXTRACT (YEAR FROM Current_Date))-\r\n"
			+ "(EXTRACT (DAY FROM qmbs.DATE_OF_BIRTH )/365 + EXTRACT ( MONTH FROM qmbs.DATE_OF_BIRTH )/12 + EXTRACT (YEAR FROM qmbs.DATE_OF_BIRTH)) \r\n"
			+ "AS Diff\r\n"
			+ "FROM QSTG_LIFE_COVER qlc\r\n"
			+ "JOIN QSTG_MEMBER_BULK_STG qmbs ON qlc.CATEGORY_ID =qmbs.CATEGORY_ID WHERE QUOTATION_ID IN \r\n"
			+ "(SELECT qmb.QUOTATION_ID FROM QSTG_MEMBER_BATCH qmb where qmb.BATCH_ID =:batchId)) x where x.Diff>x.RETIREMENT_AGE)",nativeQuery = true)
	void updateRetirementAgeStatus(Long batchId);
	
	//for memberUpload Renewal
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.CATEGORY_ID = (SELECT mc.MEMBER_CATEGORY_ID FROM MEMBER_CATEGORY mc "
			+ "WHERE LOWER(TRIM(mc.NAME)) = LOWER(TRIM(mbs.CATEGORY)) AND mc.PMST_TMP_POLICY_ID = :tmpPolicyId) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateCategoryIdRenewal(@Param("batchId") Long batchId, @Param("tmpPolicyId") Long tmpPolicyId);

	
//	for claims
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.MODE_OF_EXIT = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.MODE_OF_EXIT_NAME)) AND pli.PICK_LIST_ID=63) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateModeOfExitId(Long batchId);
	
	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.REASON_FOR_DEATH_ID = (SELECT pli.PICK_LIST_ITEM_ID  FROM PICK_LIST_ITEM pli "
			+ "WHERE LOWER(TRIM(pli.PICK_LIST_ITEM_NAME)) = LOWER(TRIM(mbs.REASON_FOR_DEATH)) AND pli.PICK_LIST_ID=64) "
			+ "WHERE mbs.MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updateReasonForDeath(Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG SET RECORD_STATUS  = 'B',"
			+ " ERROR_CODE = 'MOD'  WHERE CAST(DATE_OF_EXIT  AS DATE) != CAST(DATE_OF_DEATH  AS DATE)"
			+ " AND MODE_OF_EXIT_NAME ='Death' And MEMBER_BATCH_ID = :batchId", nativeQuery=true)
	void updatedateofexitanddeath(Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG SET RECORD_STATUS  = 'B', ERROR_CODE = 'RFD' "
			+ " WHERE REASON_FOR_DEATH_ID = 198 AND  REASON_FOR_DEATH_OTHER is NULL"
			+ "  And MEMBER_BATCH_ID = :batchId ", nativeQuery=true)
	void updateReasonForDeathOthers(Long batchId);

	@Modifying
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG SET RECORD_STATUS  = 'B', ERROR_CODE = 'POD'"
			+ " WHERE MODE_OF_EXIT = 193 AND PLACE_OF_DEATH is NULL"
			+ " And MEMBER_BATCH_ID =1200 ; ", nativeQuery=true)
	void updatePlaceOfDeath(Long batchId);

	
	
	@Modifying	
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG mbs SET mbs.PMST_MEMBER_ID =(SELECT pli.MEMBER_ID  FROM PMST_MEMBER pli"
			+ "	WHERE pli.EMPLOYEE_CODE = mbs.EMPLOYEE_CODE AND pli.LIC_ID =mbs.LIC_ID  AND pli.POLICY_ID =:pmstpolicyId)"
			+ "	WHERE mbs.MEMBER_BATCH_ID =:batchId",nativeQuery = true)
	void updatepmstmemberId(@Param ("batchId")Long batchId,@Param("pmstpolicyId") Long pmstpolicyId);
	
	@Modifying	
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG qmbs SET RECORD_STATUS  = 'B', ERROR_CODE = 'DOM'"
			+ "WHERE qmbs.DATE_OF_DEATH = NULL AND qmbs.PLACE_OF_DEATH = NULL "
			+ "AND qmbs.REASON_FOR_DEATH_OTHER = NULL  AND  qmbs.REASON_FOR_DEATH_ID =198 "
			+ "and qmbs.MODE_OF_EXIT = 193 AND  qmbs.MEMBER_BATCH_ID =:batchId",nativeQuery = true)
	void updateValidataionForOtherReason(@Param ("batchId") Long batchId);
	
	@Modifying	
	@Query(value="UPDATE QSTG_MEMBER_BULK_STG qmbs SET RECORD_STATUS  = 'B', ERROR_CODE = 'ONM'"
			+ "WHERE qmbs.DATE_OF_DEATH = NULL AND qmbs.PLACE_OF_DEATH = NULL AND  qmbs.REASON_FOR_DEATH_ID !=198"
			+ " and qmbs.MODE_OF_EXIT = 193 AND  qmbs.MEMBER_BATCH_ID =:batchId",nativeQuery = true)
	void updateValidationField(@Param ("batchId") Long batchId);
	

	@Modifying
	@Query(value = "DELETE FROM QSTG_MEMBER_BULK_STG WHERE MEMBER_BATCH_ID IN ("
			+ "SELECT BATCH_ID FROM QSTG_MEMBER_BATCH WHERE QUOTATION_ID IN (SELECT QUOTATION_ID FROM QSTG_QUOTATION WHERE PROPOSAL_NUMBER=?1))", nativeQuery = true)
	void deleteByProposalNumber(String proposalNumber);

	List<MemberBulkStgEntity> findByMemberBatchIdAndRecordStatus(Long batchId, String string);
	
}

