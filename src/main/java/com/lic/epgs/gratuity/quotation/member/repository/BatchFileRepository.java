package com.lic.epgs.gratuity.quotation.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.member.entity.MemberBatchFileEntity;

@Repository
public interface BatchFileRepository extends JpaRepository<MemberBatchFileEntity, Long> {

}
