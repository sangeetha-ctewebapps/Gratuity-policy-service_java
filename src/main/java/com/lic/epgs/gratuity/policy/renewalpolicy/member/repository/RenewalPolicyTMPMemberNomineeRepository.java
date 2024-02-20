package com.lic.epgs.gratuity.policy.renewalpolicy.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberNomineeEntity;

public interface RenewalPolicyTMPMemberNomineeRepository extends JpaRepository<RenewalPolicyTMPMemberNomineeEntity, Long>{

	@Modifying
	@Query(value = "DELETE FROM PMST_TMP_MEMBER_NOMINEE WHERE MEMBER_NOMINEE_ID=:id", nativeQuery=true)
	void deleteByNominee(@Param("id") Long id);

}
