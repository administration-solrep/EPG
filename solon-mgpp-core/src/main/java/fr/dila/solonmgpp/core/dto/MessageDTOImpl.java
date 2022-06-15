package fr.dila.solonmgpp.core.dto;

import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.core.client.AbstractMapDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.NuxeoException;

public class MessageDTOImpl extends AbstractMapDTO implements MessageDTO, Serializable {
    private static final String DEFAULT_VALUE = "inconnu";

    private static final long serialVersionUID = -820003881885393867L;

    private static final String SEPARATOR = " - ";

    public MessageDTOImpl() {
        setTypeEvenement(DEFAULT_VALUE);
        setIdDossier(DEFAULT_VALUE);
        setObjet(DEFAULT_VALUE);
        setCopieEvenement(new ArrayList<String>());
        setEnAttente(Boolean.FALSE);
    }

    @Override
    public String getEtatMessage() {
        return getString(ETAT_MESSAGE);
    }

    @Override
    public void setEtatMessage(String etatMessage) {
        put(ETAT_MESSAGE, etatMessage);
    }

    @Override
    public String getIdEvenement() {
        return getString(ID_EVENEMENT);
    }

    @Override
    public void setIdEvenement(String idEvenement) {
        put(ID_EVENEMENT, idEvenement);
    }

    @Override
    public String getEtatEvenement() {
        return getString(ETAT_EVENEMENT);
    }

    @Override
    public void setEtatEvenement(String etatEvenement) {
        put(ETAT_EVENEMENT, etatEvenement);
    }

    @Override
    public String getTypeEvenement() {
        return getString(TYPE_EVENEMENT);
    }

    @Override
    public void setTypeEvenement(String typeEvenement) {
        put(TYPE_EVENEMENT, typeEvenement);
    }

    @Override
    public Date getDateEvenement() {
        return getDate(DATE_EVENEMENT);
    }

    @Override
    public void setDateEvenement(Date dateEvenement) {
        put(DATE_EVENEMENT, dateEvenement);
    }

    @Override
    public String getEmetteurEvenement() {
        return getString(EMETTEUR_EVENEMENT);
    }

    @Override
    public void setEmetteurEvenement(String emetteurEvenement) {
        put(EMETTEUR_EVENEMENT, emetteurEvenement);
    }

    @Override
    public String getDestinataireEvenement() {
        return getString(DESTINATAIRE_EVENEMENT);
    }

    @Override
    public void setDestinataireEvenement(String destinataireEvenement) {
        put(DESTINATAIRE_EVENEMENT, destinataireEvenement);
    }

    @Override
    public List<String> getCopieEvenement() {
        return getListString(COPIE_EVENEMENT);
    }

    @Override
    public void setCopieEvenement(List<String> copieEvenement) {
        putListString(COPIE_EVENEMENT, copieEvenement);
    }

    @Override
    public Boolean isPresencePieceJointe() {
        return getBoolean(PRESENCE_PIECE_JOINTE);
    }

    @Override
    public void setPresencePieceJointe(Boolean presencePieceJointe) {
        put(PRESENCE_PIECE_JOINTE, presencePieceJointe);
    }

    @Override
    public String getObjet() {
        return getString(OBJET);
    }

    @Override
    public void setObjet(String objet) {
        put(OBJET, objet);
    }

    @Override
    public Integer getNiveauLecture() {
        return getInteger(NIVEAU_LECTURE);
    }

    @Override
    public void setNiveauLecture(Integer niveauLecture) {
        put(NIVEAU_LECTURE, niveauLecture);
    }

    @Override
    public String getCodeLecture() {
        return getString(CODE_LECTURE);
    }

    @Override
    public void setCodeLecture(String codeLecture) {
        put(CODE_LECTURE, codeLecture);
    }

    @Override
    public String getIdDossier() {
        return getString(ID_DOSSIER);
    }

    @Override
    public void setIdDossier(String idDossier) {
        put(ID_DOSSIER, idDossier);
    }

    @Override
    public String getIdSenat() {
        return getString(ID_SENAT);
    }

    @Override
    public void setIdSenat(String idSenat) {
        put(ID_SENAT, idSenat);
    }

    @Override
    public String getEtatDossier() {
        return getString(ETAT_DOSSIER);
    }

    @Override
    public void setEtatDossier(String etatDossier) {
        put(ETAT_DOSSIER, etatDossier);
    }

    @Override
    public String getNumeroDepot() {
        return getString(NUMERO_DEPOT);
    }

    @Override
    public void setNumeroDepot(String numeroDepot) {
        put(NUMERO_DEPOT, numeroDepot);
    }

    @Override
    public Date getDateDepot() {
        return getDate(DATE_DEPOT);
    }

    @Override
    public void setDateDepot(Date dateDepot) {
        put(DATE_DEPOT, dateDepot);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        try {
            EvenementTypeDescriptor evenementTypeDescriptor = SolonMgppServiceLocator
                .getEvenementTypeService()
                .getEvenementType(getTypeEvenement());
            if (evenementTypeDescriptor != null) {
                builder.append(evenementTypeDescriptor.getLabel());
            } else {
                builder.append(getTypeEvenement());
            }
        } catch (NuxeoException e) {
            builder.append(getTypeEvenement());
        }

        builder.append(SEPARATOR);
        builder.append(getIdDossier());

        if (StringUtils.isNotBlank(getObjet())) {
            builder.append(SEPARATOR);
            builder.append(getObjet());
        }

        return builder.toString();
    }

    @Override
    public String getType() {
        return MessageDTO.class.getSimpleName();
    }

    @Override
    public String getDocIdForSelection() {
        return getIdDossier();
    }

    @Override
    public Boolean isEnAttente() {
        return getBoolean(EN_ATTENTE);
    }

    @Override
    public void setEnAttente(Boolean enAttente) {
        put(EN_ATTENTE, enAttente);
    }

    @Override
    public String toComparableString() {
        StringBuilder builder = new StringBuilder();

        try {
            EvenementTypeDescriptor evenementTypeDescriptor = SolonMgppServiceLocator
                .getEvenementTypeService()
                .getEvenementType(getTypeEvenement());
            if (evenementTypeDescriptor != null) {
                builder.append(evenementTypeDescriptor.getLabel());
            } else {
                builder.append(getTypeEvenement());
            }
        } catch (NuxeoException e) {
            builder.append(getTypeEvenement());
        }

        builder.append(SEPARATOR);
        builder.append(getIdDossier());
        builder.append(SEPARATOR);
        builder.append(getObjet());
        builder.append(SEPARATOR);
        builder.append(getIdEvenement());
        return builder.toString();
    }
}
