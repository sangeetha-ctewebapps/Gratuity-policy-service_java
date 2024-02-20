package com.lic.epgs.gratuity.policyservice.freelookcancellation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policyservice.freelookcancellation.entity.FreeLookCancellationEntity;

@Repository
public interface FreeLookCancellationRepository extends JpaRepository<FreeLookCancellationEntity, Long> {

	List<FreeLookCancellationEntity> findByPolicyId(Long policyId);

    @Query(value = "SELECT * FROM PMST_FREE_LOOK_CANC_PROPS WHERE POLICY_ID=?1 AND IS_ACTIVE=1", nativeQuery = true)
	FreeLookCancellationEntity findByPolicyID(Long policyId);

}
