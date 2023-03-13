package com.example.peliculas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.peliculas.dao.IActorRepository;
import com.example.peliculas.entities.Actor;

@Service
public class ActorService implements IActorService {

    @Autowired
    private IActorRepository repo;
 
    public List<Actor> findAll() {
        return (List<Actor>) repo.findAll();
        
    }
 
    public List<Actor> findAllById(List<Long> ids) {
        return (List<Actor>) repo.findAllById(ids);
    }
    
}
