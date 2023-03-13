package com.example.peliculas.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.peliculas.dao.IGeneroRepository;
import com.example.peliculas.entities.Genero;
import com.example.peliculas.services.GeneroService;
import com.example.peliculas.services.IGeneroService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class GeneroController {
  
    private IGeneroService  service;
    
    public GeneroController(IGeneroService service){
        this.service = service;
    }

    @PostMapping("/genero")
    public Long guardar(@RequestParam String nombre) {
        Genero genero = new Genero();
        genero.setNombre(nombre); 
        service.save(genero);
        return genero.getId();
    }

    @GetMapping("/genero/{id}")
    public String buscarPorId(@PathVariable(name = "id") Long id) { 
        return service.findById(id).getNombre();

    }


    
}
