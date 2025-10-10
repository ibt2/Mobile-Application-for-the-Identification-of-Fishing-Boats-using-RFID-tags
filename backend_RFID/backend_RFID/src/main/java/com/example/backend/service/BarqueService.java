package com.example.backend.service;

import com.example.backend.entity.Barque;
import com.example.backend.repository.BarqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BarqueService {

    @Autowired
    private BarqueRepository barqueRepository;

    // Méthode pour sauver une barque (appelée par le contrôleur POST)
    @Transactional
    public Barque saveBarque(Barque barque) {
        return barqueRepository.save(barque);
    }

    // Méthode pour récupérer toutes les barques (appelée par le contrôleur GET)
    public List<Barque> getAllBarques() {
        return barqueRepository.findAll();
    }


}