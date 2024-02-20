package com.lic.epgs.gratuity.policyservices.memberlevel.transfer.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.entity.TransferMemberPolicyDetailEntity;
import com.lic.epgs.gratuity.policyservices.memberlevel.transfer.repository.TransferMemberPolicyDetailRepo;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MemberTransferDocumentService {

	@Autowired
	TransferMemberPolicyDetailRepo transferMemberPolicyDetailRepo;
	
	@Value("${transfer.member.location}")
	private String pdfLocation;

	public Map<String, Object> downloadPdf(Long transferRequisitionId) {
		TransferMemberPolicyDetailEntity transferMemberPolicyDetailEntity = transferMemberPolicyDetailRepo
				.findPolicyByTransferRequisitionId(transferRequisitionId);
		String errorMessage = "";
		FileOutputStream fileOutputStream = null;

		Map<String, Object> response = new HashMap<String, Object>();

		try {
			{
				if (transferMemberPolicyDetailEntity != null) {
					TransferMemberPolicyDetailEntity transferMemberPolicyDetailList = transferMemberPolicyDetailRepo
							.findPolicyByTransferRequisitionId(transferRequisitionId);
					String buffer = transferMemberPolicyDetailList + " Members Added";
					String fileName = "/MemberData";
					
					String completehtmlContent = "<!DOCTYPE html><html><head>\r\n"
							+ "						</head><body><p>Members Data.</p>" + buffer + "\"/  </body></html>";

					log.info("html content - " + completehtmlContent);
					String htmlFileLoc = pdfLocation +fileName+ transferRequisitionId+ ".html";

					FileWriter fw = new FileWriter(htmlFileLoc);

					try {
						fw.write(completehtmlContent);
					} catch (IOException ioe) {
						log.info("renewalNoticePdfDownload File writer exception -" + ioe.getMessage());
					} finally {
						fw.close();
					}
					log.info("Html created");

					String pdfFileLoc =pdfLocation+ fileName+ transferRequisitionId + ".pdf";
					
					
					fileOutputStream = new FileOutputStream(pdfFileLoc);

					ITextRenderer renderer = new ITextRenderer();

					renderer.setDocument(new File(htmlFileLoc));
					renderer.layout();
					renderer.createPDF(fileOutputStream, false);
					renderer.finishPDF();
					File file = new File(pdfFileLoc);
					byte[] fileContent = Files.readAllBytes(file.toPath());
					String pdfBase64 = Base64.getEncoder().encodeToString(fileContent);
					log.info(pdfBase64);
					log.info("PDF created -" + pdfBase64);

					response.put("STATUS", 1);
					//response.put("PDF Generated Sucessfully" );
					response.put("Data", pdfBase64);
				}
			}
		} catch (IOException ioe) {
			log.info("PdfDownload exception -" + ioe.getMessage());
		} finally {

			try {

				fileOutputStream.close();
			} catch (IOException e) {
				log.info("PdfDownload IS close exception -" + e.getMessage());
			}
		}
		return response;
	}

	
	
	
}
