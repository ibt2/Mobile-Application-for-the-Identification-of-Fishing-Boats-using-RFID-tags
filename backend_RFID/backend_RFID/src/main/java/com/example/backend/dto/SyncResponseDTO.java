package com.example.backend.dto;

public class SyncResponseDTO {
    private Long localId;     // ID local du frontend
    private Long backendId;   // ID généré côté backend
    private String status;    // "SYNCED" ou "ERROR"
    private String message;   // Message d'erreur (optionnel)

    // Getters & Setters
    public Long getLocalId() { return localId; }
    public void setLocalId(Long localId) { this.localId = localId; }

    public Long getBackendId() { return backendId; }
    public void setBackendId(Long backendId) { this.backendId = backendId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
