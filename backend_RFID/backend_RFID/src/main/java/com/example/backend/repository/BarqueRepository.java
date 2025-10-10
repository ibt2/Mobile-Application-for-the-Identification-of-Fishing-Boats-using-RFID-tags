package com.example.backend.repository;

import com.example.backend.entity.Barque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BarqueRepository extends JpaRepository<Barque, Long> {
    Optional<Barque> findByMatricule(String matricule); // Ajouter cette méthode

}

