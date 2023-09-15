package br.luahr.topicos1.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;

import br.luahr.topicos1.dto.CompraDTO;
import br.luahr.topicos1.dto.CompraResponseDTO;
import br.luahr.topicos1.model.Cliente;
import br.luahr.topicos1.model.Compra;
import br.luahr.topicos1.model.Flor;
import br.luahr.topicos1.repository.ClienteRepository;
import br.luahr.topicos1.repository.CompraRepository;
import br.luahr.topicos1.repository.FlorRepository;

@ApplicationScoped
public class CompraImplService implements CompraService {

    @Inject
    CompraRepository compraRepository;

    @Inject
    ClienteRepository clienteRepository;

    @Inject
    FlorRepository florRepository;

    @Inject
    Validator validator;

    @Override
    public List<CompraResponseDTO> getAll() {
        return compraRepository.findAll()
                                        .stream()
                                        .map(CompraResponseDTO::new)
                                        .collect(Collectors.toList());
    }

    @Override
    public CompraResponseDTO findById(Long id) {
        Compra compra = compraRepository.findById(id);
        if (compra == null)
            return null;
        return new CompraResponseDTO(compra);
    }

    private void validar(CompraDTO compradto) throws ConstraintViolationException {
        Set<ConstraintViolation<CompraDTO>> violations = validator.validate(compradto);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);
    }

    @Override
    @Transactional
    public CompraResponseDTO create(CompraDTO compraDTO) throws ConstraintViolationException {
        validar(compraDTO);

        Compra entity = new Compra();
        Cliente cliente = clienteRepository.findById(compraDTO.idCliente());
        entity.setCliente(cliente);

        Flor flor = florRepository.findById(compraDTO.idProduto());
        entity.setItemProduto(flor);

        entity.setQuantidadeProduto(compraDTO.quantidaProduto());
        entity.setTotalCompra(compraDTO.totalCompra());

        compraRepository.persist(entity);

        return new CompraResponseDTO(entity);
    }

    @Override
    @Transactional
    public CompraResponseDTO update(Long id, CompraDTO compradto) {
        Compra compraUpdate = compraRepository.findById(id);
        if (compraUpdate == null)
            throw new NotFoundException("Compra nÃ£o encontrada.");
        validar(compradto);

        compraUpdate.setItemProduto(compraUpdate.getItemProduto());
        compraUpdate.setQuantidadeProduto(compradto.quantidaProduto());

        compraRepository.persist(compraUpdate);
        return new CompraResponseDTO(compraUpdate);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        compraRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return compraRepository.count();
    }

    @Override
    public List<CompraResponseDTO> findByNome(String nome) throws NullPointerException{
        List<Compra> list = compraRepository.findByNome(nome);

        if (list == null)
            throw new NullPointerException("nenhum usuario encontrado");

        return list.stream()
                .map(CompraResponseDTO::new)
                .collect(Collectors.toList());
    }
}
