package com.lic.epgs.gratuity.policyservices.policymodification.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Data
public class TempPolicyModificationDataTableRequestDto {

	private Long statusId;

	private Long start;

	private Long end;

	private Integer columnSort;
}
