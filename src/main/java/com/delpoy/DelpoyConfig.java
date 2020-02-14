package com.delpoy;

import java.util.ResourceBundle;

/**
 * IDEA发布配置
 * @Author lyf
 * @Date 11:20 2020/2/1
 */
public class DelpoyConfig {

    private static Object lock              = new Object();
    private static DelpoyConfig config     = null;
    private static ResourceBundle rb        = null;
    private static final String CONFIG_IDEA_FILE = "deployIdeaConfig";
    private static final String CONFIG_ECLIPSE_FILE = "deployEclipseConfig";

    private DelpoyConfig(Integer type) {
        switch (type){
            case 1:
                rb = ResourceBundle.getBundle(CONFIG_IDEA_FILE);
                break;
            case 2:
                rb = ResourceBundle.getBundle(CONFIG_ECLIPSE_FILE);
                break;
            default:
                break;
        }
    }

    public static DelpoyConfig getInstance(Integer type) {
        synchronized(lock) {
            if(null == config) {
                config = new DelpoyConfig(type);
            }
        }
        return (config);
    }

    public String getValue(String key) {
        return (rb.getString(key));
    }

}
