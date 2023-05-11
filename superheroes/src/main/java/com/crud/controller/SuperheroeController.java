package com.crud.controller;

import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.crud.dto.SuperheroeDTO;
import com.crud.exception.RegistroDuplicadoException;

@RestController
@RequestMapping("/superheroe")
public class SuperheroeController {

	/**
	 * 
	 */
	private static final Logger LOGER = LogManager.getLogger(SuperheroeController.class);

	@Autowired
	private SuperheroeService superheroeService;

	public SuperheroeController(SuperheroeService superheroeService) {
		this.superheroeService = superheroeService;
	}

	@PostMapping("/create")
	public ResponseEntity<Void> save(@RequestBody SuperheroeDTO superheroeDTO) throws RegistroDuplicadoException {
		superheroeService.saveSuperheroe(superheroeDTO);
		LOGER.info("Superheroe con id {} creado exitosamente.", superheroeDTO.getSuperheroeId());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/find")
	@ResponseBody
	public Set<SuperheroeDTO> getAllHeroes() {
		LOGER.info("Buscando todos los superhéroes.");
		return superheroeService.getAllSuperheroes();
	}

	@GetMapping("/find/{heroe_id}")
	public Optional<SuperheroeDTO> find(@PathVariable(value = "heroe_id") Long heroe_id) {
	    LOGER.info("Buscando el superhéroe con id {}.", heroe_id);
		return superheroeService.getHeroeById(heroe_id);
	}

	@PutMapping("/matar/{heroe_id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public String matarSuperheroe(@PathVariable(value = "heroe_id") Long heroe_id) {
		LOGER.info("Matando al superhéroe con id {}.", heroe_id);
		superheroeService.matar(heroe_id);
		return "¡Superheroe muerto ...!";
	}

	@PutMapping("/resucitar/{heroe_id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public String resucitarSuperheroe(@PathVariable(value = "heroe_id") Long heroe_id) {
		LOGER.info("Resucitando al superhéroe con id {}.", heroe_id);
		superheroeService.resucitar(heroe_id);
		return "¡Superheroe resucitado ...!";
	}

	@DeleteMapping("/delete/{heroe_id}")
	public String delete(@PathVariable(value = "heroe_id") Long heroe_id) {
		LOGER.info("Eliminando al superhéroe con id {}.", heroe_id);
		superheroeService.delete(heroe_id);
		return "¡Superheroe eliminado ...!";
	}
	
    @GetMapping("/genero/{genero}")
    public ResponseEntity<Set<SuperheroeDTO>> buscarPorGenero(@PathVariable("genero") String genero) {
        Set<SuperheroeDTO> superheroeDTOList = superheroeService.buscarPorGenero(genero);
        return new ResponseEntity<>(superheroeDTOList, HttpStatus.OK);
    }
    
    @GetMapping("/universo/{universo}")
    public ResponseEntity<Set<SuperheroeDTO>> buscarPorUniverso(@PathVariable("universo") String universo) {
        Set<SuperheroeDTO> superheroeDTOList = superheroeService.buscarPorUniverso(universo);
        return new ResponseEntity<>(superheroeDTOList, HttpStatus.OK);
    }
    
    
}
