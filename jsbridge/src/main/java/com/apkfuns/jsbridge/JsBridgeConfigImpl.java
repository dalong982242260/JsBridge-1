package com.apkfuns.jsbridge;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by pengwei on 16/5/13.
 */
class JsBridgeConfigImpl implements JsBridgeConfig {

    private Map<String, HashMap<String, JsMethod>> exposedMethods = new HashMap<>();
    private Set<Class<? extends JsMethodRun>> methodRuns = new HashSet<>();
    private String protocol;
    private int sdkVersionCode;

    private JsBridgeConfigImpl() {
    }

    private static JsBridgeConfigImpl singleton;

    public static JsBridgeConfigImpl getInstance() {
        if (singleton == null) {
            synchronized (JsBridgeConfigImpl.class) {
                if (singleton == null) {
                    singleton = new JsBridgeConfigImpl();
                }
            }
        }
        return singleton;
    }

    public Set<Class<? extends JsMethodRun>> getMethodRuns() {
        return methodRuns;
    }

    @Override
    public JsBridgeConfig registerModule(Class<? extends JsModule>... modules) {
        if (modules != null && modules.length > 0) {
            for (Class<? extends JsModule> module : modules) {
                try {
                    String moduleName = module.newInstance().getModuleName();
                    if (!exposedMethods.containsKey(moduleName)) {
                        exposedMethods.put(moduleName, Utils.getAllMethod(moduleName, module));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    @Override
    public JsBridgeConfig registerMethodRun(Class<? extends JsMethodRun>... methodRuns) {
        if (methodRuns != null && methodRuns.length > 0) {
            for (Class<? extends JsMethodRun> run : methodRuns) {
                this.methodRuns.add(run);
            }
        }
        return this;
    }


    public Map<String, HashMap<String, JsMethod>> getExposedMethods() {
        return exposedMethods;
    }

    @Override
    public JsBridgeConfig setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public String getProtocol() {
        return TextUtils.isEmpty(protocol) ? DEFAULT_PROTOCOL : protocol;
    }

    @Override
    public JsBridgeConfig setSdkVersion(int versionCode) {
        this.sdkVersionCode = versionCode;
        return this;
    }

    public int getSdkVersionCode() {
        return sdkVersionCode;
    }
}
