package com.lic.epgs.gratuity.policy.surrender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.surrender.entity.SurrenderExitLoadEntity;

@Repository
public interface SurrenderExitLoadRepository extends JpaRepository<SurrenderExitLoadEntity, Long>{

	@Query(value="SELECT * FROM SURR_EXIT_LOAD WHERE SURR_RANGE =:range and POLICY_DURATION =:policyDuration",nativeQuery=true)
	public SurrenderExitLoadEntity getSurrenderExitLoadDetailsUsingRange(String range, String policyDuration);
}
