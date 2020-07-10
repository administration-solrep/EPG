package fr.dila.solonmgpp.web.suivi;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonmgpp.api.domain.ActiviteParlementaire;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.SemaineParlementaire;
import fr.dila.solonmgpp.api.service.SuiviService;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.fichepresentation.FicheLoiActionsBean;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;

/**
 * 
 * @author aRammal
 */

@Name("calendrierParlementaireActions")
@Scope(ScopeType.CONVERSATION)
public class CalendrierParlementaireActionsBean implements Serializable {

	private static final long										serialVersionUID							= 1L;

	@In(create = true, required = false)
	protected transient FacesMessages								facesMessages;

	@In(create = true, required = true)
	protected transient CoreSession									documentManager;

	@In(create = true, required = true)
	protected transient FicheLoiActionsBean							ficheLoiActions;

	@In(create = true, required = false)
	protected transient NavigationContextBean						navigationContext;

	public static final String										ACTIVITE_PARLEMENTAIRE_DIRECTORY			= "activite_parlementaire";
	public static final String										ASSEMBLEE_DIRECTORY							= "assemblee";
	public static final String										ORIENTATION_DIRECTORY						= "orientation";

	private String													activiteId									= null;
	private String													loiId										= null;
	private String													assembleeId									= null;
	private String													orientationId								= null;
	private Date													dateDeDebut									= null;
	private Date													dateDeFin									= null;
	private Date													dateDeDebutFiltre							= null;
	private Date													dateDeFinFiltre								= null;

	private Map<String, String>										listeDesActivites							= null;
	private Map<String, String>										listeDesLois								= null;

	private Map<String, String>										listeDesAssemblees							= null;
	private Map<String, String>										listeDesOrientations						= null;

	private int														ligneParPage								= 5;
	private int														numeroDeLaPageActuelle						= 1;
	private int														nombreDePages								= 1;
	private boolean													pageSuivantDisponible						= false;
	private boolean													pagePrecedentDisponible						= false;

	private long													counter										= 0;

	// private List<DocumentModel> listeDesLoisDoc = new ArrayList<DocumentModel>();
	private List<FicheLoi>											listeDeToutLesLois							= new ArrayList<FicheLoi>();
	List<FicheLoi>													pageDeLaListeDeToutLesLois					= new ArrayList<FicheLoi>();
	private List<Date>												listeDeToutesLesDatesDesActivites			= null;
	private Map<String, Collection<List<ActiviteParlementaire>>>	mapDeToutesLesActivitesParlementaireParLoi	= null;
	private Map<String, List<Map<String, String>>>					listedeSemainesParTypes						= null;
	private final String[]											daysAbrv									= {
			"Dim", "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam"													};
	
	private boolean 												activiteAjoute							= false;
	private boolean 												semaineAjoute							= false;

	public String getActiviteId() {
		return activiteId;
	}

	public void setActiviteId(String activiteId) {
		this.activiteId = activiteId;
	}

	public String getLoiId() {
		return loiId;
	}

	public void setLoiId(String loiId) {
		this.loiId = loiId;
	}

	public String getAssembleeId() {
		return assembleeId;
	}

	public void setAssembleeId(String assembleeId) {
		this.assembleeId = assembleeId;
	}

	public String getOrientationId() {
		return orientationId;
	}

	public void setOrientationId(String orientationId) {
		this.orientationId = orientationId;
	}

	public Date getDateDeDebut() {
		return DateUtil.copyDate(dateDeDebut);
	}

	public void setDateDeDebut(Date dateDeDebut) {
		this.dateDeDebut = DateUtil.copyDate(dateDeDebut);
	}

	public Date getDateDeFin() {
		return DateUtil.copyDate(dateDeFin);
	}

	public void setDateDeFin(Date dateDeFin) {
		this.dateDeFin = DateUtil.copyDate(dateDeFin);
	}

