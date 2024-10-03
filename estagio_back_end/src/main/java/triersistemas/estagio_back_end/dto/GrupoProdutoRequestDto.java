package triersistemas.estagio_back_end.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import triersistemas.estagio_back_end.enums.SituacaoCadastro;
import triersistemas.estagio_back_end.enums.TipoGrupoProduto;

import java.math.BigDecimal;

public record GrupoProdutoRequestDto(
        @NotBlank(message = "Nome do grupo é obrigatório")
        String nomeGrupo,

        @NotNull(message = "Tipo de grupo é obrigatório")
        TipoGrupoProduto tipoGrupo,

        @NotNull(message = "Margem de lucro é obrigatória")
        BigDecimal margemLucro,

        Boolean atualizaPreco,

        @NotNull(message = "Situação do cadastro é obrigatória")
        SituacaoCadastro situacaoCadastro
) {}
