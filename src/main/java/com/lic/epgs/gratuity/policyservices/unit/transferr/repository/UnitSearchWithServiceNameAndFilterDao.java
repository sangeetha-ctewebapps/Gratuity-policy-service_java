package com.lic.epgs.gratuity.policyservices.unit.transferr.repository;

import java.text.ParseException;
import java.util.List;

import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.DocumentDetailsResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.ExactMatchTransferSearchRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.MemberTransferOverviewDetailsRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.ServiceMemberTransferDetailsResponse;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferMemberSearchWithFilterRequest;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.dto.TransferSearchWithServiceResponse;
import com.lic.epgs.gratuity.policyservices.unit.transfer.dto.UnitDocumentDetailsResponse;

public interface UnitSearchWithServiceNameAndFilterDao {
	
	public List<TransferSearchWithServiceResponse> searchWithServiceNumber(ExactMatchTransferSearchRequest exactMatchTransferSearchRequest) throws ParseException;

	public List<TransferSearchWithServiceResponse> getTransferMemberDetailsWithFilter(TransferMemberSearchWithFilterRequest transferMemberSearchWithFilterRequest);
	
	public List<ServiceMemberTransferDetailsResponse> getSeviceDetails(Long transferRequisitionId);

	public List<TransferSearchWithServiceResponse> getOverviewDetails(Long transferRequisitionId);
	
	public List<UnitDocumentDetailsResponse> getDocumentDetails(Long unitTransferRequisitionId);


}
