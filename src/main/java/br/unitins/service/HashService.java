package br.unitins.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HashService {

    public String sha256(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("O valor para hash nao pode ser nulo");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(valor.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(hashBytes.length * 2);

            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo SHA-256 indisponivel", e);
        }
    }

    public String bcrypt(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("O valor para hash nao pode ser nulo");
        }

        return BCrypt.hashpw(valor, BCrypt.gensalt());
    }

    public boolean verificarBcrypt(String valor, String hash) {
        if (valor == null || hash == null) {
            throw new IllegalArgumentException("Valor e hash nao podem ser nulos");
        }

        return BCrypt.checkpw(valor, hash);
    }
}
