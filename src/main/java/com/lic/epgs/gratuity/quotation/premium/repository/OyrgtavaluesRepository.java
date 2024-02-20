package com.lic.epgs.gratuity.quotation.premium.repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.quotation.premium.entity.OyrgtavaluesEntity;


public interface OyrgtavaluesRepository extends JpaRepository<OyrgtavaluesEntity, Long> {

	@Modifying
	@Query(value = "SELECT DISTINCT selector FROM OYRGTAVALUES o WHERE SELECTOR LIKE '%X0_%' ORDER BY SELECTOR", nativeQuery = true)
	ArrayList<Object> findAllKeyValues();

	@Modifying
	@Query(value="SELECT DISTINCT selector AS key FROM OYRGTAVALUES o WHERE SELECTOR LIKE '%X04%' ORDER BY SELECTOR", nativeQuery = true)
	ArrayList<Object> findDefaultValues();

		

}
