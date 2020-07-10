import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.IdRef;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.TexteMaitre;

class CleanActNormative {

    public static final String MODE_INFO = "INFO";
    public static final String MODE_FIX = "FIX";

    public static ActiviteNormative getActiviteNormativeByUid(CoreSession session, String uid) {
        return session.getDocument(new IdRef(uid)).getAdapter(ActiviteNormative.class);
    }
    
    public static List<String> showMesuresKo(CoreSession session, ActiviteNormative activiteNormative) {
        TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);
        List<String> mesuresIds = texteMaitre.getMesuresIds();
        List<String> mesuresInexistantes = new ArrayList<String>();
        print "Début de verification des mesures enregistrées";
        for(String mesureId : mesuresIds) {
            if (!session.exists(new IdRef(mesureId))) {
                print "Mesure inconnue : id " + mesureId;
                mesuresInexistantes.add(mesureId);
            }
        }
        print "Fin de vérification des mesures enregistrées";
        return mesuresInexistantes;
    }
    
    public static void fixMesuresKo(CoreSession session, ActiviteNormative activiteNormative, List<String> mesuresToRemove) {
        TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);
        List<String> mesuresIds = texteMaitre.getMesuresIds();
        mesuresIds.removeAll(mesuresToRemove);
        texteMaitre.setMesuresIds(mesuresIds);
        texteMaitre.save(session);
	    session.save();
    }
    
    public static void run(CoreSession session, String uid, String mode) {        
        ActiviteNormative activiteNormative = getActiviteNormativeByUid(session, uid);
        List<String> mesuresInexistantes = showMesuresKo(session, activiteNormative);
        if (MODE_FIX.equals(mode)) {
             fixMesuresKo(session, activiteNormative, mesuresInexistantes);        
        } else if (!MODE_INFO.equals(mode)) {
            print "Mode non reconnu - Vous devez spécifier : -ctx \"mode='INFO'|'FIX'\" "
        }
    }
}


print "Début du script";
print "-------------------------------------------------------------------------------";
String id = Context.get("id");
String mode = Context.get("mode");

if(StringUtils.isBlank(id)) {
	print "Argument id non trouvé.";
    print "Fin du script - id n'a pas été renseigné";
    print "-------------------------------------------------------------------------------";
	return "Fin du script - id n'a pas ete renseigne";
}
if(StringUtils.isBlank(mode)) {
	print "Argument mode non trouvé. mode par défaut : 'INFO' ";
    mode = CleanActNormative.MODE_INFO;    
}
mode = mode.replace("'", "");
id = id.replace("'", "");
CleanActNormative.run (Session, id, mode)
print "-------------------------------------------------------------------------------";
print "Fin du script ";
return "Fin du script groovy";

