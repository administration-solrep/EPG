package fr.dila.solonepg.core.bdj;

import fr.dila.solonepg.api.exception.WsBdjException;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Interface pour manipuler les appels webservice à BDJ
 * Utilise par  ActiviteNormativeServiceImpl
 * @author SPL
 *
 */
public interface WsBdj {
    void publierBilanSemestrielBDJ(CoreSession session, String bilanSemestrielXml) throws WsBdjException;

    String publierEcheancierBDJ(final CoreSession session, final String echeancierXmlStr) throws WsBdjException;
}
