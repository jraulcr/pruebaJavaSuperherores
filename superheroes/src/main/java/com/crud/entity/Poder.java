package com.crud.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Poder  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "poder_id", unique = true, nullable = false)
	private Long poderId;
	
	@Column(name = "tipo_poder", nullable = false)
	private String tipoPoder;
	
	public Long getPoderId() {
		return poderId;
	}

	public void setPoderId(Long poderId) {
		this.poderId = poderId;
	}

	public String getTipoPoder() {
		return tipoPoder;
	}

	public void setTipoPoder(String tipoPoder) {
		this.tipoPoder = tipoPoder;
	}
	
}
