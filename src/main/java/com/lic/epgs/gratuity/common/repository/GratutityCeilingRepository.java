package com.lic.epgs.gratuity.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.common.entity.GratutityCeilingEntity;

@Repository
public interface GratutityCeilingRepository extends JpaRepository<GratutityCeilingEntity, Long> {

}
