package com.crud.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crud.dto.SuperheroeDTO;
import com.crud.entity.Poder;
import com.crud.entity.Superheroe;
import com.crud.repo.SuperheroeRepository;

/**
 * 
 * https://www.javatpoint.com/dto-java
 * 
 */

@Service
public class SuperheroeService {

	final Logger LOG = Logger.getLogger("SuperheroeService.class");

	private static final boolean VIVO = true;
	private static final boolean MUERTO = false;

	@Autowired
	private SuperheroeRepository repository;

	public List<SuperheroeDTO> getAllSuperheroes() {
		List<Superheroe> superheroes = repository.findAll();
		return superheroes.stream().map(this::mapToDTO).collect(Collectors.toList());
	}

	public Optional<SuperheroeDTO> getHeroeById(Long id) {
		return Optional.ofNullable(repository.findById(id).map(this::mapToDTO)
				.orElseThrow(() -> new NoSuchElementException("** No existe superheroe con este id :: " + id)));
	}

	public void matar(Long id) {
		Superheroe superheroe = repository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("** No existe superheroe con este id :: " + id));
		if (VIVO == superheroe.isEstado()) {
			superheroe.setEstado(MUERTO);
		}
		repository.save(superheroe);
	}

	public void resucitar(Long id) {
		Superheroe superheroe = repository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("** No existe superheroe con este id :: " + id));
		if (MUERTO == superheroe.isEstado()) {
			superheroe.setEstado(VIVO);
		}
		repository.save(superheroe);
	}
	
	public void delete(Long id) {
		Superheroe superheroe = repository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("** No existe superheroe con este id :: " + id));
		repository.delete(superheroe);
	}
	
	
	
	

	private SuperheroeDTO mapToDTO(Superheroe superheroe) {
		SuperheroeDTO dto = new SuperheroeDTO();
		dto.setId(superheroe.getHeroeId());
		dto.setNombre(superheroe.getNombre());
		dto.setEstado(superheroe.isEstado());
		dto.setPoderes(superheroe.getPoderes().stream().map(Poder::getTipoPoder).collect(Collectors.toList()));
		dto.setUniverso(superheroe.getUniverso().getNombreUniverso());
		return dto;
	}


}
