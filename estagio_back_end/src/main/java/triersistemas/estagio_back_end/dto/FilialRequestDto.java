package triersistemas.estagio_back_end.dto;

import triersistemas.estagio_back_end.enuns.SituacaoContrato;

public record FilialRequestDto(
    String nomeFantasia,
    String razaoSocial,
    String cnpj,
    String telefone,
    String email,
    SituacaoContrato situacaoContrato,
    EnderecoDto endereco
) {

    public record EnderecoDto(
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado
    ) {}
}