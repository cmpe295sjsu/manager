package com.manager.app.model;

import java.util.ArrayList;

public class DeviceAccessPolicy {

    public ArrayList<String> authorizedDevices;
    public ArrayList<String> authorizedUsers;

    public DeviceAccessPolicy(ArrayList<String> authorizedDevices, ArrayList<String> authorizedUsers) {
        this.authorizedDevices = authorizedDevices;
        this.authorizedUsers = authorizedUsers;
    }

    public ArrayList<String> getAuthorizedDevices() {
        return authorizedDevices;
    }

    public void setAuthorizedDevices(ArrayList<String> authorizedDevices) {
        this.authorizedDevices = authorizedDevices;
    }

    public ArrayList<String> getAuthorizedUsers() {
        return authorizedUsers;
    }

    public void setAuthorizedUsers(ArrayList<String> authorizedUsers) {
        this.authorizedUsers = authorizedUsers;
    }
}
