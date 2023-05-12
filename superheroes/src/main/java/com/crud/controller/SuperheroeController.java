package com.crud.controller;

import java.util.List;
import java.util.Optional;

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

	private static final Logger LOGER = LogManager.getLogger(SuperheroeController.class);

	@Autowired
	private SuperheroeService superheroeService;

	public SuperheroeController(SuperheroeService superheroeService) {
		this.superheroeService = superheroeService;
	}

	@PostMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> save(@RequestBody SuperheroeDTO superheroeDTO) throws RegistroDuplicadoException {
		superheroeService.saveSuperheroe(superheroeDTO);
		LOGER.info("Superheroe con id {} creado exitosamente.", superheroeDTO.getSuperheroeId());
		String mensaje = "¡Superheroe creado ...!";
		return new ResponseEntity<>(mensaje, HttpStatus.CREATED);
	}

	@GetMapping("/find")
	@ResponseBody
	public List<SuperheroeDTO> getAllHeroes() {
		LOGER.info("Buscando todos los superhéroes.");
		return superheroeService.getAllSuperheroes();
	}

	@GetMapping("/find/{heroe_id}")
	public Optional<SuperheroeDTO> find(@PathVariable(value = "heroe_id") Long heroe_id) {
	    LOGER.info("Buscando el superhéroe con id {}.", heroe_id);
		return superheroeService.getHeroeById(heroe_id);
	}

	@PutMapping("/matar/{heroe_id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> matarSuperheroe(@PathVariable(value = "heroe_id") Long heroe_id) {
		LOGER.info("Matando al superhéroe con id {}.", heroe_id);
		superheroeService.matar(heroe_id);
	    String mensaje = "¡Superheroe muerto ...!";
	    return new ResponseEntity<>(mensaje, HttpStatus.OK);
	}

	@PutMapping("/resucitar/{heroe_id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> resucitarSuperheroe(@PathVariable(value = "heroe_id") Long heroe_id) {
		LOGER.info("Resucitando al superhéroe con id {}.", heroe_id);
		superheroeService.resucitar(heroe_id);
	    String mensaje = "¡Superheroe resucitado ...!";
	    return new ResponseEntity<>(mensaje, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{heroe_id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> deleteHeroeId(@PathVariable(value = "heroe_id") Long heroe_id) {
		LOGER.info("Eliminando al superhéroe con id {}.", heroe_id);
		superheroeService.delete(heroe_id);
	    String mensaje = "¡Superheroe eliminado ...!";
	    return new ResponseEntity<>(mensaje, HttpStatus.OK);
	}
	
    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<SuperheroeDTO>> buscarPorGenero(@PathVariable("genero") String genero) {
        List<SuperheroeDTO> superheroeDTOList = superheroeService.buscarPorGenero(genero);
        return new ResponseEntity<>(superheroeDTOList, HttpStatus.OK);
    } 
    
    @GetMapping("/universo/{universo_id}")
    public ResponseEntity<List<SuperheroeDTO>> buscarPorUniverso(@PathVariable("universo_id") Long universo_id) {
    	List<SuperheroeDTO> superheroeDTOList = superheroeService.buscarPorUniverso(universo_id);
        return new ResponseEntity<>(superheroeDTOList, HttpStatus.OK);
    }   
    
    @GetMapping("/poder/{poder_id}")
    public ResponseEntity<List<SuperheroeDTO>> buscarPorPoder(@PathVariable(value = "poder_id") Long poder_id) {
    	List<SuperheroeDTO> superheroeDTOList = superheroeService.buscarPorPoder(poder_id);
        return new ResponseEntity<>(superheroeDTOList, HttpStatus.OK);
    }            
    
}
