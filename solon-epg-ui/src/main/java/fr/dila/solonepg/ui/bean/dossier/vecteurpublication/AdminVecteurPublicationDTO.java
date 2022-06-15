package fr.dila.solonepg.ui.bean.dossier.vecteurpublication;

import fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean
public class AdminVecteurPublicationDTO {
    @NxProp(
        docType = SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE,
        xpath = STSchemaConstant.ECM_UUID_XPATH
    )
    @FormParam("id")
    private String id;

    @NxProp(
        docType = SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE,
        xpath = SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_INTITULE_XPATH
    )
    @SwRequired
    @SwNotEmpty
    @FormParam("vecteurPublicationJO")
    private String intitule;

    @NxProp(
        docType = SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE,
        xpath = SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DEBUT_XPATH
    )
    @FormParam("dateDebut")
    private Calendar dateDebut;

    @NxProp(
        docType = SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE,
        xpath = SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_FIN_XPATH
    )
    @FormParam("dateFin")
    private Calendar dateFin;

    public AdminVecteurPublicationDTO() {}

    public AdminVecteurPublicationDTO(String id, String intitule, Calendar dateDebut, Calendar dateFin) {
        this.id = id;
        this.intitule = intitule;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Calendar getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Calendar dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Calendar getDateFin() {
        return dateFin;
    }

    public void setDateFin(Calendar dateFin) {
        this.dateFin = dateFin;
    }
}
