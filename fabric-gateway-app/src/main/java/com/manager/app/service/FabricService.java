package com.manager.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.manager.app.model.FabricProperties;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import com.manager.app.model.Asset;
import org.hyperledger.fabric.client.CommitException;
import org.hyperledger.fabric.client.CommitStatusException;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.EndorseException;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.GatewayException;
import org.hyperledger.fabric.client.SubmitException;
import org.hyperledger.fabric.client.identity.Identities;
import org.hyperledger.fabric.client.identity.Identity;
import org.hyperledger.fabric.client.identity.Signer;
import org.hyperledger.fabric.client.identity.Signers;
import org.hyperledger.fabric.client.identity.X509Identity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Service
public class FabricService {
    @Autowired
    private FabricProperties fabricProperties;
    private Contract contract;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private ObjectMapper mapper = new ObjectMapper();
    private Path fabricParentPath;
    private Path fabricCryptoPath;

    @PostConstruct
    public void initFabricConnection() throws Exception {
        // The gRPC client connection should be shared by all Gateway connections to
        // this endpoint.
        fabricParentPath = Paths.get(fabricProperties.getParentPath());
        fabricCryptoPath = fabricParentPath.resolve(Paths.get(fabricProperties.getCryptoPath()));
        var channel = newGrpcConnection();
        var builder = Gateway.newInstance().identity(newIdentity()).signer(newSigner()).connection(channel)
                // Default timeouts for different gRPC calls
                .evaluateOptions(options -> options.withDeadlineAfter(30, TimeUnit.SECONDS))
                .endorseOptions(options -> options.withDeadlineAfter(30, TimeUnit.SECONDS))
                .submitOptions(options -> options.withDeadlineAfter(30, TimeUnit.SECONDS))
                .commitStatusOptions(options -> options.withDeadlineAfter(1, TimeUnit.MINUTES));

        Gateway gateway = builder.connect();
        var network = gateway.getNetwork(fabricProperties.getChannelName());
        // Get the smart contract from the network.
        contract = network.getContract(fabricProperties.getChaincodeName());
    }

    private ManagedChannel newGrpcConnection() throws IOException, CertificateException {
        var tlsCertReader = Files.newBufferedReader(fabricCryptoPath.resolve(Paths.get(fabricProperties.getTlscertPath())));
        var tlsCert = Identities.readX509Certificate(tlsCertReader);

        return NettyChannelBuilder.forTarget(fabricProperties.getPeerEndpoint())
                .sslContext(GrpcSslContexts.forClient().trustManager(tlsCert).build()).overrideAuthority(fabricProperties.getOverrideAuth())
                .build();
    }

    private Identity newIdentity() throws IOException, CertificateException {
        var certReader = Files.newBufferedReader(fabricCryptoPath.resolve(Paths.get(fabricProperties.getCertPath())));
        var certificate = Identities.readX509Certificate(certReader);

        return new X509Identity(fabricProperties.getMspId(), certificate);
    }

    private Signer newSigner() throws IOException, InvalidKeyException {
        var keyReader = Files.newBufferedReader(getPrivateKeyPath());
        var privateKey = Identities.readPrivateKey(keyReader);

        return Signers.newPrivateKeySigner(privateKey);
    }

    private Path getPrivateKeyPath() throws IOException {
        try (var keyFiles = Files.list(fabricCryptoPath.resolve(Paths.get(fabricProperties.getKeyPath())))) {
            return keyFiles.findFirst().orElseThrow();
        }
    }

    /*public String createAsset(String owner, String name, String region) {
        System.out.println("\n--> Submit Transaction: createAsset");
        byte[] result;
        try {
            result = contract.submitTransaction("CreateNewDevice", owner, name, region);
            return new String(result, StandardCharsets.UTF_8);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "Error while registering new device: " + e.getMessage();
        }
    }*/

    public String createAsset(String id, String owner, String name, String region) {
        System.out.println("\n--> Submit Transaction: createAsset");
        byte[] result;
        try {
            result = contract.submitTransaction("CreateNewDeviceWithId", id, owner, name, region);
            return new String(result, StandardCharsets.UTF_8);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "Error while registering new device: " + e.getMessage();
        }
    }

