/**
 *
 */
package fr.dila.solonepg.core.parapheur;

import fr.dila.solonepg.api.parapheur.ParapheurModelRoot;
import fr.dila.st.core.domain.STDomainObjectImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * @author antoine Rolin
 *
 */
public class ParapheurModelRootImpl extends STDomainObjectImpl implements ParapheurModelRoot {
    private static final long serialVersionUID = 4011766315047584157L;

    /**
     * @param document
     */
    public ParapheurModelRootImpl(DocumentModel document) {
        super(document);
    }
}
