package com.lic.epgs.gratuity.simulation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.simulation.entity.DepositEntity;

/**
 * @author Vigneshwaran
 *
 */

@Repository
public interface DepositRepository extends JpaRepository<DepositEntity, Long>{
	@Query(value = "SELECT * FROM TMP_DEPOSIT td WHERE td.PROPOSAL_NUMBER = :proposalNumber", nativeQuery=true)
	List<DepositEntity> findAllByProposalNumber(String proposalNumber);

	@Query(value = "SELECT * FROM TMP_DEPOSIT td WHERE td.POLICY_NUMBER = :policyNumber", nativeQuery=true)
	List<DepositEntity> findAllByPolicyNumber(String policyNumber);

	
}
