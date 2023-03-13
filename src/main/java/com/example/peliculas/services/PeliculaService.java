package com.example.peliculas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.peliculas.dao.IPeliculaRepository;
import com.example.peliculas.entities.Pelicula;

@Service
public class PeliculaService implements IPeliculaService {

    @Autowired
    private IPeliculaRepository peliculaRepository;

    @Override
    public void save(Pelicula pelicula) {
        peliculaRepository.save(pelicula);
    }

    @Override
    public Pelicula findById(Long id) {
         
        return peliculaRepository.findById(id).orElse(null);
        //or further processing
    }

    @Override
    public List<Pelicula> findAll() {
        return (List<Pelicula>) peliculaRepository.findAll();
    }


    
    @Override
    public void delete(Long id) {
        peliculaRepository.deleteById(id);
    }
    //consumed by controller, comunicates with repository
 
     
    @Override
    public Page<Pelicula> findAll(org.springframework.data.domain.Pageable pageable) {
        return peliculaRepository.findAll(pageable);
    }
    
}
