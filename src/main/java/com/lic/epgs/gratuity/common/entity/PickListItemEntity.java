package com.lic.epgs.gratuity.common.entity;

import java.util.Date;

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

/**
 * @author Gopi
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PICK_LIST_ITEM")
public class PickListItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PICK_LIST_ITEM_ID_SEQUENCE")
	@SequenceGenerator(name = "PICK_LIST_ITEM_ID_SEQUENCE", sequenceName = "PICK_LIST_ITEM_ID_SEQUENCE", allocationSize = 1)
	@Column(name = "PICK_LIST_ITEM_ID")
	private Long id;
	
	@Column(name = "PICK_LIST_ID")
	private Long pickListId;
	
	@Column(name = "PICK_LIST_ITEM_NAME", length = 32)
	private String name;
	
	@Column(name = "PICK_LIST_ITEM_DESCRIPTION", length = 32)
	private String description;
	
	@Column(name = "DISPLAY_ORDER")
	private Long displayOrder;
	
	@Column(name = "PARENT_ID")
	private Long parentId;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "CREATED_BY", length = 128)
	private String createdBy;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY", length = 128)
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
}
