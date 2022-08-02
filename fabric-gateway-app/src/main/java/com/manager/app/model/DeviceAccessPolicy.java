package com.manager.app.model;

import java.util.ArrayList;

public class DeviceAccessPolicy {

    public String device_id;
    public ArrayList<String> accessing_device_id;
    public ArrayList<String> accessing_user_id;

    public DeviceAccessPolicy(String id, ArrayList<String> authorizedDevices, ArrayList<String> authorizedUsers) {
        this.device_id = id;
        this.accessing_device_id = authorizedDevices;
        this.accessing_user_id = authorizedUsers;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public ArrayList<String> getAccessing_device_id() {
        return accessing_device_id;
    }

    public void setAccessing_device_id(ArrayList<String> accessing_device_id) {
        this.accessing_device_id = accessing_device_id;
    }

    public ArrayList<String> getAccessing_user_id() {
        return accessing_user_id;
    }

    public void setAccessing_user_id(ArrayList<String> accessing_user_id) {
        this.accessing_user_id = accessing_user_id;
    }
}
