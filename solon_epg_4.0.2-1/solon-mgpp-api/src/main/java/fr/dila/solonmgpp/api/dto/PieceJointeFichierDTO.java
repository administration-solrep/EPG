package fr.dila.solonmgpp.api.dto;

import fr.sword.xsd.solon.epp.CompressionFichier;
import java.io.Serializable;

/**
 * DTO de representation d'un fichier piece jointe EPP
 *
 * @author asatre
 *
 */
public interface PieceJointeFichierDTO extends Serializable {
    public static final String CONTENU = "contenu";
    public static final String NOM_FICHIER = "nomFichier";
    public static final String COMPRESSION = "compression";
    public static final String MIME_TYPE = "mimeType";
    public static final String SHA512 = "sha512";

    byte[] getContenu();

    void setContenu(byte[] contenu);

    String getNomFichier();

    void setNomFichier(String nomFichier);

    CompressionFichier getCompression();

    void setCompression(CompressionFichier compression);

    String getMimeType();

    void setMimeType(String mimeType);

    String getSha512();

    void setSha512(String sha512);
}