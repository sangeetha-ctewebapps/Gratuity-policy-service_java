package com.lic.epgs.gratuity.policy.renewalpolicy.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberAddressEntity;

public interface RenewalPolicyTMPMemberAddressRepository extends JpaRepository<RenewalPolicyTMPMemberAddressEntity, Long>{

	@Modifying
	@Query(value = "DELETE FROM PMST_TMP_MEMBER_ADDRESS WHERE MEMBER_ADDRESS_ID=:id", nativeQuery=true)
	void deleteByAddress(@Param("id") Long id);

}
