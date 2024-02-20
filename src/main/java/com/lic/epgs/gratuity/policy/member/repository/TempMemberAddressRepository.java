package com.lic.epgs.gratuity.policy.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.policy.member.entity.TempMemberAddressEntity;

public interface TempMemberAddressRepository extends JpaRepository<TempMemberAddressEntity, Long> {

}
