package br.unitins.mapper;

import br.unitins.dto.UsuarioEnderecoRequestDTO;
import br.unitins.dto.UsuarioEnderecoResponseDTO;
import br.unitins.model.UsuarioEndereco;

public class UsuarioEnderecoMapper {

    public static UsuarioEndereco toEntity(UsuarioEnderecoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        UsuarioEndereco endereco = new UsuarioEndereco();
        endereco.setLogradouro(dto.logradouro());
        endereco.setNumero(dto.numero());
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro());
        endereco.setCidade(dto.cidade());
        endereco.setEstado(dto.estado());
        endereco.setCep(dto.cep());
        return endereco;
    }

    public static UsuarioEnderecoResponseDTO toResponseDTO(UsuarioEndereco endereco) {
        if (endereco == null) {
            return null;
        }
        return new UsuarioEnderecoResponseDTO(
            endereco.getId(),
            endereco.getLogradouro(),
            endereco.getNumero(),
            endereco.getComplemento(),
            endereco.getBairro(),
            endereco.getCidade(),
            endereco.getEstado(),
            endereco.getCep()
        );
    }
}

