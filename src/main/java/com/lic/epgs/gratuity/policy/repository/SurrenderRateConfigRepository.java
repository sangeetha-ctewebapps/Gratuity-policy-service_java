package com.lic.epgs.gratuity.policy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.entity.SurrenderRateConfigEntity;

@Repository
public interface SurrenderRateConfigRepository extends JpaRepository<SurrenderRateConfigEntity,Long> {

}
