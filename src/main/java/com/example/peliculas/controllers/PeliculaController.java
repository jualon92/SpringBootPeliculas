package com.example.peliculas.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/peliculas/{id}")
    public String editar(@PathVariable(name = "id") Long id, Model model) {
        Pelicula pelicula = service.findById(id);
        String ids = "";

        for (Actor actor : pelicula.getProtagonistas()){
            if ("".equals(ids)){
                ids = actor.getId().toString();
            }else{
                ids = actor.getId().toString();
            } 
        }
        model.addAttribute("pelicula", pelicula);
        model.addAttribute("ids", ids);
        model.addAttribute("actores", actorService.findAll());
        model.addAttribute("generos", generoService.findAll());
        model.addAttribute("titulo", "Editar pelicula");
        return "peliculas";
    }


    @GetMapping("/peliculas/{id}/delete")
    public String eliminar(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAtt) {
        service.delete(id);

        redirectAtt.addAttribute("msj", "la pelicula fue eliminado correctamente");
        
        redirectAtt.addAttribute("tipoMsj", "success");
        return "redirect:/listado";
    }
    

    @PostMapping("/peliculas")
    public String guardar(@Valid Pelicula pelicula, BindingResult br, @ModelAttribute(name = "ids") String ids,
            Model model, @RequestParam("archivo") MultipartFile imagen) {

        if (br.hasErrors()) {
            model.addAttribute("actores", actorService.findAll());
            model.addAttribute("generos", generoService.findAll()); // gets all generos

            return "peliculas";
        }

        if (!imagen.isEmpty()) {
            String archivo = pelicula.getNombre() + getExtension(imagen.getOriginalFilename());
            pelicula.setImagen(archivo);
            try {
                archivoService.guardar(archivo, imagen.getInputStream());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {
            pelicula.setImagen("default.jpg");
        }
        if (ids != null && !"".equals(ids)) { 
            List<Long> idsProtagonistas = Arrays.stream(ids.split(",")).map(Long::parseLong)
                    .collect(Collectors.toList());
            List<Actor> protagonistas = actorService.findAllById(idsProtagonistas);
            pelicula.setProtagonistas(protagonistas);
        }

        service.save(pelicula);
        return "redirect:/home";
    }

    private String getExtension(String archivo) {
        return archivo.substring(archivo.lastIndexOf("."));
    }

    @GetMapping({ "/", "/home", "/index" })
    public String home(Model model, @RequestParam(name="pagina", required = false, defaultValue = "0") Integer pagina) {
        PageRequest pr = PageRequest.of(pagina, 1); //get page size
        Page<Pelicula> page =  service.findAll(pr);
        
        if (page.getTotalPages() > 0 ){
            List<Integer> paginas = IntStream.rangeClosed(1, page.getTotalPages()).boxed().toList();
            model.addAttribute("paginas", paginas);
        }

        model.addAttribute("actual", pagina + 1);
        model.addAttribute("titulo", "catalogo de peliculas");
        model.addAttribute("peliculas", page.getContent());
        model.addAttribute("msj", "Catalogo actualizado 2023");
        model.addAttribute("tipoMsj", "success");
        return "home";
    }

    @GetMapping("/listado")
    public String listado(Model model, @RequestParam(required = false) String msj,  @RequestParam(required = false) String tipoMsj) {
        
        if(!"".equals(tipoMsj) && !"".equals(msj)){
            model.addAttribute("msj", msj);
            model.addAttribute("tipoMsj", tipoMsj);
        }
        model.addAttribute("titulo", "Listado de peliculas");
        model.addAttribute("peliculas", service.findAll());
        return "listado";
    }

}
