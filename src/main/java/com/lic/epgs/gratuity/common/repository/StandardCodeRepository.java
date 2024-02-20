package com.lic.epgs.gratuity.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.policy.surrender.entity.SurrConfigEntity;

/**
 * @author Gopi
 *
 */

@Repository
public interface StandardCodeRepository extends JpaRepository<StandardCodeEntity, Long>{

	@Query("SELECT s FROM StandardCodeEntity s WHERE s.id IN :ids")
	List<StandardCodeEntity> findMultiple(Long[] ids);

	StandardCodeEntity findById(String string);
	
	@Query(value="SELECT * FROM STANDARDCODE WHERE STANDARDCODE_NAME =:name",nativeQuery=true)
	public StandardCodeEntity getStandardCodeDetailsUsingName(String name);
}
