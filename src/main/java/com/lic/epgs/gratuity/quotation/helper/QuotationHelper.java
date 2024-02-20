package com.lic.epgs.gratuity.quotation.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.lic.epgs.gratuity.policy.dto.GenerateCBSheetPdfDto;
import com.lic.epgs.gratuity.policy.dto.PolicyBondDetailDto;
import com.lic.epgs.gratuity.quotation.dto.QuotationDto;
import com.lic.epgs.gratuity.quotation.entity.MasterQuotationEntity;
import com.lic.epgs.gratuity.quotation.entity.QuotationEntity;

public class QuotationHelper {

	public static QuotationDto entityToDto(QuotationEntity entity) {
		return new ModelMapper().map(entity, QuotationDto.class);
	}

	public static QuotationDto entityToDto(MasterQuotationEntity entity) {
		return new ModelMapper().map(entity, QuotationDto.class);
	}

	public static MasterQuotationEntity entityToMasterEntity(QuotationEntity entity, Long quotationId,
			String username) {
		MasterQuotationEntity masterQuotationEntity = new ModelMapper().map(entity, MasterQuotationEntity.class);
		masterQuotationEntity.setId(null);
		masterQuotationEntity.setTempQuotationId(quotationId);
		masterQuotationEntity.setApprovalDate(new Date());
		masterQuotationEntity.setCreatedBy(username);
		masterQuotationEntity.setCreatedDate(new Date());
		masterQuotationEntity.setModifiedBy(null);
		masterQuotationEntity.setModifiedDate(null);
//		masterQuotationEntity.setBatches(null);
		return masterQuotationEntity;
	}

	public static Long nextQuotationNumber(Long lastQuotationNumber) {
		lastQuotationNumber = lastQuotationNumber == null ? 1 : lastQuotationNumber + 1;
		return lastQuotationNumber;
	}

	public static String addQuotationNumber(String proposalNumber, String addQuotationNumber) {
		String concateQuotationNumber = "";

		String getQuotationSubstring = addQuotationNumber.substring(addQuotationNumber.length() - 2);

		if (Long.parseLong(getQuotationSubstring) < 99) {

			Long lastNumberQuotation = getQuotationSubstring == null ? 01 : Long.parseLong(getQuotationSubstring) + 01;
			String lastNumber = String.format("%02d", lastNumberQuotation);
			concateQuotationNumber = proposalNumber + lastNumber;
		} else {
			return null;
		}
		return concateQuotationNumber;

	}

	public static String addQuotationNumberForMJ(String proposalNumber, String addQuotationNumber) {
		String concateQuotationNumber = "";

		String getQuotationSubstring = addQuotationNumber.substring(addQuotationNumber.length() - 2);

		if (Long.parseLong(getQuotationSubstring) < 99999) {

			Long lastNumberQuotation = getQuotationSubstring == null ? 01 : Long.parseLong(getQuotationSubstring) + 01;
			String lastNumber = String.format("%02d", lastNumberQuotation);
			concateQuotationNumber = proposalNumber + lastNumber;
		} else {
			return null;
		}
		return concateQuotationNumber;

	}

	public static QuotationEntity clone(QuotationEntity entity, String nextQuotationNumber, String username,
			Long defaultStatusId, Long defaultSubStatusId, Long defaultTaggedStatusId) {
		QuotationDto quotationDto = new ModelMapper().map(entity, QuotationDto.class);
		quotationDto.setId(null);
		QuotationEntity quotationEntity = new ModelMapper().map(quotationDto, QuotationEntity.class);
		quotationEntity.setNumber(nextQuotationNumber);
		quotationEntity.setDate(new Date());
		quotationEntity.setStatusId(defaultStatusId);
		quotationEntity.setSubStatusId(defaultSubStatusId);
		quotationEntity.setTaggedStatusID(defaultTaggedStatusId);

		quotationEntity.setIsActive(true);
		quotationEntity.setCreatedBy(username);
		quotationEntity.setCreatedDate(new Date());
		return quotationEntity;
	}

	public static MasterQuotationEntity cloneMaster(MasterQuotationEntity masterQuotationEntity,
			String nextQuotationNumber, String username, long defaultStatusId, long defaultSubStatusId,
			long defaultTaggedStatusId) {
		QuotationDto quotationDto = new ModelMapper().map(masterQuotationEntity, QuotationDto.class);
		quotationDto.setId(null);
		MasterQuotationEntity newMasterQuotationEntity = new ModelMapper().map(quotationDto,
				MasterQuotationEntity.class);
		newMasterQuotationEntity.setNumber(nextQuotationNumber);
		newMasterQuotationEntity.setDate(new Date());
		newMasterQuotationEntity.setStatusId(defaultStatusId);
		newMasterQuotationEntity.setSubStatusId(defaultSubStatusId);
		newMasterQuotationEntity.setTaggedStatusID(defaultTaggedStatusId);
		newMasterQuotationEntity.setIsActive(true);
		newMasterQuotationEntity.setCreatedBy(username);
		newMasterQuotationEntity.setCreatedDate(new Date());
		return newMasterQuotationEntity;
	}

