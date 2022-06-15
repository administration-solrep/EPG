package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.api.service.EpgExcelService;
import fr.dila.ss.core.enumeration.StatutModeleFDR;
import fr.dila.ss.core.service.SSExcelServiceImpl;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.api.service.organigramme.OrganigrammeService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.STExcelUtil;
import fr.dila.st.core.util.SolonDateConverter;
import fr.sword.naiad.nuxeo.commons.core.schema.DublincorePropertyUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.NuxeoException;

public class EpgExcelServiceImpl extends SSExcelServiceImpl implements EpgExcelService {
    public static final String MODELES_SHEET_NAME = "Resultats_recherche_modeles";

    private static final String[] MODELE_HEADER = {
        ResourceHelper.getString("label.excel.modele.etat"),
        ResourceHelper.getString("label.excel.modele.intitule"),
        ResourceHelper.getString("label.excel.modele.numero"),
        ResourceHelper.getString("label.excel.modele.ministere"),
        ResourceHelper.getString("label.excel.modele.auteur"),
        ResourceHelper.getString("label.excel.modele.derniere.modif")
    };

    protected String[] getListModelesHeader() {
        return MODELE_HEADER;
    }

    @Override
    public Consumer<OutputStream> creationExcelListModeleFdr(CoreSession session, List<DocumentRef> idsModeles) {
        try (HSSFWorkbook wb = initExcelFile(MODELES_SHEET_NAME, getListModelesHeader())) {
            HSSFSheet sheet = wb.getSheet(MODELES_SHEET_NAME);

            List<DocumentRef> modelesExistants = idsModeles
                .stream()
                .filter(session::exists)
                .collect(Collectors.toList());
            IntStream
                .range(0, modelesExistants.size())
                .boxed()
                .map(i -> new AbstractMap.SimpleImmutableEntry<>(i + 1, session.getDocument(modelesExistants.get(i))))
                .forEach(e -> addDocumentToSheet(session, sheet, e.getKey(), e.getValue()));

            STExcelUtil.formatStyle(wb, MODELES_SHEET_NAME, getListModelesHeader().length);

            return outputStream -> {
                try {
                    wb.write(outputStream);
                } catch (IOException exc) {
                    throw new NuxeoException(exc);
                }
            };
        } catch (IOException exc) {
            throw new NuxeoException(exc);
        }
    }

    @Override
    protected void addDocumentToSheet(
        final CoreSession session,
        final HSSFSheet sheet,
        final int numRow,
        final DocumentModel document
    ) {
        if (document == null) {
            return;
        }

        SolonEpgFeuilleRoute modele = document.getAdapter(SolonEpgFeuilleRoute.class);
        OrganigrammeService organigrammeService = STServiceLocator.getOrganigrammeService();

        String etat = StatutModeleFDR.getStatutFromDoc(document);
        String intitule = modele.getTitle();
        STUserService userService = STServiceLocator.getSTUserService();
        String auteur = userService.getUserFullName(DublincorePropertyUtil.getCreator(document));
        String ministereId = modele.getMinistere();
        String ministere = StringUtils.isNotEmpty(ministereId)
            ? organigrammeService.getOrganigrammeNodeById(ministereId, OrganigrammeType.MINISTERE).getLabel()
            : "";
        Calendar modifiedDate = DublincorePropertyUtil.getModificationDate(document);

        if (modele != null) {
            HSSFRow currentRow = sheet.createRow(numRow);
            addCellToRow(
                currentRow,
                0,
                etat,
                intitule,
                modele.getNumero().toString(),
                ministere,
                auteur,
                SolonDateConverter.DATE_SLASH.format(modifiedDate)
            );
        }
    }
}
