package fr.dila.solonepg.core.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.common.utils.IdUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.runtime.api.Framework;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.attribute.meta.MetaValueTypeAttribute;
import org.odftoolkit.odfdom.dom.element.office.OfficeTextElement;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;
import org.odftoolkit.odfdom.pkg.OdfPackage;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.typescomplexe.InfoFeuilleStyleFile;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.service.FeuilleStyleService;
import fr.dila.solonepg.api.service.ParapheurModelService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.core.cases.typescomplexe.InfoFeuilleStyleFileImpl;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.SessionUtil;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.utils.SecureEntityResolver;

/**
 * @author ARN
 */
public class FeuilleStyleServiceImpl implements FeuilleStyleService {

	private static final long	serialVersionUID							= -9055009206780205690L;

	private static final Log	log											= LogFactory
																					.getLog(FeuilleStyleServiceImpl.class);

	private static final String	OPENOFFICE_VARIABLE_TAG_NAME				= "text:variable-set";

	private static final String	OPENOFFICE_VARIABLE_TEXT_NAME				= "text:name";

	private static final String	OPENOFFICE_METADONNEE_NAME					= "IdentifiantUnique";

	private static final String	OPENOFFICE_VARIABLE_TITRE_ACTE_NAME			= "titreActe";

	private static final String	OPENOFFICE_VARIABLE_SIGNATURE_NAME			= "dateSignatureActe";

	private static final String	OPENOFFICE_VARIABLE_NOR_NAME				= "nor";

	private static final String	OPENOFFICE_TEXT_DOCUMENT_FILE_EXTENSION		= "odt";

	/**
	 * Requête pour récupérer toutes les feuilles de styles d'un modèle de parapheur donné
	 */
	private static final String	GET_ALL_FEUILLES_STYLES_FOR_TYPE_ACTE_QUERY	= "select r.ecm:uuid as id from ParapheurFolder AS r WHERE isChildOf(r.ecm:uuid, select f.ecm:uuid from ModeleParapheur AS f WHERE f.ecm:uuid = ? ) = 1";

	/**
	 * url vers un fichier feuille de style
	 */
	private static final String	URL_FILE									= "nxfile/default/%s/%s/%s/file/%s";

	@Override
	public File checkFeuilleStyleValidAndTag(final File feuilleStyleFile, final CoreSession session,
			final boolean isActeIntegral) throws ClientException {
		log.debug("checkFeuilleStyleValidAndTag");
		// 1) récupération et validation du document texte openoffice
		final OdfTextDocument odfTextDoc = checkOdfTextDocumentValid(feuilleStyleFile, session, isActeIntegral);
		// si le fichier odfText est null, il est non valide
		if (odfTextDoc == null) {
			return null;
		}
		// 2) récupèration dans les metadonnées openoffice des propriété personnalisée
		final OdfOfficeMeta odfOfficeMeta = odfTextDoc.getOfficeMetadata();
		// generation d'un identifiant unique
		odfOfficeMeta.setUserDefinedData(OPENOFFICE_METADONNEE_NAME, MetaValueTypeAttribute.Value.STRING.toString(),
				IdUtils.generateStringId());

		// 3) on sauvegarde le fichier
		try {
			odfTextDoc.save(feuilleStyleFile);
		} catch (final Exception e) {
			log.warn("erreur lors de la sauvegarde du fichier!");
			return null;
		}

		return feuilleStyleFile;
	}

