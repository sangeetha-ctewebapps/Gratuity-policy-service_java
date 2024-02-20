package com.lic.epgs.gratuity.policy.member.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.member.entity.TempMemberAppointeeEntity;

@Repository
public interface TempMemberAppointeeRepository extends JpaRepository<TempMemberAppointeeEntity, Long>{

	
	@Modifying
	@Query(value="DELETE FROM PMST_TMP_MEMBER_APPOINTEE WHERE MEMBER_APPOINTEE_ID = :appointeeId",nativeQuery=true)
	void deleteByAppointee(@Param("appointeeId") Long appointeeId);

	
	@Modifying
	@Query(value="DELETE FROM PMST_TMP_MEMBER_APPOINTEE WHERE MEMBER_NOMINEE_ID = :nomineeId",nativeQuery=true)
	void deleteByNomiee(@Param("nomineeId") Long nomineeId);
	
	

}
