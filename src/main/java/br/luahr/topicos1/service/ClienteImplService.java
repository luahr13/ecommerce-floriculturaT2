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
import br.luahr.topicos1.dto.ClienteDTO;
import br.luahr.topicos1.dto.ClienteResponseDTO;
import br.luahr.topicos1.model.Cliente;
import br.luahr.topicos1.model.Endereco;
import br.luahr.topicos1.model.Sexo;
import br.luahr.topicos1.model.Telefone;
import br.luahr.topicos1.repository.ClienteRepository;
import br.luahr.topicos1.repository.EnderecoRepository;
import br.luahr.topicos1.repository.MunicipioRepository;
import br.luahr.topicos1.repository.TelefoneRepository;

@ApplicationScoped
public class ClienteImplService implements ClienteService {

    @Inject
    ClienteRepository clienteRepository;

    @Inject
    TelefoneRepository telefoneRepository;

    @Inject
    EnderecoRepository enderecoRepository;

    @Inject
    MunicipioRepository municipioRepository;

    @Inject
    Validator validator;

    @Inject
    HashServiceImpl hashServiceImpl;

    @Override
    public List<ClienteResponseDTO> getAll() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteResponseDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id);
        if (cliente == null)
            throw new NotFoundException("Não encontrado");
        return new ClienteResponseDTO(cliente);
    }

    @Override
    @Transactional
    public ClienteResponseDTO create(ClienteDTO clienteDTO) throws ConstraintViolationException {
        validar(clienteDTO);

        var entity = new Cliente();
        entity.setNome(clienteDTO.nome());

        entity.setLogin(clienteDTO.login());

        entity.setSenha(hashServiceImpl.getHashSenha(clienteDTO.senha()));
        entity.setCpf(clienteDTO.cpf());

        Integer idSexo = clienteDTO.idSexo(); // Obtém o idSexo do DTO
        Sexo sexo = idSexo != null ? Sexo.valueOf(idSexo) : null; // Converte para Sexo, considerando null quando idSexo
                                                                  // for null
        entity.setSexo(sexo); // Seta o sexo no cliente

        Telefone telefone = telefoneRepository.findById(clienteDTO.idTelefone());
        entity.setTelefone(telefone);

        Endereco endereco = enderecoRepository.findById(clienteDTO.idEndereco());
        entity.setEndereco(endereco);

        clienteRepository.persist(entity);

        return new ClienteResponseDTO(entity);
    }

    @Override
    @Transactional
    public ClienteResponseDTO update(Long id, ClienteDTO clienteDTO) throws ConstraintViolationException {
        validar(clienteDTO);

        var entity = clienteRepository.findById(id);
        entity.setNome(clienteDTO.nome());
        entity.setCpf(clienteDTO.cpf());

        Integer idSexo = clienteDTO.idSexo(); // Obtém o idSexo do DTO
        Sexo sexo = idSexo != null ? Sexo.valueOf(idSexo) : null; // Converte para Sexo, considerando null quando idSexo
                                                                  // for null
        entity.setSexo(sexo); // Seta o sexo no cliente

        if (!clienteDTO.idTelefone().equals(entity.getTelefone().getId())) {
            entity.getTelefone().setId(clienteDTO.idTelefone());
        }
        if (!clienteDTO.idEndereco().equals(entity.getEndereco().getId())) {
            entity.getEndereco().setId(clienteDTO.idEndereco());
        }

        return new ClienteResponseDTO(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) throws IllegalArgumentException, NotFoundException {
        if (id == null)
            throw new IllegalArgumentException("Número inválido");

        Cliente cliente = clienteRepository.findById(id);

        if (clienteRepository.isPersistent(cliente)) {
            clienteRepository.delete(cliente);
        } else
            throw new NotFoundException("Nenhum usuario encontrado");
    }

    @Override
    public List<ClienteResponseDTO> findByNome(String nome) throws NullPointerException {
        List<Cliente> list = clienteRepository.findByNome(nome);

        if (list == null)
            throw new NullPointerException("nenhum usuario encontrado");

        return list.stream()
                .map(ClienteResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return clienteRepository.count();
    }

    @Override
    public Cliente findByLoginAndSenha(String login, String senha) {
        return clienteRepository.findByLoginAndSenha(login, senha);
    }

    @Override
    public ClienteResponseDTO findByLogin(String login) {
        Cliente cliente = clienteRepository.findByLogin(login);
        if (cliente == null)
            throw new NotFoundException("Cliente não encontrado");
        return new ClienteResponseDTO(cliente);
    }

    @Override
    @Transactional
    public ClienteResponseDTO update(Long id, String nomeImagem) {
        Cliente cliente = clienteRepository.findById(id);

        cliente.setNomeImagem(nomeImagem);

        return new ClienteResponseDTO(cliente);
    }

    // validações
    private void validar(ClienteDTO clienteDTO) throws ConstraintViolationException {

        Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(clienteDTO);

        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);
    }
}
