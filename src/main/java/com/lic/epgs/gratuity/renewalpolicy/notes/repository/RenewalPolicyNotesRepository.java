package com.lic.epgs.gratuity.renewalpolicy.notes.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.gratuity.quotation.notes.entity.NotesEntity;
import com.lic.epgs.gratuity.renewalpolicy.notes.dto.RenewalPolicyNotesDto;
import com.lic.epgs.gratuity.renewalpolicy.notes.entity.RenewalPolicyNotesEntity;

public interface RenewalPolicyNotesRepository extends JpaRepository<RenewalPolicyNotesEntity, Long> {


	@Query(value ="SELECT n FROM RenewalPolicyNotesEntity n WHERE n.tmpPolicyId=:tmpPolicyId AND n.notesType=:noteType ORDER BY n.createdDate DESC")
	List<RenewalPolicyNotesEntity> findBytmpPolicyIdAndNotesType(Long tmpPolicyId, String noteType);

	@Modifying
	@Query(value ="DELETE from PMST_TMP_NOTES WHERE TMP_POLICY_ID=?1",nativeQuery = true)
	void deleteBytmpPolicyId(Long id);

	//List<RenewalPolicyNotesEntity> findByPolicyId(Long policyId);
}

