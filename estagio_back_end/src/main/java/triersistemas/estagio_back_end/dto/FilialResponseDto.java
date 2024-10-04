package triersistemas.estagio_back_end.dto;

import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.enums.SituacaoContrato;

public record FilialResponseDto(
        Long id,
        String nomeFantasia,
        String razaoSocial,
        String cnpj,
        String telefone,
        String email,
        SituacaoContrato situacaoContrato,

        //ENDEREÇO NÃO OBRIGATORIO

        String rua,
        String numero,
        String complemento,
        String cidade,
        String estado,
        String cep
) {
    public FilialResponseDto(Filial filial) {
        this(
                filial.getId(),
                filial.getNomeFantasia(),
                filial.getRazaoSocial(),
                filial.getCnpj(),
                filial.getTelefone(),
                filial.getEmail(),
                filial.getSituacaoContrato(),

                //ENDEREÇO NÃO OBRIGATORIO

                filial.getEndereco() != null ? filial.getEndereco().getRua() : null,
                filial.getEndereco() != null ? filial.getEndereco().getNumero() : null,
                filial.getEndereco() != null ? filial.getEndereco().getComplemento() : null,
                filial.getEndereco() != null ? filial.getEndereco().getCidade() : null,
                filial.getEndereco() != null ? filial.getEndereco().getEstado() : null,
                filial.getEndereco() != null ? filial.getEndereco().getCep() : null
        );
    }
}
