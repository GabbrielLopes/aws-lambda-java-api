package com.gabbriellps.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabbriellps.lambda.request.FileRequest;
import com.gabbriellps.lambda.response.FileResponse;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Handler implements
        RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static ObjectMapper objectMapper;
    private static List<String> carsBombastic;

    static {
        objectMapper = new ObjectMapper();

        carsBombastic = new ArrayList<>();
        carsBombastic.add("marea");
        carsBombastic.add("turbo");
        carsBombastic.add("peugeot");

    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
                                                      Context context) {
        var logger = context.getLogger();

        try {
            String requestStr = request.getBody();
            logger.log("Request received - " + requestStr);

            logger.log("Iniciando conversão do Json para Objeto...");
            FileRequest fileRequest = objectMapper.readValue(requestStr, FileRequest.class);
            logger.log("Conversão realizada com sucesso!");


            logger.log("Iniciando compressão de arquivo...");
            Double tamanhoComprimido = compressFile(fileRequest);
            logger.log("Arquivo comprimido com sucesso!");

            FileResponse response = new FileResponse(fileRequest.nome(), fileRequest.formato(),
                    tamanhoComprimido, fileRequest.unidadeMedidaTamanho());

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(objectMapper.writeValueAsString(response))
                    .withIsBase64Encoded(false);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static double compressFile(FileRequest fileRequest) {
        // Simula comprimir 20% do tamanho do arquivo
        return fileRequest.tamanho() - (fileRequest.tamanho() * 0.2);
    }

}
