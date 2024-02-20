package com.lic.epgs.gratuity.policyservices.merger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policy.member.entity.PolicyMemberEntity;

public interface PolicyMemberRepositoryMerger extends JpaRepository<PolicyMemberEntity, Long> {

	List<PolicyMemberEntity> findByPolicyIdAndIsActiveTrue(Long id);
}
