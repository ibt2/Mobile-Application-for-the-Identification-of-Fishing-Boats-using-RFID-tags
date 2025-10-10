package com.example.backend.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "tag_rfid")
public class TagRFID {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codeEpc;

    @Column(nullable = false)
    private Boolean synced = false;

    // Stockage en millisecondes dans SQLite
    @Column(name = "date_ajout", nullable = false, updatable = false)
    private Long dateAjoutMs;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAtMs;

    @PrePersist
    protected void onCreate() {
        long now = System.currentTimeMillis();
        dateAjoutMs = now;
        updatedAtMs = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAtMs = System.currentTimeMillis();
    }

    // ---------- Getters / Setters ----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeEpc() {
        return codeEpc;
    }

    public void setCodeEpc(String codeEpc) {
        this.codeEpc = codeEpc;
    }

    public Boolean getSynced() {
        return synced;
    }

    public void setSynced(Boolean synced) {
        this.synced = synced;
    }

    // Conversion Long -> LocalDateTime
    public LocalDateTime getDateAjout() {
        return Instant.ofEpochMilli(dateAjoutMs)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    // Conversion LocalDateTime -> Long
    public void setDateAjout(LocalDateTime dateAjout) {
        this.dateAjoutMs = dateAjout.atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    public LocalDateTime getUpdatedAt() {
        return Instant.ofEpochMilli(updatedAtMs)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAtMs = updatedAt.atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }
}
