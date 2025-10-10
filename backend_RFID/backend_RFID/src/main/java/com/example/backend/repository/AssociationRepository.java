package com.example.backend.repository;

import com.example.backend.entity.Association;
import com.example.backend.entity.Barque;
import com.example.backend.entity.TagRFID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssociationRepository extends JpaRepository<Association, Long> {
    // Tu pourras ajouter des méthodes si besoin, ex : findByTag, findByBarque, etc.
    Optional<Association> findByTagAndBarque(TagRFID tag, Barque barque); // Ajouter cette méthode

}
