package com.lic.epgs.gratuity.policy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.entity.DocumentStorageEntity;
import com.lic.epgs.gratuity.policy.entity.DocumentStorageEntity.DocumentStorageEntityBuilder;
import com.lic.epgs.gratuity.policy.entity.StagingPolicyEntity;

@Repository
public interface DocumentStorageRepository extends JpaRepository<DocumentStorageEntity, Long> {

	DocumentStorageEntity save(DocumentStorageEntityBuilder docBlob);

	DocumentStorageEntity findBypolicyid(Long id);

}
