package fr.dila.solonmgpp.core.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.dila.solonmgpp.api.dto.PieceJointeDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeFichierDTO;

/**
 * DTO pour les pieces jointes
 * @author asatre
 *
 */
public class PieceJointeDTOImpl implements PieceJointeDTO {

	private static final long serialVersionUID = 2156213617937821214L;
	
	private String idInterneEpp;
	private String libelle;
	private String type;
	private String url;
	
	private List<PieceJointeFichierDTO> fichier;
	
	
	public PieceJointeDTOImpl(){
		this.fichier = new ArrayList<PieceJointeFichierDTO>();
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
        if(StringUtils.isNotBlank(type) && "AUTRES".equals(type)){
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
        return "[PieceJointeDTOImpl : idInterneEpp="+idInterneEpp +", libelle="+libelle + ", type="+type +", url="+url+", fichiers="+fichier +"]";
    }
	
}
