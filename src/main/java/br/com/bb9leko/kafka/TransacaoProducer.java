package br.com.bb9leko.kafka;

import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class TransacaoProducer {

    @Inject
    @Channel("transacoes-out")
    Emitter<String> emitter;

    public void enviar(String mensagem) {
        emitter.send(mensagem);
    }
}
