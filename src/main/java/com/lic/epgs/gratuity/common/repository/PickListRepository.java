package com.lic.epgs.gratuity.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.common.entity.PickListEntity;

/**
 * @author Gopi
 *
 */

@Repository
public interface PickListRepository extends JpaRepository<PickListEntity, Long> {

	@Query("SELECT p FROM PickListEntity p WHERE IS_ACTIVE =1 ORDER  BY id ASC")
	List<PickListEntity> findAllByOrderById();

}
