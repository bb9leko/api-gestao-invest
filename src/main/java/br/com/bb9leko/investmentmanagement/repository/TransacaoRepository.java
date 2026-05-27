package br.com.bb9leko.investmentmanagement.repository;

import br.com.bb9leko.investmentmanagement.model.Transacao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransacaoRepository implements PanacheRepository<Transacao> {
}