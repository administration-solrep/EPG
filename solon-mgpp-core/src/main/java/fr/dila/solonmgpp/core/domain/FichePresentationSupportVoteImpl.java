package fr.dila.solonmgpp.core.domain;

import fr.dila.solonmgpp.api.domain.FichePresentationSupportVote;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

public abstract class FichePresentationSupportVoteImpl implements FichePresentationSupportVote {
    protected final DocumentModel document;

    public FichePresentationSupportVoteImpl(DocumentModel document) {
        this.document = document;
    }

    @Override
    public void setIdDossier(String idDossier) {
        PropertyUtil.setProperty(document, this.getSchemas(), ID_DOSSIER, idDossier);
    }

    @Override
    public String getIdDossier() {
        return PropertyUtil.getStringProperty(document, this.getSchemas(), ID_DOSSIER);
    }

    @Override
    public String getObjet() {
        return PropertyUtil.getStringProperty(document, this.getSchemas(), OBJET);
    }

    @Override
    public void setObjet(String objet) {
        PropertyUtil.setProperty(document, this.getSchemas(), OBJET, objet);
    }

    @Override
    public Calendar getDateLettrePm() {
        return PropertyUtil.getCalendarProperty(document, this.getSchemas(), DATE_LETTRE_PM);
    }

    @Override
    public void setDateLettrePm(Calendar dateLettrePm) {
        PropertyUtil.setProperty(document, this.getSchemas(), DATE_LETTRE_PM, dateLettrePm);
    }

    @Override
    public Calendar getDateVote() {
        return PropertyUtil.getCalendarProperty(document, this.getSchemas(), DATE_VOTE);
    }

    @Override
    public void setDateVote(Calendar dateVote) {
        PropertyUtil.setProperty(document, this.getSchemas(), DATE_VOTE, dateVote);
    }

    @Override
    public Long getSuffrageExprime() {
        return PropertyUtil.getLongProperty(document, this.getSchemas(), SUFFRAGE_EXPRIME);
    }

    @Override
    public void setSuffrageExprime(Long suffrageExprime) {
        PropertyUtil.setProperty(document, this.getSchemas(), SUFFRAGE_EXPRIME, suffrageExprime);
    }

    @Override
    public Long getVotePour() {
        return PropertyUtil.getLongProperty(document, this.getSchemas(), VOTE_POUR);
    }

    @Override
    public void setVotePour(Long votePour) {
        PropertyUtil.setProperty(document, this.getSchemas(), VOTE_POUR, votePour);
    }

    @Override
    public Long getVoteContre() {
        return PropertyUtil.getLongProperty(document, this.getSchemas(), VOTE_CONTRE);
    }

    @Override
    public void setVoteContre(Long voteContre) {
        PropertyUtil.setProperty(document, this.getSchemas(), VOTE_CONTRE, voteContre);
    }

    @Override
    public Long getAbstention() {
        return PropertyUtil.getLongProperty(document, this.getSchemas(), ABSTENTION);
    }

    @Override
    public void setAbstention(Long abstention) {
        PropertyUtil.setProperty(document, this.getSchemas(), ABSTENTION, abstention);
    }

    @Override
    public String getSensAvis() {
        return PropertyUtil.getStringProperty(document, this.getSchemas(), SENS_AVIS);
    }

    @Override
    public void setSensAvis(String sensAvis) {
        PropertyUtil.setProperty(document, this.getSchemas(), SENS_AVIS, sensAvis);
    }

    @Override
    public DocumentModel getDocument() {
        return this.document;
    }

    protected abstract String getSchemas();
}
