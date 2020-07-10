package fr.dila.solonepg.webtest.helper.url;

import fr.sword.naiad.commons.webtest.helper.UrlHelper;

public class UrlEpgHelper extends UrlHelper {
	
	private static final String DEV_APP_URL = "http://localhost:8180/";
	private static final String QA_APP_URL = "http://idlv-solrep-epg-qa.lyon-dev2.local:8180/";
	
	// Url à modifier selon quel est le serveur où tourne l'appli : localhost (dev : DEV_APP_URL ou QA : QA_APP_URL)
	public static final String			ACTUAL_APP_URL		= DEV_APP_URL;
	private static final String			EPG_NAME			= "solon-epg";
	private static final String			AUTOMATION_SITE		= "/site/automation";

	private static volatile UrlHelper	instance;

	public static UrlHelper getInstance() {
		if (instance == null) {
			synchronized (UrlHelper.class) {
				UrlHelper helper = new UrlEpgHelper();
				instance = helper;
			}
		}
		return instance;
	}

	@Override
	public String getAppUrl() {
		return ACTUAL_APP_URL;
	}

	public String getEpgUrl() {
		String appurl = getAppUrl();
		if (!appurl.endsWith("/")) {
			appurl += "/";
		}
		appurl += EPG_NAME;
		return appurl;
	}

	public String getAutomationUrl() {
		String repurl = getEpgUrl();
		return repurl + AUTOMATION_SITE;
	}

	private UrlEpgHelper() {
		super(ACTUAL_APP_URL);
	}

}
