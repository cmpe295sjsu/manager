package com.manager.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "device_key")
public class DeviceKey {
    @Id
    private String id;
    private String key;
    private String org;

    public DeviceKey(String key, String org) {
        this.key = key;
        this.org = org;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }
}