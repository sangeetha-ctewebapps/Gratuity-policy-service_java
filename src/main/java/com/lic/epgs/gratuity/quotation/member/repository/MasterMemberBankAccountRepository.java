package com.lic.epgs.gratuity.quotation.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.member.entity.MasterMemberBankAccount;

/**
 * @author Ismail Khan A
 *
 */

@Repository
public interface MasterMemberBankAccountRepository extends JpaRepository<MasterMemberBankAccount, Long> {
	@Query(value = "UPDATE QMST_MEMBER_BANK_ACCOUNT m SET m.IS_ACTIVE=0 WHERE m.MEMBER_ID IN "
			+ "(SELECT MEMBER_ID FROM QMST_MEMBER WHERE QUOTATION_ID=?1)", nativeQuery=true)
	void inactiveByQuotationId(Long quotationId);
}