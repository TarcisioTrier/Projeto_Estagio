package triersistemas.estagio_back_end.dto;

import triersistemas.estagio_back_end.dto.request.GrupoProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.ProdutoPagedRequestDto;
import triersistemas.estagio_back_end.enuns.AtualizaPrecoEnum;

import java.math.BigDecimal;
import java.util.List;

public record AtualizaPrecoDto(
        List<Long> produtoId,
        List<Long> grupoProdutoId,
        Long filialId,
        Boolean all,
        Boolean isProduto,
        Boolean isRelativo,
        BigDecimal valor,
        Boolean isPercentual,
        AtualizaPrecoEnum atualizaPreco,
        ProdutoPagedRequestDto produtoFilter,
        GrupoProdutoPagedRequestDto grupoProdutoFilter
) {
}
