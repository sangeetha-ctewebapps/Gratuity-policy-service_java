package com.lic.epgs.gratuity.policyservices.conversion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.member.entity.TempMemberNomineeEntity;

@Repository
public interface PsTempMemberNomineeEntityRepository extends JpaRepository<TempMemberNomineeEntity, Long> {

}
