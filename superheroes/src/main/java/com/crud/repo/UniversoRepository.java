package com.crud.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crud.entity.Universo;

public interface UniversoRepository extends JpaRepository<Universo, Long>{
    List<Universo> findByNombreUniversoContains(String nombre);
}
