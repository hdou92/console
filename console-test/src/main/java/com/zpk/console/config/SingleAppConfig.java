package com.zpk.console.config;

public class SingleAppConfig {
    private SingleInspectConfig inspect;

    public SingleInspectConfig getInspect() {
        return inspect;
    }

    public void setInspect(SingleInspectConfig inspect) {
        this.inspect = inspect;
    }

    @Override
    public String toString() {
        return "SingleAppConfig{" +
                "inspect=" + inspect +
                '}';
    }
}
