package triersistemas.estagio_back_end.dto;

import jakarta.validation.constraints.NotBlank;

public record EnderecosDto(

        @NotBlank(message = "Logradouro é obrigatório")
        String logradouro,

        Integer numero,

        String complemento,

        @NotBlank(message = "Cidade é obrigatório")
        String localidade,

        @NotBlank(message = "Estado é obrigatório")
        String estado,

        @NotBlank(message = "CEP é obrigatório")
        String cep,

        @NotBlank(message = "Bairro é obrigatório")
        String bairro
) {}
