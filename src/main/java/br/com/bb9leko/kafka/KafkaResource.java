package br.com.bb9leko.kafka;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/kafka")
public class KafkaResource {

    @Inject
    TransacaoProducer producer;

    @POST
    @Path("/enviar")
    public Response enviar(@QueryParam("msg") String mensagem) {
        producer.enviar(mensagem);
        Log.infof(">>> Resource -> Mensagem enviada do Kafka: %s", mensagem);
        return Response.ok("Mensagem enviada: " + mensagem).build();
    }
}
