package fr.dila.solonepg.core.feuilleroute;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.ecm.platform.routing.core.impl.ElementRunner;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.feuilleroute.SqueletteStepTypeDestinataire;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.core.feuilleroute.STRouteStepImpl;
import fr.dila.st.core.util.PropertyUtil;

/**
 * Implémentation d'une étape de feuille de route Solon EPG.
 * 
 * @author jtremeaux
 */
public class SolonEpgRouteStepImpl extends STRouteStepImpl implements SolonEpgRouteStep {
    /**
     * UID.
     */
    private static final long serialVersionUID = -7177633331090519475L;

    /**
     * Constructeur de SolonEpgRouteStepImpl.
     * 
     * @param doc Modèle de document
     * @param runner Exécuteur d'étape
     */
    public SolonEpgRouteStepImpl(DocumentModel doc, ElementRunner runner) {
        super(doc, runner);
    }

    @Override
    public boolean isPourInitialisation() {
        return VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION.equals(getType());
    }

    @Override
    public Long getNumeroVersion() {
        return PropertyUtil.getLongProperty(document,
                STSchemaConstant.ROUTING_TASK_SCHEMA,
                SolonEpgSchemaConstant.ROUTING_TASK_NUMERO_VERSION_PROPERTY);
    }

    @Override
    public void setNumeroVersion(Long numeroVersion) {
        PropertyUtil.setProperty(document,
                STSchemaConstant.ROUTING_TASK_SCHEMA,
                SolonEpgSchemaConstant.ROUTING_TASK_NUMERO_VERSION_PROPERTY, numeroVersion);
    }
    
	@Override
	public SqueletteStepTypeDestinataire getTypeDestinataire() {
		String prop = PropertyUtil.getStringProperty(document, STSchemaConstant.ROUTING_TASK_SCHEMA,
				SolonEpgSchemaConstant.ROUTING_TASK_TYPE_DESTINATAIRE_PROPERTY);
		if (prop == null) {
			return null;
		}
		return SqueletteStepTypeDestinataire.valueOf(prop);
	}

	@Override
	public void setTypeDestinataire(SqueletteStepTypeDestinataire typeDestinataire) {
		if (typeDestinataire == null) {
			PropertyUtil.setProperty(document, STSchemaConstant.ROUTING_TASK_SCHEMA,
					SolonEpgSchemaConstant.ROUTING_TASK_TYPE_DESTINATAIRE_PROPERTY, null);
		} else {
			PropertyUtil.setProperty(document, STSchemaConstant.ROUTING_TASK_SCHEMA,
					SolonEpgSchemaConstant.ROUTING_TASK_TYPE_DESTINATAIRE_PROPERTY, typeDestinataire.name());
		}
	}
}
