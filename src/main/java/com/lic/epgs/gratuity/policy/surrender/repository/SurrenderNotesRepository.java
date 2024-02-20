package com.lic.epgs.gratuity.policy.surrender.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lic.epgs.gratuity.policy.surrender.entity.SurrenderNotes;


public interface SurrenderNotesRepository extends JpaRepository<SurrenderNotes, Long> {

	@Query(value = "Select SURRENDER_NOTE_ID,NOTES_TEXT,USER_ID,SURRENDER_ID,MODIFIED_BY,MODIFIED_ON from SURRENDER_NOTES  where SURRENDER_ID =:surrenderId ORDER BY MODIFIED_ON DESC",nativeQuery = true)
	List<Map<String, String>> findBySurrenderId(@Param("surrenderId") Long surrenderId);

	@Query(value = "Select SURRENDER_NOTE_ID,NOTES_TEXT,USER_ID,SURRENDER_ID,MODIFIED_BY,MODIFIED_ON from SURRENDER_NOTES where SURRENDER_ID =:surrenderId ORDER BY MODIFIED_ON DESC",nativeQuery = true)
	SurrenderNotes getNotesBySurrenderId(Long surrenderId);
	
	//gsli
	@Query(value = "SELECT SURR_NOTES_ID, NOTES_TEXT AS NOTE, USER_ID, SURRENDER_ID, TO_CHAR(MODIFIED_ON,'dd-MM-yyyy HH:MI:SS') AS CREATE_DATE,\r\n" + 
			"MODIFIED_BY AS CREATED_BY\r\n" + 
			"FROM SURR_NOTES \r\n" + 
			"WHERE SURRENDER_ID=:surrenderId \r\n" + 
			"ORDER BY MODIFIED_ON DESC",nativeQuery = true)
	public List<Map<String,Object>> getSurrenderNotes(Long surrenderId);
	
}
