package fr.dila.solonmgpp.webtest.helper.url;

import fr.sword.naiad.commons.webtest.helper.UrlHelper;


public class UrlEpgHelper extends UrlHelper {

    public static final String DEFAULT_APP_URL = "http://localhost:8180/";
    private static final String EPG_NAME = "solon-epg";
    private static final String AUTOMATION_SITE = "/site/automation";

    private static volatile UrlHelper instance;

    public static UrlHelper getInstance(){
        if(instance == null){
            synchronized(UrlHelper.class){
                UrlHelper helper = new UrlEpgHelper();
                instance = helper;
            }
        }
        return instance;
    }

    @Override 
    public String getAppUrl() {
        return DEFAULT_APP_URL;
    }
    
    public String getEpgUrl(){
        String appurl = getAppUrl();
        if(!appurl.endsWith("/")){
            appurl += "/";
        }
        appurl += EPG_NAME;
        return appurl;
    }
    
    public String getAutomationUrl(){
        String repurl = getEpgUrl();
        return repurl + AUTOMATION_SITE;
    }
    
    private UrlEpgHelper(){
        super(DEFAULT_APP_URL);
    }
    
}
