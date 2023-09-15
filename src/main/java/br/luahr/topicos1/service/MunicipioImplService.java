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

import br.luahr.topicos1.dto.MunicipioDTO;
import br.luahr.topicos1.dto.MunicipioResponseDTO;
import br.luahr.topicos1.model.Estado;
import br.luahr.topicos1.model.Municipio;
import br.luahr.topicos1.repository.EstadoRepository;
import br.luahr.topicos1.repository.MunicipioRepository;

@ApplicationScoped
public class MunicipioImplService implements MunicipioService{

    @Inject
    MunicipioRepository municipioRepository;

    @Inject
    EstadoRepository estadoRepository;

    @Inject
    Validator validator;
    
    @Override
    public List<MunicipioResponseDTO> getAll() {
        return municipioRepository.findAll()
                                        .stream()
                                        .map(MunicipioResponseDTO::new)
                                        .collect(Collectors.toList());
    }

    @Override
    public MunicipioResponseDTO findById(Long id) {
        Municipio municipio = municipioRepository.findById(id);
        if (municipio == null){
            throw new NotFoundException("Não encontrado");
        }
        return new MunicipioResponseDTO(municipio);
    }

    @Override
    @Transactional
    public MunicipioResponseDTO create(MunicipioDTO municipioDTO) throws ConstraintViolationException{
        validar(municipioDTO);

        var entity = new Municipio();
        entity.setNome(municipioDTO.nome());
        
        entity.setEstado(new Estado());
        entity.getEstado().setId(municipioDTO.idEstado());

        municipioRepository.persist(entity);

        return new MunicipioResponseDTO(entity);

    }

    @Override
    @Transactional
    public MunicipioResponseDTO update(Long id, MunicipioDTO municipioDTO) throws ConstraintViolationException{
        validar(municipioDTO);

        var entity = municipioRepository.findById(id);
        entity.setNome(municipioDTO.nome());
        
        if(!municipioDTO.idEstado().equals(entity.getEstado().getId()) ){
        entity.getEstado().setId(municipioDTO.idEstado());;
        }

        return new MunicipioResponseDTO(entity);
    }

    private void validar(MunicipioDTO municipioDTO) throws ConstraintViolationException {

        Set<ConstraintViolation<MunicipioDTO>> violations = validator.validate(municipioDTO);

        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);

    }

    @Override
    @Transactional
    public void delete(Long id) throws IllegalArgumentException, NotFoundException{
        if (id == null){
            throw new IllegalArgumentException("Número inválido");
        }
        var municipio = municipioRepository.findById(id);

        if (municipioRepository.isPersistent(municipio)){
            municipioRepository.delete(municipio);
        }else
            throw new NotFoundException("Nenhum usuario encontrado");
    }

    @Override
    public List<MunicipioResponseDTO> findByNome(String nome) throws NullPointerException{
        List<Municipio> list = municipioRepository.findByNome(nome);

        if (list == null)
            throw new NullPointerException("nenhum município encontrado");

        return list.stream()
                        .map(MunicipioResponseDTO::new)
                        .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return municipioRepository.count();
    }
    
}
