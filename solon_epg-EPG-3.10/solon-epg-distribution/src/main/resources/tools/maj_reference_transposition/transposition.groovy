import java.util.Collections;
import org.nuxeo.common.utils.*;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.common.utils.IdUtils;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.query.FlexibleQueryMaker;

// Script groovy pour dénormaliser les références d'application lois et de transposition ordonnance

void denormaliserTranspositionOrdonnance(dossier){
	if (dossier !=null){
		dossier.setTranspositionOrdonnance(dossier.getTranspositionOrdonnance());
		Session.saveDocument(dossier.getDocument());
		Session.save();
		System.out.println(dossier.getTranspositionOrdonnance());
	}
	else{
		System.out.println("Erreur, le dossier est nul");
	}
}
	
void denormaliserApplicationLoi(dossier){
	if (dossier !=null){
			dossier.setApplicationLoi(dossier.getApplicationLoi());
			Session.saveDocument(dossier.getDocument());
			Session.save();
			System.out.println(dossier.getApplicationLoi());
	}
	else{
		System.out.println("Erreur, le dossier est nul");
	}
}

void denormaliser(dossier,type){
	if (type.equals("dos:transpositionOrdonnance")){
		denormaliserTranspositionOrdonnance(dossier);
	}
	else{
		denormaliserApplicationLoi(dossier);
	}	

}
class Utils{

	
	static String makeQuery(type){
		return "sql:[id]SELECT DISTINCT H.PARENTID AS id FROM HIERARCHY H INNER JOIN TRANPOSITION T ON T.ID = H.ID WHERE H.NAME = '" + type + "'";
	}
	
}
void denormaliserTous(type){
	System.out.println("Dénormalisation " + type);
	def query = Utils.makeQuery(type);
	def dossiers = Session.queryAndFetch(query, FlexibleQueryMaker.QUERY_TYPE, null);
	
	dossiers.each{
		System.out.println("Modification du dossier " + it["id"]);
		def doc = Session.getDocument(new IdRef(it["id"]));
		def dossier = doc.getAdapter(Dossier.class);
		denormaliser(dossier,type);
	}
	dossiers.close();
}

types = ["dos:transpositionOrdonnance","dos:applicationLoi"]
types.each{
	denormaliserTous(it);
}

return "Fin de script";
