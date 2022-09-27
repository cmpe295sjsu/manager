package com.manager.app.security;

public class SecurityConstants {
    public static final String SECRET = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 900_000; // 15 mins
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String FETCH_IPFS_HASH_URL = "/ipfs-hash/*";
    public static final String UPDATE_IPFS_HASH_URL = "/iot/ipfs-hash/";
}