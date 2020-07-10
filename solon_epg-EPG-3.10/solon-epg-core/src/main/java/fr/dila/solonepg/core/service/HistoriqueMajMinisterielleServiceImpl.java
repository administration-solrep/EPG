package fr.dila.solonepg.core.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.PathRef;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.MajMin;
import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants.MAJ_TARGET;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants.MAJ_TYPE;
import fr.dila.solonepg.api.dto.MesureApplicativeDTO;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.HistoriqueMajMinisterielleService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.core.util.ComplexTypeUtil;
import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.StringUtil;

public class HistoriqueMajMinisterielleServiceImpl implements HistoriqueMajMinisterielleService {

    private static final STLogger LOGGER = STLogFactory.getLog(HistoriqueMajMinisterielleServiceImpl.class);
    
    private static final String MODIF_MIN = "Modification ministérielle";
    private static final String DEVIENT = ") devient ";
    private static final String VIDE = "vide";
    private static final String TIRET = " - ";

    @Override
    public void registerMajDossier(final CoreSession session, final DocumentModel dossierDoc) throws ClientException {
        // on récupère l'ancien document
        LOGGER.info(session, EpgLogEnumImpl.CREATE_MAJ_MIN_TEC, "Creation des différences ministérielles pour le dossier");
        final String idDoc = dossierDoc.getId();

        Dossier oldDossier = null;
        List<ComplexeType> oldApplicationLoi;
        List<ComplexeType> oldTranspositionDirective;
        List<ComplexeType> oldTranspositionOrdonnance;
        if (idDoc == null) {
            oldApplicationLoi = new ArrayList<ComplexeType>();
            oldTranspositionDirective = new ArrayList<ComplexeType>();
            oldTranspositionOrdonnance = new ArrayList<ComplexeType>();
        } else {
            final DocumentModel oldDocument = session.getDocument(new IdRef(idDoc));
            oldDossier = oldDocument.getAdapter(Dossier.class);
            oldApplicationLoi = oldDossier.getApplicationLoi();
            oldTranspositionDirective = oldDossier.getTranspositionDirective();
            oldTranspositionOrdonnance = oldDossier.getTranspositionOrdonnance();
        }
        final Dossier currentDossier = dossierDoc.getAdapter(Dossier.class);

        createDifference(session, currentDossier, MAJ_TARGET.APPLICATION_LOI, oldApplicationLoi, currentDossier.getApplicationLoi());
        createDifference(session, currentDossier, MAJ_TARGET.APPLICATION_ORDONNANCE, oldTranspositionOrdonnance, currentDossier.getTranspositionOrdonnance());
        createDifference(session, currentDossier, MAJ_TARGET.TRANSPOSITION, oldTranspositionDirective, currentDossier.getTranspositionDirective());

        createMajMinHabilitation(session, oldDossier, currentDossier);

        LOGGER.info(session, STLogEnumImpl.LOG_INFO_TEC, "Fin de création des différences ministérielles pour le dossier");
    }
    
