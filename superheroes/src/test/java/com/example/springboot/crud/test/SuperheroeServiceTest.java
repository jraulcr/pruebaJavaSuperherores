package com.example.springboot.crud.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.crud.controller.SuperheroeService;
import com.crud.dto.PoderDTO;
import com.crud.dto.SuperheroeDTO;
import com.crud.dto.UniversoDTO;
import com.crud.entity.Poder;
import com.crud.entity.Superheroe;
import com.crud.entity.Universo;
import com.crud.exception.NoHayHeroeException;
import com.crud.exception.NoHayPoderException;
import com.crud.exception.NoHayUniversoException;
import com.crud.exception.RegistroDuplicadoException;
import com.crud.repo.PoderRepository;
import com.crud.repo.SuperheroeRepository;
import com.crud.repo.UniversoRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = SuperheroeServiceTest.class)
class SuperheroeServiceTest {

	private static final String VIVO = "Vivo";
	private static final String MUERTO = "Muerto";

	@InjectMocks
	private SuperheroeService superheroeService;

	@Mock
	private SuperheroeRepository superheroeRepository;
	@Mock
	private UniversoRepository universoRepository;
	@Mock
	private PoderRepository poderRepository;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void contextLoads() {
		assertThat(superheroeRepository).isNotNull();
		assertThat(universoRepository).isNotNull();
		assertThat(poderRepository).isNotNull();
	}

	@Test
	void testGetAllSuperheroes() {
		Universo dc = universoDePrueba();
		Poder superFuerza = poderDePrueba();
		Poder vuelo = new Poder();
		vuelo.setTipoPoder("Vuelo");

		Superheroe heroe1 = heroeDePrueba();
		heroe1.setUniverso(dc);
		heroe1.setPoderes(Arrays.asList(superFuerza, vuelo));

		Superheroe heroe2 = heroeDePrueba();
		heroe2.setHeroeId(2L);
		heroe2.setNombre("Spiderman");
		Poder telarana = new Poder();
		telarana.setTipoPoder("Telaraña");
		heroe2.setUniverso(universoDePrueba());
		heroe2.setPoderes(Collections.singletonList(telarana));

		when(superheroeRepository.findAll()).thenReturn(Arrays.asList(heroe1, heroe2));

		List<SuperheroeDTO> resultado = superheroeService.getAllSuperheroes();

		assertEquals(2, resultado.size(), "El resultado esperado no es el mismo");
		SuperheroeDTO supermanDTO = resultado.get(0);
		assertNotNull(supermanDTO.getUniverso(), "El universo no debe ser nulo");
		assertNotNull(supermanDTO.getPoderes(), "Los poderes no deben ser nulos");
		assertEquals("Superman", supermanDTO.getNombre(), "El nombre no coincide");

		SuperheroeDTO spidermanDTO = resultado.get(1);
		assertNotNull(spidermanDTO.getUniverso(), "El universo no debe ser nulo");
		assertNotNull(spidermanDTO.getPoderes(), "Los poderes no deben ser nulos");
		assertEquals("Spiderman", spidermanDTO.getNombre(), "El nombre no coincide");
	}

	@Test
	void testMapToDTO() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Universo universo = universoDePrueba();
		Poder poder = poderDePrueba();
		Superheroe superheroe = heroeDePrueba();

		Method mapToDTOMethod = SuperheroeService.class.getDeclaredMethod("mapToDTO", Superheroe.class);
		mapToDTOMethod.setAccessible(true);
		SuperheroeDTO dto = (SuperheroeDTO) mapToDTOMethod.invoke(superheroeService, superheroe);

		assertEquals(superheroe.getHeroeId(), dto.getSuperheroeId(), "El id del superhéroe no coincide");
		assertEquals(superheroe.getNombre(), dto.getNombre(), "El nombre del superhéroe no coincide");
		assertEquals(superheroe.getGenero(), dto.getGenero(), "El género del superhéroe no coincide");
		assertEquals(superheroe.getEstado(), dto.getEstado(), "El estado del superhéroe no coincide");

