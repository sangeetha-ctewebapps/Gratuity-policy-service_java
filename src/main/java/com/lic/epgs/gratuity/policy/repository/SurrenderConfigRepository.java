package com.lic.epgs.gratuity.policy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.gratuity.policy.entity.SurrenderConfigEntity;


@Repository
public interface SurrenderConfigRepository extends JpaRepository<SurrenderConfigEntity,Long>{

}
