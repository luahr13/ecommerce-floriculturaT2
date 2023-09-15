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

import br.luahr.topicos1.dto.FlorDTO;
import br.luahr.topicos1.dto.FlorResponseDTO;
import br.luahr.topicos1.model.Flor;
import br.luahr.topicos1.model.Fornecedor;
import br.luahr.topicos1.model.TipoFlor;
import br.luahr.topicos1.repository.FlorRepository;

@ApplicationScoped
public class FlorImplService implements FlorService{

    @Inject
    FlorRepository florRepository;

    @Inject
    Validator validator;

    @Override
    public List<FlorResponseDTO> getAll() {
        return  florRepository.findAll()
                                        .stream()
                                        .map(FlorResponseDTO::new)
                                        .collect(Collectors.toList());
    }

    @Override
    public FlorResponseDTO findById(Long id) {
        Flor flor = florRepository.findById(id);
        if (flor == null){
            throw new NotFoundException("Não encontrado");
        }
        return new FlorResponseDTO(flor);
    }

    @Override
    @Transactional
    public FlorResponseDTO create(FlorDTO florDTO) {
        validar(florDTO);

        Flor entity = new Flor();

        entity.setNome(florDTO.nome());
        entity.setDescricao(florDTO.descricao());
        entity.setValorUnidade(florDTO.valorUnidade());
        entity.setCorPetalas(florDTO.corPetalas());
        entity.setAlturaCaule(florDTO.alturaCaule());
        entity.setTipoFlor(TipoFlor.valueOf(florDTO.tipoFlor()));
        entity.setFornecedor(new Fornecedor());
        entity.getFornecedor().setId(florDTO.idFornecedor());

        florRepository.persist(entity);

        return new FlorResponseDTO(entity);

    }

    @Override
    @Transactional
    public FlorResponseDTO update(Long id, FlorDTO florDTO) {
        validar(florDTO);
        
        Flor entity = florRepository.findById(id);
        entity.setNome(florDTO.nome());
        entity.setDescricao(florDTO.descricao());
        entity.setValorUnidade(florDTO.valorUnidade());
        entity.setCorPetalas(florDTO.corPetalas());
        entity.setAlturaCaule(florDTO.alturaCaule());
        entity.setTipoFlor(TipoFlor.valueOf(florDTO.tipoFlor()));
        entity.setFornecedor(new Fornecedor());
        entity.getFornecedor().setId(florDTO.idFornecedor());

        return new FlorResponseDTO(entity);

    }

    private void validar(FlorDTO florDTO) throws ConstraintViolationException {

        Set<ConstraintViolation<FlorDTO>> violations = validator.validate(florDTO);

        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);

    }

    @Override
    @Transactional
    public void delete(Long id) throws IllegalArgumentException, NotFoundException{
        if (id == null){
            throw new IllegalArgumentException("Número inválido");
        }
        Flor flor = florRepository.findById(id);

        if (florRepository.isPersistent(flor)){
            florRepository.delete(flor);
        }else
            throw new NotFoundException("Nenhum usuario encontrado");
    }

    @Override
    public List<FlorResponseDTO> findByNome(String nome) throws NullPointerException{
        List<Flor> list = florRepository.findByNome(nome);

        if (list == null)
            throw new NullPointerException("nenhum usuario encontrado");

        return list.stream()
                    .map(FlorResponseDTO::new)
                    .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return florRepository.count();
    }

    @Override
    public Flor updateImg(Long id, String nomeImagem) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateImg'");
    }
    
}
