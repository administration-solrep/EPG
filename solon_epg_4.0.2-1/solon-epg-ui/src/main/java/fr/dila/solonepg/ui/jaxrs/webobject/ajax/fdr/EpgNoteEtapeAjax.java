package fr.dila.solonepg.ui.jaxrs.webobject.ajax.fdr;

import fr.dila.ss.ui.bean.fdr.NoteEtapeFormDTO;
import fr.dila.ss.ui.jaxrs.webobject.ajax.etape.SSNoteEtapeAjax;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AjaxNoteEtape")
public class EpgNoteEtapeAjax extends SSNoteEtapeAjax {

    @Path("shwoSelectOrganigrammeRestriction")
    @POST
    public ThTemplate showSelectOrganigramme(@FormParam("typeRestriction") String typeRestriction) {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/fdr/organigrammeSelectRestrictionNoteEtape", context);
        template.getData().put(SSTemplateConstants.NOTE_DTO, new NoteEtapeFormDTO(typeRestriction));
        return template;
    }
}
