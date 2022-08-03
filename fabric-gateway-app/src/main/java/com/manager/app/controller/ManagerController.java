package com.manager.app.controller;

import com.manager.app.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.manager.app.service.FabricService;
import java.util.ArrayList;
import org.json.JSONObject;

@RestController
public class ManagerController {

    @Autowired
    FabricService fabricService;

    ArrayList<ClientCredentials> registeredUsers = new ArrayList<>();
    ArrayList<ClientCredentials> registeredClients = new ArrayList<>();

    @RequestMapping(value = "/devices", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerNewDevice(@RequestBody DeviceRegistrationInfo deviceInfo) {
        String result = fabricService.createAsset(deviceInfo.owner, deviceInfo.name, deviceInfo.region);
        if(result.contains("Error"))
            return new ResponseEntity<>(getJsonString(result), HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(getJsonString("deviceID", result), HttpStatus.OK);
    }

    @RequestMapping(value = "/policies", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateAccessPolicy(@RequestBody DeviceAccessPolicy accessPolicy) {
        String result = fabricService.updateAccessPolicy(accessPolicy.device_id, accessPolicy.getAccessing_device_id(), accessPolicy.getAccessing_user_id());
        if(result.contains("Error"))
            return new ResponseEntity<>(getJsonString(result), HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(getJsonString("Access policy updated!"), HttpStatus.OK);
    }

    @RequestMapping(value = "/ipfs-hash/{deviceId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity fetchIPFSHashForDevice(@PathVariable String deviceId, @RequestBody ClientCredentials clientCredentials) {
        for(ClientCredentials cc: registeredClients){
            if(cc.email.equals(clientCredentials.email) && cc.password.equals(clientCredentials.password)){
                String result = fabricService.fetchIPFSHashFromUser(clientCredentials.email, deviceId);
                if(result.contains("Error"))
                    return new ResponseEntity<>(getJsonString(result), HttpStatus.BAD_REQUEST);
                if(result.contains("Unauthorized"))
                    return new ResponseEntity<>(getJsonString(result), HttpStatus.UNAUTHORIZED);
                return new ResponseEntity<>(getJsonString("ipfsHash", result), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(getJsonString("Authentication failed. Please enter valid email ID and password."), HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/devices", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllDevices() {
        ArrayList<Asset> assets = fabricService.getAllAssets();
        if(assets == null)
            return new ResponseEntity<>(getJsonString("Error fetching list of devices"), HttpStatus.INTERNAL_SERVER_ERROR);
        ArrayList<DeviceInfo> devices = new ArrayList<>();
        for(Asset asset: assets){
            DeviceInfo deviceInfo = new DeviceInfo(asset.id, asset.owner, asset.name, asset.region, asset.iPFSHash, asset.updatedAt);
            devices.add(deviceInfo);
        }
        return new ResponseEntity<>(getJsonString("devices", devices), HttpStatus.OK);
    }

    @RequestMapping(value = "/policies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllPolicies() {
        ArrayList<Asset> assets = fabricService.getAllAssets();
        if(assets == null)
            return new ResponseEntity<>(getJsonString("Error fetching list of devices"), HttpStatus.INTERNAL_SERVER_ERROR);
        ArrayList<PolicyInfo> policies = new ArrayList<>();
        for(Asset asset: assets){
            PolicyInfo policyInfo = new PolicyInfo(asset.id, asset.name, asset.authorizedDevices, asset.authorizedUsers);
            policies.add(policyInfo);
        }
        return new ResponseEntity<>(getJsonString("policies", policies), HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerUser(@RequestBody ClientCredentials newUser){
        ClientCredentials cc = new ClientCredentials(newUser.email, newUser.password);
        registeredUsers.add(cc);
        return new ResponseEntity<>(getJsonString("User successfully registered!"), HttpStatus.OK);
    }

    @RequestMapping(value = "/users/signin", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signinUser(@RequestBody ClientCredentials user){
        for(ClientCredentials cc: registeredUsers){
            if(cc.email.equals(user.email) && cc.password.equals(user.password)){
                return new ResponseEntity<>(getJsonString("Successful"), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(getJsonString("Authentication failed. Please enter valid email ID and password."), HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/clients", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerClient(@RequestBody ClientCredentials newUser){
        ClientCredentials cc = new ClientCredentials(newUser.email, newUser.password);
        registeredClients.add(cc);
        return new ResponseEntity<>(getJsonString("Client successfully registered!"), HttpStatus.OK);
    }

    private String getJsonString(String message){
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("response", message);
        return jsonObj.toString();
    }

    private String getJsonString(String key, Object value){
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(key, value);
        return jsonObj.toString();
    }
}
