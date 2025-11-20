package br.com.bb9leko.gestaodividendos.repository;

import br.com.bb9leko.gestaodividendos.model.Dividendos;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DividendosRepository implements PanacheRepository<Dividendos> {
}