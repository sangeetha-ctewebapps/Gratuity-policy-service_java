package com.lic.epgs.gratuity.policyservices.policymodification.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gratuity.common.dto.ApiResponseDto;
import com.lic.epgs.gratuity.policy.dto.PolicyDto;
import com.lic.epgs.gratuity.policy.dto.PolicySearchDto;
import com.lic.epgs.gratuity.policyservices.common.entity.TempPolicyServiceNotes;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.MPHModificationResponse;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.StatusDto;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.StatusMaster;
import com.lic.epgs.gratuity.policyservices.policymodification.dto.TempPolicyModificationDataTableRequestDto;
import com.lic.epgs.gratuity.policyservices.policymodification.service.MPHModificationService;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/mph/modification" })
public class PolicyModificationController {

	@Autowired
	MPHModificationService mphModificationService;
	
	

	protected final Logger logger = LogManager.getLogger(getClass());

	@GetMapping("{policyNumber}/{mphId}")
	public ResponseEntity<?> findById(@PathVariable("mphId") Long mphId,
			@PathVariable("policyNumber") String policyNumber) {
		logger.info("MPHModificationController - start");
		logger.info("Inputs - mphId: " + mphId + " policyNumber: " + policyNumber);
		Object response = mphModificationService.findById(policyNumber, mphId);
		logger.info("MPHModificationController - end");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("savetempmph")
	public ResponseEntity<?> saveTempMphDetails(
			@RequestBody MPHModificationResponse mPHModificationResponse) {
		
		return new ResponseEntity<>(mphModificationService.saveTempMphDetails(mPHModificationResponse), HttpStatus.OK);
	}

	@PostMapping("updateStatus")
	public StatusDto updateTempMphDetails(String policyNumber, String status) {
		return mphModificationService.updateTempMphDetails(policyNumber, status);
	}

	@RequestMapping(value = "/getPolicyModifcationMakerInprogress", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPolicyModifcationMakerInprogress(
			@RequestBody TempPolicyModificationDataTableRequestDto tempPolicyModificationDataTableRequestDto) {
		return new ResponseEntity<>(
				mphModificationService.getPolicyModifcationMakerInprogress(tempPolicyModificationDataTableRequestDto),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/getPolicyModifcationPendingForApproval", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPolicyModifcationPendingForApproval(
			@RequestBody TempPolicyModificationDataTableRequestDto tempPolicyModificationDataTableRequestDto) {
		return new ResponseEntity<>(mphModificationService.getPolicyModifcationDetailsDataTable(
				tempPolicyModificationDataTableRequestDto, StatusMaster.PENDING_FOR_APPROVAL), HttpStatus.OK);
	}

	@RequestMapping(value = "/getPolicyModifcationExisting", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPolicyModifcationExisting(
			@RequestBody TempPolicyModificationDataTableRequestDto tempPolicyModificationDataTableRequestDto) {
		return new ResponseEntity<>(
				mphModificationService.getPolicyModifcationExisting(tempPolicyModificationDataTableRequestDto),
				HttpStatus.OK);
	}

//	@GetMapping("getMPHChecker/{policyNumber}/{mphId}")
//	public MPHModificationResponseDto getMPHForChecker(@PathVariable("mphId") Long mphId,
//			@PathVariable("policyNumber") String policyNumber) {
//		logger.info("MPHModificationController - start");
//		logger.info("Inputs - mphId: " + mphId + " policyNumber: " + policyNumber);
//		MPHModificationResponseDto response = mphModificationService.getMPHForChecker(policyNumber, mphId);
//		logger.info("MPHModificationController - end");
//		return response;
//	}

	@GetMapping("getByPolicyId/{policyId}")
	public ResponseEntity<?> getByPolicyId(@PathVariable("policyId") Long policyId) {
		logger.info("getByPolicyId - start");
		logger.info(" policyId: " + policyId);
		MPHModificationResponse response = mphModificationService.getByPolicyId(policyId);
		logger.info("getByPolicyId - end");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("saveNotes")
	public ResponseEntity<?> saveNotes(
			@RequestBody TempPolicyServiceNotes tempPolicyServiceNotes) {
		
		return new ResponseEntity<>(mphModificationService.saveNotes(tempPolicyServiceNotes), HttpStatus.OK);
	}
	
	@GetMapping("getListNotes/{tempPolicyId}/{policyNumber}")
	public ResponseEntity<?> getListNotes(@PathVariable("tempPolicyId") Long tempPolicyId, @PathVariable("policyNumber") String policyNumber) {
		
		return new ResponseEntity<>(mphModificationService.getListOfNotes(tempPolicyId,policyNumber), HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/filter")
	public ApiResponseDto<List<PolicyDto>> filter(@RequestBody PolicySearchDto policySearchDto) {
		return mphModificationService.filter(policySearchDto);
	}
	
}