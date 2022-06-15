package fr.dila.solonepg.core.bdj;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.api.exception.WsBdjException;
import fr.dila.solonepg.core.dto.activitenormative.injectionbdj.EcheancierWrapper;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.core.service.STServiceLocator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.nuxeo.ecm.core.api.CoreSession;

public class WsBdjImpl implements WsBdj {
    public static final String LOGGER_INJECTION_BDJ_NAME = "LOGGER-INJECTION-BDJ";

    /** Logger utilisé pour l'injection BDJ */
    private static final Logger LOGGER = LogManager.getLogger(LOGGER_INJECTION_BDJ_NAME);

    public WsBdjImpl() {
        super();
    }

    @Override
    public void publierBilanSemestrielBDJ(CoreSession session, String bilanSemestrielXml) throws WsBdjException {
        String bdjUrl = getUrlWebservicePublicationBilanSemestrielBdj(session);
        doPost(bdjUrl, bilanSemestrielXml);
    }

    @Override
    public String publierEcheancierBDJ(final CoreSession session, final String echeancierXmlStr) throws WsBdjException {
        if (echeancierXmlStr == null) {
            throw new IllegalArgumentException("echeancierXmlStr should not be null");
        }
        String bdjUrl = getUrlWebservicePublicationBdj(session);
        if (bdjUrl == null) {
            throw new IllegalArgumentException(
                "L'URL vers le WS BDJ doit être définie dans les paramètres techniques de l'application."
            );
        }
        return doPost(bdjUrl, echeancierXmlStr);
    }

    private String doPost(String bdjUrl, String xmlcontent) throws WsBdjException {
        StringBuilder loggerStr = new StringBuilder();
        String status = StringUtils.EMPTY;

        try {
            // Injecter le XML dans du json
            String echeancierJson = buildEcheancierJsonStr(xmlcontent);

            // Envoyer le json vers BDJ
            HttpPost httpPost = new HttpPost(bdjUrl);
            InputStreamEntity reqEntity = new InputStreamEntity(new ByteArrayInputStream(echeancierJson.getBytes()));
            httpPost.setEntity(reqEntity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            loggerStr.append("[").append(this.getClass().getName()).append("]\n");
            loggerStr.append("---BEGIN OUT TRANSACTION url=(").append(bdjUrl).append(")---\n");

            loggerStr.append("--REQUEST--\n");
            loggerStr.append(echeancierJson);
            loggerStr.append("\n\n--RESPONSE");

            try (
                CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(httpPost)
            ) {
                if (
                    response.getStatusLine().getStatusCode() != HttpStatus.SC_OK &&
                    response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED
                ) {
                    throw new WsBdjException("Bad response status : " + response.getStatusLine());
                }

                String entity = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                JSONObject result = new JSONObject(entity);
                status = result.getJSONArray("statusResponses").getJSONObject(0).getString("status");

                loggerStr.append(" (Success)--\n").append(entity);
            }
        } catch (WsBdjException e) {
            loggerStr.append(" (Failure)--\n").append(e.getMessage());
            throw e;
        } catch (IOException | JSONException e) {
            loggerStr.append(" (Failure)--\n").append(e.getMessage());
            throw new WsBdjException(e);
        } finally {
            LOGGER.info(loggerStr);
        }
        return status;
    }

    protected String buildEcheancierJsonStr(String echeancierXml) throws IOException {
        EcheancierWrapper wrapper = new EcheancierWrapper(echeancierXml);
        ObjectMapper jsonMapper = new ObjectMapper();
        return jsonMapper.writeValueAsString(wrapper);
    }

    private String getUrlWebservicePublicationBilanSemestrielBdj(CoreSession session) {
        return getUrl(session, SolonEpgParametreConstant.URL_WEBSERVICE_PUBLICATION_BILAN_SEMESTRIEL_BDJ);
    }

    private String getUrlWebservicePublicationBdj(CoreSession session) {
        return getUrl(session, SolonEpgParametreConstant.URL_WEBSERVICE_PUBLICATION_ECHEANCIER_BDJ);
    }

    private String getUrl(CoreSession session, String paramName) {
        final STParametreService paramService = STServiceLocator.getSTParametreService();
        return paramService.getParametreValue(session, paramName);
    }
}
