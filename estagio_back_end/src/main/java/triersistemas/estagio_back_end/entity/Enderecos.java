package triersistemas.estagio_back_end.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import triersistemas.estagio_back_end.dto.EnderecosDto;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "Enderecos")
public class Enderecos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "CEP é obrigatório")
    @Column(name = "cep", length = 9, nullable = false)
    private String cep;

    @NotBlank(message = "Logradouro é obrigatório")
    @Column(name = "logradouro", nullable = false)
    private String logradouro;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "complemento")
    private String complemento;

    @NotBlank(message = "Bairro é obrigatório")
    @Column(name = "bairro", nullable = false)
    private String bairro;

    @NotBlank(message = "Cidade é obrigatório")
    @Column(name = "cidade", nullable = false)
    private String cidade;

    @NotBlank(message = "Estado é obrigatório")
    @Column(name = "estado", nullable = false)
    private String estado;

    @OneToOne(mappedBy = "endereco")
    private Filial filial;

    public Enderecos(EnderecosDto dto) {
        this.logradouro = dto.logradouro();
        this.numero = dto.numero();
        this.complemento = dto.complemento();
        this.cidade = dto.localidade();
        this.estado = dto.estado();
        this.cep = dto.cep();
        this.bairro = dto.bairro();
    }

    public void alterarDados(EnderecosDto dto) {
        Optional.ofNullable(dto.logradouro()).ifPresent(this::setLogradouro);
        Optional.ofNullable(dto.numero()).ifPresent(this::setNumero);
        Optional.ofNullable(dto.complemento()).ifPresent(this::setComplemento);
        Optional.ofNullable(dto.localidade()).ifPresent(this::setCidade);
        Optional.ofNullable(dto.estado()).ifPresent(this::setEstado);
        Optional.ofNullable(dto.cep()).ifPresent(this::setCep);
        Optional.ofNullable(dto.bairro()).ifPresent(this::setBairro);
    }
}