package com.crud.dto;

/**
 * @author jrcrespo
 *
 */
public class UniversoDTO {
	
    private Long universoId;
    private String nombreUniverso;
    
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
