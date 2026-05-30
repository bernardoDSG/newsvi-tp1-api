package br.unitins.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.unitins.exception.ValidationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class KeycloakPasswordServiceImpl implements KeycloakPasswordService {

    @ConfigProperty(name = "quarkus.oidc.auth-server-url")
    String authServerUrl;

    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientId;

    @ConfigProperty(name = "quarkus.oidc.credentials.secret")
    String clientSecret;

    @ConfigProperty(name = "newsvi.keycloak.admin.username", defaultValue = "admin")
    String adminUsername;

    @ConfigProperty(name = "newsvi.keycloak.admin.password", defaultValue = "admin")
    String adminPassword;

    @Inject
    ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public void alterarSenha(String login, String senhaAtual, String novaSenha) {
        validarSenhaAtual(login, senhaAtual);
        redefinirSenha(login, novaSenha);
    }

    @Override
    public void solicitarRedefinicaoSenha(String login) {
        String adminToken = obterAdminToken();
        String userId = buscarUsuarioKeycloak(adminToken, login);
        enviarEmailRedefinicaoSenha(adminToken, userId);
    }

    @Override
    public void redefinirSenha(String login, String novaSenha) {
        String adminToken = obterAdminToken();
        String userId = buscarUsuarioKeycloak(adminToken, login);
        resetarSenha(adminToken, userId, novaSenha);
    }

    @Override
    public void enviarEmailRedefinicaoSenha(String login) {
        String adminToken = obterAdminToken();
        String userId = buscarUsuarioKeycloak(adminToken, login);
        enviarEmailAcaoRedefinirSenha(adminToken, userId);
    }

    private void validarSenhaAtual(String login, String senhaAtual) {
        String body = formBody(Map.of(
            "client_id", clientId,
            "client_secret", clientSecret,
            "grant_type", "password",
            "username", login,
            "password", senhaAtual
        ));

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl() + "/realms/" + realm() + "/protocol/openid-connect/token"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

        HttpResponse<String> response = send(request);
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new ValidationException("Senha atual invalida", "senhaAtual");
        }
    }

    private String obterAdminToken() {
        String body = formBody(Map.of(
            "client_id", "admin-cli",
            "grant_type", "password",
            "username", adminUsername,
            "password", adminPassword
        ));

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl() + "/realms/master/protocol/openid-connect/token"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

        HttpResponse<String> response = send(request);
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new ValidationException("Nao foi possivel autenticar o admin do Keycloak");
        }

        try {
            return objectMapper.readTree(response.body()).get("access_token").asText();
        } catch (IOException | NullPointerException e) {
            throw new ValidationException("Resposta invalida ao obter token admin do Keycloak");
        }
    }

    private String buscarUsuarioKeycloak(String adminToken, String login) {
        String userId = buscarUsuarioPorParametro(adminToken, "username", login);
        if (userId != null) {
            return userId;
        }

        userId = buscarUsuarioPorParametro(adminToken, "email", login);
        if (userId != null) {
            return userId;
        }

        throw new NotFoundException("Usuario nao encontrado no Keycloak");
    }

    private String buscarUsuarioPorParametro(String adminToken, String parametro, String valor) {
        String encoded = URLEncoder.encode(valor, StandardCharsets.UTF_8);
        String uri = baseUrl() + "/admin/realms/" + realm() + "/users?" + parametro + "=" + encoded;
        if ("username".equals(parametro)) {
            uri += "&exact=true";
        }

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .header("Authorization", "Bearer " + adminToken)
            .GET()
            .build();

        HttpResponse<String> response = send(request);
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new ValidationException("Nao foi possivel consultar usuarios no Keycloak");
        }

        try {
            JsonNode users = objectMapper.readTree(response.body());
            if (users.isArray() && users.size() > 0) {
                return users.get(0).get("id").asText();
            }
            return null;
        } catch (IOException | NullPointerException e) {
            throw new ValidationException("Resposta invalida ao consultar usuarios no Keycloak");
        }
    }

    private void enviarEmailRedefinicaoSenha(String adminToken, String userId) {
        String body;
        try {
            body = objectMapper.writeValueAsString(List.of("UPDATE_PASSWORD"));
        } catch (IOException e) {
            throw new ValidationException("Nao foi possivel montar requisicao de redefinicao de senha");
        }

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl() + "/admin/realms/" + realm() + "/users/" + userId + "/execute-actions-email"))
            .header("Authorization", "Bearer " + adminToken)
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(body))
            .build();

        HttpResponse<String> response = send(request);
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new ValidationException("Nao foi possivel enviar email de redefinicao pelo Keycloak. Status "
                    + response.statusCode() + ": " + response.body());
        }
    }

    private void resetarSenha(String adminToken, String userId, String novaSenha) {
        String body;
        try {
            body = objectMapper.writeValueAsString(Map.of(
                "type", "password",
                "value", novaSenha,
                "temporary", false
            ));
        } catch (IOException e) {
            throw new ValidationException("Nao foi possivel montar requisicao de senha");
        }

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl() + "/admin/realms/" + realm() + "/users/" + userId + "/reset-password"))
            .header("Authorization", "Bearer " + adminToken)
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(body))
            .build();

        HttpResponse<String> response = send(request);
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new ValidationException("Nao foi possivel redefinir senha no Keycloak");
        }
    }

    private void enviarEmailAcaoRedefinirSenha(String adminToken, String userId) {
        String body;
        try {
            body = objectMapper.writeValueAsString(List.of("UPDATE_PASSWORD"));
        } catch (IOException e) {
            throw new ValidationException("Nao foi possivel montar requisicao de email");
        }

        String encodedClientId = URLEncoder.encode(clientId, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl() + "/admin/realms/" + realm() + "/users/" + userId
                    + "/execute-actions-email?client_id=" + encodedClientId + "&lifespan=900"))
            .header("Authorization", "Bearer " + adminToken)
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(body))
            .build();

        HttpResponse<String> response = send(request);
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new ValidationException("Nao foi possivel enviar email de redefinicao de senha pelo Keycloak");
        }
    }

    private HttpResponse<String> send(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new ValidationException("Falha de comunicacao com o Keycloak");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ValidationException("Comunicacao com o Keycloak interrompida");
        }
    }

    private String formBody(Map<String, String> params) {
        StringBuilder body = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (body.length() > 0) {
                body.append('&');
            }
            body.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            body.append('=');
            body.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return body.toString();
    }

    private String baseUrl() {
        int realmIndex = authServerUrl.indexOf("/realms/");
        if (realmIndex < 0) {
            throw new ValidationException("quarkus.oidc.auth-server-url invalido");
        }
        return authServerUrl.substring(0, realmIndex);
    }

    private String realm() {
        int realmIndex = authServerUrl.indexOf("/realms/");
        if (realmIndex < 0) {
            throw new ValidationException("quarkus.oidc.auth-server-url invalido");
        }
        return authServerUrl.substring(realmIndex + "/realms/".length());
    }
}
