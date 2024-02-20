package com.lic.epgs.gratuity.notifyDomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.notifyDomain.entity.NotifyDomainEntity;


@Repository
public interface NotifyDomainRepository extends JpaRepository<NotifyDomainEntity,Integer> {

}
