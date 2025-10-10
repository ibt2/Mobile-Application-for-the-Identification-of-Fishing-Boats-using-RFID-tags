package com.example.backend.dto;

import java.time.LocalDateTime;

public class AssociationUploadDTO {
    private Long id; // Peut être l'ID local, le backend peut ignorer ou utiliser pour un upsert
    private Long tagId;
    private Long barqueId;
    private String status;
    private String associatedAt;
    private String updatedAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getBarqueId() {
        return barqueId;
    }

    public void setBarqueId(Long barqueId) {
        this.barqueId = barqueId;
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

    public void voidsetAssociatedAt(String associatedAt) {
        this.associatedAt = associatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}