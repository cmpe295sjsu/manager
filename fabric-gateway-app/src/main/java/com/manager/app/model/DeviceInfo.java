package com.manager.app.model;

public class DeviceInfo {
    public String id;
    public String owner;
    public String name;
    public String region;
    public String ipfs;
    public String updatedAt;

    public DeviceInfo(String id, String owner, String name, String region, String ipfs, String updatedAt) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.region = region;
        this.ipfs = ipfs;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIpfs() {
        return ipfs;
    }

    public void setIpfs(String ipfs) {
        this.ipfs = ipfs;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
