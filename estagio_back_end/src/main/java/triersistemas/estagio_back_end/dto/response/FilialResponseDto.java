package triersistemas.estagio_back_end.dto.response;

import triersistemas.estagio_back_end.dto.EnderecosDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.enuns.SituacaoContrato;

public record FilialResponseDto(
        Long id,
        String nomeFantasia,
        String razaoSocial,
        String cnpj,
        String telefone,
        String email,
        SituacaoContrato situacaoContrato,
        EnderecosDto endereco
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
                filial.getEndereco() == null ? null :
                        new EnderecosDto(
                                filial.getEndereco().getLogradouro(),
                                filial.getEndereco().getNumero(),
                                filial.getEndereco().getComplemento(),
                                filial.getEndereco().getCidade(),
                                filial.getEndereco().getEstado(),
                                filial.getEndereco().getCep(),
                                filial.getEndereco().getBairro())
        );
    }
}