		assertEquals(1, dto.getPoderes().size());
		List<PoderDTO> poderesDTO = dto.getPoderes();
		PoderDTO poderDTO = poderesDTO.iterator().next();
		assertEquals(poder.getPoderId(), poderDTO.getPoderId(), "El id del poder no coincide");
		assertEquals(poder.getTipoPoder(), poderDTO.getTipoPoder(), "El tipo de poder no coincide");

		UniversoDTO universoDTO = dto.getUniverso();
		assertEquals(universo.getUniversoId(), universoDTO.getUniversoId(), "El id del universo no coincide");
		assertEquals(universo.getNombreUniverso(), universoDTO.getNombreUniverso(),
				"El nombre del universo no coincide");
	}

	@Test
	void testSaveSuperheroe() {

		Superheroe superheroe = heroeDePrueba();

		SuperheroeDTO superheroeDTO = new SuperheroeDTO();
		superheroeDTO.setSuperheroeId(superheroe.getHeroeId());
		superheroeDTO.setNombre(superheroe.getNombre());
		superheroeDTO.setGenero(superheroe.getGenero());
		superheroeDTO.setEstado(superheroe.getEstado());

		Universo universo = universoDePrueba();
		UniversoDTO universoDTO = new UniversoDTO();
		universoDTO.setUniversoId(universo.getUniversoId());
		universoDTO.setNombreUniverso(universo.getNombreUniverso());
		superheroeDTO.setUniverso(universoDTO);

		Poder poder = poderDePrueba();
		PoderDTO poderDTO = new PoderDTO();
		poderDTO.setPoderId(poder.getPoderId());
		poderDTO.setTipoPoder(poder.getTipoPoder());
		List<PoderDTO> poderesDTO = Collections.singletonList(poderDTO);
		superheroeDTO.setPoderes(poderesDTO);

		when(superheroeRepository.findById(superheroeDTO.getSuperheroeId())).thenReturn(Optional.empty());
		when(universoRepository.findById(universoDTO.getUniversoId())).thenReturn(Optional.of(universo));
		when(poderRepository.findById(poderDTO.getPoderId())).thenReturn(Optional.of(poder));

		superheroeService.saveSuperheroe(superheroeDTO);

		verify(superheroeRepository, times(1)).findById(superheroe.getHeroeId());
		assertEquals(1L, superheroe.getHeroeId(), "El id del superhéroe no coincide");
	}

	@Test
	void testSaveSuperheroeNuevoUniverso() {
		SuperheroeDTO superheroeDTO = new SuperheroeDTO();
		Superheroe superheroe = heroeDePrueba();
		Universo universo = universoDePrueba();
		Poder poder = poderDePrueba();

		superheroeDTO.setSuperheroeId(superheroe.getHeroeId());
		superheroeDTO.setNombre(superheroe.getNombre());
		superheroeDTO.setGenero(superheroe.getGenero());
		superheroeDTO.setEstado(superheroe.getEstado());

		UniversoDTO universoDTO = new UniversoDTO();
		universoDTO.setUniversoId(universo.getUniversoId());
		universoDTO.setNombreUniverso(universo.getNombreUniverso());
		superheroeDTO.setUniverso(universoDTO);

		PoderDTO poderDTO = new PoderDTO();
		poderDTO.setPoderId(poder.getPoderId());
		poderDTO.setTipoPoder(poder.getTipoPoder());
		List<PoderDTO> poderesDTO = Collections.singletonList(poderDTO);
		superheroeDTO.setPoderes(poderesDTO);

		when(superheroeRepository.findById(superheroeDTO.getSuperheroeId())).thenReturn(Optional.empty());
		when(universoRepository.findById(universoDTO.getUniversoId())).thenReturn(Optional.empty());

		superheroeService.saveSuperheroe(superheroeDTO);

		verify(universoRepository, times(1)).save(any(Universo.class));

		ArgumentCaptor<Universo> universoCaptor = ArgumentCaptor.forClass(Universo.class);
		verify(universoRepository).save(universoCaptor.capture());
		Universo universoGuardado = universoCaptor.getValue();
		assertNotNull(universoGuardado, "No se ha guardado ningún registro del tipo 'Universo', se esperaba que no fuera nulo");
	}

	@Test
	void testSaveSuperheroeSuperheroeExistente() {
		Long heroeId = 1L;
		Superheroe superheroeExistente = heroeDePrueba();
		SuperheroeDTO superheroeDTOExistente = new SuperheroeDTO();
		superheroeDTOExistente.setSuperheroeId(heroeId);

		when(superheroeRepository.findById(heroeId)).thenReturn(Optional.of(superheroeExistente));

		assertThrows(RegistroDuplicadoException.class, () -> {
			superheroeService.saveSuperheroe(superheroeDTOExistente);
		});

		verify(superheroeRepository, times(1)).findById(heroeId);
	    assertEquals(heroeId, superheroeExistente.getHeroeId(), "El superheroe es diferente");
	}

	@Test
	void testDelete() {

		Long heroeId = 1L;
		Superheroe superheroe = heroeDePrueba();
		Universo universo = superheroe.getUniverso();

		when(superheroeRepository.findById(heroeId)).thenReturn(Optional.of(superheroe));
		when(superheroeRepository.existsByUniversoId(universo.getUniversoId())).thenReturn(false);
		when(poderRepository.findAll()).thenReturn(Arrays.asList(poderDePrueba()));

		superheroeService.delete(heroeId);

		verify(superheroeRepository, times(1)).findById(heroeId);
		verify(universoRepository, times(1)).delete(universo);
		verify(poderRepository, times(1)).delete(any(Poder.class));
		verify(superheroeRepository, times(1)).delete(superheroe);
		
	    assertFalse(superheroeRepository.existsById(heroeId), "El superhéroe no se eliminó correctamente");
	}

	@Test
	void testBuscarPorPoder() throws NoHayPoderException {
		Long poderId = 1L;
		Poder poder = poderDePrueba();

		Superheroe superheroe1 = heroeDePrueba();
		Superheroe superheroe2 = heroeDePrueba();

		when(poderRepository.findById(poderId)).thenReturn(Optional.of(poder));
		when(superheroeRepository.findByPoderesIn(Collections.singletonList(poder)))
				.thenReturn(Arrays.asList(superheroe1, superheroe2));

		List<SuperheroeDTO> result = superheroeService.buscarPorPoder(poderId);

		assertEquals(2, result.size(), "El resultado esperado no es el mismo");
	}

	@Test
	void testNoHayPoderException() {
		Long poderId = 1L;
		when(poderRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(NoHayPoderException.class, () -> {
			superheroeService.buscarPorPoder(poderId);
		});

		verify(poderRepository, times(1)).findById(anyLong());
		verify(superheroeRepository, never()).findByPoderesIn(anyList());		
	}

	@Test
	void testMatar() {
		
		Long heroeId = 1L;
		Superheroe superheroe = heroeDePrueba();

		when(superheroeRepository.findById(heroeId)).thenReturn(Optional.of(superheroe));
		when(superheroeRepository.save(superheroe)).thenReturn(superheroe);

		superheroeService.matar(heroeId);

		verify(superheroeRepository).findById(heroeId);
		verify(superheroeRepository).save(superheroe);

		assertEquals(MUERTO, superheroe.getEstado(), "El estado del heroe no coincide");
	}

	@Test
	void testResucitar() {
		
		Long heroeId = 1L;
		Superheroe superheroe = heroeDePrueba();
		superheroe.setEstado(MUERTO);

		when(superheroeRepository.findById(heroeId)).thenReturn(Optional.of(superheroe));
		when(superheroeRepository.save(superheroe)).thenReturn(superheroe);

		superheroeService.resucitar(heroeId);

		verify(superheroeRepository).findById(heroeId);
		verify(superheroeRepository).save(superheroe);

		assertEquals(VIVO, superheroe.getEstado(), "El estado del heroe no coincide");
	}

	@Test
	void testBuscarPorUniverso() throws NoHayUniversoException {
		
		Long universoId = 1L;
		Universo universo = universoDePrueba();
		Superheroe superheroe = heroeDePrueba();
		List<Superheroe> superheroes = Collections.singletonList(superheroe);
		
		when(universoRepository.findById(anyLong())).thenReturn(Optional.of(universo));
		when(superheroeRepository.findByUniversoIn(Collections.singletonList(universo))).thenReturn(superheroes);

		List<SuperheroeDTO> resultado = superheroeService.buscarPorUniverso(universoId);

		assertNotNull(resultado);
		assertEquals(1, resultado.size(), "El resultado esperado no es el mismo");
		SuperheroeDTO superheroeDTO = resultado.get(0);
		assertEquals(superheroe.getUniverso().getUniversoId(), superheroeDTO.getUniverso().getUniversoId(), "El Id del Universo no coincide");

		verify(universoRepository, times(1)).findById(anyLong());
		verify(superheroeRepository, times(1)).findByUniversoIn(Collections.singletonList(universo));
	}

	@Test
	void testNoHayUniversoException() {
		
		Long universoId = 1L;
		when(universoRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(NoHayUniversoException.class, () -> {
			superheroeService.buscarPorUniverso(universoId);
		});

		verify(universoRepository, times(1)).findById(anyLong());
		verify(superheroeRepository, never()).findByUniversoIn(anyList());
	}

	@Test
	void testBuscarPorGenero() {
	    String generoMasculino = "Masculino";
	    String generoFemenino = "Femenino";
	    
	    Superheroe heroeMasculino = heroeDePrueba();
	    heroeMasculino.setGenero(generoMasculino);
	    
	    Superheroe heroeFemenino = heroeDePrueba();
	    heroeFemenino.setGenero(generoFemenino);

	    when(superheroeRepository.findByGenero(generoMasculino)).thenReturn(Collections.singletonList(heroeMasculino));
	    when(superheroeRepository.findByGenero(generoFemenino)).thenReturn(Collections.singletonList(heroeFemenino));

	    List<SuperheroeDTO> resultadoMasculino = superheroeService.buscarPorGenero(generoMasculino);
	    List<SuperheroeDTO> resultadoFemenino = superheroeService.buscarPorGenero(generoFemenino);

	    assertEquals(1, resultadoMasculino.size(), "El resultado esperado no es el mismo");
	    assertEquals(1, resultadoFemenino.size(), "El resultado esperado no es el mismo");

	    SuperheroeDTO superheroeDTOMasculino = resultadoMasculino.get(0);
	    assertEquals(generoMasculino, superheroeDTOMasculino.getGenero(), "El genero no coincide");

	    SuperheroeDTO superheroeDTOFemenino = resultadoFemenino.get(0);
	    assertEquals(generoFemenino, superheroeDTOFemenino.getGenero(), "El genero no coincide");
	}

	@Test
	void testGetHeroeById() throws NoHayHeroeException {
		Long heroeId = 1L;
		Superheroe superheroe = heroeDePrueba();

		when(superheroeRepository.findById(heroeId)).thenReturn(Optional.of(superheroe));

		Optional<SuperheroeDTO> resultado = superheroeService.getHeroeById(heroeId);

		assertNotNull(resultado, "El resultado no puede ser null");
		SuperheroeDTO superheroeDTO = resultado.get();
		assertEquals(superheroe.getHeroeId(), superheroeDTO.getSuperheroeId(), "El id del superhéroe no coincide");

		verify(superheroeRepository, times(1)).findById(anyLong());
	}

	@Test
	void testNoHayHeroeException() {

		Long heroeId = 1L;
		when(superheroeRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(NoHayHeroeException.class, () -> {
			superheroeService.getHeroeById(heroeId);
		});

		verify(superheroeRepository, times(1)).findById(anyLong());
	}

	
	private Superheroe heroeDePrueba() {
		Superheroe superheroe = new Superheroe();
		superheroe.setHeroeId(1L);
		superheroe.setNombre("Superman");
		superheroe.setGenero("H");
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
