package com.cibertec.lpii.controller;

import com.cibertec.lpii.model.*;
import com.cibertec.lpii.service.AlquilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/alquileres")
public class AlquilerController {

    @Autowired
    private AlquilerService alquilerService;

    // Mostrar formulario
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("alquiler", new Alquiler());
        model.addAttribute("clientes", alquilerService.obtenerClientes());
        model.addAttribute("peliculas", alquilerService.obtenerPeliculas());
        model.addAttribute("estados", EstadoAlquiler.values());
        return "form_alquiler";
    }

    // Procesar formulario
    @PostMapping("/procesar")
    public String procesarFormulario(
            @ModelAttribute("alquiler") @Valid Alquiler alquiler,
            @RequestParam("peliculasSeleccionadas") List<Integer> idsPeliculas,
            @RequestParam("cantidades") List<Integer> cantidades,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("clientes", alquilerService.obtenerClientes());
            model.addAttribute("peliculas", alquilerService.obtenerPeliculas());
            model.addAttribute("estados", EstadoAlquiler.values());
            model.addAttribute("error", "Por favor, revisa los campos.");
            return "form_alquiler";
        }

        Map<Pelicula, Integer> seleccion = new HashMap<>();
        List<Pelicula> todas = alquilerService.obtenerPeliculas();

        for (int i = 0; i < idsPeliculas.size(); i++) {
            Integer idPeli = idsPeliculas.get(i);
            Integer cantidad = cantidades.get(i);
            Pelicula peli = todas.stream().filter(p -> p.getIdPelicula().equals(idPeli)).findFirst().orElse(null);
            if (peli != null) {
                seleccion.put(peli, cantidad);
            }
        }

        BigDecimal total = seleccion.entrySet().stream()
                .map(e -> BigDecimal.valueOf(e.getValue()).multiply(BigDecimal.TEN))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        alquilerService.registrarAlquiler(alquiler.getCliente(), seleccion, total);

        model.addAttribute("mensaje", "✅ Alquiler registrado con éxito. Total: S/ " + total);
        return "form_alquiler";
    }
}
