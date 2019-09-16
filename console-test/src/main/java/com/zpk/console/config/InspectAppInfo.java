package com.zpk.console.config;

import java.util.List;

public class InspectAppInfo {
    private String packageName;
    private List<String> checkAliveAppCmds;
    private List<String> runAppCmds;
    private List<String> processIdAppCmds;
    private List<String> closeAppCmds;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getCheckAliveAppCmds() {
        return checkAliveAppCmds;
    }

    public void setCheckAliveAppCmds(List<String> checkAliveAppCmds) {
        this.checkAliveAppCmds = checkAliveAppCmds;
    }

    public List<String> getRunAppCmds() {
        return runAppCmds;
    }

    public void setRunAppCmds(List<String> runAppCmds) {
        this.runAppCmds = runAppCmds;
    }

    public List<String> getProcessIdAppCmds() {
        return processIdAppCmds;
    }

    public void setProcessIdAppCmds(List<String> processIdAppCmds) {
        this.processIdAppCmds = processIdAppCmds;
    }

    public List<String> getCloseAppCmds() {
        return closeAppCmds;
    }

    public void setCloseAppCmds(List<String> closeAppCmds) {
        this.closeAppCmds = closeAppCmds;
    }

    @Override
    public String toString() {
        return "InspectAppInfo{" +
                "packageName='" + packageName + '\'' +
                ", checkAliveAppCmds=" + checkAliveAppCmds +
                ", runAppCmds=" + runAppCmds +
                ", processIdAppCmds=" + processIdAppCmds +
                ", closeAppCmds=" + closeAppCmds +
                '}';
    }
}
