package fr.dila.solonepg.ui.bean;

public class CopyFileDTO {

    public CopyFileDTO(String fileId, String dossierId) {
        this.fileId = fileId;
        this.dossierId = dossierId;
    }

    private String fileId;

    private String dossierId;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getDossierId() {
        return dossierId;
    }

    public void setDossierId(String dossierId) {
        this.dossierId = dossierId;
    }
}