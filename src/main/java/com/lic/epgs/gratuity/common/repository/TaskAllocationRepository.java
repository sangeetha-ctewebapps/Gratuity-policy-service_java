package com.lic.epgs.gratuity.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.common.entity.TaskAllocationEntity;

@Repository
public interface TaskAllocationRepository extends JpaRepository<TaskAllocationEntity, Long> {

	@Query(value = "SELECT * FROM TASK_ALLOC where REQUEST_ID=:number", nativeQuery = true)
	TaskAllocationEntity findByRequestId(String number);

	@Query(value = "SELECT * FROM TASK_ALLOC WHERE TASK_STATUS  IN (21,22,133,134,204,205,206,208,210,213,16)", nativeQuery = true)
	List<TaskAllocationEntity> findAllocationtoMakerandCheckerStatus();

	TaskAllocationEntity findBymodulePrimaryId(Long id);

	TaskAllocationEntity findTopByRequestId(String quotationNumber);

	TaskAllocationEntity getByRequestId(String requestId);

}
