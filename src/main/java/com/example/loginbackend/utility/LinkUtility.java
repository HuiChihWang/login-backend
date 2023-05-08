package com.example.loginbackend.utility;


public class LinkUtility {
    private static final String HOST_ADDRESS = "http://localhost:8080";
    private static final String API_VERSION = "api/v1";
    private static final String REGISTRATION_PATH = "register";
    private static final String CONFIRMATION_PATH = "register/confirmation";

    public static String buildConfirmationLink(String token) {
        String endPoint = String.join("/", new String[]{HOST_ADDRESS, API_VERSION, CONFIRMATION_PATH});
        return endPoint + String.format("?token=%s", token);
    }
}
