package com.example.peliculas.dao;

import org.springframework.data.repository.CrudRepository;

import com.example.peliculas.entities.Pelicula;

public interface IPeliculaRepository extends CrudRepository<Pelicula, Long >{ 
   
}