package com.example.backend.repository;
import java.util.Optional;

import com.example.backend.entity.TagRFID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TagRFIDRepository extends JpaRepository<TagRFID, Long> {
    Optional<TagRFID> findByCodeEpc(String codeEpc); // Pas de 'static' ici !

}


