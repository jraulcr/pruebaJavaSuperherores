package com.crud.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.crud.entity.Superheroe;
import com.crud.repo.SuperheroeRepository;

@RestController
@RequestMapping("/superheroe")
public class SuperheroeController {

	@Autowired	
	private SuperheroeRepository superheroeRepository;
	
	@Autowired  
	private SuperheroeService superheroeService;
	
    public SuperheroeController(SuperheroeService superheroeService) {
        this.superheroeService = superheroeService;
    }
	

	@PostMapping("/create")
	public Superheroe save(@RequestBody Superheroe heroe) {
		return superheroeRepository.save(heroe);
	}
	 
    @GetMapping("/find")  
    @ResponseBody  
    public List<SuperheroeDTO> getAllHeroes() {  
        return superheroeService.getAllSuperheroes();  
    }  

	@GetMapping("/find/{heroe_id}")
	public Optional<SuperheroeDTO> find(@PathVariable(value = "heroe_id") Long heroe_id) {		
		return superheroeService.getHeroeById(heroe_id);
	}
	
    @PutMapping("/{heroe_id}/matar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String matarSuperheroe(@PathVariable(value = "heroe_id") Long heroe_id) {
        superheroeService.matar(heroe_id);
        return "Superheroe muerto ...!";
    }
 
    @PutMapping("/{heroe_id}/resucitar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String resucitarSuperheroe(@PathVariable(value = "heroe_id") Long heroe_id) {
        superheroeService.resucitar(heroe_id);
        return "Superheroe resucitado ...!";
    }	
	
	@DeleteMapping("/delete/{heroe_id}")
	public String delete(@PathVariable(value = "heroe_id")Long heroe_id) {	
		superheroeService.delete(heroe_id);
		return "Superheroe eliminado ...!";
	}
}
