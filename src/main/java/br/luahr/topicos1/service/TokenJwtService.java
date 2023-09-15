package br.luahr.topicos1.service;

import br.luahr.topicos1.model.Usuario;

public interface TokenJwtService {
    public String generateJwt(Usuario usuario);
}
