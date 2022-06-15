package fr.dila.solonepg.api.cases;

import fr.dila.solonepg.api.cases.typescomplexe.TranspositionsContainer;
import fr.dila.solonepg.api.fonddossier.FondDeDossierInstance;
import fr.dila.solonepg.api.parapheur.ParapheurInstance;
import fr.dila.st.api.dossier.STDossier;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Dossier interface for Solon EPG (herit CaseLink Interface)
 *
 * @author arolin
 */
public interface Dossier extends Serializable, STDossier {
    // /////////////////
    // getter/setter to caseItem Document
    // ////////////////

    /**
     * Récupère le FondDeDossier.
     */
    FondDeDossierInstance getFondDeDossier(CoreSession session);

    /**
     * Récupère le Parapheur.
     */
    ParapheurInstance getParapheur(CoreSession session);

    // /////////////////
    // Dossier method
    // ////////////////

    /**
     * Récupère toutes les métadonnées d'un dossier à partir d'un autre dossier. Récupère uniquement les données
     * concernant le schéma dossier.
     *
     * @param dossier
     *            dossier
     * @return DocumentModel dossier object updated
     */
    DocumentModel getDossierMetadata(Dossier dossier);

    // /////////////////
    // Dossier getter/setter on property
    // ////////////////

    /**
     * Getter/setter de l'identifiant du parapheur
     */

    String getDocumentParapheurId();

    void setDocumentParapheurId(String documentParapheurId);

    /**
     * Getter/setter de l'identifiant du fond de dossier
     */

    String getDocumentFddId();

    void setDocumentFddId(String documentFddId);

    // /////////////////
    // info generales dossier property
    // ////////////////

    /**
     * Getter/setter du statut fonctionnel du dossier (dissocié du cycle de vie technique)
     */

    String getStatut();

    void setStatut(String statut);

    String getStatutLabel();

    void setStatutLabel(String statutLabel);

    /**
     * Getter/setter du candidat elimination. le dossier peut etre candidat elimination par admin fonctionnel, ou par
     * admin ministeriel ou n'est pas candidat
     */

    String getCandidat();

    void setCandidat(String candidat);

    /**
     * Getter pour savoir si le dossier est issu de l'injection
     */
    Boolean getIsDossierIssuInjection();

    /**
     * Setter pour savoir si le dossier est issu de l'injection
     */
    void setIsDossierIssuInjectionP(Boolean isDossierIssuInjection);

    /**
     * Getter pour savoir si le dossier est urgent
     */
    Boolean getIsUrgent();

    /**
     * Setter pour savoir si le dossier est urgent
     */
    void setIsUrgent(Boolean isUrgent);

    /**
     * Getter/setter de la date de candidature
     */
    Calendar getDateCandidature();

    void setDateCandidature(Calendar dateCandidature);

    /**
     * Getter/setter pour statut archivage
     */
    String getStatutArchivage();

    void setStatutArchivage(String statutArchivage);

    /**
     * Getter/setter de la date De la maintien du dossier Dans La base de Production
     */
    Calendar getDateDeMaintienEnProduction();

    void setDateDeMaintienEnProduction(Calendar dateDeMaintienEnProduction);

    /**
     * Getter/setter de la date du Versement dans Archivage Intermediaire
     */
    Calendar getDateVersementArchivageIntermediaire();

    void setDateVersementArchivageIntermediaire(Calendar dateVersementArchivageIntermediaire);

    /**
     * Getter nor : numéro nor unique du dossier : voir http://fr.wikipedia.org/wiki/Syst%C3%A8me_NOR
     *
     * @return String
     */
    String getNumeroNor();

    /**
     *
     * Setter nor : numéro nor unique du dossier : voir http://fr.wikipedia.org/wiki/Syst%C3%A8me_NOR
     *
     * @param String
     *            nor
     */
    void setNumeroNor(String numeroNor);

    /**
     * Getter nbDossierRectifie : nombre de dossier rectifiés existant pour ce dossier
     *
     * @return Long
     */
    Long getNbDossierRectifie();

    /**
     *
     * Setter nbDossierRectifie : nombre de dossier rectifiés existant pour ce dossier
     *
     * @param Long
     *            nbDossierRectifie
     */
    void setNbDossierRectifie(Long nbDossierRectifie);

    /**
     * Retourne l'identifiant technique du type d'acte : au moins 34 type d'actes posssibles : amnistie, arrêté,
     * ordonnance...
     *
     * @return Identifiant technique du type d'acte
     */
    String getTypeActe();

