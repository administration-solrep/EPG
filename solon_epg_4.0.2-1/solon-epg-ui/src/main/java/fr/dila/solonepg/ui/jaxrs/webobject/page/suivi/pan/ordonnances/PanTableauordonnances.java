package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.ordonnances;

import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.lois.PanTableaulois;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanTableauordonnances")
public class PanTableauordonnances extends PanTableaulois {

    public PanTableauordonnances() {
        super();
    }

    @Override
    public Response exporterTableau() {
        return exporterTableauForSection(ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES);
    }
}
