package com.example.backend.dto;

public class TagRFIDDTO {
    private Long id;
    private String codeEpc;
    private String dateAjout;
    private String updatedAt;
    private boolean dissocie;
    private boolean isDissociated;
    private boolean synced;
    private Long backendId;

    // 💡 L’ID de la barque associée (important pour le frontend)
    private Long barqueId;

    // Getters & Setters
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

    public String getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(String dateAjout) {
        this.dateAjout = dateAjout;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDissocie() {
        return dissocie;
    }

    public void setDissocie(boolean dissocie) {
        this.dissocie = dissocie;
    }

    public boolean isDissociated() {
        return isDissociated;
    }

    public void setDissociated(boolean isDissociated) {
        this.isDissociated = isDissociated;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public Long getBackendId() {
        return backendId;
    }

    public void setBackendId(Long backendId) {
        this.backendId = backendId;
    }

    public Long getBarqueId() {
        return barqueId;
    }

    public void setBarqueId(Long barqueId) {
        this.barqueId = barqueId;
    }
}
