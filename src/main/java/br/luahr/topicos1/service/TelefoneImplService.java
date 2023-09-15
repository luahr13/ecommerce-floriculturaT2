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

import br.luahr.topicos1.dto.TelefoneDTO;
import br.luahr.topicos1.dto.TelefoneResponseDTO;
import br.luahr.topicos1.model.Telefone;
import br.luahr.topicos1.repository.TelefoneRepository;

@ApplicationScoped
public class TelefoneImplService implements TelefoneService{

    @Inject
    TelefoneRepository telefoneRepository;

    @Inject
    Validator validator;
    
    @Override
    public List<TelefoneResponseDTO> getAll() {
        return telefoneRepository.findAll()
                                        .stream()
                                        .map(TelefoneResponseDTO::new)
                                        .collect(Collectors.toList());
    }

    @Override
    public TelefoneResponseDTO findById(Long id) {
        Telefone telefone = telefoneRepository.findById(id);
        if (telefone == null){
            throw new NotFoundException("Não encontrado");
        }
        return new TelefoneResponseDTO(telefone);
    }

    @Override
    @Transactional
    public TelefoneResponseDTO create(TelefoneDTO telefoneDTO) throws ConstraintViolationException{
        validar(telefoneDTO);

        Telefone entity = new Telefone();
        entity.setCodigoArea(telefoneDTO.codigoArea());
        entity.setNumero(telefoneDTO.numero());

        telefoneRepository.persist(entity);

        return new TelefoneResponseDTO(entity);
    }

    @Override
    @Transactional
    public TelefoneResponseDTO update(Long id, TelefoneDTO telefoneDTO) throws ConstraintViolationException{
        validar(telefoneDTO);

        Telefone entity = telefoneRepository.findById(id);
        entity.setCodigoArea(telefoneDTO.codigoArea());
        entity.setNumero(telefoneDTO.numero());

        return new TelefoneResponseDTO(entity);
    }

    private void validar(TelefoneDTO telefoneDTO) throws ConstraintViolationException {

        Set<ConstraintViolation<TelefoneDTO>> violations = validator.validate(telefoneDTO);

        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);

    }

    @Override
    @Transactional
    public void delete(Long id) throws IllegalArgumentException, NotFoundException{
        if (id == null)
            throw new IllegalArgumentException("Número inválido");

        Telefone telefone = telefoneRepository.findById(id);

        if (telefoneRepository.isPersistent(telefone)){
            telefoneRepository.delete(telefone);
        }else
            throw new NotFoundException("Nenhum usuario encontrado");
    }

    @Override
    public List<TelefoneResponseDTO> findByNome(String nome) throws NullPointerException{
        List<Telefone> list = telefoneRepository.findByNome(nome);

        if (list == null)
            throw new NullPointerException("nenhum usuario encontrado");

        return list.stream()
                    .map(TelefoneResponseDTO::new)
                    .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return telefoneRepository.count();
    }
    
}
