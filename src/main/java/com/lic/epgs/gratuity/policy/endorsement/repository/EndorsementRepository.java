package com.lic.epgs.gratuity.policy.endorsement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.endorsement.dto.EndorsementDto;
import com.lic.epgs.gratuity.policy.endorsement.entity.EndorsementEntity;

@Repository
public interface EndorsementRepository extends JpaRepository<EndorsementEntity, Long>{

	List<EndorsementEntity> findBypolicyId(Long policyId);
	
	@Query(value="SELECT MAX(TO_NUMBER(SERVICE_NUMBER)) AS MAXSERVICENUMBER FROM PMST_ENDORSEMENT", nativeQuery =true)
	Long maxServiceNumber();

	@Query(value="SELECT * FROM PMST_ENDORSEMENT  WHERE ENDORSEMENT_ID =?  and ENDORSEMENT_STATUS_ID !=157 AND  ENDORSEMENT_STATUS_ID !=158", nativeQuery =true)
	Optional<EndorsementEntity> isInitiated(Long RndorsementId);


}
