package com.lic.epgs.gratuity.fund.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FUND_BATCH_MEMBER")
public class FundBatchMemberEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FUND_BATCH_MEMBER_GEN")
	@SequenceGenerator(name = "FUND_BATCH_MEMBER_GEN", sequenceName = "FUND_BATCH_MEMBER_ID_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private BigDecimal Id;
	
	@Column(name = "BATCH_NO", nullable = false)
	private String batchNo;
	
	@Column(name = "POLICY_ID", nullable = false)
	private String policyId;
	
	@Column(name = "MEMBER_ID", nullable = false)
	private String memberId;
	
	@Column(name = "UNIT_ID", nullable = false)
	private String unitId;
	
	@Column(name = "LIC_ID")
	private String licId;
	
	@Column(name = "VARIANT", nullable = false)
	private String variant;
	
	@Column(name = "STATUS", nullable = false)
	private String status;
	
	@Column(name = "TRNX_DATE", nullable = false)
	private String trnxDate;


}