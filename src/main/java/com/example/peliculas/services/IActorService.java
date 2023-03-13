package com.example.peliculas.services;

import java.util.List;

import com.example.peliculas.entities.Actor;

public interface IActorService {
    public List<Actor> findAll();
    public List<Actor> findAllById(List<Long> id); //dado una lista de id devuelve una lista de actores
    
}