    void setTypeActe(String typeActe);

    String getTypeActeLabel();

    void setTypeActeLabel(String typeActeLabel);

    /**
     * Getter titre de l'acte : remplit automatiquement à partir du fichier de l'acte
     *
     * @return String
     */
    String getTitreActe();

    void setTitreActe(String titreActe);

    /**
     * Getter isParapheurFichierInfoNonRecupere
     *
     * @return Boolean
     */
    Boolean getIsParapheurFichierInfoNonRecupere();

    /**
     * Setter isParapheurFichierInfoNonRecupere
     *
     * @param isParapheurFichierInfoNonRecupere
     */
    void setIsParapheurFichierInfoNonRecupere(Boolean isParapheurFichierInfoNonRecupere);

    /**
     * Getter isParapheurTypeActeUpdated
     *
     * @return Boolean
     */
    Boolean getIsParapheurTypeActeUpdated();

    /**
     * Setter isParapheurTypeActeUpdated
     *
     * @param isParapheurTypeActeUpdated
     */
    void setIsParapheurTypeActeUpdated(Boolean isParapheurTypeActeUpdated);

    /**
     * Retourne vrai si le dossier est une mesure nominative.
     *
     * @return Vrai si le dossier est une mesure nominative
     */
    boolean isMesureNominative();

    /**
     * Permet de renseigner si le dossier est une mesure nominative.
     *
     * @param mesureNominative
     *            Vrai si le dossier est une mesure nominative
     */
    void setMesureNominative(boolean mesureNominative);

    /**
     * Getter numeroVersionActeOuExtrait : renvoie le numéro de version de l'acte ou l'extrait
     *
     * @return le numéro de version de l'acte ou l'extrait
     */
    Long getNumeroVersionActeOuExtrait();

    /**
     * Setter numeroVersionActeOuExtrait
     *
     * @param numeroVersionActeOuExtrait
     */
    void setNumeroVersionActeOuExtrait(Long numeroVersionActeOuExtrait);

    /**
     * Getter isActeReferenceForNumeroVersion : renvoie vrai si l'acte est le fichier de référence pour récupérer le
     * numero de version.
     *
     * @return renvoie vrai si l'acte est le fichier de référence pour récupérer le numero de version.
     */
    Boolean getIsActeReferenceForNumeroVersion();

    /**
     * Setter isActeReferenceForNumeroVersion
     *
     * @param isActeReferenceForNumeroVersion
     */
    void setIsActeReferenceForNumeroVersion(Boolean isActeReferenceForNumeroVersion);

    // /////////////////
    // info responsables dossier property
    // ////////////////

    /**
     * Getter ministere responsable de l'acte
     *
     * @return String
     */
    String getMinistereResp();

    void setMinistereResp(String ministereResp);

    /**
     * Getter direction responsable de l'acte
     *
     * @return String
     */
    String getDirectionResp();

    void setDirectionResp(String ministereResp);

    /**
     * Getter ministere attaché à l'acte
     *
     * @return String
     */
    String getMinistereAttache();

    void setMinistereAttache(String ministereAttache);

    /**
     * Getter direction attaché à l'acte
     *
     * @return String
     */
    String getDirectionAttache();

    void setDirectionAttache(String ministereAttache);

    /**
     * Getter isParapheurComplet
     *
     * @return Boolean
     */
    Boolean getIsParapheurComplet();

    /**
     * Setter isParapheurComplet
     *
     * @param isParapheurComplet
     */
    void setIsParapheurComplet(Boolean isParapheurComplet);

    /**
     * Getter nom Responsable Dossier
     *
     * @return String
     */
    String getNomRespDossier();

    void setNomRespDossier(String nomRespDossier);

    /**
     * Getter prenom Responsable Dossier
     *
     * @return String
     */
    String getPrenomRespDossier();

    void setPrenomRespDossier(String prenomRespDossier);

    /**
     * Getter qualite Responsable Dossier
     *
     * @return String
     */
    String getQualiteRespDossier();

    void setQualiteRespDossier(String qualite);

    /**
     * Getter telephone Responsable Dossier
     *
     * @return String
     */
    String getTelephoneRespDossier();

    void setTelephoneRespDossier(String telephoneRespDossier);

    /**
     * Getter mail Responsable Dossier
     *
     * @return String
     */
    String getMailRespDossier();

    void setMailRespDossier(String mailRespDossier);

