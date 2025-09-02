package br.com.bb9leko.apigestaoinvest.rest;

import br.com.bb9leko.apigestaoinvest.dto.TransacaoDTO;
import br.com.bb9leko.apigestaoinvest.model.Transacao;
import br.com.bb9leko.apigestaoinvest.repository.TransacaoRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/transacao")
public class TransacaoResource {

    @Inject
    TransacaoRepository transacaoRepository;

    @GET
    @Path("/listaTransacoes")
    public List<TransacaoDTO> listarTransacoes() {
        return transacaoRepository.listAll()
                .stream()
                .map(TransacaoDTO::new)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/buscarPorTicket")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TransacaoDTO> buscarPorTicket(@QueryParam("q") String ticket) {
        return transacaoRepository.find("ticket", ticket)
                .stream()
                .map(TransacaoDTO::new)
                .collect(Collectors.toList());
    }

    @POST
    @Path("/insereTransacao")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response insereTransacao(TransacaoDTO dto) {
        Transacao transacao = new Transacao(dto);
        // valorTotal e valorTotalComTaxasEDespesas será calculado automaticamente pelo metodo @PrePersist/@PreUpdate
        transacaoRepository.persist(transacao);
        return Response.ok().build();
    }

}
