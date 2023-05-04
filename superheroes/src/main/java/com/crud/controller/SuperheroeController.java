package com.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.entity.Superheroe;
import com.crud.repo.SuperheroeRepository;

@RestController
@RequestMapping("/superheroe")
public class SuperheroeController {

	@Autowired	
	private SuperheroeRepository superheroeRepository;
	

	@PostMapping("/create")
	public Superheroe save(@RequestBody Superheroe superheroe) {
		return superheroeRepository.save(superheroe);
	}
	

	@GetMapping("/findAll")
	public List<Superheroe> findAllStuent(){
		return superheroeRepository.findAll();
	}

	@GetMapping("/find/{ID}")
	public Superheroe find(@PathVariable(value = "ID")Long id) {		
		return superheroeRepository.findById(id).orElseThrow(() -> new RuntimeException("Student is not present database"));		
	}
	

	@PutMapping("/update")
	public Superheroe update(@RequestBody Superheroe heroe) {
		return superheroeRepository.save(heroe);
	}

	@DeleteMapping("/delete/{ID}")
public String delete(@PathVariable(value = "ID")Long id) {		
		Superheroe student = superheroeRepository.findById(id).orElseThrow(() -> new RuntimeException("Student is not present database"));
		superheroeRepository.delete(student);
		return "Student deleted successfully ...!";
	}
}