    /**
     * Getter nom complet Responsable Dossier
     *
     * @return String
     */
    String getNomCompletRespDossier();

    void setNomCompletRespDossier(String nomCompletRespDossier);

    /**
     * Getter id createur dossier
     *
     * @return String
     */
    String getIdCreateurDossier();

    void setIdCreateurDossier(String idCreateurDossier);

    // /////////////////
    // info diverse property
    // ////////////////

    /**
     * Getter categorie de l'acte : réglementaire ou non réglementaire
     *
     * @return String
     */
    String getCategorieActe();

    void setCategorieActe(String categorieActe);

    String getCategorieActeLabel();

    void setCategorieActeLabel(String categorieActe);

    /**
     * Getter base légale de l'acte
     *
     * @return String
     */
    String getBaseLegaleActe();

    void setBaseLegaleActe(String baseLegaleActe);

    /**
     * Getter base légale : nature de texte
     *
     * @return String
     */
    String getBaseLegaleNatureTexte();

    void setBaseLegaleNatureTexte(String baseLegaleNatureTexte);

    /**
     * Getter base légale: numero du texte
     *
     * @return String
     */
    String getBaseLegaleNumeroTexte();

    void setBaseLegaleNumeroTexte(String baseLegaleNumeroTexte);

    /**
     * Getter base légale: date
     *
     * @return String
     */
    Calendar getBaseLegaleDate();

    void setBaseLegaleDate(Calendar baseLegaleDate);

    /**
     * Getter date de publication souhaitée
     *
     * @return Calendar
     */
    Calendar getDatePublication();

    void setDatePublication(Calendar datePublication);

    /**
     * Getter publication du rapport de presentation
     *
     * @return Boolean
     */
    Boolean getPublicationRapportPresentation();

    void setPublicationRapportPresentation(Boolean publicationRapportPresentation);

    // /////////////////
    // validation et signature
    // ////////////////

    /**
     * Getter nom complets (civilité nom prénom) des chargés de mission ayant valide le dossier
     *
     * @return List<String>
     */
    List<String> getNomCompletChargesMissions();

    void setNomCompletChargesMissions(List<String> nomCompletChargesMissions);

    /**
     * Getter nom complets (civilité nom prénom) des conseillers PM ayant valide le dossier
     *
     * @return List<String>
     */
    List<String> getNomCompletConseillersPms();

    void setNomCompletConseillersPms(List<String> nomCompletConseillersPms);

    /**
     * Getter date de signature alimenté automatiquement par exploitation de la feuille de style de l’acte
     *
     * @return Calendar
     */
    Calendar getDateSignature();

    void setDateSignature(Calendar dateSignature);

    // /////////////////
    // publication
    // ////////////////

    /**
     * Getter Pour Fourniture Epreuve facultatif
     *
     * @return Calendar
     */
    Calendar getDatePourFournitureEpreuve();

    void setDatePourFournitureEpreuve(Calendar pourFournitureEpreuve);

    /**
     * Getter vecteur publication
     *
     * @return List<String>
     */
    List<String> getVecteurPublication();

    void setVecteurPublication(List<String> vecteurPublication);

    /**
     * Getter publicationConjointe : référence des textes à publier conjointement
     *
     * @return List<String>
     */
    List<String> getPublicationsConjointes();

    void setPublicationsConjointes(List<String> publicationsConjointes);

    /**
     * Getter Unrestricted publicationConjointe : référence des textes à publier conjointement
     *
     * @param CoreSession
     * @return List<String>
     */
    List<String> getPublicationsConjointesUnrestricted(CoreSession session);

    /**
     * Setter Unrestricted publicationConjointe : référence des textes à publier conjointement
     *
     * @param CoreSession
     * @param List
     *            <String>
     */
    void setPublicationsConjointesUnrestricted(CoreSession session, List<String> publicationsConjointes);

    /**
     * Getter publicationIntegraleOuExtrait : "Intégrale" ou "Extrait"
     *
     * @return String
     */
    String getPublicationIntegraleOuExtrait();

    void setPublicationIntegraleOuExtrait(String publicationIntegraleOuExtrait);

    String getPublicationIntegraleOuExtraitLabel();

    void setPublicationIntegraleOuExtraitLabel(String publicationIntegraleOuExtrait);

    /**
     * Getter decretNumerote
     *
     * @return Boolean
     */
    Boolean getDecretNumerote();

    void setDecretNumerote(Boolean decretNumerote);

