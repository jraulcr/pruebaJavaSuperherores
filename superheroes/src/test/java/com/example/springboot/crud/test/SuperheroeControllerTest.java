package com.example.springboot.crud.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.crud.controller.SuperheroeController;
import com.crud.controller.SuperheroeService;
import com.crud.dto.PoderDTO;
import com.crud.dto.SuperheroeDTO;
import com.crud.dto.UniversoDTO;
import com.crud.entity.Poder;
import com.crud.entity.Superheroe;
import com.crud.entity.Universo;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = SuperheroeControllerTest.class)
class SuperheroeControllerTest {

	private static final String RESULTADO_NOK = "El resultado no es el esperado";
	
	@InjectMocks
	private SuperheroeController superheroeController;

	@Mock
	private SuperheroeService superheroeService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testBuscarPorGenero() {

		String genero = "masculino";
		List<SuperheroeDTO> superheroeDTOList = new ArrayList<>();

		when(superheroeService.buscarPorGenero(genero)).thenReturn(superheroeDTOList);

		ResponseEntity<List<SuperheroeDTO>> response = superheroeController.buscarPorGenero(genero);

		assertEquals(HttpStatus.OK, response.getStatusCode(), RESULTADO_NOK);

		verify(superheroeService).buscarPorGenero(genero);
	}

	@Test
	void testBuscarPorPoder() {
		
		Long poderId = 1L;
		List<SuperheroeDTO> superheroeDTOList = new ArrayList<>();

		Superheroe superheroe = heroeDePrueba();

		SuperheroeDTO superheroeDTO = new SuperheroeDTO();
		superheroeDTO.setPoderes(new ArrayList<>(superheroe.getPoderes().stream().map(poder -> {
			PoderDTO poderDTO = new PoderDTO();
			poderDTO.setPoderId(poder.getPoderId());
			poderDTO.setTipoPoder(poder.getTipoPoder());
			return poderDTO;
		}).collect(Collectors.toSet())));

		superheroeDTOList.add(superheroeDTO);

		when(superheroeService.buscarPorPoder(poderId)).thenReturn(superheroeDTOList);

		ResponseEntity<List<SuperheroeDTO>> response = superheroeController.buscarPorPoder(poderId);

		assertEquals(HttpStatus.OK, response.getStatusCode(), RESULTADO_NOK);

		verify(superheroeService).buscarPorPoder(poderId);
	}

	@Test
	void testBuscarPorUniverso() {
		Long universoId = 1L;
		List<SuperheroeDTO> superheroeDTOList = new ArrayList<>();

		Superheroe superheroe = heroeDePrueba();

		SuperheroeDTO superheroeDTO = new SuperheroeDTO();

		UniversoDTO universoDTO = new UniversoDTO();
		universoDTO.setUniversoId(superheroe.getUniverso().getUniversoId());
		universoDTO.setNombreUniverso(superheroe.getUniverso().getNombreUniverso());
		superheroeDTO.setUniverso(universoDTO);
		superheroeDTOList.add(superheroeDTO);

		when(superheroeService.buscarPorUniverso(universoId)).thenReturn(superheroeDTOList);

		ResponseEntity<List<SuperheroeDTO>> response = superheroeController.buscarPorUniverso(universoId);

		assertEquals(HttpStatus.OK, response.getStatusCode(), RESULTADO_NOK);

		verify(superheroeService, times(1)).buscarPorUniverso(universoId);
	}

	@Test
	void testMatarSuperheroe() {
		Long heroeId = 1L;

		ResponseEntity<String> response = superheroeController.matarSuperheroe(heroeId);

		assertEquals(HttpStatus.OK, response.getStatusCode(), RESULTADO_NOK);

		verify(superheroeService, times(1)).matar(heroeId);
	}

	@Test
	void testResucitarSuperheroe() {
		Long heroeId = 1L;

		ResponseEntity<String> response = superheroeController.resucitarSuperheroe(heroeId);

		assertEquals(HttpStatus.OK, response.getStatusCode(), RESULTADO_NOK);

		verify(superheroeService, times(1)).resucitar(heroeId);
	}

