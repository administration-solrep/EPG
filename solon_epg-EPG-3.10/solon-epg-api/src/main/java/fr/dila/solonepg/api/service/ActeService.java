package fr.dila.solonepg.api.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.dila.solonepg.api.constant.TypeActe;

public interface ActeService extends Serializable {

	TypeActe getActe(String typeActeId);

	TypeActe getActeByLabel(String label);

	Set<String> getLoiList();

	/**
	 * Test si le type d'acte correspond à une loi
	 * 
	 * @param typeActe
	 * @return
	 */
	boolean isLoi(String typeActe);

	/**
	 * Test si le type d'acte correspond à une ordonannce
	 * 
	 * @param typeActe
	 * @return
	 */
	boolean isOrdonnance(String typeActe);

	/**
	 * Test si le type d'acte correspond à un arrete
	 * 
	 * @param typeActe
	 * @return
	 */
	boolean isArrete(String typeActe);

	/**
	 * Test si le type d'acte correspond à un decret
	 * 
	 * @param typeActe
	 * @return
	 */
	boolean isDecret(String typeActe);

	/**
	 * Test si le type d'acte correspond a un type Non reglementaire
	 * 
	 * @param typeActe
	 * @return
	 */
	boolean isNonReglementaire(String typeActe);

	/**
	 * Teste si le type d'acte correspond à un type Convention collective
	 * 
	 * @param typeActe
	 * @return
	 */
	public boolean isConventionCollective(String typeActe);

	/**
	 * Teste si le type d'acte correspond à un rectificatif
	 * 
	 * @param typeActe
	 * @return
	 */
	boolean isRectificatif(String typeActe);

	/**
	 * Liste les types d'actes qui ont des répertoires spécifiques par défaut dans les modèles de fond de dossier.
	 */
	Set<String> getTypeActeListExtendFolderFondDeDossier();

	/**
	 * Renvoie vrai si le type d'acte a des répertoires additionels. type acte concerné : amnistie, décret CE art.37
	 * ,décret CE, décret CE CM, dde avis CE, loi, loi constitutionnelle, loi article 53 de la constitution, ordonnance
	 * 
	 * @param typeActe
	 * @return boolean
	 */
	boolean hasTypeActeExtendedFolder(String typeActe);

	/**
	 * Liste les types d'actes qui ont des format de fichier défini par défaut dans les modèles de parapheur.
	 */
	Set<String> getTypeActeListExtendFormatParapheur();

	/**
	 * Renvoie vrai si le type d'acte a un format de fichier autorisé par défault (.doc) type acte concerné : acte de
	 * type loi : loi, loi constitutionnelle, loi article 53 de la constitution, loi organique
	 * 
	 * @param typeActe
	 * @return boolean
	 */
	boolean hasTypeActeExtendedFormat(String typeActe);

	/**
	 * Renvoie vrai si le dossier ayant le type d'acte choisi est considéré comme une mesure nominative
	 * 
	 * @param typeActe
	 * @return boolean
	 */
	boolean hasTypeActeMesureNominative(String typeActe);

	/**
	 * Renvoie vrai si le dossier ayant le type d'acte choisi doit avoir un extrait pour être lancé
	 * 
	 * @param typeActe
	 * @return boolean
	 */
	boolean hasExtraitObligatoire(String typeActe);

	/**
	 * Renvoie vrai si le dossier a un type d'acte correspondant à l'injection.
	 * 
	 * @param typeActe
	 * @return boolean
	 */
	boolean hasInjectionType(String typeActe);

	/**
	 * Renvoie vrai si le type d'acte contient des données CE
	 * 
	 * @param typeActe
	 * 
	 * @return vrai si le type d'acte contient des données CE
	 */
	boolean isVisibleCE(String typeActe);

	/**
	 * Renvoie vrai si la base légale est visible
	 * 
	 * @param typeActe
	 * 
	 * @return vrai si le type d'acte peut contenir le champ base légale
	 */
	boolean isVisibleBaseLegale(String typeActe);

	/**
	 * Renvoie vrai si les champs d'applications des lois sont visibles
	 * 
	 * @param typeActe
	 * 
	 * @return vrai si le type d'acte peut contenir les champs d'applications des lois sont visibles
	 */
	boolean isVisibleApplicationLoi(String typeActe);

	/**
	 * Renvoie vrai si les champs de transposition des ordonnances sont visibles
	 * 
	 * @param typeActe
	 * 
	 * @return vrai si le type d'acte peut contenir les champs de transposition des ordonnances sont visibles
	 */
	boolean isVisibleTranspositionOrdonnance(String typeActe);