    /**
     * Getter delai Publication : A date précisée / Aucun / De rigueur / Urgent / Sans tarde/ Sous embargo
     *
     * @return String
     */
    String getDelaiPublication();

    void setDelaiPublication(String delaiPublication);

    String getDelaiPublicationLabel();

    void setDelaiPublicationLabel(String delaiPublication);

    /**
     * Getter Publication à date précisée
     *
     * @return Calendar
     */
    Calendar getDatePreciseePublication();

    void setDatePreciseePublication(Calendar datePreciseePublication);

    /**
     * Getter zone commentaire SGG-DILA
     *
     * @return String
     */
    String getZoneComSggDila();

    void setZoneComSggDila(String zoneComSggDila);

    // /////////////////
    // indexation
    // ////////////////

    /**
     * Getter rubrique : indexation de premier niveau
     *
     * @return List<String>
     */
    List<String> getIndexationRubrique();

    void setIndexationRubrique(List<String> indexationRubrique);

    /**
     * Getter motsCles : indexation de deuxième niveau
     *
     * @return List<String>
     */
    List<String> getIndexationMotsCles();

    void setIndexationMotsCles(List<String> indexationMotsCles);

    /**
     * Getter champ libre : indexation de troisième niveau
     *
     * @return List<String>
     */
    List<String> getIndexationChampLibre();

    void setIndexationChampLibre(List<String> indexationChampLibre);

    /**
     * Retourne vrai si le dossier est indexé pour IndexationSgg.
     *
     * @return Vrai si le dossier est indexé pour IndexationSgg
     */
    boolean isIndexationSgg();

    /**
     * Permet de renseigner si le dossier est indexé pour IndexationSgg.
     *
     * @param indexationSgg
     *            Vrai si le dossier est indexé pour IndexationSgg
     */
    void setIndexationSgg(boolean indexationSgg);

    /**
     * Retourne vrai si le dossier est indexé pour IndexationSggPub.
     *
     * @return Vrai si le dossier est indexé pour IndexationSggPub
     */
    boolean isIndexationSggPub();

    /**
     * Permet de renseigner si le dossier est indexé pour IndexationSggPub.
     *
     * @param indexationSggPub
     *            Vrai si le dossier est indexé pour IndexationSggPub
     */
    void setIndexationSggPub(boolean indexationSggPub);

    /**
     * Retourne vrai si le dossier est indexé pour IndexationMin.
     *
     * @return Vrai si le dossier est indexé pour IndexationMin
     */
    boolean isIndexationMin();

    /**
     * Permet de renseigner si le dossier est indexé pour IndexationMin.
     *
     * @param indexationMin
     *            Vrai si le dossier est indexé pour IndexationMin
     */
    void setIndexationMin(boolean indexationMin);

    /**
     * Retourne vrai si le dossier est indexé pour IndexationMinPub.
     *
     * @return Vrai si le dossier est indexé pour IndexationMinPub
     */
    boolean isIndexationMinPub();

    /**
     * Permet de renseigner si le dossier est indexé pour IndexationMinPub.
     *
     * @param indexationMinPub
     *            Vrai si le dossier est indexé pour IndexationMinPub
     */
    void setIndexationMinPub(boolean indexationMinPub);

    /**
     * Retourne vrai si le dossier est indexé pour IndexationDir.
     *
     * @return Vrai si le dossier est indexé pour IndexationDir
     */
    boolean isIndexationDir();

    /**
     * Permet de renseigner si le dossier est indexé pour IndexationDir.
     *
     * @param indexationDir
     *            Vrai si le dossier est indexé pour IndexationDir
     */
    void setIndexationDir(boolean indexationDir);

    /**
     * Retourne vrai si le dossier est indexé pour IndexationDirPub.
     *
     * @return Vrai si le dossier est indexé pour IndexationDirPub
     */
    boolean isIndexationDirPub();

    /**
     * Permet de renseigner si le dossier est indexé pour IndexationDirPub.
     *
     * @param indexationDirPub
     *            Vrai si le dossier est indexé pour IndexationDirPub
     */
    void setIndexationDirPub(boolean indexationDirPub);

    // /////////////////
    // transposition et application
    // ////////////////

    TranspositionsContainer getApplicationLoi();

    void setApplicationLoi(TranspositionsContainer applicationLoi);

    /**
     * Getter ordonnances
     *
     * @return List<String>
     */
    TranspositionsContainer getTranspositionOrdonnance();

