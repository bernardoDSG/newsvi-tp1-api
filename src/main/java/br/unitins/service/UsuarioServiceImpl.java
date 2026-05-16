package br.unitins.service;

import java.util.List;

import br.unitins.dto.UsuarioCompleteRegisterRequestDTO;
import br.unitins.dto.UsuarioPasswordChangeRequestDTO;
import br.unitins.dto.UsuarioPasswordForgotRequestDTO;
import br.unitins.dto.UsuarioRegisterRequestDTO;
import br.unitins.dto.UsuarioResponseDTO;
import br.unitins.dto.UsuarioUpdateRequestDTO;
import br.unitins.exception.ValidationException;
import br.unitins.mapper.UsuarioMapper;
import br.unitins.model.Perfil;
import br.unitins.model.Usuario;
import br.unitins.model.UsuarioEndereco;
import br.unitins.repository.UsuarioEnderecoRepository;
import br.unitins.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    UsuarioEnderecoRepository enderecoRepository;

    @Inject
    HashService hashService;

    @Override
    @Transactional
    public UsuarioResponseDTO register(UsuarioRegisterRequestDTO dto) {
        validateUniqueLogin(dto.login());
        validateUniqueEmail(dto.email());

        Usuario usuario = new Usuario();
        usuario.setLogin(dto.login());
        usuario.setEmail(dto.email());
        usuario.setNome(dto.nome());
        usuario.setSenhaHash(hashService.bcrypt(dto.senha()));
        usuario.setPerfil(Perfil.CLIENTE);
        usuarioRepository.persist(usuario);
        return UsuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO registerComplete(UsuarioCompleteRegisterRequestDTO dto) {
        UsuarioResponseDTO usuarioResponse = register(new UsuarioRegisterRequestDTO(dto.login(), dto.senha(), dto.email(), dto.nome()));

        Usuario usuario = usuarioRepository.findByLogin(dto.login())
                .orElseThrow(() -> new IllegalStateException("Usuario criado nao encontrado"));

        UsuarioEndereco endereco = new UsuarioEndereco();
        endereco.setLogradouro(dto.logradouro());
        endereco.setNumero(dto.numero());
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro());
        endereco.setCidade(dto.cidade());
        endereco.setEstado(dto.estado());
        endereco.setCep(dto.cep());
        endereco.setUsuario(usuario);
        enderecoRepository.persist(endereco);
        return usuarioResponse;
    }

    @Override
    public UsuarioResponseDTO findCurrent(String login) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado"));
        return UsuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO updateCurrent(String login, UsuarioUpdateRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado"));

        if (dto.email() != null && !dto.email().equals(usuario.getEmail())) {
            validateUniqueEmail(dto.email());
            usuario.setEmail(dto.email());
        }
        if (dto.nome() != null) {
            usuario.setNome(dto.nome());
        }
        return UsuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional
    public void changePassword(String login, UsuarioPasswordChangeRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado"));

        if (!hashService.verificarBcrypt(dto.senhaAtual(), usuario.getSenhaHash())) {
            throw new BadRequestException("Senha atual invalida");
        }
        usuario.setSenhaHash(hashService.bcrypt(dto.novaSenha()));
    }

    @Override
    @Transactional
    public void forgotPassword(UsuarioPasswordForgotRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByLogin(dto.login())
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado"));
        usuario.setSenhaHash(hashService.bcrypt(dto.novaSenha()));
    }

    @Override
    public Usuario findByLogin(String login) {
        return usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado"));
    }

    @Override
    public List<UsuarioResponseDTO> listAll() {
        return usuarioRepository.listAll().stream().map(UsuarioMapper::toResponseDTO).toList();
    }

    private void validateUniqueLogin(String login) {
        if (usuarioRepository.findByLogin(login).isPresent()) {
            throw new ValidationException("Login ja cadastrado", "login");
        }
    }

    private void validateUniqueEmail(String email) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new ValidationException("Email ja cadastrado", "email");
        }
    }
}

