package br.com.bb9leko.apigestaoinvest.repository;

import br.com.bb9leko.apigestaoinvest.model.Transacao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransacaoRepository implements PanacheRepository<Transacao> {
}