package triersistemas.estagio_back_end.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import triersistemas.estagio_back_end.enums.SituacaoContrato;

public record FilialRequestDto(
        @NotBlank(message = "Nome Fantasia é obrigatório")
        String nomeFantasia,

        @NotBlank(message = "Razão Social é obrigatória")
        String razaoSocial,

        @NotBlank(message = "CNPJ é obrigatório")
        String cnpj,

        @NotBlank(message = "Telefone é obrigatório")
        String telefone,

        @Email(message = "Email inválido")
        @NotBlank(message = "Email é obrigatório")
        String email,

        @NotNull(message = "Situação do Contrato é obrigatória")
        SituacaoContrato situacaoContrato,

        String rua,
        String numero,
        String complemento,
        String cidade,
        String estado,
        String cep
) {}
