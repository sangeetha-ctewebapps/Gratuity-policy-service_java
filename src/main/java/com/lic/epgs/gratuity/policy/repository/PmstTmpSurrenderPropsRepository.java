package com.lic.epgs.gratuity.policy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.entity.PmstTmpSurrenderPropsEntity;

@Repository
public interface PmstTmpSurrenderPropsRepository extends JpaRepository<PmstTmpSurrenderPropsEntity,Long> {

}
