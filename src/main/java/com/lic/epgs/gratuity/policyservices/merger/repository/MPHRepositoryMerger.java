package com.lic.epgs.gratuity.policyservices.merger.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.mph.entity.MPHEntity;
public interface MPHRepositoryMerger  extends JpaRepository<MPHEntity, Long> {



	MPHEntity findByIdAndIsActiveTrue(Long id);

}
