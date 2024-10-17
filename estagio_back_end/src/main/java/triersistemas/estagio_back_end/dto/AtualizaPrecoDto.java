package triersistemas.estagio_back_end.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record AtualizaPrecoDto(
        List<Long> produtoId,
        List<Long> grupoProdutoId,

        @DecimalMin(value = "0.0", message = "Valor Venda não pode ser negativo")
        @NotNull(message = "O valor de atualização da margem de lucro é obrigatório")
        BigDecimal margemLucroAtualizada
) {
}
