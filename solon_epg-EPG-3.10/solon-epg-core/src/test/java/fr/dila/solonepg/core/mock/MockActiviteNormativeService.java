package fr.dila.solonepg.core.mock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import edu.emory.mathcs.backport.java.util.Arrays;
import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.core.service.ActiviteNormativeServiceImpl;

public class MockActiviteNormativeService extends ActiviteNormativeServiceImpl implements ActiviteNormativeService {

	private static final long serialVersionUID = 1L;

	private Map<String, MesureApplicative> mapMesures;
	private Map<String, Decret> mapDecrets;

	public MockActiviteNormativeService() {
		initDecrets();
		initMesures();
	}

	@Override
	public List<MesureApplicative> fetchMesure(List<String> listIds, CoreSession session) throws ClientException {
		List<MesureApplicative> mesures = new ArrayList<MesureApplicative>();

		for (String mesureId : listIds) {
			mesures.add(mapMesures.get(mesureId));
		}

		return mesures;
	}

	@Override
	public List<Decret> fetchDecrets(List<String> listIds, CoreSession session) throws ClientException {
		List<Decret> decrets = new ArrayList<Decret>();

		for (String decretId : listIds) {
			decrets.add(mapDecrets.get(decretId));
		}

		return decrets;
	}

	private void initDecrets() {
		mapDecrets = new HashMap<String, Decret>();

		mapDecrets.put("INTD1801247D", new MockDecret("INTD1801247D",
				"Décret n° 2018-167 du 7 mars 2018 pris pour application de l'article 6 de la loi n° 55-385 du 3 avril 1955 relative à l'état d'urgence et de l'article L. 228-3 du code de la sécurité intérieure, et relatif au placement sous surveillance électronique mobile"));

		mapDecrets.put("INTD1909999D", new MockDecret("INTD1909999D", null));
	}

	private void initMesures() {
		mapMesures = new HashMap<String, MesureApplicative>();

		addMesure("1", 2, "Article 3, 2°", "Article L228-3, code de la sécurité intérieure",
				"Placement sous surveillance électronique mobile de toute personne à l’égard de laquelle il existe des raisons sérieuses de penser que son comportement constitue une menace d’une particulière gravité pour la sécurité et l’ordre publics",
				Arrays.asList(new String[] { "INTD1801247D", "INTD1909999D" }));

		addMesure("2", 2, "Article 11, I, 3°", "Article L114-1, code de la sécurité intérieure, I",
				"Modalités d'information des personnes intéressées de la consultation des traitements automatisés de données à caractère personnel relevant de l’article 26 de la loi n° 78-17 du 6 janvier 1978 relative à l’informatique, aux fichiers et aux libertés lors des enquêtes administratives destinées aux fins de vérifier que le comportement ces personnes n'est pas incompatible avec l'exercice des fonctions ou des missions envisagées",
				new ArrayList<String>());

		addMesure("3", 2, "Article 11, I, 3°", "Article L114-1, code de la sécurité intérieure, IV",
				"Composition et fonctionnement de l'organisme paritaire chargé de donner un avis préalable à la mutation ou à la radiation des cadres du fonctionnaire occupant un emploi participant à l’exercice de missions de souveraineté de l’État ou relevant du domaine de la sécurité ou de la défense et dont le comportement est devenu incompatible avec l’exercice de ses fonctions",
				new ArrayList<String>());

		addMesure("4", 3, "Article 11, II, 1°", "Article L4125-1, code de la défense",
				"Conditions d'exercice du recours administratif préalable aux recours contentieux formés par les militaires mentionnés à l’article L. 4111-2 à l’encontre d’actes relatifs à leur situation personnelle",
				new ArrayList<String>());

		addMesure("5", 3, "Article11, II, 2°, b) ", "Article L4139-15-1, code de la défense",
				"Composition et fonctionnement du conseil chargé de donner un avis préalable à la radiation des cadres ou à la résiliation du contrat du militaire dont le comportement est devenu incompatible avec l’exercice de ses fonctions eu égard à la menace grave qu’il fait peser sur la sécurité publique",
				new ArrayList<String>());

		addMesure("6", 3, "Article 11, II, 2°, b)", "Article L4139-15-1, code de la défense",
				"Radiation des cadres ou à la résiliation du contrat des militaires dont le comportement est devenu incompatible avec l’exercice de leurs fonctions eu égard à la menace grave qu’il font peser sur la sécurité publique",
				new ArrayList<String>());

		addMesure("7", 6, "Article 14, 1°", "Article L232-7-1, VI, code de la sécurité intérieure",
				"PNR maritime: traitement automatisé de données à caractère personnel mis en place pour les besoins de la prévention et de la constatation de certaines infractions, du rassemblement des preuves de ces infractions ainsi que de la recherche de leurs auteurs d'actes de terrorisme, d'atteintes aux intérêts fondamentaux de la Nation ainsi que les infractions mentionnées à l’article 694-32 du code de procédure pénale",
				new ArrayList<String>());

		addMesure("8", 3, "Article 15, I, 4°", "Article L852-2, code de la sécurité intérieure",
				"Liste des services intéressés pouvant être autorisés à réaliser des interceptions de correspondances échangées au sein d’un réseau de communications électroniques empruntant exclusivement la voie hertzienne et n’impliquant pas l’intervention d’un opérateur de communications électroniques, lorsque ce réseau est conçu pour une utilisation privative par une personne ou un groupe fermé d’utilisateurs, ainsi que la ou les finalités concernées, en application de l'article L. 811-4 du CSI",
				new ArrayList<String>());
	}

	private void addMesure(String numero, int month, String article, String baseLegale, String objet,
			List<String> decretIds) {
		Calendar cal = Calendar.getInstance();
		cal.set(2018, month, 1);

		MockMesureApplicative mesure = new MockMesureApplicative();
		mesure.setNumeroOrdre(numero);
		mesure.setArticle(article);
		mesure.setBaseLegale(baseLegale);
		mesure.setObjetRIM(objet);
		mesure.setDateObjectifPublication(cal);
		mesure.setDecretIds(decretIds);

		mapMesures.put(numero, mesure);
	}

}
