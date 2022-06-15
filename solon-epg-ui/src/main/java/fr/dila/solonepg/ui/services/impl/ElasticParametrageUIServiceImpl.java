package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.ui.bean.parametres.ParametreESDTO;
import fr.dila.solonepg.ui.bean.parametres.ParametreESList;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.ElasticParametrageUIService;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import java.util.stream.Collectors;
import org.nuxeo.ecm.core.api.DocumentModel;

public class ElasticParametrageUIServiceImpl implements ElasticParametrageUIService {

    @Override
    public ParametreESList handleListResult(SpecificContext context) {
        List<DocumentModel> docs = context.getFromContextData(EpgContextDataKey.PARAMETRES);
        ParametreESList listResult = new ParametreESList();
        listResult.setListe(
            docs.stream().map(doc -> MapDoc2Bean.docToBean(doc, ParametreESDTO.class)).collect(Collectors.toList())
        );
        listResult.setNbTotal(docs.size());

        return listResult;
    }
}
