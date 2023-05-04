package com.crud.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crud.entity.Superheroe;

public interface SuperheroeRepository  extends JpaRepository<Superheroe, Long>{

}
