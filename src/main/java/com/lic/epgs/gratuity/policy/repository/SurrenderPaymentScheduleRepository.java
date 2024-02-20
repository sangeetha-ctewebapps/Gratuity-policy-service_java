package com.lic.epgs.gratuity.policy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.entity.SurrenderPaymentScheduleEntity;


@Repository
public interface SurrenderPaymentScheduleRepository extends JpaRepository<SurrenderPaymentScheduleEntity,Long>{

}
