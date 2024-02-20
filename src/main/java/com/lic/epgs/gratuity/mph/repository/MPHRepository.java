package com.lic.epgs.gratuity.mph.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.mph.entity.MPHEntity;

@Repository
public interface MPHRepository extends JpaRepository<MPHEntity, Long> {
	@Query(value = "select m.member_id,m.lic_id, m.policy_id " + "from PMST_MEMBER m   "
			+ " where  m.policy_id =:policyId AND m.is_active = 1", nativeQuery = true)
	List<Object[]> findDetailsByPolicyId(String policyId);

}
