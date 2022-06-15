package fr.dila.solonepg.core.dto.activitenormative;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.service.VocabularyServiceImpl;
import java.io.Serializable;
import org.nuxeo.ecm.core.api.CoreSession;

public class FicheSignaletiqueLoiDTO extends AbstractFicheSignaletiqueApplicationDTO implements Serializable {
    private static final String PROCEDURE_VOTE_NORMALE = "1";

    private static final long serialVersionUID = 78416213510924898L;

    private String natureTexte;
    private String procedureVote;
    private String commentaire;

    private Boolean hasHabilitation;

    public FicheSignaletiqueLoiDTO(ActiviteNormative activiteNormative, CoreSession session) {
        super(activiteNormative, session);
        TexteMaitre texteMaitre = activiteNormative.getDocument().getAdapter(TexteMaitre.class);

        // infos lois
        hasHabilitation = texteMaitre.isDispositionHabilitation();

        computeProcedureVote(texteMaitre);
        computeNatureTexte(texteMaitre);

        // commentaire
        commentaire = texteMaitre.getChampLibre();

        // Observation
        setObservation(texteMaitre.getObservation());
    }

    private void computeNatureTexte(TexteMaitre texteMaitre) {
        if (texteMaitre.getProcedureVote() != null) {
            natureTexte =
                STServiceLocator
                    .getVocabularyService()
                    .getEntryLabel(VocabularyConstants.NATURE_TEXTE_DIRECTORY, texteMaitre.getNatureTexte());
        } else {
            natureTexte = "";
        }

        natureTexte = natureTexte.replace(VocabularyServiceImpl.UNKNOWN_ENTRY, "");
    }

    private void computeProcedureVote(TexteMaitre texteMaitre) {
        if (texteMaitre.getProcedureVote() == null) {
            procedureVote = "";
        } else {
            if (PROCEDURE_VOTE_NORMALE.equals(texteMaitre.getProcedureVote())) {
                procedureVote = "Vote selon la procédure de droit commun";
            } else {
                procedureVote =
                    STServiceLocator
                        .getVocabularyService()
                        .getEntryLabel(VocabularyConstants.PROCEDURE_VOTE_DIRECTORY, texteMaitre.getProcedureVote());
            }
        }
    }

    public String getNatureTexte() {
        return natureTexte;
    }

    public void setNatureTexte(String natureTexte) {
        this.natureTexte = natureTexte;
    }

    public String getProcedureVote() {
        return procedureVote;
    }

    public void setProcedureVote(String procedureVote) {
        this.procedureVote = procedureVote;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public void setHasHabilitation(Boolean hasHabilitation) {
        this.hasHabilitation = hasHabilitation;
    }

    public Boolean getHasHabilitation() {
        return hasHabilitation;
    }
}
