package com.cibertec.lpii.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibertec.lpii.model.DetalleAlquiler;
import com.cibertec.lpii.model.DetalleAlquilerId;

@Repository
public interface DetalleAlquilerRepository extends JpaRepository<DetalleAlquiler, DetalleAlquilerId> {}
