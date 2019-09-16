package com.zpk.console.config;

import java.util.Arrays;

public class SingleInspectConfig {
    private String checkAliveAppName;
    private String[] checkAliveAppCmds;
    private String runAppName;
    private String[] runAppCmds;
    private String processIdAppName;
    private String[] processIdAppCmds;
    private String closeAppName;
    private String[] closeAppCmds;

    public String getCheckAliveAppName() {
        return checkAliveAppName;
    }

    public void setCheckAliveAppName(String checkAliveAppName) {
        this.checkAliveAppName = checkAliveAppName;
    }

    public String[] getCheckAliveAppCmds() {
        return checkAliveAppCmds;
    }

    public void setCheckAliveAppCmds(String[] checkAliveAppCmds) {
        this.checkAliveAppCmds = checkAliveAppCmds;
    }

    public String getRunAppName() {
        return runAppName;
    }

    public void setRunAppName(String runAppName) {
        this.runAppName = runAppName;
    }

    public String[] getRunAppCmds() {
        return runAppCmds;
    }

    public void setRunAppCmds(String[] runAppCmds) {
        this.runAppCmds = runAppCmds;
    }

    public String getCloseAppName() {
        return closeAppName;
    }

    public void setCloseAppName(String closeAppName) {
        this.closeAppName = closeAppName;
    }

    public String[] getCloseAppCmds() {
        return closeAppCmds;
    }

    public void setCloseAppCmds(String[] closeAppCmds) {
        this.closeAppCmds = closeAppCmds;
    }

    public String getProcessIdAppName() {
        return processIdAppName;
    }

    public void setProcessIdAppName(String processIdAppName) {
        this.processIdAppName = processIdAppName;
    }

    public String[] getProcessIdAppCmds() {
        return processIdAppCmds;
    }

    public void setProcessIdAppCmds(String[] processIdAppCmds) {
        this.processIdAppCmds = processIdAppCmds;
    }

    @Override
    public String toString() {
        return "SingleInspectConfig{" +
                "checkAliveAppName='" + checkAliveAppName + '\'' +
                ", checkAliveAppCmds=" + Arrays.toString(checkAliveAppCmds) +
                ", runAppName='" + runAppName + '\'' +
                ", runAppCmds=" + Arrays.toString(runAppCmds) +
                ", processIdAppName='" + processIdAppName + '\'' +
                ", processIdAppCmds=" + Arrays.toString(processIdAppCmds) +
                ", closeAppName='" + closeAppName + '\'' +
                ", closeAppCmds=" + Arrays.toString(closeAppCmds) +
                '}';
    }
}
