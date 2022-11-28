package com.manager.app.model;

import java.util.ArrayList;

public class DeviceInfo {
    public String id;
    public String owner;
    public String name;
    public String region;
    public ArrayList<String> ipfs;
    public String updated_at;

    public DeviceInfo(String id, String owner, String name, String region, ArrayList<String> ipfs, String updatedAt) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.region = region;
        this.ipfs = ipfs;
        this.updated_at = updatedAt;
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

    public ArrayList<String> getIpfs() {
        return ipfs;
    }

    public void setIpfs(ArrayList<String> ipfs) {
        this.ipfs = ipfs;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
