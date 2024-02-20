package com.lic.epgs.gratuity.quotation.premium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.premium.entity.RateTableSelectionEntity;



@Repository
public interface RateTableSelectionRepository extends JpaRepository<RateTableSelectionEntity, Long>{

	@Query(value = "select * from RATE_TABLE_SELECTION where RISK_GROUP=?1",nativeQuery = true)
	List<RateTableSelectionEntity> findByGroupName(String groupname);

	@Query(value = "select distinct(RATE_TABLE) from RATE_TABLE_SELECTION where SUBSTR(RATE_TABLE,2,3) >= ?1 order by RATE_TABLE desc",nativeQuery = true)
	List<String> getratetable(String get);
	
	@Query(value = "select distinct(RATE_TABLE) from RATE_TABLE_SELECTION where SUBSTR(RATE_TABLE,2,3) >= ?1 order by RATE_TABLE desc",nativeQuery = true)
	List<RateTableSelectionEntity> getratetablewith(String get);

}
