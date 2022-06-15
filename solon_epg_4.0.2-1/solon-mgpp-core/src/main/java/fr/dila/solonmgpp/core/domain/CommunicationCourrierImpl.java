package fr.dila.solonmgpp.core.domain;

import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.ADOPTE_LE_1ERE_LECTURE_AN_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.ADOPTE_LE_1ERE_LECTURE_SENAT_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.ADOPTE_LE_2EME_LECTURE_AN_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.ADOPTE_LE_2EME_LECTURE_SENAT_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.ADOPTE_LE_NOUVELLE_LECTURE_AN_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.ADOPTE_LE_NOUVELLE_LECTURE_SENAT_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.ASSEMBLE_DEPOT_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.AUTEUR_LEX01_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.AUTEUR_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.COAUTEURS_LEX01_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.DATE_CONSEIL_MINISTRES_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.DATE_DEPOT_FICHE_LOI_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.DATE_DU_JOUR_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.DESTINATAIRE_COURRIER_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.INTITULE_DERNIERE_COMMUNICATION_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.INTITULE_PREMIERE_COMMUNICATION_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.MINISTERE_RESPONSABLE_DOSSIER_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.NAVETTE_LE_1ERE_LECTURE_AN_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.NAVETTE_LE_1ERE_LECTURE_SENAT_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.NOM_AN_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.NOM_SENAT_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.NOR_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.REJETE_LE_1ERE_LECTURE_AN_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.REJETE_LE_1ERE_LECTURE_SENAT_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.REJETE_LE_2EME_LECTURE_AN_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.REJETE_LE_2EME_LECTURE_SENAT_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.REJETE_LE_NOUVELLE_LECTURE_AN_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.REJETE_LE_NOUVELLE_LECTURE_SENAT_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.SCHEMA_NAME;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.SIGNATAIRE_ADJOINT_SGG_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.SIGNATAIRE_SGG_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.SIGNATURE_SGG_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.SORT_ADOPTION_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.TITRE_ACTE_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.TRANSMIS_LE_NOUVELLE_LECTURE_AN_PROP;
import static fr.dila.solonmgpp.api.constant.CommunicationCourrierSchemaConstants.TRANSMIS_LE_NOUVELLE_LECTURE_SENAT_PROP;
import static java.lang.String.join;

import fr.dila.solonmgpp.api.domain.CommunicationCourrier;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;

public class CommunicationCourrierImpl implements CommunicationCourrier {
    private final DocumentModel document;
    private static final String VALEUR_NON_TROUVEE = "**champ_non_renseigne**";

    private static final String LIST_SEP = ", ";

    public CommunicationCourrierImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public DocumentModel getDocument() {
        return document;
    }

    private String getStrParameterOrDefault(String property) {
        return StringUtils.defaultIfEmpty(
            PropertyUtil.getStringProperty(document, SCHEMA_NAME, property),
            VALEUR_NON_TROUVEE
        );
    }

    @Override
    public String getAuteurLex01() {
        return getStrParameterOrDefault(AUTEUR_LEX01_PROP);
    }

