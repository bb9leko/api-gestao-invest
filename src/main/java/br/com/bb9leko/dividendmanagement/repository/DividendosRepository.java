package br.com.bb9leko.dividendmanagement.repository;

import br.com.bb9leko.dividendmanagement.model.Dividendos;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DividendosRepository implements PanacheRepository<Dividendos> {
}