package com.example.peliculas.dao;

import org.springframework.data.repository.CrudRepository;

import com.example.peliculas.entities.Genero;

public interface IGeneroRepository extends CrudRepository<Genero, Long >{ 
   
}
