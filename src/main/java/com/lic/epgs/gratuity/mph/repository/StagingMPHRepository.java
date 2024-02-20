package com.lic.epgs.gratuity.mph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.mph.entity.MPHEntity;
import com.lic.epgs.gratuity.mph.entity.StagingMPHEntity;

@Repository
public interface StagingMPHRepository extends JpaRepository<StagingMPHEntity, Long>{

}