    @Override
    public void registerMajMesure(final CoreSession session, final TexteMaitre texteMaitre, final MesureApplicative existingMesure, final MesureApplicativeDTO updatedMesure) throws ClientException {
        // on récupère l'ancien document
    	LOGGER.info(session, EpgLogEnumImpl.CREATE_MAJ_MIN_TEC, "Creation des différences ministérielles pour la mesure");

        boolean fromAppLoi = ActiviteNormativeConstants.MESURE_FILTER.equals(existingMesure.getAdapter(ActiviteNormative.class).getApplicationLoi());
        boolean fromAppOrdo = ActiviteNormativeConstants.MESURE_FILTER.equals(existingMesure.getAdapter(ActiviteNormative.class).getApplicationOrdonnance());

        final NORService norService = SolonEpgServiceLocator.getNORService();
        final DocumentModel dossierDoc = norService.getDossierFromNOR(session, texteMaitre.getNumeroNor());
        if (dossierDoc != null) {
        	Dossier dossier = dossierDoc.getAdapter(Dossier.class);
	        if (fromAppLoi) {
	        	createMajMinMesure(session, existingMesure, updatedMesure, dossier, MAJ_TARGET.APPLICATION_LOI, texteMaitre.getTitreOfficiel(), texteMaitre.getNumero());
	        }
	        if (fromAppOrdo) {
	        	createMajMinMesure(session, existingMesure, updatedMesure, dossier, MAJ_TARGET.APPLICATION_ORDONNANCE, texteMaitre.getTitreOfficiel(), texteMaitre.getNumero());
	        }
        } else {
        	LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, "Echec de récupération du dossier par le nor " + texteMaitre.getNumeroNor());
        }
        LOGGER.info(session, STLogEnumImpl.LOG_INFO_TEC, "Fin de création des différences ministérielles pour la mesure");
    }

    /**
     * Crée un document Mise à jour dans le répertoire des mises à jour
     * 
     * @param session la session utilisateur
     * @return la maj créée
     * @throws ClientException
     */
    private MajMin createMajMin(final CoreSession session, final Dossier dossier, final MAJ_TYPE modification, final MAJ_TARGET target,
            final ComplexeType genericTransp) throws ClientException {
        final DocumentModel document = session.createDocumentModel(ActiviteNormativeConstants.MAJ_MIN_DOCUMENT_TYPE);
        document.setPathInfo(ActiviteNormativeConstants.MAJ_MIN_PATH + "/" + target.toString().toLowerCase(), UUID.randomUUID().toString());
        final DocumentModel documentCreated = session.createDocument(document);
        final MajMin maj = documentCreated.getAdapter(MajMin.class);
        maj.setModification(modification);
        maj.setIdDossier(dossier.getDocument().getId());
        maj.setDateCreation(Calendar.getInstance());
        maj.setNorDossier(dossier.getNumeroNor());
        maj.copyContentFromMap(genericTransp.getSerializableMap());
        session.saveDocument(documentCreated);
        session.save();

        return maj;
    }
    
    /**
     * Créé une entrée dans le tableau des historiques de maj ministérielles si nécessaire
     * @param session
     * @param existingMesure
     * @param updatedMesure
     * @param dossier
     * @param target
     * @param titre
     * @param numero
     * @return
     * @throws ClientException
     */
    private MajMin createMajMinMesure(final CoreSession session, final MesureApplicative existingMesure, final MesureApplicativeDTO updatedMesure, 
    		final Dossier dossier, final MAJ_TARGET target, final String titre, final String numero) throws ClientException {
    	String difference = diffMesure(existingMesure, updatedMesure);
    	MajMin maj = null;
    	if (!MODIF_MIN.equals(difference)) {
    		final DocumentModel document = session.createDocumentModel(ActiviteNormativeConstants.MAJ_MIN_DOCUMENT_TYPE);
            document.setPathInfo(ActiviteNormativeConstants.MAJ_MIN_PATH + "/" + target.toString().toLowerCase(), UUID.randomUUID().toString());
            final DocumentModel documentCreated = session.createDocument(document);
            maj = documentCreated.getAdapter(MajMin.class);
            maj.setModification(MAJ_TYPE.MINISTERE);
            maj.setIdDossier(dossier.getDocument().getId());
            maj.setDateCreation(Calendar.getInstance());
            maj.setNorDossier(dossier.getNumeroNor());
            
            maj.setTitre(titre);
            maj.setRef(numero);
            maj.setNumeroArticle(existingMesure.getArticle());
            maj.setRefMesure(existingMesure.getNumeroOrdre());
            maj.setCommentMaj(difference);
            
            session.saveDocument(documentCreated);
            session.save();
    	}
        
        return maj;
    }
    
    /**
     * Construction de la chaine de commentaire utilisée pour les différences après sauvegarde mesure.
     * Les champs vérifiés sont : direction responsable, consultations obligatoires hors CE, Calendrier des consultations obligatoires hors CE, 
     * date prévisionnelle de saisine du Conseil d'Etat, et communication ministérielle
     * @param existingMesure
     * @param updatedMesure
     * @return
     */
    private String diffMesure(final MesureApplicative existingMesure, final MesureApplicativeDTO updatedMesure) {
    	StringBuilder diff = new StringBuilder(MODIF_MIN);
    	diff.append(" : ");
    	
    	int size = diff.length();
    	// Comparaison de la direction responsable
    	diff.append(diffString(existingMesure.getDirectionResponsable(), updatedMesure.getDirectionResponsable(), "Direction responsable"));
    	if (size != diff.length()) {
    		diff.append(TIRET);
    		size = diff.length();
    	}
    	// Comparaison des consultations hors CE
    	diff.append(diffString(existingMesure.getConsultationsHCE(), updatedMesure.getConsultationsHCE(), "Consultations obligatoires hors CE"));
    	if (size != diff.length()) {
    		diff.append(TIRET);
    		size = diff.length();
    	}
    	// Comparaison du calendrier des consultations obligatoires hors CE
    	diff.append(diffString(existingMesure.getCalendrierConsultationsHCE(), updatedMesure.getCalendrierConsultationsHCE(), "Calendrier des consultations obligatoires hors CE"));
    	if (size != diff.length()) {
    		diff.append(TIRET);
    		size = diff.length();
    	}
    	// Comparaison de la date prévisionnelle de saisine du conseil d'Etat (mois/année)
    	int existingYear = -1;
		int existingMonth = -1;
		int updatedYear = -1;
		int updatedMonth = -1;
    	if (existingMesure.getDatePrevisionnelleSaisineCE() != null) {
    		existingYear = existingMesure.getDatePrevisionnelleSaisineCE().get(Calendar.YEAR);
    		existingMonth = existingMesure.getDatePrevisionnelleSaisineCE().get(Calendar.MONTH);
    	} 
    	if (updatedMesure.getDatePrevisionnelleSaisineCE() != null) {
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(updatedMesure.getDatePrevisionnelleSaisineCE());
    		updatedYear = cal.get(Calendar.YEAR);
    		updatedMonth = cal.get(Calendar.MONTH);
    	}
    	if (existingYear != updatedYear || existingMonth != updatedMonth) {
    		diff.append("Date prévisionnelle de saisine du Conseil d'État (");
    		if (existingYear == -1 && existingMonth == -1) {
    			diff.append(VIDE);
    		} else {
    			diff = existingMonth <= 9 ? diff.append("0") : diff;
    			diff.append(existingMonth + 1).append("/").append(existingYear);
    		}
    		diff.append(DEVIENT);
    		if (updatedYear == -1 && updatedMonth == -1) {
    			diff.append(VIDE);
    		} else {
    			diff = updatedMonth <= 9 ? diff.append("0") : diff;
    			diff.append(updatedMonth + 1).append("/").append(updatedYear);
    		}
    	}
    	if (size != diff.length()) {
    		diff.append(TIRET);
    		size = diff.length();
    	}
    	// Comparaison communication ministérielle
    	diff.append(diffString(existingMesure.getCommunicationMinisterielle(), updatedMesure.getCommunicationMinisterielle(), "Communication ministérielle"));
    	
    	if (size == diff.length()) {
    		diff.setLength(diff.length() - 3);
    	}
    	
    	return diff.toString();
    }
    
    private String diffString(String existingValue, String updatedValue, String label) {
    	StringBuilder diff = new StringBuilder();
    	if ((StringUtil.isNotBlank(existingValue) && !existingValue.equals(updatedValue)) 
    			|| (StringUtil.isBlank(existingValue) && StringUtil.isNotBlank(updatedValue))) {
    		diff.append(label).append(" (");
    		if (StringUtil.isNotBlank(existingValue)) {
    			diff.append(existingValue);
    		} else {
    			diff.append(VIDE);
    		}
    		diff.append(DEVIENT);
    		if (StringUtil.isNotBlank(updatedValue)) {
    			diff.append(updatedValue);
    		} else {
    			diff.append(VIDE);
    		}
    	}
    	return diff.toString();
    }

    public DocumentRef getMajMinRootRef(final CoreSession session) {
        return new PathRef(ActiviteNormativeConstants.MAJ_MIN_PATH);
    }

    @Override
    public DocumentRef getRef(final MAJ_TARGET target) {
        return new PathRef(ActiviteNormativeConstants.MAJ_MIN_PATH + "/" + target.toString().toLowerCase());
    }

    @Override
    public DocumentRef getOrdonnanceRef(final CoreSession session) {
        return getRef(MAJ_TARGET.ORDONNANCE);
    }

    @Override
    public DocumentRef getApplicationLoiRef(final CoreSession session) {
        return getRef(MAJ_TARGET.APPLICATION_LOI);
    }

    @Override
    public DocumentRef getTranspositionRef(final CoreSession session) {
        return getRef(MAJ_TARGET.TRANSPOSITION);
    }

    @Override
    public List<MajMin> createDifference(final CoreSession session, final Dossier dossier, final MAJ_TARGET target,
            final List<ComplexeType> initialList, final List<ComplexeType> finalList) throws ClientException {
        final List<MajMin> results = new ArrayList<MajMin>();

        // conversion des 2 listes en map
        final Map<String, ComplexeType> initialMap = ComplexTypeUtil.convertToMap(initialList);
        final Map<String, ComplexeType> finalMap = ComplexTypeUtil.convertToMap(finalList);

        // Cas des élements supprimées dans la liste 2
        for (Entry<String, ComplexeType> entry : initialMap.entrySet()) {
            if (!finalMap.containsKey(entry.getKey())) {
                final MajMin deleted = createMajMin(session, dossier, MAJ_TYPE.SUPPRESSION, target, entry.getValue());
                results.add(deleted);
            }
        }

        // Cas des élements ajoutés dans la liste 2
        for (Entry<String, ComplexeType> entry : finalMap.entrySet()) {
            if (!initialMap.containsKey(entry.getKey())) {
                final MajMin added = createMajMin(session, dossier, MAJ_TYPE.AJOUT, target, entry.getValue());
                results.add(added);
            }
        }

        // Cas des éléments modifiés
        for (Entry<String, ComplexeType>  entry : finalMap.entrySet()) {
            if (initialMap.containsKey(entry.getKey())) {
                final ComplexeType initialListComplexeType = initialMap.get(entry.getKey());
                final ComplexeType finalListComplexeType = entry.getValue();
                if (!initialListComplexeType.equals(finalListComplexeType)) {
                    final MajMin modified = createMajMin(session, dossier, MAJ_TYPE.MODIFICATION, target, finalListComplexeType);
                    results.add(modified);
                }
            }
        }

        return results;
    }

    @Override
    public List<DocumentModel> getHistoriqueMaj(final CoreSession session, final MAJ_TARGET target) throws ClientException {
        final DocumentModel doc = session.getDocument(getRef(target));
        final String selectMajMin = "SELECT * FROM MajMin WHERE ecm:parentId = '" + doc.getId()
                + "' and ecm:isProxy = 0 ORDER BY majm:dateCreation DESC";
        return session.query(selectMajMin);
    }

    @Override
    public List<Map<String, Serializable>> getHistoriqueMajMap(final CoreSession session, final MAJ_TARGET target) throws ClientException {
        final List<Map<String, Serializable>> results = new ArrayList<Map<String, Serializable>>();
        final List<DocumentModel> docs = getHistoriqueMaj(session, target);
        for (final DocumentModel doc : docs) {
            final Map<String, Serializable> mapForProvider = PropertyUtil.getMapForProviderFromDocumentProps(doc,
                    ActiviteNormativeConstants.MAJ_MIN_SCHEMA);
            results.add(mapForProvider);
        }
        return results;
    }

    @Override
    public MajMin createMajMinHabilitation(final CoreSession session, final Dossier oldDossier, final Dossier newDossier) throws ClientException {
        String oldRefLoi = null;
        String newNumArticles = null;
        String newRefLoi = null;
        // Dans le cas de la création d'un nouveau dossier, oldDossier est null
        if (oldDossier != null) {
            oldRefLoi = oldDossier.getHabilitationRefLoi();
        }

        newNumArticles = newDossier.getHabilitationNumeroArticles();
        newRefLoi = newDossier.getHabilitationRefLoi();

        MajMin maj = null;

        if (newRefLoi != null && !newRefLoi.equals("")) {
            final DocumentModel document = session.createDocumentModel(ActiviteNormativeConstants.MAJ_MIN_DOCUMENT_TYPE);
            document.setPathInfo(ActiviteNormativeConstants.MAJ_MIN_PATH + "/" + MAJ_TARGET.HABILITATION.toString().toLowerCase(), UUID.randomUUID()
                    .toString());
            final DocumentModel documentCreated = session.createDocument(document);
            maj = documentCreated.getAdapter(MajMin.class);
            if (oldRefLoi == null || oldRefLoi.equals("")) {
                maj.setModification(MAJ_TYPE.AJOUT);
            } else {
                maj.setModification(MAJ_TYPE.MODIFICATION);
            }
            maj.setIdDossier(newDossier.getDocument().getId());
            maj.setNorDossier(newDossier.getNumeroNor());
            maj.setDateCreation(Calendar.getInstance());

            maj.setNumeroArticle(newNumArticles);
            maj.setRefMesure(newDossier.getHabilitationNumeroOrdre());
            maj.setTitre(newDossier.getTitreActe());
            maj.setRef(newRefLoi);
            maj.setCommentMaj(newDossier.getHabilitationCommentaire());
            session.saveDocument(documentCreated);
        }

        return maj;
    }

}
