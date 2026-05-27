package br.com.bb9leko.investmentmanagement.rest;

import br.com.bb9leko.investmentmanagement.dto.TransacaoDTO;
import br.com.bb9leko.investmentmanagement.model.Transacao;
import br.com.bb9leko.investmentmanagement.repository.TransacaoRepository;
import io.quarkus.logging.Log;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
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

    @Inject
    EventBus bus;

    private final Jsonb jsonb = JsonbBuilder.create();

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
    public Response buscarPorTicket(@QueryParam("q") String ticket) {
        Log.info("Requisição Recebida.");
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Interrupted").build();
        }
        List<TransacaoDTO> lista = transacaoRepository.list("ticket", ticket)
                .stream()
                .map(TransacaoDTO::new)
                .collect(Collectors.toList());
        return Response.ok(lista).build();
    }

    @POST
    @Path("/insereTransacao")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response insereTransacao(TransacaoDTO dto) {
        Transacao transacao = new Transacao(dto);
        transacaoRepository.persist(transacao);
        return Response.ok().build();
    }

    @DELETE
    @Path("/excluirTransacao/{id}")
    @Transactional
    public Response excluirTransacao(@PathParam("id") Long id) {
        Transacao transacao = transacaoRepository.findById(id);
        if (transacao == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        transacaoRepository.delete(transacao);
        return Response.noContent().build();
    }

    @GET
    @Path("/buscarTransacaoPorId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarTransacaoPorId(@PathParam("id") Long id) {
        Transacao transacao = transacaoRepository.findById(id);
        if (transacao == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        TransacaoDTO dto = new TransacaoDTO(transacao);
        return Response.ok(dto).build();
    }

    @PUT
    @Path("/editarTransacao/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response atualizarTransacao(@PathParam("id") Long id, TransacaoDTO dto) {
        Transacao transacao = transacaoRepository.findById(id);
        if (transacao == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        transacao.aplicar(dto);
        transacaoRepository.persist(transacao);
        return Response.ok(new TransacaoDTO(transacao)).build();
    }


}
