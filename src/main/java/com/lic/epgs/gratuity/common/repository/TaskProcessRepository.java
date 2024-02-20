package com.lic.epgs.gratuity.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.common.entity.TaskProcessEntity;

@Repository
public interface TaskProcessRepository extends JpaRepository<TaskProcessEntity, Long>{

	TaskProcessEntity findByProcessName(String processName);
	
	@Query(value ="select TASK_PR_ID from TASK_PROCESS where PROCESS_NAME =?1 ",nativeQuery = true)
	Long getTaskIdByProcessName(String processName);
	
	@Query(value="Select PROCESS_NAME from TASK_PROCESS where TASK_PR_ID=?1 ",nativeQuery = true)
	String getModulType(Long taskProcessTaskPrId);
	
	@Query(value="select * from task_process where PROCESS_NAME=?1",nativeQuery = true)
	TaskProcessEntity getByTaskProcess(String processName);

}
