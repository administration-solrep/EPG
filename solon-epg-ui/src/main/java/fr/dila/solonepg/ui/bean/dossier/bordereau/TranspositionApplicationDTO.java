package fr.dila.solonepg.ui.bean.dossier.bordereau;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.mapper.MapDoc2BeanIntegerStringProcess;
import java.util.ArrayList;
import java.util.List;

@SwBean
public class TranspositionApplicationDTO {

    public TranspositionApplicationDTO() {
        super();
    }

    private List<TranspositionApplicationDetailDTO> transpositionDirectives = new ArrayList<>();
    private List<TranspositionApplicationDetailDTO> applicationLoisOrdonnances = new ArrayList<>();

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_HABILITATION_REF_LOI_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String referenceLoiHabilitation;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_HABILITATION_NUMERO_ARTICLES_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String numeroArticleHabilitation;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_HABILITATION_COMMENTAIRE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String commentaireHabilitation;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_HABILITATION_NUMERO_ORDRE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        process = MapDoc2BeanIntegerStringProcess.class
    )
    private Integer numeroOrdreHabilitation;

    public List<TranspositionApplicationDetailDTO> getTranspositionDirectives() {
        return transpositionDirectives;
    }

    public void setTranspositionDirectives(List<TranspositionApplicationDetailDTO> transpositionDirectives) {
        this.transpositionDirectives = transpositionDirectives;
    }

    public List<TranspositionApplicationDetailDTO> getApplicationLoisOrdonnances() {
        return applicationLoisOrdonnances;
    }

    public void setApplicationLoisOrdonnances(List<TranspositionApplicationDetailDTO> applicationLoisOrdonnances) {
        this.applicationLoisOrdonnances = applicationLoisOrdonnances;
    }

    public String getReferenceLoiHabilitation() {
        return referenceLoiHabilitation;
    }

    public void setReferenceLoiHabilitation(String referenceLoiHabilitation) {
        this.referenceLoiHabilitation = referenceLoiHabilitation;
    }

    public String getNumeroArticleHabilitation() {
        return numeroArticleHabilitation;
    }

    public void setNumeroArticleHabilitation(String numeroArticleHabilitation) {
        this.numeroArticleHabilitation = numeroArticleHabilitation;
    }

    public String getCommentaireHabilitation() {
        return commentaireHabilitation;
    }

    public void setCommentaireHabilitation(String commentaireHabilitation) {
        this.commentaireHabilitation = commentaireHabilitation;
    }

    public Integer getNumeroOrdreHabilitation() {
        return numeroOrdreHabilitation;
    }

    public void setNumeroOrdreHabilitation(Integer numeroOrdreHabilitation) {
        this.numeroOrdreHabilitation = numeroOrdreHabilitation;
    }
}
