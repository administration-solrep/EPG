package fr.dila.solonepg.core.documentmodel;

import fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants;
import fr.dila.solonepg.api.documentmodel.ListeTraitementPapier;
import fr.dila.st.core.domain.STDomainObjectImpl;
import java.util.Calendar;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

public class ListeTraitementPapierImpl extends STDomainObjectImpl implements ListeTraitementPapier {

    public ListeTraitementPapierImpl(DocumentModel doc) {
        super(doc);
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Long getNumeroListe() {
        return getLongProperty(
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA,
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_NUMERO_LISTE_PROPERTY
        );
    }

    @Override
    public void setNumeroListe(Long numeroListe) {
        setProperty(
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA,
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_NUMERO_LISTE_PROPERTY,
            numeroListe
        );
    }

    @Override
    public List<String> getIdsDossier() {
        return getListStringProperty(
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA,
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_IDS_DOSSIER_PROPERTY
        );
    }

    @Override
    public void setIdsDossier(List<String> idsDossier) {
        setProperty(
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA,
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_IDS_DOSSIER_PROPERTY,
            idsDossier
        );
    }

    @Override
    public String getTypeListe() {
        return getStringProperty(
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA,
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_TYPE_LISTE_PROPERTY
        );
    }

    @Override
    public void setTypeListe(String typeListe) {
        setProperty(
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA,
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_TYPE_LISTE_PROPERTY,
            typeListe
        );
    }

    @Override
    public String getTypeSignature() {
        return getStringProperty(
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA,
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_TYPE_SIGNATURE
        );
    }

    @Override
    public void setTypeSignature(String typeSignature) {
        setProperty(
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA,
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_TYPE_SIGNATURE,
            typeSignature
        );
    }

    @Override
    public Calendar getDateGenerationListe() {
        return getDateProperty(
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA,
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DATE_GENERATION_LISTE_PROPERTY
        );
    }

    @Override
    public void setDateGenerationListe(Calendar dateGenerationListe) {
        setProperty(
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA,
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DATE_GENERATION_LISTE_PROPERTY,
            dateGenerationListe
        );
    }
}
