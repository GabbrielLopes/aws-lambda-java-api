package com.gabbriellps.lambda.response;

public record FileResponse(
        String nome,
        String formato,
        Double tamanho,
        String unidadeMedidaTamanho
) {
}
