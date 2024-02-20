package com.lic.epgs.gratuity.policy.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
	
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	@Entity
	@Table(name = "PMST_POLICY")
	public class MasterPolicySearchEntity {
		
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_POLICY_SEQ")
		@SequenceGenerator(name = "PMST_POLICY_SEQ", sequenceName = "PMST_POLICY_SEQ", allocationSize = 1)
		@Column(name = "POLICY_ID")
		private Long id;
		
		@OneToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "MPH_ID", referencedColumnName = "MPH_ID")
		private MphSearchEntity policyMPHTmp;  
		 
		
		@Column(name = "POLICY_NUMBER")
		private String policyNumber;

		@Column(name = "POLICY_STATUS_ID")
		private Long policyStatusId;

		@Column(name = "PRODUCT_NAME")
		private String productName;
		
		@Column(name = "PRODUCT_VARIANT")
		private String productVariant;
		
	
		@Column(name = "UNIT_ID")
		private String unitId;
		
		@Column(name = "UNIT_CODE")
		private String unitCode;
		
		@Column(name = "CUSTOMER_NAME")
		private String customerName;
		
		@Column(name = "CUSTOMER_CODE")
		private String customerCode;
		
		@Transient
		@Column(name = "UNIT_OFFICE")
		private String unitOffice;
		
		@Column(name = "PRODUCT_ID")
		private Long productId;
		
		

		@Column(name = "POLICY_TAGGED_STATUS_ID")
		private Long policyTaggedStatusId;	
	 
		
		
		
			}
	
	
	
	
	
	
	
	
	
	
	
	


	
	
	

