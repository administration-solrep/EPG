package fr.dila.solonepg.web.client;

import java.util.Date;

import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.st.core.client.AbstractMapDTO;

public class FeuilleRouteDTOImpl extends AbstractMapDTO implements FeuilleRouteDTO {

    private static final String IS_DEFAULT = "isDefault";

    private static final String DELETER = "deleter";

    private static final long serialVersionUID = -2611460819073773991L;

    private static final String LOCK_INFO = "lockInfo";
    private static final String DATE_MODIFICATION = "dateModification";
    private static final String AUTEUR = "auteur";
    private static final String MINISTERE = "ministere";
    private static final String NUMERO = "numero";
    private static final String TITLE = "title";
    private static final String ETAT = "etat";
    private static final String ID = "id";
    public static final String DOC_ID_FOR_SELECTION = "docIdForSelection";

    public FeuilleRouteDTOImpl() {
        setDefault(Boolean.FALSE);
    }

    @Override
    public String getType() {
        return SolonEpgFeuilleRoute.class.getSimpleName();
    }

    @Override
    public String getDocIdForSelection() {
        return getString(DOC_ID_FOR_SELECTION);
    }

    @Override
    public void setDocIdForSelection(String docIdForSelection) {
        put(DOC_ID_FOR_SELECTION, docIdForSelection);
    }

    @Override
    public String getId() {
        return getString(ID);
    }

    @Override
    public void setId(String id) {
        put(ID, id);
    }

    @Override
    public String getEtat() {
        return getString(ETAT);
    }

    @Override
    public void setEtat(String etat) {
        put(ETAT, etat);
    }

    @Override
    public String getTitle() {
        return getString(TITLE);
    }

    @Override
    public void setTitle(String title) {
        put(TITLE, title);
    }

    @Override
    public String getNumero() {
        return getString(NUMERO);
    }

    @Override
    public void setNumero(String numero) {
        put(NUMERO, numero);
    }

    @Override
    public String getMinistere() {
        return getString(MINISTERE);
    }

    @Override
    public void setMinistere(String ministere) {
        put(MINISTERE, ministere);
    }

    @Override
    public String getAuteur() {
        return getString(AUTEUR);
    }

    @Override
    public void setAuteur(String auteur) {
        put(AUTEUR, auteur);
    }

    @Override
    public Date getDateModification() {
        return getDate(DATE_MODIFICATION);
    }

    @Override
    public void setDateModification(Date dateModification) {
        put(DATE_MODIFICATION, dateModification);
    }

    @Override
    public String getLockInfo() {
        return getString(LOCK_INFO);
    }

    @Override
    public void setLockInfo(String lockInfo) {
        put(LOCK_INFO, lockInfo);
    }

    @Override
    public Boolean isDeleter() {
        return getBoolean(DELETER);
    }

    @Override
    public void setDeleter(Boolean deleter) {
        put(DELETER, deleter);
    }

    @Override
    public Boolean isDefault() {
        return getBoolean(IS_DEFAULT);
    }

    @Override
    public void setDefault(Boolean isDefault) {
        put(IS_DEFAULT, isDefault);
    }

}
