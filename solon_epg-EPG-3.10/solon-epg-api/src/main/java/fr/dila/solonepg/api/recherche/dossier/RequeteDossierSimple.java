package fr.dila.solonepg.api.recherche.dossier;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import fr.dila.st.api.domain.STDomainObject;

/**
 * 
 * L'objet métier d'une requête de dossier simple
 * 
 * @author jgomez
 * 
 */

public interface RequeteDossierSimple extends STDomainObject,Serializable {

 /** Les méthodes utilisées pour les traitements **/
    
    /**
     *  Initialisation des champs de recherche
     */
    void init();
    
    /**
     * Post traitement des champs juste avant de lancer la recherche
     */
    void doBeforeQuery();
    
    /**
     * Type de requête : NXQL ou UFNXQL
     */
    String getQueryType();
    
/*** Les méta-données du volet critères principaux ***/
    
    /**
     * Met en place les identifiants des statuts de dossier à rechercher
     */
    void setIdStatutDossier(List<String> idStatutDossier);
    
    /**
     * Retourne les identifiants des statuts de dossier
     * @return
     */
    List<String> getIdStatutDossier();
    
    /**
     * Met en place le numéro de NOR à rechercher
     * @param numeroNor
     */
    void setNumeroNor(String numeroNor);
    
    /**
     * Retourne le numéro de NOR à rechercher
     * @return
     */
    String getNumeroNor();
    
    /**
     * Met en place le numéro de NOR modifié, utilisé en interne pour faire des recherches, sans modifié le champ de l'utilisateur 
     * @param numeroNor
     */
    void setNumeroNorModified(String numeroNor);
    
    /**
     * Retourne le numéro de NOR modifié à rechercher
     * @return
     */
    String getNumeroNorModified();
    
    /**
     * Met en place l'identifiant de l'auteur à rechercher
     * @param idAuteur
     */
    void setIdAuteur(String idAuteur);
    
    /**
     * Retourne l'identifant de l'auteur à rechercher
     * @return
     */
    String getIdAuteur();
    
    /**
     * Met en place les identifiants des ministères reponsables à rechercher
     * @param idMinistereResponsable
     */
    void setIdMinistereResponsable(List<String> idMinistereResponsable);
    
    /**
     * Retourne les identifiants des ministères responsables à rechercher
     * @return
     */
    List<String> getIdMinistereResponsable();
    
    /**
     * Met en place l'identifiant de la direction reponsable à rechercher
     * @param idDirectionResponsable
     */
    void setIdDirectionResponsable(String idDirectionResponsable);
    
    /**
     * Retourne la direction responsable à rechercher
     * @return
     */
    String getIdDirectionResponsable();
    
/*** Les méta-données du volet critères secondaires ***/
    
    /**
     * Retourne l'identifiant de la catégorie de l'acte
     */
    String getIdCategoryActe();
    
    /**
     * Met en place l'identifiant de la catégorie de l'acte à rechercher
     * @param idCategoryActe
     */
    void setIdCategorieActe(String idCategoryActe);
    
    /**
     * Retourne la directive de transposition à rechercher 
     */
    String getTranspositionDirective();
    
    /**
     * Met en place la directive de transposition à recherche
     * @param transpositionDirective
     */
    void setTranspositionDirective(String transpositionDirective);    
    
    /**
     * Retourne l'ordonnance de transposition à rechercher 
     */
    String getTranspositionOrdonnance();
    
    /**
     * Met en place l'ordonnance de transposition à rechercher
     * @param transpositionOrdonnance
     */
    void setTranspositionOrdonnance(String transpositionOrdonnance);
    
    /**
     * Retourne la loi d'application à rechercher 
     */
    String getApplicationLoi();
    
    /**
     * Met en place le champ relatif à la loi d'application à rechercher
     * @param applicationLoi
     */
    void setApplicationLoi(String applicationLoi);
    
    /**
     * Retourne la rubrique d'indexation à rechercher 
     */
    String getIndexationRubrique();
    
    /**
     * Met en place la rubrique d'indexation à rechercher
     * @param applicationLoi
     */
    void setIndexationRubrique(String indexationRubrique);

    /**
     * Retourne la rubrique d'indexation utilisé par la recherche.
     */
    String getIndexationRubriqueModified();
    
    /**
     * Met en place la rubrique d'indexation utilisé par la recherche.
     * @param applicationLoi
     */
    void setIndexationRubriqueModified(String indexationRubrique);
    
    /**
     * Retourne la chaîne de caractères à rechercher dans l'indexation des mots-clef  
     */
    String getIndexationMotsClefs();
    
    /**
     * Met en place la chaîne de caractères à rechercher dans l'indexation des mots-clef 
     * @param indexationMotsClefs
     */
    void setIndexationMotsClefs(String indexationMotsClef);

