package br.unitins.service;

public interface KeycloakPasswordService {

    void alterarSenha(String login, String senhaAtual, String novaSenha);

    void solicitarRedefinicaoSenha(String login);

    void redefinirSenha(String login, String novaSenha);

    void enviarEmailRedefinicaoSenha(String login);
}
