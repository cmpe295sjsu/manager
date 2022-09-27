package com.manager.app.model;

public class DeviceIPFSHashInfo {

    public String device_id;
    public String api_key;
    public String ipfs_hash;

    public DeviceIPFSHashInfo(String device_id, String api_key, String ipfs_hash) {
        this.device_id = device_id;
        this.api_key = api_key;
        this.ipfs_hash = ipfs_hash;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getIpfs_hash() {
        return ipfs_hash;
    }

    public void setIpfs_hash(String ipfs_hash) {
        this.ipfs_hash = ipfs_hash;
    }
}
