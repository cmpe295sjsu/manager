package com.manager.app.model;

public class DeviceRegistrationInfo {
    public String owner;
    public String name;
    public String region;

    public DeviceRegistrationInfo(String owner, String name, String region) {
        this.owner = owner;
        this.name = name;
        this.region = region;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
