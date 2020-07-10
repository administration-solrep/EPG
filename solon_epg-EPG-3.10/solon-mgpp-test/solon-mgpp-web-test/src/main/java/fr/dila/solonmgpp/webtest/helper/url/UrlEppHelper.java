package fr.dila.solonmgpp.webtest.helper.url;

import fr.sword.naiad.commons.webtest.helper.UrlHelper;

public class UrlEppHelper extends UrlHelper {

    public static final String DEFAULT_APP_URL = "http://localhost:8080/";
    public static final String BASE_PATH = "solon-epp/site/solonepp/";
    private static final String EPP_NAME = "solon-epp";
    private static final String AUTOMATION_SITE = "/site/automation";

    private static volatile UrlHelper instance;

    public static UrlHelper getInstance(){
        if(instance == null){
            synchronized(UrlHelper.class){
                UrlHelper helper = new UrlEppHelper();
                instance = helper;
            }
        }
        return instance;
    }

    @Override 
    public String getAppUrl() {
        return DEFAULT_APP_URL;
    }
    
    public String getEppUrl(){
        String appurl = getAppUrl();
        if(!appurl.endsWith("/")){
            appurl += "/";
        }
        appurl += EPP_NAME;
        return appurl;
    }
    
    public String getAutomationUrl(){
        String repurl = getEppUrl();
        return repurl + AUTOMATION_SITE;
    }
    
    private UrlEppHelper(){
        super(DEFAULT_APP_URL);
    }
    
    public static String getEndPoint(){
		return getInstance().getAppUrl();
	}
    
}
