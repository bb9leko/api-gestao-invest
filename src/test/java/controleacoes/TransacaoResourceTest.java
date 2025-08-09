package controleacoes;

import br.com.bb9leko.apigestaoinvest.model.Transacao;
import br.com.bb9leko.apigestaoinvest.repository.TransacaoRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransacaoResourceTest {

    @Inject
    TransacaoRepository transacaoRepository;

    @GET
    @Path("/listaAcoesTransacoes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listaAcoesTransacoes() {
        List<String> tickets = transacaoRepository.listAll().stream()
                .map(Transacao::getTicket)
                .collect(Collectors.toList());
        Map<String, List<String>> response = new HashMap<>();
        response.put("acoesCustodiadas", tickets);
        return Response.ok(response).build();
    }

}
