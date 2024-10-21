package triersistemas.estagio_back_end.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
        AtualizaPrecoEnum atualizaPreco
) {
}
