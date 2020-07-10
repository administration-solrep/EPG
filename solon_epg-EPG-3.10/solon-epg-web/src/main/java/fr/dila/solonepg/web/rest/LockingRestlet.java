package fr.dila.solonepg.web.rest;

import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMDocumentFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.w3c.dom.Element;

import fr.dila.st.core.service.STServiceLocator;

/**
 * surcharge de {@link org.nuxeo.ecm.platform.ui.web.restAPI.LockingRestlet} pour ne pas vérifier les locks
 * 
 * @author asatre
 * 
 */
public class LockingRestlet extends org.nuxeo.ecm.platform.ui.web.restAPI.LockingRestlet {

    @Override
    protected void doHandleStatelessRequest(Request req, Response res) {

        String repoId = (String) req.getAttributes().get("repo");
        String docid = (String) req.getAttributes().get("docid");

        DOMDocumentFactory domFactory = new DOMDocumentFactory();
        DOMDocument result = (DOMDocument) domFactory.createDocument();

        // init repo and document
        boolean initOk = initRepositoryAndTargetDocument(res, repoId, docid);
        if (!initOk) {
            return;
        }

        try {
            // force remove lock
            STServiceLocator.getSTLockService().unlockDocUnrestricted(session, targetDocument);
        } catch (ClientException e) {
            // ignoring
        }

        Element current = result.createElement("document");
        current.setAttribute("code", SC_LOCKINFO_NOT_LOCKED);
        current.setAttribute("message", "");
        result.setRootElement((org.dom4j.Element) current);
        res.setEntity(result.asXML(), MediaType.TEXT_XML);
        res.getEntity().setCharacterSet(CharacterSet.UTF_8);
    }

}
