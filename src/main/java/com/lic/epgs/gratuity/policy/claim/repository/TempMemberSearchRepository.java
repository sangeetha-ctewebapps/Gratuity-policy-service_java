package com.lic.epgs.gratuity.policy.claim.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.claim.entity.TempMemberSearchEntity;
import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;

@Repository
public interface TempMemberSearchRepository extends JpaRepository<TempMemberSearchEntity, Serializable> {

	List<TempMemberSearchEntity> findByPolicyTmp(Long policyId);
	
	@Query(value="SELECT * From pmst_tmp_member where TMP_POLICY_ID=?1", nativeQuery=true)
	List<TempMemberSearchEntity> findByTmpPolicyId(Long policyId);

}
