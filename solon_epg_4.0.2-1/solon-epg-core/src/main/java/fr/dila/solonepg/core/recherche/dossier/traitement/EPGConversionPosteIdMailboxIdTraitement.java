package fr.dila.solonepg.core.recherche.dossier.traitement;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.recherche.RequeteTraitement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Convertit l'identifiant de poste en identifiant de mailbox et le place dans la requête pour pouvoir chercher sur ce champ.
 * @author jgomez
 *
 */
public class EPGConversionPosteIdMailboxIdTraitement implements RequeteTraitement<RequeteDossierSimple> {

    @Override
    public void doBeforeQuery(RequeteDossierSimple requeteSimple) {
        requeteSimple.setEtapeDistributionMailboxId(getMailboxIdFromPoste(requeteSimple));
    }

    @Override
    public void doAfterQuery(RequeteDossierSimple requeteSimple) {
        return;
    }

    @Override
    public void init(RequeteDossierSimple requeteSimple) {
        return;
    }

    protected List<String> getMailboxIdFromPoste(RequeteDossierSimple requeteSimple) {
        List<String> idPosteList = requeteSimple.getEtapeIdPoste();
        List<String> idMailboxList = new ArrayList<String>();
        if (idPosteList.isEmpty() || idPosteList == null) {
            return null;
        }
        for (String idPoste : idPosteList) {
            idMailboxList.add(STConstant.PREFIX_POSTE + idPoste);
        }
        return idMailboxList;
    }
}