	public Map<String, String> getListeDesActivites() throws ClientException {
		// long start = System.nanoTime();
		if (listeDesActivites == null) {
			listeDesActivites = new HashMap<String, String>();
			DocumentModelList documentModelList = STServiceLocator.getVocabularyService().getAllEntry(
					ACTIVITE_PARLEMENTAIRE_DIRECTORY);

			for (DocumentModel documentModel : documentModelList) {
				String ident = (String) documentModel.getProperty(STVocabularyConstants.COLUMN_ID).getValue();
				String label = (String) documentModel.getProperty(STVocabularyConstants.COLUMN_LABEL).getValue();

				listeDesActivites.put(ident, label);
			}
		}

		// long end = System.nanoTime();
		// counter += (end - start) / 1000000;
		// System.out.println("getListeDesActivites() a pris " + (end-start) + " ms\t" + "counter is now "+counter);
		return listeDesActivites;
	}

	public Map<String, String> getListeDesLois() {
		return listeDesLois;
	}

	public Map<String, String> refreshListeDesLois() throws ClientException {
		// long start = System.nanoTime();

		listeDesLois = new HashMap<String, String>();

		SuiviService suiviService = SolonMgppServiceLocator.getSuiviService();
		listeDesLois = suiviService.getTexteLoiEnCoursDiscussionNonAdoptee2(documentManager);

		// long end = System.nanoTime();
		// counter += (end - start) / 1000000;
		// System.out.println("refreshListeDesLois() a pris " + (end - start) / 1000000 + " ms\t" + "counter is now "
		// + counter);

		return listeDesLois;
	}

	public Map<String, String> getListeDesAssemblees() throws ClientException {
		// long start = System.nanoTime();
		if (listeDesAssemblees == null) {
			listeDesAssemblees = new HashMap<String, String>();
			DocumentModelList documentModelList = STServiceLocator.getVocabularyService().getAllEntry(
					ASSEMBLEE_DIRECTORY);

			for (DocumentModel documentModel : documentModelList) {
				String ident = (String) documentModel.getProperty(STVocabularyConstants.COLUMN_ID).getValue();
				String label = (String) documentModel.getProperty(STVocabularyConstants.COLUMN_LABEL).getValue();

				listeDesAssemblees.put(ident, label);
			}
		}

		// long end = System.nanoTime();
		// counter += (end - start) / 1000000;
		// System.out.println("getListeDesAssemblees() a pris " + (end - start) / 1000000 + " ms\t" + "counter is now "
		// + counter);

		return listeDesAssemblees;
	}

	public Map<String, String> getListeDesOrientations() throws ClientException {
		// long start = System.nanoTime();
		if (listeDesOrientations == null) {
			listeDesOrientations = new HashMap<String, String>();
			DocumentModelList documentModelList = STServiceLocator.getVocabularyService().getAllEntry(
					ORIENTATION_DIRECTORY);

			for (DocumentModel documentModel : documentModelList) {
				String ident = (String) documentModel.getProperty(STVocabularyConstants.COLUMN_ID).getValue();
				String label = (String) documentModel.getProperty(STVocabularyConstants.COLUMN_LABEL).getValue();

				listeDesOrientations.put(ident, label);
			}
		}

		// long end = System.nanoTime();
		// counter += (end - start) / 1000000;
		// System.out.println("getListeDesOrientations() a pris " + (end - start) / 1000000 + " ms\t" +
		// "counter is now "
		// + counter);

		return listeDesOrientations;
	}

	public boolean ajouterUneActivite() throws ClientException {
		if ((loiId != null) && (activiteId != null) && (assembleeId != null) && (dateDeDebut != null)) {
			if (dateDeFin == null) {
				setDateDeFin(dateDeDebut);
			}
			if (dateDeFin.compareTo(dateDeDebut) >= 0) {
				SuiviService suiviService = SolonMgppServiceLocator.getSuiviService();

				for (Date date = dateDeDebut; date.compareTo(dateDeFin) <= 0;) {
					suiviService.creerActiviteParlementaire(documentManager, loiId, activiteId, assembleeId, date);

					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.add(Calendar.DATE, 1);
					date = cal.getTime();
				}
				activiteAjoute = true;
			} else {
				return false;
			}
		}
		initialiserToutLesParametresDeCreation();
		return true;
	}

