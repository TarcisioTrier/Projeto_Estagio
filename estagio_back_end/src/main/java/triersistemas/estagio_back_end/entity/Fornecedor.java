package triersistemas.estagio_back_end.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import triersistemas.estagio_back_end.dto.request.FornecedorRequestDto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Fornecedores",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"cnpj", "filial_id"})}
)
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Nome Fantasia é obrigatório")
    @Column(name = "nome_fantasia", nullable = false)
    private String nomeFantasia;

    @NotBlank(message = "Razão Social é obrigatória")
    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @NotBlank(message = "CNPJ é obrigatório")
    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @NotBlank(message = "Telefone é obrigatório")
    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "situacao_cadastro", nullable = false)
    @Enumerated(EnumType.STRING)
    private SituacaoCadastro situacaoCadastro;

    @ManyToOne
    @JoinColumn(name = "filial_id")
    private Filial filial;

    public Fornecedor(FornecedorRequestDto dto, Filial filial) {
        this.nomeFantasia = dto.nomeFantasia();
        this.razaoSocial = dto.razaoSocial();
        this.cnpj = dto.cnpj();
        this.telefone = dto.telefone();
        this.email = dto.email();
        this.situacaoCadastro = Objects.nonNull(dto.situacaoCadastro()) ? dto.situacaoCadastro() : SituacaoCadastro.ATIVO;
        this.filial = filial;
    }

    public void alterarDados(FornecedorRequestDto dto, Optional<Filial> filial) {
        Optional.ofNullable(dto.nomeFantasia()).ifPresent(this::setNomeFantasia);
        Optional.ofNullable(dto.razaoSocial()).ifPresent(this::setRazaoSocial);
        Optional.ofNullable(dto.cnpj()).ifPresent(this::setCnpj);
        Optional.ofNullable(dto.telefone()).ifPresent(this::setTelefone);
        Optional.ofNullable(dto.email()).ifPresent(this::setEmail);
        Optional.ofNullable(dto.situacaoCadastro()).ifPresent(this::setSituacaoCadastro);
        filial.ifPresent(this::setFilial);
    }

    public void alterarSituacao() {
        if (this.situacaoCadastro.equals(SituacaoCadastro.ATIVO)) {
            this.situacaoCadastro = SituacaoCadastro.INATIVO;
        } else {
            this.situacaoCadastro = SituacaoCadastro.ATIVO;
        }
    }
}
