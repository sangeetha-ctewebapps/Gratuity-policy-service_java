package com.lic.epgs.gratuity.quotation.member.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ismail Khan A
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberAddressDto {
	private Long id;
	private Long addressTypeId;
	private String pinCode;
	private Long countryId;
	private Long stateId;
	private String district;
	private String city;
	private String contactNumber;
	private String address1;
	private String address2;
	private String address3;
	private Boolean isPrintable;
	private Boolean isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
}
