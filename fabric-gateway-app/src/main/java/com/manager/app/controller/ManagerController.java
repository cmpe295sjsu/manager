package com.manager.app.controller;

import com.manager.app.model.Asset;
import com.manager.app.model.DeviceAccessPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.manager.app.service.FabricService;
import java.util.ArrayList;

@RestController
public class ManagerController {

    @Autowired
    FabricService fabricService;

    @PostMapping("/registerdevice")
    public String registerNewDevice(@RequestBody String owner) {
        boolean result = fabricService.createAsset(owner);
        if(result)
            return "New device registered successfully!";
        else
            return "Error registering new device!";
    }

    @PostMapping("/accesspolicy/{deviceId}")
    public String updateAccessPolicy(@PathVariable String deviceId, @RequestBody DeviceAccessPolicy accessPolicy) {
        System.out.println("mgr controller auth devices  "+accessPolicy.getAuthorizedDevices());
        System.out.println("mgr controller auth users  "+accessPolicy.getAuthorizedUsers());
        boolean result = fabricService.updateAccessPolicy(deviceId, accessPolicy.getAuthorizedDevices(), accessPolicy.getAuthorizedUsers());
        if(result)
            return "Access policy updated for device with ID " + deviceId + "!";
        else
            return "Error updating access policy for device with ID " + deviceId;
    }

    @GetMapping("/accesspolicy/{deviceId}/fetchhash/{userId}")
    public String fetchIPFSHashForDevice(@PathVariable String deviceId, @PathVariable String userId) {
        return fabricService.fetchIPFSHashFromUser(userId, deviceId);
    }

    @GetMapping("/devices")
    public ArrayList<Asset> getAllDevices() {
        return fabricService.getAllAssets();
    }
}
