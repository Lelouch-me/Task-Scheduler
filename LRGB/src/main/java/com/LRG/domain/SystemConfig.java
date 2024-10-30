package com.LRG.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "system_configuration")
public class SystemConfig {
	
	@Id
	@Column(name = "id", nullable = false)
	private int id;
	
	@Column(name = "value")
	private String value;
	
	public SystemConfig() {
		super();
	}
	
	public SystemConfig(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
