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

import br.luahr.topicos1.dto.EstadoDTO;
import br.luahr.topicos1.dto.EstadoResponseDTO;
import br.luahr.topicos1.model.Estado;
import br.luahr.topicos1.repository.EstadoRepository;

@ApplicationScoped
public class EstadoImplService implements EstadoService{

    @Inject
    EstadoRepository estadoRepository;

    @Inject
    Validator validator;
    
    @Override
    public List<EstadoResponseDTO> getAll() {
        return estadoRepository.findAll()
                                        .stream()
                                        .map(EstadoResponseDTO::new)
                                        .collect(Collectors.toList());
    }

    @Override
    public EstadoResponseDTO findById(Long id) {
        Estado estado = estadoRepository.findById(id);
        if (estado == null){
            throw new NotFoundException("Não encontrado");
        }
        return new EstadoResponseDTO(estado);
    }

    @Override
    @Transactional
    public EstadoResponseDTO create(EstadoDTO estadoDTO) {
        validar(estadoDTO);

        Estado entity = new Estado();
        entity.setNome(estadoDTO.nome());
        entity.setSigla(estadoDTO.sigla());

        estadoRepository.persist(entity);

        return new EstadoResponseDTO(entity);
    }

    @Override
    @Transactional
    public EstadoResponseDTO update(Long id, EstadoDTO estadoDTO) throws ConstraintViolationException{
        validar(estadoDTO);

        Estado entity = estadoRepository.findById(id);
        entity.setNome(estadoDTO.nome());
        entity.setSigla(estadoDTO.sigla());

        return new EstadoResponseDTO(entity);
    }

    private void validar(EstadoDTO estadoDTO) throws ConstraintViolationException {

        Set<ConstraintViolation<EstadoDTO>> violations = validator.validate(estadoDTO);

        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);

    }

    @Override
    @Transactional
    public void delete(Long id) throws IllegalArgumentException, NotFoundException{
        if (id == null)
            throw new IllegalArgumentException("Número inválido");

        Estado estado = estadoRepository.findById(id);

        if (estadoRepository.isPersistent(estado)){
            estadoRepository.delete(estado);
        }else
            throw new NotFoundException("Nenhum usuario encontrado");
    }

    @Override
    public List<EstadoResponseDTO> findByNome(String nome)  throws NullPointerException{
        List<Estado> list = estadoRepository.findByNome(nome);

        if (list == null)
            throw new NullPointerException("nenhum usuario encontrado");

        return list.stream()
                    .map(EstadoResponseDTO::new)
                    .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return estadoRepository.count();
    }
    
}
