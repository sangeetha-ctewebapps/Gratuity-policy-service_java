package com.lic.epgs.gratuity.notifyDomain.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.notifyDomain.dto.NotifyDomainDto;
import com.lic.epgs.gratuity.notifyDomain.service.NotifyDomainService;



@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/policy/")
public class NotifyDomainController {
	
	@Autowired
	NotifyDomainService notifyDomainService;
	
	
	@PostMapping(value = "/notifyDomain")
	ResponseEntity<Object> notifyDomain(@RequestBody NotifyDomainDto notifyDomainDto) {
		return ResponseEntity.ok(notifyDomainService.notifyDomain(notifyDomainDto));
	}
	
	
	

}