    public ArrayList<Asset> getAllAssets() {
        System.out.println("\n--> Evaluate Transaction: GetAllAssets");
        try {
            byte[] result = contract.evaluateTransaction("GetAllAssets");
            String assetString = prettyJson(result);
            if((new String(result, StandardCharsets.UTF_8)).isBlank())
                return new ArrayList<>();
            ArrayList<Asset> assets = Lists.newArrayList(mapper.readValue(assetString, Asset[].class));
            return assets;
        } catch (GatewayException | JsonProcessingException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private String prettyJson(final byte[] json) {
        return prettyJson(new String(json, StandardCharsets.UTF_8));
    }

    private String prettyJson(final String json) {
        var parsedJson = JsonParser.parseString(json);
        return gson.toJson(parsedJson);
    }

    public Asset readAsset(String id) {
        System.out.println("\n--> Evaluate Transaction: ReadAsset");
        try {
            byte[] evaluateResult = contract.evaluateTransaction("ReadAsset", id);
            String assetString = prettyJson(evaluateResult);
            return mapper.readValue(assetString, Asset.class);
        } catch (GatewayException | JsonProcessingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * authorizedDevices and authorizedUsers represent the new (or delta) devices and users to be added to the access policy
     * @param deviceId
     * @param authorizedDevices
     * @param authorizedUsers
     * @return
     */
    public String updateAccessPolicy(String deviceId, ArrayList<String> authorizedDevices,
                                    ArrayList<String> authorizedUsers) {
        try {
            System.out.println("\n--> Submit Transaction: updateAccessPolicy");
            Asset originalAsset = readAsset(deviceId);
            if (originalAsset == null){
                return "Error updating policy: either this device ID doesn't exist or there was an issue on the Fabric side while reading the asset.";
            }
            originalAsset.authorizedDevices.addAll(authorizedDevices);
            originalAsset.authorizedUsers.addAll(authorizedUsers);
            contract.submitTransaction("UpdateAsset", deviceId, originalAsset.owner, originalAsset.name, originalAsset.region,
                    originalAsset.iPFSHash, new Gson().toJson(originalAsset.authorizedDevices), new Gson().toJson(originalAsset.authorizedUsers));
            System.out.println("******** updateAccessPolicy transaction committed successfully");
            return "Success";
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "Error updating policy: " + e.getMessage();
        }
    }

    public String fetchIPFSHashFromUser(String userEmail, String targetDeviceId){
        Asset asset = readAsset(targetDeviceId);
        if (asset == null){
            return "Error updating policy: either this device ID doesn't exist or there was an issue on Fabric side while reading asset.";
        }
        for(String email: asset.authorizedUsers){
            if (userEmail.equals(email)){
                return asset.iPFSHash;
            }
        }
        return "Unauthorized: This user is not authorized to access requested device.";
    }

    public String pushIPFSHashToFabric(String deviceId, String hash){
        try {
            System.out.println("\n--> Submit Transaction: pushIPFSHashToFabric");
            Asset originalAsset = readAsset(deviceId);
            if (originalAsset == null){
                return "Error updating policy: either this device ID doesn't exist or there was an issue on the Fabric side while reading the asset.";
            }
            contract.submitTransaction("UpdateAsset", deviceId, originalAsset.owner, originalAsset.name, originalAsset.region,
                    hash, new Gson().toJson(originalAsset.authorizedDevices), new Gson().toJson(originalAsset.authorizedUsers));
            System.out.println("******** pushIPFSHashToFabric transaction committed successfully");
            return "Success";
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "Error updating the IPFS hash: " + e.getMessage();
        }
    }

    /*public String fetchIPFSHashFromDevice(String requestingDeviceId, String targetDeviceId){
        System.out.println("\n--> Submit Transaction: fetchIPFSHashFromDevice");
        try {
            byte[] evaluateResult = contract.evaluateTransaction("fetchIPFSHashForDeviceFromUser", requestingDeviceId, targetDeviceId);
            System.out.println("******** fetchIPFSHashFromDevice transaction committed successfully");
            return prettyJson(evaluateResult);
        } catch (GatewayException e) {
            System.out.println(e.getMessage());
            return "Error! Could not fetch IPFS hash for device with ID " + targetDeviceId;
        }
    }*/

}