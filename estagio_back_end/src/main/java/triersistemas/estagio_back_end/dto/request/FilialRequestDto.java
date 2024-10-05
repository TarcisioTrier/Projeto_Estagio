package triersistemas.estagio_back_end.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import triersistemas.estagio_back_end.dto.EnderecosDto;
import triersistemas.estagio_back_end.enuns.SituacaoContrato;


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

        SituacaoContrato situacaoContrato,

        @Valid
        EnderecosDto endereco
) {}
