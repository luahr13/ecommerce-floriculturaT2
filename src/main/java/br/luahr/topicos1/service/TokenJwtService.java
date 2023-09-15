package br.luahr.topicos1.service;

import br.luahr.topicos1.model.Cliente;

public interface TokenJwtService {
    public String generateJwt(Cliente cliente);
}
