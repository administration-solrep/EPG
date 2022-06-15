package fr.dila.solonepg.ui.bean.favoris.recherche;

import static fr.dila.solonepg.api.constant.SolonEpgConstant.FAVORIS_RECHERCHE_DOCUMENT_TYPE;
import static fr.dila.solonepg.api.constant.SolonEpgSchemaConstant.FAVORIS_RECHERCHE_TYPE_XPATH;
import static fr.dila.st.api.constant.STSchemaConstant.DUBLINCORE_CREATOR_XPATH;
import static fr.dila.st.api.constant.STSchemaConstant.DUBLINCORE_TITLE_XPATH;
import static fr.dila.st.api.constant.STSchemaConstant.ECM_UUID_XPATH;

import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.NxProp.Way;
import fr.dila.st.ui.mapper.MapDoc2BeanProcessFormatUsername;

public class EpgFavorisRechercheDTO {
    @NxProp(xpath = ECM_UUID_XPATH, docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE)
    private String idFavori;

    @NxProp(xpath = DUBLINCORE_TITLE_XPATH, docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE)
    private String intitule;

    @NxProp(xpath = FAVORIS_RECHERCHE_TYPE_XPATH, docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE)
    private String type;

    @NxProp(
        xpath = DUBLINCORE_CREATOR_XPATH,
        docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE,
        process = MapDoc2BeanProcessFormatUsername.class,
        way = Way.DOC_TO_BEAN
    )
    private String createur;

    public EpgFavorisRechercheDTO() {}

    public EpgFavorisRechercheDTO(String idFavori, String intitule, String type, String createur) {
        super();
        setIdFavori(idFavori);
        setIntitule(intitule);
        setType(type);
        setCreateur(createur);
    }

    public String getIdFavori() {
        return idFavori;
    }

    public void setIdFavori(String idFavori) {
        this.idFavori = idFavori;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateur() {
        return createur;
    }

    public void setCreateur(String createur) {
        this.createur = createur;
    }
}
