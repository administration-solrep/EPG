/**
 *
 */
package fr.dila.solonepg.core.administration;

import fr.dila.solonepg.api.administration.IndexationRubrique;
import fr.dila.solonepg.api.constant.SolonEpgIndextionConstants;
import fr.dila.st.core.domain.STDomainObjectImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * IndexationRubrique Implementation.
 *
 * @author asatre
 *
 */
public class IndexationRubriqueImpl extends STDomainObjectImpl implements IndexationRubrique {
    private static final long serialVersionUID = -3877605968865668665L;

    public IndexationRubriqueImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public String getIntitule() {
        return getStringProperty(
            SolonEpgIndextionConstants.INDEXATION_RUBRIQUE_SCHEMA,
            SolonEpgIndextionConstants.INDEXATION_RUBRIQUE_INTITULE
        );
    }

    @Override
    public void setIntitule(String intitule) {
        setProperty(
            SolonEpgIndextionConstants.INDEXATION_RUBRIQUE_SCHEMA,
            SolonEpgIndextionConstants.INDEXATION_RUBRIQUE_INTITULE,
            intitule
        );
    }
}
