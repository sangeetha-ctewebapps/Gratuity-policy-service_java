package com.lic.epgs.gratuity.policy.renewalpolicy.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lic.epgs.gratuity.policy.renewalpolicy.member.entity.RenewalPolicyTMPMemberAppointeeEntity;

public interface RenewalPolicyTMPMemberAppointeeRepository extends JpaRepository<RenewalPolicyTMPMemberAppointeeEntity, Long> {

	@Modifying
	@Query(value = "DELETE FROM PMST_TMP_MEMBER_APPOINTEE WHERE MEMBER_APPOINTEE_ID=:id", nativeQuery=true)
	void deleteByAppointee(@Param("id") Long id);

}
