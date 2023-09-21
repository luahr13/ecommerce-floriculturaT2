package br.luahr.topicos1.service;

import java.util.ArrayList;
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
import br.luahr.topicos1.dto.UsuarioDTO;
import br.luahr.topicos1.dto.UsuarioResponseDTO;
import br.luahr.topicos1.model.Usuario;
import br.luahr.topicos1.model.Endereco;
import br.luahr.topicos1.model.Sexo;
import br.luahr.topicos1.model.Telefone;
import br.luahr.topicos1.repository.UsuarioRepository;
import br.luahr.topicos1.repository.EnderecoRepository;
import br.luahr.topicos1.repository.MunicipioRepository;
import br.luahr.topicos1.repository.TelefoneRepository;

@ApplicationScoped
public class UsuarioImplService implements UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

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
    public List<UsuarioResponseDTO> getAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null)
            throw new NotFoundException("Não encontrado");
        return new UsuarioResponseDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO create(UsuarioDTO usuarioDTO) throws ConstraintViolationException {
        validar(usuarioDTO);

        var entity = new Usuario();
        entity.setNome(usuarioDTO.nome());

        entity.setLogin(usuarioDTO.login());

        entity.setSenha(hashServiceImpl.getHashSenha(usuarioDTO.senha()));
        entity.setCpf(usuarioDTO.cpf());

        Integer idSexo = usuarioDTO.idSexo();
        Sexo sexo = idSexo != null ? Sexo.valueOf(idSexo) : null;
        entity.setSexo(sexo);

        List<Telefone> telefones = new ArrayList<>();
        for (Telefone telefoneDTO : usuarioDTO.telefones()) {
            Telefone telefone = new Telefone();
            telefone.setNumero(telefoneDTO.getNumero());
            telefones.add(telefone);
        }
        entity.setTelefone(telefones);

        entity.setEndereco(usuarioDTO.endereco());

        usuarioRepository.persist(entity);

        return new UsuarioResponseDTO(entity);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO update(Long id, UsuarioDTO usuarioDTO) throws ConstraintViolationException {
        validar(usuarioDTO);

        var entity = usuarioRepository.findById(id);
        entity.setNome(usuarioDTO.nome());
        entity.setCpf(usuarioDTO.cpf());

        Integer idSexo = usuarioDTO.idSexo(); // Obtém o idSexo do DTO
        Sexo sexo = idSexo != null ? Sexo.valueOf(idSexo) : null; // Converte para Sexo, considerando null quando idSexo
                                                                  // for null
        entity.setSexo(sexo); // Seta o sexo no usuario

        return new UsuarioResponseDTO(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) throws IllegalArgumentException, NotFoundException {
        if (id == null)
            throw new IllegalArgumentException("Número inválido");

        Usuario usuario = usuarioRepository.findById(id);

        if (usuarioRepository.isPersistent(usuario)) {
            usuarioRepository.delete(usuario);
        } else
            throw new NotFoundException("Nenhum usuario encontrado");
    }

    @Override
    public List<UsuarioResponseDTO> findByNome(String nome) throws NullPointerException {
        List<Usuario> list = usuarioRepository.findByNome(nome);

        if (list == null)
            throw new NullPointerException("nenhum usuario encontrado");

        return list.stream()
                .map(UsuarioResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return usuarioRepository.count();
    }

    @Override
    public Usuario findByLoginAndSenha(String login, String senha) {
        return usuarioRepository.findByLoginAndSenha(login, senha);
    }

    @Override
    public UsuarioResponseDTO findByLogin(String login) {
        Usuario usuario = usuarioRepository.findByLogin(login);
        if (usuario == null)
            throw new NotFoundException("Usuario não encontrado");
        return new UsuarioResponseDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO update(Long id, String nomeImagem) {
        Usuario usuario = usuarioRepository.findById(id);

        usuario.setNomeImagem(nomeImagem);

        return new UsuarioResponseDTO(usuario);
    }

    // validações
    private void validar(UsuarioDTO usuarioDTO) throws ConstraintViolationException {

        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);

        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);
    }
}
