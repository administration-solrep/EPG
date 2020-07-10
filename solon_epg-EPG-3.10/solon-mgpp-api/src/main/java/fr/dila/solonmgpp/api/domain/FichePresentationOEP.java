package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface de representation d'une fiche presentation d'un OEP
 * 
 * @author asatre
 * 
 */
public interface FichePresentationOEP {

	public static final String	DOC_TYPE						= "FichePresentationOEP";
	public static final String	SCHEMA							= "fiche_presentation_oep";
	public static final String	PREFIX							= "fpoep";

	public static final String	ID_DOSSIER						= "idDossier";
	public static final String	MINISTERE_RATTACHEMENT			= "ministereRattachement";
	public static final String	MINISTERE_RATTACHEMENT_2		= "ministereRattachement2";
	public static final String	MINISTERE_RATTACHEMENT_3		= "ministereRattachement3";
	public static final String	TEXTE_REF						= "texteRef";
	public static final String	CHARGE_MISSION					= "chargeMission";
	public static final String	INFORMER_CHARGE_MISSION			= "informerChargeMission";
	public static final String	NATURE_JURIDIQUE				= "natureJuridiqueOrganisme";
	public static final String	COMMISSION_DU_DECRET2006		= "commissionDuDecret2006";
	public static final String	IS_SUIVIE_PAR_DQD				= "isSuivieDQD";
	public static final String	IS_SUIVIE_PAR_APP_SUIVI_MANDATS	= "isSuivieAppSuiviMandats";
	public static final String	MOTIF_FIN						= "motifFinOEP";
	public static final String	IDS_ANAT_LIES					= "idsANATLies";

	// Coordonnées
	public static final String	ADRESSE							= "adresse";
	public static final String	TEL								= "tel";
	public static final String	FAX								= "fax";
	public static final String	MAIL							= "mail";

	// Durée mandat
	public static final String	DUREE_MANDAT_AN					= "dureeMandatAN";
	public static final String	RENOUVELABLE_AN					= "renouvelableAN";
	public static final String	NB_MANDATS_AN					= "nbMandatsAN";
	public static final String	DUREE_MANDAT_SE					= "dureeMandatSE";
	public static final String	RENOUVELABLE_SE					= "renouvelableSE";
	public static final String	NB_MANDATS_SE					= "nbMandatsSE";

	public static final String	ID_ORGANISME_EPP				= "idOrganismeEPP";
	public static final String	NOM_ORGANISME					= "nomOrganisme";
	public static final String	ID_COMMUN						= "idCommun";

	public static final String	NB_DEPUTE						= "nbDeputes";
	public static final String	NB_SENATEUR						= "nbSenateurs";

	public static final String	TEXTE_DUREE						= "texteDuree";
	public static final String	COMMENTAIRE						= "commentaire";

	public static final String	DATE							= "date";
	public static final String	DATE_FIN						= "dateFin";
	public static final String	DATE_DIFFUSION					= "dateDiffusion";

	public static final String	NOM_INTERLOCUTEUR_REF			= "nomInterlocuteurRef";
	public static final String	FONCTION_INTERLOCUTEUR_REF		= "fonctionInterlocuteurRef";

	public static final String	DIFFUSE							= "diffuse";

	String getIdDossier();

	void setIdDossier(String idDossier);

	String getMinistereRattachement();

	void setMinistereRattachement(String ministereRattachement);

	String getMinistereRattachement2();

	void setMinistereRattachement2(String ministereRattachement2);

	String getMinistereRattachement3();

	void setMinistereRattachement3(String ministereRattachement3);

	String getTexteRef();

	void setTexteRef(String texteRef);

	List<String> getChargesMission();

	void setChargesMission(List<String> charges);

	boolean informeChargeMission();

	void setInformeChargeMission(boolean informe);

	String getNatureJuridique();

	void setNatureJuridique(String nature);

	boolean isCommissionDuDecret2006();

	void setCommissionDuDecret2006(boolean commission);

	boolean isSuivieParDQD();

	void setSuivieParDQD(boolean suivi);

	boolean isSuivieParAppSuiviMandats();

	void setSuivieparAppSuiviMandats(boolean suivi);

	String getMotifFin();

	void setMotifFin(String motif);

	String getAdresse();

	void setAdresse(String adresse);

	String getTel();

	void setTel(String tel);

	String getFax();

	void setFax(String fax);

	String getMail();

	void setMail(String mail);

	String getDureeMandatAN();

	void setDureeMandatAN(String dureeMandatAN);

	boolean isRenouvelableAN();

	void setRenouvelableAN(boolean renouvelable);

	Long getNbMandatsAN();

	void setNbMandatsAN(Long nbMandats);

	String getDureeMandatSE();

	void setDureeMandatSE(String dureeMandatSE);

	boolean isRenouvelableSE();

	void setRenouvelableSE(boolean renouvelable);

	Long getNbMandatsSE();

	void setNbMandatsSE(Long nbMandats);

	String getIdOrganismeEPP();

	void setIdOrganismeEPP(String idOrganismeEPP);

	DocumentModel getDocument();

	String getNomOrganisme();

	void setNomOrganisme(String nomOrganisme);

	Calendar getDate();

	void setDate(Calendar date);

	String getIdCommun();

	void setIdCommun(String idCommun);

	Long getNbDepute();

	void setNbDepute(Long nbDepute);

	Long getNbSenateur();

	void setNbSenateur(Long nbSenateur);

	String getTexteDuree();

	void setTexteDuree(String texteDuree);

	String getCommentaire();

	void setCommentaire(String commentaire);

	String getNomInterlocuteurRef();

	void setNomInterlocuteurRef(String nomInterlocuteurRef);

	String getFonctionInterlocuteurRef();

	void setFonctionInterlocuteurRef(String fonctionInterlocuteurRef);

	Calendar getDateFin();

	void setDateFin(Calendar dateFin);

	Calendar getDateDiffusion();

	void setDateDiffusion(Calendar dateDiffusion);

	boolean isDiffuse();

	void setDiffuse(boolean diffuse);

	public String getIdsANATLies();

	public void addToIdsANATLies(String anatId);

	List<String> getListOfIdsANATLies();

}
