package com.example.peliculas.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.peliculas.entities.Actor;
import com.example.peliculas.entities.Pelicula;
import com.example.peliculas.services.IActorService;
import com.example.peliculas.services.IArchivoService;
import com.example.peliculas.services.IGeneroService;
import com.example.peliculas.services.IPeliculaService;

import jakarta.validation.Valid;

@Controller
public class PeliculaController {

    private IPeliculaService service;
    private IGeneroService generoService;
    private IActorService actorService;
    private IArchivoService archivoService;

    public PeliculaController(IPeliculaService service, IGeneroService generoService, IActorService actorService,
            IArchivoService archivoService) {
        this.service = service;
        this.generoService = generoService;
        this.actorService = actorService;
        this.archivoService = archivoService;
    }

    @GetMapping("/peliculas")
    public String crear(Model model) {
        Pelicula pelicula = new Pelicula();

        model.addAttribute("actores", actorService.findAll());
        model.addAttribute("generos", generoService.findAll()); // gets all generos
        model.addAttribute("pelicula", pelicula);

        model.addAttribute("titulo", "Nueva pelicula");
        return "peliculas";
    }

    @GetMapping("/pelicula/{id}")
    public String editar(@PathVariable(name = "id") String id, Model model) {
        Pelicula pelicula = new Pelicula();

        model.addAttribute("pelicula", pelicula);
        model.addAttribute("titulo", "Editar pelicula");
        return "peliculas";
    }

    @PostMapping("/peliculas")
    public String guardar(@Valid Pelicula pelicula, BindingResult br, @ModelAttribute(name = "ids") String ids,
            Model model, @RequestParam("archivo") MultipartFile imagen) {


        if (br.hasErrors()) {
            model.addAttribute("actores", actorService.findAll());
            model.addAttribute("generos", generoService.findAll()); // gets all generos

            return "peliculas";
        }

        if (!imagen.isEmpty()){
            String archivo = pelicula.getNombre() +  getExtension(imagen.getOriginalFilename());
            pelicula.setImagen(archivo);
            try {
                archivoService.guardar(archivo,imagen.getInputStream());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
            
        }else{
            pelicula.setImagen("default.jpg");
        }
        // shouldnt be in controller, id reprocessing should be in service
        List<Long> idsProtagonistas = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        List<Actor> protagonistas = actorService.findAllById(idsProtagonistas);
        pelicula.setProtagonistas(protagonistas);
        service.save(pelicula);
        return "redirect:/home";
    }

    private String getExtension(String archivo) {
        return archivo.substring(archivo.lastIndexOf("."));
    }

    @GetMapping({ "/", "/home", "/index" })
    public String home(Model model) {
        model.addAttribute("peliculas", service.findAll());
        model.addAttribute("msj", "Catalogo actualizado 2023");
        model.addAttribute("tipoMsj", "success");
        return "home";
    }
    /*
     * @GetMapping("/peliculas")
     * public List <Pelicula> getPeliculas(){
     * return peliculaService.findAll();
     * }
     */
}
