import java.util.Collections;
import org.nuxeo.common.utils.IdUtils;

import java.util.Collections;
import org.nuxeo.common.utils.*;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.common.utils.IdUtils;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.api.service.SecurityService;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgAclConstant;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import org.nuxeo.ecm.core.api.security.ACP;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import fr.dila.st.core.service.STServiceLocator;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;

/**
 * Contexte :  Un certain nombre de dossiers ont été envoyés au service de publication trop tôt,
 * avec le changement de gouvernement les nors ont été changés.
 * Le script suivant doit permettre de remettre les bons nors sur les dossiers 
 * On a en entrée :	
 * - un fichier contenant la correspondance ancien ministère, nouveau ministère (trigramme et identifiant ldap)
 * - un fichier contenant les nors des dossiers concernés 
 *
 */


class Ministere{
	private String trigramme, id;
	
	public Ministere(trigramme,id){
		this.trigramme = trigramme;
		this.id = id;
	}	
}

class Correspondance{
	private Ministere oldMinistere,newMinistere;
	
	public Correspondance(oldMinistere,newMinistere){
		this.oldMinistere = oldMinistere;
		this.newMinistere = newMinistere;
	}
}

class CorrespondanceSet{

	private List<Correspondance> correspondances;
	
	public CorrespondanceSet(){
		correspondances = new ArrayList<Correspondance>();
	}
	
	void loadMinistere(correspondances_file){
		new File(correspondances_file).splitEachLine(":") { fields ->
			correspondances.add(new Correspondance(new Ministere(fields[0],fields[1]), new Ministere(fields[2],fields[3])));
		}	
	}
	
	Correspondance getCorrespondance(nor){
		for (c in correspondances){
			if (c.oldMinistere.trigramme.equals(nor.substring(0,3))){
				return c;
			}
		}
		return null;
	}
}

class DossierUtils{


	static String getNewNumero(oldNumeroNor,newMinistere){
		String trigramme = oldNumeroNor.substring(0,3);
		String newNumeroNor = oldNumeroNor.replace(trigramme, newMinistere.trigramme);
		return newNumeroNor;
	}
	
	static void rattacherDossier(session,dossier,correspond){
		if (dossier !=null){
			dossier.setMinistereResp(correspond.oldMinistere.id);
			String newNumero = getNewNumero(dossier.getNumeroNor(),correspond.oldMinistere);
			dossier.setNumeroNor(newNumero);
			session.saveDocument(dossier.getDocument());
			session.save();
			print("Dossier " + dossier.getNumeroNor() + " modifié");
		}
		else{
			print("Erreur, le dossier est nul");
		}
	}
	
	static getDossierFromBdd(session,nor){
		String selectDossierPourRattachement = "sql:[id]SELECT D.ID AS ID FROM DOSSIER_SOLON_EPG D WHERE D.NUMERONOR ='" + nor+"'";
		def dossiers = session.queryAndFetch(selectDossierPourRattachement, FlexibleQueryMaker.QUERY_TYPE, null);
		List<Dossier> results = new ArrayList<Dossier>();
		dossiers.each{
			def doc = session.getDocument(new IdRef(it["id"]));
			def dossier = doc.getAdapter(Dossier.class);
			results.add(dossier);
		}
		dossiers.close();
		Dossier result = null;
		if (results != null && !results.isEmpty()){
			result = results.get(0);
		}
		return result;
	}
	
	public static rattacherTous(session,set,nor_file){
		new File(nor_file).splitEachLine(" ") { fields ->
			String nor = fields[0];
			//print "Sélection du nor : " + nor;
			Correspondance correspond = set.getCorrespondance(nor);
			if (correspond == null){
				//print nor + " n'appartient pas aux ministères concernés, ne fait rien";
			}
			else{
				String currentNor = getNewNumero(nor,correspond.newMinistere);
				//print "Traitement du nor : " + nor;
				Dossier dossier = getDossierFromBdd(session,currentNor);
				if (dossier != null){
					print "Rattache le dossier " + currentNor + " vers nor " + nor + "et le ministere " + correspond.oldMinistere.id +";" + correspond.oldMinistere.trigramme;
					rattacherDossier(session,dossier,correspond);
				}
				else{
					print "Dossier " + currentNor + " non trouvé";
				}
			}
		}
	}
}

def prog(migration_file, nor_file){
	print "Entré dans le main";
	inputMinisteres = new CorrespondanceSet();
	inputMinisteres.loadMinistere(migration_file);
	DossierUtils.rattacherTous(Session,inputMinisteres,nor_file);
	print "Sortie du main";
}

print "Debut du script";
String NORS = "/tmp/nors.txt";
String CORRESPONDANCE_MINS = "/tmp/migration_groovy.txt";
prog(CORRESPONDANCE_MINS,NORS);
return "Fin de script"
