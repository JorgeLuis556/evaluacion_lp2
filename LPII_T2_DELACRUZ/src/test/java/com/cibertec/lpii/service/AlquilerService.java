package com.cibertec.lpii.service;

import com.cibertec.lpii.model.*;
import com.cibertec.lpii.repository.AlquilerRepository;
import com.cibertec.lpii.repository.ClienteRepository;
import com.cibertec.lpii.repository.DetalleAlquilerRepository;
import com.cibertec.lpii.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class AlquilerService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Autowired
    private AlquilerRepository alquilerRepository;

    @Autowired
    private DetalleAlquilerRepository detalleAlquilerRepository;

    public List<Cliente> obtenerClientes() {
        return clienteRepository.findAll();
    }

    public List<Pelicula> obtenerPeliculas() {
        return peliculaRepository.findAll();
    }

    @Transactional
    public void registrarAlquiler(Cliente cliente, Map<Pelicula, Integer> seleccion, BigDecimal total) {
        Alquiler alquiler = new Alquiler();
        alquiler.setCliente(cliente);
        alquiler.setFecha(LocalDate.now());
        alquiler.setTotal(total);
        alquiler.setEstado(EstadoAlquiler.ACTIVO);

        alquiler = alquilerRepository.save(alquiler);

        for (Map.Entry<Pelicula, Integer> entry : seleccion.entrySet()) {
            Pelicula pelicula = peliculaRepository.findById(entry.getKey().getIdPelicula())
                    .orElseThrow(() -> new RuntimeException("Película no encontrada"));
            Integer cantidad = entry.getValue();

            DetalleAlquilerId detalleId = new DetalleAlquilerId(alquiler.getIdAlquiler(), pelicula.getIdPelicula());

            DetalleAlquiler detalle = new DetalleAlquiler();
            detalle.setId(detalleId);
            detalle.setAlquiler(alquiler);
            detalle.setPelicula(pelicula);
            detalle.setCantidad(cantidad);

            detalleAlquilerRepository.save(detalle);

            // actualizamos stock
            pelicula.setStock(pelicula.getStock() - cantidad);
            peliculaRepository.save(pelicula);
        }
    }
}
