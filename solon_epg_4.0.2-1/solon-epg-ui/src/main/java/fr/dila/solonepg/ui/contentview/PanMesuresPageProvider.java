package fr.dila.solonepg.ui.contentview;

import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.core.dto.activitenormative.MesureApplicativeDTOImpl;
import org.apache.commons.lang3.math.NumberUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class PanMesuresPageProvider extends AbstractIdsPanPageProvider<MesureApplicative, MesureApplicativeDTOImpl> {
    private static final long serialVersionUID = 1L;

    private TexteMaitre texteMaitre = null;

    public void setTexteMaitre(TexteMaitre texteMaitre) {
        this.texteMaitre = texteMaitre;
    }

    @Override
    protected MesureApplicative getAdapter(DocumentModel documentModel) {
        return documentModel.getAdapter(MesureApplicative.class);
    }

    @Override
    protected MesureApplicativeDTOImpl mapAdapterDocToDto(CoreSession session, MesureApplicative adapterDoc) {
        return new MesureApplicativeDTOImpl(adapterDoc, texteMaitre);
    }

    @Override
    protected int defaultSort(MesureApplicative val1, MesureApplicative val2) {
        return Integer.compare(
            NumberUtils.toInt(val1.getNumeroOrdre(), -1),
            NumberUtils.toInt(val2.getNumeroOrdre(), -1)
        );
    }
}
