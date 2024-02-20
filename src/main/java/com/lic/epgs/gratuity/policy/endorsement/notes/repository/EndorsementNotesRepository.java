package com.lic.epgs.gratuity.policy.endorsement.notes.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.endorsement.notes.entity.EndorsementNotesEntity;

public interface EndorsementNotesRepository extends JpaRepository<EndorsementNotesEntity, Long> {


	@Query(value ="SELECT n FROM EndorsementNotesEntity n WHERE n.endorsementId=:endorsementId AND n.notesType=:notesType ORDER BY n.createdDate DESC")
	List<EndorsementNotesEntity> findByEndorsementIdAndNotesType(Long endorsementId, String notesType);

}
