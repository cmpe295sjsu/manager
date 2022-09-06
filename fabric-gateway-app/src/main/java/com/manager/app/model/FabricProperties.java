package com.manager.app.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("fabric")
public class FabricProperties {
    private String mspId;
    private String channelName;
    private String chaincodeName;
    private String parentPath;
    private String cryptoPath;
    private String certPath;
    private String keyPath;
    private String tlscertPath;
    private String peerEndpoint;
    private String overrideAuth;

    public String getMspId() {
        return mspId;
    }

    public void setMspId(String mspId) {
        this.mspId = mspId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChaincodeName() {
        return chaincodeName;
    }

    public void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getCryptoPath() {
        return cryptoPath;
    }

    public void setCryptoPath(String cryptoPath) {
        this.cryptoPath = cryptoPath;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }

    public String getTlscertPath() {
        return tlscertPath;
    }

    public void setTlscertPath(String tlscertPath) {
        this.tlscertPath = tlscertPath;
    }

    public String getPeerEndpoint() {
        return peerEndpoint;
    }

    public void setPeerEndpoint(String peerEndpoint) {
        this.peerEndpoint = peerEndpoint;
    }

    public String getOverrideAuth() {
        return overrideAuth;
    }

    public void setOverrideAuth(String overrideAuth) {
        this.overrideAuth = overrideAuth;
    }
}
