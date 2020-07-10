package fr.dila.solonepg.core.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.core.domain.ComplexeTypeImpl;

/**
 * Classe utilitaire pour les types complexes.
 * 
 * @author jgomez
 * 
 */
public class ComplexTypeUtil {

	private static final String	MIN_COMMENTAIRE					= "commentaire";

	private static final String	MIN_REF_MESURE					= "refMesure";

	private static final String	MIN_NUMERO_ARTICLES				= "numeroArticles";

	private static final String	MIN_TITRE						= "titre";

	private static final String	MIN_REFERENCE_KEY				= "ref";

	private static final String	MIN_ANNEE_REF_TRANSPO_DIRECTIVE	= "directiveAnnee";

	private static final Log	LOGGER							= LogFactory.getLog(ComplexTypeUtil.class);

	/**
	 * Récupère la liste des références d'un type complexe à partir de la liste du type complexe.
	 * 
	 * @param complextype
	 * @return la liste des référénces d'un type complexe à partir de la liste dutype complexe.
	 */
	public static List<String> getListeComplexeTypeRef(List<ComplexeType> complextype) {
		List<String> listeRef = new ArrayList<String>();
		for (ComplexeType complexeType : complextype) {
			listeRef.add((String) complexeType.getSerializableMap().get(
					DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY));
		}
		return listeRef;
	}

	public static ComplexeType createGenericTransposition(String ref, String titre, String numeroArticles,
			String refMesure, String commentaire, String annee) {
		ComplexeType generiqueTransposition = new ComplexeTypeImpl();
		Map<String, Serializable> contenuGeneriqueTransposition = new HashMap<String, Serializable>();
		contenuGeneriqueTransposition.put(MIN_REFERENCE_KEY, ref);
		contenuGeneriqueTransposition.put(MIN_TITRE, titre);
		contenuGeneriqueTransposition.put(MIN_NUMERO_ARTICLES, numeroArticles);
		contenuGeneriqueTransposition.put(MIN_REF_MESURE, annee + "/" + refMesure);
		contenuGeneriqueTransposition.put(MIN_COMMENTAIRE, commentaire);
		generiqueTransposition.setSerializableMap(contenuGeneriqueTransposition);
		return generiqueTransposition;
	}

	/**
	 * Convertit une liste de complexeType en une map ayant pour clé la référence des complexeType concaténée avec le
	 * numéro d'ordre si celui-ci existe.
	 * 
	 * @param list
	 *            la liste des complexe types
	 * @return une map
	 * @throws ClientException
	 */
	public static Map<String, ComplexeType> convertToMap(List<ComplexeType> list) throws ClientException {
		Map<String, ComplexeType> result = new HashMap<String, ComplexeType>();
		for (ComplexeType complex : list) {
			Map<String, Serializable> map = complex.getSerializableMap();
			if (map != null && map.containsKey(MIN_REFERENCE_KEY)) {
				Serializable reference = map.get(MIN_REFERENCE_KEY) + "/" + map.get(MIN_ANNEE_REF_TRANSPO_DIRECTIVE);
				Serializable numOrdre = map.get(MIN_REF_MESURE);
				String numOrdreStr = numOrdre == null ? "" : numOrdre.toString();
				result.put(reference + numOrdreStr, complex);
			} else {
				LOGGER.error("La map est nulle ou le complexType ne contient pas de clé référence" + map);
				throw new ClientException("La liste contient des complexeType incorrects");
			}
		}

		return result;
	}

	private ComplexTypeUtil() {
		// Private default constructor
	}
}
