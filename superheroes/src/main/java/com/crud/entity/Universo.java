package com.crud.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * @author jrcrespo
 *
 */
@Entity
@Table(name = "universos")
public class Universo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "universo_id", unique = true, nullable = false)
	private Long universoId;
	
	@Column(name = "nombre_universo", nullable = false)
	private String nombreUniverso;

    @OneToMany(mappedBy = "universo")
    private List<Superheroe> superheroes;

	public Long getUniversoId() {
		return universoId;
	}

	public void setUniversoId(Long universoId) {
		this.universoId = universoId;
	}

	public String getNombreUniverso() {
		return nombreUniverso;
	}

	public void setNombreUniverso(String nombreUniverso) {
		this.nombreUniverso = nombreUniverso;
	}

}
