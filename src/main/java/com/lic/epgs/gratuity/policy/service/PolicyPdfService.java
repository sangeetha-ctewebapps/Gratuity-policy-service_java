package com.lic.epgs.gratuity.policy.service;

import java.io.IOException;

public interface PolicyPdfService {

	String generateReport(Long policyId, String reportType)throws IOException ;

	String premiumreceipt(Long masterpolicyId) throws IOException ;

	String getcandbsheetpdf(Long policyId, Long taggedStatusId)throws IOException;

	String generatepaymentvoucher(String userName, Long policyId)throws IOException;

}