	public boolean ajouterUneSemaine() throws ClientException {
		if ((assembleeId != null) && (orientationId != null) && (dateDeDebut != null)) {
			if (dateDeFin == null) {
				dateDeFin = dateDeDebut;
			}else if (dateDeFin.compareTo(dateDeDebut) >= 0) {
				SuiviService suiviService = SolonMgppServiceLocator.getSuiviService();
				Calendar calDateDebut = Calendar.getInstance();
				Calendar calDateFin = Calendar.getInstance();

				calDateDebut.setTimeInMillis(dateDeDebut.getTime());
				calDateFin.setTimeInMillis(dateDeFin.getTime());

				suiviService.creerSemaineParlementaire(documentManager, orientationId, assembleeId, calDateDebut,
						calDateFin);
				semaineAjoute = true;
			} else {
				return false;
			}
		}

		initialiserToutLesParametresDeCreation();
		return true;
	}

	public void annulerLaCreation() {
		initialiserToutLesParametresDeCreation();
	}

	private void initialiserToutLesParametresDeCreation() {
		activiteId = null;
		loiId = null;
		assembleeId = null;
		orientationId = null;
		dateDeDebut = null;
		dateDeFin = null;
	}

	public void supprimerActivite(String idDossier) throws ClientException {
		SuiviService suiviService = SolonMgppServiceLocator.getSuiviService();
		suiviService.removeActiviteParlementaire(documentManager, idDossier);
	}

	public void supprimerSemaine(String idDossier) throws ClientException {
		SuiviService suiviService = SolonMgppServiceLocator.getSuiviService();
		suiviService.removeSemaineParlementaire(documentManager, idDossier);
	}

	public List<FicheLoi> getPageActuelleDeLaListeDesLois() {
		return pageDeLaListeDeToutLesLois;
	}

	public List<FicheLoi> refreshPageActuelleDeLaListeDesLois() {
		// long start = System.nanoTime();
		pageDeLaListeDeToutLesLois = new ArrayList<FicheLoi>();
		int indexMinimal = (numeroDeLaPageActuelle - 1) * ligneParPage;
		int indexMaximal = 0;
		List<FicheLoi> ListeDeToutLesLoisDuCalendrierParlementaire = getListeDeToutLesLois();

		if (indexMinimal > ListeDeToutLesLoisDuCalendrierParlementaire.size()) {
			pageSuivantDisponible = false;
			pagePrecedentDisponible = false;
		} else {
			if ((ligneParPage * numeroDeLaPageActuelle) >= ListeDeToutLesLoisDuCalendrierParlementaire.size()) {
				pageSuivantDisponible = false;
				indexMaximal = ListeDeToutLesLoisDuCalendrierParlementaire.size();
			} else {
				pageSuivantDisponible = true;
				indexMaximal = (ligneParPage * numeroDeLaPageActuelle);
			}

			if (indexMinimal > 0) {
				pagePrecedentDisponible = true;
			} else {
				pagePrecedentDisponible = false;
			}
		}

		for (int idx = indexMinimal; idx < indexMaximal; idx++) {
			pageDeLaListeDeToutLesLois.add(ListeDeToutLesLoisDuCalendrierParlementaire.get(idx));
		}
		// long end = System.nanoTime();
		// counter += (end - start) / 1000000;
		// System.out.println("refreshPageActuelleDeLaListeDesLois() a pris " + (end - start) / 1000000 + " ms\t"
		// + "counter is now " + counter);

		return pageDeLaListeDeToutLesLois;
	}

	public void pageSuivantDuCalendrierParlementaire() {
		numeroDeLaPageActuelle++;
	}

	public void pagePrecedentDuCalendrierParlementaire() {
		numeroDeLaPageActuelle--;
	}

	public void dernierePageDuCalendrierParlementaire() {
		numeroDeLaPageActuelle = nombreDePages;
	}

	public void premierePageDuCalendrierParlementaire() {
		numeroDeLaPageActuelle = 1;
	}

	public String statusDeLaPAgeActuelleDuCalendrierParlementaire() {
		return numeroDeLaPageActuelle + "/" + nombreDePages;
	}

	public int getLigneParPage() {
		return ligneParPage;
	}

	public void setLigneParPage(int ligneParPage) {

		if (this.ligneParPage != ligneParPage) {
			numeroDeLaPageActuelle = 1;
			nombreDePages = (int) Math.ceil(Double.valueOf((getListeDeToutLesLois().size()))
					/ Double.valueOf(ligneParPage));
		}

		this.ligneParPage = ligneParPage;
	}

	public boolean isPageSuivantDisponible() {
		return pageSuivantDisponible;
	}

	public boolean isPagePrecedentDisponible() {
		return pagePrecedentDisponible;
	}

	public void refreshListeDeToutLesLoisDuCalendrierParlementaire() throws ClientException {
		// long start = System.nanoTime();
		// if(listeDeToutLesLois == null)
		// {
		listeDeToutLesLois = new ArrayList<FicheLoi>();

		SuiviService suiviService = SolonMgppServiceLocator.getSuiviService();
		List<DocumentModel> documentModelList = suiviService.getAllTexteLoiPourCalendrierParlementaire(documentManager,
				dateDeDebutFiltre, dateDeFinFiltre);

		for (DocumentModel documentModel : documentModelList) {
			FicheLoi ficheLoi = documentModel.getAdapter(FicheLoi.class);
			listeDeToutLesLois.add(ficheLoi);
		}
		nombreDePages = (int) Math.ceil(Double.valueOf((listeDeToutLesLois.size())) / Double.valueOf(ligneParPage));
		// long end = System.nanoTime();
		// counter += (end - start) / 1000000;
		// System.out.println("refreshListeDeToutLesLoisDuCalendrierParlementaire() a pris " + (end - start) / 1000000
		// + " ms\t" + "counter is now " + counter);

		// }
	}

	public List<FicheLoi> getListeDeToutLesLois() {
		return listeDeToutLesLois;
	}

	public String openFicheDeLoi(String documentId) throws ClientException {
		// long start = System.nanoTime();
		DocumentModel documentModelFicheDeLoi = documentManager.getDocument(new IdRef(documentId));
		FicheLoi ficheLoi = documentModelFicheDeLoi.getAdapter(FicheLoi.class);

		Dossier dossier = SolonEpgServiceLocator.getDossierService().findDossierFromIdDossier(documentManager,
				ficheLoi.getIdDossier());

		ficheLoiActions.setFicheLoi(documentModelFicheDeLoi);
		ficheLoiActions.setIsReadOnly(true);
		navigationContext.setCurrentDocument(dossier.getDocument());
		// long end = System.nanoTime();
		// counter += (end - start) / 1000000;
		// System.out
		// .println("openFicheDeLoi() a pris " + (end - start) / 1000000 + " ms\t" + "counter is now " + counter);

		return "view_suivi_fiche_loi";
	}

	public void refreshListeDeToutesLesDatesDesActivites() throws ClientException {

		// long start = System.nanoTime();
		// if(listeDeToutesLesDatesDesActivites == null)
		// {
		listeDeToutesLesDatesDesActivites = new ArrayList<Date>();

		SuiviService suiviService = SolonMgppServiceLocator.getSuiviService();
		List<Calendar> dateActiviteParlementaire = suiviService.getAllDateActiviteParlementaire(documentManager,
				dateDeDebutFiltre, dateDeFinFiltre);

		for (Calendar cal : dateActiviteParlementaire) {
			listeDeToutesLesDatesDesActivites.add(cal.getTime());
		}
		// long end = System.nanoTime();
		// counter += (end - start) / 1000000;
		// System.out.println("refreshListeDeToutesLesDatesDesActivites() a pris " + (end - start) / 1000000 + " ms\t"
		// + "counter is now " + counter);

	}

	public List<Date> getListeDeToutesLesDatesDesActivites() {
		return listeDeToutesLesDatesDesActivites;
	}

	private List<ActiviteParlementaire> getToutesLesActivitesParlementaireDeCeLoi(String idDossier)
			throws ClientException {
		long start = System.nanoTime();
		List<ActiviteParlementaire> toutesLesActivitesParlementaireDeCeLoi = new ArrayList<ActiviteParlementaire>();

		SuiviService suiviService = SolonMgppServiceLocator.getSuiviService();
		List<DocumentModel> documentModelList = suiviService.getAllActiviteParlementaireByDossierId(documentManager,
				idDossier);

		for (DocumentModel documentModel : documentModelList) {
			ActiviteParlementaire activiteParlementaire = documentModel.getAdapter(ActiviteParlementaire.class);
			toutesLesActivitesParlementaireDeCeLoi.add(activiteParlementaire);
		}
		long end = System.nanoTime();
		counter += (end - start) / 1000000;
		System.out.println("getToutesLesActivitesParlementaireDeCeLoi() a pris " + (end - start) / 1000000 + " ms\t"
				+ "counter is now " + counter);

		return toutesLesActivitesParlementaireDeCeLoi;
	}

