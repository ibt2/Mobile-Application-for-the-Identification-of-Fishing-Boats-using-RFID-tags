package com.example.backend.controller;

import com.example.backend.entity.Barque;
import com.example.backend.service.BarqueService; // Assurez-vous d'importer votre BarqueService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barques")
@CrossOrigin(origins = "*") // Permet les requêtes depuis n'importe quelle origine (pour le développement)
public class BarqueController {

    @Autowired
    private BarqueService barqueService; // Injecte le service qui gère la logique métier

    // Endpoint pour AJOUTER une nouvelle barque
    // Les champs dateAjout et updatedAt seront définis automatiquement par l'entité (@PrePersist)
    @PostMapping
    public ResponseEntity<Barque> createBarque(@RequestBody Barque barque) {
        Barque savedBarque = barqueService.saveBarque(barque);
        return new ResponseEntity<>(savedBarque, HttpStatus.CREATED);
    }

    // Endpoint pour RÉCUPÉRER toutes les barques
    @GetMapping
    public List<Barque> getAllBarques() {
        return barqueService.getAllBarques();
    }

    // Si vous aviez d'autres endpoints comme par matricule, vous pouvez les laisser
    // @GetMapping("/matricule/{matricule}")
    // public ResponseEntity<Barque> getBarqueByMatricule(@PathVariable String matricule) {
    //     return barqueService.getBarqueByMatricule(matricule)
    //             .map(barque -> new ResponseEntity<>(barque, HttpStatus.OK))
    //             .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    // }
}