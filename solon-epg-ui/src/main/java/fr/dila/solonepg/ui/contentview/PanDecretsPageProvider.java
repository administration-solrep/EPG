package fr.dila.solonepg.ui.contentview;

import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.core.dto.activitenormative.DecretDTOImpl;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class PanDecretsPageProvider extends AbstractIdsPanPageProvider<Decret, DecretDTOImpl> {
    private static final long serialVersionUID = 1L;

    private TexteMaitre texteMaitre = null;
    private MesureApplicative mesure = null;

    public void setTexteMaitre(TexteMaitre texteMaitre) {
        this.texteMaitre = texteMaitre;
    }

    public void setMesure(MesureApplicative mesure) {
        this.mesure = mesure;
    }

    @Override
    protected Decret getAdapter(DocumentModel documentModel) {
        return documentModel.getAdapter(Decret.class);
    }

    @Override
    protected DecretDTOImpl mapAdapterDocToDto(CoreSession session, Decret adapterDoc) {
        return new DecretDTOImpl(adapterDoc, null, mesure, texteMaitre);
    }

    @Override
    protected int defaultSort(Decret val1, Decret val2) {
        return val1.getNumeroNor().compareTo(val2.getNumeroNor());
    }
}
