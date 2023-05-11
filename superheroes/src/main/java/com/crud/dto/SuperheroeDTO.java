package com.crud.dto;

import java.util.List;

/**
 * @author jrcrespo
 *
 */
public class SuperheroeDTO {

    private Long superheroeId;
    private String nombre;
    private String genero;
    private boolean estado;    
    private UniversoDTO universo;
    private List<PoderDTO> poderes;   
	
	public Long getSuperheroeId() {
		return superheroeId;
	}
	public void setSuperheroeId(Long superheroeId) {
		this.superheroeId = superheroeId;
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
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public UniversoDTO getUniverso() {
		return universo;
	}
	public void setUniverso(UniversoDTO universo) {
		this.universo = universo;
	}
	public List<PoderDTO> getPoderes() {
		return poderes;
	}
	public void setPoderes(List<PoderDTO> list) {
		this.poderes = list;
	}
    
    
}
