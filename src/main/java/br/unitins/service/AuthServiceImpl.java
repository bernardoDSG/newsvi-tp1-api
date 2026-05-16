package br.unitins.service;

import br.unitins.dto.AuthRequestDTO;
import br.unitins.dto.AuthResponseDTO;
import br.unitins.model.Usuario;
import br.unitins.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    HashService hashService;

    @Inject
    JwtService jwtService;

    @Override
    public AuthResponseDTO login(AuthRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByLogin(dto.login())
                .orElseThrow(() -> new WebApplicationException("Login ou senha invalidos", Status.UNAUTHORIZED));

        if (!hashService.verificarBcrypt(dto.senha(), usuario.getSenhaHash())) {
            throw new WebApplicationException("Login ou senha invalidos", Status.UNAUTHORIZED);
        }

        String token = jwtService.gerarToken(usuario);
        return new AuthResponseDTO(token, "Bearer");
    }
}

