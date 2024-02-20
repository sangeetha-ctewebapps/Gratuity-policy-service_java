package com.lic.epgs.gratuity.policy.claim.dto;
	
	import java.util.Date;
	import java.util.List;

	import com.fasterxml.jackson.annotation.JsonFormat;
	import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
	import com.lic.epgs.gratuity.quotation.dto.BenefitValuationDto;

	import lombok.AllArgsConstructor;
	import lombok.Getter;
	import lombok.NoArgsConstructor;
	import lombok.Setter;

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class PdfGeneratorDto {

		private String policyNumber;
		private String payoutNumber;
		private String unitCode;
		private Date payoutDate;
		private String mphName;
		private String mphAddress1;
		private String mphAddress2;
		private String mphAddress3;
		private String city;
		private String state;
		private String pincode;
		private String bankName;
		private String accNo;
		private String ifcsCode;
//		private String ref;
//		private String date;
	    private List<PdfGeneratorForTableDto> pdfGeneratorForTable;

	}
