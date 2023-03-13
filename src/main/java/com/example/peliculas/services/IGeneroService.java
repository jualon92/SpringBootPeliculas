package com.example.peliculas.services;

import java.util.List;

import com.example.peliculas.entities.Genero;

public interface IGeneroService  {
    public void save(Genero genero);
    public Genero findById(Long id);
    public void delete(Long id);
    public List<Genero> findAll();
}