	@Override
	public String validateFeuilleStyle(final File feuilleStyleFile, final CoreSession session,
			final boolean isActeIntegral) throws ClientException {
		log.info("checkFeuilleStyleValidAndTag");

		final OdfTextDocument odfTextDoc = checkOdfTextDocumentValid(feuilleStyleFile, session, isActeIntegral);
		// si le fichier odfText est null, il est non valide
		if (odfTextDoc == null) {
			return null;
		}

		// 2) on ajoute une metadonne personnalisee

		// récupèration dans les metadonnées openoffice des propriété personnalisée
		final OdfOfficeMeta odfOfficeMeta = odfTextDoc.getOfficeMetadata();
		// generation id unique
		final String idUnique = IdUtils.generateStringId();
		// generation d'un identifiant unique
		odfOfficeMeta.setUserDefinedData(OPENOFFICE_METADONNEE_NAME, MetaValueTypeAttribute.Value.STRING.toString(),
				idUnique);

		// 3) on sauvegarde le fichier
		try {
			odfTextDoc.save(feuilleStyleFile);
		} catch (final Exception e) {
			log.warn("erreur lors de la sauvergarde du fichier!");
			return null;
		}
		return idUnique;
	}

	@Override
	public Boolean checkFeuilleStyleValid(final File feuilleStyleFile, final String feuilleStyleName,
			final CoreSession session, final boolean isActeIntegral) throws ClientException {
		log.info("checkFeuilleStyleValid");
		// on vérifie que le document a l'extension correspond à un document de type OpenOfficeText
		if (!hasOpenOfficeTextExtension(feuilleStyleName)) {
			return false;
		}

		// si le document openOffice ne contient pas les variables requise, on ne le considère pas comme valide
		if (checkOdfTextDocumentValid(feuilleStyleFile, session, isActeIntegral) == null) {
			return false;
		}
		return true;
	}

	protected OdfTextDocument checkOdfTextDocumentValid(final File feuilleStyleFile, final CoreSession session,
			final boolean isActeIntegral) {

		// Récupération du fichier openOffice writer
		Boolean checkedTitre = false;
		Boolean checkedDateSignature = false;
		Boolean checkedNor = false;
		OdfTextDocument odfTextDoc = null;
		try {
			final OdfPackage odfPackage = OdfPackage.loadPackage(feuilleStyleFile);
			odfTextDoc = (OdfTextDocument) OdfTextDocument.loadDocument(odfPackage);
			final Document document = getXMLContent(feuilleStyleFile);

			// 1) vérifications que les variabes titre de l'acte, dateSignature et NOR sont présentes et initialisé avec
			// leur id
			// exemple : la variable "titreActe" a pour valeur initialie "titreActe"
			final NodeList listVariable = document.getElementsByTagName(OPENOFFICE_VARIABLE_TAG_NAME);
			final int listVariableLength = listVariable.getLength();
			if (listVariableLength > 0) {
				// on parcourt la liste des variables du document
				for (int i = 0; i < listVariableLength; i++) {
					final Node variable = listVariable.item(i);
					final NamedNodeMap nameNodeMap = variable.getAttributes();
					final Node name = nameNodeMap.getNamedItem(OPENOFFICE_VARIABLE_TEXT_NAME);
					final String tempVariableName = name.getNodeValue();
					if (OPENOFFICE_VARIABLE_TITRE_ACTE_NAME.equals(tempVariableName)) {
						checkedTitre = true;
					} else if (OPENOFFICE_VARIABLE_SIGNATURE_NAME.equals(tempVariableName)) {
						checkedDateSignature = true;
					} else if (OPENOFFICE_VARIABLE_NOR_NAME.equals(tempVariableName)) {
						checkedNor = true;
					}
				}
			}
		} catch (final Exception e) {
			log.warn("erreur lors de la lecture des informations du fichier!");
		}
		// si le document openOffice ne contient pas les variables requise on ne renvoie pas le document :
		// si le document est un extrait, on ne tient pas compte du champ date
		if (!checkedTitre || !checkedNor || !checkedDateSignature && isActeIntegral) {
			return null;
		}
		return odfTextDoc;
	}

