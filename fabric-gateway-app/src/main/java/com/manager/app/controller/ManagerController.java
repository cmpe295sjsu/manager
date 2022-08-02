package com.manager.app.controller;

import com.manager.app.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.manager.app.service.FabricService;
import java.util.ArrayList;

@RestController
public class ManagerController {

    @Autowired
    FabricService fabricService;

    ArrayList<ClientCredentials> registeredUsers = new ArrayList<>();
    ArrayList<ClientCredentials> registeredClients = new ArrayList<>();

    /*private ArrayList<ClientCredentials> getRegisteredUsers(){
        ClientCredentials cc1 = new ClientCredentials("sandhya.shekar@sjsu.edu", "12345");
        ClientCredentials cc2 = new ClientCredentials("dylan.zhang@sjsu.edu", "34567");
        ArrayList<ClientCredentials> clientDetails = new ArrayList<>();
        clientDetails.add(cc1);
        clientDetails.add(cc2);
        return clientDetails;
    }*/

    @PostMapping("/devices")
    public ResponseEntity registerNewDevice(@RequestBody DeviceRegistrationInfo deviceInfo) {
        String result = fabricService.createAsset(deviceInfo.owner, deviceInfo.name, deviceInfo.region);
        if(result.contains("Error"))
            return new ResponseEntity<>("Error registering new device.", HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>("New device registered successfully! Device ID: " + result, HttpStatus.OK);
    }

    @PostMapping("/policies")
    public ResponseEntity updateAccessPolicy(@RequestBody DeviceAccessPolicy accessPolicy) {
        boolean result = fabricService.updateAccessPolicy(accessPolicy.device_id, accessPolicy.getAccessing_device_id(), accessPolicy.getAccessing_user_id());
        if(result)
            return new ResponseEntity<>("Access policy updated for device with ID " + accessPolicy.device_id + "!", HttpStatus.OK);
        else
            return new ResponseEntity<>("Error updating access policy for device with ID " + accessPolicy.device_id, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/ipfs-hash/{deviceId}")
    @ResponseBody
    public ResponseEntity fetchIPFSHashForDevice(@PathVariable String deviceId, @RequestBody ClientCredentials clientCredentials) {
        for(ClientCredentials cc: registeredClients){
            if(cc.email.equals(clientCredentials.email) && cc.password.equals(clientCredentials.password)){
                String result = fabricService.fetchIPFSHashFromUser(clientCredentials.email, deviceId);
                if(result.contains("Error"))
                    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                //return new ResponseEntity<>(result, HttpStatus.OK);
                return new ResponseEntity<>("{'ipfsHash': '" + result + "'}", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Authentication failed. Please check if the email ID and password entered are correct.", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/devices")
    public ResponseEntity getAllDevices() {
        ArrayList<Asset> assets = fabricService.getAllAssets();
        if(assets == null)
            return new ResponseEntity<>("Error fetching list of devices", HttpStatus.INTERNAL_SERVER_ERROR);
        ArrayList<DeviceInfo> devices = new ArrayList<>();
        for(Asset asset: assets){
            DeviceInfo deviceInfo = new DeviceInfo(asset.id, asset.owner, asset.name, asset.region, asset.iPFSHash, asset.updatedAt);
            devices.add(deviceInfo);
        }
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    @GetMapping("/policies")
    public ResponseEntity getAllPolicies() {
        ArrayList<Asset> assets = fabricService.getAllAssets();
        if(assets == null)
            return new ResponseEntity<>("Error fetching list of devices", HttpStatus.INTERNAL_SERVER_ERROR);
        ArrayList<PolicyInfo> policies = new ArrayList<>();
        for(Asset asset: assets){
            PolicyInfo policyInfo = new PolicyInfo(asset.id, asset.name, asset.authorizedDevices, asset.authorizedUsers);
            policies.add(policyInfo);
        }
        return new ResponseEntity<>(policies, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity registerUser(@RequestBody ClientCredentials newUser){
        ClientCredentials cc = new ClientCredentials(newUser.email, newUser.password);
        registeredUsers.add(cc);
        return new ResponseEntity<>("User successfully registered!", HttpStatus.OK);
    }

    @PutMapping("/users/signin")
    public ResponseEntity signinUser(@RequestBody ClientCredentials user){
        for(ClientCredentials cc: registeredUsers){
            if(cc.email.equals(user.email) && cc.password.equals(user.password))
                return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("User authentication failed.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/clients")
    public ResponseEntity registerClient(@RequestBody ClientCredentials newUser){
        ClientCredentials cc = new ClientCredentials(newUser.email, newUser.password);
        registeredClients.add(cc);
        return new ResponseEntity<>("Client successfully registered!", HttpStatus.OK);
    }
}
