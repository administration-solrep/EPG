package fr.dila.solonepg.ui.bean;

import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.st.core.client.AbstractMapDTO;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

public class MgppFichePresentationOEPDTO extends AbstractMapDTO {
    public static final String ID = "id";
    public static final String NOM = "nom";
    public static final String MINISTERE = "ministere";
    public static final String DATE = "date";

    public MgppFichePresentationOEPDTO() {
        super();
    }

    public MgppFichePresentationOEPDTO(DocumentModel doc) {
        FichePresentationOEP fiche = doc.getAdapter(FichePresentationOEP.class);
        put(ID, fiche.getIdDossier());
        put(NOM, fiche.getNomOrganisme());
        put(MINISTERE, fiche.getMinistereRattachement());
        put(DATE, fiche.getDate());
    }

    public String getId() {
        return getString(ID);
    }

    public String getNom() {
        return getString(NOM);
    }

    public String getMinistere() {
        return getString(MINISTERE);
    }

    public Calendar getDate() {
        return getCalendar(DATE);
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getDocIdForSelection() {
        return getId();
    }
}
