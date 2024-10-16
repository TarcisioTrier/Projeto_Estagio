package triersistemas.estagio_back_end.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import triersistemas.estagio_back_end.enuns.Apresentacao;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoProduto;

import java.math.BigDecimal;

public record ProdutoRequestDto(
        @NotBlank(message = "Código de Barras é obrigatório")
        String codigoBarras,

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        String descricao,

        @NotNull(message = "Grupo Produto é obrigatório")
        Long grupoProdutoId,

        @NotNull(message = "Tipo de Produto é obrigatório")
        TipoProduto tipoProduto,

        @NotNull(message = "Apresentação é obrigatória")
        Apresentacao apresentacao,

        @DecimalMin(value = "0.0", message = "Margem de Lucro não pode ser negativa")
        BigDecimal margemLucro,

        Boolean atualizaPreco,

        @NotNull(message = "Valor Produto é obrigatório")
        @DecimalMin(value = "0.0", message = "Valor Produto não pode ser negativo")
        BigDecimal valorProduto,

        @NotNull(message = "Valor Venda é obrigatório")
        @DecimalMin(value = "0.0", message = "Valor Venda não pode ser negativo")
        BigDecimal valorVenda,

        SituacaoCadastro situacaoCadastro
) {
}
