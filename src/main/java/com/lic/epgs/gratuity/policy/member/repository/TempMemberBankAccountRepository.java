package com.lic.epgs.gratuity.policy.member.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lic.epgs.gratuity.policy.member.entity.TempMemberBankAccountEntity;


public interface TempMemberBankAccountRepository extends JpaRepository<TempMemberBankAccountEntity, Long> {

	

	@Modifying
	@Query(value="DELETE FROM PMST_TMP_MEMBER_BANK_ACCOUNT WHERE MEMBER_BANK_ACCOUNT_ID  = :memberBankId",nativeQuery=true)
	void deleteByMemberId(@Param("memberBankId") Long memberBankId);
	
	

}
