package com.lic.epgs.gratuity.policy.renewal.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@Table(name = "EMAIL_MESSAGES")
public class EmailMessagesEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMAIL_MESSAGES_ID_SEQ")
	@SequenceGenerator(name = "EMAIL_MESSAGES_ID_SEQ", sequenceName = "EMAIL_MESSAGES_ID_SEQ", allocationSize = 1)
	@Column(name = "EMAIL_MESSAGES_ID")
	private Long id;
	
	@Column(name = "NAME")
	private String name;

	@Column(name = "SUBJECT")
	private String subject;

	@Column(name = "CONTENT")
	private String content;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	
}