    /**
     * Retourne la chaîne de caractères dans l'indexation des mots-clef utilisé par la recherche.
     */
    String getIndexationMotsClefsModified();
    
    /**
     * Récupère la chaîne de caractères dans l'indexation des mots-clef utilisé par la recherche.
     * @param indexationMotsClefs
     */
    void setIndexationMotsClefsModified(String indexationMotsClef);
    
    /**
     * Retourne la chaîne de caractères à rechercher  
     */
    String getIndexationChampLibre();
    
    /**
     * Met en place la chaîne de caractères à rechercher 
     * @param indexationMotsClefs
     */
    void setIndexationChampLibre(String indexationChampLibre);
 
    /**
     * Retourne la chaîne de caractères utilisée par la recherche
     */
    String getIndexationChampLibreModified();
    
    /**
     * Récupère la chaîne de caractères utilisée par la recherche
     * @param indexationMotsClefs
     */
    void setIndexationChampLibreModified(String indexationChampLibre);

    /**
     * Retourne la date de signature 1 (celle de début ?) 
     */
    Calendar getDateSignature_1();
    
    /**
     * Met en place la date de signature 1
     * @param dateSignature_1
     */
    void setDateSignature_1(Calendar dateSignature_1);    

    /**
     * Retourne la date de signature 2 (celle de fin ?) 
     */
    Calendar getDateSignature_2();
    
    /**
     * Met en place la date de signature 2
     * @param dateSignature_2
     */
    void setDateSignature_2(Calendar dateSignature_2);
    
    /**
     * Retourne la date de publication 1 (celle de début ?) 
     */
    Calendar getDatePublication_1();
    
    /**
     * Met en place la date de signature 1
     * @param dateSignature_1
     */
    void setDatePublication_1(Calendar datePublication_1);    

    /**
     * Retourne la date de publication 2 (celle de fin ?) 
     */
    Calendar getDatePublication_2();
    
    /**
     * Met en place la date de publication 2
     * @param datePublication_2
     */
    void setDatePublication_2(Calendar datePublication_2);    

    /**
     * Retourne le numéro de texte à rechercher
     */
    String getNumeroTexte();
    
    /**
     * Met en place le numéro de texte à rechercher
     * @param numeroTexte
     */
    void setNumeroTexte(String numeroTexte);
    
/*** Les méta-données du volet critères étapes ***/
    
    /**
     * Retourne l'identifiant d'action à rechercher parmi les étapes
     */
    String getEtapeIdAction();
    
    /**
     * Met en place l'identifiant d'action à rechercher parmi les étapes
     * @param idAction
     */
    void setEtapeIdAction(String idAction);
    
    /**
     * Retourne l'identifiant de statut à rechercher parmi les étapes
     */
    String getEtapeIdStatut();
    
    /**
     * Met en place  l'identifiant de statut à rechercher parmi les étapes
     * @param idAction
     */
    void setEtapeIdStatut(String idStatut);   
    
    /**
     * Retourne la date d'activation 1 pour la recherche sur les étapes
     */
    Calendar getEtapeDateActivation_1();
    
    /**
     * Met en place la date d'activation 1 pour la recherche sur les étapes
     * @param dateActivation_1
     */
    void setEtapeDateActivation_1(Calendar dateActivation1);    
    
    /**
     * Retourne la date d'activation 2 pour la recherche sur les étapes
     */
    Calendar getEtapeDateActivation_2();
    
    /**
     * Met en place la date d'activation 2 pour la recherche sur les étapes
     * @param dateActivation_2
     */
    void setEtapeDateActivation_2(Calendar dateActivation2);
    
    /**
     * Retourne la date de validation 1 pour la recherche sur les étapes
     */
    Calendar getEtapeDateValidation_1();
    
    /**
     * Met en place la date de validation 1 pour la recherche sur les étapes
     * @param dateActivation_1
     */
    void setEtapeDateValidation_1(Calendar dateValidation1);    
    
    /**
     * Retourne la date de validation 2 pour la recherche sur les étapes
     */
    Calendar getEtapeDateValidation_2();
    
    /**
     * Met en place la date de validation 2 pour la recherche sur les étapes
     * @param dateValidation_2
     */
    void setEtapeDateValidation_2(Calendar dateValidation2);    
    
    /**
     * Retourne les identifiants des postes à rechercher sur les étapes
     */
    List<String> getEtapeIdPoste();
    
    /**
     * Met en place les identifiants de poste à rechercher sur les étapes
     * @param idPoste
     */
    void setEtapeIdPoste(List<String> idPoste);
    
    /**
     * Retourne les identifiants de mailbox à rechercher sur les étapes
     */
    List<String> getEtapeDistributionMailboxId();
    
    /**
     * Met en place les identifiants de mailbox à rechercher sur les étapes
     * @param idMailboxId
     */
    void setEtapeDistributionMailboxId(List<String> idMailbox);
    
