package com.crud.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * @author jrcrespo
 *
 */
@Entity
@Table(name = "poderes")
public class Poder implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "poder_id", unique = true, nullable = false)
	private Long poderId;
	
	@Column(name = "tipo_poder", nullable = false)
	private String tipoPoder;
	
    @ManyToMany(mappedBy = "poderes", fetch = FetchType.LAZY)
    private List<Superheroe> superheroes;

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

	public List<Superheroe> getSuperheroes() {
		return superheroes;
	}

	public void setSuperheroes(List<Superheroe> superheroes) {
		this.superheroes = superheroes;
	}

}
