package com.crud.dto;

/**
 * @author jrcrespo
 *
 */
public class PoderDTO {
    private Long poderId;
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