    void setTranspositionOrdonnance(TranspositionsContainer applicationOrdonnances);

    /**
     * Getter Directives Europeennes
     *
     * @return List<String>
     */
    TranspositionsContainer getTranspositionDirective();

    void setTranspositionDirective(TranspositionsContainer directivesEuropeennes);

    List<String> getTranspositionDirectiveNumero();

    void setTranspositionDirectiveNumero(List<String> directivesEuropeennesNumero);

    /**
     * Getter Habilitations
     *
     */
    String getHabilitationRefLoi();

    void setHabilitationRefLoi(String habilitationRefLoi);

    String getHabilitationNumeroArticles();

    void setHabilitationNumeroArticles(String numeroArticles);

    String getHabilitationNumeroOrdre();

    void setHabilitationNumeroOrdre(String habilitationNumeroOrdre);

    String getHabilitationCommentaire();

    void setHabilitationCommentaire(String habilitationCommentaire);

    Boolean isDispositionHabilitation();

    void setDispositionHabilitation(Boolean dispositionHabilitation);

    Calendar getHabilitationDateTerme();

    void setHabilitationDateTerme(Calendar habilitationDateTerme);

    String getHabilitationTerme();

    void setHabilitationTerme(String habilitationTerme);

    void setTitle(String title);

    // /////////////////
    // Texte Signale
    // ////////////////

    Boolean getArriveeSolonTS();

    void setArriveeSolonTS(Boolean arriveeSolon);

    Calendar getDateVersementTS();

    void setDateVersementTS(Calendar dateVersementTS);

    Calendar getDateEnvoiJOTS();

    void setDateEnvoiJOTS(Calendar dateEnvoiJOTS);

    String getCreatorPoste();

    void setCreatorPoste(String creatorPoste);

    Calendar getDateCreation();

    void setDateCreation(Calendar dateCreation);

    String getLastContributor();

    String getAuthor();

    // //////////////////
    // idDossier MGPP //
    // //////////////////
    String getIdDossier();

    void setIdDossier(String idDossier);

    // Dénormalisation
    void setNorAttribue(Boolean norAttribue);

    void setPublie(Boolean publie);

    void setArchive(Boolean archive);

    Boolean isArchive();

    List<String> getApplicationLoiRefs();

    void setApplicationLoiRefs(List<String> applicationLoiRefs);

    void setTranspositionOrdonnanceRefs(List<String> applicationLoiRefs);

    List<String> getTranspositionOrdonnanceRefs();

    // periodicite du rapport (Rapport au parlement)
    String getPeriodiciteRapport();

    void setPeriodiciteRapport(String periodiciteRapport);

    String getPeriodicite();

    void setPeriodicite(String periodicite);

    /**
     * Renvoie vrai si on a déjà émis une demande de publication pour ce dossier
     *
     * @return vrai si demande de publication
     */
    Boolean isAfterDemandePublication();

    /**
     * Met en place un flag pour determiner si le dossier a déjà eu une demande de publication
     *
     * @param value
     */
    void setIsAfterDemandePublication(Boolean value);

    /**
     * indique si le dossier est adopté via le mgpp
     *
     * @param adoption
     */
    void setAdoption(Boolean adoption);

    /**
     * Getter pour savoir si le dossier est à supprimer
     */
    Boolean isDossierDeleted();

    /**
     * Setter pour savoir si le dossier est à supprimer
     */
    void setIsDossierDeleted(Boolean isDossierDeleted);

    /**
     * Getter pour savoir si le dossier est de type texte entreprise
     *
     * @return Boolean
     */
    Boolean getTexteEntreprise();

    void setTexteEntreprise(Boolean texteEntreprise);

    /**
     * Getter Texte entreprise date effet
     *
     * @return List<Calendar>
     */
    List<Calendar> getDateEffet();

    void setDateEffet(List<Calendar> dateEffet);

    // ///////////////////////////////
    // Informations Parlementaires //
    // /////////////////////////////

    String getEmetteur();

    void setEmetteur(String emetteur);

    String getRubrique();

    void setRubrique(String rubrique);

    String getCommentaire();

    void setCommentaire(String commentaire);

    String getIdEvenement();

    void setIdEvenement(String idEvenement);

    // //////////
    // Adamant //
    // //////////

    String getIdExtractionLot();
    void setIdExtractionLot(String idExtractionLot);

    static Dossier adapt(DocumentModel doc) {
        return doc.getAdapter(Dossier.class);
    }
}
