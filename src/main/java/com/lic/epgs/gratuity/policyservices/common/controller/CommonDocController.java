package com.lic.epgs.gratuity.policyservices.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.policyservices.common.entity.CommonDocEntity;
import com.lic.epgs.gratuity.policyservices.common.service.impl.CommonDocServiceImpl;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/common/document" })
public class CommonDocController {

	@Autowired
	CommonDocServiceImpl policyServiceCommonDocService;

	@PostMapping("save")
	public ResponseEntity<?> saveDocument(@RequestBody List<CommonDocEntity> commonDocsDto) {

		return new ResponseEntity<>(policyServiceCommonDocService.saveDocument(commonDocsDto), HttpStatus.OK);
	}

	@PutMapping("softDelete/{documentId}/{modifiedBy}")
	public ResponseEntity<?> deleteDocument(@PathVariable("documentId") Long documentId,
			@PathVariable("modifiedBy") String modifiedBy) {

		return new ResponseEntity<>(policyServiceCommonDocService.deleteDocument(documentId, modifiedBy),
				HttpStatus.OK);
	}

	@GetMapping("getDocumentsList/{serviceNumber}")
	public ResponseEntity<?> getDocumnetList(@PathVariable("serviceNumber") String serviceNumber) {

		return new ResponseEntity<>(policyServiceCommonDocService.getDocumentList(serviceNumber), HttpStatus.OK);

	}

}
