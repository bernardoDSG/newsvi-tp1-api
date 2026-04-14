package br.unitins.mapper;

import br.unitins.dto.EnderecoRequestDTO;
import br.unitins.dto.EnderecoResponseDTO;
import br.unitins.model.Endereco;

public class EnderecoMapper {
   
    public static Endereco toEntity(EnderecoRequestDTO dto) {
        if (dto == null) return null;
        Endereco endereco = new Endereco();
        endereco.setLogradouro(dto.logradouro());
        endereco.setNumero(dto.numero());
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro());
        endereco.setCidade(dto.cidade());
        endereco.setEstado(dto.estado());
        endereco.setCep(dto.cep());
        return endereco;
    }

    public static EnderecoResponseDTO toResponseDTO(Endereco endereco) {
        if (endereco == null) return null;
        return new EnderecoResponseDTO(
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