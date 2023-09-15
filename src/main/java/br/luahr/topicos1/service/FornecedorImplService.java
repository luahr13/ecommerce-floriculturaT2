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

import br.luahr.topicos1.dto.FornecedorDTO;
import br.luahr.topicos1.dto.FornecedorResponseDTO;
import br.luahr.topicos1.model.Fornecedor;
import br.luahr.topicos1.repository.FornecedorRepository;

@ApplicationScoped
public class FornecedorImplService implements FornecedorService{

    @Inject
    FornecedorRepository fornecedorRepository;

    @Inject
    Validator validator;

    @Override
    public List<FornecedorResponseDTO> getAll() {
        return fornecedorRepository.findAll()
                                        .stream()
                                        .map(FornecedorResponseDTO::new)
                                        .collect(Collectors.toList());
    }

    @Override
    public FornecedorResponseDTO findById(Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id);
        if (fornecedor == null){
            throw new NotFoundException("Não encontrado");
        }
        return new FornecedorResponseDTO(fornecedor);
    }

    @Override
    @Transactional
    public FornecedorResponseDTO create(FornecedorDTO fornecedorDTO) {
        validar(fornecedorDTO);

        Fornecedor entity = new Fornecedor();
        entity.setNome(fornecedorDTO.nome());
        entity.setPais(fornecedorDTO.pais());
        entity.setSafra(fornecedorDTO.safra());
        entity.setVolume(fornecedorDTO.volume());

        fornecedorRepository.persist(entity);

        return new FornecedorResponseDTO(entity);
    }

    @Override
    @Transactional
    public FornecedorResponseDTO update(Long id, FornecedorDTO fornecedorDTO) throws ConstraintViolationException{
        validar(fornecedorDTO);

        Fornecedor entity = fornecedorRepository.findById(id);
        entity.setNome(fornecedorDTO.nome());
        entity.setPais(fornecedorDTO.pais());
        entity.setSafra(fornecedorDTO.safra());
        entity.setVolume(fornecedorDTO.volume());

        return new FornecedorResponseDTO(entity);
    }

    private void validar(FornecedorDTO fornecedorDTO) throws ConstraintViolationException {

        Set<ConstraintViolation<FornecedorDTO>> violations = validator.validate(fornecedorDTO);

        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);

    }

    @Override
    @Transactional
    public void delete(Long id) throws IllegalArgumentException, NotFoundException{
        if (id == null)
            throw new IllegalArgumentException("Número inválido");

        Fornecedor fornecedor = fornecedorRepository.findById(id);

        if (fornecedorRepository.isPersistent(fornecedor)){
            fornecedorRepository.delete(fornecedor);
        }else
            throw new NotFoundException("Nenhum usuario encontrado");
    }

    @Override
    public List<FornecedorResponseDTO> findByNome(String nome) throws NullPointerException{
        List<Fornecedor> list = fornecedorRepository.findByNome(nome);

        if (list == null)
            throw new NullPointerException("nenhum usuario encontrado");

        return list.stream()
                    .map(FornecedorResponseDTO::new)
                    .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return fornecedorRepository.count();
    }
}
