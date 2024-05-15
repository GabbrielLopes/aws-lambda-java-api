package com.gabbriellps.lambda.request;

public record FileRequest(
        String nome,
        String formato,
        Integer tamanho,
        String unidadeMedidaTamanho
) {
}
