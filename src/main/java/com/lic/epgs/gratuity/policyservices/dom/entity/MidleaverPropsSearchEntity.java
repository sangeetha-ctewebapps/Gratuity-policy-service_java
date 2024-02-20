package com.lic.epgs.gratuity.policyservices.dom.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "PMST_MIDLEAVER_PROPS")
public class MidleaverPropsSearchEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PMST_MIDLEAVER_PROPS_ID")
	private Long id;

	@Column(name = "STATUS_ID")
	private Long statusId;

	@OneToOne
	@JoinColumn(name = "TMP_POLICY_ID", referencedColumnName = "POLICY_ID")
	private DOMSearchEntity domSearchEntity;

}
