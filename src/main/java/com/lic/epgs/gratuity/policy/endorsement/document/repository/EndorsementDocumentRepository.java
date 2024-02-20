package com.lic.epgs.gratuity.policy.endorsement.document.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.endorsement.document.entity.EndorsementDocumentEntity;

@Repository
public interface EndorsementDocumentRepository extends JpaRepository<EndorsementDocumentEntity, Long> {
	@Query(value = "SELECT d FROM EndorsementDocumentEntity d WHERE isDeleted=false AND tmpPolicyId=:tmpPolicyId")
	List<EndorsementDocumentEntity> findByTMPPolicyId(Long tmpPolicyId);
	
	@Query(value = "Select * from PMST_TMP_DOCUMENT where  IS_DELETED=0 and TMP_POLICY_ID=?1",nativeQuery = true)
	List<EndorsementDocumentEntity> findByTmpPolicyId(Long tmpPolicyId);

}