	public static String policyBondReport(PolicyBondDetailDto policyBondDetailDto) {
		if (policyBondDetailDto.getMphAddress3().equals("null")) {
			policyBondDetailDto.setMphAddress3("");
		}
		String getpre = policyBondDetailDto.getTotalPremium();

		String reportBody = "<section class=\"content_wrapper\">	"
				+ "<p  style=\"text-align:left;\"> Master Policy No. NGGCA/</p> "

				+ " <br></br>"

				+ " <br></br>"

				+ "<div class=\"container-fluid nopadding margin-top-20 bggrey\">"
				+ "<div class=\"container text-center margin-top-30\"> " + "<div class =\"row margin-top-10\">"
				+ "<div class=\"col-md-12\">"
				+ "<h4  style=\"text-align:center;\"> LIFE INSURANCE CORPORATION OF INDIA </h4>" + "</div>" + "</div> "
				+ "</div>" + "</div>"

				+ "<div class=\"row text-bold \">" + "<div class=\"col-md-12\">"
				+ "<h5  style=\"text-align:center;\" >Pension and Group Schemes Department </h5>" + "</div>" + "</div>"

				+ "<div class=\"row text-bold \">" + "<div class=\"col-md-12\">"
				+ "<h5  style=\"text-align:center;\">(Established by the Life Insurance Corporation Act, 1956)</h5>"
				+ "</div>" + "</div>  "

				+ "<div class=\"row text-bold \">" + "<div class=\"col-md-12\">"
				+ "<h5 style=\"text-align:center;\">Registration Number: 512 </h5>" + "</div>" + "</div>	"
				+ " <br></br>"

				+ "<div class=\"row text-bold \">" + "<div class=\"col-md-12\">"
				+ "<h4 style=\"text-align:center;\">LIC's NEW GROUP GRATUITY CASH ACCUMULATION PLAN </h4>" + "</div>"
				+ "</div>	"

				+ "<div class=\"row text-bold underLine\">" + "<div class=\"col-md-12\" style=\"text-align:center;\">"
				+ "<span>(UIN: 512N281V03)</span>" + "</div>" + "</div>   "

				+ "<br />"

				+ "<div class=\"row text-bold underLine\">" + "<div class=\"col-md-12\" style=\"text-align:center;\">"
				+ "<span>(A Non-Linked, Non-participating Life Group Saving Insurance Plan)</span>" + "</div>"
				+ "</div>"

				+ "<div class=\"row underLine \">" + "<div class=\"col-md-12\">"
				+ "<h5 style=\"text-align:center;\">PART - A</h5>" + "</div>" + "</div>"

				+ "<div class=\"row\">" + "<div class=\"col-md-9 justify\" style=\"text-align:left;\">Ref : M.P.NO./"
				+ (policyBondDetailDto.getProductCode() != null ? policyBondDetailDto.getProductCode() : "") + "/"
				+ (policyBondDetailDto.getPolicyNumber() != null ? policyBondDetailDto.getPolicyNumber() : "")
				+ "</div>"
				+ "<div class=\"col-md-3 fixed\" style=\"text-align:right;\">Address &mp; Email of Branch/ P&mp;GS Unit:"
				+ "</div>" + "</div>"

				+ "<div class=\"row\">" + "<div class=\"col-md-12\">" + "<!-- <div class=\"underLine\">"
				+ (policyBondDetailDto.getMphAddress1() != null ? policyBondDetailDto.getMphAddress1() : "")
				+ "</div> -->" + "<div class=\"text-right\" style=\"text-align:right;\">"
				+ (policyBondDetailDto.getUnitName() != null ? policyBondDetailDto.getUnitName() : "") + "</div>"
				+ "</div>" + "</div>"

				+ "<div class=\"row\">" + "<div class=\"col-md-12 \">" + "<!-- <div class=\"underLine\">"
				+ (policyBondDetailDto.getMphAddress2() != null ? policyBondDetailDto.getMphAddress2() : "")
				+ "</div> -->" + "<div class=\"text-right\" style=\"text-align:right;\">"
				+ (policyBondDetailDto.getAddress1() != null ? policyBondDetailDto.getAddress1() : "") + "</div>"
				+ "</div>" + "</div>"

				+ "<div class=\"row\">" + "<div class=\"col-md-12 \">" + "<!-- <div class=\"underLine\">"
				+ (policyBondDetailDto.getMphAddress2() != null ? policyBondDetailDto.getMphAddress2() : "")
				+ "</div> -->" + "<div class=\"text-right\" style=\"text-align:right;\">"
				+ (policyBondDetailDto.getAddress2() != null ? policyBondDetailDto.getAddress2() : "") + "</div>"
				+ "</div>" + "</div>"

				+ "<div class=\"row\">" + "<div class=\"col-md-12 \">" + "<!-- <div class=\"underLine\">"
				+ (policyBondDetailDto.getMphAddress2() != null ? policyBondDetailDto.getMphAddress2() : "")
				+ "</div> -->" + "<div class=\"text-right\" style=\"text-align:right;\">"
				+ (policyBondDetailDto.getAddress3() != null ? policyBondDetailDto.getAddress3() : "") + "</div>"
				+ "</div>" + "</div>"

				+ "<br></br>"

				+ "<div class=\"row\">" + "<div class=\"col-md-12\">"
				+ "<div style=\"text-align:right 700px; line-height: 80%;\">Date:"
				+ (policyBondDetailDto.getPolicyIssuanceDate() != null ? policyBondDetailDto.getPolicyIssuanceDate()
						: "")
				+ "</div>" + "</div>" + "</div>"

				+ "<div class=\"row\">" + "<div class=\"col-md-12\">" + "<br></br>"
				+ "<!-- <div class=\"justify underLine\">"
				+ (policyBondDetailDto.getMphName() != null ? policyBondDetailDto.getMphName() : "") + "</div> -->\r\n"
				+ "<div class=\"justify\" style=\"text-align:left;\">" + policyBondDetailDto.getMphName() + "</div>"
				+ "</div>\r\n" + "</div>"

				+ "<div class=\"row\">" + "<div class=\"col-md-12\">" + "<!-- <div class=\"justify underLine\">"
				+ "</div> -->" + "<div class=\"justify\" style=\"text-align:left;\">"
				+ policyBondDetailDto.getMphAddress1() + "</div>" + "</div>" + "</div>"

				+ "<div class=\"row\">" + "<div class=\"col-md-12\">" + "<!-- <div class=\"justify underLine\">"
				+ "</div> -->" + "<div class=\"justify\" style=\"text-align:left;\">"
				+ (policyBondDetailDto.getMphAddress2() != null ? policyBondDetailDto.getMphAddress2() : "") + "</div>"
				+ "</div>" + "</div>"

				+ "<div class=\"row\">" + "<div class=\"col-md-12\">" + "<!-- <div class=\"justify underLine\">"
				+ (policyBondDetailDto.getMphAddress1() != null ? policyBondDetailDto.getMphAddress1() : "")
				+ "</div> -->" + "<div class=\"justify\" style=\"text-align:left;\">"
				+ (policyBondDetailDto.getMphAddress3() != null ? policyBondDetailDto.getMphAddress3() : "") + "</div>"
				+ "</div>" + "</div>"

				+ "<br></br>" + "<br></br>" + "<div class=\"row\">"
				+ " <div class=\"col-md-12 justify\" style=\"text-align:left;\">Dear Master Policyholder,</div>"
				+ "</div>\r\n" + "<div class=\"row\">\r\n"
				+ "<div class=\"col-md-12\"  style=\"text-align:center;\">Re: Your Master Policy No. "
				+ "<span class=\"underLine\">"
				+ (policyBondDetailDto.getPolicyNumber() != null ? policyBondDetailDto.getPolicyNumber() : "")
				+ "</span> ," + " </div>" + " </div>    <div class=\"justify\">\r\n"
				+ "  We have pleasure to forward herewith the above policy document comprising of PartA to Part G which\r\n"
				+ "  please find in order.\r\n" + " </div>" + "<br></br>" + "  <div class=\"justify\">\r\n"
				+ "  We would also like to draw your kind attention to the information mentioned in the Schedule of the\r\n"
				+ "  policy\r\n" + "  and the benefits available to each Member under the Policy.\r\n" + " </div> "
				+ "  <br></br>" + "  <div class=\"row\">\r\n"
				+ "<h4 style=\"text-align:left;\">Free Look Period :</h4>\r\n" + "</div>\r\n"
				+ "  <div class=\"justify\">\r\n"
				+ "  We would request you to go through the terms and conditions of the Policy and in case you\r\n"
				+ "  disagree to any\r\n"
				+ "  of the terms and conditions, you may return the policy within a period of 15 days stating the\r\n"
				+ "   reasons of\r\n"
				+ " your objections and disagreement. On receipt of the policy, we shall cancel the same and total\r\n"
				+ "  Contributions paid by you shall be refunded to you after deducting proportionate Risk Premium\r\n"
				+ "  for the\r\n" + "  period of cover and the stamp duty expenses.\r\n" + "  </div>\r\n"
				+ "  <br></br>" + "   <div class=\"justify\">\r\n"
				+ "  In case you have any Complaints/Grievance, you may approach the Branch/ P&mp;GS Unit office on the\r\n"
				+ "  address mentioned above or Grievance Redressal Officer/Ombudsman, whose address is as under:\r\n"
				+ "</div>" + " <br></br>" + " <br></br>" + " <br></br>" + " <div class=\"showborder\">"
				+ " Address of Grievance Officer:" + "</div>" + "<br />\r\n" + "<div class=\"showborder\">\r\n"
				+ " Address and contact details of Insurance Ombudsman:\r\n" + " </div>\r\n" + " <br />\r\n"
				+ "<div class=\"justify\">\r\n"
				+ " If you find any errors in this document, you may return this Policy for corrections.\r\n" + "<br />"
				+ " Thanking you,<br />" + "p.Manager (P&mp;GS)<br />" + " <br></br>"
				+ "<p>LIC’s New Group Gratuity Cash Accumulation plan (UIN: 512N281V03) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Page 1 of 15 </p>"
				+ "</div>" + " <br></br>" + " <br></br>" + " <br></br>"

				+ "<p  style=\"text-align:left;\"> Master Policy No. NGGCA/</p>"

				+ " <br></br>" + " <br></br>"

				+ " <div class=\"table-responsive pt-3\">"
				+ "<table  style=\"border:1px solid black;border-collapse:collapse;\">\r\n"
				+ "                        <thead>\r\n" + "                            <tr>\r\n"
				+ "                                <th style=\"border:1px solid black;\">Agent’s/ Intermediary’s Code</th>\r\n"
				+ "                                <th style=\"border:1px solid black;\">Agent’s/ Intermediary’s Name </th>\r\n"
				+ "                                <th style=\"border:1px solid black;\">Agent’s/Intermediary’s\r\n"
				+ "                                    Mobile Number/ Landline\r\n"
				+ "                                    Number\r\n" + "                                </th>\r\n"
				+ "                            </tr>\r\n" + "                        </thead>\r\n"
				+ "                        <tbody>\r\n" + "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\"> "
				+ policyBondDetailDto.getIntermediaryCode() + "</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\"> "
				+ (policyBondDetailDto.getIntermediaryName() != null ? policyBondDetailDto.getIntermediaryName() : "")
				+ "</td>\r\n" + "                                <td style=\"border:1px solid black;\">"
				+ policyBondDetailDto.getIntermediaryContactNo() + "</td>\r\n" + "                </tr>\r\n"
				+ "       </tbody>\r\n" + "  </table>\r\n" + "</div>" + "\r\n" + "  <br ></br>"
				+ "                <div class=\"justify\">We would also like to draw your attention to the following aspects:</div>\r\n"
				+ "                <br ></br>" + "                <ol>\r\n"
				+ "                    <li class=\"justify\">\r\n"
				+ "                        1) Change of Address: In case there is any change in your address, kindly ensure that the\r\n"
				+ "                        change in\r\n"
				+ "                        address is informed to the servicing P&mp;GS Unit.\r\n"
				+ "                    </li><br></br>" + "                    <li class=\"justify\">\r\n"
				+ "                        2) Within 90 days from the date of exit as defined in the Scheme Rules, intimation of such exit\r\n"
				+ "                        of\r\n"
				+ "                        Member along with death certificate, if applicable, must be notified in writing to the\r\n"
				+ "                        office of\r\n" + "                        the\r\n"
				+ "                        Corporation where the policy is serviced for any claims to be admissible. However, delay in\r\n"
				+ "                        intimation of the genuine death claim beyond 90 days, by the claimant may be condoned by the\r\n"
				+ "                        Corporation on merit and where delay is proved to be for the reasons beyond his/her control.\r\n"
				+ "\r\n" + "                    </li><br></br>" + "                    <li class=\"justify\">\r\n"
				+ "                        3) Section 45 of Insurance Act, 1938: The current provisions of the same are enclosed as\r\n"
				+ "                        Annexure-1.\r\n" + "                    </li><br></br>"
				+ "                    <li class=\"justify\">\r\n"
				+ "                        4) The approved version of Policy Document in respect of this plan is available on our website\r\n"
				+ "                        :\r\n"
				+ "                        <a class=\"color\" href=\"https://www.licindia.in\">www.licindia.in</a>.\r\n"
				+ "                    </li>\r\n"
				+ "                </ol>  <div class=\"justify\">These measures will enable us to serve you better.</div>\r\n"

				+ " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>"
				+ " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>"
				+ " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>"
				+ " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>"

				+ "<p>LIC’s New Group Gratuity Cash Accumulation plan (UIN: 512N281V03) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Page 2 of 15</p>"
				+ " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>"

				+ "<p  style=\"text-align:left;\"> Master Policy No. NGGCA/</p>"

				+ " <br></br>" + " <br></br>"

				+ "                <div>\r\n"
				+ "<div class=\"text-bold\" style=\"text-align:center;\">Preamble</div>\r\n"

				+ "                </div><br></br>" + "                <div class=\"justify\">\r\n"
				+ "                    The <b>Life Insurance Corporation Of India </b> (hereinafter called \"the Corporation\") having received\r\n"
				+ "                    a Proposal alongwith Declaration from the Trustees/ Employer of\r\n"
				+ "                    <span class=\"underLine\">"
				+ (policyBondDetailDto.getMphName() != null ? policyBondDetailDto.getMphName() : "")
				+ "</span> &nbsp;\r\n"
				+ "                    <b>Group Gratuity Cash Accumulation Scheme</b>(hereinafter called \"the Master Policyholder\"), being the\r\n"
				+ "                    policyholder for the group Gratuity policy for providing gratuity benefits as described in the Rules\r\n"
				+ "                    of the\r\n"
				+ "                    aforesaid Gratuity Scheme, which Rules together with the aforesaid proposal are hereby declared to\r\n"
				+ "                    be the\r\n"
				+ "                    basis of this policy and WHEREAS the Corporation has received an amount of <b>Rs."
				+ (policyBondDetailDto.getAdjustmentAmount() != null ? policyBondDetailDto.getAdjustmentAmount() : "")
				+ "\r\n" + "                    <span class=\"underLine\">"
				+ (policyBondDetailDto.getPaymentReceived() != null ? policyBondDetailDto.getPaymentReceived() : "")
				+ " </span>\r\n" + "                    , in\r\n" + "                    respect of\r\n"
				+ "                    <span class=\"underLine\">" + policyBondDetailDto.getTotalMember()
				+ " </span>\r\n"
				+ "                    Members’ Contribution towards provision of Gratuity and Risk Premium for Life\r\n"
				+ "                    Cover Benefit for a total Sum Assured of Rs.\r\n"
				+ "                    <sapn class=\"underLine\">"
				+ (policyBondDetailDto.getTotalSumAssured() != null ? policyBondDetailDto.getTotalSumAssured() : "")
				+ " </sapn></b>\r\n" + "                    on the Date of Commencement of this Policy.\r\n"
				+ "                </div><br ></br> <div class=\"justify\">\r\n"
				+ "                    The Master Policyholder shall pay to the Corporation, the Contributions as and when they fall due\r\n"
				+ "                    and the\r\n"
				+ "                    Risk Premium on the ________ day of __________ i.e. the date of commencement of the policy and the\r\n"
				+ "                    _____ day of ____________ every year thereafter (i.e. the Annual Renewal Date) in respect of each Member in\r\n"
				+ "                    accordance with the provision thereof.\r\n"
				+ "                </div><br /> <div class=\"justify\">\r\n"
				+ "                    Subject to the terms and provisions hereof, the Corporation hereby agrees to pay to the Master\r\n"
				+ "                    Policyholder, the appropriate benefits in respect of the Members on proof to the complete\r\n"
				+ "                    satisfaction of\r\n"
				+ "                    the Corporation of the benefit having become payable and provided that the Schedules, Terms and\r\n"
				+ "                    Conditions contained in the document are complied with.\r\n"
				+ "                </div><br />\r\n" + "                <div class=\"justify\">\r\n"
				+ "                    Any amendment to the terms and conditions of this policy shall be given effect to by an\r\n"
				+ "                    endorsement to the policy signed by an authorized Officer of the Corporation.\r\n"
				+ "                </div><br />\r\n" + "                <div class=\"justify\">\r\n"
				+ "                    This policy shall be subject to the Definitions, Benefits, Conditions related to Servicing Aspects,\r\n"
				+ "                    Other\r\n"
				+ "                    Terms and Conditions and Statutory Provisions printed on the back hereof and the following Schedule,\r\n"
				+ "                    the\r\n"
				+ "                    Scheme Rules and every endorsement placed on the Policy by the Corporation shall be deemed part of\r\n"
				+ "                    this Policy as full as if recited over the signature affixed hereto .\r\n"
				+ "                </div><br /> " + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>"
				+ " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>"
				+ " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>" + " <br></br>"
				+ " <br></br>" + " <br></br>" + " <br></br>"
				+ "<p>LIC’s New Group Gratuity Cash Accumulation plan (UIN: 512N281V03) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Page 3 of 15</p> "

				+ " <br></br>" + "<p  style=\"text-align:left;\"> Master Policy No. NGGCA/</p>"

				+ " <br></br>" + " <br></br>"
				+ "<div class=\"text-bold\" style=\"text-align:center;\">SCHEDULE</div>\r\n" + " <br></br>"
				+ " <br></br>"

				+ "                <div class=\"row\">\r\n"
				+ "                    <div class=\"col-md-9 justify text-bold\">DIVISIONAL OFFICE: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; P&mp;GS UNIT OFFICE:</div>\r\n"
				// + " <div class=\"col-md-3 text-right text-bold\"
				// style=\"text-align:right;\">P&mp;GS UNIT OFFICE:</div>\r\n"
				+ "                </div>\r\n" + "                <div class=\"table-responsive pt-3 justify\">\r\n"
				+ "                    <table  style=\"border:1px solid black;border-collapse:collapse;\" >\r\n"
				+ "                        <tbody >\r\n" + "                            <tr>\r\n"
				/*
				 * + "                                <td class=\"table_3\"  >1.</td>\r\n" +
				 * "                                <td class=\"table_2\"  >Proposal No.</td>\r\n"
				 * +
				 * "                                <td class=\"table_1\"  > @proposalNumber</td>\r\n"
				 */
				+ "                                <td style=\"border:1px solid black;\">1.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Proposal No.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">"
				+ policyBondDetailDto.getProposalNumber() + "</td>\r\n"

				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">2.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Proposal Date</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">"
				+ policyBondDetailDto.getProposalDate() + "</td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">3.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Master Policy Number</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\"> "
				+ policyBondDetailDto.getPolicyNumber() + "</td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">4.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Name of Master Policyholder</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">"
				+ policyBondDetailDto.getMphName() + "</td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">5.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Registered Address and e-mail ID of Master Policyholder</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">"
				+ (policyBondDetailDto.getMphAddress1() != null ? policyBondDetailDto.getMphAddress1() : "")
				+ (policyBondDetailDto.getMphAddress2() != null ? policyBondDetailDto.getMphAddress2() : "")
				+ (policyBondDetailDto.getMphAddress3())
				+ (policyBondDetailDto.getMphEmail() != null ? policyBondDetailDto.getMphEmail() : "") + "</td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">6.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Name of the Scheme</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">"
				+ (policyBondDetailDto.getMphName() != null ? policyBondDetailDto.getMphName() : "") + "-"
				+ (policyBondDetailDto.getProductCode() != null ? policyBondDetailDto.getProductCode() : "")
				+ "</td>\r\n" + "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">7.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Date of Commencement of Master Policy</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">"
				+ policyBondDetailDto.getValuationEffectiveDate() + "</td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">8.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Policy Term</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Annually Renewable</td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">9.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Annual Renewal Date</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">"
				+ policyBondDetailDto.getAnnualRenewalDate() + "</td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">10.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Minimum/ Maximum Contribution</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">The Contribution requirement will be determined as per AS 15\r\n"
				+ "                                    (Revised) or IND AS 19 or any other standards applicable for\r\n"
				+ "                                    long term Employee Benefits. </td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">11.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Minimum Age at Entry</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">....years</td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">12.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Maximum Age at Entry</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">....years</td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">13.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Number of Members covered as on the Date of\r\n"
				+ "                                    Commencement of Master Policy </td>\r\n"
				+ "                                <td style=\"border:1px solid black;\"> "
				+ policyBondDetailDto.getTotalMember() + "</td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\"> 14.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\"> Normal retirement age</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">  "
				+ policyBondDetailDto.getRetirementAge() + "</td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\"> 15.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\"> Event on the happening of which benefits payable:\r\n"
				+ "                                    (i) Death/ Resignation/Retirement Benefit/Termination of Service\r\n"
				+ "                                    (ii) Surrender </td>\r\n"
				+ "                                <td style=\"border:1px solid black;\"> As per Para 1 of Part C of this Policy Document.\r\n"
				+ "                                    As per Para 3 of Part D of this Policy Document. </td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\"> 16.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Total Contribution received towards Gratuity benefit on\r\n"
				+ "                                    Date of Commencement of Policy </td>\r\n"
				+ "                                  <td style=\"border:1px solid black;\">"
				+ policyBondDetailDto.getTotalGratuity() + "</td>\r\n" + "                                <td></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\"> 17.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\"> Total Risk Premium received towards Life\r\n"
				+ "                                    Cover benefit on Date of Commencement of Policy. </td>\r\n"

				+ "                                  <td style=\"border:1px solid black;\">"
				+ new BigDecimal(getpre).setScale(2, RoundingMode.HALF_UP).doubleValue() + "</td>\r\n"
				+ "                                <td></td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">18.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">  Total Sum Assured on Date of Commencement of Policy. </td>\r\n"

				+ "                                  <td style=\"border:1px solid black;\"> "
				+ policyBondDetailDto.getTotalSumAssuredAmount() + "</td>\r\n"
				+ "                                <td></td>\r\n" + "                            </tr>\r\n"
				+ "                        </tbody>\r\n" + "                    </table>\r\n"
				+ "                </div>  <br />"

				+ "                <div class=\"justify\">\r\n"
				+ "                    Signed on behalf of the Corporation at the above mentioned Branch/ P&mp;GS Unit, whose address and\r\n"
				+ "                    e-mail\r\n"
				+ "                    ID is given on the first page and to which all communications relating to the policy should be\r\n"
				+ "                    addressed.\r\n" + "                </div> <br />"
				+ "                <div class=\"row text-bold justify\">"
				+ "                    <div class=\"col-md-12\">Date :</div>" + "                </div>  "

				+ "<div class=\"row text-bold justify\">\r\n"
				+ "                    <div class=\"col-md-9\">Examined by:  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp; _____________________</div>\r\n"
				// + " <div class=\"col-md-3 text-right\" >_____________________ </div>\r\n"

				+ "<br></br >"

				+ "                </div>  <div class=\"row text-bold\">\r\n"
				+ "                    <div class=\"col-md-9 justify\">Form No.: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; p.Manager (P&mp;GS)</div>"
				// + " <div class=\"col-md-3 text-right\">p.Manager (P&GS)</div>\r\n"
				+ "                </div>  "

				+ "<br></br >" + "<br></br >" + "<br></br >" + "<br></br >" + "<br></br >" + "<br></br >" + "<br></br >"
				+ "<br></br >" + "<br></br >" + "<br></br >" + "<br></br >" + "<br></br >" + "<br></br >" + "<br></br >"
				+ "<br></br >" + "<br></br >" + "<br></br >" + "<br></br > "
				+ "<p>LIC’s New Group Gratuity Cash Accumulation plan (UIN: 512N281V03) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Page 4 of 15</p> "
				+ "<br />"

				+ " <br></br>" + "<p  style=\"text-align:left;\"> Master Policy No. NGGCA/</p>"

				+ " <br></br>" + " <br></br>" + "                <div class=\"row\">"
				+ "                    <div class=\"col-md-12 underLine text-bold\" style=\"text-align:center;\">  PART B-DEFINITIONS </div>"
				+ "                </div>"

				+ "                <br />" + "                <div class=\"justify\">"
				+ "                    The definitions of terms/words used in the Policy Document are as under:"
				+ "                </div> "

				+ " <div class=\"table-responsive pt-2 justify\">\r\n"
				+ "                    <table  style=\"border:1px solid black;border-collapse:collapse;\">"
				+ "                        <tbody>\r\n" + "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">1.</td>\r\n"
				+ "                                <td  style=\"border:1px solid black;\">Age</td>\r\n"
				+ "                                <td  style=\"border:1px solid black;\">"
				+ "                                    It is the age nearer birthday of the Member on the Entry Date\r\n"
				+ "                                    except for age 18 years for which the age is in completed years.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">2.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Annual Renewal Date (ARD)</td>\r\n"
				+ "                                <td>\r\n"
				+ "                                    It is the date on which the policy will be renewed in each\r\n"
				+ "                                    subsequent year and as mentioned in the policy Schedule.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">3.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Beneficiary </td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">"
				+ "                                    The person(s)/ entity who is/ are entitled to receive the benefits\r\n"
				+ "                                    under this Policy. The Beneficiary to whom the benefits are\r\n"
				+ "                                    payable is the Member or the nominee(s) or proved Executors\r\n"
				+ "                                    or Administrators or other Legal Representatives who should\r\n"
				+ "                                    take out representation to his/her Estate or limited to the\r\n"
				+ "                                    moneys payable under this Policy from any Court of any State\r\n"
				+ "                                    or Territory of the Union of India as applicable.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">4.</td>\r\n"
				+ "                                <td>Bulk Exit </td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    All type of exits from the Scheme, excluding complete\r\n"
				+ "                                    surrender, as defined in the Scheme Rules filed with the\r\n"
				+ "                                    Corporation at the outset, will be considered as Bulk Exits. If\r\n"
				+ "                                    the amount to be paid on total exits during the policy year\r\n"
				+ "                                    exceeds 25% of the Policy Account Value as at the beginning\r\n"
				+ "                                    of that policy year and is atleast Rs. 10 crores, it will be\r\n"
				+ "                                    considered as Bulk Exit.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">5.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Corporation</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    It means the Life Insurance Corporation of India established\r\n"
				+ "                                    under Section 3 of the LIC Act, 1956.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">6.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Contribution</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    Amount payable by the Master Policyholder as per the funding\r\n"
				+ "                                    valuation report in accordance with applicable Accounting\r\n"
				+ "                                    Standards or as per Scheme Rules.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">7.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Date of commencement of policy </td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    It is the date on which the policy comes into effect and is as\r\n"
				+ "                                    specified in the Schedule.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">8.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Death Benefit</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Benefit which is payable on death of a Member as specified in\r\n"
				+ "                                    Para 1 of Part C of this Policy Document. </td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">9.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Endorsement </td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    It is the conditions attached /affixed to this Policy incorporating\r\n"
				+ "                                    any amendments or modifications agreed to or issued by the\r\n"
				+ "                                    Corporation and forming part of the contract.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">10.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Entry Date </td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    Entry Date in respect of a Member is the date on which he/ she\r\n"
				+ "                                    joins the Scheme after receipt of premium in respect of that\r\n"
				+ "                                    Member. It means (a) in relation to original Members, the Date\r\n"
				+ "                                    of Commencement of Policy and (b) in relation to new\r\n"
				+ "                                    Members admitted to the Scheme after the Date of\r\n"
				+ "                                    Commencement of Policy, the date of their joining the Scheme\r\n"
				+ "                                    after the payment of Contribution.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">11.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Free Look Period </td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    It is the period of 15 days starting from the date of receipt of the\r\n"
				+ "                                    Policy Document by the Master Policyholder to review the\r\n"
				+ "                                    terms and conditions of this policy and where the Master\r\n"
				+ "                                    Policyholder disagrees to any of those terms and conditions, he/\r\n"
				+ "                                    she has the option to return this policy.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">12.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Group Policy Account </td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    An account which is maintained by the Corporation in respect\r\n"
				+ "                                    of this Policy to which the Contributions received from time to\r\n"
				+ "                                    time from the Master Policyholder will be credited after\r\n"
				+ "                                    adjusting the benefits paid in respect of each Member.\r\n"
				+ "                                    This Group Policy Account will accumulate with interest\r\n"
				+ "                                    declared at the end of each financial year.\r\n"
				+ "                                    For a new Scheme, Group Policy Account will be created in\r\n"
				+ "                                    respect of a Scheme as soon as the Master Policyholder pays\r\n"
				+ "                                    the first Contribution.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">13.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">IRDAI</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    Insurance Regulatory and Development Authority of India\r\n"
				+ "                                    earlier called Insurance Regulatory and Development Authority (IRDA).\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">14.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Member</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    An employee of the Master Policyholder whose name has been\r\n"
				+ "                                    communicated to the Corporation to be a Member for receipt of\r\n"
				+ "                                    benefits of this Policy according to the Scheme Rules.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">15.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Market Value Adjustment</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    Market Value Adjustment (MVA) is a reduction in the Policy\r\n"
				+ "                                    Account Value to cover the market losses of underlying\r\n"
				+ "                                    investment. It shall be calculated as per Para 4 of Part D of this\r\n"
				+ "                                    Policy Document.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">16.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Master Policyholder</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    It is the institution which has entered into a contract with the\r\n"
				+ "                                    Corporation for providing insurance cover to Members of this\r\n"
				+ "                                    policy. It is a legal owner of this policy.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">17.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Nomination</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    It is the process of nominating a person(s) who is (are) named\r\n"
				+ "                                    as “Nominee(s)” as per Scheme Rules.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">18.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Nominee</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    The person(s) nominated by the Member covered under this\r\n"
				+ "                                    policy who is (are) authorised to receive the claim benefit\r\n"
				+ "                                    payable in respect of that Member under this policy as per\r\n"
				+ "                                    Scheme Rules.\r\n" + "                                </td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">19.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Non-Participating </td>\r\n"
				+ "                                <td style=\"border:1px solid black;\"> \r\n"
				+ "                                    The Policy which is not eligible for share of profit depending\r\n"
				+ "                                    upon the Corporation’s experience\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">20.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Policy Year</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    The period between two consecutive policy anniversaries. This\r\n"
				+ "                                    period includes the first day and excludes the next policy\r\n"
				+ "                                    anniversary day\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">21.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Policy term</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    It is the period, in years, from the Date of commencement of\r\n"
				+ "                                    policy during which the contractual benefits are payable as per\r\n"
				+ "                                    the terms and conditions of the policy.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">22.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Policy/ Policy Document</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    It means this document along with endorsements, if any, issued\r\n"
				+ "                                    by the Corporation which is a legal contract between the Master\r\n"
				+ "                                    Policyholder and the Corporation.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                        </tbody>\r\n" + "                    </table>\r\n" + "<br></br>"
				+ "<br></br>"
				+ "<p>LIC’s New Group Gratuity Cash Accumulation plan (UIN: 512N281V03) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Page 5 of 15</p>"
				+ " <br></br>" + " <br></br>"

				+ "<p  style=\"text-align:left;\"> Master Policy No. NGGCA/</p>"

				+ " <br></br>" + " <br></br>"

				+ "                    <table  style=\"border:1px solid black;border-collapse:collapse;\">"
				+ "                        <tbody>\r\n" + "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">23.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Register</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    Means the Register of Members containing the names of\r\n"
				+ "                                    Members covered under this Scheme communicated to the\r\n"
				+ "                                    Corporation by the Master Policyholder; which Register shall\r\n"
				+ "                                    be deemed to be incorporated in and to form part of this policy.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">24.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Resignation / Retirement Benefit</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    Benefit payable on resignation/ retirement in respect of a\r\n"
				+ "                                    Member as per Scheme Rules but limited to the Group Policy\r\n"
				+ "                                    Account Value.\r\n" + "                                </td>\r\n"
				+ "                            </tr>\r\n"

				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">25.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Risk Premium</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    It is the contractual amount payable by the Master Policyholder\r\n"
				+ "                                    to secure the Life Cover Benefit in respect of each of the\r\n"
				+ "                                    Member covered under this policy. The term ‘Risk Premium’\r\n"
				+ "                                    used anywhere in this Policy Document does not include any\r\n"
				+ "                                    taxes, which is payable separately.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">26.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Sum Assured</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    Sum Assured in respect of each Member means an absolute\r\n"
				+ "                                    amount of benefit which is guaranteed to become payable on\r\n"
				+ "                                    death of that Member in accordance with Scheme Rules for inforce policy.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">27.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Surrender</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    It means complete withdrawal or termination of the entire\r\n"
				+ "                                    policy before expiry of policy term in accordance with the\r\n"
				+ "                                    provisions of this Policy Document.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\\\">28.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Surrender Value</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    The amount, if any, that becomes payable in case of surrender\r\n"
				+ "                                    in accordance with the terms and condition of this Policy\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">29.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Surrender Charges</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    Charge applicable on Surrender of this policy as per Para 3 of\r\n"
				+ "                                    Part D of this Policy Document.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">30.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Scheme</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    It refers to the name of Master Policyholder covered under this\r\n"
				+ "                                    policy. For example, “X” LIC’s New Group Gratuity Cash\r\n"
				+ "                                    Accumulation Scheme, where X is the name of the Master\r\n"
				+ "                                    Policyholder.\r\n" + "                                </td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">31.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">Scheme Rules / Rules</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    The Rules of the Scheme, for the time being in-force and as\r\n"
				+ "                                    amended from time to time, governing the details of benefit\r\n"
				+ "                                    structure, timing of benefit payments, eligibility conditions and\r\n"
				+ "                                    other terms and conditions specific to the Scheme.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td style=\"border:1px solid black;\">32.</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">UIN</td>\r\n"
				+ "                                <td style=\"border:1px solid black;\">\r\n"
				+ "                                    The Unique Identification Number allotted to this plan by the IRDAI.\r\n"
				+ "                                </td>\r\n" + "                            </tr>\r\n"
				+ "                        </tbody>\r\n" + "                    </table>\r\n"
				+ "                </div>\r\n" + "<br></br>" + "<br></br>"
				+ "<h3 style=\"text-align:center;\">PART – C: BENEFITS</h3>"
				+ "<h4 style=\"text-align:left;\">1. Benefits: </h4> " + "<div class=\"justify\">"
				+ " The following benefits are payable under a policy during the policy term " + "</div>" + "<br></br>"
				+ "<h4 style=\"text-align:left;\"> a. On death of a Member during service, before Normal Retirement Age: </h4>"
				+ "<div class=\"justify\">\r\n"
				+ "On death of a Member whilst in service before Normal Retirement Age, the Gratuity benefit in "
				+ "respect of a Member as per the Scheme Rules will be payable along with the Sum Assured as "
				+ "specified below:" + "</div>" + "<br></br>"
				+ "<h4 style=\"text-align:left;\"> i. If all due Risk Premiums have been paid: </h4>"
				+ "<div class=\"justify\">\r\n"
				+ " Sum Assured in respect of the Member as per the Scheme Rules shall be payable, \n" + "</div>"
				+ "<br></br>"
				+ "<h4 style=\"text-align:left;\"> ii. If death occurs during the period of 30 days after ARD but before completion of </h4>"
				+ "<div class=\"justify\">\r\n " + " Sum Assured in respect of the member as per the Scheme Rules&nbsp;"
				+ "shall be payable provided the renewal process is completed by Master Policyholder "
				+ " within 30 days from ARD (as per details mentioned in Para 3 below), \n " + "</div>" + "<br></br>"
				+ "<h4 style=\"text-align:left;\"> iii. If death occurs after expiry of the period of 30 days from ARD (i.e. after the </h4>"
				+ "<h4 style=\"text-align:left;\"> expiry of the period given for completion of renewal process): </h4> "
				+ "<div class=\"justify\">\r\n" + " Sum Assured of "
				+ " Rs.5000/- shall be payable. Any outstanding monthly Risk Premium(s), if any, from the "
				+ " date of death to the next ARD shall also be deducted from the Group Policy Account.  \n "
				+ " In case, the balance in the Group Policy Account of the Scheme is insufficient to pay out the "
				+ " benefits as per the Scheme Rules, it will be the responsibility of the Master Policyholder to "
				+ " first make the payment to the Corporation so as to enable the Corporation to pay such benefits. "
				+ " However, in any case for the Gratuity Benefit, the Corporation’s total liability towards the "
				+ " Master Policyholder shall be limited to the Group Policy Account Value remaining in the "
				+ " Policy Account. \n" + "</div>" + "<br></br>" + "<br></br>" + "<br></br>"
				+ "<p>LIC’s New Group Gratuity Cash Accumulation plan (UIN: 512N281V03) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Page 6 of 15</p>"
				+ " <br></br>" + "<p  style=\"text-align:left;\"> Master Policy No. NGGCA/</p>"

				+ "<br></br>"
				+ "<h4> b. Benefits payable on resignation or termination of service or retirement (including </h4>"
				+ "<h4> normal retirement, voluntary retirement and retirement on medical grounds as per </h4>"
				+ "<h4> Scheme Rules), </h4>" + "<div class=\"justify\">\r\n"
				+ " The Gratuity Benefit in respect of a Member shall be payable as specified in the Scheme "
				+ " Rules.  \n " + "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " In case, the balance in the Policy Account of the Scheme is insufficient to pay out the benefits "
				+ " as per the Scheme Rules, it will be the responsibility of the Master Policyholder to first make "
				+ " the payment to the Corporation so as to enable the Corporation to pay such benefits. However, "
				+ " for the Gratuity Benefit, the Corporation’s total liability towards the Master Policyholder shall "
				+ " be limited to the Group Policy Account Value remaining in the Policy Account.  \n " + "</div>"

				+ "</section>";

		String reportBody2 = "<section class=\"content_wrapper\">	"

				+ "<h4 style=\"text-align:left;\"> 2. Payment of Contributions and Risk Premium: </h4>"
				+ "<div class=\"justify\">\r\n"
				+ " i. The Master Policyholder can pay the Contributions to the Corporation at any time during "
				+ " the policy year. However, the Risk Premium for Life Cover Benefit along with applicable "
				+ " Taxes shall be paid at inception and thereafter, on or before each ARD in advance in "
				+ " respect of all the Members covered and from the date of joining the Scheme in respect of "
				+ " new Members, as per details mentioned in Para 3 below.  \n " + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n"
				+ " ii. In case of non completion of renewal process (i.e. submission of Members’ data and "
				+ " payment of Risk Premium within the period of 30 days from ARD), after the expiry of the \n \n \n \n"
				+ "</div>" + "<br></br>" + "<br></br>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " said period of 30 days, the Life Cover Benefit will be reduced to Rs. 5000/- per Member "
				+ " with effect from that ARD. In this case, monthly Risk Premium instalment shall be "
				+ " deducted from Group Policy Account. The monthly Risk Premium shall be arrived at by "
				+ " applying the monthly loading factor to the yearly Risk Premium. \r \n " + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n"
				+ " iii. The Master Policyholder shall pay to the Corporation in respect of each Member an annual "
				+ " Contribution in respect of Current Service Cost as per AS 15 (Revised) or IND AS 19 or "
				+ " any other Standards applicable for long term Employee Benefits.  \n " + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n"
				+ " iv. The Master Policyholder may also pay to the Corporation by the way of initial "
				+ " Contribution in respect of a Member who, on the date of entry into Scheme, has Past "
				+ " Service to his credit, such amount as they may decide as per AS 15 (Revised) or IND AS "
				+ " 19 or any other Standards applicable for long term Employee Benefits. The initial "
				+ " Contribution may be paid wholly on the entry date or partly any time thereafter.  \n" + "</div>"
				+ "<br></br>" + "<h3 style=\"text-align:left;\"> 3. Risk Premium: </h3> "
				+ "<div class=\"justify\">\r\n"
				+ " The age-wise Risk Premium rates in respect of each Member will depend on the Sum Assured, "
				+ " size of the group, mortality experience and risk profile including occupation of the group at "
				+ " commencement and on subsequent renewals on or before ARD. \n " + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n"
				+ " A new Member can be admitted to the Scheme in any month on a date coinciding with the "
				+ " calendar date of Annual Renewal Date (ARD). A proportionate Risk Premium of in respect of "
				+ " each of the new Member(s) will be based on the age nearer birthday as at the date of entry in to the"
				+ " Scheme and the duration from date of entry till the next ARD. In this case, Life Cover Benefit will "
				+ " commence only after the receipt of Risk Premium and Members’ data.  \n " + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n"
				+ " On or before each ARD, the Master Policyholder shall provide employees’ data along with Risk "
				+ " Premium for Life Cover Benefit to complete the renewal process. Risk Premium shall be collected "
				+ " with interest (from ARD to date of receipt of Risk Premium), if any, for delay in payment of Risk "
				+ " Premium along with applicable taxes, if any. A maximum period of 30 days shall be provided by "
				+ " the Corporation to the Master Policyholder to complete the renewal process (i.e. submission of "
				+ " Members’ data and payment of Risk Premium). \n" + "</div>" + "<br></br>"
				+ "<p>LIC’s New Group Gratuity Cash Accumulation plan (UIN: 512N281V03) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Page 7 of 15</p>"
				+ " <br></br>" + " <br></br>"

				+ "<p  style=\"text-align:left;\"> Master Policy No. NGGCA/</p>"

				+ "<br></br>" + "<div class=\"justify\">\r\n"
				+ " The rate of interest applicable for late payment of Risk Premium under this product for every 12 "
				+ " months’ period from 1st May to 30th April shall not exceed 10 year G-sec rate p.a. compounding "
				+ " half yearly as at the last trading day of previous financial year plus 300 basis points. For the period"
				+ " of 1st May 2019 to 30th April, 2020, the interest rate applicable for late payment of Risk Premium "
				+ " is 10.34% p.a. compounding half yearly.  \n " + "</div>"

				+ "<div class=\"justify\">\r\n"
				+ " In case of non completion of renewal process within the period of 30 days from ARD, after the "
				+ " expiry of the said period of 30 days, the Life Cover Benefit will be reduced to Rs. 5000/- per "
				+ " Member with effect from ARD. Based on existing Members’ data as provided on ARD, the "
				+ " corresponding monthly instalment Risk Premium for one month for such Members from ARD "
				+ " along with interest, if any, on account of late receipt of Risk Premium along with advance "
				+ " premium for the next month and applicable taxes, if any, shall be auto debited from the Group "
				+ " Policy Account for the reduced Life Cover benefit of Rs. 5000/- at the beginning of the second "
				+ " month from ARD.  \n" + "</div>" + "<br></br> " + "<div class=\"justify\">\r\n"
				+ " Once the Life Cover Benefit is reduced to the minimum level of Rs. 5000/- per Member, it will "
				+ " remain at that level in respect of all the existing Members as well as the new Members joining "
				+ " during for that policy year and the monthly Risk Premium instalment will continue to be deducted "
				+ " for the reduced Risk cover Benefit of Rs. 5000/-. For this purpose, New Member would mean any "
				+ " Member as informed by the Master Policyholder during the year in addition to the Members "
				+ " already covered as on ARD. Once the Risk Cover is reduced to Rs 5000/- per Member, the Master "
				+ " Policyholder may be allowed to restore the increased Life Cover Benefit only at next ARD for the "
				+ " surviving Members by payment of full Risk Premium as decided by the Corporation together with "
				+ " Members' data and any other details/documents required as per the Underwriting Policy of the "
				+ " Corporation subject to the explicit consent received in writing from the Master Policyholder and "
				+ " approval of the Corporation in this regard.  \n " + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n"
				+ " On renewal of a policy under this plan, there may be revision in Risk Premium rates, using lighter "
				+ " rates when mortality experience is favourable and using heavier rates when the mortality "
				+ " experience worsens. For revision in Risk Premium rates, number/amount of expected claims is "
				+ " compared with the number/amount of actual claims during the policy year under consideration. \n"
				+ "</div>" + "<br></br>" + "<h3 style=\"text-align:left;\"> 4. Group Policy Account: </h3>"
				+ "<div class=\"justify\">\r\n"
				+ " For a new Scheme, a Group Policy Account will be created in respect of a Scheme as soon as the "
				+ " Master Policyholder pays the first Contribution.  \n " + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n"
				+ " All the Contributions paid to secure Gratuity Benefit by employer/ trustees will be credited to the "
				+ " Group Policy Account maintained.  /n" + "</div> " + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " A non-negative Interest Rate subject to a minimum interest rate of 0.5% p.a. shall be declared at "
				+ " the end of each financial year. The minimum interest rate of 0.5% p.a. shall remain guaranteed "
				+ " during the entire term of the contract. At the end of each financial year, the declaration of interest "
				+ " rate in respect of each Scheme shall be based on the actual investment performance of the fund for "
				+ " each financial year after appropriate deductions as per the Interest Declaration and Expenses "
				+ " policy of the Corporation.  \n " + "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " Risk Premium paid separately or deducted from the Group Policy Account (along with interest for "
				+ " delay in payment and applicable taxes, if any) will not be considered for interest rate calculation.  \n"
				+ "</div>" + "<br></br>"
				+ "<h3 style=\"text-align:center;\"> PART – D: CONDITIONS RELATED TO SERVICING ASPECTS </h3>"
				+ "<div class=\"justify\">\r\n" + "<h4 style=\"text-align:left;\"> 1. Proof of age: </h4>"
				+ " Evidence of age of the Members, satisfactory to the Corporation must be furnished to the "
				+ " Corporation before payment of any benefits hereunder.  \n" + "</div>" + "<br></br>" + " <br></br>"
				+ "<p>LIC’s New Group Gratuity Cash Accumulation plan (UIN: 512N281V03) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Page 8 of 15</p>"
				+ " <br></br>"

				+ "<p  style=\"text-align:left;\"> Master Policy No. NGGCA/</p>"

				+ "<br></br>" + "<h4> 2. Forfeiture in certain events: </h4>" + "<div class=\"justify\">\r\n"
				+ " In case any conditions herein mentioned or any endorsements made hereto shall be contravened or "
				+ " in case it shall hereafter appear that an untrue, or incorrect statement is contained in the proposal, "
				+ " declaration, lists or statements already furnished or to be furnished to the Corporation by the "
				+ " Member or the Master Policyholder in accordance with the provisions hereof or otherwise in"
				+ "</div> " + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " respect of the Assurance effected or to be effected hereunder or that any of the matters set forth or "
				+ " referred to in such proposal, declaration, lists or statements have not been truly and fairly stated or "
				+ " that any material information has been suppressed or withheld, then and in every such case but "
				+ " subject to the provisions of Section 45 of the Insurance Act 1938, as amended from time to time "
				+ " wherever applicable, the benefits under this Policy, in so far the same relate to the Member or "
				+ " wherever applicable, the benefits under this Policy, in so far the same relate to the Member or "
				+ " material information takes place or such untrue or incorrect statement has been made either by the "
				+ " Member himself or by the Master Policyholder, shall be void and the relative Assurances shall "
				+ " cease and be determined and all claims to any benefits in respect of the Assurances shall be subject "
				+ " to the provisions of Section 45 of the Insurance Act 1938, as amended from time to time. \n "
				+ "</div>" + "<br></br>" + "<h4 style=\"text-align:left;\"> 3. Surrender Value: </h4>"
				+ "<div class=\"justify\">\r\n"
				+ " The Policy can be surrendered by the Master Policyholder at any time by giving an advance notice "
				+ " of 3 months. On surrender of the policy, the Corporation shall pay the Surrender Value equal to "
				+ " higher of Guaranteed Surrender Value and Special Surrender Value. The policy will terminate on "
				+ " surrender \n" + "</div> " + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " The Life Cover Benefit will not acquire any surrender value.  \n " + "</div>" + "<br></br>"
				+ "<h4 style=\"text-align:left;\"> Guaranteed Surrender Value: </h4> \n \n \n \n"
				+ "<div class=\"justify\">\r\n"
				+ " The Guaranteed Surrender Value shall be equal to the 90% of the total Contributions paid into the "
				+ " Group Policy Account (Net of total Risk Premium paid separately or deducted from the Group "
				+ " Policy Account along with interest and applicable taxes, if any, from inception till the date of "
				+ " surrender) less all the benefits other than Life Cover Benefits paid since the inception of the "
				+ " policy.  \n " + "</div>" + "<br></br>"
				+ "<h4 style=\"text-align:left;\"> Special Surrender Value: </h4>" + "<div class=\"justify\">\r\n"
				+ " The Special Surrender Value shall be equal to the Group Policy Account Value on the day of "
				+ " surrender less the applicable surrender charges and applicable Market value Adjustment amount as "
				+ " detailed below. \n" + "</div> " + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " The surrender charge shall be 0.05% of the Policy Account Value subject to maximum of "
				+ " Rs.500,000/- if the policy is surrendered within third policy anniversary.  \n" + "</div> "
				+ "<br></br>" + "<div class=\"justify\">\r\n"
				+ " In addition to the surrender charge, a Market Value Adjustment (MVA) as defined in Para 4 of "
				+ " Part D will be applicable on Bulk Exit and complete surrender of the policy. \n" + "</div>"
				+ "<br></br>" + "<div class=\"justify\">\r\n"
				+ " The Corporation reserves the right to revise the Surrender Charges. The modification in Surrender "
				+ " Charges will be done with prospective effect with the prior approval from IRDAI after giving the "
				+ " Master Policyholder a notice of one month.  \n " + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n"
				+ " In case the Master Policyholder does not agree with the revision of charges the Master "
				+ " Policyholder shall have the option to withdraw the Group Policy Account Value. However such "
				+ " withdrawal shall not be treated as surrender. \n " + "</div>" + "<br></br>"
				+ "<h4 style=\"text-align:left;\"> 4. Market Value Adjustment: </h4>" + "<div class=\"justify\">\r\n"
				+ " The Market Value Adjustment (MVA) will be applicable on Bulk Exit and complete surrender of "
				+ " the policy. The MVA will be applicable on total withdrawal amount during a policy year which is "
				+ " over and above 25% of the Group Policy Account Value where bulk exit shall mean exit of " + "</div>"
				+ " <br></br>" + " <br></br>"
				+ "<p>LIC’s New Group Gratuity Cash Accumulation plan (UIN: 512N281V03) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Page 9 of 15</p>"
				+ " <br></br>" + " <br></br>"

				+ "<p  style=\"text-align:left;\"> Master Policy No. NGGCA/</p>"

				+ " <br></br>"

				+ "<div class=\"justify\">\r\n"
				+ " Members from the group as defined in the Scheme Rules. Any withdrawal amount which is below "
				+ " Rs. 10 crores shall not be treated as Bulk exit and in this case, MVA shall not apply. The MVA "
				+ " amount shall be derived at the time of exit using the following formula.\n " + "</div>"

				+ "<div class=\"justify\">\r\n"
				+ " MVA amount = Maximum (0, Policy Account Value at the beginning of the policy year – Market "
				+ " Value) / Policy Account Value * (Net amount which is over and above the amount representing "
				+ " Bulk Exit) \n " + "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " Where, Market Value is derived from the latest available revaluation of assets at the time when "
				+ " MVA is carried out. The assets will be earmarked separately for this product. The MVA amount, if "
				+ " any, will be deducted from the Group Policy Account Value. \n " + "</div>" + "<br></br>"
				+ "<h4 style=\"text-align:left;\"> 5. Free Look Period: </h4>" + "<div class=\"justify\">\r\n"
				+ " During the Free Look period of 15 days from the date of receipt of the Policy Document by the "
				+ " Master Policyholder, if the Master Policyholder is not satisfied with the Terms and Conditions of "
				+ " the policy, he/she may return the policy to the Corporation stating the reason of objections. On "
				+ " receipt of the same the Corporation shall cancel the policy and return the amount of Contribution "
				+ " deposited after deducting charges for stamp duty, if applicable and proportionate risk premium for "
				+ " the period of cover. \n" + "</div> " + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " If this Policy is cancelled during the Free Look period, no interest will be credited to their Group "
				+ " Policy Account." + "</div>" + "<h4 style=\"text-align:left;\"> 6. Policy Loan: </h4>"
				+ "<div class=\"justify\">\r\n" + " No loan is payable under this policy.  \n " + "</div>" + "<br></br>"
				+ "<h4 style=\"text-align:left;\"> 7. Compulsory Termination: </h4>" + "<div class=\"justify\">\r\n"
				+ " The policy may be terminated, after giving the Master Policyholder 3 months’ notice, on the "
				+ " happening of any of the following events: "
				+ " a. The balance in the Group Policyholder’s Account falls below Rs. 10000/-; "
				+ " b. The numbers of Members covered under the policy are less than 3.  \n " + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n"
				+ " In such a case, the balance in the Group Policyholder’s Account, if any, shall be refunded to the "
				+ " Master Policyholder.  \n" + "</div>" + "<br></br> " + "<div class=\"justify\">\r\n"
				+ " 8. The Master Policyholder shall provide the list of Members covered under the policy at the time of "
				+ " inception of the policy and at each Annual Renewal date. Such list which should be in the format"
				+ " prescribed by the Corporation from time to time and shall form part of the Policy Document.  \n "
				+ "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " 9. At the end of each financial year, the Corporation shall issue a statement of Group Policy Account "
				+ " showing various transactions during the financial year to the Master Policyholder. This statement "
				+ " will provide details of the Group Policy Account for the financial year such as opening balance, "
				+ " Contribution received, interest amount credited, as applicable, withdrawal and claim payments "
				+ " debited and closing balance. \n " + "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " 10. Life Cover Benefit is effected on the life of each Member under this Policy and for the purpose of "
				+ " granting the same, each Member shall produce satisfactory evidence of his health in the form and "
				+ " manner prescribed by the Corporation at the commencement of his Membership hereunder and on "
				+ " each occasion thereafter when an increase in Life Cover Benefit is to be granted. If such "
				+ " satisfactory evidence is not furnished or if the Member is found uninsurable on the Entry Date or "
				+ " the relevant Annual Renewal Date, the Life Cover Benefit or increase in Life Cover Benefit shall "
				+ " not be effected in respect of him/her.  \n " + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n"
				+ " 11. Notwithstanding anything herein contained to the contrary, the Corporations’ liability to the "
				+ " Master Policyholder under this policy shall be limited to the Benefits enumerated under Para 1 of "
				+ " Part C under this plan effected in respect of the Members subject to the terms and conditions "
				+ " applicable to them and Group Policy Account Value standing to the credit of the Master "
				+ " Policyholder  \n" + "</div> " + "<br></br>"
				+ "<p>LIC’s New Group Gratuity Cash Accumulation plan (UIN: 512N281V03) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Page 10 of 15</p>"
				+ " <br></br>" + " <br></br>"

				+ "<p  style=\"text-align:left;\"> Master Policy No. NGGCA/</p>"

				+ " <br></br>" + " <br></br>"

				+ "<h3 style=\"text-align:center;\"> PART E </h3>"
				+ "<div class=\"justify\" style=\"text-align:center;\">\r\n" + " Not Applicable " + "</div>"
				+ "<br></br>" + "<h3 style=\"text-align:center;\"> PART – F: OTHER TERMS AND CONDITIONS </h3>"
				+ "<h4> 1. Nomination: </h4>" + "<div class=\"justify\">\r\n"
				+ " The Master Policyholder shall obtain the nomination details in respect of all the Members covered "
				+ " under this policy and ensure that the requisite nominations are updated in their records on a regular "
				+ " basis. The Master Policyholder shall provide the necessary information to Insurer on demand or as and "
				+ " when required. The Master policyholder shall certify the correctness of the nomination made by the "
				+ " Members. Nomination shall be as per the provisions of the Gratuity Act, 1972. Reference may be "
				+ " made to Section 39 of the Insurance Act, 1938, wherever applicable.  \n " + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n"
				+ " 2. The benefits payable under this policy are strictly personal and cannot be assigned, charged or "
				+ " alienated in any way by the Member or the beneficiaries. \n " + "</div>" + "<br></br>"
				+ "<h4> 3. Force Majeure: </h4>" + "<div class=\"justify\">\r\n"
				+ " In the event where Corporation’s performance or any other obligations are prevented or hindered as a "
				+ " consequence of any act of God or state, strike, lock out, legislation or restriction by any government or "
				+ " any other statutory authority or any other circumstances that lie beyond Corporation’s anticipation or "
				+ "  control, the performance of this policy shall be wholly or partially suspended during the continuance "
				+ " of such force majeure. The Corporation shall resume its obligations towards the Policy as soon as the "
				+ " Force Majeure event ceases. The Corporation undertakes to keep the IRDAI informed and seek prior "
				+ " approval before effecting any of these changes.  \n" + "</div>" + "<br></br> "
				+ "<h4> 4. Legislative Changes: </h4>" + "<div class=\"justify\">\r\n"
				+ " The Terms and Conditions including the premiums and benefits payable under this policy are subject  \n \n \n \n"
				+ " to variation in accordance with the relevant Legislation and Regulations.  \n " + "</div>"
				+ "<br></br>" + "<h4> 5. Taxes: </h4>" + "<div class=\"justify\">\r\n"
				+ " Statutory Taxes, if any, imposed on such insurance plans by the Government of India or any other "
				+ " constitutional tax Authority of India shall be as per the Tax laws and the rate of tax as applicable from "
				+ " time to time.  \n" + "</div> " + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " The amount of applicable taxes as per the prevailing rates, shall be payable by the Master Policyholder "
				+ " shall not be considered for the calculation of benefits payable under the product.  \n " + "</div>"
				+ "<br></br>" + "<div class=\"justify\">\r\n"
				+ " In any case where the Corporation is liable to the revenue authorities for Income-tax or any other Tax "
				+ " or duties on the benefits to be made under this policy or on any account in respect to this policy, the "
				+ " Corporation shall be entitled to deduct the appropriate amounts for that purpose from the respective "
				+ " payments and shall not be liable to the Master Policyholder for the sums so deducted. \n "
				+ "</div>" + "<br></br>" + "<h4> 6. Normal requirements for a claim: </h4>"
				+ "<div class=\"justify\">\r\n" + " The following documents shall be required to process the claims: "
				+ " 1. Original Death Certificate, in case of death claim "
				+ " 2. Certificate of proof of any other exits defined in the Scheme Rules "
				+ " 3. Claims forms as prescribed by the Corporation "
				+ " 4. Proof of Existence, identity and Evidence of age of the Member and of the beneficiary whenever "
				+ " required to the satisfaction of the Corporation.  \n " + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n"
				+ " As soon as a Member or a beneficiary becomes entitled to receive the benefits under the Scheme, the "
				+ " Master Policyholder shall send the relevant particulars to the Corporation whereupon the Corporation "
				+ " shall pay to the Master Policyholder appropriate benefits. \n " + "</div>" + "<br></br>"
				+ "<p>LIC’s New Group Gratuity Cash Accumulation plan (UIN: 512N281V03) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Page 11 of 15</p>"
				+ " <br></br>"

				+ "  </section>";

		String reportBody3 = "<section class=\"content_wrapper\">	"

				+ "<p  style=\"text-align:left;\"> Master Policy No. NGGCA/</p>" + " <br></br>"

				+ "<div class=\"justify\">\r\n"
				+ " Within 90 days from the date of exit as defined in the Scheme Rules, intimation of such exit of "
				+ " Member along with death certificate, if applicable, must be notified in writing to the office of the "
				+ " Corporation where the policy is serviced for any claims to be admissible. However, delay in intimation "
				+ " of the genuine death claim beyond 90 days, by the claimant may be condoned by the Corporation on "
				+ " merit and where delay is proved to be for the reasons beyond his/her control.  \n" + "</div> "
				+ "<br></br>" + "<div class=\"justify\">\r\n"
				+ " 7. The Master Policyholder shall furnish the Corporation with all particulars relevant to the Scheme and "
				+ " for the operation of this policy and the particulars so furnished may be accepted by the Corporation as "
				+ " conclusive.  \n " + "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " 8. Any amendment/s or modification/s to the Scheme shall have the effect for the purpose of this policy "
				+ " only to such extent as the Corporation shall in writing agree and the consequential changes to the "
				+ " relevant provisions hereof shall be given effect to by endorsements to the Policy. \n " + "</div>"
				+ "<br></br>" + "<div class=\"justify\">\r\n"
				+ " 9. The Master Policyholder and the Corporation reserves the right to terminate the Scheme by giving "
				+ " three months’ notice to either party. In that event, the Life Cover Benefit under this Policy shall "
				+ " terminate forthwith and the benefit available under this policy shall be as per Para 3 of Part D.  \n "
				+ "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " 10. All monies payable by the Corporation hereunder shall be paid to the Master Policyholder through the "
				+ " servicing P and GS unit and a valid discharge receipt given by the Master Policyholder or on their behalf "
				+ " by any person duly authorised in writing by the Master Policyholder shall be a valid discharge to "
				+ " the Corporation in respect of any such payment. \n " + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n"
				+ " 11. The Scheme Rules shall form part of the policy document. In case of a conflict between Scheme Rules "
				+ " and condition of policy, the terms of the policy conditions shall prevail.  \n" + "</div>"
				+ "<br></br>" + "<div class=\"justify\">\r\n "
				+ " 12. The Master Policyholder shall at the request of the Corporation produce the Policy whenever "
				+ " necessary.  \n " + "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " 13. The Corporation shall not be liable for any action taken in good faith upon any statements and "
				+ " particulars furnished by the Master Policyholder which shall be, or shall be proved to have been  \n \n \n \n"
				+ "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " erroneous. Such of the Master Policyholders’ records in original, as in the opinion of the Corporation "
				+ " have a bearing on the benefits provided, shall be open for inspection by the Corporation, whenever "
				+ " required  \n " + "</div>" + "<br></br>" + "<h3> PART – G: STATUTORY PROVISIONS </h3> \n"
				+ "<h4> Section 45 of Insurance Act, 1938: </h4> \n" + "<div class=\"justify\">\r\n "
				+ " The provisions of Section 45 of the Insurance Act, 1938, as amended from time to time, shall be "
				+ " applicable. The current provisions are contained in Annexure-1 of this policy document.  \n"
				+ "</div> " + "<br></br>" + "<h4> Grievance Redressal Mechanism: </h4>"
				+ "<h4> Of the Corporation: </h4>" + "<div class=\"justify\">\r\n"
				+ " The Corporation has Grievance Redressal Officers at Branch/ Divisional/ Zonal/ Central Office to "
				+ " redress grievances of customers. For ensuring quick redressal of customer grievances the Corporation "
				+ " has introduced Customer friendly Integrated Complaint Management System through our Customer "
				+ " Portal (website) which is http://www.licindia.in, where a registered policy holder can directly register "
				+ " Complaint/grievance and track its status. Customers can also contact at e-mail id "
				+ " co_crmgrv@licindia.com for redressal of any grievances.  \n" + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n"
				+ " Claimants not satisfied with the decision of death claim repudiation have the option of referring their "
				+ " cases for review to Zonal Office Claims Dispute Redressal Committee or Central Office Claims "
				+ " Dispute Redressal Committee. A retired High Court/ District Court Judge is Member of each of the "
				+ "</div>" + "<br></br>"
				+ "<p>LIC’s New Group Gratuity Cash Accumulation plan (UIN: 512N281V03) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Page 12 of 15</p>"
				+ " <br></br>"

				+ "<p  style=\"text-align:left;\"> Master Policy No. NGGCA/</p>"

				+ " <br></br>"

				+ "<div class=\"justify\">\r\n"
				+ " Claims Dispute Redressal Committees. For redressal of Claims related grievances, claimants can also "
				+ " approach Insurance Ombudsmen who are appointed by Government of India for providing low cost "
				+ " and speedy arbitration to customers. \n " + "</div>" + "<br></br>" + "<h4> Of IRDAI: </h4>"
				+ "<div class=\"justify\">\r\n"
				+ " In case the customer is not satisfied with the response or does not receive a response from us within 15 "
				+ " days, then the customer may approach the Grievance Cell of the IRDAI through any of the following "
				+ " modes " + "</div>" + "<div class=\"justify\" style=\"text-align:center;\">\r\n"
				+ " • Calling Toll Free Number 155255 / 18004254732 (i.e. IRDAI Grievance Call Centre)" + "</div> "
				+ "<div class=\"justify\" style=\"text-align:center;\">\r\n"
				+ " • Sending an email to complaints@irdai.gov.in " + "</div>"
				+ "<div class=\"justify\" style=\"text-align:center;\">\r\n"
				+ " • Register the complaint online at http://www.igms.irdai.gov.in " + "</div>"
				+ "<div class=\"justify\" style=\"text-align:center;\">\r\n"
				+ " • Address for sending the complaint through courier / letter: " + "</div>"
				+ "<div class=\"justify\">\r\n"
				+ " Consumer Affairs Department, Insurance Regulatory and Development Authority of India, "
				+ " Survey No.115/1, Financial District, Nanakramguda, Gachibowli, Hyderabad- 500032, Telangana.  \n "
				+ "</div>" + "<br></br>" + "<h4> Of Ombudsman: </h4>" + "<div class=\"justify\">\r\n"
				+ " For redressal of Claims related grievances, claimants can also approach Insurance Ombudsman who "
				+ " provides for low cost and speedy arbitration to customers.  \n " + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n"
				+ " The Ombudsman, as per Insurance Ombudsman Rules, 2017, can receive and consider complaints or "
				+ " disputes relating to the matters such as:"
				+ " (a). Delay in settlement of claims, beyond the time specified in the regulations, framed under the "
				+ " Insurance Regulatory and Development Authority of India Act, 1999;  "
				+ " (b). Any partial or total repudiation of claims by the life insurer, General insurer or the health insurer; "
				+ " (c). Disputes over premium paid or payable in terms of insurance policy; "
				+ " (d). Misrepresentation of policy terms and conditions at any time in the policy document or policy "
				+ " contract; "
				+ " (e). Legal construction of insurance policies in so far as the dispute relates to claim; "
				+ " (f). Policy servicing related grievances against insurers and their agents and intermediaries; "
				+ " (g). Issuance of life insurance policy, general insurance policy including health insurance policy which "
				+ " is not in conformity with the proposal form submitted by the proposer; "
				+ " (h). Non-issuance of insurance policy after receipt of premium in life insurance and general insurance "
				+ " including health insurance; and  \n \n \n \n " + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n"
				+ " (i). Any other matter resulting from the violation of provisions of the Insurance Act, 1938 or the "
				+ "  regulations, circulars, guidelines or instructions issued by the IRDAI from time to time or the "
				+ " terms and conditions of the policy contract, in so far as they relate to issues mentioned at clauses "
				+ " (a) to (f)  \n " + "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " Note: In case of dispute in respect of interpretation of terms and conditions mentioned in this "
				+ " document, the English version shall stand valid.  \n " + "</div>"
				+ "<h3> YOU ARE REQUESTED TO EXAMINE THIS POLICY, AND IF ANY MISTAKE BE</h3>"
				+ "<h3> FOUND THEREIN, RETURN IT IMMEDIATELY FOR CORRECTION. </h3> \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n "
				+ "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>"
				+ "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>"
				+ "<p>LIC’s New Group Gratuity Cash Accumulation plan (UIN: 512N281V03) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Page 13 of 15</p>"
				+ " <br></br>" + " <br></br>"

				+ "<p  style=\"text-align:left;\"> Master Policy No. NGGCA/</p>"

				+ " <br></br>" + "<h3> Section 45 as per the Insurance Act 1938: </h3>" + "<div class=\"justify\">\r\n"
				+ " (1) No policy of life insurance shall be called in question on any ground whatsoever after the expiry of"
				+ " three years from the date of the policy, i.e. from the date of issuance of the policy or the date of \n \n \n \n"
				+ "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " commencement of risk or the date of revival of the policy or the date of the rider to the policy, whichever"
				+ " is later.  \n " + "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " (2) A policy of life insurance may be called in question at any time within three years from the date of "
				+ " issuance of the policy or the date of commencement of risk or the date of revival of the policy or the date "
				+ " of the rider to the policy, whichever is later on the ground of fraud: \n" + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n "
				+ " Provided that the insurer shall have to communicate in writing to the insured or the legal representatives or "
				+ " nominees or assignees of the insured the grounds and materials on which such decision is based.  \n "
				+ "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " Explanation I- For the purposes of this sub-section, the expression “fraud” means any of the following acts "
				+ " committed by the insured or by his agent, with the intent to deceive the insurer or to induce the insurer to "
				+ " issue a life insurance policy:-"
				+ " (a) the suggestion, as a fact of that which is not true and which the insured does not believe to be true; "
				+ " (b) the active concealment of a fact by the insured having knowledge or belief of the fact; "
				+ " (c) any other act fitted to deceive; and "
				+ " (d) any such act or omission as the law specially declares to be fraudulent.  \n " + "</div>"
				+ "<br></br>" + "<div class=\"justify\">\r\n"
				+ " Explanation II- Mere silence as to facts likely to affect the assessment of the risk by the insurer is not "
				+ " fraud, unless the circumstances of the case are such that regard being had to them, it is the duty of the "
				+ " insured or his agent, keeping silence to speak, or unless his silence is, in itself, equivalent to speak.  \n"
				+ "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " (3) Notwithstanding anything contained in subsection (2), no insurer shall repudiate a life insurance policy "
				+ " on the ground of fraud if the insured can prove that the misstatement of or suppression of a material fact "
				+ " was true to the best of his knowledge and belief or that there was no deliberate intention to suppress the "
				+ " fact or that such misstatement of or suppression of a material fact are within the knowledge of the insurer:  \n "
				+ "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " Provided that in case of fraud, the onus of disproving lies upon the beneficiaries, in case the policyholder "
				+ " is not alive. \n " + "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " Explanation – A person who solicits and negotiates a contract of insurance shall be deemed for the purpose "
				+ " of the formation of the contract, to be the agent of the insurer. \n" + "</div>" + "<br></br>"
				+ " <br></br>" + " <br></br>" + " <br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>"
				+ "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>"
				+ "<br></br>"
				+ "<p>LIC’s New Group Gratuity Cash Accumulation plan (UIN: 512N281V03) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Page 14 of 15</p>"
				+ " <br></br>"

				+ "<p  style=\"text-align:left;\"> Master Policy No. NGGCA/</p>"

				+ " <br></br>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " (4) A policy of life insurance may be called in question at any time within three years from the date of "
				+ " issuance of the policy or the date of commencement of risk or the date of revival of the policy or the date "
				+ " of the rider to the policy, whichever is later, on the ground that any statement of or suppression of a fact "
				+ " material to the expectancy of the life of the insured was incorrectly made in the proposal or other "
				+ " document on the basis of which the policy was issued or revived or rider issued: \n " + "</div>"
				+ "<br></br>" + "<div class=\"justify\">\r\n"
				+ " Provided that the insurer shall have to communicate in writing to the insured or the legal representatives or "
				+ " nominees or assignees of the insured the grounds and materials on which such decision to repudiate the "
				+ " policy of life insurance is based:  \n " + "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " Provided further that in case of repudiation of the policy on the ground of misstatement or suppression of a "
				+ " material fact, and not on the ground of fraud the premiums collected on the policy till the date of "
				+ " repudiation shall be paid to the insured or the legal representatives or nominees or assignees of the insured "
				+ " within a period of ninety days from the date of such repudiation.  \n " + "</div>" + "<br></br>"
				+ "<div class=\"justify\">\r\n"
				+ " Explanation - For the purposes of this sub-section, the misstatement of or suppression of fact shall not be "
				+ " considered material unless it has a direct bearing on the risk undertaken by the insurer, the onus is on the "
				+ " insurer to show that had the insurer been aware of the said fact no life insurance policy would have been "
				+ " issued to the insured.  \n " + "</div>" + "<br></br>" + "<div class=\"justify\">\r\n"
				+ " (5) Nothing in this section shall prevent the insurer from calling for proof of age at any time if he is "
				+ " entitled to do so, and no policy shall be deemed to be called in question merely because the terms of the "
				+ " policy are adjusted on subsequent proof that the age of the life insured was incorrectly stated in the "
				+ " proposal. " + "</div>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>"
				+ "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>"
				+ "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>"
				+ "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>"
				+ "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>"
				+ "<p>LIC’s New Group Gratuity Cash Accumulation plan (UIN: 512N281V03) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Page 15 of 15</p>"
				+ "  </section>";

		reportBody = reportBody + reportBody2 + reportBody3;
		return reportBody.replace("\\", "").replaceAll("\t", "");

	}

	// karthi -- start

	public static List<GenerateCBSheetPdfDto> getcbObjtoDto(List<Object[]> getcbsheet) {

		List<GenerateCBSheetPdfDto> getAgeValuationReportDto = new ArrayList<GenerateCBSheetPdfDto>();

		for (Object[] obj : getcbsheet) {
			GenerateCBSheetPdfDto generateCBSheetPdfDto = new GenerateCBSheetPdfDto();

			System.out.println(obj[0]);

			System.out.println(obj[1]);
			if (obj[0] != null) {
				generateCBSheetPdfDto.setLicId(obj[0].toString());
			} else {
				generateCBSheetPdfDto.setLicId("");
			}
			generateCBSheetPdfDto.setName(obj[1].toString());
			generateCBSheetPdfDto.setEmployeeNo(obj[2].toString());
			System.out.println(obj[2]);
			generateCBSheetPdfDto.setQuotationNumber(obj[3].toString());
			System.out.println(obj[3]);
			generateCBSheetPdfDto.setDob(obj[4].toString());
			System.out.println(obj[4]);
			generateCBSheetPdfDto.setDoa(obj[5].toString());
			System.out.println(obj[5]);
			if (obj[6] != null) {
				generateCBSheetPdfDto.setDojToScheme(obj[6].toString());
			} else {
				generateCBSheetPdfDto.setDojToScheme("");
			}
			generateCBSheetPdfDto.setSalary(obj[7] == null ? 0 : Double.parseDouble(obj[7].toString()));
			System.out.println(obj[7]);
			if (obj[8] != null) {
				generateCBSheetPdfDto.setPastService(obj[8] == null ? 0 : Long.parseLong(obj[8].toString()));
			} else {
				generateCBSheetPdfDto.setPastService(0l);
			}
			System.out.println(obj[8]);
			generateCBSheetPdfDto.setTotalService(obj[9] == null ? 0 : Long.parseLong(obj[9].toString()));
			System.out.println(obj[9]);
			generateCBSheetPdfDto.setTotalServiceGratuity(obj[10] == null ? 0 : Double.parseDouble(obj[10].toString()));
			System.out.println(obj[10]);
			generateCBSheetPdfDto.setPastServiceGratuity(obj[11] == null ? 0 : Double.parseDouble(obj[11].toString()));
			System.out.println(obj[11]);
			generateCBSheetPdfDto.setLifeCover(obj[12] == null ? 0 : Double.parseDouble(obj[12].toString()));
			System.out.println(obj[12]);
			generateCBSheetPdfDto.setAnnualRenewlDate(obj[13].toString());
			System.out.println(obj[13]);
			generateCBSheetPdfDto.setAge(obj[14] == null ? 0 : Long.parseLong(obj[14].toString()));
			System.out.println(obj[14]);
			generateCBSheetPdfDto.setLifeCoverPremium(obj[15] == null ? 0 : Double.parseDouble(obj[15].toString()));
			generateCBSheetPdfDto.setCategory(obj[16].toString());
			if (obj[16] != null) {
				generateCBSheetPdfDto.setCategory(obj[16].toString());
			} else {
				generateCBSheetPdfDto.setCategory("");
			}
			if (obj[19] != null) {
				generateCBSheetPdfDto.setCustomerCode(obj[19].toString());
			} else {
				generateCBSheetPdfDto.setCustomerCode("");
			}
			if (obj[18] != null) {
				generateCBSheetPdfDto
						.setCurrentServiceBenefit(obj[18] == null ? 0 : Double.parseDouble(obj[18].toString()));
			} else {
				generateCBSheetPdfDto.setCurrentServiceBenefit(0.0);
			}
			if (obj[17] != null) {
				generateCBSheetPdfDto
						.setPastServiceBenefit(obj[17] == null ? 0 : Double.parseDouble(obj[17].toString()));
			} else {
				generateCBSheetPdfDto.setPastServiceBenefit(0.0);
			}

			System.out.println(obj[19].toString());
			getAgeValuationReportDto.add(generateCBSheetPdfDto);
			if (obj[20] != null) {
				generateCBSheetPdfDto.setProposalNumber(obj[20].toString());
			} else {
				generateCBSheetPdfDto.setProposalNumber("");
			}
//			}
		}
		return getAgeValuationReportDto;
	}

	// karthi -- end

	public static List<GenerateCBSheetPdfDto> getcbObj(List<Object[]> getcbsheet) {

		List<GenerateCBSheetPdfDto> getAgeValuationReportDto = new ArrayList<GenerateCBSheetPdfDto>();

		for (Object[] obj : getcbsheet) {
			GenerateCBSheetPdfDto generateCBSheetPdfDto = new GenerateCBSheetPdfDto();

			System.out.println(obj[0]);

			System.out.println(obj[1]);
			if (obj[0] != null) {
				generateCBSheetPdfDto.setLicId(obj[0].toString());
			} else {
				generateCBSheetPdfDto.setLicId("");
			}
			generateCBSheetPdfDto.setName(obj[1].toString());
			generateCBSheetPdfDto.setEmployeeNo(obj[2].toString());
			System.out.println(obj[2]);
			generateCBSheetPdfDto.setQuotationNumber(obj[3].toString());
			System.out.println(obj[3]);
			generateCBSheetPdfDto.setDob(obj[4].toString());
			System.out.println(obj[4]);
			generateCBSheetPdfDto.setDoa(obj[5].toString());
			System.out.println(obj[5]);
			if (obj[6] != null) {
				generateCBSheetPdfDto.setDojToScheme(obj[6].toString());
			} else {
				generateCBSheetPdfDto.setDojToScheme("");
			}
			generateCBSheetPdfDto.setSalary(obj[7] == null ? 0 : Double.parseDouble(obj[7].toString()));
			System.out.println(obj[7]);
			if (obj[8] != null) {
				generateCBSheetPdfDto.setPastService(obj[8] == null ? 0 : Long.parseLong(obj[8].toString()));
			} else {
				generateCBSheetPdfDto.setPastService(0l);
			}
			System.out.println(obj[8]);
			generateCBSheetPdfDto.setTotalService(obj[9] == null ? 0 : Long.parseLong(obj[9].toString()));
			System.out.println(obj[9]);
			generateCBSheetPdfDto.setTotalServiceGratuity(obj[10] == null ? 0 : Double.parseDouble(obj[10].toString()));
			System.out.println(obj[10]);
			generateCBSheetPdfDto.setPastServiceGratuity(obj[11] == null ? 0 : Double.parseDouble(obj[11].toString()));
			System.out.println(obj[11]);
			generateCBSheetPdfDto.setLifeCover(obj[12] == null ? 0 : Double.parseDouble(obj[12].toString()));
			System.out.println(obj[12]);
			generateCBSheetPdfDto.setAnnualRenewlDate(obj[13].toString());
			System.out.println(obj[13]);
			generateCBSheetPdfDto.setAge(obj[14] == null ? 0 : Long.parseLong(obj[14].toString()));
			System.out.println(obj[14]);
			generateCBSheetPdfDto.setLifeCoverPremium(obj[15] == null ? 0 : Double.parseDouble(obj[15].toString()));
			generateCBSheetPdfDto.setCategory(obj[16].toString());
			if (obj[16] != null) {
				generateCBSheetPdfDto.setCategory(obj[16].toString());
			} else {
				generateCBSheetPdfDto.setCategory("");
			}
			if (obj[17] != null) {
				generateCBSheetPdfDto.setCustomerCode(obj[17].toString());
			} else {
				generateCBSheetPdfDto.setCustomerCode("");
			}
			getAgeValuationReportDto.add(generateCBSheetPdfDto);
		}
		return getAgeValuationReportDto;
	}
}
