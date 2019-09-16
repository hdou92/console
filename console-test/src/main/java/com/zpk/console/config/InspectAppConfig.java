package com.zpk.console.config;

import java.util.Arrays;
import java.util.List;

public class InspectAppConfig {
    private List<InspectAppInfo> apps;

    public List<InspectAppInfo> getApps() {
        return apps;
    }

    public void setApps(List<InspectAppInfo> apps) {
        this.apps = apps;
    }

    @Override
    public String toString() {
        return "InspectAppConfig{" +
                "apps=" + apps +
                '}';
    }
}
