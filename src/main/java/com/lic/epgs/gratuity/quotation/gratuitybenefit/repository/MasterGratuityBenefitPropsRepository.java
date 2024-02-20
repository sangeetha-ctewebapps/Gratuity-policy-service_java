package com.lic.epgs.gratuity.quotation.gratuitybenefit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.gratuitybenefit.entity.MasterGratuityBenefitPropsEntity;

@Repository
public interface MasterGratuityBenefitPropsRepository extends JpaRepository<MasterGratuityBenefitPropsEntity, Long> {

}
