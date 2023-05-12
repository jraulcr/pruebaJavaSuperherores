package com.crud.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

/**
 * @author jrcrespo
 *
 */
@Service
public class SuperheroeService {

	private static final String VIVO = "Vivo";
	private static final String MUERTO = "Muerto";	

	@Autowired
    private SuperheroeRepository superheroeRepository;
	@Autowired
    private UniversoRepository universoRepository;
	@Autowired
    private PoderRepository poderRepository;
	
	
	public List<SuperheroeDTO> getAllSuperheroes() {
	    List<Superheroe> superheroes = superheroeRepository.findAll();
	    return mapToDTOList(superheroes);
	}		
	
	public void saveSuperheroe(SuperheroeDTO superheroeDTO) throws RegistroDuplicadoException {
	    // Crear instancia de Superheroe y asignarle los valores del DTO
	    Superheroe superheroe = new Superheroe();
	    Universo universo = new Universo();
	    List<Poder> poderes = new ArrayList<>(); 	    
	    
	    superheroe.setHeroeId(superheroeDTO.getSuperheroeId());	 	    
	    superheroe.setNombre(superheroeDTO.getNombre());
	    superheroe.setGenero(superheroeDTO.getGenero());
	    superheroe.setEstado(superheroeDTO.getEstado());
	    superheroe.setUniverso(universo);
	    superheroe.setPoderes(poderes);

	    // Verificar si el Superheroe ya existe en la base de datos
	    superheroeRepository.findById(superheroe.getHeroeId())
        .ifPresent(s -> {
            throw new RegistroDuplicadoException(superheroeDTO.getSuperheroeId());
        });  	    

	    universo.setUniversoId(superheroeDTO.getUniverso().getUniversoId());
	    universo.setNombreUniverso(superheroeDTO.getUniverso().getNombreUniverso());

	    // Buscar el objeto Universo asociado al Superheroe
	    Optional<Universo> universoExistente = universoRepository.findById(universo.getUniversoId());

	    if(universoExistente.isPresent()) {
	        // Si el objeto Universo ya existe, no lo guardamos de nuevo
	        universo = universoExistente.get();
	    } else {
	        // Si el objeto Universo no existe, lo guardamos en la base de datos
	        universoRepository.save(universo);
	    }	    
	    
	    universoRepository.save(universo);

	    // Buscar o crear los objetos Poder asociados al Superheroe    
	    poderes.addAll(superheroeDTO.getPoderes().stream()
	    	    .map(p -> {
	    	            Optional<Poder> poderOptional = poderRepository.findById(p.getPoderId());
	    	            if (poderOptional.isPresent()) {
	    	                return poderOptional.get();
	    	            } else {
	    	                Poder poder = new Poder();
	    	                poder.setPoderId(p.getPoderId());
	    	                poder.setTipoPoder(p.getTipoPoder());
	    	                return poderRepository.save(poder);
	    	            }
	    	    })
	    	    .collect(Collectors.toSet()));   
	    	    
	    superheroeRepository.save(superheroe);
	}
	
	public Optional<SuperheroeDTO> getHeroeById(Long id) {
		return Optional.ofNullable(superheroeRepository.findById(id).map(this::mapToDTO)
				.orElseThrow(() -> new NoHayHeroeException(id)));
	}

	public void matar(Long id) {
		Superheroe superheroe = superheroeRepository.findById(id)
				.orElseThrow(() -> new NoHayHeroeException(id));
		if (VIVO.equals(superheroe.getEstado())) {
			superheroe.setEstado(MUERTO);
		}
		superheroeRepository.save(superheroe);
	}

	public void resucitar(Long id) {
		Superheroe superheroe = superheroeRepository.findById(id)
				.orElseThrow(() -> new NoHayHeroeException(id));
		if (MUERTO.equals(superheroe.getEstado())) {
			superheroe.setEstado(VIVO);
		}
		superheroeRepository.save(superheroe);
	}
	
	public void delete(Long id) {
	    Superheroe superheroe = superheroeRepository.findById(id)
	            .orElseThrow(() -> new NoHayHeroeException(id));
	    Universo universo = superheroe.getUniverso();
	    // Verificar si hay otros superhéroes asociados a este universo
	    boolean existenOtrosSuperheroes = superheroeRepository.existsByUniversoId(universo.getUniversoId());
	    if (!existenOtrosSuperheroes) {
	        universoRepository.delete(universo);
	    }
	    
	 // Eliminar los registros relacionados en la tabla intermedia
	    superheroe.getPoderes().forEach(poder -> poder.getSuperheroes().remove(superheroe));
	    superheroe.getPoderes().clear();

	    // Verificar si otros superhéroes todavía usan el poder
	    for (Poder poder : poderRepository.findAll()) {
	        if (poder.getSuperheroes().isEmpty()) {
	            poderRepository.delete(poder);
	        }
	    }

	    superheroeRepository.delete(superheroe);
	}
		
    public List<SuperheroeDTO> buscarPorGenero(String genero) {
        List<Superheroe> superheroes = superheroeRepository.findByGenero(genero);
        return mapToDTOList(superheroes);
    }

    public List<SuperheroeDTO> buscarPorUniverso(Long id) {
        return universoRepository.findById(id)
                .map(universo -> superheroeRepository.findByUniversoIn(Collections.singletonList(universo)))
                .orElseThrow(() -> new NoHayUniversoException(id))
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }     
    
    public List<SuperheroeDTO> buscarPorPoder(Long id) {
        return poderRepository.findById(id)
                .map(poder -> superheroeRepository.findByPoderesIn(Collections.singletonList(poder)))
                .orElseThrow(() -> new NoHayPoderException(id))
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
  
	// Método para convertir una lista de entidades Superheroe a una lista de DTOs SuperheroeDTO
	private List<SuperheroeDTO> mapToDTOList(List<Superheroe> superheroes) {
	    return superheroes.stream().map(this::mapToDTO).collect(Collectors.toList());
	}

	// Método para convertir una entidad Superheroe a DTO SuperheroeDTO
	private SuperheroeDTO mapToDTO(Superheroe superheroe) {
	    SuperheroeDTO dto = new SuperheroeDTO();
	    dto.setSuperheroeId(superheroe.getHeroeId());
	    dto.setNombre(superheroe.getNombre());
	    dto.setGenero(superheroe.getGenero());
	    dto.setEstado(superheroe.getEstado());
	    dto.setPoderes(new ArrayList<>(superheroe.getPoderes().stream().map(this::mapToPoderDTO).collect(Collectors.toSet())));
	    dto.setUniverso(mapToUniversoDTO(superheroe.getUniverso()));
	    return dto;
	}

	// Método para convertir una entidad Universo a DTO UniversoDTO
	private UniversoDTO mapToUniversoDTO(Universo universo) {
	    UniversoDTO dto = new UniversoDTO();
	    dto.setUniversoId(universo.getUniversoId());
	    dto.setNombreUniverso(universo.getNombreUniverso());
	    return dto;
	}

	// Método para convertir una entidad Poder a DTO PoderDTO
	private PoderDTO mapToPoderDTO(Poder poder) {
	    PoderDTO dto = new PoderDTO();
	    dto.setPoderId(poder.getPoderId());
	    dto.setTipoPoder(poder.getTipoPoder());
	    return dto;
	}
}
