package fr.dila.solonepg.core.recherche;

import org.nuxeo.ecm.core.search.api.client.querymodel.Escaper;


/**
 *  Un escaper qui ne fait rien pour les -. Ne sera pas nécessaire dans une prochaine version de Nuxeo.
 *  Voir SUPNXP-3187
 * @author jgomez 
 *
 */

public class ReponsesMinimalEscaper implements Escaper { 

        public String escape(String str) {
            StringBuilder builder = new StringBuilder();
            for (int index = 0; index < str.length(); index++) {
                char charAt = str.charAt(index);
                if (charAt == '+' || charAt == '!' || charAt == '"') {
                    builder.append('\\');
                }
                builder.append(charAt);
            }
            return builder.toString();
        }

}
