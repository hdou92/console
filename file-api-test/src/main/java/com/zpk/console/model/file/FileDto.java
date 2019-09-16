package com.zpk.console.model.file;

public class FileDto {
    private String name;
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "FileDto{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
