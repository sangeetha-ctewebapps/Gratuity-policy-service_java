package com.lic.epgs.gratuity.policy.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PMST_MPH")
public class MphSearchEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMST_MPH_ID_SEQ")
	@SequenceGenerator(name = "PMST_MPH_ID_SEQ", sequenceName = "PMST_MPH_ID_SEQ", allocationSize = 1)
	@Column(name = "MPH_ID")
	private Long id;	
	
	@Column(name = "MPH_NAME")
	private String mphName;

	@Column(name = "PAN")
	private String pan;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive; 


}

	
	
