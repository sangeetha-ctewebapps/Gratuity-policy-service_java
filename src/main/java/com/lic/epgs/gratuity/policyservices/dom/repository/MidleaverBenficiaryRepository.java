package com.lic.epgs.gratuity.policyservices.dom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policyservices.dom.entity.MidleaverBenficiaryEntity;

@Repository
public interface MidleaverBenficiaryRepository extends JpaRepository<MidleaverBenficiaryEntity, Long> {

	MidleaverBenficiaryEntity findByTmpMlPropsId(Long id);

}
