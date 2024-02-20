package com.lic.epgs.gratuity.notifyDomain.serviceImpl;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.gratuity.common.utils.DateUtils;
import com.lic.epgs.gratuity.notifyDomain.constants.NotifyDomainConstants;
import com.lic.epgs.gratuity.notifyDomain.dto.CommonResponseDto;
import com.lic.epgs.gratuity.notifyDomain.dto.NotifyDomainDto;
import com.lic.epgs.gratuity.notifyDomain.dto.ResponseDto;
import com.lic.epgs.gratuity.notifyDomain.entity.NotifyDomainEntity;
import com.lic.epgs.gratuity.notifyDomain.repository.NotifyDomainRepository;
import com.lic.epgs.gratuity.notifyDomain.service.NotifyDomainService;



@Service
public class NotifyDomainServiceImpl implements NotifyDomainService  {
	
	private final Logger logger = LogManager.getLogger(NotifyDomainServiceImpl.class);
	
	@Autowired
	NotifyDomainRepository notifyDomainRepository;
	
	
	public CommonResponseDto notifyDomain(NotifyDomainDto notifyDomainDto) {
		CommonResponseDto commonResponseDto = new CommonResponseDto();
		ResponseDto responseDto = new ResponseDto();
		try {
			NotifyDomainEntity notifyDomainEntity = new NotifyDomainEntity();
			notifyDomainEntity.setDemandNo(notifyDomainDto.getDemandNo()); 
			notifyDomainEntity.setStatus(NotifyDomainConstants.SUCCESS_MSG);
			notifyDomainEntity.setRejectReason(notifyDomainDto.getRejectReason());
			notifyDomainEntity.setDateOfCollection(DateUtils.convertStringToDate(notifyDomainDto.getDateOfCollection()));
			notifyDomainEntity.setUtrNumber(notifyDomainDto.getUtrNumber());
			notifyDomainEntity.setLicReferenceId(notifyDomainDto.getLicReferenceId());
			notifyDomainEntity.setVirtualAccNo(notifyDomainDto.getVirtualAccountNumber());
			notifyDomainEntity.setTransactionAmount(notifyDomainDto.getTransactionAmount());
			notifyDomainEntity.setRemitterName(notifyDomainDto.getRemitterName());
			notifyDomainEntity.setRemitterAccNo(notifyDomainDto.getReceiverAccountNumber());
			notifyDomainEntity.setRemitterIfscCode(notifyDomainDto.getRemitterIfscCode());
			notifyDomainEntity.setRemToReceiverInform(notifyDomainDto.getRemitterToReceiverInformation());
			notifyDomainEntity.setBankTransRefNo(notifyDomainDto.getBankTransactionRefNo());
			notifyDomainEntity.setClientCode(notifyDomainDto.getClientCode());
			notifyDomainEntity.setMode(notifyDomainDto.getMode());
			notifyDomainEntity.setReceiverAccNo(notifyDomainDto.getReceiverAccountNumber());
			notifyDomainEntity.setReceiverBankId(notifyDomainDto.getReceiverBankId());
			notifyDomainEntity.setReceiverBankIfsc(notifyDomainDto.getReceiverBankIfsc());
			notifyDomainEntity.setRemitterBankName(notifyDomainDto.getRemitterBankName());
			notifyDomainEntity.setRemitterBankBranch(notifyDomainDto.getRemitterBankBranch());
			notifyDomainEntity.setCollectionNo(notifyDomainDto.getCollectionNo());
			notifyDomainEntity.setRefNo(notifyDomainDto.getRefNo());
			notifyDomainRepository.save(notifyDomainEntity);
			responseDto.setDemandNo(notifyDomainEntity.getDemandNo());
			responseDto.setCollectionNo(notifyDomainEntity.getCollectionNo());
			responseDto.setStatus(0);
			responseDto.setRefNo(notifyDomainEntity.getRefNo());
			commonResponseDto.setSuperAnnuationNotificationResponseModel(responseDto);
			
		}
		catch (ConstraintViolationException ce) {
			logger.error("Exception-- NotifyDomainServiceImpl-- saveNotifyDomainDetails --", ce);
			responseDto.setStatus(1);
			responseDto.setDemandNo(null);
			responseDto.setCollectionNo(null);
			responseDto.setRefNo(null);
			commonResponseDto.setSuperAnnuationNotificationResponseModel(responseDto);
		}
		return commonResponseDto;
	}
	

}
