package br.com.bb9leko.apigestaoinvest.rest;

import br.com.bb9leko.apigestaoinvest.dto.ClassificacaoAtivo;
import br.com.bb9leko.apigestaoinvest.dto.Evento;
import br.com.bb9leko.apigestaoinvest.dto.TransacaoDTO;
import br.com.bb9leko.apigestaoinvest.model.Transacao;
import br.com.bb9leko.apigestaoinvest.repository.TransacaoRepository;
import io.quarkus.logging.Log;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.annotation.PostConstruct;
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
        Log.info("Recebida requisição.");
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
        // valorTotal e valorTotalComTaxasEDespesas será calculado automaticamente pelo metodo @PrePersist/@PreUpdate
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

            transacao.setDataEvento(dto.getDataEvento());
            transacao.setCorretora(dto.getCorretora());
            transacao.setClassificacaoAtivo(ClassificacaoAtivo.valueOf(dto.getClassificacaoAtivo()));
            transacao.setTicket(dto.getTicket());
            transacao.setCompraOUVenda(Evento.valueOf(dto.getCompraOUVenda()));
            transacao.setQuantidade(dto.getQuantidade());
            transacao.setValorUnitario(dto.getValorUnitario());
            transacao.setValorTotal(dto.getValorTotal());
            transacao.setValorTaxaLiquidacao(dto.getValorTaxaLiquidacao());
            transacao.setValorTaxasEmolumentos(dto.getValorTaxasEmolumentos());
            transacao.setValorImpostos(dto.getValorImpostos());
            transacao.setOutrosValoresCobrados(dto.getOutrosValoresCobrados());
            transacao.setValorCorretagem(dto.getValorCorretagem());
            transacao.setValorTotalComCustosEDespesas(dto.getValorTotalComCustosEDespesas());

            transacaoRepository.persist(transacao);
            TransacaoDTO dtoAtualizado = new TransacaoDTO(transacao);

            return Response.ok(dtoAtualizado).build();
    }


}
