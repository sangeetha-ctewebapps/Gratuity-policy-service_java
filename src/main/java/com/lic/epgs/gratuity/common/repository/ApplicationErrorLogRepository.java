package com.lic.epgs.gratuity.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.common.entity.ApplicationErrorLogEntity;

/**
 * @author Gopi
 *
 */

@Repository
public interface ApplicationErrorLogRepository extends JpaRepository<ApplicationErrorLogEntity, Long>{

}
