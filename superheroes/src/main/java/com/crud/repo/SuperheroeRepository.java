package com.crud.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crud.entity.Poder;
import com.crud.entity.Superheroe;
import com.crud.entity.Universo;


public interface SuperheroeRepository extends JpaRepository<Superheroe, Long> {
    List<Superheroe> findByGenero(String genero);
    List<Superheroe> findByUniversoNombreUniversoContains(String nombre);
    List<Superheroe> findByUniversoIn(List<Universo> list);
    List<Superheroe> findByPoderesIn(List<Poder> poderes);
    boolean existsByUniversoId(Long universoId);
}
