package com.example.backend.controller;

import com.example.backend.dto.AssociationUploadDTO;
import com.example.backend.entity.Association;
import com.example.backend.entity.Barque;
import com.example.backend.entity.TagRFID;
import com.example.backend.entity.User;
import com.example.backend.repository.AssociationRepository;
import com.example.backend.repository.BarqueRepository;
import com.example.backend.repository.TagRFIDRepository;
import com.example.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/sync")
public class SyncController {

    private static final Logger logger = LoggerFactory.getLogger(SyncController.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Autowired private UserRepository userRepository;
    @Autowired private BarqueRepository barqueRepository;
    @Autowired private TagRFIDRepository tagRepository;
    @Autowired private AssociationRepository associationRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        logger.info("🔄 [SYNC USERS] Total: {}", users.size());
        for (User user : users) {
            logger.debug(" - User: ID={}, Username={}", user.getId(), user.getUsername());
        }
        return users;
    }

    @GetMapping("/barques")
    public List<Barque> getAllBarques() {
        List<Barque> barques = barqueRepository.findAll();
        logger.info("🛶 [SYNC BARQUES] Total: {}", barques.size());
        for (Barque b : barques) {
            logger.debug(" - Barque: ID={}, Matricule={}, Port={}", b.getId(), b.getMatricule(), b.getPort());
        }
        return barques;
    }

    @GetMapping("/tags")
    public List<TagRFID> getAllTags() {
        List<TagRFID> tags = tagRepository.findAll();
        logger.info("🔖 [SYNC TAGS] Total: {}", tags.size());
        for (TagRFID tag : tags) {
            logger.debug(" - Tag: ID={}, EPC={}", tag.getId(), tag.getCodeEpc());
        }
        return tags;
    }

    @GetMapping("/associations")
    public List<Association> getAllAssociations() {
        List<Association> associations = associationRepository.findAll();
        logger.info("🔗 [SYNC ASSOCIATIONS] Total: {}", associations.size());

        for (Association assoc : associations) {
            logger.debug(
                    " - Association: ID={}, BarqueID={}, TagID={}, Status={}, AssociatedAt={}, UpdatedAt={}",
                    assoc.getId(),
                    assoc.getBarque().getId(),
                    assoc.getTag().getId(),
                    assoc.getStatus(),
                    assoc.getAssociatedAt(),
                    assoc.getUpdatedAt()
            );
        }

        return associations;
    }

    @PostMapping("/upload/tags")
    @Transactional
    public ResponseEntity<List<TagRFID>> uploadTags(@RequestBody List<TagRFID> tagsToUpload) {
        logger.info("⬆️ [UPLOAD TAGS] Réception de {} tags du frontend.", tagsToUpload.size());
        List<TagRFID> savedTags = new ArrayList<>();
        for (TagRFID tag : tagsToUpload) {
            // Vérifier si un tag avec le même code_epc existe déjà
            Optional<TagRFID> existingTag = tagRepository.findByCodeEpc(tag.getCodeEpc());
            if (existingTag.isPresent()) {
                TagRFID updatedTag = existingTag.get();
                // Mise à jour si nécessaire (par exemple, si updatedAt du frontend est plus récent)
                // Pour cet exemple, nous allons simplement logguer, mais en prod, implémentez une logique de fusion
                logger.warn("Tag avec code EPC {} existe déjà. Mise à jour de l'ID backend: {}", tag.getCodeEpc(), updatedTag.getId());
                // Mettre à jour les champs si la date_ajout ou updated_at est plus récente
                if (tag.getUpdatedAt() != null && updatedTag.getUpdatedAt() != null &&
                        tag.getUpdatedAt().isAfter(updatedTag.getUpdatedAt())) {
                    updatedTag.setDateAjout(tag.getDateAjout());
                    updatedTag.setUpdatedAt(tag.getUpdatedAt());
                    savedTags.add(tagRepository.save(updatedTag));
                    logger.debug("Tag {} mis à jour", updatedTag.getCodeEpc());
                } else {
                    savedTags.add(updatedTag); // Retourner le tag existant pour que le frontend puisse mettre à jour son backend_id
                    logger.debug("Tag {} déjà à jour", updatedTag.getCodeEpc());
                }
            } else {
                tag.setId(null); // Laisser la base de données générer un nouvel ID pour le backend
                savedTags.add(tagRepository.save(tag));
                logger.debug("Nouveau tag {} enregistré avec ID: {}", tag.getCodeEpc(), tag.getId());
            }
        }
        logger.info("✅ [UPLOAD TAGS] {} tags traités et sauvegardés/mis à jour.", savedTags.size());
        return ResponseEntity.ok(savedTags);
    }

