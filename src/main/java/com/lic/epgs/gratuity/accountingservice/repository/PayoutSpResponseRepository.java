package com.lic.epgs.gratuity.accountingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.gratuity.accountingservice.entity.PayoutSpResponseEntity;

public interface PayoutSpResponseRepository  extends JpaRepository<PayoutSpResponseEntity, Long>{

	PayoutSpResponseEntity findBypayoutNumber(String payoutNo);
 

}
