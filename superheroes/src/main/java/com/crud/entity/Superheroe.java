package com.crud.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Superheroe implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "heroe_id", unique = true, nullable = false)
	private Long heroeId;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@Column(name = "genero", nullable = false)
	private String genero;

	@Column(name = "es_vivo", nullable = false)
	private boolean vivo = true;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(name = "uni_id"), name = "universo_id", insertable = true, updatable = true)
	private Universo universo;

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "superheroe_poderes", foreignKey = @ForeignKey(name = "heroe_id_fk"), 
	joinColumns = @JoinColumn(name = "heroe_id", nullable = false, referencedColumnName = "heroe_id", insertable = true, updatable = true), 
	inverseForeignKey = @ForeignKey(name = "poder_id_fk"), 
	inverseJoinColumns = @JoinColumn(name = "poder_id", nullable = false))
	private List<Poder> poderes;

	public Long getHeroeId() {
		return heroeId;
	}

	public void setHeroeId(Long heroeId) {
		this.heroeId = heroeId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public boolean isVivo() {
		return vivo;
	}

	public void setVivo(boolean vivo) {
		this.vivo = vivo;
	}

	public Universo getUniverso() {
		return universo;
	}

	public void setUniverso(Universo universo) {
		this.universo = universo;
	}

	public List<Poder> getPoderes() {
		return poderes;
	}

	public void setPoderes(List<Poder> poderes) {
		this.poderes = poderes;
	}

}