    /**
     * Retourne l'identifiant d'auteur à rechercher sur les étapes
     */
    String getEtapeIdAuteur();
    
    /**
     * Met en place l'identifiant d'auteur à rechercher sur les étapes
     * @param idPoste
     */
    void setEtapeIdAuteur(String idAuteur);    
    
    /**
     * Retourne l'expression de l'utilisateur pour les notes d'étapes
     */
    String getEtapeNote();
    
    /**
     * Met en place l'expression l'utilisateur pour les notes d'étapes
     * @param note
     */
    void setEtapeNote(String note);
    
    /**
     * Retourne l'expression à rechercher dans les notes d'étapes
     */
    String getEtapeNoteModified();
    
    /**
     * Met en place l'expression à rechercher dans les notes d'étapes
     * @param note
     */
    void setEtapeNoteModified(String note);
    
    
/*** Les méta-données du texte intégral ***/
    
    /**
     * Retourne l'expression à rechercher dans le texte des dossiers
     */
    String getTexteIntegral();
    
    /**
     * Met en place l'expression à rechercher dans le texte des dossiers
     * @param texte
     */
    void setTexteIntegral(String texte);
    
    /**
     * Retourne vrai si la recherche dans le texte est à effectuer dans les actes
     */
    Boolean getTIDansActe();
    
    /**
     * Mettre le paramêtre à vrai si la recherche dans le texte est à effectuer dans les actes
     * @param dansActe
     */
    void setTIDansActe(Boolean dansActe);
    
    /**
     * Retourne vrai si la recherche dans le texte est à effectuer dans les extraits
     */
    Boolean getTIDansExtrait();
    
    /**
     * Mettre le paramêtre à vrai si la recherche dans le texte est à effectuer dans les extraits
     * @param dansExtrait
     */
    void setTIDansExtrait(Boolean dansExtrait);
    
    /**
     * Retourne vrai si la recherche dans le texte est à effectuer dans les autres pièces du parapheur
     */
    Boolean getTIDansAutrePieceParapheur();
    
    /**
     * Mettre le paramêtre à vrai si la recherche dans le texte est à effectuer dans les autres pièces du parapheur
     * @param dansAutrePieceParapheur
     */
    void setTIDansAutrePieceParapheur(Boolean dansAutrePieceParapheur);
    
    /**
     * Retourne vrai si la recherche dans le texte est à effectuer dans les autres pièces du parapheur
     */
    Boolean getTIDansFondDossier();
    
    /**
     * Mettre le paramêtre à vrai si la recherche dans le texte est à effectuer dans le fond de dossier
     * @param dansFondDossier
     */
    void setTIDansFondDossier(Boolean dansFondDossier);

    /**
     * Placer la sous-clause qui contient la partie de code UFNXQL nécessaire
     * à la recherche sur les index fulltext
     * @param rawSubClause
     */
    void setSousClauseTexteIntegral(String rawSubClause);

    /**
     * Retourne la sous-clause UFNXQL concernant la recerche fulltext.
     * @return
     */
    String getSousClauseTexteIntegral();

    /**
     * Retourne le statut d'archivage sur lequel porte la recherche
     * @return
     */
    Integer getIdStatutArchivageDossier();

    /**
     * Met en place le statut d'archivage.
     * @param idStatutArchivageDossier
     */
    void setIdStatutArchivageDossier(Integer idStatutArchivageDossier);

    /**
     * 
     * @param etapeCurrentCycleState
     */
    void setEtapeCurrentCycleState(String etapeCurrentCycleState);

    /**
     * 
     * @param validationStatutId
     */
    void setValidationStatutId(String validationStatutId);

    /**
     * Met en place le titre de l'acte
     * @param titreActe Titre de l'acte du dossier
     */
    void setTitreActe(String titreActe);

    /**
     * Retourne le titre de l'acte
     * @param titreActe
     * @return
     */
    String getTitreActe();

    /**
     * Met en place le titre de l'acte modifié utilisé par la recherche
     * @param titreActeModified Titre de l'acte modifié
     */
    void setTitreActeModified(String titreActeModified);

    /**
     * Retourne le titre de l'acte modifié utilisé par la recherche.
     * @param titreActeModified  Titre de l'acte modifié
     * @return
     */
    String getTitreActeModifie();

    /**
     * Retourne la liste des types de fichiers utilisée par la recherche
     * 
     * @return la liste des types de fichiers
     */
    List<String> getFiletypeIds();

    /**
     * met en place les types d'acte utilisés par la recherche
     * @param idTypeActe
     */
	void setIdTypeActe(List<String> idTypeActe);

	/**
	 * Retoure les types d'acte utilisés par la recherche
	 * @return
	 */
	List<String> getIdTypeActe();

    
}
