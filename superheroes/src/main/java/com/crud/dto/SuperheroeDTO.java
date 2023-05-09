package com.crud.dto;

import java.util.List;

public class SuperheroeDTO {

    private Long id;
    private String nombre;
    private List<String> poderes;
    private String universo;
    private boolean estado;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<String> getPoderes() {
		return poderes;
	}
	public void setPoderes(List<String> poderes) {
		this.poderes = poderes;
	}
	public String getUniverso() {
		return universo;
	}
	public void setUniverso(String universo) {
		this.universo = universo;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}		
}
