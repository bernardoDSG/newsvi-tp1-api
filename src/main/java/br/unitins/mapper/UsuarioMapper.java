package br.unitins.mapper;

import br.unitins.dto.UsuarioResponseDTO;
import br.unitins.model.Usuario;

public class UsuarioMapper {

    public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getLogin(),
            usuario.getPerfil() != null ? usuario.getPerfil().name() : null
        );
    }
}