	// TODO put error in const
	@Override
	public String checkFichierParapheurValid(final File fichier, final String fileName, final String documentId,
			final CoreSession session, final DocumentModel dossier) throws ClientException {
		final DocumentModel document = session.getDocument(new IdRef(documentId));

		final Dossier dossierDoc = dossier.getAdapter(Dossier.class);
		if (dossierDoc == null) {
			log.debug("erreur : le dossier courant est nul!");
			return "erreur : le dossier courant est nul!";
		}

		// RG-DOS-TRT-09 : Un changement de la nature de l’acte en cours de parcours neutralise les contrôles réalisés
		// sur la feuille de style
		final Boolean isParapheurTypeActeUpdated = dossierDoc.getIsParapheurTypeActeUpdated();
		if (isParapheurTypeActeUpdated != null && isParapheurTypeActeUpdated) {
			log.info("le type d'acte a été changé en cours de parcours : on neutralise les contrôles sur la feuille de style");
			return null;
		}

		final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
		ParapheurFolder repertoireParapheur = null;
		// RG_ADM_DOS_009 : on effectue la vérification pour le document du parapheur la première version et pas pour
		// les précédentes
		if (!document.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_TYPE)) {

			final DocumentModel repertoire = session.getParentDocument(document.getRef());
			if (!repertoire.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_TYPE)) {
				log.info("Le répertoire ne fait pas parti du parapheur");
				return null;
			}

			if (!parapheurService.isFolderExtraitOrFolderActe(document, session)) {
				return null;
			}

