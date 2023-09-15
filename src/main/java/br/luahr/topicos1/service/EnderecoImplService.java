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

import br.luahr.topicos1.dto.EnderecoDTO;
import br.luahr.topicos1.dto.EnderecoResponseDTO;
import br.luahr.topicos1.model.Endereco;
import br.luahr.topicos1.model.Municipio;
import br.luahr.topicos1.repository.EnderecoRepository;
import br.luahr.topicos1.repository.MunicipioRepository;

@ApplicationScoped
public class EnderecoImplService implements EnderecoService{
    @Inject
    EnderecoRepository enderecoRepository;

    @Inject
    MunicipioRepository municipioRepository;

    @Inject
    Validator validator;

    @Override
    public List<EnderecoResponseDTO> getAll() {
        return enderecoRepository.findAll()
                                        .stream()
                                        .map(EnderecoResponseDTO::new)
                                        .collect(Collectors.toList());
    }

    @Override
    public EnderecoResponseDTO findById(Long id) {
        Endereco endereco = enderecoRepository.findById(id);
        if (endereco == null){
            throw new NotFoundException("Não encontrado");
        }
        return new EnderecoResponseDTO(endereco);
    }

    @Override
    @Transactional
    public EnderecoResponseDTO create(EnderecoDTO enderecoDTO) throws ConstraintViolationException{
        validar(enderecoDTO);

        Endereco entity = new Endereco();
        entity.setBairro(enderecoDTO.bairro());
        entity.setNumero(enderecoDTO.numero());
        entity.setComplemento(enderecoDTO.complemento());
        entity.setCep(enderecoDTO.cep());

        Municipio municipio = municipioRepository.findById(enderecoDTO.idMunicipio());

        entity.setMunicipio(municipio);

        enderecoRepository.persist(entity);

        return new EnderecoResponseDTO(entity);
    }

    @Override
    @Transactional
    public EnderecoResponseDTO update(Long id, EnderecoDTO enderecoDTO)  throws ConstraintViolationException{
        validar(enderecoDTO);

        var entity = enderecoRepository.findById(id);
        entity.setBairro(enderecoDTO.bairro());
        entity.setNumero(enderecoDTO.numero());
        entity.setComplemento(enderecoDTO.complemento());
        entity.setCep(enderecoDTO.cep());
        Municipio municipio = municipioRepository.findById(enderecoDTO.idMunicipio());
        if (!entity.getMunicipio().getId().equals(municipio.getId())) {
            entity.setMunicipio(municipio);
        }


        return new EnderecoResponseDTO(entity);
    }

    private void validar(EnderecoDTO enderecoDTO) throws ConstraintViolationException {

        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(enderecoDTO);

        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);

    }

    @Override
    public List<EnderecoResponseDTO> findByNome(String nome) throws NullPointerException{
        List<Endereco> list = enderecoRepository.findByNome(nome);

        if (list == null)
            throw new NullPointerException("nenhum endereço encontrado");

        return list.stream()
                        .map(EnderecoResponseDTO::new)
                        .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return enderecoRepository.count();
    }

    @Override
    @Transactional
    public void delete(Long id) throws IllegalArgumentException, NotFoundException {
        if (id == null)
            throw new IllegalArgumentException("Número inválido");

        Endereco endereco = enderecoRepository.findById(id);

        if (enderecoRepository.isPersistent(endereco)) {
            enderecoRepository.delete(endereco);
        } else
            throw new NotFoundException("Nenhum usuario encontrado");
    }
}