package com.cibertec.lpii.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibertec.lpii.model.Alquiler;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {}