package fr.dila.solonmgpp.web.suivi;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.service.SuiviService;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.fichepresentation.FicheLoiActionsBean;
import fr.dila.st.core.util.DateUtil;

/**
 * 
 * @author aRammal
 */

@Name("echeancierDePromulgationActions")
@Scope(ScopeType.CONVERSATION)
public class EcheancierDePromulgationActionsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = true)
    protected transient FicheLoiActionsBean ficheLoiActions;

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    @In(create = true, required = false)
    protected transient ResourcesAccessor resourcesAccessor;
    private Long echeancierPromulgationCount;

    private final String[] entetesDesColonnes = { "label.mgpp.echeancierPomulgation.lois", "label.mgpp.echeancierPomulgation.conseilConstitutionnelSaisine", "label.mgpp.echeancierPomulgation.conseilConstitutionnelDecision", "label.mgpp.echeancierPomulgation.dateReception", "label.mgpp.echeancierPomulgation.demandeEpreuvesJO", "label.mgpp.echeancierPomulgation.retourJO", "label.mgpp.echeancierPomulgation.envoiRelecture", "label.mgpp.echeancierPomulgation.retourRelecture", "label.mgpp.echeancierPomulgation.misAuContreseing", "label.mgpp.echeancierPomulgation.contreseingPM", "label.mgpp.echeancierPomulgation.departElysee", "label.mgpp.echeancierPomulgation.retourElysee", "label.mgpp.echeancierPomulgation.dateLimitePromulgation", "label.mgpp.echeancierPomulgation.publicationJO" };

    private List<Object[]> tableauDeLechancierDePromulgationSansTriage = null;
    private List<Object[]> tableauDeLechancierDePromulgation = null;

    private int colonneDeTriage = -1;
    private boolean triageAscendant = true;
    private int ligneParPage = 5;
    private int numeroDeLaPageActuelle = 1;
    private int nombreDePages = 1;
    private boolean pageSuivantDisponible = false;
    private boolean pagePrecedentDisponible = false;

    public List<Object[]> getTableauDeLechancierDePromulgation() throws ClientException {
        List<DocumentModel> documentModelList = SolonMgppServiceLocator.getSuiviService().getEcheancierPromulgationPage(documentManager, ligneParPage, ((numeroDeLaPageActuelle - 1) * ligneParPage));
        List<String> ids = new ArrayList<String>();
        for (DocumentModel doc : documentModelList) {
            ids.add(doc.getAdapter(FicheLoi.class).getIdDossier());
        }
        List<DocumentModel> dossierList = SolonEpgServiceLocator.getDossierService().findDossiersFromIdsDossier(documentManager, ids);

        HashMap<String, DocumentModel> dossierMap = new HashMap<String, DocumentModel>();
        for (DocumentModel doc : dossierList) {
            dossierMap.put(doc.getAdapter(Dossier.class).getIdDossier(), doc);
        }

        tableauDeLechancierDePromulgationSansTriage = new ArrayList<Object[]>();
        tableauDeLechancierDePromulgation = new ArrayList<Object[]>();

        for (DocumentModel documentModel : documentModelList) {

            FicheLoi ficheLoi = documentModel.getAdapter(FicheLoi.class);

            Object[] cellulesDeDonnees = new Object[14];

            cellulesDeDonnees[0] = ficheLoi.getIdDossier();
            cellulesDeDonnees[1] = ficheLoi.getDateSaisieCC();
            cellulesDeDonnees[2] = ficheLoi.getDateDecision();
            cellulesDeDonnees[3] = ficheLoi.getDateReception();
            if (dossierMap.containsKey(ficheLoi.getIdDossier())) {
                TraitementPapier traitementPapier = dossierMap.get(ficheLoi.getIdDossier()).getAdapter(TraitementPapier.class);
                cellulesDeDonnees[4] = traitementPapier.getEpreuve().getEpreuveDemandeLe();
                cellulesDeDonnees[5] = traitementPapier.getEpreuve().getEpreuvePourLe();
                cellulesDeDonnees[6] = traitementPapier.getEpreuve().getEnvoiRelectureLe();
                cellulesDeDonnees[7] = traitementPapier.getRetourDuBonaTitrerLe();
                cellulesDeDonnees[8] = traitementPapier.getSignaturePr().getDateEnvoiSignature();
                cellulesDeDonnees[9] = traitementPapier.getSignaturePr().getDateRetourSignature();
            }
            cellulesDeDonnees[10] = ficheLoi.getDepartElysee();
            cellulesDeDonnees[11] = ficheLoi.getRetourElysee();
            cellulesDeDonnees[12] = ficheLoi.getDateLimitePromulgation();
            cellulesDeDonnees[13] = ficheLoi.getDateJO();

            tableauDeLechancierDePromulgation.add(cellulesDeDonnees);
            tableauDeLechancierDePromulgationSansTriage.add(cellulesDeDonnees);

        }

        return tableauDeLechancierDePromulgation;
    }

    public List<Object[]> getPageActuelleDeLechancierDePromulgation() throws ClientException {
        echeancierPromulgationCount = SolonMgppServiceLocator.getSuiviService().getEcheancierPromulgationCount(documentManager);
        nombreDePages = (int) Math.ceil(Double.valueOf((echeancierPromulgationCount)) / Double.valueOf(ligneParPage));
        int indexMinimal = (numeroDeLaPageActuelle - 1) * ligneParPage;

        if (indexMinimal > echeancierPromulgationCount.intValue()) {
            pageSuivantDisponible = false;
            pagePrecedentDisponible = false;
        } else {
            if (ligneParPage * numeroDeLaPageActuelle >= echeancierPromulgationCount) {
                pageSuivantDisponible = false;
            } else {
                pageSuivantDisponible = true;
            }
            if (indexMinimal > 0) {
                pagePrecedentDisponible = true;
            } else {
                pagePrecedentDisponible = false;
            }
        }
        return getTableauDeLechancierDePromulgation();
    }

    public Long getTableauDeLechancierDePromulgationCount() {
        return echeancierPromulgationCount;
    }

    public String openFicheDeLoi(String documentId) throws ClientException {
        DocumentModel documentModelFicheDeLoi = documentManager.getDocument(new IdRef(documentId));
        FicheLoi ficheLoi = documentModelFicheDeLoi.getAdapter(FicheLoi.class);

        Dossier dossier = SolonEpgServiceLocator.getDossierService().findDossierFromIdDossier(documentManager, ficheLoi.getIdDossier());

        ficheLoiActions.setFicheLoi(documentModelFicheDeLoi);
        navigationContext.setCurrentDocument(dossier.getDocument());

        return "view_suivi_fiche_loi";
    }

    public FicheLoi getFicheDeLoi(String idDossier) throws ClientException {
        return SolonMgppServiceLocator.getDossierService().findOrCreateFicheLoi(documentManager, idDossier);
    }

    public void pageSuivantDeLechancierDePromulgation() {
        numeroDeLaPageActuelle++;
    }

    public void pagePrecedentDeLechancierDePromulgation() {
        numeroDeLaPageActuelle--;
    }

    public void dernierePageDeLechancierDePromulgation() {
        numeroDeLaPageActuelle = nombreDePages;
    }

    public void premierePageDeLechancierDePromulgation() {
        numeroDeLaPageActuelle = 1;
    }

    public String statusDeLaPAgeActuelleDeLechancierDePromulgation() {
        return numeroDeLaPageActuelle + "/" + nombreDePages;
    }

    public int getColonneDeTriage() {
        return colonneDeTriage;
    }

    public boolean isTriageAscendant() {
        return triageAscendant;
    }

    public void triTableauDeLechancierDePromulgation(int colonneDeTriage) {
        if (this.colonneDeTriage == colonneDeTriage) {
            if (triageAscendant) {
                triageAscendant = false;
            } else {
                triageAscendant = true;
                this.colonneDeTriage = -1;
            }
        } else {
            this.colonneDeTriage = colonneDeTriage;
            triageAscendant = true;
        }

        if (this.colonneDeTriage == -1) {
            tableauDeLechancierDePromulgation = new ArrayList<Object[]>();

            for (Object[] ligneDeDonnees : tableauDeLechancierDePromulgationSansTriage) {
                tableauDeLechancierDePromulgation.add(ligneDeDonnees);
            }
        } else {
            final int indexDeLaCelluleDeTriage = this.colonneDeTriage;
            Comparator<Object[]> lignedeLecheancierDePromulgationComparator = new Comparator<Object[]>() {
                @Override
                public int compare(Object[] obj1, Object[] obj2) {

                    if (obj1[indexDeLaCelluleDeTriage] instanceof String) {
                        return ((String) obj1[indexDeLaCelluleDeTriage]).compareTo((String) obj2[indexDeLaCelluleDeTriage]);

                    } else if (obj1[indexDeLaCelluleDeTriage] instanceof Calendar) {
                        return ((Calendar) obj1[indexDeLaCelluleDeTriage]).compareTo((Calendar) obj2[indexDeLaCelluleDeTriage]);
                    } else {
                        return 0;
                    }
                }
            };

            if (triageAscendant) {
                Collections.sort(tableauDeLechancierDePromulgation, lignedeLecheancierDePromulgationComparator);
            } else {
                Collections.sort(tableauDeLechancierDePromulgation, Collections.reverseOrder(lignedeLecheancierDePromulgationComparator));
            }
        }
    }

    public List<String[]> getlistEcheance() throws ClientException {
        String[] echeanceRow;
        List<String[]> listEcheance = new ArrayList<String[]>();
        for (Object[] echeance : tableauDeLechancierDePromulgation) {
            echeanceRow = new String[6];
            echeanceRow[0] = getFicheDeLoi(getFormatedValueToDisplay(echeance[0])).getIntitule();
            echeanceRow[1] = getFormatedValueToDisplay(echeance[1]);
            echeanceRow[2] = getFormatedValueToDisplay(echeance[2]);
            echeanceRow[3] = getFormatedValueToDisplay(echeance[3]);
            echeanceRow[4] = getFormatedValueToDisplay(echeance[12]);
            echeanceRow[5] = getFormatedValueToDisplay(echeance[13]);
            listEcheance.add(echeanceRow);
        }
        return listEcheance;
    }

    public String[] getListEntetes() {
        String[] entetes = new String[6];
        entetes[0] = resourcesAccessor.getMessages().get(entetesDesColonnes[0]);
        entetes[1] = resourcesAccessor.getMessages().get(entetesDesColonnes[1]);
        entetes[2] = resourcesAccessor.getMessages().get(entetesDesColonnes[2]);
        entetes[3] = resourcesAccessor.getMessages().get(entetesDesColonnes[3]);
        entetes[4] = resourcesAccessor.getMessages().get(entetesDesColonnes[12]);
        entetes[5] = resourcesAccessor.getMessages().get(entetesDesColonnes[13]);
        return entetes;
    }

    public void diffuserEcheancierPromulgation() throws ClientException {
        SuiviService suiviService = SolonMgppServiceLocator.getSuiviService();
        suiviService.diffuserEcheancierPromulgation(getlistEcheance(), getListEntetes());
    }

    public int getLigneParPage() {
        return ligneParPage;
    }

    public void setLigneParPage(int ligneParPage) {

        if (this.ligneParPage != ligneParPage) {
            numeroDeLaPageActuelle = 1;
            nombreDePages = (int) Math.ceil(Double.valueOf((echeancierPromulgationCount)) / Double.valueOf(ligneParPage));
        }

        this.ligneParPage = ligneParPage;
    }

    public boolean isPageSuivantDisponible() {
        return pageSuivantDisponible;
    }

    public boolean isPagePrecedentDisponible() {
        return pagePrecedentDisponible;
    }

    public String[] getEntetesDesColonnes() {
        if (this.entetesDesColonnes != null) {
            return this.entetesDesColonnes.clone();
        }
        return null;
    }

    public String getFormatedValueToDisplay(Object value) {
        if (value instanceof String) {
            return (String) value;

        } else if (value instanceof Calendar) {
            SimpleDateFormat formatter = DateUtil.simpleDateFormat("dd/MM/yyyy");
            return formatter.format(((Calendar) value).getTime());
        } else {
            return "";
        }
    }
}
