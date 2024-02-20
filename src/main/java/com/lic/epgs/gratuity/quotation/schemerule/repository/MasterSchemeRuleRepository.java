package com.lic.epgs.gratuity.quotation.schemerule.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.schemerule.entity.MasterSchemeRuleEntity;

/**
 * @author Gopi
 *
 */

@Repository
public interface MasterSchemeRuleRepository extends JpaRepository<MasterSchemeRuleEntity, Long> {

	MasterSchemeRuleEntity findByQuotationId(Long quotaitonId);
}
