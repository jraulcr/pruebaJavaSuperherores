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
	    Superheroe superheroe = new Superheroe();
	    Universo universo = new Universo();
	    List<Poder> poderes = new ArrayList<>(); 	    
	    
	    superheroe.setHeroeId(superheroeDTO.getSuperheroeId());	 	    
	    superheroe.setNombre(superheroeDTO.getNombre());
	    superheroe.setGenero(superheroeDTO.getGenero());
	    superheroe.setEstado(superheroeDTO.getEstado());
	    superheroe.setUniverso(universo);
	    superheroe.setPoderes(poderes);
	
	    superheroeRepository.findById(superheroe.getHeroeId())
	    .ifPresent(s -> {
	        throw new RegistroDuplicadoException(superheroeDTO.getSuperheroeId());
	    });  	    
	
	    universo.setUniversoId(superheroeDTO.getUniverso().getUniversoId());
	    universo.setNombreUniverso(superheroeDTO.getUniverso().getNombreUniverso());
	
	    Optional<Universo> universoExistente = universoRepository.findById(universo.getUniversoId());
	
	    universoExistente.ifPresentOrElse(
	    	    u -> {
	    	    },
	    	    () -> {
	    	        Universo nuevoUniverso = new Universo();
	    	        nuevoUniverso.setUniversoId(universo.getUniversoId());
	    	        nuevoUniverso.setNombreUniverso(universo.getNombreUniverso());
	    	        universoRepository.save(nuevoUniverso);
	    	    }
	    	);	    
	    
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
	    boolean existenOtrosSuperheroes = superheroeRepository.existsByUniversoId(universo.getUniversoId());
	    if (!existenOtrosSuperheroes) {
	        universoRepository.delete(universo);
	    }
	    
	    superheroe.getPoderes().forEach(poder -> poder.getSuperheroes().remove(superheroe));
        
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
    
	private List<SuperheroeDTO> mapToDTOList(List<Superheroe> superheroes) {
	    return superheroes.stream().map(this::mapToDTO).collect(Collectors.toList());
	}

    public List<SuperheroeDTO> buscarPorPoder(Long id) {
        Poder poder = poderRepository.findById(id)
                .orElseThrow(() -> new NoHayPoderException(id));
        
        return superheroeRepository.findByPoderesIn(Collections.singletonList(poder))
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
	private SuperheroeDTO mapToDTO(Superheroe superheroe) {
	    SuperheroeDTO dto = new SuperheroeDTO();
	    dto.setSuperheroeId(superheroe.getHeroeId());
	    dto.setNombre(superheroe.getNombre());
	    dto.setGenero(superheroe.getGenero());
	    dto.setEstado(superheroe.getEstado());
	    dto.setPoderes(superheroe.getPoderes().stream().map(this::mapToPoderDTO).collect(Collectors.toList()));
	    dto.setUniverso(mapToUniversoDTO(superheroe.getUniverso()));
	    return dto;
	}

	private UniversoDTO mapToUniversoDTO(Universo universo) {
	    UniversoDTO dto = new UniversoDTO();
	    dto.setUniversoId(universo.getUniversoId());
	    dto.setNombreUniverso(universo.getNombreUniverso());
	    return dto;
	}

	private PoderDTO mapToPoderDTO(Poder poder) {
	    PoderDTO dto = new PoderDTO();
	    dto.setPoderId(poder.getPoderId());
	    dto.setTipoPoder(poder.getTipoPoder());
	    return dto;
	}
}
