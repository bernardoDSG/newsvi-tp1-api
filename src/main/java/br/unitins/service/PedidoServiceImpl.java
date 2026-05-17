package br.unitins.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.unitins.dto.ItemPedidoRequestDTO;
import br.unitins.dto.PedidoRequestDTO;
import br.unitins.exception.ValidationException;
import br.unitins.model.FormaPagamento;
import br.unitins.model.ItemPedido;
import br.unitins.model.Pedido;
import br.unitins.model.Poltrona;
import br.unitins.model.Sessao;
import br.unitins.model.StatusPedido;
import br.unitins.model.Usuario;
import br.unitins.repository.PedidoRepository;
import br.unitins.repository.PoltronaRepository;
import br.unitins.repository.SessaoRepository;
import br.unitins.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class PedidoServiceImpl implements PedidoService {

    @Inject
    PedidoRepository repository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    SessaoRepository sessaoRepository;

    @Inject
    PoltronaRepository poltronaRepository;

    @Override
    @Transactional
    public Pedido create(PedidoRequestDTO dto, String loginUsuario) {
        Usuario usuario = usuarioRepository.findByLogin(loginUsuario)
                .orElseThrow(() -> new NotFoundException("Usuario autenticado nao encontrado"));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setStatus(StatusPedido.CRIADO);

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal desconto = BigDecimal.ZERO;

        for (ItemPedidoRequestDTO itemDto : dto.itens()) {
            Sessao sessao = loadSessao(itemDto.sessaoId());
            validarCapacidade(sessao, itemDto.quantidade());

            ItemPedido item = new ItemPedido();
            item.setSessao(sessao);
            item.setPoltrona(loadPoltrona(itemDto.poltronaId()));
            item.setQuantidade(itemDto.quantidade());
            item.setValorUnitario(itemDto.valorUnitario());
            item.setDescontoUnitario(itemDto.descontoUnitario() != null ? itemDto.descontoUnitario() : BigDecimal.ZERO);
            pedido.addItem(item);

            BigDecimal quantidade = BigDecimal.valueOf(item.getQuantidade());
            total = total.add(item.getValorUnitario().multiply(quantidade));
            desconto = desconto.add(item.getDescontoUnitario().multiply(quantidade));
            sessao.setCapacidadeDisponivel(sessao.getCapacidadeDisponivel() - item.getQuantidade());
        }

        pedido.setDesconto(desconto);
        pedido.setTotal(total.subtract(desconto));
        repository.persist(pedido);
        return pedido;
    }

    @Override
    public List<Pedido> findAll() {
        return repository.listAll();
    }

    @Override
    public List<Pedido> findMeusPedidos(String loginUsuario) {
        return repository.findByUsuarioLogin(loginUsuario).list();
    }

    @Override
    public Pedido findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        Pedido pedido = repository.findById(id);
        if (pedido == null) {
            throw new NotFoundException("Pedido nao encontrado com ID: " + id);
        }
        return pedido;
    }

    @Override
    public Pedido findMeuPedidoById(Long id, String loginUsuario) {
        Pedido pedido = findById(id);
        if (pedido.getUsuario() == null || !loginUsuario.equals(pedido.getUsuario().getLogin())) {
            throw new ForbiddenException("Pedido nao pertence ao usuario autenticado");
        }
        return pedido;
    }

    @Override
    @Transactional
    public void updateStatus(Long id, StatusPedido status) {
        if (status == null) {
            throw new ValidationException("Status do pedido e obrigatorio", "status");
        }
        findById(id).setStatus(status);
    }

    @Override
    @Transactional
    public Pedido iniciarPagamento(Long id, String loginUsuario, FormaPagamento formaPagamento) {
        if (formaPagamento == null) {
            throw new ValidationException("Forma de pagamento e obrigatoria", "formaPagamento");
        }

        Pedido pedido = findMeuPedidoById(id, loginUsuario);
        if (!StatusPedido.CRIADO.equals(pedido.getStatus())) {
            throw new ValidationException("Pagamento so pode ser iniciado para pedido com status CRIADO", "status");
        }

        pedido.setFormaPagamento(formaPagamento);
        pedido.setCodigoPagamento(gerarCodigoPagamento(pedido));
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        return pedido;
    }

    @Override
    @Transactional
    public Pedido confirmarPagamento(Long id) {
        Pedido pedido = findById(id);
        if (!StatusPedido.AGUARDANDO_PAGAMENTO.equals(pedido.getStatus())) {
            throw new ValidationException("Pagamento so pode ser confirmado para pedido aguardando pagamento", "status");
        }

        pedido.setStatus(StatusPedido.PAGO);
        pedido.setDataPagamento(LocalDateTime.now());
        return pedido;
    }

    @Override
    @Transactional
    public void cancelarMeuPedido(Long id, String loginUsuario) {
        Pedido pedido = findMeuPedidoById(id, loginUsuario);
        if (StatusPedido.CANCELADO.equals(pedido.getStatus())) {
            return;
        }
        if (StatusPedido.PAGO.equals(pedido.getStatus())) {
            throw new ValidationException("Pedido pago nao pode ser cancelado por este endpoint", "status");
        }
        pedido.setStatus(StatusPedido.CANCELADO);
        for (ItemPedido item : pedido.getItens()) {
            Sessao sessao = item.getSessao();
            sessao.setCapacidadeDisponivel(sessao.getCapacidadeDisponivel() + item.getQuantidade());
        }
    }

    private String gerarCodigoPagamento(Pedido pedido) {
        String forma = pedido.getFormaPagamento() != null ? pedido.getFormaPagamento().name() : "PAGAMENTO";
        String id = pedido.getId() != null ? pedido.getId().toString() : String.valueOf(System.currentTimeMillis());
        return forma + "-NEWSVI-" + id;
    }

    private Sessao loadSessao(Long sessaoId) {
        Sessao sessao = sessaoRepository.findById(sessaoId);
        if (sessao == null) {
            throw new ValidationException("Sessao informada nao existe: " + sessaoId, "sessaoId");
        }
        return sessao;
    }

    private Poltrona loadPoltrona(Long poltronaId) {
        if (poltronaId == null) {
            return null;
        }
        Poltrona poltrona = poltronaRepository.findById(poltronaId);
        if (poltrona == null) {
            throw new ValidationException("Poltrona informada nao existe: " + poltronaId, "poltronaId");
        }
        return poltrona;
    }

    private void validarCapacidade(Sessao sessao, Integer quantidade) {
        if (sessao.getCapacidadeDisponivel() == null || sessao.getCapacidadeDisponivel() < quantidade) {
            throw new ValidationException("Capacidade indisponivel para a sessao", "quantidade");
        }
    }
}
