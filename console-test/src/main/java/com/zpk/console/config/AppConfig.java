package com.zpk.console.config;

public class AppConfig {
    private InspectAppConfig inspect;

    public InspectAppConfig getInspect() {
        return inspect;
    }

    public void setInspect(InspectAppConfig inspect) {
        this.inspect = inspect;
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "inspect=" + inspect +
                '}';
    }
}
