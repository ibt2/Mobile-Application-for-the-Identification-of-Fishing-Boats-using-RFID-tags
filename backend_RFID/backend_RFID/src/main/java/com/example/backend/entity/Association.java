package com.example.backend.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "associations")
public class Association {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private TagRFID tag;

    @ManyToOne
    @JoinColumn(name = "barque_id", nullable = false)
    private Barque barque;

    @Column(nullable = false)
    private String status; // "ASSOCIE" ou "DISSOCIE"

    @Column(name = "associated_at", nullable = false)
    private String associatedAt; // Garder String pour l'instant si c'est ce qui est utilisé, mais LocalDateTime est préférable

    @Column(name = "updated_at", nullable = false)
    // Pas de @DateTimeFormat si c'est un String
    private String updatedAt; // Garder String pour l'instant

    @PrePersist
    protected void onCreate() {
        String now = LocalDateTime.now().toString();
        this.associatedAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now().toString();
    }

    // ✅ Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TagRFID getTag() {
        return tag;
    }

    public void setTag(TagRFID tag) {
        this.tag = tag;
    }

    public Barque getBarque() {
        return barque;
    }

    public void setBarque(Barque barque) {
        this.barque = barque;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssociatedAt() {
        return associatedAt;
    }

    public void setAssociatedAt(String associatedAt) {
        this.associatedAt = associatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}