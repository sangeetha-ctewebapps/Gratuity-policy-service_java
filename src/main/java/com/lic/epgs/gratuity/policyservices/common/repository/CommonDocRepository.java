package com.lic.epgs.gratuity.policyservices.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policyservices.common.entity.CommonDocEntity;

public interface CommonDocRepository extends JpaRepository<CommonDocEntity, Long> {

	List<CommonDocEntity> findByServiceNumberAndIsDeleted(String serviceNumber, boolean b);

}
