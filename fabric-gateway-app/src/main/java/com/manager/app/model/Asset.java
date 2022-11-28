package com.manager.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

public class Asset {
    @JsonProperty("ID")
    public String id;
    @JsonProperty("Owner")
    public String owner;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Region")
    public String region;
    @JsonProperty("IPFSHashList")
    public ArrayList<String> iPFSHashList;
    @JsonProperty("AuthorizedDevices")
    public ArrayList<String> authorizedDevices;
    @JsonProperty("AuthorizedUsers")
    public ArrayList<String> authorizedUsers;
    @JsonProperty("UpdatedAt")
    public String updatedAt;
}
