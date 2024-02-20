package com.lic.epgs.gratuity.policy.notes.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.policy.notes.dto.StagingPolicyNotesDto;
import com.lic.epgs.gratuity.policy.notes.entity.StagingPolicyNoteEntity;

public interface StagingPolicyNoteRepository extends JpaRepository<StagingPolicyNoteEntity, Long>{

	@Query(value ="SELECT n FROM StagingPolicyNoteEntity n WHERE n.policyId=:policyId AND n.notesType=:notesType ORDER BY n.createdDate DESC")
	List<StagingPolicyNoteEntity> findByPolicyIdAndNotesType(Long policyId, String notesType);


}
