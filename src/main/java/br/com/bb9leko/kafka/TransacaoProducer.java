package br.com.bb9leko.kafka;

import io.smallrye.reactive.messaging.MutinyEmitter;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TransacaoProducer {

    private static final Logger log = Logger.getLogger(TransacaoProducer.class);

    @Inject
    @Channel("transacoes-out")
    MutinyEmitter<String> emitter;

    public void enviar(String mensagem) {
        OutgoingKafkaRecordMetadata<?> metadata = OutgoingKafkaRecordMetadata.builder()
                .withTopic("transacoes")
                .build();

        emitter.sendMessageAndAwait(Message.of(mensagem).withMetadata(Metadata.of(metadata)));

        log.info("Tópico: transacoes");
        log.info("Mensagem: " + mensagem);
        log.info("Mensagem enviada com sucesso!");
    }
}
