package fr.dila.solonmgpp.core.dto;

import java.util.Arrays;

import fr.dila.solonmgpp.api.dto.PieceJointeFichierDTO;

public class PieceJointeFichierDTOImpl implements PieceJointeFichierDTO {

    private byte[] contenu;
    private String nomFichier;
    private String compression;
    private String mimeType;
    private String sha512;
    
    @Override
    public byte[] getContenu() {
        if(this.contenu != null){
            return (byte[])this.contenu.clone();    
        }
        return null;
    }

    @Override
    public void setContenu(byte[] contenu) {
        this.contenu = (contenu == null ? null :  Arrays.copyOf(contenu, contenu.length));
    }

    @Override
    public String getNomFichier() {
        return nomFichier;
    }

    @Override
    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    @Override
    public String getCompression() {
        return compression;
    }

    @Override
    public void setCompression(String compression) {
        this.compression = compression;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    @Override
    public String toString() {
        return String.format(
            "[PieceJointeFichierDTOImpl : contenu=%s, nomFichier=%s, compression=%s, mimeType=%s]",
            Arrays.toString(contenu), nomFichier, compression, mimeType
        );
    }

	@Override
	public String getSha512() {
		return sha512;
	}

	@Override
	public void setSha512(String sha512) {
		this.sha512 = sha512;
	}
}
