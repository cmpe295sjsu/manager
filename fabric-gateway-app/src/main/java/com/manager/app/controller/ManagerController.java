package com.manager.app.controller;

import com.manager.app.model.*;
import com.manager.app.repository.ClientsRepository;
import com.manager.app.repository.DeviceKeyRepository;
import com.manager.app.repository.UsersRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.manager.app.service.FabricService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

@RestController
public class ManagerController {

    @Autowired
    private FabricService fabricService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private DeviceKeyRepository deviceKeyRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String ORG_NAME = "SJSU";

    @RequestMapping(value = "/devices", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerNewDevice(@RequestBody DeviceRegistrationInfo deviceInfo) {
        String key = getKeyForOrg(ORG_NAME);
        if(key == null){
            key = generateApiKey();
            deviceKeyRepository.save(new DeviceKey(key, ORG_NAME));
        }

        String result = fabricService.createAsset(deviceInfo.id, deviceInfo.owner, deviceInfo.name, deviceInfo.region);
        if(result.contains("Error"))
            return new ResponseEntity<>(getJsonString(result), HttpStatus.BAD_REQUEST);
        else
        {
            Map<String, Object> keyMap = new HashMap<>();
            keyMap.put("deviceID", deviceInfo.id);
            keyMap.put("apiKey", key);
            return new ResponseEntity<>(getJsonString(keyMap), HttpStatus.OK);
        }
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
        Client client = clientsRepository.findByEmail(clientCredentials.email);
        if (client != null && bCryptPasswordEncoder.matches(clientCredentials.password, client.getPassword())){
            String result = fabricService.fetchIPFSHashFromUser(clientCredentials.email, deviceId);
            if(result.contains("Error"))
                return new ResponseEntity<>(getJsonString(result), HttpStatus.BAD_REQUEST);
            if(result.contains("Unauthorized"))
                return new ResponseEntity<>(getJsonString(result), HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(getJsonString("ipfsHash", result), HttpStatus.OK);
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
        String encodedPassword = bCryptPasswordEncoder.encode(newUser.password);
        User user = new User(newUser.email, encodedPassword);
        usersRepository.save(user);
        return new ResponseEntity<>(getJsonString("User successfully registered!"), HttpStatus.OK);
    }

    @RequestMapping(value = "/users/signin", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void signinUser(@RequestBody ClientCredentials cc){
    }

    @RequestMapping(value = "/clients", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerClient(@RequestBody ClientCredentials cc){
        String encodedPassword = bCryptPasswordEncoder.encode(cc.password);
        Client client = new Client(cc.email, encodedPassword);
        clientsRepository.save(client);
        return new ResponseEntity<>(getJsonString("Client successfully registered!"), HttpStatus.OK);
    }

    @RequestMapping(value = "/iot/ipfs-hash", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateHash(@RequestBody DeviceIPFSHashInfo deviceIPFSHashInfo){
        if(validateApiKeyForDevice(ORG_NAME, deviceIPFSHashInfo.api_key)){
            String result = fabricService.pushIPFSHashToFabric(deviceIPFSHashInfo.device_id, deviceIPFSHashInfo.ipfs_hash);
            if(result.contains("Error"))
                return new ResponseEntity<>(getJsonString(result), HttpStatus.BAD_REQUEST);
            else
                return new ResponseEntity<>(getJsonString("IPFS hash updated!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(getJsonString("API key is not valid for this device. Please provide correct device ID and key."), HttpStatus.FORBIDDEN);
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

    private String getJsonString(Map<String, Object> mapValues){
        JSONObject jsonObj = new JSONObject();
        for (String key: mapValues.keySet())
            jsonObj.put(key, mapValues.get(key));
        return jsonObj.toString();
    }

    String generateApiKey(){
        String generatedString = RandomStringUtils.randomAlphanumeric(10);
        return generatedString;
    }

    String getKeyForOrg(String org){
        DeviceKey deviceKey = deviceKeyRepository.findByOrg(org);
        if(deviceKey != null)
            return deviceKey.getKey();
        return null;
    }

    // TODO for now, using only one key per organization.
    boolean validateApiKeyForDevice(String orgName, String apiKey){
        return apiKey.equals(getKeyForOrg(orgName));
    }
}
