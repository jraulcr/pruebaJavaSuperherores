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

/**
 * @author jrcrespo
 *
 */
@Entity
@Table(name = "superheroes")
public class Superheroe implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "heroe_id", unique = true, nullable = false)
	private Long heroeId;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@Column(name = "genero", nullable = false)
	private String genero;

	@Column(name = "estado", nullable = false)
	private String estado;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "universo_id", referencedColumnName = "universo_id", foreignKey = @ForeignKey(name = "uni_id"))
	private Universo universo;
	
	
    @Column(name = "universo_id", insertable = false, updatable = false)
    private Long universoId;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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

	public Long getUniversoId() {
		return universoId;
	}

	public void setUniversoId(Long universoId) {
		this.universoId = universoId;
	}
	
}
