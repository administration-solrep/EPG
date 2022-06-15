package fr.dila.solonepg.core.dto;

import fr.dila.st.core.client.AbstractMapDTO;
import java.util.Date;

public class DossierArchivageStatDTOImpl extends AbstractMapDTO implements DossierArchivageStatDTO {
    private static final long serialVersionUID = 1158636689158993366L;

    private static final String NOR = "nor";
    private static final String TITREACTE = "titreActe";
    private static final String STATUT_PERIODE = "statutPeriode";
    private static final String DATE = "date";
    private static final String STATUT = "statut";
    private static final String ERREUR = "erreur";
    public static final String DOC_ID_FOR_SELECTION = "docIdForSelection";

    public DossierArchivageStatDTOImpl(
        String id,
        String nor,
        String titreActe,
        String statutPeriode,
        Date date,
        String statut,
        String erreur
    ) {
        setDocIdForSelection(id);
        setNor(nor);
        setTitreActe(titreActe);
        setStatutPeriode(statutPeriode);
        setStatut(statut);
        setDateModif(date);
        setErreur(erreur);
    }

    @Override
    public String getType() {
        return "Dossier";
    }

    @Override
    public String getDocIdForSelection() {
        return getString(DOC_ID_FOR_SELECTION);
    }

    @Override
    public void setDocIdForSelection(String id) {
        put(DOC_ID_FOR_SELECTION, id);
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
    public String getTitreActe() {
        return getString(TITREACTE);
    }

    @Override
    public void setTitreActe(String titreActe) {
        put(TITREACTE, titreActe);
    }

    @Override
    public String getStatutPeriode() {
        return getString(STATUT_PERIODE);
    }

    @Override
    public void setStatutPeriode(String statutPeriode) {
        put(STATUT_PERIODE, statutPeriode);
    }

    @Override
    public Date getDateModif() {
        return getDate(DATE);
    }

    @Override
    public void setDateModif(Date dateModif) {
        put(DATE, dateModif);
    }

    @Override
    public String getStatut() {
        return getString(STATUT);
    }

    @Override
    public void setStatut(String statut) {
        put(STATUT, statut);
    }

    @Override
    public String getErreur() {
        return getString(ERREUR);
    }

    @Override
    public void setErreur(String erreur) {
        put(ERREUR, erreur);
    }
}