			repertoireParapheur = repertoire.getAdapter(ParapheurFolder.class);

		} else if (!parapheurService.isFolderExtraitOrFolderActe(document, session)) {
			// RG-DOS-TRT-08 : contrôle de cohérence : on vérifie que le répertoire parent concerne type
			// "acte ou "extrait" du Parapheur
			log.info("ajout d'un fichier autre que acte ou extrait");
			return null;
		} else {
			repertoireParapheur = document.getAdapter(ParapheurFolder.class);
		}

		// on récupère les fichiers feuilles de style du répertoire

		final List<Map<String, Serializable>> currentFileList = repertoireParapheur.getFeuilleStyleFiles();
		// on récupére les identifiants des feuilles de style du répertoire
		final List<String> idfeuillestyleList = new ArrayList<String>();
		for (final Map<String, Serializable> hashMap : currentFileList) {
			idfeuillestyleList.add((String) hashMap.get(DossierSolonEpgConstants.FEUILLE_STYLE_ID_PROPERTY));
		}

		// récupération des formats autorisés
		final List<String> formatsAutorises = repertoireParapheur.getFormatsAutorises();

		// RG-DOS-TRT-08 : contrôle de cohérence : si aucune feuille de style n'est présente et qu'aucun format autorisé
		// et défini, on ne peut pas créer de fichier
		// Dans le cas de dossier issu de l'injection, la création de fichier sans feuille de style ni format est
		// autorisée
		if (idfeuillestyleList.size() < 1
				&& (formatsAutorises == null || formatsAutorises.size() < 1)
				&& (dossierDoc.getIsDossierIssuInjection() == null || !dossierDoc.getIsDossierIssuInjection()
						.booleanValue())) {
			log.debug("pas de feuille de style ni de formats autorisés !");
			return "pas de feuille de style ni de formats autorisés !";
		}

		// RG_ADM_DOS_009 : si l'acte ou l'extrait ne contient pas de feuille de style, le fichier est considéré comme
		// valide
		if (idfeuillestyleList.size() < 1) {
			log.debug("pas de fichier de feuille de style défini");
			return null;
		}

		// les feuilles de style s'appliquent uniquement sur les fichiers de type OpenOfficeText
		if (!hasOpenOfficeTextExtension(fileName)) {
			return "Le fichier n'est pas de type OpenOfficeWriter";
		}

		try {
			// Récupération du fichier openOffice writer
			final OdfPackage odfPackage = OdfPackage.loadPackage(fichier);
			final OdfTextDocument odfTextDoc = (OdfTextDocument) OdfTextDocument.loadDocument(odfPackage);
			// récupèration dans les metadonnées openoffice des propriété personnalisée
			final OdfOfficeMeta odfOfficeMeta = odfTextDoc.getOfficeMetadata();
			final String identifiantUniqueDocument = odfOfficeMeta.getUserDefinedDataValue(OPENOFFICE_METADONNEE_NAME);

			// si le fichier n'as pas d'identifiant unique, le fichier n'est pas valide
			if (identifiantUniqueDocument == null || identifiantUniqueDocument.isEmpty()
					|| !idfeuillestyleList.contains(identifiantUniqueDocument)) {
				log.debug("le fichier ne correspond pas à une des feuilles de style du dossier");
				return "le fichier ne correspond pas à une des feuilles de style du dossier";
			}

			// on récupère le nor du fichier
			final String norFichier = getNor(fichier);
			if (norFichier == null || norFichier.isEmpty()) {
				log.debug("le fichier n'a pas de numéro NOR");
				return null;
			}

			// on récupère le nor du dossier
			final String norDossier = dossierDoc.getNumeroNor();

			// RG-DOS-TRT-08 : si le fichier a un nor, il doit correspondre au nor du dossier
			if (norFichier.equals(norDossier)) {
				return null;
			}
			log.debug("le NOR du fichier ne correspond pas au NOR de l'acte");
			return "le NOR du fichier ne correspond pas au NOR de l'acte";
		} catch (final Exception e) {
			log.warn("erreur lors de la lecture des informations du fichier!");
		}
		return "erreur lors de la lecture des informations du fichier!";
	}

	@Override
	public String updateDossierMetadataFromParapheurFile(final CoreSession session, final InputStream fichierStream,
			final String fileName, final DocumentModel documentCourant, final DocumentModel dossierDoc)
			throws ClientException {

		final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();

		// les feuilles de style s'appliquent uniquement sur les fichiers de type OpenOfficeText
		if (!hasOpenOfficeTextExtension(fileName)) {
			return null;
		}

		// récupération du repertoire qui contient les fichiers
		DocumentModel repertoire = documentCourant;

		// vérifie que le fichier est un fichier ou un répertoire du parapheur
		if (!documentCourant.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE)) {
			if (documentCourant.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE)) {
				// si le documentModel courant est un fichier du parapheur, on récupère son répertoire
				repertoire = session.getParentDocument(documentCourant.getRef());
			}
		}

		// si le fichier n'est pas de type acte ou extrait, on ne cherche pas à mettre à jour les données
		if (!parapheurService.isFolderExtraitOrFolderActe(repertoire, session)) {
			log.debug("le fichier n'est pas ajouté dans l'un des répertoires acte ou extrait : on ne cherche pas à mettre à jour les données");
			return null;
		}

		// si le document du parapheur est un acte intégral, on doit récupèrer la date
		final ParapheurFolder parapheurFolder = repertoire.getAdapter(ParapheurFolder.class);
		final boolean isActeIntegral = parapheurService.isActeIntegral(parapheurFolder, session);

		String titreValue = null;
		String dateSignatureValue = null;
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		// Récupération du fichier openOffice writer
		try {
			// récupération du contenu XML du fichier
			final Document document = getXMLContent(fichierStream);
			// 1) récupération des variables titre de l'acte, dateSignature et NOR
			// exemple : la variable "titreActe" a pour valeur initialie "titreActe"
			final NodeList listVariable = document.getElementsByTagName(OPENOFFICE_VARIABLE_TAG_NAME);
			final int listVariableLength = listVariable.getLength();
			if (listVariableLength > 0) {
				// on parcourt la liste des variables du document
				for (int i = 0; i < listVariableLength; i++) {
					final Node variable = listVariable.item(i);
					// on récupère le nom de la variable
					final NamedNodeMap nameNodeMap = variable.getAttributes();
					final Node name = nameNodeMap.getNamedItem(OPENOFFICE_VARIABLE_TEXT_NAME);
					final String tempVariableName = name.getNodeValue();
					// on récupère la valeur de la variable
					final String variableValue = variable.getFirstChild().getNodeValue();
					if (OPENOFFICE_VARIABLE_TITRE_ACTE_NAME.equals(tempVariableName)) {
						titreValue = variableValue;
					} else if (OPENOFFICE_VARIABLE_SIGNATURE_NAME.equals(tempVariableName)) {
						dateSignatureValue = variableValue;
					}
				}
			}
		} catch (final Exception e) {
			log.warn(SolonEpgParapheurConstants.PARAPHEUR_ERROR_MSG_EXCEPTION_THROW);
			logErreurMiseAJourFeuilleStyle(session, dossier);
			return SolonEpgParapheurConstants.PARAPHEUR_ERROR_MSG_EXCEPTION_THROW;
		}

		// si le titre de l'acte et la date de signatures sont non nuls, on récupère ces valeurs
		if (StringUtils.isNotEmpty(titreValue)) {
			// on remplit le dossier avec les valeurs récupérés
			try {
				dossier.setTitreActe(titreValue);
				if (!isActeIntegral) {
					// si le document du parapheur est un extrait on ne cherche pas à récupérer la date de signature
					dossier.save(session);
					return null;
				}

				// on récupére la date de signature au format dd MMMMMMMMM yyyy
				final Calendar dateSignature = new GregorianCalendar();
				// si la date de signature est vide ou ne contient que des espaces, le date de signature devient vide
				final String testDateSignatureValue = dateSignatureValue;
				if (StringUtils.isEmpty(dateSignatureValue)) {
					log.info("les champs dateSignature de la feuille de style n'a pas pu être récupérés");
				} else if (testDateSignatureValue == null || testDateSignatureValue.replaceAll(" ", "").isEmpty()) {
					dossier.setDateSignature(null);
				} else {
					dateSignature.setTime(StringUtil.stringFormatWithLitteralMonthDateToDate(dateSignatureValue));
					dossier.setDateSignature(dateSignature);
					// on met à jour le champ caché du bordereau
					dossier.setIsParapheurFichierInfoNonRecupere(false);
					dossier.save(session);
					return null;
				}

			} catch (final NullPointerException e) {
				log.info("les champs titreActe et dateSignature de la feuille de style n'ont pas pu être récupérés");
			}
		}
		// on trace dans les logs que l'on a pas pu mettre à jour la feuille de style
		logErreurMiseAJourFeuilleStyle(session, dossier);
		return SolonEpgParapheurConstants.PARAPHEUR_ERROR_MSG_DATE_BAD_FORMAT;
	}

	/**
	 * Vérifie l'extension du document
	 * 
	 * @param fileName
	 * @return vrai si extension = odt - faux sinon
	 */
	protected boolean hasOpenOfficeTextExtension(final String fileName) {
		final String fileExtension = FileUtils.getFileExtension(fileName);
		if (!fileExtension.equals(OPENOFFICE_TEXT_DOCUMENT_FILE_EXTENSION)) {
			log.debug("le fichier n'est pas de type OpenOfficeWriter");
			return false;
		}
		return true;
	}

	/**
	 * enregistre dans un champ du dossier que les informations n'ont pas été enegistrée et trace l'information dans le
	 * journal.
	 * 
	 * @param session
	 * @param dossier
	 * @throws ClientException
	 */
	protected void logErreurMiseAJourFeuilleStyle(final CoreSession session, final Dossier dossier)
			throws ClientException {
		// RG-DOS-TRT-07 : Si la mise à jour n’est pas réalisable, parce que l’utilisateur n’a pas correctement exploité
		// le modèle de feuille de style, on trace dans le journal cette situation
		dossier.setIsParapheurFichierInfoNonRecupere(true);
		dossier.save(session);
		// on logge l'action
		final JournalService journalService = STServiceLocator.getJournalService();
		journalService.journaliserActionParapheur(session, dossier.getDocument(),
				SolonEpgEventConstant.FAIL_GET_FILE_INFO_PARAPHEUR,
				SolonEpgEventConstant.COMMENT_FAIL_GET_FILE_INFO_PARAPHEUR);
	}

	/**
	 * Renvoie le nor d'un fichier.
	 * 
	 * @param fichier
	 * @return String
	 */
	protected String getNor(final File fichier) {
		String norValue = null;
		try {
			// récupération du contenu XML du fichier
			final Document document = getXMLContent(fichier);
			// 1) récupération de la variable NOR
			final NodeList listVariable = document.getElementsByTagName(OPENOFFICE_VARIABLE_TAG_NAME);
			final int listVariableLength = listVariable.getLength();
			if (listVariableLength > 0) {
				// on parcourt la liste des variables du document
				for (int i = 0; i < listVariableLength; i++) {
					final Node variable = listVariable.item(i);
					// on récupère le nom de la variable
					final NamedNodeMap nameNodeMap = variable.getAttributes();
					final Node name = nameNodeMap.getNamedItem(OPENOFFICE_VARIABLE_TEXT_NAME);
					final String tempVariableName = name.getNodeValue();
					// on récupère la valeur de la variable
					final String variableValue = variable.getFirstChild().getNodeValue();
					if (OPENOFFICE_VARIABLE_NOR_NAME.equals(tempVariableName)) {
						norValue = variableValue;
					}
				}
			}
		} catch (final Exception e) {
			log.warn("erreur lors de la lecture de la métadonnée 'numéro nor'");
			return norValue;
		}
		return norValue;
	}

	/**
	 * Récupération du contenu XML d'un fichier openOffice Writer
	 * 
	 * @param fichier
	 * @return Document
	 * @throws Exception
	 */
	protected Document getXMLContent(final File fichier) throws Exception {
		// Récupération du fichier openOffice writer
		final OdfPackage odfPackage = OdfPackage.loadPackage(fichier);
		return buildXMLContent(odfPackage);
	}

	/**
	 * Récupération du contenu XML d'un fichier openOffice Writer
	 * 
	 * @param fichier
	 *            :inputStream
	 * @return Document
	 * @throws Exception
	 */
	protected Document getXMLContent(final InputStream fichierStream) throws Exception {
		// Récupération du fichier openOffice writer
		final OdfPackage odfPackage = OdfPackage.loadPackage(fichierStream);
		return buildXMLContent(odfPackage);
	}

	/**
	 * construit un fichier xml à partir d'un objet OdfPackage
	 * 
	 * @param odfPackage
	 * @return Document
	 * @throws Exception
	 */
	protected Document buildXMLContent(final OdfPackage odfPackage) throws Exception {
		final OdfTextDocument odfTextDoc = (OdfTextDocument) OdfTextDocument.loadDocument(odfPackage);
		// récupération du contenu texte du document openOffice
		final OfficeTextElement officeText = odfTextDoc.getContentRoot();
		// parsage du contenu texte du document openOffice
		final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		// création d'un constructeur de documents
		final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		docBuilder.setEntityResolver(new SecureEntityResolver());
		// definition de la source
		final ByteArrayInputStream bais = new ByteArrayInputStream(officeText.toString().getBytes());
		// lecture du contenu du fichier XML avec DOM
		return docBuilder.parse(bais);

	}

	@Override
	public List<InfoFeuilleStyleFile> getFeuilleStyleListFromTypeActe(final String typeActe) throws Exception {
		// on parcourt les éléments fils du modèle de parapheur pour récupérer les feuilles de styles
		final List<InfoFeuilleStyleFile> feuilleStyleList = new ArrayList<InfoFeuilleStyleFile>();
		if (typeActe == null || typeActe.isEmpty() || typeActe.equals("-")) {
			return feuilleStyleList;
		}
		LoginContext loginContext = null;
		CoreSession session = null;
		try {
			// on se connecte à l'application en tant que super utilisateur
			loginContext = Framework.login();
			session = SessionUtil.getCoreSession();

			// récupération du modèle de parapheur associé au type d'acte
			final ParapheurModelService parapheurModelService = SolonEpgServiceLocator.getParapheurModelService();
			final DocumentModel parapheurModel = parapheurModelService.getParapheurModelFromTypeActe(session, typeActe);

			// on effectue une requete FNXQL pour récupérer toutes les feuilles de styles du modèle de parapheur

			final Object params[] = new Object[] { parapheurModel.getId() };
			final List<DocumentModel> feuilleStyleListDocModel = QueryUtils
					.doUnrestrictedUFNXQLQueryAndFetchForDocuments(session, "ParapheurFolder",
							GET_ALL_FEUILLES_STYLES_FOR_TYPE_ACTE_QUERY, params);
			if (feuilleStyleListDocModel != null && feuilleStyleListDocModel.size() > 0) {

				// parcours des répertoires contenant les feuilles de style
				for (final DocumentModel feuilleStyle : feuilleStyleListDocModel) {

					final String id = feuilleStyle.getId();
					final ParapheurFolder feuilleStyleDoc = feuilleStyle.getAdapter(ParapheurFolder.class);
					final List<Map<String, Serializable>> listFile = feuilleStyleDoc.getFeuilleStyleFiles();
					if (listFile != null && listFile.size() > 0) {

						int iterateur = 0;
						// parcours de la liste contenant les informations des feuilles de style
						for (final Map<String, Serializable> fileMap : listFile) {
							final InfoFeuilleStyleFile infoFeuilleStyle = new InfoFeuilleStyleFileImpl();
							final String fileName = (String) fileMap.get("filename");
							final String url = String
									.format(URL_FILE,
											id,
											SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA
													+ ":"
													+ SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_FEUILLE_STYLE_FILES_PROPERTY,
											iterateur, fileName);

							// ajout des informations sur le fichier feuille de style dans la liste des fichiers à
							// afficher
							infoFeuilleStyle.setId(id);
							infoFeuilleStyle.setUrl(url);
							infoFeuilleStyle.setPos(iterateur);
							infoFeuilleStyle.setFileName(fileName);
							feuilleStyleList.add(infoFeuilleStyle);
							iterateur = iterateur + 1;
						}
					}
				}
			}
		} catch (final Exception e) {
			throw new ClientException("erreur lors de la récupération des feuilles de style");
		} finally {
			SessionUtil.close(session);
			if (loginContext != null) {
				loginContext.logout();
			}
		}
		return feuilleStyleList;
	}

	@Override
	public InfoFeuilleStyleFile getFeuilleStyleInfo(final String docId, final String pos) throws Exception {
		final InfoFeuilleStyleFile infoFeuilleStyleFinal = new InfoFeuilleStyleFileImpl();
		LoginContext loginContext = null;
		CoreSession session = null;
		try {
			// on se connecte à l'application en tant que super utilisateur
			loginContext = Framework.login();
			session = SessionUtil.getCoreSession();

			final DocumentModel repertoireFeuillesStyles = session.getDocument(new IdRef(docId));

			final ParapheurFolder feuilleStyleDoc = repertoireFeuillesStyles.getAdapter(ParapheurFolder.class);
			final List<Map<String, Serializable>> listFile = feuilleStyleDoc.getFeuilleStyleFiles();
			if (listFile != null && listFile.size() > 0) {
				int iterateur = 0;
				// parcourt de la liste contenant les informations des feuilles de styles
				for (final Map<String, Serializable> fileMap : listFile) {
					final String fileNameFile = (String) fileMap.get("filename");
					final Blob blob = (Blob) fileMap.get("file");
					final String fileExtension = blob.getMimeType();
					if (pos != null && Integer.parseInt(pos) == iterateur) {
						final String url = String.format(URL_FILE, docId,
								"parapheur_folder_solon_epg:feuilleStyleFiles", iterateur, fileNameFile);
						infoFeuilleStyleFinal.setUrl(url);
						infoFeuilleStyleFinal.setFileName(fileNameFile);
						infoFeuilleStyleFinal.setFileExtension(fileExtension);
						infoFeuilleStyleFinal.setBlob(blob);
					}
					iterateur = iterateur + 1;
				}
			}
		} catch (final Exception e) {
			throw new ClientException("erreur lors de la récupération des feuilles de style");
		} finally {
			SessionUtil.close(session);
			if (loginContext != null) {
				loginContext.logout();
			}
		}
		return infoFeuilleStyleFinal;
	}

}
