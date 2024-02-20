package com.lic.epgs.gratuity.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.lic.epgs.gratuity.common.entity.PickListItemEntity;

/**
 * @author Gopi
 *
 */

@Repository
public interface PickListItemRepository extends JpaRepository<PickListItemEntity, Long>{

	@Query("SELECT p FROM PickListItemEntity p WHERE pickListId=:pickListId AND isActive=1 ORDER BY displayOrder")
	List<PickListItemEntity> findAllByOrderByDisplayOrderByPickListId(Long pickListId);
	
	@Query(value="SELECT * FROM PICK_LIST_ITEM WHERE PICK_LIST_ID=8",nativeQuery=true)
	List<PickListItemEntity> findQuotationStatusDetails();
}
