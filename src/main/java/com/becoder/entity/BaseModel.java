package com.becoder.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseModel {
	private Integer createdBy;
	@Column(name = "created_on", insertable = false, updatable = false)
	private Date createdOn;
	private Integer updatedBy;
	private Date updatedOn;
}
