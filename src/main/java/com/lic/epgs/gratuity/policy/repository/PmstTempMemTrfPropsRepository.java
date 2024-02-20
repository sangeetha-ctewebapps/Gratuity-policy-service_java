package com.lic.epgs.gratuity.policy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.entity.PmstTempMemTrfPropsEntity;

@Repository
public interface PmstTempMemTrfPropsRepository  extends JpaRepository<PmstTempMemTrfPropsEntity, Long>{
	
	

}
