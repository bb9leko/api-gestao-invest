package br.com.bb9leko.dividendmanagement.rest;

import br.com.bb9leko.dividendmanagement.model.Dividendos;
import br.com.bb9leko.dividendmanagement.repository.DividendosRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestForm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Path("/api/dividendos")
public class DividendosResource {

    private static final Logger LOG = Logger.getLogger(DividendosResource.class);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Inject
    DividendosRepository repository;

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public Response processarCSV(@RestForm InputStream file) {
        LOG.info("Endpoint /api/dividendos/upload-csv chamado");
        if (file == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Arquivo não enviado.").build();
        }

        int salvos = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8))) {
            String header = br.readLine(); // pula cabeçalho
            if (header == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("CSV vazio.").build();
            }

            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                List<String> campos = parseCsvLine(linha);
                if (campos.size() < 7) {
                    LOG.warn("Linha ignorada (menos de 7 campos): " + linha);
                    continue;
                }

                try {
                    Dividendos d = new Dividendos();
                    d.setProduto(campos.get(0).trim());
                    d.setPagamento(LocalDate.parse(campos.get(1).trim(), DATE_FMT));
                    d.setTipoEvento(campos.get(2).trim());
                    d.setInstituicao(campos.get(3).trim());
                    d.setQuantidade(parseIntSafe(campos.get(4)));
                    d.setPrecoUnitario(parseMoeda(campos.get(5)));
                    d.setValorLiquido(parseMoeda(campos.get(6)));

                    repository.persistAndFlush(d);
                    salvos++;
                    LOG.info("Dividendo persistido: " + d);
                } catch (Exception ex) {
                    LOG.error("Falha ao processar linha: " + linha, ex);
                }
            }
        } catch (Exception e) {
            LOG.error("Erro ao processar CSV", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao processar CSV: " + e.getMessage()).build();
        }

        return Response.ok("Registros processados: " + salvos).build();
    }

    private static List<String> parseCsvLine(String line) {
        List<String> campos = new ArrayList<>();
        StringBuilder campo = new StringBuilder();
        boolean emAspas = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                emAspas = !emAspas;
            } else if (c == ',' && !emAspas) {
                campos.add(campo.toString());
                campo.setLength(0);
            } else {
                campo.append(c);
            }
        }
        campos.add(campo.toString());
        return campos;
    }

    private static int parseIntSafe(String valor) {
        if (valor == null || valor.trim().equals("-")) return 0;
        String limpo = valor.replaceAll("[^0-9]", "");
        return limpo.isEmpty() ? 0 : Integer.parseInt(limpo);
    }

    private static BigDecimal parseMoeda(String valor) {
        if (valor == null || valor.trim().equals("-")) return BigDecimal.ZERO;

        String limpo = valor
                .replace("R$", "")
                .replaceAll("\\s+", "")
                .replaceAll("[^0-9,.-]", "")
                .replace(",", ".");

        // Remove pontos extras (ex: "267.56" -> "26756" -> "267.56")
        if (limpo.contains(".")) {
            String[] partes = limpo.split("\\.");
            if (partes.length > 2) {
                // Reconstrói: tudo menos última parte + "." + última parte
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < partes.length - 1; i++) {
                    sb.append(partes[i]);
                }
                sb.append(".").append(partes[partes.length - 1]);
                limpo = sb.toString();
            }
        }

        if (limpo.isEmpty() || limpo.equals(".") || limpo.equals("-")) return BigDecimal.ZERO;

        try {
            return new BigDecimal(limpo);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
