package com.manager.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

public class Asset {
    @JsonProperty("ID")
    public String id;
    @JsonProperty("Owner")
    public String owner;
    @JsonProperty("IPFSHash")
    public String iPFSHash;
    @JsonProperty("AuthorizedDevices")
    public ArrayList<String> authorizedDevices;
    @JsonProperty("AuthorizedUsers")
    public ArrayList<String> authorizedUsers;
}