	@Test
	void testBuscarPorId() {
		Long heroeId = 1L;
		Superheroe superheroe = heroeDePrueba();
		SuperheroeDTO superheroeDTO = new SuperheroeDTO();
		superheroeDTO.setSuperheroeId(superheroe.getHeroeId());

		when(superheroeService.getHeroeById(heroeId)).thenReturn(Optional.of(superheroeDTO));

		Optional<SuperheroeDTO> response = superheroeController.buscarPorId(heroeId);

		assertTrue(response.isPresent());

		SuperheroeDTO retrievedSuperheroeDTO = response.get();
		assertEquals(heroeId, retrievedSuperheroeDTO.getSuperheroeId());
	}

	@Test
	void testGetAllHeroes() {
		Superheroe superheroe1 = heroeDePrueba();
		SuperheroeDTO superheroeDTO1 = new SuperheroeDTO();
		superheroeDTO1.setSuperheroeId(superheroe1.getHeroeId());
		superheroeDTO1.setNombre(superheroe1.getNombre());
		superheroeDTO1.setGenero(superheroe1.getGenero());
		superheroeDTO1.setEstado(superheroe1.getEstado());

		SuperheroeDTO superheroeDTO2 = new SuperheroeDTO();
		superheroeDTO2.setSuperheroeId(2L);
		superheroeDTO2.setNombre("Mujer Maravilla");
		superheroeDTO2.setGenero("Mujer");
		superheroeDTO2.setEstado("Vivo");

		List<SuperheroeDTO> superheroes = Arrays.asList(superheroeDTO1, superheroeDTO2);

		when(superheroeService.getAllSuperheroes()).thenReturn(superheroes);

		List<SuperheroeDTO> response = superheroeController.getTodosHeroes();

		assertEquals(superheroes, response, "El resultado es diferente al esperado");
	}

	@Test
	void testSave() {
		Superheroe superheroe = heroeDePrueba();
		SuperheroeDTO superheroeDTO = new SuperheroeDTO();
		superheroeDTO.setSuperheroeId(superheroe.getHeroeId());
		superheroeDTO.setNombre(superheroe.getNombre());
		superheroeDTO.setGenero(superheroe.getGenero());
		superheroeDTO.setEstado(superheroe.getEstado());

		ResponseEntity<String> response = superheroeController.guardar(superheroeDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode(), RESULTADO_NOK);
	}

	@Test
	void testDeleteHeroeId() {
		Long heroeId = 1L;

		doNothing().when(superheroeService).delete(heroeId);

		ResponseEntity<String> response = superheroeController.borrarHeroeId(heroeId);

		assertEquals(HttpStatus.OK, response.getStatusCode(), RESULTADO_NOK);
	}

	private Superheroe heroeDePrueba() {
		Superheroe superheroe = new Superheroe();
		superheroe.setHeroeId(1L);
		superheroe.setNombre("Superman");
		superheroe.setGenero("Hombre");
		superheroe.setEstado("Vivo");

		Universo universo = universoDePrueba();
		universo.getSuperheroes().add(superheroe);
		superheroe.setUniverso(universo);

		Poder poder = poderDePrueba();
		poder.getSuperheroes().add(superheroe);
		superheroe.setPoderes(Collections.singletonList(poder));
		return superheroe;
	}

	private Universo universoDePrueba() {
		Universo universo = new Universo();
		universo.setUniversoId(1L);
		universo.setNombreUniverso("DC Comics");
		universo.setSuperheroes(new ArrayList<>());
		return universo;
	}

	private Poder poderDePrueba() {
		Poder poder = new Poder();
		poder.setPoderId(1L);
		poder.setTipoPoder("Super Fuerza");
		poder.setSuperheroes(new ArrayList<>());
		return poder;
	}

}
