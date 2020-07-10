package fr.dila.solonmgpp.core.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeDTO;
import fr.dila.st.core.client.AbstractMapDTO;

public class EvenementDTOImpl extends AbstractMapDTO implements EvenementDTO, Serializable {

	private static final long	serialVersionUID	= 1156726888970180429L;

	@Override
	public String getType() {
		return EvenementDTO.class.getSimpleName();
	}

	@Override
	public String getDocIdForSelection() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getTypeEvenementName() {
		if (get(TYPE_EVENEMENT) != null) {
			return (String) ((Map<String, Serializable>) get(TYPE_EVENEMENT)).get(NAME);
		} else {
			return "TYPE_VIDE";
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getTypeEvenementLabel() {
		if (get(TYPE_EVENEMENT) != null) {
			return (String) ((Map<String, Serializable>) get(TYPE_EVENEMENT)).get(LABEL);
		} else {
			return "Type de communication inconnue";
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PieceJointeDTO> getListPieceJointe(String typePieceJointe) {
		if ("AUTRE".equals(typePieceJointe)) {
			typePieceJointe = "AUTRES";
		}
		return (List<PieceJointeDTO>) get(typePieceJointe);
	}

	@Override
	public String getIdDossier() {
		return getString(ID_DOSSIER);
	}

	@Override
	public String getIdEvenement() {
		return getString(ID_EVENEMENT);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getVersionCouranteMajeur() {
		return (Integer) ((Map<String, Serializable>) get(VERSION_COURANTE)).get(MAJEUR);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getVersionCouranteMineur() {
		return (Integer) ((Map<String, Serializable>) get(VERSION_COURANTE)).get(MINEUR);
	}

	@Override
	public void setIdDossier(String idDossier) {
		put(ID_DOSSIER, idDossier);
	}

	@Override
	public String getNor() {
		return getString(NOR);
	}

	@Override
	public void setNor(String nor) {
		put(NOR, nor);
	}

	@Override
	public String getEmetteur() {
		return getString(EMETTEUR);
	}

	@Override
	public String getDestinataire() {
		return getString(DESTINATAIRE);
	}

	@Override
	public void setDestinataire(String destinataire) {
		put(DESTINATAIRE, destinataire);

	}

	@Override
	public void setCopie(List<String> copie) {
		putListString(COPIE, copie);
	}

	@Override
	public List<String> getCopie() {
		return getListString(COPIE);
	}

	@Override
	public List<String> getActionSuivante() {
		return getListString(ACTION_SUIVANTE);
	}

	@Override
	public String getIdEvenementPrecedent() {
		return getString(ID_EVENEMENT_PRECEDENT);
	}

	@Override
	public String getObjet() {
		return getString(OBJET);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getVersionCouranteModeCreation() {
		return (String) ((Map<String, Serializable>) get(VERSION_COURANTE)).get(MODE_CREATION);
	}

	@Override
	public String getAuteur() {
		return getString(AUTEUR);
	}

	@Override
	public String getCommentaire() {
		return getString(COMMENTAIRE);
	}

	@Override
	public List<String> getMetasModifiees() {
		return getListString(METADONNEE_MODIFIEE);
	}

	@Override
	public List<String> getEvenementsSuivants() {
		return getListString(EVENEMENT_SUIVANT);
	}

	@Override
	public List<String> getNature() {
		return getListString(NATURE);
	}

	@Override
	public String getOEP() {
		return getString(OEP);
	}

	@Override
	public void setOEP(String newOEP) {
		put(OEP, newOEP);
	}

}
