package com.example.peliculas.dao;

import org.springframework.data.repository.CrudRepository;

import com.example.peliculas.entities.Actor;

public interface IActorRepository extends CrudRepository <Actor,Long> {
    
}
