package com.lic.epgs.gratuity.quotation.premium.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lic.epgs.gratuity.quotation.member.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Gopi
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "GRATCALCULATION")
public class GratuityCalculationEntity {

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PREMIUM_ID_SEQUENCE")
	//@SequenceGenerator(name = "PREMIUM_ID_SEQUENCE", sequenceName = "PREMIUM_ID_SEQUENCE", allocationSize = 1)
	@Column(name = "GRATCALCULATION_ID")
	private Long id;
	

	@Column(name="MEMBER_ID", nullable=false)
    private Long memberId;

	@Column(name = "TERM")	private Long term;
	@Column(name = "G2_DER")	private Long g2Der;
	@Column(name = "DOB_DER_YEAR")	private Long dobDerYear;
	@Column(name = "DOB_DER_AGE")	private Long dobDerAge;
	@Column(name = "DOJ_DER_YEAR")	private Long dojDerYear;
	@Column(name = "PAST_SERVICE")	private Long pastService;
	@Column(name = "PAST_SERVICE_DEATH")	private Double pastServiceDeath;
	@Column(name = "PAST_SERVICE_WDL")	private Double pastServiceWdl;
	@Column(name = "PAST_SERVICE_RET")	private Double pastServiceRet;
	@Column(name = "RET_DER_YEAR")	private Long retDerYear;
	@Column(name = "TOTAL_SERVICE")	private Long totalService;
	@Column(name = "GRATUITY_F1")	private Long gratuityF1;
	@Column(name = "BENEFITS_DEATH")	private Double benefitsDeath;
	@Column(name = "BENEFITS_WDL")	private Double benefitsWdl;
	@Column(name = "BENEFITS_RET")	private Double benefitsRet;
	@Column(name = "CURRENT_SERVICE_DEATH")	private Double currentServiceDeath;
	@Column(name = "CURRENT_SERVICE_WDL")	private Double currentServiceWdl;
	@Column(name = "CURRENT_SERVICE_RET")	private Double currentServiceRet;
	@Column(name = "BENE_CURRENT_SERVICE_DEATH")	private Double beneCurrentServiceDeath;
	@Column(name = "BENE_CURRENT_SERVICE_WITH")	private Double beneCurrentServiceWith;
	@Column(name = "BENE_CURRENT_SERVICE_RET")	private Double beneCurrentServiceRet;
	@Column(name = "PAST_SERVICE_BENEFIT_DEATH")	private Double pastServiceBenefitDeath;
	@Column(name = "PAST_SERVICE_BENEFIT_WDL")	private Double pastServiceBenefitWdl;
	@Column(name = "PAST_SERVICE_BENEFIT_RET")	private Double pastServiceBenefitRet;
	@Column(name = "MVC")	private Long mvc;
	@Column(name = "CURRENT_SERVICE_BENEFIT_DEATH")	private Double currentServiceBenefitDeath;
	@Column(name = "CURRENT_SERVICE_BENEFIT_WDL")	private Double currentServiceBenefitWdl;
	@Column(name = "CURRENT_SERVICE_BENEFIT_RET")	private Double currentServiceBenefitRet;
	@Column(name = "PAST_SERVICE_BENEFIT")	private Double pastServiceBenefit;
	@Column(name = "CURRENT_SERVICE_BENEFIT")	private Double currentServiceBenefit;
	@Column(name = "ACCRUED_GRA")	private Double accruedGra;
	@Column(name = "ACCRUED_GRAT")	private Double accruedGrat;
	@Column(name = "TOTAL_GRA")	private Double totalGra;
	@Column(name = "TOTAL_GRAT")	private Double totalGrat;
	@Column(name = "LC_SUM_ASSURED")	private Double lcSumAssured;
	@Column(name = "LC_PREMIUM")	private Double lcPremium;
	@Column(name = "IS_ACTIVE")	private Long isActive;
	@Column(name = "CREATED_BY")	private String createdBy;
	@Column(name = "CREATED_DATE")	private Date createdDate;
	@Column(name = "MODIFIED_BY")	private String modifiedBy;
	@Column(name = "MODIFIED_DATE")	private Date modifiedDate;
	@Column(name = "CAL_TYPE")	private String calType;
	@Column(name = "PVO_SUM")	private Double pvoSum;
	
	@Column(name = "SC_SUM")	private Double scSum;
	
	@Column(name = "TMP_MEMBER_ID")	private Long tmpMemberId;

}
