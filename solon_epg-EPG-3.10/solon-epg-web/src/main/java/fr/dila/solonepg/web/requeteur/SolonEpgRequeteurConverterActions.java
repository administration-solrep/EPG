package fr.dila.solonepg.web.requeteur;

import java.util.Map;

import javax.faces.convert.Converter;

import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;

import fr.dila.solonepg.api.constant.SolonEpgRequeteurConstant;
import fr.dila.st.web.converter.VocabularyIdsConverter;
import fr.dila.st.web.requeteur.STRequeteurConverterActions;

/**
 * Bean seam de conversion pour les requêteurs experts
 * 
 * @author jgomez
 * 
 */
@Name("stRequeteurConverterActions")
@Install(precedence = Install.APPLICATION + 1)
public class SolonEpgRequeteurConverterActions extends STRequeteurConverterActions {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1917033806173724435L;

	/**
	 * Retourne une map <type, converter>
	 * 
	 * @return une map qui représente une équivalence entre un type de widget et un converter de valeur.
	 */
	@Override
	protected Map<String, Class<? extends Converter>> getTypeToConverterClassMap() {
		Map<String, Class<? extends Converter>> typeToConverterClassMap = super.getTypeToConverterClassMap();
		typeToConverterClassMap.put(SolonEpgRequeteurConstant.REQUETEUR_TYPE_DISPOSITION_HABILITATION, VocabularyIdsConverter.class);
		return typeToConverterClassMap;
	}
}
