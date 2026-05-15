package br.unitins.util;

import org.eclipse.microprofile.jwt.JsonWebToken;

public class JwtUtil {

    public static String getLogin(JsonWebToken jwt) {
        if (jwt == null) {
            return null;
        }

        Object preferredUsername = jwt.getClaim("preferred_username");
        if (preferredUsername instanceof String username && !username.isBlank()) {
            return username;
        }

        Object upn = jwt.getClaim("upn");
        if (upn instanceof String upnValue && !upnValue.isBlank()) {
            return upnValue;
        }

        Object email = jwt.getClaim("email");
        if (email instanceof String emailValue && !emailValue.isBlank()) {
            return emailValue;
        }

        return jwt.getName();
    }
}
