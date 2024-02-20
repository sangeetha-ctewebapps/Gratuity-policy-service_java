package com.lic.epgs.gratuity.policy.renewal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.common.entity.StandardCodeEntity;
import com.lic.epgs.gratuity.policy.entity.MasterPolicyEntity;
import com.lic.epgs.gratuity.policy.renewal.entity.EmailMessagesEntity;

@Repository
public interface EmailMessagesRepository extends JpaRepository<EmailMessagesEntity, Long> {

	@Query(value="SELECT * FROM EMAIL_MESSAGES WHERE EMAIL_MESSAGES_ID =1",nativeQuery = true)
	EmailMessagesEntity findByprimaryId(Long emailId);
	

}