    @Override
    public void setAuteurLex01(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, AUTEUR_LEX01_PROP, value);
    }

    @Override
    public List<String> getSignatureSGG() {
        String signatures = getStrParameterOrDefault(SIGNATURE_SGG_PROP);
        return signatures != null ? Arrays.asList(signatures.split(LIST_SEP)) : null;
    }

    @Override
    public void setSignatureSGG(List<String> list) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, SIGNATURE_SGG_PROP, getListAsString(list));
    }

    @Override
    public String getNomAN() {
        return getStrParameterOrDefault(NOM_AN_PROP);
    }

    @Override
    public void setNomAN(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, NOM_AN_PROP, value);
    }

    @Override
    public String getNomSenat() {
        return getStrParameterOrDefault(NOM_SENAT_PROP);
    }

    @Override
    public void setNomSenat(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, NOM_SENAT_PROP, value);
    }

    @Override
    public String getAdopteLe1ereLectureAN() {
        return getStrParameterOrDefault(ADOPTE_LE_1ERE_LECTURE_AN_PROP);
    }

    @Override
    public void setAdopteLe1ereLectureAN(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, ADOPTE_LE_1ERE_LECTURE_AN_PROP, value);
    }

    @Override
    public String getAdopteLe1ereLectureSenat() {
        return getStrParameterOrDefault(ADOPTE_LE_1ERE_LECTURE_SENAT_PROP);
    }

    @Override
    public void setAdopteLe1ereLectureSenat(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, ADOPTE_LE_1ERE_LECTURE_SENAT_PROP, value);
    }

    @Override
    public String getAdopteLe2emeLectureAN() {
        return getStrParameterOrDefault(ADOPTE_LE_2EME_LECTURE_AN_PROP);
    }

    @Override
    public void setAdopteLe2emeLectureAN(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, ADOPTE_LE_2EME_LECTURE_AN_PROP, value);
    }

    @Override
    public String getAdopteLe2emeLectureSenat() {
        return getStrParameterOrDefault(ADOPTE_LE_2EME_LECTURE_SENAT_PROP);
    }

    @Override
    public void setAdopteLe2emeLectureSenat(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, ADOPTE_LE_2EME_LECTURE_SENAT_PROP, value);
    }

    @Override
    public String getAdopteLeNouvelleLectureAN() {
        return getStrParameterOrDefault(ADOPTE_LE_NOUVELLE_LECTURE_AN_PROP);
    }

    @Override
    public void setAdopteLeNouvelleLectureAN(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, ADOPTE_LE_NOUVELLE_LECTURE_AN_PROP, value);
    }

    @Override
    public String getAdopteLeNouvelleLectureSenat() {
        return getStrParameterOrDefault(ADOPTE_LE_NOUVELLE_LECTURE_SENAT_PROP);
    }

    @Override
    public void setAdopteLeNouvelleLectureSenat(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, ADOPTE_LE_NOUVELLE_LECTURE_SENAT_PROP, value);
    }

    @Override
    public String getRejeteLe1ereLectureAN() {
        return getStrParameterOrDefault(REJETE_LE_1ERE_LECTURE_AN_PROP);
    }

    @Override
    public void setRejeteLe1ereLectureAN(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, REJETE_LE_1ERE_LECTURE_AN_PROP, value);
    }

    @Override
    public String getRejeteLe1ereLectureSenat() {
        return getStrParameterOrDefault(REJETE_LE_1ERE_LECTURE_SENAT_PROP);
    }

    @Override
    public void setRejeteLe1ereLectureSenat(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, REJETE_LE_1ERE_LECTURE_SENAT_PROP, value);
    }

    @Override
    public String getRejeteLe2emeLectureAN() {
        return getStrParameterOrDefault(REJETE_LE_2EME_LECTURE_AN_PROP);
    }

    @Override
    public void setRejeteLe2emeLectureAN(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, REJETE_LE_2EME_LECTURE_AN_PROP, value);
    }

    @Override
    public String getRejeteLe2emeLectureSenat() {
        return getStrParameterOrDefault(REJETE_LE_2EME_LECTURE_SENAT_PROP);
    }

    @Override
    public void setRejeteLe2emeLectureSenat(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, REJETE_LE_2EME_LECTURE_SENAT_PROP, value);
    }

    @Override
    public String getRejeteLeNouvelleLectureAN() {
        return getStrParameterOrDefault(REJETE_LE_NOUVELLE_LECTURE_AN_PROP);
    }

    @Override
    public void setRejeteLeNouvelleLectureAN(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, REJETE_LE_NOUVELLE_LECTURE_AN_PROP, value);
    }

    @Override
    public String getRejeteLeNouvelleLectureSenat() {
        return getStrParameterOrDefault(REJETE_LE_NOUVELLE_LECTURE_SENAT_PROP);
    }

    @Override
    public void setRejeteLeNouvelleLectureSenat(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, REJETE_LE_NOUVELLE_LECTURE_SENAT_PROP, value);
    }

    @Override
    public String getTransmisLeNouvelleLectureAN() {
        return getStrParameterOrDefault(TRANSMIS_LE_NOUVELLE_LECTURE_AN_PROP);
    }

    @Override
    public void setTransmisLeNouvelleLectureAN(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, TRANSMIS_LE_NOUVELLE_LECTURE_AN_PROP, value);
    }

    @Override
    public String getTransmisLeNouvelleLectureSenat() {
        return getStrParameterOrDefault(TRANSMIS_LE_NOUVELLE_LECTURE_SENAT_PROP);
    }

    @Override
    public void setTransmisLeNouvelleLectureSenat(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, TRANSMIS_LE_NOUVELLE_LECTURE_SENAT_PROP, value);
    }

    @Override
    public String getAssembleDepot() {
        return getStrParameterOrDefault(ASSEMBLE_DEPOT_PROP);
    }

    @Override
    public void setAssembleDepot(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, ASSEMBLE_DEPOT_PROP, value);
    }

    @Override
    public String getDestinataireCourrier() {
        return getStrParameterOrDefault(DESTINATAIRE_COURRIER_PROP);
    }

    @Override
    public void setDestinataireCourrier(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, DESTINATAIRE_COURRIER_PROP, value);
    }

    @Override
    public List<String> getCoauteursLEX01() {
        String coauteurs = getStrParameterOrDefault(COAUTEURS_LEX01_PROP);
        return coauteurs != null ? Arrays.asList(coauteurs.split(LIST_SEP)) : null;
    }

    @Override
    public void setCoauteursLEX01(List<String> list) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, COAUTEURS_LEX01_PROP, getListAsString(list));
    }

    @Override
    public String getDateConseilMinistres() {
        return getStrParameterOrDefault(DATE_CONSEIL_MINISTRES_PROP);
    }

    @Override
    public void setDateConseilMinistres(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, DATE_CONSEIL_MINISTRES_PROP, value);
    }

    @Override
    public String getDateDepotFicheLoi() {
        return getStrParameterOrDefault(DATE_DEPOT_FICHE_LOI_PROP);
    }

    @Override
    public void setDateDepotFicheLoi(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, DATE_DEPOT_FICHE_LOI_PROP, value);
    }

    @Override
    public String getIntituleDerniereCommunication() {
        return getStrParameterOrDefault(INTITULE_DERNIERE_COMMUNICATION_PROP);
    }

    @Override
    public void setIntituleDerniereCommunication(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, INTITULE_DERNIERE_COMMUNICATION_PROP, value);
    }

    @Override
    public String getIntitulePremiereCommunication() {
        return getStrParameterOrDefault(INTITULE_PREMIERE_COMMUNICATION_PROP);
    }

    @Override
    public void setIntitulePremiereCommunication(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, INTITULE_PREMIERE_COMMUNICATION_PROP, value);
    }

    @Override
    public String getNavetteLe1ereLectureAN() {
        return getStrParameterOrDefault(NAVETTE_LE_1ERE_LECTURE_AN_PROP);
    }

    @Override
    public void setNavetteLe1ereLectureAN(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, NAVETTE_LE_1ERE_LECTURE_AN_PROP, value);
    }

    @Override
    public String getNavetteLe1ereLectureSenat() {
        return getStrParameterOrDefault(NAVETTE_LE_1ERE_LECTURE_SENAT_PROP);
    }

    @Override
    public void setNavetteLe1ereLectureSenat(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, NAVETTE_LE_1ERE_LECTURE_SENAT_PROP, value);
    }

    @Override
    public String getSortAdoptionAN() {
        return getStrParameterOrDefault(SORT_ADOPTION_PROP);
    }

    @Override
    public void setSortAdoptionAN(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, SORT_ADOPTION_PROP, value);
    }

    @Override
    public String getAuteur() {
        return getStrParameterOrDefault(AUTEUR_PROP);
    }

    @Override
    public void setAuteur(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, AUTEUR_PROP, value);
    }

    @Override
    public String getDateDuJour() {
        return getStrParameterOrDefault(DATE_DU_JOUR_PROP);
    }

    @Override
    public void setDateDuJour(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, DATE_DU_JOUR_PROP, value);
    }

    @Override
    public String getNor() {
        return getStrParameterOrDefault(NOR_PROP);
    }

    @Override
    public void setNor(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, NOR_PROP, value);
    }

    @Override
    public String getTitreActe() {
        return getStrParameterOrDefault(TITRE_ACTE_PROP);
    }

    @Override
    public void setTitreActe(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, TITRE_ACTE_PROP, value);
    }

    @Override
    public String getMinistereResponsableDossier() {
        return getStrParameterOrDefault(MINISTERE_RESPONSABLE_DOSSIER_PROP);
    }

    @Override
    public void setMinistereResponsableDossier(String value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, MINISTERE_RESPONSABLE_DOSSIER_PROP, value);
    }

    private String getListAsString(List<String> list) {
        return CollectionUtils.isNotEmpty(list) ? join(LIST_SEP, list) : null;
    }

    @Override
    public String getSignataireSGG() {
        return getStrParameterOrDefault(SIGNATAIRE_SGG_PROP);
    }

    @Override
    public void setSignataireSgg(String signataire) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, SIGNATAIRE_SGG_PROP, signataire);
    }

    @Override
    public String getSignataireAdjointSGG() {
        return getStrParameterOrDefault(SIGNATAIRE_ADJOINT_SGG_PROP);
    }

    @Override
    public void setSignataireAdjointSgg(String signataire) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, SIGNATAIRE_ADJOINT_SGG_PROP, signataire);
    }
}
