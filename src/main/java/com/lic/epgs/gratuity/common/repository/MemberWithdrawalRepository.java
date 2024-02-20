package com.lic.epgs.gratuity.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.common.entity.MemberWithdrawalEntity;


@Repository
public interface MemberWithdrawalRepository extends JpaRepository<MemberWithdrawalEntity, Long> {

//	List<MemberWithdrawalEntity> findAll();
	
	
}
