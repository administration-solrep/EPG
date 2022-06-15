package fr.dila.solonmgpp.core.dto;

import fr.dila.solonmgpp.api.dto.MgppPieceJointeDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeFichierDTO;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * DTO pour les pieces jointes
 * @author asatre
 *
 */
public class MgppPieceJointeDTOImpl implements MgppPieceJointeDTO {
    private static final long serialVersionUID = 2156213617937821214L;

    private String idInterneEpp;
    private String libelle;
    private String type;
    private String url;

    private List<PieceJointeFichierDTO> fichier;

    public MgppPieceJointeDTOImpl() {
        this.fichier = new ArrayList<>();
    }

    @Override
    public String getIdInterneEpp() {
        return idInterneEpp;
    }

    @Override
    public void setIdInterneEpp(String idInterneEpp) {
        this.idInterneEpp = idInterneEpp;
    }

    @Override
    public String getLibelle() {
        return libelle;
    }

    @Override
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String getType() {
        if (StringUtils.isNotBlank(type) && "AUTRES".equals(type)) {
            return "AUTRE";
        }
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public List<PieceJointeFichierDTO> getFichier() {
        return fichier;
    }

    @Override
    public void setFichier(List<PieceJointeFichierDTO> fichier) {
        this.fichier = fichier;
    }

    @Override
    public String toString() {
        return (
            "[PieceJointeDTOImpl : idInterneEpp=" +
            idInterneEpp +
            ", libelle=" +
            libelle +
            ", type=" +
            type +
            ", url=" +
            url +
            ", fichiers=" +
            fichier +
            "]"
        );
    }
}