	public Map<String, Collection<List<ActiviteParlementaire>>> refreshListeDesActivitesParlementaireParLoiTrie()
			throws ClientException {
		// long start = System.nanoTime();
		// if(mapDeToutesLesActivitesParlementaireParLoi == null)
		// {
		mapDeToutesLesActivitesParlementaireParLoi = new HashMap<String, Collection<List<ActiviteParlementaire>>>();
		List<FicheLoi> listeDeToutesLesLois = getListeDeToutLesLois();
		List<Date> listeDeToutesLesDates = getListeDeToutesLesDatesDesActivites();
		List<ActiviteParlementaire> toutesLesActivitesParlementaireDeCeLoi = null;
		Map<Date, List<ActiviteParlementaire>> toutesLesActivitesParlementaireDeCeLoiTrieSuivantLentete;

		for (FicheLoi ficheLoi : listeDeToutesLesLois) {
			toutesLesActivitesParlementaireDeCeLoi = getToutesLesActivitesParlementaireDeCeLoi(ficheLoi.getIdDossier());
			toutesLesActivitesParlementaireDeCeLoiTrieSuivantLentete = new LinkedHashMap<Date, List<ActiviteParlementaire>>();
			List<ActiviteParlementaire> activiteParlementaireDeCeJour;

			for (Date dateDuneActivites : listeDeToutesLesDates) {
				toutesLesActivitesParlementaireDeCeLoiTrieSuivantLentete.put(dateDuneActivites,
						new ArrayList<ActiviteParlementaire>());
			}

			for (ActiviteParlementaire activiteParlementaireDeCeLoi : toutesLesActivitesParlementaireDeCeLoi) {
				activiteParlementaireDeCeJour = toutesLesActivitesParlementaireDeCeLoiTrieSuivantLentete
						.get(activiteParlementaireDeCeLoi.getDate().getTime());
				if (activiteParlementaireDeCeJour != null) {
					activiteParlementaireDeCeJour.add(activiteParlementaireDeCeLoi);
					toutesLesActivitesParlementaireDeCeLoiTrieSuivantLentete.put(activiteParlementaireDeCeLoi.getDate()
							.getTime(), activiteParlementaireDeCeJour);
				}
			}

			// Sort All values list in the collection
			for (Map.Entry<Date, List<ActiviteParlementaire>> entry : toutesLesActivitesParlementaireDeCeLoiTrieSuivantLentete
					.entrySet()) {
				Collections.sort(entry.getValue(), new ActiviteParlementaireComp());
			}
			mapDeToutesLesActivitesParlementaireParLoi.put(ficheLoi.getIdDossier(),
					toutesLesActivitesParlementaireDeCeLoiTrieSuivantLentete.values());
		}
		// }
		// long end = System.nanoTime();
		// counter += (end - start) / 1000000;
		// System.out.println("refreshListeDesActivitesParlementaireParLoiTrie() a pris " + (end - start) / 1000000
		// + " ms\t" + "counter is now " + counter);

		return mapDeToutesLesActivitesParlementaireParLoi;
	}

	public Map<String, Collection<List<ActiviteParlementaire>>> getListeDesActivitesParlementaireParLoiTrie() {
		return this.mapDeToutesLesActivitesParlementaireParLoi;
	}

	public Collection<List<ActiviteParlementaire>> getToutesLesActivitesParlementaireDeCeLoiTrie(String idDossier) {
		return getListeDesActivitesParlementaireParLoiTrie().get(idDossier);
	}

	public void refreshListedeSemainesParTypesTrie() throws ClientException {
		// long start = System.nanoTime();
		// if(listedeSemainesParTypes == null)
		// {
		listedeSemainesParTypes = new HashMap<String, List<Map<String, String>>>();
		SuiviService suiviService = SolonMgppServiceLocator.getSuiviService();
		List<Date> listeDeToutesLesDatesDesActivites = getListeDeToutesLesDatesDesActivites();
		for (Map.Entry<String, String> assemblee : getListeDesAssemblees().entrySet()) {
			List<Map<String, String>> listeDesSemaines = new ArrayList<Map<String, String>>();
			int nombreDeColspan = 0;

			List<DocumentModel> documentModelList = suiviService.getAllSemaineParlementaire(documentManager,
					assemblee.getKey());
			if (documentModelList != null) {
				if (documentModelList.size() > 0) {
					for (DocumentModel documentModel : documentModelList) {
						SemaineParlementaire semaineParlementaire = documentModel
								.getAdapter(SemaineParlementaire.class);
						nombreDeColspan = creerStructureHtmlPourSemaineParlementaire(semaineParlementaire,
								listeDesSemaines, nombreDeColspan, listeDeToutesLesDatesDesActivites);
					}

					clearListeDesSemainesSiToutEstNull(listeDesSemaines);

					if (listeDesSemaines.size() > 0) {
						for (; nombreDeColspan < getListeDeToutesLesDatesDesActivites().size(); nombreDeColspan++) {
							listeDesSemaines.add(null);
						}

						listedeSemainesParTypes.put(assemblee.getKey(), listeDesSemaines);
					}
				}
			}

		}
		// }
		// long end = System.nanoTime();
		// counter += (end - start) / 1000000;
		// System.out.println("refreshListedeSemainesParTypesTrie() a pris " + (end - start) / 1000000 + " ms\t"
		// + "counter is now " + counter);

	}

	public Map<String, List<Map<String, String>>> getListedeSemainesParTypesTrie() {
		return listedeSemainesParTypes;
	}

	private int creerStructureHtmlPourSemaineParlementaire(SemaineParlementaire semaineParlementaire,
			List<Map<String, String>> listeDesSemaines, int nombreDeColspan,
			List<Date> listeDeToutesLesDatesDesActivites) throws NumberFormatException, ClientException {
		// long start = System.nanoTime();
		Map<String, String> cellule = null;
		String celluleLabel = getListeDesOrientations().get(semaineParlementaire.getOrientation());
		celluleLabel = (celluleLabel == null ? semaineParlementaire.getOrientation() : celluleLabel);
		int dateActiviteSize = listeDeToutesLesDatesDesActivites.size();
		if (dateActiviteSize > 0) {
			if ((listeDeToutesLesDatesDesActivites.get(0).compareTo(semaineParlementaire.getDateFin().getTime()) <= 0)
					|| (listeDeToutesLesDatesDesActivites.get((dateActiviteSize - 1)).compareTo(
							semaineParlementaire.getDateDebut().getTime()) >= 0)) {
				for (; nombreDeColspan < dateActiviteSize; nombreDeColspan++) {
					if (semaineParlementaire.getDateFin().getTime()
							.compareTo(listeDeToutesLesDatesDesActivites.get(nombreDeColspan)) >= 0) {
						Date dateDuneActivites = listeDeToutesLesDatesDesActivites.get(nombreDeColspan);

						if (cellule == null) {
							if (dateDuneActivites.compareTo(semaineParlementaire.getDateDebut().getTime()) == 0) {
								cellule = new HashMap<String, String>();
								cellule.put("label", celluleLabel);
								cellule.put("id", semaineParlementaire.getDocument().getId());
								cellule.put("colspan", "1");
							} else {
								if ((dateDuneActivites.compareTo(semaineParlementaire.getDateDebut().getTime()) > 0)
										&& (dateDuneActivites.compareTo(semaineParlementaire.getDateFin().getTime()) <= 0)) {
									cellule = new HashMap<String, String>();
									cellule.put("label", celluleLabel);
									cellule.put("id", semaineParlementaire.getDocument().getId());
									cellule.put("colspan", "1");
								} else {
									// TODO A voir pourquoi on add null
									listeDesSemaines.add(null);
								}
							}
						} else {
							if (dateDuneActivites.compareTo(semaineParlementaire.getDateFin().getTime()) < 0) {
								int colspan = Integer.parseInt(cellule.get("colspan")) + 1;
								cellule.remove("colspan");
								cellule.put("colspan", String.valueOf(colspan));
							} else if (dateDuneActivites.compareTo(semaineParlementaire.getDateFin().getTime()) == 0) {
								int colspan = Integer.parseInt(cellule.get("colspan")) + 1;
								cellule.remove("colspan");
								cellule.put("colspan", String.valueOf(colspan));
								listeDesSemaines.add(cellule);
								cellule = null;
							} else if (dateDuneActivites.compareTo(semaineParlementaire.getDateFin().getTime()) > 0) {
								listeDesSemaines.add(cellule);
								cellule = null;
								break;
							}
						}
					} else {
						if (cellule != null) {
							listeDesSemaines.add(cellule);
						}
						cellule = null;
						break;
					}
				}
				if (cellule != null) {
					listeDesSemaines.add(cellule);
				}

			}
		}
		// long end = System.nanoTime();
		// counter += (end - start) / 1000000;
		// System.out.println("creerStructureHtmlPourSemaineParlementaire() a pris " + (end - start) / 1000000 + " ms\t"
		// + "counter is now " + counter);

		return nombreDeColspan;
	}

	public void diffuserCalendrierParlementaire() throws ClientException {
		SuiviService suiviService = SolonMgppServiceLocator.getSuiviService();

		suiviService.diffuserCalendrierParlementaire(getListedeSemainesParTypesTrie(),
				getListeDeToutesLesDatesDesActivites(), getListeDeToutLesLois(),
				getListeDesActivitesParlementaireParLoiTrie());

	}

	private void clearListeDesSemainesSiToutEstNull(List<Map<String, String>> listeDesSemaines) {
		int nombreDesCellulesNull = 0;

		for (Map<String, String> cellule : listeDesSemaines) {
			if (cellule == null) {
				nombreDesCellulesNull++;
			}
		}

		if (listeDesSemaines.size() == nombreDesCellulesNull) {
			listeDesSemaines.clear();
		}
	}

	public void refreshData() throws ClientException {
		// long start = System.nanoTime();
		refreshListeDesLois();
		refreshListeDeToutLesLoisDuCalendrierParlementaire();
		refreshListeDeToutesLesDatesDesActivites();
		refreshListeDesActivitesParlementaireParLoiTrie();
		refreshListedeSemainesParTypesTrie();
		refreshPageActuelleDeLaListeDesLois();
		// long end = System.nanoTime();
		// counter += (end - start) / 1000000;
		// System.out.println("refreshData() a pris " + (end - start) / 1000000 + " ms\t" + "counter is now " +
		// counter);

	}

	public String getFormatedDateValue(Date dateValue) {
		SimpleDateFormat formatter = DateUtil.simpleDateFormat("dd/MM/yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateValue);
		return daysAbrv[calendar.get(Calendar.DAY_OF_WEEK) - 1] + ". " + formatter.format(dateValue);
	}

	public String getCurrentDate() {
		SimpleDateFormat formatter = DateUtil.simpleDateFormat("dd/MM/yyyy");
		return formatter.format(new Date());
	}

	public void setListeDesLois(Map<String, String> listeDesLois) {
		this.listeDesLois = listeDesLois;
	}

	public Date getDateDeDebutFiltre() {
		return DateUtil.copyDate(dateDeDebutFiltre);
	}

	public void setDateDeDebutFiltre(Date dateDeDebutFiltre) {
		this.dateDeDebutFiltre = DateUtil.copyDate(dateDeDebutFiltre);
	}

	public Date getDateDeFinFiltre() {
		return DateUtil.copyDate(dateDeFinFiltre);
	}

	public void setDateDeFinFiltre(Date dateDeFinFiltre) {
		this.dateDeFinFiltre = DateUtil.copyDate(dateDeFinFiltre);
	}

	public boolean isActiviteAjoute() {
		return activiteAjoute;
	}

	public void setActiviteAjoute(boolean isActiviteAjoute) {
		this.activiteAjoute = isActiviteAjoute;
	}

	public boolean isSemaineAjoute() {
		return semaineAjoute;
	}

	public void setSemaineAjoute(boolean semaineAjoute) {
		this.semaineAjoute = semaineAjoute;
	}

	class ActiviteParlementaireComp implements Comparator<ActiviteParlementaire> {
		@Override
		public int compare(ActiviteParlementaire obj1, ActiviteParlementaire obj2) {
			return obj1.getActivite().compareTo(obj2.getActivite());
		}
	}

}
