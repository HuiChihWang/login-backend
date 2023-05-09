package com.example.loginbackend.utility;


public class LinkUtility {
    public static final String HOST_ADDRESS = "http://localhost:8080";
    public static final String API_VERSION = "api/v1";
    public static final String REGISTRATION_PATH = "register";
    public static final String CONFIRMATION_PATH = "register/confirmation";

    public static String buildConfirmationLink(String token) {
        String endPoint = String.join("/", new String[]{HOST_ADDRESS, API_VERSION, CONFIRMATION_PATH});
        return endPoint + String.format("?token=%s", token);
    }
}
