package fr.dila.solonmgpp.core.domain;

import fr.dila.solonmgpp.api.domain.FichePresentationSD;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.Calendar;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

public class FichePresentationSDImpl extends FichePresentationSupportVoteImpl implements FichePresentationSD {

    public FichePresentationSDImpl(DocumentModel document) {
        super(document);
    }

    @Override
    public Calendar getDateDeclaration() {
        return PropertyUtil.getCalendarProperty(document, this.getSchemas(), DATE_DECLARATION);
    }

    @Override
    public void setDateDeclaration(Calendar dateDeclaration) {
        PropertyUtil.setProperty(document, this.getSchemas(), DATE_DECLARATION, dateDeclaration);
    }

    @Override
    public Calendar getDateDemande() {
        return PropertyUtil.getCalendarProperty(document, this.getSchemas(), DATE_DEMANDE);
    }

    @Override
    public void setDateDemande(Calendar dateDemande) {
        PropertyUtil.setProperty(document, this.getSchemas(), DATE_DEMANDE, dateDemande);
    }

    @Override
    public List<String> getGroupeParlementaire() {
        return PropertyUtil.getStringListProperty(document, SCHEMA, GROUPE_PARLEMENTAIRE);
    }

    @Override
    public void setGroupeParlementaire(List<String> groupeParlementaire) {
        PropertyUtil.setProperty(document, SCHEMA, GROUPE_PARLEMENTAIRE, groupeParlementaire);
    }

    @Override
    public void setDemandeVote(boolean demandeVote) {
        PropertyUtil.setProperty(document, SCHEMA, DEMANDE_VOTE, demandeVote);
    }

    @Override
    public boolean getDemandeVote() {
        return PropertyUtil.getBooleanProperty(document, SCHEMA, DEMANDE_VOTE);
    }

    @Override
    protected String getSchemas() {
        return SCHEMA;
    }
}