	/**
	 * Renvoie vrai si les champs de l'habilitation sont visibles
	 * 
	 * @param typeActe
	 * 
	 * @return vrai si le type d'acte peut contenir les champs de l'habilitation sont visibles
	 */
	boolean isVisibleHabilitation(String typeActe);

	/**
	 * Renvoie vrai si les champs de transpositions des directives sont visibles
	 * 
	 * @param typeActe
	 * 
	 * @return vrai si le type d'acte peut contenir les champs de transpositions des directives sont visibles
	 */
	boolean isVisibleTranspositionDirective(String typeActe);

	/**
	 * Renvoie vrai si le champ PublicationRapport est visible
	 * 
	 * @param typeActe
	 * 
	 * @return vrai si le champ PublicationRapport est visible
	 */
	boolean isVisiblePublicationRapport(String typeActe);

	/**
	 * Renvoie vrai si le champ PublicationIntegrale est visible
	 * 
	 * @param typeActe
	 * 
	 * @return vrai si le champ PublicationIntegrale est visible
	 */
	boolean isVisiblePublicationIntegrale(String typeActe);

	/**
	 * Renvoie vrai si le champ DecretNumerote est visible
	 * 
	 * @param typeActe
	 * 
	 * @return vrai si le champ DecretNumerote est visible
	 */
	boolean isVisibleDecretNumerote(String typeActe);

	/**
	 * Renvoie vrai si le champ NumeroTexteJO est visible
	 * 
	 * @param typeActe
	 * 
	 * @return vrai si le champ NumeroTexteJO est visible
	 */
	boolean isVisibleNumeroTexteJO(String typeActe);

	/**
	 * Renvoie la liste des types d'actes de SolonEpg trié dans l'ordre alphabétique : utilisé pour l'affichage des
	 * feuilles de style dans la jsp display_feuilles_styles.jsp
	 * 
	 * @return la liste des types d'actes de SolonEpg trié dans l'ordre alphabétique
	 */
	public List<TypeActe> getAllTypeActe();

	/**
	 * 
	 * @param typeActe
	 * @return true si decret du Président de la République, false sinon
	 */
	boolean isDecretPR(String typeActe);

	/**
	 * 
	 * @param typeActe
	 * @return true si depot rapport
	 */
	Boolean isRapportAuParlement(String typeActe);

	/**
	 * Map de visibilité pour les élements du bordereau
	 * 
	 * @param typeActe
	 * @return
	 */
	Map<String, Boolean> getActeVisibility(String typeActe);

	/**
	 * Retourne la liste complete des types Acte
	 * 
	 * @return
	 */
	Collection<TypeActe> findAll();

	/**
	 * Vérifie si le type d'acte est celui des textes non publié
	 * 
	 * @param typeActe
	 * @return
	 */
	Boolean isActeTexteNonPublie(String typeActe);

	/**
	 * Vérifie si le type d'acte affiche la partie 'publication' dans le bordereau
	 * 
	 * @param typeActe
	 * @return
	 */
	Boolean isPublicationVisible(String typeActe);

	/**
	 * Vérifie si le type d'acte affiche la partie 'SGG-DILA' dans le bordereau
	 * 
	 * @param typeActe
	 * @return
	 */
	Boolean isSggDilaVisible(String typeActe);

	/**
	 * Vérifie si le type d'acte affiche la partie 'Parution' dans le bordereau
	 * 
	 * @param typeActe
	 * @return
	 */
	Boolean isParutionVisible(String typeActe);

	/**
	 * Vérifie si le type d'acte affiche la partie 'Parution JORF' dans le bordereau
	 * 
	 * @param typeActe
	 * @return
	 */
	Boolean isJorfVisible(String typeActe);

	/**
	 * Vérifie si le type d'acte est celui des informations parlementaires
	 * 
	 * @param typeActe
	 * @return
	 */
	Boolean isInformationsParlementaires(String typeActe);

	/**
	 * Renvoie vrai si le type d'acte a le répertoire saisine rectificative dans fond de dossier. Sont concernés : Loi,
	 * Ordonnance, Décrèt en conseil d'état, décrèt en conseil d'état (individuel), décrèt en conseil d'état de
	 * l'article 37, décrèt en conseil d'état et conseil ministre, décrèt conseil état et conseil ministre individuel,
	 * arrêté conseil d'état
	 * 
	 * @param typeActe
	 * @return boolean
	 */
	boolean hasTypeActeSaisineRectificative(String typeActe);
}
