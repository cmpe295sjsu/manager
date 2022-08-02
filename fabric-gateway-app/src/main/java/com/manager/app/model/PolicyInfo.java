package com.manager.app.model;

import java.util.ArrayList;

public class PolicyInfo {
    public String id;
    public String name;
    public ArrayList<String> authorized_devices;
    public ArrayList<String> authorized_users;

    public PolicyInfo(String id, String name, ArrayList<String> authorizedDevices, ArrayList<String> authorizedUsers) {
        this.id = id;
        this.name = name;
        this.authorized_devices = authorizedDevices;
        this.authorized_users = authorizedUsers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getAuthorized_devices() {
        return authorized_devices;
    }

    public void setAuthorized_devices(ArrayList<String> authorized_devices) {
        this.authorized_devices = authorized_devices;
    }

    public ArrayList<String> getAuthorized_users() {
        return authorized_users;
    }

    public void setAuthorized_users(ArrayList<String> authorized_users) {
        this.authorized_users = authorized_users;
    }
}