    @PostMapping("/upload/barques")
    @Transactional
    public ResponseEntity<List<Barque>> uploadBarques(@RequestBody List<Barque> barquesToUpload) {
        logger.info("⬆️ [UPLOAD BARQUES] Réception de {} barques du frontend.", barquesToUpload.size());
        List<Barque> savedBarques = new ArrayList<>();
        for (Barque barque : barquesToUpload) {
            // Vérifier si une barque avec le même matricule existe déjà
            Optional<Barque> existingBarque = barqueRepository.findByMatricule(barque.getMatricule());
            if (existingBarque.isPresent()) {
                Barque updatedBarque = existingBarque.get();
                // Mettre à jour les champs si la date_ajout ou updated_at est plus récente
                if (barque.getUpdatedAt() != null && updatedBarque.getUpdatedAt() != null &&
                        barque.getUpdatedAt().isAfter(updatedBarque.getUpdatedAt())) {
                    updatedBarque.setNomPecheur(barque.getNomPecheur());
                    updatedBarque.setPort(barque.getPort());
                    updatedBarque.setDateAjout(barque.getDateAjout());
                    updatedBarque.setUpdatedAt(barque.getUpdatedAt());
                    savedBarques.add(barqueRepository.save(updatedBarque));
                    logger.debug("Barque {} mise à jour", updatedBarque.getMatricule());
                } else {
                    savedBarques.add(updatedBarque); // Retourner la barque existante
                    logger.debug("Barque {} déjà à jour", updatedBarque.getMatricule());
                }
            } else {
                barque.setId(null); // Laisser la base de données générer un nouvel ID pour le backend
                savedBarques.add(barqueRepository.save(barque));
                logger.debug("Nouvelle barque {} enregistrée avec ID: {}", barque.getMatricule(), barque.getId());
            }
        }
        logger.info("✅ [UPLOAD BARQUES] {} barques traitées et sauvegardées/mis à jour.", savedBarques.size());
        return ResponseEntity.ok(savedBarques);
    }

    @PostMapping("/upload/associations")
    @Transactional
    public ResponseEntity<List<Association>> uploadAssociations(@RequestBody List<AssociationUploadDTO> associationsToUploadDTO) {
        logger.info("⬆️ [UPLOAD ASSOCIATIONS] Réception de {} associations du frontend.", associationsToUploadDTO.size());
        List<Association> savedAssociations = new ArrayList<>();

        for (AssociationUploadDTO dto : associationsToUploadDTO) {
            Optional<TagRFID> tag = tagRepository.findById(dto.getTagId());
            Optional<Barque> barque = barqueRepository.findById(dto.getBarqueId());

            if (tag.isEmpty() || barque.isEmpty()) {
                logger.warn("Skipping association: Tag with ID {} or Barque with ID {} not found in backend.", dto.getTagId(), dto.getBarqueId());
                continue; // Skip if referenced tag or barque not found
            }

            // Chercher une association existante par tag_id et barque_id
            Optional<Association> existingAssoc = associationRepository.findByTagAndBarque(tag.get(), barque.get());

            Association association = existingAssoc.orElseGet(Association::new); // Utilise l'existante ou crée une nouvelle

            association.setTag(tag.get());
            association.setBarque(barque.get());
            association.setStatus(dto.getStatus());
            association.setAssociatedAt(dto.getAssociatedAt());
            association.setUpdatedAt(dto.getUpdatedAt());
            // L'ID est géré par la base de données du backend via @GeneratedValue
            // Si c'est une nouvelle association, l'ID sera généré. Si c'est une mise à jour d'une existante, l'ID sera conservé.

            savedAssociations.add(associationRepository.save(association));
            logger.debug("Association traitée (Tag: {}, Barque: {})", dto.getTagId(), dto.getBarqueId());
        }
        logger.info("✅ [UPLOAD ASSOCIATIONS] {} associations traitées et sauvegardées/mis à jour.", savedAssociations.size());
        return ResponseEntity.ok(savedAssociations);
    }
}