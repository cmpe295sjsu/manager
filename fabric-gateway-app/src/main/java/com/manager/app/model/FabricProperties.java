package com.manager.app.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.nio.file.Path;
import java.nio.file.Paths;

//@Component
//@PropertySource("classpath:application.properties")
@Configuration
@ConfigurationProperties
public class FabricProperties {

    public final String mspID = "Org1MSP";
    public final String channelName = "mychannel";
    public final String chaincodeName = "basic";
    // TODO add path to your local fabric-samples directory
    public final Path parentPath = Paths.get("../fabric-samples/");
    public Path cryptoPath = parentPath.resolve(Paths.get("test-network", "organizations", "peerOrganizations", "org1.example.com"));
    // Path to user certificate.
    public Path certPath = cryptoPath.resolve(Paths.get("users", "Admin@org1.example.com", "msp", "signcerts", "Admin@org1.example.com-cert.pem"));
    // Path to user private key directory.
    public Path keyDirPath = cryptoPath.resolve(Paths.get("users", "Admin@org1.example.com", "msp", "keystore"));
    // Path to peer tls certificate.
    public Path tlsCertPath = cryptoPath.resolve(Paths.get("peers", "peer0.org1.example.com", "tls", "ca.crt"));

    // Gateway peer end point.
    public String peerEndpoint = "localhost:7051";
    public String overrideAuth = "peer0.org1.example.com";
}
